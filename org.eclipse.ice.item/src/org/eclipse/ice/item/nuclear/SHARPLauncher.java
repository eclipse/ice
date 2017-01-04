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

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.january.form.FormStatus;

/**
 * <p>
 * A SHARP Item for launching jobs.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "SHARPLauncher")
public class SHARPLauncher extends JobLauncher {

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 */
	public SHARPLauncher() {
		// Forward
		this(null);
	}

	/**
	 * <p>
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * </p>
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 *            </p>
	 */
	public SHARPLauncher(IProject projectSpace) {
		// Forward up
		super(projectSpace);
	}

	/**
	 * <p>
	 * This operation launches the SHARP job.
	 * </p>
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
	 */
	@Override
	public FormStatus process(String actionName) {
		// Just forward the request to the super class. This operation is only
		// here in case we need to do some work before processing.
		return super.process(actionName);
	}

	/**
	 * <p>
	 * This operation sets up the form for the SHARPLauncher.
	 * </p>
	 * 
	 */
	@Override
	protected void setupForm() {

		// Local Declarations
		String cpDataStep = "cp ${installDir}sahex1/* .;";
		String relinkStep = "ln -s ${inputFile} unic.inp;";
		String launchStep = "${installDir}/unicnek_sahex1 -session sahex1;";
		String allLaunchSteps = cpDataStep + relinkStep + launchStep;
		String localInstallDir = "sharp/trunk/modules/sharp/src/tests";
		String ergInstallDir = "/home/sharp/sharp/trunk/modules/sharp"
				+ "/src/tests";

		// Setup the Form
		super.setupForm();

		// Setup the executable information
		setExecutable(getName(), getDescription(), allLaunchSteps);

		// Add a couple of hosts
		addHost("localhost.localdomain", "linux x86_64", localInstallDir);
		addHost("ergaster.ornl.gov", "linux x86_64", ergInstallDir);

		return;
	}

	/**
	 * <p>
	 * This operation is used to setup the name and description of the launcher.
	 * </p>
	 * 
	 */
	@Override
	protected void setupItemInfo() {

		// Set the name and description of the Item
		setName(SHARPLauncherBuilder.name);
		setDescription("SHARP is a nuclear reactor core simulator from "
				+ "Argonne National Laboratory that is primarily focused "
				+ "on sodium-cooled fast reactors.");

		return;
	}
}