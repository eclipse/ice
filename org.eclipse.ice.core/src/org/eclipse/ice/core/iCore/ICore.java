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

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.ItemBuilder;

/**
 * ICore is an interface that is realized by Core and used by clients that
 * connect to the Core. It describes the services and data available upon
 * request from the server.
 *
 * The Connect() operation must be called to obtain a unique identification
 * number for the client. After that, all of the other operations on this
 * interface will be available.
 *
 * Realizations of ICore are intended to be used as web-APIs and, to that end,
 * many of the arguments in the operations are Strings that should be safe to
 * cast to integers.
 *
 * @author Jay Jay Billings
 */
@Path("/")
public interface ICore {
	/**
	 * This operation "connects" a client to the ICore. The connection in this
	 * case is represented by a unique client id that is returned from this
	 * operation. It is safe to parse this id as an integer and that integer
	 * should always be greater than 0.
	 *
	 * @return The unique client identification number that has been assigned to
	 *         the client that made the request. It is safe to parse this string
	 *         as an integer.
	 */
	@GET
	@Produces("text/plain")
	public String connect();

	/**
	 * This operation will disconnect a client from the remote server if it is
	 * connected.
	 *
	 * @param uniqueClientId
	 *            The unique client identification number of the client that
	 *            would like to disconnect.
	 */
	public void disconnect(int uniqueClientId);

	/**
	 * This operation registers an ItemBuilder and thereby a particular Item
	 * class with the Core. This operation is primarily used by the underlying
	 * OSGi framework to publish available Item types to ICE.
	 *
	 * @param itemBuilder
	 *            An instance of ItemBuilder for a particular Item that is
	 *            available to the Core.
	 */
	public void registerItem(ItemBuilder itemBuilder);

	/**
	 * This operation registers a composite Item with the ICore. Composite Items
	 * are Items that depend on other Items to function. This operation is
	 * primarily used by the underlying OSGi framework to publish available Item
	 * types to ICE.
	 *
	 * @param builder
	 *            The ICompositeItemBuilder that will build the composite Item.
	 */
	public void registerCompositeItem(ICompositeItemBuilder builder);

	/**
	 * This operation unregisters an ItemBuilder and thereby a particular Item
	 * class with the Core. This operation is primarily used by the underlying
	 * OSGi framework to notify ICE if or when a particular type of Item, which
	 * is stored in an OSGi bundle, becomes unavailable.
	 *
	 * @param itemBuilder
	 *            An instance of ItemBuilder for a particular Item that is now
	 *            unavailable to the Core.
	 */
	public void unregisterItem(ItemBuilder itemBuilder);

	/**
	 * This operation directs ICE to create a new Item. If the Item is
	 * successfully created, it returns the identification number of the new
	 * Item. The caller of this operation should immediately inquire after the
	 * status of the Item that was created using the getItemStatus() operation
	 * to determine if more information is required to make the Item usable.
	 *
	 * @param itemType
	 *            The type of Item to create.
	 * @param project
	 *            The workspace project where the Item should be created and
	 *            persisted.
	 * @return The identification number given as a String of the newly created
	 *         Item or -1 if it was unable to create the Item. It is safe to
	 *         parse this string as an integer.
	 */
	public String createItem(String itemType, IProject project);

	/**
	 * This operation is a utility operation that performs the same work as the
	 * two argument createItem, except it creates the Item in a default project
	 * space.
	 *
	 * This operation is mapped via JAX-RS to the web interface.
	 *
	 * Of historical note, this was the original implementation of this
	 * operation and project support was added nearly four years later.
	 *
	 * @param itemType
	 *            The type of Item to create.
	 * @return The identification number given as a String of the newly created
	 *         Item or -1 if it was unable to create the Item. It is safe to
	 *         parse this string as an integer.
	 */
	@POST
	@Path("items/create")
	@Produces("text/plain")
	public String createItem(String itemType);

	/**
	 * This operation directs the ICore to *PERMANTENTLY* delete an Item.
	 *
	 * @param itemId
	 *            The identification number of the Item that should be deleted
	 *            given as a String. It is safe to parse this string as an
	 *            integer.
	 */
	public void deleteItem(String itemId);

	/**
	 * This operation returns the status an Item.
	 *
	 * @param id
	 *            The identification number of the Item that should be checked.
	 * @return The status of the Item.
	 */
	public FormStatus getItemStatus(Integer id);

	/**
	 * This operation loads an Item from the file and returns the Form that
	 * represents it.
	 * 
	 * This operation assumes that all necessary checks and determinations have
	 * been performed by the client, including existence checks and the
	 * determination that this IFile actually contains an Item.
	 * 
	 * @param itemFile
	 *            The IFile that contains the Item
	 * @return the Form that represents the Item
	 */
	public Form loadItem(IFile itemFile);

