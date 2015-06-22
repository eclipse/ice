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
package org.eclipse.ice.item.jobLauncher;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.ResourceComponent;

/**
 * <p>
 * The JobLauncherForm is a subclass of Form that is specialized to work with
 * the JobLauncher Item. It contains a DataComponents with Entries for any and
 * all input files (configured by setInputFiles()) as well as the hostname of
 * the machine on which the job should be launched. These data components have
 * ids 1 and 2 respectively and the hostname data component's single entry is
 * named "Platform." It also contains one ResourceComponent, id = 3, that holds
 * the output data of the simulation. Hosts are configured using the
 * add/deleteHost operations. Support for multithreading and multiprocessing is
 * enabled or disabled using the appropriated operations and these options are
 * in a fourth data component with id = 4.
 * </p>
 * <p>
 * The operations to add and removes hosts and to enable or disable
 * multithreading and multiprocessing are not protected, but the JobLauncher
 * class should be the only class to ever call these operations. Enabling either
 * of these will creating a DataComponent called "Parallel Execution" in the
 * Form. The Parallel Execution component contains an Entry each for OpenMPI and
 * MPI that are named "Number of OpenMP Threads" and "Number of MPI Processes"
 * respectively.
 * </p>
 * <p>
 * Hosts and input files should be added immediately after the constructor is
 * called. Failure to set the hosts and input files will result in a
 * malformed... er... form.
 * </p>
 * <p>
 * Additional input file types can be added to the Form. They are added directly
 * to the Data Component by calling setInputFiles() with the name, description
 * and allowed files.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "form")
public class JobLauncherForm extends Form {

	/**
	 * The id of the files data component.
	 */
	public static final int filesId = 1;

	/**
	 * The id of the output component.
	 */
	public static final int outputId = 2;

	/**
	 * The id of the parallel info data component.
	 */
	public static final int parallelId = 3;

	/**
	 * True if the parallel component has been created, false otherwise.
	 */
	private boolean parallelComponentEnabled = false;

	/**
	 * <p>
	 * The name of the OpenMP Entry.
	 * </p>
	 * 
	 */
	private static final String openMPEntryName = "Number of OpenMP Threads";

	/**
	 * <p>
	 * The name of the MPI Entry.
	 * </p>
	 * 
	 */
	private static final String mpiEntryName = "Number of MPI Processes";

	/**
	 * <p>
	 * The name of the OpenMP Entry.
	 * </p>
	 * 
	 */
	private static final String TBBEntryName = "Number of TBB Threads";

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 */
	public JobLauncherForm() {

		// Call the super constructor on Form
		super();

		// Set the name and description of the Form
		setName("Job Launcher");
		setDescription("Generic Job Launch Form");

		// Make a DataComponent for the input files
		DataComponent fileComponent = new DataComponent();
		fileComponent.setId(filesId);
		fileComponent
				.setDescription("This section contains the name of the file(s) "
						+ "used by this Job.");
		fileComponent.setName("Input File(s)");
		// Add the data components
		addComponent(fileComponent);

		// Create a ResourceComponent
		ResourceComponent outputData = new ResourceComponent();
		outputData.setName("Output Files and Data");
		outputData.setId(outputId);
		outputData.setDescription("This section describes all of the data "
				+ "and additional output created by the job launch.");

		// Add the ResourceComponent
		addComponent(outputData);

		return;

	}

	/**
	 * <p>
	 * This operation enables support for MPI that allows a client to set the
	 * number of threads for the job. A value of zero or less than zero passed
	 * to this operation will be ignore and set to 1. Furthermore, the maximum
	 * must be greater than the minimum and the default value must be equal to
	 * or in between the maximum and minimum. If these conditions are not met,
	 * it will not be enabled.
	 * </p>
	 * 
	 * @param minProcesses
	 *            <p>
	 *            The minimum number of processes that should be allowed for
	 *            this job.
	 *            </p>
	 * @param maxProcesses
	 *            <p>
	 *            The maximum number of processes that should be allowed for
	 *            this job.
	 *            </p>
	 * @param defaultProcesses
	 *            <p>
	 *            The default number of processes for MPI to use.
	 *            </p>
	 */
	public void enableMPI(int minProcesses, int maxProcesses,
			int defaultProcesses) {

		// Local Declarations - Check for negative or zero values
		final int minProcessesFixed = Math.max(minProcesses, 1);
		final int maxProcessesFixed = Math.max(maxProcesses, 1);
		final int defaultProcessesFixed = Math.max(defaultProcesses, 1);
		DataComponent parallelismComponent = null;
		Entry mpiEntry = null;

		// Only create the Entry and add it to the DataComponent if the numbers
		// are right
		if (minProcessesFixed <= maxProcessesFixed
				&& defaultProcessesFixed >= minProcessesFixed
				&& defaultProcessesFixed <= maxProcessesFixed) {
			// Get the parallelism component.
			parallelismComponent = getParallelismComponent();
			// Delete the Entry if it is already in the Component
			if (parallelismComponent.contains(mpiEntryName)) {
				parallelismComponent.deleteEntry(mpiEntryName);
			}
			// Create the Entry
			mpiEntry = new Entry() {
				// Setup the values
				@Override
				public void setup() {
					// Add the min and max threads
					if (minProcessesFixed != maxProcessesFixed) {
						allowedValues.add(String.valueOf(minProcessesFixed));
						allowedValues.add(String.valueOf(maxProcessesFixed));
						allowedValueType = AllowedValueType.Continuous;
					} else {
						// Only add one value if everything has been reset to 1
						allowedValues.add(String.valueOf(minProcessesFixed));
						allowedValueType = AllowedValueType.Discrete;
						value = String.valueOf(minProcessesFixed);
					}
					defaultValue = String.valueOf(defaultProcessesFixed);
				}
			};
			mpiEntry.setName(mpiEntryName);
			mpiEntry.setId(1);
			mpiEntry.setDescription("The number of processes to use with "
					+ "MPI");
			// Add the Entry to the Component
			parallelismComponent.addEntry(mpiEntry);
		}

		return;

	}

	/**
	 * <p>
	 * This operation disables support for MPI. The threading component will
	 * only be disabled if other threading support is also disabled.
	 * </p>
	 * 
	 */
	public void disableMPI() {

		// Local Declarations
		DataComponent parallelismComponent = null;

		// Get the parallelism component if it exists. It will always be the
		// final component.
		if (parallelComponentEnabled) {
			parallelismComponent = getParallelismComponent();
			if (!parallelismComponent.contains(openMPEntryName)
					&& !parallelismComponent.contains(this.TBBEntryName)) {
				removeParallelismComponent();
			}
			// Otherwise just remove the MPI Entry
			else {
				parallelismComponent.deleteEntry(mpiEntryName);
			}

		}

		return;

	}

	/**
	 * <p>
	 * This operation enables support for OpenMP that allows a client to set the
	 * number of threads for the job. A value of zero or less than zero passed
	 * to this operation will be ignore and set to 1. Furthermore, the maximum
	 * must be greater than the minimum and the default value must be equal to
	 * or in between the maximum and minimum. If these conditions are not met,
	 * it will not be enabled.
	 * </p>
	 * 
	 * @param minThreads
	 *            <p>
	 *            The minimum number of threads that should be allowed for this
	 *            job.
	 *            </p>
	 * @param maxThreads
	 *            <p>
	 *            The maximum number of threads that should be allowed for this
	 *            job.
	 *            </p>
	 * @param defaultThreads
	 *            <p>
	 *            The default number of threads that should be allowed for this
	 *            job.
	 *            </p>
	 */
	public void enableOpenMP(int minThreads, int maxThreads, int defaultThreads) {

		// Local Declarations - Check for negative or zero values
		final int minThreadsFixed = Math.max(minThreads, 1);
		final int maxThreadsFixed = Math.max(maxThreads, 1);
		final int defaultThreadsFixed = Math.max(defaultThreads, 1);
		DataComponent parallelismComponent = null;
		Entry openMPEntry = null;

		// Only create the Entry and add it to the DataComponent if the numbers
		// are right
		if (minThreadsFixed <= maxThreadsFixed
				&& defaultThreadsFixed >= minThreadsFixed
				&& defaultThreadsFixed <= maxThreadsFixed) {
			// Get the parallelism component)
			parallelismComponent = getParallelismComponent();
			// Delete the Entry if it is already in the Component
			if (parallelismComponent.contains(openMPEntryName)) {
				parallelismComponent.deleteEntry(openMPEntryName);
			}
			// Create the Entry
			openMPEntry = new Entry() {
				// Setup the values
				@Override
				public void setup() {
					// Add the min and max threads
					if (minThreadsFixed != maxThreadsFixed) {
						allowedValues.add(String.valueOf(minThreadsFixed));
						allowedValues.add(String.valueOf(maxThreadsFixed));
						allowedValueType = AllowedValueType.Continuous;
					} else {
						// Only add one value if everything has been reset to 1
						allowedValues.add(String.valueOf(minThreadsFixed));
						allowedValueType = AllowedValueType.Discrete;
						value = String.valueOf(minThreadsFixed);
					}
					defaultValue = String.valueOf(defaultThreadsFixed);
				}
			};
			openMPEntry.setName(openMPEntryName);
			openMPEntry.setId(1);
			openMPEntry.setDescription("The number of threads to use with "
					+ "OpenMP");
			// Add the Entry to the Component
			parallelismComponent.addEntry(openMPEntry);
		}

		return;
	}

	/**
	 * <p>
	 * This operation disables support for OpenMP. The threading component will
	 * only be disabled if other threading support is also disabled.
	 * </p>
	 * 
	 */
	public void disableOpenMP() {

		// Local Declarations
		DataComponent parallelismComponent = null;

		// Get the parallelism component if it exists. It will always be the
		// final component.
		if (parallelComponentEnabled) {
			parallelismComponent = getParallelismComponent();
			// If MPI and TBB are disabled, remove the whole component
			if (!parallelismComponent.contains(mpiEntryName)
					&& !parallelismComponent.contains(this.TBBEntryName)) {
				removeParallelismComponent();
			}
			// Otherwise just remove the MPI Entry
			else {
				parallelismComponent.deleteEntry(openMPEntryName);
			}

		}

		return;

	}

	/**
	 * <p>
	 * This operation enables support for Intel's Thread Building Blocks that
	 * allows a client to set the number of threads for the job. A value of zero
	 * or less than zero passed to this operation will be ignore and set to 1.
	 * Furthermore, the maximum must be greater than the minimum and the default
	 * value must be equal to or in between the maximum and minimum. If these
	 * conditions are not met, it will not be enabled.
	 * </p>
	 * 
	 * @param minThreads
	 *            <p>
	 *            The minimum number of threads that should be allowed for this
	 *            job.
	 *            </p>
	 * @param maxThreads
	 *            <p>
	 *            The maximum number of threads that should be allowed for this
	 *            job.
	 *            </p>
	 * @param defaultThreads
	 *            <p>
	 *            The default number of threads that should be allowed for this
	 *            job.
	 *            </p>
	 */
	public void enableTBB(int minThreads, int maxThreads, int defaultThreads) {
		// Local Declarations - Check for negative or zero values
		final int minThreadsFixed = Math.max(minThreads, 1);
		final int maxThreadsFixed = Math.max(maxThreads, 1);
		final int defaultThreadsFixed = Math.max(defaultThreads, 1);
		DataComponent parallelismComponent = null;
		Entry tBBEntry = null;

		// Only create the Entry and add it to the DataComponent if the numbers
		// are right
		if (minThreadsFixed <= maxThreadsFixed
				&& defaultThreadsFixed >= minThreadsFixed
				&& defaultThreadsFixed <= maxThreadsFixed) {
			// Get the parallelism component)
			parallelismComponent = getParallelismComponent();
			// Delete the Entry if it is already in the Component
			if (parallelismComponent.contains(this.TBBEntryName)) {
				parallelismComponent.deleteEntry(this.TBBEntryName);
			}
			// Create the Entry
			tBBEntry = new Entry() {
				// Setup the values
				@Override
				public void setup() {
					// Add the min and max threads
					if (minThreadsFixed != maxThreadsFixed) {
						allowedValues.add(String.valueOf(minThreadsFixed));
						allowedValues.add(String.valueOf(maxThreadsFixed));
						allowedValueType = AllowedValueType.Continuous;
					} else {
						// Only add one value if everything has been reset to 1
						allowedValues.add(String.valueOf(minThreadsFixed));
						allowedValueType = AllowedValueType.Discrete;
						value = String.valueOf(minThreadsFixed);
					}
					defaultValue = String.valueOf(defaultThreadsFixed);
				}
			};

			tBBEntry.setName(this.TBBEntryName);
			tBBEntry.setId(2);
			tBBEntry.setDescription("The number of threads to use with "
					+ "Thread Building Blocks");
			// Add the Entry to the Component
			parallelismComponent.addEntry(tBBEntry);
		}

		return;
	}

	/**
	 * <p>
	 * This operation disables support for Intel Thread Building Blocks. The
	 * threading component will only be disabled if other threading support is
	 * also disabled.
	 * </p>
	 * 
	 */
	public void disableTBB() {
		// Local Declarations
		DataComponent parallelismComponent = null;

		// Get the parallelism component if it exists. It will always be the
		// final component.
		if (parallelComponentEnabled) {
			parallelismComponent = getParallelismComponent();
			// If MPI and OpenMP is disabled, remove the whole component
			if (!parallelismComponent.contains(mpiEntryName)
					&& !parallelismComponent.contains(this.openMPEntryName)) {
				removeParallelismComponent();
			}
			// Otherwise just remove the TBB Entry
			else {
				parallelismComponent.deleteEntry(this.TBBEntryName);
			}
		}

		return;

	}

	/**
	 * <p>
	 * This operation sets the input files that may be selected in the Form for
	 * the input type with the specified name. Passing null for the name or file
	 * list will not make any changes to the Form.
	 * </p>
	 * 
	 * @param name	Name of the input file type.
	 * @param desc	Description of the file type.
	 * @param files	List of files available for the job.
	 */
	public void setInputFiles(String name, String desc, 
			ArrayList<String> files) {

		// Local Declarations
		int oldId = 0;
		Entry oldEntry = null;
		DataComponent fileComponent = ((DataComponent) getComponent(1));
		final ArrayList<String> finalFiles = (files != null) ? 
				(ArrayList<String>) files.clone() : new ArrayList<String>();
		String oldValue = "";

		// Determine if the Entry already exists in the component and remove it
		// if necessary
		if (fileComponent.contains(name)) {
			// Store the id and Entry
			oldEntry = fileComponent.retrieveEntry(name);
			oldId = oldEntry.getId();
			oldValue = oldEntry.getValue();
		}

		// Create an Entry with the filenames
		Entry fileEntry = new Entry() {
			// Setup the filenames
			@Override
			protected void setup() {
				// The input file should be one of the files in the "inputFiles"
				// array.
				this.allowedValues = finalFiles;
				this.allowedValueType = AllowedValueType.File;

				return;
			}

		};
		
		// Set the name and description of the Filename entry
		fileEntry.setDescription(desc);
		fileEntry.setName(name);
		fileEntry.setId((oldId > 0) ? oldId : fileComponent
				.retrieveAllEntries().size()); // FIXME! Bug - needs a unique id
		// Keep the original value, if possible
		if (fileEntry.getAllowedValues().contains(oldValue)) {
			fileEntry.setValue(oldValue);
		}
		// Determine if the Entry already exists in the component and reuse it
		if (oldEntry != null) {
			oldEntry.copy(fileEntry);
		} else {
			// Or just add it
			fileComponent.addEntry(fileEntry);
		}

		return;
	}
	
