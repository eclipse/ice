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
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.client.widgets.properties.DescriptionCellContentProvider;
import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.ice.client.widgets.properties.CellColumnLabelProvider;
import org.eclipse.ice.client.widgets.properties.TextCellEditingSupport;
import org.eclipse.ice.client.widgets.properties.TreePropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

/**
 * This class extends the {@link TreePropertySection} to provide additional
 * property-related features not available in the default table of tree
 * properties.
 * <p>
 * Additional features include:
 * <ul>
 * <li>A column for enabling/disabling the <code>Entry</code> (disabling
 * comments them out in the input file).</li>
 * <li>A column for the <code>Entry</code> description or comments.</li>
 * </ul>
 * </p>
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
	 * <code>TableViewer</code>.
	 */
	@Override
	protected void addTableViewerColumns(TableViewer tableViewer) {

		if (tableViewer != null) {
			TableColumn column;
			ICellContentProvider contentProvider;

			// ---- Create the placeholder CheckBox column. ---- //
			checkColumn = new TableViewerColumn(tableViewer, SWT.CENTER);
			column = checkColumn.getColumn();
			column.setText("Enabled");
			column.setToolTipText("If checked, the parameter will be written "
					+ "to the input file.\n" + "If unchecked, the parameter "
					+ "will be commented out in the input file.");
			column.setResizable(true);

			// Create an ICellContentProvider for a column that should have a
			// checkbox in each cell. Then hook it up to the column as a label
			// provider and editing support.
			contentProvider = new CheckboxCellContentProvider(tableViewer) {
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
			checkColumn.setLabelProvider(new CheckboxCellLabelProvider(
					contentProvider));
			checkColumn.setEditingSupport(new CheckboxCellEditingSupport(
					tableViewer, contentProvider));
			// ------------------------------------------------- //

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
			descriptionColumn.setLabelProvider(new CellColumnLabelProvider(
					contentProvider));
			descriptionColumn.setEditingSupport(new TextCellEditingSupport(
					tableViewer, contentProvider));
			// ------------------------------------------------- //
		}

		return;
	}
}
