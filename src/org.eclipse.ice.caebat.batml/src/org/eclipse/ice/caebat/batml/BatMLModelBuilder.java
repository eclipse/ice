package org.eclipse.ice.caebat.batml;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * <p>
 * This class builds the BatMLModel. It inherits its operations
 * from ItemBuilder.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class BatMLModelBuilder implements ItemBuilder {

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		return "BatML Model";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Model;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(Interface projectSpace)
	 */
	@Override
	public Item build(IProject projectSpace) {
		BatMLModel batML = new BatMLModel(projectSpace);
		batML.setItemBuilderName(getItemName());
		return batML;
	}

}
