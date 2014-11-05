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
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.eclipse.swt.graphics.Color;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.client.widgets.reactoreditor.Circle;
import org.eclipse.ice.client.widgets.reactoreditor.ColorScale;
import org.eclipse.ice.client.widgets.reactoreditor.ColorScalePalette;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CellFigure;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;

/**
 * This class extends {@link CellFigure} to provide a radial view of an
 * {@link LWRComponent}. Currently, it supports {@link LWRRod}s and {@link Tube}
 * s. It also provides both a geometric view of the component and a data-based
 * view for an associated {@link IDataProvider}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class RodFigure extends CellFigure implements ILWRComponentVisitor {

	/**
	 * This enum provides the two possible views for a RodFigure: geometry and
	 * data.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	public enum DisplayType {
		/**
		 * The default view, which just displays an empty circle.
		 */
		EMPTY("Empty"),
		/**
		 * The geometry view, which displays the rod as a radial slice of
		 * materials.
		 */
		GEOMETRY("Geometry"),
		/**
		 * The state-point data view, which displays a text label with data
		 * pertaining to a specific feature.
		 */
		DATA("State Point Data");

		/**
		 * The user-friendly name of the DisplayType.
		 */
		public final String name;

		/**
		 * The default private constructor.
		 * 
		 * @param name
		 *            The user-friendly name of the DisplayType.
		 */
		private DisplayType(String name) {
			this.name = name;
		}
	}

	/* ---- Constants and defaults ---- */
	/**
	 * The colors used for materials in a rod or tube.
	 */
	// Standard
	protected static final Color[] colors = { ColorConstants.darkGreen,
			ColorConstants.yellow, ColorConstants.red, ColorConstants.green,
			ColorConstants.orange };
	// // High-contrast
	/**
	 * The color of the background for the geometry view.
	 */
	protected static final Color geometryBackgroundColor = ColorConstants.darkBlue;
	/**
	 * The border for a selected Figure.
	 */
	private static final LineBorder selectedBorder = new LineBorder(
			ColorConstants.red, 4);
	/**
	 * The border for an unselected data label.
	 */
	private static final LineBorder dataBorder = new LineBorder(1);
	/* -------------------------------- */

	/* ---- Empty display variables ---- */
	/**
	 * The empty figure for displaying the component.
	 */
	protected Figure defaultFigure;
	/* --------------------------------- */

	/* ---- Geometry display variables ---- */
	/**
	 * The component displayed by this Figure.
	 */
	protected LWRComponent component;
	/**
	 * The Figure displaying the geometry information.
	 */
	protected Figure geometry;

	/**
	 * The maximum radius for figures. This is used to keep components with
	 * smaller radii from being the same size as components with large radii.
	 */
	protected double maxRadius;
	/**
	 * The current level of material pulled from a rod. FIXME - This needs to be
	 * configurable.
	 */
	protected final int materialLevel;
	/* ------------------------------------ */

	/* ---- Data display variables ---- */
	/**
	 * The data provider for the component displayed by this figure.
	 */
	protected IDataProvider dataProvider;
	/**
	 * The label for displaying data.
	 */
	protected Label data;

	/**
	 * An array of the IData for the current feature.
	 */
	protected List<IData> featureData;
	/**
	 * The feature for which we are displaying data.
	 */
	protected String feature;
	/**
	 * The axial level of the data this Figure displays.
	 */
	protected int axialLevel;
	/**
	 * The minimum value used to determine the data label's background color.
	 */
	protected double minValue;
	/**
	 * The maximum value used to determine the data label's background color.
	 */
	protected double maxValue;
	/**
	 * The minimum value used to determine the data label's background color.
	 */
	protected Double customMinValue;
	/**
	 * The maximum value used to determine the data label's background color.
	 */
	protected Double customMaxValue;
	/**
	 * This determines whether or not the extrema used to determine the data
	 * label's background color are custom or default. Custom extrema can be set
	 * by the class modifying this figure.
	 */
	protected boolean useCustomExtrema;
	/**
	 * The ColorScale used to color the data label's background based on the min
	 * and max values.
	 */
	protected ColorScale colorScale;
	/* -------------------------------- */

	/* ---- Other variables ---- */
	/**
	 * A List of RodAnalysisViews that should be notified when a Ring has been
	 * selected. They are notified by the MouseListeners in the visit() methods.
	 */
	private final List<RodAnalysisView> listeners;

	/**
	 * The current display type.
	 */
	protected DisplayType displayType;

	/* ------------------------- */

	/**
	 * The default constructor.
	 */
	public RodFigure() {
		super();

		// Set an initial component and data provider. They contain nothing, but
		// they are not null.
		component = new LWRComponent();
		dataProvider = new LWRDataProvider();

		// Initialize final lists, maps, etc.
		listeners = new ArrayList<RodAnalysisView>();

		// Initialize the defaults.
		maxRadius = 0.0;
		materialLevel = 0;
		feature = null;
		axialLevel = 0;
		featureData = new ArrayList<IData>();
		minValue = 0.0;
		maxValue = 0.0;
		customMinValue = null;
		customMaxValue = null;
		useCustomExtrema = false;
		colorScale = ColorScalePalette.Rainbow1.getColorScale();
		displayType = DisplayType.GEOMETRY;

		// Set the overall background color to white.
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);

		// Initialize the default Figure.
		defaultFigure = new RectangleFigure();
		defaultFigure.setVisible(false);
		defaultFigure.setBackgroundColor(ColorConstants.white);
		defaultFigure.setBorder(dataBorder);
		add(defaultFigure);

		// Initialize the geometry Figure.
		geometry = new Figure();
		geometry.setLayoutManager(new StackLayout());
		geometry.setVisible(true);
		geometry.setOpaque(true);
		geometry.setBackgroundColor(geometryBackgroundColor);
		add(geometry);

		// Initialize the data Label.
		data = new Label("0.0");
		data.setVisible(false);
		data.setBorder(null);
		data.setOpaque(false);
		data.setBackgroundColor(ColorConstants.white);
		add(data);

		// Set the IFigure created by CellFigure.
		figure = this;

		return;
	}

	/**
	 * Sets the component displayed by this RodFigure. This is the same as
	 * calling setComponent(component, null).
	 * 
	 * @param component
	 *            The new component.
	 */
	public void setComponent(LWRComponent component) {
		setComponent(component, null);
	}

	/**
	 * Sets the component displayed by this RodFigure.
	 * 
	 * @param component
	 *            The new component.
	 * @param maxRadius
	 *            The max radius. This influences the size of the pin that is
	 *            drawn.
	 */
	public void setComponent(LWRComponent component, Double maxRadius) {
		// Check the parameters.
		if (component != null && component != this.component) {
			// Update the stored reference.
			this.component = component;

			// Update maxRadius.
			if (maxRadius == null) {
				maxRadius = (new RadiusFetcher(component)).radius;
			}
			if (maxRadius > this.maxRadius) {
				this.maxRadius = maxRadius;
			}

			// Redraw the geometry figure.
			refreshGeometry();
		}
		return;
	}

	/**
	 * Sets the data provider for the data displayed by this RodFigure.
	 * 
	 * @param dataProvider
	 *            The data provider for the new component. Can be null for
	 *            tubes.
	 */
	public void setDataProvider(IDataProvider dataProvider) {
		if (dataProvider != null && dataProvider != this.dataProvider) {
			// Update the stored reference to the data provider.
			this.dataProvider = dataProvider;

			// Get the latest data list.
			featureData = dataProvider.getDataAtCurrentTime(feature);

			// If necessary, compute the default min and max values.
			if (!useCustomExtrema) {
				resetDefaultExtrema();
			}
			// Redraw the data label.
			refreshData();
		}
		return;
	}

	/* ---- Configuration Setters ---- */
	/**
	 * Set whether geometry or data is shown by this Figure.
	 * 
	 * @param displayType
	 *            The new display type.
	 */
	public void setDisplayType(DisplayType displayType) {
		// Only proceed if the display type changed.
		if (displayType != this.displayType) {
			this.displayType = displayType;

			if (displayType == DisplayType.GEOMETRY) {
				defaultFigure.setVisible(false);
				geometry.setVisible(true);
				data.setVisible(false);
			} else if (displayType == DisplayType.DATA) {
				defaultFigure.setVisible(true);
				geometry.setVisible(false);
				data.setVisible(true);
				// // Refresh the data figure.
			} else {
				defaultFigure.setVisible(true);
				geometry.setVisible(false);
				data.setVisible(false);
				// Remove the custom background color from the default figure.
				defaultFigure.setBackgroundColor(null);
			}
		}
		return;
	}

	/**
	 * Set the feature for the data being shown.
	 * 
	 * @param feature
	 *            The new feature.
	 */
	public void setFeature(String feature) {
		if (feature != null && !feature.equals(this.feature)) {
			// Update the feature.
			this.feature = feature;

			// Get the latest data list.
			featureData = dataProvider.getDataAtCurrentTime(feature);

			// Use the closest available axial level.
			if (axialLevel >= featureData.size()) {
				axialLevel = featureData.size() - 1;
			}
			if (axialLevel < 0) {
				axialLevel = 0;
			}

			// If necessary, compute the default min and max values.
			if (!useCustomExtrema) {
				resetDefaultExtrema();
			}

			// Redraw the data label.
			refreshData();
		}
		return;
	}

	/**
	 * Set the axial level for the feature being shown.
	 * 
	 * @param axialLevel
	 *            The new axial level.
	 */
	public void setAxialLevel(int axialLevel) {
		if (axialLevel >= 0 && axialLevel != this.axialLevel) {
			// Update the axial level.
			this.axialLevel = axialLevel;

			// Redraw the data label.
			refreshData();
		}
		return;
	}

	/**
	 * Sets the level for which the MaterialBlocks are displayed.
	 * 
	 * @param materialLevel
	 *            The index for the MaterialBlock to display.
	 */
	public void setMaterialLevel(int materialLevel) {
		// TODO
	}

	/**
	 * Set the max radius for this Figure. If multiple Figures are being
	 * displayed as part of an assembly, maxRadius should be set to the maximum
	 * radius among all the components.
	 * 
	 * @param maxRadius
	 *            The new maximum radius.
	 */
	public void setMaxRadius(double maxRadius) {
		if (Math.abs(maxRadius - this.maxRadius) > 1e-7) {
			// Update the max radius.
			this.maxRadius = maxRadius;

			// Redraw the geometry
			refreshGeometry();
		}
		return;
	}

	/**
	 * Sets whether or not this RodFigure uses custom min and max values to
	 * determine the color of the data label. If not, it uses the current min
	 * and max values for the displayed data. If the supplied values are
	 * invalid, expect black or white backgrounds!
	 * 
	 * @param useCustomExtrema
	 *            Whether or not to use custom extrema for the background color
	 *            of the RodFigure.
	 */
	public void useCustomExtrema(boolean useCustomExtrema) {
		if (useCustomExtrema != this.useCustomExtrema) {
			// Update the boolean.
			this.useCustomExtrema = useCustomExtrema;

			// Set the min and max values to the custom values or reset them
			// both as necessary.
			if (useCustomExtrema) {
				if (customMinValue != null) {
					minValue = customMinValue.doubleValue();
				}
				if (customMaxValue != null) {
					maxValue = customMaxValue.doubleValue();
				}
			} else {
				resetDefaultExtrema();
			}

			// Refresh the data label.
			refreshData();
		}
		return;
	}

	/**
	 * Sets the custom minimum value for determining the background color of the
	 * RodFigure.
	 * 
	 * @param customMinValue
	 *            The custom minimum value for computing background color.
	 */
	public void setMinValue(Double customMinValue) {
		if (customMinValue != null) {
			// Update the stored reference to the custom min value.
			this.customMinValue = customMinValue;

			// Update the minimum value if necessary.
			if (useCustomExtrema) {
				this.minValue = customMinValue.doubleValue();

				// Refresh the data label.
				refreshData();
			}
		}
		return;
	}

	/**
	 * Sets the custom maximum value for determining the background color of the
	 * RodFigure.
	 * 
	 * @param customMaxValue
	 *            The custom maximum value for computing background color.
	 */
	public void setMaxValue(Double customMaxValue) {
		if (customMaxValue != null) {
			// Update the stored reference to the custom max value.
			this.customMaxValue = customMaxValue;

			// Update the maximum value if necessary.
			if (useCustomExtrema) {
				this.maxValue = customMaxValue.doubleValue();

				// Refresh the data label.
				refreshData();
			}
		}
		return;
	}

	/**
	 * Sets the {@link ColorScale} currently used to determine the background
	 * color of the RodFigure. For handy preset scales, see
	 * {@link ColorScalePalette}.
	 * 
	 * @param colorScale
	 *            The new ColorScale to use for the RodFigure's background.
	 */
	public void setColorScale(ColorScale colorScale) {
		if (colorScale != null && colorScale != this.colorScale) {
			// Update the stored reference to the ColorScale.
			this.colorScale = colorScale;

			// Refresh the data label.
			refreshData();
		}
		return;
	}

	/* ------------------------------- */

	/* ---- Extends CellFigure ---- */
	/**
	 * Overrides the default behavior to make this the root figure.
	 */
	@Override
	public IFigure getFigure() {
		return this;
	}

	/**
	 * Overrides the default behavior to apply the current Cell state to all
	 * display types.
	 */
	@Override
	public void setState(State state) {

		// Modify the Figure's color or visibility based on the Cell's state.
		if (state == State.SELECTED) {
			defaultFigure.setBorder(selectedBorder);
			geometry.setBorder(selectedBorder);
			data.setBorder(selectedBorder);
		} else if (state == State.DISABLED) {
			this.setVisible(false);
		} else if (state == State.INVALID) {
			this.setVisible(false);
		} else {
			defaultFigure.setBorder(dataBorder);
			geometry.setBorder(null);
			data.setBorder(null);
		}

		return;
	}

	/**
	 * Overrides the default behavior to apply the selected/unselected state to
	 * all display types.
	 */
	@Override
	public void setSelected(boolean selected) {

		// Set the color of the CellFigure based on whether or not it is
		// selected.
		if (selected) {
			defaultFigure.setBorder(selectedBorder);
			geometry.setBorder(selectedBorder);
			data.setBorder(selectedBorder);
		} else {
			defaultFigure.setBorder(dataBorder);
			geometry.setBorder(null);
			data.setBorder(null);
		}

		return;
	}

	/* ---------------------------- */

	/**
	 * Repaints the geometry Figure.
	 */
	protected void refreshGeometry() {
		// The geometry needs to be refreshed in the following circumstances:
		// lwrComp changed
		// maxRadius changed
		// material level changed (not implemented)

		// Clear out any previous figures.
		geometry.removeAll();

		// Redraw any geometry figures.
		if (component != null) {
			component.accept(this);
		}

		return;
	}

	/**
	 * Resets the data label with the current data.
	 */
	protected void refreshData() {
		// The data label needs to be refreshed in the following circumstances:
		// lwrComp changed
		// feature changed
		// featureLevel changed

		double value;
		Color bg;
		Color fg;

		if (axialLevel < featureData.size()) {
			// Get the value for the feature at the current axial level.
			value = featureData.get(axialLevel).getValue();

			// Compute the background color of the value based on the current
			// ColorScale.
			bg = colorScale
					.getColor((value - minValue) / (maxValue - minValue));

			// Determine the proper foreground color so that the text will not
			// be an eye sore. This uses the Rec. 709 luma coefficients. We use
			// that with a threshold value to determine if the text should be
			// white or black.
			int luma = (int) (0.2126 * bg.getRed() + 0.7152 * bg.getGreen() + 0.0722 * bg
					.getBlue());
			fg = (luma < 75 ? ColorConstants.white : ColorConstants.black);
		} else {
			// If no data is available, the value is 0 and it's white on a black
			// background.
			value = 0.0;
			bg = ColorConstants.black;
			fg = ColorConstants.white;
		}

		// Set the background color for the default figure and the foreground
		// color for the data label.
		defaultFigure.setBackgroundColor(bg);
		data.setForegroundColor(fg);

		// Set the text value.
		data.setText(String.format("%.5f", value));

		return;
	}

	/**
	 * Sets the extrema ({@link #minValue} and {@link #maxValue} to their
	 * defaults. Their default values are based on the data currently in
	 * {@link #featureData}.
	 */
	private void resetDefaultExtrema() {
		// Restore the min and max values to their defaults.
		minValue = Double.MAX_VALUE;
		maxValue = Double.MIN_VALUE;
		for (IData iData : featureData) {
			double value = iData.getValue();
			minValue = Math.min(minValue, value);
			maxValue = Math.max(maxValue, value);
		}
		return;
	}

	/**
	 * Adds a RodAnalysisView as a listener for clicking on a Ring graphic.
	 * 
	 * @param listener
	 *            The RodAnalysisView that was to listen for Ring click events.
	 */
	public void addRingListener(RodAnalysisView listener) {
		if (listener != null) {
			listeners.add(listener);
		}
		return;
	}

	/**
	 * Adds a MouseListener to a Circle. The MouseListener notifies the Ring
	 * listeners that the Ring has been clicked.
	 * 
	 * @param ring
	 *            The Ring that has been clicked.
	 * @param circle
	 *            The Circle Figure that represents the Ring.
	 */
	protected MouseListener getRingClickListener(final Ring ring) {
		return new MouseListener() {
			public void mouseDoubleClicked(MouseEvent arg0) {
				return;
			}

			public void mousePressed(MouseEvent arg0) {
				return;
			}

			public void mouseReleased(MouseEvent arg0) {
				for (RodAnalysisView listener : listeners) {
					listener.clickRing(ring);
				}
			}
		};
	}

	/* ---- Implements ILWRComponentVisitor ---- */
	// As an LWRComponentVisitor, this class draws the geometric properties of
	// the current component.
	/**
	 * This function needs to draw a radial view of an LWR rod.
	 */
	public void visit(LWRRod rod) {
		int j = 0;

		/* ---- Draw the clad. ---- */
		// Get the clad.
		final Ring clad = rod.getClad();

		// Create a Circle shape for the clad.
		Circle circle = new Circle(clad.getOuterRadius(), maxRadius);
		circle.setBackgroundColor(colors[j++]);
		geometry.add(circle);

		// Add a click listener for the clad ring.
		circle.addMouseListener(getRingClickListener(clad));
		/* ------------------------ */

		// Set the initial innerRadius to the clad's inner radius. After the
		// last ring is drawn, we may need to draw an additional circle to show
		// empty space in the middle of the rod.
		double innerRadius = clad.getInnerRadius();

		/* ---- Draw the rings of materials. ---- */
		// Get the stack of materials from the rod.
		Object[] materialBlocks = rod.getMaterialBlocks().toArray();

		if ((materialBlocks == null) || (materialBlocks.length == 0)) {
			// If there is no material in the data, just draw the clad.
			circle = new Circle(clad.getInnerRadius(), maxRadius);
			circle.setBackgroundColor(geometryBackgroundColor);
			geometry.add(circle);
		} else {
			// For now, we are fetching the only MaterialBlock.
			MaterialBlock block = (MaterialBlock) materialBlocks[materialLevel];
			ArrayList<Ring> rings = block.getRings();

			// The rings are sorted innermost first by default.
			for (int i = rings.size() - 1; i >= 0; i--) {
				Ring ring = rings.get(i);

				// Create the circle shape for the ring.
				circle = new Circle(ring.getOuterRadius(), maxRadius);
				circle.setBackgroundColor(colors[j++ % colors.length]);
				geometry.add(circle);

				// Add a click listener for the ring.
				circle.addMouseListener(getRingClickListener(ring));

				// Update the inner radius.
				innerRadius = Math.min(innerRadius, ring.getInnerRadius());
			}
		}
		/* -------------------------------------- */

		// If necessary, draw another circle to show that there is empty space
		// in the middle of the rod.

		if (innerRadius > 0.0) {
			circle = new Circle(innerRadius, maxRadius);
			circle.setBackgroundColor(geometryBackgroundColor);
			geometry.add(circle);
		}

		return;
	}

	/**
	 * This function needs to draw a radial view of a tube.
	 */
	public void visit(Tube tube) {
		// Draw the tube's geometry (an empty circle/annulus).

		// Create a circle for the ring.
		Circle circle = new Circle(tube.getOuterRadius(), maxRadius);
		circle.setBackgroundColor(colors[0]);
		geometry.add(circle);

		// Add a click listener for the tube.
		circle.addMouseListener(getRingClickListener(tube));

		// Cheat by drawing an interior circle with the background color.
		circle = new Circle(tube.getInnerRadius(), maxRadius);
		circle.setBackgroundColor(geometryBackgroundColor);
		geometry.add(circle);
	}

	// Unused visit operations.
	public void visit(PressurizedWaterReactor lwrComp) {
		return;
	}

	public void visit(BWReactor lwrComp) {
		return;
	}

	public void visit(FuelAssembly lwrComp) {
		return;
	}

	public void visit(RodClusterAssembly lwrComp) {
		return;
	}

	public void visit(ControlBank lwrComp) {
		return;
	}

	public void visit(IncoreInstrument lwrComp) {
		return;
	}

	public void visit(Ring lwrComp) {
		return;
	}

	/* ---------------------------------------- */

	/**
	 * This visitor grabs the outer radius of the visited LWRComponent and
	 * stores it in {@link #radius}.
	 * 
	 * @author Jordan
	 * 
	 */
	private class RadiusFetcher implements ILWRComponentVisitor {

		/**
		 * The radius of the LWRComponent if it has an outer radius.
		 */
		public final double radius;

		/**
		 * A temporary storage for computing the radius.
		 */
		private double tmp;

		/**
		 * Creates a new RadiusFetcher. It requires a non-null LWRComponent. It
		 * visits the component and stores the radius if the component has an
		 * outer radius.
		 * 
		 * @param lwrComp
		 *            The component whose outer radius is to be found.
		 */
		public RadiusFetcher(LWRComponent lwrComp) {
			tmp = 0.0;
			if (lwrComp != null) {
				lwrComp.accept(this);
			}
			radius = tmp;
		}

		/**
		 * This function needs to draw a radial view of an LWR rod.
		 */
		public void visit(LWRRod rod) {
			tmp = rod.getClad().getOuterRadius();
		}

		/**
		 * This function needs to draw a radial view of a tube.
		 */
		public void visit(Tube tube) {
			tmp = tube.getOuterRadius();
		}

		public void visit(PressurizedWaterReactor lwrComp) {
			return;
		}

		public void visit(BWReactor lwrComp) {
			return;
		}

		public void visit(FuelAssembly lwrComp) {
			return;
		}

		public void visit(RodClusterAssembly lwrComp) {
			return;
		}

		public void visit(ControlBank lwrComp) {
			return;
		}

		public void visit(IncoreInstrument instrument) {
			tmp = instrument.getThimble().getOuterRadius();
		}

		public void visit(Ring ring) {
			tmp = ring.getOuterRadius();
		}
	}
}
