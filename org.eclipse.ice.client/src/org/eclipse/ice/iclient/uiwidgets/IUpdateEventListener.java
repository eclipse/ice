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
import org.eclipse.ice.datastructures.form.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementations of the IUpdateEventListener are registered with a FormWidget
 * and notified when the Form is updated.
 * 
 * @author Jay Jay Billings, Alex McCaskey
 */
public interface IUpdateEventListener {
	/**
	 * This operation notifies the listener that the included Form has been
	 * updated.
	 * 
	 * @param form
	 *            The widget that was changed.
	 */
	public void formUpdated(Form form);

	/**
	 * This operation retrieves the IUpdateEventListener implementation from the
	 * ExtensionRegistry.
	 *
	 * @return The current default IUpdateEventListener implementation that were
	 *         found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IUpdateEventListener getUpdateEventListener() throws CoreException {
		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(IUpdateEventListener.class);

		IUpdateEventListener listener = null;
		String id = "org.eclipse.ice.client.updateEventListener";
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(id);

		// If the point is available, create the listener
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			listener = (IUpdateEventListener) elements[0].createExecutableExtension("class");
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		System.out.println("Returning a valid IUpdateEventListener");
		return listener;
	}
}