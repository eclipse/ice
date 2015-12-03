/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Menghan Li
 * 
 *******************************************************************************/

package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;
import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.client.widgets.ICEMasterDetailsPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This class is a default extension for providing master detail pages
 * 
 * @author Menghan Li
 *
 */
public class DefaultMasterDetailsPageProvider extends DefaultPageProvider
		implements IMasterDetailsPageProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.providers.IMasterDetailsPageProvider#getPages(org.
	 * eclipse.ui.forms.editor.FormEditor, java.util.ArrayList)
	 */
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {
		
		        ArrayList<IFormPage> pages = new ArrayList<IFormPage>();
				ArrayList<ICEFormPage> masterDetailsPages = new ArrayList<ICEFormPage>();
				MasterDetailsComponent masterDetailsComponent = null;

				// Get the MasterDetailsComponent and create the MasterDetails page.
				if (!components.isEmpty()) {
					masterDetailsComponent = (MasterDetailsComponent) (components.get(0));
					if (masterDetailsComponent != null) {
						// Get the name
						String name = masterDetailsComponent.getName();
						// Make the page
						ICEMasterDetailsPage iCEMasterDetailsPage = new ICEMasterDetailsPage(
								formEditor, "MDPid", name);

						// Set the MasterDetailsComponent
						iCEMasterDetailsPage
								.setMasterDetailsComponent(masterDetailsComponent);

						masterDetailsPages.add(iCEMasterDetailsPage);
					}

				}

				pages.addAll(masterDetailsPages);
				return pages;
	}

}
