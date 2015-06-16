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
package org.eclipse.ice.item.jobLauncher.multiLauncher;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * This is a subclass of Form for MultiLaunchers that creates a
 * MasterDetailsComponent. It also creates a header for that
 * MasterDetailsComponent. Construction of the header, which is a DataComponent,
 * and the MasterDetailsComponent required by the MultiLauncher class is
 * performed here, not in the MultiLauncher. However, the description of the
 * MasterDetailsComponent can still be found on the MultiLauncher class. There
 * is not a specific unit test for this class because all it does is implement a
 * constructor that fills the Form. Instead, this class is checked as part of
 * the MultiLauncherTester.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "Form")
class MultiLauncherForm extends Form {
	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 */
	public MultiLauncherForm() {

		// Call the super constructor to set the details
		super();

		// Set the description
		setDescription("This form configures the MultiLauncher with a set of jobs that ICE will launch.");

		// Create the data component to hold execution parameters for all the
		// jobs
		DataComponent execModeComp = new DataComponent();
		execModeComp.setName("Execution Mode");
		execModeComp.setId(3);
		execModeComp.setDescription("MultiLaunchers are capable of launching "
				+ "in one of two modes; serial or parallel. In serial mode, "
				+ "all of the jobs are launched as part of a change. In "
				+ "parallel launch mode, the jobs are launched in parallel, "
				+ "although they may not all be launched at the same time.");

		// Create an Entry for the parallel launch flag
		Entry parallelLaunchEntry = new Entry() {
			// The only thing we need to do is set it up
			@Override
			protected void setup() {

				setName("Enable Parallel Execution");
				setId(1);
				setDescription("Set to true to enable parallel launch of "
						+ "the jobs.");
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("true");
				allowedValues.add("false");
				defaultValue = "true";

				return;
			}
		};

		// Add the Entry to the DataComponent
		execModeComp.addEntry(parallelLaunchEntry);

		// Create the master-details component. All we can do here is set it up.
		// The MultiLauncher has to actually add the details.
		MasterDetailsComponent masterDetailsComp = new MasterDetailsComponent();
		masterDetailsComp.setId(1);
		masterDetailsComp.setName("Job Listing");
		masterDetailsComp
				.setDescription("This section configures the jobs"
						+ " and their parameters. Jobs can be added and removed and "
						+ "their parameters configured. Only the job types in the list "
						+ "can be launched by this launcher.");

		// Set the header component of the MasterDetailsComponent
		masterDetailsComp.setGlobalsComponent(execModeComp);

		// Add the master details component to the Form
		addComponent(masterDetailsComp);

		// Create a ResourceComponent
		ResourceComponent outputData = new ResourceComponent();
		outputData.setName("Output Files and Data");
		outputData.setId(2);
		outputData.setDescription("This section describes all of the data "
				+ "and additional output created by the set of jobs.");

		// Add the ResourceComponent
		addComponent(outputData);

	}
}