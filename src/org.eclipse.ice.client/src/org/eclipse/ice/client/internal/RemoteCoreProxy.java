/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.internal;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;

import org.eclipse.ice.core.iCore.ICore;
import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.ItemBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class acts as a proxy for a realization of ICore that is running on an
 * entirely different machine. It uses the Jersey Client to connect to the
 * remote client across an HTTP or HTTPS connection. It is a simple wrapper and
 * provides not additional functionality beyond that of ICore with the exception
 * of being able to connect and disconnect from a server.
 * </p>
 * <p>
 * The hostname and port must be set before the call to connect() is made. If
 * they are not called, the connection will fail.
 * </p>
 * <p>
 * There are two connect() operations on this class. The first, with no
 * arguments, is part of the ICore interface and will attempt to connect to the
 * server without authentication. The second, which has a username and a
 * password as arguments, will connect to the server using HTTP basic
 * authentication of an SSL connection.
 * </p>
 * <p>
 * The exact mechanism by which the HTTPS connection is made and utilized is not
 * modeled here. It is sufficient to say that ICE 2.0 uses the Jersey Client.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class RemoteCoreProxy implements ICore {
	
	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RemoteCoreProxy.class);
	
	/**
	 * <p>
	 * The hostname of the server to which the proxy is connected.
	 * </p>
	 * 
	 */
	private String host = null;
	/**
	 * <p>
	 * The port on which the proxy is connected to the server.
	 * </p>
	 * 
	 */
	private int serverPort = -1;
	/**
	 * <p>
	 * The time in milliseconds that the proxy should spend on any given request
	 * to the server.
	 * </p>
	 * 
	 */
	private int timeout = 60000;
	/**
	 * <p>
	 * The unique client id assigned to this proxy and its connection to the
	 * server.
	 * </p>
	 * 
	 */
	private int clientId = -1;

	/** ----- Objects for using the Jersey client ----- **/

	/**
	 * The Jersey Client that connects to the server.
	 */
	private Client client = null;

	/**
	 * A web resource that is configured as "base" from which other resources
	 * are created.
	 */
	private WebResource baseResource = null;

	/** ----- **/

	/**
	 * <p>
	 * The Constructor.
	 * </p>
	 * 
	 */
	public RemoteCoreProxy() {

		// Create the client
		client = new Client();

	}

	/**
	 * <p>
	 * This operation returns the hostname of the server to which the core is
	 * connected or, if it is not connected, it returns null.
	 * </p>
	 * 
	 * @return <p>
	 *         The hostname
	 *         </p>
	 */
	public String getHost() {
		return host;
	}

	/**
	 * <p>
	 * This operation returns the port number on which the the proxy has made
	 * its connection or, if it is not connected, it returns -1.
	 * </p>
	 * 
	 * @return <p>
	 *         The port number
	 *         </p>
	 */
	public int getPort() {
		return serverPort;
	}

	/**
	 * <p>
	 * This operation sets the hostname of the server to which the
	 * RemoteCoreProxy should connect.
	 * </p>
	 * 
	 * @param hostname
	 *            <p>
	 *            The hostname.
	 *            </p>
	 */
	public void setHost(String hostname) {

		if (hostname != null) {
			host = "http://" + hostname;
		}
	}

	/**
	 * <p>
	 * This operation sets the port on which the RemoteCoreProxy should connect.
	 * The port is only set if it is greater than zero.
	 * </p>
	 * 
	 * @param port
	 *            <p>
	 *            The port number.
	 *            </p>
	 */
	public void setPort(int port) {

		// Set the port only if it is greater than zero
		if (port > 0) {
			serverPort = port;
		}
	}

	/**
	 * <p>
	 * This operation connects to the ICE Core using Basic authentication over
	 * HTTPS.
	 * </p>
	 * 
	 * @param username
	 *            <p>
	 *            The users' name.
	 *            </p>
	 * @param password
	 *            <p>
	 *            The users' password.
	 *            </p>
	 * @return <p>
	 *         The client id as specified by ICore.connect().
	 *         </p>
	 */
	public String connect(String username, String password) {

		// Create a filter for the client that sets the authentication
		// credentials
		final HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(
				username, password);
		client.addFilter(authFilter);

		// Connect as usual
		return connect();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#connect()
	 */
	@Override
	public String connect() {

		// Only load the resource if the hostname is valid
		if (host != null) {
			baseResource = client.resource(host + ":" + serverPort + "/ice");
		} else {
			return "-1";
		}
		String response = baseResource.accept(MediaType.TEXT_PLAIN)
				.header("X-FOO", "BAR").get(String.class);

		logger.info("RemoteCoreProxy connection information: ");
		logger.info("\tHostname: " + host);
		logger.info("\tPort: " + serverPort);
		logger.info("\tUnique Client Id: " + response);

		return response;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#disconnect(int uniqueClientId)
	 */
	@Override
	public void disconnect(int uniqueClientId) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getFileSystem(int uniqueClientID)
	 */
	@Override
	public Form getFileSystem(int uniqueClientID) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#registerItem(ItemBuilder itemBuilder)
	 */
	@Override
	public void registerItem(ItemBuilder itemBuilder) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#registerCompositeItem(ICompositeItemBuilder builder)
	 */
	@Override
	public void registerCompositeItem(ICompositeItemBuilder builder) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#unregisterItem(ItemBuilder itemBuilder)
	 */
	@Override
	public void unregisterItem(ItemBuilder itemBuilder) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#createItem(String itemType)
	 */
	@Override
	public String createItem(String itemType) {

		// Local Declarations
		String id = null;
		WebResource resource = null;

		// Only load the resource if the hostname is valid
		if (host != null) {
			resource = baseResource.path("/items/create");
		} else {
			return "-1";
		}
		// Get the available ItemTypes
		id = resource.queryParam("type", itemType).accept(MediaType.TEXT_PLAIN)
				.header("X-FOO", "BAR").post(String.class);

		logger.info("RemoteCoreProxy Message: POST URL = "
				+ resource.queryParam("type", itemType).toString());
		logger.info("RemoteCoreProxy Message: Item id = " + id);

		return id;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#deleteItem(String itemId)
	 */
	@Override
	public void deleteItem(String itemId) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemStatus(Integer id)
	 */
	@Override
	public FormStatus getItemStatus(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItem(int itemId)
	 */
	@Override
	public Form getItem(int itemId) {

		// Local Declarations
		Form itemForm = null;
		WebResource resource = null;
		String id = String.valueOf(itemId);

		// Only load the resource if the hostname is valid
		if (host != null) {
			resource = baseResource.path("/items/" + id);

			// Get the available ItemTypes
			itemForm = resource.accept(MediaType.APPLICATION_XML)
					.header("X-FOO", "BAR").get(Form.class);

		}

		return itemForm;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getAvailableItemTypes()
	 */
	@Override
	public ICEList<String> getAvailableItemTypes() {

		// Local Declarations
		ICEList<String> types = null;
		WebResource resource = null;

		// Only load the resource if the hostname is valid
		if (host != null) {
			resource = baseResource.path("/items");
		} else {
			return null;
		}
		// Get the available ItemTypes
		types = resource.accept(MediaType.APPLICATION_XML)
				.header("X-FOO", "BAR").get(ICEList.class);

		return types;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#updateItem(Form form, int uniqueClientId)
	 */
	@Override
	public FormStatus updateItem(Form form, int uniqueClientId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#processItem(int itemId, String actionName, int uniqueClientId)
	 */
	@Override
	public FormStatus processItem(int itemId, String actionName,
			int uniqueClientId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemList()
	 */
	@Override
	public ArrayList<Identifiable> getItemList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemOutputFile(int id)
	 */
	@Override
	public File getItemOutputFile(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#cancelItemProcess(int itemId, String actionName)
	 */
	@Override
	public FormStatus cancelItemProcess(int itemId, String actionName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#importFile(URI file)
	 */
	@Override
	public void importFile(URI file) {
		// TODO Auto-generated method stub

	}

	@Override
	public String importFileAsItem(URI file, String itemType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postUpdateMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}
}