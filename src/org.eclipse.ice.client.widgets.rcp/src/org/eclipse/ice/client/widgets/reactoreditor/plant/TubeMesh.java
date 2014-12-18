/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.reactoreditor.plant;

import static com.jme3.util.BufferUtils.createShortBuffer;
import static com.jme3.util.BufferUtils.createVector3Buffer;

import java.nio.FloatBuffer;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;

/**
 * This class provides a custom tube-shaped mesh. The rendered tube has either 2
 * or 4 sides: an inner cylinder, an outer cylinder, and a top and bottom edge
 * if the inner and outer cylinders do not have the same radius.
 * <p>
 * The tube is rendered from the xz-plane along the y axis. Transformations
 * should be applied to the associated geometry.
 * </p>
 * <p>
 * <b>Note:</b> After updating the properties of the mesh, you should call
 * {@link #refresh(boolean)}.
 * </p>
 * 
 * @author Jordan
 *
 */
public class TubeMesh extends Mesh {

	/**
	 * The length of the tube mesh.
	 */
	private float length = 1f;
	/**
	 * The inner radius of the tube mesh.
	 */
	private float innerRadius = 0.9f;
	/**
	 * The outer radius of the tube mesh.
	 */
	private float outerRadius = 1f;

	/**
	 * The number of sections of mesh elements along the length of the tube.
	 */
	private int axialSamples = 1;
	/**
	 * The number of sections of mesh elements around the circumference of the
	 * tube.
	 */
	private int radialSamples = 10;

	/**
	 * An array containing the outer vertices on the top of the tube. This can
	 * be used to generate a bounding box for the top edge of the tube.
	 */
	private Vector3f[] topEdgeVertices;
	/**
	 * An array containing the outer vertices on the bottom of the tube. This
	 * can be used to generate a bounding box for the bottom edge of the tube.
	 */
	private Vector3f[] bottomEdgeVertices;

	/**
	 * The default constructor.
	 */
	public TubeMesh() {
		refresh(true);
	}

	/**
	 * Constructs a tube with all default parameters except for those specified
	 * (if valid)
	 * 
	 * @param length
	 *            The length of the tube mesh.
	 * @param radius
	 *            The inner and outer radius of the tube. The top and bottom
	 *            edges will not be rendered.
	 */
	public TubeMesh(float length, float radius) {
		setLength(length);
		setRadius(radius);
		refresh(true);
	}

	/**
	 * Constructs a tube with all default parameters except for those specified
	 * (if valid)
	 * 
	 * @param length
	 *            The length of the tube mesh.
	 * @param radius
	 *            The inner and outer radius of the tube. The top and bottom
	 *            edges will not be rendered.
	 * @param axialSamples
	 *            The number of sections of mesh elements along the length of
	 *            the tube.
	 * @param radialSamples
	 *            The number of sections of mesh elements around the
	 *            circumference of the tube.
	 */
	public TubeMesh(float length, float radius, int axialSamples,
			int radialSamples) {
		setLength(length);
		setRadius(radius);
		setAxialSamples(axialSamples);
		setRadialSamples(radialSamples);
		refresh(true);
	}

	/**
	 * Constructs a tube with all default parameters except for those specified
	 * (if valid)
	 * 
	 * @param length
	 *            The length of the tube mesh.
	 * @param innerRadius
	 *            The inner radius of the tube mesh.
	 * @param outerRadius
	 *            The outer radius of the tube mesh. This should be greater than
	 *            the inner radius.
	 */
	public TubeMesh(float length, float innerRadius, float outerRadius) {
		setLength(length);
		setRadii(innerRadius, outerRadius);
		refresh(true);
	}

	/**
	 * Constructs a tube with all default parameters except for those specified
	 * (if valid)
	 * 
	 * @param length
	 *            The length of the tube mesh.
	 * @param innerRadius
	 *            The inner radius of the tube mesh.
	 * @param outerRadius
	 *            The outer radius of the tube mesh. This should be greater than
	 *            the inner radius.
	 * @param axialSamples
	 *            The number of sections of mesh elements along the length of
	 *            the tube.
	 * @param radialSamples
	 *            The number of sections of mesh elements around the
	 *            circumference of the tube.
	 */
	public TubeMesh(float length, float innerRadius, float outerRadius,
			int axialSamples, int radialSamples) {
		setLength(length);
		setRadii(innerRadius, outerRadius);
		setAxialSamples(axialSamples);
		setRadialSamples(radialSamples);
		refresh(true);
	}

