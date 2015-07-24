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

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;

/**
 * @author Jay Jay Billings, Rajeev Kumar
 *
 */
@XmlRootElement(name = "PolyLauncher")
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.jobLauncher.JobLauncher#setupForm()
	 */
	@Override
	protected void setupForm() {

		// Setup the Form
		super.setupForm();

		// Create a data component for the working directory
		DataComponent workingDirComponent = new DataComponent();
		workingDirComponent.setName("Working Directory Parameters");
		workingDirComponent.setId(JobLauncherForm.parallelId + 2);
		workingDirComponent
				.setDescription("Parameters for the Working Directory");
		form.addComponent(workingDirComponent);
		// Create the Entry
		Entry fileEntry = new Entry() {
			@Override
			public void setup() {
				allowedValueType = AllowedValueType.Undefined;
			}
		};
		fileEntry.setId(1);
		fileEntry.setName("Working Directory");
		fileEntry.setDescription("The working directory");
		workingDirComponent.addEntry(fileEntry);

		// Add localhost
		setExecutable("poly", "Run Poly", "qsub");
		addHost("extb16l01.oic.ornl.gov", "linux", "${workingDir}");

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.jobLauncher.JobLauncher#process(java.lang.String)
	 */
	@Override
	public FormStatus process(String actionName) {

		DataComponent workingDirComp = (DataComponent) form
				.getComponent(JobLauncherForm.parallelId + 2);
		String workingDirName = workingDirComp
				.retrieveEntry("Working Directory").getValue();

		setExecutable("poly", "Run Poly", "cd " + workingDirName + ";qsub");

		return super.process(actionName);
	}
}
