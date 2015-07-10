/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.test;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import java.io.File;

/**
 * This class is responsible for testing the PlotEditor class.
 * 
 * @author Robert Smith
 *
 */

@Ignore
@RunWith(SWTBotJunit4ClassRunner.class)
public class PlotEditorTester {
	/*
	 * The SWTBot used in testing.
	 */
	private static SWTWorkbenchBot bot;

	/*
	 * Creates the bot before running tests.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
	}

	/*
	 * Tests that the plot editor can open a file correctly, and display the
	 * necessary menus.
	 */
	@Test
	public void testPlotEditor() {

		// Set up the workspace
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		// The OS's file path separator
		String separator = System.getProperty("file.separator");
		String projectName = "CSVVizService";
		java.net.URI defaultProjectLocation = null;

		// Setup the project
		try {
			// Get the project handle
			IProject project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as
				// ${workspace_loc}/CAEBATModelTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.home") + separator
								+ "ICETests" + separator + projectName))
						.toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(projectName);
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail();
		}

		// Close the initial eclipse welcome window
		bot.viewByTitle("Welcome").close();

		// Open the project explorer
		bot.menu("Window").menu("Show View").menu("Project Explorer").click();

		// Open the fib8.csv file in the plot editor.
		SWTBotTreeItem node = bot.tree().getTreeItem("CSVVizService");
		node.expand();
		node.getNode("fib8.csv").select();
		node.getNode("fib8.csv").doubleClick();

		// Test the plot category selection menu
		SWTBotToolbarDropDownButton button = bot.activeEditor().bot()
				.toolbarDropDownButton();
		button.menuItem("Plot Categories").menu("Bar").menu("x vs. f(x)")
				.click();

		// Close the menu before testing the next item
		try {
			button.pressShortcut(KeyStroke.getInstance("ESC"));
		} catch (ParseException e1) {
			e1.printStackTrace();
			fail();
		}

		// Test the editor closing menu option.
		button.menuItem("Close").click();

	}
}
