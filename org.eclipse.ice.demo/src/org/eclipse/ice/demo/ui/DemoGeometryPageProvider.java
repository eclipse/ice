/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
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
package org.eclipse.ice.demo.ui;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.providers.IPageProvider;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.GeometryComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * @author Jay Jay Billings
 *
 */
public class DemoGeometryPageProvider implements IPageProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageProvider#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "demo";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageProvider#getPages(org.
	 * eclipse.ui.forms.editor.FormEditor, java.util.ArrayList)
	 */
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {

		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();
		// Get the GeometryComponent and create the GeometryPage.
		if (!(components.isEmpty())) {
			GeometryComponent geometryComponent = (GeometryComponent) (components
					.get(0));

			if (geometryComponent != null) {

				// Make the GeometryPage
				DemoGeometryPage geometryPage = new DemoGeometryPage(formEditor,
						"GPid", geometryComponent.getName());

				// No need to set the geometry component for the demo, but
				// something like would be necessary in a real application.
				// geometryPage.setGeometry(geometryComponent);

				// Add the page
				pages.add(geometryPage);
			}

		}

		return pages;
	}

}
