package org.eclipse.ice.client.widgets;

import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
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
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
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
	 * Constructor, needs the parent Composite and the List for data
	 * 
	 * @param parent
	 * 				The Composite to be used as a parent Shell or View.
	 * @param listComponent
	 * 				The ListComponent to be used as list data for the Nattable
	 */
	public ListComponentNattable(Composite parent, ListComponent listComponent){
		sectionClient = parent;
		list = listComponent;
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

		// Create the selection and viewport layers of the table
		SelectionLayer selectionLayer = new SelectionLayer(eventLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// Get the column names
		String[] columnNames = new String[list.getColumnCount()];
		for (int i = 0; i < list.getColumnCount(); i++) {
			columnNames[i] = list.getColumnName(i);
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
				configRegistry.registerConfigAttribute(
						EditConfigAttributes.CELL_EDITABLE_RULE,
						IEditableRule.ALWAYS_EDITABLE);
			}
		});

		// Configure the table (lay it out)
		natTable.configure();
		natTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		return;
	}
	
	
	/**
	 * Sets a new list to be used for the table data.
	 * @param listComponent
	 * 				A ListComponent object for the Nattable's data.
	 */
	public void setList(ListComponent listComponent){
		list = listComponent;
	}
	
	
	/**
	 * Gets the ListComponent that this Nattable uses for data.
	 * @return
	 */
	public ListComponent getList(){
		return list;
	}
	
	
	/**
	 * Sets a new parent Composite used to draw the Nattable
	 * @param parent
	 * 			A Composite object
	 */
	public void setComposite(Composite parent){
		sectionClient = parent;
	}
	
}
