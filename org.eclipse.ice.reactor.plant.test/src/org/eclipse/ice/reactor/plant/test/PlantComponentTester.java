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

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.junit.Test;

/**
 * <!-- begin-UML-doc --> Checks the methods of the PlantComponent class. <!--
 * end-UML-doc -->
 *
 * @author Anna Wojtowicz
 */
public class PlantComponentTester {
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
		PlantComponent component = new PlantComponent();

		// Check the default values.
		assertEquals("Plant Component 1", component.getName());
		assertEquals("Plant-level reactor component",
				component.getDescription());
		assertEquals(1, component.getId());

		/* ---- Check parameterized constructor ---- */

		// Try to create a component with an invalid name.
		String invalidName = null;
		component = new PlantComponent(invalidName);

		// Check that component did not take the name (defaulted to nullary
		// values).
		assertEquals("Plant Component 1", component.getName());
		assertEquals("Plant-level reactor component",
				component.getDescription());
		assertEquals(1, component.getId());

		// Try again with valid name.
		String validName = "Sir Plant Component III";
		component = new PlantComponent(validName);

		// Check that the name took.
		assertEquals(validName, component.getName());
		assertEquals("Plant-level reactor component",
				component.getDescription());
		assertEquals(1, component.getId());

		return;
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
		PlantComponent component = new PlantComponent("Arya");
		component.setDescription("Youngest of the Stark girls");
		component.setId(10);

		// Construct a component equal to the first.
		PlantComponent equalComponent = new PlantComponent("Arya");
		equalComponent.setDescription("Youngest of the Stark girls");
		equalComponent.setId(10);

		// Construct a component not equal to the first.
		PlantComponent unequalComponent = new PlantComponent("Sansa");
		unequalComponent.setDescription("Eldest of the Stark girls");
		unequalComponent.setId(5);

		// Check that component and unequalComponet are not the same.
		assertFalse(component.equals(unequalComponent));
		assertFalse(unequalComponent.equals(component));

		// Check that equality also fails with illegal values
		assertFalse(component == null);
		assertFalse(component.equals(13));
		assertFalse("just a string".equals(component));

		// Check is equals() is reflexive and symmetric.
		assertTrue(component.equals(component));
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Construct a component equal to the first, for testing transitivity.
		PlantComponent transComponent = new PlantComponent("Arya");
		transComponent.setDescription("Youngest of the Stark girls");
		transComponent.setId(10);

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
		PlantComponent component = new PlantComponent("Jon Snow");
		component.setDescription("Son of Eddard Stark");
		component.setId(30);

		/* ---- Check copying ---- */

		// Construct an empty component to copy to.
		PlantComponent componentCopy = new PlantComponent();

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

		// Make sure the object is an instance of PlantComponent.
		assertTrue(objectClone instanceof PlantComponent);

		// Cast the component.
		PlantComponent componentClone = (PlantComponent) component.clone();

		// Check the components one last time for good measure.
		assertFalse(component == componentClone);
		assertTrue(component.equals(componentClone));

		return;
	}

	/**
	 * Checks for persistence in the component.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 * @throws NullPointerException
	 */
	@Test
	public void checkPersistence()
			throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(PlantComponent.class);

		// Create a component for XML writing.
		PlantComponent writeComponent = new PlantComponent();
		writeComponent.setName("Robb Stark");
		writeComponent.setDescription("King of the North");
		writeComponent.setId(31);

		/* ---- Check reading/loading with valid values ---- */

		// Create an output stream and persist the component to XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(writeComponent, classList, outputStream);

		// Create an input stream and feed the output stream into it.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Create a component for XML reading.
		PlantComponent loadComponent = new PlantComponent();

		// Load the inputStream into the component.
		loadComponent = (PlantComponent) xmlHandler.read(classList,
				inputStream);

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
		PlantComponent component = new PlantComponent("Jon Snow");

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
}