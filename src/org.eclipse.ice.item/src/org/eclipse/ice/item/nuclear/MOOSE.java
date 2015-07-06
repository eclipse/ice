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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.iterator.BreadthFirstTreeCompositeIterator;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.eclipse.ice.item.messaging.Message;
import org.eclipse.ice.item.utilities.moose.MOOSEFileHandler;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

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
	 * Reference to teh mapping between created Postprocessor VizResources and
	 * their names.
	 */
	@XmlTransient()
	private HashMap<String, ICEResource> postProcessorResources;

	/**
	 * This map keeps track of File Entries in the modelFiles DataComponent and
	 * their corresponding parent TreeComposites so that we can keep them in
	 * sync.
	 */
	@XmlTransient()
	private HashMap<String, TreeComposite> fileEntryTreeMapping;

	/**
	 * Reference to the id of the DataComponent containign the Postprocessors
	 * the user would like to automatically display.
	 */
	@XmlTransient()
	public static final int ppDataId = 10;

	/**
	 * 
	 */
	@XmlTransient
	private IRemoteConnection remoteConnection;

	/**
	 * 
	 */
	@XmlTransient
	private IRemoteServicesManager remoteManager;

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
		addComponents();
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
	 * 
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

		// Register this Item as a listener to the MOOSE-Based Application
		// Entry so that if it changes to Remote, we can grab the
		// IRemoteConnection
		modelFiles.retrieveEntry("MOOSE-Based Application").register(this);

		// Add the parallel execution component
		form.addComponent(mooseLauncher.getForm().getComponent(3));

		// Get a handle to the model input tree
		modelTree = (TreeComposite) form.getComponent(2);

		fileEntryTreeMapping = new HashMap<String, TreeComposite>();

		// Initialize the postProcessor Mapping
		postProcessorResources = new HashMap<String, ICEResource>();

		// Create the Postprocessors DataComponent
		postProcessorsData = new DataComponent();
		postProcessorsData.setName("Show Postprocessors?");
		postProcessorsData.setDescription("Enable the Postprocessors you would like to monitor in real time.");
		postProcessorsData.setId(MOOSE.ppDataId);
		form.addComponent(postProcessorsData);

		TreeComposite ppTree;
		if ((ppTree = getTreeByName("Postprocessors")) != null) {
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

		// If the Model finished correctly, clear the old
		// file entries and load new ones.
		if (status.equals(FormStatus.ReadyToProcess)) {
			clearModelFiles();
			loadFileEntries();
		}

		// Register this Item as a listener to the Variables block
		// this is so we can use the variables to populate things like
		// kernel variable entries.
		TreeComposite variablesTree = getTreeByName("Variables");

		if (!registered && variablesTree != null) {
			variablesTree.register(this);
			modelTree.register(this);
			registered = true;
		}

		TreeComposite ppTree;
		if ((ppTree = getTreeByName("Postprocessors")) != null) {
			setupPostprocessorData(ppTree);
		}

		return status;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#process(java.lang.String)
	 */
	public FormStatus process(String actionName) {
		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;

		// Set our outputFile as the MooseLauncher's output file
		// so we can see the streaming output
		outputFile = mooseLauncher.getOutputFile();

		// Parse the action name
		if ("Launch the Job".equals(actionName)) {

			// FIXME WE NEED TO DO A CHECK FOR ALL THE REQUIRED BLOCKS!!!!

			// Add the ICEUpdater tree block to Outputs
			TreeComposite outputs = getTreeByName("Outputs");
			TreeComposite postProcessors = getTreeByName("Postprocessors");

			// First check to see if we have any post processors to
			// watch for.
			if (postProcessors.isActive() && postProcessors.getNumberOfChildren() > 0) {

				// If we do, we should add an ICEUpdater
				boolean iNeedUpdater = true;

				// If we already have one, then we shouldn't add another one
				for (int i = 0; i < outputs.getNumberOfChildren(); i++) {
					if ("ICEUpdater".equals(outputs.getChildAtIndex(i).getName())) {

						// But if the current one is not configured correctly
						// then we should add a new one, Here we make sure the
						// Item Id is correct...
						TreeComposite iceUpdater = outputs.getChildAtIndex(i);
						DataComponent data = (DataComponent) iceUpdater.getDataNodes().get(0);
						Entry itemIdEntry = data.retrieveEntry("item_id");
						if (Integer.valueOf(itemIdEntry.getValue()) != getId()) {
							itemIdEntry.setValue(String.valueOf(getId()));
						}

						// Now we have a valid ICEUpdater, so we don't need
						// to create a new one.
						iNeedUpdater = false;
						break;
					}
				}

				if (iNeedUpdater) {
					for (int i = 0; i < outputs.getChildExemplars().size(); i++) {
						if ("ICEUpdater".equals(outputs.getChildExemplars().get(i).getName())) {
							TreeComposite updater = (TreeComposite) outputs.getChildExemplars().get(i).clone();
							outputs.setNextChild(updater);

							DataComponent data = (DataComponent) updater.getDataNodes().get(0);
							data.retrieveEntry("item_id").setValue(String.valueOf(getId()));
							data.retrieveEntry("url").setValue("http://localhost:"
									+ System.getProperty("org.eclipse.equinox.http.jetty.http.port") + "/ice/update");
							updater.setActive(true);
							updater.setActiveDataNode(data);
							break;
						}
					}
				}
			}

			// Get a reference to the Launchers files component
			DataComponent launcherFiles = (DataComponent) mooseLauncher.getForm().getComponent(1);

			// Grab the file name the user has specified for the input file
			String fileName = modelFiles.retrieveEntry("Output File Name").getValue();

			// Write the Moose file if it doesn't exist
			retStatus = mooseModel.process("Write MOOSE File");
			if (!retStatus.equals(FormStatus.Processed)) {
				return retStatus;
			}

			// Set the value of the input file to the user-specified
			// file name
			launcherFiles.retrieveEntry("Input File").setValue(fileName);

			// Update the MooseLauncher's set of input files...
			mooseLauncher.update(launcherFiles.retrieveEntry("Input File"));
			for (Entry e : modelFiles.retrieveAllEntries()) {
				Entry launcherFile = launcherFiles.retrieveEntry(e.getName());
				if (launcherFile != null) {
					launcherFile.setValue(e.getValue());
				}
			}

			// Get the application URI
			URI appUri = URI.create(modelFiles.retrieveEntry("MOOSE-Based Application").getValue());

			// Check if we're local or remote
			if ("ssh".equals(appUri.getScheme())) {

				mooseLauncher.setExecutable(Paths.get(appUri.getRawPath()).getFileName().toString(), "",
						appUri.getRawPath() + " -i ${inputFile} --no-color");

				// Setup the hosts table to use the remote host
				TableComponent hostsTable = (TableComponent) mooseLauncher.getForm()
						.getComponent(JobLauncherForm.parallelId + 1);
				IRemoteConnection remoteConnection = mooseLauncher.getRemoteConnection(appUri.getHost());
				String hostname = remoteConnection.getService(IRemoteConnectionHostService.class).getHostname();
				int index = hostsTable.addRow();
				ArrayList<Entry> row = hostsTable.getRow(index);
				ArrayList<Integer> selected = new ArrayList<Integer>();
				selected.add(new Integer(index));
				row.get(0).setValue(hostname);
				hostsTable.setSelectedRows(selected);

			} else {

				// Set the executable string
				mooseLauncher.setExecutable(new File(appUri).getName(), "",
						appUri.getPath() + " -i ${inputFile} --no-color");
			}

			// Register as a listener of the resource component
			// so we can add the resources to our resource component
			((ResourceComponent) mooseLauncher.getForm().getComponent(JobLauncherForm.outputId)).register(this);

			// Launch the Moose application
			retStatus = mooseLauncher.process(actionName);

		} else if ("Write MOOSE File".equals(actionName)) {
			// Simply pass this along to the Model
			retStatus = mooseModel.process(actionName);
		}

		// Set the status flag.
		status = retStatus;

		// Keep the status in sync
		if (status.equals(FormStatus.Processing)) {
			Thread statusThread = new Thread(new Runnable() {
				public void run() {
					while (!status.equals(FormStatus.Processed)) {
						// Sleep for a bit
						Thread.currentThread();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
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
	 * This method just clears the Model Files DataComponent of its Entries so
	 * that we can populate it with new Entries.
	 */
	private void clearModelFiles() {

		ArrayList<String> toBeRemoved = new ArrayList<String>();
		for (Entry e : modelFiles.retrieveAllEntries()) {
			String name = e.getName();
			if (!"MOOSE-Based Application".equals(name) && !"Output File Name".equals(name)) {
				toBeRemoved.add(name);
			}
		}

		for (String e : toBeRemoved) {
			modelFiles.deleteEntry(e);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#loadInput(java.lang.String)
	 */
	@Override
	public void loadInput(String input) {
		mooseModel.loadInput(input);

		form = new Form();

		String description = "The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed " + "by Idaho National Laboratory.";

		// Set the model defaults
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
		if ((ppTree = getTreeByName("Postprocessors")) != null) {
			setupPostprocessorData(ppTree);
		}

		// Get a handle to the model input tree
		modelTree = (TreeComposite) form.getComponent(2);

		loadFileEntries();

		// Register this Item as a listener to the Variables block
		// this is so we can use the variables to populate things like
		// kernel variable entries.
		getTreeByName("Variables").register(this);
		TreeComposite aux = getTreeByName("AuxVariables");
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
					System.out.println("Adding Resource to Moose: " + r.getName());
					ourComp.add(r);
				}
			}

		} else if (updateable instanceof TreeComposite) {
			// If this is a tree composite we should reset our variables
			Thread varThread = new Thread(new Runnable() {

				@Override
				public void run() {
					new MOOSEFileHandler().setupVariables(modelTree);
					new MOOSEFileHandler().setupAuxVariables(modelTree);
				}

			});
			varThread.start();

		} else if (updateable instanceof Entry) {

			Entry entry = (Entry) updateable;

			// If we get here, then we have a file Entry that
			// has been changed on the modelFiles component
			// and we need to sync up the tree with it.

			// Grab the DataComponent
			if (fileEntryTreeMapping.containsKey(entry.getName())) {
				DataComponent data = (DataComponent) fileEntryTreeMapping.get(entry.getName()).getDataNodes().get(0);

				// If not null, loop over the Entries til we find
				// the file Entry.
				if (data != null) {
					for (Entry e : data.retrieveAllEntries()) {

						// If the Entry's tag is "false" it is a commented
						// out
						// parameter.
						if (!"false".equals(e.getTag()) && e.getValue() != null && !e.getValue().isEmpty()
								&& (e.getName() + " = " + e.getValue())
										.matches(mooseLauncher.getFileDependenciesSearchString())) {

							// Set the value of the tree's file entry.
							e.setValue(entry.getValue());
							break;
						}
					}
				}
			}
		}

	}

	/**
	 * This method finds the child TreeComposite of modelTree that has the given
	 * String name.
	 * 
	 * @param name
	 * @return
	 */
	private TreeComposite getTreeByName(String name) {

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
	protected void loadFileEntries() {
		// Walk the tree and get all Entries that may represent a file
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
							&& (e.getName() + " = " + e.getValue())
									.matches(mooseLauncher.getFileDependenciesSearchString())) {

						Entry clonedEntry = (Entry) e.clone();

						// If this Entry does not have a very descriptive
						// name
						// we should reset its name to the block it belongs
						// to
						if ("file".equals(clonedEntry.getName().toLowerCase())
								|| "data_file".equals(clonedEntry.getName().toLowerCase())) {
							clonedEntry.setName(child.getName());
						}

						if (!clonedEntry.getValueType().equals(AllowedValueType.File)) {
							mooseModel.convertToFileEntry(clonedEntry);

						}

						// Setup allowed values correctly
						String extension = FilenameUtils
								.getExtension(project.getFile(clonedEntry.getValue()).getLocation().toOSString());

						// Create a new content provider with the new file
						// in the allowed values list
						IEntryContentProvider prov = new BasicEntryContentProvider();
						ArrayList<String> valueList = clonedEntry.getAllowedValues();

						for (String file : getProjectFileNames(extension)) {
							if (!valueList.contains(file)) {
								valueList.add(file);
							}
						}
						prov.setAllowedValueType(AllowedValueType.File);

						// Finish setting the allowed values and default
						// value
						prov.setAllowedValues(valueList);

						// Set the new provider
						clonedEntry.setContentProvider(prov);

						// Set the value
						clonedEntry.setValue(e.getValue());

						fileEntryTreeMapping.put(clonedEntry.getName(), child);

						clonedEntry.register(this);

						// Add it to the list of model files.
						modelFiles.addEntry(clonedEntry);
					}
				}
			}
		}

		return;
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
			String directory = mooseLauncher.getJobLaunchDirectory();

			// Get a reference to the VizResource file we are going
			// to create and populate
			File dataFile = new File(directory + System.getProperty("file.separator") + name + ".csv");

			// Get a reference to the ResourceComponent
			ResourceComponent comp = (ResourceComponent) form.getComponent(3);

			try {

				if (!dataFile.exists()) {
					// If the file hasn't been created yet, we need to create
					// it and start filling it with post processor data
					dataFile.createNewFile();

					// Write the new incoming data
					PrintWriter printWriter = new PrintWriter(new FileOutputStream(dataFile, true));
					printWriter.write("Time, " + name + "\n");
					printWriter.write(time + ", " + value + "\n");
					printWriter.close();

					// Create the VizResource, and add it to the
					// ResourceComponent
					ICEResource resource = getResource(dataFile.getAbsolutePath());
					comp.add(resource);

					// Remember the name of the resource for next time
					postProcessorResources.put(name, resource);

				} else {

					// Write the data to the existing resource
					PrintWriter printWriter = new PrintWriter(new FileOutputStream(dataFile, true));
					printWriter.write(time + ", " + value + "\n");

					// Update the ICEResource
					ICEResource r = postProcessorResources.get(name);

					// Here we are faking a VizResource notification
					// by setting the name with its current name
					r.setName(r.getName());

					// Close the writer
					printWriter.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
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
		postProcessorsData.clearEntries();
		for (int i = 0; i < ppTree.getNumberOfChildren(); i++) {
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

	/**
	 * This operation is used to check equality between the MOOSE Item and
	 * another MOOSE Item. It returns true if the Items are equal and false if
	 * they are not.
	 * 
	 * @param otherMoose
	 *            The MOOSE Item that should be checked for equality.
	 * @return True if the launchers are equal, false if not
	 */
	public boolean equals(MOOSE otherMoose) {

		boolean retVal;
		// Check if they are the same reference in memory
		if (this == otherMoose) {
			return true;
		}

		// Check that the object is not null, and that it is an Item
		// Check that these objects have the same ICEObject data
		if (otherMoose == null || !(otherMoose instanceof Item) || !super.equals(otherMoose)) {
			return false;
		}

		// Check data
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
		super.copy((Item) otherMoose);

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
	public Object clone() {

		// Create a new instance of JobLauncher and copy the contents
		MOOSE clone = new MOOSE();
		clone.copy(this);

		return clone;
	}

}
