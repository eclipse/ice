/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.geometry.reactor.Extrema;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionView;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeController;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMeshCategory;
import org.eclipse.eavp.viz.service.modeling.IMesh;
import org.eclipse.eavp.viz.service.modeling.IWireframeView;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.Representation;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

/**
 * A class which manages the graphical representation of a Junction part.
 * 
 * @author Robert Smith
 *
 */
public class FXJunctionView extends JunctionView implements IWireframeView {

	/**
	 * The box which represents the Junction part
	 */
	private Box box;

	/**
	 * The JavaFX node which will contain all shapes for this view
	 */
	private Group node;

	/**
	 * The material for the shapes in this view
	 */
	private PhongMaterial material;

	/**
	 * Whether or not to display the junction in wireframe mode. It will be in
	 * wireframe mode if true or drawn regularly if false.
	 */
	private boolean wireframe;

	/**
	 * The nullary constructor.
	 */
	public FXJunctionView() {
		super();

		// Initialize the date members
		node = new Group();
		material = new PhongMaterial(Color.GRAY);
		wireframe = false;
	}

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The internal model on which this view's representation will be
	 *            based
	 */
	public FXJunctionView(JunctionMesh model) {
		super();

		// Initialize the data members
		node = new Group();
		node.setId(model.getProperty(MeshProperty.NAME));
		material = new PhongMaterial(Color.GRAY);
		wireframe = false;

		// Initialize the mesh
		createMesh(model);
	}

	/**
	 * Create a box which wraps around the correct ends of all the pipes and add
	 * it to the node.
	 * 
	 * @param model
	 *            The junction which will be rendered
	 */
	private void createMesh(IMesh model) {

		// A list of the extrema of all pipe ends
		ArrayList<Extrema> pipeEdges = new ArrayList<Extrema>();

		// Get the bottom end of each input pipe
		for (PipeController input : model.getEntitiesFromCategory(
				ReactorMeshCategory.INPUT, PipeController.class)) {

			// Check if the input is has a pipe or is a pipe
			List<PipeController> primaryPipe = input.getEntitiesFromCategory(
					ReactorMeshCategory.PRIMARY_PIPE, PipeController.class);

			// If the input is a pipe, add its extrema
			if (primaryPipe.isEmpty()) {
				pipeEdges.add(input.getUpperExtrema());
			}

			// Otherwise, get its primary pipe and add that pipe's extrema
			else {
				pipeEdges.add(primaryPipe.get(0).getUpperExtrema());
			}
		}

		// Get the top end of each output pipe
		for (PipeController output : model.getEntitiesFromCategory(
				ReactorMeshCategory.OUTPUT, PipeController.class)) {

			// Check if the output is has a pipe or is a pipe
			List<PipeController> primaryPipe = output.getEntitiesFromCategory(
					ReactorMeshCategory.PRIMARY_PIPE, PipeController.class);

			// If the input is a pipe, add its extrema
			if (primaryPipe.isEmpty()) {
				pipeEdges.add(output.getLowerExtrema());
			}

			// Otherwise, get its primary pipe and add that pipe's extrema
			else {
				pipeEdges.add(primaryPipe.get(0).getLowerExtrema());
			}
		}

		// Get the bounds of the region encompassing all pipe ends
		Extrema boxBounds = new Extrema(pipeEdges);

		// Get the size of the box, which has a minimum size of 1 x 1 x 1 in
		// case the calculated size is smaller, as might be the case if one of
		// the pipes is a secondary Heat Exchanger loop pipe which cannot be
		// drawn until the center of the junction's bounds is known
		centerX = (boxBounds.getMaxX() - boxBounds.getMinX()) / 2
				+ boxBounds.getMinX();
		centerY = (boxBounds.getMaxY() - boxBounds.getMinY()) / 2
				+ boxBounds.getMinY();
		centerZ = (boxBounds.getMaxZ() - boxBounds.getMinZ()) / 2
				+ boxBounds.getMinZ();

		// Create a box which fills the box bounds, adding 3 to each dimension
		// to give enough space between the meshes that they won't clip through
		// each other.
		double sizeX = Math.max(boxBounds.getMaxX() - boxBounds.getMinX(), 1);
		double sizeY = Math.max(boxBounds.getMaxY() - boxBounds.getMinY(), 1);
		double sizeZ = Math.max(boxBounds.getMaxZ() - boxBounds.getMinZ(), 1);
		box = new Box(sizeX + 3, sizeY + 3, sizeZ + 3);

		// Move the box to the center of the bounded area
		box.setTranslateX(centerX);
		box.setTranslateY(centerY);
		box.setTranslateZ(centerZ);

		// Add the box to the scene and set its material
		node.getChildren().add(box);
		box.setMaterial(material);

		// Set the box to the correct wireframe mode
		if (wireframe) {
			box.setDrawMode(DrawMode.LINE);
		} else {
			box.setDrawMode(DrawMode.FILL);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Representation<Group> getRepresentation() {
		return new Representation<Group>(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#refresh(org.eclipse.
	 * ice .viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void refresh(IMesh model) {

		// Remove the old box from the scene
		node.getChildren().remove(box);

		// Create a new box
		createMesh(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.reactor.javafx.datatypes.WireFrameView#
	 * setWireFrameMode(boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {

		// Save the new state
		wireframe = on;

		// Set the box to the proper mode
		if (on) {
			box.setDrawMode(DrawMode.LINE);
		} else {
			box.setDrawMode(DrawMode.FILL);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new AbstractView and make it a copy of this
		FXJunctionView clone = new FXJunctionView();
		clone.copy(this);

		return clone;
	}
}
