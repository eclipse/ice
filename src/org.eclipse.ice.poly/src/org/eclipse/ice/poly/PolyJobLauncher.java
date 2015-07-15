/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Rajeev Kumar
 *******************************************************************************/
package org.eclipse.ice.poly;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

/**
 * @author Jay Jay Billings, Rajeev Kumar
 *
 */
public class PolyJobLauncher extends JobLauncher {

	PolyJobLauncher() {
		this(null);
	}

	PolyJobLauncher(IProject project) {
		super(project);
	}

	/**
	 * This operation sets the information that identifies this Item.
	 */
	@Override
	protected void setupItemInfo() {

		// Local declarations
		String description = "Poly simulates polymers using "
				+ "self-consistent field theory.";

		// Set the model defaults
		setName(PolyItemBuilder.name);
		setDescription(description);
		setItemBuilderName(PolyItemBuilder.name);

		return;
	}

	/**
	 * Overriding setupForm to set the executable name and information
	 */
	@Override
	protected void setupForm() {

		// Setup the Form
		super.setupForm();

		// Add localhost
		setExecutable("poly", "Run Poly", "qsub");
		addHost("extb16l01.oic.ornl.gov", "linux", "${workingDir}");

		return;
	}
	
}
