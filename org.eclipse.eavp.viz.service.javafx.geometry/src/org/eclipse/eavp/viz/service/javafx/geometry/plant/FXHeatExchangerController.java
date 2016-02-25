/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerController;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;

/**
 * A HeatExchangerController specifically tailored to the functionality of
 * JavaFX.
 * 
 * @author Robert Smith
 *
 */
public class FXHeatExchangerController extends HeatExchangerController {

	/**
	 * The nullary constructor.
	 */
	public FXHeatExchangerController() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The internal representation for the heat exchanger part.
	 * @param view
	 *            The view containing the graphical representation of this part.
	 */
	public FXHeatExchangerController(HeatExchangerMesh model,
			AbstractView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#update(org.eclipse.eavp.viz.service.
	 * datastructures.VizObject.IVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {

		// Queue any messages from the view refresh
		updateManager.enqueue();

		// Refresh the view, ignoring wireframe events from anything except this
		// object's view
		if (type[0] != SubscriptionType.WIREFRAME || component == view) {
			view.refresh(model);
		}

		// Notify own listeners of the change.
		updateManager.notifyListeners(type);
		updateManager.flushQueue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerController#
	 * clone()
	 */
	@Override
	public Object clone() {

		// Create a copy of the model
		FXHeatExchangerController clone = new FXHeatExchangerController();
		clone.copy(this);

		// Refresh the view to be in sync with the model
		clone.refresh();

		return clone;
	}

}
