/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class inherits from JobLauncher and sets the test executable to the "ls"
 * command. It is only used for testing and for setting this executable command
 * name. Everything else is handled by the JobLauncher.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestJobLauncher extends JobLauncher {

	/**
	 * The Constructor
	 * 
	 * @param projectSpace
	 */
	public TestJobLauncher(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * This operation sets the executable name to ls
	 * 
	 * @see Item#setupForm()
	 */
	@Override
	protected void setupForm() {
		// begin-user-code

		// Setup the Form
		super.setupForm();

		// Setup the executable information
		setExecutable("ls", "A launcher for the " + "\"ls\" command. ", "ls");

		// Add a dummy host
		addHost("notlocalhost", "linux", "/bin");

		return;
		// end-user-code
	}

}