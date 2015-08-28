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
package org.eclipse.ice.client.common;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * This class sets up the Eclipse action bar, registers actions and fixes the
 * coolbar.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ClientActionBarAdvisor extends ActionBarAdvisor {

	/**
	 * Exit action
	 */
	private IWorkbenchAction exitAction;

	/**
	 * Create Item Action
	 */
	private IWorkbenchAction createItemAction;

	/**
	 * Save Action
	 */
	private IWorkbenchAction saveAction;

	/**
	 * Save All Action
	 */
	private IWorkbenchAction saveAllAction;

	/**
	 * The perspectives
	 */
	private IContributionItem perspectivesMenu;

	/**
	 * Import File Action
	 */
	private IWorkbenchAction importFileAction;

	/**
	 * Launch Mesh Editor Action.
	 */
	private IWorkbenchAction launchMeshEditorAction;

	/**
	 * Import Item input file action
	 */
	private IWorkbenchAction openImportWizardAction;

	/**
	 * The show view menu to show all the available views
	 */
	private IContributionItem showViewMenu;

	/**
	 * The constructor
	 * 
	 * @param configurer
	 */
	public ClientActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	/**
	 * This operation overrides makeActions to setup actions for ICE.
	 */
	@Override
	protected void makeActions(IWorkbenchWindow window) {

		// Create an Exit application action
		exitAction = ActionFactory.QUIT.create(window);
		// Create an about application action
		// Create a connect to Core action
		// Create an action to create Items
		createItemAction = new CreateItemAction(window);
		// Create an action for saving something and an action for saving
		// everything
		saveAction = ActionFactory.SAVE.create(window);
		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		// Create an action for importing files
		importFileAction = new ImportFileAction(window);
		// Create an action for launching a Mesh Editor.
		launchMeshEditorAction = new LaunchMeshEditorAction();
		// Create an action for importing input files as Items
		openImportWizardAction = new ImportItemWizardAction(window);

		// Register these actions with the ActionBarAdvisor
		register(exitAction);
		register(createItemAction);
		register(saveAction);
		register(saveAllAction);
		register(importFileAction);
		register(openImportWizardAction);

		// Create a Perspectives menu item
		perspectivesMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST
				.create(window);

		// Add the "Show View" menu
		showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

		return;
	}

	/**
	 * This operation overrides fillMenuBar to fill the menu bar for ICE.
	 */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {

		// Create a MenuManager for a File Menu Item and a Window Menu Item
		// MenuManager facilitates the addition of simple and complex menu items
		MenuManager fileMenu = new MenuManager("&File", "file");
		MenuManager window = new MenuManager("&Window",
				IWorkbenchActionConstants.M_WINDOW);

		// Create Perspectives Menu Item
		MenuManager perspectiveMenuManager = new MenuManager("Perspectives",
				"layout");
		perspectiveMenuManager.add(perspectivesMenu);
		// Add it to the Window Menu
		window.add(perspectiveMenuManager);

		// Create the Show View menu item
		MenuManager showViewMenuManager = new MenuManager("Show View",
				"showView");
		showViewMenuManager.add(showViewMenu);
		window.add(showViewMenuManager);

		// Add a Create Item action
		fileMenu.add(createItemAction);
		// Add a Connect to Core action
		// Add an Exit action
		fileMenu.add(exitAction);
		// Add an import action
		fileMenu.add(importFileAction);
		// Add an import action for input files
		fileMenu.add(openImportWizardAction);

		// Create a Help Menu and add the About action to it.
		MenuManager help = new MenuManager("&Help", "help");

		// Add all these MenuManagers to the ActionBarAdvisor's menuBar handle.
		menuBar.add(fileMenu);
		menuBar.add(window);
		menuBar.add(help);

		return;
	}

	/**
	 * This operation overrides fillCoolBar to setup the CoolBar for ICE.
	 * 
	 * @param coolBar
	 *            - A manager for the CoolBar.
	 */
	@Override
	public void fillCoolBar(ICoolBarManager coolBar) {

		// Local Declarations
		IToolBarManager toolBar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);

		// Register the create Item Action
		toolBar.add(createItemAction);

		// Register the connect Action;

		// Register the save actions
		toolBar.add(saveAction);
		toolBar.add(saveAllAction);

		// Register the import action
		toolBar.add(importFileAction);

		// Register the launch Mesh Editor action.
		toolBar.add(launchMeshEditorAction);

		// Register the import input file action for Items
		toolBar.add(openImportWizardAction);

		// Add the save toolbar to the CoolBar
		coolBar.add(new ToolBarContributionItem(toolBar, "iceTools"));

		return;
	}

}
