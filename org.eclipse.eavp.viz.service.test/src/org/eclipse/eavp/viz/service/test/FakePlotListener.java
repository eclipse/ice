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
package org.eclipse.eavp.viz.service.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.IPlotListener;

public class FakePlotListener implements IPlotListener {

	public IPlot plot = null;
	public String key = null;
	public String value = null;

	private final AtomicBoolean wasNotified = new AtomicBoolean();

	@Override
	public void plotUpdated(IPlot plot, String key, String value) {
		this.plot = plot;
		this.key = key;
		this.value = value;
		wasNotified.set(true);
	}

	public void reset() {
		plot = null;
		key = null;
		value = null;
		wasNotified.set(false);
	}

	public boolean wasNotified(long timeout) {
		long interval = 50;
		long time = 0;
		while (!wasNotified.get() && time < timeout) {
			try {
				Thread.sleep(interval);
				time += interval;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return wasNotified.get();
	}
}
