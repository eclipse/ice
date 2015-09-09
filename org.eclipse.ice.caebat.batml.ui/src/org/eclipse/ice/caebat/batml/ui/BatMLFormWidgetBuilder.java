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
package org.eclipse.ice.caebat.batml.ui;

import org.eclipse.ice.client.widgets.IFormWidgetBuilder;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.viz.service.IVizServiceFactory;

/**
 * This class provides the declarative service that registers the Reflectivity
 * FormEditor Widget with ICE's service for dynamically extending the widget
 * factory. This lets us tailor the standard ICEFormEditor behavior for
 * Reflectivity Model Builders.
 * 
 * @author Kasper Gammeltoft, Jordan H. Deyton
 *
 */
public class BatMLFormWidgetBuilder implements IFormWidgetBuilder {

	/**
	 * The name of the items/forms used to create {@link ReflectivityFormEditor}
	 * s. This should be the same as the name in the ReflectivityModel item
	 * class.
	 */
	public static final String name = "BatML Model";

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
		return new BatMLEclipseFormWidget(vizServiceFactory);
	}

	/**
	 * This method is called by the OSGi implementation to bind the
	 * {@link IVizServiceFactory} (an OSGi service) to this instance.
	 * 
	 * @param factory
	 *            The factory service available through OSGi.
	 */
	public void setVizServiceFactory(IVizServiceFactory factory) {
		System.out.println("ReflectivityFormWidgetBuilder: Setting Viz Service");
		this.vizServiceFactory = factory;
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
