/*******************************************************************************
* Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.test;

import org.eclipse.ice.iclient.uiwidgets.IErrorBox;

/** 
 * <!-- begin-UML-doc -->
 * <p>The FakeErrorBoxWidget is a realization of IErrorBox that is used for testing. It provides several methods in addition to the IErrorBox interface that are used for testing and inspection.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeErrorBoxWidget implements IErrorBox {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Boolean to signify if a listener was registered.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean observed;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Boolean to store the display state.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean displayed;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The error message.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String errorMsg = null;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns true if the display operation is called for the FakeErrorBoxWidget.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if the widget was displayed, false if not.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean widgetDisplayed() {
		// begin-user-code
		return this.displayed;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation implements display() from UIWidget with a simple pass through that makes whether or not the method was called. Nothing is drawn on the screen.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display() {
		// begin-user-code

		this.displayed = true;

		return;

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IErrorBox#setErrorString(String error)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setErrorString(String error) {
		// begin-user-code

		// Set the error message
		errorMsg = error;

		return;

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IErrorBox#getErrorString()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getErrorString() {
		// begin-user-code

		// Return the error message
		return errorMsg;

		// end-user-code
	}

}