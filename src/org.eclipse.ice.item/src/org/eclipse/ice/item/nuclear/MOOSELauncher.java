/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
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
package org.eclipse.ice.item.nuclear;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.item.jobLauncher.SuiteLauncher;

/**
 * A SuiteLauncher Item for all MOOSE products (MARMOT, BISON, RELAP-7, RAVEN).
 * The MOOSE framework is developed by Idaho National Lab.
 * 
 * @author Anna Wojtowicz
 */

@XmlRootElement(name = "MOOSELauncher")
public class MOOSELauncher extends SuiteLauncher implements IUpdateableListener {

	/**
	 * The currently selected MOOSE application. Set by reviewEntries().
	 */
	private String execName = "";
	
	/**
	 * The currently selected *.i input file.
	 */
	private String inputFileName = "";

	/**
	 * The name of the YAML/action syntax generator
	 */
	private static final String yamlSyntaxGenerator = "Generate YAML/action syntax";

	/**
	 * The name of the custom MOOSE executable.
	 */
	private static final String customExecName = "Custom executable name";
	
	/**
	 * The DataComponent on the form that is used to store the list of
	 * executable apps. This DataComponent contains two Entries: [0] The Entry 
	 * containing BISON, MARMOT, RAVEN and RELAP-7, and [1] An Entry of type
	 * Undefined in which the user can enter their own MOOSE app name.
	 */
	private DataComponent execDataComp;
	
	/**
	 * Nullary constructor.
	 */
	public MOOSELauncher() {
		this(null);
	}

