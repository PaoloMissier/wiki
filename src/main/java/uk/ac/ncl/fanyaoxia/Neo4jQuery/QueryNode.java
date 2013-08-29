package uk.ac.ncl.fanyaoxia.Neo4jQuery;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import uk.ac.ncl.fanyaoxia.ServerRoot.ServerRoot;


public class QueryNode extends ServerRoot{
	
	public static void checkDatabaseIsRunning(){
		WebResource resource = Client.create().resource(SERVERROOT);
		ClientResponse response = resource.get(ClientResponse.class);
		
		System.out.println(String.format("GET on [%s], status code [%d]",SERVERROOT, response.getStatus()));
		response.close();
	}
	
	public String query(String type, String indexName,
			String key, String value) {
		indexName = indexName.replaceAll(" ", "%20");
		/*
		value = value.replaceAll(" ", "%20");
		value = value.replaceAll("\\\\", "%5C");
		value = value.replaceAll("\"", "%22");
		value = value.replaceAll("=", "%3D");
		value = value.replaceAll("[+]", "%2B");
		value = value.replaceAll("\\^", "%5E");
		*/
		final String queryNodeUri = SERVERROOT + "index/" + type + "/" + indexName
				+ "/" + key + "/" + value + "";

		WebResource resource = Client.create().resource(queryNodeUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		String nodeOrRelationshipUri = null;
		try {
			JSONArray test = new JSONArray(response.getEntity(String.class));
			JSONObject json = test.getJSONObject(0);
			nodeOrRelationshipUri = json.getString("self");

		} catch (Exception e) {
			//System.err.println(e.getMessage());
		}
		response.close();
		return nodeOrRelationshipUri;
	}
}
