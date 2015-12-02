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
 *   Using extension helper to fetch providers - Nick Stanish
 *******************************************************************************/
package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ExtensionHelper;

/**
 * This is an interface for page providers for Form Editors in ICE that provides
 * the set of pages required to draw the UI.
 * 
 * @author Menghan Li, Jay Jay Billings, Nick Stanish
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
	public static ArrayList<IResourcePageProvider> getProviders() throws CoreException {
		ExtensionHelper<IResourcePageProvider> extensionHelper = new ExtensionHelper<IResourcePageProvider>();
		return extensionHelper.getExtensions(EXTENSION_POINT_ID);
	}

}
