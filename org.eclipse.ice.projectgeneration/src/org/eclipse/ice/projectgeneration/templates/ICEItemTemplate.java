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
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.OptionTemplateSection;
import org.osgi.framework.Bundle;

public abstract class ICEItemTemplate extends OptionTemplateSection {

	protected static final String BUNDLE_ID = "org.eclipse.ice.projectgeneration";
	
	// Strings used for templating
	protected static final String KEY_CLASS_NAME = "className";
	protected static final String KEY_EXTENSION_NAME = "extensionName";
	protected static final String KEY_EXTENSION_ID = "extensionId";
	private static final String CLASS_NAME = "";
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
	
	private static String splitCamelCase(String s) {
		return s.replaceAll(
		String.format("%s|%s|%s",
	         "(?<=[A-Z])(?=[A-Z][a-z])",
	         "(?<=[^A-Z])(?=[A-Z])",
	         "(?<=[A-Za-z])(?=[^A-Za-z])"
	      ),
	      " "
	   );
	}
	
	public void addPages(Wizard wizard) {
		// create one wizard page for the options
		WizardPage p1 = createPage(0);
		p1.setTitle(getPageTitle());
		p1.setDescription(getPageDescription());
		wizard.addPage(p1);
		markPagesAdded();
	}
	
	protected String[][] getLookupList(String extensionPoint, String name, String id, String label, boolean optional) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(extensionPoint);
		Map<String, String> map = new HashMap<>();
		if (optional){
			map.put("", null);
		}
		IConfigurationElement[] configurationElements = point.getConfigurationElements();
		for (IConfigurationElement e : configurationElements) {
			if (e.getName().equals(name)) {
				map.put(e.getAttribute(label), e.getAttribute(id));
			}
		}
		String[][] options = new String[map.size()][];
		int i = 0;
		for (String k : map.keySet()) {
			options[i++] = new String[] { map.get(k), k };
		}
		return options;
	}
	
	
	/**
	 * 
	 */
	protected void initializeFields(IFieldData data) {
		String id = data.getId();
		String packageName = getFormattedPackageName(id);
		initializeOption(KEY_PACKAGE_NAME, packageName);
		initializeOption(KEY_CLASS_NAME, getClassName());
		initializeOption(KEY_EXTENSION_ID, packageName + "." + getSectionId());
		initializeOption(KEY_EXTENSION_NAME, splitCamelCase(getClassName()));
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
	
	protected String getClassName() { return CLASS_NAME; }
	public String getPageDescription() { return KEY_DESCRIPTION; }
	public String getPageTitle() { return "New " + KEY_SECTION_ID; }
	
	@Override
	protected URL getInstallURL() {	
		return Platform.getBundle(BUNDLE_ID).getEntry("/");	
	}

	@Override
	protected ResourceBundle getPluginResourceBundle() {
		return new ICEProjectResources();	
	}

	@Override public String[] getNewFiles() { return new String[0]; }
	@Override public String getSectionId() { return KEY_SECTION_ID; }
	@Override public String getUsedExtensionPoint() { return EXTENSION_POINT; }
	
}
