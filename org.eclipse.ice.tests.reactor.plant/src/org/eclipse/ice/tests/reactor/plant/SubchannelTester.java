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
package org.eclipse.ice.tests.reactor.plant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
import org.eclipse.ice.reactor.plant.Subchannel;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Anna Wojtowicz
 */
public class SubchannelTester {
	/**
	 * <p>
	 * Boolean flag to mark if the PlantComponent was successfully visited.
	 * </p>
	 * 
	 */
	private boolean wasVisited = false;

	/**
	 * <p>
	 * Checks the construction of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		/* ---- Check nullary constructor ---- */

		// Create a new component.
		Subchannel component = new Subchannel();

		// Define some default values.
		int numRods = 1;
		double rodDiameter = 1.0, pitch = 1.5;

		// Check the default values.
		assertEquals("Subchannel 1", component.getName());
		assertEquals("A subchannel plant component for reactors",
				component.getDescription());
		assertEquals(1, component.getId());
		assertTrue(numRods == component.getNumRods());
		assertTrue(rodDiameter == component.getRodDiameter());
		assertTrue(pitch == component.getPitch());

		/* ---- Check parameterized constructor ---- */

		// Create a new component with valid parameters.
		numRods = 10;
		rodDiameter = 2.4542;
		pitch = 2.4647;
		component = new Subchannel(numRods, rodDiameter, pitch);

		// Check the values.
		assertEquals("Subchannel 1", component.getName());
		assertEquals("A subchannel plant component for reactors",
				component.getDescription());
		assertEquals(1, component.getId());
		assertTrue(numRods == component.getNumRods());
		assertTrue(rodDiameter == component.getRodDiameter());
		assertTrue(pitch == component.getPitch());

		// We don't need to check with invalid parameters as the setters used
		// in construction will throw InvalidParameterExceptions.

