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

import org.eclipse.ice.core.iCore.ICore;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;

import java.util.ArrayList;
import java.io.File;
import java.net.URI;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.ItemBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * <!-- begin-UML-doc -->
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
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class RemoteCoreProxy implements ICore {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The hostname of the server to which the proxy is connected.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String host = null;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The port on which the proxy is connected to the server.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int serverPort = -1;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The time in milliseconds that the proxy should spend on any given request
	 * to the server.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int timeout = 60000;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The unique client id assigned to this proxy and its connection to the
	 * server.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public RemoteCoreProxy() {
		// begin-user-code

		// Create the client
		client = new Client();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hostname of the server to which the core is
	 * connected or, if it is not connected, it returns null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hostname
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getHost() {
		// begin-user-code
		return host;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the port number on which the the proxy has made
	 * its connection or, if it is not connected, it returns -1.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The port number
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getPort() {
		// begin-user-code
		return serverPort;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the hostname of the server to which the
	 * RemoteCoreProxy should connect.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param hostname
	 *            <p>
	 *            The hostname.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setHost(String hostname) {
		// begin-user-code

		if (hostname != null) {
			host = "http://" + hostname;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the port on which the RemoteCoreProxy should connect.
	 * The port is only set if it is greater than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param port
	 *            <p>
	 *            The port number.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPort(int port) {
		// begin-user-code

		// Set the port only if it is greater than zero
		if (port > 0) {
			serverPort = port;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation connects to the ICE Core using Basic authentication over
	 * HTTPS.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String connect(String username, String password) {
		// begin-user-code

		// Create a filter for the client that sets the authentication
		// credentials
		final HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(
				username, password);
		client.addFilter(authFilter);

		// Connect as usual
		return connect();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#connect()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String connect() {
		// begin-user-code

		// Only load the resource if the hostname is valid
		if (host != null) {
			baseResource = client.resource(host + ":" + serverPort + "/ice");
		} else {
			return "-1";
		}
		String response = baseResource.accept(MediaType.TEXT_PLAIN)
				.header("X-FOO", "BAR").get(String.class);

		System.out.println("RemoteCoreProxy connection information: ");
		System.out.println("\tHostname: " + host);
		System.out.println("\tPort: " + serverPort);
		System.out.println("\tUnique Client Id: " + response);

		return response;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#disconnect(int uniqueClientId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disconnect(int uniqueClientId) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getFileSystem(int uniqueClientID)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getFileSystem(int uniqueClientID) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#registerItem(ItemBuilder itemBuilder)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerItem(ItemBuilder itemBuilder) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#registerCompositeItem(ICompositeItemBuilder builder)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerCompositeItem(ICompositeItemBuilder builder) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#unregisterItem(ItemBuilder itemBuilder)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void unregisterItem(ItemBuilder itemBuilder) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#createItem(String itemType)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String createItem(String itemType) {
		// begin-user-code

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

		System.out.println("RemoteCoreProxy Message: POST URL = "
				+ resource.queryParam("type", itemType).toString());
		System.out.println("RemoteCoreProxy Message: Item id = " + id);

		return id;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#deleteItem(String itemId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void deleteItem(String itemId) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemStatus(Integer id)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus getItemStatus(Integer id) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItem(int itemId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getItem(int itemId) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getAvailableItemTypes()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEList<String> getAvailableItemTypes() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#updateItem(Form form, int uniqueClientId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus updateItem(Form form, int uniqueClientId) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#processItem(int itemId, String actionName, int uniqueClientId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus processItem(int itemId, String actionName,
			int uniqueClientId) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemList()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Identifiable> getItemList() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemOutputFile(int id)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public File getItemOutputFile(int id) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#cancelItemProcess(int itemId, String actionName)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus cancelItemProcess(int itemId, String actionName) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#importFile(URI file)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void importFile(URI file) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
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