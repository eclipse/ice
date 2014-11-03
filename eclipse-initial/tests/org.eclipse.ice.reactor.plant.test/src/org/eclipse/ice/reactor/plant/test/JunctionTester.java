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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
import org.eclipse.ice.reactor.plant.IJunction;
import org.eclipse.ice.reactor.plant.IJunctionListener;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class JunctionTester {
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

		// Create a Junction using the nullary constructor and assert that it
		// initializes a non-null and empty list of inputs, outputs, and
		// connections.
		Junction junction = new Junction();
		assertNotNull(junction.getInputs());
		assertNotNull(junction.getOutputs());
		assertTrue(junction.getInputs().isEmpty());
		assertTrue(junction.getOutputs().isEmpty());

		// Create a Junction with the parameterized constructor but with bad
		// arguments. The inputs and outputs should be empty lists like the
		// nullary constructor.
		junction = new Junction(null, null, null);
		assertNotNull(junction.getInputs());
		assertNotNull(junction.getOutputs());
		assertTrue(junction.getInputs().isEmpty());
		assertTrue(junction.getOutputs().isEmpty());

		// Create some inputs and outputs.
		Pipe input1 = new Pipe();
		Pipe input2 = new Pipe();
		Pipe output1 = new Pipe();
		HeatExchanger output2 = new HeatExchanger();

		ArrayList<PlantComponent> inputs = new ArrayList<PlantComponent>();
		ArrayList<PlantComponent> outputs = new ArrayList<PlantComponent>();
		inputs.add(input1);
		inputs.add(input2);
		outputs.add(output1);
		outputs.add(output2);

		// Create a Junction with inputs and outputs, note we are
		// ignoring the connections as it's unclear how they should work
		try {
			junction = new Junction(inputs, outputs, null);
		} catch (IllegalArgumentException ex) {
			fail();
		}
		assertNotNull(junction.getInputs());
		assertNotNull(junction.getOutputs());
		assertEquals(2, junction.getInputs().size());
		assertEquals(2, junction.getOutputs().size());

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

		// Create a Junction with inputs and outputs, note we are
		// ignoring the connections as it's unclear how they should work
		Junction junction = new Junction();
		assertTrue(junction.getInputs().isEmpty());
		assertTrue(junction.getOutputs().isEmpty());

		// Set the new input and outputs
		junction.setInputs(inputs);
		junction.setOutputs(outputs);

		// Assert that the lists were set correctly
		assertTrue(junction.getInputs().equals(inputs));
		assertTrue(junction.getOutputs().equals(outputs));

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
		// Create two equal Junctions with
		// nullary constructor
		Junction j1 = new Junction();
		j1.setName("j1");
		j1.setDescription("Description");

		Junction equalJ1 = new Junction();
		equalJ1.setName("j1");
		equalJ1.setDescription("Description");

		// Check reflexivity
		assertTrue(j1.equals(j1));

		// Check equality and symmetry
		assertTrue(j1.equals(equalJ1));
		assertTrue(equalJ1.equals(j1));

		// Since equal, hashcodes should be equal
		assertTrue(j1.hashCode() == equalJ1.hashCode());
		assertTrue(j1.hashCode() == j1.hashCode());

		// Make them unequal
		j1.setName("j1_newName");

		// Check inequality and symmetry
		assertFalse(j1.equals(equalJ1));
		assertFalse(equalJ1.equals(j1));

		// Now check equality with other constructor
		// Create some inputs and outputs
		PlantComponent input1 = new Pipe();
		input1.setName("input1");
		PlantComponent input2 = new Pipe();
		input2.setName("input2");
		PlantComponent output1 = new Pipe();
		output1.setName("output1");
		PlantComponent output2 = new HeatExchanger();
		output2.setName("output2");
		PlantComponent equalInput1 = new Pipe();
		equalInput1.setName("input1");
		PlantComponent equalInput2 = new Pipe();
		equalInput2.setName("input2");
		PlantComponent equalOutput1 = new Pipe();
		equalOutput1.setName("output1");
		PlantComponent equalOutput2 = new HeatExchanger();
		equalOutput2.setName("output2");

		ArrayList<PlantComponent> inputs = new ArrayList<PlantComponent>();
		ArrayList<PlantComponent> outputs = new ArrayList<PlantComponent>();
		ArrayList<PlantComponent> equalInputs = new ArrayList<PlantComponent>();
		ArrayList<PlantComponent> equalOutputs = new ArrayList<PlantComponent>();
		inputs.add(input1);
		inputs.add(input2);
		outputs.add(output1);
		outputs.add(output2);
		equalInputs.add(equalInput1);
		equalInputs.add(equalInput2);
		equalOutputs.add(equalOutput1);
		equalOutputs.add(equalOutput2);

		// Create two equal junctions
		Junction junction = new Junction(inputs, outputs, null);
		Junction equalJunction = new Junction(equalInputs, equalOutputs, null);

		// Check reflexivity
		assertTrue(junction.equals(junction));

		// Check equality and symmetry
		assertTrue(junction.equals(equalJunction));
		assertTrue(equalJunction.equals(junction));

		// Make them unequal
		inputs.get(0).setName("NewName");

		assertFalse(junction.equals(equalJunction));

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

		// Create a Junction to copy
		Junction junction = new Junction();
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

		// Create a copy junction
		Junction copy = new Junction();

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

		assertTrue(clone instanceof Junction);

		Junction casted = (Junction) clone;

		assertFalse(casted == junction);
		assertTrue(casted.equals(junction));

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
	@Test
	public void checkVisitation() {
		// begin-user-code
		// Create a new component to visit.
		Junction junction = new Junction();

		// Create an invalid visitor, and try to visit the component.
		FakeComponentVisitor visitor = null;
		junction.accept(visitor);

		// Check that the component wasn't visited yet.
		assertFalse(wasVisited);

		// Create a valid visitor, and try to visit the component.
		visitor = new FakeComponentVisitor();
		junction.accept(visitor);

		// Check that the component was visited.
		assertTrue(wasVisited);

		// Grab the visitor's visited component.
		Component visitorComponent = visitor.component;

		// Check that the visitor's component is the same component we initially
		// created.
		assertTrue(junction == visitorComponent);
		assertTrue(junction.equals(visitorComponent));

		return;
		// end-user-code
	}

	/**
	 * Checks the methods implemented from the IJunction interface.
	 */
	@Test
	public void checkJunctionImplementation() {
		// begin-user-code

		Pipe pipe;

		// Create a junction for testing.
		Junction junction = new Junction();

		// ---- Add an input pipe. ---- //
		pipe = new Pipe();
		pipe.setId(1);
		junction.addInput(pipe);

		assertEquals(1, junction.getInputs().size());
		assertEquals(0, junction.getOutputs().size());
		assertSame(pipe, junction.getInputs().get(0));

		// ---- isInput() should return true. ---- //
		assertTrue(junction.isInput(pipe));

		// ---- Remove an input pipe. ---- //
		junction.removeInput(pipe);

		assertEquals(0, junction.getInputs().size());
		assertEquals(0, junction.getOutputs().size());

		// ---- Add an output pipe. ---- //
		junction.addOutput(pipe);

		assertEquals(0, junction.getInputs().size());
		assertEquals(1, junction.getOutputs().size());
		assertSame(pipe, junction.getOutputs().get(0));

		// ---- isInput() should return false. ---- //
		assertFalse(junction.isInput(pipe));

		// ---- Remove an output pipe. ---- //
		junction.removeOutput(pipe);

		assertEquals(0, junction.getInputs().size());
		assertEquals(0, junction.getOutputs().size());

		// ---- Try invalid pipes. ---- //
		junction.addInput(null);
		assertTrue(junction.getInputs().isEmpty());
		junction.removeOutput(pipe);
		assertTrue(junction.getOutputs().isEmpty());
		assertFalse(junction.isInput(new PlantComponent()));

		return;
		// end-user-code
	}

	/**
	 * Checks the correct notification of registered IJunctionListeners.
	 */
	@Test
	public void checkJunctionListeners() {
		// begin-user-code

		Pipe pipe;
		List<PlantComponent> pipes = new ArrayList<PlantComponent>();

		// Create a junction for testing.
		Junction junction = new Junction();

		// Create lists that the listener will update when pipes are added or
		// removed when notified by the junction.
		List<PlantComponent> added, removed;

		// Create the listener. For pipe adds, it will add to the added pipe
		// list. For pipe removes, it will add to the removed pipe list.
		TestJunctionListener listener = new TestJunctionListener();
		junction.registerJunctionListener(listener);

		// ---- Add an input pipe. ---- //
		pipe = new Pipe();
		pipe.setId(1);
		junction.addInput(pipe);

		assertTrue(listener.wasNotified());
		added = listener.getAddedPipes();
		assertEquals(1, added.size());
		assertSame(pipe, added.get(0));

		// ---- Remove an input pipe. ---- //
		listener.reset();

		junction.removeInput(pipe);

		assertTrue(listener.wasNotified());
		removed = listener.getRemovedPipes();
		assertEquals(1, removed.size());
		assertSame(pipe, removed.get(0));

		// ---- Add an output pipe. ---- //
		listener.reset();

		pipe = new Pipe();
		pipe.setId(2);
		junction.addOutput(pipe);

		assertTrue(listener.wasNotified());
		added = listener.getAddedPipes();
		assertEquals(1, added.size());
		assertSame(pipe, added.get(0));

		// ---- Remove an input pipe. ---- //
		listener.reset();

		junction.removeOutput(pipe);

		assertTrue(listener.wasNotified());
		removed = listener.getRemovedPipes();
		assertEquals(1, removed.size());
		assertSame(pipe, removed.get(0));

		// ---- Set Input Pipes. ---- //
		listener.reset();

		pipes.add(pipe);
		pipes.add(new Pipe());
		junction.setInputs((ArrayList<PlantComponent>) pipes);

		assertTrue(listener.wasNotified());
		added = listener.getAddedPipes();
		assertEquals(2, added.size());
		assertSame(pipes.get(0), added.get(0));
		assertSame(pipes.get(1), added.get(1));
		removed = listener.getRemovedPipes();
		assertEquals(0, removed.size());

		// ---- Set Output Pipes. ---- //
		listener.reset();

		junction.setOutputs((ArrayList<PlantComponent>) pipes);

		assertTrue(listener.wasNotified());
		added = listener.getAddedPipes();
		assertEquals(2, added.size());
		assertSame(pipes.get(0), added.get(0));
		assertSame(pipes.get(1), added.get(1));
		removed = listener.getRemovedPipes();
		assertEquals(0, removed.size());

		// ---- Unregister the listener. ---- //
		listener.reset();

		junction.unregisterJunctionListener(listener);

		// Adding a new pipe should not notify the listener.
		pipe = new Pipe();
		pipe.setId(3);
		junction.addInput(pipe);

		assertFalse(listener.wasNotified());

		return;
		// end-user-code
	}

	/**
	 * This listener is a test IJunctionListener. It should be reset between
	 * tests, and wasNotifed() can be used as a way to determine if it has been
	 * notified within 250ms.
	 * 
	 * @author djg
	 * 
	 */
	private class TestJunctionListener implements IJunctionListener {

		private List<PlantComponent> added = new ArrayList<PlantComponent>();
		private List<PlantComponent> removed = new ArrayList<PlantComponent>();

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

			while (!notified.get() && ++counter < 250) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					fail("JunctionTester error: "
							+ "Thread interrupted during IJunctionListener test.");
				}
			}

			return notified.get();
		}

		public void reset() {
			added.clear();
			removed.clear();
			notified.set(false);
		}

		public List<PlantComponent> getAddedPipes() {
			return added;
		}

		public List<PlantComponent> getRemovedPipes() {
			return removed;
		}

		public void addedPipes(IJunction junction, List<PlantComponent> pipes) {
			added = pipes;
			notified.set(true);
		}

		public void removedPipes(IJunction junction, List<PlantComponent> pipes) {
			removed = pipes;
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