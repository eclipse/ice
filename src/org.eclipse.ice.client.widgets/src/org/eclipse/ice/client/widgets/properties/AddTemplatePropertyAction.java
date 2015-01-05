package org.eclipse.ice.client.widgets.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.updateableComposite.Component;

// TODO documentation
public class AddTemplatePropertyAction extends AddPropertyAction {

	private List<Entry> properties;

	@Override
	public void run() {
		super.run();

		// TODO Remove this and do something useful.
		for (Entry entry : properties) {
			System.out.println(entry.getName());
		}
	}

	/**
	 * Searches the tree's parent to find the corresponding exemplar tree node
	 * and retrieves all properties from the exemplar node if it exists.
	 * <p>
	 * In this case, properties correspond to <i>ready</i> {@code Entry}s inside
	 * the exemplar tree node's active {@code DataComponent}.
	 * </p>
	 * 
	 * @param tree
	 *            The tree node to which template properties can be added.
	 * @return A list containing the template properties. This method returns an
	 *         empty list if no template properties can be found.
	 */
	private List<Entry> findTemplateProperties(TreeComposite tree) {

		List<Entry> properties = null;

		// Determine the exemplar tree node corresponding to the specified tree
		// node.
		if (tree != null) {
			TreeComposite parent = tree.getParent();
			if (parent != null && parent.hasChildExemplars()) {
				DataComponent dataNode = null;
				List<TreeComposite> exemplars = parent.getChildExemplars();
				// Use the ID to see if the nodes match.
				int id = tree.getId();
				System.out.println(id);
				System.out.println("Found the parent's child exemplars...");
				for (TreeComposite exemplar : exemplars) {
					System.out.println("exemplar: " + exemplar.getName() + " " + exemplar.getId());
					if (exemplar.getId() == id) {
						// If we found a match, pull the templated properties
						// from the active data node.
						dataNode = (DataComponent) exemplar.getActiveDataNode();
						// FIXME Shouldn't the active data node be set?
						if (dataNode == null) {
							List<Component> dataNodes = exemplar.getDataNodes();
							if (!dataNodes.isEmpty()) {
								dataNode = (DataComponent) dataNodes.get(0);
							}
						}
						if (dataNode != null) {
							properties = dataNode.retrieveAllEntries();
							// FIXME We should be retrieving ready entries, but there are none...
//							properties = dataNode.retrieveReadyEntries();
						}
						break;
					}
				}
			}
		}

		// Return an empty list if there was no corresponding exemplar with
		// templated properties.
		if (properties == null) {
			properties = new ArrayList<Entry>(1);
		}

		return properties;
	}

	/**
	 * Sets the current {@link TreeComposite} to which properties (in the form
	 * of {@link Entry}s) can be added.
	 * 
	 * @param tree
	 *            The parent tree. If null, the action is disabled.
	 */
	@Override
	public void setTree(TreeComposite tree) {
		// Clear the list of properties.
		properties = new ArrayList<Entry>(1);
		// Set the tree.
		super.setTree(tree);
		// Update the list of properties and set the enabled flag.
		properties = findTemplateProperties(tree);
		setEnabled(!properties.isEmpty());

		return;
	}

	/**
	 * Overrides the default behavior. If there are no {@link #properties}, then
	 * the returned {@code DataComponent} is null.
	 */
	@Override
	protected DataComponent canAdd(TreeComposite tree) {
		return properties.isEmpty() ? null : super.canAdd(tree);
	}

}