//	/**
//	 * This method is the same as {@link #setInputFiles}, except that is does 
//	 * not take a boolean flag in the parameters. This method calls the other
//	 * {@link #setInputFiles} with the boolean isFileEntry flag as false.
//	 * 
//	 * @param name	Name of the input file type.
//	 * @param desc	Description of the file type.
//	 * @param files	List of files available for the job.
//	 */
//	public void setInputFiles(String name, String desc, 
//			ArrayList<String> files) {
//		
//		// Call the other method with the boolean flag as false
//		setInputFiles(name, desc, files, false);
//		
//		return;
//	
//	}

	/**
	 * This method removes an input file type from the Form. If null is passed
	 * in, nothing is changed.
	 * 
	 * @param name	The name of the input file type
	 */
	public void removeInputFiles(String name) {
		
		// Get the file(s) component on the form
		DataComponent fileComponent = ((DataComponent) getComponent(1));
		
		if (name != null && fileComponent.contains(name)) {
			fileComponent.deleteEntry(name);
		}
		
		return;
	}
	
	/**
	 * <p>
	 * This operation creates the parallelism component for the Form and returns
	 * it. It also adds it to the Form.
	 * </p>
	 * 
	 * @return <p>
	 *         The parallelism component.
	 *         </p>
	 */
	private DataComponent createParallelismComponent() {

		// Local Declarations
		DataComponent parallelismComponent = new DataComponent();

		// Setup the Parallel Execution Data Component. It is not added until
		// TBB, MPI or OpenMP support is specifically requested.
		parallelismComponent.setId(parallelId);
		parallelismComponent.setName("Parallel Execution");
		parallelismComponent
				.setDescription("Specify the number of OpenMP "
						+ "threads, TBB Threads, or MPI processes that should be used for the Job.");

		// Create an Entry to hold an account code/project name
		Entry accountEntry = new Entry() {
			@Override
			public void setup() {
				setId(3);
				setName("Account Code/Project Code");
				setDescription("Account code or project name that "
						+ "should be used when launching the simulation.");
			    allowedValueType = AllowedValueType.Undefined;
			    defaultValue = "none";
				return;
			}
		};
		// Add the Entry to the component
		parallelismComponent.addEntry(accountEntry);
		
		// Add the component
		addComponent(parallelismComponent);

		// Throw the flag to mark the component as enabled.
		parallelComponentEnabled = true;

		return parallelismComponent;
	}

	/**
	 * <p>
	 * This operation retrieves the parallelism component from the set and
	 * returns it. If it is not already in the set of components for the Form
	 * then it creates it.
	 * </p>
	 * 
	 * @return <p>
	 *         The parallelism component.
	 *         </p>
	 */
	private DataComponent getParallelismComponent() {

		return (parallelComponentEnabled) ? (DataComponent) getComponent(parallelId)
				: createParallelismComponent();
	}

	/**
	 * <p>
	 * This operation removes the parallelism component from the set of
	 * components in the JobLauncherForm.
	 * </p>
	 * 
	 */
	private void removeParallelismComponent() {

		// Remove the parallelism component
		removeComponent(parallelId);

		// Reset the flag so that the component will be marked as removed.
		parallelComponentEnabled = false;

		return;
	}
}