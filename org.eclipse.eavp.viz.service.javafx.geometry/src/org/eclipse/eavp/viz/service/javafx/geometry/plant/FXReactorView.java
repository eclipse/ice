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

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.IWireFramePart;
import org.eclipse.eavp.viz.service.geometry.reactor.Extrema;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeController;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMesh;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

/**
 * A view for a Reactor part that creates a JavaFX mesh.
 * 
 * @author Robert Smith
 *
 */
public class FXReactorView extends AbstractView implements IWireFramePart {

	/**
	 * The JavaFX node containing the reactor's mesh
	 */
	Group node;

	/**
	 * The JavaFX node which will collect all the individual JavaFX objects
	 * which will make up the completed mesh,
	 */
	Group reactorNode;

	/**
	 * The first of the reactor's straight sides
	 */
	Box side1;

	/**
	 * The second of the reactor's straight sides
	 */
	Box side2;

	/**
	 * The reactor's bottom curved side.
	 */
	MeshView lowerArch;

	/**
	 * The reactor's top curved side.
	 */
	MeshView upperArch;

	/**
	 * Whether or not the reactor will display in wireframe mode. It will be a
	 * wireframe if true, or solid if false.
	 */
	boolean wireframe;

	/**
	 * The nullary constructor.
	 */
	public FXReactorView() {
		super();

		// Initialize the nodes
		node = new Group();
		reactorNode = new Group();
		node.getChildren().add(reactorNode);
	}

	/**
	 * The default constructor
	 */
	public FXReactorView(ReactorMesh model) {
		super();

		// Initialize the nodes
		node = new Group();
		reactorNode = new Group();
		node.getChildren().add(reactorNode);

		// Initialize the mesh
		createShape(model);
	}

