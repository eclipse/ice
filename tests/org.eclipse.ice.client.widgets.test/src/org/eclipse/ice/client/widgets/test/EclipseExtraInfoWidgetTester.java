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

import org.eclipse.ice.client.widgets.EclipseExtraInfoWidget;
import org.eclipse.ice.datastructures.form.Form;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the EclipseExtraInfoWidget.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseExtraInfoWidgetTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The EclipseExtraInfoWidget used in the test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EclipseExtraInfoWidget eclipseExtraInfoWidget;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Form used in the test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Form form;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is responsible for checking the Form accessors for the
	 * EclipseExtraInfoWidget class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFormAccessors() {
		// begin-user-code

		// Setup the Form
		form = new Form();
		form.setId(2);

		// Setup the widget and set the Form
		eclipseExtraInfoWidget = new EclipseExtraInfoWidget();
		eclipseExtraInfoWidget.setForm(form);

		// Check the Form
		assertEquals(2, eclipseExtraInfoWidget.getForm().getId());

		return;

		// end-user-code
	}
}