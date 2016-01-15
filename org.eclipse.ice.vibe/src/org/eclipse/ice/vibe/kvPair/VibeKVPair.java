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
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.Entry;
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
 * @author Jay Jay Billings, Andrew Bennett
 * 
 */
@XmlRootElement(name = "VibeKVPair")
public class VibeKVPair extends Item implements IReader, IWriter {

	/**
	 * Keep track of everything that we can do with the KV Pair Item
	 */
	private ArrayList<String> actionItems;

	/**
	 * The tag that indicates this file should be exported to kv pairs.
	 */
	private String customTaggedExportString = "Export to key-value pair output";

	/**
	 * The nullary constructor.
	 */
	public VibeKVPair() {
		this(null);
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
		return;
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

		// If loading from the new item button we should just
		// load up the default case 6 file by passing in null
		if (project != null) {
			loadInput(null);
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
					logger.error(getClass().getName() + " Exception!",e);
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

		// If nothing is specified, load case 6 from inside the plugin
		IFile inputFile = null;
		File temp = null;
		if (name == null) {
			try {
				// Path to the default file
				String defaultFilePath = null;
				// Create a filepath for the default file
				if (project != null) {
					defaultFilePath = project.getLocation().toOSString()
							+ System.getProperty("file.separator")
							+ "case_6.dat";
				} else {
					defaultFilePath = ResourcesPlugin.getWorkspace().getRoot()
							.getLocation().toOSString()
							+ System.getProperty("file.separator")
							+ "case_6.dat";
				}

				// Create a temporary location to load the default file
				temp = new File(defaultFilePath);
				if (!temp.exists()) {
					temp.createNewFile();
				}

				// Pull the default file from inside the plugin
				URI uri = new URI(
						"platform:/plugin/org.eclipse.ice.vibe/data/case_6.dat");
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
					inputFile = project.getFile("case_6.dat");
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} else {
					inputFile = ResourcesPlugin.getWorkspace().getRoot()
							.getFile(new Path(defaultFilePath));
				}

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
				logger.error(getClass().getName() + " Exception!",e);
			}
		} else {
			// Get the file
			inputFile = project.getFile(name);
		}

		// Load the components from the file and setup the form
		logger.info("VibeKVPair Message: Loading"
				+ inputFile.getFullPath().toOSString());

		form = read(inputFile);
		form.setName(getName());
		form.setDescription(getDescription());
		form.setId(getId());
		form.setItemID(getId());
		form.setActionList(actionItems);

		// Delete default file if it was copied into the workspace
		if (temp != null) {
			temp.delete();
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
		Form form = new Form();

		// Read in the ini file to an ArrayList<String>
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					ifile.getContents()));

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
		} catch (FileNotFoundException e) {
			logger.error("VibeKVPair Message: "
					+ "Error! Could not find file for loading.");
			return null;
		} catch (IOException e) {
			logger.error("VibeKVPair Message: "
					+ "Error! Trouble reading file.");
			return null;
		} catch (CoreException e) {
			logger.error("VibeKVPair Message: "
					+ "Error! Trouble reading file from project location.");
			return null;
		}

		//
		TableComponent kvTable = new TableComponent();
		ArrayList<IEntry> row;
		ArrayList<IEntry> kvEntries = new ArrayList<IEntry>();
		IEntry key = new StringEntry();
		IEntry value = new StringEntry();
		key.setName("Key");
		value.setName("Value");
		kvEntries.add(key);
		kvEntries.add(value);
		kvTable.setRowTemplate(kvEntries);

		for (String line : lines) {
			if (line.contains("=")) {
				String[] keyValue = line.split("=");
				int rowID = kvTable.addRow();
				row = kvTable.getRow(rowID);
				row.get(0).setValue(keyValue[0]);
				row.get(1).setValue(keyValue[1]);
			}
		}

		form.addComponent(kvTable);
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
		if (components != null && components.size() > 0 && outputFile.isFile()) {
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
				logger.error(getClass().getName() + " Exception!",e);
			}
		}

	}

	@Override
	public ArrayList<Entry> findAll(IFile file, String regex) {
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

}
