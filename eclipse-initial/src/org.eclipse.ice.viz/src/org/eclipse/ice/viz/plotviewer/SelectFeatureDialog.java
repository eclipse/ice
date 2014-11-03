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
 * The SelectFeatureDialog is a custom dialog which prompts the user to select
 * an X feature and Y feature to plot against.
 * 
 * @author w8o,xxi
 * 
 */
public class SelectFeatureDialog extends Dialog {
	/**
	 * The list of features to be displayed in the feature selection dialog
	 */
	private ArrayList<String> featureList;
	/**
	 * The list of dependent variables to be displayed in the feature selection
	 * dialog
	 */
	private ArrayList<String> independentVarList;
	/**
	 * The selected X Axis Feature or independent variable
	 */
	private String xAxisFeature;
	/**
	 * The selected Y Axis Feature or independent/dependent variable
	 */
	private String yAxisFeature;

	/**
	 * Constructor that sets the shell and attributes to null
	 * 
	 * @param parentShell
	 * @param multi
	 */
	protected SelectFeatureDialog(Shell parentShell) {
		super(parentShell);
		// Sets the attributes to null
		featureList = null;
		xAxisFeature = null;
		yAxisFeature = null;
		// TODO Auto-generated constructor stub
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
					| SWT.BORDER | SWT.H_SCROLL);
			xFeatureList.setLayoutData(new GridData(100, 150));

			// The list for the Y features
			final List yFeatureList = new List(scrollLists, SWT.V_SCROLL
					| SWT.BORDER | SWT.H_SCROLL);
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
					if (selection.length > 0) {
						xAxisFeature = selection[0];
					}
				}
			});

			// The listener for the yFeatureList which sets what the user
			// selected to yAxisFeature
			yFeatureList.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					String[] selection = yFeatureList.getSelection();
					if (selection.length > 0) {
						yAxisFeature = selection[0];
					}
				}
			});

			// Select the first available independent variable by default.
			if (!independentVarList.isEmpty()) {
				xFeatureList.setSelection(0);
				xAxisFeature = independentVarList.get(0);
			}

			// Select the first available feature by default.
			if (!featureList.isEmpty()) {
				yFeatureList.setSelection(0);
				yAxisFeature = featureList.get(0);
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
	 * @param features
	 */
	public void setXAxisFeatures(ArrayList<String> independentVars) {
		this.independentVarList = independentVars;
	}

	/**
	 * Sets the dependent variables List for the X Axis to the provided features
	 * 
	 * @param features
	 */
	public void setYAxisFeatures(ArrayList<String> features) {
		this.featureList = features;
	}

	/**
	 * Returns the selected X axis feature
	 * 
	 * @return
	 */
	public String getXAxisFeature() {
		return this.xAxisFeature;
	}

	/**
	 * Returns the selected Y axis feature
	 * 
	 * @return
	 */
	public String getYAxisFeature() {
		return this.yAxisFeature;
	}

}
