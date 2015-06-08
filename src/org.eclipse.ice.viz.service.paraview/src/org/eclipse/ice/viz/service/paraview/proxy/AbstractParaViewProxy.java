/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.proxy;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.kitware.vtk.web.VtkWebClient;

/**
 * This class provides a basic implementation of {@link IParaViewProxy} and
 * should be used whenever possible when dealing with the ParaView Java client.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractParaViewProxy implements IParaViewProxy {

	/**
	 * The URI for this proxy. This should only ever be set once.
	 */
	private final URI uri;

	/**
	 * The ParaView ID pointing to the file's proxy on the server.
	 */
	private int fileId = -1;
	/**
	 * The ParaView ID pointing to the file's associated render view proxy on
	 * the server.
	 */
	private int viewId = -1;
	/**
	 * The ParaView ID pointing to the file's associated representation proxy on
	 * the server.
	 */
	private int repId = -1;

	/**
	 * The default constructor. This should only be called by sub-class
	 * constructors.
	 * 
	 * @param uri
	 *            The URI for the ParaView-supported file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	protected AbstractParaViewProxy(URI uri) throws NullPointerException {
		// Throw an exception if the argument is null.
		if (uri == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a null URI.");
		}

		// Set the reference to the URI.
		this.uri = uri;

		return;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean open(ParaViewConnectionAdapter connection)
			throws NullPointerException {
		// Throw an exception if the argument is null.
		if (connection == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a proxy with a null connection.");
		}

		// Set the default return value.
		boolean opened = false;

		// Only attempt to open the file if the connection is established.
		if (connection.getState() == ConnectionState.Connected) {
			// Validate that the connection server and the URI point to the same
			// server.

			// Get the connection host.
			String clientHost = connection.getHost();

			// Get the URI host.
			String fileHost = uri.getHost();
			if (fileHost == null) {
				fileHost = "localhost";
			}

			// TODO We need better validation of hostnames, local vs remote,
			// FQDN vs IP, etc.

			// If they match, attempt to open the file.
			if (clientHost != null && clientHost.equals(fileHost)) {
				opened = openImpl(connection.getConnection(), uri.getPath());
			}
		}

		return opened;
	}

	/**
	 * Opens the file at the specified path using the ParaView web client. When
	 * called, the full path is expected to be a path on the client's specified
	 * host.
	 * <p>
	 * This method should set {@link #fileId}, {@link #viewId}, and
	 * {@link #repId}.
	 * </p>
	 * 
	 * @param client
	 *            The connection client used to open the file.
	 * @param fullPath
	 *            The full path on the client machine to the file that will be
	 *            opened.
	 * @return True if the file at the specified path on the client machine
	 *         could be opened, false otherwise.
	 */
	private boolean openImpl(VtkWebClient client, String fullPath) {
		boolean opened = false;

		// The argument array must contain the full path to the file.
		JsonArray args = new JsonArray();
		args.add(new JsonPrimitive(fullPath));

		// Attempt to create a view on the ParaView server.
		try {
			JsonObject response = client.call("createView", args).get();

			// If the response does not contain an "error" value, then try to
			// determine the file, view, and representation IDs.
			if (response.get("error") == null) {
				// The file was likely opened successfully.
				opened = true;
				// Try to get the IDs from the response.
				try {
					fileId = response.get("proxyId").getAsInt();
					viewId = response.get("viewId").getAsInt();
					repId = response.get("repId").getAsInt();
				} catch (NullPointerException | ClassCastException
						| IllegalStateException e) {
					System.err.println("ParaViewProxy error: "
							+ "Could not retrieve file, view, and/or "
							+ "representation ID from connection.");
					opened = false;
				}
			}
			// Otherwise, if the response contains an "error" value, print out
			// the reason for the error.
			else {
				// Try to determine the reason for the failure.
				String reason;
				try {
					reason = response.get("error").getAsString();
				} catch (NullPointerException | ClassCastException
						| IllegalStateException e) {
					reason = "Unknown due to malformed response.";
				}
				// Print out the reason for the failure.
				System.err.println("ParaViewProxy error: "
						+ "Failed to open \"" + fullPath + "\". Reason: "
						+ reason);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.err.println("ParaViewProxy error: " + "Failed to open \""
					+ fullPath + "\" due to connection error.");
		}

		return opened;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public URI getURI() {
		return uri;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getFeatureCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getFeatures(String category)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setFeature(String category, String feature)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getPropertyValues(String property)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setProperty(String property, String value)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public int setProperties(Map<String, String> properties)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Gets the ParaView ID pointing to the file's proxy on the server.
	 * 
	 * @return The ID for the server's file proxy (the main proxy for the file
	 *         on the server, sometimes just called "the" proxy).
	 */
	protected int getFileId() {
		return fileId;
	}

	/**
	 * Gets the ParaView ID pointing to the file's associated render view proxy
	 * on the server.
	 * 
	 * @return The ID for the server's view proxy.
	 */
	protected int getViewId() {
		return viewId;
	}

	/**
	 * Gets the ParaView ID pointing to the file's associated representation
	 * proxy on the server.
	 * 
	 * @return The ID for the server's representation proxy.
	 */
	protected int getRepresentationId() {
		return repId;
	}
}