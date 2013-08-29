package uk.ac.ncl.fanyaoxia.Neo4jQuery;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import uk.ac.ncl.fanyaoxia.ServerRoot.ServerRoot;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class CreateIndex extends ServerRoot{
	private static boolean checkIndexCreation=true;
	public void createIndex() {
		createIndexes("node", "articleNodeIndex");
		createIndexes("node", "editedNodeIndex");
		createIndexes("node", "userNodeIndex");
		createIndexes("relationship","wasGeneratedBy");
		createIndexes("relationship","wasAssociatedWith");
		createIndexes("relationship","wasRevisionOf");
	}

	public void createIndexes(String indexType, String indexName) {
		WebResource resource = Client.create().resource(SERVERROOT + "index/" + indexType + "/");		
		String indexJson = "{ \"name\" : \"" + indexName + "\"}";
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(indexJson)
				.post(ClientResponse.class);
		response.close();	
		System.out.println(indexJson);
	}
	public static void indexCreated(){
		checkIndexCreation=false;
	}
	public static boolean getIndexCreation(){
		return checkIndexCreation;
	}
}