	/**
	 * Sets the length of the tube mesh.
	 * 
	 * @param length
	 *            The length of the tube mesh. This must be greater than 0.
	 */
	public void setLength(float length) {
		if (length > 0f) {
			this.length = length;
		}
	}

	/**
	 * Sets the radius of the tube mesh. The top and bottom edges will not be
	 * rendered.
	 * 
	 * @param radius
	 *            The inner and outer radius of the tube mesh.
	 */
	public void setRadius(float radius) {
		if (radius > 0f) {
			innerRadius = radius;
			outerRadius = radius;
		}
	}

	/**
	 * Sets the inner and outer radii of the tube mesh. The top and bottom edges
	 * will be rendered.
	 * 
	 * @param innerRadius
	 *            The inner radius of the tube mesh.
	 * @param outerRadius
	 *            The outer radius of the tube mesh. This should be greater than
	 *            the inner radius.
	 */
	public void setRadii(float innerRadius, float outerRadius) {
		if (innerRadius > 0f && outerRadius > innerRadius) {
			this.innerRadius = innerRadius;
			this.outerRadius = outerRadius;
		}
	}

	/**
	 * Sets the number of axial samples along the length of the tube mesh.
	 * 
	 * @param axialSamples
	 *            The number of sections of mesh elements along the length of
	 *            the tube. Must be greater than 0, although it should be
	 *            increased if the tube is very long.
	 */
	public void setAxialSamples(int axialSamples) {
		if (axialSamples > 0) {
			this.axialSamples = axialSamples;
		}
	}

	/**
	 * Sets the number of radial samples around the tube mesh.
	 * 
	 * @param radialSamples
	 *            The number of sections of mesh elements around the
	 *            circumference of the tube. Must be greater than 2.
	 */
	public void setRadialSamples(int radialSamples) {
		if (radialSamples > 2) {
			this.radialSamples = radialSamples;
		}
	}

	/**
	 * Updates the underlying buffers used to render the mesh. This should be
	 * called after the mesh has been customized via the set operations.
	 * 
	 * @param indicesChanged
	 *            Whether or not the number of vertices changed. This should be
	 *            true if any of the following are true:
	 *            <ul>
	 *            <li>The axial or radial samples change</li>
	 *            <li>The mesh is switched between rendering and not rendering
	 *            the edges (e.g., tube thickness changes from or to 0)</li>
	 *            </ul>
	 */
	public void refresh(boolean indicesChanged) {
		if (indicesChanged) {
			updateBuffers();
			updateIndices();
		}
		updateGeometry();
		updateBound();
	}

	/**
	 * Gets the outermost vertices from the bottom edge of the tube.
	 * 
	 * @return An array of the vertices from the bottom edge of the tube. <b>Do
	 *         not modify this array!</b>
	 */
	public Vector3f[] getBottomEdgeVertices() {
		return bottomEdgeVertices;
	}

	/**
	 * Gets the outermost vertices from the top edge of the tube.
	 * 
	 * @return An array of the vertices from the top edge of the tube. <b>Do not
	 *         modify this array!</b>
	 */
	public Vector3f[] getTopEdgeVertices() {
		return topEdgeVertices;
	}

	/**
	 * Re-allocates the buffers for vertex positions, vertex normals, and face
	 * indices.
	 */
	private void updateBuffers() {

		// Get the number of vertices and faces required for the inner and outer
		// cylinders.
		int numVertices = 2 * radialSamples * (axialSamples + 1);
		int numFaces = 2 * axialSamples * radialSamples;
		// If we can render the top and bottom edges, add their vertex and face
		// counts.
		if (outerRadius > innerRadius) {
			numVertices += radialSamples * 4;
			numFaces += radialSamples * 2;
		}

		// A single normal is required for each specified vertex. However, to
		// create the "sharp" edges (with respect to lighting) we require two
		// vertices at the connecting edge location, each with different normal
		// values.

		// Create the vertex buffer.
		setBuffer(Type.Position, 3,
				createVector3Buffer(getFloatBuffer(Type.Position), numVertices));

		// Create the normal buffer. We must have one normal per vertex.
		setBuffer(Type.Normal, 3,
				createVector3Buffer(getFloatBuffer(Type.Normal), numVertices));

		// We have two triangles per face. To specify a face, we require 6
		// integers, one for each corner of the two triangles.
		setBuffer(Type.Index, 3,
				createShortBuffer(getShortBuffer(Type.Index), numFaces * 6));

		// Allocate space for storing the top and bottom edge vertices.
		topEdgeVertices = new Vector3f[radialSamples];
		bottomEdgeVertices = new Vector3f[radialSamples];
		for (int i = 0; i < radialSamples; i++) {
			topEdgeVertices[i] = new Vector3f();
			bottomEdgeVertices[i] = new Vector3f();
		}

		return;
	}

