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

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.ice.client.widgets.test.utils.AbstractWorkbenchTester;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList.ListElement;
import org.junit.Test;

/**
 * A class designed to test the functionality of the Reflectivity Model UI.
 * 
 * @author Robert Smith
 *
 */
public class ReflectivityModelTester extends AbstractWorkbenchTester {

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

	// Test the various functionalities of the
	@Test
	public void checkReflectivityModel() {

		// Slow down the bot's execution.
		SWTBotPreferences.PLAYBACK_DELAY = 100;

		// Open the ICE perspective
		bot.menu("Window").menu("Perspective").menu("Open Perspective")
				.menu("Other...").click();
		bot.table(0).select("ICE");
		bot.button("OK").click();

		// Create a new reflectivity model item
		bot.toolbarButtonWithTooltip("Create an Item").click();
		bot.list(0).select("Reflectivity Model");
		bot.button("Finish").click();

		// Check the starting form label
		assertTrue("Ready to process.".equals(bot.clabel(0).getText()));

		// Try The first combo box option
		bot.comboBox(0).setSelection(1);
		bot.button("Go!").click();

		// It should complete and update the form's label
		String temp = bot.clabel(0).getText();
		boolean temp2 = "Done!".equals(temp);
		assertTrue(temp2);

		// The the second combo box option, it too should complete
		bot.comboBox(0).setSelection(2);
		bot.button("Go!").click();
		temp = bot.clabel(0).getText();
		temp2 = "Done!".equals(temp);
		assertTrue(temp2);

		// Check that the cancel button is working
		assertTrue(bot.button("Cancel").isEnabled());

		// Get the nattable
		NatTable realTable = bot.widget(widgetOfType(NatTable.class));
		SWTBotNatTable table = new SWTBotNatTable(realTable);

		// It should start with five rows
		assertEquals(5, table.rowCount());

		// Add a material to the table
		bot.button("Add").click();
		NatTable realMaterialsTable = bot.widget(widgetOfType(NatTable.class));
		SWTBotNatTable materialsTable = new SWTBotNatTable(realMaterialsTable);
		materialsTable.selectCell(bot, 1, 2);
		bot.button("OK").click();

		// The table should now have six rows
		assertEquals(6, table.rowCount());

		// Check that the new row's value is correct
		String cellName = (String) realTable.getDataValueByPosition(1, 6);
		assertTrue("107Ag".equals(cellName));

		// Click the delete button with nothing selected. No rows should be
		// deleted
		bot.button(3).click();
		assertEquals(6, table.rowCount());

		// Select the new row and try to move it down a row. This shouldn't do
		// anything.
		table.selectCell(bot, 0, 5);
		bot.button(5).click();
		cellName = (String) realTable.getDataValueByPosition(1, 6);
		assertTrue("107Ag".equals(cellName));

		// Move it up two rows
		bot.button(4).click();
		bot.button(4).click();
		cellName = (String) realTable.getDataValueByPosition(1, 4);
		assertTrue("107Ag".equals(cellName));

		// Move it up five times. This should let it reach the top of the table
		// then stop.
		bot.button(4).click();
		bot.button(4).click();
		bot.button(4).click();
		bot.button(4).click();
		bot.button(4).click();
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("107Ag".equals(cellName));

		// Delete the row and check that first and last rows are correct.
		bot.button("Delete").click();
		assertEquals(5, table.rowCount());
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Air".equals(cellName));
		cellName = (String) realTable.getDataValueByPosition(1, 5);
		assertTrue("Si".equals(cellName));

		// Check that the wave vector file brows button is enabled
		bot.viewByPartName("Properties").setFocus();
		assertTrue(bot.button("Browse...").isEnabled());

		// Try entering text into the four text boxes
		// TODO Validate that they only accept correct input values.
		enterText("Num Layers of Roughness:", "invalid input");
		enterText("deltaQ0:", "invalid input");
		enterText("deltaQ1ByQ:", "invalid input");
		enterText("Wave Length:", "invalid input");

		// Try out the other tabs
		// TODO Test the functionality of the Cell Editor tab
		final ListElement tab1 = bot.widget(widgetOfType(ListElement.class), 1);
		selectPropertyTabItem(tab1);

		final ListElement tab2 = bot.widget(widgetOfType(ListElement.class), 1);
		selectPropertyTabItem(tab2);

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
		// Find the appropriate widget
		SWTBotText widget = bot.textWithLabel(label);

		// Type in the text and hit enter
		widget.setText(text);
		widget.pressShortcut(Keystrokes.LF);
	}

	/**
	 * Click on the given ListElement from a TabbedPropertiesList
	 * 
	 * @param index
	 *            Label to find.
	 */
	public static void selectPropertyTabItem(final ListElement tabItem) {
		UIThreadRunnable.syncExec(SWTUtils.display(), new VoidResult() {
			public void run() {
				if (tabItem != null) {

					// Create a new UI event
					Event event = new Event();

					// Get the current display
					Display display = getDisplay();

					// Initialize the x and y coordinates to the tabItem's
					// location, relative to its parent composite, plus the
					// shell's location.
					int x = tabItem.getBounds().x
							+ display.getActiveShell().getBounds().x;
					int y = tabItem.getBounds().y
							+ display.getActiveShell().getBounds().y;

					// For each parent composite, add that composite's location,
					// relative to its own parent, to the coordinates. This will
					// calculate the absolute coordinates of the tabe item.
					Composite widget = tabItem.getParent();
					while (widget != null) {
						x += widget.getBounds().x;
						y += widget.getBounds().y;
						widget = widget.getParent();
					}

					// Move the mouse to the tabItem.
					event.type = SWT.MouseMove;
					event.x = x;
					event.y = y;
					display.post(event);

					// Click
					event = new Event();
					event.type = SWT.MouseDown;
					event.button = 1;
					display.post(event);
					display.post(event);

					// Unclick
					event = new Event();
					event.type = SWT.MouseUp;
					event.button = 1;
					display.post(event);
				}
			}
		});
	}

}
