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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

/**
 * This class tests the creation and configuration of new ICE Item projects.
 *
 * @author arbennett
 */
public class NewICEItemProjectTester {

	private static SWTWorkbenchBot bot;

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
		bot.menu("File").menu("New").menu("New ICE Item Project").click();
		SWTBotShell shell = bot.shell("New ICE Item Project");
		shell.activate();
	
		// Page 1
		bot.textWithLabel("Project name:").setText(PROJECT_NAME);
		bot.button("Next >").click();
		
		// Page 2
		bot.textWithLabel("Identifier:").setText(PROJECT_NAME);
		bot.textWithLabel("Version:").setText(VERSION);
		bot.textWithLabel("Name:").setText(NAME);
		bot.textWithLabel("Vendor:").setText(INSTITUTE);
		bot.button("Next >").click();
	
		// Page 3
		bot.textWithLabel("Extension Base Name").setText(EXTENSION_NAME);
		bot.textWithLabel("Package Name").setText(PACKAGE_NAME);
		bot.textWithLabel("Class Base Name").setText(CLASS_NAME);
		bot.button("Finish").click();
		
		bot.waitUntil(shellCloses(shell));
	}
}
