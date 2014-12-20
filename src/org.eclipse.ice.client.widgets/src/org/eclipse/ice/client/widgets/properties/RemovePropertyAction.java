package org.eclipse.ice.client.widgets.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

// TODO class documentation
public class RemovePropertyAction extends Action implements
		ISelectionChangedListener {

	/**
	 * A list of all selected {@link TreeProperty} instances. This list is
	 * re-populated on selection change events.
	 */
	private final List<TreeProperty> selectedProperties = new ArrayList<TreeProperty>();

	/**
	 * The default constructor.
	 */
	public RemovePropertyAction() {
		setText("Remove");
		setToolTipText("Removes all selected parameters.");

		// TODO Decorate this action.

		// Disable the action initially.
		setEnabled(false);
	}

	/**
	 * Deletes all {@link TreeProperty} instances from the parent data node
	 * based on the selection from the associated selection provider.
	 */
	@Override
	public void run() {

		// Loop over all selected properties. If a property can be deleted,
		// remove it from its parent data node.
		for (TreeProperty selectedProperty : selectedProperties) {
			if (canDelete(selectedProperty)) {
				String entryName = selectedProperty.getEntry().getName();
				selectedProperty.getDataNode().deleteEntry(entryName);
			}
		}
		// Clear all of the known selected properties.
		selectedProperties.clear();
		setEnabled(false);

		return;
	}

	/**
	 * Sets the current tree from which associated {@link TreeProperty}
	 * instances can be removed.
	 * 
	 * @param tree
	 *            The parent tree. If null, the action is disabled.
	 */
	public void setTree(TreeComposite tree) {
		// Clear all of the known selected properties.
		selectedProperties.clear();
		setEnabled(false);
	}

	/**
	 * Gets whether or not the specified <code>Entry</code> (aka property or
	 * parameter) can be deleted from its containing <code>DataComponent</code>
	 * or data node in the {@link #tree}. Any parameter can be deleted.
	 * 
	 * @param property
	 *            The property or parameter that is a candidate for deletion.
	 * @return True if the property can be deleted, false otherwise.
	 */
	private boolean canDelete(TreeProperty property) {
		return (property != null && !property.getEntry().isRequired());
	}

	/**
	 * Enables or disables the action based on the selection event. If the
	 * selection contains an Entry that can be deleted, then the action will be
	 * enabled. Otherwise, it will be disabled.
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		// Remove all currently selected properties.
		selectedProperties.clear();
		// We need to determine whether the action should be enabled/disabled.
		// The action should only be enabled if the selection includes deletable
		// TreeProperties.
		boolean enabled = false;
		// First, we have to check that the selection is non-empty.
		ISelection selection = event.getSelection();
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			// Loop over all selected elements.
			Iterator<?> iterator = ((IStructuredSelection) selection)
					.iterator();
			while (iterator.hasNext()) {
				Object element = iterator.next();
				// Throw any selected TreeProperty into the list.
				if (element instanceof TreeProperty) {
					TreeProperty property = (TreeProperty) element;
					selectedProperties.add(property);
					// Update the enabled flag if the property is deletable.
					if (canDelete(property)) {
						enabled = true;
					}
				}
			}
		}
		// Enable/disable the action.
		setEnabled(enabled);

		return;
	}
}