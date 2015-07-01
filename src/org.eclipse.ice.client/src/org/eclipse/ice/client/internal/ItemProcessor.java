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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.core.iCore.ICore;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;

/**
 * <p>
 * This class is responsible for processing a Form for a specific Item. It
 * implements the Runnable interface and should be run on a separate thread from
 * the UI. However, it is not thread safe and all of its required information
 * must be set before it is launched. Setting the values while the thread is
 * running will cause it to fail. This strategy is employed to keep clients from
 * updating the process request mid-stream, which could cause problems much
 * worse than a thread exception in the ICE core, because the ItemProcessor is
 * only meant to be launched once per process request. It realizes the
 * IWidgetClosedListener interface so that it can be notified by
 * IExtraInfoWidgets when they are closed. The ItemProcessor will attempt to
 * push streaming input if it is available for an Item to the
 * IStreamingTextWidget that is supplied during configuration. It will also set
 * the label of the widget.
 * </p>
 * <p>
 * All of the set operations, with the exception of setPollTime() and
 * setStreamingOutputWidget(), must be called before the processor can be
 * launched. There is a default polling time configured in the processor (100ms)
 * and if a streaming text widget is not set the ItemProcessor will not push the
 * output.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ItemProcessor implements IWidgetClosedListener, Runnable {
	/**
	 * <p>
	 * A reference to an IExtraInfoWidget that can be used to gather extra
	 * information if the process request requires it.
	 * </p>
	 * 
	 */
	private IExtraInfoWidget infoWidget;
	/**
	 * <p>
	 * A reference to the FormWidget that rendered the Form.
	 * </p>
	 * 
	 */
	private IFormWidget formWidget;
	/**
	 * <p>
	 * The name of the action that should be performed on the Item.
	 * </p>
	 * 
	 */
	private String actionName;
	/**
	 * <p>
	 * The id of the Item that should be processed.
	 * </p>
	 * 
	 */
	private int itemId;
	/**
	 * <p>
	 * The ICore to which the ItemProcessor should direct its requests.
	 * </p>
	 * 
	 */
	private ICore iceCore;

	/**
	 * <p>
	 * The period for which the ItemProcessor should poll the Core for updates
	 * in units of milliseconds. The default value is 100 milliseconds, 0.1
	 * seconds.
	 * </p>
	 * 
	 */
	private int pollTime = 100;

	/**
	 * <p>
	 * This AtomicBoolean is true if the IExtraInfoWidget used by the
	 * ItemProcessor was closed OK and is false otherwise.
	 * </p>
	 * 
	 */
	private AtomicBoolean widgetClosedOK;
	/**
	 * <p>
	 * This AtomicBoolean is true if the IExtraInfoWidget used by the
	 * ItemProcessor was cancelled and is false otherwise.
	 * </p>
	 * 
	 */
	private AtomicBoolean widgetCancelled;

	/**
	 * <p>
	 * The IStreamingTextWidget to which output streamed from the output file is
	 * written.
	 * </p>
	 * 
	 */
	private IStreamingTextWidget streamingTextWidget;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 */
	public ItemProcessor() {

		// Set the default values
		infoWidget = null;
		formWidget = null;
		actionName = null;
		itemId = -1;
		iceCore = null;
		streamingTextWidget = null;

		// Initialize the AtomicBooleans
		widgetClosedOK = new AtomicBoolean();
		widgetClosedOK.set(false);
		widgetCancelled = new AtomicBoolean();
		widgetCancelled.set(false);

	}

	/**
	 * <p>
	 * This operation retrieves the IExtraInfoWidget that is used by the
	 * ItemProcessor. If it has not been previously set, this operation returns
	 * null.
	 * </p>
	 * 
	 * @return <p>
	 *         The IExtraInfoWidget
	 *         </p>
	 */
	public IExtraInfoWidget getInfoWidget() {
		return infoWidget;
	}

	/**
	 * <p>
	 * This operation sets the IExtraInfoWidget that is used by the
	 * ItemProcessor.
	 * </p>
	 * 
	 * @param widget
	 *            <p>
	 *            The IExtraInfoWidget
	 *            </p>
	 */
	public void setInfoWidget(IExtraInfoWidget widget) {

		infoWidget = widget;

		return;

	}

	/**
	 * <p>
	 * This operation sets the IFormWidget that is updated by the ItemProcessor.
	 * </p>
	 * 
	 * @param widget
	 *            <p>
	 *            The IFormWidget
	 *            </p>
	 */
	public void setFormWidget(IFormWidget widget) {

		formWidget = widget;

		return;

	}

	/**
	 * <p>
	 * This operation retrieves the IFormWidget that is updated by the
	 * ItemProcessor. If it has not been previously set, this operation returns
	 * null.
	 * </p>
	 * 
	 * @return <p>
	 *         The IFormWidget
	 *         </p>
	 */
	public IFormWidget getFormWidget() {
		return formWidget;
	}

	/**
	 * <p>
	 * This operation sets the IStreamingTextWidget that is updated by the
	 * ItemProcessor.
	 * </p>
	 * 
	 * @param widget
	 *            <p>
	 *            The IStreamingTextWidget
	 *            </p>
	 */
	public void setStreamingTextWidget(IStreamingTextWidget widget) {

		// Set the widget
		streamingTextWidget = widget;

		return;

	}

	/**
	 * <p>
	 * This operation retrieves the IStreamingTextWidget that is updated by the
	 * ItemProcessor. If it has not been previously set, this operation returns
	 * null.
	 * </p>
	 * 
	 * @return <p>
	 *         The IStreamingTextWidget
	 *         </p>
	 */
	public IStreamingTextWidget getStreamingTextWidget() {
		return streamingTextWidget;
	}

	/**
	 * <p>
	 * This operation sets the name of the action that should be performed when
	 * the Item is processed.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The action name
	 *            </p>
	 */
	public void setActionName(String name) {

		actionName = name;

		return;

	}

	/**
	 * <p>
	 * This operation retrieves the name of the action that the ItemProcessor
	 * will use to process the Item. If it has not been previously set, this
	 * operation returns null.
	 * </p>
	 * 
	 * @return <p>
	 *         The name of the action
	 *         </p>
	 */
	public String getActionName() {

		return actionName;
	}

	/**
	 * <p>
	 * This operation sets the id of the Item that the ItemProcessor will
	 * process.
	 * </p>
	 * 
	 * @param id
	 */
	public void setItemId(int id) {

		itemId = id;

		return;

	}

	/**
	 * <p>
	 * This operation retrieves the id of the Item that the ItemProcessor will
	 * process. If it has not been previously set, this operation returns -1.
	 * </p>
	 * 
	 * @return <p>
	 *         The Item's id
	 *         </p>
	 */
	public int getItemId() {

		return itemId;
	}

	/**
	 * <p>
	 * This operation sets the ICore to which the ItemProcessor directs its
	 * requests.
	 * </p>
	 * 
	 * @param core
	 *            <p>
	 *            The ICore
	 *            </p>
	 */
	public void setCore(ICore core) {

		iceCore = core;

		return;

	}

	/**
	 * <p>
	 * This operation retrieves the ICore to which the ItemProcessor directs its
	 * requests or returns null if it was not previously set.
	 * </p>
	 * 
	 * @return <p>
	 *         The ICore
	 *         </p>
	 */
	public ICore getCore() {

		return iceCore;
	}

	/**
	 * <p>
	 * This operation sets the poll time of the ItemProcessor. It checks to make
	 * sure that the submitted poll time is positive, greater than zero and less
	 * than 30,000 milliseconds (30 seconds). If the submitted time is invalid,
	 * this operation sets it to the default polling time.
	 * </p>
	 * 
	 * @param milliseconds
	 *            <p>
	 *            The poll time in milliseconds. This value must be positive,
	 *            greater than zero and less than 30,000.
	 *            </p>
	 */
	public void setPollTime(int milliseconds) {

		// Check the poll time before setting it
		if (milliseconds > 1 && milliseconds < 30001) {
			pollTime = milliseconds;
		} else {
			pollTime = 100;
		}
		return;

	}

	/**
	 * <p>
	 * This operation retrieves the current poll time. The units of the returned
	 * value are milliseconds.
	 * </p>
	 * 
	 * @return <p>
	 *         The current polling time in milliseconds.
	 *         </p>
	 */
	public int getPollTime() {
		return pollTime;
	}

	/**
	 * <p>
	 * This operation launches the thread to process the Item.
	 * </p>
	 * 
	 */
	@Override
	public void run() {

		// Local Declarations
		FormStatus status = FormStatus.InfoError;
		Form form = null;
		AtomicBoolean posted = new AtomicBoolean();
		HashMap<FormStatus, String> statusMessageMap = new HashMap<FormStatus, String>();
		File outputFile = null;
		FileReader outputFileReader = null;
		BufferedReader outputFileBufferedReader = null;
		String nextLine = null;

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

		// Set the initial status
		formWidget.updateStatus(statusMessageMap.get(FormStatus.Processing));

		// Try processing the Item - FIXME - client id is hardwired
		status = iceCore.processItem(itemId, actionName, 1);

		// Grab the output file handle
		outputFile = iceCore.getItemOutputFile(itemId);
		// Open the file if it is available
		if (outputFile != null && outputFile.exists()
				&& streamingTextWidget != null) {
			try {
				// Create the readers
				outputFileReader = new FileReader(outputFile);
				outputFileBufferedReader = new BufferedReader(outputFileReader);
				// Set the widget label
				streamingTextWidget.setLabel(formWidget.getForm().getName()
						+ " " + formWidget.getForm().getId() + " Live Output");
				// Open the widget
				streamingTextWidget.display();
			} catch (FileNotFoundException e) {
				// Complain that the file could not be opened
				e.printStackTrace();
			}
		}

		// The event loop - until status != FormStatus.NeedsInfo or
		// FormStatus.Processing
		posted.set(false);
		while (status.equals(FormStatus.NeedsInfo)
				|| status.equals(FormStatus.Processing)) {

			// Throw up the extra info widget if more information is needed
			if (status.equals(FormStatus.NeedsInfo)) {
				// Check whether or not to post to the info widget to the screen
				if (!posted.get()) {
					// Set the Form for the InfoWidget
					form = iceCore.getItem(itemId);
					infoWidget.setForm(form);
					// Register as a listener of the infoWidget
					infoWidget.setCloseListener(this);
					// Display the widget
					infoWidget.display();
					// Set the posted flag so that widget does not continue to
					// be displayed
					posted.set(true);
				} else {
					// FIXME This is a potential design flaw, as any attempt to
					// cancel will be ignored if the widget is closed
					// "successfully" before this thread makes it to this if
					// condition.
					// Otherwise if the widget has been posted, see if it has
					// been closed ok.
					if (widgetClosedOK.get()) {
						// Return the extra information
						iceCore.updateItem(form, 1); // FIXME - hardwired
														// client id!
						// Reset the posted flag and the widgetClosedOK flag so
						// that the widget can be shown again if needed.
						posted.set(false);
						widgetClosedOK.set(false);
					} else if (widgetCancelled.get()) {
						// If the widget was cancelled, try to kill the task
						iceCore.cancelItemProcess(itemId, actionName);
						// Update the status
						status = iceCore.getItemStatus(itemId);
						// Update the IFormWidget's status
						formWidget.updateStatus(statusMessageMap.get(status));
						return;
					}
				}
			}

			// Update the status
			status = iceCore.getItemStatus(itemId);
			// Update the IFormWidget's status
			formWidget.updateStatus(statusMessageMap.get(status));

			// Read the file if it was opened correctly
			if (outputFileBufferedReader != null && streamingTextWidget != null) {
				// Get everything currently there
				try {
					while ((nextLine = outputFileBufferedReader.readLine()) != null) {
						// Write it to the streaming text widget
						streamingTextWidget.postText(nextLine);
					}
				} catch (IOException e) {
					// Complain because the next line could not be read
					e.printStackTrace();
				}
			}

			// The Form is completely processed, it is time to break out of the
			// loop.
			if (status.equals(FormStatus.Processed)) {
				try {
					// Close the readers
					if (outputFileBufferedReader != null) {
						outputFileBufferedReader.close();
						outputFileReader.close();
					}
				} catch (IOException e) {
					// Complain
					e.printStackTrace();
				}

				break;
			} else {
				// Otherwise, put the thread to sleep for a bit so that it does
				// not spam requests incessantly.
				try {
					Thread.sleep(pollTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Print a debug message for now

		}

		// Update the IFormWidget's status one final time
		formWidget.updateStatus(statusMessageMap.get(status));

		// Print a debug message for now
		System.out.println("IClient ItemProcessor Message: Status = " + status);

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#closedOK()
	 */
	@Override
	public void closedOK() {

		// Set the flag
		widgetClosedOK.set(true);

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#cancelled()
	 */
	@Override
	public void cancelled() {

		// Set the flag
		widgetCancelled.set(true);

		return;
	}
}