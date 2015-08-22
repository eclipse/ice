/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides an implementation of {@link IAnalysisWidgetRegistry}. The
 * underlying structure is a Map with Classes as the key and
 * {@link IAnalysisWidgetFactory} instances as the value.<br>
 * <br>
 * Since this class is meant to provide an OSGi Declarative Service, please see
 * the corresponding <code>AnalysisWidgetRegistry.xml</code>. There should also
 * be a bundle somewhere that consumes (references) this service.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class AnalysisWidgetRegistry implements IAnalysisWidgetRegistry {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AnalysisWidgetRegistry.class);

	/**
	 * The Map providing the structure of the registry for
	 * IAnalysisWidgetFactories.
	 */
	private Map<Class<?>, IAnalysisWidgetFactory> analysisWidgetFactoryRegistry = new HashMap<Class<?>, IAnalysisWidgetFactory>();

	/**
	 * Adds an {@link IAnalysisWidgetFactory} to the registry. If the factory's
	 * {@link IAnalysisWidgetFactory#getModelClasses()} returns null, the
	 * factory will not be added. If the method returns a pre-existing class
	 * already in the registry, the old factory will be replaced with this one.
	 * 
	 */
	@Override
	public void addAnalysisWidgetFactory(IAnalysisWidgetFactory factory) {
		logger.info("AnalysisWidgetRegistry message: "
				+ "Adding an IAnalysisWidgetFactory.");

		if (factory != null) {
			List<Class<?>> classes = factory.getModelClasses();
			if (classes != null) {
				for (Class<?> c : classes) {
					analysisWidgetFactoryRegistry.put(c, factory);
				}
			}
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.reactoreditor.prototype.
	 * IAnalysisWidgetRegistry#getAnalysisWidgetFactory(java.lang.Class)
	 */
	@Override
	public IAnalysisWidgetFactory getAnalysisWidgetFactory(Class<?> key) {
		if (key != null) {
			logger.info("AnalysisWidgetRegistry message: "
					+ "Fetching an IAnalysisWidgetFactory for class name \""
					+ key.getName() + "\"");
			return analysisWidgetFactoryRegistry.get(key);
		}
		return null;
	}
}
