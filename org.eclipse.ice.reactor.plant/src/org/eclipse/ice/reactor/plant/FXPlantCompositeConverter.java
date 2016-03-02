/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.reactor.plant;

import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerController;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeController;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorController;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMeshCategory;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMeshProperty;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPlantViewControllerProviderFactory;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPlantViewRootController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.IPlantData;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;

/**
 * A class which interfaces between a PlantComposite and a tree of
 * AbstractControllers, converting the RELAP7 data structures into JavaFX
 * modeling data structures for use with the plant view.
 * 
 * @author Robert Smith
 *
 */
public class FXPlantCompositeConverter
		implements IPlantData, IVizUpdateableListener {

	/**
	 * The scale which translates between RELAP7 units and JavaFX units. Each
	 * RELAP7 unit will be treated as SCALE JavaFX units.
	 */
	private final int SCALE = 50;

	/**
	 * The root of the tree of plant parts converted from the source.
	 */
	FXPlantViewRootController output;

	/**
	 * The PlantComposite containing the RELAP7 representation of the plant's
	 * structure.
	 */
	PlantComposite source;

	/**
	 * The default constructor.
	 * 
	 * @param source
	 *            The PlantComposite which is to be converted into modeling data
	 *            structures.
	 */
	public FXPlantCompositeConverter(PlantComposite source) {
		this.source = source;
		source.register(this);
	}

	/**
	 * Convert the PlantComposite data structure into an equivalent
	 * FXPlantViewRootController data structure.
	 * 
	 * @return A JavaFX modeling data structure representation fo the plant.
	 */
	@Override
	public AbstractController getPlant() {

		// If the output has not been initialized, create it
		if (output == null) {
			refresh();
		}

		return output;
	}

	/**
	 * Recreate the output model based on the current state of the source.
	 */
	private void refresh() {

		// Create a new root
		output = new FXPlantViewRootController(new AbstractMesh(),
				new AbstractView());

		// Visit each plant component, converting it for the new data structure
		FXPlantComponentVisitor visitor = new FXPlantComponentVisitor(output);
		for (PlantComponent plantComp : source.getPlantComponents()) {
			plantComp.accept(visitor);
		}
	}

	private class FXPlantComponentVisitor implements IPlantComponentVisitor {

		/**
		 * The root of the tree of plant parts
		 */
		FXPlantViewRootController root;

		/**
		 * The factory for constructing controllers and views for the meshs.
		 */
		FXPlantViewControllerProviderFactory factory;

		/**
		 * The default constructor.
		 * 
		 * @param root
		 *            The root controller all visited objects will be added to.
		 */
		public FXPlantComponentVisitor(FXPlantViewRootController root) {
			this.root = root;
			factory = new FXPlantViewControllerProviderFactory();
		}

		/**
		 * Find an AbstractController which is under the Pipes or Core Channels
		 * category with the given ID.
		 * 
		 * @param ID
		 *            The unique ID to search for
		 * @return The AbstractController with the property Id equal to ID that
		 *         is under the root's "Pipes" or "Core Channels" category, or
		 *         null if no such pipe exists.
		 */
		private IController findPipe(Integer ID) {

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<IController> pipeList = root
					.getEntitiesFromCategory(ReactorMeshCategory.PIPES);
			pipeList.addAll(root
					.getEntitiesFromCategory(ReactorMeshCategory.CORE_CHANNELS));

			// Check the root to see if a pipe with that id already exists
			for (IController pipe : pipeList) {
				if (Integer.parseInt(pipe.getProperty(MeshProperty.ID)) == ID) {
					return pipe;
				}
			}

			// If we made it out of the above loop a match was not found, so
			// create a new pipe

			// Convert the pipe into a modeling data structure
			source.getPlantComponent(ID).accept(this);

			// Refresh the list of pipes
			pipeList = root.getEntitiesFromCategory(ReactorMeshCategory.PIPES);
			pipeList.addAll(root
					.getEntitiesFromCategory(ReactorMeshCategory.CORE_CHANNELS));

			// Now that the pipe is guaranteed to be in the root, as it
			// was added when visited, find the pipe with that id and
			// return it
			for (IController pipe : pipeList) {
				if (Integer.parseInt(pipe.getProperty(MeshProperty.ID)) == ID) {
					return pipe;
				}
			}

			return null;
		}

		/**
		 * Find an AbstractController which is under the Heat Exchangers
		 * category with the given ID.
		 * 
		 * @param ID
		 *            The unique ID to search for
		 * @return The AbstractController with the property Id equal to ID that
		 *         is under the root's "Heat Exchangers" category, or null if no
		 *         such heat exchanger exists.
		 */
		private IController findHeatExchanger(Integer ID) {

			// Check the root to see if a pipe with that id already exists
			for (IController pipe : root.getEntitiesFromCategory(
					ReactorMeshCategory.HEAT_EXCHANGERS)) {
				if (Integer.parseInt(pipe.getProperty(MeshProperty.ID)) == ID) {
					return pipe;
				}
			}

			// If we made it out of the above loop a match was not found, so
			// create a new pipe

			// Convert the pipe into a modeling data structure
			source.getPlantComponent(ID).accept(this);

			// Now that the pipe is guaranteed to be in the root, as it
			// was added when visited, find the pipe with that id and
			// return it
			for (IController pipe : root.getEntitiesFromCategory(
					ReactorMeshCategory.HEAT_EXCHANGERS)) {
				if (Integer.parseInt(pipe.getProperty(MeshProperty.ID)) == ID) {
					return pipe;
				}
			}

			return null;
		}

		@Override
		public void visit(PlantComposite plantComp) {
			// Don't do anything for generic plant composites
		}

		@Override
		public void visit(GeometricalComponent plantComp) {
			// Don't do anything for generic geometrical components
		}

		@Override
		public void visit(Junction plantComp) {

			// Create a new junction
			JunctionMesh mesh = new JunctionMesh();
			JunctionController junction = (JunctionController) factory
					.createProvider(mesh).createController(mesh);

			junction.setProperty(MeshProperty.NAME, plantComp.getName());

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<IController> pipeList = root
					.getEntitiesFromCategory(ReactorMeshCategory.PIPES);
			pipeList.addAll(root
					.getEntitiesFromCategory(ReactorMeshCategory.CORE_CHANNELS));

			// Add all the input pipes to the junction
			for (PlantComponent input : plantComp.getInputs()) {

				// Get the pipe with the correct ID
				IController pipe = findPipe(input.getId());

				// If the pipe was found, add it
				if (pipe != null) {

					// Set up this junction as an output to that pipe
					junction.addEntityToCategory(pipe,
							ReactorMeshCategory.INPUT);
					pipe.addEntityToCategory(junction,
							ReactorMeshCategory.OUTPUT);
				}

				// If no pipe was found, it must be a heat exchanger instead
				else {

					// Find the heat exchanger
					pipe = findHeatExchanger(input.getId());

					// TODO We currently just check if the input is a
					// HeatExchanger as primary pipes are added directly as
					// pipes while secondary pipes are added through the Heat
					// Exchanger. This should be changed after figuring out how
					// the Junction is referencing the primary pipe directly
					// despite it sharing its ID with its parent HeatExchanger
					// and not
					// being directly in the PlantComposite's component tree.
					// See
					// org.eclipse.ice.client.widgets.reactoreditor.plant.JunctionController's
					// addPipes() function.
					if (input instanceof HeatExchanger) {
						// Set up this junction as an output to the heat
						// exchanger's
						// secondary pipe
						junction.addEntityToCategory(pipe,
								ReactorMeshCategory.INPUT);
						pipe.addEntityToCategory(junction,
								ReactorMeshCategory.OUTPUT);
					}

					else {
						junction.addEntityToCategory(
								((HeatExchangerController) pipe)
										.getPrimaryPipe(),
								ReactorMeshCategory.INPUT);
						((HeatExchangerController) pipe).getPrimaryPipe()
								.addEntityToCategory(junction,
										ReactorMeshCategory.SECONDARY_OUTPUT);
					}

				}

			}

			// Add all the output pipes to the junction
			for (PlantComponent output : plantComp.getOutputs()) {

				// Get the pipe with the correct ID
				IController pipe = findPipe(output.getId());

				// If the pipe was found, add it
				if (pipe != null) {

					// Set up this junction as an output to that pipe
					junction.addEntityToCategory(pipe,
							ReactorMeshCategory.OUTPUT);
					pipe.addEntityToCategory(junction,
							ReactorMeshCategory.INPUT);
				}

				// If no pipe was found, it must be a heat exchanger instead
				else {

					// Find the heat exchanger
					pipe = findHeatExchanger(output.getId());

					// TODO We currently just check if the input is a
					// HeatExchanger as primary pipes are added directly as
					// pipes while secondary pipes are added through the Heat
					// Exchanger. This should be changed after figuring out how
					// the Junction is referencing the primary pipe directly
					// despite it sharing its ID with its parent HeatExchanger
					// and not
					// being directly in the PlantComposite's component tree.
					// See
					// org.eclipse.ice.client.widgets.reactoreditor.plant.JunctionController's
					// addPipes() function.
					if (output instanceof HeatExchanger) {
						// Set up this junction as an input to the heat
						// exchanger's
						// secondary pipe
						junction.addEntityToCategory(pipe,
								ReactorMeshCategory.OUTPUT);
						pipe.addEntityToCategory(junction,
								ReactorMeshCategory.INPUT);
					}

					else {
						junction.addEntityToCategory(
								((HeatExchangerController) pipe)
										.getPrimaryPipe(),
								ReactorMeshCategory.OUTPUT);
						((HeatExchangerController) pipe).getPrimaryPipe()
								.addEntityToCategory(junction,
										ReactorMeshCategory.SECONDARY_INPUT);
					}

				}

			}

			// Add the junction to the root
			root.addEntityToCategory(junction, ReactorMeshCategory.JUNCTIONS);

		}

		@Override
		public void visit(Reactor plantComp) {

			// Create a new reactor
			ReactorMesh mesh = new ReactorMesh();
			ReactorController reactor = (ReactorController) factory
					.createProvider(mesh).createController(mesh);

			// Simply add the reactor, as the plant view assumes there is only
			// one and the root will take care of adding the core channels.
			root.addEntityToCategory(reactor, ReactorMeshCategory.REACTORS);

		}

		@Override
		public void visit(PointKinetics plantComp) {
			// PointKinetics are not drawn

		}

		@Override
		public void visit(HeatExchanger plantComp) {

			// Create a new heat exchanger
			HeatExchangerMesh mesh = new HeatExchangerMesh();
			HeatExchangerController heatExchanger = (HeatExchangerController) factory
					.createProvider(mesh).createController(mesh);

			// Heat Exchangers require a contained primary pipe, so create one
			// for it.
			PipeController pipe = createPipe(plantComp.getPrimaryPipe());
			heatExchanger.setPrimaryPipe(pipe);

			// Set the heat exchanger's position
			applyTransformation(heatExchanger, plantComp.getPosition(),
					plantComp.getOrientation(), plantComp.getLength());

			// Add the heat exchanger to the root
			root.addEntityToCategory(heatExchanger,
					ReactorMeshCategory.HEAT_EXCHANGERS);

		}

		@Override
		public void visit(Pipe plantComp) {

			// If this pipe has already been converted, ignore it
			boolean found = false;

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<IController> pipeList = root
					.getEntitiesFromCategory(ReactorMeshCategory.PIPES);
			pipeList.addAll(root
					.getEntitiesFromCategory(ReactorMeshCategory.CORE_CHANNELS));

			// Check the root to see if a pipe with that id already exists
			for (IController pipe : pipeList) {
				if (Integer.parseInt(
						pipe.getProperty(MeshProperty.ID)) == plantComp
								.getId()) {

					// Match found, stop the search
					found = true;
					break;
				}
			}

			if (!found) {

				// Convert the pipe and add it to the root.
				root.addEntityToCategory(createPipe(plantComp),
						ReactorMeshCategory.PIPES);

			}

		}

		@Override
		public void visit(CoreChannel plantComp) {

			// If this pipe has already been converted, ignore it
			boolean found = false;

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<IController> pipeList = root
					.getEntitiesFromCategory(ReactorMeshCategory.PIPES);
			pipeList.addAll(root
					.getEntitiesFromCategory(ReactorMeshCategory.CORE_CHANNELS));

			// Check the root to see if a pipe with that id already exists
			for (IController pipe : pipeList) {
				if (Integer.parseInt(
						pipe.getProperty(MeshProperty.ID)) == plantComp
								.getId()) {

					// Match found, stop the search
					found = true;
					break;
				}
			}

			if (!found) {

				// Convert the pipe
				PipeController pipe = createPipe(plantComp);

				// Set the pipe as a core channel and add it to the root
				pipe.setProperty(ReactorMeshProperty.CORE_CHANNEL, "True");
				root.addEntity(pipe);

			}

		}

		@Override
		public void visit(Subchannel plantComp) {
			// Redirect to the Pipe function
			visit((Pipe) plantComp);
		}

		@Override
		public void visit(PipeWithHeatStructure plantComp) {
			// Redirect to the Pipe function
			visit((Pipe) plantComp);
		}

		@Override
		public void visit(Branch plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SubchannelBranch plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(VolumeBranch plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(FlowJunction plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(WetWell plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Boundary plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(OneInOneOutJunction plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Turbine plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(IdealPump plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Pump plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Valve plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(PipeToPipeJunction plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Inlet plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(MassFlowInlet plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Outlet plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SolidWall plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(TDM plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(TimeDependentJunction plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(TimeDependentVolume plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(DownComer plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SeparatorDryer plantComp) {
			// Redirect to the Junction function
			visit((Junction) plantComp);
		}

		/**
		 * Apply transformations to the target part so that it is in the
		 * position described by the given parameters.
		 * 
		 * @param target
		 *            The part to apply the transformation to.
		 * @param position
		 *            A length 3 vector describing the coordinates of the center
		 *            of the pipe's input end, in the order x, y, z.
		 * @param orientation
		 *            A length 3 vector from the origin which describes the
		 *            pipe's central axis.
		 * @param pipeLength
		 *            The pipe's length must be given to aid in the calculation,
		 *            but it is NOT changed by this function.
		 */
		private void applyTransformation(IController target, double[] position,
				double[] orientation, double pipeLength) {

			// Get the data describing the pipe's location. Position is the
			// center of the pipe's input end, while orientation is a vector
			// from the position which describes the pipe's axis.

			// Multiply the positions to the proper scale
			position[0] = position[0] * SCALE;
			position[1] = position[1] * SCALE;
			position[2] = position[2] * SCALE;

			// System.out.println("Pipe Position: " + position[0] + " "
			// + position[1] + " " + position[2]);

			// Normalize the orientation vector
			double[] normalized = new double[3];
			double length = Math.sqrt(
					Math.pow(orientation[0], 2) + Math.pow(orientation[1], 2)
							+ Math.pow(orientation[2], 2));
			normalized[0] = orientation[0] / length;
			normalized[1] = orientation[1] / length;
			normalized[2] = orientation[2] / length;

			// The tube is, by default, centered on the origin. Stepping one
			// half its length in the direction of the orientation vector will
			// place the output edge's center on the origin, so that the
			// position vector now properly represents the movement from the
			// origin to the pipe's position.
			position[0] += pipeLength / 2 * normalized[0];
			position[1] += pipeLength / 2 * normalized[1];
			position[2] += pipeLength / 2 * normalized[2];

			// Set the pipe's translation
			target.setTranslation(position[0], position[1], position[2]);

			// Calculate the amount of radians per axis as follows: (rotation z)
			// = atan(y/x) and (rotation y) = atan (z / sqrt(x ^ 2 + y ^ 2))

			// Calculate the y rotation angle
			double yRotation;
			if (normalized[1] != 0 || normalized[0] != 0) {
				yRotation = normalized[2] / Math.sqrt(Math.pow(normalized[0], 2)
						+ Math.pow(normalized[1], 2));
			} else {
				yRotation = 0d;
			}

			// Calculate the z rotation angle
			double zRotation;
			if (normalized[0] != 0) {
				zRotation = normalized[1] / normalized[0];
			} else {
				zRotation = 0d;
			}

			// If the pitch and yaw are both zero, then the orientation vector
			// is pointing down one of the axes. For other angles, we simple set
			// the rotation
			if ((yRotation != 0 && zRotation != 0)) {
				target.setRotation(0, -Math.atan(yRotation),
						-Math.atan(zRotation));
			}

			// Explicitly set the pipe to point down the x, y, or z axis
			else {

				// Rotate the pipe to point down the x axis by rotating about
				// the z
				if (normalized[0] > 0) {
					target.setRotation(0, 0, -Math.PI / 2);
				}

				// Rotate in the other direction if the vector is negative
				else if (normalized[0] < 0) {
					target.setRotation(0, 0, Math.PI / 2);
				}

				// Rotate the pipe to point down the z axis by rotating about
				// the x
				else if (normalized[2] > 0) {
					target.setRotation(Math.PI / 2, 0, 0);
				}

				// Rotate in the other direction if the vector is negative
				else if (normalized[2] < 0) {
					target.setRotation(-Math.PI / 2, 0, 0);
				}

				// If the orientation is the negated y vector, flip the tube by
				// 180 degrees about the x axis to turn it upside down. The
				// positive y vector is the tube's default position, and thus
				// does not need to be handled.
				else if (normalized[1] < 0) {
					target.setRotation(-Math.PI, 0, 0);
				}
			}
		}

		/**
		 * Creates a JavaFX PipeController from a RELAP7 Pipe.
		 * 
		 * @param plantComp
		 *            The pipe to be converted.
		 * @return The converted pipe.
		 */
		public PipeController createPipe(Pipe plantComp) {
			// Create a new pipe
			PipeMesh mesh = new PipeMesh();

			// Set the pipe's properties
			mesh.setProperty(MeshProperty.ID,
					Integer.toString(plantComp.getId()));
			mesh.setLength(plantComp.getLength() * SCALE);
			mesh.setRadius(plantComp.getRadius() * SCALE);
			mesh.setInnerRadius(plantComp.getRadius() * SCALE);
			mesh.setAxialSamples(plantComp.getNumElements());

			mesh.setProperty(MeshProperty.NAME, plantComp.getName());

			// Create the view and controller
			PipeController pipe = (PipeController) factory.createProvider(mesh)
					.createController(mesh);

			// Apply the position and orientation
			applyTransformation(pipe, plantComp.getPosition(),
					plantComp.getOrientation(), plantComp.getLength());

			return pipe;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener#update(org.eclipse.eavp.viz.service.
	 * datastructures. VizObject.IVizUpdateable)
	 */
	@Override
	public void update(IVizUpdateable component) {

		// On receiving an update, refresh the data structure
		refresh();
	}
}
