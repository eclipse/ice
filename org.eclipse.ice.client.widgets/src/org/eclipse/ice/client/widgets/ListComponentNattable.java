/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import java.util.Iterator;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionProvider;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * Displays the information contained in a ListComponent as a Nattable.
 * 
 * 
 * @author Jay Jay Billings, Kasper Gammeltoft
 * 
 */
public class ListComponentNattable {

	/**
	 * The Composite will act as a parent where the Nattable will be drawn.
	 * 
	 */
	private Composite sectionClient;

	/**
	 * The ListComponent is the data input for the Nattable to use.
	 * 
	 */
	private ListComponent list;

	/**
	 * Holds the selected list components.
	 */
	private ListComponent selectedList;

	/**
	 * The table to hold the list data.
	 */
	private NatTable table;

	/**
	 * Provides the information about the selection layer for the NatTable.
	 * Gets/Sets the selected rows for the table.
	 */
	private RowSelectionProvider selectionProvider;

	/**
	 * If the NatTable is editable or not (from the user's side). If false, the
	 * user will only be able to select table cells, if true then the user will
	 * be able to change the table's values.
	 */
	private boolean canEdit;

	private boolean percentResize;

	/**
	 * Constructor, needs the parent Composite and the List for data. This has
	 * the column percent resizing for the table automatically turned on. You
	 * must use the constructor with that explicit variable if you do not want
	 * column width resizing to fit the parent Composite.
	 * 
	 * @param parent
	 *            The Composite to be used as a parent Shell or View.
	 * @param listComponent
	 *            The ListComponent to be used as list data for the Nattable
	 * @param editable
	 *            A boolean representing if the table is editable by the user
	 */
	public ListComponentNattable(Composite parent, ListComponent listComponent,
			boolean editable) {
		sectionClient = parent;
		list = listComponent;
		selectedList = new ListComponent();
		canEdit = editable;
		percentResize = true;
		createTable();
	}

	/**
	 * Constructor, needs the parent Composite and the List for data
	 * 
	 * @param parent
	 *            The Composite to be used as a parent Shell or View.
	 * @param listComponent
	 *            The ListComponent to be used as list data for the Nattable
	 * @param editable
	 *            A boolean representing if the table is editable by the user
	 * @param sizeForParent
	 *            A boolean representing if the table should take the size of
	 *            its parent or maintain its preferred size and have scroll bars
	 *            or unfilled space instead. Only effects column width.
	 */
	public ListComponentNattable(Composite parent, ListComponent listComponent,
			boolean editable, boolean sizeForParent) {
		sectionClient = parent;
		list = listComponent;
		selectedList = new ListComponent();
		canEdit = editable;
		percentResize = sizeForParent;
		createTable();
	}

	/**
	 * This operation configures the NatTable used to render the ListComponent
	 * on the screen.
	 */
	private void createTable() {

		// Create the data layer of the table
		IColumnPropertyAccessor accessor = new ListComponentColumnPropertyAccessor(
				list);
		IDataProvider dataProvider = new ListDataProvider(list, accessor);
		DataLayer dataLayer = new DataLayer(dataProvider);
		GlazedListsEventLayer eventLayer = new GlazedListsEventLayer(dataLayer,
				list);

		// If the table's columns should autoresize their widths to fill the
		// parent Composite.
		dataLayer.setColumnPercentageSizing(percentResize);

		// Create the selection and viewport layers of the table
		SelectionLayer selectionLayer = new SelectionLayer(eventLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// Get the column names
		String[] columnNames = new String[list.getColumnCount()];
		for (int i = 0; i < list.getColumnCount(); i++) {
			columnNames[i] = accessor.getColumnProperty(i);
		}

		// Create the column header layer (column names) of the table
		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(
				columnNames);
		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(
				columnHeaderDataProvider);
		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer,
				viewportLayer, selectionLayer);
		// Turn the column labels on by default
		columnHeaderDataLayer
				.setConfigLabelAccumulator(new ColumnLabelAccumulator());

		// Create the row header layer (row names) of the table
		IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(
				dataProvider);

		DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(
				rowHeaderDataProvider);
		ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer,
				viewportLayer, selectionLayer);

		// Create the corner layer of the table
		IDataProvider cornerDataProvider = new DefaultCornerDataProvider(
				columnHeaderDataProvider, rowHeaderDataProvider);
		DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
		ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer,
				columnHeaderLayer);

		// Create the grid layer and the table
		GridLayer gridLayer = new GridLayer(viewportLayer, columnHeaderLayer,
				rowHeaderLayer, cornerLayer);
		NatTable natTable = new NatTable(sectionClient, gridLayer, false);
		ConfigRegistry configRegistry = new ConfigRegistry();
		natTable.setConfigRegistry(configRegistry);
		// Set the default table style
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

		// Make the table editable by updating the configuration rules
		natTable.addConfiguration(new AbstractRegistryConfiguration() {
			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				// only allow editing if the user can edit.
				if (canEdit) {
					configRegistry.registerConfigAttribute(
							EditConfigAttributes.CELL_EDITABLE_RULE,
							IEditableRule.ALWAYS_EDITABLE);
				} else {
					configRegistry.registerConfigAttribute(
							EditConfigAttributes.CELL_EDITABLE_RULE,
							IEditableRule.NEVER_EDITABLE);
				}
			}
		});

		// Configure the table (lay it out)
		natTable.configure();
		natTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		// Setting table instance variable
		table = natTable;

		// Create a new selectionProvider to listen to selection events.
		selectionProvider = new RowSelectionProvider(selectionLayer,
				(IRowDataProvider) dataProvider, false);
		// Add the listener
		selectionProvider
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent e) {

						// Get the selection and add the selected objects to a
						// ListComponent for reference.
						IStructuredSelection selection = (IStructuredSelection) e
								.getSelection();
						selectedList.clear();
						Iterator it = selection.iterator();
						while (it.hasNext()) {
							selectedList.add(it.next());
						}
					}
				});

		return;
	}

	/**
	 * Gets the currently selected elements.
	 * 
	 * @return
	 */
	public ListComponent getSelectedObjects() {
		return selectedList;

	}

	/**
	 * Gets the row selection provider so that another class could potentially
	 * listen for selection events in the table.
	 * 
	 * @return
	 */
	public RowSelectionProvider getSelectionProvider() {
		return selectionProvider;
	}

	/**
	 * Gets the SWT.COLOR of the current background for the table. By default is
	 * a light gray
	 * 
	 * @return Color The background color.
	 */
	public Color getBackground() {
		return table.getBackground();
	}

	/**
	 * Sets the elements to be selected for this table.
	 * 
	 * @param elements
	 */
	public void setSelection(ListComponent elements) {
		StructuredSelection newSelection = new StructuredSelection(elements);
		selectionProvider.setSelection(newSelection);
	}

	/**
	 * Gets the preferred width of the table.
	 * 
	 * @return int The preferred width
	 */
	public int getPreferredWidth() {
		return table.getPreferredWidth();
	}

	/**
	 * Gets the preferred height of the table.
	 * 
	 * @return int The preferred height
	 */
	public int getPreferredHeight() {
		return table.getPreferredHeight();
	}

	/**
	 * Gets the ListComponent that this Nattable uses for data.
	 * 
	 * @return
	 */
	public ListComponent getList() {
		return list;
	}

	/**
	 * Gets the NatTable
	 * 
	 * @return
	 */
	public NatTable getTable() {
		return table;
	}

}