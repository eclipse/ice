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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class HeatExchangerTester {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Boolean flag to mark if the PlantComponent was successfully visited.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean wasVisited = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the getting and setting of the component's attributes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkProperties() {
		// begin-user-code

		// Create a new component.
		HeatExchanger component = new HeatExchanger();

		// Check the default values.
		assertEquals("Heat Exchanger", component.getName());
		assertEquals("A heat exchanger for plants", component.getDescription());
		assertEquals(1, component.getId());
		assertEquals(0.1, component.getInnerRadius(), 0.001);
		assertEquals(1.0, component.getLength(), 0.001);
		assertEquals(1, component.getNumElements());
		assertNotNull(component.getPrimaryPipe());
		assertNotNull(component.getSecondaryPipe());
		assertEquals(2, component.getComponents().size());

		// Check the inner radius
		component.setInnerRadius(2.0);
		assertEquals(2.0, component.getInnerRadius(), 0.001);

		// Check the length
		component.setLength(3.0);
		assertEquals(3.0, component.getLength(), 0.001);

		// Check the number of elements
		component.setNumElements(4);
		assertEquals(4, component.getNumElements());

		// Check that the list of components has the correct size and contents.
		assertEquals(2, component.getComponents().size());
		assertEquals(component.getPrimaryPipe(),
				component.getComponents().get(0));
		assertEquals(component.getSecondaryPipe(), component.getComponents()
				.get(1));

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the hashCode and equality methods of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		// Construct a component to test against.
		HeatExchanger component = new HeatExchanger();
		component.setInnerRadius(2.0);
		component.setLength(3.0);
		component.setNumElements(4);

		// Construct a component equal to the first.
		HeatExchanger equalComponent = new HeatExchanger();
		equalComponent.setInnerRadius(2.0);
		equalComponent.setLength(3.0);
		equalComponent.setNumElements(4);

		// Construct a component equal to the first for checking transitivity
		HeatExchanger transComponent = new HeatExchanger();
		transComponent.setInnerRadius(2.0);
		transComponent.setLength(3.0);
		transComponent.setNumElements(4);

		// Construct a component not equal to the first.
		HeatExchanger unequalComponent = new HeatExchanger();

		// Check that component and unequalComponet are not the same.
		assertFalse(component.equals(unequalComponent));
		assertFalse(unequalComponent.equals(component));

		// Check that equality also fails with illegal values
		assertFalse(component == null);
		assertFalse(component.equals(11));
		assertFalse("House Lannister".equals(component));

		// Check is equals() is reflexive and symmetric.
		assertTrue(component.equals(component));
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the copy and clone methods of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Construct a component to test against.
		HeatExchanger component = new HeatExchanger();
		component.setInnerRadius(2.0);
		component.setLength(3.0);
		component.setNumElements(4);
		component.setOrientation(new double[] { 1, 2, 5 });

		/* ---- Check copying ---- */

		// Construct an empty component to copy to.
		HeatExchanger componentCopy = new HeatExchanger();

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

		// Make sure the object is an instance of HeatExchanger.
		assertTrue(objectClone instanceof HeatExchanger);

		// Cast the component.
		HeatExchanger componentClone = (HeatExchanger) component.clone();

		// Check the components one last time for good measure.
		assertFalse(component == componentClone);
		assertTrue(component.equals(componentClone));

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks for persistence in the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Ignore
	@Test
	public void checkPersistence() {
		// begin-user-code

		// Construct a component to test against.
		HeatExchanger writeComponent = new HeatExchanger();
		writeComponent.setInnerRadius(2.0);
		writeComponent.setLength(3.0);
		writeComponent.setNumElements(4);

		/* ---- Check reading/loading with valid values ---- */

		// Create an output stream and persist the component to XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeComponent.persistToXML(outputStream);
		writeComponent.persistToXML(System.out); // Debugging

		// Create an input stream and feed the output stream into it.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Create a component for XML reading.
		HeatExchanger loadComponent = new HeatExchanger();

		// Load the inputStream into the component.
		loadComponent.loadFromXML(inputStream);
		loadComponent.persistToXML(System.out);

		// Compare the two components, they should be the same.
		assertEquals(writeComponent, loadComponent);

		/* ---- Check reading/loading with invalid values ---- */

		// Try to load with an invalid stream.
		loadComponent.loadFromXML(null);

		// Check that the component remains unchanged.
		assertEquals(writeComponent, loadComponent);

		// Try to write to an invalid stream.
		outputStream = null;
		loadComponent.persistToXML(outputStream);

		// Check that the output stream remains unchanged (is null).
		assertNull(outputStream);

		return;

		// end-user-code
	}

	/**
	 * Checks that the HeatExchanger's primary and secondary Pipes are linked
	 * with the HeatExchanger's properties, including ID, length, radius, and
	 * properties inherited from GeometricalComponent.
	 */
	@Test
	public void checkPipes() {
		// begin-user-code

		// Create a HeatExchanger and get its primary and secondary Pipes.
		HeatExchanger component = new HeatExchanger();
		Pipe primary = component.getPrimaryPipe();
		Pipe secondary = component.getSecondaryPipe();

		// ---- Check inner radius ---- //
		// Check the default value.
		double radius = component.getInnerRadius();
		assertEquals(radius, primary.getRadius(), 0.001);
		assertEquals(radius, secondary.getRadius(), 0.001);

		// Set to a new value and check it.
		radius += 1.0;
		component.setInnerRadius(radius);
		assertEquals(radius, component.getInnerRadius(), 0.001);
		assertEquals(radius, primary.getRadius(), 0.001);
		assertEquals(radius, secondary.getRadius(), 0.001);

		// ---- Check length ---- //
		// Check the default value.
		double length = component.getLength();
		assertEquals(length, primary.getLength(), 0.001);
		assertEquals(length, secondary.getLength(), 0.001);

		// Set to a new value and check it.
		length += 1.0;
		component.setLength(length);
		assertEquals(length, component.getLength(), 0.001);
		assertEquals(length, primary.getLength(), 0.001);
		assertEquals(length, secondary.getLength(), 0.001);

		// ---- Check ID ---- //
		// Check the default value.
		int id = component.getId();
		assertEquals(id, primary.getId());
		assertEquals(id, secondary.getId());

		// Set to a new value and check it.
		id++;
		component.setId(id);
		assertEquals(id, component.getId());
		assertEquals(id, primary.getId());
		assertEquals(id, secondary.getId());

		// ---- Check position ---- //
		// Check the default value.
		double[] position = component.getPosition();
		assertTrue(Arrays.equals(position, primary.getPosition()));
		assertTrue(Arrays.equals(position, secondary.getPosition()));

		// Set to a new value and check it.
		position = new double[] { 55, 55, 55 };
		component.setPosition(position);
		assertTrue(Arrays.equals(position, component.getPosition()));
		assertTrue(Arrays.equals(position, primary.getPosition()));
		assertTrue(Arrays.equals(position, secondary.getPosition()));

		// ---- Check orientation ---- //
		// Check the default value.
		double[] orientation = component.getOrientation();
		assertTrue(Arrays.equals(orientation, primary.getOrientation()));
		assertTrue(Arrays.equals(orientation, secondary.getOrientation()));

		// Set to a new value and check it.
		orientation = new double[] { 07734, 1337, 42.0 };
		component.setOrientation(orientation);
		assertTrue(Arrays.equals(orientation, component.getOrientation()));
		assertTrue(Arrays.equals(orientation, primary.getOrientation()));
		assertTrue(Arrays.equals(orientation, secondary.getOrientation()));

		// ---- Check rotation ---- //
		// Check the default value.
		double rotation = component.getRotation();
		assertEquals(rotation, primary.getRotation(), 0.001);
		assertEquals(rotation, secondary.getRotation(), 0.001);

		// Set to a new value and check it.
		rotation += 1.0;
		component.setRotation(rotation);
		assertEquals(rotation, component.getRotation(), 0.001);
		assertEquals(rotation, primary.getRotation(), 0.001);
		assertEquals(rotation, secondary.getRotation(), 0.001);

		// ---- Check number of elements ---- //
		// Check the default value.
		int numElements = component.getNumElements();
		assertEquals(numElements, primary.getNumElements());
		assertEquals(numElements, secondary.getNumElements());

		// Set to a new value and check it.
		numElements++;
		component.setNumElements(numElements);
		assertEquals(numElements, component.getNumElements());
		assertEquals(numElements, primary.getNumElements());
		assertEquals(numElements, secondary.getNumElements());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the visitation routine of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkVisitation() {
		// begin-user-code

		// Construct a component to test against.
		HeatExchanger component = new HeatExchanger();

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Fake class to test the visitation routine of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @author w5q
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
		public void visit(HeatExchanger plantComp) {
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