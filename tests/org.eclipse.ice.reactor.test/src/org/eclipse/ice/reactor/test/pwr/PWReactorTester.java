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
package org.eclipse.ice.reactor.test.pwr;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.eclipse.ice.datastructures.ICEObject.Component;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the PWReactor class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */

public class PWReactorTester {
	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {
		// begin-user-code

		// end-user-code
	}

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
		PressurizedWaterReactor reactor;
		PressurizedWaterReactor defaultReactor;
		int defaultSize = 17; // Default size when an erroneous value is set on
								// the reactor
		String defaultName = "PWReactor 1";
		String defaultDescription = "PWReactor 1's Description";
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.PWREACTOR;

		// This test is to show the default value for a reactor when it is
		// created with a negative value.
		defaultReactor = new PressurizedWaterReactor(-1);
		assertEquals(defaultSize, defaultReactor.getSize());
		assertEquals(defaultName, defaultReactor.getName());
		assertEquals(defaultDescription, defaultReactor.getDescription());
		assertEquals(defaultId, defaultReactor.getId());
		assertEquals(type, defaultReactor.getHDF5LWRTag());

		// This test is to show the default value for a reactor when its created
		// with a zero value
		defaultReactor = new PressurizedWaterReactor(0);
		assertEquals(defaultSize, defaultReactor.getSize());
		assertEquals(defaultName, defaultReactor.getName());
		assertEquals(defaultDescription, defaultReactor.getDescription());
		assertEquals(defaultId, defaultReactor.getId());
		assertEquals(type, defaultReactor.getHDF5LWRTag());

