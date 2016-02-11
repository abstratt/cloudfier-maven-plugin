package com.abstratt.kirra.mojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This mojo can generate code from a previously deployed Cloudfier project.
 */
@Mojo(name="generate", requiresProject=false, requiresOnline=true, threadSafe=true, defaultPhase=LifecyclePhase.GENERATE_SOURCES)
public class GeneratorMojo extends AbstractCloudfierMojo {
    /**
     * Whether files should be generated even in case they already exist in the target path.
     */
    @Parameter(property="kirra.generator.override", defaultValue="false")
    public Boolean override;
    
    /**
     * The target platform to generate for. 
     */
    @Parameter(property="kirra.target.platform", defaultValue="jse")
    public String targetPlatform;
    
    /**
     * The local directory where to generate code into.
     */
    @Parameter(property="kirra.target.dir", defaultValue="${user.dir}")
    public String targetDir;

    @Override
    public void execute() throws MojoExecutionException {
        String generationUri = serverBaseUri + "/generator/" + getOneTimeProjectSlug() + "/platform/" + targetPlatform;
        this.getLog().debug("Generation URI: " + generationUri);
        GetMethod generateRequest = new GetMethod(generationUri);
        generateRequest.setRequestHeader("Content-Type", "application/zip");
        byte[] body;
        this.getLog().debug("Generation destination: " + targetDir);
        new File(targetDir).mkdirs();
        File output = null;
        try {
        	HttpClient client = buildClient();
            int response = client.executeMethod(generateRequest);
            if (response != 200)
                throw new MojoExecutionException("Unexpected status: " + response);
            body = generateRequest.getResponseBody();
            output = File.createTempFile("test", null);
            FileUtils.writeByteArrayToFile(output, body);
            ZipFile zipFile = new ZipFile(output);
            try {
                Enumeration<? extends ZipEntry> e = zipFile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    InputStream entryContents = zipFile.getInputStream(entry);
                    OutputStream destinationContents = null;
                    try {
                        File destination = new File(targetDir, entry.getName());
                        if (destination.exists() && !override)
                            throw new MojoExecutionException("File or directory already exists at: " + destination + " and overriding was not enabled");
                        getLog().info("Generating at " + destination);
                        destinationContents = FileUtils.openOutputStream(destination);
                        IOUtils.copy(entryContents, destinationContents);
                    } finally {
                        IOUtils.closeQuietly(destinationContents);
                        IOUtils.closeQuietly(entryContents);
                    }
                }
            } finally {
                zipFile.close();
            }
        } catch (IOException e1) {
            getLog().error(e1);
        } finally {
            FileUtils.deleteQuietly(output);
        }
    }
}