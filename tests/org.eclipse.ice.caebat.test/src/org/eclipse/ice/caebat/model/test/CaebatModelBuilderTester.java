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
package org.eclipse.ice.caebat.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ice.caebat.model.CaebatModel;
import org.eclipse.ice.caebat.model.CaebatModelBuilder;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.item.ItemType;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the CaebatModelBuilder.
 * </p>
 * <!-- end-UML-doc -->
 */
public class CaebatModelBuilderTester {
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
		CaebatModelBuilder caebatBuilder = new CaebatModelBuilder();

		// Check the Item name
		assertEquals("Caebat Model", caebatBuilder.getItemName());

		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the CaebatModelBuilder.build(). This should check
	 * to see if a form is created, but it should not check the contents of that
	 * form. That is checked in a lower level tester.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkBuild() {

		// begin-user-code

		// Local declarations
		CaebatModelBuilder caebatBuilder = new CaebatModelBuilder();
		CaebatModel caebatModel = null;

		// Builder can be passed a null project.
		caebatModel = (CaebatModel) caebatBuilder.build(null);

		// Check that the item builder name is set
		assertEquals(caebatBuilder.getItemName(),
				caebatModel.getItemBuilderName());

		// A form is created
		assertNotNull(caebatModel.getForm());

		// Make sure there is something on the form
		assertEquals(caebatModel.getForm().getComponents().size(),4);

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
		CaebatModelBuilder caebatBuilder = new CaebatModelBuilder();

		// Check the Item name
		assertEquals(ItemType.Model, caebatBuilder.getItemType());

		// end-user-code

	}
}
