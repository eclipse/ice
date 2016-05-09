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
package org.eclipse.ice.client.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ice.core.iCore.ICore;
import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.ItemBuilder;

/**
 * <p>
 * FakeCore is a Core used during unit testing by ClientTester to break the
 * class dependency between IClient and ICore. It isolates Client for unit
 * testing.
 * </p>
 * <p>
 * The FakeCore defines four types of Items that are "available" for the
 * getAvailableItemTypes operation: Red, Orange, Yellow, Green.
 * </p>
 * <p>
 * An update request passed to the FakeCore will return successfully if the id
 * of the Form is positive and fail with FormStatus.InfoError if the id is
 * exactly equal to 8675309. The update will return FormStatus.InReview if the
 * id of the Form is greater than 100. The name of the Form will be changed to
 * "passed" if the FakeCore.updateItem operation was called.
 * </p>
 * <p>
 * A request to process an Item with the FakeCore will return
 * FormStatus.Processed if the Item id is positive and the ActionName is not
 * null. It will return FormStatus.InfoError otherwise. If the ActionName is
 * equal to "NeedsInfo" then the FakeCore will return FormStatus.NeedsInfo to
 * make the Client launch an IExtraInfoWidget. The status from the last call to
 * the process() operation can be retrieved by calling
 * FakeCore.getLastProcessStatus().
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeCore implements ICore {
	/**
	 * <p>
	 * The status of the last call to the process() operation.
	 * </p>
	 * 
	 */
	private FormStatus lastProcessStatus;

	/**
	 * <p>
	 * This flag is true if updateItem() was called after processItem() returned
	 * FormStatus.NeedsInfo and false otherwise.
	 * </p>
	 * 
	 */
	private boolean updateItemCalled;

	/**
	 * <p>
	 * True if an output file was retrieved, false if not.
	 * </p>
	 * 
	 */
	private boolean outputFileRetrieved = false;

	/**
	 * <p>
	 * True if a file was imported, false otherwise.
	 * </p>
	 * 
	 */
	private boolean imported;

	/**
	 * True if the Item's name was changed, false otherwise.
	 */
	private boolean itemNameChanged;

	/**
	 * <p>
	 * True if the client attempted to cancel an Item process request, false
	 * otherwise.
	 * </p>
	 * 
	 */
	private boolean cancelled = false;

	/**
	 * Field variable for faking that the Items were deleted
	 */
	private boolean deleted = false;

	/**
	 * Field variable for faking that the Item was loaded
	 */
	private boolean loaded = false;

	/**
	 * The Constructor
	 */
	public FakeCore() {
		lastProcessStatus = FormStatus.InfoError;
		imported = false;
		itemNameChanged = false;
	}

	/**
	 * <p>
	 * This operation returns the status of the last call to process().
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The status of the last call to process().
	 *         </p>
	 */
	public FormStatus getLastProcessStatus() {
		return lastProcessStatus;
	}

	/**
	 * <p>
	 * This operation resets the value of the last process status to
	 * FormStatus.Info error so that the test can be sure that the event is
	 * processed.
	 * </p>
	 * 
	 */
	public void reset() {

		lastProcessStatus = FormStatus.InfoError;
		updateItemCalled = false;
		imported = false;
		cancelled = false;
		loaded = false;
		itemNameChanged = false;

		return;

	}

	/**
	 * <p>
	 * This operation returns true if updateItem() was called after a call to
	 * process() that returned FormStatus.NeedsInfo.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         True if the Item was updated, false if not.
	 *         </p>
	 */
	public boolean itemUpdated() {
		return updateItemCalled;
	}

	/**
	 * <p>
	 * True if an output file was retrieved, false if not.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         True if a file was retrieved, false if not.
	 *         </p>
	 */
	public boolean outputFileRetrieved() {
		return outputFileRetrieved;
	}

	/**
	 * <p>
	 * This operation returns true if the FakeCore was asked to import a file.
	 * </p>
	 * 
	 * @return
	 */
	public boolean fileImported() {
		return imported;
	}

	/**
	 * <p>
	 * This operation returns the true if the client previously tried to cancel
	 * a process request.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The status of the last call to process().
	 *         </p>
	 */
	public boolean wasCancelled() {

		return cancelled;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#connect()
	 */
	@Override
	public String connect() {
		// TODO Auto-generated method stub
		return "0";
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

	@Override
	public String createItem(String itemType) {

		// Return -1 if it is the case with the error
		if ("Spray Starch".equals(itemType)) {
			return "-1";
		}
		return "2";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#deleteItem(String itemId)
	 */
	@Override
	public void deleteItem(String itemId) {

		deleted = true;

	}

	@Override
	public FormStatus getItemStatus(Integer id) {
		// Set the proper status message
		if (this.lastProcessStatus.equals(FormStatus.NeedsInfo)) {
			return FormStatus.NeedsInfo;
		} else {
			return FormStatus.Processed;
		}
	}

	@Override
	public Form getItem(int itemId) {

		// Local Declarations
		Form retForm = null;

		// Create the Form if the id is valid
		if (itemId > 0) {
			retForm = new Form();
			retForm.setItemID(itemId);
		}

		return retForm;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getAvailableItemTypes()
	 */
	@Override
	public ICEList<String> getAvailableItemTypes() {

		// Local Declarations
		ArrayList<String> types = new ArrayList<String>();
		ICEList<String> retList = new ICEList<String>();

		// Add the types to the list
		types.add("Red");
		types.add("Orange");
		types.add("Yellow");
		types.add("Green");

		// Fix the list
		retList.setList(types);

		return retList;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#updateItem(Form form, int uniqueClientId)
	 */
	@Override
	public FormStatus updateItem(Form form, int uniqueClientId) {

		// Change the name
		if (form.getId() == 8675309) {
			System.out.println("FakeCore Message: Failing with "
					+ "FormStatus.InfoError!");
			return FormStatus.InfoError;
		}

		// Set the update flag
		updateItemCalled = true;

		// Setup the status and return
		if (form.getId() > 100) {
			return FormStatus.InReview;
		} else if (form.getId() < 0) {
			return FormStatus.InfoError;
		} else {
			form.setName("passed");
			System.out.println("FakeCore Message: Name = " + form.getName());
			return FormStatus.ReadyToProcess;
		}

	}

	@Override
	public FormStatus processItem(int itemId, String actionName,
			int uniqueClientId) {

		// Check the Item id and the action name to make sure they are
		// acceptable
		if (itemId > 0 && actionName != null) {
			// Set the last status value and return it
			if ("NeedsInfo".equals(actionName)) {
				lastProcessStatus = FormStatus.NeedsInfo;
			} else {
				lastProcessStatus = FormStatus.Processed;
			}
		} else {
			// Otherwise setup an InfoError
			lastProcessStatus = FormStatus.InfoError;
		}

		System.out.println("FakeCore Message: Call to process() finished.");
		System.out.println(
				"Item id = " + itemId + " , actionName = " + actionName);

		return lastProcessStatus;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemList()
	 */
	@Override
	public ArrayList<Identifiable> getItemList() {

		// Local Declarations
		ArrayList<Identifiable> items = new ArrayList<Identifiable>();
		ICEObject itemObject = new ICEObject();

		// Set the particular details
		itemObject.setName("Kathryn Janeway");
		itemObject.setId(2);
		itemObject.setDescription("Captain of the starship Voyager. "
				+ "Not actually #2. #1 on that ship.");

		if (!deleted) {
			// Add the object to the list
			items.add(itemObject);
		} else {
			// Throw the flag so that it will work correctly again
			deleted = false;
		}
		return items;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#getItemOutputFile(int id)
	 */
	@Override
	public File getItemOutputFile(int id) {

		// Local Declarations
		File outputFile = null;

		// Only create this file if the caller is behaving well.
		if (id > 0) {
			// Setup the file
			outputFile = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator")
					+ "fakeCoreTestFile");
			try {
				// Create file writing streams
				FileWriter fstream = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fstream);
				// Write to the stream
				out.write("Client Item Output Test File");
				// Flush the streams
				out.flush();
				fstream.flush();
				// Close the output streams
				out.close();
				fstream.close();
			} catch (Exception e) {
				// Complain
				System.err.println("FakeCore Message: " + e.getMessage());
			}
			// Hoist the colors
			outputFileRetrieved = true;
		}

		return outputFile;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#cancelItemProcess(int itemId, String actionName)
	 */
	@Override
	public FormStatus cancelItemProcess(int itemId, String actionName) {

		// Throw the flag and return it
		cancelled = true;
		return FormStatus.ReadyToProcess;

	}

	@Override
	public String importFileAsItem(URI file, String itemType) {

		// Local Declarations
		String returnString = String.valueOf(0);

		if (file != null && itemType != null) {
			returnString = String.valueOf(1);
			imported = true;
		}

		return returnString;

	}

	@Override
	public String postUpdateMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createItem(String itemType, IProject project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Form loadItem(IFile itemFile) {
		loaded = true;
		return new Form();
	}

	public boolean wasLoaded() {
		return loaded;
	}

	@Override
	public String importFileAsItem(URI file, String itemType,
			IProject project) {
		// Local Declarations
		String returnString = String.valueOf(0);

		if (file != null && itemType != null) {
			returnString = String.valueOf(1);
			imported = true;
		}

		return returnString;
	}

	@Override
	public String importFileAsItem(IFile file, String itemType) {
		// Local Declarations
		String returnString = String.valueOf(0);

		// if (file != null && itemType != null) {
		returnString = String.valueOf(1);
		imported = true;
		// }

		return returnString;
	}

	@Override
	public String importFileAsItem(URI file, String itemType,
			String projectName) {
		// Local Declarations
		String returnString = String.valueOf(0);

		if (file != null && itemType != null) {
			returnString = String.valueOf(1);
			imported = true;
		}

		return returnString;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#importFile(URI file)
	 */
	@Override
	public void importFile(URI file) {
		if (file != null) {
			imported = true;
		} else {
			System.out.println("FakeCore Message: " + "Imported file is null!");
		}
		return;
	}

	@Override
	public void importFile(URI file, IProject project) {
		if (file != null) {
			imported = true;
		} else {
			System.out.println("FakeCore Message: " + "Imported file is null!");
		}
		return;
	}

	@Override
	public void importFile(URI file, String projectName) {
		if (file != null) {
			imported = true;
		} else {
			System.out.println("FakeCore Message: " + "Imported file is null!");
		}
		return;
	}

	public boolean itemNameChanged() {
		return itemNameChanged;
	}

	@Override
	public void renameItem(int itemID, String name) {
		itemNameChanged = true;
	}
}