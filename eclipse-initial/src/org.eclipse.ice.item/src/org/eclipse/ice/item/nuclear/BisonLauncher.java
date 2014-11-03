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
package org.eclipse.ice.item.nuclear;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.core.resources.IProject;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A MOOSE Item for launching Bison jobs.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "BisonLauncher")
public class BisonLauncher extends JobLauncher {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BisonLauncher() {
		// begin-user-code
		super();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BisonLauncher(IProject projectSpace) {
		// begin-user-code
		super(projectSpace);

		return;
		// end-user-code
	}

	/**
	 * This operations sets up some Bison-specific information for the launcher,
	 * including the default project installation directory.
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Set the name and description of the Item
		setName("Bison Launcher");
		setDescription("Bison is a fuel performance simulator "
				+ "from Idaho National Laboratory.");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the form for the MOOSELauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// Setup the Bison's executable path.
		String bisonExecPath = "../moose/trunk/bison/bison-opt";
		String bisonExec = bisonExecPath + " -i ${inputFile}";
		String space = " ";

		// Setup the full launch command. Bison only runs on Linux, so this
		// command is Linux-specific.
		String fullExecCMD = bisonExec;

		// Have the job launcher setup the Form so it can be customized
		super.setupForm();

		// Setup the executable
		setExecutable(getName(), getDescription(), fullExecCMD);

		// Setup the platforms
		addHost("localhost.localdomain", "linux", bisonExecPath);
		addHost("habilis.ornl.gov", "linux", bisonExecPath);

		// Add the input files types for the fuel files
		addInputType("Mesh", "meshFile", "Fuel pin mesh file.", ".e");
		addInputType(
				"Power History",
				"powerHistoryFile",
				"Input file containing average " + "rod input power over time.",
				".csv");
		addInputType("Peaking Factors", "peakingFactorsFile",
				"An input file containing the "
						+ "axial power profile as a function of time.", ".csv");

		// Enable MPI
		enableMPI(1, 10000, 1);

		// Enable TBB
		enableTBB(1, 256, 1);

		return;
		// end-user-code
	}

}