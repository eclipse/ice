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

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBManipulator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Base class for junction-like components that connect objects, such as pipes.
 * The class keeps track of what is an input and what is an output.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Junction extends PlantComponent implements IJunction {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The inputs of this junction.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected final ArrayList<PlantComponent> inputs;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Outputs of this junction.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected final ArrayList<PlantComponent> outputs;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A list of coupled input-output pairs representing the connections of the
	 * junction. Input-output pairs are based on component name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Junction() {
		// begin-user-code
		super();

		// Initialize the input and output lists.
		inputs = new ArrayList<PlantComponent>();
		outputs = new ArrayList<PlantComponent>();

		// Initialize the list of listeners.
		listeners = new ArrayList<IJunctionListener>();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Junction(ArrayList<PlantComponent> ins,
			ArrayList<PlantComponent> outs, ArrayList<String> conns) {
		// begin-user-code
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
		// end-user-code
	}

	/**
	 * @return the inputs
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<PlantComponent> getInputs() {
		// begin-user-code
		return new ArrayList<PlantComponent>(inputs);
		// end-user-code
	}

	/**
	 * @param inputs
	 *            the inputs to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setInputs(ArrayList<PlantComponent> ins) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * @return the outputs
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<PlantComponent> getOutputs() {
		// begin-user-code
		return new ArrayList<PlantComponent>(outputs);
		// end-user-code
	}

	/**
	 * @param outputs
	 *            the outputs to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setOutputs(ArrayList<PlantComponent> outs) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * @return the connections
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getConnections() {
		// begin-user-code
		return connections;
		// end-user-code
	}

	/**
	 * @param connections
	 *            the connections to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setConnections(ArrayList<String> connections) {
		// begin-user-code
		this.connections = connections;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code
		Junction temp = new Junction();
		temp.copy(this);
		return temp;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Junction otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		int hash = super.hashCode();
		hash = 31 * hash + inputs.hashCode();
		hash = 31 * hash + outputs.hashCode();
		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Accepts PlantComponentVisitors to reveal the type of a PlantComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The PlantComponent's visitor.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IPlantComponentVisitor visitor) {
		// begin-user-code
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
		// end-user-code
	}

	/**
	 * <p>
	 * This operation loads the component from the XML stream.
	 * </p>
	 * 
	 * @param inputStream
	 *            <p>
	 *            The stream containing the XML for this object.
	 *            </p>
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code

		// Initialize JAXBManipulator.
		jaxbManipulator = new ICEJAXBManipulator();

		// Call the read() on jaxbManipulator to create a new Object instance
		// from the inputStream.
		Object dataObject;
		try {
			dataObject = jaxbManipulator.read(this.getClass(), inputStream);
			// Copy contents of new object into current data structure
			this.copy((Junction) dataObject);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Nullerize jaxbManipilator.
		jaxbManipulator = null;

		return;
		// end-user-code
	}

	// ---- Implements IJunction ---- //
	public void registerJunctionListener(IJunctionListener listener) {
		// begin-user-code

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
		// end-user-code
	}

	public void unregisterJunctionListener(IJunctionListener listener) {
		// begin-user-code

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
		// end-user-code
	}

	public void notifyJunctionListeners(final List<PlantComponent> components,
			final boolean added) {
		// begin-user-code

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
		// end-user-code
	}

	public boolean isInput(PlantComponent component) {
		// begin-user-code

		boolean found = false;

		// The list shouldn't be too big, so a linear search is okay for now.
		int size = inputs.size();
		for (int i = 0; !found && i < size; i++) {
			found = (component == inputs.get(i));
		}
		return found;
		// end-user-code
	}

	public void addInput(PlantComponent input) {
		// begin-user-code

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
		// end-user-code
	}

	public void removeInput(PlantComponent input) {
		// begin-user-code

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
		// end-user-code
	}

	public void addOutput(PlantComponent output) {
		// begin-user-code

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
		// end-user-code
	}

	public void removeOutput(PlantComponent output) {
		// begin-user-code

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
		// end-user-code
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