package com.abstratt.kirra.mojo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * This mojo can test a project that is currently deployed on a Cloudfier server.
 */
@Mojo(name="test", requiresProject=false, requiresOnline=true, threadSafe=true, defaultPhase=LifecyclePhase.TEST)
public class TesterMojo extends AbstractCloudfierMojo {
    @Override
    public void execute() throws MojoExecutionException {
        String dataUri = serverBaseUri + "/api/" + getOneTimeProjectSlug() + "/data";
        String testUri = serverBaseUri + "/api/" + getOneTimeProjectSlug() + "/tests";
        HttpClient client = new HttpClient();
        PostMethod dataRequest = new PostMethod(dataUri);
        PostMethod testRequest = new PostMethod(testUri);
        String responseBody = "";
        try {
            getLog().debug("Redeploying database at " + dataUri);
            int response = client.executeMethod(dataRequest);
            if (response != 200)
                throw new MojoExecutionException("Unexpected status deploying the database: " + response + "\n" + dataRequest.getResponseBodyAsString());
            getLog().debug("Running tests at " + testUri);
            response = client.executeMethod(testRequest);
            if (response != 200)
                throw new MojoExecutionException("Unexpected status running tests: " + response + "\n" + testRequest.getResponseBodyAsString());
            responseBody = testRequest.getResponseBodyAsString();
        } catch (IOException e) {
            getLog().error(e);
            return;
        }
        try {
            JsonNode parsed = parse(new StringReader(responseBody));
            JsonNode stats = parsed.get("stats");
            long failed = stats.get("failed").asLong();
            long total = stats.get("total").asLong();
            getLog().debug("Total tests: " + total);
            if (failed > 0) {
                ArrayNode resultsArray = ((ArrayNode) parsed.get("results"));
                resultsArray.elements().forEachRemaining(
                    result -> { 
                        if (result.get("testStatus").asText().equals("Fail")) {
                            getLog().error("Test failed: " + result.get("testClassName").asText() + "." + result.get("testCaseName").asText() + " - " + result.get("testMessage").asText());
                        }
                    }   
                );
                throw new MojoExecutionException("Tests failed");
            }
            getLog().debug("All tests passed");
        } catch (JsonProcessingException e) {
            throw new MojoExecutionException("Error parsing results", e);
        } catch (IOException e) {
            throw new MojoExecutionException("Error parsing results", e);
        }
    }
    
    public static <T extends JsonNode> T parse(Reader contents) throws IOException, JsonParseException, JsonProcessingException {
        JsonParser parser = new JsonFactory().createJsonParser(contents);
        parser.setCodec(new ObjectMapper());
        return (T) parser.readValueAsTree();
    }

}