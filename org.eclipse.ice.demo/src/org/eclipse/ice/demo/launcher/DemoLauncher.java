/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.demo.launcher;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.january.form.FormStatus;

@XmlRootElement(name = "DemoLauncher")
public class DemoLauncher extends JobLauncher {

	// TODO:
	// These need to be filled in before using this item
	// They can be set in the DemoModel(IProject) method
	private String execCommand;
	private String readerName;
	// End required variables

	private IIOService ioService;
	private IReader reader;

	/**
	 * Nullary constructory
	 */
	public DemoLauncher() {
		this(null);
	}

	/**
	 * Main constructor
	 * 
	 * Developer is required to update the class variables that are
	 * 
	 */
	public DemoLauncher(IProject project) {
		super(project);

		// TODO: These must be customized before using this item
		execCommand = "DemoDefaultExecCommand";
		readerName = "DemoDefaultReaderName";
		// End required variables

		// TODO: Add User Code Here
	}

	/**
	 * Sets the name and description for the item.
	 */
	@Override
	protected void setupItemInfo() {
		setName("Demo Launcher");
		setDescription("Provide information to launch Demo");
	}

	/**
	 * Sets a default host, architecture, and path. Can be edited here or
	 * changed on the fly when a new Demo item is created via the ICE
	 * perspective. Other forms of custom logic for the launcher should be
	 * implemented here, although they are compeletely optional.
	 */
	@Override
	public void setupForm() {
		super.setupForm();
		setAppendInputFlag(false);
		addHost("localhost", "linux x86_64", "/home/user/Demo");

		// TODO: (Optional) Add User Code Here
	}

	/**
	 *
	 * 
	 * @param actionName
	 *            The action to take when processing
	 * @return Whether the form was valid and successful in processing
	 */
	@Override
	public FormStatus process(String actionName) {
		FormStatus retStatus = FormStatus.ReadyToProcess;

		// Make sure that the launcher code has been updated
		if (execCommand == "DemoDefaultExecCommand"
				|| readerName == "DemoDefaultReaderName") {
			return FormStatus.InfoError;
		}

		// TODO: Add User Code Here
		reader = ioService.getReader(readerName);
		// TODO: Add User Code Here

		setExecutable(getName(), getDescription(), execCommand);
		retStatus = super.process(actionName);

		return retStatus;
	}
}