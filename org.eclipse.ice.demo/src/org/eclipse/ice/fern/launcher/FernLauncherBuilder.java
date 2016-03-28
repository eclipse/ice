package org.eclipse.ice.fern.launcher;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

public class FernLauncherBuilder extends AbstractItemBuilder {
	
	public FernLauncherBuilder() {
		setName("FernLauncher");
		setType(ItemType.Simulation);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		Item item = new FernLauncher(projectSpace);
		item.setItemBuilderName(this.getItemName());
		return item;
	}
}
