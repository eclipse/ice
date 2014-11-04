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
package org.eclipse.ice.caebat.batml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.caebat.batml.BatMLModel;
import org.eclipse.ice.caebat.batml.BatMLModelBuilder;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.item.ItemType;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the CaebatModelBuilder.
 * </p>
 * <!-- end-UML-doc -->
 */
public class BatMLModelBuilderTester {
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
		BatMLModelBuilder batMLBuilder = new BatMLModelBuilder();

		// Check the Item name
		assertEquals("BatML Model", batMLBuilder.getItemName());

		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the BatMLModelBuilder.build(). This should check
	 * to see if a form is created, but it should not check the contents of that
	 * form. That is checked in a lower level tester.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkBuild() {

		// begin-user-code

		// Local declarations
		BatMLModelBuilder batMLBuilder = new BatMLModelBuilder();
		BatMLModel batMLModel = null;

		// Builder can be passed a null project.
		batMLModel = (BatMLModel) batMLBuilder.build(null);

		// Check that the item builder name is set
		assertEquals(batMLBuilder.getItemName(),
				batMLModel.getItemBuilderName());

		// Just make sure the Form is created. Its contents will be checked later.
		assertNotNull(batMLModel.getForm());
		
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the CaebatModelBuilder.getItemType().
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkGetItemType() {

		// begin-user-code

		// Local declarations
		BatMLModelBuilder batMLBuilder = new BatMLModelBuilder();

		// Check the Item name
		assertEquals(ItemType.Model, batMLBuilder.getItemType());

		// end-user-code

	}
}
