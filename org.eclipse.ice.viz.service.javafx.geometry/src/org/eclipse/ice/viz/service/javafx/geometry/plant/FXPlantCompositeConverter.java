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
package org.eclipse.ice.viz.service.javafx.geometry.plant;

import org.eclipse.ice.reactor.plant.Boundary;
import org.eclipse.ice.reactor.plant.Branch;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.DownComer;
import org.eclipse.ice.reactor.plant.FlowJunction;
import org.eclipse.ice.reactor.plant.GeometricalComponent;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.IPlantComponentVisitor;
import org.eclipse.ice.reactor.plant.IdealPump;
import org.eclipse.ice.reactor.plant.Inlet;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.MassFlowInlet;
import org.eclipse.ice.reactor.plant.OneInOneOutJunction;
import org.eclipse.ice.reactor.plant.Outlet;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PipeToPipeJunction;
import org.eclipse.ice.reactor.plant.PipeWithHeatStructure;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.PointKinetics;
import org.eclipse.ice.reactor.plant.Pump;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SeparatorDryer;
import org.eclipse.ice.reactor.plant.SolidWall;
import org.eclipse.ice.reactor.plant.SpecifiedDensityAndVelocityInlet;
import org.eclipse.ice.reactor.plant.Subchannel;
import org.eclipse.ice.reactor.plant.SubchannelBranch;
import org.eclipse.ice.reactor.plant.TDM;
import org.eclipse.ice.reactor.plant.TimeDependentJunction;
import org.eclipse.ice.reactor.plant.TimeDependentVolume;
import org.eclipse.ice.reactor.plant.Turbine;
import org.eclipse.ice.reactor.plant.Valve;
import org.eclipse.ice.reactor.plant.VolumeBranch;
import org.eclipse.ice.reactor.plant.WetWell;
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
public class FXPlantCompositeConverter implements IVizUpdateableListener {

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

			// Add all the input pipes to the junction
			for (PlantComponent input : plantComp.getInputs()) {

				// Whether or not a match was found
				boolean found = false;

				// Check the root to see if a pipe with that id already exists
				for (AbstractController pipe : root
						.getEntitiesByCategory("Pipes")) {
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
					input.accept(this);

					// Now that the pipe is guaranteed to be in the root, as it
					// was added when visited, find the pipe with that id and
					// add it
					for (AbstractController pipe : root
							.getEntitiesByCategory("Pipes")) {
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
				for (AbstractController pipe : root
						.getEntitiesByCategory("Pipes")) {
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
					output.accept(this);

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

			// Check the root to see if a pipe with that id already exists
			for (AbstractController pipe : root
					.getEntitiesByCategory("Pipes")) {
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
				primary.accept(this);

				// Now that the pipe is guaranteed to be in the root, as it was
				// added when visited, find the pipe with that id and add it
				for (AbstractController pipe : root
						.getEntitiesByCategory("Pipes")) {
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
			for (AbstractController pipe : root
					.getEntitiesByCategory("Pipes")) {
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
				primary.accept(this);

				// Now that the pipe is guaranteed to be in the root, as it was
				// added when visited, find the pipe with that id and add it
				for (AbstractController pipe : root
						.getEntitiesByCategory("Pipes")) {
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

			// Convert the pipe and add it to the root.
			addPipe(plantComp);
		}

		@Override
		public void visit(CoreChannel plantComp) {

			// Convert the pipe and add it to the root
			PipeController pipe = addPipe(plantComp);

			// Set the pipe as a core channel and add it to the root again, this
			// time in the core channel category
			pipe.setProperty("Core Channel", "True");
			root.addEntityByCategory(pipe, "Core Channels");

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
		 * Creates a JavaFX PipeController from a RELAP7 Pipe and adds it to the
		 * visitor's root node.
		 * 
		 * @param plantComp
		 *            The pipe to be converted.
		 * @return The converted pipe.
		 */
		public PipeController addPipe(Pipe plantComp) {
			// Create a new pipe
			PipeMesh mesh = new PipeMesh();
			PipeController pipe = (PipeController) factory
					.createController(mesh);

			// Set the pipe's properties
			pipe.setProperty("Id", Integer.toString(plantComp.getId()));
			pipe.setLength(plantComp.getLength());
			pipe.setRadius(plantComp.getRadius());
			pipe.setInnerRadius(plantComp.getRadius());
			pipe.setAxialSamples(plantComp.getNumElements());

			// Get the data describing the pipe's location. Position is the
			// center of the pipe's input end, while orientation is a vector
			// from the position which describes the pipe's axis.
			double[] position = plantComp.getPosition();
			double[] orientation = plantComp.getOrientation();

			// Normalize the orientation vector
			double[] normalized = new double[3];
			double length = Math.sqrt(
					Math.pow(orientation[0], 2) + Math.pow(orientation[1], 2)
							+ Math.pow(orientation[2], 2));
			normalized[0] = normalized[0] / length;
			normalized[1] = normalized[1] / length;
			normalized[2] = normalized[2] / length;

			// The tube is, by default, centered on the origin. Stepping one
			// half its length in the direction of the orientation vector will
			// place the output edge's center on the origin, so that the
			// position vector now properly represents the movement from the
			// origen to the pipe's position.
			position[0] += plantComp.getLength() / 2 * normalized[0];
			position[1] += plantComp.getLength() / 2 * normalized[1];
			position[2] += plantComp.getLength() / 2 * normalized[2];

			// Set the pipe's translation
			pipe.setTranslation(position[0], position[1], position[2]);

			// The normalized orientation vector can be represented by an
			// XY-plane angle calculated by arctan(y/x) and an angle from the z
			// vector, calculated by arccos(z)
			pipe.setRotation(Math.acos(normalized[2]), 0,
					Math.atan(normalized[1] / normalized[2]));

			// Add the pipe to the root
			root.addEntityByCategory(pipe, "Pipes");

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