	/**
	 * This operation returns the representational state, a Form, of an Item
	 * that is managed by the ICore to the caller.
	 *
	 * If this operation is called immediately after processItem() with the same
	 * Item id and the call to processItem() returns FormStatus.NeedsInfo, then
	 * this operation will return a simple Form composed of a single
	 * DataComponent with Entries for all of the additional required
	 * information. The smaller Form is created by the Action that is executed
	 * during the call to processItem().
	 *
	 * @param itemId
	 *            The identification number of the Item that should be
	 *            retrieved.
	 * @return A Form that represents the Item managed by the core.
	 */
	@GET
	@Path("items/{id}")
	@Produces("application/xml")
	public Form getItem(@PathParam("id") int itemId);

	/**
	 * This operation returns a list of the available Item types that can be
	 * created by ICE or null if no Items are registered with the Core. It
	 * returns an ICEList of Strings, (i.e. - ICEList&lt;Strings&gt; in Java).
	 *
	 * @return The list of ItemTypes that can be created by ICE. This list is
	 *         determined by the Items that are currently registered with the
	 *         running realization of ICore.
	 */
	@GET
	@Path("items")
	@Produces("application/xml")
	public ICEList<String> getAvailableItemTypes();

	/**
	 * This operation posts an updated Form to the Core so that the updated
	 * information can be processed by the appropriate Item.
	 *
	 * @param form
	 *            The Form that carries new information for an Item.
	 * @param uniqueClientId
	 *            The unique client id the IClient that is making the update
	 *            request.
	 * @return The status of the updated Item.
	 */
	public FormStatus updateItem(Form form, int uniqueClientId);

	/**
	 * This operation directs the Core to process the Item with the specified id
	 * by performing the specific action. The action name must be one of the set
	 * of actions from the Form that represents the Item with the specified id.
	 *
	 * It is possible that ICE may require information in addition to that which
	 * was requested in the original Form, such as for a username and password
	 * for a remote machine. If this is the case, processItem will return
	 * FormStatus.NeedsInfo and a new, temporary Form will be available for the
	 * Item by calling getItem(). Once this new Form is submitted (by calling
	 * updateItem() with the completed Form), the Item will finish processing.
	 *
	 * @param itemId
	 *            The item id for the Item that should be processed with the
	 *            specified action.
	 *
	 * @param actionName
	 *            The action that should be performed on the Item.
	 *
	 * @param uniqueClientId
	 *            The unique identification number of the client making the
	 *            request.
	 *
	 * @return The status of the Item after the action was performed.
	 */
	public FormStatus processItem(int itemId, String actionName,
			int uniqueClientId);

	/**
	 * This operation returns the list of Items that have been created in ICE.
	 * It returns a list of Identifiables that represent those Items and
	 * provides the name, description and identification number of each Item.
	 * This operation is only meant to provide information about the Items.
	 *
	 * @return The list of Identifiables that represent the Items.
	 */
	public ArrayList<Identifiable> getItemList();

	/**
	 * This operation returns a file handle to the output file for the Item with
	 * the specified id. It returns a handle to the file whether or not it
	 * actually exists and clients should check the File.exists() operation
	 * before attempting to manipulate the file. The description of the output
	 * file can be found in the Item class documentation. This file handle
	 * returned is the <i>real</i> file handle and can be written, but clients
	 * should be careful to only read from the file. It will return null if an
	 * Item with the specified id does not exist.
	 *
	 * @param id
	 *            The id of the Item.
	 * @return The output file for the specified Item, thoroughly documented
	 *         elsewhere.
	 */
	public File getItemOutputFile(int id);

	/**
	 * This operation cancels the process with the specified name for the Item
	 * identified.
	 *
	 * @param itemId
	 *            The id of the Item whose process should be canceled.
	 * @param actionName
	 *            The name of the action that should be canceled for the
	 *            specified Item.
	 * @return The status
	 */
	public FormStatus cancelItemProcess(int itemId, String actionName);

	/**
	 * This operation directs the core to import a file into its workspace.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 */
	public void importFile(URI file);

	/**
	 * This operation directs the core to import a file into its workspace and
	 * load that file as an input for the specified Item type. It returns the id
	 * of the newly created Item.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 * @param itemType
	 *            The type of Item to create.
	 * @return The identification number given as a String of the newly created
	 *         Item or -1 if it was unable to create the Item. It is safe to
	 *         parse this string as an integer.
	 */
	public String importFileAsItem(URI file, String itemType);

	/**
	 * This operation posts a message containing an update to the ICE Item
	 * designated in the body of the message.
	 *
	 * This operation is primarily used by the ICE Updater to post messages to
	 * the Core from remote processes. The message format can be found in the
	 * documentation for the Updater.
	 *
	 * @param message
	 *            The message that should be passed on to the specified Item.
	 *            This string must be in JSON and conform to the message format
	 *            of the ICE Updater.
	 * @return "OK" if the post was successful, null if not to conform to JAX-RS
	 *         HTTP 200/204 return code conversion.
	 */
	@POST
	@Path("update")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("text/plain")
	public String postUpdateMessage(String message);
}