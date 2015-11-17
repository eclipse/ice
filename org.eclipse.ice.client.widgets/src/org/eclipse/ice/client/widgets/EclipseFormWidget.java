/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class implements the IFormWidget interface to display a Form using
 * elements of SWT/JFace and the Eclipse Rich Client Platform. It is a wrapper
 * and delegates almost all work, including drawing and event processing, to the
 * ICEFormEditor. It wraps the Form from ICE in an instance of ICEFormInput to
 * conform to Eclipse's Editor interface.
 * </p>
 *
 * @author Jay Jay Billings
 */
public class EclipseFormWidget implements IFormWidget {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected final Logger logger;

	/**
	 * Handle for storing the input to the form.
	 */
	protected ICEFormInput ICEFormInput;

	/**
	 * <p>
	 * The Form that should be displayed by the FormWidget.
	 * </p>
	 */
	protected Form widgetForm;

	/**
	 * <p>
	 * The list of IUpdateEventListeners that have subscribed to this Widget.
	 * </p>
	 */
	protected ArrayList<IUpdateEventListener> updateListeners;

	/**
	 * <p>
	 * The list of IProcessEventListeners that have subscribed to this Widget.
	 * </p>
	 */
	protected ArrayList<IProcessEventListener> processListeners;

	/**
	 * <p>
	 * The instance of ICEFormEditor that is created and wrapped by this class.
	 * </p>
	 *
	 */
	protected ICEFormEditor ICEFormEditor;

	/**
	 * <p>
	 * The Constructor.
	 * </p>
	 */
	public EclipseFormWidget() {
		// Create the logger
		logger = LoggerFactory.getLogger(EclipseFormWidget.class);
	}

	/**
	 * <p>
	 * This is an alternative constructor that allows the ICEFormEditor to be
	 * injected. It is primarily used for testing but may have other uses.
	 * </p>
	 *
	 * @param editor
	 *            <p>
	 *            The instance of ICEFormEditor that should be used by the
	 *            Widget.
	 *            </p>
	 */
	public EclipseFormWidget(ICEFormEditor editor) {
		this();
		ICEFormEditor = editor;

		// Get the EditorInput object, more than likely this is 
		// an ICEFormInput
		IEditorInput input = ICEFormEditor.getEditorInput();

		// Get the Form if we can.
		if (input instanceof ICEFormInput) {
			ICEFormInput formInput = (ICEFormInput) input;
			widgetForm = formInput.getForm();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#
	 * registerUpdateListener(org.eclipse.ice.iclient.uiwidgets.
	 * IUpdateEventListener)
	 */
	@Override
	public void registerUpdateListener(IUpdateEventListener listener) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerUpdateListener(listener);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#
	 * registerProcessListener(org.eclipse.ice.iclient.uiwidgets.
	 * IProcessEventListener)
	 */
	@Override
	public void registerProcessListener(IProcessEventListener listener) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerProcessListener(listener);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#
	 * registerResourceProvider(org.eclipse.ice.iclient.uiwidgets.
	 * ISimpleResourceProvider)
	 */
	@Override
	public void registerResourceProvider(ISimpleResourceProvider provider) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerResourceProvider(provider);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.iclient.uiwidgets.IObservableWidget#notifyUpdateListeners
	 * ()
	 */
	@Override
	public void notifyUpdateListeners() {

		// Delegate to the ICEFormEditor
		ICEFormEditor.notifyUpdateListeners();

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#
	 * notifyProcessListeners(java.lang.String)
	 */
	@Override
	public void notifyProcessListeners(String process) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.notifyProcessListeners(process);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.iclient.uiwidgets.IObservableWidget#notifyCancelListeners
	 * (java.lang.String)
	 */
	@Override
	public void notifyCancelListeners(String process) {

		ICEFormEditor.notifyCancelListeners(process);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.iclient.uiwidgets.IFormWidget#setForm(org.eclipse.ice.
	 * datastructures.form.Form)
	 */
	@Override
	public void setForm(Form form) {
		widgetForm = form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.iclient.uiwidgets.IFormWidget#getForm()
	 */
	@Override
	public Form getForm() {
		return widgetForm;
	}

	/**
	 * This operation instructs the UI system to construct an editor so that the
	 * Form can be edited by users. It creates an ICEFormEditor within Eclipse.
	 */
	@Override
	public void display() {

		// Local Declarations
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		// If the Form has been set, load the widget
		if (widgetForm != null) {

			// Setup the Input
			ICEFormInput = new ICEFormInput(widgetForm);

			// Open the page
			try {
				IEditorPart formEditor = page.openEditor(ICEFormInput, org.eclipse.ice.client.widgets.ICEFormEditor.ID);
				// Set this editor reference so that listeners can be registered
				// later.
				ICEFormEditor = (ICEFormEditor) formEditor;
			} catch (PartInitException e) {
				// Dump the stacktrace if something happens.
				logger.error(getClass().getName() + " Exception!", e);
			}

		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.iclient.uiwidgets.IFormWidget#updateStatus(java.lang.
	 * String)
	 */
	@Override
	public void updateStatus(String statusMessage) {

		// Forward the updated status message
		ICEFormEditor.updateStatus(statusMessage);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.iclient.uiwidgets.IFormWidget#disable(boolean)
	 */
	@Override
	public void disable(boolean state) {

		// Forward the request
		ICEFormEditor.disable(state);

	}

}