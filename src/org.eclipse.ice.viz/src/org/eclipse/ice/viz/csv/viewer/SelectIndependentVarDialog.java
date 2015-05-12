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

public class SelectIndependentVarDialog extends Dialog {
	/**
	 * The ArrayList of selected features for independent variable
	 */
	private final ArrayList<String> independentVariables;
	/**
	 * The list of features to be displayed in the feature selection dialog
	 */
	private final ArrayList<String> featureList;
	/**
	 * The source or name of the provider
	 */
	private String source;

	/**
	 * Default Constructor
	 * 
	 * @param parent
	 */
	public SelectIndependentVarDialog(Shell parent) {
		super(parent);
		featureList = new ArrayList<String>();
		source = null;
		independentVariables = new ArrayList<String>();
	}

	/**
	 * Overrides the dialog box. Prompts the user for what features to select.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// The composite for the dialog box
		Composite independentVarSelection = (Composite) super
				.createDialogArea(parent);
		independentVarSelection.setLayout(new GridLayout(1, true));
		independentVarSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));

		// Checks that a list of features was set for the user to select from
		if (featureList != null) {
			// Label to prompt the user to select an X and a Y
			Label headerLabel = new Label(independentVarSelection, SWT.FILL);
			headerLabel.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT,
					false, false));
			headerLabel.setText("Select independent variables for " + source
					+ "\n(Ctrl-click to select multiple)");

			// The list for the X features
			final List independentFeatureList = new List(
					independentVarSelection, SWT.V_SCROLL | SWT.BORDER
							| SWT.H_SCROLL | SWT.MULTI);
			independentFeatureList.setLayoutData(new GridData(SWT.FILL,
					SWT.DEFAULT, true, false));

			for (String feature : featureList) {
				independentFeatureList.add(feature);
			}

			// The listener for the xFeatureList which sets what the user
			// selected to xAxisFeature
			independentFeatureList.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					independentVariables.clear();
					String[] selection = independentFeatureList.getSelection();
					for (int i = 0; i < selection.length; i++) {
						independentVariables.add(selection[i]);
					}
				}
			});

			// Select the first item by default.
			if (!featureList.isEmpty()) {
				independentFeatureList.setSelection(0);
				independentVariables.add(featureList.get(0));
			}
		}

		return independentVarSelection;
	}

	/**
	 * Sets the title of the custom dialog
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select Independent Variables");
	}

	/**
	 * Returns the selected dependent variables from the dialog window
	 * 
	 * @return A list of selected x axis features. This will never be null.
	 */
	public ArrayList<String> getIndependentVars() {
		return new ArrayList<String>(independentVariables);
	}

	/**
	 * Sets the features that the user will select from
	 * 
	 * @param features
	 */
	public void setFeatureList(ArrayList<String> features) {
		featureList.clear();
		if (features != null) {
			featureList.addAll(features);
			// Sort the list of features.
			Collections.sort(featureList);
		}
	}

	public void setProviderName(String source) {
		if (source != null) {
			this.source = source;
		}
	}
}
