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

/**
 * This class provides an interface for an OSGi-DS-based registry. The current
 * intention is to provide a registry of IAnalysisWidgetFactories. When a
 * particular model is loaded for analysis, the model's class is passed to the
 * registry, which will return a factory used to generate IAnalysisViews
 * customized for that particular model.<br>
 * <br>
 * For an implementation of this interface, see {@link AnalysisWidgetRegistry}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IAnalysisWidgetRegistry {

	/**
	 * Adds an {@link IAnalysisWidgetFactory} to the registry. This factory
	 * should be used to generate customized {@link IAnalysisView} instances.
	 * 
	 * @param factory
	 *            The IAnalysisWidgetFactory to add to the registry.
	 */
	public void addAnalysisWidgetFactory(IAnalysisWidgetFactory factory);

	/**
	 * Queries the registry for factories registered to a particular class.
	 * 
	 * @param key
	 *            The class of the model to analyze.
	 * @return Returns an {@link IAnalysisWidgetFactory} registered to the
	 *         provided class, or <code>null</code> if there is none.
	 */
	public IAnalysisWidgetFactory getAnalysisWidgetFactory(Class<?> key);
}
