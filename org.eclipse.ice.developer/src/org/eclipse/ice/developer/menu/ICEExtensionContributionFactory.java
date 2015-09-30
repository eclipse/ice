/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alex McCaskey - Initial API and implementation and/or initial documentation
 *
 *******************************************************************************/
package org.eclipse.ice.developer.menu;

import java.util.HashMap;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.developer.actions.GitCloneHandler;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;
import org.osgi.framework.Bundle;

/**
 * The ICEExtensionContributionFactory is a subclass of
 * ExtensionContributionFactory that dynamically creates a Developer Menu Item
 * that contains sub-menus based on available org.eclipse.ice.developer
 * extension points.
 * 
 * @author Alex McCaskey
 *
 */
public class ICEExtensionContributionFactory extends ExtensionContributionFactory {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.menus.AbstractContributionFactory#createContributionItems(org.eclipse.ui.services.IServiceLocator, org.eclipse.ui.menus.IContributionRoot)
	 * 
	 * This implementation adds a Developer Menu Item to the Eclipse ICE Menu Bar. 
	 * 
	 */
	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {

		// Local Declarations
		HashMap<String, MenuManager> categoryMenus = new HashMap<String, MenuManager>();
		String codeName = "", repoURL = "", vcType = "", category = "";
		MenuManager developer = null, codeMenu = null;
		
		// Create the Developer Menu Item
		developer = new MenuManager("&Developer", "iceDev");

		// Get the registry and the extensions
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint("org.eclipse.ice.developer.code");
		IExtension[] codeExtensions = extensionPoint.getExtensions();

		// Create the ICE Menu first...
		IExtension iceExtension = extensionPoint.getExtension("org.eclipse.ice.developer.icedev");
		
		// We will always have only one IConfigurationElement for this extension
		IConfigurationElement element = iceExtension.getConfigurationElements()[0];

		// Get the attribute data
		codeName = element.getAttribute("codeName");
		repoURL = element.getAttribute("repoURL");
		
		// Create the ICE Menu
		codeMenu = new MenuManager(codeName, codeName + "ID");
		createDefaultCommands(serviceLocator, "git", codeName, repoURL, codeMenu);
		developer.add(codeMenu);

		// Create the category sub-menus
		for (CodeCategory c : CodeCategory.values()) {
			MenuManager manager = new MenuManager(c.name(), c.name() + "ID");
			manager.setVisible(true);
			categoryMenus.put(c.name(), manager);
			developer.add(manager);
		}

		// Loop over the rest of the Extensions and create 
		// their sub-menus. 
		for (IExtension code : codeExtensions) {
			// Don't add ICE again...
			if (!code.getContributor().getName().equals("org.eclipse.ice.developer")) {
				// Get the name of the bundle this extension comes from 
				// so we can instantiate its AbstractHandlers
				String contributingPlugin = code.getContributor().getName();
				
				// Get the elements of this extension
				IConfigurationElement[] elements = code.getConfigurationElements();
				for (IConfigurationElement e : elements) {

					// Get the attribute data
					category = e.getAttribute("codeCategory");
					codeName = e.getAttribute("codeName");
					repoURL = e.getAttribute("repoURL");
					vcType = e.getAttribute("versionControlType");
					boolean genDefault = Boolean.valueOf(e.getAttribute("generateDefaultCommands"));

					// Create a sub menu for the code in the
					// correct category
					codeMenu = new MenuManager(codeName, codeName + "ID");

					// If requested, create the default commands
					if (genDefault) {
						createDefaultCommands(serviceLocator, vcType, codeName, repoURL, codeMenu);
					}

					// Create a menu item for each extra command they provided
					for (IConfigurationElement command : e.getChildren("command")) {
						createCommand(serviceLocator, contributingPlugin, command.getAttribute("implementation"),
								command.getAttribute("commandName"), codeMenu);
					}

					// Add it to the correct category menu
					categoryMenus.get(category).add(codeMenu);

				}
			}
		}

		// Add the newly constructed developer menu
		additions.addContributionItem(developer, null);

		return;
	}

	/**
	 * This private method creates specified commands that are attached to 
	 * a given MenuManager. 
	 * 
	 * @param serviceLocator
	 * @param plugin
	 * @param impl
	 * @param commandName
	 * @param codeMenu
	 */
	private void createCommand(IServiceLocator serviceLocator, String plugin, String impl, String commandName,
			MenuManager codeMenu) {
		// Get the services we need
		ICommandService commandService = serviceLocator.getService(ICommandService.class);
		IHandlerService handlerService = serviceLocator.getService(IHandlerService.class);

		// Create a new undefined command and then define it
		Command command = commandService.getCommand(impl);
		command.define("Git Clone", "This is created Programatically!",
				commandService.getCategory("org.eclipse.ice.developer.command.category"));

		// Load the associated IHandler for this command!
		try {
			Bundle bundle = Platform.getBundle(plugin);
			Class<?> commandClass = bundle.loadClass(impl);
			IHandler handler = (IHandler) commandClass.newInstance();
			handlerService.activateHandler(command.getId(), handler);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		// Create the ContributionItem and add it to the menu
		CommandContributionItemParameter p = new CommandContributionItemParameter(serviceLocator, "", impl, SWT.PUSH);
		p.label = commandName;
		CommandContributionItem item = new CommandContributionItem(p);
		item.setVisible(true);
		codeMenu.add(item);
	}

	/**
	 * This private method is used to create default clone and fork commands. 
	 * 
	 * @param serviceLocator
	 * @param vcType
	 * @param codeName
	 * @param repoURL
	 * @param codeMenu
	 */
	private void createDefaultCommands(IServiceLocator serviceLocator, String vcType, String codeName, String repoURL,
			MenuManager codeMenu) {
		ICommandService commandService = serviceLocator.getService(ICommandService.class);
		IHandlerService handlerService = serviceLocator.getService(IHandlerService.class);

		Command gitCommand = commandService.getCommand("org.eclipse.ice.developer.actions.gitclonehandler");
		gitCommand.define("Git Clone", "This is created Programatically!",
				commandService.getCategory("org.eclipse.ice.developer.command.category"));

		handlerService.activateHandler(gitCommand.getId(), new GitCloneHandler(repoURL));

		CommandContributionItemParameter p = new CommandContributionItemParameter(serviceLocator, "",
				"org.eclipse.ice.developer.actions.gitclonehandler", SWT.PUSH);
		p.label = "Clone " + codeName;

		CommandContributionItem item = new CommandContributionItem(p);
		item.setVisible(true);
		codeMenu.add(item);
	}

	/**
	 * This is an enumeration of all categories we can 
	 * put various scientific codes in. 
	 * 
	 * @author Alex McCaskey
	 *
	 */
	private enum CodeCategory {

		Framework,

		Nuclear,

		MolecularDynamics,

		Physics,
		
		DensityFunctionalTheory

	}

}
