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
 * A CASL Item for launching jobs.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "CASLLauncher")
public class CASLLauncher extends JobLauncher {

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
	public CASLLauncher() {
		// begin-user-code
		// Forward
		this(null);
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
	public CASLLauncher(IProject projectSpace) {
		// begin-user-code
		// Forward up
		super(projectSpace);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation launches the CASL job.
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

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the form for the CASLLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// Local Declarations
		String copyStep = "cp /opt/denovo/daily/dev1.20130909.02-opt_mpi"
				+ "/bin/casl_comp.sh5 .;";
		String launchStep = "mpirun -np 16 --machinefile ~/.cores "
				+ "${installDir}/neutronics -i ${inputFile}";

		// Setup the Form
		super.setupForm();

		// Setup the executable information
		setExecutable(getName(), getDescription(), copyStep + launchStep);

		// Add a couple of hosts
		addHost("orthanc.ornl.gov", "linux x86_64",
				"/opt/denovo/daily/dev1.20130311.14-opt_mpi/bin");
		addHost("habilis.ornl.gov", "linux x86_64", "/usr/bin");
		addHost("u233.ornl.gov", "linux x86_64", "/usr/bin");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to setup the name and description of the launcher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Set the name and description of the Item
		setName(CASLLauncherBuilder.name);
		setDescription("VERA-CS is a nuclear reactor core simulator from "
				+ "CASL that is primarily focused on neutronics.");

		// end-user-code
	}
}