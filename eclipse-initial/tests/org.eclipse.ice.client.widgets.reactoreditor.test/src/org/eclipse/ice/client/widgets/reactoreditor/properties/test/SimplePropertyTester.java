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
package org.eclipse.ice.client.widgets.reactoreditor.properties.test;

import static org.junit.Assert.assertEquals;
import org.eclipse.ice.client.widgets.reactoreditor.properties.SimpleProperty;

import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.junit.Test;

/**
 * This class tests the SimpleProperty class.
 * 
 * @author djg
 * 
 */
public class SimplePropertyTester {

	/**
	 * Test construction. Since the SimpleProperty's getters are based on the
	 * values specified in the constructor, they are also tested in this case.
	 */
	@Test
	public void checkConstruction() {

		SimpleProperty property;
		PropertyDescriptor descriptor;

		// Valid values.
		String id = "Where's";
		String displayName = "my";
		String category = "burrito?";
		Object value = 1;

		// Default String value.
		String emptyString = "";

		// Check valid construction.
		property = new SimpleProperty(id, displayName, category, value);
		assertEquals(id, property.getId());
		assertEquals(value, property.getValue());
		descriptor = property.getPropertyDescriptor();
		assertEquals(id, descriptor.getId());
		assertEquals(displayName, descriptor.getDisplayName());
		assertEquals(category, descriptor.getCategory());

		// Check all parameters null.
		property = new SimpleProperty(null, null, null, null);
		assertEquals(emptyString, property.getId());
		assertEquals(null, property.getValue());
		descriptor = property.getPropertyDescriptor();
		assertEquals(emptyString, descriptor.getId());
		assertEquals(emptyString, descriptor.getDisplayName());
		assertEquals(emptyString, descriptor.getCategory());

		// ID null, should return an empty String.
		property = new SimpleProperty(null, displayName, category, value);
		assertEquals(emptyString, property.getId());
		assertEquals(value, property.getValue());
		descriptor = property.getPropertyDescriptor();
		assertEquals(emptyString, descriptor.getId());
		assertEquals(displayName, descriptor.getDisplayName());
		assertEquals(category, descriptor.getCategory());

		// Display name null, should return an empty String.
		property = new SimpleProperty(id, null, category, value);
		assertEquals(id, property.getId());
		assertEquals(value, property.getValue());
		descriptor = property.getPropertyDescriptor();
		assertEquals(id, descriptor.getId());
		assertEquals(emptyString, descriptor.getDisplayName());
		assertEquals(category, descriptor.getCategory());

		// Category null, should return an empty String.
		property = new SimpleProperty(id, displayName, null, value);
		assertEquals(id, property.getId());
		assertEquals(value, property.getValue());
		descriptor = property.getPropertyDescriptor();
		assertEquals(id, descriptor.getId());
		assertEquals(displayName, descriptor.getDisplayName());
		assertEquals(emptyString, descriptor.getCategory());

		// Value null.
		property = new SimpleProperty(id, displayName, category, null);
		assertEquals(id, property.getId());
		assertEquals(null, property.getValue());
		descriptor = property.getPropertyDescriptor();
		assertEquals(id, descriptor.getId());
		assertEquals(displayName, descriptor.getDisplayName());
		assertEquals(category, descriptor.getCategory());

		return;
	}
}
