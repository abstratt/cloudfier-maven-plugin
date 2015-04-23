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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This mojo can generate code from a public Cloudfier project.
 */
@Mojo(name="generate", requiresProject=false, requiresOnline=true, threadSafe=true, defaultPhase=LifecyclePhase.GENERATE_SOURCES)
public class GeneratorMojo extends AbstractMojo {

    /**
     * The URI of the Cloudfier instance. 
     */
    @Parameter(property="kirra.uri", defaultValue="http://develop.cloudfier.com")
    public String serverBaseUri;
    
    /**
     * The name of the project to generate from. 
     */
    @Parameter(property="kirra.project.name", defaultValue="timetracker")
    public String projectName;
    
    /**
     * The owner of the project to generate from. 
     */
    @Parameter(property="kirra.project.owner", defaultValue="test")
    public String projectOwner;
    
    @Parameter(property="kirra.generator.override", defaultValue="false")
    public Boolean override;
    
    /**
     * The base path of the project to generate from. 
     */
    @Parameter(property="kirra.project.basepath", defaultValue="cloudfier-examples")
    public String projectBase;
    
    /**
     * The target platform to generate for. 
     */
    @Parameter(property="kirra.target.platform", defaultValue="jse")
    public String targetPlatform;
    
    /**
     * The local directory where to generate code into
     */
    @Parameter(property="kirra.target.root", defaultValue="${user.dir}")
    public String targetRoot;

    @Override
    public void execute() throws MojoExecutionException {
        System.out.println("PLATFORM: " + targetPlatform);
        String projectBaseUri = serverBaseUri + "/services/generator/" + projectOwner + "-" + projectBase.replace('/', '-') + "-" + projectName;
        String generationUri = projectBaseUri + "/platform/" + targetPlatform;
        this.getLog().debug("Generation URI: " + generationUri);
        GetMethod generateRequest = new GetMethod(generationUri);
        generateRequest.setRequestHeader("Content-Type", "application/zip");
        HttpClient client = new HttpClient();
        byte[] body;
        this.getLog().debug("Generation destination: " + targetRoot);
        new File(targetRoot).mkdirs();
        File output = null;
        try {
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
                        File destination = new File(targetRoot, entry.getName());
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