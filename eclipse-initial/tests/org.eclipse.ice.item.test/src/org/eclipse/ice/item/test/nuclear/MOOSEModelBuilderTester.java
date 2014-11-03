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
package org.eclipse.ice.item.test.nuclear;

import static org.junit.Assert.*;

import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.ice.item.nuclear.MOOSEModelBuilder;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the MOOSEModelBuilder.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MOOSEModelBuilderTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation insures that the builder can properly construct the
	 * MOOSEModel.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkItemBuild() {
		// begin-user-code

		MOOSEModelBuilder builder = new MOOSEModelBuilder();
		assertEquals("MOOSE Model Builder", MOOSEModelBuilder.name);
		assertEquals("MOOSE Model Builder", builder.getItemName());
		assertEquals(ItemType.Model, MOOSEModelBuilder.type);
		assertEquals(ItemType.Model, builder.getItemType());
		Item model = builder.build(null);
		assertTrue(model instanceof MOOSEModel);

		return;
		// end-user-code
	}
}