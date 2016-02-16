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
package org.eclipse.ice.item.nuclear;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.iterator.BreadthFirstTreeCompositeIterator;
import org.eclipse.ice.datastructures.jaxbclassprovider.ICEJAXBClassProvider;
import org.eclipse.ice.datastructures.jaxbclassprovider.IJAXBClassProvider;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.action.Action;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.eclipse.ice.item.messaging.Message;
import org.eclipse.ice.item.utilities.moose.MOOSEFileHandler;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;

/**
 * The MOOSE Item represents a unification of the MOOSEModel and MOOSELauncher.
 * This Item essentially provides a Composite Item composed of the model and
 * launcher, and as such brokers the data necessary for user input during an
 * entire MOOSE workflow, ie input generation to launching and data
 * visualization.
 * 
 * @author Alex McCaskey
 *
 */
@XmlRootElement(name = "MOOSE")
public class MOOSE extends Item {

	/**
	 * Reference to the MOOSE Input Model for this MOOSE workflow.
	 */
	@XmlTransient()
	private MOOSEModel mooseModel;

	/**
	 * Reference to the MOOSELauncher used in executing a constructed MOOSE
	 * input file.
	 */
	@XmlTransient()
	private MOOSELauncher mooseLauncher;

	/**
	 * Reference to the Model's list of files.
	 */
	@XmlTransient()
	private DataComponent modelFiles;

	/**
	 * Reference to the DataComponent containing the Postprocessors to display
	 * automatically.
	 */
	@XmlTransient()
	private DataComponent postProcessorsData;

	/**
	 * Reference to the Model's input tree.
	 */
	@XmlTransient()
	private TreeComposite modelTree;

	/**
	 * Reference to the id of the DataComponent containign the Postprocessors
	 * the user would like to automatically display.
	 */
	@XmlTransient()
	public static final int ppDataId = 10;

	/**
	 * Boolean to indicate whether this Item has already registered with the
	 * necessary Tree blocks.
	 */
	@XmlTransient()
	private boolean registered = false;

