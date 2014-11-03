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
package org.eclipse.ice.iclient.uiwidgets;

/** 
 * <!-- begin-UML-doc -->
 * <p>This widget streams output text to a client. Text that is posted to this widget can not be retrieved and it is an append-only interface.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IStreamingTextWidget {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation sets the label or name of the streaming output widget that should be displayed to clients.</p>
	 * <!-- end-UML-doc -->
	 * @param label <p>The label</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLabel(String label);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation appends text to the output of the streaming text widget.</p>
	 * <!-- end-UML-doc -->
	 * @param sText <p>The text that should be displayed.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void postText(String sText);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the IStreamingTextWidget to display. It must be implemented by subclasses that code to a specific UI API (SWT, Swing).</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display();
}