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

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.geometry.reactor.HeatExchangerController;
import org.eclipse.ice.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.ice.viz.service.geometry.reactor.JunctionController;
import org.eclipse.ice.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.ice.viz.service.geometry.reactor.PipeController;
import org.eclipse.ice.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.ice.viz.service.geometry.reactor.ReactorController;
import org.eclipse.ice.viz.service.geometry.reactor.ReactorMesh;
import org.eclipse.ice.viz.service.javafx.geometry.plant.FXPlantViewFactory;
import org.eclipse.ice.viz.service.javafx.geometry.plant.FXPlantViewRootController;
import org.eclipse.ice.viz.service.javafx.geometry.plant.IPlantData;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.AbstractView;

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
	private final int SCALE = 25;

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

		// PipeMesh mesh = new PipeMesh();
		// mesh.setAxialSamples(40);
		// mesh.setInnerRadius(40);
		// mesh.setLength(100);
		// mesh.setRadius(50);
		//
		// PipeController pipe = (PipeController) new FXPlantViewFactory()
		// .createController(mesh);
		//
		// pipe.setRotation(0, 0, 2);
		//
		// ShapeMesh box = new ShapeMesh();
		// box.setProperty("Type", "Cube");
		// ShapeController boxC = new ShapeController(box, new
		// FXShapeView(box));
		//
		// output.addEntity(boxC);

		// output.addEntity(pipe);
	}

	private class FXPlantComponentVisitor implements IPlantComponentVisitor {

		/**
		 * The root of the tree of plant parts
		 */
		FXPlantViewRootController root;

		/**
		 * The factory for constructing controllers and views for the meshs.
		 */
		FXPlantViewFactory factory;

		/**
		 * The default constructor.
		 * 
		 * @param root
		 *            The root controller all visited objects will be added to.
		 */
		public FXPlantComponentVisitor(FXPlantViewRootController root) {
			this.root = root;
			factory = new FXPlantViewFactory();
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
					.createController(mesh);

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<AbstractController> pipeList = root
					.getEntitiesByCategory("Pipes");
			pipeList.addAll(root.getEntitiesByCategory("Core Channels"));

			// Add all the input pipes to the junction
			for (PlantComponent input : plantComp.getInputs()) {

				// Whether or not a match was found
				boolean found = false;

				// Check the root to see if a pipe with that id already exists
				for (AbstractController pipe : pipeList) {
					if (Integer.parseInt(pipe.getProperty("Id")) == input
							.getId()) {

						// If found, set up this junction as an output to that
						// pipe
						junction.addEntityByCategory(pipe, "Input");
						pipe.addEntityByCategory(junction, "Output");

						// Match found, stop the search
						found = true;
						break;
					}
				}

				// If a match was not found, create a new pipe
				if (!found) {

					// Convert the pipe into a modeling data structure
					// input.accept(this);
					source.getPlantComponent(input.getId()).accept(this);

					// Refresh the list of pipes
					pipeList = root.getEntitiesByCategory("Pipes");
					pipeList.addAll(
							root.getEntitiesByCategory("Core Channels"));

					// Now that the pipe is guaranteed to be in the root, as it
					// was added when visited, find the pipe with that id and
					// add it
					for (AbstractController pipe : pipeList) {
						if (Integer.parseInt(pipe.getProperty("Id")) == input
								.getId()) {
							junction.addEntityByCategory(pipe, "Input");
							pipe.addEntityByCategory(junction, "Output");
							break;
						}
					}
				}
			}

			// Add all the output pipes to the junction
			for (PlantComponent output : plantComp.getOutputs()) {

				// Whether or not a match was found
				boolean found = false;

				// Check the root to see if a pipe with that id already exists
				for (AbstractController pipe : pipeList) {
					if (Integer.parseInt(pipe.getProperty("Id")) == output
							.getId()) {

						// If found, set up this junction as an input to that
						// pipe
						junction.addEntityByCategory(pipe, "Output");
						pipe.addEntityByCategory(junction, "Input");

						// Match found, stop the search
						found = true;
						break;
					}
				}

				// If a match was not found, create a new pipe
				if (!found) {

					// Convert the pipe into a modeling data structure
					// output.accept(this);
					source.getPlantComponent(output.getId()).accept(this);

					// Refresh the list of pipes
					pipeList = root.getEntitiesByCategory("Pipes");
					pipeList.addAll(
							root.getEntitiesByCategory("Core Channels"));

					// Now that the pipe is gauranteed to be in the root, as it
					// was added when visited, find the pipe with that id and
					// add it
					for (AbstractController pipe : root
							.getEntitiesByCategory("Pipes")) {
						if (Integer.parseInt(pipe.getProperty("Id")) == output
								.getId()) {
							junction.addEntityByCategory(pipe, "Output");
							pipe.addEntityByCategory(junction, "Input");
							break;
						}
					}
				}
			}

			// Add the junction to the root
			root.addEntityByCategory(junction, "Junctions");

		}

		@Override
		public void visit(Reactor plantComp) {

			// Create a new reactor
			ReactorMesh mesh = new ReactorMesh();
			ReactorController reactor = (ReactorController) factory
					.createController(mesh);

			// Simply add the reactor, as the plant view assumes there is only
			// one and the root will take care of adding the core channels.
			root.addEntity(reactor);

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
					.createController(mesh);

			// Get the primary pipe
			Pipe primary = plantComp.getPrimaryPipe();

			// Whether or not a match was found
			boolean found = false;

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<AbstractController> pipeList = root
					.getEntitiesByCategory("Pipes");
			pipeList.addAll(root.getEntitiesByCategory("Core Channels"));

			// Check the root to see if a pipe with that id already exists
			for (AbstractController pipe : pipeList) {
				if (Integer.parseInt(pipe.getProperty("Id")) == primary
						.getId()) {

					// If found, set the pipe as the exchanger's primary
					heatExchanger.setPrimaryPipe((PipeController) pipe);

					// Match found, stop the search
					found = true;
					break;
				}
			}

			// If a match was not found, create a new pipe
			if (!found) {

				// Convert the pipe into a modeling data structure
				// primary.accept(this);
				source.getPlantComponent(primary.getId()).accept(this);

				// Refresh the list of pipes
				pipeList = root.getEntitiesByCategory("Pipes");
				pipeList.addAll(root.getEntitiesByCategory("Core Channels"));

				// Now that the pipe is guaranteed to be in the root, as it was
				// added when visited, find the pipe with that id and add it
				for (AbstractController pipe : pipeList) {
					if (Integer.parseInt(pipe.getProperty("Id")) == primary
							.getId()) {
						heatExchanger.setPrimaryPipe((PipeController) pipe);
						break;
					}
				}
			}

			// Get the secondary pipe
			Pipe secondary = plantComp.getSecondaryPipe();

			found = false;

			// Check the root to see if a pipe with that id already exists
			for (AbstractController pipe : pipeList) {
				if (Integer.parseInt(pipe.getProperty("Id")) == secondary
						.getId()) {

					// If found, set the pipe as the exchanger's primary
					heatExchanger.setSecondaryPipe((PipeController) pipe);

					// Match found, stop the search
					found = true;
					break;
				}
			}

			// If a match was not found, create a new pipe
			if (!found) {

				// Convert the pipe into a modeling data structure
				// primary.accept(this);
				source.getPlantComponent(secondary.getId()).accept(this);

				// Refresh the list of pipes
				pipeList = root.getEntitiesByCategory("Pipes");
				pipeList.addAll(root.getEntitiesByCategory("Core Channels"));

				// Now that the pipe is guaranteed to be in the root, as it was
				// added when visited, find the pipe with that id and add it
				for (AbstractController pipe : pipeList) {
					if (Integer.parseInt(pipe.getProperty("Id")) == secondary
							.getId()) {
						heatExchanger.setSecondaryPipe((PipeController) pipe);
						break;
					}
				}
			}

			// Add the heat exchanger to the root
			root.addEntityByCategory(heatExchanger, "Heat Exchangers");

		}

		@Override
		public void visit(Pipe plantComp) {

			// If this pipe has already been converted, ignore it
			boolean found = false;

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<AbstractController> pipeList = root
					.getEntitiesByCategory("Pipes");
			pipeList.addAll(root.getEntitiesByCategory("Core Channels"));

			// Check the root to see if a pipe with that id already exists
			for (AbstractController pipe : pipeList) {
				if (Integer.parseInt(pipe.getProperty("Id")) == plantComp
						.getId()) {

					// Match found, stop the search
					found = true;
					break;
				}
			}

			if (!found) {

				// Convert the pipe and add it to the root.
				root.addEntityByCategory(createPipe(plantComp), "Pipes");

			}

		}

		@Override
		public void visit(CoreChannel plantComp) {

			// If this pipe has already been converted, ignore it
			boolean found = false;

			// Create a list of all pipes in the plant by combining the pipes
			// with the core channels
			List<AbstractController> pipeList = root
					.getEntitiesByCategory("Pipes");
			pipeList.addAll(root.getEntitiesByCategory("Core Channels"));

			// Check the root to see if a pipe with that id already exists
			for (AbstractController pipe : pipeList) {
				if (Integer.parseInt(pipe.getProperty("Id")) == plantComp
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
				pipe.setProperty("Core Channel", "True");
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
			mesh.setProperty("Id", Integer.toString(plantComp.getId()));
			mesh.setLength(plantComp.getLength() * SCALE);
			mesh.setRadius(plantComp.getRadius() * SCALE);
			mesh.setInnerRadius(plantComp.getRadius() * SCALE);
			mesh.setAxialSamples(plantComp.getNumElements());

			// Create the view and controller
			PipeController pipe = (PipeController) factory
					.createController(mesh);

			// Get the data describing the pipe's location. Position is the
			// center of the pipe's input end, while orientation is a vector
			// from the position which describes the pipe's axis.
			double[] position = plantComp.getPosition();

			// Multiply the positions to the proper scale
			position[0] = position[0] * SCALE;
			position[1] = position[1] * SCALE;
			position[2] = position[2] * SCALE;

			double[] orientation = plantComp.getOrientation();

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
			double pipeLength = plantComp.getLength() * SCALE;
			position[0] += pipeLength / 2 * normalized[0];
			position[1] += pipeLength / 2 * normalized[1];
			position[2] += pipeLength / 2 * normalized[2];

			// Set the pipe's translation
			pipe.setTranslation(position[0], position[1], position[2]);

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
				pipe.setRotation(0, -Math.atan(yRotation),
						-Math.atan(zRotation));
			}

			// Explicitly set the pipe to point down the x, y, or z axis
			else {

				// Rotate the pipe to point down the x axis by rotating about
				// the z
				if (normalized[0] > 0) {
					pipe.setRotation(0, 0, -Math.PI / 2);
				}

				// Rotate in the other direction if the vector is negative
				else if (normalized[0] < 0) {
					pipe.setRotation(0, 0, Math.PI / 2);
				}

				// Rotate the pipe to point down the z axis by rotating about
				// the x
				else if (normalized[2] > 0) {
					pipe.setRotation(Math.PI / 2, 0, 0);
				}

				// Rotate in the other direction if the vector is negative
				else if (normalized[2] < 0) {
					pipe.setRotation(-Math.PI / 2, 0, 0);
				}

				// If the orientation is the negated y vector, flip the tube by
				// 180 degrees about the x axis to turn it upside down. The
				// positive y vector is the tube's default position, and thus
				// does not need to be handled.
				else if (normalized[1] < 0) {
					pipe.setRotation(-Math.PI, 0, 0);
				}
			}

			//
			// // Calculate the amount of z rotation in the formula, applying
			// none
			// // if the normalized vector has a 0 X component. This is done to
			// // avoid division by 0.
			// double zRotation;
			// if (normalized[0] != 0) {
			// zRotation = normalized[1] / normalized[0] + 90;
			// } else {
			// zRotation = 90d;
			// }
			//
			// // The normalized orientation vector can be represented by an
			// // XY-plane angle calculated by arctan(y/x) and an angle from the
			// z
			// // vector, calculated by arccos(z).
			// pipe.setRotation(Math.acos(normalized[2] * 180 / Math.PI), 0,
			// Math.atan(zRotation * 180 / Math.PI));

			return pipe;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener#update(org.eclipse.ice.viz.service.datastructures.
	 * VizObject.IVizUpdateable)
	 */
	@Override
	public void update(IVizUpdateable component) {

		// On receiving an update, refresh the data structure
		refresh();
	}
}
