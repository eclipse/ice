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
package org.eclipse.ice.core.internal.itemmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.core.iCore.IPersistenceProvider;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemListener;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.messaging.Message;

/**
 * <p>
 * The ItemManager is responsible for storing, managing and distributing all
 * instances of the Item class. The ItemManager implements a basic, CRUD-style
 * interface for managing items and contains two static methods to manage
 * dynamic registrations of ItemBuilders from the underlying OSGi framework.
 * This class will only store one instance of each ItemBuilder that is
 * registered with it.
 * </p>
 * <p>
 * The ItemManager will store and retrieve Items to and from a data store if an
 * IPersistenceProvider is set by calling ItemManager.setPersistenceProvider().
 * The ItemManager will persist Items when they are created, updated and
 * processed. It loads all Item in the Provider by calling loadItems() and it
 * persists all currently active Items by calling persistItems().
 * </p>
 * <p>
 * The process output file of an Item can be retrieved by calling
 * getOutputFile() and passing the id of the Item as an argument. Retrieving an
 * output file and retrieving a Form are separated because they are treated as
 * two distinctly different things on the Item class.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ItemManager implements ItemListener {
	/**
	 * <p>
	 * This is a list of all of the items that are managed by the ItemManger.
	 * The key is the Item Id and the value is a reference to the Item.
	 * </p>
	 * 
	 */
	private HashMap<Integer, Item> itemList;

	/**
	 * <p>
	 * The list of ItemBuilders that can be used to create items. The keys are
	 * the names of the builders and the values are the builders.
	 * </p>
	 */
	private HashMap<String, ItemBuilder> itemBuilderList;

	/**
	 * <p>
	 * This private attribute is used to create Item Ids for newly created
	 * Items. It equal to the next available integer, starting from 1, that has
	 * not been used by an Item.
	 * </p>
	 * 
	 */
	private int nextSequentialId;

	/**
	 * <p>
	 * This private list is used to store the ids of Items that have been
	 * deleted from the system so that they may be reused without have to
	 * compute their values, which would require a time consuming search over
	 * all Items.
	 * </p>
	 * 
	 */
	private ArrayList<Integer> reusableIds;

	/**
	 * <p>
	 * This list of ICompositeBuilders registered in ICE. This list is
	 * maintained because composite Items must be notified when the list of
	 * available builders changes.
	 * </p>
	 * 
	 */
	private ArrayList<ICompositeItemBuilder> compositeBuilders;

	/**
	 * <p>
	 * The persistence provider.
	 * </p>
	 * 
	 */
	private IPersistenceProvider provider;

	/**
	 * The project space from which Items were loaded from the persistence
	 * provider.
	 */
	private IProject loadedProject = null;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 */
	public ItemManager() {

		// Setup the ids
		nextSequentialId = 1;
		reusableIds = new ArrayList<Integer>();

		// Setup the lists
		itemBuilderList = new HashMap<String, ItemBuilder>();
		compositeBuilders = new ArrayList<ICompositeItemBuilder>();
		itemList = new HashMap<Integer, Item>();

	}

	/**
	 * <p>
	 * This operation creates a new item of type newItemType and returns the
	 * unique integer id of the created Item or, if the Item can not be created,
	 * -1. The id of the Item is set from either a sequential list of ids or a
	 * previously used id that was made available by the deletion of an Item.
	 * The IProject passed to this operation is a handle to the Eclipse project
	 * where files should be stored by the newly created Item, but since there
	 * is no guarantee that an Item will need to store or use files, this
	 * argument may be null.
	 * </p>
	 * <p>
	 * The ItemManager will call the persistence provider to store the newly
	 * created Item when this operation is called.
	 * </p>
	 * 
	 * @param newItemType
	 *            <p>
	 *            The type of new Item to create. This type name must be one of
	 *            the set of names returned by a call to getAvailableBuilders().
	 *            </p>
	 * @param project
	 *            <p>
	 *            The Eclipse project where the newly created Item should store
	 *            files and search for other resources.
	 *            </p>
	 * @return <p>
	 *         The new and unique id of the item that was created.
	 *         </p>
	 */
	public int createItem(String newItemType, IProject project) {

		// Local Declarations
		int retVal = -1;
		Item item = null;

		// Create the new Item if the type is valid by doing a linear search
		// across the map. This shouldn't be too expensive, for now, because the
		// list of items is small.
		if (newItemType != null) {
			for (ItemBuilder i : itemBuilderList.values()) {
				if (i.getItemName().equals(newItemType)) {
					item = i.build(project);
				}
			}
		}

		// Set the Item's id if it was created, add it to the list and
		// update the return value.
		if (item != null) {
			// Set the id to a previously used id if one is available
			if (!(reusableIds.isEmpty())) {
				item.setId(reusableIds.get(0));
				reusableIds.remove(0);
			} else {
				// Set the id to the next sequential id
				item.setId(nextSequentialId);
				// Update the next sequential id
				++nextSequentialId;
			}
			// Register as an observer of the Item
			item.addListener(this);
			// Add the Item to the list
			itemList.put(item.getId(), item);
			// Set the return value to the Item's id
			retVal = item.getId();
		}

		// If the provider exists, persist to the provider
		if (provider != null) {
			System.out.println("ItemManager Message: Persisting Item " + retVal
					+ " with the provider");
			provider.persistItem(item);
		}

		return retVal;

	}

	/**
	 * <p >
	 * This operation creates a new item of type newItemType and returns the
	 * unique integer id of the created Item or, if the Item can not be created,
	 * -1. The id of the Item is set from either a sequential list of ids or a
	 * previously used id that was made available by the deletion of an Item.
	 * The IProject passed to this operation is a handle to the Eclipse project
	 * where files should be stored by the newly created Item, but since there
	 * is no guarantee that an Item will need to store or use files, this
	 * argument may be null.
	 * </p>
	 * <p >
	 * The ItemManager will call the persistence provider to store the newly
	 * created Item when this operation is called.
	 * </p>
	 * <p>
	 * This version takes a file that should be loaded as input by the newly
	 * created Item and is primarily intend for file import.
	 * </p>
	 * 
	 * @param filename
	 *            <p>
	 *            The file that should be imported. Nothing will happen if this
	 *            argument is null. It should be a file in the project space.
	 *            </p>
	 * @param itemType
	 *            <p>
	 *            The type of Item to create.
	 *            </p>
	 * @param project
	 *            <p>
	 *            The Eclipse project where the newly created Item should store
	 *            files and search for other resources.
	 *            </p>
	 * @return <p>
	 *         The identification number of the newly created Item or -1 if it
	 *         was unable to create the Item.
	 *         </p>
	 */
	public int createItem(String filename, String itemType, IProject project) {

		// Local Declarations
		int itemId = 0;

		if (filename != null) {
			// Delegate the real work to someone else!
			itemId = createItem(itemType, project);
			// Try to load the item if it was created
			if (itemId > 0) {
				// Get the Item from the table
				Item item = itemList.get(itemId);
				// Load it up
				item.loadInput(filename);
			}
		}

		return itemId;
	}

	/**
	 * <p>
	 * This operation retrieves the Form that represents the Item with id equal
	 * to itemID and returns it to the caller.
	 * </p>
	 * <p>
	 * If this operation is called immediately after processItem() with the same
	 * Item id and the call to processItem() returns FormStatus.NeedsInfo, then
	 * this operation will return a simple Form composed of a single
	 * DataComponent with Entries for all of the additional required
	 * information. The smaller Form is created by the Action that is executed
	 * during the call to processItem().
	 * </p>
	 * 
	 * @param itemID
	 *            <p>
	 *            The unique itemID of the item that should be retrieved.
	 *            </p>
	 * @return <p>
	 *         The Form that represents the Item with id itemID.
	 *         </p>
	 */
	public Form retrieveItem(int itemID) {

		// Local Declarations
		Form form = null;

		// Retrieve the Form if and only if the Item id is greater than zero and
		// is also in the list of Items.
		if (itemID > 0 && this.itemList.containsKey(itemID)) {
			form = this.itemList.get(itemID).getForm();
		}

		return form;
	}

	/**
	 * <p>
	 * This operation registers an ItemBuilder and thereby a particular Item
	 * class with the ItemManager. This operation is primarily used by the
	 * underlying OSGi framework to publish available Item types to ICE.
	 * </p>
	 * 
	 * @param builder
	 *            <p>
	 *            An instance of ItemBuilder for a particular Item that is
	 *            available to the Core.
	 *            </p>
	 */
	public void registerBuilder(ItemBuilder builder) {

		// Make sure the builder is not null and add it to the list, if it's not
		// there already.
		if (builder != null
				&& !this.itemBuilderList.containsKey(builder.getItemName())) {
			this.itemBuilderList.put(builder.getItemName(), builder);
			// Notify the composite Items of the updated builder list
			for (ICompositeItemBuilder compositeBuilder : compositeBuilders) {
				compositeBuilder.addBuilders(new ArrayList<ItemBuilder>(
						itemBuilderList.values()));
			}
			// Get the list of Items and see if any disabled ones can be
			// re-enabled because this builder is their parent.
			for (int i = 0; i < itemList.values().size(); i++) {
				Item item = (Item) itemList.values().toArray()[i];
				if (!item.isEnabled()
						&& item.getItemBuilderName().equals(
								builder.getItemName())) {
					rebuildItem(builder, item, loadedProject);
					item.disable(false);
					System.out.println("ItemManager Message: "
							+ "Enabling orphaned Item " + item.getName() + " "
							+ item.getId() + " with builder "
							+ builder.getItemName() + ".");
				}

			}
		}

		return;

	}

	/**
	 * <p>
	 * This operation registers an ICompositeItemBuilder with the ItemManager.
	 * This operation is primarily used by the underlying OSGi framework to
	 * publish available Item types to ICE. It functions as registerItem(). The
	 * usual unregisterBuilder() operation should be called to unregister an
	 * ICompositeItemBuilder.
	 * </p>
	 * 
	 * @param builder
	 *            <p>
	 *            The ICompositeItemBuilder that will be used to create an Item
	 *            that depends on others.
	 *            </p>
	 */
	public void registerCompositeBuilder(ICompositeItemBuilder builder) {

		// Register the composite builder if it is real
		if (builder != null) {
			compositeBuilders.add(builder);
			registerBuilder(builder);
		}

	}

	/**
	 * <p>
	 * This operation unregisters an ItemBuilder and thereby a particular Item
	 * class with the ItemManager.
	 * </p>
	 * 
	 * @param builder
	 *            <p>
	 *            An instance of ItemBuilder for a particular Item that is now
	 *            unavailable to the Core.
	 *            </p>
	 */
	public void unregisterBuilder(ItemBuilder builder) {

		if (builder != null
				&& this.itemBuilderList.containsKey(builder.getItemName())) {
			this.itemBuilderList.remove(builder.getItemName());
		}

		return;

	}

	/**
	 * <p>
	 * This operation returns a list of the names of Items that can be created
	 * based on the ItemBuilders that have been registered in the ItemManager.
	 * If no ItemBuilders have been registered, this operation returns null.
	 * </p>
	 * 
	 * @return <p>
	 *         The list of available Items.
	 *         </p>
	 */
	public ArrayList<String> getAvailableBuilders() {

		// Local Declarations
		ArrayList<String> builders = new ArrayList<String>();

		// Pack the list of ItemBuilders into an arraylist, but copy the values
		// to new Strings since HashMap.keySet() returns the set of keys by
		// reference and changes to that list would cause the map to change.
		for (String j : this.itemBuilderList.keySet()) {
			if (itemBuilderList.get(j).isPublishable()) {
				builders.add(new String(j));
			}
		}

		// Check the size and determine whether or not null should be returned
		if (builders.isEmpty()) {
			builders = null;
		}

		return builders;

	}

	/**
	 * <p>
	 * This operation returns a list of the names of Items that can be created
	 * based on the ItemBuilders that have been registered in the ItemManager by
	 * ItemType. If no ItemBuilders have been registered for the specified
	 * ItemType, this operation returns null.
	 * </p>
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<String> getAvailableBuilders(ItemType type) {

		// Local Declarations
		ArrayList<String> builders = new ArrayList<String>();

		// Pack the list of ItemBuilders into an arraylist, but copy the values
		// to new Strings since HashMap.keySet() returns the set of keys by
		// reference and changes to that list would cause the map to change.
		// For this operation only the ones with a specific Item type are
		// required and a linear search is fine since the number of Builders is
		// (or at least should be) small.
		for (ItemBuilder i : this.itemBuilderList.values()) {
			if (i.getItemType() == type) {
				builders.add(i.getItemName());
			}
		}
		// Check the size and determine whether or not null should be returned
		if (builders.isEmpty()) {
			builders = null;
		}
		return builders;

	}

	/**
	 * <p>
	 * This operation returns the status of an Item with the specified id.
	 * </p>
	 * 
	 * @param itemId
	 * @return
	 */
	public FormStatus getItemStatus(int itemId) {

		// Local Declarations
		FormStatus status = null;
		Item item = null;

		// Check the id
		if (itemId > 0) {
			// Get the Item
			item = itemList.get(itemId);
			if (item != null) {
				// Set the status if the Item is actually in the map
				status = item.getStatus();
			}
		}

		return status;
	}

	/**
	 * <p>
	 * This operation sets up the persistence provider that implements the
	 * IPersistenceProvider interface.
	 * </p>
	 * 
	 * @param provider
	 *            <p>
	 *            The persistence provider.
	 *            </p>
	 */
	public void setPersistenceProvider(IPersistenceProvider provider) {

		if (provider != null) {
			System.out.println("ItemManager Message: PersistenceProvider set!");
			this.provider = provider;
		}

	}

	/**
	 * This operation rebuilds an Item from its builder and the current project
	 * space.
	 */
	private void rebuildItem(ItemBuilder builder, Item item,
			IProject projectSpace) {
		
		// Build the proper Item
		Item rebuiltItem = itemBuilderList.get(item.getItemBuilderName())
				.build(projectSpace);

		// Give the project to this temp Item
		item.setProject(projectSpace);
		// Copy over the information from the persistence
		// provider
		rebuiltItem.copy(item);
		// Setup the project space
		rebuiltItem.setProject(projectSpace);
		// Refresh the data on the Item
		rebuiltItem.reloadProjectData();
		// Resubmit the Item's Form so that it can repair its state
		rebuiltItem.submitForm(rebuiltItem.getForm());
		// Register as a observer of the Item
		rebuiltItem.addListener(this);
		// Load the Item into the list
		itemList.put(rebuiltItem.getId(), rebuiltItem);
	}

	/**
	 * <p>
	 * This operation is called to direct the ItemManager to load all Items that
	 * are currently persisted via its IPersistenceProvider. This operation can
	 * only load the Items if the IPersistenceProvider has been set, but it
	 * should not fail if the persistence provider has not been set. It is meant
	 * to be called as a "initialization" or "start up" operation immediately
	 * after the core starts and should not be called frequently.
	 * </p>
	 * <p>
	 * The ItemManager will call the persistence provider to load all available
	 * Items when this operation is called. It tries to load the Items in such a
	 * way that new Items are created with unique ids and old, unused ids are
	 * made available for reuse to prevent fragmentation of the set of Item ids.
	 * (No one wants to have five Items spread across three orders of magnitude
	 * in ids!)
	 * </p>
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The project space that the Items should use for their work. It
	 *            may be null, but it shouldn't be.
	 *            </p>
	 */
	public void loadItems(IProject projectSpace) {

		// Make sure the persistence provider is available before requesting
		// information from it.
		if (provider != null) {
			// Get all of the Items
			ArrayList<Item> oldItems = provider.loadItems();
			// Put all of the Items in to the list if the provider was able to
			// load anything.
			if (oldItems != null && !(oldItems.isEmpty())) {
				// Loop over each Item and load it up
				for (Item item : oldItems) {
					// Reconstruct the Item to use the proper subclass by
					// searching the builders for the builder with the
					// appropriate name.
					if (itemBuilderList.containsKey(item.getItemBuilderName())) {
						ItemBuilder builder = itemBuilderList.get(item
								.getItemBuilderName());
						rebuildItem(builder, item, projectSpace);
					} else {
						System.out.println("ItemManager Message: "
								+ "Builder not found for " + item.getName()
								+ " " + item.getId() + " with builder "
								+ item.getItemBuilderName()
								+ ". It will be disabled.");
						// Otherwise just put the Item in the list, but disable
						// it. It can still be read, just not processed.
						item.disable(true);
						itemList.put(item.getId(), item);
					}
				}
				// Get the keys from the map and sort them
				TreeSet<Integer> keys = new TreeSet<Integer>(itemList.keySet());
				// Set the next sequential id such that it is equal to one plus
				// the last id in the set of Items from the provider. This will
				// keep any new items from possibly colliding with old ones in
				// the map.
				nextSequentialId = keys.last() + 1;
				// Loop over the set of ids and figure out if there are any
				// gaps, which can be reused to keep the ids from fragmenting.
				for (int i = 1; i < nextSequentialId; i++) {
					// If the set doesn't contain i, add it to the reusable id
					// list
					if (!keys.contains(i)) {
						reusableIds.add(i);
					}
				}
			} else {
				// Complain a little bit
				System.out.println("Unable to load items in bulk from "
						+ "the IPersistenceProvider.");
			}
			// Save the project space
			loadedProject = projectSpace;

		}

		return;

	}

	/**
	 * <p>
	 * This operation is called to direct the ItemManager to persist all Items
	 * via its IPersistenceProvider. This operation can only persist the Items
	 * if the IPersistenceProvider has been set, but it should not fail if the
	 * persistence provider has not been set. It is meant to be called as a
	 * "finalization" operation immediately before the core shuts down and
	 * should not be called frequently.
	 * </p>
	 * <p>
	 * The ItemManager will call the persistence provider to persist all
	 * available Items when this operation is called. This is not exactly a
	 * persistence operation per se since all of the Items have already been
	 * initially persisted during creation and updating. Instead, the
	 * persistence provider is asked to update all of the information for the
	 * persisted Items.
	 * </p>
	 * 
	 */
	public void persistItems() {

		// I'm not sure how to check this in tests in a very detailed way. It is
		// only minimally tested now.

		// Update all of the Items in the database if the provider is available.
		if (provider != null) {
			System.out.println("ItemManager Message: Updating all Items with "
					+ "Persistence Provider.");
			for (Item item : itemList.values()) {
				provider.updateItem(item);
			}
		}

	}

	/**
	 * <p>
	 * This operation returns a file handle to the output file for the Item with
	 * the specified id. It returns a handle to the file whether or not it
	 * actually exists and clients should check the File.exists() operation
	 * before attempting to manipulate the file. The description of the output
	 * file can be found elsewhere in the class documentation. This file handle
	 * returned is the <i>real</i> file handle and can be written, but clients
	 * should be careful to only read from the file. It will return null if an
	 * Item with the specified id does not exist.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The id of the Item.
	 *            </p>
	 * @return <p>
	 *         The output file for the specified Item, thoroughly documented
	 *         elsewhere.
	 *         </p>
	 */
	public File getOutputFile(int id) {

		// Local Declarations
		File outputFile = null;

		if (itemList.containsKey(id)) {
			outputFile = itemList.get(id).getOutputFile();
		}

		return outputFile;
	}

	/**
	 * <p>
	 * This operation cancels the process with the specified name for the Item
	 * identified.
	 * </p>
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
	 */
	public FormStatus cancelItemProcess(int itemId, String actionName) {

		// Local Declarations
		FormStatus status = FormStatus.InfoError;

		// Find the item if the id is valid
		if (itemList.keySet().contains(itemId)) {
			Item item = itemList.get(itemId);
			// Try to cancel the task. This kills all processes regardless of
			// name for now.
			status = item.cancelProcess();
		}

		return status;
	}

	/**
	 * <p>
	 * This operation directs the ItemManager to have its Items reload any data
	 * because of updates to projects during runtime.
	 * </p>
	 * 
	 */
	public void reloadItemData() {

		// Send a reload signal to all of the Items
		for (Item item : itemList.values()) {
			item.reloadProjectData();
		}

		return;
	}

	/**
	 * <p>
	 * This operation updates the Item specified by the Message to let it know
	 * that a particular event has occurred in an ICE subsystem, remote ICE
	 * subsystem or external third-party process.
	 * </p>
	 * 
	 * @param msg
	 *            <p>
	 *            The incoming Message.
	 *            </p>
	 * @return <p>
	 *         True if the ItemManager was able to forward the Message and if
	 *         the Item was able to respond to the Message, false otherwise.
	 *         </p>
	 */
	public boolean postUpdateMessage(Message msg) {

		// Local Declarations
		boolean retVal = false;
		int itemId = msg.getItemId();

		System.out.println("Update Message Item Id is " + itemId);
		// Push the message if possible
		if (itemList.containsKey(itemId)) {
			// Grab the Item
			Item messagedItem = itemList.get(itemId);
			// Post the message
			retVal = messagedItem.update(msg);
		}

		return retVal;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemListener#reloadProjectData()
	 */
	@Override
	public void reloadProjectData() {

		// Not threaded for now, but should it be? ~JJB 20130912 17:06

		// Direct all of the Items to reload their data
		System.out.println("ItemManager Message: "
				+ "Reloading all Item project data.");
		for (Item item : itemList.values()) {
			item.reloadProjectData();
		}

		return;
	}

	/**
	 * <p>
	 * This operation will return a list of Identifiables that contain the names
	 * and unique item ids of each Item that is managed by the ItemManager.
	 * </p>
	 * 
	 * @return <p>
	 *         The list of ItemHandles that contains the names and unique ids of
	 *         the Items managed by the ItemManager.
	 *         </p>
	 */
	public ArrayList<Identifiable> retrieveItemList() {

		// Local Declarations
		ArrayList<Identifiable> items = new ArrayList<Identifiable>();

		// Retrieve the list
		for (Identifiable i : this.itemList.values()) {
			items.add(i);
		}

		return items;
	}

	/**
	 * <p>
	 * This operation updates an Item that is managed by the ItemManager using
	 * the Form for that Item and returns the status of that Item. If the
	 * database has been configured by the OSGi and the Core, the ItemManager
	 * will also try to persist the updated Item to the database if it is not
	 * erroneous or working.
	 * </p>
	 * <p>
	 * The ItemManager will call the persistence provider to update the Item
	 * when this operation is called.
	 * </p>
	 * 
	 * @param form
	 *            <p>
	 *            The Form that is associated with the Item that needs to be
	 *            updated.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after the Form is submitted.
	 *         </p>
	 */
	public FormStatus updateItem(Form form) {

		// Local Declarations
		FormStatus status = FormStatus.InfoError;
		int id = -1;
		Item currentItem = null;

		// Get the Item to which the Form belongs
		id = form.getItemID();

		// Make sure the Id is valid and then find its parent
		if (itemList.containsKey(id)) {
			currentItem = itemList.get(id);
			status = currentItem.submitForm(form);
		}

		// Check the status and write to the database if it is enabled
		// Only try to write to the database if the EntityManagers are ready
		if ((status.equals(FormStatus.Processed) || status
				.equals(FormStatus.ReadyToProcess)) && provider != null) {
			provider.updateItem(currentItem);
		}

		return status;
	}

	/**
	 * <p>
	 * This operation processes the Item with the specified id and action. The
	 * action name must be one of the set of actions from the Form that
	 * represents the Item with the specified id.
	 * </p>
	 * <p>
	 * It is possible that ICE may require information in addition to that which
	 * was requested in the original Form, such as for a username and password
	 * for a remote machine. If this is the case, processItem will return
	 * FormStatus.NeedsInfo and a new, temporary Form will be available for the
	 * Item by calling getItem(). Once this new Form is submitted It is possible
	 * that ICE may require information in addition to that which was requested
	 * in the original Form, such as for a username and password for a remote
	 * machine. If this is the case, processItem will return
	 * FormStatus.NeedsInfo and a new, temporary Form will be available for the
	 * Item by calling getItem(). Once this new Form is submitted (by calling
	 * updateItem() with the completed Form), the Item will finish processing.
	 * </p>
	 * <p>
	 * The ItemManager will call the persistence provider to update the Item
	 * when this operation is called.
	 * </p>
	 * 
	 * @param itemId
	 *            <p>
	 *            The identification number of the Item.
	 *            </p>
	 * @param actionName
	 *            <p>
	 *            The name of the action that should be performed for the Item.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after the action has been performed.
	 *         </p>
	 */
	public FormStatus processItem(int itemId, String actionName) {

		// Local Declarations
		FormStatus status = FormStatus.InfoError;
		Item tmpItem = null;

		// Check the Item id and actionName for validity
		if (itemId > 0 && actionName != null) {
			// Retrieve the Item from the map if it exists
			tmpItem = itemList.get(itemId);
			if (tmpItem != null) {
				status = tmpItem.process(actionName);
			}
		}

		return status;
	}

	/**
	 * <p>
	 * This operation will delete the item with id itemID.
	 * </p>
	 * <p>
	 * The ItemManager will call the persistence provider to delete the Item
	 * when this operation is called.
	 * </p>
	 * 
	 * @param itemID
	 *            <p>
	 *            The id of the item that should be deleted.
	 *            </p>
	 * @return <p>
	 *         True if the Item was deleted, false if something went wrong.
	 *         </p>
	 */
	public boolean deleteItem(int itemID) {

		// Local Declarations
		boolean retVal = false;

		// Try to delete the Item if and only if the Item's id is greater than
		// zero and it is in the list of Items and set the return value.
		if (itemID > 0 && this.itemList.containsKey(itemID)) {
			// If the provider exists, delete the Item from the provider
			if (this.provider != null) {
				Item item = itemList.get(itemID);
				System.out.println("ItemManager Message: Deleting Item "
						+ item.getName() + " " + item.getId()
						+ " from provider");
				provider.deleteItem(itemList.get(itemID));
			}
			// Remove the Item from the list
			retVal = (this.itemList.remove(itemID) != null || false);
			// Add the id to the list so that it can be reused
			reusableIds.add(itemID);
		}

		return retVal;

	}
}
