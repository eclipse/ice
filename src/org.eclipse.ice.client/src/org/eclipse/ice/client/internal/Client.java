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

import org.eclipse.ice.client.common.internal.ClientHolder;
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
 * 
 * @author Jay Jay Billings
 */
public class Client implements IUpdateEventListener, IProcessEventListener,
		ISimpleResourceProvider, IWidgetClosedListener, IClient {
	/**
	 * 
	 */
	private ICore iCore;

	/**
	 * <p>
	 * The set of ErroBoxWidgets used by the Client.
	 * </p>
	 * 
	 */
	private ArrayList<IErrorBox> errorBoxWidgets;

	/**
	 * 
	 */
	private IWidgetFactory iWidgetFactory;

	/**
	 * <p>
	 * The set of IFormWidgets used by the Client. This set is stored in a
	 * HashTable with Forms as keys and IFormWidgets as the values.
	 * </p>
	 */
	private Hashtable<Integer, IFormWidget> formWidgetTable;

	/**
	 * <p>
	 * This AtomicBoolean is true if the IExtraInfoWidget used by the Client was
	 * closed OK and is false otherwise.
	 * </p>
	 * 
	 */
	private AtomicBoolean widgetClosedOK;

	/**
	 * <p>
	 * This AtomicBoolean is true if the IExtraInfoWidget used by the Client was
	 * cancelled and is false otherwise.
	 * </p>
	 * 
	 */
	private AtomicBoolean widgetCancelled;

	/**
	 * <p>
	 * The unique id assigned to this client by the ICE server.
	 * </p>
	 * 
	 */
	private int clientId = -1;

	/**
	 * A set of status messages used by the core to describe the different
	 * states of Items.
	 */
	HashMap<FormStatus, String> statusMessageMap = new HashMap<FormStatus, String>();

	/**
	 * <p>
	 * The Constructor
	 * </p>
	 * 
	 */
	public Client() {

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

		// Set the reference to this in the Singleton for the widget classes to retrieve as needed.
		ClientHolder.setClient(this);
		
	}

	/**
	 * <p>
	 * This private operation is called by the implementations of
	 * IClient.processItem() and IProcessEventListener.processSelected(). It
	 * calls the ICore and directs it to process an Item. This operation
	 * launches a FormProcessor to handle polling and update the IFormWidget for
	 * the Item as it is processed by the Core.
	 * </p>
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
	 */
	private void processItem(IFormWidget formWidget, String actionName) {
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
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#setCoreService(ICore core)
	 */
	public void setCoreService(ICore core) {
		System.out.println("IClient Message: Core service set.");
		this.iCore = core;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#createItem(String itemType)
	 */
	public int createItem(String itemType) {

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
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#setUIWidgetFactory(IWidgetFactory widgetFactory)
	 */
	public void setUIWidgetFactory(IWidgetFactory widgetFactory) {

		iWidgetFactory = widgetFactory;

		if (iWidgetFactory != null) {
			System.out.println("IClient Message: Widget Factory set!");
		} else {
			System.out
					.println("IClient Message: Widget Factory set, but is null.");
		}
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#loadItem(int itemId)
	 */
	public void loadItem(int itemId) {

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

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#throwSimpleError(String error)
	 */
	public void throwSimpleError(String error) {

		// Local Declarations
		IErrorBox errorBox = null;

		// Make sure the error isn't null and post it
		if (error != null) {
			errorBox = iWidgetFactory.getErrorBox();
			errorBox.setErrorString(error);
			errorBox.display();
		}

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#getAvailableItemTypes()
	 */
	public ArrayList<String> getAvailableItemTypes() {

		// Local Declarations
		ArrayList<String> types = null;

		// Get the types
		types = this.iCore.getAvailableItemTypes().getList();

		return types;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#processItem(int itemId, String actionName)
	 */
	public void processItem(int itemId, String actionName) {

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
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#connectToCore(String hostname, int port)
	 */
	public boolean connectToCore(String hostname, int port) {

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
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#getItems()
	 */
	public ArrayList<Identifiable> getItems() {
		return iCore.getItemList();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#deleteItem(int id)
	 */
	public void deleteItem(int id) {

		// Forward the call
		iCore.deleteItem(String.valueOf(id));

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#importFile(URI file)
	 */
	public void importFile(URI file) {

		// Just forward the call
		iCore.importFile(file);

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateEventListener#formUpdated(Form form)
	 */
	public void formUpdated(Form form) {

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

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IProcessEventListener#processSelected(Form form, String process)
	 */
	public void processSelected(Form form, String process) {

		// Forward the request to process the form on to the Core. The
		// actual processing is done by the Item that is represented by the
		// Form, so send the ItemID along.
		processItem(form.getItemID(), process);

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IProcessEventListener#cancelRequested(Form form, String process)
	 */
	public void cancelRequested(Form form, String process) {

		// Forward the request to the core so that it can try to shut the
		// process down.
		iCore.cancelItemProcess(form.getId(), process);

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ISimpleResourceProvider#loadResource(ICEResource resource)
	 */
	public void loadResource(ICEResource resource) {

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
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#closedOK()
	 */
	public void closedOK() {

		widgetClosedOK.set(true);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#cancelled()
	 */
	public void cancelled() {

		widgetCancelled.set(true);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IClient#getFileSystem()
	 */
	public Object getFileSystem() {
		// TODO Auto-generated method stub
		return iCore.getFileSystem(1);
	}

	@Override
	public int importFileAsItem(URI file, String itemType) {

		// Pass the call on to the core
		return Integer.valueOf(iCore.importFileAsItem(file, itemType));

	}

}