/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *    
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.connections;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.connections.ConnectionAdapter;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.ice.viz.service.paraview.web.HttpParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * This class provides a {@link ConnectionAdapter} that wraps a
 * {@link IParaViewWebClient}. It handles connecting and disconnecting as well as
 * updating the required connection properties.
 * 
 * @author Jordan Deyton
 *
 */
@Deprecated
public class ParaViewConnectionAdapter extends ConnectionAdapter<IParaViewWebClient> {

	// TODO Theoretically, one connection could support multiple views.
	// These will need to be managed somewhere.
	// /**
	// * A set containing the view IDs for all views created on this connection.
	// */
	// private final Set<Integer> viewIds = new HashSet<Integer>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionAdapter#openConnection
	 * ()
	 */
	@Override
	protected IParaViewWebClient openConnection() {

		// Set the default return value.
		IParaViewWebClient client = null;

		// Try to create and connect to a ParaView web client.
		boolean connected = false;
		try {
			// Create an HTTP implementation of the ParaView web client..
			client = new HttpParaViewWebClient();
			// Get the host and port from the connection properties.
			String host = getConnectionProperty("host");
			String port = getConnectionProperty("port");
			// Set up the HTTP URL.
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
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionAdapter#closeConnection
	 * (java.lang.Object)
	 */
	@Override
	protected boolean closeConnection(IParaViewWebClient connection) {
		boolean closed = false;
		// To close the connection, we need only tell it to close.
		if (connection != null) {
			connection.disconnect();
			closed = true;
		}
		return closed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionAdapter#
	 * setConnectionProperties(java.util.List)
	 */
	@Override
	public boolean setConnectionProperties(List<Entry> row) {
		// We need to get the following information:
		// host
		// port

		// TODO Update this when we can launch the python server.

		boolean changed = false;

		if (row != null && row.size() >= 3) {
			// Update the name of the connection.
			objectName = row.get(0).getValue();

			// Update the required properties. If a property change requires the
			// connection to be reset, set the changed flag to true.
			changed |= setConnectionProperty("host", row.get(1).getValue());
			changed |= setConnectionProperty("port", row.get(2).getValue());
		}

		return changed;
	}

	/**
	 * Determines the relative path for the full path with respect to the client
	 * connection's data directory.
	 * 
	 * @param fullPath
	 *            The full path to the file.
	 * @return The relative path, or null if it could not be determined.
	 */
	public String findRelativePath(String fullPath) {
		String relativePath = null;

		// TODO Move responsibility for this to the python code.

		IParaViewWebClient client = getConnection();
		JsonArray args = new JsonArray();
		JsonObject object;

		args.add(new JsonPrimitive("."));
		try {
			object = client.call("file.server.directory.list", args).get();
			if (object != null) {
				String directory = object.get("path").getAsJsonArray().get(0)
						.getAsString();
				System.out.println("The directory is: " + directory);

				// If the path is indeed a full path, we need to determine its
				// relative path.
				if (fullPath.startsWith("/")) {
					// Determine the path to the base directory.
					relativePath = "";
					String[] split = directory.split("/");
					for (int i = 0; i < split.length; i++) {
						if (!split[i].trim().isEmpty()) {
							relativePath += "../";
						}
					}
					// Add in the rest of the full path, excluding the initial
					// forward slash.
					if (fullPath.length() > 1) {
						relativePath += fullPath.substring(1);
					}
				} else {
					relativePath = fullPath;
				}
			}
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}

		return relativePath;
	}
}
