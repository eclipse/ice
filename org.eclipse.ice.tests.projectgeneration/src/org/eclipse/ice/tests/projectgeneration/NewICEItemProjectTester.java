/*******************************************************************************
* Copyright (c) 2013, 2014 UT-Battelle, LLC.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Initial API and implementation and/or initial documentation - Jay Jay Billings,
*   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
*   Claire Saunders, Matthew Wang, Anna Wojtowicz
*******************************************************************************/
package org.eclipse.ice.tests.projectgeneration;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.LineNumberReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * This class tests the creation and configuration of new ICE Item projects.
 *
 * @author arbennett
 */
public class NewICEItemProjectTester {

	private static SWTWorkbenchBot bot;
	private static final String SEP = System.getProperty("file.separator");
	private static final String PROJECT_NAME = "org.eclipse.ice.newitem";
	private static final int MANIFEST_LINE_COUNT = 16;
	private static final int MODEL_LINE_COUNT = 239;
	private static final int LAUNCHER_LINE_COUNT = 82;

	/**
	 * Check the setup of a New ICE Item Project
	 */
	@Test
	public void testICEItemWizard() {
		// Create the project
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.menu("File").menu("New").menu("Other...").click().setFocus();
		bot.tree().getTreeItem("ICE Item Project Creation Wizards").expand();
		bot.tree().getTreeItem("ICE Item Project Creation Wizards").getNode("New ICE Item Project").select();
		bot.button("Next >").click();
		bot.textWithLabel("&Project name:").setText("org.eclipse.ice.newitem");
		bot.button("Next >").click();
		bot.comboBox().setText("Oak Ridge National Laboratory");
		bot.button("Next >").click();
		bot.textWithLabel("Item Class Base Name").setText("NewItem");
		bot.button("Finish").click();

		// Wait for the wizard to complete
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Check whether the project exists
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		try {
			workspaceRoot.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		IProject project = workspaceRoot.getProject(PROJECT_NAME);
		assertNotNull(project);
		assertTrue(project.exists());
		try {
			project.open(null);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
			fail("Could not open project!");
		}
		
		// Make sure that manifest is set up
		IFile manifest = project.getFile("META-INF" + SEP + "MANIFEST.MF");
		assertTrue(manifest.exists());
		LineNumberReader lnr = null;
		int lineCount = 0;
		try {
			lnr = new LineNumberReader(new FileReader(manifest.getLocation().toFile()));
			lnr.skip(Long.MAX_VALUE);
			lineCount = lnr.getLineNumber() + 1;
			lnr.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not read manifest!");
		}
		assertEquals(lineCount, MANIFEST_LINE_COUNT);
		
		// Make sure that the launcher and model packages exist
		String srcPath = "src" + SEP + "org" + SEP + "eclipse" + SEP + "ice" + SEP + "newitem";
		String modelPath = srcPath + SEP + "model" + SEP + "NewItemModel.java";
		String launcherPath = srcPath + SEP + "launcher" + SEP + "NewItemLauncher.java";
		IFile modelFile = project.getFile(modelPath);
		IFile launcherFile = project.getFile(launcherPath);
		assertNotNull(modelFile);
		assertNotNull(launcherFile);
		
		// Check that the model and launcher have the correct number of lines
		try {
			lnr = new LineNumberReader(new FileReader(modelFile.getLocation().toFile()));
			lnr.skip(Long.MAX_VALUE);
			lineCount = lnr.getLineNumber();
			lnr.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not read model!");
		}
		assertEquals(lineCount, MODEL_LINE_COUNT);
		try {
			lnr = new LineNumberReader(new FileReader(launcherFile.getLocation().toFile()));
			lnr.skip(Long.MAX_VALUE);
			lineCount = lnr.getLineNumber() + 1;
			lnr.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not read launcher!");
		}
		assertEquals(lineCount, LAUNCHER_LINE_COUNT);
	}
}
