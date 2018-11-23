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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.client.internal.ItemProcessor;
import org.eclipse.ice.core.iCore.ICore;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.junit.Test;

/**
 * This class is responsible for testing the FakeItemProcessor class.
 * 
 * @author Jay Jay Billings
 */
public class ItemProcessorTester {
	/**
	 * The FakeItemProcessor to test.
	 */
	private ItemProcessor itemProcessor;

	/**
	 * This operation checks the accessor operations of the FakeItemProcessor class.
	 */
	@Test
	public void checkAccessors() {

		// Allocate the FakeItemProcessor
		itemProcessor = new ItemProcessor();

		/**
		 * The tests of the accessors below all follow the same pattern: 1.)
		 * Create the value 2.) Make sure the getter on the FakeItemProcessor return
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
	 * This operation checks the FakeItemProcessor to make sure that it can properly
	 * process an Item. It resets the polling time to 50ms and waits for 75ms on
	 * the thread to make sure that setting the polling time actually affects
	 * the thread.
	 */
	@Test
	public void checkProcessing() {

		// Local Declarations
		int itemId = -1;
		FakeExtraInfoWidget infoWidget = new FakeExtraInfoWidget();
		FakeStreamingTextWidget textWidget = new FakeStreamingTextWidget();
		IFormWidget formWidget = new FakeFormWidget();
		String actionName = "blend";
		FakeCore core = new FakeCore();
		Thread processThread = null;

		// We don't want a rigid sleep time when we need to wait on the
		// FakeItemProcessor to process a task, as the total time spent may vary
		// wildly. Thus, we define a maximum sleep time of 5 seconds, and when
		// waiting, we periodically check back every 50 ms until either the
		// limit is reached or the condition we want to check has been
		// satisfied.
		final long sleepLimit = 5000;
		final long sleepIncrement = 50;
		long sleepTime;

		// Put a dummy form on the widget
		formWidget.setForm(new Form());

		// Allocate the FakeItemProcessor
		itemProcessor = new ItemProcessor();

		// Create the Item
		itemId = Integer.parseInt(core.createItem("Red"));

		// Set the FakeItemProcessor's required information
		itemProcessor.setFormWidget(formWidget);
		itemProcessor.setInfoWidget(infoWidget);
		itemProcessor.setStreamingTextWidget(textWidget);
		itemProcessor.setActionName(actionName);
		itemProcessor.setItemId(itemId);
		itemProcessor.setCore(core);
		itemProcessor.setPollTime(50);

		// Check the Item id
		assertTrue(itemId > 0);

		// Start the FakeItemProcessor on a separate thread.
		processThread = new Thread(itemProcessor);
		processThread.start();
		try {
			processThread.join();
		} catch (InterruptedException e1) {
			fail("ItemProcessorTester error: Failed to join with ItemProccessor thread.");
		}

		// Check that the action was processed.
		assertEquals(FormStatus.Processed, core.getLastProcessStatus());

		// Reset the FakeCore.
		core.reset();

		// Reset the FakeItemProcessor. This time we are going to test the case
		// where more information is needed. Set the Action name to "NeedsInfo"
		// so that the FakeCore will return the proper return code,
		// FormStatus.NeedsInfo.
		actionName = "NeedsInfo";
		// Create the Item
		itemId = Integer.parseInt(core.createItem("Red"));

		// Set the new FakeItemProcessor properties
		itemProcessor.setActionName(actionName);
		itemProcessor.setItemId(itemId);

		// Start the FakeItemProcessor on a new thread.
		processThread = new Thread(itemProcessor);
		processThread.start();

		// 1 - The FakeItemProcessor notifies the Core to process the item for the
		// action, which sets the status to NeedsInfo.
		// 2 - The output file is fetched from the Core, and the
		// StreamingTextWidget is updated.
		// 3 - The InfoWidget is displayed.
		// 4 - The StreamingTextWidget is updated.
		// 5 - After the InfoWidget closes (which is immediately), the Core is
		// notified to update the item.

		// Give the thread a little time to do its work. The Item action is
		// processed first, so wait until the status is NeedsInfo.
		sleepTime = 0;
		while (core.getLastProcessStatus() != FormStatus.NeedsInfo
				&& sleepTime < sleepLimit) {
			sleepTime += sleepIncrement;
			try {
				Thread.sleep(sleepIncrement);
			} catch (InterruptedException e) {
				fail("ItemProcessorTester error: "
						+ "Cannot sleep while waiting for FakeItemProcessor to respond.");
			}
		}
		assertEquals(FormStatus.NeedsInfo, core.getLastProcessStatus());

		// Make sure the widget was displayed. This may also take some time.
		sleepTime = 0;
		while (!infoWidget.widgetDisplayed() && sleepTime < sleepLimit) {
			sleepTime += sleepIncrement;
			try {
				Thread.sleep(sleepIncrement);
			} catch (InterruptedException e) {
				fail("ItemProcessorTester error: "
						+ "Cannot sleep while waiting for FakeItemProcessor to respond.");
			}
		}
		assertTrue(infoWidget.widgetDisplayed());

		// Make sure the Form was resubmitted to the core. This happens after
		// the widget is displayed (and dismissed, which is immediate for the
		// FakeInfoWidget). We need to give the FakeItemProcessor more time...
		while (!core.itemUpdated() && sleepTime < sleepLimit) {
			sleepTime += sleepIncrement;
			try {
				Thread.sleep(sleepIncrement);
			} catch (InterruptedException e) {
				fail("ItemProcessorTester error: "
						+ "Cannot sleep while waiting for FakeItemProcessor to respond.");
			}
		}
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

		// Because the FakeExtraInfoWidget closes immediately after being
		// displayed, it's possible to get the FakeItemProcessor in an infinite loop
		// where it posts the message to the widget, which closes and notifies
		// the processor, which triggers the same sequence to repeat (the status
		// is always NeedsInfo). Set the flag so the widget does NOT close
		// immediately, then tell the FakeItemProcessor that the widget cancelled
		// the process.
		infoWidget.closeImmediately = false;
		itemProcessor.cancelled();

		// Check that the core was notified that the FakeItemProcessor was
		// cancelled. Give the FakeItemProcessor some time to work...
		while (!core.wasCancelled() && sleepTime < sleepLimit) {
			sleepTime += sleepIncrement;
			try {
				Thread.sleep(sleepIncrement);
			} catch (InterruptedException e) {
				fail("ItemProcessorTester error: "
						+ "Cannot sleep while waiting for FakeItemProcessor to respond.");
			}
		}
		assertTrue(core.wasCancelled());

		return;
	}
}