/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.eavp.viz.service.visit.test;

import static org.junit.Assert.fail;

import org.eclipse.eavp.viz.service.visit.VisItPlot;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class is responsible for testing the {@link VisItPlot}.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItPlotTester {

	// TODO Implement these tests.

	/**
	 * This method checks the plot types returned by the plot.
	 * 
	 * @see VisItPlot#getPlotTypes()
	 */
	@Ignore
	@Test
	public void checkPlotTypes() {
		fail("Not implemented.");
	}

	/**
	 * This method checks that the plot can be drawn correctly.
	 * 
	 * @see VisItPlot#draw(String, String, org.eclipse.swt.widgets.Composite)
	 */
	@Ignore
	@Test
	public void checkDraw() {
		fail("Not implemented.");
	}

	/**
	 * This test checks the number of axes for the plot.
	 * 
	 * @see VisItPlot#getNumberOfAxes()
	 */
	@Ignore
	@Test
	public void checkNumberOfAxes() {
		fail("Not implemented.");
	}

	/**
	 * This test checks methods related to the plot's properties.
	 * 
	 * @see VisItPlot#getProperties()
	 * @see VisItPlot#setProperties(java.util.Map)
	 */
	@Ignore
	@Test
	public void checkProperties() {
		fail("Not implemented.");
	}

	/**
	 * This test checks methods related to the plot's source information.
	 * 
	 * @see VisItPlot#getDataSource()
	 * @see VisItPlot#getSourceHost()
	 * @see VisItPlot#isSourceRemote()
	 */
	@Ignore
	@Test
	public void checkSource() {
		fail("Not implemented.");
	}
}
