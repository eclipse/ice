package org.eclipse.ice.materials.ui.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;
import org.eclipse.ice.client.widgets.test.utils.AbstractWorkbenchTester;
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

public class MOOSETester extends AbstractWorkbenchTester {

	// Test the functionality of the Moose Actions drop down button
	@Test
	public void checkMooseActions() {
		SWTWorkbenchBot bot = getBot();

		// Open the MOOSE perspective
		bot.menu("Window").menu("Perspective").menu("Open Perspective")
				.menu("Other...").click();
		bot.table(0).select("MOOSE");
		bot.button("OK").click();

		//Select fork the stork
		bot.toolbarDropDownButtonWithTooltip(
				"Click the arrow to the right to view available MOOSE Actions.")
				.menuItem("MOOSE Fork the Stork").click();

		//Type text into the three fields
		bot.text(0).typeText("Application Name");
		bot.text(1).typeText("User Name");
		bot.text(2).typeText("Password");

		//Click the cancel button, as we shouldn't actually be trying to connect to github during our test
		bot.button("Cancel").click();
		
		//TODO Write tests for the other options once they are implemented
		
//		bot.toolbarButton("Import an input file for an Item into ICE.").click();
//		bot.button("Browse...").click();
//		bot.toolbarButton(0);
//		bot.toolbarButton(10);
		bot.shell("MOOSE - Eclipse SDK").setFocus();
		bot.sleep(10000);
		bot.viewByPartName("Script Unit Test").close();
		bot.viewByPartName("MOOSE").setFocus();
		bot.buttonWithTooltip("Create an Item");
		bot.sleep(5000);
		bot.toolbarButtonWithTooltip("Import an input file for an Item into ICE.").click();
//		bot.sleep(5000);
		bot.button("Browse...").click();
	}

	@Test
	public void checkMooseWorkflow() {
//		SWTWorkbenchBot bot = getBot();
//		
//		//SWTBotPreferences.PLAYBACK_DELAY = 500;
//
//		// Open the MOOSE perspective
//		bot.menu("Window").menu("Perspective").menu("Open Perspective")
//				.menu("Other...").click();
//		bot.table(0).select("MOOSE");
//		bot.button("OK").click();
//		
//		bot.toolbarButtonWithTooltip("Import an input file for an Item into ICE.").click();
////		bot.sleep(5000);
//		bot.button("Browse...").click();
//		
//		bot.sleep(10000);
	}

}