		return;
	}

	/**
	 * <p>
	 * Checks the getting and setting of the component's attributes.
	 * </p>
	 * 
	 */
	@Test
	public void checkProperties() {

		// Create a new component.
		Subchannel component = new Subchannel();

		// Set valid rod number, diameter and pitch values.
		int numRods = 16;
		double rodDiameter = 3.4593, pitch = 3.4600;
		component.setNumRods(numRods);
		component.setRodDiameter(rodDiameter);
		component.setPitch(pitch);

		// Check the values were successfully set.
		assertTrue(numRods == component.getNumRods());
		assertTrue(rodDiameter == component.getRodDiameter());
		assertTrue(pitch == component.getPitch());

		// We don't need to check with invalid parameters as the setters will
		// throw InvalidParameterExceptions.

	}

	/**
	 * <p>
	 * Checks the hashCode and equality methods of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Construct a component to test against.
		Subchannel component = new Subchannel();
		component.setName("Eddard Stark");
		component.setDescription("Hand of the King, Robert Baratheon");
		component.setId(8);
		int numRods = 22;
		double rodDiameter = 33.9722, pitch = 33.9723;
		component.setNumRods(numRods);
		component.setRodDiameter(rodDiameter);
		component.setPitch(pitch);

		// Construct a component equal to the first.
		Subchannel equalComponent = new Subchannel();
		equalComponent.setName("Eddard Stark");
		equalComponent.setDescription("Hand of the King, Robert Baratheon");
		equalComponent.setId(8);
		equalComponent.setNumRods(numRods);
		equalComponent.setRodDiameter(rodDiameter);
		equalComponent.setPitch(pitch);

		// Construct a component not equal to the first.
		Subchannel unequalComponent = new Subchannel();
		unequalComponent.setName("Ned Stark");
		unequalComponent.setDescription("Lord of Winterfell");
		unequalComponent.setId(7);
		numRods = 24;
		rodDiameter = 34.4995;
		pitch = 35.0000;
		unequalComponent.setNumRods(numRods);
		unequalComponent.setRodDiameter(rodDiameter);
		unequalComponent.setPitch(pitch);

		// Check that component and unequalComponet are not the same.
		assertFalse(component.equals(unequalComponent));
		assertFalse(unequalComponent.equals(component));

		// Check that equality also fails with illegal values
		assertFalse(component == null);
		assertFalse(component.equals(11));
		assertFalse("Sean Bean".equals(component));

		// Check is equals() is reflexive and symmetric.
		assertTrue(component.equals(component));
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Construct a component equal to the first, for testing transitivity.
		Subchannel transComponent = new Subchannel();
		transComponent.setName("Eddard Stark");
		transComponent.setDescription("Hand of the King, Robert Baratheon");
		transComponent.setId(8);
		numRods = 22;
		rodDiameter = 33.9722;
		pitch = 33.9723;
		transComponent.setNumRods(numRods);
		transComponent.setRodDiameter(rodDiameter);
		transComponent.setPitch(pitch);

		// Check equals() is transitive.
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

		return;
	}

	/**
	 * <p>
	 * Checks the copy and clone methods of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Construct a base component to copy from.
		Subchannel component = new Subchannel();
		component.setName("Catelyn Tully");
		component.setDescription("The Lady of Winterfell");
		component.setId(44);
		int numRods = 18;
		double rodDiameter = 13.9722, pitch = 13.9728;
		component.setNumRods(numRods);
		component.setRodDiameter(rodDiameter);
		component.setPitch(pitch);

		/* ---- Check copying ---- */

		// Construct an empty component to copy to.
		Subchannel componentCopy = new Subchannel();

		// Check that component and componentCopy are not identical yet.
		assertFalse(component == componentCopy);
		assertFalse(component.equals(componentCopy));

		// Copy contents over.
		componentCopy.copy(component);

		// Check component and componentCopy are identical.
		assertTrue(component.equals(componentCopy));

		// Try to copy contents of an invalid component.
		componentCopy.copy(null);

		// Check that componentCopy remains unchanged.
		assertTrue(component.equals(componentCopy));

		// Make sure they are still different references!
		assertFalse(component == componentCopy);

		/* ---- Check cloning ---- */

		// Get a clone of the original component.
		Object objectClone = component.clone();

		// Make sure it's not null!
		assertNotNull(objectClone);

		// Make sure the reference is different but the contents are equal.
		assertFalse(component == objectClone);
		assertTrue(component.equals(objectClone));
		assertFalse(componentCopy == objectClone);
		assertTrue(componentCopy.equals(objectClone));

		// Make sure the object is an instance of Subchannel.
		assertTrue(objectClone instanceof Subchannel);

		// Cast the component.
		Subchannel componentClone = (Subchannel) component.clone();

		// Check the components one last time for good measure.
		assertFalse(component == componentClone);
		assertTrue(component.equals(componentClone));

		return;
	}

	/**
	 * Checks for persistence in the component.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Ignore
	public void checkPersistence() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Subchannel.class);
		
		// Create a component for XML writing.
		Subchannel writeComponent = new Subchannel();
		writeComponent.setName("Catelyn Tully");
		writeComponent.setDescription("Wife of Eddard Stark");
		writeComponent.setId(4);
		int numRods = 30;
		double rodDiameter = 3.98, pitch = 3.99;
		writeComponent.setNumRods(numRods);
		writeComponent.setRodDiameter(rodDiameter);
		writeComponent.setPitch(pitch);

		/* ---- Check reading/loading with valid values ---- */

		// Create an output stream and persist the component to XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(writeComponent, classList, outputStream);

		// Create an input stream and feed the output stream into it.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Create a component for XML reading.
		Subchannel loadComponent = new Subchannel();

		// Load the inputStream into the component.
		loadComponent = (Subchannel) xmlHandler.read(classList, inputStream);

		// Compare the two components, they should be the same.
		assertTrue(writeComponent.equals(loadComponent));

		return;
	}

	/**
	 * <p>
	 * Checks the visitation routine of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		// Create a new component to visit.
		Subchannel component = new Subchannel();

		// Create an invalid visitor, and try to visit the component.
		FakeComponentVisitor visitor = null;
		component.accept(visitor);

		// Check that the component wasn't visited yet.
		assertFalse(wasVisited);

		// Create a valid visitor, and try to visit the component.
		visitor = new FakeComponentVisitor();
		component.accept(visitor);

		// Check that the component was visited.
		assertTrue(wasVisited);

		// Grab the visitor's visited component.
		Component visitorComponent = visitor.component;

		// Check that the visitor's component is the same component we initially
		// created.
		assertTrue(component == visitorComponent);
		assertTrue(component.equals(visitorComponent));

		// ---- Check PlantComponent visitation. ---- //
		wasVisited = false;

		// Create an invalid visitor, and try to visit the component.
		FakePlantComponentVisitor plantVisitor = null;
		component.accept(plantVisitor);

		// Check that the component wasn't visited yet.
		assertFalse(wasVisited);

		// Create a valid visitor, and try to visit the component.
		plantVisitor = new FakePlantComponentVisitor();
		component.accept(plantVisitor);

		// Check that the component was visited.
		assertTrue(wasVisited);

		// Grab the visitor's visited component.
		PlantComponent visitorPlantComponent = plantVisitor.component;

		// Check that the visitor's component is the same component we initially
		// created.
		assertTrue(component == visitorPlantComponent);
		assertTrue(component.equals(visitorPlantComponent));

		return;
	}

	/**
	 * <p>
	 * Fake class to test the visitation routine of the component.
	 * </p>
	 * 
	 * @author Anna Wojtowicz
	 */
	private class FakeComponentVisitor extends SelectiveComponentVisitor {

		// The fake visitor's visited component.
		private IReactorComponent component = null;

		@Override
		public void visit(IReactorComponent component) {

			// Set the IComponentVisitor component (if valid), and flag the
			// component as having been visited.
			if (component != null) {
				this.component = component;
				wasVisited = true;
			}
			return;
		}
	};

	/**
	 * Fake class to test the PlantComponent visitation routine.
	 * 
	 * @author Jordan
	 * 
	 */
	private class FakePlantComponentVisitor extends
			SelectivePlantComponentVisitor {

		// The fake visitor's visited component.
		private PlantComponent component = null;

		@Override
		public void visit(Subchannel plantComp) {
			// Set the IComponentVisitor component (if valid), and flag the
			// component as having been visited.
			if (plantComp != null) {
				this.component = plantComp;
				wasVisited = true;
			}
			return;
		}
	}
}