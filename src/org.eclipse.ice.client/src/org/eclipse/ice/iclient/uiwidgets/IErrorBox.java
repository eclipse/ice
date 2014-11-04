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

/** 
 * <!-- begin-UML-doc -->
 * <p>The IErrorBox interface describes the operations that ICE expects from a Widget that can report errors.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IErrorBox {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation sets the error string that will be displayed by the ErrorBoxWidget.</p>
	 * <!-- end-UML-doc -->
	 * @param error <p>A string containing the error message that should be displayed.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setErrorString(String error);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation retrieves the error string that is currently stored in the ErrorBoxWidget.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The error string.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getErrorString();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the IErrorBox to display its message. It must be implemented by subclasses that code to a specific UI API (SWT, Swing).</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display();
}