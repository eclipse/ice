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
package org.eclipse.eavp.viz.service.javafx.mesh.datatypes;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.javafx.internal.Util;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * A class which provides a JavaFX graphical representation for a Vertex.
 * 
 * @author Robert Smith
 *
 */
public class FXVertexView extends AbstractView {

	/**
	 * A group containing the shape which represents the part and a gizmo which
	 * modifies the shape's appearance
	 */
	private Group node;

	/** */
	private Sphere mesh;

	/** */
	private PhongMaterial defaultMaterial;

	/** */
	private PhongMaterial selectedMaterial;

	/**
	 * The Material to display when the Vertex is first being made
	 */
	private PhongMaterial constructingMaterial;

	/**
	 * The scale of the application the vertex will be displayed in. The vertex
	 * will be drawn with each coordinate's value multiplied by the scale
	 */
	private int scale;

	/**
	 * The nullary constructor.
	 */
	public FXVertexView() {
		super();

		// Instantiate the class variables
		node = new Group();
		scale = 1;

		// Create the materials
		defaultMaterial = new PhongMaterial(Color.rgb(80, 30, 140));
		selectedMaterial = new PhongMaterial(Color.rgb(0, 127, 255));
		constructingMaterial = new PhongMaterial(Color.rgb(0, 255, 0));

	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model which this view will display
	 */
	public FXVertexView(VertexMesh model) {
		this();

		// Set the node's name
		node.setId(model.getProperty("Name"));

		// Center the node on the vertex's location
		transformation.setTranslation(model.getX(), model.getY(), 0);

		// Flatten the sphere into a circle
		transformation.setScale(1, 1, 0.75);

		// Set the node's transformation
		node.getTransforms().setAll(Util.convertTransformation(transformation));

		// Create a Shape3D for the model
		mesh = new Sphere(1);

		// Set the sphere to be the constructing material by default
		mesh.setMaterial(constructingMaterial);
		node.getChildren().add(mesh);
	}

	/**
	 * Get the scale of the application the view is drawn in.
	 * 
	 * @return The conversion rate between internal units and JavaFX units
	 */
	public int getApplicationScale() {
		return scale;
	}

	/**
	 * Associates the view's controller with the representation's data
	 * structure, so that user interactions with the displayed shape will have a
	 * way be associated back to the controller.
	 * 
	 * @param controller
	 *            This view's controller
	 */
	public void setController(AbstractController controller) {

		// Put the controller in the node's data structure
		node.getProperties().put(ShapeController.class, mesh);
	}

	/**
	 * Sets the scale of the application this vertex will be displayed in. The
	 * vertex will now be drawn with all the coordinates in the VertexMesh
	 * multiplied by the scale.
	 * 
	 * @param scale
	 *            The conversion factor between JavaFX units and the logical
	 *            units used by the application.
	 */
	public void setApplicationScale(int scale) {
		this.scale = scale;

		// Notify listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Object getRepresentation() {
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#refresh(org.eclipse.ice
	 * .viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void refresh(AbstractMesh model) {

		// Center the node on the vertex's location
		transformation.setTranslation(((VertexMesh) model).getX() * scale,
				((VertexMesh) model).getY() * scale, 0);

		// Set the node's transformation
		node.getTransforms().setAll(Util.convertTransformation(transformation));

		// If the vertex is under construction, leave the material unchanged,
		// otherwise set it based on whether or not the vertex is selected
		if (!"True".equals(model.getProperty("Constructing"))) {

			// If the part is selected, set the selected material
			if ("True".equals(model.getProperty("Selected"))) {
				mesh.setMaterial(selectedMaterial);
			}

			// Otherwise set the non-selected material
			else {
				mesh.setMaterial(defaultMaterial);
			}
		}

		else {
			mesh.setMaterial(constructingMaterial);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractView#clone()
	 */
	@Override
	public Object clone() {
		FXVertexView clone = new FXVertexView();
		clone.copy(this);

		// Force an update from the transformation
		clone.transformation.setSize(clone.transformation.getSize());
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#update(org.eclipse.ice.
	 * viz.service.datastructures.VizObject.IVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {

		// If the transformation updated, update the JavaFX transformation
		if (component == transformation) {
			// Set the node's transformation
			node.getTransforms()
					.setAll(Util.convertTransformation(transformation));
		}

		super.update(component, type);

	}
}
