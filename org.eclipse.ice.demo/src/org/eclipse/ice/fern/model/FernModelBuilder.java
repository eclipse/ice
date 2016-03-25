package org.eclipse.ice.fern.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.model.AbstractModelBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

public class FernModelBuilder extends AbstractModelBuilder {

	public FernModelBuilder() {
		setName("FernModel");
		setType(ItemType.Model);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		Item item = new FernModel(projectSpace);
		item.setItemBuilderName(this.getItemName());
		return item;
	}
}