	/**
	 * Create the mesh for the reactor and add it to the node, removing the
	 * reactor's current mesh, if any. This will create a capsule shaped wall
	 * around the reactor's core channels. This function assumes that all core
	 * channels will lie on either the XY, XZ, or YZ plane. The two longer sides
	 * will be straight, touching the edges of the farthest core channels. The
	 * two smaller sides will be semi-circles reaching from one of the straight
	 * sides to the other.
	 * 
	 * @param model
	 *            The model which will be graphically represented by the mesh
	 */
	private void createShape(ReactorMesh model) {

		// A list of all the extrema of all core channels contained by the
		// reactor
		ArrayList<Extrema> extrema = new ArrayList<Extrema>();

		// Check all the reactor's children for core channels
		for (AbstractController channel : model.getEntities()) {
			if ("True".equals(channel.getProperty("Core Channel"))) {

				// Add the extrema of core channels to the list
				extrema.add(((PipeController) channel).getLowerExtrema());
				extrema.add(((PipeController) channel).getUpperExtrema());
			}
		}

		// The bounds of the rectangle defining the capsule's shape
		Extrema bounds = new Extrema(extrema);

		// How thick the mesh will be
		double thickness = 5;

		// The number of samples to be used in the creation of the circular
		// portion of the mesh
		int samples = 50;

		// The dimensions of the mesh
		double width;
		double height;
		double depth;

		// The literal sizes in each dimension
		double sizeX = bounds.getMaxX() - bounds.getMinX();
		double sizeY = bounds.getMaxY() - bounds.getMinY();
		double sizeZ = bounds.getMaxZ() - bounds.getMinZ();

		// Set the characteristics based on the sizes
		depth = Math.min(Math.min(sizeX, sizeY), sizeZ);
		height = Math.max(Math.max(sizeX, sizeY), sizeZ);
		if (sizeX < height && sizeX > depth) {
			width = sizeX;
		} else if (sizeY < height && sizeY > depth) {
			width = sizeY;
		} else {
			width = sizeZ;
		}

		// Widen the reactor so that the inner wall is touching the bounds of
		// the core channels, saving the original width for later comparisons
		double origWidth = width;
		width = width + thickness;

		// The material for the shapes
		PhongMaterial material = new PhongMaterial(Color.WHITE);

		// Discard the old reactor node and replace it with a new one.
		node.getChildren().remove(reactorNode);
		reactorNode = new Group();
		node.getChildren().add(reactorNode);

		// Create the two straight sides
		side1 = new Box(thickness, height, depth);
		side1.setTranslateX(width / 2);
		side1.setMaterial(material);
		reactorNode.getChildren().add(side1);

		side2 = new Box(thickness, height, depth);
		side2.setTranslateX(width / -2);
		side2.setMaterial(material);
		reactorNode.getChildren().add(side2);

		// Get the vertices of the semicircular edges
		float[] innerVertices = createSemiCircle(
				(float) (width / 2 - thickness / 2), samples, true);
		float[] outerVertices = createSemiCircle(
				(float) (width / 2 + thickness / 2), samples, true);

		// The number of coordinates on one side of the circular mesh
		int blockSize = samples * 2 * 3;

		// A list of all coordinates in the mesh in the format of the first
		// vertex's x, y, and z coordinates, the second vertex's x, y, and z
		// coordinates, etc
		float[] vertices = new float[blockSize * 2];

		// At each sample point, create the four vertices defining a
		// rectangular slice of the semicircle
		for (int i = 0; i < samples; i++) {

			// The bottom inner vertex
			vertices[i * 6] = innerVertices[i * 2];
			vertices[i * 6 + 1] = innerVertices[i * 2 + 1];
			vertices[i * 6 + 2] = (float) (depth / -2);

			// The top inner vertex
			vertices[i * 6 + 3] = innerVertices[i * 2];
			vertices[i * 6 + 4] = innerVertices[i * 2 + 1];
			vertices[i * 6 + 5] = (float) (depth / 2);

			// The bottom outer vertex
			vertices[blockSize + i * 6] = outerVertices[i * 2];
			vertices[blockSize + i * 6 + 1] = outerVertices[i * 2 + 1];
			vertices[blockSize + i * 6 + 2] = (float) (depth / -2);

			// The top outer vertex
			vertices[blockSize + i * 6 + 3] = outerVertices[i * 2];
			vertices[blockSize + i * 6 + 4] = outerVertices[i * 2 + 1];
			vertices[blockSize + i * 6 + 5] = (float) (depth / 2);
		}

		// Create the mesh and add the vertices to it
		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().addAll(vertices);

		// Do not apply a texture, instead add a single dummy coordinate.
		float[] texCoords = { 0, 0 };
		mesh.getTexCoords().addAll(texCoords);

		// A list of the faces in the mesh specified as indices into the vertex
		// array and texture coordinate arrays
		int[] faces = new int[samples * 8 * 6];

		// Smoothing groups for the faces, indexed by the face's index in the
		// face array
		int[] smoothingGroups = new int[samples * 8];

		// The number of faces on one side of the mesh
		int faceBlockSize = samples * 2;

		// Half the total number of faces
		int halfFaces = samples * 4;

		// At each vertex, construct two faces
		for (int i = 0; i < halfFaces - 2; i++) {

			// Ignore the last two vertices on the first edge of the mesh, there
			// are no more faces to make past them
			if (i < faceBlockSize - 2 || i >= faceBlockSize) {

				if (i < faceBlockSize - 2) {
					// Create a face along the side of the mesh
					faces[i * 6] = i % 2 != 0 ? i + 2 : i;

					// Set all texture coordinates to zero
					faces[i * 6 + 1] = 0;
					faces[i * 6 + 2] = i + 1;
					faces[i * 6 + 3] = 0;
					faces[i * 6 + 4] = i % 2 == 0 ? i + 2 : i;
					faces[i * 6 + 5] = 0;
				}

				else {
					// Create a face along the side of the mesh
					faces[i * 6] = i % 2 == 0 ? i + 2 : i;

					// Set all texture coordinates to zero
					faces[i * 6 + 1] = 0;
					faces[i * 6 + 2] = i + 1;
					faces[i * 6 + 3] = 0;
					faces[i * 6 + 4] = i % 2 != 0 ? i + 2 : i;
					faces[i * 6 + 5] = 0;
				}

				// Create a face along the top/bottom of the mesh, with two
				// vertices on this vertex's side and one on the opposite side
				if (i < faceBlockSize) {

					faces[(halfFaces + i) * 6] = i % 2 == 0 ? i
							: i + faceBlockSize + 2;
					faces[(halfFaces + i) * 6 + 1] = 0;
					faces[(halfFaces + i) * 6 + 2] = i + 2;
					faces[(halfFaces + i) * 6 + 3] = 0;
					faces[(halfFaces + i) * 6 + 4] = i % 2 != 0 ? i
							: i + faceBlockSize + 2;
					faces[(halfFaces + i) * 6 + 5] = 0;
				}

				else {
					faces[(halfFaces + i) * 6] = i % 2 != 0 ? i
							: i + 2 - faceBlockSize - 2;
					faces[(halfFaces + i) * 6 + 1] = 0;
					faces[(halfFaces + i) * 6 + 2] = i + 2;
					faces[(halfFaces + i) * 6 + 3] = 0;
					faces[(halfFaces + i) * 6 + 4] = i % 2 == 0 ? i
							: i + 2 - faceBlockSize - 2;
					faces[(halfFaces + i) * 6 + 5] = 0;

				}
			}
		}

		// Add the faces to the mesh
		mesh.getFaces().addAll(faces);
		mesh.getFaceSmoothingGroups().addAll(smoothingGroups);

		// Create the top arch from the mesh
		upperArch = new MeshView(mesh);
		upperArch.setMaterial(material);
		upperArch.setTranslateY(height / 2);
		reactorNode.getChildren().add(upperArch);

		// Create the bottom arch by rotating the top one 180 degrees about the
		// axis
		lowerArch = new MeshView(mesh);
		lowerArch.setMaterial(material);
		lowerArch.setRotate(180d);
		lowerArch.setTranslateY(-height / 2 - width / 2 - thickness / 2);
		reactorNode.getChildren().add(lowerArch);

		// Rotate the mesh such that its height is pointing in its longest
		// direction and its hole is pointing along its shortest distance
		// Rotate on the z axis if the X is the height or Z is the height and
		// Y is the width
		if ((sizeX > sizeY && sizeX > sizeZ)
				|| (sizeZ > sizeY && sizeY > sizeX)) {
			Rotate temp = new Rotate();
			temp.setAxis(Rotate.Z_AXIS);
			temp.setAngle(90);
			reactorNode.getTransforms().add(temp);
		}

		// If z is the highest dimension, rotate on the x axis
		if (sizeY != height && sizeX != height && sizeZ == height) {
			Rotate temp = new Rotate();
			temp.setAxis(Rotate.X_AXIS);
			temp.setAngle(90);
			reactorNode.getTransforms().add(temp);
		}

		// If z is the widest dimension, rotate on the y axis
		else if (sizeY != origWidth && sizeX != origWidth
				&& sizeZ == origWidth) {
			Rotate temp = new Rotate();
			temp.setAxis(Rotate.Y_AXIS);
			temp.setAngle(90);
			reactorNode.getTransforms().add(temp);
		}

		// Move the reactor to surround the region
		reactorNode.setTranslateX(
				(bounds.getMaxX() - bounds.getMinX()) / 2 + bounds.getMinX());
		reactorNode.setTranslateY(
				(bounds.getMaxY() - bounds.getMinY()) / 2 + bounds.getMinY());
		reactorNode.setTranslateZ(
				(bounds.getMaxZ() - bounds.getMinZ()) / 2 + bounds.getMinZ());

		// Set the shapes to the correct rendering mode
		setWireFrameMode(wireframe);
	}

