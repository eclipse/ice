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
package org.eclipse.eavp.viz.service.javafx.geometry.datatypes;

import org.eclipse.eavp.viz.service.geometry.reactor.Extrema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.shape.TriangleMesh;

/**
 * A class which creates and manages a JavaFX TriangleMesh for a tube.
 * FXTubeMeshs will be cylindrical with a hole running down the axis, which may
 * be as large as the cylinder itself, in which case the tube is infinitely thin
 * with no top or bottom edges.
 * 
 * @author Robert Smith
 *
 */
public class FXTube {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FXTube.class);

	/**
	 * The tube's height
	 */
	double height;

	/**
	 * The radius of the hole in the tube
	 */
	double innerRadius;

	/**
	 * The tube's radius
	 */
	double outerRadius;

	/**
	 * The number of sample points along the axis
	 */
	int axialSamples;

	/**
	 * The number of sample points about the circumference
	 */
	int radialSamples;

	/**
	 * The JavaFX mesh which represents the tube.
	 */
	TriangleMesh mesh;

	/**
	 * A constructor which initializes the mesh's parameters.
	 * 
	 * @param height
	 *            The mesh's height
	 * @param innerRadius
	 *            The radius of the hole in the tube. Must be no larger than the
	 *            outer radius
	 * @param outerRadius
	 *            The tube's radius
	 * @param axialSamples
	 *            The number of sample points along the axis
	 * @param radialSamples
	 *            The number of sample points in a circle about the axis
	 */
	public FXTube(double height, double innerRadius, double outerRadius,
			int axialSamples, int radialSamples) {

		// Set the object's parameters
		this.height = height;
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.axialSamples = axialSamples;
		this.radialSamples = radialSamples;

		// Create the mesh
		mesh = createMesh();
	}

	/**
	 * Create a TriangleMesh representing a tube with the given parameters.
	 * 
	 * @param height
	 *            The mesh's height
	 * @param innerRadius
	 *            The radius of the hole in the tube. Must be no larger than the
	 *            outer radius
	 * @param outerRadius
	 *            The tube's radius
	 * @param axialSamples
	 *            The number of sample points along the axis
	 * @param radialSamples
	 *            The number of sample points in a circle about the axis
	 * @return A mesh which models a tube as described by the given arguments
	 */
	private TriangleMesh createMesh() {

		// Check the parameters for validity
		if (axialSamples < 2) {
			logger.error("A pipe must have at least two axial samples");
			return null;
		}

		if (radialSamples < 3) {
			logger.error("A pipe must have at least three radial samples");
			return null;
		}

		if (height <= 0) {
			logger.error("A pipe's height must be positive");
			return null;
		}

		if (outerRadius < innerRadius) {
			logger.error(
					"A pipe's inner radius must not be larger than its outer radius");
			return null;
		}

		// Initialize the mesh
		mesh = new TriangleMesh();

		// The y coordinate of the pipe's bottom edge
		float base = (float) (height / -2);

		// The vertices for the inner circle on the lower level
		float[] innerVertices = new float[radialSamples * 2];

		// The vertices for the outer circle on the lower level
		float[] outerVertices = new float[radialSamples * 2];

		// Get the XZ coordinates for the circles defining the tube's thickness
		innerVertices = createCircle((float) innerRadius, radialSamples);
		outerVertices = createCircle((float) outerRadius, radialSamples);

		// The number of coordinates required to specify every 3D vertex for a
		// cylinder of height axialSamples with radialSample points per level,
		// including the base.
		int blockSize = (axialSamples + 1) * radialSamples * 3;

		// A list of all vertices in the mesh, in the ordering first vertex's x,
		// y, and z coordinates, second vertex's x, y, and z coordinates, etc.
		float[] vertices = new float[blockSize * 2];

		for (int i = 0; i <= axialSamples; i++) {
			for (int j = 0; j < radialSamples; j++) {

				// Index in the array where the current inner vertex's data will
				// start
				int innerIndex = i * radialSamples * 3 + j * 3;

				// X coordinate of the inner circle
				vertices[innerIndex] = innerVertices[j * 2];

				// Y coordinate of the current segment's height
				vertices[innerIndex
						+ 1] = (float) (base + i * height / axialSamples);

				// Z coordinate of the inner circle
				vertices[innerIndex + 2] = innerVertices[j * 2 + 1];

				// Index in the array where the current outer vertex's data will
				// start
				int outerIndex = blockSize + i * radialSamples * 3 + j * 3;

				// X coordinate of the inner circle
				vertices[outerIndex] = outerVertices[j * 2];

				// Y coordinate of the current segment's height
				vertices[outerIndex
						+ 1] = (float) (base + i * height / axialSamples);

				// Z coordinate of the inner circle
				vertices[outerIndex + 2] = outerVertices[j * 2 + 1];

			}
		}

		// Add the vertices to the mesh
		mesh.getPoints().addAll(vertices);

		// Do not apply a texture, instead add a single dummy coordinate.
		float[] texCoords = { 0, 0 };
		mesh.getTexCoords().addAll(texCoords);

		// A list of all the indices into the coordinate and texture coordinate
		// arrays needed to construct the faces for both sides of the tube.
		int[] indices = new int[axialSamples * radialSamples * 12 * 2];

		// An array of which contains the mesh's smoothing group numbers,
		// indexed by face number
		int[] smoothingGroups = new int[axialSamples * radialSamples * 2 * 2];

		// The index of the next empty location in the indices array
		int i = 0;

		// The number of vertices needed to fully specify one of the two
		// cylindrical faces of the tube mesh
		int vertexBlockSize = (axialSamples + 1) * radialSamples;

		// Construct the tube out of identical vertical segments, one at a time.
		for (int axialSegment = 0; axialSegment < axialSamples; axialSegment++) {

			// Add two triangles for each vertex along the current circle
			for (int radialSegment = 0; radialSegment < radialSamples; radialSegment++) {

				// Create the two triangles for the inner face:
				// Create a triangle between the current vertex, the next vertex
				// along the circle, and the vertex immediately above this one.
				// Include 0s as references to the dummy value in the texture
				// coordinate array.
				indices[i] = axialSegment * radialSamples + radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = axialSegment * radialSamples
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = (axialSegment + 1) * radialSamples
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 1;
				i++;

				// Create a triangle between the current vertex, the vertex
				// immediately above it, and the last one along the circle from
				// that one. Include 0s as references to the dummy value in the
				// texture coordinate array.
				indices[i] = (axialSegment + 1) * radialSamples
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = (axialSegment + 1) * radialSamples + radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = axialSegment * radialSamples + radialSegment;
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 1;
				i++;

				// Create the two triangles for the outer face:
				// Create a triangle between the current vertex, the next vertex
				// along the circle, and the vertex immediately above this one.
				// Include 0s as references to the dummy value in the texture
				// coordinate array.
				indices[i] = vertexBlockSize
						+ (axialSegment + 1) * radialSamples
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = vertexBlockSize + axialSegment * radialSamples
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = vertexBlockSize + axialSegment * radialSamples
						+ radialSegment;
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 1;
				i++;

				// Create a triangle between the current vertex, the vertex
				// immediately above it, and the last one along the circle from
				// that one. Include 0s as references to the dummy value in the
				// texture coordinate array.
				indices[i] = vertexBlockSize + axialSegment * radialSamples
						+ radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = vertexBlockSize
						+ (axialSegment + 1) * radialSamples + radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = vertexBlockSize
						+ (axialSegment + 1) * radialSamples
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 1;
				i++;
			}
		}

		// Add the indices and smoothing groups to the mesh
		mesh.getFaces().addAll(indices);
		mesh.getFaceSmoothingGroups().addAll(smoothingGroups);

		// If the radii are different, the tube also needs a top and bottom edge
		if (innerRadius != outerRadius) {

			// Reset the index
			i = 0;

			// Create an array for four triangles per radial sample
			indices = new int[radialSamples * 4 * 6];

			// Create a new smoothing group array for the new faces
			smoothingGroups = new int[radialSamples * 4];

			// Add two triangles for each vertex along the current circle
			for (int radialSegment = 0; radialSegment < radialSamples; radialSegment++) {

				// Create the triangles for the bottom edge:
				// Create a triangle between the current vertex, the next vertex
				// along the circle, and the corresponding vertex on the other
				// edge. Include 0s as references to the dummy value in the
				// texture coordinate array.
				indices[i] = vertexBlockSize
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = radialSegment;
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 2;
				i++;

				// Create a triangle between the current vertex, the
				// corresponding vertex on the other edge, and the last one
				// along the circle from the other edge. Include 0s as
				// references to the dummy value in the texture coordinate
				// array.
				indices[i] = radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = vertexBlockSize + radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = vertexBlockSize
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 2;
				i++;

				// Create the triangles for the top edge:
				// Create a triangle between the current vertex, the next vertex
				// along the circle, and the corresponding vertex on the other
				// edge. Include 0s as references to the dummy value in the
				// texture coordinate array.
				indices[i] = ((axialSamples) * radialSamples) + radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = ((axialSamples) * radialSamples)
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = ((axialSamples) * radialSamples) + vertexBlockSize
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 2;
				i++;

				// Create a triangle between the current vertex, the vertex
				// immediately above it, and the last one along the circle from
				// that one. Include 0s as references to the dummy value in the
				// texture coordinate array.
				indices[i] = ((axialSamples) * radialSamples) + vertexBlockSize
						+ ((radialSegment + 1) % radialSamples);
				i++;
				indices[i] = 0;
				i++;
				indices[i] = ((axialSamples) * radialSamples) + vertexBlockSize
						+ radialSegment;
				i++;
				indices[i] = 0;
				i++;
				indices[i] = ((axialSamples) * radialSamples) + radialSegment;
				i++;
				indices[i] = 0;

				// Set this face's smoothing group
				smoothingGroups[i / 6] = 2;
				i++;
			}

			mesh.getFaces().addAll(indices);
			mesh.getFaceSmoothingGroups().addAll(smoothingGroups);
		}

		return mesh;
	}

	/**
	 * Creates a series of points which lie evenly spaced on the edge of a
	 * circle on the XZ plane defined by the arguments.
	 * 
	 * @param radius
	 *            The circle's radius
	 * @param samples
	 *            The number of points to create
	 * @return An array of floats defining the points on the circle. It is
	 *         ordered as: the first point's x coordinate, the first point's z
	 *         coordinate, the second point's x coordinate, the second point's y
	 *         coordinate, the third point's x coordinate, etc.
	 */
	private float[] createCircle(float radius, int samples) {

		// The points defining the circle
		float[] points = new float[samples * 2];

		// The angle of the current point on the circle
		float angle = 0;

		for (int i = 0; i < samples; i++) {

			// Place the point's coordinates into the array
			points[i * 2] = (float) (radius * Math.cos(angle));
			points[i * 2 + 1] = (float) (radius * Math.sin(angle));

			// Move the angle by 1/(number of samples)th of the circle.
			angle += 2f / samples * Math.PI;
		}

		return points;
	}

	/**
	 * Getter method for the JavaFX mesh
	 * 
	 * @return A JavaFX mesh with a shape defined by this tube's properties
	 */
	public TriangleMesh getMesh() {
		return mesh;
	}

	/**
	 * Gets the farthest points on the mesh in all three directions
	 * 
	 * @return The mesh's extrema
	 */
	public Extrema getExtrema() {
		return new Extrema(-outerRadius, outerRadius, -height / 2, height / 2,
				-outerRadius, outerRadius);
	}

	/**
	 * Gets the points on the tube's lower edge, in the format: first point's x,
	 * y, and z coordinates, second point's x, y, and z coordinates, etc.
	 * 
	 * @return
	 */
	public float[] getLowerBoundary() {

		float[] pointsOrig = createCircle((float) outerRadius, radialSamples);
		float[] points = new float[radialSamples * 3];

		for (int i = 0; i < radialSamples; i++) {
			points[i * 3] = pointsOrig[i * 2];
			points[i * 3 + 1] = (float) (-height / 2);
			points[i * 3 + 2] = pointsOrig[i * 2 + 1];
		}

		return points;
	}

	/**
	 * Gets the points on the tube's upper edge, in the format: first point's x,
	 * y, and z coordinates, second point's x, y, and z coordinates, etc.
	 * 
	 * @return
	 */
	public float[] getUpperBoundary() {

		float[] pointsOrig = createCircle((float) outerRadius, radialSamples);
		float[] points = new float[radialSamples * 3];

		for (int i = 0; i < radialSamples; i++) {
			points[i * 3] = pointsOrig[i * 2];
			points[i * 3 + 1] = (float) (height / 2);
			points[i * 3 + 2] = pointsOrig[i * 2 + 1];
		}

		return points;
	}

}
