package org.eclipse.ice.projectgeneration.templates;

import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.ui.templates.PluginReference;

public class JobLauncherTemplate extends ICEItemTemplate {
	private static final String CLASS_NAME = "JobLauncher";
	private static final String EXTENSION_POINT = "org.eclipse.ice.item";
	private static final String KEY_DESCRIPTION = "Specify job launcher parameters";
	private static final String KEY_SECTION_ID = "job launcher";

	@Override
	public IPluginReference[] getDependencies(String schemaVersion) {
		return new IPluginReference[] { 
				new PluginReference("org.eclipse.ice.item.jobLauncher.JobLauncher", null, 0),
				new PluginReference("org.eclipse.ice.io.serializable.IIOService", null, 0), };
	}
}
