/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.item;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.item.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A place hold for the eventual IActionFactory service interface.
 * 
 * @author Jay Jay Billings
 *
 */
public interface IActionFactory {

	/**
	 * Return the Action with the provided name.
	 * 
	 * @param actionName
	 *            The name of the Action to create.
	 * @return action The Action corresponding to the provided name.
	 */
	public Action getAction(String actionName);

	/**
	 * This operation retrieves the IActionFactory implementation from the
	 * ExtensionRegistry.
	 *
	 * @return The current default IActionFactory implementation that were found
	 *         in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IActionFactory getActionFactory() throws CoreException {
		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(IActionFactory.class);

		IActionFactory factory = null;
		String id = "org.eclipse.ice.item.actionFactory";
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(id);

		// If the point is available, create all the factories and load them
		// into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			factory = (IActionFactory) elements[0].createExecutableExtension("class");
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return factory;
	}
}
