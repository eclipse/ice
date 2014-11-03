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

import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.datastructures.updateableComposite.Composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;

/**
 * <p>
 * This class is used by the {@link PlantIOFactory} for writing PlantComponents
 * to HDF5 files. It assumes an HDF5 Group is already created (including the
 * tag) and adds all of the attributes and datasets for the information stored
 * in the PlantComponent.
 * </p>
 * <p>
 * This class uses the {@link IPlantComponentVisitor} interface to handle
 * writing for each type of PlantComponent.
 * </p>
 * 
 * @author djg
 * 
 */
public class PlantComponentWriter implements IPlantComponentVisitor {

	/**
	 * The {@link PlantIOFactory} that is using this writer. We need access to
	 * it for its HDF5 writing methods.
	 */
	private final HdfIOFactory factory;

	/**
	 * A stack of opened group IDs in case the initial PlantComponent requires
	 * additional child PlantComponents to be written.
	 */
	private final Stack<Integer> groupIds;

	/**
	 * The ancestor Composites containing the current Component being visited.
	 * There should always be at least one Composite in this stack (it has 0
	 * children).
	 */
	private final Stack<Composite> parents;

	/**
	 * The default constructor.
	 * 
	 * @param factory
	 *            The {@link PlantIOFactory} that is using this writer. We need
	 *            access to it for its HDF5 writing methods.
	 */
	public PlantComponentWriter(HdfIOFactory factory) {

		// Set the factory if possible. It should not be null.
		this.factory = (factory != null ? factory : new HdfIOFactory());

		// FIXME - Given the linear execution order required for this visitor,
		// we *can* avoid using the stacks below.

		// Initialize the stack of HDF5 Group IDs.
		groupIds = new Stack<Integer>();

		// Initialize the stack of parent Composites.
		parents = new Stack<Composite>();
		parents.push(new PlantComposite());

		return;
	}

	/**
	 * Writes a {@link PlantComponent}'s information to the specified HDF5 Group
	 * ID.
	 * 
	 * @param component
	 *            The PlantComponent to write to the file.
	 * @param groupId
	 *            The HDF5 Group ID for the PlantComponent.
	 */
	public void writePlantComponent(PlantComponent component, int groupId)
			throws NullPointerException, HDF5Exception {
		// name:String
		// description:String
		// uniqueId:int

		if (component != null) {

			// Add the component's group ID to the top of the stack.
			groupIds.push(groupId);

			// Write all of the component's ICEObject Attributes.
			factory.writeICEObjectInfo(component, groupId);

			// Visit the component to perform the proper write operations.
			component.accept(this);

			// Remove the component's group ID from the top of the stack and
			// check for any error.
			int topGroup = groupIds.pop();
			if (groupId != topGroup) {
				throw new HDF5Exception("PlantComponentWriter error: "
						+ "Group mismatch! Expected group " + groupId
						+ " but found group " + topGroup);
			}
		}
		return;
	}

