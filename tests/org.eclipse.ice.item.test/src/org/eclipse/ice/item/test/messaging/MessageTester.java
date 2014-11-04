/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.test.messaging;

import static org.junit.Assert.assertEquals;

import org.eclipse.ice.item.messaging.Message;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the Message class to make sure the Bean
 * works correctly.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MessageTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation makes sure that the accessors of the Message class work.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAccessors() {
		// begin-user-code

		// Local Declarations
		int id = 1, itemId = 2;
		String type = "FILE_UPDATED", content = "Starfleet Academy";

		// Create a message
		Message msg = new Message();
		msg.setId(id);
		msg.setItemId(itemId);
		msg.setMessage(content);
		msg.setType(type);

		// Check it
		assertEquals(id, msg.getId());
		assertEquals(itemId, msg.getItemId());
		assertEquals(content, msg.getMessage());
		assertEquals(type, msg.getType());

		return;
		// end-user-code
	}
}