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

import org.eclipse.ice.viz.service.geometry.reactor.Extrema;
import org.eclipse.ice.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.ice.viz.service.geometry.reactor.PipeView;
import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.javafx.geometry.datatypes.FXShapeView;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.IWireFramePart;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * A class managing the JavaFX graphical representation of a Pipe part.
 * 
 * @author Robert Smith
 *
 */
public class FXPipeView extends FXShapeView
		implements PipeView, IWireFramePart {

	/**
	 * The nullary constructor
	 */
	public FXPipeView() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The internal representation of the part which will be
	 *            rendered.
	 */
	public FXPipeView(PipeMesh model) {
		super(model);

		// Pipes are cyan by default
		if (!"True".equals(model.getProperty("Core Channel"))) {
			PhongMaterial material = new PhongMaterial(Color.CYAN);
			material.setSpecularColor(Color.WHITE);
			setMaterial(material);
		}

		// Core channels are red
		else {
			PhongMaterial material = new PhongMaterial(Color.RED);
			material.setSpecularColor(Color.WHITE);
			setMaterial(material);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.reactor.javafx.datatypes.PipeView#
	 * getLowerExtrema()
	 */
	@Override
	public Extrema getLowerExtrema() {

		// Get the mesh's lower boundary and calculate its extrema
		float[] points = tubeShape.getLowerBoundary();
		return calculateExtrema(points);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.reactor.javafx.datatypes.PipeView#
	 * getUpperExtrema()
	 */
	@Override
	public Extrema getUpperExtrema() {

		// Get the mesh's lower boundary and calculate its extrema
		float[] points = tubeShape.getUpperBoundary();
		return calculateExtrema(points);
	}

	/**
	 * Calculate the extrema of a series of points after this view's
	 * transformation is applied to them
	 * 
	 * @param points
	 * @return
	 */
	private Extrema calculateExtrema(float[] points) {

		// Get the transformation's parameters
		double[] rotation = transformation.getRotation();
		double[] scale = transformation.getScale();
		double size = transformation.getSize();
		double[] skew = transformation.getSkew();
		double[] translation = transformation.getTranslation();

		// Consider each point one at a time
		for (int i = 0; i < points.length / 3; i++) {

			// Save the original values for use in the skew calculation
			float origX = points[i * 3];
			float origY = points[i * 3 + 1];
			float origZ = points[i * 3 + 2];

			// Apply size and scale to the points
			points[i * 3] = (float) (points[i * 3] * size * scale[0]);
			points[i * 3 + 1] = (float) (points[i * 3 + 1] * size * scale[1]);
			points[i * 3 + 2] = (float) (points[i * 3 + 2] * size * scale[2]);

			// Apply the rotation to the point
			float x = points[i * 3];
			float y = points[i * 3 + 1];
			float z = points[i * 3 + 2];

			// Rotate about the z axis
			float tempY = (float) (x * Math.sin(rotation[2])
					+ y * Math.cos(rotation[2]));
			x = (float) (x * Math.cos(rotation[2]) - y * Math.sin(rotation[2]));
			y = tempY;

			// Rotate about the y axis
			float tempX = (float) (z * Math.sin(rotation[1])
					+ x * Math.cos(rotation[1]));
			z = (float) (z * Math.cos(rotation[1]) - x * Math.sin(rotation[1]));
			x = tempX;

			// Rotate about the x axis
			tempY = (float) (y * Math.cos(rotation[0])
					- z * Math.sin(rotation[0]));
			z = (float) (y * Math.sin(rotation[0]) + z * Math.cos(rotation[0]));
			y = tempY;

			// Apply the skew and save the calculated values back to the array
			points[i * 3] = (float) (x + origY * skew[1] + origZ * skew[2]);
			points[i * 3 + 1] = (float) (y + origX * skew[0] + origZ * skew[2]);
			points[i * 3 + 2] = (float) (z + origX * skew[0] + origY * skew[1]);

			// Apply translation to each coordinate
			points[i * 3] = (float) (points[i * 3] + translation[0]);
			points[i * 3 + 1] = (float) (points[i * 3 + 1] + translation[1]);
			points[i * 3 + 2] = (float) (points[i * 3 + 2] + translation[2]);

		}

		// Initialize the extrema with the first point
		float minX = points[0];
		float minY = points[1];
		float minZ = points[2];
		float maxX = points[0];
		float maxY = points[1];
		float maxZ = points[2];

		// Compare each point to the current extrema, setting the
		// minimum/maximum values if they are lower/higher
		for (int i = 0; i < points.length / 3; i++) {

			if (minX > points[i * 3]) {
				minX = points[i * 3];
			}

			else if (maxX < points[i * 3]) {
				maxX = points[i * 3];
			}

			if (minY > points[i * 3 + 1]) {
				minY = points[i * 3 + 1];
			}

			else if (maxY < points[i * 3 + 1]) {
				maxY = points[i * 3 + 1];
			}

			if (minZ > points[i * 3 + 2]) {
				minZ = points[i * 3 + 2];
			}

			else if (maxZ < points[i * 3 + 2]) {
				maxZ = points[i * 3 + 2];
			}
		}

		return new Extrema(minX, maxX, minY, maxY, minZ, maxZ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.javafx.geometry.datatypes.FXShapeView#refresh
	 * (org.eclipse.ice.viz.service.modeling.AbstractMesh)
	 */
	@Override
	public void refresh(AbstractMesh model) {

		// Pipes are cyan by default
		if (!"True".equals(model.getProperty("Core Channel"))) {
			PhongMaterial material = new PhongMaterial(Color.CYAN);
			material.setSpecularColor(Color.WHITE);
			setMaterial(material);
		}

		// Core channels are red
		else {
			PhongMaterial material = new PhongMaterial(Color.RED);
			material.setSpecularColor(Color.WHITE);
			setMaterial(material);
		}

		super.refresh(model);

	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// org.eclipse.ice.viz.service.modeling.AbstractView#refresh(org.eclipse.ice
	// * .viz.service.modeling.AbstractMeshComponent)
	// */
	// @Override
	// public void refresh(AbstractMeshComponent model) {
	//
	// //Remove the current mesh from the node
	// node.getChildren().remove(mesh);
	//
	// // Cast the model as a PipeComponent and get the parameters
	// PipeComponent pipe = (PipeComponent) model;
	// int axialSamples = pipe.getAxialSamples();
	// double height = pipe.getLength();
	// double outerRadius = pipe.getRadius();
	// double innerRadius = pipe.getInnerRadius();
	//
	// // Create the mesh
	// mesh = new FXTubeMesh(height, innerRadius, outerRadius, axialSamples,
	// 50);
	//
	// super.refresh(model);
	//
	// // Set the node's transformation and children
	// node.getTransforms().setAll(Util.convertTransformation(transformation));
	// node.getChildren().add(mesh.getMesh());
	// }

	@Override
	protected void createShape(AbstractMesh model, ShapeType type) {
		super.createShape(model, type);

		Extrema e = getLowerExtrema();
		Sphere sphere = new Sphere(e.getMaxZ() - e.getMinZ());
		// sphere.setTranslateX((e.getMaxX() - e.getMinX() / 2 + e.getMinX()));
		// sphere.setTranslateY((e.getMaxY() - e.getMinY() / 2 + e.getMinY()));
		// sphere.setTranslateZ((e.getMaxZ() - e.getMinZ() / 2 + e.getMinZ()));
		sphere.setTranslateY(((PipeMesh) model).getLength() / 2);
		node.getChildren().add(sphere);
	}

}
