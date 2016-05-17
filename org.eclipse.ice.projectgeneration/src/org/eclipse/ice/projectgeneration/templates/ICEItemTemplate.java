/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.projectgeneration.templates;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Locale;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.projectgeneration.ICEProjectResources;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.IPluginFieldData;
import org.eclipse.pde.ui.templates.OptionTemplateSection;

/**
 * Provides the information for the ICEItemWizard, the final page in the 
 * new ICE item wizard.
 * 
 * @author arbennett
 */ 
public class ICEItemTemplate extends OptionTemplateSection {

	protected static final String BUNDLE_ID = "org.eclipse.ice.projectgeneration";
	
	// Strings used for templating
	protected static final String EXTENSION_POINT = "org.eclipse.ice.item.itemBuilder";
	protected static final String KEY_CLASS_NAME = "className";
	protected static final String KEY_EXTENSION_NAME = "packageName";
	protected static final String KEY_JOB_LAUNCHER_EXT = "createJobLauncher";
	protected static final String KEY_MODEL_EXT = "createModel";
	
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
		p1.setDescription("Specify ICE Item Parameters.");
		wizard.addPage(p1);
		markPagesAdded();
	}
	
	 /**
	  * Define the options, descriptions, default values, and page numbers
	  */
	protected void setOptions() {
		addOption(KEY_CLASS_NAME     , "Item Class Base Name"   , "" , 0);
		addOption(KEY_JOB_LAUNCHER_EXT, "Create Job Launcher?", true, 0);
		addOption(KEY_MODEL_EXT, "Create Model?", true, 0);
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
	
	public void setExtensionName(String extName) {
		addOption(KEY_EXTENSION_NAME, "Extension Base Name", extName, 0);
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
		IPluginBase plugin;
		String pluginId;
		IPluginExtension extension;
		IPluginModelFactory factory;
		IPluginElement element;
		
		// Model builder plugin.xml entry
		if (getBooleanOption(KEY_MODEL_EXT)) {
			plugin = model.getPluginBase();
			pluginId = plugin.getId();
			extension = createExtension(EXTENSION_POINT, false);
			extension.setName(splitCamelCase(getStringOption(KEY_CLASS_NAME) + " Model"));
			extension.setId(getStringOption(KEY_CLASS_NAME) + "ModelBuilder");
			factory = model.getPluginFactory();
			element = factory.createElement(extension);
			element.setName("implementation");
			element.setAttribute("class", pluginId + ".model." + getStringOption(KEY_CLASS_NAME) + "ModelBuilder");
			extension.add(element);
			if (!extension.isInTheModel())
				plugin.add(extension);
		}
		// Job launcher builder plugin.xml entry
		if (getBooleanOption(KEY_JOB_LAUNCHER_EXT)) {
			plugin = model.getPluginBase();
			pluginId = plugin.getId();
			extension = createExtension(EXTENSION_POINT, false);
			extension.setName(splitCamelCase(getStringOption(KEY_CLASS_NAME)+ " Launcher"));
			extension.setId(getStringOption(KEY_CLASS_NAME) + "LauncherBuilder");
			factory = model.getPluginFactory();
			element = factory.createElement(extension);
			element.setName("implementation");
			element.setAttribute("class", pluginId + ".launcher." + getStringOption(KEY_CLASS_NAME) + "LauncherBuilder");
			extension.add(element);
			if (!extension.isInTheModel())
				plugin.add(extension);
		}
	}
}
