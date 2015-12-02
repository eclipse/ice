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

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * This interfaces establishes the contract for factories that would provide a
 * set of pages to the ICEFormEditor.
 * 
 * @author Jay Jay Billings
 *
 */
public interface IPageFactory {

	/**
	 * ID for the associated extension point.
	 */
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.pageFactory";

	/**
	 * This operation returns the rendered pages that will display the contents
	 * of the resource components.
	 * 
	 * @param editor
	 *            the form editor where the pages will reside
	 * @param components
	 *            the resource components whose contents should be rendered
	 * @return the form pages on which the contents of the resources have been
	 *         rendered
	 */
	public ArrayList<IFormPage> getResourceComponentPages(FormEditor editor,
			ArrayList<Component> components);

	/**
	 * This operation returns the rendered pages that will display the contents
	 * of the list components.
	 *
	 * @param editor
	 *            the form editor where the pages will reside
	 * @param components
	 *            the list components whose contents should be rendered
	 * @return the form pages on which the contents of the lists have been
	 *         rendered
	 */
	public ArrayList<IFormPage> getListComponentPages(FormEditor editor,
			ArrayList<Component> components);

	/**
	 * This operation returns an error page stating that the form was empty.
	 *
	 * @param editor
	 *            the form editor where the page will reside
	 * @return the form page showing an error
	 */
	public IFormPage getErrorPage(FormEditor editor);

	/**
	 * 
	 * @param editor
	 * @param components
	 * @return
	 */
	public ArrayList<IFormPage> getComponentPages(FormEditor editor,
			ArrayList<Component> components);
	
	public ArrayList<IFormPage> getGeometryComponentPages(FormEditor editor,
			ArrayList<Component> components);
	
	public ArrayList<IFormPage> getIEFSectionComponentPages(FormEditor editor,
			ArrayList<Component> components);
}
