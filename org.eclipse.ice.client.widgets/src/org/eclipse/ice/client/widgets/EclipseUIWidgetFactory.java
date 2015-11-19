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
package org.eclipse.ice.client.widgets;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.iclient.uiwidgets.IErrorBox;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;
import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class is a concrete implementation of the UIWidgetFactory abstract class
 * and creates widgets that work with the Eclipse Rich Client Platform.
 * </p>
 * <p>
 * The IFormWidget that the factory uses to render Forms can be customized by
 * realizing the IFormWidgetBuilder interface and registering it dynamically
 * with the EclipseUIWidgetFactory. When the factory tries to create a new
 * FormWidget, it will compare the name of the Form with the target Item names
 * of the IFormWidgetBuilders that have registered with it. If there is a match,
 * it will use that Builder to create a new IFormWidget and render the Form.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseUIWidgetFactory implements IWidgetFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EclipseUIWidgetFactory.class);

	/**
	 * <p>
	 * The set of IFormWidgetBuilders registered with the Factory.
	 * </p>
	 * 
	 */
	private HashMap<String, IFormWidgetBuilder> widgetBuildersMap;

	/**
	 * The constructor
	 */
	public EclipseUIWidgetFactory() {
		widgetBuildersMap = new HashMap<String, IFormWidgetBuilder>();
	}

	/**
	 * This operation registers an IFormWidgetBuilder with the Factory. The
	 * IFormWidgetBuilder is used to extend the abilities of the default set of
	 * EclipseUIWidgets to draw ICE's Form's.
	 * 
	 * @param builder
	 *            The builder that will generate the custom IFormWidget.
	 */
	public void registerFormWidgetBuilder(IFormWidgetBuilder builder) {

		// Only add the widget builder if it is good
		if (builder != null && builder.getTargetFormName() != null) {
			widgetBuildersMap.put(builder.getTargetFormName(), builder);
			logger.info("EclipseUIWidgetFactory Message: New "
					+ "IFormWidgetBuilder registered for "
					+ builder.getTargetFormName());
		}

		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						"org.eclipse.ice.client.widgets.iformwidgetbuilder");
		logger.info(
				"Available configuration elements(in org.eclipse.ice.client.widgets.EclipseUIWdigetFactory.java):");
		for (IConfigurationElement element : elements) {
			logger.info(
					element.getNamespaceIdentifier() + " " + element.getName());
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IWidgetFactory#getFormWidget(String formName)
	 */
	@Override
	public IFormWidget getFormWidget(String formName) {

		// Local Declarations
		IFormWidget widget = null;

		// Use a service to create the FormWidget if possible
		if (widgetBuildersMap.containsKey(formName)) {
			widget = widgetBuildersMap.get(formName).build();
		} else {
			// Otherwise just create a "regular" FormWidget
			widget = new EclipseFormWidget();
		}
		return widget;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetFactory#getErrorBox()
	 */
	@Override
	public IErrorBox getErrorBox() {
		return new EclipseErrorBoxWidget();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetFactory#getTextEditor()
	 */
	@Override
	public ITextEditor getTextEditor() {
		return new EclipseTextEditor();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetFactory#getExtraInfoWidget()
	 */
	@Override
	public IExtraInfoWidget getExtraInfoWidget() {
		return new EclipseExtraInfoWidget();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetFactory#getStreamingTextWidget()
	 */
	@Override
	public IStreamingTextWidget getStreamingTextWidget() {
		return new EclipseStreamingTextWidget();
	}
}