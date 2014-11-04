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
package org.eclipse.ice.sassena.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.sassena.SassenaCoherentModel;
import org.eclipse.ice.sassena.SassenaCoherentModelBuilder;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the SassenaCoherentModelBuilder.
 * </p>
 * <!-- end-UML-doc -->
 */
public class SassenaCoherentModelBuilderTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the CaebatModelBuilder.getItemName().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetItemName() {

		// begin-user-code

		// Local declarations
		SassenaCoherentModelBuilder sassenaBuilder = new SassenaCoherentModelBuilder();

		// Check the Item name
		assertEquals("Sassena Coherent Model", sassenaBuilder.getItemName());

		
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the SassenaCoherentModelBuilder.build(). This should check
	 * to see if a form is created, but it should not check the contents of that
	 * form. That is checked in a lower level tester.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkBuild() {

		// begin-user-code

		// Local declarations
		SassenaCoherentModelBuilder sassenaBuilder = new SassenaCoherentModelBuilder();
		SassenaCoherentModel sassenaModel = null;

		// Builder can be passed a null project.
		sassenaModel = (SassenaCoherentModel) sassenaBuilder.build(null);

		// Check that the item builder name is set
		assertEquals(sassenaBuilder.getItemName(),
				sassenaModel.getItemBuilderName());

		// Just make sure the Form is created. Its contents will be checked later.
		assertNotNull(sassenaModel.getForm());
		
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the SassenaIncohrerentModelBuilder.getItemType().
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkGetItemType() {

		// begin-user-code

		// Local declarations
		SassenaCoherentModelBuilder sassenaBuilder = new SassenaCoherentModelBuilder();

		// Check the Item name
		assertEquals(ItemType.Model, sassenaBuilder.getItemType());

		// end-user-code

	}
}