	/**
	 * Parameterized constructor.
	 */
	public MOOSELauncher(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * Overriding setupForm to set the executable names and host information.
	 */
	@Override
	protected void setupForm() {

		// Local Declarations
		String userHome = System.getProperty("user.home");
		String separator = System.getProperty("file.separator");

		String localInstallDir = userHome + separator + "projects";
		String remoteInstallDir = "/home/moose";

		// Create the list of executables
		ArrayList<String> executables = new ArrayList<String>();
		executables.add("MARMOT");
		executables.add("BISON");
		executables.add("RELAP-7");
		executables.add("RAVEN");
		executables.add("MOOSE_TEST");
		executables.add(customExecName);
		//executables.add(yamlSyntaxGenerator);

		// Add the list to the suite
		addExecutables(executables);
		
		// Setup the Form
		super.setupForm();
		
		// Get a handle on the "executables" DataComponent
		execDataComp = (DataComponent) form.getComponent(5);
		// Register the launcher as a listener of the executables DataComponent
		execDataComp.register(this);
		
		// Create an entry for a "custom" MOOSE executable
		Entry customExecEntry = new Entry();
		customExecEntry.setName(customExecName);
		customExecEntry.setDescription("A custom MOOSE-based executable. Note "
				+ "that this field is case-sensitive and should be entered as "
				+ "it appears in the filesystem.");
		customExecEntry.setId(2);
		customExecEntry.setParent(execDataComp.retrieveAllEntries().get(0).getName());
		customExecEntry.setReady(false);
		
		// Add it to the form
		execDataComp.addEntry(customExecEntry);

		// Grab the DataComponent responsible for managing Input Files
		DataComponent inputFilesComp = (DataComponent) form.getComponent(1);
		// Set the input file to only *.i files (to reduce workspace clutter)
		inputFilesComp.deleteEntry("Input File");
		addInputType("Input File", "inputFile",
				"The MOOSE input file that defines the problem.", ".i");

		// Add hosts
		addHost("localhost", "linux", localInstallDir);
		addHost("habilis.ornl.gov", "linux", remoteInstallDir);

		// Enable MPI
		enableMPI(1, 10000, 1);

		// Enable TBB
		enableTBB(1, 256, 1);

		// Register this MooseLauncher as a listener of the Input File Entry. 
		// When it is set to something we can react with a search of related 
		// moose files.
		inputFilesComp.retrieveEntry("Input File").register(this);

		// Go ahead and create the list of files related to the Input File
		if (!inputFilesComp.retrieveEntry("Input File").getValue().isEmpty()
				&& inputFilesComp.retrieveEntry("Input File").getValue()
						.contains(".i") && getReader() != null) {
			update(inputFilesComp.retrieveEntry("Input File"));
		}

		return;
	}

	/**
	 * Overrides the base class operation to properly account for MOOSE's file
	 * structure.
	 * 
	 * @param installDir
	 *            The installation directory of MOOSE.
	 * @param executable
	 *            The name of the executable selected by a client.
	 * @return The complete launch command specific for a given MOOSE product,
	 *         determined by the executable name selected by the client.
	 */
	@Override
	protected String updateExecutablePath(String installDir, String executable) {

		// Local declarations
		Entry customExecEntry = execDataComp.retrieveEntry(customExecName);
		String customExecValue = "";
		if (execDataComp != null && customExecEntry != null 
				&& customExecEntry.getValue() != null) {
			customExecValue = 
					execDataComp.retrieveEntry(customExecName).getValue();
		}
		
		// A HashMap of MOOSE product executables that can be launched
		HashMap<String, String> executableMap = new HashMap<String, String>();
		executableMap.put("MARMOT", "marmot");
		executableMap.put("BISON", "bison");
		executableMap.put("RELAP-7", "relap-7");
		executableMap.put("RAVEN", "raven");
		executableMap.put("MOOSE_TEST", "moose_test");
		executableMap.put(customExecName, customExecValue);
		executableMap.put(yamlSyntaxGenerator, yamlSyntaxGenerator);

		// Create the command that will launch the MOOSE product
		String launchCommand = null;
		setUploadInputFlag(true);

		if ("MOOSE_TEST".equals(executable)) {
			launchCommand = "${installDir}" + "moose/test/"
					+ executableMap.get(executable)
					+ "-opt -i ${inputFile} --no-color";
		} else if (yamlSyntaxGenerator.equals(executable)) {
			launchCommand =
					// BISON files
			"if [ -d ${installDir}bison ] "
					+ "&& [ -f ${installDir}bison/bison-opt ]\n then\n"
					+ "    ${installDir}bison/bison-opt --yaml > bison.yaml\n"
					+ "    ${installDir}bison/bison-opt --syntax > bison.syntax\n"
					+ "    echo 'Generating BISON files'\n"
					+ "fi\n"
					// MARMOT files
					+ "if [ -d ${installDir}marmot ] "
					+ "&& [ -f ${installDir}marmot/marmot-opt ]\n then\n"
					+ "    ${installDir}marmot/marmot-opt --yaml > marmot.yaml\n"
					+ "    ${installDir}marmot/marmot-opt --syntax > marmot.syntax\n"
					+ "    echo 'Generating MARMOT files'\n"
					+ "fi\n"
					// RELAP-7 files
					+ "if [ -d ${installDir}relap-7 ] "
					+ "&& [ -f ${installDir}relap-7/relap-7-opt ]\n then\n"
					+ "    ${installDir}relap-7/relap-7-opt --yaml > relap.yaml\n"
					+ "    ${installDir}relap-7/relap-7-opt --syntax > relap.syntax\n"
					+ "    echo 'Generating RELAP-7 files'\n"
					+ "elif [ -d ${installDir}r7_moose ] " // Old name
					+ "&& [ -f ${installDir}r7_moose/r7_moose-opt ]\n then\n"
					+ "    ${installDir}r7_moose/r7_moose-opt --yaml > relap.yaml\n"
					+ "    ${installDir}r7_moose/r7_moose-opt --syntax > relap.syntax\n"
					+ "    echo 'Generating RELAP-7 files'\n"
					+ "fi\n"
					// RAVEN files
					+ "if [ -d ${installDir}raven ] "
					+ "&& [ -f ${installDir}raven/RAVEN-opt ]\n then\n"
					+ "    ${installDir}raven/RAVEN-opt --yaml > raven.yaml\n"
					+ "    ${installDir}raven/RAVEN-opt --syntax > raven.syntax\n"
					+ "    echo 'Generating RAVEN files'\n" + "fi\n";
		} else if ("RAVEN".equals(executable)) {
			// RAVEN directory is lowercase, but the executable is uppercase
			launchCommand = "${installDir}" + executableMap.get(executable)
					+ "/" + executable + "-opt -i ${inputFile} --no-color";

		} else {
			// BISON, MARMOT, RELAP-7 and (presumably) custom apps follow the 
			// same execution pattern
			launchCommand = "${installDir}" + executableMap.get(executable)
					+ "/" + executableMap.get(executable)
					+ "-opt -i ${inputFile} --no-color";
		}

		return launchCommand;
	}

	/**
	 * Sets the information that identifies the Item.
	 */
	protected void setupItemInfo() {

		// Local declarations
		String description = "The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed "
				+ "by Idaho National Laboratory.";

		// Set the model defaults
		setName(MOOSELauncherBuilder.name);
		setDescription(description);
		setItemBuilderName(MOOSELauncherBuilder.name);

		return;
	}

	/**
	 * This operation overrides Item.reviewEntries(). This override is required
	 * in the event that the YAML/action syntax generator is selected, in which
	 * case certain JobLaunch flags (related to file uploading) must be turned
	 * off. Conversely, these flags must be turned back on for any other
	 * executable.
	 * 
	 * @param preparedForm
	 *            The Form to review.
	 * @return The Form's status.
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {

		// Local declaration
		FormStatus retStatus = null;

		// Call the super's status review first
		retStatus = super.reviewEntries(preparedForm);

		// If the super's status review was successful, keep going
		if (!retStatus.equals(FormStatus.InfoError)) {

			if (execDataComp != null) {
				// Grab the name of the current executable selected by the
				// client
				execName = execDataComp.retrieveAllEntries().get(0).getValue();
			}

			// Check the DataComponent and app selection is valid
			if ("Available Executables".equals(execDataComp.getName())) {

				// Set this back to true in case it's been changed by the YAML/
				// action syntax generator
				setUploadInputFlag(true);
				
				if (yamlSyntaxGenerator.equals(execName)) {
					// Disable input file appending (no input file to append)
					setAppendInputFlag(false);
					// Disable input file uploading
					setUploadInputFlag(false);
				}
				
				retStatus = FormStatus.ReadyToProcess;
								
			} else {
				retStatus = FormStatus.InfoError;
			}
		}

		return retStatus;
	}

	/**
	 * Override of the JobLauncher.updateResourceComponent() method to also
	 * process the downloaded *.yaml and *.syntax files after the super method
	 * is executed. Any extraneous header/footer text is removed, and the
	 * resulting file is placed in the default/MOOSE folder (which is created,
	 * if it doesn't already exist). Any old *.yaml and *.syntax files in the
	 * MOOSE directory will be overwritten.
	 */
	@Override
	protected void updateResourceComponent() {
		// Call the super
		super.updateResourceComponent();

		// Get the working directory for the job launch
		String workingDirectory = getWorkingDirectory();

		// If this is the YAML/action syntax process, we need a few extra steps
		if (yamlSyntaxGenerator.equals(execName)) {

			// Get the MOOSE folder
			IFolder mooseFolder = project.getFolder("MOOSE");

			// Check if the MOOSE folder exists; create it if it doesn't
			if (!mooseFolder.exists()) {
				try {
					mooseFolder.create(true, true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}

			// Get all files that end with .yaml or .syntax and move them from
			// the
			// MooseLauncher working directory to the MOOSE folder.
			moveFiles(workingDirectory, mooseFolder.getLocation().toOSString(),
					".yaml");
			moveFiles(workingDirectory, mooseFolder.getLocation().toOSString(),
					".syntax");

			// Clean up the MOOSE yaml/syntax files.
			try {
				mooseFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
				for (IResource resource : mooseFolder.members()) {
					if (resource.getType() == IResource.FILE
							&& resource.getProjectRelativePath().lastSegment()
									.contains(".yaml")
							|| resource.getProjectRelativePath().lastSegment()
									.contains(".syntax")) {
						createCleanMOOSEFile(resource.getLocation()
								.toOSString());

					}
				}

			} catch (CoreException | IOException e) {
				e.printStackTrace();
			}

			// Since this is just the GenerateYAML Executable, we don't
			// really need to keep the working directory, so delete it here.
			deleteDirectory(workingDirectory);

			// Refresh the project space
			refreshProjectSpace();
		}

	}

	/**
	 * This method is intended to take a filePath corresponding to a MOOSE YAML
	 * or action syntax file, and remove any extraneous header or footer lines
	 * that aren't valid syntax. If any lines from the file were removed, it
	 * re-writes the file. If no changes were made (no header/footer to remove),
	 * it does nothing.
	 * 
	 * @param filePath
	 *            The filepath to the YAML or action syntax file.
	 * @throws IOException
	 * @throws CoreException
	 */
	public void createCleanMOOSEFile(String filePath) throws IOException,
			CoreException {

		// Local declarations
		String fileExt, execName, fileType = null;
		boolean hasHeader = false, hasFooter = false;
		int headerLine = 0, footerLine = 0;
		String separator = System.getProperty("file.separator");
		ArrayList<String> fileLines;

		// Check if the MOOSE folder exists; create it if it doesn't
		IFolder mooseFolder = project.getFolder("MOOSE");

		// If the MOOSE folder doesn't exist, create it
		if (!mooseFolder.exists()) {
			mooseFolder.create(true, true, null);
		}

		// Define where the "clean" MOOSE file will be written
		fileExt = filePath.substring(filePath.lastIndexOf("."));
		execName = filePath.substring(filePath.lastIndexOf(separator) + 1,
				filePath.lastIndexOf(fileExt));
		String cleanFilePath = filePath.substring(0,
				filePath.lastIndexOf(separator))
				+ separator + execName + fileExt;

		if (".yaml".equals(fileExt)) {
			fileType = "YAML";
		} else if (".syntax".equals(fileExt)) {
			fileType = "SYNTAX";
		} else {
			System.out.println("MOOSEFileHandler message: File does not have "
					+ "vaid file extension. Must be .yaml or .syntax but is "
					+ fileExt);
		}

		// Read in the MOOSE file into an ArrayList of Strings
		java.nio.file.Path readPath = Paths.get(filePath);
		fileLines = (ArrayList<String>) Files.readAllLines(readPath,
				Charset.defaultCharset());

		// Define what the header/footer lines look like
		String header = "**START " + fileType + " DATA**";
		String footer = "**END " + fileType + " DATA**";

		// Determine if there is a header and/or footer
		hasHeader = fileLines.contains(header);
		hasFooter = fileLines.contains(footer);

		// Cut off the footer, if there is one
		if (hasFooter) {

			// Record the line number of the footer
			footerLine = fileLines.indexOf(footer);

			// Remove the footer line and anything after it
			int i = footerLine;
			while (i < fileLines.size()) {
				fileLines.remove(i);
			}
		}

		// Cut off the header, if there is one
		if (hasHeader) {

			// Record the line number
			headerLine = fileLines.indexOf(header);

			// Remove the header line and anything before it
			for (int i = headerLine; i >= 0; i--) {
				fileLines.remove(i);
			}
		}

		// If there was any changes made to the file, write it out and replace
		// the original one
		if (hasHeader || hasFooter) {

			// If there's an already existing file to where we want to write,
			// get rid of it
			IFile cleanFile = mooseFolder.getFile(execName + fileExt);
			if (cleanFile.exists()) {
				cleanFile.delete(true, null);
			}

			// Write out to the clean file now
			java.nio.file.Path writePath = Paths.get(cleanFilePath);
			Files.write(writePath, fileLines, Charset.defaultCharset(),
					StandardOpenOption.CREATE);
			System.out.println("MOOSELauncher Message: "
					+ "Placing file in /ICEFiles/default/MOOSE: " + execName
					+ fileExt);

			// Delete the old file
			// File oldFile = new File(filePath);
			// oldFile.delete();
		}

		return;
	}

	/**
	 * This method provides a implementation of the IUpdateable interface that
	 * listens for changes in objects that are registered with this MOOSE
	 * Launcher. Primarily, it will toggle a "custom MOOSE executable" on and
	 * off, in addition reducing the number of updates that are called on the
	 * "Input File" Entry if it's value hasn't changed.
	 */
	@Override
	public void update(IUpdateable component) {
		
		// If the component is a DataComponent, then toggle the custom MOOSE
		// app entry on/off
		if (component instanceof DataComponent) {
			
			// Grab the name of the current executable selected by the user
			if (execDataComp != null) {
				execName = execDataComp.retrieveAllEntries().get(0).getValue();
			}
			
			Entry parentEntry = execDataComp.retrieveEntry("Executable");
			Entry customExecEntry = execDataComp.retrieveEntry(customExecName);
			
			if (execName.equals(customExecName) && !customExecEntry.isReady()) {
				// Reveal the custom app Entry if it's currently hidden and 
				// the user wants to enter a name
				customExecEntry.update(parentEntry.getName(), "true");
			} else if (!execName.equals(customExecName) && customExecEntry.isReady()) {
				// Hide the custom app Entry if it's exposed and the user
				// selected another app
				customExecEntry.update(parentEntry.getName(), "false");
			}
		} else {
			
			if (component instanceof Entry) {

				// Check if this is the Input File entry and has a valid value
				Entry entry = (Entry) component;
				if (entry.getName().equals("Input File") && !entry.getValue().isEmpty()) {
					
					// First, check if the file extension on the value is valid
					if (!entry.getValue().contains(".i")) {
						// Complain and exit
						System.out.println("MOOSELauncher Message: Input files"
								+ "must have a *.i extension!");
						return;
					}
					
					// Check if the input file name has changed
					if (!entry.getValue().equals(inputFileName)) {
						// Set the new reference value
						inputFileName = entry.getValue();
					} else {
						// If the file name hasn't changed, just stop here
						// (otherwise super.update will be called, which will
						// redundantly re-search the whole file for dependencies
						// and re-draw them)
						return;
					}
				}
			}
			
			super.update(component);
		}
		
		return;		
	}
	
	@Override
	protected String getFileDependenciesSearchString() {
		return "file";
	}

	/**
	 * @see org.eclipse.ice.item.Item#getIOType()
	 */
	@Override
	public String getIOType() {
		return "moose";
	}
}
