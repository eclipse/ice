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

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.viz.service.csv.CSVDataProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author Matthew Wang
 * 
 */
public class DataTableContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		if (inputElement instanceof IDataProvider) {
			// Cast into an IDataProvider
			IDataProvider myDataProvider = (IDataProvider) inputElement;
			// Cast into a CSVDataProvider
			CSVDataProvider myCSVDataProvider = (CSVDataProvider) myDataProvider;
			// Get the independent variables - requires CSVDataProvider
			ArrayList<String> independentVar = myCSVDataProvider
					.getIndependentVariables();
			// Get the features at the current time - does not require
			// CSVDataProvider
			ArrayList<String> features = myCSVDataProvider
					.getFeaturesAtCurrentTime();
			// Gets the number of columns
			int numColumns = independentVar.size() + features.size();
			int numRows = myCSVDataProvider.getDataAtCurrentTime(
					features.get(0)).size();

			double[][] returnArray = new double[numRows][numColumns];
			for (int i = 0; i < numColumns; i++) {
				double[] data;
				if (i < independentVar.size()) {
					data = myCSVDataProvider
							.getPositionAtCurrentTime(independentVar.get(i));
				} else {
					data = myCSVDataProvider.getValuesAtCurrentTime(features
							.get(i - independentVar.size()));
				}
				for (int j = 0; j < numRows; j++) {
					returnArray[j][i] = data[j];
				}
			}
			return returnArray;
		}
		return null;
	}

}
