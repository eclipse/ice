/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.vibe.kvPair;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.Item;

/**
 * This class is a Model Item that configures a set of data components that
 * represent key-value pairs for VIBE input.
 * 
 * @author Jay Jay Billings, Andrew Bennett, Robert Smith
 * 
 */
@XmlRootElement(name = "VibeKVPair")
public class VibeKVPair extends Item implements IReader, IWriter {

	/**
	 * The index for the template component in the list of components.
	 */
	private static final int TEMPLATE_COMPONENT_ID = 0;

	/**
	 * Keep track of everything that we can do with the KV Pair Item
	 */
	private ArrayList<String> actionItems;

	/**
	 * The name of the default template to load.
	 */
	private static String defaultTemplate = "None";

	/**
	 * The tag that indicates this file should be exported to kv pairs.
	 */
	private String customTaggedExportString = "Export to key-value pair output";

	/**
	 * The component for the table of editable key value pairs.
	 */
	TableComponent kvTable;

	/**
	 * The name of the last template used to initialize the table component.
	 */
	private String templateName;

	/**
	 * The nullary constructor.
	 */
	public VibeKVPair() {
		this(null);

		// Initialize the template name
		templateName = defaultTemplate;

		return;
	}

	/**
	 * The required constructor.
	 * 
	 * @param projectSpace
	 *            The Eclipse Project space needed for file manipulation.
	 */
	public VibeKVPair(IProject projectSpace) {
		// Punt to the base class.
		super(projectSpace);

		// Initialize the template name
		templateName = defaultTemplate;
		return;
	}

	/**
	 * This operation returns a data component that contains the list of
	 * reafiles that may be selected by a user from the Nek5000_Model_Builder
	 * directory. This component will be empty if there are no files and contain
	 * an Entry that says "No problems available."
	 *
	 * @param files
	 *            The set of Nek reafiles available to be loaded
	 * @return the data component
	 */
	private DataComponent createSelectorComponent() {

		// Local Declaration
		DataComponent caseComp = new DataComponent();

		// The list of available templates
		ArrayList<String> templateNames = new ArrayList<String>();
		templateNames.add("None");
		templateNames.add("NTG");
		templateNames.add("DualFoil");

		// Setup the data component
		caseComp.setName("Problem Type Templates");
		caseComp.setDescription("The following is a list of VIBE Key-Value "
				+ "Pair input type templates for editing.");
		caseComp.setId(0);

		// Create an Entry for the template names
		DiscreteEntry templatesEntry = new DiscreteEntry();
		templatesEntry.setAllowedValues(templateNames);
		templatesEntry.setDefaultValue(defaultTemplate);
		templatesEntry.setValue(defaultTemplate);

		// Setup the file Entry's descriptive information
		templatesEntry.setName("Available Templates");
		templatesEntry.setDescription("A list of the templates available "
				+ "for populating the VIBE Key-Value Pair table component");
		templatesEntry.setId(1);

		// Add the Entry to the Component
		caseComp.addEntry(templatesEntry);

		// Register this Item as a listener of templatesEntry
		templatesEntry.register(this);

		return caseComp;

	}

	/**
	 * This operation overrides the base class' operation to create a Form with
	 * Entries that will be used to generate the VIBEkey-value pair file. It has
	 * one DataComponent per VIBE component.
	 */
	@Override
	public void setupForm() {
		// Create the form
		form = new Form();

		// Add the template selector component
		form.addComponent(createSelectorComponent());

		// If loading from the new item button we should just
		// load up the default case 6 file by passing in null
		if (project != null) {
			loadDefault(defaultTemplate);
		}
	}

	/**
	 * This operation is used to setup the name and description of the
	 * generator.
	 */
	@Override
	protected void setupItemInfo() {
		// Setup everything
		setName(VibeKVPairBuilder.name);
		itemType = VibeKVPairBuilder.type;
		setItemBuilderName(VibeKVPairBuilder.name);
		setDescription("Generate input files for VIBE.");
		allowedActions.remove("Export to ICE Native Format");
		actionItems = getAvailableActions();

		// Register this class as an IOService.
		// Note: Andrew, what are you doing here?
		IIOService ioService = getIOService();
		if (ioService == null) {
			setIOService(new IOService());
			ioService = getIOService();
		}
		ioService.addReader(this);
		ioService.addWriter(this);

		return;
	}

