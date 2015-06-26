/*******************************************************************************
* Copyright (c) 2011, 2014 UT-Battelle, LLC.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Initial API and implementation and/or initial documentation - Jay Jay Billings,
*   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
*   Claire Saunders, Matthew Wang, Anna Wojtowicz
*******************************************************************************/
package org.eclipse.ice.core.jaxrs.test;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;

/**
 * This is a simple test class to check the JAXRS interface bindings in ICE.
 * 
 * @author Jay Jay Billings
 * 
 */
public class JAXRSTester {

	/**
	 * This operation checks posting updates to the server.
	 */
	@Test
	public void checkPostingUpdates() {

		// Create the json message
		String message = "post={\"item_id\":\"5\", "
				+ "\"client_key\":\"1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ\", "
				+ "\"posts\":[{\"type\":\"UPDATER_STARTED\",\"message\":\"\"},"
				+ "{\"type\":\"FILE_MODIFIED\","
				+ "\"message\":\"/tmp/file\"}]}";

		// Create the client
		Client jaxRSClient = Client.create();
		// Create the filter that will let us login with basic HTTP
		// authentication.
		final HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter("ice",
				"veryice");
		jaxRSClient.addFilter(authFilter);
		// Add a filter for logging. This filter will automatically dump stuff
		// to stdout.
		jaxRSClient.addFilter(new LoggingFilter());

		// Get the handle to the server as a web resource
		WebResource webResource = jaxRSClient
				.resource("http://localhost:8080/ice");

		// Get the normal response from the server to make sure we can connect
		// to it
		webResource.accept(MediaType.TEXT_PLAIN).header("X-FOO", "BAR")
				.get(String.class);

		// Try posting something to the update page
		webResource.path("update").type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(String.class, message);

		return;
	}

}
