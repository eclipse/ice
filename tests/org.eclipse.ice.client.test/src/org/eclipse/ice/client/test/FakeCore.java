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

import org.eclipse.ice.core.iCore.ICore;

import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.net.URI;
import java.io.FileWriter;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.ItemBuilder;

/**
 * <!-- begin-UML-doc -->
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
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeCore implements ICore {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The status of the last call to the process() operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FormStatus lastProcessStatus;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This flag is true if updateItem() was called after processItem() returned
	 * FormStatus.NeedsInfo and false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean updateItemCalled;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if an output file was retrieved, false if not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean outputFileRetrieved = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if a file was imported, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean imported;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the client attempted to cancel an Item process request, false
	 * otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean cancelled = false;

	/**
	 * Field variable for faking that the Items were deleted
	 */
	private boolean deleted = false;

	/**
	 * The Constructor
	 */
	public FakeCore() {
		lastProcessStatus = FormStatus.InfoError;
		imported = false;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the status of the last call to process().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The status of the last call to process().
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus getLastProcessStatus() {
		// begin-user-code
		return lastProcessStatus;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation resets the value of the last process status to
	 * FormStatus.Info error so that the test can be sure that the event is
	 * processed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code

		lastProcessStatus = FormStatus.InfoError;
		updateItemCalled = false;
		imported = false;
		cancelled = false;

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if updateItem() was called after a call to
	 * process() that returned FormStatus.NeedsInfo.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the Item was updated, false if not.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean itemUpdated() {
		// begin-user-code
		return updateItemCalled;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if an output file was retrieved, false if not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if a file was retrieved, false if not.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean outputFileRetrieved() {
		// begin-user-code
		return outputFileRetrieved;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the FakeCore was asked to import a file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean fileImported() {
		// begin-user-code
		return imported;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the true if the client previously tried to cancel
	 * a process request.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The status of the last call to process().
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasCancelled() {
		// begin-user-code

		return cancelled;
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
		// TODO Auto-generated method stub
		return "0";
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
		return new Form();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#registerItem(ItemBuilder itemBuilder)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
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
	@Override
	public void unregisterItem(ItemBuilder itemBuilder) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void deleteItem(String itemId) {
		// begin-user-code

		deleted = true;

		// end-user-code
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEList<String> getAvailableItemTypes() {
		// begin-user-code

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

		// end-user-code
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
		System.out.println("Item id = " + itemId + " , actionName = "
				+ actionName);

		return lastProcessStatus;
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

		// Local Declarations
		File outputFile = null;

		// Only create this file if the caller is behaving well.
		if (id > 0) {
			// Setup the file
			outputFile = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator") + "fakeCoreTestFile");
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

		// Throw the flag and return it
		cancelled = true;
		return FormStatus.ReadyToProcess;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICore#importFile(URI file)
	 */
	public void importFile(URI file) {
		// begin-user-code
		if (file != null) {
			imported = true;
		} else {
			System.out.println("FakeCore Message: " + "Imported file is null!");
		}
		return;
		// end-user-code
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
}