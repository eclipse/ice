/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and re-implementation and/or initial documentation - 
 *   Fangzhou Lin
 *   Minor updates for architecture compliance, original implementation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.ICEGeometryPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.ICEGeometry;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This class is a default extension for providing Default Geometry Page
 * 
 * @author Fangzhou Lin, Jay Jay Billings
 *
 */
public class DefaultGeometryPageProvider extends DefaultPageProvider
		implements IGeometryPageProvider {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.providers.IGeometryPageProvider#getPages(
	 * org. eclipse.ui.forms.editor.FormEditor, java.util.ArrayList)
	 */
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {
		ICEGeometryPage geometryPage;
		GeometryComponent geometryComponent = new GeometryComponent();
		geometryComponent.setGeometry(new ICEGeometry());
		ArrayList<IFormPage> regeometryPage = new ArrayList<IFormPage>();
		// Get the GeometryComponent and create the GeometryPage.
		if (!(components.isEmpty())) {
			geometryComponent = (GeometryComponent) (components.get(0));

			if (geometryComponent != null) {

				// Make the GeometryPage
				geometryPage = new ICEGeometryPage(formEditor, "GPid",
						geometryComponent.getName());

				// Set the GeometryComponent
				geometryPage.setGeometry(geometryComponent);
				regeometryPage.add(geometryPage);
			}

		}

		return regeometryPage;
	}
}
