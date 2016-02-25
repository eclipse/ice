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
package org.eclipse.eavp.viz.service.mesh.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.eavp.viz.service.datastructures.VizObject.VizJAXBHandler;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryCondition;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryConditionType;
import org.eclipse.eavp.viz.service.mesh.datastructures.EdgeProperties;
import org.junit.Test;

/**
 * <p>
 * Tests the EdgeProperties class.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class EdgePropertiesTester {

	/**
	 * <p>
	 * This operation tests the ability to set and get fluid boundary conditions
	 * for an EdgeProperties instance.
	 * </p>
	 * 
	 */
	@Test
	public void checkFluidBoundaryCondition() {

		EdgeProperties properties = new EdgeProperties();

		BoundaryCondition condition;
		BoundaryCondition defaultCondition = new BoundaryCondition();

		/* ---- Check the initial value. ---- */
		assertEquals(defaultCondition, properties.getFluidBoundaryCondition());
		/* ---------------------------------- */

		/* ---- Try setting the condition to valid values. ---- */
		// We should be able to set a new value.
		condition = new BoundaryCondition(BoundaryConditionType.Flux);
		assertTrue(properties.setFluidBoundaryCondition(condition));
		assertEquals(condition, properties.getFluidBoundaryCondition());

		// We should be able to pass in a new reference even though it is
		// equivalent.
		condition = new BoundaryCondition(BoundaryConditionType.Flux);
		assertTrue(properties.setFluidBoundaryCondition(condition));
		assertEquals(condition, properties.getFluidBoundaryCondition());
		/* ---------------------------------------------------- */

		/* ---- Try setting the condition to invalid values. ---- */
		// Setting the value to null should change nothing.
		assertFalse(properties.setFluidBoundaryCondition(null));
		assertEquals(condition, properties.getFluidBoundaryCondition());

		// Setting the value to the same object should change nothing.
		assertFalse(properties.setFluidBoundaryCondition(condition));
		assertEquals(condition, properties.getFluidBoundaryCondition());
		/* ------------------------------------------------------ */

		return;
	}

	/**
	 * <p>
	 * This operation tests the ability to set and get thermal boundary
	 * conditions for an EdgeProperties instance.
	 * </p>
	 * 
	 */
	@Test
	public void checkThermalBoundaryCondition() {

		EdgeProperties properties = new EdgeProperties();

		BoundaryCondition condition;
		BoundaryCondition defaultCondition = new BoundaryCondition();

		/* ---- Check the initial value. ---- */
		assertEquals(defaultCondition, properties.getThermalBoundaryCondition());
		/* ---------------------------------- */

		/* ---- Try setting the condition to valid values. ---- */
		// We should be able to set a new value.
		condition = new BoundaryCondition(BoundaryConditionType.Flux);
		assertTrue(properties.setThermalBoundaryCondition(condition));
		assertEquals(condition, properties.getThermalBoundaryCondition());

		// We should be able to pass in a new reference even though it is
		// equivalent.
		condition = new BoundaryCondition(BoundaryConditionType.Flux);
		assertTrue(properties.setThermalBoundaryCondition(condition));
		assertEquals(condition, properties.getThermalBoundaryCondition());
		/* ---------------------------------------------------- */

		/* ---- Try setting the condition to invalid values. ---- */
		// Setting the value to null should change nothing.
		assertFalse(properties.setThermalBoundaryCondition(null));
		assertEquals(condition, properties.getThermalBoundaryCondition());

		// Setting the value to the same object should change nothing.
		assertFalse(properties.setThermalBoundaryCondition(condition));
		assertEquals(condition, properties.getThermalBoundaryCondition());
		/* ------------------------------------------------------ */

		return;
	}

	/**
	 * <p>
	 * This operation tests the ability to set and get passive scalar boundary
	 * conditions for an EdgeProperties instance.
	 * </p>
	 * 
	 */
	@Test
	public void checkOtherBoundaryConditions() {

		EdgeProperties properties = new EdgeProperties();

		BoundaryCondition condition;
		BoundaryCondition defaultCondition = new BoundaryCondition();
		ArrayList<BoundaryCondition> conditions = new ArrayList<BoundaryCondition>();

		int scalarId = 0;

		/* ---- Check the initial values. ---- */
		assertEquals(conditions, properties.getOtherBoundaryConditions());
		assertTrue(properties.getOtherBoundaryCondition(scalarId) == null);
		/* ----------------------------------- */

		/* ---- Try setting a condition to valid values. ---- */
		scalarId = 1;

		// We should be able to set a new value.
		condition = new BoundaryCondition(BoundaryConditionType.Flux);
		assertTrue(properties.setOtherBoundaryCondition(scalarId, condition));

		// Update our own book-keeping.
		conditions.add(defaultCondition);
		conditions.add(condition);

		// Check the updated boundary condition.
		assertEquals(condition, properties.getOtherBoundaryCondition(scalarId));
		// Check the contents of the lower-index bounddary conditions.
		for (int i = 0; i < scalarId; i++) {
			assertEquals(defaultCondition,
					properties.getOtherBoundaryCondition(i));
		}
		// Check the contents of the ArrayList.
		assertEquals(conditions, properties.getOtherBoundaryConditions());

		// We should be able to pass in a new reference even though it is
		// equivalent.
		condition = new BoundaryCondition(BoundaryConditionType.Flux);
		assertTrue(properties.setOtherBoundaryCondition(scalarId, condition));
		// Check the updated boundary condition.
		assertEquals(condition, properties.getOtherBoundaryCondition(scalarId));
		// Check the contents of the lower-index bounddary conditions.
		for (int i = 0; i < scalarId; i++) {
			assertEquals(defaultCondition,
					properties.getOtherBoundaryCondition(i));
		}
		// Check the contents of the ArrayList.
		assertEquals(conditions, properties.getOtherBoundaryConditions());
		/* --------------------------------------------------- */

		/* ---- Try setting a condition to invalid values. ---- */
		// Setting the value to null should change nothing.
		assertFalse(properties.setOtherBoundaryCondition(scalarId, null));
		// Check the boundary condition.
		assertEquals(condition, properties.getOtherBoundaryCondition(scalarId));
		// Check the contents of the lower-index bounddary conditions.
		for (int i = 0; i < scalarId; i++) {
			assertEquals(defaultCondition,
					properties.getOtherBoundaryCondition(i));
		}
		// Check the contents of the ArrayList.
		assertEquals(conditions, properties.getOtherBoundaryConditions());

		// Setting the value to the same object should change nothing.
		assertFalse(properties.setOtherBoundaryCondition(scalarId, condition));
		// Check the boundary condition.
		assertEquals(condition, properties.getOtherBoundaryCondition(scalarId));
		// Check the contents of the lower-index bounddary conditions.
		for (int i = 0; i < scalarId; i++) {
			assertEquals(defaultCondition,
					properties.getOtherBoundaryCondition(i));
		}
		// Check the contents of the ArrayList.
		assertEquals(conditions, properties.getOtherBoundaryConditions());
		/* ----------------------------------------------------- */

		/* ---- Make sure the internal list cannot be modified. ---- */
		// Try to clear the list.
		properties.getOtherBoundaryConditions().clear();
		// Check the boundary condition.
		assertEquals(condition, properties.getOtherBoundaryCondition(scalarId));
		// Check the contents of the lower-index bounddary conditions.
		for (int i = 0; i < scalarId; i++) {
			assertEquals(defaultCondition,
					properties.getOtherBoundaryCondition(i));
		}
		// Check the contents of the ArrayList.
		assertEquals(conditions, properties.getOtherBoundaryConditions());
		/* --------------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * This operation checks the ability of the EdgeProperties instance to
	 * persist itself to XML and to load itself from an XML input stream.
	 * </p>
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 * 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// Create an object to test.
		EdgeProperties properties = new EdgeProperties();
		VizJAXBHandler xmlHandler = new VizJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(EdgeProperties.class);

		// Add a fluid and passive scalar boundary condition.
		properties.setFluidBoundaryCondition(new BoundaryCondition(
				BoundaryConditionType.Flux));
		properties.setOtherBoundaryCondition(1, new BoundaryCondition(
				BoundaryConditionType.Wall));

		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(properties, classList, outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		EdgeProperties loadedProperties = new EdgeProperties();
		loadedProperties = (EdgeProperties) xmlHandler.read(classList, inputStream);

		// Make sure the two components match.
		assertTrue(properties.equals(loadedProperties));

		return;
	}

	/**
	 * <p>
	 * This operation checks the EdgeProperties to insure that its equals() and
	 * hashCode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Initialize objects for testing.
		EdgeProperties object = new EdgeProperties();
		EdgeProperties equalObject = new EdgeProperties();

		object.setOtherBoundaryCondition(2, new BoundaryCondition(
				BoundaryConditionType.Internal));
		equalObject.setOtherBoundaryCondition(2, new BoundaryCondition(
				BoundaryConditionType.Internal));

		// Initialize the unequalObject.
		EdgeProperties unequalObject = new EdgeProperties();

		unequalObject.setOtherBoundaryCondition(2, new BoundaryCondition(
				BoundaryConditionType.Internal));
		// Give it a new set of values where only the last one is different.
		ArrayList<Float> values = new ArrayList<Float>();
		for (int i = 0; i < 4; i++) {
			values.add(0f);
		}
		values.add(0.0000001f);
		unequalObject.getOtherBoundaryCondition(2).setValues(values);

		// Make sure the references are different.
		assertFalse(object == equalObject);
		assertFalse(object == unequalObject);
		assertFalse(equalObject == unequalObject);

		// Check that equality is reflexive and symmetric.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that equals will fail when it should.
		assertFalse(object==null);
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Check the hash codes.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());
		assertFalse(object.hashCode() == unequalObject.hashCode());

		return;
	}

	/**
	 * <p>
	 * This operation checks the EdgeProperties to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Create a EdgeProperties to test.
		EdgeProperties object = new EdgeProperties();
		EdgeProperties copy = new EdgeProperties();
		EdgeProperties clone = null;

		// Give the object some data.
		object.setThermalBoundaryCondition(new BoundaryCondition(
				BoundaryConditionType.Periodic));
		object.setOtherBoundaryCondition(5, new BoundaryCondition(
				BoundaryConditionType.Symmetry));

		// Make sure the objects are not equal before copying.
		assertFalse(object == copy);
		assertFalse(object.equals(copy));

		// Copy the object.
		copy.copy(object);

		// Make sure the references are different but contents the same.
		assertFalse(object == copy);
		assertTrue(object.equals(copy));

		// Do the same for the clone operation.

		// Make sure the objects are not equal before copying.
		assertFalse(object == clone);
		assertFalse(object.equals(clone));

		// Copy the object.
		clone = (EdgeProperties) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}