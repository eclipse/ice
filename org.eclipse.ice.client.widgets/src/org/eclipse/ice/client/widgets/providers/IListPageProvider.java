/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Nick Stanish
 *******************************************************************************/

package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.jaxbclassprovider.IJAXBClassProvider;
import org.eclipse.ui.forms.editor.FormEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an interface for list page providers for Form Editors in ICE that provides
 * the set of list section pages required to draw the UI.
 * 
 * @author Jay Jay Billings
 *
 */
public interface IListPageProvider {
	
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.listPageProvider";
	
	
	/**
	 * This is a static interface method that returns all of the currently
	 * registered IListPageProvider.
	 * 
	 * @return The available providers
	 * @throws CoreException 
	 */
	public static IListPageProvider[] getProviders() throws CoreException {
		// Logger for handling event messages and other information.
		Logger logger = LoggerFactory.getLogger(IJAXBClassProvider.class);
		IListPageProvider[] listPageProviders = null;
		
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT_ID);

		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			listPageProviders = new IListPageProvider[elements.length];
			for (int i = 0; i < elements.length; i++) {
				listPageProviders[i] = (IListPageProvider) elements[i].createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + EXTENSION_POINT_ID + " does not exist");
		}
		return listPageProviders;
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
	 * @param formEditor
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
	public ArrayList<ICEFormPage> getPages(FormEditor formEditor, Map<String, ArrayList<Component>> componentMap);

}