	/**
	 * Updates the indices for all triangular polygons comprising the mesh.
	 */
	private void updateIndices() {

		/*-
		 * This mesh must be split into four sections:
		 * 
		 * 1) The top, narrow edge of the tube
		 * 2) The bottom, narrow edge of the tube
		 * 3) The outside of the tube
		 * 4) The inside of the tube.
		 * 
		 * Furthermore, each of these sections has its own set of vertices and 
		 * normals even though their locations overlap. This gives "sharp" edges
		 * when the mesh is shaded.
		 */

		// Get the index buffer. Rewind it so we re-fill it from the beginning.
		IndexBuffer ib = getIndexBuffer();

		// The index used in the index buffer. This should be incremented every
		// time something is added to the buffer.
		int index = 0;
		// The current offset used for each face.
		int offset = 0;

		int verticesPerLevel = 2 * radialSamples;
		for (int l = 0; l < axialSamples; l++) {
			for (int i = 1; i < radialSamples; i++) {
				// inner...
				ib.put(index++, offset);
				ib.put(index++, offset + verticesPerLevel);
				ib.put(index++, offset + verticesPerLevel + 2);
				ib.put(index++, offset);
				ib.put(index++, offset + verticesPerLevel + 2);
				ib.put(index++, offset + 2);
				offset++;
				// outer...
				ib.put(index++, offset);
				ib.put(index++, offset + verticesPerLevel + 2);
				ib.put(index++, offset + verticesPerLevel);
				ib.put(index++, offset);
				ib.put(index++, offset + 2);
				ib.put(index++, offset + verticesPerLevel + 2);
				offset++;
			}
			// Connect the end of the cylinders to the front.
			// inner...
			ib.put(index++, offset);
			ib.put(index++, offset + verticesPerLevel);
			ib.put(index++, offset + 2);
			ib.put(index++, offset);
			ib.put(index++, offset + 2);
			ib.put(index++, offset + 2 - verticesPerLevel);
			offset++;
			// outer...
			ib.put(index++, offset);
			ib.put(index++, offset + 2);
			ib.put(index++, offset + verticesPerLevel);
			ib.put(index++, offset);
			ib.put(index++, offset + 2 - verticesPerLevel);
			ib.put(index++, offset + 2);
			offset++;
		}
		// By the end of the above loop, the current vertex index is the offset
		// defined below.
		offset = (axialSamples + 1) * verticesPerLevel;

		// If the top and bottom edges must be rendered, render them.
		if (outerRadius > innerRadius) {
			// The bottom edge.
			for (int i = 1; i < radialSamples; i++) {
				ib.put(index++, offset);
				ib.put(index++, offset + 3);
				ib.put(index++, offset + 1);
				ib.put(index++, offset);
				ib.put(index++, offset + 2);
				ib.put(index++, offset + 3);
				offset += 2;
			}
			// Connect the end of the annulus to the front.
			ib.put(index++, offset);
			ib.put(index++, offset + 3 - verticesPerLevel);
			ib.put(index++, offset + 1);
			ib.put(index++, offset);
			ib.put(index++, offset + 2 - verticesPerLevel);
			ib.put(index++, offset + 3 - verticesPerLevel);
			offset += 2;
			// The top edge.
			for (int i = 1; i < radialSamples; i++) {
				ib.put(index++, offset);
				ib.put(index++, offset + 1);
				ib.put(index++, offset + 3);
				ib.put(index++, offset);
				ib.put(index++, offset + 3);
				ib.put(index++, offset + 2);
				offset += 2;
			}
			// Connect the end of the annulus to the front.
			ib.put(index++, offset);
			ib.put(index++, offset + 1);
			ib.put(index++, offset + 3 - verticesPerLevel);
			ib.put(index++, offset);
			ib.put(index++, offset + 3 - verticesPerLevel);
			ib.put(index++, offset + 2 - verticesPerLevel);
			offset += 2;
		}

		return;
	}

