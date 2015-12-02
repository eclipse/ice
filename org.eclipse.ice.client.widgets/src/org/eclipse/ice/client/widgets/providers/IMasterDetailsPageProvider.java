/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Nick Stanish
 *******************************************************************************/


package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ExtensionHelper;

/**
 * This is an interface for master derails page provider for Form Editors in ICE that
 * provides the set of ICEMasterDetailsPage  required to draw the UI.
 * 
 * @author Menghan Li
 *
 */
public interface IMasterDetailsPageProvider extends IPageProvider {
	/**
	 * Extension point ID for IMasterDetailsPageProvider.
	 */
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.masterDetailsPageProvider";

	/**
	 * This is a static interface method that returns all of the currently
	 * registered IMasterDerailsPageProvider.
	 * 
	 * @return The available providers
	 * @throws CoreException
	 */
	public static ArrayList<IMasterDetailsPageProvider> getProviders() throws CoreException {
		ExtensionHelper<IMasterDetailsPageProvider> extensionHelper = new ExtensionHelper<IMasterDetailsPageProvider>();
		return extensionHelper.getExtensions(EXTENSION_POINT_ID);
		
	}
}
