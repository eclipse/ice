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

import java.util.concurrent.Callable;

import org.eclipse.ice.client.widgets.jme.IRenderQueue;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Junction;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * This class provides a controller for heat exchangers and links the
 * {@link HeatExchanger} model with the {@link HeatExchangerView}. Any updates
 * to the view should be coordinated through this class.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class HeatExchangerController extends PipeController {

	/**
	 * The {@link HeatExchanger} model for which this controller provides a
	 * view.
	 */
	private final HeatExchanger model;
	/**
	 * The {@link HeatExchangerView} associated with this controller.
	 */
	private final HeatExchangerView view;

	// ---- Additional Features ---- //
	// TODO Any features that are not a part of the model but are configurable
	// in Peacock should go here.
	// ----------------------------- //

	// ---- Model/View synchronization ---- //
	// ------------------------------------ //

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model (a {@link Junction}) for which this controller
	 *            provides a view.
	 * @param view
	 *            The view (a {@link JunctionView}) associated with this
	 *            controller.
	 * @param renderQueue
	 *            The queue responsible for tasks that need to be performed on
	 *            the jME rendering thread.
	 */
	public HeatExchangerController(HeatExchanger model, HeatExchangerView view,
			IRenderQueue renderQueue) {

		// This controller extends PipeController, so the super constructor
		// needs a Pipe. We need to check for null before we can send the
		// primary Pipe to the super constructor.
		super((model != null ? model.getPrimaryPipe() : null), view,
				renderQueue);

		// Set the model. If it is null, create a new, default model.
		this.model = (model != null ? model : new HeatExchanger());
		this.model.register(this);

		// Set the view. If it is null, create a new, default view.
		this.view = (view != null ? view : new HeatExchangerView(
				"Invalid View", null));

		// ---- Initialize any additional features. ---- //
		// --------------------------------------------- //

		// If any of the arguments were invalid, we should throw an exception
		// now after all class variables have been initialized.
		if (model == null) {
			throw new IllegalArgumentException(
					"HeatExchangerController error: Model is null!");
		} else if (view == null) {
			throw new IllegalArgumentException(
					"HeatExchangerController error: View is null!");
		} else if (renderQueue == null) {
			throw new IllegalArgumentException(
					"HeatExchangerController error: Update queue is null!");
		}

		// The view should not be attached to the scene yet, so we should be
		// able to synchronize it with the current model.

		// ---- Synchronize the initial view with the model. ---- //
		// Some of the synchronization is already done in the pipe MVC.
		// TODO Anything extra that this controller/view adds to the default
		// pipe.
		// ------------------------------------------------------ //

		return;
	}

	@Override
	public BoundingBox setSecondaryInletPosition(Vector3f center) {
		updateSecondaryPipePosition(center, true);
		return super.setSecondaryInletPosition(center);
	}

	@Override
	public BoundingBox setSecondaryOutletPosition(Vector3f center) {
		updateSecondaryPipePosition(center, false);
		return super.setSecondaryOutletPosition(center);
	}

	private void updateSecondaryPipePosition(Vector3f center,
			final boolean input) {
		// Determine the center point with the local translation and rotation
		// applied in reverse. These will be re-applied later.
		Vector3f tmp = center
				.subtract(PlantMath.getVector(model.getPosition()));
		Quaternion q = getQuaternionFromOrientation(PlantMath.getVector(model
				.getOrientation()));
		q.inverseLocal().multLocal(tmp);

		// Update the location and orientation of the secondary pipe mesh before
		// querying the view for the secondary pipe's BoundingBox.
		view.updateSecondaryMesh(input, (float) model.getInnerRadius(), tmp);

		// Sync the view's mesh and geometry.
		renderQueue.enqueue(new Callable<Boolean>() {
			public Boolean call() {
				view.refreshSecondaryMesh(input);
				return true;
			}
		});
	}

	/**
	 * Updates the {@link #view} depending on the changes in the {@link #model}.
	 */
	public void update(IUpdateable component) {
		if (component == model) {
			super.update(model.getPrimaryPipe());

			// If any other model properties need to be synced with the view, do
			// that here.
		}

		return;
	}

	/**
	 * Overrides the default return value because the parent classes return the
	 * primary {@code Pipe} instead of the {@code HeatExchanger} itself.
	 */
	@Override
	public IUpdateable getModel() {
		return model;
	}

}
