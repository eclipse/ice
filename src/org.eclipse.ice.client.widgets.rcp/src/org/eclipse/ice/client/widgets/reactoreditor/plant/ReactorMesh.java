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

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;

/**
 * This class provides a custom mesh for displaying a cut-away view of a reactor
 * chamber. It has a rougly capsule shape with two sides cut away so that it
 * resembles a belt.
 * <p>
 * When given a {@link BoundingBox}, the mesh will contain the entire box. The
 * front of the box will be exposed, the left and right side will be closed, and
 * the top and bottom sides will be curved.<br>
 * </p>
 * <p>
 * In practice, the two biggest sides of the box should be exposed so that the
 * pipes inside can be seen. The next two biggest sides should be solid, while
 * the two smallest sides should be the curved sides. To do this, you need to
 * rotate the spatial that will be using this mesh.<br>
 * </p>
 * The thickness of the reactor wall can be set with
 * {@link #setThickness(float)}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ReactorMesh extends Mesh {

	/**
	 * The bounds of the central part of the mesh. This part is what contains
	 * all the pipe meshes (tubes) in the reactor.
	 */
	private final BoundingBox bounds;

	/**
	 * How thick to draw the "belt" shape of the mesh.
	 */
	private float thickness = 0.1f;

	/**
	 * The number of sections on one of the two rounded ends of the mesh.
	 */
	private int curveSamples = 10;

	/**
	 * The default constructor. Creates a reactor mesh around a unit box.
	 */
	public ReactorMesh() {

		// Set the initial bounds to the unit box.
		bounds = new BoundingBox(Vector3f.ZERO.clone(),
				Vector3f.UNIT_XYZ.clone());

		// Create the initial mesh.
		updateBuffers();
		updateGeometry();
		updateIndices();
		updateBound();

		return;
	}

	/**
	 * Sets the thickness of the reactor mesh. This will cause the mesh to
	 * update if the value changes.
	 * 
	 * @param thickness
	 *            The new thickness of the mesh. Must be greater than 0.
	 */
	public void setThickness(float thickness) {

		if (thickness != this.thickness && thickness > 0.0001f) {
			this.thickness = thickness;

			// If the thickness changes, the number of vertices is the same.
			// We only need to update the positions of said vertices.
			updateGeometry();
			updateBound();
		}

		return;
	}

	/**
	 * Sets the number of curve samples for the curved edges of the mesh. This
	 * will cause the mesh to update if the value changes.
	 * 
	 * @param curveSamples
	 *            The new number of curve samples. Must be greater than 0.
	 */
	public void setCurveSamples(int curveSamples) {

		if (curveSamples != this.curveSamples && curveSamples > 0) {
			this.curveSamples = curveSamples;

			// If the number of curve samples changes, so does the number of
			// vertices. Thus, we must update the positions AND both the buffers
			// and indices.
			updateBuffers();
			updateGeometry();
			updateIndices();
			updateBound();
		}

		return;
	}

	/**
	 * Sets the bounds of the central part of the mesh. This part is what
	 * contains all the pipe meshes (tubes) in the reactor.
	 * 
	 * @param box
	 *            A BoundingBox defining the bounds of the central part of the
	 *            reactor core mesh. Must not be null to have an effect.
	 */
	public void setReactorBounds(BoundingBox box) {

		if (box != null) {
			// Make sure the bounds have actually changed before redrawing the
			// mesh.
			Vector3f min = bounds.getMin(new Vector3f());
			Vector3f max = bounds.getMax(new Vector3f());
			Vector3f newMin = box.getMin(new Vector3f());
			Vector3f newMax = box.getMax(new Vector3f());
			if (!min.equals(newMin) || !max.equals(newMax)) {
				bounds.setMinMax(newMin, newMax);

				// If the bounds of the reactor have changed, the number of
				// vertices remains the same. We need only update the vertex
				// positions.
				updateGeometry();
				updateBound();
			}
		}

		return;
	}

	/**
	 * Gets the number of vertices along a single edge of the belt. The total
	 * number of unique vertices in the mesh will be this number * four (there
	 * are four "faces" to this mesh), but the total size of the vertex buffer
	 * should be twice that (we duplicate vertices to provide "sharp" normals
	 * for the front and back of the mesh).
	 * 
	 * @return The number of vertices along a single edge of the mesh.
	 */
	private int getNumberOfVerticesPerEdge() {
		// Compute the number of vertices along a single edge of the mesh.
		return 2 * (curveSamples + 1);
	}

	/**
	 * Gets the number of boxes or "strips" comprising the mesh. There is a box
	 * on the right, one on the left, and {@link #curveSamples} boxes for each
	 * of the top and bottom curved portions of the mesh (2 * (curveSamples) +
	 * 1). Each box is composed of 4 faces or polygons, each of which is
	 * composed of 2 triangles.
	 * 
	 * @return The number of boxes comprising the mesh.
	 */
	private int getNumberOfBoxes() {
		// The number of boxes around the mesh. Each box only has four sides
		// rendered, and the boxes loop around to form the 3D mesh.
		return 2 * (curveSamples + 1);
	}

	/**
	 * A utility function that fills out a pre-defined triple float or vector
	 * with the specified values.
	 * 
	 * @param vector
	 *            The float array to fill out.
	 * @param x
	 *            The x value to put in the float.
	 * @param y
	 *            The y value to put in the float.
	 * @param z
	 *            The z value to put in the float.
	 */
	private void setVector(float[] vector, float x, float y, float z) {
		vector[0] = x;
		vector[1] = y;
		vector[2] = z;
	}

	/**
	 * Re-allocates the buffers for vertex positions, vertex normals, and face
	 * indices.
	 */
	private void updateBuffers() {

		// Each vertex must be specified in the buffer twice. This is because
		// the front and back sides of the mesh must have separate normals from
		// the curved edges to get the "sharp" look from the shader.
		// In this case, it's numVerticesPerEdge * 2 (inside and outside edge) *
		// 2 (front and back) * 2 (duplication for sharp edge).
		setBuffer(
				Type.Position,
				3,
				createVector3Buffer(getFloatBuffer(Type.Position),
						getNumberOfVerticesPerEdge() * 8));

		// We need one normal per vertex.
		setBuffer(
				Type.Normal,
				3,
				createVector3Buffer(getFloatBuffer(Type.Normal),
						getVertexCount()));

		// Two specify two triangles per face, we need numBoxes * 4 rendered
		// sides * 6 integers (to specify the two triangles).
		setBuffer(
				Type.Index,
				3,
				createShortBuffer(getShortBuffer(Type.Index),
						getNumberOfBoxes() * 24));

		return;
	}

	/**
	 * Updates the indices for all triangular polygons comprising the mesh.
	 */
	private void updateIndices() {

		// The orientation of the mesh does not matter so much here. All that is
		// important to know about this method is that it loops over the
		// vertices and creates quads (rather, to triangles) for each set of
		// four vertices. Note that since our mesh has duplicate vertices for
		// the back/front and outside/inside (it's for additional normals), our
		// main loop variable i is incremented by 8 at each step instead of 4.

		// Get the number of boxes and the index buffer.
		int totalBoxes = getNumberOfBoxes();
		IndexBuffer ib = getIndexBuffer();

		int i = 0;
		int index = 0;

		for (; i < (totalBoxes - 1) * 8; i += 8) {
			// This sets the indices for the back, front, outer, and inner
			// sides, respectively.
			for (int j = 0; j <= 6; j += 2) {
				ib.put(index++, i + j);
				ib.put(index++, i + j + 9);
				ib.put(index++, i + j + 8);
				ib.put(index++, i + j + 9);
				ib.put(index++, i + j);
				ib.put(index++, i + j + 1);
			}
		}

		// Re-use the first set of vertices for the last box.
		for (int j = 0; j <= 6; j += 2) {
			ib.put(index++, i + j);
			ib.put(index++, (i + j + 9) % 8);
			ib.put(index++, (i + j + 8) % 8);
			ib.put(index++, (i + j + 9) % 8);
			ib.put(index++, i + j);
			ib.put(index++, i + j + 1);
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
		 * 1) The outside of the belt 
		 * 2) The inside of the belt 
		 * 3) The front, narrow edge of the mesh 
		 * 4) The back, narrow edge of the mesh
		 * 
		 * This is because the normals for the outside and inside edges should be
		 * calculated based on adjacent faces to appear rounded. The narrow sides of
		 * the mesh must have their own normals without appearing rounded.
		 */

		// Get the bounds of the mesh.
		Vector3f min = bounds.getMin(new Vector3f());
		Vector3f max = bounds.getMax(new Vector3f());

		// Get the radius (half width) and center x coordinate of the bounds.
		float radius = (max.x - min.x) / 2f;
		float centerX = max.x - radius;

		// To reduce math ops, we compute frequently used angles in advance. It
		// may be helpful to think of these angles as the curve samples split
		// across the pi radians comprising the positive y values.
		float[] cos = new float[curveSamples + 1];
		float[] sin = new float[curveSamples + 1];
		float centralAngle = FastMath.PI;
		float inverseRadial = 1.0f / curveSamples;
		cos[0] = 1f;
		sin[0] = 0f;
		for (int i = 1; i < curveSamples; i++) {
			float angle = i * centralAngle * inverseRadial;
			cos[i] = FastMath.cos(angle);
			sin[i] = FastMath.sin(angle);
		}
		cos[curveSamples] = -1f;
		sin[curveSamples] = 0f;
		// Note: The angles above do not correspond to the center of the whole
		// mesh, but to the centers at the top and bottom of the mesh.

		// Get the position and normal buffers.
		FloatBuffer pb = getFloatBuffer(Type.Position);
		FloatBuffer nb = getFloatBuffer(Type.Normal);
		pb.rewind();
		nb.rewind();

		float[] normal = new float[3];
		float[] frontNormal = { 0f, 0f, 1f };
		float[] backNormal = { 0f, 0f, -1f };

		// Loop around the curved top of the mesh from the top right of the box
		// to the top left of the box.
		for (int i = 0; i <= curveSamples; i++) {
			float x1 = centerX + cos[i] * (radius + thickness);
			float x2 = centerX + cos[i] * radius;
			float y1 = max.y + sin[i] * (radius + thickness);
			float y2 = max.y + sin[i] * radius;

			// back
			pb.put(x1).put(y1).put(min.z);
			pb.put(x2).put(y2).put(min.z);
			nb.put(backNormal).put(backNormal);
			// front
			pb.put(x2).put(y2).put(max.z);
			pb.put(x1).put(y1).put(max.z);
			nb.put(frontNormal).put(frontNormal);
			// outside
			pb.put(x1).put(y1).put(max.z);
			pb.put(x1).put(y1).put(min.z);
			setVector(normal, cos[i], sin[i], 0f);
			nb.put(normal).put(normal);
			// inside
			pb.put(x2).put(y2).put(min.z);
			pb.put(x2).put(y2).put(max.z);
			setVector(normal, -cos[i], -sin[i], 0f);
			nb.put(normal).put(normal);
		}

		// Loop around the curved bottom of the mesh from the bottom left of the
		// box to the bottom right of the box.
		for (int i = 0; i <= curveSamples; i++) {
			float x1 = centerX - cos[i] * (radius + thickness);
			float x2 = centerX - cos[i] * radius;
			float y1 = min.y - sin[i] * (radius + thickness);
			float y2 = min.y - sin[i] * radius;

			// back
			pb.put(x1).put(y1).put(min.z); // 0
			pb.put(x2).put(y2).put(min.z); // 1
			nb.put(backNormal).put(backNormal);
			// front
			pb.put(x2).put(y2).put(max.z); // 2
			pb.put(x1).put(y1).put(max.z); // 3
			nb.put(frontNormal).put(frontNormal);
			// outside
			pb.put(x1).put(y1).put(max.z); // 4 == 3
			pb.put(x1).put(y1).put(min.z); // 5 == 0
			setVector(normal, -cos[i], -sin[i], 0f);
			nb.put(normal).put(normal);
			// inside
			pb.put(x2).put(y2).put(min.z); // 6 == 1
			pb.put(x2).put(y2).put(max.z); // 7 == 2
			setVector(normal, cos[i], sin[i], 0f);
			nb.put(normal).put(normal);
			// It's not tha east or tha west side... (No it's not!)
			// It's not tha north or tha south side... (No it's not!)
			// It's tha dark side. (That is correct!)
		}

		// We have to set the buffer again to get the Mesh#updateBound() method
		// to work properly.
		setBuffer(Type.Position, 3, pb);
		setBuffer(Type.Normal, 3, nb);

		return;
	}

}
