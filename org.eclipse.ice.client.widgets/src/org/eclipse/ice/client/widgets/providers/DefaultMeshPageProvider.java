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

import org.eclipse.ice.client.widgets.ICEMeshPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This class is a default extension for providing Default Mesh Page
 * 
 * @author Fangzhou Lin, Jay Jay Billings
 *
 */
public class DefaultMeshPageProvider extends DefaultPageProvider
		implements IMeshPageProvider {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IMeshPageProvider(org.
	 * eclipse.ui.forms.editor.FormEditor, java.util.ArrayList)
	 */
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();

		MeshComponent meshComponent = new MeshComponent();

		// Get the GeometryComponent and create the GeometryPage.
		if (!(components.isEmpty())) {
			meshComponent = (MeshComponent) (components.get(0));

			if (meshComponent != null) {
				ICEMeshPage meshPage;
				// Make the MeshPage
				meshPage = new ICEMeshPage(formEditor, "MeshPid",
						meshComponent.getName());

				// Set the MeshComponent
				meshPage.setMeshComponent(meshComponent);
				pages.add(meshPage);
			}

		}

		return pages;
	}
}
