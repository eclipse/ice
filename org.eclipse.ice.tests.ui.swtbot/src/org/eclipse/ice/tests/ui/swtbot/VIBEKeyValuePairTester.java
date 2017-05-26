/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Robert Smith
 *******************************************************************************/
package org.eclipse.ice.tests.ui.swtbot;

import org.eclipse.ice.tests.client.widgets.utils.AbstractWorkbenchTester;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.Test;

/**
 * A class which tests the VIBE Key-Value pair file creator.
 * 
 * @author Robert Smith
 *
 */
public class VIBEKeyValuePairTester extends AbstractWorkbenchTester {

	/**
	 * The bot to be used in testing.
	 */
	static SWTWorkbenchBot bot;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.utils.AbstractWorkbenchTester#
	 * beforeAllTests()
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();
		bot = getBot();
	}

	/**
	 * Tests the functionality of the VIBE Key-Value Pair item.
	 */
	@Test
	public void checkVIBEKeyValuePair() {

		SWTBotPreferences.PLAYBACK_DELAY = 100;

		// Open the ICE perspective
		bot.menu("Window").menu("Perspective").menu("Open Perspective")
				.menu("Other...").click();
		bot.table(0).select("ICE");
		bot.button().click();

		// Create a new reflectivity model item
		bot.toolbarButtonWithTooltip("Create an Item").click();
		bot.list(0).select("VIBE Key-Value Pair");
		bot.button("Finish").click();

		// Check the starting form label
		assertTrue("Ready to process.".equals(bot.clabel(0).getText()));

		// Check that the cancel button is enabled
		assertTrue(bot.button("Cancel").isEnabled());

		//The table should be empty at the start
		assertEquals(0, bot.table(1).rowCount());
		
		//Set the table to the NTG template
		bot.radio(1).click();
		
		// Check the table's values for the default settings
		assertTrue("ELECTRICAL".equals(bot.table(1).cell(0, 0)));

		// Change the first row's value to two
		bot.table(1).click(0, 1);
		bot.text("1 ", 0).setText("2");
		bot.table(1).click(0, 0);

		// Check that the cell was set
		assertTrue("2".equals(bot.table(1).cell(0, 1)));

		// Makes sure the form registers that it is dirty
		assertTrue("There are unsaved changes on the form."
				.equals(bot.clabel(0).getText()));

		// Save and check that the header's text returned to normal.
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
		assertTrue(bot.clabel(0).getText().equals("Ready to process."));

		// Make sure there are 21 rows
		assertEquals(21, bot.table(1).rowCount());

		// Add a row and check that it is there.
		bot.button("+").click();
		assertEquals(22, bot.table(1).rowCount());

		// Remove the second from last row and test that there are one fewer
		// rows and that the last row is empty.
		bot.table(1).click(20, 0);
		bot.button("-").click();
		assertEquals(21, bot.table(1).rowCount());
		System.out.println(bot.table(1).cell(20, 0));
		assertTrue("".equals(bot.table(1).cell(20, 0)));

		// Check that the form reports the job as complete after performing it.
		bot.button("Go!").click();
		assertTrue(bot.clabel(0).getText().equals("Done!"));

		// Set the template to DualFoil
		bot.radio(2).click();

		// Check that a couple of the DualFoil's rows are set up correctly
		assertTrue("NUMSEG".equals(bot.table(1).cell(14, 0)));
		assertTrue("CUTOFFL".equals(bot.table(1).cell(17, 0)));
		assertTrue("2.0,2.0,2.0,2.0,2.0".equals(bot.table(1).cell(17, 1)));

		// Set the NUMSEG key to 8.
		bot.table(1).click(14, 1);
		bot.text("5", 0).setText("8");
		bot.table(1).click(14, 0);

		// CUTOFFL should have had its vector increased in size to account for
		// the new NUMSEG value
		assertTrue("2.0,2.0,2.0,2.0,2.0,0.0,0.0,0.0"
				.equals(bot.table(1).cell(17, 1)));

		// Set NUMSEG to 4
		bot.table(1).click(14, 1);
		bot.text("8", 0).setText("4");
		bot.table(1).click(14, 0);

		// CUTOFFL should have been reduced to only 4 numbers
		assertTrue("2.0,2.0,2.0,2.0".equals(bot.table(1).cell(17, 1)));

	}
}