		// This is a test to show a valid creation of a reactor
		reactor = new PressurizedWaterReactor(17);
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
	 * This operation checks the operations for the GridLabelProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLabels() {
		// begin-user-code
		// Local Declarations
		PressurizedWaterReactor reactor = null;
		ArrayList<String> rowLabels = new ArrayList<String>();
		ArrayList<String> colLabels = new ArrayList<String>();
		GridLabelProvider provider;
		int reactorSize = 5;

		// Set the rowLabels
		rowLabels.add("1");
		rowLabels.add("2");
		rowLabels.add("3");
		rowLabels.add("4");
		rowLabels.add("5");

		// Set the colLabels
		colLabels.add("A");
		colLabels.add("B");
		colLabels.add("C");
		colLabels.add("D");
		colLabels.add("E");

		// Make a new reactor
		reactor = new PressurizedWaterReactor(reactorSize);
		// Check to see the default's gridLabelprovider
		assertEquals(-1, reactor.getGridLabelProvider().getColumnFromLabel("A"));
		assertEquals(reactorSize, reactor.getGridLabelProvider().getSize()); // Size
																				// the
																				// same
																				// as
																				// reactor.
																				// Very
																				// important!

		// Check default values for gridlabelprovider
		provider = new GridLabelProvider(reactorSize);
		// Adding the column and row labels
		provider.setColumnLabels(colLabels);
		provider.setRowLabels(rowLabels);
		reactor.setGridLabelProvider(provider);

		// Check the row and column labels
		assertTrue(this.doLabelsMatchLabelsInReactor(true, rowLabels, reactor));
		assertTrue(this.doLabelsMatchLabelsInReactor(false, colLabels, reactor));

		// You can not set it to null or illegal size
		reactor.setGridLabelProvider(null);

		// Check the row and column labels
		assertTrue(this.doLabelsMatchLabelsInReactor(true, rowLabels, reactor));
		assertTrue(this.doLabelsMatchLabelsInReactor(false, colLabels, reactor));

		reactor.setGridLabelProvider(new GridLabelProvider(reactorSize + 22));

		// Stays the same as before
		// Check the row and column labels
		assertTrue(this.doLabelsMatchLabelsInReactor(true, rowLabels, reactor));
		assertTrue(this.doLabelsMatchLabelsInReactor(false, colLabels, reactor));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter, setter, and adding of FuelAssemblies to
	 * the PWReactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFuelAssembly() {
		// begin-user-code

		// Local Declarations
		int reactorSize = 17;
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(
				reactorSize);
		FuelAssembly testComponent = new FuelAssembly(5), testComponent2 = new FuelAssembly(
				5), testComponent3 = new FuelAssembly(5);
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;

		// Check the getter and setter for the pitch

		// Check default value of Pitch
		assertEquals(0.0, reactor.getFuelAssemblyPitch(), 0.0);

		// Set the pitch to 0 and check setting - VALID
		reactor.setFuelAssemblyPitch(0.0);

		assertEquals(0.0, reactor.getFuelAssemblyPitch(), 0.0);

		// Try to set to positive double - VALID
		reactor.setFuelAssemblyPitch(100.01);
		assertEquals(100.01, reactor.getFuelAssemblyPitch(), 0.0);

		// Try to set to negative double - NOT VALID
		reactor.setFuelAssemblyPitch(-1.0);
		assertEquals(100.01, reactor.getFuelAssemblyPitch(), 0.0); // Stays the
																	// same as
																	// the
																	// previous
																	// value

		// Check the default values of the Component under test
		reactor = new PressurizedWaterReactor(reactorSize);

		// No assemblies should be added by default. Therefore every
		// location is bad
		for (int i = 0; i < reactorSize; i++) {
			for (int j = 0; j < reactorSize; j++) {
				assertNull(reactor.getAssemblyByLocation(AssemblyType.Fuel, i,
						j));
			}
		}

		// Check the names, should be empty!
		assertEquals(0, reactor.getAssemblyNames(AssemblyType.Fuel).size());

		// Try to get by name - valid string, empty string, and null
		assertNull(reactor.getAssemblyByName(AssemblyType.Fuel,
				"validNameThatDoesNotExistInThere152423"));
		assertNull(reactor.getAssemblyByName(AssemblyType.Fuel, ""));
		assertNull(reactor.getAssemblyByName(AssemblyType.Fuel, null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the reactor
		reactor.addAssembly(AssemblyType.Fuel, testComponent);

		// See that no location is set
		assertNull(reactor.getAssemblyByLocation(AssemblyType.Fuel, rowLoc1,
				colLoc1));
		// Check locations to be within bounds
		assertNull(reactor.getAssemblyByLocation(AssemblyType.Fuel, -1,
				reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.Fuel, 1,
				reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.Fuel,
				reactorSize + 25, reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.Fuel,
				reactorSize - 1, reactorSize + 25));

		// Set the valid location:
		assertTrue(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponentName, rowLoc1, colLoc1));

		// Try to break location setter
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel, null,
				rowLoc1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponentName, -1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponentName, rowLoc1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel, null, -1,
				colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel, null,
				rowLoc1, -1));
		assertFalse(reactor
				.setAssemblyLocation(AssemblyType.Fuel, null, -1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponentName, rowLoc1, reactorSize + 25));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponentName, reactorSize + 25, colLoc1));

		// The above erroneous settings does not change the original location of
		// the first, valid set
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.Fuel, testComponentName)));

		// Check invalid overwrite of location:
		testComponent2.setName(testComponentName2);

		// Add reactor, overwrite the previous testComponent's location
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Check that it is the first, but not second
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.Fuel, testComponentName)));

		// Add it in there
		assertTrue(reactor.addAssembly(AssemblyType.Fuel, testComponent2));

		// Show that you can have at least 2 components in there
		assertTrue(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponent2.getName(), rowLoc2, colLoc2));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.Fuel, testComponentName)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByName(
				AssemblyType.Fuel, testComponentName2)));

		// Check the locations
		assertTrue(testComponent.equals(reactor.getAssemblyByLocation(
				AssemblyType.Fuel, rowLoc1, colLoc1)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByLocation(
				AssemblyType.Fuel, rowLoc2, colLoc2)));

		// Check the names, should contain 2!
		assertEquals(2, reactor.getAssemblyNames(AssemblyType.Fuel).size());
		assertEquals(testComponentName,
				reactor.getAssemblyNames(AssemblyType.Fuel).get(0));
		assertEquals(testComponentName2,
				reactor.getAssemblyNames(AssemblyType.Fuel).get(1));

		// Check operation for null
		reactor.addAssembly(AssemblyType.Fuel, null);
		assertNull(reactor.getAssemblyByName(AssemblyType.Fuel, null)); // Make
																		// sure
																		// null
																		// does
		// not work!

		// Finally, demonstrate what happens when a component of the same name
		// is added, it should not overwrite the previous item in the table!
		testComponent3.setName(testComponent.getName()); // Same name as the
															// other
		// component
		testComponent3.setId(testComponentId); // Id should differ from
												// testComponent!
		assertFalse(testComponent.getId() == testComponentId);

		// Overwrite in table
		assertFalse(reactor.addAssembly(AssemblyType.Fuel, testComponent3));

		// Check that the object has not been overwritten
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.Fuel, testComponentName)));
		assertFalse(testComponent3.equals(reactor.getAssemblyByName(
				AssemblyType.Fuel, testComponentName)));

		// Test to remove components from the reactor
		assertFalse(reactor.removeAssembly(AssemblyType.Fuel, null));
		assertFalse(reactor.removeAssembly(AssemblyType.Fuel, ""));
		assertFalse(reactor.removeAssembly(AssemblyType.Fuel,
				"!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"));

		// Remove the first component
		assertTrue(reactor.removeAssembly(AssemblyType.Fuel,
				testComponent.getName()));

		// Check that it does not exist in the location or getting the name
		assertNull(reactor.getAssemblyByLocation(AssemblyType.Fuel, rowLoc1,
				colLoc1));
		assertNull(reactor.getAssemblyByName(AssemblyType.Fuel,
				testComponent.getName()));
		// Check size
		assertEquals(1, reactor.getNumberOfAssemblies(AssemblyType.Fuel));

		// It can now be overridden!
		assertTrue(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Show that the component's names can NOT overwrite each others
		// locations
		assertTrue(reactor.addAssembly(AssemblyType.Fuel, testComponent));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.Fuel,
				testComponent.getName(), rowLoc1, colLoc1));

		// Check the size, the respective locations
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.Fuel, rowLoc1,
						colLoc1).getName());
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.Fuel, rowLoc2,
						colLoc2).getName());
		assertEquals(2, reactor.getNumberOfAssemblies(AssemblyType.Fuel));
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter, setter, and adding of ControlBanks to
	 * the PWReactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkControlBank() {
		// begin-user-code
		// Local Declarations
		int reactorSize = 17;
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(
				reactorSize);
		ControlBank testComponent = new ControlBank(), testComponent2 = new ControlBank(), testComponent3 = new ControlBank();
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;

		// Check the default values of the Component under test
		reactor = new PressurizedWaterReactor(reactorSize);

		// No assemblies should be added by default. Therefore every
		// location is bad
		for (int i = 0; i < reactorSize; i++) {
			for (int j = 0; j < reactorSize; j++) {
				assertNull(reactor.getAssemblyByLocation(
						AssemblyType.ControlBank, i, j));
			}
		}

		// Check the names, should be empty!
		assertEquals(0, reactor.getAssemblyNames(AssemblyType.ControlBank)
				.size());

		// Try to get by name - valid string, empty string, and null
		assertNull(reactor.getAssemblyByName(AssemblyType.ControlBank,
				"validNameThatDoesNotExistInThere152423"));
		assertNull(reactor.getAssemblyByName(AssemblyType.ControlBank, ""));
		assertNull(reactor.getAssemblyByName(AssemblyType.ControlBank, null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the reactor
		reactor.addAssembly(AssemblyType.ControlBank, testComponent);

		// See that no location is set
		assertNull(reactor.getAssemblyByLocation(AssemblyType.ControlBank,
				rowLoc1, colLoc1));
		// Check locations to be within bounds
		assertNull(reactor.getAssemblyByLocation(AssemblyType.ControlBank, -1,
				reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.ControlBank, 1,
				reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.ControlBank,
				reactorSize + 25, reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.ControlBank,
				reactorSize - 1, reactorSize + 25));

		// Set the valid location:
		assertTrue(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponentName, rowLoc1, colLoc1));

		// Try to break location setter
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank, null,
				rowLoc1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponentName, -1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponentName, rowLoc1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank, null,
				-1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank, null,
				rowLoc1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank, null,
				-1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponentName, rowLoc1, reactorSize + 25));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponentName, reactorSize + 25, colLoc1));

		// The above erroneous settings does not change the original location of
		// the first, valid set
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.ControlBank, testComponentName)));

		// Check invalid overwrite of location:
		testComponent2.setName(testComponentName2);

		// Add reactor, overwrite the previous testComponent's location
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Check that it is the first, but not second
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.ControlBank, testComponentName)));

		// Add it in there
		assertTrue(reactor
				.addAssembly(AssemblyType.ControlBank, testComponent2));

		// Show that you can have at least 2 components in there
		assertTrue(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponent2.getName(), rowLoc2, colLoc2));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.ControlBank, testComponentName)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByName(
				AssemblyType.ControlBank, testComponentName2)));

		// Check the locations
		assertTrue(testComponent.equals(reactor.getAssemblyByLocation(
				AssemblyType.ControlBank, rowLoc1, colLoc1)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByLocation(
				AssemblyType.ControlBank, rowLoc2, colLoc2)));

		// Check the names, should contain 2!
		assertEquals(2, reactor.getAssemblyNames(AssemblyType.ControlBank)
				.size());
		assertEquals(testComponentName,
				reactor.getAssemblyNames(AssemblyType.ControlBank).get(0));
		assertEquals(testComponentName2,
				reactor.getAssemblyNames(AssemblyType.ControlBank).get(1));

		// Check operation for null
		reactor.addAssembly(AssemblyType.ControlBank, null);
		assertNull(reactor.getAssemblyByName(AssemblyType.ControlBank, null)); // Make
																				// sure
																				// null
																				// does
		// not work!

		// Finally, demonstrate what happens when a component of the same name
		// is added, it should not overwrite the previous item in the table!
		testComponent3.setName(testComponent.getName()); // Same name as the
															// other
		// component
		testComponent3.setId(testComponentId); // Id should differ from
												// testComponent!
		assertFalse(testComponent.getId() == testComponentId);

		// Overwrite in table
		assertFalse(reactor.addAssembly(AssemblyType.ControlBank,
				testComponent3));

		// Check that the object has not been overwritten
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.ControlBank, testComponentName)));
		assertFalse(testComponent3.equals(reactor.getAssemblyByName(
				AssemblyType.ControlBank, testComponentName)));

		// Test to remove components from the reactor
		assertFalse(reactor.removeAssembly(AssemblyType.ControlBank, null));
		assertFalse(reactor.removeAssembly(AssemblyType.ControlBank, ""));
		assertFalse(reactor.removeAssembly(AssemblyType.ControlBank,
				"!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"));

		// Remove the first component
		assertTrue(reactor.removeAssembly(AssemblyType.ControlBank,
				testComponent.getName()));

		// Check that it does not exist in the location or getting the name
		assertNull(reactor.getAssemblyByLocation(AssemblyType.ControlBank,
				rowLoc1, colLoc1));
		assertNull(reactor.getAssemblyByName(AssemblyType.ControlBank,
				testComponent.getName()));
		// Check size
		assertEquals(1, reactor.getNumberOfAssemblies(AssemblyType.ControlBank));

		// It can now be overridden!
		assertTrue(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Show that the component's names can NOT overwrite each others
		// locations
		assertTrue(reactor.addAssembly(AssemblyType.ControlBank, testComponent));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.ControlBank,
				testComponent.getName(), rowLoc1, colLoc1));

		// Check the size, the respective locations
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.ControlBank,
						rowLoc1, colLoc1).getName());
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.ControlBank,
						rowLoc2, colLoc2).getName());
		assertEquals(2, reactor.getNumberOfAssemblies(AssemblyType.ControlBank));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter, setter, and adding ofInCoreInstruments
	 * to the PWReactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkInCoreInstrument() {
		// begin-user-code
		// Local Declarations
		int reactorSize = 17;
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(
				reactorSize);
		IncoreInstrument testComponent = new IncoreInstrument(), testComponent2 = new IncoreInstrument(), testComponent3 = new IncoreInstrument();
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;

		// Check the default values of the Component under test
		reactor = new PressurizedWaterReactor(reactorSize);

		// No assemblies should be added by default. Therefore every
		// location is bad
		for (int i = 0; i < reactorSize; i++) {
			for (int j = 0; j < reactorSize; j++) {
				assertNull(reactor.getAssemblyByLocation(
						AssemblyType.IncoreInstrument, i, j));
			}
		}

		// Check the names, should be empty!
		assertEquals(0, reactor.getAssemblyNames(AssemblyType.IncoreInstrument)
				.size());

		// Try to get by name - valid string, empty string, and null
		assertNull(reactor.getAssemblyByName(AssemblyType.IncoreInstrument,
				"validNameThatDoesNotExistInThere152423"));
		assertNull(reactor.getAssemblyByName(AssemblyType.IncoreInstrument, ""));
		assertNull(reactor.getAssemblyByName(AssemblyType.IncoreInstrument,
				null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the reactor
		reactor.addAssembly(AssemblyType.IncoreInstrument, testComponent);

		// See that no location is set
		assertNull(reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
				rowLoc1, colLoc1));
		// Check locations to be within bounds
		assertNull(reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
				-1, reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
				1, reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
				reactorSize + 25, reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
				reactorSize - 1, reactorSize + 25));

		// Set the valid location:
		assertTrue(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponentName, rowLoc1, colLoc1));

		// Try to break location setter
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				null, rowLoc1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponentName, -1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponentName, rowLoc1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				null, -1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				null, rowLoc1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				null, -1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponentName, rowLoc1, reactorSize + 25));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponentName, reactorSize + 25, colLoc1));

		// The above erroneous settings does not change the original location of
		// the first, valid set
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.IncoreInstrument, testComponentName)));

		// Check invalid overwrite of location:
		testComponent2.setName(testComponentName2);

		// Add reactor, overwrite the previous testComponent's location
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Check that it is the first, but not second
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.IncoreInstrument, testComponentName)));

		// Add it in there
		assertTrue(reactor.addAssembly(AssemblyType.IncoreInstrument,
				testComponent2));

		// Show that you can have at least 2 components in there
		assertTrue(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponent2.getName(), rowLoc2, colLoc2));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.IncoreInstrument, testComponentName)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByName(
				AssemblyType.IncoreInstrument, testComponentName2)));

		// Check the locations
		assertTrue(testComponent.equals(reactor.getAssemblyByLocation(
				AssemblyType.IncoreInstrument, rowLoc1, colLoc1)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByLocation(
				AssemblyType.IncoreInstrument, rowLoc2, colLoc2)));

		// Check the names, should contain 2!
		assertEquals(2, reactor.getAssemblyNames(AssemblyType.IncoreInstrument)
				.size());
		assertEquals(testComponentName,
				reactor.getAssemblyNames(AssemblyType.IncoreInstrument).get(0));
		assertEquals(testComponentName2,
				reactor.getAssemblyNames(AssemblyType.IncoreInstrument).get(1));

		// Check operation for null
		reactor.addAssembly(AssemblyType.IncoreInstrument, null);
		assertNull(reactor.getAssemblyByName(AssemblyType.IncoreInstrument,
				null)); // Make sure null does
		// not work!

		// Finally, demonstrate what happens when a component of the same name
		// is added, it should not overwrite the previous item in the table!
		testComponent3.setName(testComponent.getName()); // Same name as the
															// other
		// component
		testComponent3.setId(testComponentId); // Id should differ from
												// testComponent!
		assertFalse(testComponent.getId() == testComponentId);

		// Overwrite in table
		assertFalse(reactor.addAssembly(AssemblyType.IncoreInstrument,
				testComponent3));

		// Check that the object has not been overwritten
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.IncoreInstrument, testComponentName)));
		assertFalse(testComponent3.equals(reactor.getAssemblyByName(
				AssemblyType.IncoreInstrument, testComponentName)));

		// Test to remove components from the reactor
		assertFalse(reactor.removeAssembly(AssemblyType.IncoreInstrument, null));
		assertFalse(reactor.removeAssembly(AssemblyType.IncoreInstrument, ""));
		assertFalse(reactor.removeAssembly(AssemblyType.IncoreInstrument,
				"!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"));

		// Remove the first component
		assertTrue(reactor.removeAssembly(AssemblyType.IncoreInstrument,
				testComponent.getName()));

		// Check that it does not exist in the location or getting the name
		assertNull(reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
				rowLoc1, colLoc1));
		assertNull(reactor.getAssemblyByName(AssemblyType.IncoreInstrument,
				testComponent.getName()));
		// Check size
		assertEquals(1,
				reactor.getNumberOfAssemblies(AssemblyType.IncoreInstrument));

		// It can now be overridden!
		assertTrue(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Show that the component's names can NOT overwrite each others
		// locations
		assertTrue(reactor.addAssembly(AssemblyType.IncoreInstrument,
				testComponent));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				testComponent.getName(), rowLoc1, colLoc1));

		// Check the size, the respective locations
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
						rowLoc1, colLoc1).getName());
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.IncoreInstrument,
						rowLoc2, colLoc2).getName());
		assertEquals(2,
				reactor.getNumberOfAssemblies(AssemblyType.IncoreInstrument));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter, setter, and adding of
	 * RodClusterAssemblies to the PWReactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkRodClusterAssembly() {
		// begin-user-code
		// Local Declarations
		int reactorSize = 17;
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(
				reactorSize);
		RodClusterAssembly testComponent = new RodClusterAssembly(10), testComponent2 = new RodClusterAssembly(
				10), testComponent3 = new RodClusterAssembly(10);
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;

		// Check the default values of the Component under test
		reactor = new PressurizedWaterReactor(reactorSize);

		// No assemblies should be accessible or added to the list because every
		// location is bad
		for (int i = 0; i < reactorSize; i++) {
			for (int j = 0; j < reactorSize; j++) {
				assertNull(reactor.getAssemblyByLocation(
						AssemblyType.RodCluster, i, j));
			}
		}

		// Check the names, should be empty!
		assertEquals(0, reactor.getAssemblyNames(AssemblyType.RodCluster)
				.size());

		// Try to get by name - valid string, empty string, and null
		assertNull(reactor.getAssemblyByName(AssemblyType.RodCluster,
				"validNameThatDoesNotExistInThere152423"));
		assertNull(reactor.getAssemblyByName(AssemblyType.RodCluster, ""));
		assertNull(reactor.getAssemblyByName(AssemblyType.RodCluster, null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the reactor
		reactor.addAssembly(AssemblyType.RodCluster, testComponent);

		// See that no location is set
		assertNull(reactor.getAssemblyByLocation(AssemblyType.RodCluster,
				rowLoc1, colLoc1));
		// Check locations to be within bounds
		assertNull(reactor.getAssemblyByLocation(AssemblyType.RodCluster, -1,
				reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.RodCluster, 1,
				reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.RodCluster,
				reactorSize + 25, reactorSize - 1));
		assertNull(reactor.getAssemblyByLocation(AssemblyType.RodCluster,
				reactorSize - 1, reactorSize + 25));

		// Set the valid location:
		assertTrue(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponentName, rowLoc1, colLoc1));

		// Try to break location setter
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster, null,
				rowLoc1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponentName, -1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponentName, rowLoc1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster, null,
				-1, colLoc1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster, null,
				rowLoc1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster, null,
				-1, -1));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponentName, rowLoc1, reactorSize + 25));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponentName, reactorSize + 25, colLoc1));

		// The above erroneous settings does not change the original location of
		// the first, valid set
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.RodCluster, testComponentName)));

		// Check invalid overwrite of location:
		testComponent2.setName(testComponentName2);

		// Add reactor, overwrite the previous testComponent's location
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Check that it is the first, but not second
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.RodCluster, testComponentName)));

		// Add it in there
		assertTrue(reactor.addAssembly(AssemblyType.RodCluster, testComponent2));

		// Show that you can have at least 2 components in there
		assertTrue(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponent2.getName(), rowLoc2, colLoc2));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.RodCluster, testComponentName)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByName(
				AssemblyType.RodCluster, testComponentName2)));

		// Check the locations
		assertTrue(testComponent.equals(reactor.getAssemblyByLocation(
				AssemblyType.RodCluster, rowLoc1, colLoc1)));
		assertTrue(testComponent2.equals(reactor.getAssemblyByLocation(
				AssemblyType.RodCluster, rowLoc2, colLoc2)));

		// Check the names, should contain 2!
		assertEquals(2, reactor.getAssemblyNames(AssemblyType.RodCluster)
				.size());
		assertEquals(testComponentName,
				reactor.getAssemblyNames(AssemblyType.RodCluster).get(0));
		assertEquals(testComponentName2,
				reactor.getAssemblyNames(AssemblyType.RodCluster).get(1));

		// Check operation for null
		reactor.addAssembly(AssemblyType.RodCluster, null);
		assertNull(reactor.getAssemblyByName(AssemblyType.RodCluster, null)); // Make
																				// sure
																				// null
																				// does
		// not work!

		// Finally, demonstrate what happens when a component of the same name
		// is added, it should not overwrite the previous item in the table!
		testComponent3.setName(testComponent.getName()); // Same name as the
															// other
		// component
		testComponent3.setId(testComponentId); // Id should differ from
												// testComponent!
		assertFalse(testComponent.getId() == testComponentId);

		// Overwrite in table
		assertFalse(reactor
				.addAssembly(AssemblyType.RodCluster, testComponent3));

		// Check that the object has not been overwritten
		assertTrue(testComponent.equals(reactor.getAssemblyByName(
				AssemblyType.RodCluster, testComponentName)));
		assertFalse(testComponent3.equals(reactor.getAssemblyByName(
				AssemblyType.RodCluster, testComponentName)));

		// Test to remove components from the reactor
		assertFalse(reactor.removeAssembly(AssemblyType.RodCluster, null));
		assertFalse(reactor.removeAssembly(AssemblyType.RodCluster, ""));
		assertFalse(reactor.removeAssembly(AssemblyType.RodCluster,
				"!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"));

		// Remove the first component
		assertTrue(reactor.removeAssembly(AssemblyType.RodCluster,
				testComponent.getName()));

		// Check that it does not exist in the location or getting the name
		assertNull(reactor.getAssemblyByLocation(AssemblyType.RodCluster,
				rowLoc1, colLoc1));
		assertNull(reactor.getAssemblyByName(AssemblyType.RodCluster,
				testComponent.getName()));
		// Check size
		assertEquals(1, reactor.getNumberOfAssemblies(AssemblyType.RodCluster));

		// It can now be overridden!
		assertTrue(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponent2.getName(), rowLoc1, colLoc1));

		// Show that the component's names can NOT overwrite each others
		// locations
		assertTrue(reactor.addAssembly(AssemblyType.RodCluster, testComponent));
		assertFalse(reactor.setAssemblyLocation(AssemblyType.RodCluster,
				testComponent.getName(), rowLoc1, colLoc1));

		// Check the size, the respective locations
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.RodCluster, rowLoc1,
						colLoc1).getName());
		assertEquals(
				testComponent2.getName(),
				reactor.getAssemblyByLocation(AssemblyType.RodCluster, rowLoc2,
						colLoc2).getName());
		assertEquals(2, reactor.getNumberOfAssemblies(AssemblyType.RodCluster));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation demonstrates the behaviors listed with the overridden
	 * composite implementations from LWRComposite.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCompositeImplementations() {
		// begin-user-code
		// Local Declarations
		int reactorSize = 17;
		PressurizedWaterReactor reactor;
		ArrayList<String> compNames = new ArrayList<String>();
		ArrayList<Component> components = new ArrayList<Component>();
		int numberOfDefaultComponents = 0;

		// Defaults for FuelAssembly
		LWRComposite fuelComposite;
		String compName = "Fuel Assemblies";
		String compDescription = "A Composite that contains many FuelAssembly Components.";
		int compId = 2;

		// Setup component for comparison
		fuelComposite = new LWRComposite();
		fuelComposite.setName(compName);
		fuelComposite.setId(compId);
		fuelComposite.setDescription(compDescription);
		// Add to arraylist
		compNames.add(fuelComposite.getName());
		components.add((Component) fuelComposite);

		// Defaults for ControlBank
		LWRComposite controlComposite;
		compName = "Control Banks";
		compDescription = "A Composite that contains many ControlBank Components.";
		compId = 1;

		// Setup component for comparison
		controlComposite = new LWRComposite();
		controlComposite.setName(compName);
		controlComposite.setId(compId);
		controlComposite.setDescription(compDescription);
		// Add to arraylist
		compNames.add(controlComposite.getName());
		components.add((Component) controlComposite);

		// Defaults for RCA
		LWRComposite rodComposite;
		compName = "Rod Cluster Assemblies";
		compDescription = "A Composite that contains many RodClusterAssembly Components.";
		compId = 4;

		// Setup component for comparison
		rodComposite = new LWRComposite();
		rodComposite.setName(compName);
		rodComposite.setId(compId);
		rodComposite.setDescription(compDescription);
		// Add to arraylist
		compNames.add(rodComposite.getName());
		components.add((Component) rodComposite);

		// Defaults for IncoreInstruments
		LWRComposite coreComposite;
		compName = "Incore Instruments";
		compDescription = "A Composite that contains many IncoreInstrument Components.";
		compId = 3;

		// Setup component for comparison
		coreComposite = new LWRComposite();
		coreComposite.setName(compName);
		coreComposite.setId(compId);
		coreComposite.setDescription(compDescription);
		// Add to arraylist
		compNames.add(coreComposite.getName());
		components.add((Component) coreComposite);

		// Setup the default number of components
		numberOfDefaultComponents = components.size();

		// Check the default Composite size and attributes on PWRAssembly
		reactor = new PressurizedWaterReactor(reactorSize);

		// Has a size of numberOfDefaultComponents
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// It is equal to the default rodComposite for many of the composite
		// getters
		assertTrue(rodComposite.equals(reactor.getComponent(4)));
		assertTrue(rodComposite.equals(reactor.getComponent(rodComposite
				.getName())));

		// It is equal to the default coreComposite for many of the composite
		// getters
		assertTrue(coreComposite.equals(reactor.getComponent(3)));
		assertTrue(coreComposite.equals(reactor.getComponent(coreComposite
				.getName())));

		// It is equal to the default fuelComposite for many of the composite
		// getters
		assertTrue(fuelComposite.equals(reactor.getComponent(2)));
		assertTrue(fuelComposite.equals(reactor.getComponent(fuelComposite
				.getName())));

		// It is equal to the default controlComposite for many of the composite
		// getters
		assertTrue(controlComposite.equals(reactor.getComponent(1)));
		assertTrue(controlComposite.equals(reactor
				.getComponent(controlComposite.getName())));

		// Check general getters for the other pieces
		assertTrue(compNames.size() == reactor.getComponentNames().size());
		assertTrue(compNames.containsAll(reactor.getComponentNames()));

		// These operations will show that these will not work for this class

		// Check addComponent
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());
		reactor.addComponent(new LWRComposite());
		// No size change!
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// Check removeComponent - id
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());
		reactor.removeComponent(1);
		// No size change!
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// Check remove component - name
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// No size change!
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This is a utility operation that checks the operations for the labels on
	 * the reactor to see if they are equal to the passed labels. Keep in mind
	 * that the first parameter, rows, checks to see if its comparing rows
	 * (true) or columns (false). Returns true if the labels are equal, false
	 * otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rows
	 *            <p>
	 *            True if comparing rows. False if comparing columns.
	 *            </p>
	 * @param labels
	 *            <p>
	 *            The labels to be compared to the rows or columns.
	 *            </p>
	 * @param reactor
	 *            <p>
	 *            The reactor to be compared with.
	 *            </p>
	 * @return <p>
	 *         True if they are equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean doLabelsMatchLabelsInReactor(boolean rows,
			ArrayList<String> labels, PressurizedWaterReactor reactor) {
		// begin-user-code
		int colCount = 0, rowCount = 0;

		if (rows == false) {
			// Check you can get the column labels
			for (int i = 0; i < labels.size(); i++) {
				// Assert that the column labels are in order and are set
				// correctly to the 0 indexed value of size
				assertEquals(labels.get(i), reactor.getGridLabelProvider()
						.getLabelFromColumn(i));
				assertEquals(i, reactor.getGridLabelProvider()
						.getColumnFromLabel(labels.get(i)));
				colCount++;
			}

			// Assert that the size of the columns equals the size of the
			// reactor
			assertEquals(reactor.getSize(), colCount);
		}

		else {

			// Check you can get the rowlabels
			for (int i = 0; i < labels.size(); i++) {
				// Assert that the row labels are in order and are set correctly
				// to the 0 indexed value of size
				assertEquals(labels.get(i), reactor.getGridLabelProvider()
						.getLabelFromRow(i));
				assertEquals(
						i,
						reactor.getGridLabelProvider().getRowFromLabel(
								labels.get(i)));
				rowCount++;
			}

			// Assert that the size of the rows equals the size of the reactor
			assertEquals(reactor.getSize(), rowCount);
		}

		return true;

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
		PressurizedWaterReactor object, equalObject, unEqualObject, transitiveObject;
		int size = 5;

		// Setup Values
		FuelAssembly assembly = new FuelAssembly("FUELS!", size);
		ControlBank bank = new ControlBank("BANKS!", 2, 5);
		RodClusterAssembly rca = new RodClusterAssembly("RODS!", size);
		IncoreInstrument instrument = new IncoreInstrument("Instruments!",
				new Ring());

		// Setup root object
		object = new PressurizedWaterReactor(size);
		object.addAssembly(AssemblyType.ControlBank, bank);
		object.addAssembly(AssemblyType.Fuel, assembly);
		object.addAssembly(AssemblyType.IncoreInstrument, instrument);
		object.addAssembly(AssemblyType.RodCluster, rca);
		object.setAssemblyLocation(AssemblyType.ControlBank, bank.getName(), 0,
				0);
		object.setAssemblyLocation(AssemblyType.Fuel, assembly.getName(), 0, 0);
		object.setAssemblyLocation(AssemblyType.RodCluster, rca.getName(), 0, 0);
		object.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), 0, 0);

		// Setup equalObject equal to object
		equalObject = new PressurizedWaterReactor(size);
		equalObject.addAssembly(AssemblyType.ControlBank, bank);
		equalObject.addAssembly(AssemblyType.Fuel, assembly);
		equalObject.addAssembly(AssemblyType.IncoreInstrument, instrument);
		equalObject.addAssembly(AssemblyType.RodCluster, rca);
		equalObject.setAssemblyLocation(AssemblyType.ControlBank,
				bank.getName(), 0, 0);
		equalObject.setAssemblyLocation(AssemblyType.Fuel, assembly.getName(),
				0, 0);
		equalObject.setAssemblyLocation(AssemblyType.RodCluster, rca.getName(),
				0, 0);
		equalObject.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), 0, 0);

		// Setup transitiveObject equal to object
		transitiveObject = new PressurizedWaterReactor(size);
		transitiveObject.addAssembly(AssemblyType.ControlBank, bank);
		transitiveObject.addAssembly(AssemblyType.Fuel, assembly);
		transitiveObject.addAssembly(AssemblyType.IncoreInstrument, instrument);
		transitiveObject.addAssembly(AssemblyType.RodCluster, rca);
		transitiveObject.setAssemblyLocation(AssemblyType.ControlBank,
				bank.getName(), 0, 0);
		transitiveObject.setAssemblyLocation(AssemblyType.Fuel,
				assembly.getName(), 0, 0);
		transitiveObject.setAssemblyLocation(AssemblyType.RodCluster,
				rca.getName(), 0, 0);
		transitiveObject.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), 0, 0);

		// Set its data, not equal to object
		unEqualObject = new PressurizedWaterReactor(size);
		unEqualObject.addAssembly(AssemblyType.ControlBank, bank);
		unEqualObject.addAssembly(AssemblyType.Fuel, assembly);
		unEqualObject.addAssembly(AssemblyType.IncoreInstrument, instrument);
		unEqualObject.addAssembly(AssemblyType.RodCluster, rca);
		unEqualObject.setAssemblyLocation(AssemblyType.ControlBank,
				bank.getName(), 0, 0);
		unEqualObject.setAssemblyLocation(AssemblyType.Fuel,
				assembly.getName(), 0, 0);
		unEqualObject.setAssemblyLocation(AssemblyType.RodCluster,
				rca.getName(), 0, 0);
		unEqualObject.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), 0, 1); // Only difference here

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

		// Local Declarations
		PressurizedWaterReactor object;
		PressurizedWaterReactor copyObject = new PressurizedWaterReactor(0), clonedObject;
		int size = 5;

		// Setup Values
		FuelAssembly assembly = new FuelAssembly("FUELS!", size);
		ControlBank bank = new ControlBank("BANKS!", 2, 5);
		RodClusterAssembly rca = new RodClusterAssembly("RODS!", size);
		IncoreInstrument instrument = new IncoreInstrument("Instruments!",
				new Ring());

		// Setup root object
		object = new PressurizedWaterReactor(size);
		object.addAssembly(AssemblyType.ControlBank, bank);
		object.addAssembly(AssemblyType.Fuel, assembly);
		object.addAssembly(AssemblyType.IncoreInstrument, instrument);
		object.addAssembly(AssemblyType.RodCluster, rca);
		object.setAssemblyLocation(AssemblyType.ControlBank, bank.getName(), 0,
				0);
		object.setAssemblyLocation(AssemblyType.Fuel, assembly.getName(), 0, 0);
		object.setAssemblyLocation(AssemblyType.RodCluster, rca.getName(), 0, 0);
		object.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), 0, 0);

		// Run the copy routine
		copyObject = new PressurizedWaterReactor(0);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (PressurizedWaterReactor) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the HDF5 writing operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkHDF5Writeables() {
		// begin-user-code

		// Local Declarations
		int size = 5;
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(size);
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = reactor.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		double fuelAssemblyPitch = 4;

		// Setup Assemblies, instruments, and controlBank locations

		// Size of the assemblies
		int assemblySize = 3;

		// Locations
		GridLocation loc1 = new GridLocation(0, 0);
		GridLocation loc2 = new GridLocation(2, 0);
		GridLocation loc3 = new GridLocation(0, 3);
		GridLocation loc4 = new GridLocation(1, 1);
		GridLocation loc5 = new GridLocation(3, 1);
		GridLocation loc6 = new GridLocation(1, 3);
		GridLocation loc7 = new GridLocation(3, 0);
		GridLocation loc8 = new GridLocation(2, 3);
		GridLocation loc9 = new GridLocation(3, 3);

		// Control Banks
		ControlBank bank1 = new ControlBank("ControlBank1", 2, 4);
		ControlBank bank2 = new ControlBank("ControlBank2", 3, 6);

		// IncoreInstruments
		IncoreInstrument instruments1 = new IncoreInstrument("Instrument1",
				new Ring("I am a thimble!"));
		IncoreInstrument instruments2 = new IncoreInstrument("Instrument2",
				new Ring("I am a thimble, too!"));

		// Fuel Assembly
		FuelAssembly fuel1 = new FuelAssembly("Fuel Assembly 1", assemblySize);
		FuelAssembly fuel2 = new FuelAssembly("Fuel Assembly 2", assemblySize);

		// RodClusterAssembly
		RodClusterAssembly rod1 = new RodClusterAssembly("RCA 1", assemblySize);
		RodClusterAssembly rod2 = new RodClusterAssembly("RCA 2", assemblySize);

		// Setup Duplicate Grids
		LWRGridManager bankGridManager = new LWRGridManager(size);
		LWRGridManager coreGridManager = new LWRGridManager(size);
		LWRGridManager fuelGridManager = new LWRGridManager(size);
		LWRGridManager rodGridManager = new LWRGridManager(size);

		// Setup names
		bankGridManager.setName("Control Bank Grid");
		coreGridManager.setName("Incore Instrument Grid");
		fuelGridManager.setName("Fuel Assembly Grid");
		rodGridManager.setName("Rod Cluster Assembly Grid");

		// Add objects to the grid for later comparison
		// ControlBank
		bankGridManager.addComponent(bank1, loc1);
		bankGridManager.addComponent(bank2, loc2);
		// Incore Instruments
		coreGridManager.addComponent(instruments1, loc3);
		coreGridManager.addComponent(instruments2, loc4);
		// Fuel Assemblies
		fuelGridManager.addComponent(fuel1, loc5);
		fuelGridManager.addComponent(fuel2, loc6);
		// RodClusterAssemblies
		rodGridManager.addComponent(rod1, loc7);
		rodGridManager.addComponent(rod2, loc8);

		// Setup LWRComposite clones
		LWRComposite bankComposite = new LWRComposite();
		LWRComposite coreComposite = new LWRComposite();
		LWRComposite fuelComposite = new LWRComposite();
		LWRComposite rodComposite = new LWRComposite();

		// Setup names, descriptions, ids and add pieces
		// Control Bank
		bankComposite.setName("Control Banks");
		bankComposite
				.setDescription("A Composite that contains many ControlBank Components.");
		bankComposite.setId(1);
		bankComposite.addComponent(bank1);
		bankComposite.addComponent(bank2);
		// Incore Instrument
		coreComposite.setName("Incore Instruments");
		coreComposite
				.setDescription("A Composite that contains many IncoreInstrument Components.");
		coreComposite.setId(3);
		coreComposite.addComponent(instruments1);
		coreComposite.addComponent(instruments2);
		// Fuel Composite
		fuelComposite.setName("Fuel Assemblies");
		fuelComposite
				.setDescription("A Composite that contains many FuelAssembly Components.");
		fuelComposite.setId(2);
		fuelComposite.addComponent(fuel1);
		fuelComposite.addComponent(fuel2);
		// Rod Cluster Assemblies
		rodComposite.setName("Rod Cluster Assemblies");
		rodComposite
				.setDescription("A Composite that contains many RodClusterAssembly Components.");
		rodComposite.setId(4);
		rodComposite.addComponent(rod1);
		rodComposite.addComponent(rod2);

		// Setup Rows and Columns
		ArrayList<String> rowLabels = new ArrayList<String>();
		ArrayList<String> columnLabels = new ArrayList<String>();

		// Setup row labels
		rowLabels.add("A");
		rowLabels.add("B");
		rowLabels.add("C");
		rowLabels.add("D");
		rowLabels.add("E");

		// Setup col labels
		rowLabels.add("1");
		rowLabels.add("2");
		rowLabels.add("3");
		rowLabels.add("4");
		rowLabels.add("OVER 9000!");

		GridLabelProvider provider = new GridLabelProvider(size);
		provider.setRowLabels(rowLabels);
		provider.setColumnLabels(columnLabels);
		provider.setName("Grid Labels");

		// Setup reactor
		reactor.setName(name);
		reactor.setId(id);
		reactor.setDescription(description);
		reactor.setGridLabelProvider(provider);
		reactor.setFuelAssemblyPitch(fuelAssemblyPitch);

		// Add pieces to reactor
		// Control Bank
		reactor.addAssembly(AssemblyType.ControlBank, bank1);
		reactor.addAssembly(AssemblyType.ControlBank, bank2);
		// IncoreInstrument
		reactor.addAssembly(AssemblyType.IncoreInstrument, instruments1);
		reactor.addAssembly(AssemblyType.IncoreInstrument, instruments2);
		// Fuel Assemblies
		reactor.addAssembly(AssemblyType.Fuel, fuel1);
		reactor.addAssembly(AssemblyType.Fuel, fuel2);
		// RodClusterAssemblies
		reactor.addAssembly(AssemblyType.RodCluster, rod1);
		reactor.addAssembly(AssemblyType.RodCluster, rod2);

		// Setup Positions
		// Control Bank
		reactor.setAssemblyLocation(AssemblyType.ControlBank, bank1.getName(),
				loc1.getRow(), loc1.getColumn());
		reactor.setAssemblyLocation(AssemblyType.ControlBank, bank2.getName(),
				loc2.getRow(), loc2.getColumn());
		// IncoreInstrument
		reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instruments1.getName(), loc3.getRow(), loc3.getColumn());
		reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instruments2.getName(), loc4.getRow(), loc4.getColumn());
		// Fuel Assemblies
		reactor.setAssemblyLocation(AssemblyType.Fuel, fuel1.getName(),
				loc5.getRow(), loc5.getColumn());
		reactor.setAssemblyLocation(AssemblyType.Fuel, fuel2.getName(),
				loc6.getRow(), loc6.getColumn());
		// RodClusterAssemblies
		reactor.setAssemblyLocation(AssemblyType.RodCluster, rod1.getName(),
				loc7.getRow(), loc7.getColumn());
		reactor.setAssemblyLocation(AssemblyType.RodCluster, rod2.getName(),
				loc8.getRow(), loc8.getColumn());

		// Setup the HDF5 File
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "test.h5");
		URI uri = dataFile.toURI();
		H5File h5File = HdfFileFactory.createH5File(uri);
		try {
			h5File.open();
		} catch (Exception e1) {
			// Fail here
			e1.printStackTrace();
			fail();
		}

		// Check to see if it has any children
		assertNotNull(reactor.getWriteableChildren());
		// Check Children - use equals comparison
		// Check size
		assertEquals(9, reactor.getWriteableChildren().size());
		// Check contents
		assertTrue(rodComposite.equals(reactor.getWriteableChildren().get(0)));
		assertTrue(coreComposite.equals(reactor.getWriteableChildren().get(1)));
		assertTrue(fuelComposite.equals(reactor.getWriteableChildren().get(2)));
		assertTrue(bankComposite.equals(reactor.getWriteableChildren().get(3)));
		assertTrue(provider.equals(reactor.getWriteableChildren().get(4)));

		assertTrue(reactor.getWriteableChildren().contains(bankGridManager));
		assertTrue(reactor.getWriteableChildren().contains(fuelGridManager));
		assertTrue(reactor.getWriteableChildren().contains(coreGridManager));
		assertTrue(reactor.getWriteableChildren().contains(rodGridManager));

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(reactor.writeAttributes(h5File, h5Group));

		// Close group and then reopen
		try {
			h5File.close();
			h5File.open();
		} catch (Exception e1) {
			e1.printStackTrace();
			dataFile.delete();
			fail();
		}

		// Get the group again
		h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();

		// Check attributes
		assertEquals("/", h5Group.getName());

		try {
			// Show that there are no other groups made at this time
			assertEquals(0, h5Group.getMemberList().size());

			// Check the meta data
			assertEquals(6, h5Group.getMetadata().size());

			// Check String attribute - HDF5LWRTag
			attribute = (Attribute) h5Group.getMetadata().get(0);
			assertEquals(attribute.getName(), "HDF5LWRTag");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(tag.toString(), attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check String Attribute - description
			attribute = (Attribute) h5Group.getMetadata().get(1);
			assertEquals(attribute.getName(), "description");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(description, attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Double Attribute - fuelAssemblyPitch
			attribute = (Attribute) h5Group.getMetadata().get(2);
			assertEquals(attribute.getName(), "fuelAssemblyPitch");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(fuelAssemblyPitch,
					((double[]) attribute.getValue())[0], 1.2);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Integer Attribute - id
			attribute = (Attribute) h5Group.getMetadata().get(3);
			assertEquals(attribute.getName(), "id");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(id, ((int[]) attribute.getValue())[0]);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check String Attribute - name
			attribute = (Attribute) h5Group.getMetadata().get(4);
			assertEquals(attribute.getName(), "name");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(name, attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Integer Attribute - size
			attribute = (Attribute) h5Group.getMetadata().get(5);
			assertEquals(attribute.getName(), "size");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(size, ((int[]) attribute.getValue())[0]);
			// Reset Values
			attribute = null;
			attributeValue = null;

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(reactor.writeAttributes(null, h5Group));
		assertFalse(reactor.writeAttributes(h5File, null));

		// Check dataSet.
		assertFalse(reactor.writeDatasets(null, null));

		// Check Group Creation
		H5Group group = reactor.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(1, h5Group.getMemberList().size());
		// Check that it has the same name as the root reactor
		assertEquals(reactor.getName(), h5Group.getMemberList().get(0)
				.toString());
		// Check that the returned group is a Group but no members
		assertEquals(0, group.getMemberList().size());
		assertEquals(0, ((Group) h5Group.getMemberList().get(0))
				.getMemberList().size());

		// Close that h5 file!
		try {
			h5File.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			dataFile.delete();
			fail();
		}

		// Delete the file once you are done
		dataFile.delete();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the test.h5 file after the tests fails (to keep the workspace
	 * clean).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@AfterClass
	public static void afterClass() {
		// begin-user-code

		// Cleans up the datafile if it exists due to a failed test
		File dataFile = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "test.h5");

		// If it exists, remove it
		if (dataFile.exists()) {
			dataFile.delete();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the HDF5 readable operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkHDF5Readables() {
		// begin-user-code

		// Local Declarations
		int size = 5;
		PressurizedWaterReactor component = new PressurizedWaterReactor(size);
		PressurizedWaterReactor newComponent = new PressurizedWaterReactor(-1);
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		double fuelAssemblyPitch = 4;
		H5Group subGroup = null;
		String testFileName = "testWrite.h5";

		// Setup Assemblies, instruments, and controlBank locations

		// Size of the assemblies
		int assemblySize = 3;

		// Locations
		GridLocation loc1 = new GridLocation(0, 0);
		GridLocation loc2 = new GridLocation(2, 0);
		GridLocation loc3 = new GridLocation(0, 3);
		GridLocation loc4 = new GridLocation(1, 1);
		GridLocation loc5 = new GridLocation(3, 1);
		GridLocation loc6 = new GridLocation(1, 3);
		GridLocation loc7 = new GridLocation(3, 0);
		GridLocation loc8 = new GridLocation(2, 3);
		GridLocation loc9 = new GridLocation(3, 3);

		// Control Banks
		ControlBank bank1 = new ControlBank("ControlBank1", 2, 4);
		ControlBank bank2 = new ControlBank("ControlBank2", 3, 6);

		// IncoreInstruments
		IncoreInstrument instruments1 = new IncoreInstrument("Instrument1",
				new Ring("I am a thimble!"));
		IncoreInstrument instruments2 = new IncoreInstrument("Instrument2",
				new Ring("I am a thimble, too!"));

		// Fuel Assembly
		FuelAssembly fuel1 = new FuelAssembly("Fuel Assembly 1", assemblySize);
		FuelAssembly fuel2 = new FuelAssembly("Fuel Assembly 2", assemblySize);

		// RodClusterAssembly
		RodClusterAssembly rod1 = new RodClusterAssembly("RCA 1", assemblySize);
		RodClusterAssembly rod2 = new RodClusterAssembly("RCA 2", assemblySize);

		// Setup Duplicate Grids
		LWRGridManager bankGridManager = new LWRGridManager(size);
		LWRGridManager coreGridManager = new LWRGridManager(size);
		LWRGridManager fuelGridManager = new LWRGridManager(size);
		LWRGridManager rodGridManager = new LWRGridManager(size);

		// Setup names
		bankGridManager.setName("Control Bank Grid");
		coreGridManager.setName("Incore Instrument Grid");
		fuelGridManager.setName("Fuel Assembly Grid");
		rodGridManager.setName("Rod Cluster Assembly Grid");

		// Add objects to the grid for later comparison
		// ControlBank
		bankGridManager.addComponent(bank1, loc1);
		bankGridManager.addComponent(bank2, loc2);
		// Incore Instruments
		coreGridManager.addComponent(instruments1, loc3);
		coreGridManager.addComponent(instruments2, loc4);
		// Fuel Assemblies
		fuelGridManager.addComponent(fuel1, loc5);
		fuelGridManager.addComponent(fuel2, loc6);
		// RodClusterAssemblies
		rodGridManager.addComponent(rod1, loc7);
		rodGridManager.addComponent(rod2, loc8);

		// Setup LWRComposite clones
		LWRComposite bankComposite = new LWRComposite();
		LWRComposite coreComposite = new LWRComposite();
		LWRComposite fuelComposite = new LWRComposite();
		LWRComposite rodComposite = new LWRComposite();

		// Setup names, descriptions, ids and add pieces
		// Control Bank
		bankComposite.setName("Control Banks");
		bankComposite
				.setDescription("A Composite that contains many ControlBank Components.");
		bankComposite.setId(1);
		bankComposite.addComponent(bank1);
		bankComposite.addComponent(bank2);
		// Incore Instrument
		coreComposite.setName("Incore Instruments");
		coreComposite
				.setDescription("A Composite that contains many IncoreInstrument Components.");
		coreComposite.setId(3);
		coreComposite.addComponent(instruments1);
		coreComposite.addComponent(instruments2);
		// Fuel Composite
		fuelComposite.setName("Fuel Assemblies");
		fuelComposite
				.setDescription("A Composite that contains many FuelAssembly Components.");
		fuelComposite.setId(2);
		fuelComposite.addComponent(fuel1);
		fuelComposite.addComponent(fuel2);
		// Rod Cluster Assemblies
		rodComposite.setName("Rod Cluster Assemblies");
		rodComposite
				.setDescription("A Composite that contains many RodClusterAssembly Components.");
		rodComposite.setId(4);
		rodComposite.addComponent(rod1);
		rodComposite.addComponent(rod2);

		// Setup Rows and Columns
		ArrayList<String> rowLabels = new ArrayList<String>();
		ArrayList<String> columnLabels = new ArrayList<String>();

		// Setup row labels
		rowLabels.add("A");
		rowLabels.add("B");
		rowLabels.add("C");
		rowLabels.add("D");
		rowLabels.add("E");

		// Setup col labels
		rowLabels.add("1");
		rowLabels.add("2");
		rowLabels.add("3");
		rowLabels.add("4");
		rowLabels.add("OVER 9000!");

		GridLabelProvider provider = new GridLabelProvider(size);
		provider.setRowLabels(rowLabels);
		provider.setColumnLabels(columnLabels);
		provider.setName("Grid Labels");

		// Setup reactor
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setGridLabelProvider(provider);
		component.setFuelAssemblyPitch(fuelAssemblyPitch);

		// Add pieces to component
		// Control Bank
		component.addAssembly(AssemblyType.ControlBank, bank1);
		component.addAssembly(AssemblyType.ControlBank, bank2);
		// IncoreInstrument
		component.addAssembly(AssemblyType.IncoreInstrument, instruments1);
		component.addAssembly(AssemblyType.IncoreInstrument, instruments2);
		// Fuel Assemblies
		component.addAssembly(AssemblyType.Fuel, fuel1);
		component.addAssembly(AssemblyType.Fuel, fuel2);
		// RodClusterAssemblies
		component.addAssembly(AssemblyType.RodCluster, rod1);
		component.addAssembly(AssemblyType.RodCluster, rod2);

		// Setup Positions
		// Control Bank
		component.setAssemblyLocation(AssemblyType.ControlBank,
				bank1.getName(), loc1.getRow(), loc1.getColumn());
		component.setAssemblyLocation(AssemblyType.ControlBank,
				bank2.getName(), loc2.getRow(), loc2.getColumn());
		// IncoreInstrument
		component.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instruments1.getName(), loc3.getRow(), loc3.getColumn());
		component.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instruments2.getName(), loc4.getRow(), loc4.getColumn());
		// Fuel Assemblies
		component.setAssemblyLocation(AssemblyType.Fuel, fuel1.getName(),
				loc5.getRow(), loc5.getColumn());
		component.setAssemblyLocation(AssemblyType.Fuel, fuel2.getName(),
				loc6.getRow(), loc6.getColumn());
		// RodClusterAssemblies
		component.setAssemblyLocation(AssemblyType.RodCluster, rod1.getName(),
				loc7.getRow(), loc7.getColumn());
		component.setAssemblyLocation(AssemblyType.RodCluster, rod2.getName(),
				loc8.getRow(), loc8.getColumn());

		// Setup the HDF5 File
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ testFileName);
		URI uri = dataFile.toURI();
		H5File h5File = HdfFileFactory.createH5File(uri);
		try {
			h5File.open();
		} catch (Exception e1) {
			e1.printStackTrace();
			fail();
		}

		// Setup PWRAssembly with Data in the Group

		H5Group parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		try {
			// Setup the subGroup
			subGroup = (H5Group) h5File.createGroup(name, parentH5Group);

			// Setup the subGroup's attributes

			// Setup Tag Attribute
			HdfWriterFactory.writeStringAttribute(h5File, subGroup,
					"HDF5LWRTag", tag.toString());

			// Setup name attribute
			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "name",
					name);

			// Setup id attribute
			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup, "id", id);

			// Setup description attribute
			HdfWriterFactory.writeStringAttribute(h5File, subGroup,
					"description", description);

			// Setup size attribute
			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup, "size",
					size);

			// Setup fuelAssemblyPitch attribute
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup,
					"fuelAssemblyPitch", fuelAssemblyPitch);

			// Close group and then reopen
			h5File.close();
			h5File.open();
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Get the subGroup
			subGroup = (H5Group) parentH5Group.getMemberList().get(0);

			// Read information
			assertTrue(newComponent.readAttributes(subGroup));
			assertFalse(newComponent.readDatasets(null));
			assertTrue(newComponent.readChild(bankComposite));
			assertTrue(newComponent.readChild(fuelComposite));
			assertTrue(newComponent.readChild(rodComposite));
			assertTrue(newComponent.readChild(coreComposite));
			assertTrue(newComponent.readChild(provider));
			assertTrue(newComponent.readChild(bankGridManager));
			assertTrue(newComponent.readChild(coreGridManager));
			assertTrue(newComponent.readChild(fuelGridManager));
			assertTrue(newComponent.readChild(rodGridManager));

			// Check with setup component
			assertTrue(component.equals(newComponent));

			// Try to break the readChild operation
			assertFalse(newComponent.readChild(null));
			assertTrue(newComponent.readChild(new LWRGridManager(size)));
			assertTrue(newComponent.readChild(new LWRComposite()));

			// Check with setup component
			assertTrue(component.equals(newComponent));

			// Now, lets try to set an erroneous H5Group with missing data
			subGroup.getMetadata().remove(1);

			// Run it through
			assertFalse(newComponent.readAttributes(subGroup));
			// Check it does not change
			assertTrue(component.equals(newComponent));

			// Check for nullaries
			assertFalse(newComponent.readAttributes(null));
			// Doesn't change anything
			assertTrue(component.equals(newComponent));

			h5File.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		dataFile.delete();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that checks the data providers by location.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkDataProviderLocations() {
		// begin-user-code

		// Local Declarations

		// Create a PWReactor, add a few pieces and setup their locations.
		int size = 5;
		PressurizedWaterReactor object = new PressurizedWaterReactor(size);

		int row1 = 0, col1 = 0, row2 = 2, col2 = 2, row3 = 4, col3 = 3;

		// Setup Values
		FuelAssembly assembly = new FuelAssembly("FUELS!", size);
		ControlBank bank = new ControlBank("BANKS!", 2, 5);
		RodClusterAssembly rca = new RodClusterAssembly("RODS!", size);
		IncoreInstrument instrument = new IncoreInstrument("Instruments!",
				new Ring());

		// Setup object
		object = new PressurizedWaterReactor(size);
		object.addAssembly(AssemblyType.ControlBank, bank);
		object.addAssembly(AssemblyType.Fuel, assembly);
		object.addAssembly(AssemblyType.IncoreInstrument, instrument);
		object.addAssembly(AssemblyType.RodCluster, rca);

		// Try to get at locations that do not exist yet for empty
		assertNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		assertNull(object.getAssemblyDataProviderAtLocation(AssemblyType.Fuel,
				row1, col1));
		assertNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.RodCluster, row1, col1));
		assertNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.IncoreInstrument, row1, col1));

		// Setup locations
		object.setAssemblyLocation(AssemblyType.ControlBank, bank.getName(),
				row1, col1);
		object.setAssemblyLocation(AssemblyType.Fuel, assembly.getName(), row1,
				col1);
		object.setAssemblyLocation(AssemblyType.RodCluster, rca.getName(),
				row1, col1);
		object.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), row1, col1);

		object.setAssemblyLocation(AssemblyType.ControlBank, bank.getName(),
				row2, col2);
		object.setAssemblyLocation(AssemblyType.Fuel, assembly.getName(), row2,
				col2);
		object.setAssemblyLocation(AssemblyType.RodCluster, rca.getName(),
				row2, col2);
		object.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), row2, col2);

		object.setAssemblyLocation(AssemblyType.ControlBank, bank.getName(),
				row3, col3);
		object.setAssemblyLocation(AssemblyType.Fuel, assembly.getName(), row3,
				col3);
		object.setAssemblyLocation(AssemblyType.RodCluster, rca.getName(),
				row3, col3);
		object.setAssemblyLocation(AssemblyType.IncoreInstrument,
				instrument.getName(), row3, col3);

		// Setup some data
		LWRData data1 = new LWRData("Feature1111");
		LWRData data2 = new LWRData("Feature1111");
		LWRData data3 = new LWRData("Feature1113");
		LWRData data4 = new LWRData("Feature1114");

		// Setup some times
		double time1 = 0.0;
		double time2 = 0.1;

		// Setup duplicate LWRDataProvider
		LWRDataProvider provider = new LWRDataProvider();
		provider.addData(data1, time1);
		provider.addData(data2, time1);
		provider.addData(data3, time1);
		provider.addData(data3, time2);
		provider.addData(data4, time2);

		// Try to get data by location
		// Try to get at locations that do not exist yet for empty
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.Fuel, row1, col1));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.RodCluster, row1, col1));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.IncoreInstrument, row1, col1));

		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row2, col2));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.Fuel, row2, col2));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.RodCluster, row2, col2));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.IncoreInstrument, row2, col2));

		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row3, col3));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.Fuel, row3, col3));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.RodCluster, row3, col3));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.IncoreInstrument, row3, col3));

		// Add some data
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		object.getAssemblyDataProviderAtLocation(AssemblyType.ControlBank,
				row1, col1).addData(data1, time1);
		assertNotNull(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1));
		object.getAssemblyDataProviderAtLocation(AssemblyType.ControlBank,
				row1, col1).addData(data2, time1);

		object.getAssemblyDataProviderAtLocation(AssemblyType.ControlBank,
				row1, col1).addData(data3, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.ControlBank,
				row1, col1).addData(data3, time2);
		object.getAssemblyDataProviderAtLocation(AssemblyType.ControlBank,
				row1, col1).addData(data4, time2);

		// Add some data
		object.getAssemblyDataProviderAtLocation(AssemblyType.Fuel, row1, col1)
				.addData(data1, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.Fuel, row1, col1)
				.addData(data2, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.Fuel, row1, col1)
				.addData(data3, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.Fuel, row1, col1)
				.addData(data3, time2);
		object.getAssemblyDataProviderAtLocation(AssemblyType.Fuel, row1, col1)
				.addData(data4, time2);

		// Add some data
		object.getAssemblyDataProviderAtLocation(AssemblyType.RodCluster, row1,
				col1).addData(data1, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.RodCluster, row1,
				col1).addData(data2, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.RodCluster, row1,
				col1).addData(data3, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.RodCluster, row1,
				col1).addData(data3, time2);
		object.getAssemblyDataProviderAtLocation(AssemblyType.RodCluster, row1,
				col1).addData(data4, time2);

		// Add some data
		object.getAssemblyDataProviderAtLocation(AssemblyType.IncoreInstrument,
				row1, col1).addData(data1, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.IncoreInstrument,
				row1, col1).addData(data2, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.IncoreInstrument,
				row1, col1).addData(data3, time1);
		object.getAssemblyDataProviderAtLocation(AssemblyType.IncoreInstrument,
				row1, col1).addData(data3, time2);
		object.getAssemblyDataProviderAtLocation(AssemblyType.IncoreInstrument,
				row1, col1).addData(data4, time2);

		// Do comparisons
		assertTrue(object.getAssemblyDataProviderAtLocation(
				AssemblyType.ControlBank, row1, col1).equals(provider));
		assertTrue(object.getAssemblyDataProviderAtLocation(AssemblyType.Fuel,
				row1, col1).equals(provider));
		assertTrue(object.getAssemblyDataProviderAtLocation(
				AssemblyType.RodCluster, row1, col1).equals(provider));
		assertTrue(object.getAssemblyDataProviderAtLocation(
				AssemblyType.IncoreInstrument, row1, col1).equals(provider));

		// end-user-code
	}

}