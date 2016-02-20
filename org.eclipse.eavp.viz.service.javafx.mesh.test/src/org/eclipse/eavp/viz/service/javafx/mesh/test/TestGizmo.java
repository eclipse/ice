/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.mesh.test;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.javafx.mesh.AxisGridGizmo;

import javafx.scene.shape.Box;

/**
 * An extension of the AxisGridGizmo that allows its axes to be retrieved.
 * 
 * @author RobertSmith
 *
 */
public class TestGizmo extends AxisGridGizmo {

	/**
	 * The default constructor.
	 * 
	 * @param gridSize
	 */
	public TestGizmo(double gridSize) {
		super(gridSize);
	}

	/**
	 * Gets the three axes displayed by the gizmo.
	 * 
	 * @return An ArrayList containing the three Box shapes used to represent
	 *         the axes, in the order x, y, z.
	 */
	public ArrayList<Box> getAxes() {
		ArrayList<Box> axes = new ArrayList<Box>();

		axes.add(axisX);
		axes.add(axisY);
		axes.add(axisZ);

		return axes;
	}

}
