package org.eclipse.ice.ui.swtbot.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.ice.materials.ui.MaterialsDatabaseServiceHolder;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Test;

public class MaterialsDatabaseTester extends AbstractSWTTester {

	/**
	 * The Workbench's previous IMaterialsDatabase, maintained so that the tests
	 * can return the workbench to its default state after completion.
	 */
	IMaterialsDatabase realDatabase;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester#beforeAllTests
	 * ()
	 */
	@Override
	public void beforeAllTests() {
		// Don't create a shell or bot for these tests.

		// Switch the IMaterialsDatabase service to the test database.
		realDatabase = MaterialsDatabaseServiceHolder.get();
		MaterialsDatabaseServiceHolder.set(new FakeMaterialsDatabase());

	}

	@Test
	public void checkMaterialsTree() {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();

		// These tests need to be on a slight delay, or else junit will test
		// before ICE's UI has had time to properly update.
		SWTBotPreferences.PLAYBACK_DELAY = 50;

		// Close the initial eclipse welcome window
		bot.viewByTitle("Welcome").close();

		// Open the materials database
		bot.toolbarButtonWithTooltip("Edit Materials Database").click();

		SWTBotButton add = bot.button("Add");
		SWTBotButton delete = bot.button("Delete");
		SWTBotButton reset = bot.button("Reset");

		// Try to delete a material. Nothing is selected, so nothing should
		// happen.
		delete.click();
		bot.button("OK").click();

		// Check that silver is still in the table. An exception will be thrown
		// here if it is not.
		bot.tree(1).getTreeItem("Ag");

		// Select silver in the table
		bot.tree(1).select(0);

		// Get the NatTable of material properties.
		NatTable realTable = bot.widget(widgetOfType(NatTable.class));
		SWTBotNatTable propertyTable = new SWTBotNatTable(realTable);

		assertEquals(3, propertyTable.rowCount());

		// Check the property name for the first data row.
		String cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Abs xs".equals(cellName));

		// Check the property value for the fist data row
		double cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(63.3, cellValue);

		// Check the property name and value for the second data row
		cellName = (String) realTable.getDataValueByPosition(1, 2);
		assertTrue("Coh b".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 2);
		assertEquals(5.922, cellValue);

		SWTBotButton addProperty = bot.button("Add", 1);
		SWTBotButton deleteProperty = bot.button("Delete", 1);

		// Pressing this with nothing selected shouldn't do anything.
		deleteProperty.click();

		// Check that the last row is unchanged
		assertEquals(3, propertyTable.rowCount());
		cellName = (String) realTable.getDataValueByPosition(1, 3);
		assertTrue("Dens (at/nm3)".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 3);
		assertEquals(58.62, cellValue);

		// Open the add property dialog
		addProperty.click();

		// Add a new property
		bot.text(0).typeText("Custom Property");
		bot.text(1).typeText("2.5");
		bot.button("OK").click();

		// Check the new fourth row
		assertEquals(4, propertyTable.rowCount());
		cellName = (String) realTable.getDataValueByPosition(1, 4);
		assertTrue("Custom Property".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 4);
		assertEquals(2.5, cellValue);

		// Edit the first row's property name. The provider should not actually
		// let the edit change the name.
		propertyTable.selectCell(bot, 0, 0);
		bot.text("Abs xs").typeText("ignore this input\n");
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Abs xs".equals(cellName));

		// Edit the first row's value to a non-number. This should be rejected
		// and the original value restored.
		propertyTable.selectCell(bot, 1, 0);
		bot.text("63.3").typeText("ignore this input");
		propertyTable.selectCell(bot, 0, 0);
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(63.3, cellValue);
		// cellName = (String) realTable.getDataValueByPosition(1, 2);
		// assertTrue("63.3".equals(cellName));

		// Change the first row's value to another valid number.
		propertyTable.selectCell(bot, 1, 0);
		bot.text("63.3").typeText("0.25");
		propertyTable.selectCell(bot, 0, 0);
		bot.sleep(99); // Wait for the NatTable to be updated
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(0.25, cellValue);

		// Delete the first row
		propertyTable.selectCell(bot, 0, 0);
		deleteProperty.click();

		// Check that the first row is gone
		assertEquals(3, propertyTable.rowCount());
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Coh b".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(5.922, cellValue);

		// Pressing this with nothing selected shouldn't do anything.
		deleteProperty.click();

		// Check the last row is unchanged
		assertEquals(3, propertyTable.rowCount());
		cellName = (String) realTable.getDataValueByPosition(1, 3);
		assertTrue("Custom Property".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 3);
		assertEquals(2.5, cellValue);

		// Delete the second row
		propertyTable.selectCell(bot, 0, 1);
		deleteProperty.click();

		// Check that the first row is unchanged
		assertEquals(2, propertyTable.rowCount());
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Coh b".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(5.922, cellValue);

		// Check the third row is now the second row
		cellName = (String) realTable.getDataValueByPosition(1, 2);
		assertTrue("Custom Property".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 2);
		assertEquals(2.5, cellValue);

		// Delete the rest of the table, then press the delete button again.
		// This should do nothing.
		propertyTable.selectCell(bot, 1, 0);
		deleteProperty.click();
		propertyTable.selectCell(bot, 0, 0);
		deleteProperty.click();
		deleteProperty.click();

		// Change the material selection, then return to silver.
		bot.tree(1).select(1);
		bot.tree(1).select(0);

		// Check that the table is still empty after being reloaded;
		assertEquals(0, propertyTable.rowCount());

		// Select aluminum in the table
		bot.tree(1).select(1);

		// Make sure the property table has aluminum's properties
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Abs xs".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(0.231, cellValue);

		// Delete aluminum
		delete.click();
		bot.button("OK").click();

		// Select americum, which is now in aluminum's previous spot, in the
		// table
		bot.tree(1).select(1);

		// Make sure the property table has americum's properties.
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Abs xs".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(75.3, cellValue);

		// Open the add material dialog
		add.click();
		bot.text(0).typeText("Custom Material");
		bot.text(1).typeText("3.5");

		// Clicking delete with an empty table shouldn't do anything.
		bot.button("Delete").click();

		// Open the material database and add silver and americum to the custom
		// material.
		bot.button("Add").click();

		// Get a reference to the NatTable.
		NatTable realMaterialTable = bot.widget(widgetOfType(NatTable.class));
		SWTBotNatTable materialTable = new SWTBotNatTable(realMaterialTable);

		// In this SWTBot test, the OK button will not be visible until the
		// window is resized. This does not seem to effect the actual behavior
		// in ICE.
		maximizeableShell dialog = new maximizeableShell(
				bot.activeShell().widget);
		dialog.maximize();

		// Select americum
		materialTable.selectCell(bot, 2, 2);
		bot.button("OK").click();

		// Repeat the above process for silver
		bot.button("Add").click();
		realMaterialTable = bot.widget(widgetOfType(NatTable.class));
		materialTable = new SWTBotNatTable(realMaterialTable);
		dialog = new maximizeableShell(bot.activeShell().widget);
		dialog.maximize();
		materialTable.selectCell(bot, 1, 1);
		bot.button("OK").click();

		// Get the NatTable describing the custom material
		NatTable realCustomMaterialTable = bot
				.widget(widgetOfType(NatTable.class));
		SWTBotNatTable customMaterialTable = new SWTBotNatTable(
				realCustomMaterialTable);

		// Check that the table has americum and silver in it
		cellName = (String) realCustomMaterialTable
				.getDataValueByPosition(1, 1);
		assertTrue("Am".equals(cellName));
		int cellIntValue = (int) realCustomMaterialTable
				.getDataValueByPosition(2, 1);
		assertEquals(1, cellIntValue);
		cellName = (String) realCustomMaterialTable
				.getDataValueByPosition(1, 2);
		assertTrue("Ag".equals(cellName));
		cellIntValue = (int) realCustomMaterialTable.getDataValueByPosition(2,
				2);
		assertEquals(1, cellIntValue);

		// Edit the first row's material name. The provider should not actually
		// let the edit change the name.
		customMaterialTable.selectCell(bot, 0, 0);
		bot.text("Am").typeText("ignore this input");
		customMaterialTable.selectCell(bot, 1, 0);
		customMaterialTable.selectCell(bot, 0, 0);
		cellName = (String) realCustomMaterialTable
				.getDataValueByPosition(1, 1);
		assertTrue("Am".equals(cellName));

		// Change the amount of americum to a non-integer. This should be
		// rounded to 6
		customMaterialTable.selectCell(bot, 1, 0);
		bot.text("1").typeText("6.5");
		customMaterialTable.selectCell(bot, 0, 0);
		customMaterialTable.selectCell(bot, 1, 0);
		cellIntValue = (int) realCustomMaterialTable.getDataValueByPosition(2,
				1);
		assertEquals(6, cellIntValue);

		 
		 // Change the amount of americum to a negative number. This change
		 // should be rejected.
		 customMaterialTable.selectCell(bot, 1, 0);
		 bot.text("6").typeText("-1");
		 customMaterialTable.selectCell(bot, 0, 0);
		 customMaterialTable.selectCell(bot, 1, 0);
		 cellIntValue = (int)
		 realCustomMaterialTable.getDataValueByPosition(2, 1);
		 assertEquals(6, cellIntValue);

		// Edit the amount of americum.
		customMaterialTable.selectCell(bot, 1, 0);
		bot.text("6").typeText("2");
		customMaterialTable.selectCell(bot, 0, 0);
		customMaterialTable.selectCell(bot, 1, 0);
		cellIntValue = (int) realCustomMaterialTable.getDataValueByPosition(2,
				1);
		assertEquals(2, cellIntValue);

		// Open the materials database dialog and search for gold
		bot.button("Add").click();
		bot.text(0).typeText("Au");

		realMaterialTable = bot.widget(widgetOfType(NatTable.class));
		materialTable = new SWTBotNatTable(realMaterialTable);
		dialog = new maximizeableShell(bot.activeShell().widget);
		dialog.maximize();
		bot.sleep(1000);
		materialTable.selectCell(bot, 0, 0);
		bot.sleep(1000);
		bot.button("OK").click();
		bot.sleep(1000);

		// Check that the table has gold in it
		cellName = (String) realCustomMaterialTable
				.getDataValueByPosition(1, 3);
		assertTrue("Au".equals(cellName));

		// Delete the silver row
		customMaterialTable.selectCell(bot, 0, 1);
		bot.button("Delete").click();

		// Check that gold is now the last row
		// There should be just 1 row because this table lacks the extra row
		// being subtracted from other NatTables
		assertEquals(1, materialTable.rowCount());
		cellName = (String) realCustomMaterialTable
				.getDataValueByPosition(1, 2);
		assertTrue("Au".equals(cellName));

		// Add the custom material
		bot.button("Finish").click();

		// Search for the custom material and open it
		bot.text(1).typeText("Custom");
		bot.tree(1).select(0);

		// Check the property name for the first data row.
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Dens (g/cm3)".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(3.5, cellValue);

		// Select the gold under the custom material
		bot.tree(1).expandNode("Custom Material");
		bot.tree(1).getTreeItem("Custom Material").select("Au (1)");

		// Check the property name for the first data row.

		realTable = bot.widget(widgetOfType(NatTable.class));
		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Abs xs".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(98.65, cellValue);

		// Close and reopen the editor to see if changes are persisted.
		bot.editorByTitle("Materials Database Editor").close();
		bot.toolbarButtonWithTooltip("Edit Materials Database").click();
		bot.sleep(5000);

		// Make sure the custom material is still there
		bot.tree(1).expandNode("Custom Material");
		bot.tree(1).getTreeItem("Custom Material").select("Au (1)");

		// Reset the database.
		reset.click();
		bot.button("Reset").click();
		bot.button("OK").click();

		// Check that aluminum is back to its default state
		bot.tree(1).select("Al");

		realTable = bot.widget(widgetOfType(NatTable.class));
		propertyTable = new SWTBotNatTable(realTable);

		cellName = (String) realTable.getDataValueByPosition(1, 1);
		assertTrue("Abs xs".equals(cellName));
		cellValue = (double) realTable.getDataValueByPosition(2, 1);
		assertEquals(0.231, cellValue);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester#afterAllTests()
	 */
	@Override
	public void afterAllTests() {
		MaterialsDatabaseServiceHolder.set(realDatabase);
	}

}

class maximizeableShell extends SWTBotShell {

	public maximizeableShell(Shell shell) throws WidgetNotFoundException {
		super(shell);
	}

	public void maximize() {
		syncExec(new Result<Object>() {
			public Object run() {
				widget.setMaximized(true);
				return null;
			}
		});

	}

}
