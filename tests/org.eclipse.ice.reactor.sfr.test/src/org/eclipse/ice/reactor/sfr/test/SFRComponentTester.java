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
package org.eclipse.ice.reactor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.BatteryComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.test.TestComponentListener;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.base.SFRData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the operations of the SFRComponent class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SFRComponentTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the constructors and default values of the SFRComponent class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// We have two constructors to test:
		// new SFRComponent();
		// new SFRComponent(String name);

		// Component variable used for testing the constructor.
		SFRComponent component;

		/* ---- Test the nullary constructor. ---- */
		component = new SFRComponent();

		// Test all of the getters for default values.
		assertEquals("Component 1", component.getName());
		assertEquals(1, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Information", component.getSourceInfo());
		assertNotNull(component.getTimes());
		assertTrue(component.getTimes().isEmpty());
		assertEquals("seconds", component.getTimeUnits());
		assertEquals(-1, component.getTimeStep(42));
		/* --------------------------------------- */

		/* ---- Test a constructor with a String parameter. ---- */
		component = new SFRComponent("Arthur");

		// Test all of the getters for default values.
		assertEquals("Arthur", component.getName());
		assertEquals(1, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Information", component.getSourceInfo());
		assertNotNull(component.getTimes());
		assertTrue(component.getTimes().isEmpty());
		assertEquals("seconds", component.getTimeUnits());
		assertEquals(-1, component.getTimeStep(3.14));
		/* ----------------------------------------------------- */

		/* ---- Test a constructor with a bad (null) String parameter. ---- */
		component = new SFRComponent(null);

		// Test all of the getters for default values.
		assertEquals("Component 1", component.getName());
		assertEquals(1, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Information", component.getSourceInfo());
		assertNotNull(component.getTimes());
		assertTrue(component.getTimes().isEmpty());
		assertEquals("seconds", component.getTimeUnits());
		assertEquals(-1, component.getTimeStep(Double.MIN_VALUE));
		/* ---------------------------------------------------------------- */

		/* ---- Test a constructor with a bad (empty) String parameter. ---- */
		component = new SFRComponent("");

		// Test all of the getters for default values.
		assertEquals("Component 1", component.getName());
		assertEquals(1, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Information", component.getSourceInfo());
		assertNotNull(component.getTimes());
		assertTrue(component.getTimes().isEmpty());
		assertEquals("seconds", component.getTimeUnits());
		assertEquals(-1, component.getTimeStep(Double.MIN_VALUE));
		/* ----------------------------------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the getters and setters for the name, description, id, sourceInfo
	 * and timeUnits attributes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAttributes() {
		// begin-user-code

		SFRComponent component = new SFRComponent();

		// Set name, description, id, sourceInfo and timeUnit attributes
		component.setName("Zaphod");
		component.setDescription("fooDescription");
		component.setId(1);
		component.setSourceInfo("fooSource");
		component.setTimeUnits("fooSeconds");

		// Check the name, description, id, sourceInfo and timeUnit attributes
		assertEquals("Zaphod", component.getName());
		assertEquals("fooDescription", component.getDescription());
		assertEquals(1, component.getId());
		assertEquals("fooSource", component.getSourceInfo());
		assertEquals("fooSeconds", component.getTimeUnits());

		// Set attributes to illegal values
		component.setName(null);
		component.setDescription(null);
		component.setId(-1);
		component.setSourceInfo(null);
		component.setTimeUnits(null);

		// Check attributes, should remain unchanged
		assertEquals("Zaphod", component.getName());
		assertEquals("fooDescription", component.getDescription());
		assertEquals(1, component.getId());
		assertEquals("fooSource", component.getSourceInfo());
		assertEquals("fooSeconds", component.getTimeUnits());

		// Set string attributes to empty strings
		component.setName("");
		component.setDescription("	");
		component.setSourceInfo("   ");
		component.setTimeUnits("");

		// Check string attributes, should remain unchanged
		assertEquals("Zaphod", component.getName());
		assertEquals("fooDescription", component.getDescription());
		assertEquals("fooSource", component.getSourceInfo());
		assertEquals("fooSeconds", component.getTimeUnits());

		// Set string attributes with leading/trailing whitespaces
		component.setName("  Zaphod ");
		component.setDescription(" fooDescription ");
		component.setSourceInfo(" fooSource  ");
		component.setTimeUnits("  fooSeconds  ");

		// Check string attributes for trimmed leading/trailing whitespaces
		assertEquals("Zaphod", component.getName());
		assertEquals("fooDescription", component.getDescription());
		assertEquals("fooSource", component.getSourceInfo());
		assertEquals("fooSeconds", component.getTimeUnits());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Test the addition and removal of SFRData from the SFRComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkDataAddRem() {
		// begin-user-code

		// The component that we will be updating for this test.
		SFRComponent component = new SFRComponent();

		// Some pre-defined features, times, and IData objects.
		String featOne = "Two heads";
		String featTwo = "Three arms";
		String featThree = "Ex-Galactic president";
		double timeOne = 1.5;
		double timeTwo = 3.0;
		double timeThree = 4.5;
		SFRData dataOne = new SFRData(featOne);
		SFRData dataTwo = new SFRData(featOne);
		SFRData dataThree = new SFRData(featTwo);
		SFRData dataFour = new SFRData(featThree);

		// Overall data information (these change when we add/remove
		// timesteps/features).
		int numberOfTimeSteps;
		List<Double> times;
		List<String> featureList;

		// Time-/feature-specific information.
		double currentTime;
		int timeStep;
		List<String> featuresAtCurrentTime;
		List<IData> dataAtCurrentTime;
		List<IData> emptyData = new ArrayList<IData>();

		/* ---- Check the overall information. ---- */
		numberOfTimeSteps = 0;
		times = new ArrayList<Double>();
		featureList = new ArrayList<String>();

		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList, component.getFeatureList());
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		currentTime = 0.0;
		timeStep = -1;
		featuresAtCurrentTime = new ArrayList<String>();
		dataAtCurrentTime = new ArrayList<IData>();

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Add IData to the component at t=1.5
		component.addData(dataOne, timeOne);

		/* ---- Check the overall information. ---- */
		numberOfTimeSteps++;
		times.add(timeOne);
		featureList.add(featOne);

		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Update the current time.
		component.setTime(timeOne);

		/* ---- Check the overall information. ---- */
		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		currentTime = timeOne;
		timeStep = 0;
		featuresAtCurrentTime.add(featOne);
		dataAtCurrentTime = new ArrayList<IData>();
		dataAtCurrentTime.add(dataOne);

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(dataAtCurrentTime, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Add another piece of IData to the component at t=1.5
		component.addData(dataTwo, timeOne);

		/* ---- Check the overall information. ---- */
		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		dataAtCurrentTime.add(dataTwo);

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(dataAtCurrentTime, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Change current time to t=3.0
		component.setTime(timeTwo);

		/* ---- Check the overall information. ---- */
		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		currentTime = timeTwo;
		timeStep = -1;
		featuresAtCurrentTime = new ArrayList<String>();

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Add third piece of IData to component, at current time (t=3.0)
		component.addData(dataThree, timeTwo);

		/* ---- Check the overall information. ---- */
		numberOfTimeSteps++;
		times.add(timeTwo);
		featureList.add(featTwo);

		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		timeStep = 1;
		featuresAtCurrentTime.add(featTwo);
		dataAtCurrentTime = new ArrayList<IData>();
		dataAtCurrentTime.add(dataThree);

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(dataAtCurrentTime, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Add fourth piece of IData to component, at t=4.5
		component.addData(dataFour, timeThree);

		/* ---- Check the overall information. ---- */
		numberOfTimeSteps++;
		times.add(timeThree);
		featureList.add(featThree);

		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(dataAtCurrentTime, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Change current time to t=4.5
		component.setTime(timeThree);

		/* ---- Check the overall information. ---- */
		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		currentTime = timeThree;
		timeStep = 2;
		featuresAtCurrentTime = new ArrayList<String>();
		featuresAtCurrentTime.add(featThree);
		dataAtCurrentTime = new ArrayList<IData>();
		dataAtCurrentTime.add(dataFour);

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(dataAtCurrentTime,
				component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Set the time to 1.5 and add dataFour.
		component.setTime(timeOne);
		component.addData(dataFour, timeOne);

		/* ---- Check the overall information. ---- */
		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		currentTime = timeOne;
		timeStep = 0;
		featuresAtCurrentTime = new ArrayList<String>();
		featuresAtCurrentTime.add(featOne);
		featuresAtCurrentTime.add(featThree);

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime.size(), component
				.getFeaturesAtCurrentTime().size());
		assertTrue(component.getFeaturesAtCurrentTime().containsAll(
				featuresAtCurrentTime));

		// Check the data for the features at this timestep. (dataOne and
		// dataTwo for featOne, dataFour for featThree)
		dataAtCurrentTime = new ArrayList<IData>();
		dataAtCurrentTime.add(dataOne);
		dataAtCurrentTime.add(dataTwo);
		assertEquals(dataAtCurrentTime, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		dataAtCurrentTime = new ArrayList<IData>();
		dataAtCurrentTime.add(dataFour);
		assertEquals(dataAtCurrentTime,
				component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Try to set invalid time
		component.setTime(-1.0);

		// The time should not have changed.
		assertEquals(currentTime, component.getCurrentTime(), 0);

		// Remove featThree IData from component
		component.removeDataFromFeature(featThree);

		/* ---- Check the overall information. ---- */
		featureList.remove(featThree);

		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		featuresAtCurrentTime = new ArrayList<String>();
		featuresAtCurrentTime.add(featOne);

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep. (dataOne and
		// dataTwo for featOne, now empty for featThree)
		dataAtCurrentTime = new ArrayList<IData>();
		dataAtCurrentTime.add(dataOne);
		dataAtCurrentTime.add(dataTwo);
		assertEquals(dataAtCurrentTime, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));

		// Update the current time so we can check timeThree's data.
		component.setTime(timeThree);

		currentTime = timeThree;
		timeStep = 2;
		featuresAtCurrentTime = new ArrayList<String>();

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Remove featTwo IData from component
		component.removeDataFromFeature(featTwo);

		/* ---- Check the overall information. ---- */
		featureList.remove(featTwo);

		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		// Change time to t=3.0
		component.setTime(timeTwo);

		/* ---- Check the time-/feature-specific information. ---- */
		currentTime = timeTwo;
		timeStep = 1;
		featuresAtCurrentTime = new ArrayList<String>();

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		// Remove featOne IData from component
		component.removeDataFromFeature(featOne);

		/* ---- Check the overall information. ---- */
		featureList.remove(featOne);

		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		// Change time to t=1.5
		component.setTime(timeOne);

		/* ---- Check the overall information. ---- */
		assertEquals(numberOfTimeSteps, component.getNumberOfTimeSteps());
		assertEquals(times, component.getTimes());
		assertEquals(featureList.size(), component.getFeatureList().size());
		for (String feature : featureList) {
			assertTrue(component.getFeatureList().contains(feature));
		}
		/* ---------------------------------------- */

		/* ---- Check the time-/feature-specific information. ---- */
		currentTime = timeOne;
		timeStep = 0;
		featuresAtCurrentTime = new ArrayList<String>();

		assertEquals(currentTime, component.getCurrentTime(), 0);
		assertEquals(timeStep, component.getTimeStep(currentTime));
		assertEquals(featuresAtCurrentTime,
				component.getFeaturesAtCurrentTime());

		// Check the data for the features at this timestep.
		assertEquals(emptyData, component.getDataAtCurrentTime(featOne));
		assertEquals(emptyData, component.getDataAtCurrentTime(featTwo));
		assertEquals(emptyData, component.getDataAtCurrentTime(featThree));
		/* ------------------------------------------------------- */

		/* ---- Check adding data in a different order. ---- */

		// Initialize three components: one component, one which will have data
		// added in the same order, and one with data added in another order.
		component = new SFRComponent();
		SFRComponent sameOrder = new SFRComponent();
		SFRComponent diffOrder = new SFRComponent();

		// At this point, they are all the same.
		assertEquals(component, sameOrder);
		assertEquals(component, diffOrder);

		// Create some data with different features to add.
		SFRData data1 = new SFRData("feature1");
		SFRData data2 = new SFRData("feature2");
		SFRData data3 = new SFRData("feature3");
		SFRData data4 = new SFRData("feature4");

		// Add the data to the component.
		component.addData(data1, 0.0);
		component.addData(data2, 0.0);
		component.addData(data3, 0.0);
		component.addData(data4, 0.0);

		// Add the data in the same order to another component.
		sameOrder.addData(data1, 0.0);
		sameOrder.addData(data2, 0.0);
		sameOrder.addData(data3, 0.0);
		sameOrder.addData(data4, 0.0);

		// Add the data in a different order to another component.
		diffOrder.addData(data1, 0.0);
		diffOrder.addData(data4, 0.0);
		diffOrder.addData(data2, 0.0);
		diffOrder.addData(data3, 0.0);

		// Check the list of feature names.
		assertEquals(component.getFeatureList(), sameOrder.getFeatureList());
		assertEquals(component.getFeatureList(), diffOrder.getFeatureList());

		// Check the list of feature names at the current time.
		assertEquals(component.getFeaturesAtCurrentTime(),
				sameOrder.getFeaturesAtCurrentTime());
		assertEquals(component.getFeaturesAtCurrentTime(),
				diffOrder.getFeaturesAtCurrentTime());

		// Check the contents of the feature at the current time.
		for (int i = 1; i <= 4; i++) {
			String feature = "feature" + i;

			// Check the size and contents.
			assertEquals(component.getDataAtCurrentTime(feature).size(),
					sameOrder.getDataAtCurrentTime(feature).size());
			assertTrue(sameOrder.getDataAtCurrentTime(feature).containsAll(
					component.getDataAtCurrentTime(feature)));

			assertEquals(component.getDataAtCurrentTime(feature).size(),
					diffOrder.getDataAtCurrentTime(feature).size());
			assertTrue(diffOrder.getDataAtCurrentTime(feature).containsAll(
					component.getDataAtCurrentTime(feature)));
		}

		// The components should all still be equal!
		assertEquals(component, sameOrder);
		assertEquals(component, diffOrder);
		/* ------------------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the visitation of ISFRComponentVisitors in the SFRComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkVisitation() {
		// begin-user-code

		// Create a new SFRComponent to visit.
		SFRComponent component = new SFRComponent("Marvin");

		// Create a fake visitor that stores a reference to the visited
		// IReactorComponent.
		FakeComponentVisitor visitor = null;

		// Try accepting an invalid visitor.
		component.accept(visitor);

		// Initialize the visitor and get its current component (null).
		visitor = new FakeComponentVisitor();
		Object visitedObject = visitor.getComponent();

		// Make sure the component and the [un]visited object are not equal.
		assertFalse(component.equals(visitedObject));

		// Try accepting the fake component visitor.
		component.accept(visitor);
		visitedObject = visitor.getComponent();

		// See if the visited component from the visitor is the same component
		// that we initially created.
		assertTrue(component == visitedObject);
		assertTrue(component.equals(visitedObject));

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the equality operation of SFRComponents.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		// Construct a component to test against
		SFRComponent component = new SFRComponent("Earth");
		component.setDescription("Mostly Harmless");
		component.setId(10);

		// Construct a component equal to the first
		SFRComponent equalComponent = new SFRComponent("Earth");
		equalComponent.setDescription("Mostly Harmless");
		equalComponent.setId(10);

		// Construct a component not equal to the first
		SFRComponent unequalComponent = new SFRComponent("Betelgeuse 5");
		unequalComponent.setDescription("Suspiciously Shifty");
		unequalComponent.setId(5);

		// Check that component and unequalComponet are not the same
		assertFalse(component.equals(unequalComponent));
		assertFalse(unequalComponent.equals(component));

		// Check that equality also fails with illegal values
		assertFalse(component == null);
		assertFalse(component.equals(42));
		assertFalse("just a string".equals(component));

		// Check is equals() is reflexive and symmetric
		assertTrue(component.equals(component));
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Construct a component equal to the first, for testing transitivity
		SFRComponent transComponent = new SFRComponent("Earth");
		transComponent.setDescription("Mostly Harmless");
		transComponent.setId(10);

		// Check equals is transitive()
		if (component.equals(transComponent)
				&& transComponent.equals(equalComponent)) {
			assertTrue(component.equals(equalComponent));
		} else {
			fail();
		}
		// Check the hashCode values.
		assertEquals(component.hashCode(), component.hashCode());
		assertEquals(component.hashCode(), equalComponent.hashCode());
		assertFalse(component.hashCode() == unequalComponent.hashCode());

		// FIXME - We might also want to test components where the datasets
		// are/aren't different.

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the copying and cloning operation of SFRComponents.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Construct a base component to copy from
		SFRComponent component = new SFRComponent("Ford Prefect");
		component.setDescription("Out-of-work actor from Guildford");
		component.setId(30);

		/* ---- Check copying. ---- */
		// Construct an empty component to copy to
		SFRComponent componentCopy = new SFRComponent();

		// Check that component and componentCopy are not identical yet
		assertFalse(component == componentCopy);
		assertFalse(component.equals(componentCopy));

		// Copy contents over
		componentCopy.copy(component);

		// Check component and componentCopy are identical
		assertTrue(component.equals(componentCopy));

		// Try to copy contents of an invalid component
		componentCopy.copy(null);

		// Check that componentCopy remains unchanged
		assertTrue(component.equals(componentCopy));

		// Make sure they are still different references!
		assertFalse(component == componentCopy);
		/* ------------------------ */

		/* ---- Check cloning. ---- */
		// Get a clone of the original component.
		Object objectClone = component.clone();

		// Make sure it's not null!
		assertNotNull(objectClone);

		// Make sure the reference is different but the contents are equal.
		assertFalse(component == objectClone);
		assertTrue(component.equals(objectClone));
		assertFalse(componentCopy == objectClone);
		assertTrue(componentCopy.equals(objectClone));

		// Make sure the object is an instance of SFRComponent.
		assertTrue(objectClone instanceof SFRComponent);

		// Cast the component.
		SFRComponent componentClone = (SFRComponent) component.clone();

		// Check the components one last time for good measure.
		assertFalse(component == componentClone);
		assertTrue(component.equals(componentClone));
		/* ------------------------ */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the notifyListeners method of SFRComponents.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkNotifications() {
		// begin-user-code

		// Create a new component to listen to.
		SFRComponent component = new SFRComponent("Tricia McMillan");
		SFRComponent component2 = new SFRComponent();

		// All methods that should notify listeners:
		// component.addData(SFRData, double)
		// component.copy(SFRComponent)
		// component.removeDataFromFeature(String)
		// component.setDescription(String)
		// component.setId(int)
		// component.setName(String)
		// component.setSourceInfo(String)
		// component.setTime(double)
		// component.setTimeUnits(String)

		// All of these methods are tested at some point below.

		// Create a test listener and register it with the component.
		TestComponentListener listener1 = new TestComponentListener();
		component.register(listener1);

		// The setter should notify the only listener.
		component.addData(new SFRData("Heart of Gold"), 1.5);
		assertTrue(listener1.wasNotified());
		listener1.reset();

		// The setter should notify the only listener.
		component.setDescription("Arthur's love interest?");
		assertTrue(listener1.wasNotified());
		listener1.reset();

		// The setter should notify the only listener.
		component.setId(42);
		assertTrue(listener1.wasNotified());
		listener1.reset();

		// The setter should notify the only listener.
		component.setName("Trillian");
		assertTrue(listener1.wasNotified());
		listener1.reset();

		// Create a new listener and register it with the original component.
		TestComponentListener listener2 = new TestComponentListener();
		component.register(listener2);

		// The setter should notify both listeners.
		component.setSourceInfo("Earth");
		assertTrue(listener1.wasNotified());
		listener1.reset();
		assertTrue(listener2.wasNotified());
		listener2.reset();

		// The setter should notify both listeners.
		component.setTime(1.5);
		assertTrue(listener1.wasNotified());
		listener1.reset();
		assertTrue(listener2.wasNotified());
		listener2.reset();

		// The copy operation should notify both listeners.
		component2.copy(component);
		assertTrue(listener1.wasNotified());
		listener1.reset();
		assertTrue(listener2.wasNotified());
		listener2.reset();

		// Create a new listener and register it with the component copy.
		TestComponentListener listener3 = new TestComponentListener();
		component2.register(listener3);

		// The component copy should notify listeners 1, 2, and 3.
		component2.setTimeUnits("tea");
		assertTrue(listener1.wasNotified());
		listener1.reset();
		assertTrue(listener2.wasNotified());
		listener2.reset();
		assertTrue(listener3.wasNotified());
		listener3.reset();

		// Test removeDataFromFeature. The original component should only notify
		// listeners 1 and 2.
		component.removeDataFromFeature("Heart of Gold");
		assertTrue(listener1.wasNotified());
		listener1.reset();
		assertTrue(listener2.wasNotified());
		listener2.reset();

		// Listener3 is NOT supposed to be notified. However, attempts to test
		// this behavior have proved unsuccessful: the CountDownLatch.await()
		// operation in listener.wasNotified() does not always wait, so we can
		// never tell if it succeeded or if JUnit decided it should not run.
		// assertFalse(listener3.wasNotified());
		// listener3.reset();

		return;
		// end-user-code
	}

	/**
	 * This is a fake visitor class to test the accept(IComponentVisitor) method
	 * of SFRComponent.
	 * 
	 * @author djg
	 * 
	 */
	private class FakeComponentVisitor implements IComponentVisitor {
		private IReactorComponent component = null;

		protected IReactorComponent getComponent() {
			return component;
		}

		public void visit(IReactorComponent component) {
			this.component = component;
		}

		public void visit(DataComponent component) {
		}

		public void visit(ResourceComponent component) {
		}

		public void visit(TableComponent component) {
		}

		public void visit(MatrixComponent component) {
		}

		public void visit(IShape component) {
		}

		public void visit(GeometryComponent component) {
		}

		public void visit(MasterDetailsComponent component) {
		}

		public void visit(TreeComposite component) {
		}

		public void visit(TimeDataComponent component) {
		}

		@Override
		public void visit(MeshComponent component) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(BatteryComponent component) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(AdaptiveTreeComposite component) {
			// TODO Auto-generated method stub

		}
	};
}