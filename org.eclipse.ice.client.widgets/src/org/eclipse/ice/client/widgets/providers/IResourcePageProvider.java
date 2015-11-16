/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Menghan Li
 *   Minor updates for architecture compliance - Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets.providers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * This is an interface for page providers for Form Editors in ICE that provides
 * the set of pages required to draw the UI.
 * 
 * @author Menghan Li, Jay Jay Billings
 *
 */
public interface IResourcePageProvider extends IPageProvider {

	/**
	 * ID for the associated extension point.
	 */
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.resourcePageProvider";

	/**
	 * This is a static interface method that returns all of the currently
	 * registers IResourceProviders.
	 * 
	 * @return The available providers
	 */
	public static IResourcePageProvider[] getProviders() throws CoreException {
		Logger logger = LoggerFactory.getLogger(IResourcePageProvider.class);
		IResourcePageProvider[] resourcePageProviders = null;

		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(EXTENSION_POINT_ID);

		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			resourcePageProviders = new IResourcePageProvider[elements.length];
			for (int i = 0; i < elements.length; i++) {
				resourcePageProviders[i] = (IResourcePageProvider) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + EXTENSION_POINT_ID
					+ " does not exist");
		}
		return resourcePageProviders;
	}

}
