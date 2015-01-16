package org.eclipse.ice.viz.service.visit.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.properties.CellColumnLabelProvider;
import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

/**
 * This class provides a basic JFace {@link IStructuredContentProvider} for ICE
 * {@link TableComponent}s. It automatically registers for updates from the
 * input {@code TableComponent} and refreshes the associated JFace
 * {@link Viewer} when the {@code TableComponent} changes.
 * 
 * @author Jordan Deyton
 *
 */
public class TableComponentContentProvider implements
		IStructuredContentProvider, IUpdateableListener {

	/**
	 * The data model for the {@link #viewer}.
	 */
	private TableComponent tableComponent;

	/**
	 * The JFace {@code TableViewer} that shows the contents of the
	 * {@link #tableComponent}.
	 */
	private TableViewer viewer;

	/**
	 * A list to keep track of the current row template. If the row template
	 * changes, then the viewer's columns will need to be recreated.
	 */
	private List<Entry> rowTemplate;

	/**
	 * The current {@link TableViewerColumn}s inside the {@link #viewer}.
	 */
	private final List<TableViewerColumn> columns = new ArrayList<TableViewerColumn>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// Nothing to do yet.
	}

	/**
	 * This method expects a new {@link #tableComponent} as input. This content
	 * provider will register for updates from the {@code TableComponent} and
	 * update the {@link #viewer} when the data model changes.
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (viewer != null && viewer instanceof TableViewer && newInput != null) {

			// Update the reference to the JFace Viewer.
			this.viewer = (TableViewer) viewer;
			// Unregister from the old input if necessary.
			if (tableComponent != null) {
				tableComponent.unregister(this);
			}
			// Set the new input and register for model updates so that the
			// viewer will be automatically updated later.
			if (newInput instanceof TableComponent) {
				tableComponent = (TableComponent) newInput;
				// TableComponents sent to this content provider should have a
				// template set!
				if (tableComponent.getRowTemplate() == null) {
					throw new NullPointerException(
							"TableComponentContentProvider error: "
									+ "Cannot render TableComponents with no row template.");
				}
				// Before registering, trigger an update to the viewer.
				if (!this.viewer.isBusy()) {
					update(tableComponent);
				}
				tableComponent.register(this);
			}
		}

		return;
	}

	/**
	 * For the root element (the {@link #tableComponent}), this method returns
	 * the rows in the table. For each row, the {@link Entry} instances in the
	 * row are returned.
	 * <p>
	 * All other input elements are ignored.
	 * </p>
	 */
	@Override
	public Object[] getElements(Object inputElement) {

		Object[] elements;

		// Handle the TableComponent.
		if (inputElement == tableComponent) {
			// To get the rows, we have to get the IDs and then query the
			// TableComponent for each row ID.
			List<Integer> ids = tableComponent.getRowIds();
			int size = ids.size();
			elements = new Object[size];
			for (int i = 0; i < size; i++) {
				elements[i] = tableComponent.getRow(i);
			}
		}
		// Return an empty array for anything else.
		else {
			elements = new Object[] {};
		}

		return elements;
	}

	/**
	 * This listens for changes to the {@link #tableComponent} and updates the
	 * {@link #viewer} as necessary.
	 */
	@Override
	public void update(IUpdateable component) {
		// Update the viewer since the underlying data has changed

		if (component != null && component == tableComponent) {

			// The viewer update will depend on whether the row template
			// changed.
			final boolean columnsChanged = updateRowTemplate();

			// We must use the UI Thread
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					// If the columns did not change, we only need to refresh
					// the viewer.
					if (!columnsChanged) {
						viewer.refresh();
					}
					// If the columns changed, we need to refresh the columns.
					else {
						refreshTableColumns();
					}
				}
			});
		}

		return;
	}

	/**
	 * Updates {@link #rowTemplate} with on the current {@link #tableComponent}.
	 * 
	 * @return True if the row template changed (in which case the
	 *         {@link #viewer} will need to be re-structured), false otherwise.
	 */
	private boolean updateRowTemplate() {
		boolean changed = false;

		// Get the new row template and check that it is set. TableComponents
		// sent to this content provider should have a template set!
		List<Entry> newRowTemplate = tableComponent.getRowTemplate();

		if (!newRowTemplate.equals(rowTemplate)) {
			changed = true;
			rowTemplate = newRowTemplate;
		}

		return changed;
	}

	/**
	 * Refreshes the {@link #viewer}'s columns based on the current row
	 * template.
	 * <p>
	 * <b>Note:</b> This method should be called on the UI thread!
	 * </p>
	 */
	private void refreshTableColumns() {

		// Remove all previous columns from the viewer.
		for (TableViewerColumn column : columns) {
			column.getColumn().dispose();
		}
		columns.clear();

		EntryCellContentProvider basicContentProvider = new EntryCellContentProvider();

		// Add a new column for each Entry.
		for (int i = 0; i < rowTemplate.size(); i++) {
			Entry entry = rowTemplate.get(i);

			// Create the column for the TableViewer.
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
			columns.add(column);

			// Customize the underlying Column widget.
			TableColumn columnWidget = column.getColumn();
			columnWidget.setText(entry.getName());
			columnWidget.setToolTipText(entry.getDescription());
			columnWidget.setResizable(true);

			ICellContentProvider contentProvider = new ListCellContentProvider(
					basicContentProvider, i);
			column.setLabelProvider(new CellColumnLabelProvider(contentProvider));
			// TODO EditingSupport
		}

		// Refresh the viewer and re-adjust the widths of the columns.
		viewer.refresh();
		for (TableViewerColumn column : columns) {
			column.getColumn().pack();
		}

		return;
	}

}
