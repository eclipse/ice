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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.hdf.LWRComponentReader;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PWRAssembly;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

/**
 * This tests the HDF reading functionality that the {@link LWRComponentReader}
 * provides for <i>all</i> {@link LWRComponent} sub-classes.
 * <p>
 * It does this by using a factory to create expected components, reading the
 * components from an old-format test file, and comparing them with the
 * originals.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class LWRComponentReaderTester {

	/**
	 * The reader that is under test.
	 */
	private LWRComponentReader reader;

	/**
	 * The factory used to generate expected components.
	 */
	private LWRComponentFactory componentFactory;
	/**
	 * The factory used to read HDF files. This is only necessary because the
	 * reader operates directly on HDF groups and objects.
	 */
	private HdfIOFactory factory;

	/**
	 * The test file from which components will be read.
	 */
	private File file;
	/**
	 * The handle to the test file.
	 */
	private int fileId;
	/**
	 * The handle to the root group that contains all the components to be read.
	 * In order to use the old tools to write all the components to one file, we
	 * encapsulated them in an {@link LWRComposite}. This group corresponds to
	 * that composite in the file.
	 */
	private int rootGroup;

	/**
	 * Opens the test HDF file for reading. Creates other class variables used
	 * in each test.
	 */
	@Before
	public void beforeEachTest() {

		// Create the test files. The old file should always exist, whereas the
		// new file should not.
		String s = System.getProperty("file.separator");
		file = new File(System.getProperty("user.home") + s + "ICETests" + s
				+ "reactorData" + s + "oldFormatLWRComponents.h5");

		// Create the factory, writer, and reader.
		componentFactory = new LWRComponentFactory();
		factory = new HdfIOFactory();
		reader = new LWRComponentReader(factory);

		// Create the HDF file.
		if (file.exists()) {
			assertTrue(file.canRead());
		}
		try {
			fileId = factory.openFile(file.toURI());
			rootGroup = factory.openGroup(fileId, "/root");
		} catch (HDF5LibraryException e) {
			fail(getClass().getName() + " error: "
					+ " Could not open HDF file \"" + file.getPath() + "\".");
		}

		return;
	}

	/**
	 * Closes the test HDF file containing all components.
	 */
	@After
	public void afterEachTest() {

		// Close the HDF file.
		try {
			factory.closeGroup(rootGroup);
			factory.closeFile(fileId);
		} catch (HDF5LibraryException e) {
			fail(getClass().getName() + " error: "
					+ " Could not close HDF file \"" + file.getPath() + "\".");
		}

		return;
	}

	/**
	 * Generates, reads, and compares an LWRComponent.
	 */
	@Test
	public void checkLWRComponent() {
		LWRComponent expectedComponent = componentFactory.createLWRComponent();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares an LWRComposite.
	 */
	@Test
	public void checkLWRComposite() {
		LWRComposite expectedComponent = componentFactory.createLWRComposite();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a GridLabelProvider.
	 */
	@Test
	public void checkGridLabelProvider() {
		GridLabelProvider expectedComponent = componentFactory
				.createGridLabelProvider(10, true, true);
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares an LWRGridManager.
	 */
	@Test
	public void checkLWRGridManager() {
		LWRGridManager expectedComponent = componentFactory
				.createLWRGridManager(7);
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a Material.
	 */
	@Test
	public void checkMaterial() {
		Material expectedComponent = componentFactory.createLiquidMaterial();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a Ring.
	 */
	@Test
	public void checkRing() {
		Ring expectedComponent = componentFactory.createRing();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a MaterialBlock.
	 */
	@Test
	public void checkMaterialBlock() {
		MaterialBlock expectedComponent = componentFactory
				.createMaterialBlock();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares an LWRRod.
	 */
	@Test
	public void checkLWRRod() {
		LWRRod expectedComponent = componentFactory.createLWRRod();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a Tube.
	 */
	@Test
	public void checkTube() {
		Tube expectedComponent = componentFactory.createTube();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a PWRAssembly.
	 */
	@Test
	public void checkPWRAssembly() {
		PWRAssembly expectedComponent = componentFactory.createPWRAssembly();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a FuelAssembly.
	 */
	@Test
	public void checkFuelAssembly() {
		FuelAssembly expectedComponent = componentFactory.createFuelAssembly();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a RodClusterAssembly.
	 */
	@Test
	public void checkRodClusterAssembly() {
		RodClusterAssembly expectedComponent = componentFactory
				.createRodClusterAssembly();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares an IncoreInstrument.
	 */
	@Test
	public void checkIncoreInstrument() {
		IncoreInstrument expectedComponent = componentFactory
				.createIncoreInstrument();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a ControlBank.
	 */
	@Test
	public void checkControlBank() {
		ControlBank expectedComponent = componentFactory.createControlBank();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares an LWReactor.
	 */
	@Test
	public void checkLWReactor() {
		LWReactor expectedComponent = componentFactory.createLWReactor();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a BWReactor.
	 */
	@Test
	public void checkBWReactor() {
		BWReactor expectedComponent = componentFactory.createBWReactor();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

	/**
	 * Generates, reads, and compares a PressurizedWaterReactor.
	 */
	@Test
	public void checkPressurizedWaterReactor() {
		PressurizedWaterReactor expectedComponent = componentFactory
				.createPressurizedWaterReactor();
		LWRComponent component = null;

		// Check that the component's group exists, open it, read it into a new
		// component, and close the group.
		String name = expectedComponent.getName();
		try {
			// The component should have a group in the file.
			assertTrue(factory.hasChildGroup(rootGroup, name));
			// Open the group, read it into an LWRComponent, and close it.
			int child = factory.openGroup(rootGroup, name);
			component = reader.readComponent(child);
			factory.closeGroup(child);
		} catch (NullPointerException | HDF5Exception e) {
			fail(getClass().getName() + " error: " + "Error while reading the "
					+ expectedComponent.getClass().getName() + ".");
		}

		// Compare the expected component with the read component.
		assertEquals(expectedComponent, component);

		return;
	}

}
