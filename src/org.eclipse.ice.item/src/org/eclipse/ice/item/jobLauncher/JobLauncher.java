/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBManipulator;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.action.JobLaunchAction;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The JobLauncher class handles local and remote job launch of simulations and
 * other tasks. It implements process(), reviewEntries() and setupForm().
 * Subclasses must override setupForm(), call super.setupForm() to properly
 * construct the Form and JobLauncher Item and call setExecutable() with the
 * executable name, description and command name. It uses a LocalExecutorAction
 * or a RemoteExecutorAction, depending on whether or not the job should be
 * launched locally or remotely. If the job is launched remotely, the
 * JobLauncher may need to request additional information for a username and
 * password, (possibly even a keyboard-interactive password).
 * </p>
 * <p>
 * The platforms on which the job can be launched must be configured with the
 * addHost() operation.
 * </p>
 * <p>
 * It is possible to direct the JobLauncher to enable support for parallelism
 * via TBB, OpenMP and MPI. This will only work so long as MPI is installed on
 * the remote system and available on the user's PATH and if the executable is
 * compiled with MPI, TBB and/or OpenMP support. It is possible to enable MPI,
 * TBB and/or OpenMP support at the same time.
 * </p>
 * <p>
 * The JobLauncher gets its input information from the DataComponent on the
 * JobLauncherForm and it places ICEResources that represent its output on the
 * ResourceComponent on the JobLauncherForm.
 * </p>
 * <p>
 * The setExecutable() operation uses string replacement to replace a select
 * number of flags in the executable string. See that operation for details. The
 * flags are:
 * </p>
 * <p>
 * ${installDir} - The installation directory of the executable on the current
 * host.
 * </p>
 * <p>
 * ${inputFile} - The input file for the job.
 * </p>
 * <p>
 * These flags can be inserted as part of the executable name and the
 * JobLauncher will automatically replace them for the appropriate host so long
 * as they are provided. By defeault the JobLauncher appends the name of the
 * input file to the end of the executable string. This option can be disabled
 * by calling setAppendInputFlag.
 * </p>
 * <p>
 * The setExecutable() operation should not be called with MPI, OpenMP or Intel
 * TBB directives as part of the executable. If it is, the JobLauncher will not
 * attempt consider the launch command as hard-wired and it will not try to
 * change the number of cores or threads. It will still perform any string
 * replacement that may be required for any included flags.
 * </p>
 * <p>
 * JobLaunchers expect that there will always be at least one input file and
 * always configures itself to require at least one file. Some situations may
 * require multiple types of input files. For example, a particular simulator
 * may require run time parameters, a 3d mesh and materials information in one
 * file and another simulator may expect three separate files for this
 * information. The two additional input file types can be added to the job
 * launcher by calling the addInputType() operation.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "JobLauncher")
