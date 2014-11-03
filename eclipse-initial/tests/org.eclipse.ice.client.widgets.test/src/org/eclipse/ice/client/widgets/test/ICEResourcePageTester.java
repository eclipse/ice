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

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.datastructures.form.ResourceComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the ICEResourcePage.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEResourcePageTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ICEResourcePage that will be tested.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICEResourcePage page;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ResourceComponent accessor operations on
	 * ICEResourcePage
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkResourceComponents() {
		// begin-user-code

		// Local Declarations
		ResourceComponent comp = new ResourceComponent(), retComp = null;

		// Create the ICEResourcePage
		page = new ICEResourcePage(new ICEFormEditor(), "1", "2");

		// Set some information on the Component and add it to the page
		comp.setId(1992);
		comp.setName("First year of Eugenics War");
		comp.setDescription("The Eugenics War started in 1992 and ran until "
				+ "1996 on Earth. It resulted in the deaths of 36 million "
				+ "people.");
		page.setResourceComponent(comp);

		// Retrieve the ResourceComponent and make sure it is the same one
		retComp = page.getResourceComponent();
		assertEquals(retComp.getId(), comp.getId());
		assertEquals(retComp.getName(), comp.getName());
		assertEquals(retComp.getDescription(), retComp.getDescription());

		return;

		// end-user-code
	}
}