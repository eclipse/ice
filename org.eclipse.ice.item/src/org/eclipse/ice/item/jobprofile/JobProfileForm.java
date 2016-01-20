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
import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.entry.ContinuousEntry;
import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.TableComponent;

/**
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
 * 
 * @author Jay Jay Billings
 */
public class JobProfileForm extends Form {
	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 */
	public JobProfileForm() {

		// Local Declarations
		DataComponent exeInfo = null;
		DataComponent threadOps = null;
		TableComponent hostnames = null;
		TableComponent dataFiles = null;
		int entryId = 1;

		IEntry jobName = null, execName = null, params = null;
		IEntry openMPEntry = null, nThreads = null, mpiEntry = null, 
				nProcs = null, tbbEntry = null, nTbbThreads = null;
		
		ArrayList<IEntry> rowTemplate = null;

		// Setup JobProfile name and description
		setName("Job Profile Editor");
		setDescription("Create or edit a Job Profile that will be used " + "by ICE to launch jobs.");

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
				+ "the job, input file and output types, the executable, and " + "parameters.");

		// Setup the Entry - Job Profile
		jobName = new StringEntry();
		jobName.setName("Job Name");
		jobName.setDescription("This is a name for the job profile.");
		jobName.setDefaultValue("JobProfile");
		jobName.setTag("jobName");

		// Set the id and increment the counter
		jobName.setId(entryId);
		entryId++;
		// Add to component
		exeInfo.addEntry(jobName);

		// Setup the Entry - Executable Name
		execName = new StringEntry();
		execName.setName("Executable Name");
		execName.setDescription(
				"Specifies the command line argument to " + "execute the job (excludes parameters for the job). "
						+ "If the executable is not installed on the path, you "
						+ "should include the ${installDir} variable in the " + "appropriate place.");
		execName.setDefaultValue("ls");
		execName.setTag("executableName");

		// Set the id and increment the counter
		execName.setId(entryId);
		entryId++;
		// Add to component
		exeInfo.addEntry(execName);

		// Setup the Entry - Parameters
		params = new StringEntry();
		params.setName("Parameters");
		params.setDescription("Specifies the parameters for a job.");
		params.setTag("parameters");

		// Set the id and increment the counter
		params.setId(entryId);
		entryId++;
		// Add to component
		exeInfo.addEntry(params);

		// Setup the Threading Options
		threadOps.setName("Threading Options");
		threadOps.setDescription("This section contains tools to enable "
				+ "certain threading or processes options for MPI " + "and OpenMP.");

		// Setup the Entry - Enable OpenMP
		openMPEntry = new DiscreteEntry("yes", "no");
		openMPEntry.setName("Enable OpenMP");
		openMPEntry.setDescription("Specifies if the program utilizes OpenMP library.");
		openMPEntry.setDefaultValue("no");
		openMPEntry.setTag("enableOpenMP");

		// Set the id and increment the counter
		openMPEntry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(openMPEntry);

		// Setup the Entry - Number of Threads
		nThreads = new ContinuousEntry("1","128")  {
			@Override
			public void update(IUpdateable updateable) {
				if (updateable instanceof DiscreteEntry) {
					DiscreteEntry entry = (DiscreteEntry) updateable;
					if ("Enable OpenMP".equals(entry.getName())) {
						boolean turnOn = entry.getValue().equals("yes") ? true : false;
						setReady(turnOn);
					}
				}
			}
		};
		nThreads.setName("Default Number of Threads");
		nThreads.setDescription("Specifies the default number of threads " + "for OpenMP.");
		nThreads.setDefaultValue("1");
		nThreads.setTag("numOfThreads");
		nThreads.setReady(false);

		// Register for selections of the Open MP Entry
		openMPEntry.register(nThreads);
		
		// Set the id and increment the counter
		nThreads.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(nThreads);

		// Setup the Entry - Enable MPI
		mpiEntry = new DiscreteEntry("no", "yes");
		mpiEntry.setName("Enable MPI");
		mpiEntry.setDescription("Specifies if the program utilizes " + "MPI library.");
		mpiEntry.setDefaultValue("no");
		mpiEntry.setValue("no");
		mpiEntry.setTag("enableMPI");

		// Set the id and increment the counter
		mpiEntry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(mpiEntry);

