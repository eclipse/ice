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

/**
 * This class provides a basic plot capable of drawing in multiple parent
 * {@code Composite}s simply via the methods provided by the {@link IPlot}
 * interface.
 * <p>
 * For client code that will be drawing these plots, do the following:
 * <ol>
 * <li>Call {@link #draw(String, String, Composite)} with a {@code Composite}
 * and any category and type. This renders (if possible) a plot inside the
 * specified {@code Composite} based on the specified plot category and type.</li>
 * <li>Call {@link #draw(String, String, Composite)} with the same
 * {@code Composite} but different category and type. <i>The plot rendered by
 * the previous call will have its plot category and type changed.</i></li>
 * <li>Call {@link #draw(String, String, Composite)} with a {@code Composite}
 * and any category and type. This renders (if possible) a plot inside the
 * {@code Composite} based on the specified plot category and type. <i>You now
 * have two separate renderings based on the same {@code IPlot}.</i></li>
 * </ol>
 * </p>
 * <p>
 * Sub-classes should override the following methods so that the correct
 * {@link PlotRender} is created and updated properly:
 * <ol>
 * <li>{@link #createPlotRender(Composite)}</li>
 * <li>{@link #updatePlotRender(PlotRender)}</li>
 * </ol>
 * </p>
 * 
 * @author Jordan
 *
 */
public abstract class MultiPlot implements IPlot {

	/**
	 * The visualization service responsible for this plot.
	 */
	private final IVizService vizService;

	/**
	 * The data source, either a local or remote file.
	 */
	private URI source;

	/**
	 * The map of current {@link PlotRender}s, keyed on their parent
	 * {@code Composite}s.
	 */
	private final Map<Composite, PlotRender> plotRenders;

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

		// Initialize the map of PlotRenders.
		plotRenders = new HashMap<Composite, PlotRender>();

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

		// Check the parameters.
		if (category == null || plotType == null || parent == null) {
			throw new NullPointerException("IPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED, "IPlot error: "
					+ "Cannot draw plot inside disposed Composite.");
		}

		final PlotRender plotRender;

		// If necessary, create the PlotRender.
		if (!plotRenders.containsKey(parent)) {
			// Create it and save it in the map.
			plotRender = createPlotRender(parent);
			plotRenders.put(parent, plotRender);

			// Send the new plot category and type to the PlotRender.
			plotRender.setPlotCategory(category);
			plotRender.setPlotType(plotType);

			// If we are not on the UI thread, create its content asynchronously
			// on the UI thread.
			if (Display.getCurrent() == null) {
				parent.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						plotRender.createPlotContent(SWT.NONE);
					}
				});
			}
			// If we are on the UI thread, create the content synchronously.
			else {
				plotRender.createPlotContent(SWT.NONE);
			}
		} else {
			// Get the existing PlotCopmosite.
			plotRender = plotRenders.get(parent);

			// Send the new plot category and type to the PlotRender.
			plotRender.setPlotCategory(category);
			plotRender.setPlotType(plotType);
		}

		// Trigger the appropriate update to the PlotRender's content.
		updatePlotRender(plotRender);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getNumberOfAxes()
	 */
	public int getNumberOfAxes() {
		return 0;
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
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	public String getSourceHost() {
		return source.getHost();
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
	/**
	 * Creates a {@link PlotRender} inside the specified parent
	 * {@code Composite}. The {@code PlotRender}'s content should not be created
	 * yet.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that will contain the new
	 *            {@code PlotRender}.
	 * @return The new {@code PlotRender}.
	 */
	protected abstract PlotRender createPlotRender(Composite parent);

	/**
	 * Updates the specified {@link PlotRender}. The default behavior of this
	 * method is to call {@link PlotRender#refresh()}.
	 * 
	 * @param plotRender
	 *            The {@code PlotRender} to update.
	 */
	protected void updatePlotRender(PlotRender plotRender) {
		plotRender.refresh();
	}
	// -------------------- //

}
