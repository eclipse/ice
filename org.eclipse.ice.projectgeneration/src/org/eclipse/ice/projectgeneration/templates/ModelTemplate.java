package org.eclipse.ice.projectgeneration.templates;

import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.ui.templates.PluginReference;

public class ModelTemplate extends ICEItemTemplate {
	private static final String CLASS_NAME = "Model";
	private static final String EXTENSION_POINT = "org.eclipse.ice.item.model";
	private static final String KEY_DESCRIPTION = "Specify model parameters";
	private static final String KEY_SECTION_ID = "model";
	
	@Override
	public IPluginReference[] getDependencies(String schemaVersion) {
		return new IPluginReference[] { 
				new PluginReference("org.eclipse.core.resources.IFile", null, 0),
				new PluginReference("org.eclipse.core.resources.IProject", null, 0), 
				new PluginReference("org.eclipse.core.resources.IResource", null, 0),
				new PluginReference("org.eclipse.core.runtime.CoreException", null, 0),
				new PluginReference("org.eclipse.ice.datastructures.ICEObject.Component", null, 0),
				new PluginReference("org.eclipse.ice.datastructures.form.Form", null, 0), 
				new PluginReference("org.eclipse.ice.datastructures.form.FormStatus", null, 0),
				new PluginReference("org.eclipse.ice.io.serializable.IIOService", null, 0),
				new PluginReference("org.eclipse.ice.io.serializable.IOService", null, 0),
				new PluginReference("org.eclipse.ice.item.Item", null, 0)
		};
	}
}
