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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * for implementations of IClient. It is implemented separately from the client
 * to separate the code needed for drawing to the screen from the code needed to
 * communicate with instances of ICore. An instance of this class must be set
 * before implementations of IClient can be used.
 * 
 * @author Jay Jay Billings
 */
public interface IWidgetFactory {

	/**
	 * This operation retrieves all of the IWidgetFactories from the
	 * ExtensionRegistry.
	 *
	 * @return The array of IWidgetFactories that were found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IWidgetFactory[] getIWidgetFactories() throws CoreException {

		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(IWidgetFactory.class);

		IWidgetFactory[] builders = null;
		String id = "org.eclipse.ice.client.IWidgetFactory";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			builders = new IWidgetFactory[elements.length];
			for (int i = 0; i < elements.length; i++) {
				builders[i] = (IWidgetFactory) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return builders;
	}

	/**
	 * This operation returns an IFormWidget that is rendered by the underlying
	 * graphics package.
	 * 
	 * @param formName
	 *            The name of the Form that will be rendered with the widget.
	 *            This option can be used by classes that realize the
	 *            IWidgetFactory interface for further customization or special
	 *            checks, but it is not required (and may be null).
	 * @return An IFormWidget
	 */
	public IFormWidget getFormWidget(String formName);

	/**
	 * This operation returns an IErrorBox that is rendered by the underlying
	 * graphics package.
	 * 
	 * @return The IErrorBox
	 */
	public IErrorBox getErrorBox();

	/**
	 * This operation returns an ITextEditor that is rendered by the underlying
	 * graphics package.
	 * 
	 * @return The ITextEditor
	 */
	public ITextEditor getTextEditor();

	/**
	 * This operation returns an IExtraInfoWidget.
	 * 
	 * @return The IExtraInfoWidget.
	 */
	public IExtraInfoWidget getExtraInfoWidget();

	/**
	 * This operation returns a widget that can be used to stream text to a
	 * client.
	 * 
	 * @return The IStreamingTextWidget that can post messages to be viewed by a
	 *         client.
	 */
	public IStreamingTextWidget getStreamingTextWidget();
}