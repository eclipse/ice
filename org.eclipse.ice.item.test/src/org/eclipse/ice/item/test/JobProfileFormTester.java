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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.item.jobprofile.JobProfileForm;
import org.junit.Test;

/**
 * <p>
 * This class checks the JobProfileForm.
 * </p>
 * 
 * @author Jay Jay Billings
 */

public class JobProfileFormTester {
	/**
	 * 
	 */
	private JobProfileForm jobProfileForm;

	/**
	 * <p>
	 * This operation checks the contents of the JobProfileForm to make sure
	 * that they are consistent with the specification.
	 * </p>
	 * 
	 */
	@Test
	public void checkFormContents() {

		// Local Declarations
		DataComponent exeInfo = null;
		DataComponent threadOps = null;
		TableComponent hostnames = null;
		TableComponent dataFiles = null;
		int entryId = 1;

		Entry entry = null;

		// Create a new JobProfileForm
		jobProfileForm = new JobProfileForm();

		// Check jobProfile
		assertEquals("Job Profile Editor", jobProfileForm.getName());
		assertEquals(
				"Create or edit a Job Profile that will be used by ICE to launch jobs.",
				jobProfileForm.getDescription());

		// Check contents. Make sure labels are set correctly, default values,
		// etc.

		// Check that there are 4 components
		// 2 data components and 2 table components.

		assertEquals(4, jobProfileForm.getComponents().size());
		assertEquals(DataComponent.class, jobProfileForm.getComponents().get(0)
				.getClass());
		assertEquals(DataComponent.class, jobProfileForm.getComponents().get(1)
				.getClass());
		assertEquals(TableComponent.class, jobProfileForm.getComponents()
				.get(2).getClass());
		assertEquals(TableComponent.class, jobProfileForm.getComponents()
				.get(3).getClass());

		// Check first Component - Execution Information
		// get the first dataComponent
		exeInfo = (DataComponent) jobProfileForm.getComponents().get(0);
		// check name and description for section
		assertEquals("Execution Information", exeInfo.getName());
		assertEquals(
				"This block contains information pertaining to the execution of the program.  This includes the name of the job, input file and output types, the executable, and parameters.",
				exeInfo.getDescription());

		// check entries of component name, description, and default values;
		// Job Name
		entry = (Entry) exeInfo.retrieveAllEntries().get(0);
		// check name, description, and values
		assertEquals("Job Name", entry.getName());
		assertEquals("This is a name for the job profile.",
				entry.getDescription());
		assertEquals("JobProfile", entry.getDefaultValue());
		assertEquals("JobProfile", entry.getValue());
		assertEquals(AllowedValueType.Undefined, entry.getValueType());
		assertEquals("jobName", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Executable Name
		entry = (Entry) exeInfo.retrieveAllEntries().get(1);
		// check name, description, and values
		assertEquals("Executable Name", entry.getName());
		assertEquals("ls", entry.getDefaultValue());
		assertEquals("ls", entry.getValue());
		assertEquals("Specifies the command line argument to "
				+ "execute the job (excludes parameters for the job). "
				+ "If the executable is not installed on the path, you "
				+ "should include the ${installDir} variable in the "
				+ "appropriate place.", entry.getDescription());
		assertEquals(AllowedValueType.Undefined, entry.getValueType());
		assertEquals("executableName", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Commented out due to it will not show on the profile at this time
		/*
		 * //Input File entry = (Entry) exeInfo.retrieveAllEntries().get(2);
		 * //check name, description, and values assertEquals("Input File",
		 * entry.getName()); assertEquals("inputFile.txt",
		 * entry.getDefaultValue()); assertEquals("inputFile.txt",
		 * entry.getValue());
		 * assertEquals("Specifies the input file for the simulator.",
		 * entry.getDescription()); assertEquals(AllowedValueType.Undefined,
		 * entry.getValueType()); assertEquals("inputFile", entry.getTag());
		 */

		// Parameters
		// Input File
		entry = (Entry) exeInfo.retrieveAllEntries().get(2);
		// check name, description, and values
		assertEquals("Parameters", entry.getName());
		assertEquals("Specifies the parameters for a job.",
				entry.getDescription());
		assertEquals(AllowedValueType.Undefined, entry.getValueType());
		assertEquals("parameters", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Commented out due to it will not show on the profile at this time
		/*
		 * //Output File Type entry = (Entry)
		 * exeInfo.retrieveAllEntries().get(4); //check name, description, and
		 * values assertEquals("Output File Type", entry.getName());
		 * assertEquals("*.type", entry.getDefaultValue());
		 * assertEquals("*.type", entry.getValue());
		 * assertEquals("Specifies the output file type for a simulator.",
		 * entry.getDescription()); assertEquals(AllowedValueType.Undefined,
		 * entry.getValueType()); assertEquals("outputFileType",
		 * entry.getTag());
		 */

		// Check second Component - Threading Options
		// get the second dataComponent
		threadOps = (DataComponent) jobProfileForm.getComponents().get(1);
		// check name and description for section
		assertEquals("Threading Options", threadOps.getName());
		assertEquals(
				"This section contains tools to enable certain threading or processes options for MPI and OpenMP.",
				threadOps.getDescription());

		// check entries of component name, description, and default values;
		// Enable OpenMP
		entry = (Entry) threadOps.retrieveAllEntries().get(0);
		// check name, description, and values
		assertEquals("Enable OpenMP", entry.getName());
		assertEquals("Specifies if the program utilizes OpenMP library.",
				entry.getDescription());
		assertEquals("No", entry.getDefaultValue());
		assertEquals("No", entry.getValue());
		assertEquals("Yes", entry.getAllowedValues().get(0));
		assertEquals("No", entry.getAllowedValues().get(1));
		assertEquals(AllowedValueType.Discrete, entry.getValueType());
		assertEquals("enableOpenMP", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Number of Threads
		entry = (Entry) threadOps.retrieveAllEntries().get(1);
		// check name, description, and values
		assertEquals("Default Number of Threads", entry.getName());
		assertEquals("Specifies the default number of threads for OpenMP.",
				entry.getDescription());
		assertEquals("1", entry.getDefaultValue());
		assertEquals("1", entry.getValue());
		assertEquals("1", entry.getAllowedValues().get(0));
		assertEquals("128", entry.getAllowedValues().get(1));
		assertEquals(AllowedValueType.Continuous, entry.getValueType());
		assertEquals("numOfThreads", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Enable MPI
		entry = (Entry) threadOps.retrieveAllEntries().get(2);
		// check name, description, and values
		assertEquals("Enable MPI", entry.getName());
		assertEquals("Specifies if the program utilizes MPI library.",
				entry.getDescription());
		assertEquals("No", entry.getDefaultValue());
		assertEquals("No", entry.getValue());
		assertEquals("Yes", entry.getAllowedValues().get(0));
		assertEquals("No", entry.getAllowedValues().get(1));
		assertEquals(AllowedValueType.Discrete, entry.getValueType());
		assertEquals("enableMPI", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Number of Processes
		entry = (Entry) threadOps.retrieveAllEntries().get(3);
		// check name, description, and values
		assertEquals("Default Number of Processes", entry.getName());
		assertEquals("Specifies the default number of processes for MPI.",
				entry.getDescription());
		assertEquals("1", entry.getDefaultValue());
		assertEquals("1", entry.getValue());
		assertEquals("1", entry.getAllowedValues().get(0));
		assertEquals("512000", entry.getAllowedValues().get(1));
		assertEquals(AllowedValueType.Continuous, entry.getValueType());
		assertEquals("numOfProcesses", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Enable TBB
		entry = (Entry) threadOps.retrieveAllEntries().get(4);
		// check name, description, and values
		assertEquals("Enable TBB", entry.getName());
		assertEquals(
				"Specifies if the program utilizes Thread Building Blocks.",
				entry.getDescription());
		assertEquals("No", entry.getDefaultValue());
		assertEquals("No", entry.getValue());
		assertEquals("Yes", entry.getAllowedValues().get(0));
		assertEquals("No", entry.getAllowedValues().get(1));
		assertEquals(AllowedValueType.Discrete, entry.getValueType());
		assertEquals("enableTBB", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Number of TBBs
		entry = (Entry) threadOps.retrieveAllEntries().get(5);
		// check name, description, and values
		assertEquals("Default Number of TBBs", entry.getName());
		assertEquals("Specifies the default number of Thread Blocks.",
				entry.getDescription());
		assertEquals("1", entry.getDefaultValue());
		assertEquals("1", entry.getValue());
		assertEquals("1", entry.getAllowedValues().get(0));
		assertEquals("128", entry.getAllowedValues().get(1));
		assertEquals(AllowedValueType.Continuous, entry.getValueType());
		assertEquals("numOfTBBs", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Check third Component - Hostnames
		// get the second dataComponent
		hostnames = (TableComponent) jobProfileForm.getComponents().get(2);
		// check name and description for section
		assertEquals("Hostnames", hostnames.getName());
		assertEquals(
				"This section contains information pertaining to the hostname, operating system, and installed directories for the executables.",
				hostnames.getDescription());

		// Check the tableComponent's properties - initialized correctly
		assertEquals(3, hostnames.numberOfColumns());
		// It will have a row pre-initialized
		assertEquals(1, hostnames.numberOfRows());

		// check column names
		assertEquals(3, hostnames.getColumnNames().size());
		assertEquals("Hostname", hostnames.getColumnNames().get(0));
		assertEquals("Operating System", hostnames.getColumnNames().get(1));
		assertEquals("Install Directory", hostnames.getColumnNames().get(2));

		// check entries of component name, description, and default values;
		// Hostname
		entry = (Entry) hostnames.getRowTemplate().get(0);
		// check name, description, and values
		assertEquals("Hostname", entry.getName());
		assertEquals(
				"Specifies the location of the executable.  Use localhost for executables stored locally.",
				entry.getDescription());
		assertEquals("localhost", entry.getDefaultValue());
		assertEquals("localhost", entry.getValue());
		assertEquals(AllowedValueType.Undefined, entry.getValueType());
		assertEquals("hostname", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Operating System
		entry = (Entry) hostnames.getRowTemplate().get(1);
		// check name, description, and values
		assertEquals("Operating System", entry.getName());
		assertEquals(
				"Specifies the operating system installed for the hostname.",
				entry.getDescription());
		assertEquals("Linux x86_x64", entry.getDefaultValue());
		assertEquals("Linux x86_x64", entry.getValue());
		assertEquals("Linux x86_x64", entry.getAllowedValues().get(0));
		assertEquals("Linux x86", entry.getAllowedValues().get(1));
		assertEquals("Windows x86", entry.getAllowedValues().get(2));
		assertEquals("Windows x64", entry.getAllowedValues().get(3));
		assertEquals("Mac OSX", entry.getAllowedValues().get(4));
		assertEquals(AllowedValueType.Discrete, entry.getValueType());
		assertEquals("operatingSystem", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// Install Directory
		entry = (Entry) hostnames.getRowTemplate().get(2);
		// check name, description, and values
		assertEquals("Install Directory", entry.getName());
		assertEquals("Specifies the install directory for a hostname.",
				entry.getDescription());
		assertEquals("/bin", entry.getDefaultValue());
		assertEquals("/bin", entry.getValue());
		assertEquals(AllowedValueType.Undefined, entry.getValueType());
		assertEquals("installDirectory", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// check the first row's values
		Entry entry1 = null;
		Entry entry2 = null;
		Entry entry3 = null;
		entry1 = (Entry) hostnames.getRow(0).get(0);
		entry2 = (Entry) hostnames.getRow(0).get(1);
		entry3 = (Entry) hostnames.getRow(0).get(2);

		assertEquals("localhost", entry1.getValue());
		assertEquals("Linux x86_x64", entry2.getValue());
		assertEquals("/bin", entry3.getValue());

		// Check forth Component - Data Files
		// get the second dataComponent
		dataFiles = (TableComponent) jobProfileForm.getComponents().get(3);
		// check name and description for section
		assertEquals("Data Files", dataFiles.getName());
		assertEquals(
				"This section contains information pertaining to required files for the program to function nominally.",
				dataFiles.getDescription());

		// Check the tableComponent's properties - initialized correctly
		assertEquals(2, dataFiles.numberOfColumns());
		assertEquals(0, dataFiles.numberOfRows());

		// check column names
		assertEquals(2, dataFiles.getColumnNames().size());
		assertEquals("Data File", dataFiles.getColumnNames().get(0));
		assertEquals("File Path", dataFiles.getColumnNames().get(1));

		// check entries of component name, description, and default values;
		// Data File
		entry = (Entry) dataFiles.getRowTemplate().get(0);
		// check name, description, and values
		assertEquals("Data File", entry.getName());
		assertEquals("Specifies a data file required to run an executable.",
				entry.getDescription());
		assertEquals("text.txt", entry.getDefaultValue());
		assertEquals("text.txt", entry.getValue());
		assertEquals(AllowedValueType.Undefined, entry.getValueType());
		assertEquals("dataFile", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		// File Path
		entry = (Entry) dataFiles.getRowTemplate().get(1);
		// check name, description, and values
		assertEquals("File Path", entry.getName());
		assertEquals("Specifies the install directory for a data file.",
				entry.getDescription());
		assertEquals("/opt/bin", entry.getDefaultValue());
		assertEquals("/opt/bin", entry.getValue());
		assertEquals(AllowedValueType.Undefined, entry.getValueType());
		assertEquals("filePath", entry.getTag());
		// Check the Entry's id and increment the counter.
		assertEquals(entryId, entry.getId());
		entryId++;

		return;

	}
}