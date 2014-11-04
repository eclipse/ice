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
package org.eclipse.ice.client.test;

import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;

/** 
 * <!-- begin-UML-doc -->
 * <p>The FakeStreamingTextWidget is a realization of IStreamingTextWidget that is used for testing. It provides the widgetDisplayed() operation in addition to the IStreamingTextWidget interface for inspection.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeStreamingTextWidget implements IStreamingTextWidget {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Boolean to store the display state.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean displayed = false;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The last message pushed to the widget.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String errorMsg = null;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>True if text was pushed to this widget, false otherwise.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean textPushed = false;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>True if the label was set, false otherwise.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean labelSet = false;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns true if the display operation is called for the FakeStreamingTextWidget.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if the widget was displayed, false if not.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean widgetDisplayed() {
		// begin-user-code
		return displayed;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation implements display() as a simple pass through that makes whether or not the method was called. Nothing is drawn on the screen.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display() {
		// begin-user-code

		displayed = true;

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>True if text was pushed to this widget through the IStreamingTextWidget interface, false if not.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if text was pushed, false if not.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean textPushed() {
		// begin-user-code
		return textPushed;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>True if the label was set, false otherwise.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if the label was set, false otherwise.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean labelSet() {
		// begin-user-code
		return labelSet;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IStreamingTextWidget#setLabel(String label)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLabel(String label) {
		// begin-user-code

		System.out.println("FakeStreamingWidget Message: " + label);
		labelSet = true;

		return;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IStreamingTextWidget#postText(String sText)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void postText(String sText) {
		// begin-user-code

		// Write to stdout
		System.out.println("FakeStreamingWidget Message: " + sText);
		// Hoist the colors
		textPushed = true;

		// end-user-code
	}
}