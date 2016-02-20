/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation
 *   - Kasper Gammeltoft
 *******************************************************************************/

package org.eclipse.eavp.viz.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

public class AbstractSeriesStyleTest {

	@Test
	public void testAbstractSeriesStyle() {
		// Create a new style and make sure it is correctly instantiated
		FakeSeriesStyle style = new FakeSeriesStyle();
		assertNotNull(style);
		assertNotNull(style.getAllPropertyTypes());
		assertEquals(3, style.getAllPropertyTypes().size());
	}

	@Test
	public void testHashCode() {
		// Create a new style and get its hash
		FakeSeriesStyle style = new FakeSeriesStyle();
		int hash = style.hashCode();
		// Create another identical style and make sure their hash codes are the
		// same
		FakeSeriesStyle style2 = new FakeSeriesStyle();
		assertEquals(hash, style2.hashCode());

		// Change properties and make sure that the hash is reflected properly.
		// Then change back and make sure that the hash will go back to the
		// previous value (where the two styles where equal)
		style2.setProperty(FakeSeriesStyle.NAME, "some other name");
		assertFalse(hash == style2.hashCode());
		style2.setProperty(FakeSeriesStyle.NAME,
				style.getProperty(FakeSeriesStyle.NAME));
		assertTrue(hash == style2.hashCode());

		// Do the same thing with a different object
		style2.setProperty(FakeSeriesStyle.THING, new ArrayList<Object>());
		assertFalse(hash == style2.hashCode());
		style2.setProperty(FakeSeriesStyle.THING,
				style.getProperty(FakeSeriesStyle.THING));
		assertTrue(hash == style2.hashCode());
	}

	@Ignore
	@Test
	public void testGetSetProperty() {
		// Create new style, change a property and test that it is set and
		// retrieved properly
		FakeSeriesStyle style = new FakeSeriesStyle();
		Object curThing = style.getProperty(FakeSeriesStyle.THING);
		style.setProperty(FakeSeriesStyle.THING, "Something");
		assertNotEquals(curThing, style.getProperty(FakeSeriesStyle.THING));

		// Set the name of the style to some other values, make sure the
		// properties are set properly
		String curName = (String) style.getProperty(FakeSeriesStyle.NAME);
		style.setProperty(FakeSeriesStyle.NAME, "test");
		assertNotEquals(curName, FakeSeriesStyle.NAME);
		assertEquals(style.getProperty(FakeSeriesStyle.NAME), "name");

		// Make sure a non string value will not be put into the specified
		// property
		style.setProperty(FakeSeriesStyle.NAME, new ArrayList<String>());
		assertEquals(style.getProperty(FakeSeriesStyle.NAME), "name");

	}

	@Test
	public void testGetAllPropertyTypes() {
		// Create a new style and get the property types
		FakeSeriesStyle style = new FakeSeriesStyle();
		Set<String> propTypes = style.getAllPropertyTypes();
		// Test for each of the declared property types
		assertTrue(propTypes.contains(FakeSeriesStyle.COLOR));
		assertTrue(propTypes.contains(FakeSeriesStyle.THING));
		assertTrue(propTypes.contains(FakeSeriesStyle.COLOR));
	}

	@Test
	public void testEqualsObject() {
		// Create the new styles to test
		FakeSeriesStyle thisStyle = new FakeSeriesStyle();
		FakeSeriesStyle otherStyle = new FakeSeriesStyle();
		// Make sure that identical styles are equal
		assertTrue(thisStyle.equals(otherStyle));
		// Change a property to make sure they are now not equal
		otherStyle.setProperty(FakeSeriesStyle.NAME, "something else");
		assertFalse(thisStyle.equals(otherStyle));

		// Make sure that equals handles other objects as well
		assertFalse(thisStyle.equals(new Object()));
		assertFalse(thisStyle.equals("some string"));
	}

	@Test
	public void testCopy() {
		// Create some new styles to test
		FakeSeriesStyle style = new FakeSeriesStyle();
		FakeSeriesStyle copy = new FakeSeriesStyle();
		// Change a property, make sure that the styles are not equal
		style.setProperty(FakeSeriesStyle.THING, new HashMap<String, Object>());
		assertNotEquals(style, copy);
		// Copy and prove the new style is now equal. Copying should copy over
		// all properties
		copy.copy(style);
		assertEquals(style, copy);

	}

}
