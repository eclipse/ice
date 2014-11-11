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
package org.eclipse.ice.iclient.uiwidgets;

import org.eclipse.ice.datastructures.form.Form;

/** 
 * <!-- begin-UML-doc -->
 * <p>The IFormWidget interface describes the operations that ICE expects from a Widget that can display Forms.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IFormWidget extends IObservableWidget {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation sets the Form that should be displayed by the Widget.</p>
	 * <!-- end-UML-doc -->
	 * @param form <p>The Form that should be used by the Widget.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setForm(Form form);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation retrieves the Form from the IFormWidget and should be used whenever an update is dispatched from the Widget to a Listener.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The Form from the Widget.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getForm();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the IFormWidget to display its Form. It must be implemented by subclasses that code to a specific UI API (SWT, Swing).</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation posts a status message to the IFormWidget that should be displayed to the user or system viewing the widget. It is a simple string.</p>
	 * <!-- end-UML-doc -->
	 * @param statusMessage <p>The status message.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void updateStatus(String statusMessage);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation disables the Form widget. Disabled FormWidgets will not make it possible for clients to process the Form. Any buttons or facilities that enable this should be disabled.</p>
	 * <!-- end-UML-doc -->
	 * @param state <p>True if the widget is disabled, false if not.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disable(boolean state);
}