/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - bug 474742
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.ice.client.widgets.reactoreditor.MaterialShape;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.swt.graphics.Color;

/**
 * This class extends the RodFigure, which provides a radial view of an
 * {@link LWRComponent}, usually {@link LWRRod}s, to instead provide an axial or
 * vertical view of the component.
 * 
 * @author Jordan H. Deyton
 * 
 */
// FIXME The MouseListeners for the rod shapes aren't working.
public class AxialRodFigure extends RodFigure {

	/**
	 * A new figure to contain a column of axial data.
	 */
	private final Figure data;

	/**
	 * The default constructor. Responsible for creating a default, axial
	 * (vertical slice) view of a rod.
	 */
	public AxialRodFigure() {
		// Set up the default RodFigure first.
		super();

		GridLayout layout;

		// Change the overall layout of the geometry figure to a GridLayout. We
		// want to stack all of the material blocks in a column.
		layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		geometry.setLayoutManager(layout);

		// We also want to use a figure to contain several data labels, one for
		// each axial level. These should be put into a column like geometry.
		data = new Figure();
		layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		data.setLayoutManager(layout);
		data.setVisible(false);
		data.setBorder(null);
		data.setBackgroundColor(ColorConstants.cyan);
		add(data);

		return;
	}

	/**
	 * Overrides the default function to use the custom data figure.
	 * 
	 * @param displayType
	 *            The new display type.
	 */
	@Override
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
	 * Overrides the parent behavior to provide an axial view of the component's
	 * data.
	 */
	@Override
	protected void refreshData() {

		// Clear the current figure.
		data.removeAll();

		double value;
		Color bg;
		Color fg;
		Label label;
		GridData gridData;

		// Add all data for the current feature to labels arranged in a column.
		for (int i = featureData.size() - 1; i >= 0; i--) {
			value = featureData.get(i).getValue();

			// Compute the background color of the value based on the current
			// color factory.
			int color = colorFactory
					.findColor((value - minValue) / (maxValue - minValue));
			bg = colorFactory.createColor(null, color);

			// Determine the proper foreground color so that the text will not
			// be an eye sore. This uses the Rec. 709 luma coefficients. We use
			// that with a threshold value to determine if the text should be
			// white or black.
			int luma = (int) (0.2126 * bg.getRed() + 0.7152 * bg.getGreen()
					+ 0.0722 * bg.getBlue());
			fg = (luma < 75 ? ColorConstants.white : ColorConstants.black);

			// Create and add the label to the GridLayout.
			label = new Label(String.format("%.5f", value));
			label.setBackgroundColor(bg);
			label.setForegroundColor(fg);
			label.setOpaque(true);

			gridData = new GridData(0, 0, true, true, 1, 1);
			gridData = new GridData(GridData.FILL_BOTH);
			data.add(label, gridData);
		}

		if (featureData.isEmpty()) {
			label = new Label("0.0");
			label.setBackgroundColor(ColorConstants.black);
			label.setForegroundColor(ColorConstants.white);
			label.setOpaque(true);

			gridData = new GridData(0, 0, true, true, 1, 1);
			data.add(label, gridData);
		}

		return;
	}

	/**
	 * Overrides the parent behavior to provide an axial view of the rod's
	 * geometry.
	 */
	@Override
	public void visit(LWRRod rod) {
		// We need to draw the rod differently depending on the display type.

		// Clear the root figure.
		geometry.removeAll();

		// For the geometry view, we need to draw each block of material
		// from the top to the bottom of the rod.

		// Set the geometry background color.
		setBackgroundColor(geometryBackgroundColor);

		// Get the clad for the rod. This will be reused later. The max
		// radius is the outer radius of the clad.
		Ring clad = rod.getClad();
		double maxRadius = clad.getOuterRadius();

		// Get each of the material blocks.
		Object[] materialBlocks = rod.getMaterialBlocks().toArray();

		// If there is no material data available, just draw the clad.
		if ((materialBlocks == null) || (materialBlocks.length == 0)) {
			// Get a figure in which to store this material block drawing.
			Figure block = new Figure();
			block.setLayoutManager(new StackLayout());
			geometry.add(block, new GridData(GridData.FILL_BOTH));

			// Draw the clad.
			MaterialShape r = new MaterialShape(maxRadius, maxRadius);
			r.setBackgroundColor(colors[0]);
			block.add(r);

			// Add a mouse listener to listen for clicks on the ring.
			r.addMouseListener(getRingClickListener(clad));

			// Draw the clad filler.
			r = new MaterialShape(clad.getInnerRadius(), maxRadius);
			r.setBackgroundColor(geometryBackgroundColor);
			block.add(r);

			return;
		}

		// Loop through the material blocks from the top to the bottom.
		for (int i = materialBlocks.length - 1; i >= 0; i--) {
			// k is used to select different material colors.
			int k = 0;

			// Get a figure in which to store this material block drawing.
			Figure block = new Figure();
			block.setLayoutManager(new StackLayout());
			geometry.add(block, new GridData(GridData.FILL_BOTH));

			// Draw the clad.
			MaterialShape r = new MaterialShape(maxRadius, maxRadius);
			r.setBackgroundColor(colors[k++]);
			block.add(r);

			// Add a mouse listener to listen for clicks on the ring.
			r.addMouseListener(getRingClickListener(clad));

			// Draw all of the materials at this position.
			MaterialBlock materialBlock = (MaterialBlock) materialBlocks[i];
			// Fetch the array of materials in this material block.
			ArrayList<Ring> rings = materialBlock.getRings();
			for (int j = rings.size() - 1; j >= 0; j--) {
				// Add the shape to the block figure.
				r = new MaterialShape(rings.get(j).getOuterRadius(), maxRadius);
				r.setBackgroundColor(colors[k++]);
				block.add(r);

				// Add a mouse listener to listen for clicks on the ring.
				r.addMouseListener(getRingClickListener(rings.get(j)));
			}
		}

		return;
	}

	/**
	 * Overrides the parent behavior to provide an axial view of the tube's
	 * geometry.
	 */
	@Override
	public void visit(Tube tube) {

		// Clear the root Figure.
		geometry.removeAll();

		// Set the geometry background color.
		setBackgroundColor(geometryBackgroundColor);

		// Create a block figure to contain the components of the tube.
		Figure block = new Figure();
		block.setLayoutManager(new StackLayout());
		geometry.add(block, new GridData(GridData.FILL_BOTH));

		// Draw the tube.
		MaterialShape r = new MaterialShape(tube.getOuterRadius(),
				tube.getOuterRadius());
		r.setBackgroundColor(colors[0]);
		block.add(r);

		// Add a listener to listen for clicks on the tube.
		r.addMouseListener(getRingClickListener(tube));

		// To make the tube look hollow, draw another shape using the tube's
		// inner radius.
		r = new MaterialShape(tube.getInnerRadius(), tube.getOuterRadius());
		r.setBackgroundColor(geometryBackgroundColor);
		block.add(r);

		return;
	}
}
