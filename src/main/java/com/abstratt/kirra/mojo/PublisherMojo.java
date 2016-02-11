package com.abstratt.kirra.mojo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This mojo can publish a project into a Cloudfier server.
 */
@Mojo(name="publish", requiresProject=false, requiresOnline=true, threadSafe=true, defaultPhase=LifecyclePhase.COMPILE)
public class PublisherMojo extends AbstractCloudfierMojo {

    /**
     * The project source dir. 
     */
    @Parameter(property="kirra.project.sourcedir", defaultValue="${basedir}/src/main/textuml/")
    public String projectSourceDir;
    
    @Override
    public void execute() throws MojoExecutionException {
        String projectBaseUri = serverBaseUri + "/publisher/" + getOneTimeProjectSlug() + "/";
        File output = null;
        HttpMethod uploadRequest = null;
        try {
        	HttpClient client = buildClient();
            File sourceRootDir = new File(projectSourceDir);
            if (!sourceRootDir.isDirectory())
                throw new MojoExecutionException("No source directory at: "+ sourceRootDir);
            getLog().debug("Source root directory: " + sourceRootDir);
            Path sourceRootPath = sourceRootDir.getAbsoluteFile().toPath();
            List<String> toMatch = Arrays.asList(".tuml", "mdd.properties", "data.json");
            Stream<Path> sourceFiles = Files.find(sourceRootPath, Integer.MAX_VALUE, (path, attributes) -> toMatch.stream().anyMatch(suffix -> path.getFileName().toString().endsWith(suffix)));
            Map<Path, byte[]> sources = sourceFiles.collect(Collectors.toMap(
                p -> sourceRootPath.relativize(p), 
                p -> readPath(p)
            ));
            sources.keySet().forEach(path -> getLog().info("Publishing " + path));
            sourceFiles.close();
            uploadRequest = buildMultipartRequest(URI.create(projectBaseUri), sources);
            getLog().debug("Publishing project at: " + projectBaseUri);
            client.executeMethod(uploadRequest);
            String responseText = uploadRequest.getResponseBodyAsString();
            getLog().debug(responseText);
        } catch (IOException e1) {
            getLog().error(e1);
        } finally {
            if (uploadRequest != null)
                uploadRequest.releaseConnection();
            FileUtils.deleteQuietly(output);
        }
    }

    
    private byte[] readPath(Path p) {
        try {
            return Files.readAllBytes(p);
        } catch (IOException e) {
            getLog().error(e);
            throw new RuntimeException("Error reading file at " + p);
        }
    }

    public HttpMethod buildMultipartRequest(URI location, Map<Path, byte[]> toUpload) throws HttpException, IOException {
        PostMethod filePost = new PostMethod(location.toASCIIString());
        List<Part> parts = new ArrayList<Part>(toUpload.size());
        for (Entry<Path, byte[]> entry : toUpload.entrySet())
            parts.add(new FilePart(entry.getKey().toString(), new ByteArrayPartSource(entry.getKey().toString(), entry.getValue())));
        filePost.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[0]), filePost.getParams()));
        return filePost;
    }
}