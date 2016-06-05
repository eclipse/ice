/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
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
package org.eclipse.ice.item.utilities.moose;

import java.util.ArrayList;

import org.eclipse.january.form.DataComponent;
import org.eclipse.january.form.TreeComposite;

/**
 * This class represents a MOOSE block as loaded from YAML. Its sole purpose is
 * to overload toTreeComposite() and write the subblocks as exemplar children
 * instead of real children in the TreeComposite.
 * 
 * @author Jay Jay Billings, Anna Wojtowicz
 */
public class YAMLBlock extends Block {
	/**
	 * This operation overrides Block.toTreeComposite() to create a
	 * TreeComposite from the Block with the sub-blocks configured as exemplar
	 * children. Everything else is the same.
	 * 
	 * @return The tree composite.
	 */
	@Override
	public TreeComposite toTreeComposite() {

		// Local Declarations
		TreeComposite treeComp = new TreeComposite();
		DataComponent dataComp = new DataComponent();

		// Setup the tree composites name
		treeComp.setName(name);
		treeComp.setDescription(description);

		// Add the parameters to the list if they exist
		if (parameters != null) {
			// Setup the data component
			dataComp.setName(name + " Parameters");
			dataComp.setDescription(name + " Parameters");
			dataComp.setId(1);
			for (Parameter param : parameters) {
				dataComp.addEntry(param.toEntry());
			}
			// Add the data component as a data node
			treeComp.addComponent(dataComp);
		}

		// Add the subblocks to the tree as child exemplars, if there are any.
		// i+2 is used for the id because the DataComponent has id = 1.
		if (subblocks != null) {
			ArrayList<TreeComposite> exemplars = new ArrayList<TreeComposite>();
			for (int i = 0; i < subblocks.size(); i++) {
				TreeComposite childTreeComp = subblocks.get(i)
						.toTreeComposite();
				if (childTreeComp.getName().equals("*")) {
					childTreeComp.setName("New_"
							+ name.substring(0, name.length() - 1));
				}
				childTreeComp.setId(i + 2);
				exemplars.add(childTreeComp);
			}
			treeComp.setChildExemplars(exemplars);
		}

		return treeComp;
	}

	/**
	 * This operation overrides Block.getNewBlock() to create an instance of
	 * YAMLBlocks that can be used when the tree is being walked instead of
	 * instances of the Block base class.
	 * 
	 * @return The new YAMLBlock.
	 */
	@Override
	protected Block getNewBlock() {
		return new YAMLBlock();
	}
}