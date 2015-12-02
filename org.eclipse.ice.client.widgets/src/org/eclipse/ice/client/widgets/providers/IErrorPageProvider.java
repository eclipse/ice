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

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ExtensionHelper;

/**
 * This is an interface for error page providers for Form Editors in ICE that
 * provides the error pages required to draw the UI.
 * 
 * @author Nick Stanish, Jay Jay Billings
 *
 */
public interface IErrorPageProvider extends IPageProvider {

	/**
	 * Extension point ID for IErrorPageProviders.
	 */
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.errorPageProvider";

	/**
	 * This is a static interface method that returns all of the currently
	 * registered IErrorPageProvider.
	 * 
	 * @return The available providers
	 * @throws CoreException
	 */
	public static ArrayList<IErrorPageProvider> getProviders() throws CoreException {
		ExtensionHelper<IErrorPageProvider> extensionHelper = new ExtensionHelper<IErrorPageProvider>();
		return extensionHelper.getExtensions(EXTENSION_POINT_ID);
	}

}