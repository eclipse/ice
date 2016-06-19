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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.projectgeneration.ICEProjectResources;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.IPluginFieldData;
import org.eclipse.pde.ui.templates.AbstractChoiceOption;
import org.eclipse.pde.ui.templates.OptionTemplateSection;
import org.eclipse.pde.ui.templates.StringOption;
import org.eclipse.pde.ui.templates.TemplateOption;

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
	protected static final String KEY_IO_FORMAT_EXT = "ioFormat";
	protected static final String KEY_DEFAULT_FILE_NAME = "defaultFileName";
	
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
		p1.setPageComplete(false);
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
		addOption(KEY_IO_FORMAT_EXT, "File Format", getIOFormatOptions(), "", 0);
		addDataOption(KEY_DEFAULT_FILE_NAME, "Choose a default dataset (leave blank for none):", "", 0);
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

	private String[][] getIOFormatOptions() {
		ArrayList<String> readerTypes = new ArrayList<String>();
		ArrayList<String> writerTypes = new ArrayList<String>();
		try {
			for (IReader reader : IReader.getIReaders()) {
				readerTypes.add(reader.getReaderType());
			}
			for (IWriter writer : IWriter.getIWriters()) {
				writerTypes.add(writer.getWriterType());
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String[]> ioFormats = new ArrayList<String[]>();
		for (String writer : writerTypes) {
			if (readerTypes.contains(writer))
				ioFormats.add(new String[] {writer, writer});
		}
		ioFormats.add(0, new String[] {"", ""});
		String[][] options = new String[ioFormats.size()][2];
		options = ioFormats.toArray(options);
		return options; 
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

	/**
	 * 
	 */
	public void execute(IProject project, IPluginModelBase model, IProgressMonitor monitor) throws CoreException {
		initializeFields(model);
		super.execute(project, model, monitor);
		Path filePath = Paths.get(getStringOption(KEY_DEFAULT_FILE_NAME));
		if (filePath != null && filePath.toFile().exists()) {
			String fileName = filePath.getFileName().toString(); 
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
			IFolder dataFolder = project.getFolder("data");
			if (!dataFolder.exists()) {
				dataFolder.create(false, true, null);
			}
			IFile dataFile = dataFolder.getFile(fileName);
			FileInputStream dataContents;
			try {
				dataContents = new FileInputStream(filePath.toString());
				dataFile.create(dataContents, false, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * 
	 * @param name
	 * @param label
	 * @param value
	 * @param pageIndex
	 * @return
	 */
	protected TemplateOption addDataOption(String name, String label, String value, int pageIndex) {
		DataFileOption option = new DataFileOption(this, name, label);
		option.setValue(value);
		registerOption(option, value, pageIndex);
		return option;
	}
	

	/**
	 * 
	 */
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
