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
package org.eclipse.ice.viz.plotviewer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * The AddPlotDialog is a custom dialog that asks the user which type of plot
 * (line, scatter, bar, etc.) to plot.
 * 
 * @author w8o
 * 
 */
public class AddPlotDialog extends Dialog {

	/**
	 * The attribute for which plot types the user selected. Multiple plot type
	 * may be selected
	 */
	private String[] selections;

	/**
	 * 
	 * @param parentShell
	 */
	public AddPlotDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Creates the dialog window
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		// The composite for the dialog
		Composite plotSelection = (Composite) super.createDialogArea(parent);
		plotSelection.setLayout(new GridLayout(1, true));
		plotSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		// The label for selecting a plot type
		Label headerLabel = new Label(plotSelection, SWT.FILL);
		headerLabel.setText("Select Plot type");

		// The list for the types of plots to choose from
		final List plotsList = new List(plotSelection, SWT.MULTI | SWT.BORDER);
		plotsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// Current plot types are line, scatter, bar and contour with more to be
		// added
		plotsList.add("Line");
		plotsList.add("Scatter");
		plotsList.add("Bar");
		plotsList.add("Contour");

		// The listener for the list that gets the selected plot
		plotsList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				selections = plotsList.getSelection();
			}
		});

		// Default to the first item in the list.
		plotsList.setSelection(0);
		selections = plotsList.getSelection();

		return plotSelection;
	}

	/**
	 * Sets the title of the dialog
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add a Plot");
	}

	/**
	 * Returns the array of selections that the user chose.
	 * 
	 * @return
	 */
	public String[] getSelections() {
		return selections;
	}

}
