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
import org.eclipse.january.form.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementations of the IProcessEventListener are registered with a FormWidget
 * and notified when a process is selected.
 * 
 * @author Jay Jay Billings, Alex McCaskey
 */
public interface IProcessEventListener {
	
	/**
	 * This operation notifies the listener that the Form has been marked to be
	 * processed.
	 * 
	 * @param form
	 * @param process
	 */
	public void processSelected(Form form, String process);

	/**
	 * <p>
	 * This operation directs the listener to cancel the last process request
	 * for the Form. There is no guarantee that it can actually cancel the
	 * process (it may finish first!).
	 * </p>
	 * 
	 * @param form
	 *            The form that was previously processed.
	 * @param process
	 *            The name of the process that was requested for the previous
	 *            form.
	 */
	public void cancelRequested(Form form, String process);
	
	/**
	 * This operation retrieves the IProcessEventListener implementation from the
	 * ExtensionRegistry.
	 *
	 * @return The current default IProcessEventListener implementation that were
	 *         found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IProcessEventListener getProcessEventListener() throws CoreException {
		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(IProcessEventListener.class);

		IProcessEventListener listener = null;
		String id = "org.eclipse.ice.client.processEventListener";
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(id);

		// If the point is available, create the listener
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			listener = (IProcessEventListener) elements[0].createExecutableExtension("class");
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return listener;
	}
}