public class JobLauncher extends Item {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the executable command that should be launched. This is
	 * different than the proper name of the executable. For example, the name
	 * of a popular Linux text editor is Vi Improved, but its executable command
	 * name is vim. In order to use JPA on this object, there is a string limit
	 * of 1000 characters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	private String executableCommandName;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The table of hosts on which the job can be launched. The table's columns
	 * are the hostname, the operating system and the installation directory of
	 * the executable on that platform. In order to use JPA on this object,
	 * there is a string limit of 1000 characters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "TableComponent")
	private TableComponent hostsTable;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the job should be launched with MPI, false otherwise and by
	 * default.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	private boolean mpiEnabled = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the job should be launched OpenMP, false otherwise and by
	 * default.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private boolean openMPEnabled = false;

	/**
	 * True if the launcher should append the name of the input file to the
	 * launch command, false otherwise.
	 */
	@XmlAttribute
	private boolean appendInput;

	/**
	 * True if the launcher should upload input file(s) to a remote machine,
	 * otherwise false. Setting this flag to false will still allow the
	 */
	@XmlAttribute
	private boolean uploadInput;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The set of hosts available for the job. In order to use JPA on this
	 * object, there is a string limit of 1000 characters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement
	private ArrayList<String> hosts;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of input files from which a client may select for the job.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement
	private ArrayList<String> inputFiles;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the job should be launched with Intel's Thread Building Blocks
	 * (TBB), false otherwise and by default.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private boolean tbbEnabled = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The directory on the remote machine from which files should be
	 * downloaded.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient()
	private String remoteDownloadDir;

	/**
	 * A map for storing key-value pairs needed by the actions.
	 */
	@XmlTransient()
	private Dictionary<String, String> actionDataMap;

	/**
	 * The set of resources stored in the project and their last modification
	 * time. It may be different than what is returned by
	 * IResource.getModificationTime(), which is exactly why we are tracking it.
	 */
	@XmlTransient()
	private HashMap<IResource, Long> projectMemberModMap;

	/**
	 * This is a utility class used to describe a type of file by the
	 * JobLauncher.
	 * 
	 * @author Jay Jay Billings
	 */
	private static class FileType {
		/**
		 * The name of the input file type. ("Input" or "Host configuration" or
		 * "Materials information" for example)
		 */
		public String name = "";
		/**
		 * The name that will be used to refer to this input file when it is
		 * referred to by a variable in, for example, the executable string that
		 * is used by the JobLauncher. This name must end with the word "file"
		 * and, by convention, it should not contain white space or special
		 * symbols. If it does not end with the word "file," the JobLauncher
		 * will attempt to add it. This parameter may not be null.
		 */
		public String varName = "";
		/**
		 * A short description of the input file.
		 */
		public String desc = "";
		/**
		 * The file type extension that should be used to filter files from the
		 * project space for this input. If this parameter is null, then it will
		 * ignored.
		 */
		public String typeExt = "";
	};

	/**
	 * A map for storing the names of input files and their file types
	 */
	@XmlTransient()
	protected HashMap<String, FileType> inputFileNameMap;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to set the executable name, description and
	 * command name for the job that should be launched. It may contain one of
	 * two flags that will be replaced by string replacement before launch:
	 * ${inputFile} and ${installDir}. The ${inputFile} flag will be replaced
	 * with the name of the input file that should be used during the launch and
	 * the ${installDir} flag will be replaced with the installation directory
	 * of the executable for the given host as configured with the addHost()
	 * operation. If the ${inputFile} flag is used, the JobLauncher will set
	 * inputFile=none in the dictionary used by the RemoteExecutorAction.
	 * </p>
	 * <p>
	 * Set executable should only be called once.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param execName
	 *            <p>
	 *            The proper and full name of the job that will be launched
	 *            (e.g. - Microsoft Word). The JobLauncher class appends
	 *            " Launcher" to this name.
	 *            </p>
	 * @param execDesc
	 *            <p>
	 *            The description of the executable.
	 *            </p>
	 * @param execCommand
	 *            <p>
	 *            The name of the executable command that should be launched.
	 *            This is different than the proper name of the executable. For
	 *            example, the name of a popular Linux text editor is Vi
	 *            Improved, but its executable command name is vim.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setExecutable(String execName, String execDesc,
			String execCommand) {
		// begin-user-code

		// Set the executable command name
		executableCommandName = execCommand;

		// Set the name of the Form
		String nameAppend = execName.contains("Launcher") ? "" : " Launcher";
		form.setName(execName + nameAppend);

		// Set the Form description
		form.setDescription(execDesc);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public JobLauncher(IProject projectSpace) {
		// begin-user-code

		// Call the Item constructor
		super(projectSpace);

		// Setup the resource list
		projectMemberModMap = new HashMap<IResource, Long>();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This is a private operation used by JobLauncher.process() to create the
	 * standard output and standard error output files in the project directory.
	 * It also adds the paths of these files to the map. It should only be
	 * called by JobLauncher.process() because it is solely a utility function
	 * for code cleanliness.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void createOutputFiles() {
		// begin-user-code

		// Local Declarations
		ResourceComponent outputData = (ResourceComponent) form
				.getComponent(JobLauncherForm.outputId);
		String stdOutHeader, stdErrHeader, stdOutFileName, stdErrFileName;

		// Setup the headers
		stdOutHeader = createOutputHeader("standard output");
		stdErrHeader = createOutputHeader("standard error");

		// Setup the file names
		stdOutFileName = form.getName().replaceAll("\\s+", "_") + "_stdout_"
				+ getId() + ".txt";
		stdErrFileName = form.getName().replaceAll("\\s+", "_") + "_stderr_"
				+ getId() + ".txt";

		// Create the stdout file in the project space
		if (project != null) {
			// Create the Eclipse Resources IFile handle for standard out
			IFile stdOutProjectFile = project.getFile(stdOutFileName);
			// Create the Eclipse Resources IFile handle for standard error
			IFile stdErrProjectFile = project.getFile(stdErrFileName);
			// Create the standard out project file
			try {
				// Delete the file if it already exists - we want a clean one
				if (stdOutProjectFile.exists()) {
					stdOutProjectFile.delete(true, null);
				}
				// Create the managed file - creation closes the input
				// stream
				stdOutProjectFile.create(
						new ByteArrayInputStream(stdOutHeader.getBytes()),
						false, null);
			} catch (CoreException e) {
				// Complain
				e.printStackTrace();
			}
			// Create the standard error project file
			try {
				// Delete the file if it already exists - we want a clean one
				if (stdErrProjectFile.exists()) {
					stdErrProjectFile.delete(true, null);
				}
				// Create the managed file - creation closes the input
				// stream
				stdErrProjectFile.create(
						new ByteArrayInputStream(stdErrHeader.getBytes()),
						false, null);
			} catch (CoreException e) {
				// Complain
				e.printStackTrace();
			}
			// Put the paths to the standard error and out files into the map.
			// Note that the toOSSString() operation returns the file path with
			// the proper file separators ("/" vs. "\") added.
			actionDataMap.put("stdOutFileName", stdOutProjectFile.getLocation()
					.toOSString());
			actionDataMap.put("stdErrFileName", stdErrProjectFile.getLocation()
					.toOSString());
			System.out.println("JobLauncher Message: File paths: "
					+ "\n\tStandard Out File = "
					+ actionDataMap.get("stdOutFileName")
					+ "\n\tStandard Error File = "
					+ actionDataMap.get("stdErrFileName"));
			// Add the output files to the resource component
			addOutputFile(1, stdOutProjectFile, "Standard Output", outputData);
			addOutputFile(2, stdErrProjectFile, "Standard Error Output",
					outputData);
			try {
				// Grab the current list of project members that are managed for
				// this item.
				IResource[] members = project.members();
				for (int i = 0; i < members.length; i++) {
					// Add the resource and its modification time stamp to the
					// list.
					projectMemberModMap.put(members[i],
							members[i].getModificationStamp());
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return;
		// end-user-code
	}

	/**
	 * This operation updates the contents of the resource component after a run
	 * so that it contains any files that were created during the processing
	 * action.
	 */
	protected void updateResourceComponent() {

		// Local Declarations
		int lastId;
		long lastTimeStamp;
		ResourceComponent resources = (ResourceComponent) form.getComponent(2);
		ArrayList<ICEResource> resourceList = resources.getResources();
		ArrayList<String> resourceNames = new ArrayList<String>();
		String fileName;

		try {
			// Refresh the project space
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			// Get the list of members
			IResource[] latestMembers = project.members();
			// Get the names of the current resources
			for (ICEResource namedResource : resourceList) {
				resourceNames.add(namedResource.getPath().toASCIIString());
			}
			// Get the last resource id. Kinda messy. Nothing doing though.
			lastId = resources.getResources()
					.get(resources.getResources().size() - 1).getId();
			// Find the members that are new
			for (int i = 0; i < latestMembers.length; i++) {
				IResource currentResource = latestMembers[i];
				if (!projectMemberModMap.keySet().contains(currentResource)) {
					System.out.println("JobLauncher Message: " + "Adding file "
							+ currentResource.getName() + " to list.");
					// Create a new resource for the member
					ICEResource resource = new ICEResource();
					resource.setName(currentResource.getName());
					resource.setId(lastId + i + 1);
					resource.setDescription(currentResource.getName()
							+ " from " + getName() + " " + getId());
					resource.setContents(new File(currentResource
							.getLocationURI()));
					// Add the resource
					resources.addResource(resource);
				} else {
					// If we already have the file, get it.
					lastTimeStamp = projectMemberModMap.get(currentResource);
					// Get its file full file name
					fileName = currentResource.getLocationURI().toASCIIString();
					// Check the time stamp to see if it was modified AND make
					// sure that the file is not already in the resource list.
					if (lastTimeStamp != currentResource.getModificationStamp()
							&& !resourceNames.contains(fileName)) {
						System.out.println("JobLauncher Message: "
								+ "Adding file " + currentResource.getName()
								+ " to list.");
						// Create a new resource for the member
						ICEResource resource = new ICEResource();
						resource.setName(currentResource.getName());
						resource.setId(lastId + i + 1);
						resource.setDescription(currentResource.getName()
								+ " from " + getName() + " " + getId());
						resource.setContents(new File(currentResource
								.getLocationURI()));
						// Add the resource
						resources.addResource(resource);
					}
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation fills the data dictionary for the JobLaunchAction based on
	 * the contents of the Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The status of the operation. It will only equal
	 *         FormStatus.ReadyToProcess or FormStatus.InfoError and should
	 *         cause the calling operation to fail if the latter is returned.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FormStatus fillActionDataMap() {
		// begin-user-code

		// Local Declarations
		String filename = null, hostname = null;
		String installDir = ".";
		IResource fileResource = null;
		String os = "linux", accountCode = "";
		DataComponent fileData = null, parallelData = null;
		Entry fileEntry = null, mpiEntry = null;
		int numProcs = 1, numTBBThreads = 1;

		// Assign the data components
		fileData = (DataComponent) form.getComponent(JobLauncherForm.filesId);
		parallelData = (DataComponent) form
				.getComponent(JobLauncherForm.parallelId);
		// Check the components and fail if they are null
		if (fileData == null) {
			return FormStatus.InfoError;
		}
		// Allocate the map
		actionDataMap = new Hashtable<String, String>();

		// Get the input file's name
		fileData = (DataComponent) form.getComponent(1);
		// Write the file name for debug info
		for (String entryName : inputFileNameMap.keySet()) {
			fileEntry = fileData.retrieveEntry(entryName);
			if (fileEntry != null) {
				// Get the IResource for the file
				fileResource = project.findMember(fileEntry.getValue());
				// Make sure the file exists
				if (fileResource == null || !fileResource.exists()) {
					System.out.println("JobLauncher Message: Base filename = "
							+ filename);
					System.out
							.println("JobLauncher Message: Allowed file names = "
									+ fileEntry.getAllowedValues());
					return FormStatus.InfoError;
				}
				// Get the full filename
				filename = fileResource.getLocation().toOSString();
				actionDataMap.put(inputFileNameMap.get(entryName).varName,
						filename);
			} else {
				System.out.println("File not found in Form: " + entryName);
			}
		}

		// Get the hostname and OS by identifying the host in the table. Start
		// by getting the selected row ids from the host table.
		ArrayList<Integer> selectedRowIds = hostsTable.getSelectedRows();
		// Default to the first row
		int selectedRowId = 1;
		// If there were rows selected, set the hostname from the first
		// *selected* row. Default to the first row.
		selectedRowId = 0; // FIXME! Row id should be > 0! This is a known
							// issue.
		if (selectedRowIds != null) {
			selectedRowId = selectedRowIds.get(0);
		}
		// Get the hostname and os
		ArrayList<Entry> hostEntries = hostsTable.getRow(selectedRowId);
		hostname = hostEntries.get(0).getValue();
		os = hostEntries.get(1).getValue();
		installDir = hostEntries.get(2).getValue();

		// Could put the queue type in the map here

		// Get the parallel launch parameters from the form
		if (parallelData != null) {
			// Figure out whether or not MPI should be used. Get the MPI Entry
			// first.
			mpiEntry = parallelData.retrieveEntry("Number of MPI Processes");
			// Get the number of cores if the Entry is there
			if (mpiEntry != null) {
				numProcs = Math.max(numProcs,
						Integer.parseInt(mpiEntry.getValue()));
			}
			// Figure out whether or not TBB threads should be used.
			Entry tbbThreadsEntry = parallelData
					.retrieveEntry("Number of TBB Threads");
			// Get the number of cores if the Entry is there
			if (tbbThreadsEntry != null) {
				numTBBThreads = Math.max(numTBBThreads,
						Integer.parseInt(tbbThreadsEntry.getValue()));
			}
			// Get the account code
			Entry accountEntry = parallelData
					.retrieveEntry("Account Code/Project Code");
			accountCode = accountEntry.getValue();
		}

		// Load the dictionary with the remaining parameters. Make sure to put
		// the set the noAppendInputFlag in there depending on whether or not
		// the execution string has been modified! (c.f.-
		// JobLaunchAction docs)
		actionDataMap.put("executable", executableCommandName);
		actionDataMap.put("uploadInput", String.valueOf(uploadInput));
		// Note: "no append" is reversed logic from "append". ICE also checks
		// the command name to determine it too.
		if (appendInput && !executableCommandName.contains("${inputFile}")) {
			actionDataMap.put("noAppendInput", "false");
		} else {
			actionDataMap.put("noAppendInput", "true");
		}
		actionDataMap.put("installDir", installDir);
		actionDataMap.put("hostname", hostname);
		actionDataMap.put("os", os);
		actionDataMap.put("accountCode", accountCode);
		// Add the number of processors to the action data dictionary. It will
		// always be at least 1.
		actionDataMap.put("numProcs", String.valueOf(numProcs));
		actionDataMap.put("numTBBThreads", String.valueOf(numTBBThreads));
		// Set the download directory if possible
		if (remoteDownloadDir != null) {
			actionDataMap.put("downloadDirectory", remoteDownloadDir);
		}
		// Dump the action map if debugging is enabled
		if (debuggingEnabled) {
			System.out.println("JobLauncher Message: " + "Action Data Map = "
					+ actionDataMap);
		}

		return FormStatus.ReadyToProcess;
		// end-user-code
	}

	/**
	 * This operation adds an output file to the output data resource. It is a
	 * utility function used primarily by createOutputFiles().
	 * 
	 * @param resourceId
	 *            The id of the ICEResource in the ResourceComponent
	 * @param file
	 *            The IFile that will store the data.
	 * @param resourceName
	 *            A short descriptive name of the resource. It will be appended
	 *            to the name of the Form.
	 * @param outputComp
	 *            The ResourceComponent that contains the data.
	 */
	private void addOutputFile(int resourceId, IFile file, String resourceName,
			ResourceComponent outputComp) {

		// Get the specified resource from the resource component
		ICEResource outputResource = null;
		for (ICEResource i : outputComp.getResources()) {
			if (i.getId() == resourceId) {
				outputResource = i;
				break;
			}
		}
		// Add the ICEResource for the file if it does not already exist
		if (outputResource == null) {
			// Create the resource
			try {
				outputResource = new ICEResource(new File(file.getLocation()
						.toString()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Resource location = "
					+ file.getFullPath().toString());
			// Set the properties
			outputResource.setName(form.getName() + " " + resourceName);
			outputResource.setId(resourceId);
			outputResource.setDescription(resourceName + " from the job.");
			// Add the ICEResource to the Output component
			outputComp.addResource(outputResource);
		}

		return;
	}

	/**
	 * This operation creates a standard header that contains information about
	 * the job being launched. It is used primarily by the createOutputFiles()
	 * operation.
	 * 
	 * @param logName
	 *            The name that should be used to identify the log in its
	 *            header.
	 * @return The header.
	 */
	private String createOutputHeader(String logName) {

		// Local Declarations
		String header = null;

		// Get the identity of this machine as the point of origin for the job
		// launch
		try {
			// Get the address of localhost
			InetAddress addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Identify the log
		header = "# Log of " + logName + " collected by ICE for " + getName()
				+ " " + getId() + "\n";

		return header;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the JobLauncherForm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		ArrayList<Entry> columnNames = new ArrayList<Entry>();
		Entry entry1 = new Entry();
		Entry entry2 = new Entry();
		Entry entry3 = new Entry();

		// Initialize the host and input lists
		hosts = new ArrayList<String>();
		inputFiles = new ArrayList<String>();
		inputFileNameMap = new HashMap<String, FileType>();

		// Set the Item Type
		itemType = ItemType.Simulation;

		// Setup TableComponent
		hostsTable = new TableComponent();
		hostsTable.setId(JobLauncherForm.parallelId + 1);
		hostsTable.setName("Hosts");
		hostsTable.setDescription("The available hosts for the job.");
		// Setup the Entries that define the row template for the table
		entry1.setName("Hostnames");
		entry2.setName("Operating Systems");
		entry3.setName("Execution Path");
		// Add the Entries to the the array list of column names
		columnNames.add(entry1);
		columnNames.add(entry2);
		columnNames.add(entry3);
		// Set the row template
		hostsTable.setRowTemplate(columnNames);

		// Setup the list of actions
		allowedActions = new ArrayList<String>();
		allowedActions.add("Launch the Job");

		// Create the JobLauncherForm
		form = new JobLauncherForm();
		form.setActionList(allowedActions);

		// Add the generic input file type
		String desc = "The input file that should be used in the launch.";
		addInputType("Input File", "inputFile", desc, null);
		appendInput = true;
		uploadInput = true;

		// Add the host table to the form
		form.addComponent(hostsTable);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation reviews the Entries in the JobLauncherForm to make sure
	 * that it can actually perform the launch.
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
	protected FormStatus reviewEntries(Form preparedForm) {
		// begin-user-code

		// Local Declarations
		FormStatus status = FormStatus.InfoError;

		// Check the Form and reset the name
		if (preparedForm != null) {
			// Make sure the Forms are the same
			if (form.getId() == preparedForm.getId()
					&& form.getItemID() == preparedForm.getItemID()) {
				// Check the hosts table
				// FIXME
				TableComponent updatedHostsTable = (TableComponent) form
						.getComponent(JobLauncherForm.parallelId + 1);
				if (updatedHostsTable.numberOfRows() > 0) {
					// Update the hosts table
					hostsTable = updatedHostsTable;
					// Mark the Form as ready, update the Form and set the
					// status.
					preparedForm.markReady(true);
					form = preparedForm;
					status = FormStatus.ReadyToProcess;
				} else if (debuggingEnabled) {
					System.err.println("JobLauncher Message: "
							+ "Found an empty hosts table "
							+ "during entry review!");
				}
			} else if (debuggingEnabled) {
				System.err.println("JobLauncher Message: "
						+ "Invalid form id for submitted form during review!");
			}
		}

		return status;
		// end-user-code
	}

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
	public JobLauncher() {
		// begin-user-code

		// Just call the other constructor with a null argument
		this(null);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation performs the job launch if the action name is equal to
	 * "Launch the Job"
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of the action to perform.
	 *            </p>
	 * @return <p>
	 *         The status.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus process(String actionName) {
		// begin-user-code

		// Local Declarations
		FormStatus localStatus = FormStatus.InfoError;

		// Only process the job if the Item is enabled
		if (isEnabled()) {

			// Check the action name and return and error if the action name is
			// not equal to "Launch the Job"
			if (!actionName.equals(allowedActions.get(0))) {
				return FormStatus.InfoError;
			}

			// Make sure the form is ready before processing, otherwise return
			// an error.
			if (form.isReady()) {
				// Fill the action data dictionary or fail if there is a
				// problem.
				if (!fillActionDataMap().equals(FormStatus.ReadyToProcess)) {
					return FormStatus.InfoError;
				}
				// Create the output files in the project space
				createOutputFiles();
				// Launch the action
				action = new JobLaunchAction();
				localStatus = action.execute(actionDataMap);
			} else {
				localStatus = FormStatus.InfoError;
			}
			// Start the thread that fills the output file to stream to clients.
			streamOutputData();

		}

		// Set the status
		status = localStatus;

		return status;
		// end-user-code
	}

	/**
	 * This operations grabs the information from the stdout and stderr files
	 * and puts it into the output file for JobLauncher that is consumed by
	 * clients.
	 */
	private void streamOutputData() {

		// Create the thread
		Thread streamingThread = new Thread(new Runnable() {
			public void run() {
				// Local Declarations
				File stdout = new File(actionDataMap.get("stdOutFileName")), stderr = new File(
						actionDataMap.get("stdErrFileName"));
				FileWriter outputFileWriter = null;
				BufferedWriter outputFileBufferedWriter = null;
				FileReader stdoutReader = null, stderrReader = null;
				BufferedReader stdoutBufferredReader = null, stderrBufferredReader = null;
				String line = null;

				try {
					// Open the output file for writing
					outputFileWriter = new FileWriter(outputFile);
					outputFileBufferedWriter = new BufferedWriter(
							outputFileWriter);
					// Open the JobLauncherAction stdout file for reading
					stdoutReader = new FileReader(stdout);
					stdoutBufferredReader = new BufferedReader(stdoutReader);
					// Open the JobLauncherAction stderr file
					stderrReader = new FileReader(stderr);
					stderrBufferredReader = new BufferedReader(stderrReader);
					// Run until the launcher is done processing
					while (status.equals(FormStatus.Processing)
							|| status.equals(FormStatus.NeedsInfo)) {
						// Read stdout into the output file
						while ((line = stdoutBufferredReader.readLine()) != null) {
							outputFileBufferedWriter.write(line);
							outputFileBufferedWriter.write("\r\n");
						}
						// Read stderr into the output file
						while ((line = stderrBufferredReader.readLine()) != null) {
							outputFileBufferedWriter.write(line);
							outputFileBufferedWriter.write("\r\n");
						}
						// Flush!
						outputFileBufferedWriter.flush();
						// Sleep for a bit
						Thread.currentThread();
						Thread.sleep(100);
					}
					// Close stdout
					stdoutBufferredReader.close();
					stdoutReader.close();
					// Close stderr
					stderrBufferredReader.close();
					stderrReader.close();
					// Close the output file
					outputFileBufferedWriter.close();
					outputFileWriter.close();
					// Check the project space to see if new resources were
					// downloaded that should be added to the ICEResource.
					updateResourceComponent();
				} catch (IOException e) {
					// Complain and return
					e.printStackTrace();
					return;
				} catch (InterruptedException e) {
					// Complain and return
					e.printStackTrace();
					return;
				}

			}
		});

		// Start the thread
		System.out.println("JobLauncher Message: Starting output data stream.");
		streamingThread.start();

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation adds a host to the set of available hosts on which the
	 * JobLauncher can launch the job. If a host is a duplicate, it is not
	 * added. If the hostname is the same, but the other attributes are
	 * different, this operation will overwrite an existing host.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param hostname
	 *            <p>
	 *            The hostname of the new host.
	 *            </p>
	 * @param os
	 *            <p>
	 *            The operating system of the new host.
	 *            </p>
	 * @param execInstallPath
	 *            <p>
	 *            The installation path of the executable on the host.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addHost(String hostname, String os, String execInstallPath) {
		// begin-user-code

		// Local Declarations
		ArrayList<Entry> row = new ArrayList<Entry>();
		boolean contractSatisfied = hostname != null && os != null
				&& execInstallPath != null && form != null
				&& !hosts.contains(hostname) && hostsTable != null;

		// Make sure parameters are not null. If null, return
		if (contractSatisfied) {
			// Add a new row
			int RowId = hostsTable.addRow();
			// Get the newly created row
			row = hostsTable.getRow(RowId);

			// Set the columns in the row
			row.get(0).setValue(hostname);
			row.get(1).setValue(os);
			row.get(2).setValue(execInstallPath);

			// Update the host table
			hosts.add(hostname);

			// Dump some debugging information
			if (debuggingEnabled) {
				System.out.println("JobLauncher Message: "
						+ "Successfully added host " + hostname);
			}

		} else if (debuggingEnabled) {
			System.err.println("JobLauncher Message: " + "Invalid host added!");
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation removes a host from the set of hosts on which the
	 * JobLauncher can launch a job. If the host is not in the set, it does
	 * nothing.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param hostname
	 *            <p>
	 *            The hostname to remove from the set of hosts.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void deleteHost(String hostname) {
		// begin-user-code

		if (hostname != null) {

			// Delete the hostname from the list of hosts
			hosts.remove(hostname);

			// Delete the host from the hostTable
			for (int i = 1; i < hostsTable.getRowIds().size(); i++) {
				if (hostsTable.getRow(i).get(0).equals(hostname)) {
					hostsTable.deleteRow(i);
				}
			}

		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hostnames of all the hosts available to this
	 * JobLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The list of hostnames or null if there are not hosts configured.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAllHosts() {
		// begin-user-code

		return hosts;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the JobLauncher to enable MPI support for this
	 * task. It expects "mpirun" to be in the users' PATH on the remote machine.
	 * A value of zero or less than zero passed to this operation will be ignore
	 * and set to 1. Furthermore, the maximum must be greater than the minimum
	 * and the default value must be equal to or in between the maximum and
	 * minimum. If these conditions are not met, it will not be enabled.
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

		// if form is not set, return
		if (form == null) {
			return;
		}
		// Perform Operation
		((JobLauncherForm) form).enableMPI(minProcesses, maxProcesses,
				defaultProcesses);

		// If the components are not greater than 2, then it is false
		if (form.getComponents().size() > 3) {
			DataComponent dataC = (DataComponent) form.getComponents().get(
					JobLauncherForm.parallelId);
			Entry entry = (Entry) dataC
					.retrieveEntry("Number of MPI Processes");
			if (entry != null) {
				this.mpiEnabled = true;
				return;
			}
		}
		this.mpiEnabled = false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables MPI support for the job.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disableMPI() {
		// begin-user-code

		// if form is not set, return
		if (form == null) {
			return;
		}
		// Turn off feature in form
		((JobLauncherForm) form).disableMPI();

		// Turn off feature
		this.mpiEnabled = false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation enables OpenMP support for the job so long as the
	 * executable was compiled with OpenMP support. A value of zero or less than
	 * zero passed to this operation will be ignore and set to 1. Furthermore,
	 * the maximum must be greater than the minimum and the default value must
	 * be equal to or in between the maximum and minimum. If these conditions
	 * are not met, it will not be enabled.
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

		// If form is not set, return
		if (form != null) {
			((JobLauncherForm) form).enableOpenMP(minThreads, maxThreads,
					defaultThreads);

			// If the components are not greater than 2, then it is false
			if (form.getComponents().size() > 3) {
				DataComponent dataC = (DataComponent) form
						.getComponent(JobLauncherForm.parallelId);
				Entry entry = (Entry) dataC
						.retrieveEntry("Number of OpenMP Threads");
				if (entry != null) {
					openMPEnabled = true;
					return;
				}
			}

			openMPEnabled = false;
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables OpenMP support for the job.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disableOpenMP() {
		// begin-user-code
		// if form is not set, return
		if (form == null) {
			return;
		}
		// disable in form
		((JobLauncherForm) form).disableOpenMP();

		// Turn off feature
		this.openMPEnabled = false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation enables Intel Thread Building Block (TBB) support for the
	 * job so long as the executable was compiled with Intel Thread Building
	 * Block (TBB) support. A value of zero or less than zero passed to this
	 * operation will be ignore and set to 1. Furthermore, the maximum must be
	 * greater than the minimum and the default value must be equal to or in
	 * between the maximum and minimum. If these conditions are not met, it will
	 * not be enabled.
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

		// If form is not se
		if (form != null) {
			// Set the default value
			tbbEnabled = false;

			// Enable the threading entry in the form
			((JobLauncherForm) form).enableTBB(minThreads, maxThreads,
					defaultThreads);

			// Check to see if it was enabled or not
			JobLauncherForm form = (JobLauncherForm) this.getForm();

			// If there are more than 3 components, then parallelism is enabled.
			if (form.getComponents().size() > 3) {
				DataComponent dataC = (DataComponent) form
						.getComponent(JobLauncherForm.parallelId);
				Entry entry = (Entry) dataC
						.retrieveEntry("Number of TBB Threads");
				if (entry != null) {
					tbbEnabled = true;
				}
			}

		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables Intel Thread Building Block (TBB) support for the
	 * job.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disableTBB() {
		// begin-user-code
		// if form is not set, return
		if (form == null) {
			return;
		}
		// disable in form
		((JobLauncherForm) form).disableTBB();

		// Turn off feature
		this.tbbEnabled = false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between the JobLauncher and
	 * another JobLauncher. It returns true if the Items are equal and false if
	 * they are not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherLauncher
	 *            <p>
	 *            The JobLauncher that should be checked for equality.
	 *            </p>
	 * @return <p>
	 *         True if the launchers are equal, false if not
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(JobLauncher otherLauncher) {
		// begin-user-code

		boolean retVal;
		// Check if they are the same reference in memory
		if (this == otherLauncher) {
			return true;
		}

		// Check that the object is not null, and that it is an Item
		// Check that these objects have the same ICEObject data
		if (otherLauncher == null || !(otherLauncher instanceof Item)
				|| !super.equals(otherLauncher)) {
			return false;
		}

		// Check data
		retVal = (this.allowedActions.equals(otherLauncher.allowedActions))
				&& (this.form.equals(otherLauncher.form))
				&& (this.itemType == otherLauncher.itemType)
				&& (this.status.equals(otherLauncher.status))
				&& (this.mpiEnabled == otherLauncher.mpiEnabled)
				&& (this.openMPEnabled == otherLauncher.openMPEnabled)
				&& (this.tbbEnabled == otherLauncher.tbbEnabled)
				&& (this.hostsTable.equals(otherLauncher.hostsTable))
				&& (this.hosts.equals(otherLauncher.hosts));

		// Check the remote download directory if it has been configure
		if (remoteDownloadDir != null) {
			retVal &= (this.remoteDownloadDir
					.equals(otherLauncher.remoteDownloadDir));
		}

		// Check project
		if (this.project != null && otherLauncher.project != null
				&& (!(this.project.equals(otherLauncher.project)))) {
			return false;

		}

		// Check project - set to null

		if (this.project == null && otherLauncher.project != null
				|| this.project != null && otherLauncher.project == null) {
			return false;
		}

		// Check executable command name
		if (this.executableCommandName != null
				&& otherLauncher.executableCommandName != null
				&& !(this.executableCommandName
						.equals(otherLauncher.executableCommandName))) {
			return false;

		}

		// Check dictionary
		if (this.actionDataMap != null && otherLauncher.actionDataMap != null
				&& (!(this.actionDataMap.equals(otherLauncher.actionDataMap)))) {
			return false;
		}

		return retVal;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hashcode value of the JobLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashcode
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declaration
		int hash = 9;
		int tempHash = 0;
		// Compute hash code from Item data
		hash = 31 * hash + super.hashCode();

		// Compute from jobLauncher
		if (this.executableCommandName != null) {
			hash = 31 * hash + this.executableCommandName.hashCode();
		}
		if (this.hostsTable != null) {
			hash = 31 * hash + this.hostsTable.hashCode();
		}
		// MPIEnabled
		if (this.mpiEnabled) {
			tempHash = 1;
		} else {
			tempHash = 0;
		}

		hash = 31 * hash + tempHash;

		// OpenMPEnabled
		if (this.openMPEnabled) {
			tempHash = 1;
		} else {
			tempHash = 0;
		}

		hash = 31 * hash + tempHash;

		// tBBEnabled
		if (this.tbbEnabled) {
			tempHash = 1;
		} else {
			tempHash = 0;
		}

		hash = 31 * hash + tempHash;

		if (actionDataMap != null) {
			hash = 31 * hash + actionDataMap.hashCode();
		}

		if (this.hosts != null) {
			hash = 31 * hash + hosts.hashCode();
		}

		// Remote download directory contributes too.
		if (remoteDownloadDir != null) {
			hash = 31 * hash + remoteDownloadDir.hashCode();
		}

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param otherLauncher
	 *            <p>
	 *            This operation performs a deep copy of the attributes of
	 *            another JobLauncher into the current JobLauncher.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(JobLauncher otherLauncher) {
		// begin-user-code

		// Return if otherLauncher is null
		if (otherLauncher == null) {
			return;
		}
		// Copy contents into super and current object
		super.copy((Item) otherLauncher);

		// Clone contents correctly
		JobLauncherForm launcherForm = new JobLauncherForm();
		launcherForm.copy(otherLauncher.form);
		form = launcherForm;

		this.executableCommandName = otherLauncher.executableCommandName;
		// Want to make sure its cloned and not a shallow copy.
		this.hostsTable = (TableComponent) otherLauncher.hostsTable.clone();
		this.mpiEnabled = otherLauncher.mpiEnabled;
		this.openMPEnabled = otherLauncher.openMPEnabled;
		this.tbbEnabled = otherLauncher.tbbEnabled;
		this.remoteDownloadDir = otherLauncher.remoteDownloadDir;
		this.actionDataMap = otherLauncher.actionDataMap;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation provides a deep copy of the JobLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A clone of the JobLauncher.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Create a new instance of JobLauncher and copy the contents
		JobLauncher clone = new JobLauncher();
		clone.copy(this);

		return clone;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the ICEObject.loadFromXML() operation to
	 * properly load the Entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            The InputStream from which the Item should be loaded.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code
		// Initialize JAXBManipulator
		jaxbManipulator = new ICEJAXBManipulator();

		// Call the read() on jaxbManipulator to create a new Object instance
		// from the inputStream
		Object dataObject;
		try {
			dataObject = jaxbManipulator.read(this.getClass(), inputStream);
			// Copy contents of new object into current data structure
			this.copy((JobLauncher) dataObject);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Nullerize jaxbManipilator
		jaxbManipulator = null;

		// Setup the Entry list and register dependencies

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation adds a new input file type to the JobLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name of the input file type. ("Input" or
	 *            "Host configuration" or "Materials information" for example)
	 *            </p>
	 * @param varName
	 *            <p>
	 *            The name that will be used to refer to this input file when it
	 *            is referred to by a variable in, for example, the executable
	 *            string that is used by the JobLauncher. This name must end
	 *            with the word "file" and, by convention, it should not contain
	 *            white space or special symbols. If it does not end with the
	 *            word "file," the JobLauncher will attempt to add it.
	 *            </p>
	 *            <p>
	 *            This parameter may not be null.
	 *            </p>
	 * @param description
	 *            <p>
	 *            A short description of the input file.
	 *            </p>
	 * @param fileExtension
	 *            <p>
	 *            The file type extension that should be used to filter files
	 *            from the project space for this input. If this parameter is
	 *            null, then it will ignored.
	 *            </p>
	 * @param isFileEntry
	 * 			  <p>
	 * 			  Flags whether or not the Entry created should be handled as
	 *            a file Entry. Flagging it as a file Entry will cause it to
	 *            render on the Form with a browse button.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addInputType(String name, String varName, String description,
			String fileExtension) {
		// begin-user-code

		// Don't send junk
		if (name != null && description != null && varName != null) {
			// Get the files with this type
			ArrayList<String> files = getProjectFileNames(fileExtension);
			// Forward the request to the JobLauncherForm
			((JobLauncherForm) form).setInputFiles(name, description, files);
			// Fix the variable name if needed
			if (!varName.toLowerCase().endsWith("file")) {
				varName = varName + "File";
			}
			// Create the FileType for this file
			FileType type = new FileType();
			type.name = name;
			type.varName = varName;
			type.desc = description;
			type.typeExt = fileExtension;
			// Update the input file map
			inputFileNameMap.put(name, type);
		}

		return;
		// end-user-code
	}
	
	/**
	 * This method is the same as {@link #addInputType}, except that is does 
	 * not take a boolean flag in the parameters. This method calls the other
	 * {@link #addInputType} with the boolean isFileEntry flag as false.
	 * 
	 * @param name
	 *            <p>
	 *            The name of the input file type. ("Input" or
	 *            "Host configuration" or "Materials information" for example)
	 *            </p>
	 * @param varName
	 *            <p>
	 *            The name that will be used to refer to this input file when it
	 *            is referred to by a variable in, for example, the executable
	 *            string that is used by the JobLauncher. This name must end
	 *            with the word "file" and, by convention, it should not contain
	 *            white space or special symbols. If it does not end with the
	 *            word "file," the JobLauncher will attempt to add it.
	 *            </p>
	 *            <p>
	 *            This parameter may not be null.
	 *            </p>
	 * @param description
	 *            <p>
	 *            A short description of the input file.
	 *            </p>
	 * @param fileExtension
	 *            <p>
	 *            The file type extension that should be used to filter files
	 *            from the project space for this input. If this parameter is
	 *            null, then it will ignored.
	 *            </p>
	 */
//	public void addInputType(String name, String varName, String description,
//			String fileExtension) {
//		
//		// Call the other method with the boolean flag as false
//		addInputType(name, varName, description, fileExtension, false);
//		
//		return;
//		
//	}
	
	/**
	 * This method removes an input file type from the Job Launcher. If the
	 * file type is not found from the list of already existing types, it will
	 * do nothing.
	 * 
	 * @param name	The name of the input file type to remove.
	 */
	public void removeInputType(String name) {
		
		if (name != null && inputFileNameMap.containsKey(name)) {
			
			// Remove it form the name map
			inputFileNameMap.remove(name);
			
			// Remove the entry from the DataComponent
			((JobLauncherForm) form).removeInputFiles(name);
		}
		
		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation will override the default download directory of the
	 * JobLauncher and set a new directory on the remote machine from which data
	 * should be downloaded. This is an advanced and protected operation that
	 * should be used with caution. It is only available via subclassing
	 * JobLauncher, not through the JobProfile Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dir
	 *            <p>
	 *            The new directory from which content should be downloaded.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setRemoteDownloadDirectory(String dir) {
		// begin-user-code

		// Set the directory
		remoteDownloadDir = dir;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the JobLauncher to reload the data it used from
	 * the project space. It reloads the lists of input files used by the
	 * launcher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reloadProjectData() {

		// begin-user-code

		// Local Declarations
		String name, desc;
		boolean isFile = false;

		// Get the DataComponent with the input files on it
		DataComponent dataComp = (DataComponent) form.getComponent(1);
		
		// Refresh the files for each type in the set
		for (FileType type : inputFileNameMap.values()) {
			name = type.name;
			desc = type.desc;
		
			// Re-set the input files
			ArrayList<String> files = getProjectFileNames(type.typeExt);
			((JobLauncherForm) form).setInputFiles(name, desc, files);
			
			
		}

		// Refresh the hostnames
		ArrayList<String> hostnames = getAllHosts();

		return;
		// end-user-code
	}

	/**
	 * This operation is to be used by subclasses to notify the JobLauncher if
	 * it should append the name of the input file to the end of the launch
	 * command. By default it does.
	 * 
	 * @param flag
	 *            True if the name should be appended, false otherwise.
	 */
	protected void setAppendInputFlag(boolean flag) {
		appendInput = flag;
	}

	/**
	 * This operation is to be used by subclasses to notify the JobLauncher if
	 * it should upload the input file. By default, input files are uploaded.
	 * 
	 * @param flag
	 *            True if the name should be appended, false otherwise.
	 */
	protected void setUploadInputFlag(boolean flag) {
		uploadInput = flag;
	}
	
	/**
	 * This method returns the working directory for the job launch. 
	 * 
	 * @return directory The directory where the job is launched from.
	 */
	protected String getWorkingDirectory() {
		return actionDataMap.get("workingDir");
	}

}
