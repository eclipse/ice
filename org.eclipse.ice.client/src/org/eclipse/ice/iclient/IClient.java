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
package org.eclipse.ice.iclient;

import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.core.iCore.ICore;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;
import org.eclipse.ice.item.ItemBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IClient is an interface that must be realized by clients of ICE. It describes
 * the possible interactions between the Eclipse User and ICE.
 *
 * @author Jay Jay Billings
 */
public interface IClient {

	/**
	 * A setter for the OSGI Core service. This setter is used to register the
	 * ICore with the client. It should be used to set the service when the
	 * intention is to run the client in "local-only" mode.
	 *
	 * @param core
	 */
	public void setCoreService(ICore core);

	/**
	 * This operation directs ICE to create a new Item. If the Item is
	 * successfully created, it returns the identification number of the new
	 * Item and launches a UIWidget to gather additional information if needed.
	 *
	 * @param itemType
	 *            The type of Item to create.
	 * @return The identification number of the new Item.
	 */
	public int createItem(String itemType);

	/**
	 * This operation directs ICE to create a new Item. If the Item is
	 * successfully created, it returns the identification number of the new
	 * Item and launches a UIWidget to gather additional information if needed.
	 *
	 * @param itemType
	 *            The type of Item to create.
	 * @param project
	 *            The project where the Item should be created
	 * @return The identification number of the new Item.
	 */
	public int createItem(String selectedItem, IProject project);

	/**
	 * This operation sets the factory that must be used to create widgets for
	 * the UI system that is used in the client implementation.
	 *
	 * @param widgetFactory
	 */
	public void setUIWidgetFactory(IWidgetFactory widgetFactory);

	/**
	 * This operation loads an Item for editing or review by the user.
	 *
	 * @param itemId
	 *            The identification number of the Item that should be loaded.
	 */
	public void loadItem(int itemId);

	/**
	 * This operation attempts to load an Item from an IFile reference and
	 * returns the Form representing it.
	 * 
	 * @param itemFile
	 *            the file that should be loaded as an Item
	 * @return the Form that represents the Item
	 */
	public Form loadItem(IFile itemFile);

	/**
	 * This operation displays a simple error message for which no response is
	 * required.
	 *
	 * @param error
	 *            The error message.
	 */
	public void throwSimpleError(String error);

	/**
	 * This operation returns a list of the available Item types that can be
	 * created by ICE or null if no Items are available.
	 *
	 * @return The list of ItemTypes that can be created by ICE.
	 */
	public ArrayList<String> getAvailableItemTypes();

	/**
	 * This operation directs ICE to process the Item with the specified id by
	 * performing a certain Action.
	 *
	 * @param itemId
	 *            The id number of Item that should be processed.
	 * @param actionName
	 *            The name of the Action that should be performed for the Item.
	 */
	public void processItem(int itemId, String actionName);

	/**
	 * This operation directs the client to connect to the Core at the specified
	 * remote address and port. If the port is less than or equal to zero, the
	 * client will default to port 80. If the hostname is null, this operation
	 * returns false. If the client needs to retrieve a username and password
	 * from the user, it will request an ExtraInfoWidget that is configured to
	 * seek a username and password.
	 *
	 * @param hostname
	 *            The hostname of the remote ICore.
	 * @param port
	 *            The port on which the client should connect to the remote
	 *            ICore.
	 * @return True if the client was able to connect to the core and false if
	 *         not.
	 */
	public boolean connectToCore(String hostname, int port);

	/**
	 * This operation returns a list of ICEObjects that represent the Items
	 * currently managed by ICE. Each ICEObject contains the name, id and
	 * description of an Item.
	 *
	 * @return The list of Identifiables that represents the Items.
	 */
	public ArrayList<Identifiable> getItems();

	/**
	 * This operation directs ICE to delete the specified Item.
	 *
	 * @param id
	 *            The identification number of the new Item.
	 */
	public void deleteItem(int id);

	/**
	 * This operation directs ICE to import a file into its workspace.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 */
	public void importFile(URI file);

	/**
	 * This operation directs ICE to import a file into the provided IProject instance.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 * @param project
	 *            The IProject instance that this file will be imported into.
	 */
	public void importFile(URI file, IProject project);
	
	/**
	 * This operation directs ICE to import a file into the provided IProject name.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 * @param projectName
	 *            The name of the IProject instance that this file will be imported into.
	 */
	public void importFile(URI file, String projectName);
	
	/**
	 * This operation direct ICE import a file into the default project and load that
	 * file as an input for the specified Item type.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 * @param itemType
	 *            The type of Item to create.
	 * @return The identification number given of the newly created Item or -1
	 *         if it was unable to create the Item.
	 */
	public int importFileAsItem(URI file, String itemType);

	/**
	 * This operation direct ICE import a file into the specified IProject and load that
	 * file as an input for the specified Item type.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 * @param itemType
	 *            The type of Item to create.
	 * @param project
	 * 			  The project instance this file will be imported into.
	 * @return The identification number given of the newly created Item or -1
	 *         if it was unable to create the Item.
	 */
	public int importFileAsItem(URI file, String itemType, IProject project);
	
	/**
	 * This operation direct ICE import an existing IFile into the its corresponding 
	 * IProject instance  and load that file as an input for the specified Item type.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 * @param itemType
	 *            The type of Item to create.
	 * @return The identification number given of the newly created Item or -1
	 *         if it was unable to create the Item.
	 */
	public int importFileAsItem(IFile file, String itemType);

	/**
	 * This operation direct ICE import a file into the specified IProject given 
	 * by the provided projectName and load that file as an input for the specified Item type.
	 *
	 * @param file
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null.
	 * @param itemType
	 *            The type of Item to create.
	 * @param projectName
	 * 			  The name of the project instance this file will be imported into.
	 * @return The identification number given of the newly created Item or -1
	 *         if it was unable to create the Item.
	 */
	public int importFileAsItem(URI file, String itemType, String projectName);

	/**
	 * This operation retrieves all of the ItemBuilders from the
	 * ExtensionRegistry.
	 *
	 * @return The array of ItemBuilders that were found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IClient getClient() throws CoreException {
		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(ItemBuilder.class);

		IClient client = null;
		String id = "org.eclipse.ice.client.clientInstance";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			client = (IClient) elements[0].createExecutableExtension("class");
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return client;
	}
}