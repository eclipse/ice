/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and re-implementation and/or initial documentation - 
 *   Nick Stanish
 *   Minor updates for architecture compliance, original implementation - 
 *   Jay Jay Billings
 *******************************************************************************/

package org.eclipse.ice.client.widgets.providers.Default;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.ListComponentSectionPage;
import org.eclipse.ice.client.widgets.providers.IListPageProvider;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.ListComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This class is a default extension for providing list section pages
 * 
 * @author Nick Stanish, Jay Jay Billings
 *
 */
public class DefaultListPageProvider extends DefaultPageProvider
		implements IListPageProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.providers.IListPageProvider#getPages(org.
	 * eclipse.ui.forms.editor.FormEditor, java.util.ArrayList)
	 */
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {

		// Get the lists from the component map
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>(
				components.size());

		// If there are some lists, render sections for them
		for (int i = 0; i < components.size(); i++) {
			ListComponent<?> list = (ListComponent<?>) components.get(i);
			// Make sure the list isn't null since that value can be put in
			// a collection
			if (list != null) {
				// Create a new page for the list
				ListComponentSectionPage page = new ListComponentSectionPage(
						formEditor, list.getName(), list.getName());

				page.setList(list);
				// Add the page to the return list
				pages.add(page);
			}
		}

		return pages;
	}

}
