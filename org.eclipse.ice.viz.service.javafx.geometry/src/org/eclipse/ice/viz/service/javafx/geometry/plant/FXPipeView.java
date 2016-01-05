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
import org.eclipse.ice.viz.service.geometry.reactor.JunctionController;
import org.eclipse.ice.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.ice.viz.service.geometry.reactor.PipeView;
import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.javafx.geometry.datatypes.FXShapeView;
import org.eclipse.ice.viz.service.javafx.geometry.datatypes.FXTube;
import org.eclipse.ice.viz.service.javafx.internal.Util;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.IWireFramePart;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

/**
 * A class managing the JavaFX graphical representation of a Pipe part.
 * 
 * @author Robert Smith
 *
 */
public class FXPipeView extends FXShapeView
		implements PipeView, IWireFramePart {

	/**
	 * A separating wall drawn around Heat Exchanger pipes.
	 */
	Box wall;

	/**
	 * The inlet for the secondary loop of Heat Exchanger pipes
	 */
	FXTube secondaryInlet;

	/**
	 * A view on the secondary inlet's mesh
	 */
	MeshView inletView;

	/**
	 * The outlet for the secondary loop of Heat Exchanger pipes
	 */
	FXTube secondaryOutlet;

	/**
	 * A view on the secondary outlet's mesh
	 */
	MeshView outletView;

	/**
	 * The nullary constructor
	 */
	public FXPipeView() {
		super();

		defaultMaterial = new PhongMaterial(Color.CYAN);
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
		float[] points = tubeShape.getLowerBoundary();
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

		// TODO Apply skew from the transformation
		// Consider each point one at a time
		for (int i = 0; i < points.length / 3; i++) {

			// Save the original values for use in the skew calculation
			float origX = points[i * 3];
			float origY = points[i * 3 + 1];
			float origZ = points[i * 3 + 2];

			// Apply size and scale to the points
			points[i * 3] = (float) (points[i * 3] * size * scale[0]);
			points[i * 3 + 1] = (float) (points[i * 3] * size * scale[1]);
			points[i * 3 + 2] = (float) (points[i * 3] * size * scale[2]);

			// Apply the rotation to the point
			float x = points[i * 3];
			float y = points[i * 3 + 1];
			float z = points[i * 3 + 2];

			// Rotate about the x axis
			float tempY = (float) (y * Math.cos(rotation[0])
					- z * Math.sin(rotation[0]));
			z = (float) (y * Math.sin(rotation[0]) - z * Math.cos(rotation[0]));
			y = tempY;

			// Rotate about the y axis
			float tempX = (float) (z * Math.sin(rotation[1])
					- x * Math.cos(rotation[1]));
			z = (float) (z * Math.cos(rotation[1]) - x * Math.sin(rotation[1]));
			x = tempX;

			// Rotate about the z axis
			tempY = (float) (x * Math.sin(rotation[2])
					- y * Math.cos(rotation[2]));
			x = (float) (x * Math.cos(rotation[2]) - y * Math.sin(rotation[2]));
			y = tempY;

			// Apply the skew and save the calculated values back to the array
			points[i * 3] = (float) (x + origY * skew[1] + origZ * skew[2]);
			points[i * 3 + 1] = (float) (y + origX * skew[0] + origZ * skew[2]);
			points[i * 3 + 2] = (float) (z + origX * skew[0] + origY * skew[1]);

			// Apply translation to each coordinate
			points[i * 3] = (float) (points[i * 3] + translation[0]);
			points[i * 3 + 1] = (float) (points[i * 3] + translation[1]);
			points[i * 3 + 2] = (float) (points[i * 3] + translation[2]);

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

	/**
	 * Creates a tube mesh from a heat exchanger's wall to the specified point.
	 * The created pipe will have the same radius as the primary pipe, will
	 * intersect with the primary pipe perpendicularly within the wall, and its
	 * other end will be centered on the given point. The tube's mesh will be
	 * set as a child to the view's node, with defaultMaterial set as its
	 * material.
	 * 
	 * @param point
	 *            The point at which to center the tube's non-intersecting end
	 * @param model
	 *            The model containing the information about the pipe's physical
	 *            characteristics, which will be copied for the new tube
	 * @return A new tube mesh adhering to the above specifications
	 */
	private FXTube createTubeToPoint(double[] point, PipeMesh model) {

		// Get the primary tube's start point
		Extrema start = getLowerExtrema();
		double[] startPoint = new double[3];
		startPoint[0] = (start.getMaxX() - start.getMinX()) / 2;
		startPoint[1] = (start.getMaxY() - start.getMinY()) / 2;
		startPoint[2] = (start.getMaxZ() - start.getMinZ()) / 2;

		// Get the primary tube's end point
		Extrema end = getUpperExtrema();
		double[] endPoint = new double[3];
		endPoint[0] = (end.getMaxX() - end.getMinX()) / 2;
		endPoint[1] = (end.getMaxY() - end.getMinY()) / 2;
		endPoint[2] = (end.getMaxZ() - end.getMinZ()) / 2;

		// Calculate the direction of the axis
		double[] axis = new double[3];
		axis[0] = endPoint[0] - startPoint[0];
		axis[1] = endPoint[1] - startPoint[1];
		axis[2] = endPoint[2] - startPoint[2];

		// Calculate the point 1/10th of its length along the axis from the
		// start point
		double[] wallStart = new double[3];
		wallStart[0] = startPoint[0] + (axis[0] * 0.1d);
		wallStart[1] = startPoint[1] + (axis[1] * 0.1d);
		wallStart[2] = startPoint[2] + (axis[2] * 0.1d);

		// Calculate the point 1/10th of its length along the axis from the end
		// point
		double[] wallEnd = new double[3];
		wallEnd[0] = endPoint[0] - (axis[0] * 0.1d);
		wallEnd[1] = endPoint[1] - (axis[1] * 0.1d);
		wallEnd[2] = endPoint[2] - (axis[2] * 0.1d);

		// Get the vector representing the axis segment from one end of the wall
		// to the other
		double[] primaryVector = new double[3];
		primaryVector[0] = wallEnd[0] - wallStart[0];
		primaryVector[1] = wallEnd[1] - wallStart[1];
		primaryVector[2] = wallEnd[2] - wallStart[2];

		// Calculate the squared magnitude of the primary pipe's axis
		double axisMag = Math.pow(primaryVector[0], 2)
				+ Math.pow(primaryVector[1], 2) + Math.pow(primaryVector[2], 2);

		// Get the vector from the start point to the target point
		double[] targetVector = new double[3];
		targetVector[0] = point[0] - startPoint[0];
		targetVector[1] = point[1] - startPoint[1];
		targetVector[2] = point[2] - startPoint[2];

		// Calculate the dot product with between the axis and target vector
		double dotProduct = primaryVector[0] * targetVector[0]
				+ primaryVector[1] * targetVector[1]
				+ primaryVector[2] * targetVector[2];

		// Get the normalized length to the intersection point
		double step = dotProduct / axisMag;

		// Calculate the intersection point by stepping along the axis from the
		// start of the wall
		double[] intersection = new double[3];
		intersection[0] = wallStart[0] + primaryVector[0] * step;
		intersection[1] = wallStart[1] + primaryVector[1] * step;
		intersection[2] = wallStart[2] + primaryVector[2] * step;

		// Get the line between the intersection and the target point
		Point3D start3D = new Point3D(intersection[0], intersection[1],
				intersection[2]);
		Point3D end3D = new Point3D(point[0], point[1], point[2]);
		Point3D line = end3D.subtract(start3D);

		// Get the axis of rotation for the cylinder
		Point3D axisOfRotation = line.crossProduct(0f, 1f, 0f);

		// Calculate the number of degrees to rotate about the axis.
		double rotationAmount = Math.acos(line.normalize().dotProduct(0, 1, 0));

		// Apply the rotation to the cylinder
		Rotate rotation = new Rotate(-Math.toDegrees(rotationAmount),
				axisOfRotation);

		// Create a new tube that is long enough to reach from the intersection
		// point to the target and as wide as the primary pipe
		FXTube tube = new FXTube(
				Math.sqrt(Math.pow(intersection[0] + point[0], 2)
						+ Math.pow(intersection[1] + point[1], 2)
						+ Math.pow(intersection[2] + point[2], 2)),
				model.getRadius(), model.getRadius(), model.getAxialSamples(),
				50);

		// Create a rotation to restore the tube to the default position after
		// the Pipe's transformation is applied, so that the rotation calculated
		// above will rotate it correctly
		double[] nodeRotation = transformation.getRotation();
		Rotate reverseRotation = Util.eulerToRotate(nodeRotation[0] * -1d,
				nodeRotation[0] * -1d, nodeRotation[0] * -1d);

		// Create a view on the mesh, apply the rotations and material, and add
		// it to the node
		MeshView tubeView = new MeshView(tube.getMesh());
		tubeView.getTransforms().setAll(rotation, reverseRotation);
		tubeView.setMaterial(defaultMaterial);
		node.getChildren().add(tubeView);

		return tube;

		// //Calculate the vector of the line from the intersection point to the
		// target
		// double[] newAxis = new double[3];
		// newAxis[0] = point[0] - intersection[0];
		// newAxis[1] = point[1] - intersection[1];
		// newAxis[2] = point[2] - intersection[2];
		//
		// //Take the cross product of the desired axis with the y axis
		// double[] crossProduct = new double[3];
		// crossProduct[0] = newAxis[2] * -1d;
		// crossProduct[1] = 0;
		// crossProduct[2] = newAxis[0];
		//
		// //Get the magnitude of the cross produce
		// double crossMag = Math.pow(crossProduct[0], 2) +
		// Math.pow(crossProduct[2], 2);
		//
		// //Normalize the cross product
		// double[] normal = new double[3];
		// normal[0] = crossProduct[0] / crossMag;
		// normal[1] = 0;
		// normal[2] = crossProduct[2] / crossMag;
		//
		// dotProduct[0] =
		//
		// // Calculate the number of degrees to rotate about the axis.
		// double rotationAmount = Math
		// .acos(angle.normalize().dotProduct(0, 1, 0));
		//
		// // Apply the rotation to the cylinder
		// Rotate rotation = new Rotate(-Math.toDegrees(rotationAmount), axis);
		// edge.getTransforms().addAll(rotation);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.geometry.shapes.FXShapeView#createShape(org.
	 * eclipse.ice.viz.service.modeling.AbstractMeshComponent,
	 * org.eclipse.ice.viz.service.geometry.shapes.ShapeType)
	 */
	@Override
	protected void createShape(AbstractMesh model, ShapeType type) {
		super.createShape(model, type);

		// Heat exchangers have three more shapes to display
		if (((PipeMesh) model).getPipeType() == PipeType.HEAT_EXCHANGER) {

			// Remove the extra parts from the node
			node.getChildren().remove(wall);
			node.getChildren().remove(secondaryInlet);
			node.getChildren().remove(secondaryOutlet);

			// Create the wall around the primary pipe
			double wallSize = ((PipeMesh) model).getRadius() * 4;
			wall = new Box(wallSize, ((PipeMesh) model).getLength() * 0.8d,
					wallSize);

			// Create the secondary inlet
			AbstractController inletJunction = model
					.getEntitiesByCategory("Secondary Input").get(0);
			secondaryInlet = createTubeToPoint(
					((JunctionController) inletJunction).getCenter(),
					(PipeMesh) model);

			// Create the secondary outlet
			AbstractController outletJunction = model
					.getEntitiesByCategory("Secondary Output").get(0);
			secondaryOutlet = createTubeToPoint(
					((JunctionController) outletJunction).getCenter(),
					(PipeMesh) model);

			// Set the primary pipe's color
			defaultMaterial = new PhongMaterial(Color.BLUE);
			shape.setMaterial(defaultMaterial);

			// Add the wall to the scene
			wall.setMaterial(defaultMaterial);
			node.getChildren().add(wall);

			// Add the secondary pipes to the scene
			inletView = new MeshView(secondaryInlet.getMesh());
			inletView.setMaterial(defaultMaterial);
			node.getChildren().add(inletView);
			outletView = new MeshView(secondaryOutlet.getMesh());
			outletView.setMaterial(defaultMaterial);
			node.getChildren().add(outletView);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.reactor.javafx.datatypes.WireFrameView#
	 * setWireFrameMode(boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {

		// Set each of the pieces to line mode
		if (on) {
			shape.setDrawMode(DrawMode.LINE);

			// If wall exists, the secondary pipe should as well, so set them
			// all
			if (wall != null) {
				wall.setDrawMode(DrawMode.LINE);
				inletView.setDrawMode(DrawMode.LINE);
				outletView.setDrawMode(DrawMode.LINE);
			}

			// Set each of the pieces to fill mode
		} else {
			shape.setDrawMode(DrawMode.FILL);

			// If wall exists, the secondary pipe should as well, so set them
			// all
			if (wall != null) {
				wall.setDrawMode(DrawMode.FILL);
				inletView.setDrawMode(DrawMode.FILL);
				outletView.setDrawMode(DrawMode.FILL);
			}
		}

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

}
