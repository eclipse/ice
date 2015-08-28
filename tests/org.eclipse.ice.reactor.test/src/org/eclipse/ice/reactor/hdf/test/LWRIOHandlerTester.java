/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.ice.reactor.hdf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.hdf.LWRIOHandler;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LWRIOHandlerTester {

	private LWRIOHandler handler;

	private File newFile;
	private File oldFile;

	private LWRComponentFactory componentFactory;

	@After
	public void afterEachTest() {
		// Delete the created file if it exists.
		if (newFile != null) {
			if (newFile.exists()) {
				newFile.delete();
			}
			newFile = null;
		}
		return;
	}

	@Before
	public void beforeEachTest() {

		// Create the test files. The old file should always exist, whereas the
		// new file should not.
		String s = System.getProperty("file.separator");
		oldFile = new File(System.getProperty("user.home") + s + "ICETests" + s
				+ "reactorData" + s + "oldFormatLWR.h5");
		newFile = new File(System.getProperty("user.home") + s + "ICETests" + s
				+ "reactorData" + s + "testLWR.h5");

		handler = new LWRIOHandler();

		componentFactory = new LWRComponentFactory();

		return;
	}

	@Test
	public void checkRead() {

		// Check that the file can be read.
		assertTrue(oldFile.exists());
		assertTrue(oldFile.canRead());
		URI uri = oldFile.toURI();

		// Create the expected reactor.
		PressurizedWaterReactor expectedReactor = componentFactory
				.createOldReactor();

		// Read the old file.
		List<LWRComponent> components = handler.readHDF5(uri);

		// The list of read components should include the expected reactor.
		assertNotNull(components);
		assertEquals(1, components.size());
		assertEquals(expectedReactor, components.get(0));

		// Check invalid input such that no components can be read from a file.

		// Try a null argument.
		components = handler.readHDF5(null);
		assertNotNull(components);
		assertTrue(components.isEmpty());

		// Try a URI for a nonexistent file.
		File file = new File(oldFile.getParent()
				+ System.getProperty("file.separator") + "doesNotExist");
		uri = file.toURI();
		components = handler.readHDF5(uri);
		assertNotNull(components);
		assertTrue(components.isEmpty());

		return;
	}

	@Test
	public void checkWrite() {

		// Ensure that the new file can be written.
		if (newFile.exists()) {
			assertTrue(newFile.canWrite());
		}
		URI uri = newFile.toURI();

		// Create some components.
		PressurizedWaterReactor reactor = componentFactory
				.createPressurizedWaterReactor();
		LWRRod rod = componentFactory.createLWRRod();

		// Set up the list of components to write.
		List<LWRComponent> components = new ArrayList<LWRComponent>();
		components.add(reactor);
		components.add(null); // This should be ignored.
		components.add(rod);

		// Write the file.
		assertEquals(2, handler.writeHDF5(uri, components));

		// Check invalid input such that no components can be written to a file.
		// One or both parameters are null...
		assertEquals(0, handler.writeHDF5(null, null));
		assertEquals(0, handler.writeHDF5(uri, null));
		assertEquals(0, handler.writeHDF5(null, components));
		// List of components is empty or has nothing but null values...
		components.clear();
		assertEquals(0, handler.writeHDF5(uri, components));
		components.add(null);
		components.add(null);
		assertEquals(0, handler.writeHDF5(uri, components));

		return;
	}

	@Test
	public void checkWriteAndRead() {
		// Ensure that the new file can be written.
		if (newFile.exists()) {
			assertTrue(newFile.canWrite());
		}
		URI uri = newFile.toURI();

		// Create some components.
		PressurizedWaterReactor oldReactor = componentFactory
				.createOldReactor();
		PressurizedWaterReactor reactor = componentFactory
				.createPressurizedWaterReactor();
		FuelAssembly assembly = componentFactory.createFuelAssembly();
		LWRRod rod = componentFactory.createLWRRod();
		MaterialBlock block = componentFactory.createMaterialBlock();
		Ring ring = componentFactory.createRing();
		Material material = componentFactory.createGasMaterial();

		// Set up the list of components to write.
		List<LWRComponent> components = new ArrayList<LWRComponent>();
		components.add(oldReactor);
		components.add(reactor);
		components.add(null); // This list item should be ignored.
		components.add(assembly);
		components.add(rod);
		components.add(block);
		components.add(ring);
		components.add(material);
		int expectedNumberOfComponents = components.size() - 1;

		// Write the file.
		assertEquals(expectedNumberOfComponents,
				handler.writeHDF5(uri, components));

		// Now try to read them.
		components = handler.readHDF5(uri);

		// Check the response.
		assertNotNull(components);
		assertEquals(expectedNumberOfComponents, components.size());
		// Equivalent components should be in the list.
		assertTrue(components.contains(material));
		assertTrue(components.contains(ring));
		assertTrue(components.contains(block));
		assertTrue(components.contains(rod));
		assertTrue(components.contains(assembly));
		assertTrue(components.contains(reactor));
		assertTrue(components.contains(oldReactor));

		return;
	}
}
