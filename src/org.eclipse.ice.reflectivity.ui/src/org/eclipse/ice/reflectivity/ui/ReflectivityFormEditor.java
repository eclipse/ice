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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.ICEFormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		System.out.println("Reflectivity Form Editor: Adding Custom Page");
		// Loop over the DataComponents and get them into the map
		for (Component i : iceDataForm.getComponents()) {
			System.out.println("ICEFormEditor Message: Adding component "
					+ i.getName() + " " + i.getId());
			i.accept(this);
		}

		if (!(componentMap.get("data").isEmpty())
				|| !(componentMap.get("list").isEmpty())
				|| !(componentMap.get("output").isEmpty())) {

			DataComponent dataComp = (DataComponent) componentMap.get("data").get(0);
			ListComponent listComp = (ListComponent) componentMap.get("list").get(0);
			ResourceComponent resComp = (ResourceComponent) componentMap.get("output").get(0);
			
			
			TestReflectivitySectionPage page = new TestReflectivitySectionPage(this, "ID", "Test Section Page");
			try {
				addPage(page);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			
		}

	}
}
