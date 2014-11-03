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

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.jobLauncher.SuiteLauncher;

/**
 * A SuiteLauncher Item for all MOOSE products (MARMOT, BISON, RELAP-7, RAVEN).
 * The MOOSE framework is developed by Idaho National Lab.
 * 
 * @author w5q
 * 
 */

@XmlRootElement(name = "MOOSELauncher")
public class MOOSELauncher extends SuiteLauncher {

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
		String localInstallDir = "/home/moose";
		String remoteInstallDir = "/home/bison";

		// Create the list of executables
		ArrayList<String> executables = new ArrayList<String>();
		executables.add("MARMOT");
		executables.add("BISON");
		executables.add("RELAP-7");
		executables.add("RAVEN");
		executables.add("MOOSE_TEST");

		// Add the list to the suite
		addExecutables(executables);

		// Setup the Form
		super.setupForm();

		// Add hosts
		addHost("localhost", "linux", localInstallDir);
		addHost("ergaster.ornl.gov", "linux", remoteInstallDir);

		// Enable MPI
		enableMPI(1, 10000, 1);

		// Enable TBB
		enableTBB(1, 256, 1);

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

		// A HashMap of MOOSE product executable names. Some MOOSE products
		// are named differently in the MOOSE file structure than their
		// colloquial name (ie. RELAP-7 is in a folder called r7_moose)
		HashMap<String, String> executableMap = new HashMap<String, String>();
		executableMap.put("MARMOT", "marmot");
		executableMap.put("BISON", "bison");
		executableMap.put("RELAP-7", "r7_moose");
		executableMap.put("RAVEN", "raven");
		executableMap.put("MOOSE_TEST", "moose_test");

		// Create the command that will launch the MOOSE product
		String launchCommand;
		if ("RAVEN".equals(executable)) {
			// If we're dealing with RAVEN, the executable directory is in
			// lowercase, ("raven"), but the executable is in caps ("RAVEN")
			launchCommand = "${installDir}" + executableMap.get(executable)
					+ "/" + executable + "-opt -i ${inputFile}";
		} else if ("MOOSE_TEST".equals(executable)) {
			launchCommand = "${installDir}" + executableMap.get(executable)
					+ "-opt -i ${inputFile}";
		} else {
			launchCommand = "${installDir}" + executableMap.get(executable)
					+ "/" + executableMap.get(executable)
					+ "-opt -i ${inputFile}";
		}

		// Appending this arguement to resolve some output formatting issues
		// that occur when MOOSE output is redirected.
		launchCommand += " --no-color";

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
	 * in the event that the BISON executable is chosen, in which case
	 * additional files (mesh, power history, peaking factors) will need to be
	 * specified by the client. This method will toggle the additional input
	 * file menus on and off depending on the selected executable.
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

			// Grab the name of the current executable selected by the client
			String execName = execDataComp.retrieveAllEntries().get(0)
					.getValue();

			// Check the DataComponent is valid
			if (execDataComp != null
					&& "Available Executables".equals(execDataComp.getName())) {

				// Grab the DataComponent responsible for managing Input Files
				DataComponent inputFilesComp = (DataComponent) form
						.getComponent(1);
				int numInputFiles = inputFilesComp.retrieveAllEntries().size();
				ArrayList<String> inputFileNames = new ArrayList<String>();
				for (int i = 0; i < numInputFiles; i++) {
					inputFileNames.add(inputFilesComp.retrieveAllEntries()
							.get(i).getName());
				}

				// If the current executable is BISON, remove RAVEN inputs (if
				// any) and
				// specify additional fuel files will need to be added to the
				// form.
				if ("BISON".equals(execName)) {

					// Delete RAVEN input files (if any)
					if (inputFileNames.contains("Control Logic")) {
						inputFilesComp.deleteEntry("Control Logic");
					}

					// Replace the Form's Input Files DataComponent
					form.removeComponent(1);
					form.addComponent(inputFilesComp);

					// Add new input types
					addInputType("Mesh", "meshFile", "Fuel pin mesh file.",
							".e");
					addInputType(
							"Power History",
							"powerHistoryFile",
							"Input file containing average rod input power over time.",
							".csv");
					addInputType("Peaking Factors", "peakingFactorsFile",
							"An input file containing the axial power profile as a "
									+ "function of time.", ".csv");
				}

				// Else if the current executable is RAVEN, remove BISON inputs
				// (if any) and specify an additional control logic file that
				// will need to be added to the form.
				else if ("RAVEN".equals(execName)) {

					// Delete BISON input files (if any)
					if (inputFileNames.contains("Mesh")) {
						inputFilesComp.deleteEntry("Mesh");
					}
					if (inputFileNames.contains("Power History")) {
						inputFilesComp.deleteEntry("Power History");
					}
					if (inputFileNames.contains("Peaking Factors")) {
						inputFilesComp.deleteEntry("Peaking Factors");
					}

					// Replace the Form's Input Files DataComponent
					form.removeComponent(1);
					form.addComponent(inputFilesComp);

					// Add new input types
					addInputType("Control Logic", "logicFile", "Python control"
							+ "logic input file.", ".py");
				} else {
					// If the Input Files DataComponent is larger than 1,
					// it means the additional BISON or RAVEN input files are
					// loaded
					// from an earlier time and all need to be removed
					if (numInputFiles > 1) {

						// Delete all extra input files
						if (inputFileNames.contains("Mesh")) {
							inputFilesComp.deleteEntry("Mesh");
						}
						if (inputFileNames.contains("Power History")) {
							inputFilesComp.deleteEntry("Power History");
						}
						if (inputFileNames.contains("Peaking Factors")) {
							inputFilesComp.deleteEntry("Peaking Factors");
						}
						if (inputFileNames.contains("Control Logic")) {
							inputFilesComp.deleteEntry("Control Logic");
						}

						// Replace the Form's Input Files DataComponent
						form.removeComponent(1);
						form.addComponent(inputFilesComp);
					}
				}

			}

			else {
				retStatus = FormStatus.InfoError;
			}
		}

		return retStatus;
	}
}
