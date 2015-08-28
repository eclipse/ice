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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

public class LWRComponentReaderTester {

	private LWRComponentReader reader;

	private LWRComponentFactory componentFactory;
	private HdfIOFactory factory;

	private File file;
	private int fileId;
	private int rootGroup;

	/**
	 * Creates and opens a temporary HDF file for writing and reading. Creates
	 * other class variables used in each test.
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

	@Test
	public void checkRodClusterAssembly() {
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