	/**
	 * Nullary constructor.
	 */
	public MOOSE() {
		this(null);
		mooseModel = new MOOSEModel(null);
		mooseLauncher = new MOOSELauncher(null);
		modelTree = (TreeComposite) form.getComponent(2);
		postProcessorsData = (DataComponent) form.getComponent(MOOSE.ppDataId);
		mooseModel.getForm().addComponent(modelTree);
		modelFiles = (DataComponent) form.getComponent(1);
		status = FormStatus.ReadyToProcess;
		TreeComposite ppTree;
		if ((ppTree = getTopLevelTreeByName("Postprocessors")) != null) {
			setupPostprocessorData(ppTree);
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param projectSpace
	 */
	public MOOSE(IProject projectSpace) {
		super(projectSpace);
		mooseModel = new MOOSEModel(projectSpace);
		mooseLauncher = new MOOSELauncher(projectSpace);
		addComponents();
	}

	/**
	 * This private method add the necessary components to the Form.
	 */
	private void addComponents() {
		// Loop over all components and add them to this form
		for (Component c : mooseModel.getForm().getComponents()) {
			if (c.getName().equals("Mesh")) {
				c.setName("Mesh and Output Files");
			}
			form.addComponent(c);
		}

		// Grab an explicit reference to the files component from the Model
		modelFiles = (DataComponent) form.getComponent(1);

		// Add the parallel execution component
		form.addComponent(mooseLauncher.getForm().getComponent(3));

		// Get a handle to the model input tree
		modelTree = (TreeComposite) form.getComponent(2);

		// Create the Postprocessors DataComponent
		postProcessorsData = new DataComponent();
		postProcessorsData.setName("Show Postprocessors?");
		postProcessorsData.setDescription("Enable the Postprocessors you would like to monitor in real time. "
				+ "Plots will display at launch if the ICEUpdater Output block is available in your MOOSE installation.");
		postProcessorsData.setId(MOOSE.ppDataId);
		form.addComponent(postProcessorsData);

		TreeComposite ppTree;
		if ((ppTree = getTopLevelTreeByName("Postprocessors")) != null) {
			setupPostprocessorData(ppTree);
		}
	}

	/**
	 * Very simply, this method just creates a new Form that we will populate
	 * later in the constructor.
	 */
	@Override
	protected void setupForm() {
		form = new Form();
	}

	/**
	 * Sets the information that identifies the MOOSE Item.
	 */
	@Override
	protected void setupItemInfo() {

		// Local declarations
		String description = "The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed " + "by Idaho National Laboratory.";

		// Set the model defaults
		setName("MOOSE Workflow");
		setDescription(description);
		setItemBuilderName("MOOSE Workflow");

		// Setup the action list. Remove key-value pair support.
		allowedActions.remove(taggedExportActionString);
		allowedActions.remove(nativeExportActionString);
		allowedActions.add("Launch the Job");
		allowedActions.add("Write MOOSE File");
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#cancelProcess()
	 */
	@Override
	public FormStatus cancelProcess() {

		// Only cancel if the Item is actuallly processing
		if (status.equals(FormStatus.Processing)) {
			// Try to cancel the action
			mooseLauncher.cancelProcess();
			// Reset the state to "ready" since it was clearly able to process.
			status = FormStatus.ReadyToProcess;
		}

		return status;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#reviewEntries(org.eclipse.ice.datastructures.form.Form)
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {

		// Tell the model to review its entries
		FormStatus status = mooseModel.reviewEntries(preparedForm);

		// Register this Item as a listener to the Variables block -
		// this is so we can use the variables to populate things like
		// kernel variable entries.
		TreeComposite variablesTree = getTopLevelTreeByName("Variables");

		if (!registered && variablesTree != null) {
			variablesTree.register(this);
			modelTree.register(this);
			registered = true;
		}

		// Setup the Postprocessor data component
		TreeComposite ppTree;
		if ((ppTree = getTopLevelTreeByName("Postprocessors")) != null) {
			setupPostprocessorData(ppTree);
		}

		return status;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#process(java.lang.String)
	 */
	@Override
	public FormStatus process(String actionName) {
		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		String thisHost = "localhost", remoteHost = "";

		// Parse the action name
		if ("Launch the Job".equals(actionName)) {

			// First and foremost, Get the application URI
			// and see if it is a remote app or not
			URI appUri = URI.create(modelFiles.retrieveEntry("MOOSE-Based Application").getValue());
			boolean isRemote = "ssh".equals(appUri.getScheme());

			System.out.println("APPURI IS " + appUri + ", " + isRemote);
			// Validate the Tree, this will also make
			// sure all required files are in the workspace
//			if (!fullTreeValidation(appUri, isRemote)) {
//				logger.error("Moose Input Tree could not be validated. See error log for details.");
//				return FormStatus.InfoError;
//			}

			// Change the host name if we are remote
			if (isRemote) {
				IRemoteConnection remoteConnection = mooseLauncher.getRemoteConnection(appUri.getHost());
				remoteHost = remoteConnection.getService(IRemoteConnectionHostService.class).getHostname();

				// Get an ICEUpdater, this will return null if the
				// user does not want this feature
				try {
					thisHost = InetAddress.getLocalHost().getHostAddress();// .getCanonicalHostName();
				} catch (UnknownHostException e) {
					e.printStackTrace();
					logger.error(this.getClass().getName() + " Exception! ", e);
				}

			}

			// System.out.println("Using " + thisHost + " as the host name for
			// the ICEUpdater URL.");
			// If not remote, then this will just be localhost
			createICEUpdaterBlock(thisHost);

			// Populate the MOOSELaunchers files list, check for error.
			if (populateListOfLauncherFiles() != FormStatus.ReadyToProcess) {
				logger.error(getClass().getName() + " Error Populating list of files");
				return FormStatus.InfoError;
			}

			// Configure the execute string
			if (isRemote) {

				System.out.println("Setting the New HOST NAME");
				// Set the remote executable string
				mooseLauncher.setExecutable(Paths.get(appUri.getRawPath()).getFileName().toString(), "",
						appUri.getRawPath() + " -i ${inputFile} --no-color");

				// Setup the hosts table to use the remote host
				TableComponent hostsTable = (TableComponent) mooseLauncher.getForm()
						.getComponent(JobLauncherForm.parallelId + 1);
				int index = hostsTable.addRow();
				ArrayList<Entry> row = hostsTable.getRow(index);
				ArrayList<Integer> selected = new ArrayList<Integer>();
				selected.add(new Integer(index));
				row.get(0).setValue(remoteHost);
				hostsTable.setSelectedRows(selected);

			} else {

				// Setup the hosts table to use the local host
				TableComponent hostsTable = (TableComponent) mooseLauncher.getForm()
						.getComponent(JobLauncherForm.parallelId + 1);
				ArrayList<Integer> selected = new ArrayList<Integer>();
				selected.add(new Integer(0));
				hostsTable.setSelectedRows(selected);
				
				// Set the executable string
				mooseLauncher.setExecutable(new File(appUri).getName(), "",
						appUri.getPath() + " -i ${inputFile} --no-color");
			}

			// Register as a listener of the resource component
			// so we can add the resources to our resource component
			((ResourceComponent) mooseLauncher.getForm().getComponent(JobLauncherForm.outputId)).register(this);

			// Launch the Moose application
			retStatus = mooseLauncher.process(actionName);

			// Set our outputFile as the MooseLauncher's output file
			// so we can see the streaming output
			outputFile = mooseLauncher.getOutputFile();

		} else if ("Write MOOSE File".equals(actionName)) {
			// Simply pass this along to the Model
			retStatus = mooseModel.process(actionName);
		}

		// Set the status flag.
		status = retStatus;

		// Keep the status in sync
		if (status.equals(FormStatus.Processing)) {
			Thread statusThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!status.equals(FormStatus.Processed)) {
						// Sleep for a bit
						Thread.currentThread();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							logger.error(getClass().getName() + " Exception!", e);
						}

						// Set the status
						status = mooseLauncher.getStatus();
					}

					return;
				}
			});

			statusThread.start();
		}
		return retStatus;
	}

