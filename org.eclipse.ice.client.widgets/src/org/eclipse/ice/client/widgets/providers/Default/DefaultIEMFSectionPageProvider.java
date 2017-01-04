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
package org.eclipse.ice.client.widgets.providers.Default;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.EMFSectionPage;
import org.eclipse.ice.client.widgets.providers.IEMFSectionPageProvider;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.emf.EMFComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This class is a default extension for providing Default IEMFSection Page
 * 
 * @author Fangzhou Lin, Jay Jay Billings
 *
 */
public class DefaultIEMFSectionPageProvider extends DefaultPageProvider
		implements IEMFSectionPageProvider {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.providers.IEMFSectionPageProvider(org.
	 * eclipse.ui.forms.editor.FormEditor, java.util.ArrayList)
	 */
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();
		EMFComponent emfComponent = null;
		EMFSectionPage emfPage = null;
		if (components.size() > 0) {
			for (Component comp : components) {
				emfComponent = (EMFComponent) comp;
				if (emfComponent != null) {
					// Make the EMFSectionPage
					emfPage = new EMFSectionPage(formEditor,
							emfComponent.getName(), emfComponent.getName());
					// Set the EMFComponent
					emfPage.setEMFComponent(emfComponent);
					pages.add(emfPage);
				}
			}
		}

		return pages;
	}
}
