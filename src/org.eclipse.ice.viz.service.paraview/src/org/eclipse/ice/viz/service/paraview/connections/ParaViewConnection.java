/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.connections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.connections.VizConnection;
import org.eclipse.ice.viz.service.paraview.web.HttpParaViewWebClient;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Provides an {@link IVizConnection} for connecting to
 * {@link IParaViewWebClient}s. This connection specifically uses the
 * {@link HttpParaViewWebClient} implementation for the web client.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewConnection extends VizConnection<IParaViewWebClient> {

	/*
	 * Implements an abstract method from VizConnection.
	 */
	@Override
	protected IParaViewWebClient connectToWidget() {
		// Set the default return value.
		IParaViewWebClient client = null;

		// Try to create and connect to a ParaView web client.
		boolean connected = false;
		try {
			// Create an HTTP implementation of the ParaView web client..
			client = new HttpParaViewWebClient();
			// Set up the HTTP URL
			String host = getHost();
			String port = Integer.toString(getPort());
			String url = "http://" + host + ":" + port + "/rpc-http/";
			// Try to connect.
			connected = client.connect(url).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// If the connection was not successful, we should return null.
		if (!connected) {
			client = null;
		}

		return client;
	}

	/*
	 * Implements an abstract method from VizConnection.
	 */
	@Override
	protected boolean disconnectFromWidget(IParaViewWebClient widget) {
		boolean closed = false;
		// Attempt to disconnect, returning the success of the operation.
		if (widget != null) {
			try {
				closed = widget.disconnect().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return closed;
	}

	/**
	 * Gets the ParaView web client's proxy object with the specified ID.
	 * 
	 * @param id
	 *            The ID of the proxy object to get (for instance, a view,
	 *            representation, or file ID).
	 * @return The corresponding proxy JsonObject, or {@code null} if an error
	 *         was encountered.
	 */
	public JsonObject getProxyObject(int id) {
		JsonArray args;
		JsonObject object = null;

		args = new JsonArray();
		args.add(new JsonPrimitive(id));

		try {
			object = getWidget().call("pv.proxy.manager.get", args).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return object;
	}
	
}
