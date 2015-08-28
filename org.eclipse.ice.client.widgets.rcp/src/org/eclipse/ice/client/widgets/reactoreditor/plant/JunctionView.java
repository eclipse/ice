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

import org.eclipse.ice.reactor.plant.Junction;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

/**
 * This class provides a view for a {@link Junction}. A junction is rendered as
 * a box that contains the top ends of all input pipes and all bottom ends of
 * output pipes.<br>
 * <br>
 * <b>Operations in this class (not including the constructor) should be called
 * from a SimpleApplication's simpleUpdate() thread.</b>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class JunctionView extends AbstractPlantView {

	// TODO Replace the synchronized flags with locks where necessary.

	/**
	 * The mesh for the {@link AbstractPlantView#geometry} . This is a simple
	 * box that represents a junction. It should also use a predefined texture
	 * on each side.
	 */
	private final Box box;

	/**
	 * The map that contains the BoundingBoxes of all {@link PipeView}
	 * inlets/outlets connected to this junction. BoundingBoxes are keyed on
	 * their pipes' IDs. The map also provides the min and max bounds of all
	 * contained BoundingBoxes.
	 */
	private final BoundingBoxMap boxMap;

	/**
	 * The current bounds of {@link #box}. It is updated in
	 * {@link #refreshBounds()} and synchronized with the geometry and its mesh
	 * in {@link #refreshMesh()}.
	 */
	private final BoundingBox boundingBox;

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
	public JunctionView(String name, Material material) {
		super(name, material);

		// Create the mesh (a box) for the geometry.
		box = new Box(1f, 1f, 1f);
		geometry.setMesh(box);

		// Initialize the map used to keep track of the pipe inlet/outlet
		// BoundingBoxes based on their IDs.
		boxMap = new BoundingBoxMap();

		// Initialize the overall bounding box.
		boundingBox = new BoundingBox(Vector3f.ZERO, Vector3f.UNIT_XYZ);

		// If any of the arguments were invalid, we should throw an exception
		// now after all class variables have been initialized.
		if (material == null) {
			throw new IllegalArgumentException(
					"JunctionView: Material is null!");
		}

		return;
	}

	/**
	 * Detaches all BoundingBoxes defined by the outlets or inlets of input or
	 * output pipes, respectively.
	 * 
	 * @param ids
	 *            The IDs of the pipes that have been detached from the
	 *            Junction.
	 */
	public synchronized boolean removePipes(List<Integer> ids) {

		// Remove the specified pipes. If the bounds have changed, update
		// the mesh.
		boolean boundsChanged = boxMap.removeAll(ids);
		if (boundsChanged) {
			refreshBounds();
		}

		return boundsChanged;
	}

	/**
	 * Adds or updates the view with BoundingBoxes defined by the outlets or
	 * inlets of input or output pipes, respectively. If the overall bounds have
	 * changed, the mesh will be updated.
	 * 
	 * @param ids
	 *            A List of affected Pipe IDs.
	 * @param boxes
	 *            The BoundingBoxes for the changing Pipes' outlets or inlets.
	 */
	public synchronized boolean putPipes(List<Integer> ids,
			List<BoundingBox> boxes) {

		// Add/update the specified pipes. If the bounds have changed, update
		// the mesh.
		boolean boundsChanged = boxMap.putAll(ids, boxes);
		if (boundsChanged) {
			refreshBounds();
		}

		return boundsChanged;
	}

	/**
	 * Refreshes {@link #boundingBox} based on the current max and min vectors
	 * in {@link #boxMap}.
	 */
	private synchronized void refreshBounds() {
		Vector3f min = new Vector3f();
		Vector3f max = new Vector3f(1f, 1f, 1f);

		BoundingBox boxMapBounds = boxMap.getBoundingBox();
		if (boxMapBounds != null) {
			min = boxMapBounds.getMin(min);
			max = boxMapBounds.getMax(max);
		}

		boundingBox.setMinMax(min, max);

		return;
	}

	/**
	 * Sets the {@link BoundingBox}es for the ends of the attached secondary
	 * pipes. These pipes should already be connected to the center of the
	 * junction via {@link #getCenter()}.
	 * 
	 * @param boxes
	 *            A List of BoundingBoxes for secondary pipe inlets/outlets.
	 * @return True if the bounds changed, false otherwise.
	 */
	public synchronized boolean setSecondaryPipes(List<BoundingBox> boxes) {
		// To tell if the bounds changed, we need to store the current min and
		// max values from the bounding box and see if they change.
		Vector3f previousMin = boundingBox.getMin(new Vector3f());
		Vector3f previousMax = boundingBox.getMax(new Vector3f());
		Vector3f min = new Vector3f();
		Vector3f max = new Vector3f();

		// Reset the BoundingBox to be just big enough for all of the primary
		// pipes. This is necessary in case the secondary pipes that are already
		// attached produce the max and min values.
		refreshBounds();

		boolean boundsChanged = false;

		// Update the BoundingBox with the min and max values from the list.
		if (boxes != null) {
			min = boundingBox.getMin(min);
			max = boundingBox.getMax(max);

			Vector3f tmp = new Vector3f();
			for (BoundingBox box : boxes) {
				if (box != null) {
					min.minLocal(box.getMin(tmp));
					max.maxLocal(box.getMax(tmp));
				}
			}
			boundingBox.setMinMax(min, max);
		}

		// Get the new min and max values from the bounding box and compare
		// them with the previous values.
		min = boundingBox.getMin(min);
		max = boundingBox.getMax(max);
		boundsChanged = (min.x != previousMin.x || min.y != previousMin.y
				|| min.z != previousMin.z || max.x != previousMax.x
				|| max.y != previousMax.y || max.z != previousMax.z);

		return boundsChanged;
	}

	/**
	 * Updates the box mesh based on the current min and max vectors in
	 * {@link #boxMap}.
	 */
	public void refreshMesh() {

		// Pad the size of the mesh a little to avoid flat boxes and to prevent
		// glitching (that results from pipes and boxes having the same
		// surfaces).
		float offset = 0.01f;
		Vector3f min = boundingBox.getMin(new Vector3f());
		Vector3f max = boundingBox.getMax(new Vector3f());
		min.subtractLocal(offset, offset, offset);
		max.addLocal(offset, offset, offset);

		// Update the mesh and the geometry.
		box.updateGeometry(min, max);
		geometry.updateModelBound();

		return;
	}

	/**
	 * Gets the current center of the JunctionView. The view's rotation and
	 * orientation are not applied.
	 * 
	 * @return A {@link Vector3f} with the coordinates of the junction's center.
	 */
	public synchronized Vector3f getCenter() {
		return boundingBox.getCenter(new Vector3f());
	}

}
