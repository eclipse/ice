/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Menghan Li
 *   Minor updates for architecture compliance - Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is the default implementation of IPageProvider and it is responsible for
 * generating the default set of pages for ICEFormEditor.
 * 
 * @author Menghan Li, Jay Jay Billings
 *
 */
public class DefaultResourcePageProvider extends DefaultPageProvider
		implements IResourcePageProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.providers.DefaultPageProvider#getPages(org
	 * .eclipse.ui.forms.editor.FormEditor, java.util.ArrayList)
	 */
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {

		// Local Declarations
		ResourceComponent resourceComponent = null;
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();
		ICEResourcePage resourceComponentPage = null;

		// Get the ResourceComponent and create the ICEOutput page. There
		// should
		// only be one output page.
		if (!(components.isEmpty())) {
			resourceComponent = (ResourceComponent) (components
					.get(0));
			if (resourceComponent != null) {
				// Make the page
				resourceComponentPage = new ICEResourcePage(formEditor,
						resourceComponent.getName(),
						resourceComponent.getName());
				// Set the ResourceComponent
				resourceComponentPage.setResourceComponent(resourceComponent);
			}
		}

		// Add and return the page
		pages.add(resourceComponentPage);
		return pages;
	}

}
