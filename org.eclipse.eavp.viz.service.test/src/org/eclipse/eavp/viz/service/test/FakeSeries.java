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

import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.ISeriesStyle;

/**
 * A fake {@link ISeries} implementation for convenience in the plot/series
 * infrastructure tests.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeSeries implements ISeries {

	private final String category;

	private String label = null;

	private boolean enabled = false;

	public FakeSeries(String category) {
		this.category = category;
	}

	@Override
	public double[] getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getDataPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTime(double time) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ISeries getParentSeries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public ISeriesStyle getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStyle(ISeriesStyle style) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enable) {
		enabled = enable;
	}

	@Override
	public String getCategory() {
		return category;
	}

}
