package org.eclipse.ice.caebat.batml;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class builds the BatMLModel. It inherits its operations from
 * ItemBuilder.
 * 
 * @author Scott Forest Hull II
 */
public class BatMLModelBuilder extends AbstractItemBuilder {

	/**
	 * The Constructor
	 */
	public BatMLModelBuilder() {
		setName("BatML Model");
		setType(ItemType.Model);
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		BatMLModel batML = new BatMLModel(projectSpace);
		batML.setItemBuilderName(getItemName());
		return batML;
	}

}
