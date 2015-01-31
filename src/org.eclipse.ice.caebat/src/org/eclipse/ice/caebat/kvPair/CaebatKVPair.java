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
package org.eclipse.ice.caebat.kvPair;

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
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.Item;

/**
 * This class is a Model Item that configures a set of data components that
 * represent key-value pairs for CAEBAT input.
 * 
 * @author Jay Jay Billings, Andrew Bennett
 * 
 */
@XmlRootElement(name = "CaebatKVPair")
public class CaebatKVPair extends Item implements IReader, IWriter {

	/**
	 * Keep track of everything that we can do with the KV Pair Item
	 */
	private ArrayList<String> actionItems;

	private String customTaggedExportString = "Export to key-value pair output";

	/**
	 * The nullary constructor.
	 */
	public CaebatKVPair() {
		this(null);
		return;
	}
	
	/**
	 * The required constructor.
	 * 
	 * @param projectSpace
	 *            The Eclipse Project space needed for file manipulation.
	 */
	public CaebatKVPair(IProject projectSpace) {
		// Punt to the base class.
		
		super(projectSpace);
		return;
	}

	/**
	 * This operation overrides the base class' operation to create a Form with
	 * Entries that will be used to generate the CAEBAT key-value pair file. It
	 * has one DataComponent per CAEBAT component.
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
	protected void setupItemInfo() {
		// Setup everything
		setName(CaebatKVPairBuilder.name);
		itemType = CaebatKVPairBuilder.type;
		setItemBuilderName(CaebatKVPairBuilder.name);
		setDescription("An item to generate CAEBAT " + "key-value pair files.");
		allowedActions.remove("Export to ICE Native Format");
		actionItems = getAvailableActions();
	}

	protected FormStatus reviewEntries(Form preparedForm) {

		// begin-user-code
		FormStatus retStatus = FormStatus.ReadyToProcess;

		// Grab the data component from the Form and only proceed if it exists
		ArrayList<Component> components = preparedForm.getComponents();

		// Make sure the form has the right amount of data
		if (components.size() == 0) {
			System.out
					.println("Caebat KV Pair Generator Message: Could not find any data to write out");
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
	 * @return
	 */
	public FormStatus process(String actionName) {
		// begin-user-code
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
					System.err.println("CaebatKVPair Generator Message: "
							+ "Failed to refresh the project space.");
					e.printStackTrace();
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
		// end-user-code

	}

	/**
	 * <p>
	 * This operation loads the given example into the Form.
	 * </p>
	 * 
	 * @param name
	 *            The path name of the example file name to load.
	 */
	public void loadInput(String name) {

		// If nothing is specified, load case 6 from inside the plugin
		IFile inputFile = null;
		File temp = null;
		System.out.println("Going to go for branch name = " + name);
		if (name == null) {
			try {
				// Path to the default file
				String defaultFilePath = null;
				// Create a filepath for the default file
				if (project != null) {
					System.out.println("Took branch at line 223");
					defaultFilePath = project.getLocation().toOSString()
							+ System.getProperty("file.separator")
							+ "case_6.dat";
					System.out.println(defaultFilePath);
				} else {
					System.out.println("Took branch at line 226");
					defaultFilePath = ResourcesPlugin.getWorkspace().getRoot()
							.getLocation().toOSString()
							+ System.getProperty("file.separator") + "case_6.dat";
					System.out.println(defaultFilePath);
				}

				// Create a temporary location to load the default file
				temp = new File(defaultFilePath);
				if (!temp.exists()) {
					temp.createNewFile();
				}

				// Pull the default file from inside the plugin
				URI uri = new URI(
						"platform:/plugin/org.eclipse.ice.caebat/data/case_6.dat");
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
					System.out.println("Grabbing file from project at line 250 " + project.getLocation().toOSString());
					inputFile = project.getFile("case_6.dat");
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} else {
					System.out.println("Grabbing file from project at line 253 " + ResourcesPlugin.getWorkspace().getRoot()
							.getFile(new Path(defaultFilePath)).getLocation().toOSString());
					inputFile = ResourcesPlugin.getWorkspace().getRoot()
							.getFile(new Path(defaultFilePath));
				}

			} catch (URISyntaxException e) {
				System.err
						.println("CaebatKVPair Message: Error!  Could not load the default"
								+ " Caebat case data!");
			} catch (MalformedURLException e) {
				System.err
						.println("CaebatKVPair Message: Error!  Could not load the default"
								+ " Caebat case data!");
			} catch (IOException e) {
				System.err
						.println("CaebatKVPair Message: Error!  Could not load the default"
								+ " Caebat case data!");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Get the file
			inputFile = project.getFile(name);
		}

		// Load the components from the file and setup the form
		System.out.println("CaebatKVPair Message: Loading"
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
			System.out
					.println("CaebatKVPair Message: Error!  Could not find file for loading.");
			return null;
		} catch (IOException e) {
			System.out
					.println("CaebatKVPair Message: Error!  Trouble reading file.");
			return null;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			System.out.println("CaebatKVPair Message: Error!  Trouble reading file from project location.");
			return null;
		}

		//
		TableComponent kvTable = new TableComponent();
		ArrayList<Entry> row;
		ArrayList<Entry> kvEntries = new ArrayList<Entry>();
		Entry key = new Entry();
		Entry value = new Entry();
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
				ArrayList<Entry> row;
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
				System.out.println("CaebatKVPair Message: Could not find "
						+ outputFile.getAbsolutePath() + " for writing.");
			} catch (IOException e) {
				System.out.println("CaebatKVPair Message: Could not write to "
						+ outputFile.getAbsolutePath() + ".");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	 * Return that this is a CaebatKVPairItem
	 */
	@Override
	public String getWriterType() {
		return "CaebatKVPairItem";
	}

	/**
	 * Return that this is a CaebatKVPairItem
	 */
	@Override
	public String getReaderType() {
		return "CaebatKVPairItem";
	}

}
