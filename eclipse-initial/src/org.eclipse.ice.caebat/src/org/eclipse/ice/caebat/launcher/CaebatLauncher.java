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
package org.eclipse.ice.caebat.launcher;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class inherits from JobLauncher form. It will create the Caebat launcher
 * so that it can remote execute the code.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 */
@XmlRootElement(name = "CaebatLauncher")
public class CaebatLauncher extends JobLauncher {

	// The executable command
	private String fullExecCMD;

	/**
	 * The default CAEBAT home directory.
	 */
	private String CAEBATHome;

	/**
	 * A nullary constructor that delegates to the project constructor.
	 */
	public CaebatLauncher() {
		this(null);
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. Takes an IProject argument. Calls the super constructor
	 * on JobLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param project
	 *            <p>
	 *            The project space.
	 *            </p>
	 */
	public CaebatLauncher(IProject project) {

		// begin-user-code

		// Call the JobLauncher constructor
		super(project);

		return;
		// end-user-code
	}

	/**
	 * This operations sets up some CAEBAT-specific information for the
	 * launcher, including the default project installation directory.
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Set the name and description of the Item
		setName("Caebat Launcher");
		setDescription("Caebat is a coupled battery and "
				+ "physics simulation from ORNL.");

		// Set the name of the home directory
		CAEBATHome = "/data1/projects/caebat/";

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides setupForm() on JobLauncher. It will setup the
	 * paths and add the locations for the remote server addresses. It will call
	 * super.setupForm() prior to setting up the executable and hostnames.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	public void setupForm() {
		// begin-user-code

		// Setup the Caebat's executable path.
		String CAEBATExec = CAEBATHome + "framework/bin/ips.py";

		// Setup the command stages. An explicit forward slash is used here, so
		// will only work on linux for now.
		fullExecCMD = CAEBATExec + " -a --log=temp.log --platform="
				+ CAEBATHome + "/workstation.conf --simulation=${inputFile}";

		// Setup form
		super.setupForm();

		// setup the executable information
		setExecutable(getName(), getDescription(), this.fullExecCMD);

		// Add localhost
		addHost("livingstone.ornl.gov", "linux x86_64", CAEBATExec);

		// Add the workstation conf

		// Add the input files types for the BatML files
		addInputType("Thermal Configuration File", "thermalFile",
				"Configuration file for the "
						+ "thermal properties of the battery", ".xml");
		addInputType("Electrochemical Configuration File", "electroChemFile",
				"Configuration file for the "
						+ "electrochemical properties of the battery", ".xml");
		addInputType("Electrical Configuration File", "electricalFile",
				"Configuration file for the "
						+ "electrical properties of the battery", ".xml");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides process by setting the executable correctly and then forwarding
	 * later. Still calls super.process(actionName) once the executable is set
	 * correctly for the workstation.conf file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param the
	 *            action name
	 * @return The status of the action
	 */
	public FormStatus process(String actionName) {

		// begin-user-code

		// Local Declarations
		DataComponent fileComponent = (DataComponent) form.getComponent(1);
		Entry inputFileEntry = fileComponent.retrieveEntry("Input File");
		InputStream fileStream;
		Scanner fileScanner;
		String next, runDir, runDirAppendix = "/work/THERMAL__Amperes_2/";
		String[] runIDArray;

		// Get the input file from the project
		IFile inputFile = project.getFile(inputFileEntry.getValue());
		try {
			// Load a scanner to read the file
			fileStream = inputFile.getContents();
			fileScanner = new Scanner(fileStream);
			fileScanner.useDelimiter(" = ");
			// Look for the run id by scanning the file
			while (fileScanner.hasNext()) {
				next = fileScanner.next();
				// Assign the run directory to the value in the ID and break out
				// of the loop
				if (next.contains("RUN_ID")) {
					runIDArray = fileScanner.next().split("\n");
					runDir = runIDArray[0] + runDirAppendix;
					// Set the remote download directory
					setRemoteDownloadDirectory(CAEBATHome + runDir);
					break;
				}
			}
			// Close the underlying stream in the scanner
			fileScanner.close();
			return super.process(actionName);
		} catch (CoreException e) {
			// Complain
			System.out.println("CAEBATLauncher Message: "
					+ "Unable to load input file to determine run directory.");
			e.printStackTrace();
		}

		// Unable to find the run directory. Return an error.
		return FormStatus.InfoError;

		// end-user-code

	}

}