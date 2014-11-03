/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.jobLauncher;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

/**
 * The SuiteLauncher is capable of launching suites of codes where a suite is
 * defined as a code base with multiple executables, each for solving a
 * different problem. This includes codes like SHARP, MOOSE and Nek5000.
 * 
 * The SuiteLauncher relies on several very simple overrides of operations on
 * the JobLauncher base class and provides very little little new capability. It
 * provides all of the normal capabilities of a JobLauncher.
 * 
 * Like JobLauncher, the setupForm operation has to be called early by
 * SuiteLauncher subclasses. It should be called after the list of executables
 * is provided with addExecutables() and it will add an additional component
 * from which the executable can be selected. The additional component has an id
 * equal to JobLauncherForm.parallelId + 2.
 * 
 * The setExecutable operation is overridden to only set the executable and not
 * the Form details like in the JobLauncher.
 * 
 * The reviewEntries operation is overridden to reset the executable when the
 * Form is submitted.
 * 
 * The only thing that can be tested with this class is the public
 * setExecutables operation because it relies on JobLauncher to do all of the
 * heavy lifting.
 * 
 * The updateExecutablePath operation should be overridden by subclasses that
 * serve codes with a hierarchical layout. The SuiteLauncher assumes that the
 * directory structure is flat by default such that all executables can be found
 * with ${installDir}/${executable} is always true. Overriding
 * updateExecutablePath allows subclasses to have unique layouts like
 * ${installDir}/${executable}-specialFolder/${executable}.
 * 
 * @author Jay Jay Billings
 * 
 */
public class SuiteLauncher extends JobLauncher {

	/**
	 * The name of the component that lists the executables.
	 */
	private static final String execComponentName = "Available Executables";

	/**
	 * The description of the component that lists the executables.
	 */
	private static final String execComponentDesc = "The following executables "
			+ "are available for this suite.";

	/**
	 * The name of the entry that holds the executable name.
	 */
	private static final String execEntryName = "Executable";

	/**
	 * The description of the entry that holds the executable name.
	 */
	private static final String execEntryDesc = "The executable that "
			+ "will be launched by ICE.";

	/**
	 * The component that holds the executable information.
	 */
	private DataComponent execComponent;

	/**
	 * The entry that holds the name of the executable in the component;
	 */
	private Entry execEntry;

	/**
	 * The content provider for the entry that holds all of the names of the
	 * executables.
	 */
	private BasicEntryContentProvider execContentProvider;

	/**
	 * The list of executables.
	 */
	private ArrayList<String> executablesList;

