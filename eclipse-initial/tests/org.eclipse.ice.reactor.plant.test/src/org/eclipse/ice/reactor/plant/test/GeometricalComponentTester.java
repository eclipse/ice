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

import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.reactor.plant.Boundary;
import org.eclipse.ice.reactor.plant.Branch;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.DownComer;
import org.eclipse.ice.reactor.plant.FlowJunction;
import org.eclipse.ice.reactor.plant.GeometricalComponent;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.IPlantComponentVisitor;
import org.eclipse.ice.reactor.plant.IdealPump;
import org.eclipse.ice.reactor.plant.Inlet;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.MassFlowInlet;
import org.eclipse.ice.reactor.plant.OneInOneOutJunction;
import org.eclipse.ice.reactor.plant.Outlet;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PipeToPipeJunction;
import org.eclipse.ice.reactor.plant.PipeWithHeatStructure;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.PointKinetics;
import org.eclipse.ice.reactor.plant.Pump;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SeparatorDryer;
import org.eclipse.ice.reactor.plant.SolidWall;
import org.eclipse.ice.reactor.plant.SpecifiedDensityAndVelocityInlet;
import org.eclipse.ice.reactor.plant.Subchannel;
import org.eclipse.ice.reactor.plant.SubchannelBranch;
import org.eclipse.ice.reactor.plant.TDM;
import org.eclipse.ice.reactor.plant.TimeDependentJunction;
import org.eclipse.ice.reactor.plant.TimeDependentVolume;
import org.eclipse.ice.reactor.plant.Turbine;
import org.eclipse.ice.reactor.plant.Valve;
import org.eclipse.ice.reactor.plant.VolumeBranch;
import org.eclipse.ice.reactor.plant.WetWell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class GeometricalComponentTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Boolean flag to mark if the component was successfully visited.
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
	 * Checks the construction of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		/* ---- Check nullary constructor ---- */

		// Create a new component.
		GeometricalComponent component = new GeometricalComponent();

		double[] position = new double[3], orientation = new double[3];

		// Check the default values.
		assertEquals("Geometrical Component 1", component.getName());
		assertEquals("A geometrical plant component for reactors",
				component.getDescription());
		assertEquals(1, component.getId());
		assertTrue(Arrays.equals(position, component.getPosition()));
		assertTrue(Arrays.equals(orientation, component.getOrientation()));
		assertTrue(0.0 == component.getRotation());
		assertTrue(1 == component.getNumElements());

		/* ---- Check parameterized constructor ---- */

		// Create a new component with valid parameters.
		for (int i = 0; i < 3; i++) {
			position[i] = 5 * i + 3.4930;
			orientation[i] = 7 * i + 2.1893;
		}
		double rotation = 270, numElements = 1;
		component = new GeometricalComponent(position, orientation, rotation);

		// Check the values.
		assertEquals("Geometrical Component 1", component.getName());
		assertEquals("A geometrical plant component for reactors",
				component.getDescription());
		assertEquals(1, component.getId());
		assertTrue(Arrays.equals(position, component.getPosition()));
		assertTrue(Arrays.equals(orientation, component.getOrientation()));
		assertTrue(rotation == component.getRotation());
		assertTrue(numElements == component.getNumElements());

		// We don't need to check with invalid parameters as the setters used
		// in construction will throw InvalidParameterExceptions.

		return;
		// end-user-code
	}

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
		GeometricalComponent component = new GeometricalComponent();

		// Set valid position, orientation and rotation values.
		double[] position = new double[3], orientation = new double[3];
		for (int i = 0; i < 3; i++) {
			position[i] = 5 * i + 3.4930;
			orientation[i] = 7 * i + 2.1893;
		}
		double rotation = 172;
		int numElements = 15;
		component.setPosition(position);
		component.setOrientation(orientation);
		component.setRotation(rotation);
		component.setNumElements(numElements);

		// Check the values were sucessfully set.
		assertTrue(Arrays.equals(position, component.getPosition()));
		assertTrue(Arrays.equals(orientation, component.getOrientation()));
		assertTrue(rotation == component.getRotation());
		assertTrue(numElements == component.getNumElements());

		// We don't need to check with invalid parameters as the setters will
		// throw InvalidParameterExceptions.

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
		GeometricalComponent component = new GeometricalComponent();
		component.setName("Tyrion Lannister");
		component.setDescription("Shrewd, educated, and calculating");
		component.setId(7);
		double[] position = new double[3], orientation = new double[3];
		for (int i = 0; i < 3; i++) {
			position[i] = 17.44 * i + 9.881;
			orientation[i] = 2.2 * i + 2.1893;
		}
		double rotation = 172;
		int numElements = 8;
		component.setPosition(position);
		component.setOrientation(orientation);
		component.setRotation(rotation);
		component.setNumElements(numElements);

		// Construct a component equal to the first.
		GeometricalComponent equalComponent = new GeometricalComponent();
		equalComponent.setName("Tyrion Lannister");
		equalComponent.setDescription("Shrewd, educated, and calculating");
		equalComponent.setId(7);
		equalComponent.setPosition(position);
		equalComponent.setOrientation(orientation);
		equalComponent.setRotation(rotation);
		equalComponent.setNumElements(numElements);

		// Construct a component not equal to the first.
		GeometricalComponent unequalComponent = new GeometricalComponent();
		unequalComponent.setName("Jaime Lannister");
		unequalComponent
				.setDescription("A born warrior, but confused about the boundaries of familial relations");
		unequalComponent.setId(40);
		for (int i = 0; i < 3; i++) {
			position[i] = 47.61 * i + 10.4;
			orientation[i] = 78.1 * i + 66.11;
		}
		rotation = 78.4444;
		numElements = 10;
		unequalComponent.setPosition(position);
		unequalComponent.setOrientation(orientation);
		unequalComponent.setRotation(rotation);
		unequalComponent.setNumElements(numElements);

		// Check that component and unequalComponet are not the same.
		assertFalse(component.equals(unequalComponent));
		assertFalse(unequalComponent.equals(component));

		// Check that equality also fails with illegal values
		assertFalse(component==null);
		assertFalse(component.equals(11));
		assertFalse("House Lannister".equals(component));

		// Check is equals() is reflexive and symmetric.
		assertTrue(component.equals(component));
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Construct a component equal to the first, for testing transitivity.
		GeometricalComponent transComponent = new GeometricalComponent();
		transComponent.setName("Tyrion Lannister");
		transComponent.setDescription("Shrewd, educated, and calculating");
		transComponent.setId(7);
		for (int i = 0; i < 3; i++) {
			position[i] = 17.44 * i + 9.881;
			orientation[i] = 2.2 * i + 2.1893;
		}
		rotation = 172;
		numElements = 8;
		transComponent.setPosition(position);
		transComponent.setOrientation(orientation);
		transComponent.setRotation(rotation);
		transComponent.setNumElements(numElements);

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

		// Construct a base component to copy from.
		GeometricalComponent component = new GeometricalComponent();
		component.setName("Cersei Lannister");
		component.setDescription("The Queen Regent of the Seven Kingdoms");
		component.setId(39);
		double[] position = new double[3], orientation = new double[3];
		for (int i = 0; i < 3; i++) {
			position[i] = Math.random() * (i + 100);
			orientation[i] = Math.random() * (i + 100);
		}
		double rotation = 172;
		int numElements = 9;
		component.setPosition(position);
		component.setOrientation(orientation);
		component.setRotation(rotation);
		component.setNumElements(numElements);

		/* ---- Check copying ---- */

		// Construct an empty component to copy to.
		GeometricalComponent componentCopy = new GeometricalComponent();

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

		// Make sure the object is an instance of GeometricalComponent.
		assertTrue(objectClone instanceof GeometricalComponent);

		// Cast the component.
		GeometricalComponent componentClone = (GeometricalComponent) component
				.clone();

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
	@Test
	public void checkPersistence() {
		// begin-user-code

		// Create a component for XML writing.
		GeometricalComponent writeComponent = new GeometricalComponent();
		writeComponent.setName("Tywin Lannister");
		writeComponent.setDescription("Lord of Casterly Rock");
		writeComponent.setId(98);
		double[] position = new double[3], orientation = new double[3];
		for (int i = 0; i < 3; i++) {
			position[i] = Math.random() * (i + 100);
			orientation[i] = Math.random() * (i + 100);
		}
		double rotation = 71;
		int numElements = 12;
		writeComponent.setPosition(position);
		writeComponent.setOrientation(orientation);
		writeComponent.setRotation(rotation);
		writeComponent.setNumElements(numElements);

		/* ---- Check reading/loading with valid values ---- */

		// Create an output stream and persist the component to XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeComponent.persistToXML(outputStream);
		// writeComponent.persistToXML(System.out); // Debugging

		// Create an input stream and feed the output stream into it.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Create a component for XML reading.
		GeometricalComponent loadComponent = new GeometricalComponent();

		// Load the inputStream into the component.
		loadComponent.loadFromXML(inputStream);

		// Compare the two components, they should be the same.
		assertTrue(writeComponent.equals(loadComponent));

		/* ---- Check reading/loading with invalid values ---- */

		// Try to load with an invalid stream.
		loadComponent.loadFromXML(null);

		// Check that the component remains unchanged.
		assertTrue(writeComponent.equals(loadComponent));

		// Try to write to an invalid stream.
		outputStream = null;
		loadComponent.persistToXML(outputStream);

		// Check that the output stream remains unchanged (is null).
		assertNull(outputStream);

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

		// Create a new component to visit.
		GeometricalComponent component = new GeometricalComponent();

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
		// end-user-code
	}

	/**
	 * IUpdateableListeners should be notified when the geometric properties
	 * change to new values.
	 */
	@Test
	public void checkListeners() {
		// begin-user-code

		// Create a GeometricalComponent for testing.
		GeometricalComponent component = new GeometricalComponent();

		// Add a listener for testing notifications when geometric properties
		// change.
		TestListener listener = new TestListener();
		component.register(listener);

		double[] tmp = new double[] { 1, 2, 3 };

		// ---- Check position notifications. ---- //
		// Setting a new position should notify the listener.
		component.setPosition(tmp);
		assertTrue(listener.wasNotified());

		// Setting the same position should not notify the listener.
		component.setPosition(tmp);
		assertFalse(listener.wasNotified());

		// ---- Check orientation notifications. ---- //
		// Setting a new orientation should notify the listener.
		component.setOrientation(tmp);
		assertTrue(listener.wasNotified());

		// Setting the same orientation should not notify the listener.
		component.setOrientation(tmp);
		assertFalse(listener.wasNotified());

		// ---- Check rotation notifications. ---- //
		// Setting a new rotation should notify the listener.
		component.setRotation(789);
		assertTrue(listener.wasNotified());

		// Setting the same rotation should not notify the listener.
		component.setRotation(789);
		assertFalse(listener.wasNotified());

		// ---- Check numElements notifications. ---- //
		// Setting a new numElements should notify the listener.
		component.setNumElements(51);
		assertTrue(listener.wasNotified());

		// Setting the same numElements should not notify the listener.
		component.setNumElements(51);
		assertFalse(listener.wasNotified());

		return;
		// end-user-code
	}

	/**
	 * This listener is a test IUpdateableListener. wasNotifed() can be used as
	 * a way to determine if it has been notified within 250 ms.
	 * 
	 * @author djg
	 * 
	 */
	private class TestListener implements IUpdateableListener {

		private final AtomicBoolean notified = new AtomicBoolean(false);

		/**
		 * Whether or not the listener was notified. Expires after 250ms. It
		 * returns within 10ms of being notified.
		 * 
		 * @return True if the listener was notified within 250ms, false
		 *         otherwise.
		 */
		public boolean wasNotified() {

			int counter = 0;

			while (!notified.get() && ++counter < 25) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					fail("JunctionTester error: "
							+ "Thread interrupted during IJunctionListener test.");
				}
			}

			return notified.getAndSet(false);
		}

		public void update(IUpdateable component) {
			notified.set(true);
		}
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
	private class FakeComponentVisitor implements IPlantComponentVisitor {

		// The fake visitor's visited component.
		private GeometricalComponent component = null;

		public void visit(PlantComposite plantComp) {
			// Do nothing.
		}

		public void visit(GeometricalComponent plantComp) {

			// Set the IPlantComponentVisitor component (if valid), and flag the
			// component as having been visited.
			if (plantComp != null) {
				this.component = plantComp;
				wasVisited = true;
			}
			return;
		}

		public void visit(Junction plantComp) {
			// Do nothing.

		}

		public void visit(Reactor plantComp) {
			// Do nothing.

		}

		public void visit(PointKinetics plantComp) {
			// Do nothing.

		}

		public void visit(HeatExchanger plantComp) {
			// Do nothing.

		}

		public void visit(Pipe plantComp) {
			// Do nothing.

		}

		public void visit(CoreChannel plantComp) {
			// Do nothing.

		}

		public void visit(Subchannel plantComp) {
			// Do nothing.

		}

		public void visit(PipeWithHeatStructure plantComp) {
			// Do nothing.

		}

		public void visit(Branch plantComp) {
			// Do nothing.

		}

		public void visit(SubchannelBranch plantComp) {
			// Do nothing.

		}

		public void visit(VolumeBranch plantComp) {
			// Do nothing.

		}

		public void visit(FlowJunction plantComp) {
			// Do nothing.

		}

		public void visit(WetWell plantComp) {
			// Do nothing.

		}

		public void visit(Boundary plantComp) {
			// Do nothing.

		}

		public void visit(OneInOneOutJunction plantComp) {
			// Do nothing.

		}

		public void visit(Turbine plantComp) {
			// Do nothing.

		}

		public void visit(IdealPump plantComp) {
			// Do nothing.

		}

		public void visit(Pump plantComp) {
			// Do nothing.

		}

		public void visit(Valve plantComp) {
			// Do nothing.

		}

		public void visit(PipeToPipeJunction plantComp) {
			// Do nothing.

		}

		public void visit(Inlet plantComp) {
			// Do nothing.

		}

		public void visit(MassFlowInlet plantComp) {
			// Do nothing.

		}

		public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
			// Do nothing.

		}

		public void visit(Outlet plantComp) {
			// Do nothing.

		}

		public void visit(SolidWall plantComp) {
			// Do nothing.

		}

		public void visit(TDM plantComp) {
			// Do nothing.

		}

		public void visit(TimeDependentJunction plantComp) {
			// Do nothing.

		}

		public void visit(TimeDependentVolume plantComp) {
			// Do nothing.

		}

		public void visit(DownComer plantComp) {
			// Do nothing.

		}

		public void visit(SeparatorDryer plantComp) {
			// Do nothing.

		}

	};
}