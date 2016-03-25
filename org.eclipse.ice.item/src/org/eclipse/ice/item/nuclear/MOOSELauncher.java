/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.jobLauncher.SuiteLauncher;

/**
 * A SuiteLauncher Item for all MOOSE products (MARMOT, BISON, RELAP-7, RAVEN).
 * The MOOSE framework is developed by Idaho National Lab.
 *
 * @author Anna Wojtowicz
 */
@XmlRootElement(name = "MOOSELauncher")
public class MOOSELauncher extends SuiteLauncher implements IUpdateableListener {

	/**
	 * The currently selected MOOSE application. Set by reviewEntries().
	 */
	private String execName = "";

	/**
	 * The currently selected *.i input file.
	 */
	private String inputFileName = "";

	/**
	 * The name of the YAML/action syntax generator
	 */
	private static final String yamlSyntaxGenerator = "Generate YAML/action syntax";

	/**
	 * The name of the custom MOOSE executable.
	 */
	private static final String customExecName = "Custom executable name";

	/**
	 * The DataComponent on the form that is used to store the list of
	 * executable apps. This DataComponent contains two Entries: [0] The Entry
	 * containing BISON, MARMOT, RAVEN and RELAP-7, and [1] An Entry of type
	 * Undefined in which the user can enter their own MOOSE app name.
	 */
	private DataComponent execDataComp;

	/**
	 * Nullary constructor.
	 */
	public MOOSELauncher() {
		this(null);
	}

