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

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEGeometryPage;
import org.eclipse.ice.datastructures.form.geometry.*;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the accessors of the ICEGeometryPage.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author gqx
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEGeometryPageTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the accessors for getting and setting a geometryComponent in
	 * geometryPage.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGeometryPageAccessors() {
		// begin-user-code

		// Get two GeometryComponent to check equality
		GeometryComponent geometryComponent = new GeometryComponent();
		GeometryComponent otherGeometryComponent = new GeometryComponent();

		// Get ICEGeometryPage to check accessor
		ICEFormEditor ICEFormEditor = new ICEFormEditor();
		ICEGeometryPage GeometryPage = new ICEGeometryPage(ICEFormEditor, "id",
				"title");

		// Set, then get back another GeometryComponent
		GeometryPage.setGeometry(geometryComponent);
		otherGeometryComponent = GeometryPage.getGeometry();

		// Check the equality
		assertTrue(geometryComponent.equals(otherGeometryComponent));

		// end-user-code
	}
}