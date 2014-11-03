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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.eclipse.ice.client.widgets.EclipseErrorBoxWidget;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ErrorBoxWidgetTester is responsible for testing the ErrorBoxWidget.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseErrorBoxWidgetTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EclipseErrorBoxWidget eclipseErrorBoxWidget;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the error string getters and setters to make sure
	 * that ErrorBoxWidget can properly manage the error string.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkErrorStrings() {
		// begin-user-code

		// Local Declarations
		String testString = "You'll have to use those models.";

		// Create the Error Box
		eclipseErrorBoxWidget = new EclipseErrorBoxWidget();

		eclipseErrorBoxWidget.setErrorString(testString);
		assertEquals(eclipseErrorBoxWidget.getErrorString(), testString);

		// end-user-code
	}
}