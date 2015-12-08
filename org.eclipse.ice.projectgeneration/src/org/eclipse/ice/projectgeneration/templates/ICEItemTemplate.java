package org.eclipse.ice.projectgeneration.templates;

import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.ui.templates.OptionTemplateSection;

public abstract class ICEItemTemplate extends OptionTemplateSection {

	protected static final String BUNDLE_ID = "org.eclipse.ice.projectgeneration.templates";
	
	// Strings used for templating
	protected static final String KEY_CLASS_NAME = "className";
	protected static final String KEY_EXTENSION_NAME = "extensionName";
	protected static final String KEY_EXTENSION_ID = "extensionId";
	private static final String EXTENSION_POINT = "";
	private static final String KEY_DESCRIPTION = "";
	private static final String KEY_SECTION_ID = "";
	
	public ICEItemTemplate() {
		setPageCount(1);
		setOptions();
	}
	
	 /**
	  * Define the options, descriptions, default values, and page numbers
	  */
	protected void setOptions() {
		 addOption(KEY_EXTENSION_ID   , "Model ID"          , "" , 0);
		 addOption(KEY_EXTENSION_NAME , "Model Name"        , "" , 0);
		 addOption(KEY_PACKAGE_NAME   , "Package Name"      , "" , 0);
		 addOption(KEY_CLASS_NAME     , "Class Name"        , "" , 0);
		 addOption(KEY_DESCRIPTION    , "Model Description" , "" , 0);
	}

	/**
	 * 
	 */
	@Override
	protected void updateModel(IProgressMonitor monitor) throws CoreException {
		IPluginBase plugin = model.getPluginBase();
		IPluginExtension extension = createExtension(EXTENSION_POINT, true);
		IPluginModelFactory factory = model.getPluginFactory();
		IPluginElement element = factory.createElement(extension);
		element.setName(getSectionId());
		element.setAttribute("class", getStringOption(KEY_PACKAGE_NAME) + "." + getStringOption(KEY_CLASS_NAME));
		element.setAttribute(KEY_DESCRIPTION, getStringOption(KEY_DESCRIPTION));
		element.setAttribute("id", getStringOption(KEY_EXTENSION_ID));
		element.setAttribute("name", getStringOption(KEY_EXTENSION_NAME));
		element.setAttribute("visible", "true");
		extension.add(element);
		if (!extension.isInTheModel())
			plugin.add(extension);
	}
	
	protected String getClassName() { return KEY_CLASS_NAME; }
	public String getPageDescription() { return KEY_DESCRIPTION; }
	public String getPageTitle() { return "New " + KEY_SECTION_ID; }
	
	@Override
	protected URL getInstallURL() {	
		return Platform.getBundle(BUNDLE_ID).getEntry("/");	
	}

	@Override
	protected ResourceBundle getPluginResourceBundle() {
		return Platform.getResourceBundle(Platform.getBundle(BUNDLE_ID));	
	}

	@Override public String[] getNewFiles() { return new String[0]; }
	@Override public String getSectionId() { return KEY_SECTION_ID; }
	@Override public String getUsedExtensionPoint() { return EXTENSION_POINT; }
}
