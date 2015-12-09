package org.eclipse.ice.projectgeneration.templates;

import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.ui.templates.PluginReference;

public class JobLauncherBuilderTemplate extends ICEItemTemplate {
	private static final String CLASS_NAME = "AbstractItemBuilder";
	private static final String EXTENSION_POINT = "org.eclipse.ice.item";
	private static final String KEY_DESCRIPTION = "Specify job launcher builder parameters";
	private static final String KEY_SECTION_ID = "job launcher builder";
	
	@Override
	public IPluginReference[] getDependencies(String schemaVersion) {
		return new IPluginReference[] { 
				new PluginReference("org.eclipse.ice.item.Item", null, 0),
				new PluginReference("org.eclipse.ice.item.ItemType", null, 0), 
				new PluginReference("org.eclipse.ice.item.AbstractItemBuilder", null, 0),
				new PluginReference("org.eclipse.core.resources.IProject", null, 0)};
	}
}
