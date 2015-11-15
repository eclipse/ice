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

import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.client.widgets.ListComponentSectionPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This class is a default extension for providing list section pages
 * 
 * @author Nick Stanish
 *
 */
public class DefaultListPageProvider implements IListPageProvider {

	/**
	 * The page provider name for this class.
	 */
	public static final String PROVIDER_NAME = "default";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IListPageProvider#getName()
	 */
	@Override
	public String getName() {
		return PROVIDER_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.providers.IListPageProvider#getPages(org.
	 * eclipse.ui.forms.editor.FormEditor, java.util.Map)
	 */
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor, Map<String, ArrayList<Component>> componentMap) {

		// Get the lists from the component map
		ArrayList<Component> lists = componentMap.get("list");
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>(lists.size());

		// If there are some lists, render sections for them
		for (int i = 0; i < lists.size(); i++) {
			ListComponent<?> list = (ListComponent<?>) lists.get(i);
			// Make sure the list isn't null since that value can be put in
			// a collection
			if (list != null) {
				// Create a new page for the list
				ListComponentSectionPage page = new ListComponentSectionPage(formEditor, list.getName(),
						list.getName());

				page.setList(list);
				// Add the page to the return list
				pages.add(page);
			}
		}

		return pages;
	}
	
}
