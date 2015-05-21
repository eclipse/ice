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
package org.eclipse.ice.viz.csv.viewer;

import java.util.ArrayList;
import java.util.Collections;

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
 * The SelectFeatureDialog is a custom dialog which prompts the user to select
 * an X feature and Y feature to plot against.
 * 
 * @author Matthew Wang,Claire Saunders
 * 
 */
public class SelectFeatureDialog extends Dialog {
	/**
	 * The list of features to be displayed in the feature selection dialog
	 */
	private final ArrayList<String> featureList;
	/**
	 * The list of dependent variables to be displayed in the feature selection
	 * dialog
	 */
	private final ArrayList<String> independentVarList;
	/**
	 * The selected X Axis Feature or independent variable
	 */
	private final ArrayList<String> xAxisFeatures;
	/**
	 * The selected Y Axis Feature or independent/dependent variable
	 */
	private final ArrayList<String> yAxisFeatures;

	/**
	 * Constructor that sets the shell and attributes to null
	 * 
	 * @param parentShell
	 * @param multi
	 */
	protected SelectFeatureDialog(Shell parentShell) {
		super(parentShell);
		// Sets the attributes to null
		featureList = new ArrayList<String>();
		xAxisFeatures = new ArrayList<String>();
		yAxisFeatures = new ArrayList<String>();
		independentVarList = new ArrayList<String>();
	}

	/**
	 * Overrides the dialog box. Prompts the user for what features to select.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// The composite for the dialog box
		Composite featureSelection = (Composite) super.createDialogArea(parent);
		featureSelection.setLayout(new GridLayout(1, true));
		featureSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		// Checks that a list of features was set for the user to select from
		if (featureList != null) {

			// Label to prompt the user to select an X and a Y
			Label headerLabel = new Label(featureSelection, SWT.FILL);
			headerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));

			headerLabel.setText("Select features for X and Y axis");

			// Composite for the two scroll lists, one for X and one for Y
			Composite scrollLists = new Composite(featureSelection, SWT.BORDER);
			scrollLists.setLayout(new GridLayout(2, true));
			scrollLists.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));

			// A label for X
			Label xLabel = new Label(scrollLists, SWT.FILL | SWT.BORDER
					| SWT.CENTER);
			xLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			xLabel.setText("X");

			// A label for Y
			Label yLabel = new Label(scrollLists, SWT.FILL | SWT.BORDER
					| SWT.CENTER);
			yLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			yLabel.setText("Y");

			// The list for the X features
			final List xFeatureList = new List(scrollLists, SWT.V_SCROLL
					| SWT.BORDER | SWT.H_SCROLL | SWT.MULTI);
			xFeatureList.setLayoutData(new GridData(100, 150));

			// The list for the Y features
			final List yFeatureList = new List(scrollLists, SWT.V_SCROLL
					| SWT.BORDER | SWT.H_SCROLL | SWT.MULTI);
			yFeatureList.setLayoutData(new GridData(100, 150));

			// Add the dependent variables to the lists for display and
			// selection
			for (int i = 0; i < independentVarList.size(); i++) {
				xFeatureList.add(independentVarList.get(i));
			}

			// Add the features to the lists for display and selection
			for (int i = 0; i < featureList.size(); i++) {
				yFeatureList.add(featureList.get(i));
			}

			// The listener for the xFeatureList which sets what the user
			// selected to xAxisFeature
			xFeatureList.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					String[] selection = xFeatureList.getSelection();
					xAxisFeatures.clear();
					if (selection.length > 0) {
						for (String selected : selection) {
							xAxisFeatures.add(selected);
						}
					}
					return;
				}
			});

			// The listener for the yFeatureList which sets what the user
			// selected to yAxisFeature
			yFeatureList.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					String[] selection = yFeatureList.getSelection();
					yAxisFeatures.clear();
					if (selection.length > 0) {
						for (String selected : selection) {
							yAxisFeatures.add(selected);
						}
					}
					return;
				}
			});

			// Select the first available independent variable by default.
			if (!independentVarList.isEmpty()) {
				xFeatureList.setSelection(0);
				xAxisFeatures.add(independentVarList.get(0));
			}

			// Select the first available feature by default.
			if (!featureList.isEmpty()) {
				yFeatureList.setSelection(0);
				yAxisFeatures.add(featureList.get(0));
			}
		}

		return featureSelection;
	}

	/**
	 * Sets the title of the custom dialog
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select Features");
	}

	/**
	 * Sets the dependent variables List for the X Axis to the provided features
	 * 
	 * @param independentVars
	 */
	public void setXAxisFeatures(ArrayList<String> independentVars) {
		independentVarList.clear();
		if (independentVars != null) {
			independentVarList.addAll(independentVars);
			// Sort the list of x-axis variables.
			Collections.sort(independentVarList);
		}
	}

	/**
	 * Sets the dependent variables List for the X Axis to the provided features
	 * 
	 * @param features
	 */
	public void setYAxisFeatures(ArrayList<String> features) {
		featureList.clear();
		if (features != null) {
			featureList.addAll(features);
			// Sort the list of y-axis variables.
			Collections.sort(featureList);
		}
	}

	/**
	 * Returns the selected X axis feature
	 * 
	 * @return A list of selected X axis features. This will never be null.
	 */
	public ArrayList<String> getXAxisFeatures() {
		return new ArrayList<String>(xAxisFeatures);
	}

	/**
	 * Returns the selected Y axis feature
	 * 
	 * @return A list of selected Y axis features. This will never be null.
	 */
	public ArrayList<String> getYAxisFeatures() {
		return new ArrayList<String>(yAxisFeatures);
	}

}
