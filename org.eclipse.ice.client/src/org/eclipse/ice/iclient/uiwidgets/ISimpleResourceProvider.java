/*******************************************************************************
* Copyright (c) 2012, 2014 UT-Battelle, LLC.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Initial API and implementation and/or initial documentation - Jay Jay Billings,
*   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
*   Claire Saunders, Matthew Wang, Anna Wojtowicz
*******************************************************************************/
package org.eclipse.ice.iclient.uiwidgets;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.january.form.ICEResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface simplifies the loading and retrieval ICEResources. It should
 * be used to retrieve an ICEResource to gather metadata and to direct ICE to
 * load the resource. In general, the ICEResource returned from getResource()
 * should not be loaded by clients but should be used only to gather metadata
 * about the resource. The provider will properly load the resource in a way
 * that is consistent with its inner workings if clients call loadResource().
 * 
 * @author Jay Jay Billings
 */
public interface ISimpleResourceProvider {
	/**
	 * This operation directs the simple provider to load the resource in a way
	 * that is consistent with its inner workings.
	 * 
	 * @param resource
	 *            The ICEResource that should be loaded by the provider.
	 */
	public void loadResource(ICEResource resource);
	
	/**
	 * This operation retrieves the ISimpleResourceProvider implementation from the
	 * ExtensionRegistry.
	 *
	 * @return The current default ISimpleResourceProvider implementation that were
	 *         found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static ISimpleResourceProvider getSimpleResourceProvider() throws CoreException {
		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(ISimpleResourceProvider.class);

		ISimpleResourceProvider provider = null;
		String id = "org.eclipse.ice.client.simpleResourceProvider";
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(id);

		// If the point is available, create the provider
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			provider = (ISimpleResourceProvider) elements[0].createExecutableExtension("class");
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return provider;
	}
}