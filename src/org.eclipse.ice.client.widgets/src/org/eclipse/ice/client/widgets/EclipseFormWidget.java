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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

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
		ICEFormEditor = editor;
	}

	/*
	 * Implements method from IObservableWidget.
	 */
	@Override
	public void registerUpdateListener(IUpdateEventListener listener) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerUpdateListener(listener);

		return;
	}

	/*
	 * Implements a method from IObservableWidget.
	 */
	@Override
	public void registerProcessListener(IProcessEventListener listener) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerProcessListener(listener);

		return;
	}

	/*
	 * Implements a method from IObservableWidget.
	 */
	@Override
	public void registerResourceProvider(ISimpleResourceProvider provider) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerResourceProvider(provider);

		return;
	}

	/*
	 * Implements method from IObservableWidget.
	 */
	@Override
	public void notifyUpdateListeners() {

		// Delegate to the ICEFormEditor
		ICEFormEditor.notifyUpdateListeners();

		return;
	}

	/*
	 * Implements method from IObservableWidget.
	 */
	@Override
	public void notifyProcessListeners(String process) {

		// Delegate to the ICEFormEditor
		ICEFormEditor.notifyProcessListeners(process);

		return;
	}

	/*
	 * Implements method from IObservableWidget.
	 */
	@Override
	public void notifyCancelListeners(String process) {

		ICEFormEditor.notifyCancelListeners(process);

		return;
	}

	/*
	 * Implements a method from IFormWidget.
	 */
	@Override
	public void setForm(Form form) {
		widgetForm = form;
	}

	/*
	 * Implements a method from IFormWidget.
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
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		// If the Form has been set, load the widget
		if (widgetForm != null) {

			// Setup the Input
			ICEFormInput = new ICEFormInput(widgetForm);

			// Open the page
			try {
				IEditorPart formEditor = page.openEditor(ICEFormInput,
						org.eclipse.ice.client.widgets.ICEFormEditor.ID);
				// Set this editor reference so that listeners can be registered
				// later.
				ICEFormEditor = (ICEFormEditor) formEditor;
			} catch (PartInitException e) {
				// Dump the stacktrace if something happens.
				e.printStackTrace();
			}

		}

		return;
	}

	/*
	 * Implements a method from IFormWidget.
	 */
	@Override
	public void updateStatus(String statusMessage) {

		// Forward the updated status message
		ICEFormEditor.updateStatus(statusMessage);

	}

	/*
	 * Implements a method from IFormWidget.
	 */
	@Override
	public void disable(boolean state) {

		// Forward the request
		ICEFormEditor.disable(state);

	}

}