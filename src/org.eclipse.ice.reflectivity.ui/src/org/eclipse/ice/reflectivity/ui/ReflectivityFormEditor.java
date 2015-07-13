/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.reflectivity.ui;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * The custom form editor for the reflectivity model. Should be used instead of
 * {@link ICEFormEditor} to display reflectivity models.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class ReflectivityFormEditor extends ICEFormEditor {

	/**
	 * ID for Eclipse, used for the bundle's editor extension point.
	 */
	public static final String ID = "org.eclipse.ice.reflectivity.ui.ReflectivityFormEditor";

	private ReflectivityPage reflectPage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.ICEFormEditor#addPages()
	 */
	@Override
	protected void addPages() {

		// Loop over the DataComponents and get them into the map. This is the
		// same process as for the regular ICEFormEditor
		for (Component i : iceDataForm.getComponents()) {
			System.out.println("ICEFormEditor Message: Adding component "
					+ i.getName() + " " + i.getId());
			i.accept(this);
		}

		// Get the components out if they were all properly set.
		if (!(componentMap.get("data").isEmpty())
				|| !(componentMap.get("list").isEmpty())
				|| !(componentMap.get("output").isEmpty())) {

			// Retrieve the data component
			DataComponent dataComp = (DataComponent) componentMap.get("data")
					.get(0);

			// Retrieve the list component
			ListComponent listComp = (ListComponent) componentMap.get("list")
					.get(0);

			// Retrieve the resource component
			ResourceComponent resComp = (ResourceComponent) componentMap
					.get("output").get(0);

			// Create the reflectivity page. Use all of the components for the
			// Id.
			ReflectivityPage page = new ReflectivityPage(this,
					dataComp.getName() + listComp.getName() + resComp.getName(),
					"Reflectivity Page");

			// Set the resource component page for the resource view to know
			// where to open the resources (the VizResources)
			super.resourceComponentPage = page;

			// Add the viz service and the components to the reflectivity page.
			page.setVizService(this.getVizServiceFactory());
			page.setResourceComponent(resComp);
			page.setDataComponent(dataComp);
			page.setList(listComp);

			// Finally, try adding the page to the editor.
			try {
				addPage(page);
			} catch (PartInitException e) {
				// Catch the stack trace
				e.printStackTrace();
			}

			// Finally set the global variable to reference later
			reflectPage = page;
		}
	}

	/**
	 * Gets the specific adapter needed for the class given
	 */

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class) {
			return reflectPage.getAdapter(adapter);
		}
		return super.getAdapter(adapter);
	}
}
