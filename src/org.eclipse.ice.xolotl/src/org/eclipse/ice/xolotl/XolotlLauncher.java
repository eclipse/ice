/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.xolotl;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

/**
 * A JobLauncher for Xolotl, a Plasma-Surface Interactions Simulator.
 * 
 * @author Jay Jay Billings
 * 
 */
@XmlRootElement(name = "XolotlLauncher")
public class XolotlLauncher extends JobLauncher {

	/**
	 * The default constructor.
	 * 
	 * @param project
	 *            The Eclipse project space where this launchers files are
	 *            stored.
	 */
	public XolotlLauncher(IProject project) {
		super(project);
	}

	/**
	 * Nullary constructor.
	 */
	public XolotlLauncher() {
		this(null);
	}

	/**
	 * This operation overrides the base class implementation of setupForm to
	 * configure the launcher's Form.
	 */
	@Override
	public void setupForm() {

		// Setup the base JobLauncher form on the base class
		super.setupForm();

		// Create the command that will be launched and set the install
		// directory
		String cmd = "./xolotl params.txt";
		String installDir = "/lustre/atlas/proj-shared/fus049/"
				+ "xolotl-psi-trunk/release-build";
		String networkFileDescription = "The file that contains the "
				+ "network in HDF5 format.";

		// Configure the host
		addHost("titan.ccs.ornl.gov", "linux", installDir);
		setExecutable(XolotlLauncherBuilder.name,
				"The Plasma-Surface Interactions Simulator", cmd);
		// Turn MPI on since this is Titan after all!
		enableMPI(1, 100000, 100);
		
		// Add the network file to the list of required inputs
		addInputType("Network File (HDF5)", "networkFile",
				networkFileDescription, ".h5");

		return;
	}

	/**
	 * This operation sets the name and description for this launcher.
	 */
	@Override
	public void setupItemInfo() {
		setDescription("The Plasma-Surface Interactions Simulator");
		setItemBuilderName(XolotlLauncherBuilder.name);
		setName(XolotlLauncherBuilder.name);
	}

}
