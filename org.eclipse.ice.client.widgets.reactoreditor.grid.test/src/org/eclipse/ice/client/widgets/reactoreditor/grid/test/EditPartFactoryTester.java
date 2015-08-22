/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.grid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.gef.EditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CellEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CircularCellEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CircularGridEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CircularGridEditPartFactory;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Grid;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditPartFactory;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorInput;
import org.eclipse.ice.client.widgets.reactoreditor.grid.HexagonalCellEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.HexagonalGridEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.HexagonalGridEditPartFactory;
import org.junit.Test;

/**
 * This class tests the custom GEF EditPartFactories from the GridEditor
 * package. This must be run as a JUnit Plug-in Test.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class EditPartFactoryTester {

	/**
	 * Tests the rectangular GridEditPartFactory.
	 */
	@Test
	public void checkGridEditPartFactory() {
		// Create an EditPartFactory.
		GridEditPartFactory factory = new GridEditPartFactory();

		// Create a basic Grid model (contains Cell models).
		GridEditorInput input = new GridEditorInput(5, 7);
		Grid grid = new Grid(input);

		// Make sure the factory creates the right EditPart for the Grid.
		EditPart editPart = factory.createEditPart(null, grid);
		assertTrue(editPart instanceof GridEditPart);
		assertEquals(grid, editPart.getModel());

		// Make sure the factory creates the right EditParts for the Cells.
		for (Cell cell : grid.getCells()) {
			editPart = factory.createEditPart(null, cell);
			assertTrue(editPart instanceof CellEditPart);
			assertEquals(cell, editPart.getModel());
		}
	}

	/**
	 * Tests the HexagonalGridEditPartFactory.
	 */
	@Test
	public void checkHexagonalGridEditPartFactory() {
		// Create an EditPartFactory.
		HexagonalGridEditPartFactory factory = new HexagonalGridEditPartFactory();

		// Create a basic Grid model (contains Cell models).
		GridEditorInput input = new GridEditorInput(5, 7);
		Grid grid = new Grid(input);

		// Make sure the factory creates the right EditPart for the Grid.
		EditPart editPart = factory.createEditPart(null, grid);
		assertTrue(editPart instanceof HexagonalGridEditPart);
		assertEquals(grid, editPart.getModel());

		// Make sure the factory creates the right EditParts for the Cells.
		for (Cell cell : grid.getCells()) {
			editPart = factory.createEditPart(null, cell);
			assertTrue(editPart instanceof HexagonalCellEditPart);
			assertEquals(cell, editPart.getModel());
		}
	}

	/**
	 * Tests the CircularGridEditPartFactory.
	 */
	@Test
	public void checkCircularGridEditPartFactory() {
		// Create an EditPartFactory.
		CircularGridEditPartFactory factory = new CircularGridEditPartFactory();

		// Create a basic Grid model (contains Cell models).
		GridEditorInput input = new GridEditorInput(5, 7);
		Grid grid = new Grid(input);

		// Make sure the factory creates the right EditPart for the Grid.
		EditPart editPart = factory.createEditPart(null, grid);
		assertTrue(editPart instanceof CircularGridEditPart);
		assertEquals(grid, editPart.getModel());

		// Make sure the factory creates the right EditParts for the Cells.
		for (Cell cell : grid.getCells()) {
			editPart = factory.createEditPart(null, cell);
			assertTrue(editPart instanceof CircularCellEditPart);
			assertEquals(cell, editPart.getModel());
		}
	}

}
