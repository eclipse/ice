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
package org.eclipse.ice.item.nuclear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.eclipse.ice.item.jobLauncher.SuiteLauncher;

/**
 * A SuiteLauncher Item for all MOOSE products (MARMOT, BISON, RELAP-7, RAVEN).
 * The MOOSE framework is developed by Idaho National Lab.
 * 
 * @author w5q
 * 
 */

@XmlRootElement(name = "MOOSELauncher")
public class MOOSELauncher extends SuiteLauncher implements IUpdateableListener {

	/**
	 * The currently selected MOOSE application. Set by reviewEntries().
	 */
	private String execName = "";

	/**
	 * The name of the YAML/action syntax generator
	 */
	private static final String yamlSyntaxGenerator = "Generate YAML/action syntax";

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
		executables.add("PUMA");

		// executables.add(yamlSyntaxGenerator);

		// Add the list to the suite
		addExecutables(executables);

		// Setup the Form
		super.setupForm();

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

		// Register this MooseLauncher as a listener of the
		// Input File Entry. When it is set to something we can react
		// with a search of related moose files.
		inputFilesComp.retrieveEntry("Input File").register(this);

		// Go ahead and create the list of files related to the Input File
		if (!inputFilesComp.retrieveEntry("Input File").getValue().isEmpty()
				&& inputFilesComp.retrieveEntry("Input File").getValue()
						.contains(".i") && getReader() != null) {
			update(inputFilesComp.retrieveEntry("Input File"));
		}

		execEntry.register(this);

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

		// A HashMap of MOOSE product executables that can be launched
		HashMap<String, String> executableMap = new HashMap<String, String>();
		executableMap.put("MARMOT", "marmot");
		executableMap.put("BISON", "bison");
		executableMap.put("RELAP-7", "relap-7");
		executableMap.put("RAVEN", "raven");
		executableMap.put("MOOSE_TEST", "moose_test");
		executableMap.put("PUMA", "puma");
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
			// BISON, MARMOT and RELAP-7 following the same execution pattern
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

			// Grab the DataComponent in the from that lists available
			// executables
			DataComponent execDataComp = (DataComponent) preparedForm
					.getComponent(5);

			if (execDataComp != null) {
				// Grab the name of the current executable selected by the
				// client
				execName = execDataComp.retrieveAllEntries().get(0).getValue();
			}

			// Check the DataComponent is valid
			if ("Available Executables".equals(execDataComp.getName())) {

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
	private void createCleanMOOSEFile(String filePath) throws IOException,
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

	@Override
	public void update(IUpdateable component) {

		refreshProjectSpace();

		//if (component instanceof Entry) {

		//	Entry incomingEntry = (Entry) component;
			
			
			
			super.update(component);

			if (execEntry.getValue().equals("PUMA")) {

				DataComponent fileData = (DataComponent) form
						.getComponent(JobLauncherForm.filesId);
				Entry proteusFile = fileData
						.retrieveEntry("external_code_input_file");
				if (proteusFile != null) {
					IFile file = project.getFile(proteusFile.getValue());
					if (file.exists()) {
						try {
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(file.getContents()));
							String line = "";

							while ((line = reader.readLine()) != null) {
								if (line.contains("FILE")
										&& !line.startsWith("!")
										&& !line.contains("EXPORT")) {
									line = line.replaceAll("\\s+", " ");
									String[] split = line.split(" ");
									System.out.println("LINE: " + line + " "
											+ split.length + " " + split[0]
											+ " " + split[1]);
									addInputType(
											split[0],
											split[0].replaceAll(" ", ""),
											"FILE DESCRIPTION",
											"."
													+ split[1]
															.split("\\.(?=[^\\.]+$)")[1]);
								}
							}
						} catch (CoreException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		//}
	}

	/**
	 * 
	 */
	@Override
	protected String getFileDependenciesSearchString() {
		return "file"; // FIXME THIS SHOULD BE REPLACED WITH REG EXP
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#getIOType()
	 */
	@Override
	public String getIOType() {
		return "moose";
	}
}
