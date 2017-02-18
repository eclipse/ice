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

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
import org.eclipse.ice.reactor.plant.WetWell;
import org.junit.Test;

/**
 * 
 * @author Anna Wojtowicz
 */
public class WetWellTester {
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

	}

	/**
	 * <p>
	 * Checks the getting and setting of the component's attributes.
	 * </p>
	 * 
	 */
	@Test
	public void checkProperties() {
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

	}

	/**
	 * <p>
	 * Checks the hashCode and equality methods of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {
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

	}

	/**
	 * <p>
	 * Checks the copy and clone methods of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {
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
	}

	/**
	 * <p>
	 * Checks for persistence in the component.
	 * </p>
	 * 
	 */
	public void checkPersistence() {
		// TODO Auto-generated method stub

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
		WetWell component = new WetWell();

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
		public void visit(WetWell plantComp) {
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