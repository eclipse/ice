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

import java.io.InputStream;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.core.resources.IProject;

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
	private String CAEBAT_ROOT;

	/**
	 * The default IPS home directory.
	 */
	private String IPS_ROOT;

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
		CAEBAT_ROOT = "/home/batsim/caebat";
		IPS_ROOT = "$IPS_ROOT";

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
		
		// Setup the script to copy the data files for case 6
		//TableComponent hostTable = (TableComponent) form.getComponent(4);
		//CAEBAT_ROOT = hostTable.getRow(0).get(2).getValue();	
		//String exportRoot = "export CAEBAT_ROOT=" + CAEBAT_ROOT + ";";
		//String copyCase = "source `pwd`/${inputFile} >> /dev/null && cp -r $SIM_ROOT/* .;";
		
		String copyCase = "cp -r ${installDir}vibe/examples/case6/* .;";
		String fixSIMROOT = "sed -i.bak 's?SIM_ROOT\\ =\\ .*?"
				+ "SIM_ROOT\\ =\\ '`pwd`'?g' ${inputFile};";
		// Setup the Caebat's launch script
		String CAEBATExec = "${installDir}ipsframework-code/install/bin/ips.py"
				+ " -a --log=temp.log --platform=" + IPS_ROOT
				+ "/workstation.conf --simulation=${inputFile};";
		
		// Setup the command stages. An explicit forward slash is used here, so
		// will only work on linux for now.
		fullExecCMD =  copyCase + fixSIMROOT + CAEBATExec;
		// Setup form
		super.setupForm();

		// Stop the launcher from trying to append the input file
		setAppendInputFlag(false);
		
		// Setup the executable information
		setExecutable(getName(), getDescription(), this.fullExecCMD);

		// Add localhost
		addHost("localhost", "linux x86_64", CAEBAT_ROOT);

		// Add the input files types for the BatML files
		addInputType("Key-value pair file", "keyValueFile",
				"Key-value pair with case parameters", ".dat");

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

		return super.process(actionName);

		// end-user-code

	}
}