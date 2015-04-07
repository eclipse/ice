package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.swt.widgets.Composite;

/**
 * This class is used to render a mesh file based on the preferred visualization
 * service. The file is pulled from an {@link Entry}.
 * 
 * @author Jordan Deyton
 *
 */
public class MeshViewComposite extends Composite implements IUpdateableListener {

	// We have four states:
	// 1 - no file specified, prompt the user
	// 2 - specified file could not be found, prompt the user
	// 3 - file found, could not open, prompt the user
	// 4 - file found, everything works

	/**
	 * The current {@code Entry} pointing to a potentially renderable mesh file.
	 */
	private Entry fileEntry = null;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public MeshViewComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Sets the current {@code Entry} whose value is treated as a file that
	 * should be rendered via the preferred visualization service.
	 * <p>
	 * <b>Note:</b> When the {@code Entry}'s value changes, this view will
	 * automatically update accordingly.
	 * </p>
	 * 
	 * @param fileEntry
	 *            The new fileEntry. This value has different effects:
	 *            <ul>
	 *            <li><b>If it is the same {@code Entry},</b> nothing is done.</li>
	 *            <li><b>If it is {@code null},</b> the view is cleared.</li>
	 *            <li><b>If the file cannot be found,</b> the user is prompted
	 *            to locate the file.</li>
	 *            <li><b>If the file is invalid,</b> the user is prompted to
	 *            locate a new file.</li>
	 *            <li><b>If the file is valid,</b> the mesh is displayed.</li>
	 *            </ul>
	 */
	public void setFileEntry(Entry fileEntry) {
		if (fileEntry != this.fileEntry) {

			// If we are registered for updates from the current file Entry, we
			// should unregister from those updates and update the reference to
			// the registered file Entry.
			if (this.fileEntry != null) {
				this.fileEntry.unregister(this);
			}
			this.fileEntry = fileEntry;

			// Determine the file pointed to by the new Entry.
			findFile(fileEntry);
			// Update the visualization.
			updateVisualization();

			// Register for updates in case the Entry's value changes.
			fileEntry.register(this);
		}

		return;
	}

	/**
	 * Finds the file associated with the specified {@code Entry}.
	 * 
	 * @param entry
	 *            A reference to the file {@code Entry}. This value is not
	 *            checked for null.
	 */
	private void findFile(Entry entry) {
		AllowedValueType type = entry.getValueType();
		if (type == AllowedValueType.File) {
			// TODO
			System.out.println("Found a file name from a file Entry: \""
					+ entry.getValue() + "\"");
		} else {
			// TODO
			System.out
					.println("Found a possible file name from a generic Entry: \""
							+ entry.getValue() + "\"");
		}
		return;
	}

	/**
	 * Updates the view based on the currently preferred and compatible
	 * visualization service and the current {@link #fileEntry}.
	 */
	private void updateVisualization() {
		// TODO This method will need an argument pointing to a file or
		// resource... not sure which yet.
	}

	/**
	 * Listens for changes to the {@link #fileEntry}'s current value, which is
	 * expected to be a file name or path.
	 * 
	 * @param component
	 *            The component. This should be the current {@link #fileEntry}.
	 */
	@Override
	public void update(IUpdateable component) {
		if (component == fileEntry) {

			// Determine the file now pointed to by this Entry.
			findFile((Entry) component);
			// Update the visualization.
			updateVisualization();
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		// We need to unregister as a listener from the current file Entry.
		if (fileEntry != null) {
			fileEntry.unregister(this);
		}

		fileEntry = null;
		// TODO Unset any other class variables.

		return;
	}

}
