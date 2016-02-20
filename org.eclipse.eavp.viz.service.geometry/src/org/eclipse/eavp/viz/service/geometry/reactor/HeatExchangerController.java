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
package org.eclipse.eavp.viz.service.geometry.reactor;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.IWireFramePart;

/**
 * The internal data representation for a Heat Exchanger part.
 * 
 * @author Robert Smith
 *
 */
public class HeatExchangerController extends AbstractController
		implements IWireFramePart {

	/**
	 * The nullary constructor.
	 */
	public HeatExchangerController() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The part's internal representation.
	 * @param view
	 *            The part's graphical representation in the rendering program.
	 */
	public HeatExchangerController(HeatExchangerMesh model, AbstractView view) {
		super(model, view);
	}

	/**
	 * Convenience getter method for the primary pipe.
	 * 
	 * @return The Heat Exchanger's primary pipe, or null if it does not have
	 *         one.
	 */
	public PipeController getPrimaryPipe() {
		return ((HeatExchangerMesh) model).getPrimaryPipe();
	}

	/**
	 * Convenience getter method for the secondary pipe.
	 * 
	 * @return The Heat Exchanger's secondary pipe, or null if it does not have
	 *         one.
	 */
	public PipeController getSecondaryPipe() {
		return ((HeatExchangerMesh) model).getSecondaryPipe();
	}

	/**
	 * Set the heat exchanger's primary pipe, removing any other primary pipe as
	 * necessary.
	 * 
	 * @param pipe
	 *            The Heat Exchanger's new primary pipe.
	 */
	public void setPrimaryPipe(PipeController pipe) {
		((HeatExchangerMesh) model).setPrimaryPipe(pipe);
	}

	/**
	 * Set the heat exchanger's secondary pipe, removing any other secondary
	 * pipe as necessary.
	 * 
	 * @param pipe
	 *            The Heat Exchanger's new secondary pipe.
	 */
	public void setSecondaryPipe(PipeController pipe) {
		((HeatExchangerMesh) model).setSecondaryPipe(pipe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a copy of the model
		HeatExchangerController clone = new HeatExchangerController();
		clone.copy(this);

		// Refresh the view to be in sync with the model
		clone.refresh();

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IWireFramePart#setWireFrameMode(
	 * boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {
		((IWireFramePart) view).setWireFrameMode(on);
	}

}