	/**
	 * This private utility method is invoked before all job launches to verify
	 * that the constructed MOOSE tree is valid.
	 * 
	 * @param uri
	 * @param isRemote
	 * @return
	 */
	private boolean fullTreeValidation(URI uri, boolean isRemote) {
		// Create and execute the CheckMooseInputAction!
		Action checkInput = getActionFactory().getAction("Check Moose Input");
		Dictionary<String, String> map = new Hashtable<String, String>();
		map.put("projectSpaceDir", project.getName());
		map.put("isRemote", String.valueOf(isRemote));
		map.put("localJobLaunchDirectory", "tempICELaunch");
		
		try {
			map.put("inputTree", writeComponentToXML(modelTree));
			map.put("appComp", writeComponentToXML(modelFiles));
		} catch (JAXBException e) {
			e.printStackTrace();
			logger.error("Error writing Tree and DataComponent to XML.", e);
		}
		
		// Upload files if remote
		if (isRemote) {
			DataComponent filesData = new DataComponent();
			filesData.addEntry(modelFiles.retrieveEntry("Output File Name"));
			for (Entry fileE : getFileEntries()) {
				filesData.addEntry(fileE);
			}
			
			map.put("hostname", uri.getHost());
			try {
				map.put("filesDataComponent", writeComponentToXML(filesData));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			// Upload all required files to the remote machine
			getActionFactory().getAction("Remote File Upload").execute(map);
		}
		
		return checkInput.execute(map) == FormStatus.ReadyToProcess ? true : false;
	}

	/**
	 * Write the provided Component to an XML String
	 * @param comp
	 * @return
	 * @throws JAXBException
	 */
	private <T> String writeComponentToXML(T comp) throws JAXBException {
		// Get the XML
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// Create the marshaller and write the item
		Marshaller marshaller;
		
		// Make an array to store the class list of registered Items
		ArrayList<Class> classList = new ArrayList<Class>();
		Class[] classArray = {};
		classList.addAll(new ICEJAXBClassProvider().getClasses());
		// Create new JAXB class context and unmarshaller
		JAXBContext context = JAXBContext.newInstance(classList.toArray(classArray));

		try {
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(comp, outputStream);
		} catch (JAXBException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
		}

		return new String(outputStream.toByteArray());
	}

	/**
	 * This method is used in the job launch mechanism to collect all required
	 * files for the MooseLauncher
	 * 
	 * @return
	 */
	private FormStatus populateListOfLauncherFiles() {
		// Get a reference to the Launchers files component
		DataComponent launcherFiles = (DataComponent) mooseLauncher.getForm().getComponent(1);

		// Grab the file name the user has specified for the input file
		String fileName = modelFiles.retrieveEntry("Output File Name").getValue();

		// Write the Moose file if it doesn't exist
		FormStatus retStatus = mooseModel.process("Write MOOSE File");
		if (!retStatus.equals(FormStatus.Processed)) {
			return retStatus;
		}

		// Set the value of the input file to the user-specified
		// file name
		launcherFiles.retrieveEntry("Input File").setValue(fileName);

		// Update the MooseLauncher's set of input files...
		mooseLauncher.update(launcherFiles.retrieveEntry("Input File"));
		for (Entry e : getFileEntries()) {
			Entry launcherFile = launcherFiles.retrieveEntry(e.getName());
			if (launcherFile != null) {
				launcherFile.setValue(e.getValue());
			}
		}

		return FormStatus.ReadyToProcess;
	}

	/**
	 * This private method is used to create a new ICEUpdater block for the
	 * launch. It checks to see if one can, or even should be created, and then
	 * creates it, configures it, and adds it to the tree.
	 * 
	 * @param isRemote
	 * @return
	 */
	private void createICEUpdaterBlock(String host) {

		// Add the ICEUpdater tree block to Outputs
		TreeComposite outputs = getTopLevelTreeByName("Outputs");
		TreeComposite postProcessors = getTopLevelTreeByName("Postprocessors");
		TreeComposite iceUpdater = null;
		boolean display = false, iNeedUpdater = true, updaterExists = false;

		// Check that we have a moose install that even has the ICEUpdater...
		for (TreeComposite child : outputs.getChildExemplars()) {
			if ("ICEUpdater".equals(child.getName())) {
				updaterExists = true;
				break;
			}
		}

		// Do nothing if we don't have postprocessors
		if (postProcessors == null || !postProcessors.isActive() || postProcessors.getNumberOfChildren() < 1
				|| !updaterExists) {
			return;
		}

		// If we do, see if the user checked any to be displayed
		DataComponent displayPPs = (DataComponent) form.getComponent(MOOSE.ppDataId);
		for (Entry e : displayPPs.retrieveAllEntries()) {
			if (e.getValue().equals("yes")) {
				display = true;
				break;
			}
		}

		// If we want postprocessors displayed then add the ICEUpdater
		if (display) {

			// If one already exists in the tree, then we shouldn't add another
			// one
			for (int i = 0; i < outputs.getNumberOfChildren(); i++) {
				if ("ICEUpdater".equals(outputs.getChildAtIndex(i).getName())) {

					// Check that the current one is configured correctly
					iceUpdater = outputs.getChildAtIndex(i);
					DataComponent data = (DataComponent) iceUpdater.getDataNodes().get(0);
					Entry itemIdEntry = data.retrieveEntry("item_id");
					if (Integer.valueOf(itemIdEntry.getValue()) != getId()) {
						itemIdEntry.setValue(String.valueOf(getId()));
					}
					data.retrieveEntry("url").setValue("http://" + host + ":"
							+ System.getProperty("org.eclipse.equinox.http.jetty.http.port") + "/ice/update");
					// Now we have a valid ICEUpdater, so we don't need
					// to create a new one.
					iNeedUpdater = false;
					break;
				}
			}

			// If we didnt find one, the iNeedUpdater should be true
			if (iNeedUpdater) {
				for (int i = 0; i < outputs.getChildExemplars().size(); i++) {
					if ("ICEUpdater".equals(outputs.getChildExemplars().get(i).getName())) {

						// Create a new one
						iceUpdater = (TreeComposite) outputs.getChildExemplars().get(i).clone();
						outputs.setNextChild(iceUpdater);

						// Set the pertinent data
						DataComponent data = (DataComponent) iceUpdater.getDataNodes().get(0);
						data.retrieveEntry("item_id").setValue(String.valueOf(getId()));
						data.retrieveEntry("url").setValue("http://" + host + ":"
								+ System.getProperty("org.eclipse.equinox.http.jetty.http.port") + "/ice/update");
						iceUpdater.setActive(true);
						iceUpdater.setActiveDataNode(data);
						break;
					}
				}
			}
		} else {
			// if there was already an ICEUpdater block, remove it
			// If we already have one, then we shouldn't add another one
			for (int i = 0; i < outputs.getNumberOfChildren(); i++) {
				if ("ICEUpdater".equals(outputs.getChildAtIndex(i).getName())) {
					// Remove the existing
					outputs.removeChild(outputs.getChildAtIndex(i));
					break;
				}
			}
		}

		// Return it, whether null or not, clients should check
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#loadInput(java.lang.String)
	 */
	@Override
	public void loadInput(String input) {

		// Load the model input
		mooseModel.loadInput(input);

		// Create a new Form
		form = new Form();

		// Set its ICEObject data
		String description = "The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed " + "by Idaho National Laboratory.";
		form.setName("MOOSE Workflow");
		form.setDescription(description);
		form.setItemID(getId());
		form.setActionList(allowedActions);

		// Loop over all components and add them to this form
		for (Component c : mooseModel.getForm().getComponents()) {
			form.addComponent(c);
		}

		// Add the model files component
		modelFiles = (DataComponent) form.getComponent(1);

		// Loop over all components and add the parallel exec and output
		// components only
		for (Component c : mooseLauncher.getForm().getComponents()) {

			if ("Parallel Execution".equals(c.getName()) || "Output Files and Data".equals(c.getName())) {
				form.addComponent(c);
			}

		}

		// Set up the postProcessorData DataComponent to contain
		// a list of Boolean Discrete Entries for each Postprocessor
		postProcessorsData = new DataComponent();
		postProcessorsData.setName("Show Postprocessors?");
		postProcessorsData.setDescription("Enable the Postprocessors you would like to monitor in real time.");
		postProcessorsData.setId(MOOSE.ppDataId);
		form.addComponent(postProcessorsData);

		TreeComposite ppTree;
		if ((ppTree = getTopLevelTreeByName("Postprocessors")) != null) {
			setupPostprocessorData(ppTree);
		}

		// Get a handle to the model input tree
		modelTree = (TreeComposite) form.getComponent(2);

		// Register this Item as a listener to the Variables block
		// this is so we can use the variables to populate things like
		// kernel variable entries.
		TreeComposite vars = getTopLevelTreeByName("Variables");
		if (vars != null) {
			vars.register(this);
		}
		TreeComposite aux = getTopLevelTreeByName("AuxVariables");
		if (aux != null) {
			aux.register(this);
		}
		modelTree.register(this);
		registered = true;

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.Item#update(org.eclipse.ice.datastructures.ICEObject
	 * .IUpdateable)
	 */
	@Override
	public void update(IUpdateable updateable) {

		if (updateable instanceof ResourceComponent) {
			ResourceComponent comp = (ResourceComponent) updateable;
			ResourceComponent ourComp = (ResourceComponent) form.getComponent(3);

			ArrayList<String> names = new ArrayList<String>();

			// Get a list of all our Resource Names:
			for (ICEResource r : ourComp.getResources()) {
				names.add(r.getName());
			}

			for (ICEResource r : comp.getResources()) {
				if (!names.contains(r.getName())) {
					logger.info("Adding Resource to Moose: " + r.getName());
					ourComp.add(r);
				}
			}

		} else if (updateable instanceof TreeComposite) {
			// If this is a tree composite we should reset our variables
			Thread varThread = new Thread(new Runnable() {

				@Override
				public void run() {
					// Sleep this thread for a second
					// to avoid any concurrent modifications
					// of the tree
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					new MOOSEFileHandler().setupVariables(modelTree);
					new MOOSEFileHandler().setupAuxVariables(modelTree);
				}

			});
			varThread.start();

		}

	}

	/**
	 * This method finds the child TreeComposite of modelTree that has the given
	 * String name.
	 * 
	 * @param name
	 * @return
	 */
	private TreeComposite getTopLevelTreeByName(String name) {

		for (int i = 0; i < modelTree.getNumberOfChildren(); i++) {
			TreeComposite child = modelTree.getChildAtIndex(i);
			if (child.getName().equals(name)) {
				return child;
			}
		}

		return null;
	}

	/**
	 * This method searches the Model input tree and locates all file Entries
	 * and loads them on the Model File DataComponent.
	 */
	private ArrayList<Entry> getFileEntries() {
		// protected void loadFileEntries() {
		// Walk the tree and get all Entries that may represent a file
		ArrayList<Entry> files = new ArrayList<Entry>();
		BreadthFirstTreeCompositeIterator iter = new BreadthFirstTreeCompositeIterator(modelTree);
		while (iter.hasNext()) {
			TreeComposite child = iter.next();

			// Make sure we have a valid DataComponent
			if (child.getActiveDataNode() != null && child.isActive()) {
				DataComponent data = (DataComponent) child.getActiveDataNode();
				for (Entry e : data.retrieveAllEntries()) {

					// If the Entry's tag is "false" it is a commented out
					// parameter.
					if (!"false".equals(e.getTag()) && e.getValue() != null && !e.getValue().isEmpty()
							&& e.getValueType() == AllowedValueType.File) {

						Entry clonedEntry = (Entry) e.clone();
						files.add(clonedEntry);
					}
				}
			}
		}

		return files;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.Item#update(org.eclipse.ice.item.messaging.Message)
	 */
	@Override
	public boolean update(Message message) {

		// Get the message type and string text
		String type = message.getType();
		String text = message.getMessage();

		// Parse the message type
		if ("MESSAGE_POSTED".equals(type)) {

			// If its a message posted, we expect it to
			// be of the format pp_name:time:value
			String[] data = text.split(":");
			String name = data[0];
			Double time = Double.valueOf(data[1]);
			Double value = Double.valueOf(data[2]);

			// We need the jobLaunch directory to create new VizResources
			IFolder directory = mooseLauncher.getJobLaunchFolder();
			if (directory == null || !directory.exists()) {
				logger.info("MOOSE Job Launch directory was null or did not exist. Cannot show real-time plots.");
				return false;
			}

			// Refresh the project space
			refreshProjectSpace();

			// Grab the new Postprocessor CSV file
			IFile dataFile = directory.getFile(name + ".csv");

			// Get a reference to the ResourceComponent
			ResourceComponent comp = (ResourceComponent) form.getComponent(3);

			try {

				if (!dataFile.exists()) {
					// If the file hasn't been created yet, we need to create
					// it and start filling it with post processor data
					String initialData = "Time," + name + "\n" + time + "," + value + "\n";
					dataFile.create(new ByteArrayInputStream(initialData.getBytes()), true, null);// .createNewFile();

					// Create the VizResource, and add it to the
					// ResourceComponent
					ICEResource resource = getResource(dataFile.getLocation().toOSString());
					comp.add(resource);

				} else {

					// Write the data to the existing resource
					dataFile.appendContents(new ByteArrayInputStream(new String(time + "," + value + "\n").getBytes()),
							IResource.FORCE, null);
				}

			} catch (IOException | CoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

		return true;
	}

	/**
	 * This private method takes the Postprocessor tree node and populates the
	 * postProcessorData DataComponent.
	 * 
	 * @param ppTree
	 */
	private void setupPostprocessorData(TreeComposite ppTree) {
		// postProcessorsData.clearEntries();
		for (int i = 0; i < ppTree.getNumberOfChildren(); i++) {
			if (!postProcessorsData.contains(ppTree.getChildAtIndex(i).getName())) {
				Entry ppEntry = new Entry() {
					@Override
					public void setup() {
						allowedValueType = AllowedValueType.Discrete;
						allowedValues.add("yes");
						allowedValues.add("no");
						defaultValue = "no";
					}
				};
				ppEntry.setName(ppTree.getChildAtIndex(i).getName());
				ppEntry.setDescription("Select whether this Postprocessor should be displayed in real-time.");
				ppEntry.setId(i);
				postProcessorsData.addEntry(ppEntry);
			}
		}
	}

	/**
	 * This operation is used to check equality between the MOOSE Item and
	 * another MOOSE Item. It returns true if the Items are equal and false if
	 * they are not.
	 * 
	 * @param otherMoose
	 *            The MOOSE Item that should be checked for equality.
	 * @return True if the launchers are equal, false if not
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
		if (other == null || !(other instanceof MOOSE) || !super.equals(other)) {
			return false;
		}

		// Check data
		MOOSE otherMoose = (MOOSE) other;
		retVal = (this.allowedActions.equals(otherMoose.allowedActions)) && (this.form.equals(otherMoose.form))
				&& (this.itemType == otherMoose.itemType) && (this.status.equals(otherMoose.status));

		// Check project
		if (this.project != null && otherMoose.project != null && (!(this.project.equals(otherMoose.project)))) {
			return false;

		}

		// Check project - set to null

		if (this.project == null && otherMoose.project != null || this.project != null && otherMoose.project == null) {
			return false;
		}

		if (!mooseModel.equals(otherMoose.mooseModel) && !mooseLauncher.equals(otherMoose.mooseLauncher)) {
			return false;
		}

		return retVal;
	}

	/**
	 * This operation returns the hashcode value of the MooseItem.
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
		// Compute hash code from Item data
		hash = 31 * hash + super.hashCode();
		hash = 31 * hash + mooseModel.hashCode();
		hash = 31 * hash + mooseLauncher.hashCode();

		return hash;
	}

	/**
	 * Copy the provided Item into this Item.
	 * 
	 * @param otherMoose
	 *            <p>
	 *            This operation performs a deep copy of the attributes of
	 *            another MOOSE Item into the current MOOSE Item.
	 *            </p>
	 */
	@Override
	public void copy(Item otherItem) {

		// Return if otherMoose is null
		if (otherItem == null) {
			return;
		}

		// Cast to a MOOSE Item
		MOOSE otherMoose = (MOOSE) otherItem;

		// Copy contents into super and current object
		super.copy(otherMoose);

		// Add the model files component
		modelFiles = (DataComponent) form.getComponent(1);

		// Get a handle to the model input tree
		modelTree = (TreeComposite) form.getComponent(2);

		// Must do this or we can't walk the tree to
		// get file entries correctly
		mooseModel.setActiveDataNodes(modelTree);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.Item#setProject(org.eclipse.core.resources.IProject)
	 */
	@Override
	public void setProject(IProject projectSpace) {
		super.setProject(projectSpace);
		mooseModel.setProject(projectSpace);
		mooseLauncher.setProject(projectSpace);
	}

	/**
	 * <p>
	 * This operation provides a deep copy of the MOOSE Item.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         A clone of the Moose Item.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Create a new instance of JobLauncher and copy the contents
		MOOSE clone = new MOOSE();
		clone.copy(this);

		return clone;
	}

	/**
	 * Overriding the default behavior here because the overall process output
	 * should be in the to-be-created local job folder.
	 * 
	 */
	@Override
	protected void setupOutputFile() {
		return;
	}

}
