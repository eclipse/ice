/*******************************************************************************
 * Copyright (c) 2014, 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.eavp.viz.service.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.styles.BasicErrorStyle;
import org.eclipse.eavp.viz.service.styles.XYZAxisStyle;
import org.eclipse.eavp.viz.service.styles.XYZPlotStyle;
import org.eclipse.eavp.viz.service.styles.XYZSeriesStyle;
import org.eclipse.nebula.visualization.widgets.datadefinition.ColorMap;
import org.eclipse.nebula.visualization.widgets.datadefinition.ColorMap.PredefinedColorMap;
import org.eclipse.nebula.visualization.widgets.figures.IntensityGraphFigure;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.dataprovider.Sample;
import org.eclipse.nebula.visualization.xygraph.figures.Axis;
import org.eclipse.nebula.visualization.xygraph.figures.ToolbarArmedXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.ErrorBarType;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is an editor for displaying an SWT XYGraph used to draw plots from CSV
 * files. It is opened by the associated visualization views in
 * org.eclipse.eavp.viz.
 * 
 * @author Matthew Wang
 * @author Taylor Patterson
 * @author Anna Wojtowicz
 * @author Jordan Deyton
 * @author Alex McCaskey
 * @author Kasper Gammeltoft - Viz refactor, enabled the editor to work properly
 *         with ISeries rather than SeriesProvider
 */
public class CSVPlotEditor extends EditorPart {

