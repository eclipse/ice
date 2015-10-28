/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is an interface for page providers for Form Editors in ICE that provides
 * the set of pages required to draw the UI.
 * 
 * @author Jay Jay Billings
 *
 */
public interface IPageProvider {

	/**
	 * This is a static interface method that returns all of the currently
	 * registers IPageProviders.
	 * 
	 * @return The available providers
	 */
	public static IPageProvider[] getProviders() {
		// Add Extensions grabbing code
		return null;
	}

	/**
	 * This operation returns the name of the provider.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * This operation directs the provider to create and return all of its pages
	 * based on the provided set of pages.
	 * 
	 * @param componentMap
	 *            This map must contain the Components in the Form organized by
	 *            type. The type is the key and a string equal to one of "data,"
	 *            "output," "matrix," "masterDetails", "table," "geometry,"
	 *            "shape," "tree," "mesh," or "reactor." The value is a list
	 *            that stores all components of that type; DataComponent,
	 *            ResourceComponent, MatrixComponent, MasterDetailsComponent,
	 *            TableComponent, GeometryComponent, ShapeComponent,
	 *            TreeComponent, MeshComponent, ReactorComponent, etc. This is a
	 *            simulated multimap.
	 * @return the form pages created from the map
	 */
	public IFormPage[] getPages(Map<String, ArrayList<Component>> componentMap);

}
