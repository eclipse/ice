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
package org.eclipse.ice.client.test;

import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.eclipse.ice.datastructures.resource.ICEResource;

/** 
 * <!-- begin-UML-doc -->
 * <p>The FakeTextEditor is a realization of ITextEditor that is used for testing. It provides several methods in addition to the ITextEditor interface that are used for testing and introspection.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeTextEditor implements ITextEditor {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Boolean to store the display state.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean displayed;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The ICEResource that is ostensibly managed by the FakeTextEditor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICEResource iceResource;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns true if the display operation is called for the FakeTextEditor.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean widgetDisplayed() {
		// begin-user-code
		return displayed;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ITextEditor#setResource(ICEResource resource)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setResource(ICEResource resource) {
		// begin-user-code

		iceResource = resource;

		return;

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ITextEditor#getResource()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEResource getResource() {
		// begin-user-code
		return iceResource;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ITextEditor#display()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display() {
		// begin-user-code

		// Display if and only if the ICEResource is not equal to null
		if (iceResource != null) {
			displayed = true;
		}

		// end-user-code
	}
}