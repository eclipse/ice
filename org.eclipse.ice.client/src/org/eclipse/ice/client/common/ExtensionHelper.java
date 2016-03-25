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



package org.eclipse.ice.client.common;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic helper for extensions of a given type
 * 
 * @author Nick Stanish
 *
 * @param <T> Type of extensions to be helped with
 */
public class ExtensionHelper<T> {
	
	public ExtensionHelper (){
		
	}
	
	/**
	 * Helper for finding all extensions of an extension point id and casting them to the generic type
	 * @param extensionPointID
	 * @return
	 * @throws CoreException
	 */
	public ArrayList<T> getExtensions(String extensionPointID) throws CoreException{
		// Logger for handling event messages and other information.
		Logger logger = LoggerFactory.getLogger(ExtensionHelper.class);
		ArrayList<T> providers = new ArrayList<T>();

		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(extensionPointID);

		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				T provider = (T) elements[i].createExecutableExtension("class");
				providers.add(provider);
			}
		} else {
			logger.error("Extension Point " + extensionPointID
					+ " does not exist");
		}
		return providers;
	}

}
