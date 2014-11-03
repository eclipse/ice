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
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CellEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Grid;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditPartFactory;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorInput;

import java.util.BitSet;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * The GridEditorDialog offers a dialog-based view of the GridEditor. GEF
 * Editors are not meant to be used in dialogs, so instead of instantiating a
 * GridEditor, we can create the GraphicalViewer normally used to display the
 * content of the editor. We can still use the GridEditorInput as a means to
 * provide data about the grid to the GraphicalViewer.
 * 
 * @author djg
 * 
 */
public class GridEditorDialog extends Dialog {

	/**
	 * The model typically used with a GridEditor. We need to build this model
	 * from the GridEditorInput and feed its information to the GraphicalViewer.
	 */
	private Grid grid;

	/**
	 * The default constructor for a GridEditorDialog.
	 * 
	 * @param shell
	 *            The parent shell.
	 * @param input
	 *            The GridEditorInput that would normally be used with a
	 *            GridEditor.
	 */
	public GridEditorDialog(Shell shell, GridEditorInput input) {
		super(shell);

		// Construct a Grid model from the input.
		grid = new Grid(input);
	}

	/**
	 * Overridden from the default behavior.
	 */
	@Override
	protected void setShellStyle(int style) {
		super.setShellStyle(SWT.TITLE | SWT.RESIZE | SWT.CLOSE);
	}

	/**
	 * Overrides the default behavior of <code>initializeBounds()</code> to size
	 * the dialog's shell and center it on the monitor. <br>
	 * <br>
	 * Originally, this code was placed in <code>createDialogArea()</code>.
	 * However, according to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=289029">this bug
	 * report</a>, <code>initializeBounds()</code> is called automatically
	 * afterwards in the dialog's <code>create()</code> method, and changes to
	 * the size of the shell should be declared there.
	 */
	@Override
	protected void initializeBounds() {
		// Get the shell for this dialog.
		Shell shell = getShell();

		// Set the shell's size.
		shell.setSize(800, 800);

		// Perform whatever is necessary for the Dialog class to perform.
		super.initializeBounds();

		// Move the dialog to the middle of the monitor
		Rectangle monitorArea = shell.getDisplay().getPrimaryMonitor()
				.getBounds();
		Rectangle shellArea = shell.getBounds();
		int x = monitorArea.x + (monitorArea.width - shellArea.width) / 2;
		int y = monitorArea.y + (monitorArea.height - shellArea.height) / 2;
		shell.setLocation(x, y);
	}

	/**
	 * Overridden from the default behavior. Creates the dialog's contents,
	 * including any SWT Composites or Widgets.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// Get the Composite created by the super's createDialogArea().
		Composite composite = (Composite) super.createDialogArea(parent);

		// Set the shell message
		composite.getShell().setText("Grid Cell Selection");

		// Set the layout of the Composite to a FillLayout. The GraphicalViewer
		// should take up all available space in the dialog.
		composite.setLayout(new FillLayout());

		// Create the GraphicalViewer. This is fairly standard procedure for
		// GEF.
		GraphicalViewer viewer = new ScrollingGraphicalViewer();
		viewer.setRootEditPart(new SimpleRootEditPart());
		viewer.createControl(composite);
		// Pass the custom EditPartFactory and the Grid model to the
		// GraphicalViewer.
		viewer.setEditPartFactory(new GridEditPartFactory());
		viewer.setContents(grid);
		// Set the GraphicalViewer's EditDomain to a default.
		viewer.setEditDomain(new DefaultEditDomain(null));

		/* -- Set the viewer's current selection of cells. -- */
		// Here, we need to set the viewer's current selection of EditParts to
		// the currently-selected Cells in the Grid model. This allows us to
		// show the user the Cells that were previously selected. To do this, we
		// need to add all of the EditParts for selected Cells to the viewer's
		// StructuredSelection.

		// Get the indices of the currently-selected Cells in the Grid.
		BitSet selectedCells = grid.getSelectedCells();

		// Create the object array needed to create the StructuredSelection.
		Object[] selectionArray = new Object[selectedCells.cardinality()];

		// Add the CellEditPart of each selected Cell to the array.
		int j = 0;
		// This loops through all of the set bits in the BitSet.
		for (int i = selectedCells.nextSetBit(0); i >= 0; i = selectedCells
				.nextSetBit(i + 1)) {
			// Get the Cell and its EditPart at the current index.
			Cell cell = grid.getCells().get(i);
			CellEditPart editPart = (CellEditPart) viewer.getEditPartRegistry()
					.get(cell);

			// Add the CellEditPart to the array.
			selectionArray[j++] = editPart;
		}

		// Create the StructuredSelection from the array and pass it to viewer.
		StructuredSelection selection = new StructuredSelection(selectionArray);
		viewer.setSelection(selection);
		/* -------------------------------------------------- */

		return composite;
	}

	/**
	 * Gets a BitSet representing selected locations in the grid.
	 * 
	 * @return A BitSet of selected grid locations.
	 */
	public BitSet getSelectedLocations() {
		return grid.getSelectedCells();
	}
}
