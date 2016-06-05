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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.reactor.plant.FlowJunction;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.IReactorComponent;
import org.eclipse.january.form.SelectiveComponentVisitor;
import org.junit.Test;

/**
 * 
 * @author Anna Wojtowicz
 */
public class FlowJunctionTester {
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
		FlowJunction junction = new FlowJunction();
		assertNotNull(junction.getInputs());
		assertNotNull(junction.getOutputs());
		assertTrue(junction.getInputs().isEmpty());
		assertTrue(junction.getOutputs().isEmpty());
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

		// Create a FlowJunction with inputs and outputs, note we are
		// ignoring the connections as it's unclear how they should work
		FlowJunction junction = new FlowJunction();
		assertTrue(junction.getInputs().isEmpty());
		assertTrue(junction.getOutputs().isEmpty());

		// Set the new input and outputs
		junction.setInputs(inputs);
		junction.setOutputs(outputs);

		// Assert that the lists were set correctly
		assertTrue(junction.getInputs().equals(inputs));
		assertTrue(junction.getOutputs().equals(outputs));

	}

	/**
	 * <p>
	 * Checks the hashCode and equality methods of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {
		// Create two equal FlowJunctions with
		// nullary constructor
		FlowJunction j = new FlowJunction();
		j.setName("j");
		j.setDescription("Description");

		FlowJunction equalJ = new FlowJunction();
		equalJ.setName("j");
		equalJ.setDescription("Description");

		// Check reflexivity
		assertTrue(j.equals(j));

		// Check equality and symmetry
		assertTrue(j.equals(equalJ));
		assertTrue(equalJ.equals(j));

		// Since equal, hashcodes should be equal
		assertTrue(j.hashCode() == equalJ.hashCode());
		assertTrue(j.hashCode() == j.hashCode());

		// Make them unequal
		j.setName("j_newName");

		// Check inequality and symmetry
		assertFalse(j.equals(equalJ));
		assertFalse(equalJ.equals(j));
	}

	/**
	 * <p>
	 * Checks the copy and clone methods of the component.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {
		// Create a FlowJunction to copy
		FlowJunction junction = new FlowJunction();
		junction.setName("Name");
		junction.setDescription("desc");
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
		junction.setInputs(inputs);
		junction.setOutputs(outputs);

		// Create a copy FlowJunction
		FlowJunction copy = new FlowJunction();

		// Assert they are not the same to begin with
		assertFalse(copy.equals(junction));

		// Copy junction
		copy.copy(junction);

		// Check the copy succeeded
		assertTrue("Name".equals(copy.getName()));
		assertTrue("desc".equals(copy.getDescription()));
		assertFalse(copy == junction);
		assertTrue(copy.getInputs().equals(inputs));
		assertTrue(copy.getOutputs().equals(outputs));

		// Get a clone to verify the clone() method
		Object clone = junction.clone();

		assertNotNull(clone);
		assertFalse(clone == junction);
		assertTrue(clone.equals(junction));

		assertTrue(clone instanceof FlowJunction);

		FlowJunction casted = (FlowJunction) clone;

		assertFalse(casted == junction);
		assertTrue(casted.equals(junction));

	}

	/**
	 * <p>
	 * Checks for persistence in the component.
	 * </p>
	 * 
	 */
	public void checkPersistence() {

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
		FlowJunction component = new FlowJunction();

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

		// Check that the visitor's component is
		// the same component we initially created.
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
		public void visit(FlowJunction plantComp) {
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