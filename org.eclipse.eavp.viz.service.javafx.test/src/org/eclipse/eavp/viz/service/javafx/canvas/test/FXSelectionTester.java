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
package org.eclipse.eavp.viz.service.javafx.canvas.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.canvas.FXSelection;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.junit.Test;

/**
 * A class to test the functionality of the FXSelection
 * 
 * @author Robert Smith
 *
 */
public class FXSelectionTester {

	/**
	 * Check that the selection holds its input correctly.
	 */
	@Test
	public void checkContents() {

		// Create a part
		AbstractController part = new AbstractController(new AbstractMesh(),
				new AbstractView());

		// Create a selection on the part
		FXSelection selection = new FXSelection(part);

		// Check that the part is in the selection
		assertTrue(part == selection.getShape());
	}
}
