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

import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeView;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.Extrema;
import org.eclipse.eavp.viz.service.geometry.reactor.IPipeView;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * A class managing the JavaFX graphical representation of a Pipe part.
 * 
 * @author Robert Smith
 *
 */
public class FXPipeView extends FXShapeView implements IPipeView {

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
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.reactor.IPipeView#getLowerExtrema()
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
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.reactor.IPipeView#getUpperExtrema()
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
	 * org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeView#refresh
	 * (org.eclipse.eavp.viz.service.modeling.AbstractMesh)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractView#clone()
	 */
	@Override
	public Object clone() {
		FXPipeView clone = new FXPipeView();
		clone.copy(this);

		// Force an update from the transformation
		clone.transformation.setSize(clone.transformation.getSize());
		return clone;
	}

}