	/**
	 * Creates a series of points which lie evenly spaced on the edge of a
	 * semicircle on the XY plane defined by the arguments.
	 * 
	 * @param radius
	 *            The circle's radius
	 * @param samples
	 *            The number of points to create
	 * @param top
	 *            Whether or not to render the top half of a circle. If true,
	 *            the top half of the circle will be rendered. If false, the
	 *            bottom half of the circle will be rendered.
	 * @return An array of floats defining the points on the circle. It is
	 *         ordered as: the first point's x coordinate, the first point's z
	 *         coordinate, the second point's x coordinate, the second point's y
	 *         coordinate, the third point's x coordinate, etc.
	 */
	private float[] createSemiCircle(float radius, int samples, boolean top) {

		// The points defining the circle
		float[] points = new float[samples * 2];

		// The angle of the current point on the circle
		float angle = 0;

		for (int i = 0; i < samples; i++) {

			// Place the point's coordinates into the array
			points[i * 2] = (float) (radius * Math.cos(angle));
			points[i * 2 + 1] = (float) (radius * Math.sin(angle));

			// Move the angle by 1/(number of samples - 1)th of the circle, such
			// that the first point will be at the corner of the semicircle and
			// the last point will be at the corner of the semicircle
			angle += 1f / (samples - 1) * Math.PI;
		}

		return points;
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

		// Redraw the mesh
		createShape((ReactorMesh) model);
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

		// Set each of the reactor's parts to line mode
		if (on) {

			// If side1 exists, the other objects should too
			if (side1 != null) {
				side1.setDrawMode(DrawMode.LINE);
				side2.setDrawMode(DrawMode.LINE);
				lowerArch.setDrawMode(DrawMode.LINE);
				upperArch.setDrawMode(DrawMode.LINE);
			}
		}

		// Set each of the reactor's parts to fill mode
		else {

			// If side1 exists, the other objects should too
			if (side1 != null) {
				side1.setDrawMode(DrawMode.FILL);
				side2.setDrawMode(DrawMode.FILL);
				lowerArch.setDrawMode(DrawMode.FILL);
				upperArch.setDrawMode(DrawMode.FILL);
			}
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
		FXReactorView clone = new FXReactorView();
		clone.copy(this);

		return clone;
	}
}
