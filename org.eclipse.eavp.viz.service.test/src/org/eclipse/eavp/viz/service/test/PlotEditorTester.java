/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.test;

import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.eavp.viz.service.BasicVizServiceFactory;
import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.eavp.viz.service.csv.CSVVizService;
import org.eclipse.eavp.viz.service.internal.VizServiceFactoryHolder;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class is responsible for testing the PlotEditor class.
 * 
 * @author Robert Smith
 *
 */

@RunWith(SWTBotJunit4ClassRunner.class)
public class PlotEditorTester {
	/*
	 * The SWTBot used in testing.
	 */
	private static SWTWorkbenchBot bot;

	private static IVizServiceFactory realFactory;

	/*
	 * Creates the bot before running tests.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		realFactory = VizServiceFactoryHolder.getFactory();

	}

	/*
	 * Tests that the plot editor can open a file correctly, and display the
	 * necessary menus.
	 */
	@Test
	public void testPlotEditor() {

		IVizServiceFactory fakeFactory = new BasicVizServiceFactory();
		fakeFactory.register(new CSVVizService());

		VizServiceFactoryHolder.setVizServiceFactory(fakeFactory);

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
						System.getProperty("user.home") + separator + "ICETests"
								+ separator + projectName)).toURI();
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

		// Test the plot series selection dialog.
		SWTBotToolbarButton button;
		button = bot.activeEditor().bot().toolbarButton(0);
		button.click();
		bot.shell("Select a series").activate();
		bot.tree().select("f(x)");
		bot.button("OK").click();

		// Check that the data tab is present
		bot.cTabItem("Data").activate();
		bot.cTabItem("Plot").activate();

		// Test the editor closing menu option.
		button = bot.activeEditor().bot().toolbarButton(1).click();

		return;
	}

	@AfterClass
	public static void afterClass() throws Exception {
		VizServiceFactoryHolder.setVizServiceFactory(realFactory);

	}
}