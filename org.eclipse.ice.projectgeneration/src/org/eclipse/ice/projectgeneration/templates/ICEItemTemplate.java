package org.eclipse.ice.projectgeneration.templates;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.OptionTemplateSection;
import org.osgi.framework.Bundle;

public class ICEItemTemplate extends OptionTemplateSection {

	protected static final String BUNDLE_ID = "org.eclipse.ice.projectgeneration";
	
	// Strings used for templating
	protected static final String EXTENSION_POINT = "org.eclipse.ice.item.itemBuilder";
	protected static final String KEY_CLASS_NAME = "className";
	protected static final String KEY_EXTENSION_NAME = "extensionName";
	protected static final String KEY_PACKAGE_NAME = "packageName";

	/**
	 * Constructor
	 */
	public ICEItemTemplate() {
		setPageCount(1);
		setOptions();
	}
	
	/**
	 * Add pages to the wizard
	 */
	public void addPages(Wizard wizard) {
		// create one wizard page for the options
		WizardPage p1 = createPage(0);
		p1.setTitle("New ICE Item Project");
		p1.setDescription("Specify ICE Item parameters");
		wizard.addPage(p1);
		markPagesAdded();
	}
	
	 /**
	  * Define the options, descriptions, default values, and page numbers
	  */
	protected void setOptions() {
		 addOption(KEY_EXTENSION_NAME , "Extension Base Name"        , "" , 0);
		 addOption(KEY_PACKAGE_NAME   , "Package Name"      , "" , 0);
		 addOption(KEY_CLASS_NAME     , "Class Base Name"        , "" , 0);
	}
	
	@Override
	protected URL getInstallURL() {	
		return Platform.getBundle(BUNDLE_ID).getEntry("/");	
	}

	@Override
	protected ResourceBundle getPluginResourceBundle() {
		return new ICEProjectResources();	
	}
	
	@Override public String[] getNewFiles() { return new String[0]; }
	@Override public String getSectionId() { return "ICEItem"; }
	@Override public String getUsedExtensionPoint() { return EXTENSION_POINT; }
	
	protected String getFormattedPackageName(String id) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < id.length(); i++) {
			char ch = id.charAt(i);
			if (buffer.length() == 0) {
				if (Character.isJavaIdentifierStart(ch))
					buffer.append(Character.toLowerCase(ch));
			} else {
				if (Character.isJavaIdentifierPart(ch) || ch == '.')
					buffer.append(ch);
			}
		}
		return buffer.toString().toLowerCase(Locale.ENGLISH);
	}
	
	protected static String splitCamelCase(String s) {
		return s.replaceAll(
		String.format("%s|%s|%s",
	         "(?<=[A-Z])(?=[A-Z][a-z])",
	         "(?<=[^A-Z])(?=[A-Z])",
	         "(?<=[A-Za-z])(?=[^A-Za-z])"
	      ),
	      " "
	   ).trim();
	}
	
	@Override
	protected void updateModel(IProgressMonitor monitor) throws CoreException {
		// Model builder plugin.xml entry
		IPluginBase plugin = model.getPluginBase();
		IPluginExtension extension = createExtension(EXTENSION_POINT, false);
		extension.setName(splitCamelCase(getStringOption(KEY_EXTENSION_NAME) + " Model"));
		extension.setId(getStringOption(KEY_EXTENSION_NAME).toLowerCase() + "ModelBuilder");
		IPluginModelFactory factory = model.getPluginFactory();
		IPluginElement element = factory.createElement(extension);
		element.setName("implementation");
		element.setAttribute("class", getStringOption(KEY_PACKAGE_NAME) + "." + getStringOption(KEY_CLASS_NAME) + "ModelBuilder");
		extension.add(element);
		if (!extension.isInTheModel())
			plugin.add(extension);

		// Job launcher builder plugin.xml entry
		plugin = model.getPluginBase();
		extension = createExtension(EXTENSION_POINT, false);
		extension.setName(splitCamelCase(getStringOption(KEY_EXTENSION_NAME) + " Launcher"));
		extension.setId(getStringOption(KEY_EXTENSION_NAME).toLowerCase() + "LauncherBuilder");
		factory = model.getPluginFactory();
		element = factory.createElement(extension);
		element.setName("implementation");
		element.setAttribute("class", getStringOption(KEY_PACKAGE_NAME) + "." + getStringOption(KEY_CLASS_NAME) + "LauncherBuilder");
		extension.add(element);
		if (!extension.isInTheModel())
			plugin.add(extension);
	}
}
