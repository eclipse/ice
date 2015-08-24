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

import org.eclipse.ice.viz.service.datastructures.IVizUpdateable;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;

/**
 * <p>
 * This class provides a controller for Edge instances, and it creates an
 * EdgeView for an Edge. It also listens for changes to its Edge, at which point
 * it updates its EdgeView. It can also send updates to the Edge model based on
 * user input.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class EdgeController extends AbstractMeshController {
	/**
	 * <p>
	 * The EdgeView managed by this controller.
	 * </p>
	 * 
	 */
	private EdgeView view;
	/**
	 * <p>
	 * The Edge managed by this controller.
	 * </p>
	 * 
	 */
	private Edge model;

	// ---- Property IDs. ---- //
	/**
	 * The location of the edge's end points.
	 */
	private static final String locationId = "location";

	// ----------------------- //

	/**
	 * <p>
	 * The default constructor.
	 * </p>
	 * 
	 * @param edge
	 *            <p>
	 *            The Edge managed by this controller.
	 *            </p>
	 * @param queue
	 *            <p>
	 *            The queue used for updating views handled by this and other
	 *            controllers.
	 *            </p>
	 */
	public EdgeController(Edge edge,
			ConcurrentLinkedQueue<AbstractMeshController> queue,
			Material material) {
		super(edge, queue);

		// Store a reference to the model.
		model = edge;

		// Create a view.
		view = new EdgeView(Integer.toString(model.getId()), material);

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
				float[] start = model.getStartLocation();
				float[] end = model.getEndLocation();
				Vector3f startVector = new Vector3f(start[0], start[1],
						start[2]);
				Vector3f endVector = new Vector3f(end[0], end[1], end[2]);
				float scale = getInverseScale();
				view.setLine(startVector.multLocal(scale),
						endVector.multLocal(scale));
			}
		};
		properties.put(scaleId, locationHandler);
		properties.put(locationId, locationHandler);
		// -------------------------------------------------------- //

		return;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this Edge and another
	 * Edge. It returns true if the objects are equal and false if they are not.
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
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the Edge.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode of the object.
	 *         </p>
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * <p>
	 * This operation copies the contents of a Edge into the current object
	 * using a deep copy.
	 * </p>
	 * 
	 * @param controller
	 *            <p>
	 *            The object from which the values should be copied.
	 *            </p>
	 */
	@Override
	public void copy(AbstractMeshController controller) {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * This operation returns a clone of the Edge using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		EdgeController object = new EdgeController((Edge) model.clone(),
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