	/**
	 * Checks to make sure that the form given has some data to write out.
	 * 
	 * @param preparedForm
	 *            The form to review.
	 * @return retStatus Whether or not the form passed review
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {

		FormStatus retStatus = FormStatus.ReadyToProcess;

		// Grab the data component from the Form and only proceed if it exists
		ArrayList<Component> components = preparedForm.getComponents();

		// Make sure the form has the right amount of data
		if (components.size() == 0) {
			logger.info("VIBE KV Pair Generator Message: "
					+ "Could not find any data to write out");
			retStatus = FormStatus.InfoError;
		}

		// Get the selection from the template component
		DataComponent templateComp = (DataComponent) components
				.get(TEMPLATE_COMPONENT_ID);
		String template = templateComp.retrieveAllEntries().get(0).getValue();

		return retStatus;
	}

	/**
	 * Process the item. If told to export to KV pair file then a new file with
	 * the key value pairs will be written out. Otherwise fall back to the
	 * default definition
	 * 
	 * @param actionName
	 *            The name of the action to be taken.
	 * @return retStatus The status of the action
	 */
	@Override
	public FormStatus process(String actionName) {
		FormStatus retStatus;

		// If it is the custom operation, call this here.
		if (this.customTaggedExportString.equals(actionName)) {

			// Get the file from the project space to create the output
			String filename = getName().replaceAll("\\s+", "_") + "_" + getId()
					+ ".dat";
			String filePath = null;
			IFile outputFile = null;
			if (project != null) {
				filePath = project.getLocation().toOSString()
						+ System.getProperty("file.separator") + filename;
				outputFile = project.getFile(filename);
			} else {
				filePath = ResourcesPlugin.getWorkspace().getRoot()
						.getLocation().toOSString()
						+ System.getProperty("file.separator") + filename;
				outputFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(filePath));
			}

			// Get the data from the form
			ArrayList<Component> components = form.getComponents();

			// Make sure the form has something on it
			if (components.size() > 0) {
				try {
					// Write the output file
					this.write(form, outputFile);
					// Refresh the project space
					project.refreshLocal(IResource.DEPTH_ONE, null);
				} catch (CoreException e) {
					// Complain
					System.err.println("VibeKVPair Generator Message: "
							+ "Failed to refresh the project space.");
					logger.error(getClass().getName() + " Exception!", e);
				}
				// return a success
				retStatus = FormStatus.Processed;
			} else {
				// return an error
				System.err.println("Not enough components to write new file!");
				retStatus = FormStatus.InfoError;
			}
		}
		// Otherwise let item deal with the process
		else {
			retStatus = super.process(actionName);
		}

