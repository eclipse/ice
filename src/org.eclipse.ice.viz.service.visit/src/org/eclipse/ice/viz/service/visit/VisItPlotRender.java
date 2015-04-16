package org.eclipse.ice.viz.service.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtWidget;

import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import visit.java.client.ViewerMethods;

public class VisItPlotRender extends ConnectionPlotRender<VisItSwtConnection> {

	// TODO This could be moved to the parent class, as the connection adapter
	// may prove useful.
	private final IConnectionAdapter<VisItSwtConnection> adapter;

	/**
	 * The current plot category rendered in the associated rendering widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotCategory;
	/**
	 * The current plot type rendered in the associated rendering widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotType;

	/**
	 * The ID of the associated VisIt window. Each window corresponds to a
	 * unique "view" for a VisIt connection.
	 */
	private final int windowId;

	/**
	 * The plot {@code Composite} that renders the files through the VisIt
	 * connection.
	 */
	private VisItSwtWidget canvas;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent Composite that contains the plot render.
	 * @param plot
	 *            The rendered ConnectionPlot. This cannot be changed.
	 */
	public VisItPlotRender(Composite parent, VisItPlot plot) {
		// TODO If the window ID is -1, VisIt won't automatically create a new
		// one...
		this(parent, plot, 1);
	}

	/**
	 * The full constructor.
	 * 
	 * @param parent
	 *            The parent Composite that contains the plot render.
	 * @param plot
	 *            The rendered ConnectionPlot. This cannot be changed.
	 * @param windowId
	 *            The ID of the associated VisIt window. Each window corresponds
	 *            to a unique "view" for a VisIt connection.
	 */
	public VisItPlotRender(Composite parent, VisItPlot plot, int windowId) {
		super(parent, plot);

		// Set the reference to the VisIt window ID.
		this.windowId = windowId;

		// Store the adapter so that we can access its connection later.
		adapter = plot.getVisItConnectionAdapter();

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * getPreferenceNodeID()
	 */
	@Override
	protected String getPreferenceNodeID() {
		return "org.eclipse.ice.viz.service.paraview.preferences";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * createPlotComposite(org.eclipse.swt.widgets.Composite, int,
	 * java.lang.Object)
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style,
			VisItSwtConnection connection) throws Exception {

		// Create a new window on the VisIt server if one does not already
		// exist. We will need the corresponding connection and a window ID. If
		// the window ID is -1, a new one is created.

		// Create the canvas.
		canvas = new VisItSwtWidget(parent, style | SWT.DOUBLE_BUFFERED);
		int windowWidth = Integer.parseInt(adapter
				.getConnectionProperty("windowWidth"));
		int windowHeight = Integer.parseInt(adapter
				.getConnectionProperty("windowHeight"));

		// Establish the canvas' connection to the VisIt server. This may throw
		// an exception.
		canvas.setVisItSwtConnection(connection, windowId, windowWidth,
				windowHeight);

		// Create a mouse manager to handle mouse events inside the
		// canvas.
		new VisItMouseManager(canvas);

		// Set the canvas' context Menu to the parent's.
		canvas.setMenu(parent.getMenu());

		return canvas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * updatePlotComposite(org.eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite,
			VisItSwtConnection connection) throws Exception {

		// Check the input arguments. The canvas should be the plot Composite.
		if (plotComposite != canvas) {
			throw new Exception("VisItPlot error: "
					+ "The canvas was not created properly.");
		}

		// Get the source path from the VisItPlot class. We can't,
		// unfortunately, use the URI as specified.
		String sourcePath = VisItPlot.getSourcePath(plot.getDataSource());

		// Make sure the Canvas is activated.
		canvas.activate();

		// See if the plot category and type have been updated since the last
		// refresh. We should also make sure the current plot category and type
		// are valid before we try to update them.
		final String category = getPlotCategory();
		final String type = getPlotType();
		boolean plotTypeChanged = ((category != null && !category
				.equals(plotCategory)) || (type != null && !type
				.equals(plotType)));
		if (plotTypeChanged && type != null) {
			plotTypeChanged = false;
			String[] types = plot.getPlotTypes().get(category);
			if (types != null) {
				for (int i = 0; !plotTypeChanged && i < types.length; i++) {
					if (type.equals(types[i])) {
						plotTypeChanged = true;
					}
				}
			}
		}

		// If the plot category or type changed (and they are both valid),
		// update the reference to the currently drawn category and type and
		// update the widget.
		if (plotTypeChanged) {

			// TODO Remove this output...
			System.out.println("VisItPlot message: " + "Drawing plot "
					+ category + " - " + type + " for source file \""
					+ sourcePath + "\".");

			// Draw the specified plot on the Canvas.
			ViewerMethods widget = canvas.getViewerMethods();

			// Remove all existing plots.
			widget.deleteActivePlots();

			// FIXME How do we handle invalid paths?
			widget.openDatabase(sourcePath);
			widget.addPlot(category, type);
			widget.drawPlots();

			// Change the record of the current plot category and type for this
			// PlotRender.
			if (category != null) {
				plotCategory = category;
			}
			if (type != null) {
				plotType = type;
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.PlotRender#clearCache()
	 */
	@Override
	protected void clearCache() {
		// Nothing to do yet.
	}
}
