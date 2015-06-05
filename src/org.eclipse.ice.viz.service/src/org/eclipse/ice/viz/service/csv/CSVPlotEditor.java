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
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz.service.csv;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.widgets.datadefinition.ColorMap;
import org.eclipse.nebula.visualization.widgets.datadefinition.ColorMap.PredefinedColorMap;
import org.eclipse.nebula.visualization.widgets.figures.IntensityGraphFigure;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.ToolbarArmedXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

/**
 * This is an editor for displaying an SWT XYGraph used to draw plots from CSV
 * files. It is opened by the associated visualization views in
 * org.eclipse.ice.viz.
 * 
 * @author Matthew Wang
 * @author Taylor Patterson
 * @author Anna Wojtowicz
 * @author Jordan Deyton
 */
public class CSVPlotEditor extends EditorPart {

	/**
	 * The ID for this editor.
	 */
	public static final String ID = "org.eclipse.ice.viz.service.csv.CSVPlotEditor";

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
	 * The constructor
	 */
	public CSVPlotEditor() {
		return;
	}

	/*
	 * Overrides a super class method.
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		return;
	}

	/*
	 * Overrides a super class method.
	 */
	@Override
	public void doSaveAs() {
		return;
	}

	/*
	 * Overrides a super class method.
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
	 * Overrides a super class method.
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	/*
	 * Overrides a super class method.
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
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

	/*
	 * Overrides a super class method.
	 */
	@Override
	public void setFocus() {
		return;
	}

	/**
	 * This operation refreshes the VisIt view.
	 */
	public void refresh() {
		setFocus();
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

			List<Double> times = displayPlotProvider.getTimes();
			double time = (times.isEmpty() ? 0.0 : times.get(0));
			showXYGraph(displayPlotProvider, time);

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
			// Plot as a contour plot
			// Get the series list
			ArrayList<SeriesProvider> seriesProviderList = displayPlotProvider
					.getSeriesAtTime(displayPlotProvider.getTimes().get(0));
			// Get the first series
			final SeriesProvider seriesProvider = seriesProviderList.get(0);

			// Create a new intensity graph
			final IntensityGraphFigure intensityGraph = new IntensityGraphFigure();

			// Set the minimum and maximum
			intensityGraph.setMax(seriesProvider.getDataMax());
			intensityGraph.setMin(seriesProvider.getDataMin());

			// Set the data width and data height
			intensityGraph.setDataHeight(seriesProvider.getDataHeight());
			intensityGraph.setDataWidth(seriesProvider.getDataWidth());

			// Stick with predefined colormap
			intensityGraph.setColorMap(new ColorMap(PredefinedColorMap.JET,
					true, true));

			// Set the contents
			lws.setContents(intensityGraph);

			intensityGraph.setDataArray(seriesProvider.getYData());

			// Add controls for setting the min and max values
			Composite minMaxComp = new Composite(vizComposite, SWT.NONE);
			minMaxComp.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM,
					false, false));
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
			applyMinMaxButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, true));

			vizComposite.layout();

			applyMinMaxButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					seriesProvider.setDataMax(Double.parseDouble(maxText
							.getText()));
					seriesProvider.setDataMin(Double.parseDouble(minText
							.getText()));

					// Set the minimum and maximum
					intensityGraph.setMax(seriesProvider.getDataMax());
					intensityGraph.setMin(seriesProvider.getDataMin());

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
	private void showXYGraph(PlotProvider plotProvider, double time) {

		// Creates a new XYGraph
		XYGraph newXYGraph = new XYGraph();
		// A new ToolbarArmedXYGraph to hold the new XYGraph
		ToolbarArmedXYGraph newToolbarArmedXYGraph = new ToolbarArmedXYGraph(
				newXYGraph);
		// Set the title as the source
		newXYGraph.setTitle(plotProvider.getPlotTitle());
		newXYGraph.primaryXAxis.setShowMajorGrid(true);
		newXYGraph.primaryYAxis.setShowMajorGrid(true);
		newXYGraph.primaryYAxis.setAutoScale(true);
		newXYGraph.primaryXAxis.setAutoScale(true);
		// Sets the contents of the LightWeightSystem to the new graph
		lws.setContents(newToolbarArmedXYGraph);
		// Get the series
		ArrayList<SeriesProvider> seriesProviderList = plotProvider
				.getSeriesAtTime(time);
		if (seriesProviderList == null) {
			seriesProviderList = new ArrayList<SeriesProvider>();
		}
		for (SeriesProvider series : seriesProviderList) {

			CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(
					false);
			// Sets the size of the buffer
			traceDataProvider.setBufferSize(series.getXData().length);
			// Set the data to be plotted
			traceDataProvider.setCurrentXDataArray(series.getXData());
			traceDataProvider.setCurrentYDataArray(series.getYData());

			// Creates a new trace with the name, axis,and provider to plot
			Trace trace = new Trace(series.getSeriesTitle(),
					newXYGraph.primaryXAxis, newXYGraph.primaryYAxis,
					traceDataProvider);
			// Sets the point style to a x initially
			trace.setPointStyle(PointStyle.XCROSS);
			// Determine which type of plot was selected
			if ("Line".equals(series.getSeriesType())) {
				trace.setTraceType(TraceType.SOLID_LINE);
			} else if ("Scatter".equals(series.getSeriesType())) {
				trace.setTraceType(TraceType.POINT);
			} else if ("Bar".equals(series.getSeriesType())) {
				trace.setTraceType(TraceType.BAR);
				trace.setLineWidth(4);
			}

			// Add the trace to newXYGraph
			newXYGraph.addTrace(trace);
		}
		// Auto-scale the image
		newXYGraph.performAutoScale();

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
		timeButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
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
		timeSelectedLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		// Initially set it to the selected index
		timeSelectedLabel.setText(displayPlotProvider.getTimes().get(0)
				.toString());

		// The buttons for moving up and moving down
		Button downSpinnerButton = new Button(sliderComp, SWT.PUSH);
		Button upSpinnerButton = new Button(sliderComp, SWT.PUSH);
		upSpinnerButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		upSpinnerButton.setText(">");
		downSpinnerButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		downSpinnerButton.setText("<");

		final Runnable setPlotTime = new Runnable() {
			@Override
			public void run() {
				// Get the slider's selection which is an index to the times
				int sliderValue = slider.getSelection();

				double time = displayPlotProvider.getTimes().get(sliderValue);
				showXYGraph(displayPlotProvider, time);
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

	/**
	 * Gets the canvas used to render the CSV plot.
	 * 
	 * @return The plot canvas.
	 */
	public Canvas getPlotCanvas() {
		return plotCanvas;
	}
}