		return retStatus;

	}

	/**
	 * <p>
	 * This operation loads the given example into the Form.
	 * </p>
	 * 
	 * @param name
	 *            The path name of the example file name to load.
	 */
	@Override
	public void loadInput(String name) {
		IFile inputFile = inputFile = project.getFile(name);
		logger.info("VibeKVPair Message: Loading"
				+ inputFile.getFullPath().toOSString());
		form = read(inputFile);
		form.setName(getName());
		form.setDescription(getDescription());
		form.setId(getId());
		form.setItemID(getId());
		form.setActionList(actionItems);
	}

	/**
	 * Loads a default dataset that's embedded in the plugin
	 * 
	 * @param caseName
	 *            The name of the file containing the default case that will be
	 *            used to populate the table
	 */
	public void loadDefault(String caseName) {
		IFile inputFile = null;
		File temp = null;
		try {
			// Path to the default file
			String defaultFilePath = null;
			// Create a filepath for the default file
			if (project != null) {
				defaultFilePath = project.getLocation().toOSString()
						+ System.getProperty("file.separator") + caseName
						+ ".dat";
			} else {
				defaultFilePath = ResourcesPlugin.getWorkspace().getRoot()
						.getLocation().toOSString()
						+ System.getProperty("file.separator") + caseName
						+ ".dat";
			}

			// Create a temporary location to load the default file
			temp = new File(defaultFilePath);
			if (!temp.exists()) {
				temp.createNewFile();
			}

			// Pull the default file from inside the plugin
			URI uri = new URI("platform:/plugin/org.eclipse.ice.vibe/data/"
					+ caseName + ".dat");
			InputStream reader = uri.toURL().openStream();
			FileOutputStream outStream = new FileOutputStream(temp);

			// Write out the default file from the plugin to the temp
			// location
			int fileByte;
			while ((fileByte = reader.read()) != -1) {
				outStream.write(fileByte);
			}
			outStream.close();
			if (project != null) {
				inputFile = project.getFile(caseName + ".dat");
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} else {
				inputFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(defaultFilePath));
			}
			logger.info("VibeKVPair Message: Loading"
					+ inputFile.getFullPath().toOSString());

			form = read(inputFile);
			form.setName(getName());
			form.setDescription(getDescription());
			form.setId(getId());
			form.setItemID(getId());
			form.setActionList(actionItems);
		} catch (URISyntaxException e) {
			System.err.println("VibeKVPair Message: "
					+ "Error!  Could not load the default"
					+ " Vibe case data!");
		} catch (MalformedURLException e) {
			System.err.println("VibeKVPair Message: "
					+ "Error!  Could not load the default"
					+ " Vibe case data!");
		} catch (IOException e) {
			System.err.println("VibeKVPair Message: "
					+ "Error!  Could not load the default"
					+ " Vibe case data!");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			logger.error(getClass().getName() + " Exception!", e);
		}
	}

	/**
	 * Reads in the KV Pair file to a form.
	 * 
	 * @param ifile
	 *            : the IFile representation of the KV Pair File
	 * 
	 * @return a form containing the data from the ifile
	 */
	@Override
	public Form read(IFile ifile) {
		// Make sure there's something to look in
		if (ifile == null) {
			return null;
		}

		// Initialize the form if it does not exist
		if (form == null) {
			form = new Form();
		}

		// Read in the ini file to an ArrayList<String>
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(ifile.getContents()));
			
			// Read the FileInputStream and append to a StringBuffer
			StringBuffer buffer = new StringBuffer();
			int fileByte;
			while ((fileByte = reader.read()) != -1) {
				buffer.append((char) fileByte);
			}

			// Break up the StringBuffer at each newline character
			String[] bufferSplit = (buffer.toString()).split("\n");
			for (String each : bufferSplit) {
				lines.add(each);
			}

			// Add a dummy EOF line so that the last line of the file is
			// read in correctly
			lines.add("EOF");
			
			//Close the reader
			reader.close();
			
		} catch (FileNotFoundException e) {
			logger.error("VibeKVPair Message: "
					+ "Error! Could not find file for loading.");
			return null;
		} catch (IOException e) {
			logger.error(
					"VibeKVPair Message: " + "Error! Trouble reading file.");
			return null;
		} catch (CoreException e) {
			logger.error("VibeKVPair Message: "
					+ "Error! Trouble reading file from project location.");
			return null;
		}

		// Create the table if it doesn't exist or empty it if it does
		if (kvTable == null) {
			kvTable = new TableComponent();
		} else {
			for (int i : kvTable.getRowIds()) {
				kvTable.deleteRow(0);
			}
		}
		ArrayList<IEntry> row;
		ArrayList<IEntry> kvEntries = new ArrayList<IEntry>();
		IEntry key = new StringEntry();
		IEntry value = new StringEntry();
		key.setName("Key");
		value.setName("Value");
		kvEntries.add(key);
		kvEntries.add(value);
		kvTable.setRowTemplate(kvEntries);

		// The list of keys whose rows will be dependent on another row
		ArrayList<String> dependentKeys = new ArrayList<String>();
		dependentKeys.add("MODESEG");
		dependentKeys.add("CUTOFFL");
		dependentKeys.add("CUTOFFH");

		// The list of rows dependent on another row
		ArrayList<VibeKVPairRow> dependentRows = new ArrayList<VibeKVPairRow>();

		// The row for the NUMSEG key
		VibeKVPairRow numsegRow = null;

		for (String line : lines) {
			if (line.contains("=")) {
				String[] keyValue = line.split("=");
				int rowID = kvTable.addRow();
				row = kvTable.getRow(rowID);

				// The new row under construction
				VibeKVPairRow entryRow = null;

				// For most keys, create a standard row
				if (!dependentKeys.contains(keyValue[0])) {

					entryRow = new VibeKVPairRow((StringEntry) row.get(0),
							(StringEntry) row.get(1));

					if ("NUMSEG".equals(keyValue[0])) {
						numsegRow = entryRow;

						// The prescence of NUMSEG means that this is a DualFoil
						// problem. We must set the template name appropriately,
						// in case this function was invoked by importing a
						// key-value file directly.
						templateName = "DualFoil";
						((DataComponent) form
								.getComponent(TEMPLATE_COMPONENT_ID))
										.retrieveAllEntries().get(0)
										.setValue("DualFoil");
					}
				}

				else {

					// TODO If we ever want to create more complex relationships
					// between rows, change this to handle things more
					// generically
					// For keys whose lengths depend on numseg, add a listener
					entryRow = new VibeKVPairRow((StringEntry) row.get(0),
							(StringEntry) row.get(1)) {
						@Override
						public void update(IUpdateable component) {

							// This row won't need to send updates of its own,
							// so ignore the value entry
							if (component == getValue()) {
								return;
							}

							// Cast the component
							VibeKVPairRow source = (VibeKVPairRow) component;

							// The string that will be written to this row's
							// value
							String valueString = getValue().getValue();

							// Calculate the size of the vector
							int vectorSize = valueString.length()
									- valueString.replace(",", "").length() + 1;

							// Get the new value of the numseg key
							int numseg = Integer
									.valueOf(source.getValue().getValue());

							// If numseg is an invalid value, ignore the change
							if (numseg <= 0) {
								return;
							}

							// If numseg has been reduced, shorten the value
							// vector
							if (numseg < vectorSize) {

								// The amount of numbers to be removed
								int numRemove = vectorSize - numseg;

								// Remove the last number in the vector,
								// numRemove times
								for (int i = 0; i < numRemove; i++) {
									valueString = valueString.substring(0,
											valueString.lastIndexOf(','));
								}

								getValue().setValue(valueString);

							}

							// If numseg has been increased, lengthen the value
							// vector
							else if (numseg > vectorSize) {

								// The number of additional values to add
								int numAdd = numseg - vectorSize;

								// The default value to pad the vector with
								String pad = null;

								// MODESEG is a vector of integers, all others
								// are vectors of doubles
								if (!getKey().getValue().equals("MODESEG")) {
									pad = ",0.0";
								} else {
									pad = ",0";
								}

								// Pad the vector out with the default value
								for (int i = 0; i < numAdd; i++) {
									valueString += pad;
								}

								getValue().setValue(valueString);
							}
						}
					};

					// Add this row to the list
					dependentRows.add(entryRow);
				}

				entryRow.getKey().setValue(keyValue[0]);
				entryRow.getValue().setValue(keyValue[1]);
			}
		}

		// Register each dependent row as a listener to the NUMSEG row
		if (numsegRow != null) {
			for (VibeKVPairRow dependentRow : dependentRows) {
				numsegRow.register(dependentRow);
			}
		}

		//Add the table to the form if it isn't already there
		if(form.getComponents().size() < 2){
			form.addComponent(kvTable);
		}
		// Return the form
		return form;
	}

	/**
	 * Writes the KV pair file from the given form
	 * 
	 * @param formToWrite
	 * @param ifile
	 */
	@Override
	public void write(Form formToWrite, IFile ifile) {
		// Make sure the input isn't null
		if (form == null || ifile == null) {
			return;
		}

		// Get the components from the form and make sure we have a
		// valid place that we can write the file out to
		ArrayList<Component> components = form.getComponents();

		// Make sure that the form had data that looks correct, and the output
		// file exists
		if (components != null && components.size() > 0
				&& outputFile.isFile()) {
			try {
				// Get an output stream to the file
				PipedInputStream in = new PipedInputStream(8196);
				PipedOutputStream out = new PipedOutputStream(in);

				if (!ifile.exists()) {
					byte[] blank = "".getBytes();
					InputStream s = new ByteArrayInputStream(blank);
					ifile.create(s, true, new NullProgressMonitor());
				}

				int numComponents = components.size();
				String configString = "";
				ArrayList<IEntry> row;
				byte[] byteArray;

				// Build the output by going through each row
				TableComponent kvPairs = (TableComponent) components
						.get(numComponents - 1);
				for (int i = 0; i < kvPairs.numberOfRows(); i++) {
					row = kvPairs.getRow(i);
					configString += row.get(0).getValue().trim() + "="
							+ row.get(1).getValue().trim() + "\n";
				}
				configString += "\n";

				// Write it out
				byteArray = configString.getBytes();
				out.write(byteArray);
				out.close();
				ifile.setContents(in, true, false, new NullProgressMonitor());

			} catch (FileNotFoundException e) {
				logger.info("VibeKVPair Message: Could not find "
						+ outputFile.getAbsolutePath() + " for writing.");
			} catch (IOException e) {
				logger.info("VibeKVPair Message: Could not write to "
						+ outputFile.getAbsolutePath() + ".");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

	}

	@Override
	public ArrayList<IEntry> findAll(IFile file, String regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replace(IFile file, String regex, String value) {
		// TODO Auto-generated method stub
	}

	/**
	 * Return that this is a VibeKVPairItem
	 */
	@Override
	public String getWriterType() {
		return "VibeKVPairItem";
	}

	/**
	 * Return that this is a VibeKVPairItem
	 */
	@Override
	public String getReaderType() {
		return "VibeKVPairItem";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.Item#update(org.eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable updateable) {

		if (updateable instanceof DiscreteEntry) {
			DiscreteEntry e = (DiscreteEntry) updateable;

			// Get the user selected template
			String template = e.getValue();

			// If the template was changed from the last loaded template, load
			// the
			// newly selected template
			if (!templateName.equals(template)) {
				loadDefault(template);
				templateName = template;
			}
		}
	}
}
