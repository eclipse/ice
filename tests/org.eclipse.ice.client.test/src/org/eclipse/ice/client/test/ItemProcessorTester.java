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

import static org.junit.Assert.*;

import org.junit.Test;
import org.eclipse.ice.client.internal.ItemProcessor;
import org.eclipse.ice.client.widgets.EclipseExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;

import org.eclipse.ice.core.iCore.ICore;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;

/**
 * This class is responsible for testing the ItemProcessor class.
 * 
 * @author Jay Jay Billings
 */
public class ItemProcessorTester {
	/**
	 * The ItemProcessor to test.
	 */
	private ItemProcessor itemProcessor;

	/**
	 * This operation checks the accessor operations of the ItemProcessor class.
	 */
	@Test
	public void checkAccessors() {

		// Allocate the ItemProcessor
		itemProcessor = new ItemProcessor();

		/**
		 * The tests of the accessors below all follow the same pattern: 1.)
		 * Create the value 2.) Make sure the getter on the ItemProcessor return
		 * the default 3.) Set the value 4.) Check that the value from the
		 * getter is not the default 5.) Check that the value is the same as
		 * what was set
		 */

		// Check IExtraInfoWidget accessors
		IExtraInfoWidget infoWidget = new FakeExtraInfoWidget();
		assertNull(itemProcessor.getInfoWidget());
		itemProcessor.setInfoWidget(infoWidget);
		assertNotNull(itemProcessor.getInfoWidget());
		assertEquals(infoWidget, itemProcessor.getInfoWidget());

		// Check IFormWidget accessors
		IFormWidget formWidget = new FakeFormWidget();
		assertNull(itemProcessor.getFormWidget());
		itemProcessor.setFormWidget(formWidget);
		assertNotNull(itemProcessor.getFormWidget());
		assertEquals(formWidget, itemProcessor.getFormWidget());

		// Check ActionName accessors
		String actionName = "Fire Phasers!";
		assertNull(itemProcessor.getActionName());
		itemProcessor.setActionName(actionName);
		assertNotNull(itemProcessor.getActionName());
		assertEquals(actionName, itemProcessor.getActionName());

		// Check Item id accessors
		int id = 9;
		assertEquals(-1, itemProcessor.getItemId());
		itemProcessor.setItemId(id);
		assertTrue(itemProcessor.getItemId() > 0);
		assertEquals(id, itemProcessor.getItemId());

		// Check ICore accessors
		ICore core = new FakeCore();
		assertNull(itemProcessor.getCore());
		itemProcessor.setCore(core);
		assertNotNull(itemProcessor.getCore());
		assertEquals(core, itemProcessor.getCore());

		// Check the polling time accessors with a good value
		int pollTime = 5;
		assertNotNull(itemProcessor.getPollTime());
		assertEquals(100, itemProcessor.getPollTime());
		itemProcessor.setPollTime(pollTime);
		assertEquals(pollTime, itemProcessor.getPollTime());

		// Check the polling time with a bad value
		pollTime = -5;
		itemProcessor.setPollTime(pollTime);
		assertEquals(100, itemProcessor.getPollTime());

		return;
	}

	/**
	 * This operation checks the ItemProcessor to make sure that it can properly
	 * process an Item. It resets the polling time to 50ms and waits for 75ms on
	 * the thread to make sure that setting the polling time actually affects
	 * the thread.
	 */
	@Test
	public void checkProcessing() {
		// begin-user-code

		// Local Declarations
		int itemId = -1;
		FormStatus status = null;
		FakeExtraInfoWidget infoWidget = new FakeExtraInfoWidget();
		FakeStreamingTextWidget textWidget = new FakeStreamingTextWidget();
		IFormWidget formWidget = new FakeFormWidget();
		String actionName = "blend";
		FakeCore core = new FakeCore();
		Thread processThread = null;

		// Put a dummy form on the widget
		formWidget.setForm(new Form());

		// Allocate the ItemProcessor
		itemProcessor = new ItemProcessor();

		// Setup the process thread
		processThread = new Thread(itemProcessor);

		// Create the Item
		itemId = Integer.parseInt(core.createItem("Red"));

		// Set the ItemProcessor's required information
		itemProcessor.setFormWidget(formWidget);
		itemProcessor.setInfoWidget(infoWidget);
		itemProcessor.setStreamingTextWidget(textWidget);
		itemProcessor.setActionName(actionName);
		itemProcessor.setItemId(itemId);
		itemProcessor.setCore(core);
		itemProcessor.setPollTime(50);

		// Check the Item id
		assertTrue(itemId > 0);

		// Process the Item with the ItemProcessor
		processThread.start();

		// Give the thread a little time to do its work
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Fail if an exception is caught
			fail();
		}

		// Check the process status with the core to make sure the event was
		// processed.
		assertEquals(FormStatus.Processed, core.getLastProcessStatus());

		// Reset the FakeCore's Process state
		core.reset();

		// Reset the ItemProcessor. This time we are going to test the case
		// where more information is needed. Set the Action name to "NeedsInfo"
		// so that the FakeCore will return the proper return code,
		// FormStatus.NeedsInfo.
		actionName = "NeedsInfo";
		itemProcessor = new ItemProcessor();

		// Reset the thread
		processThread = new Thread(itemProcessor);

		// Create the Item
		itemId = Integer.parseInt(core.createItem("Red"));

		// Set the ItemProcessor properties
		itemProcessor.setActionName(actionName);
		itemProcessor.setFormWidget(formWidget);
		itemProcessor.setInfoWidget(infoWidget);
		itemProcessor.setStreamingTextWidget(textWidget);
		itemProcessor.setActionName(actionName);
		itemProcessor.setItemId(itemId);
		itemProcessor.setCore(core);
		itemProcessor.setPollTime(50);

		// Start the thread
		processThread.start();

		// Give the thread a little time to do its work
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Fail if an exception is caught
			fail();
		}

		// Check the process status to make sure the client made the call
		assertEquals(FormStatus.NeedsInfo, core.getLastProcessStatus());

		// Make sure the widget was displayed
		assertTrue(infoWidget.widgetDisplayed());

		// Make sure the Form was resubmitted to the core
		assertTrue(core.itemUpdated());

		// Check that the label was set. It should be set before being
		// displayed.
		assertTrue(textWidget.labelSet());
		// Check that the streaming text widget was displayed
		assertTrue(textWidget.widgetDisplayed());
		// Check that the output file was retrieved from the core
		assertTrue(core.outputFileRetrieved());
		// Check that the text was pushed to the streaming text widget
		assertTrue(textWidget.textPushed());

		// Start the thread...
		processThread = new Thread(itemProcessor);
		processThread.start();
		// ... and immediately cancel!
		itemProcessor.cancelled();
		// Give the thread a little time to do its work
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Fail if an exception is caught
			fail();
		}

		// Check the process status to make sure the client made the call
		assertTrue(core.wasCancelled());

		return;
		// end-user-code
	}
}