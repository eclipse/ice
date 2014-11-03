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

import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A MOOSE Item for launching RELAP7 jobs.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "RELAP7Launcher")
public class RELAP7Launcher extends JobLauncher {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public RELAP7Launcher() {
		// begin-user-code
		super();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with a project space in which files should be
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
	public RELAP7Launcher(IProject projectSpace) {
		// begin-user-code
		super(projectSpace);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations sets up some RELAP7-specific information for the
	 * launcher, including the default project installation directory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Set the name and description of the Item
		setName("RELAP-7 Launcher");
		setDescription("RELAP-7 is a plant-level nuclear reactor system safety "
				+ "simulator from Idaho National Laboratory.");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the form for the RELAP7Launcher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// Setup the RELAP7 executable path.
		String relapExec = "../${installDir}r7_moose-opt -i ${inputFile}";
		// Note: Extra '../' added in front of the r7_moose dir as the ICE
		// Launcher
		// executes from a subdir of the user's home however, the user doesn't
		// necessarily know this in specifying the RELAP7 install path.

		// Have the job launcher setup the Form so it can be customized
		super.setupForm();

		// Setup the executable
		setExecutable(getName(), getDescription(), relapExec);

		// Setup the platforms
		addHost("localhost.localdomain", "linux", "../moose/trunk/r7_moose/");
		addHost("ergaster.ornl.gov", "linux", "../../bison/trunk/r7_moose/");

		// Enable MPI
		enableMPI(1, 10000, 1);

		// Enable TBB
		enableTBB(1, 256, 1);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation launches the RELAP-7 job.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of action that should be performed using the
	 *            processed Form data.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after processing the Form and executing
	 *         the action. It returns FormStatus.InfoError if it is unable to
	 *         run for any reason, including being asked to run actions that are
	 *         not in the list of available actions.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus process(String actionName) {
		// begin-user-code
		// Just forward the request to the super class. This operation is only
		// here in case we need to do some work before processing.
		return super.process(actionName);
		// end-user-code
	}

}
