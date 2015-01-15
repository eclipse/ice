package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.ice.client.widgets.properties.CellColumnLabelProvider;
import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.ice.client.widgets.properties.TextCellEditingSupport;
import org.eclipse.ice.viz.service.visit.ConnectionPreference;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * This {@code Composite} renders a table of {@link Connection}s contained by a
 * {@link ConnectionManager}, along with the requisite buttons and actions to
 * add or remove valid {@code Connection}s to/from the manager.
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionComposite extends Composite {

	/**
	 * The JFace {@code TableViewer} that contains the {@link #manager}'s
	 * {@code Connection}s.
	 */
	private TableViewer tableViewer;

	/**
	 * The manager rendered in the {@link #tableViewer}.
	 */
	private ConnectionManager manager;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public ConnectionComposite(Composite parent, int style) {
		super(parent, style);

		// Make sure we always have a valid ConnectionManager.
		manager = new ConnectionManager();

		// TODO Add the add/remove/wizard buttons and lay everything out
		// properly. For now, just use a FillLayout since all we have is the
		// table.
		setLayout(new FillLayout());

		// Create the TableViewer.
		tableViewer = createTableViewer();
		addTableViewerColumns(tableViewer);

		return;
	}

	/**
	 * Sets a new {@link ConnectionManager} to show in the custom connection
	 * {@link #tableViewer}.
	 * 
	 * @param manager
	 *            The new manager. If {@code null} or the same as before,
	 *            nothing changes.
	 */
	public void setConnectionManager(final ConnectionManager manager) {
		if (manager != null && manager != this.manager) {
			this.manager = manager;

			// Update the TableViewer on the UI thread.
//			Display.getCurrent().asyncExec(new Runnable() {
//				@Override
//				public void run() {
					tableViewer.setInput(manager);
					tableViewer.refresh();
//				}
//			});
		}

		return;
	}

	/**
	 * Creates (but does not set) the {@link #tableViewer}.
	 * 
	 * @return A new {@code TableViewer}.
	 */
	private TableViewer createTableViewer() {
		
		// Create the TableViewer and the underlying Table Control.
		TableViewer viewer = new TableViewer(this, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		// Set some properties for the table.
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Set up the content provider for the viewer. Now the viewer's input
		// can be set.
		viewer.setContentProvider(new ConnectionContentProvider());

		// Enable tool tips for the Table's cells.
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.NO_RECREATE);

		return viewer;
	}

	// TODO We may just be able to use a TableComponent...
	
	/**
	 * Adds columns to the specified {@code TableViewer}.
	 * 
	 * @param tableViewer
	 *            The {@code TableViewer} that needs columns.
	 */
	private void addTableViewerColumns(TableViewer tableViewer) {
		
		TableColumn column;
		ICellContentProvider contentProvider;
		
		TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
		column = idColumn.getColumn();
		column.setText("Connection Name");
		column.setToolTipText(ConnectionPreference.ConnectionID.getDescription());
		column.setResizable(true);
		contentProvider = new ConnectionCellContentProvider();
		idColumn.setLabelProvider(new CellColumnLabelProvider(contentProvider));
		idColumn.setEditingSupport(new TextCellEditingSupport(tableViewer,
				contentProvider));

		column.pack();
		
	}
}
