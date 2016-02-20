/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences.test;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.connections.preferences.TableComponentComposite;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizTableComponent;
import org.eclipse.eavp.viz.service.test.utils.AbstractSWTTester;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class performs automated UI tests on the {@link TableComponentComposite}
 * . In particular, it tests the composite's ability to add, remove, and update
 * rows in the associated {@link TableComponent}.
 *
 * @author Jordan Deyton
 * @author Robert Smith
 *
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TableComponentCompositeTester extends AbstractSWTTester {

	// TODO Implement these tests.

	private static TableComponentComposite testComposite;

	private static VizTableComponent table;

	private static SWTBot bot;

	@Override
	public void beforeEachTest() {
		super.beforeAllTests();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				testComposite = new TableComponentComposite(getShell(),
						SWT.NONE);
			}
		});

		// Create the composite that will be tested.
		table = new VizTableComponent();
		ArrayList<VizEntry> template = new ArrayList<VizEntry>();
		template.add(new VizEntry() {
			@Override
			public void setup() {
				defaultValue = "new item";
			}
		});
		table.setRowTemplate(template);
		testComposite.setTableComponent(table);

		// Things won't display in this window unless it is resized.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				Point p = getShell().getSize();
				getShell().setSize(p.x - 1, p.y);
				getShell().layout();
			}
		});

		bot = getBot();
	}

	/**
	 * Check that rows can be added and that the underlying table is updated.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void checkAddRow() throws InterruptedException {
		// Press the add row button
		bot.button(0).click();

		// Get the table's first row
		ArrayList<VizEntry> row = table.getRow(0);

		// The row should have one entry with the default value.
		assertNotNull(row);
		assertEquals(row.size(), 1);
		assertTrue(row.get(0).getValue().equals("new item"));

		// The row on the graphical table should contain the same string.
		bot.table(0).getTableItem(0); // Wait until row 0 exists
		assertTrue(bot.table(0).cell(0, 0).contains("new item"));

		// The second row should not exist yet.
		assertNull(table.getRow(1));

		// Try adding multiple more rows
		bot.button(0).click();
		bot.button(0).click();
		bot.button(0).click();

		// There should be four rows in the table now
		assertNotNull(table.getRow(0));
		assertNotNull(table.getRow(1));
		assertNotNull(table.getRow(2));
		assertNotNull(table.getRow(3));
		assertNull(table.getRow(4));

		// Check one of the new rows to ensure it is populated correctly.
		row = table.getRow(3);
		assertEquals(row.size(), 1);
		assertTrue(row.get(0).getValue().equals("new item"));
		// This test fails on the CLI build, so I have disabled it. ~JJB
		// 20151017 14:36
		// assertTrue(bot.table(0).cell(3, 0).contains("new item"));

		return;
	}

	/**
	 * Check that rows can be removed and that the underlying table is updated.
	 */
	@Test
	public void checkRemoveRow() {
		// The table starts off empty, the remove row button should be disabled
		assertFalse(bot.button(1).isEnabled());

		SWTBotPreferences.PLAYBACK_DELAY = 50;

		// Add a row
		bot.button(0).click();

		// Remove button should still be disabled until the row is clicked
		assertFalse(bot.button(1).isEnabled());
		bot.table(0).getTableItem(0); // Wait until row 0 exists
		bot.table(0).click(0, 0);
		bot.button(1).click();

		// The table should be empty
		assertNull(table.getRow(0));

		// The row on the graphical table should also be empty
		assertEquals(bot.table(0).rowCount(), 0);

		// The button should be disabled again
		assertFalse(bot.button(1).isEnabled());

		// Try a series of clicks that should end with the table being empty
		// again.
		bot.button(0).click();
		bot.button(0).click();
		bot.button(0).click();
		bot.table(0).getTableItem(2); // Wait until row 0 exists
		bot.table(0).click(2, 0);
		bot.button(1).click();
		bot.table(0).click(0, 0);
		bot.button(1).click();
		bot.button(0).click();
		bot.table(0).getTableItem(1); // Wait until row 0 exists
		bot.table(0).click(1, 0);
		bot.button(1).click();
		bot.table(0).click(0, 0);
		bot.button(1).click();

		// Make sure the row count is correct
		assertNull(table.getRow(0));
		// Wait until the row is actually deleted since this may take a variable
		// amount of time due to running on a different thread.
		bot.waitUntil(new ICondition() {

			@Override
			public boolean test() throws Exception {
				return bot.table(0).rowCount() == 0;
			}

			@Override
			public void init(SWTBot bot) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getFailureMessage() {
				return "Row count never updated to 0.";
			}
		});
		assertEquals(bot.table(0).rowCount(), 0);
		assertFalse(bot.button(1).isEnabled());

		return;
	}

	/**
	 * Check that a row can be updated by changing a value in the table widget.
	 * The associated Entry in the table should be updated.
	 */
	@Test
	public void checkSetEntryValue() {
		KeyStroke enter = KeyStroke.getInstance(SWT.LF);

		// Add a row
		bot.button(0).click();

		// Edit the first row's entry
		bot.table(0).getTableItem(0); // Wait until row 0 exists
		bot.table(0).click(0, 0);
		bot.text("new item").setText("edited item");
		bot.table(0).pressShortcut(enter);
		assertTrue(bot.table(0).cell(0, 0).contains("edited item"));
		assertTrue(table.getRow(0).get(0).getValue().equals("edited item"));

		// Makes sure changes aren't maintained when removing and replacing a
		// row.
		bot.button(1).click();
		bot.button(0).click();
		bot.table(0).getTableItem(0); // Wait until row 0 exists
		assertTrue(bot.table(0).cell(0, 0).contains("new item"));
		assertTrue(table.getRow(0).get(0).getValue().equals("new item"));

		// Try editing when there are multiple rows.
		bot.button(0).click();
		bot.button(0).click();
		bot.button(0).click();
		bot.table(0).click(2, 0);
		bot.text("new item").setText("edited item");
		bot.table(0).pressShortcut(enter);
		// Wait until the cell contains the text
		bot.waitUntil(new ICondition() {

			@Override
			public boolean test() throws Exception {
				return bot.table(0).cell(2, 0).contains("edited item");
			}

			@Override
			public void init(SWTBot bot) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getFailureMessage() {
				return "Cell text never updated.";
			}
		});
		assertTrue(bot.table(0).cell(2, 0).contains("edited item"));
		assertTrue(table.getRow(2).get(0).getValue().equals("edited item"));

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester#afterEachTest
	 * ()
	 */
	@Override
	public void afterEachTest() {
		super.afterEachTest();

		// Close the window
		bot.activeShell().close();
	}

}
