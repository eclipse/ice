package org.eclipse.ice.demo.visualization.launcher;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

public class VisualizationCompleteLauncherBuilder extends AbstractItemBuilder {
	
	public VisualizationCompleteLauncherBuilder() {
		setName("VisualizationCompleteLauncher");
		setType(ItemType.Simulation);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		Item item = new VisualizationCompleteLauncher(projectSpace);
		item.setItemBuilderName(this.getItemName());
		return item;
	}
}
