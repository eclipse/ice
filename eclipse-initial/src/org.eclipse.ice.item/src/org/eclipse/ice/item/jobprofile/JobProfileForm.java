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
package org.eclipse.ice.item.jobprofile;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.TableComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The JobProfileForm is a subclass of Form that is specialized to work with the
 * JobProfile Item. It contains two Data Components ("Execution Information" and
 * "Threading Options") and two TableComponents ("Hostnames" and "Data Files").
 * Its exact specification is given by the following Painfully Simple Form:
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">#A PSF of the ICE Job
 * Profile Editor</span><span style="color:#000000;font-family:"Courier
 * New";"></span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">#Form name,
 * description and type</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">formName=Job Profile
 * Editor</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">formDescription=Create
 * or edit a Job Profile that will be used by ICE to launch jobs</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">formType=Model</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Execution
 * Information</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">groupDescription=This
 * block contains information pertaining to the execution of the program. This
 * includes the name of the job, input file and output types, the executable,
 * and parameters.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Threading
 * Options</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">groupDescription=This
 * section contains tools to enable certain threading or processes options for
 * MPI and OpenMP.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">table=Hostnames</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tableDescription=This
 * section contains information pertaining to the hostname, operating system,
 * and installed directories for the executables.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">table=Data
 * Files</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tableDescription=This
 * section contains information pertaining to required files for the program to
 * function nominally.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Job Name</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=This is a
 * name for the job profile.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">defaultValue=JobProfile</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tag=jobName</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Execution
 * Information</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Executable
 * Name</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the command line argument to execute the job (excludes parameters for the
 * job).</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">defaultValue=ls</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">tag=executableName</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Execution
 * Information</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Input File</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the input file for the job.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">defaultValue=inputFile.txt</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tag=inputFile</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Execution
 * Information</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Parameters</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the parameters for a job.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tag=parameters</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Execution
 * Information</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Output File
 * Type</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the output file type for a job.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">defaultValue=*.type</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">tag=outputFileType</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Execution
 * Information</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Enable
 * OpenMP</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * if the program utilizes OpenMP library.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">defaultValue=No</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Discrete</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValue=Yes</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=No</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">tag=enableOpenMP</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Threading
 * Options</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Number of
 * Threads</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the number of threads for the OpenMP library.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">defaultValue=1</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Continuous</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=1</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValue=128</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">tag=numOfThreads</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Threading
 * Options</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Enable MPI</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * if the program utilizes MPI library.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">defaultValue=No</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Discrete</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValue=Yes</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=No</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tag=enableMPI</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Threading
 * Options</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Number of
 * Processes</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifes
 * the number of processes for the MPI library.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">defaultValue=1</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Continuous</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=1</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValue=512000</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">tag=numOfProcesses</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Threading
 * Options</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Hostname</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the location of the executable.  Use localhost for executables stored
 * locally.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">defaultValue=localhost</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tag=hostname</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Hostnames</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Operating
 * System</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the operating system installed for the hostname.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">defaultValue=Linux
 * x86_x64</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Discrete</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=Linux
 * x86_x64</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=Linux
 * x86</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=Windows
 * x86</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=Windows
 * x64</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">allowedValue=Mac
 * OSX</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">tag=operatingSystem</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Hostnames</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Install
 * Directory</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the install directory for a hostname.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">defaultValue=/opt/bin</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">tag=installDirectory</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Hostnames</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=Data File</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * a data file required to run an executable.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">defaultValue=text.txt</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tag=dataFile</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Data
 * Files</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";"> </span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">name=File Path</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">description=Specifies
 * the install directory for a data file.</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">defaultValue=/opt/bin</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier
 * New";">allowedValueType=Undefined</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">tag=filePath</span>
 * </p>
 * <p class="MsoNormal">
 * <span style="color:#000000;font-family:"Courier New";">group=Data
 * Files</span>
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class JobProfileForm extends Form {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public JobProfileForm() {
		// begin-user-code

		// Local Declarations
		DataComponent exeInfo = null;
		DataComponent threadOps = null;
		TableComponent hostnames = null;
		TableComponent dataFiles = null;
		int entryId = 1;

		Entry entry = null;

		ArrayList<Entry> rowTemplate = null;

		// Setup JobProfile name and description
		setName("Job Profile Editor");
		setDescription("Create or edit a Job Profile that will be used "
				+ "by ICE to launch jobs.");

		// Create Components
		exeInfo = new DataComponent();
		threadOps = new DataComponent();
		hostnames = new TableComponent();
		dataFiles = new TableComponent();

		// Add components to form.
		addComponent(exeInfo);
		addComponent(threadOps);
		addComponent(hostnames);
		addComponent(dataFiles);

		// Setup the Execution Information
		exeInfo.setName("Execution Information");
		exeInfo.setDescription("This block contains information pertaining to"
				+ " the execution of the program.  This includes the name of "
				+ "the job, input file and output types, the executable, and "
				+ "parameters.");

		// Setup the Entry - Job Profile
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Job Name");
				this.setDescription("This is a name for the job profile.");
				this.defaultValue = "JobProfile";
				this.allowedValueType = AllowedValueType.Undefined;
				this.tag = "jobName";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		exeInfo.addEntry(entry);

		// Setup the Entry - Executable Name
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Executable Name");
				this.setDescription("Specifies the command line argument to "
						+ "execute the job (excludes parameters for the job). "
						+ "If the executable is not installed on the path, you "
						+ "should include the ${installDir} variable in the "
						+ "appropriate place.");
				this.defaultValue = "ls";
				this.allowedValueType = AllowedValueType.Undefined;
				this.tag = "executableName";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		exeInfo.addEntry(entry);

		// Setup the Entry - Parameters
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Parameters");
				this.setDescription("Specifies the parameters for a job.");
				this.allowedValueType = AllowedValueType.Undefined;
				this.tag = "parameters";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		exeInfo.addEntry(entry);

		// Setup the Threading Options
		threadOps.setName("Threading Options");
		threadOps.setDescription("This section contains tools to enable "
				+ "certain threading or processes options for MPI "
				+ "and OpenMP.");

		// Setup the Entry - Enable OpenMP
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Enable OpenMP");
				this.setDescription("Specifies if the program utilizes "
						+ "OpenMP library.");
				this.defaultValue = "No";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("Yes");
				this.allowedValues.add("No");
				this.allowedValueType = AllowedValueType.Discrete;
				this.tag = "enableOpenMP";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(entry);

		// Setup the Entry - Number of Threads
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Default Number of Threads");
				this.setDescription("Specifies the default number of threads "
						+ "for OpenMP.");
				this.defaultValue = "1";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("1");
				this.allowedValues.add("128");
				this.allowedValueType = AllowedValueType.Continuous;
				this.tag = "numOfThreads";
				this.setParent("Enable OpenMP");
				this.setReady(false);
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(entry);

		// Setup the Entry - Enable MPI
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Enable MPI");
				this.setDescription("Specifies if the program utilizes "
						+ "MPI library.");
				this.defaultValue = "No";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("Yes");
				this.allowedValues.add("No");
				this.allowedValueType = AllowedValueType.Discrete;
				this.tag = "enableMPI";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(entry);

		// Setup the Entry - Number of Processes
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Default Number of Processes");
				this.setDescription("Specifies the default number of processes "
						+ "for MPI.");
				this.defaultValue = "1";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("1");
				this.allowedValues.add("512000");
				this.allowedValueType = AllowedValueType.Continuous;
				this.tag = "numOfProcesses";
				this.setParent("Enable MPI");
				this.setReady(false);
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(entry);

		// Setup the Entry - Enable Thread Building Block
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Enable TBB");
				this.setDescription("Specifies if the program utilizes "
						+ "Thread Building Blocks.");
				this.defaultValue = "No";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("Yes");
				this.allowedValues.add("No");
				this.allowedValueType = AllowedValueType.Discrete;
				this.tag = "enableTBB";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(entry);

		// Setup the Entry - Number of Thread Building Blocks
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Default Number of TBBs");
				this.setDescription("Specifies the default number of "
						+ "Thread Blocks.");
				this.defaultValue = "1";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("1");
				this.allowedValues.add("128");
				this.allowedValueType = AllowedValueType.Continuous;
				this.tag = "numOfTBBs";
				this.setParent("Enable TBB");
				this.setReady(false);
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(entry);

		// Setup the Hostnames
		hostnames.setName("Hostnames");
		hostnames
				.setDescription("This section contains information pertaining "
						+ "to the hostname, operating system, and installed "
						+ "directories for the executables.");

		// Setup Template
		rowTemplate = new ArrayList<Entry>();
		// Setup Entries to Template

		// Setup the Entry - Hostname
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Hostname");
				this.setDescription("Specifies the location of the executable. "
						+ " Use localhost for executables stored locally.");
				this.defaultValue = "localhost";
				this.allowedValueType = AllowedValueType.Undefined;
				this.tag = "hostname";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate.add(entry);

		// Setup the Entry - Operating System
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Operating System");
				this.setDescription("Specifies the operating system installed "
						+ "for the hostname.");
				this.defaultValue = "Linux x86_x64";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("Linux x86_x64");
				this.allowedValues.add("Linux x86");
				this.allowedValues.add("Windows x86");
				this.allowedValues.add("Windows x64");
				this.allowedValues.add("Mac OSX");
				this.allowedValueType = AllowedValueType.Discrete;
				this.tag = "operatingSystem";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate.add(entry);

		// Setup the Entry - Install Directory
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Install Directory");
				this.setDescription("Specifies the install directory for a hostname.");
				this.defaultValue = "/bin";
				this.allowedValueType = AllowedValueType.Undefined;
				this.tag = "installDirectory";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate.add(entry);

		// Add rowTemplate to table
		hostnames.setRowTemplate(rowTemplate);

		// Automatically add a row
		hostnames.addRow();

		// Setup the Data Files
		dataFiles.setName("Data Files");
		dataFiles.setDescription("This section contains information "
				+ "pertaining to required files for the program "
				+ "to function nominally.");

		// Setup Template
		ArrayList<Entry> rowTemplate2 = new ArrayList<Entry>();
		// Setup Entries to Template

		// Setup the Entry - Data File
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("Data File");
				this.setDescription("Specifies a data file required to run "
						+ "an executable.");
				this.defaultValue = "text.txt";
				this.allowedValueType = AllowedValueType.Undefined;
				this.tag = "dataFile";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate2.add(entry);

		// Setup the Entry - File Path
		entry = new Entry() {

			@Override
			protected void setup() {
				this.setName("File Path");
				this.setDescription("Specifies the install directory "
						+ "for a data file.");
				this.defaultValue = "/opt/bin";
				this.allowedValueType = AllowedValueType.Undefined;
				this.tag = "filePath";
			}
		};
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate2.add(entry);

		// Add rowTemplate to table
		dataFiles.setRowTemplate(rowTemplate2);

		// end-user-code
	}
}