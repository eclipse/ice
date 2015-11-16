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
package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is an interface for page providers for Form Editors in ICE that provide
 * the set of pages required to draw the UI.
 * 
 * @author Jay Jay Billings
 *
 */
public interface IPageProvider {

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
	 * @param components
	 *            The list of components that should be rendered onto pages by
	 *            this provider. Determining the exact type of the components is
	 *            up to implementing class, but all the components in the list
	 *            shall have the same type. (No mixed lists!)
	 * @return the form pages created from the map
	 */
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components);

}
