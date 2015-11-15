/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Nick Stanish, Jay Jay Billings
 *******************************************************************************/

package org.eclipse.ice.client.widgets.providers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an interface for list page providers for Form Editors in ICE that
 * provides the set of list section pages required to draw the UI.
 * 
 * @author Nick Stanish, Jay Jay Billings
 *
 */
public interface IListPageProvider extends IPageProvider {

	/**
	 * Extension point ID for IListPageProviders.
	 */
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.listPageProvider";

	/**
	 * This is a static interface method that returns all of the currently
	 * registered IListPageProvider.
	 * 
	 * @return The available providers
	 * @throws CoreException
	 */
	public static IListPageProvider[] getProviders() throws CoreException {
		// Logger for handling event messages and other information.
		Logger logger = LoggerFactory.getLogger(IListPageProvider.class);
		IListPageProvider[] listPageProviders = null;

		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(EXTENSION_POINT_ID);

		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			listPageProviders = new IListPageProvider[elements.length];
			for (int i = 0; i < elements.length; i++) {
				listPageProviders[i] = (IListPageProvider) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + EXTENSION_POINT_ID
					+ " does not exist");
		}
		return listPageProviders;
	}

}