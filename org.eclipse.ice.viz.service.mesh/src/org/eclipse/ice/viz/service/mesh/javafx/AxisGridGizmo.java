/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.mesh.javafx;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * A group containing the grid and axis for a JavaFX Mesh Editor.
 *
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 * @author Robert Smith
 *
 */
public class AxisGridGizmo extends Group {

	/** */
	private Box axisX;
	private Box axisY;
	private Box axisZ;

	/** */
	private float axisWidth = .25f;

	/** */
	private Node owner;

	/**
	 * The default constructor. This creates a grid that is 16 x gridSize tall
	 * and 32 x gridSize wide, with cells being squares with a side length of
	 * gridSize.
	 *
	 * @param gridSize
	 *            The scale of the grid
	 */
	public AxisGridGizmo(double gridSize) {
		super();

		PhongMaterial axisXMaterial = new PhongMaterial();
		axisXMaterial.setDiffuseColor(Color.RED);

		PhongMaterial axisYMaterial = new PhongMaterial();
		axisYMaterial.setDiffuseColor(Color.GREEN);

		PhongMaterial axisZMaterial = new PhongMaterial();
		axisZMaterial.setDiffuseColor(Color.BLUE);

		PhongMaterial gridMaterial = new PhongMaterial();
		gridMaterial.setDiffuseColor(Color.WHITE);

		// Set the grid to be ignored by the mouse
		setMouseTransparent(true);

		ObservableList<Node> children = getChildren();

		// Iterate across each of the columns
		for (double i = (-gridSize * 16); i <= gridSize * 16; i = i
				+ gridSize) {

			Box line;
			// Make every fourth line slightly thicker
			if (i % 4 == 0) {
				line = new Box(0.2, gridSize * 16, 0);
			} else {
				line = new Box(0.1, gridSize * 16, 0);
			}
			line.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));
			line.setTranslateX(i);
			line.setTranslateZ(-0.5);

			children.add(line);
		}

		// Iterate across each of the large boxes in the column
		for (double j = (-gridSize * 8); j <= gridSize * 8; j = j + gridSize) {

			Box line;
			// Make every fourth line slightly thicker
			if (j % 4 == 0) {
				line = new Box(gridSize * 32, 0.2, 0);
			} else {
				line = new Box(gridSize * 32, 0.1, 0);
			}
			line.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));
			line.setTranslateY(j);
			line.setTranslateZ(-0.5);

			children.add(line);
		}

		// Create the x axis
		axisX = new Box(gridSize * 16, axisWidth, 0);
		axisX.setMaterial(axisXMaterial);
		axisX.setTranslateX(gridSize * 8);
		axisX.setTranslateY(0);
		axisX.setTranslateZ(-0.6);

		// Create the y axis
		axisY = new Box(axisWidth, gridSize * 8, 0);
		axisY.setMaterial(axisYMaterial);
		axisY.setTranslateX(0);
		axisY.setTranslateY((gridSize * 4));
		axisY.setTranslateZ(-0.6);

		// Create the z axis
		axisZ = new Box(axisWidth, axisWidth, gridSize);
		axisZ.setMaterial(axisZMaterial);
		axisZ.setTranslateX(0);
		axisZ.setTranslateY(0);
		axisZ.setTranslateZ(-gridSize * 0.5);

		children.add(axisX);
		children.add(axisY);
		children.add(axisZ);
	}

	/**
	 * Create two dimensional square with its upper left corner at (i,j) on the
	 * xy plane and side length gridSize * 4. Inside this outer square there
	 * will be a grid of 16 smaller squares of side length gridSize.
	 * 
	 * @param i
	 *            The box's upper left corner's x coordinate
	 * @param j
	 *            The box's upper right corner's y coordinate
	 * @param gridSize
	 *            The side length of the smaller 16 squares in the grid
	 */
	public void createGrid(double i, double j, double gridSize) {

		// // Create the boundary box
		// Box boundary = new Box(gridSize * 4, gridSize * 4, 0);
		// boundary.setMouseTransparent(true);
		// boundary.setDrawMode(DrawMode.LINE);
		// boundary.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));
		//
		// // Set it so that the corner is at point (i, j)
		// boundary.setTranslateX(i + gridSize / 2);
		// boundary.setTranslateY(j + gridSize / 2);
		// boundary.setTranslateZ(-0.5);
		//
		// // Add it to the node
		// ObservableList<Node> children = getChildren();
		// children.add(boundary);
		//
		// // Iterate across each of the box's columns
		// for (double k = -1; k < 3; k++) {
		//
		// // Iterate across each of the grid squares inside the column
		// for (double l = -1; l < 3; l++) {
		//
		// // Create the grid box
		// Box box = new Box(gridSize, gridSize, 0);
		// box.setMouseTransparent(true);
		// box.setDrawMode(DrawMode.LINE);
		// box.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));
		//
		// // Set it so that it is in position (k, l) of the parent
		// // boundary box's layout
		// box.setTranslateX(i + k * gridSize);
		// box.setTranslateY(j + l * gridSize);
		// box.setTranslateZ(-0.5);
		//
		// // Add it to the node
		// children.add(box);
		// }
		// }

	}

	public Node getOwner() {
		return owner;
	}

	public void setOwner(Node owner) {
		this.owner = owner;
	}

	/**
	 * Sets the x, y, and z axis as either visible or invisible
	 * 
	 * @param on
	 *            Whether or not to display the axes. If on is true, the axes
	 *            will be rendered. If false, they will not be.
	 */
	public void toggleAxis(boolean on) {
		axisX.setVisible(on);
		axisY.setVisible(on);
		axisZ.setVisible(on);
	}

	/**
	 * Checks whether the gizmo's axes are visible.
	 * 
	 * @return True if the gizmo's axes are visible, false otherwise.
	 */
	public boolean axesVisible() {
		return axisX.isVisible();
	}
}
