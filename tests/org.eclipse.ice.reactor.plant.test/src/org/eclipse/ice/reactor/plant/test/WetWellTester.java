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

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
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
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.WetWell;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class WetWellTester {
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
		WetWell well = new WetWell();
		assertEquals(0.0, well.getHeight(), 1e-6);
		assertEquals(0.0, well.getzIn(), 1e-6);
		assertEquals(0.0, well.getzOut(), 1e-6);
		assertNotNull(well.getInputs());
		assertNotNull(well.getOutputs());
		assertTrue(well.getInputs().isEmpty());
		assertTrue(well.getOutputs().isEmpty());

		well = new WetWell(1.0);
		assertEquals(1.0, well.getHeight(), 1e-6);
		assertEquals(0.0, well.getzIn(), 1e-6);
		assertEquals(0.0, well.getzOut(), 1e-6);

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
		// Create some inputs and outputs
		PlantComponent input1 = new Pipe();
		input1.setName("input1");
		PlantComponent input2 = new Pipe();
		input2.setName("input2");
		PlantComponent output1 = new Pipe();
		output1.setName("output1");
		PlantComponent output2 = new HeatExchanger();
		output2.setName("output2");
		ArrayList<PlantComponent> inputs = new ArrayList<PlantComponent>();
		ArrayList<PlantComponent> outputs = new ArrayList<PlantComponent>();
		inputs.add(input1);
		inputs.add(input2);
		outputs.add(output1);
		outputs.add(output2);

		// Create a WetWell with inputs and outputs, note we are
		// ignoring the connections as it's unclear how they should work
		WetWell well = new WetWell(1.0);
		assertTrue(well.getInputs().isEmpty());
		assertTrue(well.getOutputs().isEmpty());
		assertEquals(1.0, well.getHeight(), 1e-6);

		// Set the new input and outputs
		well.setInputs(inputs);
		well.setOutputs(outputs);

		// Assert that the lists were set correctly
		assertTrue(well.getInputs().equals(inputs));
		assertTrue(well.getOutputs().equals(outputs));

		well.setzIn(33.3);
		well.setzOut(23.3);

		assertEquals(33.3, well.getzIn(), 1e-6);
		assertEquals(23.3, well.getzOut(), 1e-6);

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
		WetWell w1 = new WetWell();
		w1.setName("w1");
		w1.setDescription("Description");
		w1.setHeight(22.2);
		w1.setzIn(1.0);
		w1.setzOut(3.0);

		WetWell equalW1 = new WetWell();
		equalW1.setName("w1");
		equalW1.setDescription("Description");
		equalW1.setHeight(22.2);
		equalW1.setzIn(1.0);
		equalW1.setzOut(3.0);

		// Check reflexivity
		assertTrue(w1.equals(w1));

		// Check equality and symmetry
		assertTrue(w1.equals(equalW1));
		assertTrue(equalW1.equals(w1));

		// Since equal, hashcodes should be equal
		assertTrue(w1.hashCode() == equalW1.hashCode());
		assertTrue(w1.hashCode() == w1.hashCode());

		// Make them unequal
		w1.setHeight(2.0);

		// Check inequality and symmetry
		assertFalse(w1.equals(equalW1));
		assertFalse(equalW1.equals(w1));

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
		WetWell w1 = new WetWell();
		w1.setName("w1");
		w1.setDescription("Description");
		w1.setHeight(22.2);
		w1.setzIn(1.0);
		w1.setzOut(3.0);

		WetWell copy = new WetWell();

		copy.copy(w1);

		// Check the copy succeeded
		assertTrue("w1".equals(copy.getName()));
		assertTrue("Description".equals(copy.getDescription()));
		assertFalse(copy == w1);
		assertEquals(copy.getHeight(), w1.getHeight(), 1e-12);
		assertEquals(copy.getzIn(), w1.getzIn(), 1e-12);
		assertEquals(copy.getzOut(), w1.getzOut(), 1e-12);

		// Get a clone to verify the clone() method
		Object clone = w1.clone();

		assertNotNull(clone);
		assertFalse(clone == w1);
		assertTrue(clone.equals(w1));

		assertTrue(clone instanceof WetWell);

		WetWell casted = (WetWell) clone;

		assertFalse(casted == w1);
		assertTrue(casted.equals(w1));
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
	public void checkPersistence() {
		// begin-user-code
		// TODO Auto-generated method stub

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
	public void checkVisitation() {
		// begin-user-code
		// Create a new component to visit.
		WetWell well = new WetWell();

		// Create an invalid visitor, and try to visit the component.
		FakeComponentVisitor visitor = null;
		well.accept(visitor);

		// Check that the component wasn't visited yet.
		assertFalse(wasVisited);

		// Create a valid visitor, and try to visit the component.
		visitor = new FakeComponentVisitor();
		well.accept(visitor);

		// Check that the component was visited.
		assertTrue(wasVisited);

		// Grab the visitor's visited component.
		Component visitorComponent = visitor.component;

		// Check that the visitor's component is the same component we initially
		// created.
		assertTrue(well == visitorComponent);
		assertTrue(well.equals(visitorComponent));

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
	private class FakeComponentVisitor implements IComponentVisitor {

		// The fake visitor's visited component.
		private IReactorComponent component = null;

		public void visit(IReactorComponent component) {

			// Set the IComponentVisitor component (if valid), and flag the
			// component as having been visited.
			if (component != null) {
				this.component = component;
				wasVisited = true;
			}
			return;
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

		public void visit(MeshComponent component) {
		}

		public void visit(BatteryComponent component) {
		}

		public void visit(AdaptiveTreeComposite component) {
		}
	};
}