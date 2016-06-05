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
package org.eclipse.ice.reactor.plant.test;

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

import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.ICEJAXBHandler;
import org.eclipse.january.form.IReactorComponent;
import org.eclipse.january.form.SelectiveComponentVisitor;
import org.junit.Test;

/**
 * 
 * @author Anna Wojtowicz
 */
public class PipeTester {
	/**
	 * <p>
	 * Boolean flag to mark if the component was successfully visited.
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
		Pipe component = new Pipe();

		// Define the default values.
		double length = 1.0, radius = 0.1;

		// Check the default values.
		assertEquals("Pipe 1", component.getName());
		assertEquals("Pipe plant component for reactors",
				component.getDescription());
		assertEquals(1, component.getId());
		assertTrue(length == component.getLength());
		assertTrue(radius == component.getRadius());

		/* ---- Check parameterized constructor ---- */

		// Create a new component with valid parameters.
		length = 390.548;
		radius = 23.97;
		component = new Pipe(length, radius);

		// Check the values.
		assertEquals("Pipe 1", component.getName());
		assertEquals("Pipe plant component for reactors",
				component.getDescription());
		assertEquals(1, component.getId());
		assertTrue(length == component.getLength());
		assertTrue(radius == component.getRadius());

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
		Pipe component = new Pipe();

		// Set valid length, radius and number of elements.
		double length = 393.45, radius = 32.15;
		component.setLength(length);
		component.setRadius(radius);

		// Check the values were sucessfully set.
		assertTrue(length == component.getLength());
		assertTrue(radius == component.getRadius());

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
		Pipe component = new Pipe();
		component.setName("Daenerys Targaryen");
		component.setDescription("Mother of Dragons");
		component.setId(44);
		component.setLength(325.1);
		component.setRadius(17.5);

		// Construct a component equal to the first.
		Pipe equalComponent = new Pipe();
		equalComponent.setName("Daenerys Targaryen");
		equalComponent.setDescription("Mother of Dragons");
		equalComponent.setId(44);
		equalComponent.setLength(325.1);
		equalComponent.setRadius(17.5);

		// Construct a component not equal to the first.
		Pipe unequalComponent = new Pipe();
		unequalComponent.setName("Viserys Targaryen");
		unequalComponent.setDescription("The Third of His Name, King of the "
				+ "Andals, the Rhoynar and the First Men, Lord of the Seven "
				+ "Kingdoms and Protector of the Realm");
		unequalComponent.setId(43);
		unequalComponent.setLength(297.2256);
		unequalComponent.setRadius(37.51);

		// Check that component and unequalComponet are not the same.
		assertFalse(component.equals(unequalComponent));
		assertFalse(unequalComponent.equals(component));

		// Check that equality also fails with illegal values.
		assertFalse(component==null);
		assertFalse(component.equals(11));
		assertFalse("rararrr dragons!".equals(component));

		// Check is equals() is reflexive and symmetric.
		assertTrue(component.equals(component));
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Construct a component equal to the first, for testing transitivity.
		Pipe transComponent = new Pipe();
		transComponent.setName("Daenerys Targaryen");
		transComponent.setDescription("Mother of Dragons");
		transComponent.setId(44);
		transComponent.setLength(325.1);
		transComponent.setRadius(17.5);

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
		Pipe component = new Pipe();
		component.setName("Khaleesi of the Great Grass Sea");
		component.setDescription("Wife of Khal Drogo");
		component.setId(39);
		component.setLength(2.88);
		component.setRadius(0.712);

		/* ---- Check copying ---- */

		// Construct an empty component to copy to.
		Pipe componentCopy = new Pipe();

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

		// Make sure the object is an instance of Pipe.
		assertTrue(objectClone instanceof Pipe);

		// Cast the component.
		Pipe componentClone = (Pipe) component.clone();

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
	@Test
	public void checkPersistence() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Pipe.class);

		// Create a component for XML writing.
		Pipe writeComponent = new Pipe();
		writeComponent.setName("Khal Drogo");
		writeComponent
				.setDescription("A powerful warlord of the fearsome Dothraki nomads");
		writeComponent.setId(98);
		writeComponent.setLength(487.001);
		writeComponent.setRadius(58.371);

		/* ---- Check reading/loading with valid values ---- */

		// Create an output stream and persist the component to XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(writeComponent, classList, outputStream);

		// Create an input stream and feed the output stream into it.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Create a component for XML reading.
		Pipe loadComponent = new Pipe();

		// Load the inputStream into the component.
		loadComponent = (Pipe) xmlHandler.read(classList, inputStream);

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
		Pipe component = new Pipe();

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
		public void visit(Pipe plantComp) {
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