		// Setup the Entry - Number of Processes
		nProcs = new ContinuousEntry("1", "512000") {
			@Override
			public void update(IUpdateable updateable) {
				if (updateable instanceof DiscreteEntry) {
					DiscreteEntry entry = (DiscreteEntry) updateable;
					if ("Enable MPI".equals(entry.getName())) {
						boolean turnOn = entry.getValue().equals("yes") ? true : false;
						setReady(turnOn);
					}
				}
			}
		};
		nProcs.setName("Default Number of Processes");
		nProcs.setDescription("Specifies the default number of processes " + "for MPI.");
		nProcs.setDefaultValue("1");
		nProcs.setTag("numOfProcesses");
		nProcs.setReady(false);
		
		// Register for selections of the MPI Entry
		mpiEntry.register(nProcs);
		
		// Set the id and increment the counter
		nProcs.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(nProcs);

		// Setup the Entry - Enable Thread Building Block
		tbbEntry = new DiscreteEntry("no", "yes");
		tbbEntry.setName("Enable TBB");
		tbbEntry.setDescription("Specifies if the program utilizes " + "Thread Building Blocks.");
		tbbEntry.setDefaultValue("no");
		tbbEntry.setTag("enableTBB");

		// Set the id and increment the counter
		tbbEntry.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(tbbEntry);

		// Setup the Entry - Number of Thread Building Blocks
		nTbbThreads = new ContinuousEntry("1", "128") {
			@Override
			public void update(IUpdateable updateable) {
				if (updateable instanceof DiscreteEntry) {
					DiscreteEntry entry = (DiscreteEntry) updateable;
					if ("Enable TBB".equals(entry.getName())) {
						boolean turnOn = entry.getValue().equals("yes") ? true : false;
						setReady(turnOn);
					}
				}
			}
		};
		nTbbThreads.setName("Default Number of TBBs");
		nTbbThreads.setDescription("Specifies the default number of " + "Thread Blocks.");
		nTbbThreads.setDefaultValue("1");
		nTbbThreads.setTag("numOfTBBs");
		nTbbThreads.setReady(false);
		
		// Register for selections of the TBB Entry
		tbbEntry.register(nTbbThreads);
		
		// Set the id and increment the counter
		nTbbThreads.setId(entryId);
		entryId++;
		// Add to component
		threadOps.addEntry(nTbbThreads);

		// Setup the Hostnames
		hostnames.setName("Hostnames");
		hostnames.setDescription("This section contains information pertaining "
				+ "to the hostname, operating system, and installed " + "directories for the executables.");

		// Setup Template
		rowTemplate = new ArrayList<IEntry>();
		// Setup Entries to Template

		// Setup the Entry - Hostname
		IEntry entry = new StringEntry();
		entry.setName("Hostname");
		entry.setDescription(
				"Specifies the location of the executable. " + " Use localhost for executables stored locally.");
		entry.setDefaultValue("localhost");
		entry.setValue("localhost");
		entry.setTag("hostname");
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate.add(entry);

		// Setup the Entry - Operating System
		entry = new DiscreteEntry("Linux x86_x64", "Linux x86", "Windows x86", "Windows x64", "Mac OSX");
		entry.setName("Operating System");
		entry.setDescription("Specifies the operating system installed " + "for the hostname.");
		entry.setDefaultValue("Linux x86_x64");
		entry.setValue(entry.getDefaultValue());
		entry.setTag("operatingSystem");

		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate.add(entry);

		// Setup the Entry - Install Directory
		entry = new StringEntry();
		entry.setName("Install Directory");
		entry.setDescription("Specifies the install directory for a hostname.");
		entry.setDefaultValue("/bin");
		entry.setValue("/bin");
		entry.setTag("installDirectory");
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
		dataFiles.setDescription("This section contains information " + "pertaining to required files for the program "
				+ "to function nominally.");

		// Setup Template
		ArrayList<IEntry> rowTemplate2 = new ArrayList<IEntry>();
		// Setup Entries to Template

		// Setup the Entry - Data File
		entry = new StringEntry();
		entry.setName("Data File");
		entry.setDescription("Specifies a data file required to run " + "an executable.");
		entry.setDefaultValue("text.txt");
		entry.setValue(entry.getDefaultValue());
		entry.setTag("dataFile");
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate2.add(entry);

		// Setup the Entry - File Path
		entry = new StringEntry();
		entry.setName("File Path");
		entry.setDescription("Specifies the install directory " + "for a data file.");
		entry.setDefaultValue("/opt/bin");
		entry.setValue("/opt/bin");
		entry.setTag("filePath");
		// Set the id and increment the counter
		entry.setId(entryId);
		entryId++;
		// Add to row template
		rowTemplate2.add(entry);

		// Add rowTemplate to table
		dataFiles.setRowTemplate(rowTemplate2);

	}
}