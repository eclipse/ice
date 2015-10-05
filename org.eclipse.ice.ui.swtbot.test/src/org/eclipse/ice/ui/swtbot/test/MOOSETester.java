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
package org.eclipse.ice.ui.swtbot.test;

import org.eclipse.ice.client.widgets.test.utils.AbstractWorkbenchTester;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Test;

public class MOOSETester extends AbstractWorkbenchTester {

	SWTWorkbenchBot bot;

	@Override
	public void beforeAllTests() {
		super.beforeAllTests();
		bot = getBot();
	}

	// Test the functionality of the Moose Actions drop down button
	@Test
	public void checkMooseActions() {

		// Open the MOOSE perspective
		bot.menu("Window").menu("Perspective").menu("Open Perspective")
				.menu("Other...").click();
		bot.table(0).select("MOOSE");
		bot.button("OK").click();

		// Select fork the stork
		bot.toolbarDropDownButtonWithTooltip(
				"Click the arrow to the right to view available MOOSE Actions.")
				.menuItem("MOOSE Fork the Stork").click();

		// Type text into the three fields
		bot.text(0).typeText("Application Name");
		bot.text(1).typeText("User Name");
		bot.text(2).typeText("Password");
		
		// Click the cancel button, as we shouldn't actually be trying to
		// connect to github during our test
		bot.button("Cancel").click();

		// TODO Write tests for the other options once they are implemented

		// Close the Script unit test
		bot.shell("MOOSE - Eclipse SDK").setFocus();
		bot.viewByPartName("Script Unit Test").close();

		// Test the import button then cancel out, as we can't create a SWTbot
		// test for the OS provided file browser.
		bot.toolbarButtonWithTooltip(
				"Import an input file for an Item into ICE.").click();
		bot.button("Cancel").click();

		// Open the add item dialog and add a MOOSE workflow
		bot.viewByPartName("Item Viewer").setFocus();
		bot.toolbarButton(0).click();
		bot.list(0).select("MOOSE Workflow");
		bot.button("Finish").click();

		// Check the form's header text
		assertTrue(bot.clabel(0).getText().equals("Ready to process."));

		// Make sure controls in the header are enabled
		bot.comboBoxWithLabel("Process:").setSelection(0);
		assertTrue(bot.button("Go!").isEnabled());
		assertTrue(bot.button("Cancel").isEnabled());
		
		// Change a text field and see if the header's text is updated to
		// reflect a dirty editor.
		enterText("Output File Name:", "Test output file name");
		assertTrue(bot.clabel(0).getText()
				.equals("There are unsaved changes on the form."));
		
		// Try it again with another text field
		enterText("Account Code/Project Code:", "test account numnber");
		assertTrue(bot.clabel(0).getText()
				.equals("There are unsaved changes on the form."));
		
		// Save and check that the header's text returned to normal.
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
		assertTrue(bot.clabel(0).getText().equals("Ready to process."));
		
		// Try an invalid input string. The header should reflect this and give
		// an appropriate error message.
		enterText("Number of MPI Processes:", "Invalid Input");
		assertTrue(bot.clabel(0).getText().equals(
				"'Invalid Input' is an unacceptable value. The value must be between 1 and 10000."));

		// Try an invalid input number. The header should reflect the detection
		// of two errors.
		enterText("Number of TBB Threads:", "999");
		assertTrue(bot.clabel(0).getText().equals("2 errors detected"));

		// Put in a valid number for one of the text boxes. The error should
		// change to that of the second text box.
		enterText("Number of MPI Processes:", "2");
		assertTrue(bot.clabel(0).getText().equals(
				"'999' is an unacceptable value. The value must be between 1 and 256."));

		// Correct the second text box. The header should revert to the dirty
		// editor warning.
		enterText("Number of TBB Threads:", "3");
		assertTrue(bot.clabel(0).getText()
				.equals("There are unsaved changes on the form."));

		// Try a negative number of threads
		enterText("Number of TBB Threads:", "-1");
		assertTrue(bot.clabel(0).getText().equals(
				"'-1' is an unacceptable value. The value must be between 1 and 256."));

		//TODO Add this check back into the test once the associated bug is fixed.
		// // Try a non-integer number of threads.
		// enterText("Number of TBB Threads:", "1.5");
		// assertTrue(bot.clabel(0).getText().equals(
		// "'1.5' is an unacceptable value. The value must be between 1 and
		// 256."));
		
		//Nothing should happen on a click while an error message is displayed.
		bot.button("Go!").click();
	
		//Fix the error and try again, it should detect a new error for missing a file.
		enterText("Number of TBB Threads:", "4");
		bot.button("Go!").click();
		assertTrue(bot.clabel(0).getText().equals(
				"The Form contains an error and cannot be processed."));

		// See if the tab can be changed
		bot.cTabItem("Mesh and Output Files").activate();

	}

	/**
	 * Enters the specified text into a text widget with the given label,
	 * followed by pressing the enter key. This function will cause the test to
	 * fail if the text widget with the given label is not enabled.
	 * 
	 * @param label
	 *            The label of the text widget to type in.
	 * @param text
	 *            The text to be typed.
	 */
	public void enterText(String label, String text) {
		//Find the appropriate widget
		SWTBotText widget = bot.textWithLabel(label);
		
		//Type in the text and hit enter
		widget.setText(text);
		widget.pressShortcut(Keystrokes.LF);
	}
}
