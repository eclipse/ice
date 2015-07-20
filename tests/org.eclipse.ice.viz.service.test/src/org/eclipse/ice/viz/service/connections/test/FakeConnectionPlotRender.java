/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections.test;

import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a fake {@link ConnectionPlotRender}.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeConnectionPlotRender extends ConnectionPlotRender<FakeClient> {

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that contains the plot render.
	 * @param plot
	 *            The rendered {@code ConnectionPlot}. This cannot be changed.
	 */
	public FakeConnectionPlotRender(Composite parent,
			ConnectionPlot<FakeClient> plot) {
		super(parent, plot);
	}

	/*
	 * Implements an abstract method from ConnectionPlotRender.
	 */
	@Override
	protected String getPreferenceNodeID() {
		return "org.eclipse.ice.viz.service.connections.test";
	}

	/*
	 * Implements an abstract method from ConnectionPlotRender.
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style,
			FakeClient connection) throws Exception {
		return new Composite(parent, SWT.NONE);
	}

	/*
	 * Implements an abstract method from ConnectionPlotRender.
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite,
			FakeClient connection) throws Exception {
		// Nothing to do.
	}

	/*
	 * Implements an abstract method from PlotRender.
	 */
	@Override
	protected void clearCache() {
		// Nothing to do.
	}

}
