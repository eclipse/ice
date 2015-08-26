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
package org.eclipse.ice.viz.service.jme3.mesh;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;

/**
 * <p>
 * This class provides a controller for Vertex instances, and it creates a
 * VertexView for a Vertex. It also listens for changes to its Vertex, at which
 * point it updates its VertexView. It can also send updates to the Vertex model
 * based on user input.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class VertexController extends AbstractMeshController {
	/**
	 * <p>
	 * The VertexView managed by this controller.
	 * </p>
	 * 
	 */
	private VertexView view;
	/**
	 * <p>
	 * The Vertex managed by this controller.
	 * </p>
	 * 
	 */
	private Vertex model;

	// ---- Property IDs. ---- //
	/**
	 * The location of the vertex.
	 */
	private static final String locationId = "location";

	// ----------------------- //

	/**
	 * <p>
	 * The default constructor.
	 * </p>
	 * 
	 * @param vertex
	 *            <p>
	 *            The Vertex managed by this controller.
	 *            </p>
	 * @param queue
	 *            <p>
	 *            The queue used for updating views handled by this and other
	 *            controllers.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The jME3 Material that should be used for the vertex graphics.
	 *            </p>
	 */
	// TODO Update documentation in model. It has the jME3 material now!
	public VertexController(Vertex vertex,
			ConcurrentLinkedQueue<AbstractMeshController> queue,
			Material material) {
		super(vertex, queue);

		// Store a reference to the model.
		model = vertex;

		// Create a new view.
		view = new VertexView(Integer.toString(model.getId()), material);

		// ---- Set up the property handlers to sync the view. ---- //
		properties.put(stateId, new PropertyHandler() {
			@Override
			public void syncView() {
				view.setColor(getState().getColor());
			}
		});
		properties.put(parentNodeId, new PropertyHandler() {
			@Override
			public void syncView() {
				view.setParentNode(getParentNode());
			}
		});
		properties.put(sizeId, new PropertyHandler() {
			@Override
			public void syncView() {
				view.setSize(getSize());
			}
		});
		// The scale and location properties both affect the location of the
		// vertex view.
		PropertyHandler locationHandler = new PropertyHandler() {
			@Override
			public void syncView() {
				view.setLocation(getLocation().multLocal(getInverseScale()));
			}
		};
		properties.put(scaleId, locationHandler);
		properties.put(locationId, locationHandler);
		// -------------------------------------------------------- //

		return;
	}

	/**
	 * <p>
	 * Gets the current location from the model.
	 * </p>
	 * 
	 * @return <p>
	 *         The current location of the Vertex.
	 *         </p>
	 */
	public Vector3f getLocation() {
		float[] loc = model.getLocation();
		return new Vector3f(loc[0], loc[1], loc[2]);
	}

	/**
	 * <p>
	 * Sets the current location in the model.
	 * </p>
	 * 
	 * @param location
	 *            <p>
	 *            The new location of the Vertex.
	 *            </p>
	 */
	public void setLocation(Vector3f location) {
		model.setLocation(location.x, location.y, location.z);
	}

	/**
	 * <p>
	 * This operation is used to check equality between this VertexController
	 * and another VertexController. It returns true if the objects are equal
	 * and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the objects are equal, false otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof VertexController) {

			// We can now cast the other object.
			VertexController controller = (VertexController) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && view.equals(controller.view));

			// The model is already handled by AbstractMeshController.
		}

		return equals;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the VertexController.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode of the object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Static hash.
		int hash = super.hashCode();

		// Add local hashes.
		hash = 31 * hash + view.hashCode();
		// The model is already handled by AbstractMeshController.

		return hash;
	}

	/**
	 * <p>
	 * This operation copies the contents of a VertexController into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param controller
	 *            <p>
	 *            The object from which the values should be copied.
	 *            </p>
	 */
	public void copy(VertexController controller) {

		// Check the parameter.
		if (controller != null) {

			// Perform the super copy operation.
			super.copy(controller);

			// The super copy method handles the queue and its own private
			// variables.

			// We need to copy the reference to the Vertex model. The super copy
			// only copies the reference to an IUpdateable.
			model = controller.model;

			// Dispose of the current view.
			view.dispose();

			// Clone the view.
			view = (VertexView) controller.view.clone();

			// We will need to update the view accordingly.
			updateQueue.add(this);
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the VertexController using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		VertexController object = new VertexController((Vertex) model.clone(),
				updateQueue, view.geometry.getMaterial());

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see AbstractMeshController#syncView()
	 */
	@Override
	public void syncView() {
		super.syncView();

		// If necessary, dispose of the view.
		if (disposed.get()) {
			view.dispose();
		}

		return;
	}

	/**
	 * Overrides the default behavior to make sure the appropriate properties
	 * that derive directly from the model get marked as dirty.
	 */
	@Override
	public void update(IVizUpdateable component) {
		// Update the location.
		setDirtyProperty(locationId);
	}
}