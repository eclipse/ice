package org.eclipse.ice.viz.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public abstract class MultiPlot implements IPlot {

	// TODO Implement support for multiple plot Composites.

	/**
	 * The visualization service responsible for this plot.
	 */
	private final IVizService vizService;

	/**
	 * The data source, either a local or remote file.
	 */
	private URI source;

	/**
	 * The composite responsible for rendering the plot data.
	 */
	private PlotComposite plotComposite;

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 * @param file
	 *            The data source, either a local or remote file.
	 */
	public MultiPlot(IVizService vizService, URI file) {
		// Check the parameters.
		if (vizService == null) {
			throw new NullPointerException("IPlot error: "
					+ "Null viz service not allowed.");
		}

		this.vizService = vizService;

		// Set the data source now. This should build any required meta data.
		setDataSource(file);

		return;
	}

	// ---- Implements IPlot ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void draw(String category, String plotType, Composite parent)
			throws Exception {
		// Get the current parent control.
		Composite currentParent = (plotComposite != null ? plotComposite.parent
				: null);

		// Check the parameters.
		if (category == null || plotType == null || parent == null) {
			throw new NullPointerException("IPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED, "IPlot error: "
					+ "Cannot draw plot inside disposed Composite.");
		} else if (currentParent != null && parent != currentParent) {
			throw new IllegalArgumentException("IPlot error: "
					+ "Cannot draw same plot in multiple Composites.");
		}

		// If necessary, create the plotComposite.
		if (plotComposite == null) {
			plotComposite = createPlotComposite(parent);

			// Send the new plot category and type to the plotComposite.
			plotComposite.setPlotCategory(category);
			plotComposite.setPlotType(plotType);

			// If we are not on the UI thread, create its content asynchronously
			// on the UI thread.
			if (Display.getCurrent() == null) {
				parent.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						plotComposite.createPlotContent(SWT.NONE);
					}
				});
			}
			// If we are on the UI thread, create the content synchronously.
			else {
				plotComposite.createPlotContent(SWT.NONE);
			}
		} else {
			// Send the new plot category and type to the plotComposite.
			plotComposite.setPlotCategory(category);
			plotComposite.setPlotType(plotType);
		}

		// Trigger the appropriate update to the plotComposite's content.
		updatePlotComposite(plotComposite);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#setProperties(java.util
	 * .Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return !"localhost".equals(getSourceHost());
	}

	// -------------------------- //

	/**
	 * Sets the data source (which is currently rendered if the plot is drawn).
	 * If the data source is valid and new, then the plot will be updated
	 * accordingly.
	 * 
	 * @param uri
	 *            The new data source URI.
	 */
	protected void setDataSource(URI file) {
		if (file != null) {
			source = file;
		}
	}

	/**
	 * Gets the visualization service responsible for this plot.
	 * 
	 * @return The visualization service responsible for this plot.
	 */
	protected IVizService getVizService() {
		return vizService;
	}

	// ---- UI Widgets ---- //
	protected PlotComposite createPlotComposite(Composite parent) {
		return new PlotComposite(parent, this);
	};

	protected void updatePlotComposite(PlotComposite composite) {
		plotComposite.refresh();
	}
	// -------------------- //

}
