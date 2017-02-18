package org.eclipse.ice.tests.ui.swtbot;

import org.eclipse.ice.tests.client.widgets.utils.AbstractWorkbenchTester;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.Test;

public class VIBEModelTester extends AbstractWorkbenchTester {

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

		SWTBotPreferences.PLAYBACK_DELAY = 125;

		// Open the ICE perspective
		bot.menu("Window").menu("Perspective").menu("Open Perspective")
				.menu("Other...").click();
		bot.table(0).select("ICE");
		bot.button("OK").click();

		// Create a new reflectivity model item
		bot.toolbarButtonWithTooltip("Create an Item").click();
		bot.list(0).select("VIBE Model");
		bot.button("Finish").click();

		// Check the starting form label
		assertTrue("Ready to process.".equals(bot.clabel(0).getText()));

		// Check that the Cancel and Browse buttons are enabled
		assertTrue(bot.button("Cancel").isEnabled());

		// Try out each of the three processes and make sure each completes.
		// Save after each one to reset the header.
		bot.button("Go!").click();
		assertTrue("Done!".equals(bot.clabel(0).getText()));
		bot.table(1).click(0, 0);
		bot.table(1).click(1, 0);

		bot.comboBox().setSelection(1);
		bot.button("Go!").click();
		assertTrue("Done!".equals(bot.clabel(0).getText()));
		bot.table(1).click(0, 0);
		bot.table(1).click(1, 0);

		bot.comboBox().setSelection(2);
		bot.button("Go!").click();
		assertTrue("Done!".equals(bot.clabel(0).getText()));
		bot.table(1).click(0, 0);
		bot.table(1).click(1, 0);

		// Try editing the global configuration table and ensure the change
		// was accepted
		bot.table(1).click(1, 0);
		bot.text(7).typeText("Edited name");
		// bot.text().setText("Edited name");

		bot.table(1).click(0, 0);
		assertTrue("Edited name".equals(bot.table(1).cell(1, 0)));

		// Check that this made the form dirty
		assertTrue("There are unsaved changes on the form."
				.equals(bot.clabel(0).getText()));

		// Save and check that the header's text returned to normal.
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
		assertTrue(bot.clabel(0).getText().equals("Ready to process."));

		// Check the global configuration table's initial contents
		assertEquals(20, bot.table(1).rowCount());

		// Add a row and check that it was added correctly
		bot.button("+").click();
		assertEquals(21, bot.table(1).rowCount());
		assertTrue("".equals(bot.table(1).cell(20, 0)));

		// Remove a row from the table and check that it is gone
		bot.table(1).click(0, 0);
		bot.button("-").click();
		assertEquals(20, bot.table(1).rowCount());
		assertTrue("".equals(bot.table(1).cell(19, 0)));

		// Remove rows until the table is empty and remove another. Nothing
		// should happen.
		for (int i = 0; i < 21; i++) {
			bot.button("-").click();
		}

		// Try editing the table and ensure the change was accepted
		bot.table(2).click(1, 0);

		bot.text("DRIVER").typeText("Edited port");
		bot.table(2).click(0, 0);
		assertTrue("Edited port".equals(bot.table(2).cell(1, 0)));

		// Check the ports table's initial contents
		assertEquals(5, bot.table(2).rowCount());

		// Add a row and check that it was added correctly
		bot.button("+", 1).click();
		assertEquals(6, bot.table(2).rowCount());
		assertTrue("".equals(bot.table(2).cell(5, 0)));

		// Remove a row from the table and check that it is gone
		bot.table(2).click(0, 0);
		bot.button("-", 1).click();
		assertEquals(5, bot.table(2).rowCount());
		assertTrue("".equals(bot.table(2).cell(4, 0)));

		// Remove rows until the table is empty and remove another. Nothing
		// should happen.
		for (int i = 0; i < 6; i++) {
			bot.button("-").click();
		}

		// Change to the Ports Master tab
		bot.cTabItem("Ports Master").activate();

		// bot.b
		// bot.buttonWithTooltip("Set Details Below Masters").click();
		// bot.button("Vertical Orientation").click();
		// bot.button("Horizontal Orientation").click();

		// Test each of the default options, making sure they have at least the
		// correct number of text fields enabled.
		bot.table(1).select(0);
		bot.text(11).typeText("Test");
		bot.table(1).select(1);
		bot.text(13).typeText("Test");
		bot.table(1).select(2);
		bot.text(13).typeText("Test");
		bot.table(1).select(3);
		bot.text(13).typeText("Test");
		bot.table(1).select(4);
		bot.text(13).typeText("Test");

		// Check each of the port types available in the menu and check the
		// number of text fields they display.
		bot.button(2).click();
		bot.table().select("INIT_STATE");
		bot.button("OK").click();
		bot.table(1).select(5);
		bot.text(13).typeText("Test");

		bot.button(2).click();
		bot.table().select("AMPERES_THERMAL");
		bot.button("OK").click();
		bot.table(1).select(6);
		bot.text(13).typeText("Test");

		bot.button(2).click();
		bot.table().select("AMPERES_ELECTRICAL");
		bot.button("OK").click();
		bot.table(1).select(7);
		bot.text(13).typeText("Test");

		bot.button(2).click();
		bot.table().select("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		bot.button("OK").click();
		bot.table(1).select(8);
		bot.text(12).typeText("Test");

		bot.button(2).click();
		bot.table().select("NTG");
		bot.button("OK").click();
		bot.table(1).select(9);
		bot.text(14).typeText("Test");

		bot.button(2).click();
		bot.table().select("DUALFOIL");
		bot.button("OK").click();

		// Dualfoil doesn't have any text boxes, count the number of rows in the
		// table to see if it was added instead.
		assertEquals(11, bot.table(1).rowCount());

		bot.button(2).click();
		bot.table().select("AMPERES");
		bot.button("OK").click();
		bot.table(1).select(11);
		bot.text(14).typeText("Test");

		bot.button(2).click();
		bot.table().select("CHARTRAN_THERMAL_DRIVER");
		bot.button("OK").click();
		bot.table(1).select(12);
		bot.text(10).typeText("Test");

		// Delete a row and make sure it's gone
		bot.table().select(0);
		bot.button("Delete").click();
		assertTrue("11 DUALFOIL".equals(bot.table(1).cell(10, 0)));
	}
}
