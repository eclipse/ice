/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.reactoreditor;

import org.eclipse.ice.client.widgets.EclipseFormWidget;
import org.eclipse.ice.client.widgets.ICEFormEditor;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * This class extends the EclipseFormWidget and opens the ReactorFormEditor
 * instead of the ICEFormEditor.
 * 
 * @author bkj
 * 
 */
public class ReactorEclipseFormWidget extends EclipseFormWidget {

	/**
	 * The current registry used to generate analysis widgets for customized
	 * analysis views.
	 */
	private final IAnalysisWidgetRegistry widgetRegistry;

	/**
	 * The current registry of ReactorFormEditors.
	 */
	private final IReactorEditorRegistry editorRegistry;

	/**
	 * The default constructor.
	 * 
	 * @param registry
	 *            The {@link IAnalysisWidgetRegistry} used to pull tailored
	 *            analysis views.
	 */
	public ReactorEclipseFormWidget(IAnalysisWidgetRegistry widgetRegistry,
			IReactorEditorRegistry editorRegistry) {

		// Set the registry.
		this.widgetRegistry = widgetRegistry;
		this.editorRegistry = editorRegistry;

		return;
	}

	/**
	 * This operation displays the ReactorFormEditor.
	 */
	@Override
	public void display() {

		// Local Declarations
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		// If the Form has been set, load the widget
		if (widgetForm != null) {

			// Setup the Input. We need to pass the registry to the
			// ReactorFormEditor through the input.
			ReactorFormInputFactory factory = new ReactorFormInputFactory();
			ICEFormInput = factory.createInput(widgetForm, widgetRegistry);

			// Open the page
			try {
				IEditorPart formEditor = page.openEditor(ICEFormInput,
						ReactorFormEditor.ID);
				// Set this editor reference so that listeners can be registered
				// later.
				ICEFormEditor = (ICEFormEditor) formEditor;

				// Add the ReactorFormEditor to the registry.
				editorRegistry.addReactorEditor((ReactorFormEditor) formEditor,
						ICEFormInput.getForm().getId());

			} catch (PartInitException e) {
				// Dump the stacktrace if something happens.
				e.printStackTrace();
			}

		}

		return;
	}

}
