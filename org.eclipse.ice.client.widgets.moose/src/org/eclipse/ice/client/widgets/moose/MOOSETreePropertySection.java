/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   Robert Smith, Jordan Deyton - bug 474744
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.client.common.properties.CellColumnLabelProvider;
import org.eclipse.ice.client.common.properties.DescriptionCellContentProvider;
import org.eclipse.ice.client.common.properties.ICellContentProvider;
import org.eclipse.ice.client.common.properties.TextCellEditingSupport;
import org.eclipse.ice.client.common.properties.TreePropertyContentProvider;
import org.eclipse.ice.client.widgets.TreePropertySection;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * This class extends the {@link TreePropertySection} to provide additional
 * property-related features not available in the default table of tree
 * properties.
 * <p>
 * Additional features include:
 * </p>
 * <ul>
 * <li>A column for enabling/disabling the <code>Entry</code> (disabling
 * comments them out in the input file).</li>
 * <li>A column for the <code>Entry</code> description or comments.</li>
 * </ul>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class MOOSETreePropertySection extends TreePropertySection {

	/**
	 * A placeholder column to contain the Checkboxes in the
	 * <code>TableViewer</code>.
	 */
	private TableViewerColumn checkColumn;
	/**
	 * The <code>TableViewer</code>'s column for a property or parameter's
	 * description.
	 */
	private TableViewerColumn descriptionColumn;

	/**
	 * In addition to the default refresh, this packs the additional columns.
	 */
	@Override
	public void refresh() {

		// Perform the usual refresh operation.
		super.refresh();

		// Automatically adjust the widths of the additional columns.
		if (descriptionColumn != null) {
			checkColumn.getColumn().pack();
			descriptionColumn.getColumn().pack();
		}

		return;
	}

	/**
	 * In addition to the default dispose operation, this disposes any resources
	 * used by this sub-class.
	 */
	@Override
	public void dispose() {
		super.dispose();

		// Clear references to all widgets. (These are already disposed by
		// disposing the section in the super dispose method.)
		checkColumn = null;
		descriptionColumn = null;

		return;
	}

	/**
	 * Adds the default and additional columns to the provided
	 * <code>TableViewer</code>. This should only be invoked on
	 * CheckboxTableViewers.
	 */
	@Override
	protected void addTableViewerColumns(TableViewer tableViewer) {
		if (tableViewer != null) {
			TableColumn column;
			ICellContentProvider contentProvider;

			// // ---- Create the placeholder CheckBox column. ---- //
			checkColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			column = checkColumn.getColumn();
			column.setText("Enabled");
			column.setToolTipText("If checked, the parameter will be written "
					+ "to the input file.\n" + "If unchecked, the parameter "
					+ "will be commented out in the input file.");
			column.setResizable(true);

			// Create the check state manager. It also functions as the cell
			// content provider for the first column.
			TableCheckStateManager checkStateManager = new TableCheckStateManager(
					tableViewer) {
				@Override
				public boolean setValue(Object element, Object value) {
					boolean changed = super.setValue(element, value);
					// If the value changed, mark the associated ICEFormEditor
					// as dirty.
					if (changed && getFormEditor() != null) {
						getFormEditor().setDirty(true);
					}
					return changed;
				}
			};

			// Create a MOOSECheckStateProvider which sets the FormEditor as
			// dirty when the checkbox's value is changed.
			contentProvider = checkStateManager;

			// Add a blank label provider. Nothing should appear in the first
			// column except for the checkboxes.
			checkColumn.setLabelProvider(
					new CellColumnLabelProvider(contentProvider));

			// Set the content provider and listener for the CheckBox column
			CheckboxTableViewer checkedTableViewer = (CheckboxTableViewer) tableViewer;
			checkedTableViewer.setCheckStateProvider(checkStateManager);
			checkedTableViewer.addCheckStateListener(checkStateManager);

			// Create the default columns.
			super.addTableViewerColumns(tableViewer);

			// ---- Create the description/comments column. ---- //
			descriptionColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			column = descriptionColumn.getColumn();
			column.setText("Comments");
			column.setToolTipText("Comments about the property. These are "
					+ "stored as comments in the input file.");
			column.setResizable(true);
			// Create an ICellContentProvider for a column that shows the
			// descriptions of Entries. Then hook it up as a label provider and
			// for editing support.
			contentProvider = new DescriptionCellContentProvider() {
				@Override
				public boolean setValue(Object element, Object value) {
					boolean changed = super.setValue(element, value);
					// If the value changed, mark the associated ICEFormEditor
					// as dirty.
					if (changed && getFormEditor() != null) {
						getFormEditor().setDirty(true);
					}
					return changed;
				}
			};
			descriptionColumn.setLabelProvider(
					new CellColumnLabelProvider(contentProvider));
			descriptionColumn.setEditingSupport(
					new TextCellEditingSupport(tableViewer, contentProvider));
			// ------------------------------------------------- //
		}

		return;
	}

	/**
	 * Creates the table that displays properties for viewing and editing.
	 * 
	 * @param client
	 *            The client <code>Composite</code> that should contain the
	 *            table of properties.
	 * @return The <code>TableViewer</code> for the table of properties.
	 */
	@Override
	protected TableViewer createTableViewer(Composite client) {

		CheckboxTableViewer tableViewer = null;

		if (client != null) {
			Table table;

			// Create the TableViewer and the underlying Table Control.
			tableViewer = CheckboxTableViewer.newCheckList(client,
					SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
			// Set some properties for the table.
			table = tableViewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);

			// Set up the content provider for the table viewer. Now the table
			// viewer's input can be set.
			tableViewer.setContentProvider(new TreePropertyContentProvider());

			// Enable tool tips for the Table's cells.
			ColumnViewerToolTipSupport.enableFor(tableViewer,
					ToolTip.NO_RECREATE);

			// Populate the TableViewer with its columns.
			addTableViewerColumns(tableViewer);
		}

		return tableViewer;
	}
}
