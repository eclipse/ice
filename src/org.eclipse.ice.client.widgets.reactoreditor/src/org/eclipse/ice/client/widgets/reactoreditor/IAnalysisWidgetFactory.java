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

import org.eclipse.ice.datastructures.ICEObject.Component;

import java.util.List;

import org.eclipse.jface.wizard.IWizard;

/**
 * This provides a factory interface for producing analysis widgets. For
 * example, the ReactorEditor requires a number of specialized Composites for
 * analysis. The ReactorEditorWidgetFactory manages these specialized
 * Composites.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IAnalysisWidgetFactory {

	/**
	 * Returns an array of names associated with all allowed views (AVCs).
	 * 
	 * @param dataSource
	 *            The data source, e.g., "Input" or "Reference", for the new
	 *            AVC.
	 * 
	 * @return An array of allowable view names.
	 */
	public List<String> getAvailableViews(DataSource dataSource);

	/**
	 * Creates one of the possible views given a string. The requester needs to
	 * supply the parent Composite.
	 * 
	 * @param viewName
	 *            The name of the view. These should come from
	 *            getAvailableViews().
	 * @param parent
	 *            The parent Composite of the new AVC.
	 * @param parentATC
	 *            The parent AnalysisToolComposite of the new AVC.
	 * @param dataSource
	 *            The data source, e.g., "Input" or "Reference", for the new
	 *            AVC.
	 * @return Returns a new AnalysisViewComposite or null if viewName is
	 *         invalid.
	 */
	public IAnalysisView createView(String viewName, DataSource dataSource);

	/**
	 * This method should return a class. This factory will create
	 * IAnalysisViews for objects of that class.
	 * 
	 * @return The class of the model for which we must analyze.
	 */
	public List<Class<?>> getModelClasses();

	/**
	 * This method should return an {@link IStateBrokerHandler}. Each
	 * {@link IAnalysisView} may need its own keys, so the key provider should
	 * be tailored for this specific set of analysis widgets.
	 * 
	 * @return A new IStateBrokerKeyProvider for this set of analysis widgets.
	 */
	public IStateBrokerHandler createStateBrokerHandler();

	/**
	 * Gets a JFace {@link IWizard} for creating a new child of the specified
	 * component.
	 * 
	 * @param selection
	 *            The object that will be getting a wizard.
	 * @return An IWizard, or null if no wizard is available for the component
	 *         type.
	 */
	public IWizard createWizard(Object selection);
}