	/**
	 * The main constructor for SuiteLauncher.
	 * 
	 * @param projectSpace
	 *            The project space in which the launcher should do its work and
	 *            find its data.
	 */
	public SuiteLauncher(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * The nullary constructor. Same purpose as the nullary JobLauncher
	 * constructor.
	 */
	public SuiteLauncher() {
		// Pass it on
		this(null);
	}

	/**
	 * This operation overrides the JobLauncher.setupForm operation to add an
	 * additional component for selecting executables.
	 */
	@Override
	protected void setupForm() {

		// Local Declarations
		ArrayList<String> execList;
		String defaultValue = "";

		// Setup the basic part of the form
		super.setupForm();

		// Create the content provider for the Entry.
		execContentProvider = new BasicEntryContentProvider();
		// Determine whether or not executables are available or it should be
		// given an empty list.
		if (executablesList == null || executablesList.isEmpty()) {
			execList = new ArrayList<String>();
		} else {
			execList = executablesList;
			defaultValue = executablesList.get(0);
		}
		// Finish setting the allowed values and default value
		execContentProvider.setAllowedValues(execList);
		execContentProvider.setAllowedValueType(AllowedValueType.Discrete);
		execContentProvider.setDefaultValue("");
		// Set the Entry tag
		execContentProvider.setTag("exec");

		// Create the Entry for listing the executables
		execEntry = new Entry(execContentProvider);
		execEntry.setDescription(execEntryDesc);
		execEntry.setId(1);
		execEntry.setName(execEntryName);

		// Create the component for listing executables
		execComponent = new DataComponent();
		// The id should be greater than the id for the parallel component in
		// the JobLauncherForm.
		execComponent.setId(JobLauncherForm.parallelId + 2);
		execComponent.setName(execComponentName);
		execComponent.setDescription(execComponentDesc);
		// Set the Entry
		execComponent.addEntry(execEntry);

		// Add it to the Form
		form.addComponent(execComponent);

		return;
	}

	/**
	 * This operation sets the list of executables from which clients can make a
	 * choice.
	 * 
	 * @param executableList
	 *            The list of executables available to launch.
	 */
	public void addExecutables(ArrayList<String> executables) {

		// Make sure the list is available
		if (executablesList == null) {
			executablesList = new ArrayList<String>();
		}
		// Set the list of executables.
		executablesList = executables;
		// Update the content provider if needed
		if (execContentProvider != null) {
			execContentProvider.setAllowedValues(executablesList);
		}

		return;

	}

	/**
	 * This operation overrides the same operation on JobLauncher and is called
	 * whenever the entries are reviewed to update the executable.
	 * 
	 * Set executable should only be called once.
	 * 
	 * @param execName
	 *            The proper and full name of the job that will be launched
	 *            (e.g. - Microsoft Word). The JobLauncher class appends
	 *            " Launcher" to this name.
	 * @param execDesc
	 *            The description of the executable.
	 * @param execCommand
	 *            The name of the executable command that should be launched.
	 *            This is different than the proper name of the executable. For
	 *            example, the name of a popular Linux text editor is Vi
	 *            Improved, but its executable command name is vim.
	 */
	@Override
	public void setExecutable(String execName, String execDesc,
			String execCommand) {

		// Set the executable name
		super.setExecutable(getName(), getDescription(), execCommand);

		return;
	}

	/**
	 * This operation allows subclasses to tailor the layout of the installation
	 * directory by modifying the name of the executable to add folders, etc.
	 * 
	 * Subclasses should never add slashes or back slashes to the beginning of
	 * the return value. The updated path should always be relative the the
	 * current directory.
	 * 
	 * @param installDir
	 *            The installation directory of the executable.
	 * @param executable
	 *            The name of the executable chosen by the client.
	 * @return The updated executable path. By default, this is equal to the
	 *         executable. Subclasses should modify as necessary by appending
	 *         directory names (folder/${executable} or ${executable}/realExec}.
	 * 
	 */
	protected String updateExecutablePath(String installDir, String executable) {
		return executable;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation reviews the Entries in the JobLauncherForm to make sure
	 * that it can actually perform the launch and updates the executable if
	 * needed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param preparedForm
	 *            <p>
	 *            The Form to review.
	 *            </p>
	 * @return <p>
	 *         The status.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {

		// Local Declarations
		FormStatus retVal = FormStatus.InfoError;

		// Call the base class review first so that JobLauncher can do its
		// thing.
		retVal = super.reviewEntries(preparedForm);

		// If the review passed, updated the executable name.
		if (!retVal.equals(FormStatus.InfoError)) {
			// Get the updated data component and entry
			execComponent = (DataComponent) form
					.getComponent(JobLauncherForm.parallelId + 2);
			execEntry = execComponent.retrieveAllEntries().get(0);

			// Get the hosts table
			TableComponent hostsTable = (TableComponent) form
					.getComponent(JobLauncherForm.parallelId + 1);

			// Get the selected rows of the host table
			int selectedRowIndex;
			ArrayList<Integer> selectedRowIds = hostsTable.getSelectedRows();
			// Default to the first entry if no row is selected
			if (selectedRowIds == null) {
				selectedRowIndex = 0;
			} else {
				selectedRowIndex = selectedRowIds.get(0);
			}
			// Get the installation directory and executable command
			String installDir = hostsTable.getRow(selectedRowIndex).get(2)
					.getValue();
			String execCommand = updateExecutablePath(installDir,
					execEntry.getValue());

			// Set the executable
			setExecutable(null, null, execCommand);
		}

		return retVal;
	}

	/**
	 * This operation overrides JobLauncher.clone to get the cloned type
	 * correct.
	 * 
	 * @return The clone.
	 */
	@Override
	public Object clone() {

		// Create a new instance of JobLauncher and copy the contents
		SuiteLauncher clone = new SuiteLauncher();
		clone.copy(this);

		return clone;
	}

	/**
	 * This operation overrides JobLauncher.copy to copy extra executable
	 * information.
	 * 
	 * @param otherLauncher
	 *            The launcher to copy.
	 */
	public void copy(SuiteLauncher otherLauncher) {

		if (otherLauncher != null) {

			// Let JobLauncher do most of the work.
			super.copy(otherLauncher);

			// Get the executable information. The Entry is not simply cloned
			// because a reference to the content provider is required.
			this.execComponent = otherLauncher.execComponent;
			this.execContentProvider = (BasicEntryContentProvider) otherLauncher.execContentProvider
					.clone();
			this.execEntry = new Entry(execContentProvider);
			this.executablesList = (ArrayList<String>) otherLauncher.executablesList
					.clone();

		}

		return;
	}

	/**
	 * This operation overrides JobLauncher.equals() to check the additional
	 * pieces of this class.
	 * 
	 * @param otherLauncher
	 *            The launcher to compare against.
	 * @return True if equal, false otherwise.
	 */
	public boolean equals(SuiteLauncher otherLauncher) {

		// Aside from checking the executables, there's nothing to do but let
		// Joblauncher check its info and the Form.
		boolean retVal = super.equals(otherLauncher)
				&& executablesList.equals(otherLauncher.executablesList);

		return retVal;
	}

	/**
	 * This operation overrides JobLauncher.hashCode() to check the additional
	 * pieces of this class.
	 * 
	 * @return The hash
	 */
	public int hashCode() {

		int execHash = (executablesList != null) ? executablesList.hashCode()
				: 0;

		// Aside from hashing the executables, there's nothing left to do but
		// let the JobLauncher compute its hash.
		int retVal = super.hashCode() + execHash;

		return retVal;
	}
}
