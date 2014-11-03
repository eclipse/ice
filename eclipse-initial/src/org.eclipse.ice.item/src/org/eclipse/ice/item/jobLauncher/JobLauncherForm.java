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

import org.eclipse.ice.datastructures.form.Form;

import java.awt.print.Paper;
import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.ResourceComponent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
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
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the OpenMP Entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static final String openMPEntryName = "Number of OpenMP Threads";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the MPI Entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static final String mpiEntryName = "Number of MPI Processes";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the OpenMP Entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static final String TBBEntryName = "Number of TBB Threads";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public JobLauncherForm() {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation enables support for MPI that allows a client to set the
	 * number of threads for the job. A value of zero or less than zero passed
	 * to this operation will be ignore and set to 1. Furthermore, the maximum
	 * must be greater than the minimum and the default value must be equal to
	 * or in between the maximum and minimum. If these conditions are not met,
	 * it will not be enabled.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void enableMPI(int minProcesses, int maxProcesses,
			int defaultProcesses) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables support for MPI. The threading component will
	 * only be disabled if other threading support is also disabled.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disableMPI() {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation enables support for OpenMP that allows a client to set the
	 * number of threads for the job. A value of zero or less than zero passed
	 * to this operation will be ignore and set to 1. Furthermore, the maximum
	 * must be greater than the minimum and the default value must be equal to
	 * or in between the maximum and minimum. If these conditions are not met,
	 * it will not be enabled.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void enableOpenMP(int minThreads, int maxThreads, int defaultThreads) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables support for OpenMP. The threading component will
	 * only be disabled if other threading support is also disabled.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disableOpenMP() {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation enables support for Intel's Thread Building Blocks that
	 * allows a client to set the number of threads for the job. A value of zero
	 * or less than zero passed to this operation will be ignore and set to 1.
	 * Furthermore, the maximum must be greater than the minimum and the default
	 * value must be equal to or in between the maximum and minimum. If these
	 * conditions are not met, it will not be enabled.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void enableTBB(int minThreads, int maxThreads, int defaultThreads) {
		// begin-user-code
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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables support for Intel Thread Building Blocks. The
	 * threading component will only be disabled if other threading support is
	 * also disabled.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disableTBB() {
		// begin-user-code
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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the input files that may be selected in the Form for
	 * the input type with the specified name. Passing null for the name or file
	 * list will not make any changes to the Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 * @param desc
	 * @param files
	 *            <p>
	 *            The list of inputFiles available for the job.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setInputFiles(String name, String desc, ArrayList<String> files) {
		// begin-user-code

		// Local Declarations
		int oldId = 0;
		Entry oldEntry = null;
		DataComponent fileComponent = ((DataComponent) getComponent(1));
		final ArrayList<String> finalFiles = (files != null) ? (ArrayList<String>) files
				.clone() : new ArrayList<String>();
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
			public void setup() {
				// The input file should be one of the files in the "inputFiles"
				// array.
				this.allowedValues = finalFiles;
				this.allowedValueType = AllowedValueType.Discrete;

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates the parallelism component for the Form and returns
	 * it. It also adds it to the Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The parallelism component.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private DataComponent createParallelismComponent() {
		// begin-user-code

		// Local Declarations
		DataComponent parallelismComponent = new DataComponent();

		// Setup the Parallel Execution Data Component. It is not added until
		// TBB, MPI or OpenMP support is specifically requested.
		parallelismComponent.setId(parallelId);
		parallelismComponent.setName("Parallel Execution");
		parallelismComponent
				.setDescription("Specify the number of OpenMP "
						+ "threads, TBB Threads, or MPI processes that should be used for the Job.");

		// Add the component
		addComponent(parallelismComponent);

		// Throw the flag to mark the component as enabled.
		parallelComponentEnabled = true;

		return parallelismComponent;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the parallelism component from the set and
	 * returns it. If it is not already in the set of components for the Form
	 * then it creates it.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The parallelism component.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private DataComponent getParallelismComponent() {
		// begin-user-code

		return (parallelComponentEnabled) ? (DataComponent) getComponent(parallelId)
				: createParallelismComponent();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation removes the parallelism component from the set of
	 * components in the JobLauncherForm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void removeParallelismComponent() {
		// begin-user-code

		// Remove the parallelism component
		removeComponent(parallelId);

		// Reset the flag so that the component will be marked as removed.
		parallelComponentEnabled = false;

		return;
		// end-user-code
	}
}