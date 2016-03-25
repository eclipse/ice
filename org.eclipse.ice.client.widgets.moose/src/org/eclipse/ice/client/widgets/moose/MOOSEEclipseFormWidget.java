/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.ice.client.widgets.EclipseFormWidget;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates and displays a {@link MOOSEFormEditor} for modifying a
 * MOOSE model according to standard operating procedure in ICE.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class MOOSEEclipseFormWidget extends EclipseFormWidget {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MOOSEEclipseFormWidget.class);

	/**
	 * The visualization service factory provided to MOOSEFormEditors through
	 * OSGi.
	 */
	private final IVizServiceFactory vizServiceFactory;

	/**
	 * The default constructor.
	 * 
	 * @param factory
	 *            The visualization service factory provided to MOOSEFormEditors
	 *            through OSGi.
	 */
	public MOOSEEclipseFormWidget(IVizServiceFactory factory) {
		// Nothing to do yet.
		this.vizServiceFactory = factory;
	}

	/**
	 * This operation displays the {@link MOOSEFormEditor} instead of the
	 * standard ICEFormEditor.
	 */
	@Override
	public void display() {

		// Local Declarations
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		// Create the ICEFormInput for the MOOSEFormEditor.
		ICEFormInput = new ICEFormInput(widgetForm);

		try {
			// Use the workbench to open the editor with our input.
			IEditorPart formEditor = page.openEditor(ICEFormInput,
					MOOSEFormEditor.ID);
			// Set this editor reference so that listeners can be registered
			// later.
			ICEFormEditor = (ICEFormEditor) formEditor;

			// FIXME Since this is a static method, it should probably be set
			// from a more general OSGi-referencing service in the main widgets
			// bundle.
			org.eclipse.ice.client.widgets.ICEFormEditor
					.setVizServiceFactory(vizServiceFactory);

		} catch (PartInitException e) {
			// Dump the stacktrace if something happens.
			logger.error(getClass().getName() + " Exception!", e);
		}

		return;
	}
}