	public void visit(PlantComposite plantComp) {

		// This method writes a PlantComposite in the following way:
		// Pipes, HeatExchangers, Junctions, Reactors, and other PlantComponents
		// are split into separate groups. This is done so that Pipes and
		// HeatExchangers can be read in first (Junctions and Reactors use
		// Pipes and HeatExchangers).

		// Create the lists of components for each major group listed above.
		final List<PlantComponent> pipes = new ArrayList<PlantComponent>();
		final List<PlantComponent> heatExchangers = new ArrayList<PlantComponent>();
		final List<PlantComponent> junctions = new ArrayList<PlantComponent>();
		final List<PlantComponent> reactors = new ArrayList<PlantComponent>();
		final List<PlantComponent> others = new ArrayList<PlantComponent>();

		// Create a visitor that puts components into the above lists depending
		// on its base type, e.g. Pipe sub-classes go in the pipes list.
		IPlantComponentVisitor visitor = new IPlantComponentVisitor() {

			public void visit(PlantComposite plantComp) {
				// Do nothing.
			}

			public void visit(GeometricalComponent plantComp) {
				others.add(plantComp);
			}

			public void visit(Junction plantComp) {
				junctions.add(plantComp);
			}

			public void visit(Reactor plantComp) {
				reactors.add(plantComp);
			}

			public void visit(PointKinetics plantComp) {
				others.add(plantComp);
			}

			public void visit(HeatExchanger plantComp) {
				heatExchangers.add(plantComp);
			}

			public void visit(Pipe plantComp) {
				pipes.add(plantComp);
			}

			public void visit(CoreChannel plantComp) {
				visit((Pipe) plantComp);
			}

			public void visit(Subchannel plantComp) {
				visit((Pipe) plantComp);
			}

			public void visit(PipeWithHeatStructure plantComp) {
				visit((Pipe) plantComp);
			}

			public void visit(Branch plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(SubchannelBranch plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(VolumeBranch plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(FlowJunction plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(WetWell plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(Boundary plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(OneInOneOutJunction plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(Turbine plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(IdealPump plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(Pump plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(Valve plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(PipeToPipeJunction plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(Inlet plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(MassFlowInlet plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(Outlet plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(SolidWall plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(TDM plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(TimeDependentJunction plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(TimeDependentVolume plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(DownComer plantComp) {
				visit((Junction) plantComp);
			}

			public void visit(SeparatorDryer plantComp) {
				visit((Junction) plantComp);
			}

		};
		// Use the visitor to sort all of the plant's components.
		for (PlantComponent component : plantComp.getPlantComponents()) {
			if (component != null) {
				component.accept(visitor);
			}
		}

		// Get the plant's HDF5 Group ID.
		int plantId = groupIds.peek();

		// Put the PlantComposite in the stack of parent Composites.
		parents.push(plantComp);

		// We want to write the Pipes, Junctions, etc. in different groups.
		// Create a list of the names of these groups.
		List<String> groups = new ArrayList<String>(5);
		groups.add("Pipes");
		groups.add("HeatExchangers");
		groups.add("Junctions");
		groups.add("Reactors");
		groups.add("Other");

		// Associate a the lists used by the visitor with the group names. We
		// use an implied association by using the same index for this list and
		// the list of group names above.
		List<List<PlantComponent>> lists = new ArrayList<List<PlantComponent>>(
				5);
		lists.add(pipes);
		lists.add(heatExchangers);
		lists.add(junctions);
		lists.add(reactors);
		lists.add(others);

		try {
			// Write each major group of PlantComponents.
			for (int i = 0; i < groups.size(); i++) {
				// Get the group's name and its list of PlantComponents.
				String name = groups.get(i);
				List<PlantComponent> list = lists.get(i);

				// Create the major group of components.
				int groupId = factory.createGroup(plantId, name);

				// Write a new group for each PlantComponent in this major
				// group. Since the PlantIOFactory handles all PlantComponents,
				// we do not need to look up the IO factory for the components.
				for (PlantComponent component : list) {
					// Create a group for the component.
					int componentId = factory.createGroup(groupId,
							component.toString());
					// Write the tag Attribute for the component.
					String tag = factory.getTag(component.getClass());
					factory.writeTag(tag, componentId);
					// Write the object's data to its new group.
					factory.writeObjectData(componentId, component);
					// Close the component's group.
					factory.closeGroup(componentId);
				}

				// Close the major group of components.
				factory.closeGroup(groupId);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write PlantComposite " + plantComp.getName()
					+ " " + plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write PlantComposite " + plantComp.getName()
					+ " " + plantComp.getId());
		}

		// Remove the PlantComposite from the stack of parent Composites.
		parents.pop();

		return;
	}

	// ---- Base classes ---- //
	public void visit(GeometricalComponent plantComp) {
		// super:PlantComponent
		// position:double[3]
		// orientation:double[3]
		// rotation:double
		// numElements:int

		int objectId = groupIds.peek();

		try {
			// Write the rotation.
			factory.writeAttribute(objectId, "rotation",
					HDF5Constants.H5T_NATIVE_DOUBLE, plantComp.getRotation());
			// Write the number of elements.
			factory.writeAttribute(objectId, "numElements",
					HDF5Constants.H5T_NATIVE_INT, plantComp.getNumElements());

			// ---- Position and orientation ---- //
			// Combine the position and orientation into a single Dataset
			// (rather, a 2x3 array of doubles).

			// The overall array containing position and orientation is 2x3.
			long[] dims = new long[] { 2, 3 };
			int type = HDF5Constants.H5T_NATIVE_DOUBLE;
			// Create the buffer that contains the position and orientation
			// values.
			double[] buffer = new double[6];
			System.arraycopy(plantComp.getPosition(), 0, buffer, 0, 3);
			System.arraycopy(plantComp.getOrientation(), 0, buffer, 3, 3);
			// Write the Dataset.
			factory.writeDataset(objectId, "vectors", 2, dims, type, buffer);
			// ---------------------------------- //

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write GeometricalComponent "
					+ plantComp.getName() + " " + plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write GeometricalComponent "
					+ plantComp.getName() + " " + plantComp.getId());
		}

		return;
	}

	public void visit(Junction plantComp) {
		// super:PlantComponent
		// inputs:List<PlantComponent>
		// outputs:List<PlantComponent>

		// Get the junction's HDF5 Group ID.
		int junctionId = groupIds.peek();

		try {
			// Get the junction's inputs.
			List<PlantComponent> inputs = plantComp.getInputs();
			int size = inputs.size();

			// Set up the parameters required to write a 1-D dataset containing
			// the IDs of all input components.

			// The number of dimensions is 1.
			int rank = 2;
			// The size of each dimension (in this case, the number of inputs).
			long[] dims = new long[] { size, 2 };
			// The type of data stored in the dataset (integers).
			int type = HDF5Constants.H5T_NATIVE_INT;
			// Create the buffer that contains the dataset's raw information. We
			// need to put the IDs of all the inputs in this buffer.
			int bufferSize = size * 2;
			int[] buffer = new int[bufferSize];
			for (int i = 0, j = 0; i < bufferSize; j++) {
				PlantComponent component = inputs.get(j);
				buffer[i++] = component.getId();
				buffer[i++] = (component instanceof HeatExchanger ? 0 : 1);
			}
			// Write the inputs dataset.
			factory.writeDataset(junctionId, "inputs", rank, dims, type, buffer);

			// Get the junction's outputs.
			List<PlantComponent> outputs = plantComp.getOutputs();
			size = outputs.size();

			// Set up the parameters required to write a 1-D dataset containing
			// the IDs of all input components.

			// Update the dimensions and the buffer.
			dims[0] = size;
			bufferSize = size * 2;
			buffer = new int[bufferSize];
			for (int i = 0, j = 0; i < bufferSize; j++) {
				PlantComponent component = outputs.get(j);
				buffer[i++] = component.getId();
				buffer[i++] = (component instanceof HeatExchanger ? 0 : 1);
			}
			// Write the outputs dataset.
			factory.writeDataset(junctionId, "outputs", rank, dims, type,
					buffer);

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write Junction " + plantComp.getName() + " "
					+ plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write Junction " + plantComp.getName() + " "
					+ plantComp.getId());
		}

		return;
	}

	public void visit(Reactor plantComp) {
		// super:PlantComponent
		// coreChannels:List<CoreChannel>

		// Get the junction's HDF5 Group ID.
		int reactorId = groupIds.peek();

		try {
			// Get the reactor's core channels.
			List<CoreChannel> coreChannels = plantComp.getCoreChannels();
			int size = coreChannels.size();

			// Set up the parameters required to write a 1-D dataset containing
			// the IDs of all input components.

			// The number of dimensions is 1.
			int rank = 1;
			// The size of each dimension (in this case, the number of inputs).
			long[] dims = new long[] { size };
			// The type of data stored in the dataset (integers).
			int type = HDF5Constants.H5T_NATIVE_INT;
			// Create the buffer that contains the dataset's raw information. We
			// need to put the IDs of all the inputs in this buffer.
			int[] buffer = new int[size];
			for (int i = 0; i < size; i++) {
				buffer[i] = coreChannels.get(i).getId();
			}
			// Write the coreChannels dataset.
			factory.writeDataset(reactorId, "coreChannels", rank, dims, type,
					buffer);

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write Junction " + plantComp.getName() + " "
					+ plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write Junction " + plantComp.getName() + " "
					+ plantComp.getId());
		}

		return;
	}

	public void visit(PointKinetics plantComp) {
		// super:PlantComponent

		// Nothing to do.
	}

	public void visit(HeatExchanger plantComp) {
		// super:GeometricalComponent
		// innerRadius:double
		// length:double
		// primaryPipe:Pipe
		// secondaryPipe:Pipe

		// Write the GeometricalComponent properties.
		visit((GeometricalComponent) plantComp);

		int objectId = groupIds.peek();

		try {

			// Write the radius.
			factory.writeAttribute(objectId, "innerRadius",
					HDF5Constants.H5T_NATIVE_DOUBLE, plantComp.getInnerRadius());
			// Write the length.
			factory.writeAttribute(objectId, "length",
					HDF5Constants.H5T_NATIVE_DOUBLE, plantComp.getLength());

			// There is currently no need to write the pipe information since
			// they are tied directly to the HeatExchanger's properties.

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write HeatExchanger " + plantComp.getName()
					+ " " + plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write HeatExchanger " + plantComp.getName()
					+ " " + plantComp.getId());
		}

		return;
	}

	public void visit(Pipe plantComp) {
		// super:GeometricalComponent
		// radius:double
		// length:double

		// Write the GeometricalComponent properties.
		visit((GeometricalComponent) plantComp);

		int objectId = groupIds.peek();

		try {

			// Write the radius.
			factory.writeAttribute(objectId, "radius",
					HDF5Constants.H5T_NATIVE_DOUBLE, plantComp.getRadius());
			// Write the length.
			factory.writeAttribute(objectId, "length",
					HDF5Constants.H5T_NATIVE_DOUBLE, plantComp.getLength());

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write Pipe " + plantComp.getName() + " "
					+ plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentWriter error: "
					+ "Failed to write Pipe " + plantComp.getName() + " "
					+ plantComp.getId());
		}

		return;
	}

	// ---- Pipe subclasses ---- //
	public void visit(CoreChannel plantComp) {
		visit((Pipe) plantComp);
	}

	public void visit(Subchannel plantComp) {
		visit((Pipe) plantComp);
	}

	public void visit(PipeWithHeatStructure plantComp) {
		visit((Pipe) plantComp);
	}

	// ---- Junction subclasses ---- //
	public void visit(Branch plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(SubchannelBranch plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(VolumeBranch plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(FlowJunction plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(WetWell plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(Boundary plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(OneInOneOutJunction plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(Turbine plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(IdealPump plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(Pump plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(Valve plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(PipeToPipeJunction plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(Inlet plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(MassFlowInlet plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(Outlet plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(SolidWall plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(TDM plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(TimeDependentJunction plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(TimeDependentVolume plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(DownComer plantComp) {
		visit((Junction) plantComp);
	}

	public void visit(SeparatorDryer plantComp) {
		visit((Junction) plantComp);
	}

}
