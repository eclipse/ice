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
package org.eclipse.ice.io.hdf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.io.hdf.HdfIORegistry;
import org.eclipse.ice.io.hdf.IHdfIOFactory;
import org.eclipse.ice.io.hdf.IHdfIORegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests the {@link HdfIORegistry}'s ability to register and retrieve new
 * {@link IHdfIOFactory} instances.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class HdfIORegistryTester {

	/**
	 * Reference to the HdfIORegistry to test
	 */
	private HdfIORegistry registry;

	/**
	 * Stub classes to act as supported classes for the FakeHdfIOFactory
	 * 
	 * @author Alex McCaskey
	 * 
	 */
	private class SupportedOne {
	}

	private class SupportedTwo {
	}

	/**
	 * Fake HdfIOFactory to be used in the testing the registration
	 * functionality of the IHdfIORegistry
	 * 
	 * @author Alex McCaskey
	 * 
	 */
	private class FakeHdfIOFactory extends HdfIOFactory {

		private final Map<String, Class<?>> tagMap = new HashMap<String, Class<?>>();
		private final Map<Class<?>, String> supportedMap = new HashMap<Class<?>, String>();

		FakeHdfIOFactory() {
			tagMap.put("tag1", SupportedOne.class);
			tagMap.put("tag2", SupportedTwo.class);
			supportedMap.put(SupportedOne.class, "tag1");
			supportedMap.put(SupportedTwo.class, "tag2");
		}

		public List<Class<?>> getSupportedClasses() {
			return new ArrayList<Class<?>>(tagMap.values());
		}

		public String getTag(Class<?> supportedClass) {
			return supportedMap.get(supportedClass);
		}

	}

	/**
	 * Checks that {@link IHdfIOFactory} instances can register with the
	 * registry and be queried via
	 * {@link IHdfIORegistry#getHdfIOFactory(Object)} and
	 * {@link IHdfIORegistry#getHdfIOFactory(String)}.
	 */
	@Test
	public void checkRegistration() {

		// Create a new HdfIORegistry to test
		registry = new HdfIORegistry();

		// Create a Fake Factory to register with the
		// HdfIORegistry
		FakeHdfIOFactory factory = new FakeHdfIOFactory();

		// Register that FakeHdfIOFactory
		registry.registerHdfIOFactory(factory);

		// Test that it was registered correctly
		IHdfIOFactory hdfIOFactory = registry
				.getHdfIOFactory(new SupportedOne());
		assertNotNull(hdfIOFactory);
		assertEquals(factory, hdfIOFactory);

		hdfIOFactory = registry.getHdfIOFactory(new SupportedTwo());
		assertNotNull(hdfIOFactory);
		assertEquals(factory, hdfIOFactory);

		// Check an unsupported class and make sure it
		// returns null
		hdfIOFactory = registry.getHdfIOFactory(new Object());
		assertNull(hdfIOFactory);

		// Now make sure we can get the factory from tags
		hdfIOFactory = registry.getHdfIOFactory("tag1");
		assertNotNull(hdfIOFactory);
		assertEquals(factory, hdfIOFactory);

		hdfIOFactory = registry.getHdfIOFactory("tag2");
		assertNotNull(hdfIOFactory);
		assertEquals(factory, hdfIOFactory);

		// Test an invalid tag and make sure it returns null
		hdfIOFactory = registry.getHdfIOFactory("tag3");
		assertNull(hdfIOFactory);

	}

}
