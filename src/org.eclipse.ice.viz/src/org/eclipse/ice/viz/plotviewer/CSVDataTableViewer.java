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

import org.eclipse.ice.viz.VizFileViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

/**
 * This class extends the ViewPart class and provides a view in the
 * Visualization Perspective to display the raw data associated with currently
 * displayed CSV plot.
 * 
 * @author Taylor Patterson, Matthew Wang
 * 
 */
public class CSVDataTableViewer extends ViewPart implements ISelectionListener {

	/**
	 * The ID for this view
	 */
	public static final String ID = "org.eclipse.ice.viz.plotviewer.CSVDataTableViewer";

	/**
	 * The TabFolder for the
	 */
	private TabFolder timesTabFolder;

	/**
	 * The list of existing tabs
	 */
	private ArrayList<String> tabs;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Initialize the TabFolder and the list of tabs
		timesTabFolder = new TabFolder(parent, SWT.NONE);
		tabs = new ArrayList<String>();

		// Register as a listener to the VizFileViewer.
		getSite().getWorkbenchWindow().getSelectionService()
				.addPostSelectionListener(VizFileViewer.ID, this);

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Do nothing yet.
		return;
	}

	/**
	 * Update this view based on selections in the {@link VizFileViewer}.
	 * 
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart,
	 *      ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// FIXME Disabling this until the issues are resolved;

		// // Check that the selection's source is the VizFileViewer.
		// // Try to get the VizResource from the ISelection. We first have to
		// // cast the ISelection to an IStructuredSelection, whose first
		// // element should be a VizResource.
		// if (part.getSite().getId().equals(VizFileViewer.ID)
		// && selection != null
		// && selection instanceof IStructuredSelection) {
		// IStructuredSelection structuredSelection = (IStructuredSelection)
		// selection;
		// if (!structuredSelection.isEmpty()) {
		// Object object = structuredSelection.getFirstElement();
		//
		// if (object instanceof VizResource) {
		// VizResource resource = (VizResource) object;
		// String fileName = "";
		//
		// if (resource.getFileSet() != null
		// && resource.getFileSetTitle() != null) {
		// fileName = resource.getFileSetTitle();
		// } else {
		// fileName = resource.getContents().getAbsolutePath();
		// }
		//
		// // Create a CSVDataLoader and CSVDataProvider if the
		// // selection is a .csv file set
		// CSVDataProvider newDataProvider = null;
		// CSVDataLoader newCSVDataLoader = null;
		//
		// // Handle a CSV file
		// if (fileName.matches(".*\\.csv$")) {
		//
		// // Initialize the CSVDataLoader
		// newCSVDataLoader = new CSVDataLoader();
		//
		// // Create the CSVDataProvider from a file set
		// if (resource.getFileSet() != null
		// && resource.getFileSetTitle() != null) {
		//
		// // Initialize the CSVDataProvider
		// newDataProvider = newCSVDataLoader
		// .loadAsFileSet(resource.getFileSet());
		//
		// // Set the source as the file name
		// newDataProvider.setSource(resource
		// .getFileSetTitle());
		// }
		//
		// // Or create the CSVDataProvider from a file
		// else {
		//
		// // Initialize the CSVDataProvider
		// newDataProvider = newCSVDataLoader.load(fileName);
		//
		// // Set the source as the file name
		// File file = resource.getContents();
		// newDataProvider.setSource(file.getName());
		// }
		// } else {
		// return;
		// }
		//
		// // Refresh this view based on the selection
		// refreshDataTable(newDataProvider);
		// }
		// }
		// }

		return;
	}

	/**
	 * This function creates a new data table in the CSV Data Table Viewer to
	 * display the new provider.
	 * 
	 * @param newDataProvider
	 *            The CSVDataProvider to populate the view
	 */
	public void refreshDataTable(CSVDataProvider newDataProvider) {

		System.out.println("CSVDataTableViewer Message: Refreshing the data "
				+ "table: " + newDataProvider.getSourceInfo());

		// Iterates through the times
		for (Double time : newDataProvider.getTimes()) {

			// Sets the time for the provider
			newDataProvider.setTime(time);

			// FIXME We need a better check for existing tabs
			if (!tabs.contains(time.toString())) {
				// Create a new tab of the time for the loaded file
				TabItem newTimeTab = new TabItem(timesTabFolder, SWT.NONE);
				newTimeTab.setText(time.toString());

				// Add to the list of the existing tabs
				tabs.add(time.toString());

				// Get the independent variables and features from the
				// new data provider to be used to set up the columns
				ArrayList<String> independentVarsAndFeatures = new ArrayList<String>();
				independentVarsAndFeatures.addAll(newDataProvider
						.getIndependentVariables());
				independentVarsAndFeatures.addAll(newDataProvider
						.getFeaturesAtCurrentTime());
				// Get the number of columns
				int numColumns = independentVarsAndFeatures.size();

				// Creating the composite for the table viewer
				Composite tableViewerComp = new Composite(timesTabFolder,
						SWT.BORDER);
				tableViewerComp.setLayout(new GridLayout(1, false));
				tableViewerComp.setLayoutData(new GridData(SWT.DEFAULT,
						SWT.DEFAULT, true, true));
				// Sets the control of the tab to the composite for the table
				// view
				newTimeTab.setControl(tableViewerComp);

				// Creating the table viewer
				TableViewer viewer = new TableViewer(tableViewerComp,
						SWT.BORDER | SWT.FULL_SELECTION);
				// Creating the layout of columns
				TableLayout layout = new TableLayout();
				for (int k = 0; k < numColumns; k++) {
					layout.addColumnData(new ColumnWeightData(2,
							ColumnWeightData.MINIMUM_WIDTH, true));
				}
				viewer.getTable().setLayout(layout);
				// Setting the layout of the table
				viewer.getControl().setLayoutData(
						new GridData(SWT.FILL, SWT.FILL, true, true));
				// Showing the headers
				viewer.getTable().setLinesVisible(true);
				viewer.getTable().setHeaderVisible(true);
				// Set the content providers
				viewer.setContentProvider(new DataTableContentProvider());
				viewer.setLabelProvider(new DataTableLabelProvider());

				// Setting the column text
				for (String columnText : independentVarsAndFeatures) {
					TableColumn newColumn = new TableColumn(viewer.getTable(),
							SWT.CENTER);
					newColumn.setText(columnText);
				}
				// Setting the input
				viewer.setInput(newDataProvider);

				// Refreshing the composite so the table will show
				tableViewerComp.layout();
				timesTabFolder.layout();
			}
		}

		return;
	}

}
