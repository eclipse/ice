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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.ice.client.widgets.geometry.Tube;
import org.eclipse.ice.client.widgets.jme.IRenderQueue;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.reactor.plant.Pipe;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * This class provides a controller that manages a {@link Pipe} and an
 * associated {@link PipeView}. Any updates to the view should be coordinated
 * through this class.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PipeController extends AbstractPlantController {

	/**
	 * The {@link Pipe} model for which this controller provides a view.
	 */
	private final Pipe model;
	/**
	 * The {@link PipeView} associated with this controller.
	 */
	private final PipeView view;

	// ---- Additional Features ---- //
	// TODO Any features that are not a part of the model but are configurable
	// in Peacock should go here.
	// ----------------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model (a {@link Pipe}) for which this controller provides
	 *            a view.
	 * @param view
	 *            The view (a {@link PipeView}) associated with this controller.
	 * @param renderQueue
	 *            The queue responsible for tasks that need to be performed on
	 *            the jME rendering thread.
	 */
	public PipeController(Pipe model, PipeView view,
			IRenderQueue renderQueue) {
		super(model, view, renderQueue);

		// Set the model. If it is null, create a new, default model.
		this.model = (model != null ? model : new Pipe());

		// Set the view. If it is null, create a new, default view.
		this.view = (view != null ? view : new PipeView("Invalid View", null));

		// ---- Initialize any additional features. ---- //
		// --------------------------------------------- //

		// If any of the arguments were invalid, we should throw an exception
		// now after all class variables have been initialized.
		if (model == null) {
			throw new IllegalArgumentException(
					"PipeController error: Model is null!");
		} else if (view == null) {
			throw new IllegalArgumentException(
					"PipeController error: View is null!");
		} else if (renderQueue == null) {
			throw new IllegalArgumentException(
					"PipeController error: Update queue is null!");
		}

		// The view should not be attached to the scene yet, so we should be
		// able to synchronize it with the current model.
		view.updateMesh((float) model.getRadius(), (float) model.getLength(),
				model.getNumElements());
		view.setLocation(PlantMath.getVector(model.getPosition()));
		view.setRotation(getQuaternionFromOrientation(PlantMath.getVector(model
				.getOrientation())));
		view.refreshMesh();

		return;
	}

	/**
	 * Gets a {@link BoundingBox} oriented along the default axes that contains
	 * all vertices used to render the incoming (bottom) end of the pipe. This
	 * is for the primary inlet, and is the same as calling
	 * {@link #getInletBounds(boolean) getInletBounds(true)}.
	 * 
	 * @return Returns a BoundingBox containing the bottom of the pipe mesh.
	 */
	public BoundingBox getInletBounds() {
		return getInletBounds(true);
	}

	/**
	 * Gets a {@link BoundingBox} oriented along the default axes that contains
	 * all vertices used to render the incoming (bottom) end of the pipe.
	 * 
	 * @param primary
	 *            If true, gets the bounds of the pipe's <i>primary</i> inlet.
	 *            If false, gets the bounds of the pipe's <i>secondary</i>
	 *            inlet.
	 * 
	 * @return Returns a BoundingBox containing the bottom of the pipe mesh.
	 */
	public BoundingBox getInletBounds(boolean primary) {

		List<Vector3f> offsets = new ArrayList<Vector3f>();
		List<Quaternion> rotations = new ArrayList<Quaternion>();

		offsets.add(PlantMath.getVector(model.getPosition()));
		rotations.add(getQuaternionFromOrientation(PlantMath.getVector(model
				.getOrientation())));

		return getWorldBounds(view.getBottomVertices(primary), offsets,
				rotations);
	}

	/**
	 * Gets a {@link BoundingBox} oriented along the default axes that contains
	 * all vertices used to render the outgoing (top) end of the pipe. This is
	 * for the primary outlet, and is the same as calling
	 * {@link #getOutletBounds(boolean) getOutletBounds(true)}.
	 * 
	 * @return Returns a BoundingBox containing the top of the pipe mesh.
	 */
	public BoundingBox getOutletBounds() {
		return getOutletBounds(true);
	}

	/**
	 * Gets a {@link BoundingBox} oriented along the default axes that contains
	 * all vertices used to render the outgoing (top) end of the pipe.
	 * 
	 * @param primary
	 *            If true, gets the bounds of the pipe's <i>primary</i> outlet.
	 *            If false, gets the bounds of the pipe's <i>secondary</i>
	 *            outlet.
	 * @return Returns a BoundingBox containing the top of the pipe mesh.
	 */
	public BoundingBox getOutletBounds(boolean primary) {

		List<Vector3f> offsets = new ArrayList<Vector3f>();
		List<Quaternion> rotations = new ArrayList<Quaternion>();

		offsets.add(PlantMath.getVector(model.getPosition()));
		rotations.add(getQuaternionFromOrientation(PlantMath.getVector(model
				.getOrientation())));

		return getWorldBounds(view.getTopVertices(primary), offsets, rotations);
	}

	/**
	 * This is a generic method that computes the world coordinates for a list
	 * of coordinates taking into account a chain of offsets and rotations that
	 * may be affecting the world location of the points.<br>
	 * <br>
	 * <b>The lists of offsets and rotations must be the same size and must not
	 * contain null values.</b>
	 * 
	 * @param points
	 *            An array of points to be converted to world coordinates.
	 * @param offsets
	 *            A list of geometric offsets. These locations are equivalent to
	 *            local translations for nodes/geometries.
	 * @param rotations
	 *            A list of rotations. These rotations are equivalent to local
	 *            rotations for nodes/geometries.
	 * @return A BoundingBox in world coordinates. It contains all the points
	 *         described with all specified offsets and rotations applied.
	 */
	protected final BoundingBox getWorldBounds(Vector3f[] points,
			List<Vector3f> offsets, List<Quaternion> rotations) {

		// Set the default return value.
		BoundingBox box = new BoundingBox(Vector3f.ZERO, Vector3f.UNIT_XYZ);

		int size = offsets.size();
		if (points != null && points.length > 0
				&& offsets.size() == rotations.size()) {

			// Set the initial min and max values.
			Vector3f tmp = new Vector3f(points[0]);
			// Apply the rotations and offsets to the point.
			for (int i = 0; i < size; i++) {
				rotations.get(i).multLocal(tmp);
				tmp.addLocal(offsets.get(i));
			}
			Vector3f min = new Vector3f(tmp);
			Vector3f max = new Vector3f(tmp);

			// Loop over the points and update the min and max values based on
			// the world coordinates for the array of points.
			for (int i = 1; i < points.length; i++) {
				tmp.set(points[i]);
				// Apply the rotations and offsets to the point.
				for (int j = 0; j < size; j++) {
					rotations.get(j).multLocal(tmp);
					tmp.addLocal(offsets.get(j));
				}
				// Update the min and max as necessary.
				min.minLocal(tmp);
				max.maxLocal(tmp);
			}

			// Update the bounding box.
			box.setMinMax(min, max);
		}

		return box;
	}

	/**
	 * Gets a {@link BoundingBox} oriented along the default axes that contains
	 * all vertices used to render the pipe.
	 * 
	 * @return Returns a BoundingBox containing the entire pipe mesh.
	 */
	public BoundingBox getBounds() {
		// Get the arrays for the inlets and outlets of the tube.
		Vector3f[] bottomVertices = view.getBottomVertices(true);
		int bottomSize = bottomVertices.length;
		Vector3f[] topVertices = view.getTopVertices(true);
		int topSize = topVertices.length;

		// Combine them into a single array.
		Vector3f[] allEndVertices = new Vector3f[bottomSize + topSize];
		System.arraycopy(bottomVertices, 0, allEndVertices, 0, bottomSize);
		System.arraycopy(topVertices, 0, allEndVertices, bottomSize, topSize);

		// Get the current offsets and rotations.
		List<Vector3f> offsets = new ArrayList<Vector3f>();
		List<Quaternion> rotations = new ArrayList<Quaternion>();
		offsets.add(PlantMath.getVector(model.getPosition()));
		rotations.add(getQuaternionFromOrientation(PlantMath.getVector(model
				.getOrientation())));

		// Get the BoundingBox for the end vertices in world coordinates.
		return getWorldBounds(allEndVertices, offsets, rotations);
	}

	/**
	 * Gets a Quaternion for rotating the view based on the "orientation" of a
	 * {@link Pipe}. In this case, the orientation of a pipe is the vector (from
	 * the origin) along the center of the pipe. Since {@link PipeView}s use
	 * {@link Tube}s, whose default "orientation" is the y-axis, a quaternion
	 * must be computed between the y-axis and the orientation vector.
	 * 
	 * @param orientation
	 *            The "orientation" vector of the {@link Pipe}.
	 * @return A Quaternion representing the local rotation of the orientation
	 *         vector from the y-axis.
	 */
	protected static final Quaternion getQuaternionFromOrientation(
			Vector3f orientation) {

		Quaternion quaternion;

		/*-
		 * // Normally, you would cross the two vectors and set the quaternion's
		 * // x, y, and z to the cross product. Then you would set w to:
		 * // sqrt(length(u)^2 * length(v)^2) + u dot v
		 * float w = FastMath.sqrt(Vector3f.UNIT_Y.lengthSquared()
		 * 		* orientation.lengthSquared())
		 * 		+ Vector3f.UNIT_Y.dot(orientation);
		 * Vector3f cross = Vector3f.UNIT_Y.cross(orientation);
		 * quaternion = new Quaternion(cross.x, cross.y, cross.z, w);
		 */

		// Since one of the vectors involved in the cross product is the unit y
		// vector, we can skip a lot of arithmetic.

		/*-
		 * // A slightly briefer version of the above with all the math but
		 * // still using a vector and a float.
		 * Vector3f cross = new Vector3f(orientation.z, 0f, -orientation.x);
		 * float w = orientation.length() + orientation.y;
		 * quaternion = new Quaternion(cross.x, cross.y, cross.z, w);
		 */

		// We need to make sure that the orientation vector is not just the
		// negated unit y vector. If it's not, then we can perform the usual
		// quaternion construction.
		if (orientation.x != 0f || orientation.z != 0f || orientation.y > 0f) {
			// The shortened quaternion construction in all its glory.
			quaternion = new Quaternion(orientation.z, 0f, -orientation.x,
					orientation.y + orientation.length()).normalizeLocal();
		}
		// If the orientation is the negative unit y vector, create a quaternion
		// for 180 degree rotation around the z axis (or x axis).
		else {
			quaternion = new Quaternion().fromAngles(0f, 0f, FastMath.PI);
		}

		return quaternion;
	}

	/**
	 * Updates the {@link #view} depending on the changes in the {@link model}.
	 */
	public void update(IUpdateable component) {

		// If the argument matches the model, then we may need to update the
		// view accordingly.
		if (component == model && !disposed.get()) {

			// Get final values of the current properties of the pipe.
			float radius = (float) model.getRadius();
			float length = (float) model.getLength();
			int axialSamples = model.getNumElements();
			final Vector3f location = PlantMath.getVector(model.getPosition());
			final Quaternion rotation = getQuaternionFromOrientation(PlantMath
					.getVector(model.getOrientation()));

			// Update the mesh. This method only affects the underlying mesh.
			view.updateMesh(radius, length, axialSamples);

			// Add a new ISyncAction to synchronize these properties with the
			// view.
			renderQueue.enqueue(new Callable<Boolean>() {
				public Boolean call() {
					view.setLocation(location);
					view.setRotation(rotation);
					view.refreshMesh();
					return true;
				}
			});
		}

		return;
	}

	public BoundingBox setSecondaryInletPosition(Vector3f center) {
		return getInletBounds(false);
	}

	public BoundingBox setSecondaryOutletPosition(Vector3f center) {
		return getOutletBounds(false);
	}

}
