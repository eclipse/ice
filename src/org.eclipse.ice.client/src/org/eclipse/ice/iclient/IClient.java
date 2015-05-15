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

import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;
import org.eclipse.ice.core.iCore.ICore;
import java.util.ArrayList;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import java.net.URI;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;

/** 
 * <p>IClient is an interface that must be realized by clients of ICE. It describes the possible interactions between the Eclipse User and ICE.</p>
 * @author Jay Jay Billings
 */
public interface IClient {
	/** 
	 * <p>This operation retrieves a representation of the file system from the Core.</p>
	 * @return <p>A hierarchical list of the workspace directories available to the Eclipse User.</p>
	 */
	public Object getFileSystem();

	/** 
	 * <p>A setter for the OSGI Core service. This setter is used to register the ICore with the client. It should be used to set the service when the intention is to run the client in "local-only" mode.</p>
	 * @param core
	 */
	public void setCoreService(ICore core);

	/** 
	 * <p>This operation directs ICE to create a new Item. If the Item is successfully created, it returns the identification number of the new Item and launches a UIWidget to gather additional information if needed. </p>
	 * @param itemType <p>The type of Item to create.</p>
	 * @return <p>The identification number of the new Item.</p>
	 */
	public int createItem(String itemType);

	/** 
	 * <p>This operation sets the factory that must be used to create widgets for the UI system that is used in the client implementation.</p>
	 * @param widgetFactory
	 */
	public void setUIWidgetFactory(IWidgetFactory widgetFactory);

	/** 
	 * <p>This operation loads an Item for editing or review by the user.</p>
	 * @param itemId <p>The identification number of the Item that should be loaded.</p>
	 */
	public void loadItem(int itemId);

	/** 
	 * <p>This operation displays a simple error message for which no response is required.</p>
	 * @param error <p>The error message.</p>
	 */
	public void throwSimpleError(String error);

	/** 
	 * <p>This operation returns a list of the available Item types that can be created by ICE or null if no Items are available.</p>
	 * @return <p>The list of ItemTypes that can be created by ICE.</p>
	 */
	public ArrayList<String> getAvailableItemTypes();

	/** 
	 * <p>This operation directs ICE to process the Item with the specified id by performing a certain Action.</p>
	 * @param itemId <p>The id number of Item that should be processed.</p>
	 * @param actionName <p>The name of the Action that should be performed for the Item.</p>
	 */
	public void processItem(int itemId, String actionName);

	/** 
	 * <p>This operation directs the client to connect to the Core at the specified remote address and port. If the port is less than or equal to zero, the client will default to port 80. If the hostname is null, this operation returns false. If the client needs to retrieve a username and password from the user, it will request an ExtraInfoWidget that is configured to seek a username and password.</p>
	 * @param hostname <p>The hostname of the remote ICore.</p>
	 * @param port <p>The port on which the client should connect to the remote ICore.</p>
	 * @return <p>True if the client was able to connect to the core and false if not.</p>
	 */
	public boolean connectToCore(String hostname, int port);

	/** 
	 * <p>This operation returns a list of ICEObjects that represent the Items currently managed by ICE. Each ICEObject contains the name, id and description of an Item.</p>
	 * @return <p>The list of Identifiables that represents the Items.</p>
	 */
	public ArrayList<Identifiable> getItems();

	/** 
	 * <p>This operation directs ICE to delete the specified Item.</p>
	 * @param id <p>The identification number of the new Item.</p>
	 */
	public void deleteItem(int id);

	/** 
	 * <p>This operation directs ICE to import a file into its workspace.</p>
	 * @param file <p>The file that should be imported. Nothing will happen if this argument is null.</p>
	 */
	public void importFile(URI file);

	/** 
	 * <p>This operation direct ICE import a file into its workspace and load that file as an input for the specified Item type.</p>
	 * @param file <p>The file that should be imported. Nothing will happen if this argument is null.</p>
	 * @param itemType <p>The type of Item to create.</p>
	 * @return <p>The identification number given of the newly created Item or -1 if it was unable to create the Item.</p>
	 */
	public int importFileAsItem(URI file, String itemType);
}