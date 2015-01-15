package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

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
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {
					tableViewer.setInput(manager);
					tableViewer.refresh();
				}
			});
		}

		return;
	}

	/**
	 * Creates (but does not set) the {@link #tableViewer}.
	 * 
	 * @return A new {@code TableViewer}.
	 */
	private TableViewer createTableViewer() {
		TableViewer viewer = new TableViewer(this);
		viewer.setContentProvider(new ConnectionContentProvider());
		return viewer;
	}

	/**
	 * Adds columns to the specified {@code TableViewer}.
	 * 
	 * @param tableViewer
	 *            The {@code TableViewer} that needs columns.
	 */
	private void addTableViewerColumns(TableViewer tableViewer) {
		// TODO
	}
}
