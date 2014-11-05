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
package org.eclipse.ice.viz.plotviewer.test;

import static org.junit.Assert.*;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.viz.plotviewer.CSVData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

/**
 * This class is responsible for testing the CSVData to make sure that it is
 * correctly constructed.
 * 
 * @author xxi
 * 
 */
public class CSVDataTester {

	/**
	 * Fields for this class
	 */
	private ArrayList<Double> position;
	private double value;
	private double uncertainty;
	private String units;
	private String feature;
	private CSVData csv;

	/**
	 * This operation performs the initial setup.
	 */
	@Before
	public void beforeClass() {

		// Set position
		position = new ArrayList<Double>();
		position.add(0.0);
		position.add(0.0);
		position.add(0.0);

		// Set value, uncertainty, units, features
		value = 3.70;
		uncertainty = 2.50;
		units = "meters";
		feature = "Intensity";
		csv = new CSVData(feature, value);

	}

	/**
	 * Check the getPosition() method.
	 */
	@Test
	public void checkSetAndGetPosition() {

		// Set and then get the position from the CSVData field.
		csv.setPosition(position);
		ArrayList<Double> csvPosition = csv.getPosition();

		// Create a new position array and set the CSVData field to it.
		ArrayList<Double> newPosition = new ArrayList<Double>();
		newPosition.add(0.0);
		newPosition.add(0.0);
		newPosition.add(0.0);
		csv.setPosition(newPosition);

		// Check ArrayList size
		assertEquals(csvPosition.size(), newPosition.size());
		assertEquals(csvPosition, newPosition);

		// Check that values match
		for (int i = 0; i < csvPosition.size(); i++) {
			assertEquals(csvPosition.get(i), newPosition.get(i), 1e-15);
		}

	}

	/**
	 * Check the getUncertainty() and setUncertainty() methods.
	 */
	@Test
	public void checkSetAndGetUncertainty() {

		csv.setUncertainty(uncertainty);
		assertEquals(uncertainty, csv.getUncertainty(), 1e-15);

	}

	/**
	 * Check the getUnits() and setUnits() methods.
	 */
	@Test
	public void checkSetAndGetUnits() {

		csv.setUnits(units);
		assertEquals(units, csv.getUnits());

	}

	/**
	 * Check the getFeature() and setFeature() methods.
	 */
	@Test
	public void checkSetAndGetFeature() {

		csv.setFeature(feature);
		assertEquals(feature, csv.getFeature());

	}

	/**
	 * Check the getValue() and setValue() methods.
	 */
	@Test
	public void checkSetAndGetValue() {

		csv.setValue(value);
		assertEquals(value, csv.getValue(), 1e-15);

	}

	/**
	 * Check the equals() method.
	 */
	@Test
	public void testEquals() {

		// Check case where equals is true
		String feature1 = feature;
		double value1 = value;
		CSVData csv1 = new CSVData(feature1, value1);
		assertEquals(csv, csv1);

		ICEObject otherCSVData = new ICEObject();
		assertFalse(otherCSVData instanceof CSVData);

		IData otherCSVData_ID = new CSVData(feature, value);
		assertTrue(otherCSVData_ID instanceof CSVData);

		// Check case where equals is false
		// Set a null object
		CSVData objectTest = null;
		assertFalse(csv.equals(objectTest));
		// !Null object compared to null object
		String newFeature = feature;
		CSVData csv2 = new CSVData(newFeature, 4.0);
		assertFalse(csv.equals(csv2));

		CSVData niceCSVData = new CSVData(feature, value);
		assertFalse(super.equals(niceCSVData));

		CSVData otherCSVData2 = new CSVData(feature, value);
		CSVData castedCSVData = (CSVData) otherCSVData2;
		double checkValue = value - castedCSVData.getValue();
		double tolTest = value + castedCSVData.getValue();
		assertFalse(checkValue > tolTest);

		// Check ArrayList
		ArrayList<Double> newPositionEquals = new ArrayList<Double>();
		newPositionEquals.add(0.0);
		newPositionEquals.add(0.0);
		newPositionEquals.add(0.0);
		newPositionEquals.add(0.0);
		csv.setPosition(newPositionEquals);
		assertFalse(csv.equals(newPositionEquals));

		ArrayList<Double> newPositionEquals2 = new ArrayList<Double>();
		newPositionEquals2.add(0.0);
		newPositionEquals2.add(0.0);
		newPositionEquals2.add((double) Math.pow(1, -16));
		csv.setPosition(newPositionEquals2);
		assertEquals(csv.equals(newPositionEquals2), csv.equals(position));

	}

	/**
	 * Check the clone() method.
	 */
	@Test
	public void testClone() {

		CSVData csv1 = csv.clone();
		assertEquals(csv1, csv);

	}

	/**
	 * This checks the NullPointerException thrower
	 * 
	 * The ExpectedException Rule allows in-test specification of expected
	 * exception types and messages.
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void nullPointerExceptionClone() {

		thrown.expect(NullPointerException.class);
		throw new NullPointerException();

	}

	/**
	 * Check the copy() method.
	 */
	@Test
	public void testCopy() {

		CSVData csv1 = new CSVData(feature, value);
		csv1.copy(csv);
		assertEquals(csv1, csv);

	}

}
