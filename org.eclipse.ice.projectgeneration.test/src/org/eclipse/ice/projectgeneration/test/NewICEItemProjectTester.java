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
package org.eclipse.ice.projectgeneration.test;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.eclipse.ice.projectgeneration.ICEItemNature;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

/**
 * This class tests the creation and configuration of new ICE Item projects.
 *
 * @author arbennett
 */
public class NewICEItemProjectTester {

	private static SWTWorkbenchBot bot;
	private static SWTBotShell shell;
	private static SWTBotTree tree;
	private static SWTBot pBot;
	
	private static final String PROJECT_NAME = "org.eclipse.ice.newitem";
	private static final String VERSION = "1.0.0";
	private static final String NAME = "ICE Item";
	private static final String INSTITUTE = "Oak Ridge National Laboratory";
	private static final String EXTENSION_NAME = "org.eclipse.ice.newitem";
	private static final String PACKAGE_NAME = "org.eclipse.ice.newitem";
	private static final String CLASS_NAME = "NewItem";
	
	
	/**
	 * Check the setup of a New ICE Item Project
	 */
	@Test
	public void testICEItemWizard() {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.menu("File").menu("New").menu("Other...").click();
		shell = bot.shell("New");
		pBot = shell.bot();
		tree = pBot.tree();
		tree.expandNode("ICE Item Creation Wizards", "ICE Item Project Creation Wizard").select();
		pBot.button("Next >").click();
		
		// Page 1
		pBot.textWithLabel("Project name:").setText(PROJECT_NAME);
		pBot.button("Next >").click();
		
		// Page 2
		pBot.textWithLabel("Identifier:").setText(PROJECT_NAME);
		pBot.textWithLabel("Version:").setText(VERSION);
		pBot.textWithLabel("Name:").setText(NAME);
		pBot.textWithLabel("Vendor:").setText(INSTITUTE);
		pBot.button("Next >").click();
	
		// Page 3
		pBot.textWithLabel("Extension Base Name").setText(EXTENSION_NAME);
		pBot.textWithLabel("Package Name").setText(PACKAGE_NAME);
		pBot.textWithLabel("Class Base Name").setText(CLASS_NAME);
		pBot.button("Finish").click();
		pBot.waitUntil(shellCloses(shell));
	}
}
