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

import org.eclipse.ice.client.widgets.ICESectionPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is the default implementation of IBasicComponentPageProvider and it is responsible for
 * generating the default set of basic component pages for ICEFormEditor.
 * 
 * @author 
 *
 */

public class DefaultBasicComponentPageProvider extends DefaultPageProvider 
	implements IBasicComponentPageProvider {
	
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {
		
		ArrayList<IFormPage> sectionPages = new ArrayList<IFormPage>();
		ICESectionPage tmpPage = null;
		String pageName = null;
		int numCompsPerPage = 4, i = 0, j = 0;
		int numComponents = 0;

		numComponents = components.size();

		// If there are less components than the allowed number of components
		// per page, just add them all to the first page
		if (numComponents < numCompsPerPage) {
			// Set a name for the page that is a combination of the first two
			// components or only the first one if there is but one component
			if (numComponents == 1) {
				pageName = ((ICEObject) (components.get(0))).getName();
			} else {
				pageName = ((ICEObject) (components.get(0))).getName() + ", "
						+ ((ICEObject) (components.get(1))).getName() + ", etc.";
			}
			// Instantiate the page
			tmpPage = new ICESectionPage(formEditor, pageName, pageName);
			// Loop over the list of DataComponents and create pages for them
			for (Component component : components) {
				tmpPage.addComponent(component);
			}
			sectionPages.add(tmpPage);
		} else {
			// Otherwise, if there are more components than the number of
			// components per page, add them all with numCompsPerPage Components
			// per page. This loop adds all of the full pages.
			for (i = 0; i < (numComponents / numCompsPerPage)
					* numCompsPerPage; i = i + numCompsPerPage) {
				// Set a name for the page that is a combination of the first
				// two components
				pageName = ((ICEObject) (components.get(i))).getName() + ", "
						+ ((ICEObject) (components.get(i + 1))).getName() + ", etc.";
				// Create the page
				tmpPage = new ICESectionPage(formEditor, pageName, pageName);
				// Add the components
				for (j = 0; j < numCompsPerPage; j++) {
					// i - 1 + j is the buffer offset
					tmpPage.addComponent(components.get(i + j));
				}
				// Add the page to the list
				sectionPages.add(tmpPage);
			}

			// Clean up the left over components by just adding them to
			// their own page.
			if (i != components.size()) {
				// Start by adding setting up the name
				if (components.size() - i == 1) {
					pageName = ((ICEObject) (components.get(i))).getName();
				} else {
					pageName = ((ICEObject) (components.get(i))).getName() + ", "
							+ ((ICEObject) (components.get(i + 1))).getName()
							+ ", etc.";
				}
				// Create the page
				tmpPage = new ICESectionPage(formEditor, pageName, pageName);
				// Add the components
				while (i < components.size()) {
					tmpPage.addComponent(components.get(i));
					i++;
				}
				// Add the page to the list
				sectionPages.add(tmpPage);
			}
		}

		return sectionPages;
	}

}
