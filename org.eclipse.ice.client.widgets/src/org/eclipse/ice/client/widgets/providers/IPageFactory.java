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

import org.eclipse.january.form.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

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
	 * This operation returns the pages rendered for DataComponent,
	 * TableComponents and MatrixComponents. These "Basic Components" are
	 * combined to optimize page usage.
	 * 
	 * @param editor
	 *            the form editor where the pages will reside
	 * @param components
	 *            the data, table or matrix components that should be rendered
	 * @return the form pages on which the components have been rendered
	 */
	public ArrayList<IFormPage> getBasicComponentPages(FormEditor editor,
			ArrayList<Component> components);

	/**
	 * This operation returns the rendered pages that will display the contents
	 * of the Geometry components.
	 *
	 * @param editor
	 *            the form editor where the pages will reside
	 * @param components
	 *            the geometry components whose contents should be rendered
	 * @return the form pages on which the contents of the lists have been
	 *         rendered
	 */
	public ArrayList<IFormPage> getGeometryComponentPages(FormEditor editor,
			ArrayList<Component> components);

	/**
	 * This operation returns the rendered pages that will display the contents
	 * of the IEMFSection components.
	 *
	 * @param editor
	 *            the form editor where the pages will reside
	 * @param components
	 *            the EMF components whose contents should be rendered
	 * @return the form pages on which the contents of the lists have been
	 *         rendered
	 */
	public ArrayList<IFormPage> getIEMFSectionComponentPages(FormEditor editor,
			ArrayList<Component> components);

	/**
	 * This operation returns the rendered pages that will display the contents
	 * of the Mesh components.
	 *
	 * @param editor
	 *            the form editor where the pages will reside
	 * @param components
	 *            the mesh components whose contents should be rendered
	 * @return the form pages on which the contents of the lists have been
	 *         rendered
	 */
	public ArrayList<IFormPage> getMeshComponentPages(FormEditor editor,
			ArrayList<Component> components);

	/**
	 * This operation returns the rendered pages that will display the contents
	 * of the master-details pattern.
	 * 
	 * @param editor
	 *            the form editor where the pages will reside
	 * @param components
	 *            the master-details pages whose contents should be rendered
	 * @return the form pages on which the contents of the masterDetails have
	 *         been rendered
	 */
	public ArrayList<IFormPage> getMasterDetailsPages(FormEditor editor,
			ArrayList<Component> components);

}
