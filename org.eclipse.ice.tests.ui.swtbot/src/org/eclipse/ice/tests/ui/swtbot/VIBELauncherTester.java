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
 * A class to test the functionality of the Vibe Launcher.
 * 
 * @author Robert Smith
 *
 */
public class VIBELauncherTester extends AbstractWorkbenchTester {

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
	 * Tests the functionality of the VIBE Launcher item.
	 */
	@Test
	public void checkVIBELauncher() {

		SWTBotPreferences.PLAYBACK_DELAY = 100;

		// Open the ICE perspective
		bot.menu("Window").menu("Perspective").menu("Open Perspective")
				.menu("Other...").click();
		bot.table(0).select("ICE");
		bot.button("OK").click();

		// Create a new reflectivity model item
		bot.toolbarButtonWithTooltip("Create an Item").click();
		bot.list(0).select("VIBE Launcher");
		bot.button("Finish").click();

		// Check the starting form label
		assertTrue("Ready to process.".equals(bot.clabel(0).getText()));
		
		//Check that the Cancel and Browse buttons are enabled
		assertTrue(bot.button("Cancel").isEnabled());
		assertTrue(bot.button("Browse...").isEnabled());
		
		//Test that the input file combo box can be manipulated
		bot.comboBoxWithLabel("Input File:").setSelection(1);
		
		// Check that this made the form dirty
		assertTrue("There are unsaved changes on the form.".equals(bot.clabel(0).getText()));
		
		// Save and check that the header's text returned to normal.
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
		assertTrue(bot.clabel(0).getText().equals("Ready to process."));
		
		//Click the custom file box.
		bot.checkBox("Use custom key-value pair file?").click();

		//Set the selection of the key-value combo box
		bot.comboBox().setSelection(1);;
		
		//Check that the key-value browse button is enabled
		assertTrue(bot.button("Browse...", 1).isEnabled());
		
		//Check the host table's initial contents
		assertTrue("localhost".equals(bot.table(1).cell(0, 0)));
		assertEquals(1, bot.table(1).rowCount());
		
		//Add a row and check that it was added correctly
		bot.button("+").click();
		assertEquals(2, bot.table(1).rowCount());
		assertTrue("".equals(bot.table(1).cell(1, 0)));
		
		//Try editing the table and ensure the change was accepted
		bot.table(1).click(1, 0);
		bot.text("").typeText("8.8.8.8");
		bot.table(1).click(0, 0);
		assertTrue("8.8.8.8".equals(bot.table(1).cell(1, 0)));
		
		//Remove a row from the table and check that it is gone
		bot.button("-").click();
		assertEquals(1, bot.table(1).rowCount());
		assertTrue("8.8.8.8".equals(bot.table(1).cell(0, 0)));
		
		//Remove rows until the table is empty and remove another. Nothing should happen.
		bot.button("-").click();
		bot.button("-").click();
		
		// See if the tab can be changed
		bot.cTabItem("Output Files and Data").activate();
		
		
	}
}
