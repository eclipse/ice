/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.reflectivity.ui;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.client.widgets.IFormWidgetBuilder;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.viz.service.IVizServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the declarative service that registers the Reflectivity
 * FormEditor Widget with ICE's service for dynamically extending the widget
 * factory. This lets us tailor the standard ICEFormEditor behavior for
 * Reflectivity Model Builders.
 *
 * @author Kasper Gammeltoft, Jordan H. Deyton
 *
 */
public class ReflectivityFormWidgetBuilder implements IFormWidgetBuilder {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger(ReflectivityFormWidgetBuilder.class);

	/**
	 * The name of the items/forms used to create {@link ReflectivityFormEditor}
	 * s. This should be the same as the name in the ReflectivityModel item
	 * class.
	 */
	public static final String name = "Reflectivity Model";

	/**
	 * This class consumes (references) the {@link IVizServiceFactory} OSGi
	 * service. The factory should be passed down to the ReflectivityFormEditor.
	 */
	private IVizServiceFactory vizServiceFactory;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.client.widgets.IFormWidgetBuilder#getTargetFormName
	 * ()
	 */
	@Override
	public String getTargetFormName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.client.widgets.IFormWidgetBuilder#build()
	 */
	@Override
	public IFormWidget build() {
		return new ReflectivityEclipseFormWidget(vizServiceFactory);
	}

	/**
	 * This method is called by the OSGi implementation to bind the
	 * {@link IVizServiceFactory} (an OSGi service) to this instance.
	 *
	 * @param factory
	 *            The factory service available through OSGi.
	 */
	public void setVizServiceFactory(IVizServiceFactory factory) {
		logger.info("ReflectivityFormWidgetBuilder: Setting Viz Service");
		this.vizServiceFactory = factory;

		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("org.eclipse.ice.viz.service.IVizServiceFactory");
		logger.info("ReflectivityFormWidgetBuilder: Available configuration elements");
		for(IConfigurationElement element : elements){
			logger.info(element.getName());
		}
		
		return;
	}

	/**
	 * This method is called by the OSGi implementation to unbind the
	 * {@link IVizServiceFactory} (an OSGi service) from this instance.
	 *
	 * @param factory
	 *            The factory service that is no longer available through OSGi.
	 */
	public void unsetVizServiceFactory(IVizServiceFactory factory) {
		if (factory == this.vizServiceFactory) {
			this.vizServiceFactory = null;
		}
	}

}
