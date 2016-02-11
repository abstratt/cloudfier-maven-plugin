package com.abstratt.kirra.mojo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
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
    

	protected HttpClient buildClient() throws UnknownHostException {
		HttpClient client = new HttpClient();
        HostConfiguration hostConfiguration = new HostConfiguration();
        hostConfiguration.setLocalAddress(InetAddress.getLocalHost());
		client.setHostConfiguration(hostConfiguration);
		return client;
	}
}
