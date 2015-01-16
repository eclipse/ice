package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

public class TableComponentComposite extends Composite {

	/**
	 * The current {@code TableComponent} data model that is rendered in the
	 * {@link #tableViewer}.
	 */
	private TableComponent tableComponent;

	/**
	 * The JFace {@code Viewer} that displays the {@link #tableComponent}'s rows
	 * as rows in an SWT {@link Table}.
	 */
	private TableViewer tableViewer;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public TableComponentComposite(Composite parent, int style) {
		super(parent, style);

		// TODO Add add/remove/wizard buttons and change the layout.
		// For now, use a FillLayout.
		setLayout(new FillLayout());

		// Create the TableViewer.
		tableViewer = createTableViewer(this);

		return;
	}

	/**
	 * Sets the current {@code TableComponent} data model that is rendered in
	 * the {@link #tableViewer}.
	 * 
	 * @param table
	 *            The new {@code TableComponent}. This value must not be
	 *            {@code null} and must have a row template.
	 */
	public void setTableComponent(TableComponent table) {

		// We cannot handle TableComponents with no template set.
		if (table == null || table.getRowTemplate() == null) {
			return;
		}
		// Otherwise, we should be able to add rows based on the set template.
		tableComponent = table;

		// Update the viewer's input.
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewer.setInput(tableComponent);
			}
		});

		return;
	}

	/**
	 * Gets the current {@code TableComponent} data model that is rendered in
	 * the {@link #tableViewer}.
	 * 
	 * @return The current {@code TableComponent}.
	 */
	public TableComponent getTableComponent() {
		return tableComponent;
	}

	/**
	 * Creates a new {@code TableViewer} to show a {@link TableComponent}.
	 * 
	 * @param parent
	 *            The containing {@code Composite}.
	 * @return The created JFace {@code TableViewer}.
	 */
	private TableViewer createTableViewer(Composite parent) {
		// Create the TableViewer and the underlying Table Control.
		TableViewer viewer = new TableViewer(this, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		// Set some properties for the table.
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Set up the content provider for the viewer. Now the viewer's input
		// can be set.
		viewer.setContentProvider(new TableComponentContentProvider());

		// Enable tool tips for the Table's cells.
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.NO_RECREATE);

		return viewer;
	}

}
