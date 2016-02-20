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
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXTube;
import org.eclipse.eavp.viz.service.javafx.internal.Util;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.IWireFramePart;
import org.eclipse.eavp.viz.service.geometry.reactor.Extrema;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

/**
 * A JavaFX view for a HeatExchanger part.
 * 
 * @author Robert Smith
 *
 */
public class FXHeatExchangerView extends AbstractView
		implements IWireFramePart {

	/**
	 * A group containing the shape which represents the part.
	 */
	protected Group node;

	/**
	 * A separating wall drawn around Heat Exchanger pipes.
	 */
	Box wall;

	/**
	 * A node containing the primary pipe's mesh.
	 */
	Group primaryPipe;

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
	 * Whether to display this part in wireframe mode. It will be displayed as a
	 * wireframe if true or as a solid if false.
	 */
	boolean wireframe;

	/**
	 * The nullary constructor.
	 */
	public FXHeatExchangerView() {
		super();

		// Initialize the node
		node = new Group();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model to be rendered.
	 */
	public FXHeatExchangerView(AbstractMesh model) {
		super();

		// Initialize the node
		node = new Group();

		// Render shapes based on the model
		refresh(model);

	}

	/**
	 * Creates a tube mesh from a heat exchanger's wall to the specified point.
	 * The created pipe will have the same radius as the primary pipe, will
	 * intersect with the primary pipe perpendicularly within the wall, and its
	 * other end will be centered on the given point. The tube's mesh will be
	 * set as a child to the view's node, with a blue material.
	 * 
	 * This function returns the FXTube as a return value and writes the
	 * MeshView constructed from that FXTube's data to the "view" argument.
	 * 
	 * @param point
	 *            The point at which to center the tube's non-intersecting end
	 * @param model
	 *            The model containing the information about the pipe's physical
	 *            characteristics, which will be copied for the new tube
	 * @param view
	 *            A MeshView for JavaFX representation of the new tube. The
	 *            pipe's graphical view will be saved to this object.
	 * @param wireframe
	 *            Whether or not to render the MeshView as a wireframe. If true,
	 *            it will display as a wireframe, otherwise it will display as a
	 *            solid.
	 * @return A new tube mesh adhering to the above specifications
	 */
	private FXTube createTubeToPoint(double[] point, HeatExchangerMesh model,
			MeshView view, boolean wireframe) {

		// Get the primary tube's start point
		Extrema start = model.getPrimaryPipe().getLowerExtrema();
		double[] startPoint = new double[3];
		startPoint[0] = (start.getMaxX() - start.getMinX()) / 2
				+ start.getMinX();
		startPoint[1] = (start.getMaxY() - start.getMinY()) / 2
				+ start.getMinY();
		startPoint[2] = (start.getMaxZ() - start.getMinZ()) / 2
				+ start.getMinZ();

		// Get the primary tube's end point
		Extrema end = model.getPrimaryPipe().getUpperExtrema();
		double[] endPoint = new double[3];
		endPoint[0] = (end.getMaxX() - end.getMinX()) / 2 + end.getMinX();
		endPoint[1] = (end.getMaxY() - end.getMinY()) / 2 + end.getMinY();
		endPoint[2] = (end.getMaxZ() - end.getMinZ()) / 2 + end.getMinZ();

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
		targetVector[0] = point[0] - wallStart[0];
		targetVector[1] = point[1] - wallStart[1];
		targetVector[2] = point[2] - wallStart[2];

		// Calculate the dot product between the axis and target vector
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

		// Calculate the length of the tube
		double length = Math.sqrt(Math.pow(intersection[0] - point[0], 2)
				+ Math.pow(intersection[1] - point[1], 2)
				+ Math.pow(intersection[2] - point[2], 2));

		// Create a new tube that is long enough to reach from the intersection
		// point to the target and as wide as the primary pipe
		FXTube tube = new FXTube(length, model.getPrimaryPipe().getRadius(),
				model.getPrimaryPipe().getRadius(),
				model.getPrimaryPipe().getAxialSamples(), 10);

		// Create a rotation to restore the tube to the default position after
		// the Pipe's transformation is applied, so that the rotation calculated
		// above will rotate it correctly
		double[] nodeRotation = transformation.getRotation();
		Rotate reverseRotation = Util.eulerToRotate(nodeRotation[0] * -1d,
				nodeRotation[0] * -1d, nodeRotation[0] * -1d);

		// Create a view on the mesh, apply the rotations and material, and add
		// it to the node
		view = new MeshView(tube.getMesh());
		view.getTransforms().setAll(rotation, reverseRotation);
		view.setMaterial(new PhongMaterial(Color.BLUE));
		if (wireframe) {
			view.setDrawMode(DrawMode.LINE);
		}
		node.getChildren().add(view);

		// Calculate the vector between the intersection point and the target
		// point
		double[] directVector = new double[3];
		directVector[0] = point[0] - intersection[0];
		directVector[1] = point[1] - intersection[1];
		directVector[2] = point[2] - intersection[2];

		// Normalize the vector of the direct line
		double directLength = Math.sqrt(Math.pow(directVector[0], 2)
				+ Math.pow(directVector[1], 2) + Math.pow(directVector[2], 2));
		double[] normalizedDirect = new double[3];
		normalizedDirect[0] = directVector[0] / directLength;
		normalizedDirect[1] = directVector[1] / directLength;
		normalizedDirect[2] = directVector[2] / directLength;

		// Move the view to the intersection point, then step along the direct
		// vector a distance equal to half the pipe's length. Since tubes's
		// translations are based on their central point, this will shift the
		// tube so that one end is at the intersection point and the other is at
		// the target.
		view.setTranslateX(
				intersection[0] + (normalizedDirect[0] * length / 2));
		view.setTranslateY(
				intersection[1] + (normalizedDirect[1] * length / 2));
		view.setTranslateZ(
				intersection[2] + (normalizedDirect[2] * length / 2));

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
	 * .viz.service.modeling.AbstractMesh)
	 */
	@Override
	public void refresh(AbstractMesh model) {

		// Remove the extra parts from the node
		node.getChildren().remove(primaryPipe);
		node.getChildren().remove(wall);
		node.getChildren().remove(inletView);
		node.getChildren().remove(outletView);

		node.getChildren().clear();

		// The heat exchanger cannot be drawn without a central pipe to
		// contain.
		if (((HeatExchangerMesh) model).getPrimaryPipe() == null) {
			return;
		}

		// Get a reference to the primary pipe
		FXPipeController primaryPipeController = (FXPipeController) ((HeatExchangerMesh) model)
				.getPrimaryPipe();

		// Set the primary pipe to the same wireframe mode as this object
		primaryPipeController.setWireFrameMode(wireframe);

		// Recolor the primary pipe to blue and add its mesh to the node
		primaryPipeController.setMaterial(new PhongMaterial(Color.BLUE));
		primaryPipe = (Group) primaryPipeController.getRepresentation();
		node.getChildren().add(primaryPipe);

		// Create the wall around the primary pipe
		double wallSize = primaryPipeController.getRadius() * 4;
		wall = new Box(wallSize, primaryPipeController.getLength() * 0.8d,
				wallSize);

		// Add the wall to the scene
		wall.setMaterial(new PhongMaterial(Color.BLUE));
		if (wireframe) {
			wall.setDrawMode(DrawMode.LINE);
		}
		node.getChildren().add(wall);
		wall.getTransforms().setAll(Util.convertTransformation(transformation));

		// Get the secondary input junction
		List<AbstractController> secondaryInputList = model
				.getEntitiesByCategory("Secondary Input");

		// If there is a secondary input, draw its pipe
		if (!secondaryInputList.isEmpty()) {
			// Create the secondary inlet
			AbstractController inletJunction = model
					.getEntitiesByCategory("Secondary Input").get(0);
			secondaryInlet = createTubeToPoint(
					((JunctionController) inletJunction).getCenter(),
					(HeatExchangerMesh) model, inletView, wireframe);

			// Add the secondary pipes to the scene
			inletView = new MeshView(secondaryInlet.getMesh());
			inletView.setMaterial(new PhongMaterial(Color.BLUE));

		}

		// Get the secondary input junction
		List<AbstractController> secondaryOutputList = model
				.getEntitiesByCategory("Secondary Output");

		// If there is a secondary input, draw its pipe
		if (!secondaryOutputList.isEmpty()) {
			// Create the secondary inlet
			AbstractController outletJunction = model
					.getEntitiesByCategory("Secondary Output").get(0);
			secondaryOutlet = createTubeToPoint(
					((JunctionController) outletJunction).getCenter(),
					(HeatExchangerMesh) model, outletView, wireframe);

			// Add the secondary pipes to the scene
			outletView = new MeshView(secondaryOutlet.getMesh());
			outletView.setMaterial(new PhongMaterial(Color.BLUE));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IWireFramePart#setWireFrameMode(
	 * boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {

		// Save the wireframe state
		wireframe = on;

		// Set each of the pieces that exist to line mode
		if (on) {
			if (wall != null)
				wall.setDrawMode(DrawMode.LINE);
			if (inletView != null)
				inletView.setDrawMode(DrawMode.LINE);
			if (outletView != null)
				outletView.setDrawMode(DrawMode.LINE);
		}

		// Set each of the pieces to fill mode
		else {
			if (wall != null)
				wall.setDrawMode(DrawMode.FILL);
			if (inletView != null)
				inletView.setDrawMode(DrawMode.FILL);
			if (outletView != null)
				outletView.setDrawMode(DrawMode.FILL);
		}

		// Notify listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.WIREFRAME };
		updateManager.notifyListeners(eventTypes);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new AbstractView and make it a copy of this
		FXHeatExchangerView clone = new FXHeatExchangerView();
		clone.copy(this);

		return clone;
	}

}
