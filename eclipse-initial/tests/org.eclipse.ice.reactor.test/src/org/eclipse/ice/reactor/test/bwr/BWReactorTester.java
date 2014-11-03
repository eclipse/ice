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
package org.eclipse.ice.reactor.test.bwr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A class that tests the BWReactor.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class BWReactorTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the constructor and it's default values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code
		// Local Declarations
		BWReactor reactor;
		String defaultName = "BWReactor 1";
		String defaultDescription = "BWReactor 1's Description";
		int defaultId = 1;
		int defaultSize = 1;
		HDF5LWRTagType type = HDF5LWRTagType.BWREACTOR;

		// This test is to show the default value for a reactor when it is
		// created with a negative value.
		reactor = new BWReactor(-1);
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// This test is to show the default value for a reactor when its created
		// with a zero value
		reactor = new BWReactor(0);
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// This is a test to show a valid creation of a reactor
		reactor = new BWReactor(17);
		assertEquals(17, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks equals() and hashCode() operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		// Local Declarations
		BWReactor object, equalObject, unEqualObject, transitiveObject;
		int size = 5;
		int unEqualSize = 7;

		// Setup root object
		object = new BWReactor(size);

		// Setup equalObject equal to object
		equalObject = new BWReactor(size);

		// Setup transitiveObject equal to object
		transitiveObject = new BWReactor(size);

		// Set its data, not equal to object
		unEqualObject = new BWReactor(unEqualSize);

		// Assert that these two objects are equal
		assertTrue(object.equals(equalObject));

		// Assert that two unequal objects returns false
		assertFalse(object.equals(unEqualObject));

		// Check that equals() is Reflexive
		// x.equals(x) = true
		assertTrue(object.equals(object));

		// Check that equals() is Symmetric
		// x.equals(y) = true iff y.equals(x) = true
		assertTrue(object.equals(equalObject) && equalObject.equals(object));

		// Check that equals() is Transitive
		// x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
		if (object.equals(equalObject) && equalObject.equals(transitiveObject)) {
			assertTrue(object.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(object.equals(equalObject) && object.equals(equalObject)
				&& object.equals(equalObject));
		assertTrue(!object.equals(unEqualObject)
				&& !object.equals(unEqualObject)
				&& !object.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(object==null);

		// Assert that two equal objects have the same hashcode
		assertTrue(object.equals(equalObject)
				&& object.hashCode() == equalObject.hashCode());

		// Assert that hashcode is consistent
		assertTrue(object.hashCode() == object.hashCode());

		// Assert that hashcodes are different for unequal objects
		assertFalse(object.hashCode() == unEqualObject.hashCode());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the copy and clone routines.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Local declarations
		BWReactor object;
		BWReactor copyObject = new BWReactor(0), clonedObject;

		int size = 5;

		// Setup root object
		object = new BWReactor(size);

		// Run the copy routine
		copyObject = new BWReactor(0);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (BWReactor) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

		// end-user-code
	}
}