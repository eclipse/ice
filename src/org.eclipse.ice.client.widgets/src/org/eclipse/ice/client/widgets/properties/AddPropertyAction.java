package org.eclipse.ice.client.widgets.properties;

import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.action.Action;

// TODO Class documentation
public class AddPropertyAction extends Action {

	/**
	 * The current {@link TreeComposite} to which properties (in the form of
	 * {@link Entry}s) can be added.
	 */
	private TreeComposite tree;

	/**
	 * The default new property name.
	 */
	private static final String newPropertyName = "new_parameter";

	public AddPropertyAction() {
		setText("Add New Parameter");
		setToolTipText("Adds a new, blank parameter.");

		// TODO Decorate this action.

		// Disable the action initially.
		setEnabled(false);

		return;
	}

	/**
	 * Adds a new <code>Entry</code> (aka property or parameter) to the active
	 * or first available data node in the {@link #tree}.
	 */
	@Override
	public void run() {
		// Get either the active data node or the first available data node.
		DataComponent dataNode = canAdd(tree);

		// If there is a data node, add a new Entry to it.
		if (dataNode != null) {
			// Find the next available property name.
			String entryName = newPropertyName;
			for (int i = 1; dataNode.contains(entryName); i++) {
				entryName = newPropertyName + "_" + Integer.toString(i);
			}
			// Create an Entry with a BasicEntryContentProvider.
			Entry entry = new Entry(new BasicEntryContentProvider());
			// Set the Entry's initial properties.
			entry.setName(entryName);
			entry.setDescription("");
			entry.setTag(entry.getName());
			entry.setRequired(false);
			entry.setValue("");
			entry.setReady(true);
			// Add the Entry to the data node.
			dataNode.addEntry(entry);
		}

		return;
	}

	/**
	 * Sets the current {@link TreeComposite} to which properties (in the form
	 * of {@link Entry}s) can be added.
	 * 
	 * @param tree
	 *            The parent tree. If null, the action is disabled.
	 */
	public void setTree(TreeComposite tree) {
		this.tree = tree;
		setEnabled((canAdd(tree) != null ? true : false));
	}

	/**
	 * Gets whether or not the specified <code>TreeComposite</code> can have an
	 * <code>Entry</code> (aka property or parameter) added to it. The
	 * <code>DataComponent</code> that can receive the new <code>Entry</code> is
	 * returned.
	 * 
	 * @param tree
	 *            The tree to which we would like to add a new property or
	 *            parameter.
	 * @return The tree's data node that can receive a new property or
	 *         parameter, or null if one cannot be added.
	 */
	private DataComponent canAdd(TreeComposite tree) {

		DataComponent dataNode = null;

		// Look for the active data node. If there is no active data node, get
		// the first available data node.
		if (tree != null) {
			dataNode = (DataComponent) tree.getActiveDataNode();
			if (dataNode == null && !tree.getDataNodes().isEmpty()) {
				dataNode = (DataComponent) tree.getDataNodes().get(0);
			}
		}

		return dataNode;
	}

}
