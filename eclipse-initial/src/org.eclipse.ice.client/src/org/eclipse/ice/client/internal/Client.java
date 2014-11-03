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

import org.eclipse.ice.iclient.IClient;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;
import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;
import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;

import static org.eclipse.ice.client.internal.ItemProcessor.*;
import org.eclipse.ice.core.iCore.ICore;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.iclient.uiwidgets.IErrorBox;
import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.iclient.uiwidgets.IFormWidget;

import java.util.Hashtable;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;

import java.net.URI;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.FormStatus;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The Client class is a base class for clients of the Core. It's primary
 * function is to manage calls to and from the Core to and from whatever user
 * interface is provided by IWidgetFactory.
 * </p>
 * <p>
 * The Client realizes the IClient interface and is registered as an OSGi
 * service. It requires an implementation of the IWidgetFactory so that it can
 * present data from ICE to users. It also implements IUpdateEvenListener,
 * IProcessEventListener, ISimpleResourceProvider and IWidgetClosedLIstener so
 * that it can handle notifications and requests from the widgets and data
 * structures.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author jaybilly
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Client implements IUpdateEventListener, IProcessEventListener,
		ISimpleResourceProvider, IWidgetClosedListener, IClient {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICore iCore;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The set of ErroBoxWidgets used by the Client.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IErrorBox> errorBoxWidgets;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IWidgetFactory iWidgetFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The set of IFormWidgets used by the Client. This set is stored in a
	 * HashTable with Forms as keys and IFormWidgets as the values.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private Hashtable<Integer, IFormWidget> formWidgetTable;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This AtomicBoolean is true if the IExtraInfoWidget used by the Client was
	 * closed OK and is false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicBoolean widgetClosedOK;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This AtomicBoolean is true if the IExtraInfoWidget used by the Client was
	 * cancelled and is false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicBoolean widgetCancelled;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The unique id assigned to this client by the ICE server.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int clientId = -1;

	/**
	 * A set of status messages used by the core to describe the different
	 * states of Items.
	 */
	HashMap<FormStatus, String> statusMessageMap = new HashMap<FormStatus, String>();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Client() {
		// begin-user-code

		// Create the lists of widgets
		errorBoxWidgets = new ArrayList<IErrorBox>();
		formWidgetTable = new Hashtable<Integer, IFormWidget>();

		// Setup the Atomics for the extra info widget
		widgetClosedOK = new AtomicBoolean();
		widgetClosedOK.set(false);
		widgetCancelled = new AtomicBoolean();
		widgetCancelled.set(false);

		// Setup the message map. This map contains the messages that will be
		// posted to the IFormWidget based on the status of the process.
		statusMessageMap.put(FormStatus.Processed, "Done!");
		statusMessageMap.put(FormStatus.Processing, "Processing Form...");
		statusMessageMap.put(FormStatus.InfoError, "The Form contains an error"
				+ " and cannot be processed.");
		statusMessageMap.put(FormStatus.ReadyToProcess, "Ready to process.");
		statusMessageMap.put(FormStatus.NeedsInfo,
				"The Form requires additional information before "
						+ "it can be processed.");
		statusMessageMap.put(FormStatus.InReview, "In review...");
		statusMessageMap.put(FormStatus.Unacceptable, "This Form will not be "
				+ "processed or updated. It should be considered read-only.");

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This private operation is called by the implementations of
	 * IClient.processItem() and IProcessEventListener.processSelected(). It
	 * calls the ICore and directs it to process an Item. This operation
	 * launches a FormProcessor to handle polling and update the IFormWidget for
	 * the Item as it is processed by the Core.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param formWidget
	 *            <p>
	 *            The Form that represents the Item that will be processed.
	 *            </p>
	 * @param actionName
	 *            <p>
	 *            The name of the action which should be performed when the Item
	 *            is processed.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void processItem(IFormWidget formWidget, String actionName) {
		// begin-user-code
		// TODO Auto-generated method stub

		// Local Declarations
		IExtraInfoWidget infoWidget = null;
		IStreamingTextWidget textWidget = null;
		ItemProcessor processor = new ItemProcessor();
		Thread processorThread = null;

		// Check the Item Id and forward the request if it is valid
		if (formWidget != null) {
			// Load up the processor
			processor.setActionName(actionName);
			processor.setCore(iCore);
			infoWidget = iWidgetFactory.getExtraInfoWidget();
			infoWidget.setForm(formWidget.getForm());
			textWidget = iWidgetFactory.getStreamingTextWidget();
			processor.setInfoWidget(infoWidget);
			processor.setFormWidget(formWidget);
			processor.setStreamingTextWidget(textWidget);
			processor.setItemId(formWidget.getForm().getItemID());
			// Launch the processor on another thread
			processorThread = new Thread(processor);
			processorThread.start();
		} else {
			// Otherwise notify the use that the Item is invalid
			throwSimpleError("IClient Message: "
					+ "Item has no parent widget in this client.");
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#setCoreService(ICore core)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCoreService(ICore core) {
		// begin-user-code
		System.out.println("IClient Message: Core service set.");
		this.iCore = core;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#createItem(String itemType)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int createItem(String itemType) {
		// begin-user-code

		// Local Declarations
		int itemId = -1;

		// Create the Item
		itemId = Integer.valueOf(iCore.createItem(itemType));

		// FIXME - Get the status! Need ItemStatus type or something

		// Either load the Item or throw an error
		if (itemId > 0) {// FIXME Status check!
			loadItem(itemId);
		} else if (itemId <= 0) {
			throwSimpleError("Unable to create Item of type " + itemType + ".");
		}

		return itemId;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#setUIWidgetFactory(IWidgetFactory widgetFactory)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUIWidgetFactory(IWidgetFactory widgetFactory) {
		// begin-user-code

		iWidgetFactory = widgetFactory;

		if (iWidgetFactory != null) {
			System.out.println("IClient Message: Widget Factory set!");
		} else {
			System.out
					.println("IClient Message: Widget Factory set, but is null.");
		}
		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#loadItem(int itemId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadItem(int itemId) {
		// begin-user-code

		// Local Declarations
		IFormWidget formWidget = null;
		Form form = null;
		FormStatus formStatus = FormStatus.ReadyToProcess;

		// If the Item exists
		if (itemId > 0) {
			// Get the Form
			form = iCore.getItem(itemId);
			// Load the editor
			formWidget = iWidgetFactory.getFormWidget(form.getName());
			formWidget.setForm(form);
			// Display the editor
			formWidget.display();
			// Set the initial status of the Form
			formStatus = iCore.getItemStatus(itemId);
			formWidget.updateStatus(statusMessageMap.get(iCore
					.getItemStatus(itemId)));
			// If the FormStatus signifies that the Form is absolutely
			// unacceptable, then the user should be warned.
			if (formStatus.equals(FormStatus.Unacceptable)) {
				formWidget.disable(true);
				throwSimpleError("This Form has been set to a read-only mode by "
						+ "ICE. Please be advised that it can not be upated"
						+ " or processed.");
			}
			// Register for updates
			formWidget.registerUpdateListener(this);
			formWidget.registerProcessListener(this);
			formWidget.registerResourceProvider(this);
			System.out.println("IClient Message: Loaded Item " + itemId + ", "
					+ form.getName());
			// Store the widget in the table of FormWidgets
			formWidgetTable.put(itemId, formWidget);
		} else {
			// Complain otherwise
			throwSimpleError("Unable to load Item " + itemId + ".");
		}

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#throwSimpleError(String error)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void throwSimpleError(String error) {
		// begin-user-code

		// Local Declarations
		IErrorBox errorBox = null;

		// Make sure the error isn't null and post it
		if (error != null) {
			errorBox = iWidgetFactory.getErrorBox();
			errorBox.setErrorString(error);
			errorBox.display();
		}

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#getAvailableItemTypes()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableItemTypes() {
		// begin-user-code

		// Local Declarations
		ArrayList<String> types = null;

		// Get the types
		types = this.iCore.getAvailableItemTypes().getList();

		return types;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#processItem(int itemId, String actionName)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processItem(int itemId, String actionName) {
		// begin-user-code

		// Local Declarations
		Form itemForm = null;

		// Check the Item Id and forward the request if it is valid
		if (itemId > 0) {
			// Find the Form widget in the table. Linear search for now since
			// the list should be small.
			for (Integer i : formWidgetTable.keySet()) {
				if (i == itemId) {
					itemForm = formWidgetTable.get(i).getForm();
					break;
				}
			}
			// Process the item
			if (itemForm != null) {
				System.out.println("IClient Message: Processing Item " + itemId
						+ ", " + itemForm.getName());
				processItem(formWidgetTable.get(itemId), actionName);
			}
		} else if (itemId < 0 || itemForm == null) {
			// Otherwise notify the use that the Item is invalid
			throwSimpleError("The Item id is invalid. "
					+ "Please double check it and try again "
					+ "or notify your systems administrator.");
		}
		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#connectToCore(String hostname, int port)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean connectToCore(String hostname, int port) {
		// begin-user-code

		// Local Declarations
		boolean status = false;
		int realPort = (port <= 0) ? 80 : port, newClientId = -1;
		RemoteCoreProxy proxyCore = null;

		// Connect the RemoteClientProxy if the hostname is not null
		if (hostname != null) {
			proxyCore = new RemoteCoreProxy();
			proxyCore.setHost(hostname);
			proxyCore.setPort(realPort);
			// Connect and parse the response as an integer. It should be
			// greater than 0.
			newClientId = Integer.parseInt(proxyCore.connect("ice", "veryice"));
			// If the id is acceptable, set the status and core reference.
			if (newClientId > 0) {
				status = true;
				iCore = proxyCore;
			}
		}

		System.out.println("IClient Message: Connected to core... " + status);

		return status;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#getItems()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Identifiable> getItems() {
		// begin-user-code
		return iCore.getItemList();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#deleteItem(int id)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void deleteItem(int id) {
		// begin-user-code

		// Forward the call
		iCore.deleteItem(String.valueOf(id));

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#importFile(URI file)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void importFile(URI file) {
		// begin-user-code

		// Just forward the call
		iCore.importFile(file);

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateEventListener#formUpdated(Form form)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void formUpdated(Form form) {
		// begin-user-code

		// Local Declarations
		int formId = -1;
		FormStatus status = FormStatus.InfoError;

		// Process the Form if it is not null
		if (form != null) {
			formId = form.getId();

			// Post the update //FIXME! Client ID!
			status = this.iCore.updateItem(form, 1);

			// Update the Form if needed, skip FormStatus.InReview for now.
			// FIXME! - need FormStatus.InReview
			if (!status.equals(FormStatus.InfoError)
					&& !status.equals(FormStatus.Unacceptable)) {
				form = iCore.getItem(formId);
				// Update the status of the Item update
				if (formWidgetTable.containsKey(form.getItemID())) {
					String statusMessage = statusMessageMap.get(status);
					formWidgetTable.get(form.getItemID()).updateStatus(
							statusMessage);
				}
			} else {
				// Notify the user that there is some invalid information in the
				// Form
				throwSimpleError("Form contains invalid information. "
						+ "Please review it for completeness and "
						+ "accuracy and resubmit.");
			}
		} else {
			// Otherwise let the user know
			throwSimpleError("Fatal Error: "
					+ "Form returned to Client can not be null!");
		}

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IProcessEventListener#processSelected(Form form, String process)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processSelected(Form form, String process) {
		// begin-user-code

		// Forward the request to process the form on to the Core. The
		// actual processing is done by the Item that is represented by the
		// Form, so send the ItemID along.
		processItem(form.getItemID(), process);

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IProcessEventListener#cancelRequested(Form form, String process)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void cancelRequested(Form form, String process) {
		// begin-user-code

		// Forward the request to the core so that it can try to shut the
		// process down.
		iCore.cancelItemProcess(form.getId(), process);

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ISimpleResourceProvider#loadResource(ICEResource resource)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadResource(ICEResource resource) {
		// begin-user-code

		// Make sure the ICEResource is not null
		if (resource != null) {
			// Get an ITextEditor from the WidgetFactory
			ITextEditor textEditor = iWidgetFactory.getTextEditor();
			// Set the Resource
			textEditor.setResource(resource);
			// Display the widget
			textEditor.display();
		} else {
			throwSimpleError("The resource that you asked to load does not "
					+ "exist or is erroneously linked.");
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#closedOK()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void closedOK() {
		// begin-user-code

		widgetClosedOK.set(true);

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#cancelled()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void cancelled() {
		// begin-user-code

		widgetCancelled.set(true);

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#getFileSystem()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object getFileSystem() {
		// begin-user-code
		// TODO Auto-generated method stub
		return iCore.getFileSystem(1);
		// end-user-code
	}

	@Override
	public int importFileAsItem(URI file, String itemType) {

		// Pass the call on to the core
		return Integer.valueOf(iCore.importFileAsItem(file, itemType));

	}

}