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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.IParameterValues;
import org.eclipse.core.commands.ParameterValuesException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.developer.actions.GitHubCloneHandler;
import org.eclipse.jface.action.Action;
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

	/**
	 * Reference to the provided IServiceLocator 
	 * - Used for getting the ICommand and IHandler
	 * Services
	 */
	private IServiceLocator serviceLocator;

	/**
	 * Reference to the Extension Registry
	 */
	private IExtensionRegistry registry;

	/**
	 * Reference to the top-level Developer Menu
	 */
	private MenuManager developerMenu;

	/**
	 * Reference to the list of IParameters 
	 * we can provide to the created Commands
	 */
	private ArrayList<IParameter> parameters;

	/**
	 * The constructor
	 */
	public ICEExtensionContributionFactory() {
		parameters = new ArrayList<IParameter>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.menus.AbstractContributionFactory#createContributionItems(
	 * org.eclipse.ui.services.IServiceLocator,
	 * org.eclipse.ui.menus.IContributionRoot)
	 * 
	 * This implementation adds a Developer Menu Item to the Eclipse ICE Menu
	 * Bar.
	 * 
	 */
	@Override
	public void createContributionItems(IServiceLocator serviceLoc, IContributionRoot additions) {

		// Local Declarations
		HashMap<String, MenuManager> categoryMenus = new HashMap<String, MenuManager>();
		String codeName = "", category = "";
		MenuManager codeMenu = null;

		// Set the ServiceLocator
		serviceLocator = serviceLoc;

		// Create the Developer Menu Item
		developerMenu = new MenuManager("&Developer", "iceDev");

		// Get the registry and the extensions
		registry = Platform.getExtensionRegistry();

		// Create the ICE Menu here so it's first in the list
		MenuManager ice = new MenuManager("ICE", "ICEID");
		categoryMenus.put("ICE", ice);
		developerMenu.add(ice);

		// Create the category sub-menus
		for (CodeCategory c : CodeCategory.values()) {
			MenuManager manager = new MenuManager(c.name(), c.name() + "ID");
			manager.setVisible(true);
			categoryMenus.put(c.name(), manager);
			developerMenu.add(manager);
		}

		// Get the Extension Points
		IExtensionPoint extensionPoint = registry.getExtensionPoint("org.eclipse.ice.developer.code");
		IExtension[] codeExtensions = extensionPoint.getExtensions();

		// Loop over the rest of the Extensions and create
		// their sub-menus.
		for (IExtension code : codeExtensions) {
			// Get the name of the bundle this extension comes from
			// so we can instantiate its AbstractHandlers
			String contributingPlugin = code.getContributor().getName();

			// Get the elements of this extension
			IConfigurationElement[] elements = code.getConfigurationElements();
			for (IConfigurationElement e : elements) {
				// Get the Code Name
				codeName = e.getAttribute("codeName");
				
				// Get whether this is the ICE declaration
				boolean isICE = "ICE".equals(codeName);
				
				// Get the Code Category - Framework, Physics, etc...
				category = isICE ? codeName : e.getAttribute("codeCategory");

				// Create a sub menu for the code in the
				// correct category, if this is not ICE ( we've already done it for ICE)
				codeMenu = isICE ? categoryMenus.get(codeName)
						: new MenuManager(codeName, codeName + "ID");

				// Generate the IParameters for the Command
				generateParameters(e);

				// Create a menu item for each extra command they provided
				for (IConfigurationElement command : e.getChildren("command")) {
					generateParameters(command);
					createCommand(contributingPlugin, command.getAttribute("implementation"), codeMenu);
				}

				// Add it to the correct category menu
				if (!"ICE".equals(codeName)) {
					categoryMenus.get(category).add(codeMenu);
				}

			}
		}

		// Add the newly constructed developer menu
		additions.addContributionItem(developerMenu, null);

		return;
	}

	/**
	 * This private method creates specified commands that are attached to a
	 * given MenuManager.
	 * 
	 * @param serviceLocator
	 * @param plugin
	 * @param impl
	 * @param commandName
	 * @param codeMenu
	 */
	private void createCommand(String plugin, String impl, MenuManager codeMenu) {

		// Local Declarations
		HashMap<String, String> map = new HashMap<String, String>();

		// Get the services we need
		ICommandService commandService = serviceLocator.getService(ICommandService.class);
		IHandlerService handlerService = serviceLocator.getService(IHandlerService.class);

		// Map the list of Parameters to a HashMap
		for (IParameter p : parameters) {
			map.put(p.getId(), p.getName());
		}

		// Create a new undefined command and then define it
		Command command = commandService.getCommand(impl);
		command.define(map.get("commandNameID"), "This is created Programatically!",
				commandService.getCategory("org.eclipse.ice.developer.command.category"),
				parameters.toArray(new IParameter[parameters.size()]));

		// Create the ParameterizedCommand to be executed
		ParameterizedCommand parameterizedCommand = ParameterizedCommand.generateCommand(command, map);

		// Load the associated IHandler for this command!
		try {
			Bundle bundle = Platform.getBundle(plugin);
			Class<?> commandClass = bundle.loadClass(impl);
			final IHandler handler = (IHandler) commandClass.newInstance();
			handlerService.activateHandler(parameterizedCommand.getId(), handler);

			// Create a custom Action that executes the 
			// Parameterized Command instead of the Command
			Action launchAction = new Action() {
				@Override
				public void run() {
					try {
						handler.execute(handlerService.createExecutionEvent(parameterizedCommand, null));
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			};
			
			// Set it's text then add it to the Menu!
			launchAction.setText(map.get("commandNameID"));
			codeMenu.add(launchAction);

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

	}

	/**
	 * This private method is for generating the list of parameters 
	 * to be used by the Command. 
	 * 
	 * @param element
	 */
	private void generateParameters(IConfigurationElement element) {

		for (final String attribute : element.getAttributeNames()) {
			String value = element.getAttribute(attribute);
			parameters.add(new IParameter() {
				@Override
				public String getId() {
					return attribute + "ID";
				}
				@Override
				public String getName() {
					return value;
				}
				@Override
				public IParameterValues getValues() throws ParameterValuesException {
					return new IParameterValues() {
						@Override
						public Map getParameterValues() {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(getId(), getName());
							return map;
						};
					};
				}
				@Override
				public boolean isOptional() {
					return false;
				}
			});
		}
	}

	/**
	 * This is an enumeration of all categories we can put various scientific
	 * codes in.
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