	/**
	 * Parameterized constructor.
	 */
	public MOOSELauncher(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * Overriding setupForm to set the executable names and host information.
	 */
	@Override
	protected void setupForm() {

		// Local Declarations
		String userHome = System.getProperty("user.home");
		String separator = System.getProperty("file.separator");

		String localInstallDir = userHome + separator + "projects";
		String remoteInstallDir = "/home/moose";

		// Setup the Form
		super.setupForm();

		// Create the list of executables
		ArrayList<String> executables = new ArrayList<String>();
		executables.add("MARMOT");
		executables.add("BISON");
		executables.add("RELAP-7");
		executables.add("RAVEN");
		executables.add("MOOSE_TEST");
		executables.add(customExecName);
		// executables.add(yamlSyntaxGenerator);

		// Add the list to the suite
		addExecutables(executables);

		// Get a handle on the "executables" DataComponent
		execDataComp = (DataComponent) form.getComponent(5);
		// Register the launcher as a listener of the executables DataComponent
		execDataComp.register(this);

		// Create an entry for a "custom" MOOSE executable
		IEntry customExecEntry = new StringEntry();
		customExecEntry.setName(customExecName);
		customExecEntry.setDescription("A custom MOOSE-based executable. Note "
				+ "that this field is case-sensitive and should be entered as " + "it appears in the filesystem.");
		customExecEntry.setId(2);
		customExecEntry.setReady(false);

		// Add it to the form
		execDataComp.addEntry(customExecEntry);

		// Grab the DataComponent responsible for managing Input Files
		DataComponent inputFilesComp = (DataComponent) form.getComponent(1);
		// Set the input file to only *.i files (to reduce workspace clutter)
		inputFilesComp.deleteEntry("Input File");
		addInputType("Input File", "inputFile", "The MOOSE input file that defines the problem.", ".i");

		// Add hosts
		addHost("localhost", "linux", localInstallDir);
		addHost("habilis.ornl.gov", "linux", remoteInstallDir);

		// Enable MPI
		enableMPI(1, 10000, 1);

		// Enable TBB
		enableTBB(1, 256, 1);

		// Register this MooseLauncher as a listener of the Input File Entry.
		// When it is set to something we can react with a search of related
		// moose files.
		IEntry inputFile = inputFilesComp.retrieveEntry("Input File");
		inputFile.register(this);
		String fileValue = inputFile.getValue();
		// Go ahead and create the list of files related to the Input File
		if (!fileValue.isEmpty() && fileValue.contains(".i") && getReader() != null) {
			update(inputFile);
		}

		return;
	}

	/**
	 * <p>
	 * This operation is used to check equality between the MOOSEModel Item and
	 * another MOOSEModel Item. It returns true if the Items are equal and false
	 * if they are not.
	 * </p>
	 *
	 * @param otherMoose
	 *            <p>
	 *            The MOOSEModel Item that should be checked for equality.
	 *            </p>
	 * @return
	 * 		<p>
	 *         True if the launchers are equal, false if not
	 *         </p>
	 */
	@Override
	public boolean equals(Object other) {

		boolean retVal;

		// Check if they are the same reference in memory
		if (this == other) {
			return true;
		}

		// Check that the object is not null, and that it is an Item
		// Check that these objects have the same ICEObject data
		if (other == null || !(other instanceof MOOSELauncher) || !super.equals(other)) {
			return false;
		}

		// Check data
		MOOSELauncher otherMooseLauncher = (MOOSELauncher) other;
		retVal = (this.allowedActions.equals(otherMooseLauncher.allowedActions))
				&& (this.form.equals(otherMooseLauncher.form)) && (this.itemType == otherMooseLauncher.itemType)
				&& (this.status.equals(otherMooseLauncher.status));

		// Check project
		if (this.project != null && otherMooseLauncher.project != null
				&& (!(this.project.equals(otherMooseLauncher.project)))) {
			return false;
		}

		// Check project
		if (this.project == null && otherMooseLauncher.project != null
				|| this.project != null && otherMooseLauncher.project == null) {
			return false;
		}

		// MOOSE Model specific stuff...

		return retVal;
	}

	/**
	 * <p>
	 * This operation returns the hashcode value of the MOOSELauncher.
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         The hashcode
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Local Declaration
		int hash = 9;
		// Compute hash code from MOOSELauncher data
		hash = 31 * hash + super.hashCode();
		hash = 31 * hash + inputFileName.hashCode();

		return hash;
	}

	/**
	 *
	 * @param otherMoose
	 *            <p>
	 *            This operation performs a deep copy of the attributes of
	 *            another MOOSELauncher Item into the current MOOSELauncher
	 *            Item.
	 *            </p>
	 */
	public void copy(MOOSELauncher otherMoose) {

		// Return if otherMoose is null
		if (otherMoose == null) {
			return;
		}

		// Copy contents into super and current object
		super.copy(otherMoose);

		// Clone contents correctly
		form = new Form();
		form.copy(otherMoose.form);

		// Copy Moose Launcher specific stuff
		inputFileName = otherMoose.inputFileName;
		execDataComp = (DataComponent) otherMoose.execDataComp.clone();

		return;
	}

	/**
	 * <p>
	 * This operation provides a deep copy of the MOOSELauncher Item.
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         A clone of the MOOSELauncher Item.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Create a new instance of Moose Launcher and copy the contents
		MOOSELauncher clone = new MOOSELauncher();
		clone.copy(this);

		return clone;
	}

	/**
	 * Sets the information that identifies the Item.
	 */
	@Override
	protected void setupItemInfo() {

		// Local declarations
		String description = "The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed " + "by Idaho National Laboratory.";

		// Set the model defaults
		setName(MOOSELauncherBuilder.name);
		setDescription(description);
		setItemBuilderName(MOOSELauncherBuilder.name);

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
		// Run the job launchers process method.
		return super.process(actionName);

	}

	/**
	 * This operation overrides Item.reviewEntries(). This override is required
	 * in the event that the YAML/action syntax generator is selected, in which
	 * case certain JobLaunch flags (related to file uploading) must be turned
	 * off. Conversely, these flags must be turned back on for any other
	 * executable.
	 *
	 * @param preparedForm
	 *            The Form to review.
	 * @return The Form's status.
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {

		// Local declaration
		FormStatus retStatus = null;

		// Call the super's status review first
		retStatus = super.reviewEntries(preparedForm);

		// If the super's status review was successful, keep going
		if (!retStatus.equals(FormStatus.InfoError)) {

			if (execDataComp != null) {
				// Grab the name of the current executable selected by the
				// client
				execName = execDataComp.retrieveAllEntries().get(0).getValue();
			}

			// Check the DataComponent and app selection is valid
			if ("Available Executables".equals(execDataComp.getName())) {

				// Set this back to true in case it's been changed by the YAML/
				// action syntax generator
				setUploadInputFlag(true);

				if (yamlSyntaxGenerator.equals(execName)) {
					// Disable input file appending (no input file to append)
					setAppendInputFlag(false);
					// Disable input file uploading
					setUploadInputFlag(false);
				}

				retStatus = FormStatus.ReadyToProcess;

			} else {
				retStatus = FormStatus.InfoError;
			}
		}

		return retStatus;
	}

	/**
	 * This method provides a implementation of the IUpdateable interface that
	 * listens for changes in objects that are registered with this MOOSE
	 * Launcher. Primarily, it will toggle a "custom MOOSE executable" on and
	 * off, in addition reducing the number of updates that are called on the
	 * "Input File" Entry if it's value hasn't changed.
	 */
	@Override
	public void update(IUpdateable component) {

		if (component instanceof IEntry) {

			// Check if this is the Input File entry and has a valid value
			IEntry entry = (IEntry) component;
			if (entry.getName().equals("Input File") && !entry.getValue().isEmpty()) {

				// First, check if the file extension on the value is valid
				if (!entry.getValue().contains(".i")) {
					// Complain and exit
					logger.info("MOOSELauncher Message: Input files" + "must have a *.i extension!");
					return;
				}

			}

			super.update(entry);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.item.jobLauncher.JobLauncher#
	 * getFileDependenciesSearchString()
	 */
	@Override
	protected String getFileDependenciesSearchString() {
		String re1 = "((?i:[a-z][a-z0-9_]*))";
		String re2 = "(\\s+)";
		String re3 = "(=)";
		String re4 = "(\\s+)";
		String re5 = "((?i:[a-z][a-z0-9_]*))";
		String re6 = "(\\.)";
		String re7 = "((?i:[a-z][a-z0-9_]*))";
		return re1 + re2 + re3 + re4 + re5 + re6 + re7;
	}

	/**
	 * @see org.eclipse.ice.item.Item#getIOType()
	 */
	@Override
	public String getIOType() {
		return "moose";
	}
}
