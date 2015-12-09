package org.eclipse.ice.projectgeneration.templates;

import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.ui.templates.PluginReference;

public class ModelBuilderTemplate extends ICEItemTemplate {
	private static final String CLASS_NAME = "AbstractModelBuilder";
	private static final String EXTENSION_POINT = "org.eclipse.ice.item.model";
	private static final String KEY_DESCRIPTION = "Specify model builder parameters";
	private static final String KEY_SECTION_ID = "model builder";
	
	@Override
	public IPluginReference[] getDependencies(String schemaVersion) {
		return new IPluginReference[] { 
				new PluginReference("org.eclipse.ice.item.Item", null, 0),
				new PluginReference("org.eclipse.ice.item.ItemType", null, 0), 
				new PluginReference("org.eclipse.ice.item.model.AbstractModelBuilder", null, 0),
				new PluginReference("org.eclipse.core.resources.IProject", null, 0)};
	}
}
