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

import java.util.List;

import org.eclipse.ice.reactor.plant.Reactor;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * This class provides a view for a {@link Reactor}. A reactor is rendered as a
 * capsule-like belt shape around a set of control channel pipes. A basic box
 * contains all of the pipes, while hemispheres are drawn on the top and bottom.<br>
 * <br>
 * <b>Operations in this class (not including the constructor) should be called
 * from a SimpleApplication's simpleUpdate() thread.</b>
 * 
 * @author djg
 * 
 */
public class ReactorView extends AbstractPlantView {

	/**
	 * The mesh for the {@link AbstractPlantView#geometry}. This is a
	 * capsule-like belt shape around a set of control channel pipes.
	 */
	private final ReactorMesh mesh;

	/**
	 * The map that contains the BoundingBoxes of all {@link PipeView}s
	 * contained in this reactor. BoundingBoxes are keyed on their pipe's IDs.
	 * The map also provides the min and max bounds of all contained
	 * BoundingBoxes.
	 */
	private final BoundingBoxMap boxMap;

	/**
	 * The default constructor. It creates a mesh for the view's
	 * {@link AbstractPlantView#geometry}.
	 * 
	 * @param name
	 *            The name of the AbstractView's node,
	 *            {@link AbstractView#viewNode}.
	 * @param material
	 *            The jME3 Material that should be used for the view's geometry.
	 *            Must not be null.
	 */
	public ReactorView(String name, Material material) {
		super(name, material);

		// Create the mesh for the geometry.
		mesh = new ReactorMesh();
		geometry.setMesh(mesh);

		// Initialize the map used to keep track of the pipe BoundingBoxes based
		// on their IDs.
		boxMap = new BoundingBoxMap();

		// If any of the arguments were invalid, we should throw an exception
		// now after all class variables have been initialized.
		if (material == null) {
			throw new IllegalArgumentException(
					"JunctionView: Material is null!");
		}
		return;
	}

	/**
	 * Detaches all BoundingBoxes defined by the specified pipes.
	 * 
	 * @param ids
	 *            The IDs of the pipes that have been detached from the
	 *            Junction.
	 */
	public void removePipes(List<Integer> ids) {

		// Remove the specified pipes. If the bounds have changed, update
		// the mesh.
		if (boxMap.removeAll(ids)) {
			refreshMesh();
		}
		return;
	}

	/**
	 * Adds or updates the view with BoundingBoxes defined by the specified
	 * pipes. If the overall bounds have changed, the mesh will be updated.
	 * 
	 * @param ids
	 *            A List of affected Pipe IDs.
	 * @param boxes
	 *            The BoundingBoxes for the changing Pipes' outlets or inlets.
	 */
	public void putPipes(List<Integer> ids, List<BoundingBox> boxes) {

		// Add/update the specified pipes. If the bounds have changed, update
		// the mesh.
		if (boxMap.putAll(ids, boxes)) {
			refreshMesh();
		}
		return;
	}

	/**
	 * Updates the box mesh based on the current min and max vectors in
	 * {@link #boxMap}.
	 */
	private void refreshMesh() {

		// Get the BoundingBox from the map. If null, use a default unit box.
		BoundingBox box = boxMap.getBoundingBox();
		if (box == null) {
			box = new BoundingBox(new Vector3f(), new Vector3f(1f, 1f, 1f));
		}
		// Get the min and max points from the bounding box.
		Vector3f min = box.getMin(new Vector3f());
		Vector3f max = box.getMax(new Vector3f());

		// Get the extents. We need to rotate the reactor mesh depending on
		// which sides of the box are bigger.
		float x = box.getXExtent();
		float y = box.getYExtent();
		float z = box.getZExtent();

		// Create a quaternion for rotation.
		Quaternion rotation = new Quaternion();

		// Determine the rotation quaternion based on the sizes of the sides of
		// the box.
		if (y >= x && y >= z) {
			// No outer rotation.
			if (x < z) {
				// Rotate 90 degrees about y axis.
				rotation.fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
			}
		} else if (x >= y && x >= z) {
			// Rotate 90 degrees about z axis.
			rotation.fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);
			if (y < z) {
				// Rotate 90 degrees about x axis.
				rotation.multLocal(new Quaternion().fromAngleNormalAxis(
						FastMath.HALF_PI, Vector3f.UNIT_X));
			}
		} else {
			// Rotate 90 degrees about x axis.
			rotation.fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
			if (x < y) {
				// Rotate 90 degrees about z axis.
				rotation.multLocal(new Quaternion().fromAngleNormalAxis(
						FastMath.HALF_PI, Vector3f.UNIT_Z));
			}
		}

		// Rotate the bounding box with the quaternion and apply the inverse
		// rotation to the geometry.
		box.setMinMax(rotation.multLocal(min), rotation.multLocal(max));
		geometry.setLocalRotation(rotation.inverseLocal());

		// Update the mesh.
		mesh.setReactorBounds(box);
		geometry.updateModelBound();

		return;
	}

}
