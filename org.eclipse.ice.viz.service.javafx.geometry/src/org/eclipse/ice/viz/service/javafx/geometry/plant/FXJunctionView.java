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
package org.eclipse.ice.viz.service.javafx.geometry.plant;

import java.util.ArrayList;

import org.eclipse.ice.viz.service.geometry.reactor.Extrema;
import org.eclipse.ice.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.ice.viz.service.geometry.reactor.JunctionView;
import org.eclipse.ice.viz.service.geometry.reactor.PipeController;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.IWireFramePart;

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
public class FXJunctionView extends JunctionView implements IWireFramePart {

	/**
	 * The box which represents the Junction part
	 */
	Box box;

	/**
	 * The JavaFX node which will contain all shapes for this view
	 */
	Group node;

	/**
	 * The material for the shapes in this view
	 */
	PhongMaterial material;

	/**
	 * The nullary constructor.
	 */
	public FXJunctionView() {
		super();

		// Initialize the date members
		node = new Group();
		material = new PhongMaterial(Color.GRAY);
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
		node.setId(model.getProperty("Name"));
		material = new PhongMaterial(Color.GRAY);

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
	private void createMesh(AbstractMesh model) {

		// A list of the extrema of all pipe ends
		ArrayList<Extrema> pipeEdges = new ArrayList<Extrema>();

		// Get the bottom end of each input pipe
		for (AbstractController input : model.getEntitiesByCategory("Input")) {
			pipeEdges.add(((PipeController) input).getLowerExtrema());
		}

		// Get the top end of each output pipe
		for (AbstractController output : model
				.getEntitiesByCategory("Output")) {
			pipeEdges.add(((PipeController) output).getUpperExtrema());
		}

		// Get the bounds of the region encompassing all pipe ends
		Extrema boxBounds = new Extrema(pipeEdges);

		// Get the size of the box, which has a minimum size of 1 x 1 x 1 in
		// case the calculated size is smaller, as might be the case if one of
		// the pipes is a secondary Heat Exchanger loop pipe which cannot be
		// drawn until the center of the junction's bounds is known
		centerX = Math.max(boxBounds.getMaxX() - boxBounds.getMinX(), 1);
		centerY = Math.max(boxBounds.getMaxY() - boxBounds.getMinY(), 1);
		centerZ = Math.max(boxBounds.getMaxZ() - boxBounds.getMinZ(), 1);

		// Create a box which fills the box bounds, adding 1 to each dimension
		// to give enough space between the meshes that they won't clip through
		// each other.
		box = new Box(centerX + 1, centerY + 1, centerZ + 1);

		// Move the box to the center of the bounded area
		box.setTranslateX((boxBounds.getMaxX() - boxBounds.getMinX()) / 2
				+ boxBounds.getMinX());
		box.setTranslateY((boxBounds.getMaxY() - boxBounds.getMinY()) / 2
				+ boxBounds.getMinY());
		box.setTranslateZ((boxBounds.getMaxZ() - boxBounds.getMinZ()) / 2
				+ boxBounds.getMinZ());

		// Add the box to the scene and set its material
		node.getChildren().add(box);
		box.setMaterial(material);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Object getRepresentation() {
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#refresh(org.eclipse.ice
	 * .viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void refresh(AbstractMesh model) {

		// Remove the old box from the scene
		node.getChildren().remove(box);

		// Create a new box
		createMesh(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.reactor.javafx.datatypes.WireFrameView#
	 * setWireFrameMode(boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {

		// Set the box to the proper mode
		if (on) {
			box.setDrawMode(DrawMode.LINE);
		} else {
			box.setDrawMode(DrawMode.FILL);
		}

	}
}
