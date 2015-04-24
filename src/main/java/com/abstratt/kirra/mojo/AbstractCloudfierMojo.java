package com.abstratt.kirra.mojo;

import java.util.UUID;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractCloudfierMojo extends AbstractMojo {
    
    public static String uniqueVmId = UUID.randomUUID().toString(); 
    
    /**
     * The URI of the Cloudfier instance. 
     */
    @Parameter(property="kirra.uri", defaultValue="http://develop.cloudfier.com/services")
    public String serverBaseUri;
    
    /**
     * The name of the project to publish into. 
     */
    @Parameter(property="kirra.project.slug", defaultValue="${project.groupId}-${project.artifactId}-${project.version}")
    public String projectSlug;

    public String getOneTimeProjectSlug() {
        return "@temp-"+ projectSlug + "-" + uniqueVmId;
    }
}
