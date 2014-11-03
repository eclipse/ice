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
package org.eclipse.ice.viz.plotviewer;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class PlotTreeLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub

		// Check if element is instanceof class PlotProvider, Double,
		// SeriesProvider, or String
		if (element instanceof PlotProvider) {
			PlotProvider plot = (PlotProvider) element;
			// return element
			return plot.getPlotTitle();
		} else if (element instanceof PlotTimeIdentifierMapping) {
			PlotTimeIdentifierMapping plotTime = (PlotTimeIdentifierMapping) element;
			return plotTime.getTime() + "";
		} else if (element instanceof SeriesProvider) {
			SeriesProvider series = (SeriesProvider) element;
			// return element
			return series.getSeriesTitle();
		} else if (element instanceof String) {
			String feature = (String) element;
			// return element
			return feature;
		}

		return null;
	}

}
