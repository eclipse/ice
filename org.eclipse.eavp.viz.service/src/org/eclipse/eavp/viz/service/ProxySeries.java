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
package org.eclipse.eavp.viz.service;

/**
 * This class provides a proxy series that points to a source {@link ISeries}
 * implementation. This is used to share data between series--each with its own
 * customization--without duplicating the series data.
 * <p>
 * As such, if a source has been set via {@link #setSource(ISeries)}, then the
 * following methods will be redirected to the respective method from the source
 * series:
 * </p>
 * <ul>
 * <li>{@link #getBounds()}</li>
 * <li>{@link #getCategory()}</li>
 * <li>{@link #getDataPoints()}</li>
 * <li>{@link #getParentSeries()}</li>
 * <li>{@link #getTime()}</li>
 * <li>{@link #setTime(double)}</li>
 * </ul>
 * All other methods (or <i>all</i> methods if the source is <i>not</i> set)
 * will be diverted to the default implementation provided by
 * {@link AbstractSeries}.
 * 
 * @author Jordan Deyton
 *
 */
public class ProxySeries extends AbstractSeries {

	/**
	 * The series that contains the source data.
	 */
	private ISeries source;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractSeries#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return source != null ? source.getBounds() : super.getBounds();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractSeries#getCategory()
	 */
	@Override
	public String getCategory() {
		return source != null ? source.getCategory() : super.getCategory();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractSeries#getDataPoints()
	 */
	@Override
	public Object[] getDataPoints() {
		return source != null ? source.getDataPoints() : super.getDataPoints();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractSeries#getParentSeries()
	 */
	@Override
	public ISeries getParentSeries() {
		return source != null ? source.getParentSeries()
				: super.getParentSeries();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractSeries#getTime()
	 */
	@Override
	public double getTime() {
		return source != null ? source.getTime() : super.getTime();
	}

	/**
	 * Sets the source {@link ISeries} on which this series is based. This
	 * method allows this series' data to be derived from the specified series.
	 * 
	 * @param source
	 *            The source series. If {@code null}, the source will be unset
	 *            and all source data will revert to the implementation provided
	 *            by {@link AbstractSeries}.
	 */
	public void setSource(ISeries source) {
		if (source != this.source) {
			// Unregister from the old source series.

			// Register with the new source series.
			this.source = source;
			if (source != null) {
				// If the label is unset, use the source series' label.
				if (getLabel() == null) {
					setLabel(source.getLabel());
				}
			}
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractSeries#setTime(double)
	 */
	@Override
	public void setTime(double time) {
		if (source != null) {
			source.setTime(time);
		} else {
			super.setTime(time);
		}
	}

}
