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
package org.eclipse.eavp.viz.service.csv;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.IPlotListener;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.ProxyPlot;
import org.eclipse.eavp.viz.service.ProxySeries;
import org.eclipse.eavp.viz.service.styles.XYZSeriesStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * This class serves as a proxy for a normal {@link CSVPlot}, which is itself
 * responsible for loading and storing the CSV data. Instances of this class are
 * provided from the {@link CSVVizService} when a plot is created, and it gives
 * full access to customizing a plot without affecting the data source plot.
 * 
 * @author Jordan Deyton
 *
 */
public class CSVProxyPlot extends ProxyPlot implements IPlotListener {

	/**
	 * The currently drawn plot composite.
	 */
	private CSVPlotComposite plotComposite = null;

	/**
	 * Whether or not the current data from the associated {@link CSVPlot} is
	 * loaded.
	 */
	private boolean loaded = false;

	/**
	 * A proxy of the series serving as this Plot's independent series.
	 */
	private ProxySeries independentProxy = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.ProxyPlot#createProxySeries(org.eclipse.ice
	 * .viz.service.ISeries)
	 */
	@Override
	protected ProxySeries createProxySeries(ISeries source) {
		ProxySeries series = super.createProxySeries(source);
		XYZSeriesStyle style = (XYZSeriesStyle) source.getStyle();
		series.setStyle((XYZSeriesStyle) style.clone());
		return series;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.AbstractPlot#draw(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {

		// If necessary, create a new plot composite.
		if (plotComposite == null) {
			// Check the parent Composite.
			if (parent == null) {
				throw new SWTException(SWT.ERROR_NULL_ARGUMENT, "IPlot error: "
						+ "Cannot draw plot in a null Composite.");
			} else if (parent.isDisposed()) {
				throw new SWTException(SWT.ERROR_WIDGET_DISPOSED,
						"IPlot error: "
								+ "Cannot draw plot in a disposed Composite.");
			}
			// Create a plot composite.
			plotComposite = new CSVPlotComposite(parent, SWT.BORDER);
			plotComposite.setPlot(this);
			
			// Make sure the plot data has been loaded
			while (!((CSVPlot)getSource()).isLoaded()) {
				Thread.sleep(200);
			}
			
			// Tell it to update based on the new plot.
			plotComposite.refresh();

			// When the composite is disposed, unset the reference to it so a
			// new composite can be drawn later.
			plotComposite.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					plotComposite = null;
				}
			});
		}
		// Otherwise, ignore the parent argument and refresh.
		else {
			plotComposite.refresh();
		}

		return plotComposite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.ProxyPlot#getCategories()
	 */
	@Override
	public List<String> getCategories() {
		// Try to reload the series data if necessary.
		if (!loaded) {
			reloadSeries();
		}
		return super.getCategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.ProxyPlot#getDependentSeries(java.lang.String
	 * )
	 */
	@Override
	public List<ISeries> getDependentSeries(String category) {
		// Try to reload the series data if necessary.
		if (!loaded) {
			reloadSeries();
		}
		return super.getDependentSeries(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getIndependentSeries()
	 */
	@Override
	public ISeries getIndependentSeries() {
		// Try to reload the series data if necessary.
		if (!loaded) {
			reloadSeries();
		}
		
		// Wrap the source IPlot's independent series and return it. 
		return createProxySeries(getSource().getIndependentSeries());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.IPlotListener#plotUpdated(org.eclipse.ice
	 * .viz.service.IPlot, java.lang.String, java.lang.String)
	 */
	@Override
	public void plotUpdated(IPlot plot, String key, String value) {
		// The only event sent out by the CSVPlot signifies that loading has
		// completed. Forces the data to be reloaded the next time it is fetched
		// from client code (which should happen in the refresh).
		loaded = false;
		if (plotComposite != null && !plotComposite.isDisposed()) {
			plotComposite.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#redraw()
	 */
	@Override
	public void redraw() {
		// Use the source CSVPlot's redraw operation, which reloads the file.
		IPlot source = getSource();
		if (source != null) {
			loaded = false;
			source.redraw();
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.ProxyPlot#reloadSeries()
	 */
	@Override
	protected void reloadSeries() {

		IPlot source = getSource();
		if (source != null && ((CSVPlot) source).isLoaded()) {
			// We can now load the proxy series.
			loaded = true;

			// Load the dependent series (and categories) in the default way.
			super.reloadSeries();

			// Enable the first dependent series.
			for (String category : getCategories()) {
				List<ISeries> series = getDependentSeries(category);
				if (series != null && !series.isEmpty()) {
					series.iterator().next().setEnabled(true);
					break;
				}
			}

			// Reset the independent series.
			ISeries sourceSeries = source.getIndependentSeries();
			ProxySeries proxy = createProxySeries(sourceSeries);
			// Enable the series by default.
			proxy.setEnabled(true);
			super.setIndependentSeries(proxy);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.AbstractPlot#setIndependentSeries(org.eclipse
	 * .ice.viz.service.ISeries)
	 */
	@Override
	public void setIndependentSeries(ISeries series) {
		
		//Set own independent series as well as the source plot's
		super.setIndependentSeries(series);
		getSource().setIndependentSeries(series);
		
		//Create a proxy for the independent series and reload
		independentProxy = createProxySeries(series);
		loaded = false;
		getSource().redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.ProxyPlot#setSource(org.eclipse.eavp.viz.service
	 * .IPlot)
	 */
	@Override
	public void setSource(IPlot source) {
		super.setSource(source);

		// Register as a listener to be notified when the data has been
		// reloaded.
		((CSVPlot) source).addPlotListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#createAdditionalPage(org.eclipse.ui.part.MultiPageEditorPart, org.eclipse.ui.IFileEditorInput, int)
	 */
	@Override
	public String createAdditionalPage(MultiPageEditorPart parent, IFileEditorInput file, int pageNum) {
		return source.createAdditionalPage(parent, file, pageNum);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#getNumAdditionalPages()
	 */
	@Override
	public int getNumAdditionalPages() {
		return source.getNumAdditionalPages();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#save()
	 */
	@Override
	public void save(IProgressMonitor monitor) {
		source.save(monitor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#saveAs()
	 */
	@Override
	public void saveAs() {
		source.saveAs();
	}

}
