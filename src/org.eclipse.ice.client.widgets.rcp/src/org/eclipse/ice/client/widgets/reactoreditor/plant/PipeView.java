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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.ice.reactor.plant.Pipe;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;

/**
 * This class provides a view for a {@link Pipe}. A pipe is a hollow cylinder
 * with the input being the bottom end and the output being the top end.<br>
 * <br>
 * <b>Operations in this class (not including the constructor) should be called
 * from a SimpleApplication's simpleUpdate() thread.</b>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PipeView extends AbstractPlantView {

	/**
	 * The default radius of the pipe.
	 */
	protected static final float defaultRadius = 0.1f;
	/**
	 * The default length of the pipe.
	 */
	protected static final float defaultLength = 1f;
	/**
	 * The default number of axial samples along the length of the pipe.
	 */
	protected static final int axialSamples = 5;
	/**
	 * The number of sides for the tube mesh. This is currently static, but this
	 * can be changed at a later date.
	 */
	protected static final int radialSamples = 10;

	/**
	 * The mesh for the {@link AbstractPlantView#geometry}. This is a tube that
	 * represents a pipe. The inner and outer radius are usually the same, and
	 * the radial elements should be 10.
	 */
	private final TubeMesh tube;

	/**
	 * The lock that should be used when reading mesh information.
	 */
	protected final Lock readLock;
	/**
	 * The lock that should be used when writing mesh information.
	 */
	protected final Lock writeLock;

	/**
	 * The default constructor. It creates a mesh for the view's
	 * {@link AbstractPlantView#geometry}.
	 * 
	 * @param name
	 *            The name of the view's root node.
	 * @param material
	 *            The jME3 Material that should be used for the view's geometry.
	 *            Must not be null.
	 */
	public PipeView(String name, Material material) {
		super(name, material);

		// Create the mesh (a tube) for the geometry.
		tube = new TubeMesh(defaultLength, defaultRadius, axialSamples,
				radialSamples);
		geometry.setMesh(tube);

		// Get the read/write locks from a ReentrantReadWriteLock.
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		readLock = lock.readLock();
		writeLock = lock.writeLock();

		// If any of the arguments were invalid, we should throw an exception
		// now after all class variables have been initialized.
		if (material == null) {
			throw new IllegalArgumentException("PipeView: Material is null!");
		}
		return;
	}

	/**
	 * Updates the mesh for the pipe view. This should be called when any of the
	 * pipe's physical dimensions or properties have changed.
	 * 
	 * @param radius
	 *            The radius of the pipe in jME3 world units.
	 * @param length
	 *            The length of the pipe in jME3 world units.
	 * @param axialSamples
	 *            The number of axial samples used to render the pipe. For
	 *            instance, a value of 2 should mean that there are 2 rows of
	 *            triangles forming the pipe along its length, i.e. there are 2
	 *            sections of the pipe.
	 */
	public void updateMesh(float radius, float length, int axialSamples) {

		// Update the mesh itself and force the geometry to refresh based on the
		// new mesh dimensions.
		writeLock.lock();
		try {
			tube.setLength(length);
			tube.setRadius(radius);
			tube.setAxialSamples(axialSamples);
			tube.refresh(true);
		} finally {
			writeLock.unlock();
		}

		return;
	}

	/**
	 * Applies any changes to the underlying mesh to
	 * {@link AbstractPlantView#geometry}.
	 */
	public void refreshMesh() {
		// Force the geometry to refresh based on the new mesh dimensions.
		geometry.updateModelBound();
	}

	/**
	 * Gets the vertices of the bottom (inlet) of the PipeView's mesh.
	 * 
	 * @param primary
	 *            Whether to get the inlet for the primary pipe or for the
	 *            secondary pipe associated with the view.
	 * @return An array of the vertices for the bottom of the pipe mesh.
	 */
	public Vector3f[] getBottomVertices(boolean primary) {
		// Because the source array could be destroyed in updateMesh(), we
		// should return a shallow copy of it. Fortunately, the Vector3f values
		// will not be destroyed by the mesh, so those do not need to be copied.
		Vector3f[] copy = null;
		readLock.lock();
		try {
			Vector3f[] vertices = tube.getBottomEdgeVertices();
			copy = new Vector3f[vertices.length];
			for (int i = 0; i < vertices.length; i++) {
				copy[i] = vertices[i];
			}
		} finally {
			readLock.unlock();
		}

		return copy;
	}

	/**
	 * Gets the vertices of the top (outlet) of the PipeView's mesh.
	 * 
	 * @param primary
	 *            Whether to get the outlet for the primary pipe or for the
	 *            secondary pipe associated with the view.
	 * @return An array of the vertices for the top of the pipe mesh.
	 */
	public Vector3f[] getTopVertices(boolean primary) {
		// Because the source array could be destroyed in updateMesh(), we
		// should return a shallow copy of it. Fortunately, the Vector3f values
		// will not be destroyed by the mesh, so those do not need to be copied.
		Vector3f[] copy = null;
		readLock.lock();
		try {
			Vector3f[] vertices = tube.getTopEdgeVertices();
			copy = new Vector3f[vertices.length];
			for (int i = 0; i < vertices.length; i++) {
				copy[i] = vertices[i];
			}
		} finally {
			readLock.unlock();
		}

		return copy;
	}

}
