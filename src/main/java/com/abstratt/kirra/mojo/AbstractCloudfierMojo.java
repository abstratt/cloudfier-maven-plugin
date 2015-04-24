package com.abstratt.kirra.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractCloudfierMojo extends AbstractMojo {
    /**
     * The URI of the Cloudfier instance. 
     */
    @Parameter(property="kirra.uri", defaultValue="http://develop.cloudfier.com")
    public String serverBaseUri;
    
    /**
     * The name of the project to publish into. 
     */
    @Parameter(property="kirra.project.slug", defaultValue="${project.groupId}-${project.artifactId}-${project.version}")
    public String projectSlug;

}
