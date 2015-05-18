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
package org.eclipse.ice.reactor.plant;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

/**
 * <p>
 * Base class for junction-like components that connect objects, such as pipes.
 * The class keeps track of what is an input and what is an output.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class Junction extends PlantComponent implements IJunction {

	/**
	 * <p>
	 * The inputs of this junction.
	 * </p>
	 * 
	 */
	protected final ArrayList<PlantComponent> inputs;

	/**
	 * <p>
	 * Outputs of this junction.
	 * </p>
	 * 
	 */
	protected final ArrayList<PlantComponent> outputs;

	/**
	 * <p>
	 * A list of coupled input-output pairs representing the connections of the
	 * junction. Input-output pairs are based on component name.
	 * </p>
	 * 
	 */
	protected ArrayList<String> connections;

	/**
	 * A list of IJunctionListeners that are notified when input or output is
	 * added or removed from this Junction.
	 */
	private final List<IJunctionListener> listeners;

	/**
	 * This visitor adds supported PlantComponents to {@link #inputs}.
	 */
	private final Visitor addInputVisitor = new Visitor() {
		public void visit(Pipe plantComp) {
			boolean found = false;

			// Make sure the component is not already in the list before adding.
			int size = inputs.size();
			for (int i = 0; !found && i < size; i++) {
				found = (plantComp == inputs.get(i));
			}
			if (!found) {
				inputs.add(plantComp);
			}
		}

		public void visit(HeatExchanger plantComp) {
			boolean found = false;

			// Make sure the component is not already in the list before adding.
			int size = inputs.size();
			for (int i = 0; !found && i < size; i++) {
				found = (plantComp == inputs.get(i));
			}
			if (!found) {
				inputs.add(plantComp);
			}
		}
	};
	/**
	 * This visitor adds supported PlantComponents to {@link #outputs}.
	 */
	private final Visitor addOutputVisitor = new Visitor() {
		public void visit(Pipe plantComp) {
			boolean found = false;

			// Make sure the component is not already in the list before adding.
			int size = outputs.size();
			for (int i = 0; !found && i < size; i++) {
				found = (plantComp == outputs.get(i));
			}
			if (!found) {
				outputs.add(plantComp);
			}
		}

		public void visit(HeatExchanger plantComp) {
			boolean found = false;

			// Make sure the component is not already in the list before adding.
			int size = outputs.size();
			for (int i = 0; !found && i < size; i++) {
				found = (plantComp == outputs.get(i));
			}
			if (!found) {
				outputs.add(plantComp);
			}
		}
	};

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public Junction() {
		super();

		// Initialize the input and output lists.
		inputs = new ArrayList<PlantComponent>();
		outputs = new ArrayList<PlantComponent>();

		// Initialize the list of listeners.
		listeners = new ArrayList<IJunctionListener>();

	}

	/**
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * 
	 * @param inputs
	 *            <p>
	 *            Inputs of this junction.
	 *            </p>
	 * @param outputs
	 *            <p>
	 *            Outputs of this junction.
	 *            </p>
	 * @param connections
	 *            <p>
	 *            A list of coupled input-output pairs representing the
	 *            connections of the junction.
	 *            </p>
	 */
	public Junction(ArrayList<PlantComponent> ins,
			ArrayList<PlantComponent> outs, ArrayList<String> conns) {
		this();

		// If the ins list is not null, add all non-null components from the ins
		// list to the inputs list.
		if (ins != null) {
			// Add all non-null components from the list to the inputs.
			for (PlantComponent component : ins) {
				if (component != null) {
					component.accept(addInputVisitor);
				}
			}
		}

		// If the outs list is not null, add all non-null components from the
		// outs list to the outputs list.
		if (outs != null) {
			// Add all non-null components from the list to the inputs.
			for (PlantComponent component : ins) {
				if (component != null) {
					component.accept(addOutputVisitor);
				}
			}
		}

		connections = null;
	}

	/**
	 * @return the inputs
	 */
	public ArrayList<PlantComponent> getInputs() {
		return new ArrayList<PlantComponent>(inputs);
	}

	/**
	 * @param inputs
	 *            the inputs to set
	 */
	public void setInputs(ArrayList<PlantComponent> ins) {

		if (ins != null) {

			// Clear the list of inputs and notify IJunctionListeners that the
			// inputs have been removed.
			List<PlantComponent> removed = new ArrayList<PlantComponent>(inputs);
			inputs.clear();
			notifyJunctionListeners(removed, false);

			// Add all non-null components from the list to the inputs. Keep
			// track of the ones that are actually added.
			int size = ins.size();
			List<PlantComponent> added = new ArrayList<PlantComponent>(size);
			size = inputs.size();
			for (PlantComponent component : ins) {
				if (component != null) {
					component.accept(addInputVisitor);
					// If the size of the inputs list has changed, then the
					// component was successfully added.
					if (inputs.size() > size) {
						added.add(component);
						size++;
					}
				}
			}

			// Notify JunctionListeners of the new inputs.
			notifyJunctionListeners(added, true);

			// Notify IUpdateableListeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * @return the outputs
	 */
	public ArrayList<PlantComponent> getOutputs() {
		return new ArrayList<PlantComponent>(outputs);
	}

	/**
	 * @param outputs
	 *            the outputs to set
	 */
	public void setOutputs(ArrayList<PlantComponent> outs) {

		if (outs != null) {

			// Clear the list of outputs and notify IJunctionListeners that the
			// outputs have been removed.
			List<PlantComponent> removed = new ArrayList<PlantComponent>(
					outputs);
			outputs.clear();
			notifyJunctionListeners(removed, false);

			// Add all non-null components from the list to the outputs. Keep
			// track of the ones that are actually added.
			int size = outs.size();
			List<PlantComponent> added = new ArrayList<PlantComponent>(size);
			size = outputs.size();
			for (PlantComponent component : outs) {
				if (component != null) {
					component.accept(addOutputVisitor);
					// If the size of the outputs list has changed, then the
					// component was successfully added.
					if (outputs.size() > size) {
						added.add(component);
						size++;
					}
				}
			}

			// Notify JunctionListeners of the new outputs.
			notifyJunctionListeners(added, true);

			// Notify IUpdateableListeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * @return the connections
	 */
	public ArrayList<String> getConnections() {
		return connections;
	}

	/**
	 * @param connections
	 *            the connections to set
	 */
	public void setConnections(ArrayList<String> connections) {
		this.connections = connections;
	}

	/**
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {

		// By default, assume the objects are not equivalent.
		boolean equals = false;

		// If the references are the same, then the objects are equivalent.
		if (this == otherObject) {
			equals = true;
		}
		// If the other object is not null and the same type, then check all of
		// the class variables.
		else if (otherObject != null && otherObject instanceof Junction) {
			Junction otherJunction = (Junction) otherObject;

			equals = super.equals(otherJunction)
					&& inputs.equals(otherJunction.inputs)
					&& outputs.equals(otherJunction.outputs);
		}

		return equals;
	}

	/**
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 */
	public Object clone() {
		Junction temp = new Junction();
		temp.copy(this);
		return temp;
	}

	/**
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 */
	public void copy(Junction otherObject) {

		// Make sure other is not null
		if (otherObject == null) {
			return;
		}
		// Copy the PlantComponent and ICEObject data
		super.copy(otherObject);

		// Clear all inputs and outputs and notify IJunctionListeners.
		List<PlantComponent> components = new ArrayList<PlantComponent>(inputs);
		components.addAll(outputs);
		notifyJunctionListeners(components, false);
		inputs.clear();
		outputs.clear();

		// Copy the input and output data.
		inputs.addAll(otherObject.inputs);
		outputs.addAll(otherObject.outputs);

		// Notify IPlantComposite listeners of the added components.
		components = new ArrayList<PlantComponent>(inputs);
		components.addAll(outputs);
		notifyJunctionListeners(components, true);

		// Notify IUpdateableListeners of the change.
		notifyListeners();

		return;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 */
	public int hashCode() {
		int hash = super.hashCode();
		hash = 31 * hash + inputs.hashCode();
		hash = 31 * hash + outputs.hashCode();
		return hash;
	}

	/**
	 * <p>
	 * Accepts PlantComponentVisitors to reveal the type of a PlantComponent.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The PlantComponent's visitor.
	 *            </p>
	 */
	public void accept(IPlantComponentVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}

	// ---- Implements IJunction ---- //
	public void registerJunctionListener(IJunctionListener listener) {

		if (listener != null) {

			boolean found = false;

			// The list of listeners is usually small, so use a linear search.
			int size = listeners.size();
			for (int i = 0; !found && i < size; i++) {
				found = (listener == listeners.get(i));
			}
			// If the listener is not already in the list, add it.
			if (!found) {
				listeners.add(listener);
			}
		}
		return;
	}

	public void unregisterJunctionListener(IJunctionListener listener) {

		boolean found = false;

		// Loop over the list of listeners and remove the first matching
		// listener reference.
		int i, size = listeners.size();
		for (i = 0; !found && i < size; i++) {
			found = (listener == listeners.get(i));
		}
		// If the listener was found, remove it.
		if (found) {
			listeners.remove(i - 1);
		}
		return;
	}

	public void notifyJunctionListeners(final List<PlantComponent> components,
			final boolean added) {

		if (components != null && !components.isEmpty()) {
			// Create a thread to notify IJunctionListeners that pipes were
			// either added or removed.
			Thread notifierThread = new Thread() {
				@Override
				public void run() {
					if (added) {
						for (IJunctionListener listener : listeners) {
							listener.addedPipes(Junction.this, components);
						}
					} else {
						for (IJunctionListener listener : listeners) {
							listener.removedPipes(Junction.this, components);
						}
					}
				}
			};
			notifierThread.start();
		}

		return;
	}

	public boolean isInput(PlantComponent component) {

		boolean found = false;

		// The list shouldn't be too big, so a linear search is okay for now.
		int size = inputs.size();
		for (int i = 0; !found && i < size; i++) {
			found = (component == inputs.get(i));
		}
		return found;
	}

	public void addInput(PlantComponent input) {

		if (input != null) {
			int size = inputs.size();

			// Use the visitor to add the input component if possible.
			input.accept(addInputVisitor);

			// Notify listeners if the add was successful.
			if (inputs.size() > size) {
				// Notify IJunctionListeners that a component was added.
				List<PlantComponent> added = new ArrayList<PlantComponent>(1);
				added.add(input);
				notifyJunctionListeners(added, true);

				// Notify IUpdateableListeners.
				notifyListeners();
			}
		}

		return;
	}

	public void removeInput(PlantComponent input) {

		if (input != null) {

			// Search through the list to find a matching component.
			boolean found = false;
			Iterator<PlantComponent> iterator = inputs.iterator();
			while (!found && iterator.hasNext()) {
				found = (iterator.next() == input);
			}

			// Notify listeners if the remove was successful.
			if (found) {
				// Remove the component.
				iterator.remove();

				// Notify IJunctionListeners that a component was removed.
				List<PlantComponent> removed = new ArrayList<PlantComponent>(1);
				removed.add(input);
				notifyJunctionListeners(removed, false);

				// Notify IUpdateableListeners.
				notifyListeners();
			}
		}

		return;
	}

	public void addOutput(PlantComponent output) {

		if (output != null) {
			int size = outputs.size();

			// Use the visitor to add the input component if possible.
			output.accept(addOutputVisitor);

			// Notify listeners if the add was successful.
			if (outputs.size() > size) {
				// Notify IJunctionListeners that a component was added.
				List<PlantComponent> added = new ArrayList<PlantComponent>(1);
				added.add(output);
				notifyJunctionListeners(added, true);

				// Notify IUpdateableListeners.
				notifyListeners();
			}
		}

		return;
	}

	public void removeOutput(PlantComponent output) {

		if (output != null) {

			// Search through the list to find a matching component.
			boolean found = false;
			Iterator<PlantComponent> iterator = outputs.iterator();
			while (!found && iterator.hasNext()) {
				found = (iterator.next() == output);
			}

			// Notify listeners if the remove was successful.
			if (found) {
				// Remove the component.
				iterator.remove();

				// Notify IJunctionListeners that a component was removed.
				List<PlantComponent> removed = new ArrayList<PlantComponent>(1);
				removed.add(output);
				notifyJunctionListeners(removed, false);

				// Notify IUpdateableListeners.
				notifyListeners();
			}
		}

		return;
	}

	// ------------------------------ //

	/**
	 * This class provides an IPlantComponentVisitor with the operations that
	 * are unnecessary for this class implemented as doing nothing. Useful visit
	 * operations are left as abstract.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private abstract class Visitor implements IPlantComponentVisitor {

		// When a Pipe is added, the Junction should simply store it as a
		// PlantComponent.
		public abstract void visit(Pipe plantComp);

		// When a HeatExchanger is added, the Junction should store it as a
		// PlantComponent. If connecting to its primary pipe, then the
		// HeatExchanger's primary pipe should be added directly. This method
		// assumes the HeatExchanger's secondary pipe will be used.
		public abstract void visit(HeatExchanger plantComp);

		// Redirect to the Pipe behavior.
		public void visit(CoreChannel plantComp) {
			visit((Pipe) plantComp);
		}

		public void visit(Subchannel plantComp) {
			visit((Pipe) plantComp);
		}

		public void visit(PipeWithHeatStructure plantComp) {
			visit((Pipe) plantComp);
		}

		// Unused visit operations.
		public void visit(PlantComposite plantComp) {
		}

		public void visit(GeometricalComponent plantComp) {
		}

		public void visit(Junction plantComp) {
		}

		public void visit(Reactor plantComp) {
		}

		public void visit(PointKinetics plantComp) {
		}

		public void visit(Branch plantComp) {
		}

		public void visit(SubchannelBranch plantComp) {
		}

		public void visit(VolumeBranch plantComp) {
		}

		public void visit(FlowJunction plantComp) {
		}

		public void visit(WetWell plantComp) {
		}

		public void visit(Boundary plantComp) {
		}

		public void visit(OneInOneOutJunction plantComp) {
		}

		public void visit(Turbine plantComp) {
		}

		public void visit(IdealPump plantComp) {
		}

		public void visit(Pump plantComp) {
		}

		public void visit(Valve plantComp) {
		}

		public void visit(PipeToPipeJunction plantComp) {
		}

		public void visit(Inlet plantComp) {
		}

		public void visit(MassFlowInlet plantComp) {
		}

		public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
		}

		public void visit(Outlet plantComp) {
		}

		public void visit(SolidWall plantComp) {
		}

		public void visit(TDM plantComp) {
		}

		public void visit(TimeDependentJunction plantComp) {
		}

		public void visit(TimeDependentVolume plantComp) {
		}

		public void visit(DownComer plantComp) {
		}

		public void visit(SeparatorDryer plantComp) {
		}
	};
}