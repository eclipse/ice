package org.eclipse.ice.caebat.batml;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class builds the BatMLModel. It inherits its operations from
 * ItemBuilder.
 * 
 * @author Scott Forest Hull II
 */
public class BatMLModelBuilder implements ItemBuilder {

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public String getItemName() {
		return "BatML Model";
	}

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Model;
	}

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public Item build(IProject projectSpace) {
		BatMLModel batML = new BatMLModel(projectSpace);
		batML.setItemBuilderName(getItemName());
		return batML;
	}

}
