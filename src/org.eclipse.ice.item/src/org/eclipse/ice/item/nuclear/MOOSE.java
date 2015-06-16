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
import java.net.URI;
import java.util.ArrayList;

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
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.iterator.BreadthFirstTreeCompositeIterator;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.eclipse.ice.item.messaging.Message;
import org.eclipse.ice.item.utilities.moose.MOOSEFileHandler;

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
	@XmlElement()
	private MOOSEModel mooseModel;

	/**
	 * Reference to the MOOSELauncher used in executing a constructed MOOSE
	 * input file.
	 */
	@XmlElement()
	private MOOSELauncher mooseLauncher;

	/**
	 * Reference to the Model's list of files.
	 */
	@XmlTransient()
	private DataComponent modelFiles;

	/**
	 * Reference to the Model's input tree.
	 */
	@XmlTransient()
	private TreeComposite modelTree;

	/**
	 * Nullary constructor.
	 */
	public MOOSE() {
		this(null);
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
				+ "Environment (MOOSE) is a multiphysics framework developed "
				+ "by Idaho National Laboratory.";

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

		if (!registered) {
			variablesTree.register(this);
			modelTree.register(this);
			registered = true;
		}

		return status;
	}

	private boolean registered = false;

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

			// Add the ICEUpdater tree block to Outputs
			TreeComposite outputs = getTreeByName("Outputs");
			boolean iNeedUpdater = true;
			for (int i = 0; i < outputs.getNumberOfChildren(); i++) {
				if ("ICEUpdater".equals(outputs.getChildAtIndex(i).getName())) {
					iNeedUpdater = false;
					break;
				}
			}

			if (iNeedUpdater) {
				for (int i = 0; i < outputs.getChildExemplars().size(); i++) {
					if ("ICEUpdater".equals(outputs.getChildExemplars().get(i)
							.getName())) {
						System.out
								.println("Creating an instance of ICEUpdater");
						TreeComposite updater = (TreeComposite) outputs
								.getChildExemplars().get(i).clone();
						outputs.setNextChild(updater);

						DataComponent data = (DataComponent) updater
								.getDataNodes().get(0);
						data.retrieveEntry("item_id").setValue(
								String.valueOf(getId()));
						data.retrieveEntry("url")
								.setValue(
										"http://localhost:"
												+ System.getProperty("org.eclipse.equinox.http.jetty.http.port")
												+ "/ice/update");
						updater.setActive(true);
						updater.setActiveDataNode(data);
						break;
					}
				}
			}

			// Get a reference to the Launchers files component
			DataComponent launcherFiles = (DataComponent) mooseLauncher
					.getForm().getComponent(1);

			// Grab the file name the user has specified for the input file
			String fileName = modelFiles.retrieveEntry("Output File Name")
					.getValue();

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
			URI appUri = URI.create(modelFiles.retrieveEntry(
					"MOOSE-Based Application").getValue());

			// Set the executable string
			mooseLauncher.setExecutable(new File(appUri).getName(), "",
					appUri.getPath() + " -i ${inputFile} --no-color");

			// Register as a listener of the resource component
			// so we can add the resources to our resource component
			((ResourceComponent) mooseLauncher.getForm().getComponent(
					JobLauncherForm.outputId)).register(this);

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
			if (!"MOOSE-Based Application".equals(name)
					&& !"Output File Name".equals(name)) {
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
				+ "Environment (MOOSE) is a multiphysics framework developed "
				+ "by Idaho National Laboratory.";

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

			if ("Parallel Execution".equals(c.getName())
					|| "Output Files and Data".equals(c.getName())) {
				form.addComponent(c);
			}

		}

		// Get a handle to the model input tree
		modelTree = (TreeComposite) form.getComponent(2);

		loadFileEntries();

		// Register this Item as a listener to the Variables block
		// this is so we can use the variables to populate things like
		// kernel variable entries.
		getTreeByName("Variables").register(this);
		getTreeByName("AuxVariables").register(this);
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
			ResourceComponent ourComp = (ResourceComponent) form
					.getComponent(3);

			ArrayList<String> names = new ArrayList<String>();

			// Get a list of all our Resource Names:
			for (ICEResource r : ourComp.getResources()) {
				names.add(r.getName());
			}

			for (ICEResource r : comp.getResources()) {
				if (!names.contains(r.getName())) {
					System.out.println("Adding Resource to Moose: "
							+ r.getName());
					ourComp.add(r);
				}
			}

		} else if (updateable instanceof TreeComposite) {
			// If this is a tree composite we should reset our variables
			TreeComposite tree = (TreeComposite) updateable;
			Thread varThread = new Thread(new Runnable() {

				@Override
				public void run() {
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
		BreadthFirstTreeCompositeIterator iter = new BreadthFirstTreeCompositeIterator(
				modelTree);
		while (iter.hasNext()) {
			TreeComposite child = iter.next();

			// Make sure we have a valid DataComponent
			if (child.getActiveDataNode() != null && child.isActive()) {
				DataComponent data = (DataComponent) child.getActiveDataNode();
				for (Entry e : data.retrieveAllEntries()) {

					// If the Entry's tag is "false" it is a commented out
					// parameter.
					if (!"false".equals(e.getTag())
							&& e.getValue() != null
							&& !e.getValue().isEmpty()
							&& (e.getName() + " = " + e.getValue())
									.matches(mooseLauncher
											.getFileDependenciesSearchString())) {

						Entry clonedEntry = (Entry) e.clone();

						// If this Entry does not have a very descriptive
						// name
						// we should reset its name to the block it belongs
						// to
						if ("file".equals(clonedEntry.getName().toLowerCase())
								|| "data_file".equals(clonedEntry.getName()
										.toLowerCase())) {
							clonedEntry.setName(child.getName());
						}

						if (!clonedEntry.getValueType().equals(
								AllowedValueType.File)) {
							mooseModel.convertToFileEntry(clonedEntry);

						}

						// Setup allowed values correctly
						String extension = FilenameUtils.getExtension(project
								.getFile(clonedEntry.getValue()).getLocation()
								.toOSString());

						// Create a new content provider with the new file
						// in the allowed values list
						IEntryContentProvider prov = new BasicEntryContentProvider();
						ArrayList<String> valueList = clonedEntry
								.getAllowedValues();

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

		super.update(message);

		String type = message.getType();
		String text = message.getMessage();

		if ("FILE_CREATED".equals(type)) {

		} else if ("MESSAGE_POSTED".equals(type)) {

		}

		return true;
	}
}
