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
 * This class provides a custom mesh for displaying a Peacock-style reactor. The
 * mesh has a roughly capsule shape with two sides cut away so that it resembles
 * a belt.<br>
 * <br>
 * When given a {@link BoundingBox}, the mesh will contain the entire box. The
 * front of the box will be exposed, the left and right side will be closed, and
 * the top and bottom sides will be curved.<br>
 * <br>
 * In practice, the two biggest sides of the box should be exposed so that the
 * pipes inside can be seen. The next two biggest sides should be solid, while
 * the two smallest sides should be the curved sides. To do this, you need to
 * rotate the spatial that will be using this mesh.<br>
 * <br>
 * The thickness of the reactor wall can be set with
 * {@link #setThickness(float)}.
 * 
 * @author djg
 * 
 */
public class ReactorMesh extends Mesh {
	// TODO Add normals.

	/**
	 * The bounds of the central part of the mesh. This part is what contains
	 * all the pipe meshes (tubes) in the reactor.
	 */
	private final BoundingBox bounds;

	/**
	 * How thick to draw the "belt" shape of the mesh.
	 */
	private static final float thickness = 0.1f;

	/**
	 * The number of sections on one of the two rounded ends of the mesh.
	 */
	private static final int curveSamples = 10;

	// If the number of boxes changes (i.e. curveSamples changes), we need
	// to recreate all buffers!

	/**
	 * The default constructor. Creates a reactor mesh around a unit box.
	 */
	public ReactorMesh() {
		Vector3f min = new Vector3f(Vector3f.ZERO);
		Vector3f max = new Vector3f(1f, 1f, 1f);

		bounds = new BoundingBox(min, max);

		// Create the buffers and fill out the initial index and vertex data.
		createBuffers();
		setIndices();
		setVertices();

		// Update the mesh.
		updateBound();

		return;
	}

	/**
	 * Gets the number of unique vertices in the mesh.
	 * 
	 * @return The number of unique vertices in the mesh.
	 */
	private int getNumberOfVertices() {
		return 4 * (getNumberOfBoxes());
	}

	/**
	 * Gets the number of "boxes" in the mesh. The boxes in the mesh do not have
	 * tops or bottoms and connect to each other to form the mesh.
	 * 
	 * @return The number of boxes in the mesh. Each box has a strip of 8
	 *         triangles comprising its 4 sides.
	 */
	private int getNumberOfBoxes() {
		return 2 * (curveSamples + 1);
	}

	/**
	 * Creates the vertex and index buffers along with any other buffers.
	 */
	private void createBuffers() {

		// Create the vertex array. We need number of vertices * 3.
		setBuffer(
				Type.Position,
				3,
				createVector3Buffer(getFloatBuffer(Type.Position),
						getNumberOfVertices()));

		// Create the index array. We need number of boxes * 4 sides * 6
		// integers.
		setBuffer(
				Type.Index,
				3,
				createShortBuffer(getShortBuffer(Type.Index),
						getNumberOfBoxes() * 6 * 4));

		return;
	}

	/**
	 * Updates the vertex buffer based on the current {@link #bounds}.
	 */
	private void setVertices() {

		// Get the bounds of the mesh.
		Vector3f min = bounds.getMin(new Vector3f());
		Vector3f max = bounds.getMax(new Vector3f());

		// Get the radius (half width) and center x coordinate of the bounds.
		float radius = (max.x - min.x) / 2f;
		float centerX = max.x - radius;

		// Get the vertex buffer. We'll put vertex locations in this.
		FloatBuffer pb = getFloatBuffer(Type.Position);

		// To reduce math ops, we compute frequently used angles in advance. We
		// actually do not use the first element in the array, but we leave it
		// for convenience (so we don't have an offset index).
		float[] sin = new float[curveSamples + 1];
		float[] cos = new float[curveSamples + 1];
		float centralAngle = FastMath.PI;
		float inverseRadial = 1.0f / curveSamples;
		for (int i = 1; i <= curveSamples; i++) {
			float angle = i * centralAngle * inverseRadial;
			sin[i] = FastMath.sin(angle);
			cos[i] = FastMath.cos(angle);
		}

		// Reset the marker in the buffer to the first position.
		pb.rewind();

		// ---- The right side of the mesh: a box. ---- //
		// Set the bottom vertices of the right box.
		pb.put(max.x + thickness).put(min.y).put(min.z);
		pb.put(max.x + thickness).put(min.y).put(max.z);
		pb.put(max.x).put(min.y).put(max.z);
		pb.put(max.x).put(min.y).put(min.z);
		// Set the top vertices of the right box.
		pb.put(max.x + thickness).put(max.y).put(min.z);
		pb.put(max.x + thickness).put(max.y).put(max.z);
		pb.put(max.x).put(max.y).put(max.z);
		pb.put(max.x).put(max.y).put(min.z);
		// -------------------------------------------- //

		// ---- The top of the mesh: a curved surface. ---- //
		for (int i = 1; i <= curveSamples; i++) {
			float x1 = centerX + cos[i] * (radius + thickness);
			float x2 = centerX + cos[i] * radius;
			float y1 = max.y + sin[i] * (radius + thickness);
			float y2 = max.y + sin[i] * radius;
			pb.put(x1).put(y1).put(min.z);
			pb.put(x1).put(y1).put(max.z);
			pb.put(x2).put(y2).put(max.z);
			pb.put(x2).put(y2).put(min.z);
		}
		// ------------------------------------------------ //

		// ---- The left side of the mesh: a box. ---- //
		pb.put(min.x - thickness).put(min.y).put(min.z);
		pb.put(min.x - thickness).put(min.y).put(max.z);
		pb.put(min.x).put(min.y).put(max.z);
		pb.put(min.x).put(min.y).put(min.z);
		// ------------------------------------------- //

		// ---- The bottom of the mesh: a curved surface. ---- //
		for (int i = 1; i < curveSamples; i++) {
			float x1 = centerX - cos[i] * (radius + thickness);
			float x2 = centerX - cos[i] * radius;
			float y1 = min.y - sin[i] * (radius + thickness);
			float y2 = min.y - sin[i] * radius;
			pb.put(x1).put(y1).put(min.z);
			pb.put(x1).put(y1).put(max.z);
			pb.put(x2).put(y2).put(max.z);
			pb.put(x2).put(y2).put(min.z);
		}
		// --------------------------------------------------- //

		// We have to set the buffer again to get the Mesh#updateBound() method
		// to work properly.
		setBuffer(Type.Position, 3, pb);

		return;
	}

	/**
	 * Updates the index buffer based on {@link #curveSamples}.
	 */
	private void setIndices() {

		// In this class, a "box" in the mesh has its top and bottom removed.
		// They attach to each other like Lego pieces, so the tops and bottoms
		// do not need to be defined in the index buffer. We start with the
		// vertical box on the right of the loop and work our way around
		// counter-clockwise. For each box, we loop around from the right side
		// around the y-axis all the way to the back side, setting the bottom
		// triangle for the side first, then setting the top triangle. (Of
		// course, as we move around the mesh, our definition of right, left,
		// bottom, and top change with respect to the orientation.)

		// Get the number of boxes and the index buffer.
		int totalBoxes = getNumberOfBoxes();
		IndexBuffer ib = getIndexBuffer();

		// Set the indices for each side of each box. In each iteration, we move
		// i to the lowest-indexed vertex in each box, then wrap around the box
		// from there.
		int i = 0, index = 0;
		for (; i < (totalBoxes - 1) * 4; i += 4) {
			// The right side.
			ib.put(index++, i);
			ib.put(index++, i + 5);
			ib.put(index++, i + 1);
			ib.put(index++, i);
			ib.put(index++, i + 4);
			ib.put(index++, i + 5);
			// The front side.
			ib.put(index++, i + 1);
			ib.put(index++, i + 6);
			ib.put(index++, i + 2);
			ib.put(index++, i + 1);
			ib.put(index++, i + 5);
			ib.put(index++, i + 6);
			// The left side.
			ib.put(index++, i + 2);
			ib.put(index++, i + 7);
			ib.put(index++, i + 3);
			ib.put(index++, i + 2);
			ib.put(index++, i + 6);
			ib.put(index++, i + 7);
			// The back side.
			ib.put(index++, i + 3);
			ib.put(index++, i + 4);
			ib.put(index++, i);
			ib.put(index++, i + 3);
			ib.put(index++, i + 7);
			ib.put(index++, i + 4);
			// It's not tha east or tha west side... (No it's not!)
			// It's not tha north or tha south side... (No it's not!)
			// It's tha dark side. (That is correct!)
		}

		// ---- Re-use the first 4 vertices for the last box. ---- //
		// We could use modulo division, but, in the interest of performance, we
		// do this by hand.

		// The right side.
		ib.put(index++, i);
		ib.put(index++, 1);
		ib.put(index++, i + 1);
		ib.put(index++, i);
		ib.put(index++, 0);
		ib.put(index++, 1);
		// The front side.
		ib.put(index++, i + 1);
		ib.put(index++, 2);
		ib.put(index++, i + 2);
		ib.put(index++, i + 1);
		ib.put(index++, 1);
		ib.put(index++, 2);
		// The left side.
		ib.put(index++, i + 2);
		ib.put(index++, 3);
		ib.put(index++, i + 3);
		ib.put(index++, i + 2);
		ib.put(index++, 2);
		ib.put(index++, 3);
		// The back side.
		ib.put(index++, i + 3);
		ib.put(index++, 0);
		ib.put(index++, i);
		ib.put(index++, i + 3);
		ib.put(index++, 3);
		ib.put(index++, 0);
		// ------------------------------------------------------- //

		return;
	}

	/**
	 * Gets the bounds of the central part of the mesh. This part is what
	 * contains all the pipe meshes (tubes) in the reactor.
	 * 
	 * @return A BoundingBox defining the bounds of the central part of the
	 *         reactor core mesh.
	 */
	public BoundingBox getReactorBounds() {
		return new BoundingBox(bounds);
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
			// Update bounds with the new bounding box.
			bounds.setMinMax(box.getMin(new Vector3f()),
					box.getMax(new Vector3f()));

			// Update the vertex data and refresh the mesh.
			setVertices();
			updateBound();
		}

		return;
	}

}
