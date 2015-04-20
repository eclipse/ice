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
package org.eclipse.ice.core.iCore;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.ICEObject.ICEList;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.ItemBuilder;

import java.io.File;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * ICore is an interface that is realized by Core and used by clients that
 * connect to the Core. It describes the services and data available upon
 * request from the server.
 * </p>
 * <p>
 * The Connect() operation must be called to obtain a unique identification
 * number for the client. After that, all of the other operations on this
 * interface will be available.
 * </p>
 * <p>
 * Realizations of ICore are intended to be used as web-APIs and, to that end,
 * many of the arguments in the operations are Strings that should be safe to
 * cast to integers.
 * </p>
 * <p>
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@Path("/")
public interface ICore {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation "connects" a client to the ICore. The connection in this
	 * case is represented by a unique client id that is returned from this
	 * operation. It is safe to parse this id as an integer and that integer
	 * should always be greater than 0.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The unique client identification number that has been assigned to
	 *         the client that made the request. It is safe to parse this string
	 *         as an integer.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@GET
	@Produces("text/plain")
	public String connect();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation will disconnect a client from the remote server if it is
	 * connected.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param uniqueClientId
	 *            <p>
	 *            The unique client identification number of the client that
	 *            would like to disconnect.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disconnect(int uniqueClientId);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the workspace file system available to the
	 * ICEUser. It returns a Form that contains the directory structure.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param uniqueClientID
	 *            <p>
	 *            The unique ID of the Client calling the operation.
	 *            </p>
	 * @return <p>
	 *         A Form that contains a description of the workspace file system
	 *         available to the ICEUser.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getFileSystem(int uniqueClientID);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation registers an ItemBuilder and thereby a particular Item
	 * class with the Core. This operation is primarily used by the underlying
	 * OSGi framework to publish available Item types to ICE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemBuilder
	 *            <p>
	 *            An instance of ItemBuilder for a particular Item that is
	 *            available to the Core.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerItem(ItemBuilder itemBuilder);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation registers a composite Item with the ICore. Composite Items
	 * are Items that depend on other Items to function. This operation is
	 * primarily used by the underlying OSGi framework to publish available Item
	 * types to ICE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param builder
	 *            <p>
	 *            The ICompositeItemBuilder that will build the composite Item.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerCompositeItem(ICompositeItemBuilder builder);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation unregisters an ItemBuilder and thereby a particular Item
	 * class with the Core. This operation is primarily used by the underlying
	 * OSGi framework to notify ICE if or when a particular type of Item, which
	 * is stored in an OSGi bundle, becomes unavailable.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemBuilder
	 *            <p>
	 *            An instance of ItemBuilder for a particular Item that is now
	 *            unavailable to the Core.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void unregisterItem(ItemBuilder itemBuilder);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs ICE to create a new Item. If the Item is
	 * successfully created, it returns the identification number of the new
	 * Item. The caller of this operation should immediately inquire after the
	 * status of the Item that was created using the getItemStatus() operation
	 * to determine if more information is required to make the Item usable.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemType
	 *            <p>
	 *            The type of Item to create.
	 *            </p>
	 * @return <p>
	 *         The identification number given as a String of the newly created
	 *         Item or -1 if it was unable to create the Item. It is safe to
	 *         parse this string as an integer.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@POST
	@Path("items/create")
	@Produces("text/plain")
	public String createItem(String itemType);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the ICore to permanently delete an Item. Again:
	 * this is permanent!
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemId
	 *            <p>
	 *            The identification number of the Item that should be deleted
	 *            given as a String. It is safe to parse this string as an
	 *            integer.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void deleteItem(String itemId);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the status an Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param id
	 *            <p>
	 *            The identification number of the Item that should be checked.
	 *            </p>
	 * @return <p>
	 *         The status of the Item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus getItemStatus(Integer id);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the representational state, a Form, of an Item
	 * that is managed by the ICore to the caller.
	 * </p>
	 * <p>
	 * If this operation is called immediately after processItem() with the same
	 * Item id and the call to processItem() returns FormStatus.NeedsInfo, then
	 * this operation will return a simple Form composed of a single
	 * DataComponent with Entries for all of the additional required
	 * information. The smaller Form is created by the Action that is executed
	 * during the call to processItem().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemId
	 *            <p>
	 *            The identification number of the Item that should be
	 *            retrieved.
	 *            </p>
	 * @return <p>
	 *         A Form that represents the Item managed by the core.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@GET
	@Path("items/{id}")
	@Produces("application/xml")
	public Form getItem(@PathParam("id") int itemId);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a list of the available Item types that can be
	 * created by ICE or null if no Items are registered with the Core. It
	 * returns an ICEList of Strings, (i.e. - ICEList&lt;Strings&gt; in Java).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The list of ItemTypes that can be created by ICE. This list is
	 *         determined by the Items that are currently registered with the
	 *         running realization of ICore.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@GET
	@Path("items")
	@Produces("application/xml")
	public ICEList<String> getAvailableItemTypes();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation posts an updated Form to the Core so that the updated
	 * information can be processed by the appropriate Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param form
	 *            <p>
	 *            The Form that carries new information for an Item.
	 *            </p>
	 * @param uniqueClientId
	 *            <p>
	 *            The unique client id the IClient that is making the update
	 *            request.
	 *            </p>
	 * @return <p>
	 *         The status of the updated Item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus updateItem(Form form, int uniqueClientId);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the Core to process the Item with the specified id
	 * by performing the specific action. The action name must be one of the set
	 * of actions from the Form that represents the Item with the specified id.
	 * </p>
	 * <p>
	 * It is possible that ICE may require information in addition to that which
	 * was requested in the original Form, such as for a username and password
	 * for a remote machine. If this is the case, processItem will return
	 * FormStatus.NeedsInfo and a new, temporary Form will be available for the
	 * Item by calling getItem(). Once this new Form is submitted (by calling
	 * updateItem() with the completed Form), the Item will finish processing.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemId
	 *            <p>
	 *            The item id for the Item that should be processed with the
	 *            specified action.
	 *            </p>
	 * @param actionName
	 *            <p>
	 *            The action that should be performed on the Item.
	 *            </p>
	 * @param uniqueClientId
	 *            <p>
	 *            The unique identification number of the client making the
	 *            request.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after the action was performed.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus processItem(int itemId, String actionName,
			int uniqueClientId);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the list of Items that have been created in ICE.
	 * It returns a list of ICEObjects that represent those Items and provide
	 * the name, description and identification number. This operation is only
	 * meant to provide information about the Items.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The list of Identifiables that represent the Items.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Identifiable> getItemList();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a file handle to the output file for the Item with
	 * the specified id. It returns a handle to the file whether or not it
	 * actually exists and clients should check the File.exists() operation
	 * before attempting to manipulate the file. The description of the output
	 * file can be found in the Item class documentation. This file handle
	 * returned is the <i>real</i> file handle and can be written, but clients
	 * should be careful to only read from the file. It will return null if an
	 * Item with the specified id does not exist.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param id
	 *            <p>
	 *            The id of the Item.
	 *            </p>
	 * @return <p>
	 *         The output file for the specified Item, thoroughly documented
	 *         elsewhere.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public File getItemOutputFile(int id);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation cancels the process with the specified name for the Item
	 * identified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemId
	 *            <p>
	 *            The id of the Item whose process should be canceled.
	 *            </p>
	 * @param actionName
	 *            <p>
	 *            The name of the action that should be canceled for the
	 *            specified Item.
	 *            </p>
	 * @return <p>
	 *         The status
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus cancelItemProcess(int itemId, String actionName);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the core to import a file into its workspace.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param file
	 *            <p>
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void importFile(URI file);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the core to import a file into its workspace and
	 * load that file as an input for the specified Item type. It returns the id
	 * of the newly created Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param file
	 *            <p>
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 *            </p>
	 * @param itemType
	 *            <p>
	 *            The type of Item to create.
	 *            </p>
	 * @return <p>
	 *         The identification number given as a String of the newly created
	 *         Item or -1 if it was unable to create the Item. It is safe to
	 *         parse this string as an integer.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String importFileAsItem(URI file, String itemType);

	/**
	 * This operation posts a message containing an update to the ICE Item
	 * designated in the body of the message.
	 * <p>
	 * This operation is primarily used by the ICE Updater to post messages to
	 * the Core from remote processes. The message format can be found in the
	 * documentation for the Updater.
	 * </p>
	 * @param message
	 *            The message that should be passed on to the specified Item.
	 *            This string must be in JSON and conform to the message format
	 *            of the ICE Updater.
	 * @return 
	 *         "OK" if the post was successful, null if not to conform to JAX-RS
	 *         HTTP 200/204 return code conversion.
	 */
	@POST
	@Path("update")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("text/plain")
	public String postUpdateMessage(String message);
}