	/**
	 * The ID for this editor.
	 */
	public static final String ID = "org.eclipse.eavp.viz.service.csv.CSVPlotEditor";

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CSVPlotEditor.class);

	/**
	 * The top level composite that holds the editor's contents.
	 */
	private Composite vizComposite;

	/**
	 * The {@code Composite} that contains the time slider.
	 */
	private Composite sliderComposite;

	/**
	 * The canvas used for rendering the plot.
	 */
	private Canvas plotCanvas;

	/**
	 * LightweightSystem for an SWT XYGraph
	 */
	private LightweightSystem lws;

	/**
	 * The xy graph used to display the csv input
	 */
	private XYGraph xyGraph;

	/**
	 * The widget that holds the toolbar for the xy graph
	 */
	private ToolbarArmedXYGraph graphToolbar;

	/**
	 * Error map, keeps track of which series should be used as error data for
	 * other series.
	 */
	private Map<ISeries, List<ISeries>> seriesMap;

	/**
	 * A map of existing traces that are tracing certain series. This is useful
	 * for not adding identical traces and being able to remove traces later
	 */
	private Map<ISeries, Trace> existingTraces;

	/**
	 * The independent series, to use as the x axis for plotting. Should be set
	 * to non null {@link ISeries}
	 */
	private ISeries independentSeries;

	/**
	 * The constructor
	 */
	public CSVPlotEditor() {
		seriesMap = new HashMap<ISeries, List<ISeries>>();
		existingTraces = new HashMap<ISeries, Trace>();
		xyGraph = null;
		graphToolbar = null;
		return;
	}

	/**
	 * Configures the specified axis for the style given.
	 * 
	 * @param style
	 *            The XYZAxisStyle to use when configuring the axis properties
	 * @param axis
	 *            The Axis to configure
	 */
	private void configureAxis(Axis axis, XYZAxisStyle style) {
		// Set the default behavior if there is no axis style
		if (style == null) {
			axis.setShowMajorGrid(true);
			axis.setAutoScale(true);

			// Otherwise, set the config options from the axis style
		} else {
			// Set the title
			axis.setTitle((String) style.getProperty(XYZAxisStyle.AXIS_TITLE));
			// Set the axis title font, if specified
			if (style.getProperty(XYZAxisStyle.TITLE_FONT) != null) {
				axis.setTitleFont(
						(Font) style.getProperty(XYZAxisStyle.TITLE_FONT));
			}
			// Set the axis font, if specified
			if (style.getProperty(XYZAxisStyle.SCALE_FONT) != null) {
				axis.setFont((Font) style.getProperty(XYZAxisStyle.SCALE_FONT));
			}
			// Set the axis color, if specified
			if (style.getProperty(XYZAxisStyle.AXIS_COLOR) != null) {
				axis.setForegroundColor(
						(Color) style.getProperty(XYZAxisStyle.AXIS_COLOR));
			}
			// Set the log scale, true for use log, false for use linear scale
			axis.setLogScale((boolean) style.getProperty(XYZAxisStyle.IS_LOG));
			// Set the auto scale feature, true for does auto scale, false for
			// does not
			axis.setAutoScale(
					(boolean) style.getProperty(XYZAxisStyle.AUTO_SCALE));
			// Set the major grid lines to appear, true for show the grid lines,
			// false for do not
			axis.setShowMajorGrid(
					(boolean) style.getProperty(XYZAxisStyle.SHOW_GRID_LINE));
			// Set the grid line to either be dashed for true and solid for
			// false
			axis.setDashGridLine((boolean) style
					.getProperty(XYZAxisStyle.GRID_LINE_IS_DASHED));
			// Sets the grid line color if it is available
			if (style.getProperty(XYZAxisStyle.GRID_LINE_COLOR) != null) {
				axis.setMajorGridColor((Color) style
						.getProperty(XYZAxisStyle.GRID_LINE_COLOR));
			}

		}

	}

	/**
	 * This operation returns the trace that is created with the given series.
	 * Returns null if this series is not enabled or is an error series.
	 * 
	 * @param series
	 *            The series to use when creating this trace.
	 * @param xValues
	 *            The x values, or independent series values for this trace.
	 *            This variable is added so as to not calculate the array every
	 *            time a trace is added (in cases where there might be many
	 *            dependent series to add)
	 * @param xPlusError
	 *            The x plus error, this is an input so as to not have to
	 *            recalculate the information for every trace, if adding
	 *            multiple at a time
	 * @param xMinusError
	 *            The x minus error, this is an input so as to not have to
	 *            recalculate the information for every trace, if adding
	 *            multiple at a time
	 * @return
	 */
	private Trace configureTrace(ISeries series, double[] xValues,
			double[] xPlusError, double[] xMinusError) {

		// Local declarations
		Trace trace = null;

		// Make sure it is a valid series for creating a trace
		if (series != null && series.isEnabled()
				&& !(series.getStyle() instanceof BasicErrorStyle)
				&& !existingTraces.containsKey(series)) {

			// Get the data and create the new error arrays
			double[] yValues = getDoubleValue(series);
			int seriesSize = yValues.length;
			double[] yPlusError = new double[seriesSize];
			double[] yMinusError = new double[seriesSize];

			// Create the data provider
			final CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(
					false);
			// Sets the size of the buffer
			traceDataProvider.setBufferSize(seriesSize);

			// Gets the error for the series, if there is any
			if (seriesMap.containsKey(series)) {
				List<ISeries> errors = seriesMap.get(series);
				if (errors != null && errors.size() > 0) {
					for (ISeries errSeries : errors) {
						// Get the error style, and add the error to the
						// appropriate series
						BasicErrorStyle errStyle = (BasicErrorStyle) errSeries
								.getStyle();
						// If the style is positive error (the bars are above
						// the point)
						if (errStyle.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
								.equals(ErrorBarType.PLUS)) {
							sumArrays(yPlusError, getDoubleValue(errSeries));
							// If the style is negative error (the bars are
							// below the point)
						} else if (errStyle
								.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
								.equals(ErrorBarType.MINUS)) {
							sumArrays(yMinusError, getDoubleValue(errSeries));
							// If the style is both (above and below the point)
						} else if (errStyle
								.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
								.equals(ErrorBarType.BOTH)) {
							sumArrays(yPlusError, getDoubleValue(errSeries));
							sumArrays(yMinusError, getDoubleValue(errSeries));
						}
					}
				}

			}

			// Set the data to be plotted
			for (int i = 0; i < seriesSize; i++) {
				// Create the new point add add it to the trace
				Sample point = new Sample(xValues[i], yValues[i], yPlusError[i],
						yMinusError[i], xPlusError[i], xMinusError[i],
						Double.toString(xValues[i]) + ", "
								+ Double.toString(yValues[i]));

				// Add the new point to the trace
				traceDataProvider.addSample(point);
			}

			// Creates a new trace with the name, axis,and provider to plot
			trace = new Trace(series.getLabel(), xyGraph.primaryXAxis,
					xyGraph.primaryYAxis, traceDataProvider);

			// Get the series style to configure the trace
			XYZSeriesStyle style = (XYZSeriesStyle) series.getStyle();

			// Sets trace properties
			trace.setAntiAliasing(
					(boolean) style.getProperty(XYZSeriesStyle.ANTI_ALIASING));
			trace.setAreaAlpha(
					(int) style.getProperty(XYZSeriesStyle.AREA_ALPHA));

			// Sets the trace color if it is set in the style
			if (style.getProperty(XYZSeriesStyle.COLOR) != null) {
				trace.setTraceColor(
						(Color) style.getProperty(XYZSeriesStyle.COLOR));
			}

			// Sets the trace type if it has been set
			if (style.getProperty(XYZSeriesStyle.TYPE) != null) {
				trace.setTraceType(
						(TraceType) style.getProperty(XYZSeriesStyle.TYPE));
			}

			// Sets the point style for this trace
			if (style.getProperty(XYZSeriesStyle.POINT) != null) {
				trace.setPointStyle(
						(PointStyle) style.getProperty(XYZSeriesStyle.POINT));
			}

			// Sets the line's width for this trace.
			trace.setLineWidth(
					(int) style.getProperty(XYZSeriesStyle.LINE_WIDTH));

			// Set the point size (radius) for the points on the trace
			trace.setPointSize(
					(int) style.getProperty(XYZSeriesStyle.POINT_SIZE));

			// Set the error to show up if this series has error
			trace.setErrorBarEnabled(
					(boolean) style.getProperty(XYZSeriesStyle.ERROR_ENABLED));

			// Sets the error type for the error bars when drawing
			trace.setYErrorBarType((ErrorBarType) style
					.getProperty(XYZSeriesStyle.ERROR_TYPE));

		}
		// Finally, return the trace
		return trace;

	}

	/**
	 * This operation sets up the Composite that contains the VisIt canvas and
	 * create the VisIt widget.
	 * 
	 * @param parent
	 *            The parent Composite to create the Control in.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create a top level composite to hold the canvas or text
		vizComposite = new Composite(parent, SWT.NONE);
		vizComposite.setLayout(new GridLayout(1, true));

		// Set up the canvas where the graph is displayed
		plotCanvas = new Canvas(vizComposite, SWT.BORDER);
		plotCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// MAGIC
		lws = new LightweightSystem(plotCanvas);

		return;
	}

	/**
	 * This method implements the Composite to hold the slider for moving
	 * through plotted file sets.
	 * 
	 * @param sliderComp
	 *            The Composite to create the widgets in
	 * @param displayPlotProvider
	 *            The PlotProvider containing the information to create the plot
	 */
	private Composite createSliderComp(Composite parent,
			final PlotProvider displayPlotProvider) {

		final Composite sliderComp = new Composite(parent, SWT.NONE);

		// Set the layout and layout data for this Composite
		sliderComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		sliderComp.setLayout(new GridLayout(5, false));

		// The label for the slider
		Label timeButtons = new Label(sliderComp, SWT.FILL);
		timeButtons.setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, false, false));
		timeButtons.setText("Slider: ");

		// The slider itself
		final Slider slider = new Slider(sliderComp, SWT.HORIZONTAL);
		slider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Set the selection, minimum, maximum, thumb, increment, and page
		// increment
		final int spinnerSliderMin = 0;
		final int spinnerSliderMax = displayPlotProvider.getTimes().size() - 1;
		slider.setValues(0, spinnerSliderMin, spinnerSliderMax, 1, 1, 1);

		// Create the label for the selected time
		final Label timeSelectedLabel = new Label(sliderComp, SWT.NONE);
		timeSelectedLabel.setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, false, false));
		// Initially set it to the selected index
		timeSelectedLabel
				.setText(displayPlotProvider.getTimes().get(0).toString());

		// The buttons for moving up and moving down
		Button downSpinnerButton = new Button(sliderComp, SWT.PUSH);
		Button upSpinnerButton = new Button(sliderComp, SWT.PUSH);
		upSpinnerButton
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		upSpinnerButton.setText(">");
		downSpinnerButton
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		downSpinnerButton.setText("<");

		final Runnable setPlotTime = new Runnable() {
			@Override
			public void run() {
				// Get the slider's selection which is an index to the times
				int sliderValue = slider.getSelection();

				double time = displayPlotProvider.getTimes().get(sliderValue);
				showXYGraph(displayPlotProvider, time, false);
				timeSelectedLabel.setText(Double.toString(time));

				// Refresh the slider composite
				vizComposite.layout();
				sliderComp.layout();

				return;
			}
		};

		// The listener for the slider.
		slider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Apply the new time to the plot.
				setPlotTime.run();
			}
		});

		// The listener for the up button in the slider.
		upSpinnerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Get the slider's selection which is an index to the times
				int sliderValue = slider.getSelection();

				// Check that the index is not out of bounds
				if (sliderValue < spinnerSliderMax) {
					// increment the index from the up button click
					sliderValue++;
					// Move the slider to the index of the selection
					slider.setSelection(sliderValue);

					// Apply the new time to the plot.
					setPlotTime.run();
				}

				return;
			}
		});
		// The listener for the down button in the slider.
		downSpinnerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Get the slider's selection which is an index to the times
				int sliderValue = slider.getSelection();

				// Check that the index is within bounds
				if (sliderValue > spinnerSliderMin) {
					// Decrement the index
					sliderValue--;
					// Move the slider to the index of the selection
					slider.setSelection(sliderValue);

					// Apply the new time to the plot.
					setPlotTime.run();
				}

				return;
			}
		});

		return sliderComp;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		return;
	}

	/**
	 * Gets the main {@code Composite} in which the plot editor is drawn. This
	 * is an ancestor of the {@link #plotCanvas}.
	 * 
	 * @return The main {@code Composite}.
	 */
	protected Composite getComposite() {
		return vizComposite;
	}

	/**
	 * Gets the double values of the specified series for all of its data
	 * points. If the series data points cannot be cast to Double, then throws
	 * an exception.
	 * 
	 * @param series
	 *            The series to retrieve the data from
	 * @return Returns an array of double values to use
	 */
	private double[] getDoubleValue(ISeries series) {
		Object[] dataPoints = series.getDataPoints();
		double[] array = new double[dataPoints.length];
		for (int i = 0; i < dataPoints.length; i++) {
			array[i] = (Double) dataPoints[i];
		}
		return array;

		// // Try to get valid data from the series, and convert to double
		// values
		// ISeries csvData = (ISeries) series;
		// double[] newArray = new double[csvData.size()];
		// for (int i = 0; i < csvData.size(); i++) {
		// newArray[i] = (Double) csvData.get(i);
		// }
		// return newArray;
	}

	/**
	 * Gets the canvas used to render the CSV plot.
	 * 
	 * @return The plot canvas.
	 */
	public Canvas getPlotCanvas() {
		return plotCanvas;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// Set the site, input and name
		setSite(site);
		setInput(input);
		setPartName("CSV Plot Editor");

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * This operation completely recreates the trace for the specified series
	 * and replaces the current trace with this one.
	 * 
	 * @param series
	 *            The series to recreate the trace for
	 */
	private void redrawSeries(ISeries series) {
		// Get the x values and error for plotting
		double[] xValues = getDoubleValue(independentSeries);
		double[] xPlusError = new double[xValues.length];
		double[] xMinusError = new double[xValues.length];
		// Gets the error for the series, if there is any
		if (seriesMap.containsKey(independentSeries)) {
			List<ISeries> errors = seriesMap.get(independentSeries);
			if (errors != null && errors.size() > 0) {
				for (ISeries errSeries : errors) {
					// Get the error style, and add the error to the
					// appropriate series
					BasicErrorStyle errStyle = (BasicErrorStyle) errSeries
							.getStyle();
					// If the style is positive error (the bars are above
					// the point)
					if (errStyle.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
							.equals(ErrorBarType.PLUS)) {
						sumArrays(xPlusError, getDoubleValue(errSeries));
						// If the style is negative error (the bars are
						// below the point)
					} else
						if (errStyle.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
								.equals(ErrorBarType.MINUS)) {
						sumArrays(xMinusError, getDoubleValue(errSeries));
						// If the style is both (above and below the point)
					} else if (errStyle
							.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
							.equals(ErrorBarType.BOTH)) {
						sumArrays(xPlusError, getDoubleValue(errSeries));
						sumArrays(xMinusError, getDoubleValue(errSeries));
					}
				}
			}

		}

		// Create the new trace
		Trace trace = configureTrace(series, xValues, xPlusError, xMinusError);

		// If the trace is not null, then add the new trace to the graph and
		// take the old trace out
		if (trace != null) {
			XYZSeriesStyle indepStyle = (XYZSeriesStyle) independentSeries
					.getStyle();

			// Configure the x error for the series
			trace.setXErrorBarType((ErrorBarType) indepStyle
					.getProperty(XYZSeriesStyle.ERROR_TYPE));
		}
		// Replace the other trace in the existing traces map
		Trace toRemove = this.existingTraces.remove(series);
		trace.setTraceColor(toRemove.getTraceColor());
		xyGraph.remove(toRemove);
		xyGraph.addTrace(trace);

		// Re-add to the existing traces map
		existingTraces.put(series, trace);

	}

	/**
	 * This operation refreshes the VisIt view.
	 */
	public void refresh() {
		setFocus();
	}

	/**
	 * Removes the selected series from this editor. Should remove if the series
	 * is
	 * 
	 * @param series
	 */
	public void removeSeries(ISeries series) {

		// Searches the series map to see if it is there and removes the series
		// if so
		if (seriesMap.containsKey(series)) {
			seriesMap.remove(series);
			// See if it is an error series to a parent in the map and remove it
		} else if (seriesMap.containsKey(series.getParentSeries())) {
			ISeries parent = series.getParentSeries();
			// Updates the trace for the parent, so that
			seriesMap.get(parent).remove(series);
			// Redraw the series and recalculate the trace so that it updates on
			// screen
			redrawSeries((ISeries) parent);

		}

		// Remove the trace as well if it is being plotted on the graph
		if (existingTraces.containsKey(series)) {
			xyGraph.removeTrace(existingTraces.remove(series));
			// TODO- Run on separate thread to not hang UI?
			xyGraph.repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		return;
	}

	/**
	 * This function sets up the SWT XYGraph
	 * 
	 * @param displayPlotProvider
	 *            The PlotProvider containing the information to create the plot
	 */
	public void showPlotProvider(PlotProvider displayPlotProvider) {

		// If it is not a contour plot then plot the regular series
		if (!displayPlotProvider.isContour()) {
			// Get the time to show
			List<Double> times = displayPlotProvider.getTimes();
			double time = (times.isEmpty() ? 0.0 : times.get(0));

			// Get the list of series from the data provider
			List<ISeries> seriesList = displayPlotProvider
					.getSeriesAtTime(time);
			ISeries indepSeries = displayPlotProvider.getIndependentSeries();

			boolean resetAllTraces = false;

			// Refresh the current traces
			if (!indepSeries.equals(independentSeries)) {
				resetAllTraces = true;
				independentSeries = indepSeries;

			}

			// Go through and add the series to the series map to find all error
			// series first
			for (ISeries series : seriesList) {
				// If the series has an error style, then it should go in the
				// map
				if (series.getStyle() instanceof BasicErrorStyle) {
					// See if the parent is already in the map
					ISeries parent = series.getParentSeries();
					// If the parent is not a key yet, then create the new map
					// entry
					if (!seriesMap.containsKey(parent)) {
						List<ISeries> errors = new ArrayList<ISeries>();
						errors.add(series);
						// Otherwise, just add this series to the existing list
						// of error series for the parent. Do not add the same
						// error series twice!
					} else if (!seriesMap.get(parent).contains(series)) {
						seriesMap.get(parent).add(series);
					}
					// If it is a regular series add it to the map with no error
					// series, if it has not already been added
				} else if (!seriesMap.containsKey(series)) {
					seriesMap.put(series, new ArrayList<ISeries>());
				}
			}

			// Show the graph
			showXYGraph(displayPlotProvider, time, resetAllTraces);

			// Re-create the slider only if there are alternate times to use.
			if (displayPlotProvider.getTimes().size() > 1) {
				if (sliderComposite != null) {
					sliderComposite.dispose();
					sliderComposite = createSliderComp(vizComposite,
							displayPlotProvider);
				}
			}
			// Otherwise, dispose the slider if it is not needed and exists.
			else if (sliderComposite != null) {
				sliderComposite.dispose();
				sliderComposite = null;
			}

		} else {

			// Get the first series
			final ISeries plotSeries = displayPlotProvider
					.getIndependentSeries();

			// Create a new intensity graph
			final IntensityGraphFigure intensityGraph = new IntensityGraphFigure();

			// Set the minimum and maximum
			double[] bounds = plotSeries.getBounds();
			intensityGraph.setMax(bounds[0] + bounds[1]);
			intensityGraph.setMin(bounds[0]);

			// FIXME Set the data width and data height
			// intensityGraph.setDataHeight(seriesProvider.getDataHeight());
			// intensityGraph.setDataWidth(seriesProvider.getDataWidth());

			// Stick with predefined colormap
			intensityGraph.setColorMap(
					new ColorMap(PredefinedColorMap.JET, true, true));

			// Set the contents
			lws.setContents(intensityGraph);

			// Try to get valid data from the series
			double[] newArray = getDoubleValue(plotSeries);

			// Sets the data array.
			intensityGraph.setDataArray(newArray);

			// Add controls for setting the min and max values
			Composite minMaxComp = new Composite(vizComposite, SWT.NONE);
			minMaxComp.setLayoutData(
					new GridData(SWT.CENTER, SWT.BOTTOM, false, false));
			minMaxComp.setLayout(new GridLayout(5, false));

			Label minLabel = new Label(minMaxComp, SWT.NONE);
			minLabel.setText("Minimum: ");
			final Text minText = new Text(minMaxComp, SWT.BORDER);
			minText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			Label maxLabel = new Label(minMaxComp, SWT.NONE);
			maxLabel.setText("Maximum: ");
			final Text maxText = new Text(minMaxComp, SWT.BORDER);
			maxText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			Button applyMinMaxButton = new Button(minMaxComp, SWT.BORDER);
			applyMinMaxButton.setText("Apply");
			applyMinMaxButton.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true));

			vizComposite.layout();

			applyMinMaxButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					// Set the minimum and maximum
					intensityGraph
							.setMax(Double.parseDouble(maxText.getText()));
					intensityGraph
							.setMin(Double.parseDouble(minText.getText()));

					// Set the contents
					lws.setContents(intensityGraph);

					return;
				}
			});
		}

		return;
	}

	/**
	 * @param plotProvider
	 * @param time
	 */
	private void showXYGraph(PlotProvider plotProvider, double time,
			boolean resetTraces) {
		if (this.xyGraph == null) {
			xyGraph = new XYGraph();
			graphToolbar = new ToolbarArmedXYGraph(xyGraph);
			lws.setContents(graphToolbar);
			existingTraces.clear();

			// Set the title as the source
			xyGraph.setTitle(plotProvider.getPlotTitle());

			// Set all of the xy graph settings from the plot style
			XYZPlotStyle plotStyle = plotProvider.getPlotStyle();
			if (plotStyle != null) {
				xyGraph.setTransparent((boolean) plotStyle
						.getProperty(XYZPlotStyle.IS_TRANSPARENT));
				xyGraph.setBackgroundColor(
						(Color) plotStyle.getProperty(XYZPlotStyle.PLOT_COLOR));
				xyGraph.setShowLegend((boolean) plotStyle
						.getProperty(XYZPlotStyle.SHOW_LEGEND));
				xyGraph.getPlotArea().setShowBorder((boolean) plotStyle
						.getProperty(XYZPlotStyle.SHOW_PLOT_BORDER));

			}

			// Get the axes styles from the plot provider
			XYZAxisStyle xStyle = plotProvider.getXAxisStyle();
			XYZAxisStyle yStyle = plotProvider.getYAxisStyle();

			// Configure the axes
			configureAxis(xyGraph.primaryXAxis, xStyle);
			configureAxis(xyGraph.primaryYAxis, yStyle);
		}

		// Make sure the lightweight system is displaying the xy graph
		lws.setContents(graphToolbar);

		// Get the series
		ArrayList<ISeries> seriesToPlot = plotProvider.getSeriesAtTime(time);
		if (seriesToPlot == null) {
			seriesToPlot = new ArrayList<ISeries>();
		}

		// Get the x values and error for plotting
		double[] xValues = getDoubleValue(independentSeries);
		double[] xPlusError = new double[xValues.length];
		double[] xMinusError = new double[xValues.length];
		// Gets the error for the series, if there is any
		if (seriesMap.containsKey(independentSeries)) {
			List<ISeries> errors = seriesMap.get(independentSeries);
			if (errors != null && errors.size() > 0) {
				for (ISeries errSeries : errors) {
					// Get the error style, and add the error to the
					// appropriate series
					BasicErrorStyle errStyle = (BasicErrorStyle) errSeries
							.getStyle();
					// If the style is positive error (the bars are above
					// the point)
					if (errStyle.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
							.equals(ErrorBarType.PLUS)) {
						sumArrays(xPlusError, getDoubleValue(errSeries));
						// If the style is negative error (the bars are
						// below the point)
					} else
						if (errStyle.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
								.equals(ErrorBarType.MINUS)) {
						sumArrays(xMinusError, getDoubleValue(errSeries));
						// If the style is both (above and below the point)
					} else if (errStyle
							.getProperty(BasicErrorStyle.ERROR_BAR_TYPE)
							.equals(ErrorBarType.BOTH)) {
						sumArrays(xPlusError, getDoubleValue(errSeries));
						sumArrays(xMinusError, getDoubleValue(errSeries));
					}
				}
			}

		}

		XYZSeriesStyle indepStyle = (XYZSeriesStyle) independentSeries
				.getStyle();
		// Iterate over all of the series and see if any need to be added or
		// reset
		for (ISeries iseries : seriesToPlot) {
			ISeries series = (ISeries) iseries;

			// Creates a new trace with the name, axis,and provider to plot
			Trace trace = configureTrace(series, xValues, xPlusError,
					xMinusError);

			// If the trace is not null, then add the new trace to the graph
			if (trace != null) {
				// Configure the x error for the series
				trace.setXErrorBarType((ErrorBarType) indepStyle
						.getProperty(XYZSeriesStyle.ERROR_TYPE));
				// Add the trace that was just created
				this.existingTraces.put(series, trace);
				// Make sure that if a series was recently removed that this
				// series won't be a new color for no reason.
				if (trace.getTraceColor() == null) {
					List<ISeries> keys = new ArrayList<ISeries>(
							existingTraces.keySet());
					trace.setTraceColor(XYGraphMediaFactory.getInstance()
							.getColor(XYGraph.DEFAULT_TRACES_COLOR[keys
									.indexOf(series)
									% XYGraph.DEFAULT_TRACES_COLOR.length]));
				}
				xyGraph.addTrace(trace);

			}

		}

		return;

	}

	/**
	 * Adds the second array to the first, element by element.
	 * 
	 * @param runningSum
	 *            The array that is modified by adding the elements of the other
	 * @param toAdd
	 *            The array to add to runningSum
	 */
	private void sumArrays(double[] runningSum, double[] toAdd) {
		for (int i = 0; i < runningSum.length && i < toAdd.length; i++) {
			runningSum[i] += toAdd[i];
		}
	}
}
