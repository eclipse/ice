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

import java.util.ArrayList;

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
 * The SelectTimeDialog is a custom dialog that prompts the user for which time
 * to plot in the provider
 * 
 * @author w8o
 * 
 */
public class SelectTimeDialog extends Dialog {

	/**
	 * ArrayList for the available times in the arraylist
	 */
	private ArrayList<Double> availableTimes;
	/**
	 * The selected time
	 */
	private Double selectedTime;

	/**
	 * The default constructor
	 * 
	 * @param parent
	 */
	protected SelectTimeDialog(Shell parent) {
		super(parent);
		// TODO Auto-generated constructor stub
		availableTimes = null;
		selectedTime = null;
	}

	/**
	 * Overrides the dialog box. Prompts the user for what features to select.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// The composite for the dialog box
		Composite timeSelection = (Composite) super.createDialogArea(parent);
		timeSelection.setLayout(new GridLayout(1, false));
		timeSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		// Checks that a list of times was set for the user to select from
		if (availableTimes != null) {
			// Label to prompt the user to select a time
			Label headerLabel = new Label(timeSelection, SWT.FILL);
			headerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));
			headerLabel.setText("Select time");

			// The list for the times
			final List timesList = new List(timeSelection, SWT.V_SCROLL
					| SWT.BORDER | SWT.H_SCROLL);
			timesList.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true,
					false));

			// Add the times to the lists for display and selection
			for (int i = 0; i < availableTimes.size(); i++) {
				timesList.add(availableTimes.get(i) + "");
			}

			// The listener for the yFeatureList which sets what the user
			// selected to yAxisFeature
			timesList.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					String[] selection = timesList.getSelection();
					if (selection.length > 0) {
						selectedTime = Double.parseDouble(selection[0]);
					}
				}
			});

			// Select the first item by default.
			if (!availableTimes.isEmpty()) {
				timesList.setSelection(0);
				selectedTime = availableTimes.get(0);
			}
		}

		return timeSelection;
	}

	/**
	 * Sets the title of the custom dialog
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select the Time");
	}

	/**
	 * Sets the available times to the given times arraylist
	 * 
	 * @param times
	 */
	public void setTimes(ArrayList<Double> times) {
		this.availableTimes = times;
	}

	/**
	 * Returns the selectedTime from the dialog
	 * 
	 * @return
	 */
	public Double getSelectedTime() {
		return selectedTime;
	}
}
