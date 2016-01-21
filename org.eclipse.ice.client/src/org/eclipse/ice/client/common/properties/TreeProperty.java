/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.common.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;

/**
 * This class provides a wrapper for {@link TreeComposite} properties or
 * parameters. Properties for a <code>TreeComposite</code> are the {@link IEntry}
 * instances stored in the tree's {@link DataComponent}s or "data nodes".
 * <p>
 * This class also provides some helper methods for determining if a property is
 * read only or if it is the "adaptive type" for an
 * {@link AdaptiveTreeComposite}.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class TreeProperty {

	/**
	 * The ID of the property. This value can be used for keys in
	 * <code>Map</code>s.
	 */
	public final int id;

	/**
	 * The parent tree for this property.
	 */
	private final TreeComposite tree;
	/**
	 * The data node in the {@link #tree} that contains the {@link #entry} or
	 * property.
	 */
	private final DataComponent dataNode;
	/**
	 * The actual "property" or "parameter".
	 */
	private final IEntry entry;

	/**
	 * Whether or not the {@link #entry} is the adaptive type for the
	 * {@link #tree}.
	 */
	private final boolean isAdaptiveType;
	/**
	 * Whether or not the entry is read only.
	 */
	private final boolean readOnly;

	/**
	 * The default constructor.
	 * 
	 * @param id
	 *            The ID of the property. This is usually the index of the
	 *            property among all its sibling properties.
	 * @param tree
	 *            The parent tree for this property.
	 * @param dataNode
	 *            The data node in the tree that contains the entry.
	 * @param entry
	 *            The actual "property" or "parameter".
	 */
	public TreeProperty(int id, TreeComposite tree, DataComponent dataNode,
			IEntry entry) {
		this.id = id;
		this.tree = tree;
		this.dataNode = dataNode;
		this.entry = entry;

		// Set isAdaptiveType to true if the TreeComposite is adaptive and if
		// the Entry is the "type" Entry for the tree.
		boolean isAdaptiveType = false;
		if ("type".equals(entry.getName())) {
			final List<Boolean> isAdaptive = new ArrayList<Boolean>(1);
			IComponentVisitor visitor = new SelectiveComponentVisitor() {
				@Override
				public void visit(AdaptiveTreeComposite component) {
					isAdaptive.add(true);
				}
			};
			tree.accept(visitor);
			isAdaptiveType = !isAdaptive.isEmpty();
		}
		this.isAdaptiveType = isAdaptiveType;

		// Set the readOnly flag if certain conditions are met.
		readOnly = false;

		return;
	}

	/**
	 * Gets the parent tree for this property.
	 * 
	 * @return The parent <code>TreeComposite</code>.
	 */
	public TreeComposite getTree() {
		return tree;
	}

	/**
	 * Gets the data node in the {@link #tree} that contains the {@link #entry}
	 * or property.
	 * 
	 * @return The parent <code>DataComponent</code>.
	 */
	public DataComponent getDataNode() {
		return dataNode;
	}

	/**
	 * Gets the actual "property" or "parameter".
	 * 
	 * @return The <code>Entry</code> corresponding to the tree property or
	 *         parameter.
	 */
	public IEntry getEntry() {
		return entry;
	}

	/**
	 * Whether or not the entry is read only.
	 * 
	 * @return True if the property is read only, false otherwise.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Whether or not the {@link #entry} is the adaptive type for the
	 * {@link #tree}.
	 * 
	 * @return True if the <code>Entry</code>'s value is the type of
	 *         {@link AdaptiveTreeComposite} for the parent tree, false
	 *         otherwise.
	 */
	public boolean isAdaptiveType() {
		return isAdaptiveType;
	}

	/**
	 * The supported types for the parent {@link AdaptiveTreeComposite}.
	 * 
	 * @return A <code>List</code> of <code>Strings</code> representing the
	 *         parent tree's adaptive types, or an empty <code>List</code> if
	 *         the property does not correspond to the parent tree's adaptive
	 *         type <code>Entry</code>.
	 */
	public List<String> getAdaptiveTypes() {
		return (isAdaptiveType ? ((AdaptiveTreeComposite) tree).getTypes()
				: new ArrayList<String>(1));
	}
}
