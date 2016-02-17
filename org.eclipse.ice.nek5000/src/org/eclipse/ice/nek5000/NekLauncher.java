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
package org.eclipse.ice.nek5000;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.jobLauncher.SuiteLauncher;

/**
 * This class is a JobLauncher Item for the Nek5000 flow code from Argonne
 * National Laboratory.
 * 
 * @author Jay Jay Billings
 * 
 */
@XmlRootElement(name = "Nek5000Launcher")
public class NekLauncher extends SuiteLauncher {

	/**
	 * A nullary constructor that delegates to the project constructor.
	 */
	public NekLauncher() {
		this(null);
	}

	/**
	 * The Constructor
	 */
	public NekLauncher(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * Overriding setupForm to set the executable name and information
	 */
	@Override
	protected void setupForm() {

		// Local Declarations
		String baseInstallDir = "/home/jay/Programs/nek";

		// Setup the Form
		super.setupForm();
		
		// Create the list of executables
		ArrayList<String> executables = new ArrayList<String>();
		executables.add("2d_eigtest");
		executables.add("3dbox");
		executables.add("axi");
		executables.add("blasius");
		executables.add("conj_ht");
		executables.add("cyl_restart");
		executables.add("eddy");
		executables.add("eddy_psi_omega");
		executables.add("expansions");
		executables.add("ext_cyl");
		executables.add("fs_2");
		executables.add("fs_hydro");
		executables.add("hemi");
		executables.add("kov_st_state");
		executables.add("kovasznay");
		executables.add("lowMach_test");
		executables.add("mhd");
		executables.add("moab");
		executables.add("moab_conjht");
		executables.add("os7000");
		executables.add("peris");
		executables.add("pipe");
		executables.add("rayleigh");
		executables.add("robin");
		executables.add("shear4");
		executables.add("solid");
		executables.add("strat");
		executables.add("taylor");
		executables.add("thermal_io");
		executables.add("timing");
		executables.add("turbChannel");
		executables.add("turbJet");
		executables.add("var_vis");
		executables.add("vortex");
		executables.add("vortex2");

		// Add the list to the suite
		addExecutables(executables);

		// Add localhost
		addHost("localhost", "linux", baseInstallDir);

		return;
	}

	/**
	 * This operation overrides the base class operation to properly account for
	 * the Nek5000 file structure.
	 * 
	 * @param installDir
	 *            The installation directory of Nek5000.
	 * @param executable
	 *            The name of the executable selected by a client.
	 * @return The updated executable name reflecting the fact that all Nek5000
	 *         examples are setup as ${executable}/nek5000.
	 */
	@Override
	protected String updateExecutablePath(String installDir, String executable) {

		// Map of proper file names so that the examples work. Nek5000 has
		// different directory, executable and input file names. The executable
		// is ${executable}/nek5000 and uses data files that are roughly named
		// after ${executable}.
		HashMap<String, String> executableMap = new HashMap<String, String>();
		executableMap.put("2d_eigtest", "eig1");
		executableMap.put("3dbox", "b3d");
		executableMap.put("axi", "axi");
		// benard
		executableMap.put("blasius", "blasius");
		// cone
		executableMap.put("conj_ht", "conj_ht");
		executableMap.put("cyl_restart", "ca");
		executableMap.put("eddy", "eddy_uv");
		// eddy_neknek
		executableMap.put("eddy_psi_omega", "psi_omega");
		executableMap.put("expansion", "expansion");
		executableMap.put("ext_cyl", "ext,cyl");
		executableMap.put("fs_2", "st1");
		executableMap.put("fs_hydro", "fs_hydro");
		executableMap.put("hemi", "hemi");
		executableMap.put("kov_st_state", "kov_st_stokes");
		executableMap.put("kovasznay", "kov");
		executableMap.put("lowMach_test", "lowMach_test.rea");
		executableMap.put("mhd", "gpf");
		executableMap.put("moab", "pipe");
		executableMap.put("moab_conjht", "moab_conjht");
		executableMap.put("os7000", "u3_t020_n13");
		executableMap.put("peris", "peris");
		executableMap.put("pipe", "helix");
		executableMap.put("rayleigh", "ray0");
		executableMap.put("robin", "robin");
		executableMap.put("shear4", "shear4");
		executableMap.put("solid", "solid");
		executableMap.put("strat", "re10f1000p0001");
		executableMap.put("taylor", "taylor");
		executableMap.put("thermal_io", "recirc");
		executableMap.put("timing", "timing");
		executableMap.put("turbChannel", "turbChannel");
		executableMap.put("turbJet", "jet");
		executableMap.put("var_vis", "st2");
		executableMap.put("vortex", "r1854a");
		executableMap.put("vortex2", "v2d");

		// Launch Stages. These commands copy the example into the working
		// directory, remove the unneeded object files, rename the input file
		// to what Nek expects, and then copies nek executable builder in
		// working directory.
		String exampleCopyStage = "cp -r " + installDir + "/examples/"
				+ executable + "/* .;";
		String rmObjStage = "rm -rf obj/;";
		String inputCopyStage = "cp ${inputFile} "
				+ executableMap.get(executable) + ".rea;";

		// Define the nek and visnek launch stages
		String launchStage = "${installDir}trunk/tools/scripts/nek "
				+ executableMap.get(executable) + ";";
		String vizStage = "${installDir}trunk/tools/scripts/visnek "
				+ executableMap.get(executable) + ";";

		// Configure the entire launch script
		String launchCommand = exampleCopyStage + rmObjStage + inputCopyStage
				+ launchStage + vizStage;

		return launchCommand;
	}

	/**
	 * This operation sets the information that identifies this Item.
	 */
	@Override
	protected void setupItemInfo() {

		// Local declarations
		String description = "Nek5000 is an open-source computational "
				+ "fluid dynamics solver based on the spectral element method "
				+ "and is actively developed at the Mathematics and Computer "
				+ "Science Division of Argonne National Laboratory. The code is "
				+ "written in Fortran77 and employs the MPI standard for "
				+ "parallelism.";

		// Set the model defaults
		setName(NekLauncherBuilder.name);
		setDescription(description);
		setItemBuilderName(NekLauncherBuilder.name);

		return;
	}

}
