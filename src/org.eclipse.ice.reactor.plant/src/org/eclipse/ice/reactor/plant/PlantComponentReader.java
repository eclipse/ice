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
import org.eclipse.ice.datastructures.ICEObject.Composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;

/**
 * <p>
 * This class is used by the {@link PlantIOFactory} for reading PlantComponents
 * from HDF5 files. It assumes an HDF5 Group is already opened (including the
 * tag) and reads all of the attributes and datasets into the PlantComponent.
 * </p>
 * <p>
 * This class uses the {@link IPlantComponentVisitor} interface to handle
 * reading for each type of PlantComponent.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantComponentReader implements IPlantComponentVisitor {

	/**
	 * The {@link PlantIOFactory} that is using this reader. We need access to
	 * it for its HDF5 reading methods.
	 */
	private final HdfIOFactory factory;

	/**
	 * A stack of opened group IDs in case the initial PlantComponent requires
	 * additional child PlantComponents to be read.
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
	 *            The {@link PlantIOFactory} that is using this reader. We need
	 *            access to it for its HDF5 reading methods.
	 */
	public PlantComponentReader(HdfIOFactory factory) {

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
	 * Reads a {@link PlantComponent}'s information from the specified HDF5
	 * Group.
	 * 
	 * @param component
	 *            The PlantComponent whose information is being read from the
	 *            file.
	 * @param groupId
	 *            The HDF5 Group ID for the PlantComponent.
	 */
	public void readPlantComponent(PlantComponent component, int groupId)
			throws NullPointerException, HDF5Exception {
		// name:String
		// description:String
		// uniqueId:int

		if (component != null) {

			// Add the component's group ID to the top of the stack.
			groupIds.push(groupId);

			// Write all of the component's ICEObject Attributes.
			factory.readICEObjectInfo(component, groupId);

			// Visit the component to perform the proper read operations.
			component.accept(this);

			// Remove the component's group ID from the top of the stack and
			// check for any error.
			int topGroup = groupIds.pop();
			if (groupId != topGroup) {
				throw new HDF5Exception("PlantComponentReader error: "
						+ "Group mismatch! Expected group " + groupId
						+ " but found group " + topGroup);
			}
		}
		return;
	}

	public void visit(PlantComposite plantComp) {

		// Get the plant's HDF5 Group ID.
		int plantId = groupIds.peek();

		// Put the PlantComposite in the stack of parent Composites.
		parents.push(plantComp);

		// The Pipes, Junctions, etc. are stored in different HDF5 Groups.
		// Create a list of the names of these groups.
		List<String> groups = new ArrayList<String>(5);
		groups.add("Pipes");
		groups.add("HeatExchangers");
		groups.add("Junctions");
		groups.add("Reactors");
		groups.add("Other");

		try {
			// Read each major group of PlantComponents.
			for (int i = 0; i < groups.size(); i++) {
				String name = groups.get(i);

				// Open the major group of components.
				int groupId = factory.openGroup(plantId, name);

				// Read each component from the open group of major components.
				// Since the PlantIOFactory handles all PlantComponents, we do
				// not need to look up the IO factory for the components.
				for (String child : factory.getChildNames(groupId,
						HDF5Constants.H5O_TYPE_GROUP)) {
					// Open the component's group.
					int componentId = factory.openGroup(groupId, child);
					// Read the group's tag.
					String tag = factory.readTag(componentId);
					// Read the component (requires the tag).
					Object object = factory.read(componentId, tag);
					// Add the component to the PlantComposite.
					plantComp.addPlantComponent((PlantComponent) object);
					// Close the component's group.
					factory.closeGroup(componentId);
				}

				// Close the major group of components.
				factory.closeGroup(groupId);
			}
			// ------------------------- //

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
			int H5T_NATIVE_INT = HDF5Constants.H5T_NATIVE_INT;
			int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE;

			// Read the rotation.
			plantComp.setRotation((Double) factory.readAttribute(objectId,
					"rotation", H5T_NATIVE_DOUBLE));

			// Read the number of elements.
			plantComp.setNumElements((Integer) factory.readAttribute(objectId,
					"numElements", H5T_NATIVE_INT));

			// ---- Position and orientation ---- //
			// Combine the position and orientation into a single Dataset
			// (rather, a 2x3 array of doubles).

			// The overall array containing position and orientation is 2x3.
			int type = HDF5Constants.H5T_NATIVE_DOUBLE;
			// Create the buffer that contains the position and orientation
			// values.
			double[] buffer = (double[]) factory.readDataset(objectId,
					"vectors", type);

			double[] vector = new double[3];

			System.arraycopy(buffer, 0, vector, 0, 3);
			plantComp.setPosition(vector);

			System.arraycopy(buffer, 3, vector, 0, 3);
			plantComp.setOrientation(vector);
			// ---------------------------------- //

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentReader error: "
					+ "Failed to read GeometricalComponent "
					+ plantComp.getName() + " " + plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentReader error: "
					+ "Failed to read GeometricalComponent "
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
		// Get the parent Composite.
		Composite parent = parents.peek();

		try {
			int type = HDF5Constants.H5T_NATIVE_INT;
			int[] buffer;

			// Create a list of input PlantComponents.
			ArrayList<PlantComponent> inputs = new ArrayList<PlantComponent>();
			// Read all IDs from the junction's "inputs" dataset. Query the
			// parent Composite for the PlantComponents with the IDs and put
			// the components into the list of inputs.
			buffer = (int[]) factory.readDataset(junctionId, "inputs", type);
			for (int i = 0; i < buffer.length;) {
				int id = buffer[i++];
				PlantComponent component = (PlantComponent) parent
						.getComponent(id);
				if (buffer[i++] == 1 && component instanceof HeatExchanger) {
					inputs.add(((HeatExchanger) component).getPrimaryPipe());
				} else {
					inputs.add(component);
				}
			}
			// Set the inputs for the junction.
			plantComp.setInputs(inputs);

			// Create a list of output PlantComponents.
			ArrayList<PlantComponent> outputs = new ArrayList<PlantComponent>();
			// Read all IDs from the junction's "outputs" dataset. Query the
			// parent Composite for the PlantComponents with the IDs and put
			// the components into the list of outputs.
			buffer = (int[]) factory.readDataset(junctionId, "outputs", type);
			for (int i = 0; i < buffer.length;) {
				int id = buffer[i++];
				PlantComponent component = (PlantComponent) parent
						.getComponent(id);
				if (buffer[i++] == 1 && component instanceof HeatExchanger) {
					outputs.add(((HeatExchanger) component).getPrimaryPipe());
				} else {
					outputs.add(component);
				}
			}
			// Set the outputs for the junction.
			plantComp.setOutputs(outputs);

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
		// Get the parent Composite.
		Composite parent = parents.peek();

		try {
			int type = HDF5Constants.H5T_NATIVE_INT;
			int[] buffer;

			// Create a list of .
			ArrayList<CoreChannel> coreChannels = new ArrayList<CoreChannel>();
			// Read all IDs from the reactor's "coreChannels" dataset. Query the
			// parent Composite for the PlantComponents with the IDs and put
			// the components into the list of core channels.
			buffer = (int[]) factory.readDataset(reactorId, "coreChannels",
					type);
			for (int i = 0; i < buffer.length; i++) {
				coreChannels.add((CoreChannel) parent.getComponent(buffer[i]));
			}
			// Set the core channels for the reactor.
			plantComp.setCoreChannels(coreChannels);

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

			int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE;

			// Read the radius.
			plantComp.setInnerRadius((Double) factory.readAttribute(objectId,
					"innerRadius", H5T_NATIVE_DOUBLE));

			// Read the length.
			plantComp.setLength((Double) factory.readAttribute(objectId,
					"length", H5T_NATIVE_DOUBLE));

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentReader error: "
					+ "Failed to read HeatExchanger " + plantComp.getName()
					+ " " + plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentReader error: "
					+ "Failed to read HeatExchanger " + plantComp.getName()
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

			int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE;

			// Read the radius.
			plantComp.setRadius((Double) factory.readAttribute(objectId,
					"radius", H5T_NATIVE_DOUBLE));

			// Read the length.
			plantComp.setLength((Double) factory.readAttribute(objectId,
					"length", H5T_NATIVE_DOUBLE));

		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("PlantComponentReader error: "
					+ "Failed to read Pipe " + plantComp.getName() + " "
					+ plantComp.getId());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.out.println("PlantComponentReader error: "
					+ "Failed to read Pipe " + plantComp.getName() + " "
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