	/**
	 * Updates the positions and normals for all vertices in the mesh.
	 */
	private void updateGeometry() {

		/*-
		 * This mesh must be split into four sections:
		 * 
		 * 1) The top, narrow edge of the tube
		 * 2) The bottom, narrow edge of the tube
		 * 3) The outside of the tube
		 * 4) The inside of the tube.
		 * 
		 * Furthermore, each of these sections has its own set of vertices and 
		 * normals even though their locations overlap. This gives "sharp" edges
		 * when the mesh is shaded.
		 */

		// y is held constant while iterating around the circle, so declare y.
		float y;

		// ---- Create commonly used values. ---- //
		// To construct a tube below, we re-use the x and z values for a circle
		// centered on the y axis. The circle starts on the x axis and goes
		// first through the negative z half, then the positive z half.

		// We keep track of the inner circle (with the inner radius) and the
		// outer circle (with the outer radius). We also keep track of the
		// x and z values for the unit circle (with radius 1) to use for the
		// vertex normals.
		float[] innerX = new float[radialSamples];
		float[] innerZ = new float[radialSamples];
		float[] outerX = new float[radialSamples];
		float[] outerZ = new float[radialSamples];
		float[] normX = new float[radialSamples];
		float[] normZ = new float[radialSamples];

		// There's no need to compute the first values... They are known.
		innerX[0] = innerRadius;
		innerZ[0] = 0f;
		outerX[0] = outerRadius;
		outerZ[0] = 0f;
		normX[0] = 1f;
		normZ[0] = 0f;

		// Compute the re-usable values based on the number of radial samples.
		float angleDifference = FastMath.TWO_PI / radialSamples;
		for (int i = 1; i < radialSamples; i++) {
			float angle = i * angleDifference;
			float cos = FastMath.cos(angle);
			float sin = -FastMath.sin(angle); // Negate the z!

			innerX[i] = cos * innerRadius;
			outerX[i] = cos * outerRadius;
			innerZ[i] = sin * innerRadius;
			outerZ[i] = sin * outerRadius;
			normX[i] = cos;
			normZ[i] = sin;
		}
		// -------------------------------------- //

		// Get the position and normal buffers. Rewind them so we re-fill them
		// from the beginning.
		FloatBuffer pb = getFloatBuffer(Type.Position);
		FloatBuffer nb = getFloatBuffer(Type.Normal);
		pb.rewind();
		nb.rewind();

		// Create the outer and inner cylinders.
		float segmentLength = length / axialSamples;
		for (int l = 0; l <= axialSamples; l++) {
			y = l * segmentLength;
			for (int i = 0; i < radialSamples; i++) {
				// The inner cylinder. Note that the normal is negated.
				pb.put(innerX[i]).put(y).put(innerZ[i]);
				nb.put(-normX[i]).put(0f).put(-normZ[i]);
				// The outer cylinder.
				pb.put(outerX[i]).put(y).put(outerZ[i]);
				nb.put(normX[i]).put(0f).put(normZ[i]);
			}
		}

		// Set the outermost top and bottom vertices for use outside the class.
		for (int i = 0; i < radialSamples; i++) {
			bottomEdgeVertices[i].set(outerX[i], 0f, outerZ[i]);
			topEdgeVertices[i].set(outerX[i], length, outerZ[i]);
		}

		// If the top and bottom edges must be rendered, create them.
		if (outerRadius > innerRadius) {
			// The bottom edge.
			y = 0f;
			float[] norm = new float[] { 0f, -1f, 0f };
			for (int i = 0; i < radialSamples; i++) {
				pb.put(innerX[i]).put(y).put(innerZ[i]);
				pb.put(outerX[i]).put(y).put(outerZ[i]);
				nb.put(norm).put(norm);
			}
			// The top edge.
			y = length;
			norm[1] = 1f;
			for (int i = 0; i < radialSamples; i++) {
				pb.put(innerX[i]).put(y).put(innerZ[i]);
				pb.put(outerX[i]).put(y).put(outerZ[i]);
				nb.put(norm).put(norm);
			}
		}

		return;
	}
}