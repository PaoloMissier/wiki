package uk.ac.ncl.fanyaoxia.Neo4jQuery;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.jdom2.JDOMException;

import uk.ac.ncl.fanyaoxia.ServerRoot.ServerRoot;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class Delete extends ServerRoot {

	/**
	 * 
	 */
	public void delete(String deleteIndexUri) {
		deleteIndexUri = SERVERROOT + deleteIndexUri;
		WebResource resource = Client.create().resource(deleteIndexUri);
		resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

	}

	/**
	 * 
	 */
	public void deleteAllIndex() {
		String deleteArticleNodeIndexUri = "index/node/articleNodeIndex";
		delete(deleteArticleNodeIndexUri);

		String deleteActivityNodeIndexUri = "index/node/editedNodeIndex";
		delete(deleteActivityNodeIndexUri);

		String deleteUserNodeIndexUri = "index/node/userNodeIndex";
		delete(deleteUserNodeIndexUri);

		String deleteWasRevisionOfIndexUri = "index/relationship/wasRevisionOf";
		delete(deleteWasRevisionOfIndexUri);

		String deleteWasGeneratedByIndexUri = "index/relationship/wasGeneratedBy";
		delete(deleteWasGeneratedByIndexUri);

		String deleteWasAssociatedWithIndexUri = "index/relationship/wasAssociatedWith";
		delete(deleteWasAssociatedWithIndexUri);

		String deleteUsedIndexUri = "index/relationship/used";
		delete(deleteUsedIndexUri);
	}

	/**
	 * 
	 */
	public String deleteAllNodeOrRelation() throws ClientHandlerException,
			UniformInterfaceException, JDOMException {
		String cypherPayload = "{\"query\": \"START a=node(*) MATCH a-[r?]-() DELETE a,r RETURN a\", \"params\":{}}";
		String user_name = getUserName(cypherPayload);
		return user_name;
	}

	/**
	 * 
	 */
	public void getGeneralDeleteResponse(String cypherUri) {
		WebResource resource = Client.create().resource(cypherUri);
		resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
	}

	/**
	 * 
	 */
	private String getUserName(String cypherPayload)
			throws ClientHandlerException, UniformInterfaceException,
			JDOMException {
		String user_name = null;
		ClientResponse response = getGeneralPostResponse(cypherPayload);
		try {
			JSONObject json = new JSONObject(response.getEntity(String.class));
			if (json.has("data")) {
				JSONArray getData = json.getJSONArray("data");
				if (!getData.isNull(0)) {
					JSONArray test = getData.getJSONArray(0);
					JSONObject getDataObject = test.getJSONObject(0);
					JSONObject getNodeData = getDataObject
							.getJSONObject("data");
					user_name = getNodeData.getString("user_name");
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		response.close();
		return user_name;
	}

	/**
	 * 
	 */
	public static ClientResponse getGeneralPostResponse(String cypherPayload) {
		final String cypherUri = SERVERROOT + "cypher";
		WebResource resource = Client.create().resource(cypherUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(cypherPayload)
				.post(ClientResponse.class);
		return response;
	}
}
