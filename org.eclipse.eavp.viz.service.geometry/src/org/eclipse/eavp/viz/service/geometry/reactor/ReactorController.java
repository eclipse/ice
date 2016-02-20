/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
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
 * A reactor part for a Reactor Analyzer. Reactors represent a container around
 * core channels. Reactors are represented by a cut away view of a capsule
 * shaped container which is drawn around all the part's core channel children.
 * 
 * @author Robert Smith
 *
 */
public class ReactorController extends AbstractController
		implements IWireFramePart {

	/**
	 * The nullary constructor.
	 */
	public ReactorController() {
		super();
	}

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The internal representation of the reactor
	 * @param view
	 *            The graphical representation of the reactor
	 */
	public ReactorController(ReactorMesh model, AbstractView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.reactor.javafx.datatypes.WireFramePart#
	 * setWireFrameMode(boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {
		((IWireFramePart) view).setWireFrameMode(on);
	}
}
