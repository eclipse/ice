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

import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.ice.client.widgets.EclipseFormWidget;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * This class displays a {@link ReflectivityFormEditor} for editing a
 * reflectivity model using the standard operating procedure in ICE.
 * 
 * @author Kasper Gammeltoft, Jordan H. Deyton
 *
 */
public class ReflectivityEclipseFormWidget extends EclipseFormWidget {

	/**
	 * This operation displays the {@link ReflectivityFormEditor} instead of the
	 * standard ICEFormEditor.
	 */
	@Override
	public void display() {

		// Local Declarations
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		// Create the ICEFormInput for the ReflectivityFormBuilder.
		ICEFormInput = new ICEFormInput(widgetForm);

		try {
			// Use the workbench to open the editor with our input.
			IEditorPart formEditor = page.openEditor(ICEFormInput,
					ReflectivityFormEditor.ID);
			// Set this editor reference so that listeners can be registered
			// later.
			ICEFormEditor = (ICEFormEditor) formEditor;

		} catch (PartInitException e) {
			// Dump the stacktrace if something happens.
			e.printStackTrace();
		}

		return;
	}

}
