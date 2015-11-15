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
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public static IErrorPageProvider[] getProviders() throws CoreException {
		// Logger for handling event messages and other information.
		Logger logger = LoggerFactory.getLogger(IErrorPageProvider.class);
		IErrorPageProvider[] errorPageProviders = null;

		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(EXTENSION_POINT_ID);

		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			errorPageProviders = new IErrorPageProvider[elements.length];
			for (int i = 0; i < elements.length; i++) {
				errorPageProviders[i] = (IErrorPageProvider) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + EXTENSION_POINT_ID
					+ " does not exist");
		}
		return errorPageProviders;
	}

}