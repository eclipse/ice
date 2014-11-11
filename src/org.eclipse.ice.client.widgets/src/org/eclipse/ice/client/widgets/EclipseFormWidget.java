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

import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;
import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class implements the IFormWidget interface to display a Form using elements of SWT/JFace and the Eclipse Rich Client Platform. It is a wrapper and delegates almost all work, including drawing and event processing, to the ICEFormEditor. It wraps the Form from ICE in an instance of ICEFormInput to conform to Eclipse's Editor interface.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseFormWidget implements IFormWidget {

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ICEFormInput ICEFormInput;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The Form that should be displayed by the FormWidget.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Form widgetForm;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The list of IUpdateEventListeners that have subscribed to this Widget.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<IUpdateEventListener> updateListeners;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The list of IProcessEventListeners that have subscribed to this Widget.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<IProcessEventListener> processListeners;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The instance of ICEFormEditor that is created and wrapped by this class.</p>
	 * 
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ICEFormEditor ICEFormEditor;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The Constructor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public EclipseFormWidget() {
		// begin-user-code

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This is an alternative constructor that allows the ICEFormEditor to be injected. It is primarily used for testing but may have other uses.</p>
	 * <!-- end-UML-doc -->
	 * @param editor <p>The instance of ICEFormEditor that should be used by the Widget.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public EclipseFormWidget(ICEFormEditor editor) {
		// begin-user-code
		ICEFormEditor = editor;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IObservableWidget#registerUpdateListener(IUpdateEventListener listener)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerUpdateListener(IUpdateEventListener listener) {
		// begin-user-code

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerUpdateListener(listener);

		return;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IObservableWidget#registerProcessListener(IProcessEventListener listener)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerProcessListener(IProcessEventListener listener) {
		// begin-user-code

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerProcessListener(listener);

		return;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IObservableWidget#registerResourceProvider(ISimpleResourceProvider provider)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerResourceProvider(ISimpleResourceProvider provider) {
		// begin-user-code

		// Delegate to the ICEFormEditor
		ICEFormEditor.registerResourceProvider(provider);

		return;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IObservableWidget#notifyUpdateListeners()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyUpdateListeners() {
		// begin-user-code

		// Delegate to the ICEFormEditor
		ICEFormEditor.notifyUpdateListeners();

		return;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IObservableWidget#notifyProcessListeners(String process)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyProcessListeners(String process) {
		// begin-user-code

		// Delegate to the ICEFormEditor
		ICEFormEditor.notifyProcessListeners(process);

		return;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IObservableWidget#notifyCancelListeners(String process)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyCancelListeners(String process) {
		// begin-user-code

		ICEFormEditor.notifyCancelListeners(process);
		
		return;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormWidget#setForm(Form form)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setForm(Form form) {
		// begin-user-code
		widgetForm = form;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormWidget#getForm()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getForm() {
		// begin-user-code
		return widgetForm;
		// end-user-code
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
						ICEFormEditor.ID);
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

	/** 
	 * (non-Javadoc)
	 * @see IFormWidget#updateStatus(String statusMessage)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void updateStatus(String statusMessage) {
		// begin-user-code

		// Forward the updated status message
		ICEFormEditor.updateStatus(statusMessage);

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormWidget#disable(boolean state)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disable(boolean state) {
		// begin-user-code

		// Forward the request
		ICEFormEditor.disable(state);

		// end-user-code
	}

}