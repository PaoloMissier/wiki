package uk.ac.ncl.fanyaoxia.Neo4jQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import uk.ac.ncl.fanyaoxia.ServerRoot.ServerRoot;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AddNodeOrRelationToIndex extends ServerRoot {

	/**
	 * 
	 */
	public URI createNode() {
		final String nodeEntryPointUri = SERVERROOT + "node";
		WebResource resource = Client.create().resource(nodeEntryPointUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity("{}")
				.post(ClientResponse.class);

		final URI location = response.getLocation();
		System.out.println(String.format(
				"POST to [%s], status code [%d], location header [%s]",
				nodeEntryPointUri, response.getStatus(), location.toString()));
		response.close();
		return location;
	}

	/**
	 * 
	 */
	public void addProperty(URI nodeUri, String propertyName,
			String propertyValue) {
		String propertyUri = nodeUri.toString() + "/properties/" + propertyName;
		WebResource resource = Client.create().resource(propertyUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.entity("\"" + propertyValue + "\"").put(ClientResponse.class);
		response.close();
	}

	/**
	 * 
	 */
	public URI addRelationship(URI startNode, URI endNode,
			String relationshipType, String jsonAttributes)
			throws URISyntaxException {
		URI fromUri = new URI(startNode.toString() + "/relationships");
		String relationshipJson = generateJsonRelationship(endNode,
				relationshipType, jsonAttributes);

		WebResource resource = Client.create().resource(fromUri);
		// POST JSON to the relationships URI
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(relationshipJson)
				.post(ClientResponse.class);

		final URI location = response.getLocation();
		System.out.println(String.format(
				"POST to [%s], status code [%d], location header [%s]",
				fromUri, response.getStatus(), location.toString()));

		response.close();
		return location;
	}

	/**
	 * 
	 */
	public String generateJsonRelationship(URI endNode,
			String relationshipType, String... jsonAttributes) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"to\" : \"");
		sb.append(endNode.toString());
		sb.append("\", ");
		sb.append("\"type\" : \"");
		sb.append(relationshipType);
		if (jsonAttributes == null || jsonAttributes.length < 1) {
			sb.append("\"");
		} else {
			sb.append("\", \"data\" : ");
			for (int i = 0; i < jsonAttributes.length; i++) {
				sb.append(jsonAttributes[i]);
				if (i < jsonAttributes.length - 1) {
					sb.append(", ");
				}
			}
		}
		sb.append(" }");
		return sb.toString();
	}

	/*
	public void addMetadataToProperty(URI relationshipUri, Map<String,String>property) throws URISyntaxException {
		URI propertyUri = new URI(relationshipUri.toString() + "/properties");
		String entity = toJsonNameValuePairCollection(property);
		WebResource resource = Client.create().resource(propertyUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(entity)
				.put(ClientResponse.class);

		System.out.println(String.format("PUT [%s] to [%s], status code [%d]",
				entity, propertyUri, response.getStatus()));
		response.close();
	}

	public String toJsonNameValuePairCollection(Map<String, String> property) {

		Set<String> keys = property.keySet();
		Iterator<String> iter = keys.iterator();
		String outPut = "{ ";
		while (iter.hasNext()) {
			String key = iter.next();
			String value = property.get(key);

			outPut += "\"" + key + "\" : \"" + value + "\"";
			if (iter.hasNext()) {
				outPut += ", ";
			}
		}
		outPut += "}";
		return outPut;
	}
	*/
	/**
	 * 
	 */
	public void addNodeOrRelationshipToIndex(String type, String indexName,
			String key, String value, String nodeUri) {
		indexName = indexName.replaceAll(" ", "%20");
		value = value.replaceAll("\\\\", "\\\\\\\\");
		value = value.replaceAll("\"", "\\\\\"");
		final String indexNodeUri = SERVERROOT + "index/" + type + "/" + indexName + "";
		WebResource resource = Client.create().resource(indexNodeUri);
		String indexNodeJson = "{\"key\" : \"" + key + "\", \"value\" : \"" + value + "\", \"uri\" : \"" + nodeUri + "\" }";

		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(indexNodeJson)
				.post(ClientResponse.class);
		response.close();
	}
}
