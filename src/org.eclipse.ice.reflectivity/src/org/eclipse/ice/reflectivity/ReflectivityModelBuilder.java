package org.eclipse.ice.reflectivity;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

public class ReflectivityModelBuilder implements ItemBuilder {

	@Override
	public String getItemName() {
		return "Reflectivity Model";
	}

	@Override
	public ItemType getItemType() {
		return ItemType.Model;
	}

	@Override
	public Item build(IProject projectSpace) {
		return new ReflectivityModel(projectSpace);
	}

}
