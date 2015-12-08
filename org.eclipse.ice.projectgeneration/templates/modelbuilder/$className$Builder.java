package $packageName$.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

public class $className$Builder extends AbstractItemBuilder {

	public $className$Builder() {
		setName("$className$");
		setType(ItemType.Model);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		Item item = new $className$(projectSpace);
		item.setItemBuilderName(this.getItemName());
		return item;
	}
}