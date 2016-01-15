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
package org.eclipse.ice.item.jobLauncher.multiLauncher;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;

/**
 * <p>
 * The MultiLauncher is an Item that is capable of launching a set of multiple
 * jobs. It is a composite Item and requires information about the other job
 * launchers available to ICE. The list of other job launchers is provided by
 * the MultiLauncherBuilder.
 * </p>
 * <p>
 * The MultiLauncher is capable of launching a set of jobs, either serially or
 * in parallel. By default, the MultiLauncher launches the jobs serially unless
 * it is directed otherwise through its "Execution Mode" Data Component.
 * </p>
 * <p>
 * The output data from all of the runs are collected and placed in a
 * ResourceComponent on the MultiLauncherForm.
 * </p>
 * <p>
 * For the time being, this class assumes that it is working with instances of
 * ICE's JobLauncher class. Specifically, it assumes that the first
 * DataComponent in the Form for one of the Items contains the job launch
 * details. (See the JobLauncherForm for reference.)
 * </p>
 * <p>
 * When jobs are launched in sequential mode, the MultiLauncher launches a
 * thread and monitors each job launcher until it finishes, then launches the
 * next or breaks out if there was an error. When jobs are launched in parallel,
 * the job launchers are processed asynchronously.
 * </p>
 * <p>
 * This class implements Runnable and uses itself as the thread. The run()
 * operation works overtime: it handles all updates to the ResourceComponent
 * from the launchers, it handles all job launch for the sequential launch and
 * it polls the running jobs constantly to update the status if it is launched
 * in parallel. This is implemented with two blocks. The first handles the
 * sequential execution if it is required and the second updates both the
 * parallel execution status and the ResourceComponent. I think, ideally, this
 * would be separated out to a couple of separate threads.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class MultiLauncher extends Item implements Runnable {
	/**
	 * <p>
	 * The set of JobLaunchers that are available to the MultiLauncher.
	 * </p>
	 * 
	 */
	private ArrayList<Item> launchers = null;

	/**
	 * <p>
	 * The DataComponent from the Form that is the header of the
	 * MasterDetailsComponent and contains the Execution Mode information. It
	 * contains a single Entry that determines whether or not the jobs should be
	 * launched sequentially or in parallel. The name of the DataComponent is
	 * "Execution Mode" and the name of its Entry is
	 * "Enable Parallel Execution."
	 * </p>
	 * <p>
	 * </p>
	 * 
	 */
	private DataComponent executionModeComp;

	/**
	 * <p>
	 * The MasterDetailsComponent from the Form that contains all of the job
	 * launch information. The template for this component is set in
	 * setJobLaunchers() after gathering the information from the Items.
	 * </p>
	 * 
	 */
	private MasterDetailsComponent jobMasterDetailsComp;

	/**
	 * <p>
	 * The list of launchers that are currently being executed by the
	 * MultiLauncher.
	 * </p>
	 * 
	 */
	private ArrayList<Item> runningLaunchers;

	/**
	 * <p>
	 * An AtomicInteger for storing the current status of the jobs.
	 * </p>
	 * 
	 */
	private AtomicReference<FormStatus> multiLaunchStatus;

	/**
	 * <p>
	 * An AtomicBoolean that evaluates to true if the launch is being performed
	 * sequentially and false if not.
	 * </p>
	 * 
	 */
	private AtomicBoolean isSequential;

	/**
	 * <p>
	 * The foremost launcher whose status is FormStatus.NeedsInfo.
	 * </p>
	 * 
	 */
	private Item needyLauncher;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The project space in which the MultiLauncher should manage its
	 *            data.
	 *            </p>
	 */
	public MultiLauncher(IProject projectSpace) {

		// Setup the super class
		super(projectSpace);

		// Setup the atomics
		isSequential = new AtomicBoolean();
		isSequential.set(false);
		multiLaunchStatus = new AtomicReference<FormStatus>();
		multiLaunchStatus.set(FormStatus.InfoError);

	}

	/**
	 * <p>
	 * This operation sets a set of Items that are capable of launching jobs.
	 * This sets the list of jobs that a client may configure in the launcher's
	 * Form.
	 * </p>
	 * 
	 * @param jobLaunchers
	 *            <p>
	 *            The list of launchers available to the MultiLauncher. These
	 *            are Items with an Item type of Simulation.
	 *            </p>
	 */
	public void setJobLaunchers(ArrayList<Item> jobLaunchers) {

		// Local Declarations
		ArrayList<MasterDetailsPair> pairs = null;
		MasterDetailsPair tmpPair = null;
		DataComponent details = null;
		IEntry inputFilesEntry = null;
		ArrayList<String> inputFilesList = null;

		// Set the launchers
		launchers = jobLaunchers;

		// If the launchers actually exist, setup the list on the
		// MultiLauncherForm by creating a template of master-details pairs for
		// the MasterDetailsComponent.
		if (jobMasterDetailsComp != null && launchers != null) {
			// Create the array of pairs that will be the template
			pairs = new ArrayList<MasterDetailsPair>();
			// There is one pair for each launcher
			for (int i = 0; i < launchers.size(); i++) {
				Item launcher = launchers.get(i);
				// Create the pair
				tmpPair = new MasterDetailsPair();
				tmpPair.setMaster(launcher.getName());
				// FIXME! - The next set of commands assumes the Item is a
				// JobLauncher!
				// Get the component for the details
				details = (DataComponent) launcher.getForm().getComponent(1);
				// Get the input files and add the "LAST" value for chaining
				// jobs.
				inputFilesEntry = details.retrieveEntry("Input File");
				inputFilesList = (ArrayList<String>) inputFilesEntry.getAllowedValues();
				inputFilesList.add("LAST");
				// Reset the list
				((JobLauncherForm) launcher.getForm()).setInputFiles(
						"Input File",
						"The input file that should be used in the launch.",
						inputFilesList);
				// Set the details
				tmpPair.setDetails(details);
				// Add the pair to the list
				pairs.add(tmpPair);
			}
			// Set the template
			jobMasterDetailsComp.setTemplates(pairs);
		}

	}

	/**
	 * <p>
	 * This operation overrides Item.setupForm and creates a Form for the
	 * MultiLauncher that contains a MasterDetailsComponent and a DataComponent.
	 * The MasterDetailsComponent is built from the Forms of the other
	 * JobLaunchers currently running in ICE. The header - a DataComponent - of
	 * the MasterDetailsComponent contains a single Entry that determines
	 * whether or not the jobs should be launched sequentially or in parallel.
	 * </p>
	 * <p>
	 * The name of the DataComponent is "Execution Mode" and the name of its
	 * Entry is "Enable Parallel Execution."
	 * </p>
	 * <p>
	 * Construction of these pieces is delegated to MultiLauncherForm.
	 * </p>
	 * 
	 */
	@Override
	protected void setupForm() {

		// Set some particulars
		setName("MultiLauncher");
		setDescription("The MultiLauncher is capable of launching a set of "
				+ "jobs, either serially or in parallel. By default, the "
				+ "MultiLauncher launches the jobs serially unless it is "
				+ "directed otherwise through its \"Execution Mode\" Data "
				+ "Component.");

		// Create a new MultiLauncherForm. It does everything we need! :)
		form = new MultiLauncherForm();

		// Setup the list for the running launchers
		runningLaunchers = new ArrayList<Item>();

		// Setup the list of actions
		allowedActions.clear();
		allowedActions.add("Launch");

		// Get the components
		for (Component i : form.getComponents()) {
			i.accept(this);
		}

	}

	/**
	 * <p>
	 * This operation directs the MultiLauncher to launch the jobs. The only
	 * valid input for this operation is the word "Launch" and any other string
	 * will result in failure.
	 * </p>
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of the action. For this class, only "Launch" is
	 *            valid.
	 *            </p>
	 * @return <p>
	 *         The status of the launch or FormStatus.InfoError if the launch
	 *         fails.
	 *         </p>
	 */
	@Override
	public FormStatus process(String actionName) {

		// Local Declarations
		FormStatus launcherStatus = FormStatus.InfoError;

		// Only proceed if the action is valid and jobs are in the form
		if ("Launch".equals(actionName)
				&& jobMasterDetailsComp.numberOfMasters() > 0) {
			// Configure the Forms of the job launchers based on the form of the
			// MultiLauncher
			launcherStatus = configureSubforms();
			// Only process if all the launchers are ready
			if (launcherStatus.equals(FormStatus.ReadyToProcess)) {
				// Figure out whether to launch sequentially or in parallel
				boolean isParallel = Boolean.parseBoolean(executionModeComp
						.retrieveEntry("Enable Parallel Execution").getValue());
				// Set the atomic boolean so that this can be tracked on other
				// threads. Make sure to flip the boolean since this one is for
				// sequential mode not parallel mode.
				isSequential.set(!isParallel);
				// Launch the jobs
				if (isParallel) {
					// In parallel
					launcherStatus = launchInParallel();
				} else {
					// In sequence
					launcherStatus = launchSequentially();
				}
			}
		} else if (!(runningLaunchers.isEmpty())) {
			// Return "Processing" if the MultiLauncher is already working.
			launcherStatus = FormStatus.Processing;
		}

		// Set the final launcherStatus that will be preserved.
		multiLaunchStatus.set(launcherStatus);

		return launcherStatus;
	}

	/**
	 * <p>
	 * This operation overrides Item.getStatus(). It functions as
	 * Item.getStatus(), but it must be overridden because the MultiLauncher
	 * needs to check the status of each running job, not just the current
	 * running action.
	 * </p>
	 * 
	 * @return <p>
	 *         The status. See Item.getStatus() for an exact description.
	 *         </p>
	 */
	@Override
	public FormStatus getStatus() {

		// Local Declarations
		FormStatus launcherStatus = FormStatus.InfoError;

		// Get the status
		launcherStatus = multiLaunchStatus.get();

		return launcherStatus;
	}

	/**
	 * <p>
	 * This operation overrides Item.getForm() to return the proper Form for one
	 * of the launchers if it is needed. Otherwise, it behaves exactly like
	 * Item.getForm().
	 * </p>
	 * 
	 * @return <p>
	 *         The Form.
	 *         </p>
	 */
	@Override
	public Form getForm() {

		// Local Declarations
		Form retForm = form;
		Item launcher = null;
		FormStatus launchStatus = getStatus();

		// If the MultiLauncher is processing, then the launchers need to be
		// checked.
		if (launchStatus.equals(FormStatus.Processing)
				|| launchStatus.equals(FormStatus.NeedsInfo)) {
			// Search for the first launcher that has a status of "NeedsInfo"
			for (int i = 0; i < runningLaunchers.size(); i++) {
				launcher = runningLaunchers.get(i);
				if (launcher.getStatus().equals(FormStatus.NeedsInfo)) {
					// Return the Form
					retForm = launcher.getForm();
					// Set the needyLauncher reference
					needyLauncher = launcher;
					break;
				}
			}
		}

		return retForm;
	}

	/**
	 * <p>
	 * This operation overrides Item.submitForm() to make sure that the Form is
	 * submitted to the proper JobLauncher and not submitted to an Action.
	 * Otherwise, it behaves exactly like Item.submitForm().
	 * </p>
	 * 
	 * @param preparedForm
	 *            <p>
	 *            The Form.
	 *            </p>
	 * @return <p>
	 *         The status.
	 *         </p>
	 */
	@Override
	public FormStatus submitForm(Form preparedForm) {

		// Local Declarations
		FormStatus launcherStatus = FormStatus.InfoError;

		// Set the initial launcherStatus condition
		launcherStatus = FormStatus.InfoError;

		// Check the Form Entries only if the Form represents this Item
		if (preparedForm.getItemID() == this.uniqueId) {
			// Figure out whether to submit it to the Action
			if (!getStatus().equals(FormStatus.NeedsInfo)) {
				// Review the Entries
				launcherStatus = reviewEntries(preparedForm);
				// Overwrite the current Form if the review passed
				if (launcherStatus != FormStatus.InfoError) {
					form = preparedForm;
				}
				// If the review passed and the Form is ready to process, mark
				// it as such
				if (launcherStatus == FormStatus.ReadyToProcess) {
					form.markReady(true);
				}
				// Set the launcherStatus
				multiLaunchStatus.set(launcherStatus);
			} else {
				// Submit the Form to the needy launcher
				needyLauncher.submitForm(preparedForm);
				logger.info("MultiLauncher Message: "
						+ "Submitting sub-form to needy launcher.");
				launcherStatus = FormStatus.Processing;
				multiLaunchStatus.set(launcherStatus);
			}
		}

		return launcherStatus;
	}

	/**
	 * <p>
	 * This operation launches the jobs sequentially. It starts the
	 * MultiLauncher thread to do this.
	 * </p>
	 * 
	 * @return <p>
	 *         The launch status.
	 *         </p>
	 */
	private FormStatus launchSequentially() {

		// Local Declarations
		Thread launchThread = new Thread(this);

		// Set the status flag
		multiLaunchStatus.set(FormStatus.Processing);

		// Launch the thread
		launchThread.start();

		return FormStatus.Processing;
	}

	/**
	 * <p>
	 * This operation launches the jobs in parallel. They jobs are launched
	 * asynchronously on their own threads, which they manage, so that this
	 * operation need not be threaded.
	 * </p>
	 * 
	 * @return <p>
	 *         The launch status.
	 *         </p>
	 */
	private FormStatus launchInParallel() {

		// Local Declarations
		FormStatus retVal = FormStatus.InfoError;
		Thread launchThread = new Thread(this);

		// Launch each job
		for (Item job : runningLaunchers) {
			// Launch the job if the Form is ready to process
			if (job.getStatus().equals(FormStatus.ReadyToProcess)) {
				logger.info("MultiLauncher Message: "
						+ "Launching parallel job " + job.getName()
						+ " with id " + job.getId());
				// Launch it
				retVal = job.process("Launch the Job");
			} else {
				logger.info("MultiLauncher Message: "
						+ "Unable to launch parallel job!");
				logger.info("MultiLauncher Message: " + "Failed job name = "
						+ job.getName());
				logger.info("MultiLauncher Message: " + "Failed job id = "
						+ job.getId());
				break; // Otherwise throw the error.
			}
		}

		// Set the status flag
		multiLaunchStatus.set(FormStatus.Processing);

		// Launch the thread
		launchThread.start();

		return retVal;
	}

	/**
	 * <p>
	 * This operation sets up a chained input file from a previous job if it is
	 * required based on the configuration.
	 * </p>
	 * 
	 * @param filename
	 *            <p>
	 *            The name of the output file that the Form's input should be
	 *            changed to if its current input is "LAST."
	 *            </p>
	 * @param currentJob
	 *            <p>
	 *            The current job whose form should be reconfigured.
	 *            </p>
	 */
	private void setupChainedInput(String filename, Item currentJob) {

		// Local Declarations
		Form jobForm = null;
		String inputName = null;
		DataComponent inputFilesComp = null;
		IEntry inputFilesEntry = null;
		ArrayList<String> inputFilesList = null;

		// Get the Form
		jobForm = currentJob.getForm();
		// Get the file name and check it. FIXME! This assumes the form is
		// coming from a JobLauncher
		inputName = ((DataComponent) jobForm.getComponent(1)).retrieveEntry(
				"Input File").getValue();
		// If the name of the input file is set to LAST, then we need to set it
		// to the proper name to enable the chain.
		if ("LAST".equals(inputName)) {
			inputFilesComp = (DataComponent) jobForm.getComponent(1);
			inputFilesEntry = inputFilesComp.retrieveEntry("Input File");
			// Add the input file to the list of input files if it does not
			// exist
			if (!inputFilesEntry.getAllowedValues().contains(filename)) {
				inputFilesList = (ArrayList<String>) inputFilesEntry.getAllowedValues();
				inputFilesList.add(0, filename);
				logger.info("MultiLauncher Message: "
						+ "Resetting file name for chained launch to "
						+ filename);
				// Reset the list
				String desc = "The input file that should be used in the launch.";
				((JobLauncherForm) currentJob.getForm()).setInputFiles(
						"Input File", desc, inputFilesList);
			}
			// Set the input name
			inputFilesEntry.setValue(filename);
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns the name of the first output file in the
	 * ResourceComponent of the job's Form.
	 * </p>
	 * 
	 * @param job
	 *            <p>
	 *            The Item, assumed to be a JobLauncher, that is should be
	 *            examined and its first output filename returned.
	 *            </p>
	 * @return <p>
	 *         The name of the first output file in the ResourceComponent from
	 *         the Job.
	 *         </p>
	 */
	private String getOutputFilename(Item job) {

		// Local Declarations
		Form jobForm = null;
		String outputName = null;
		URI fileURI = null;
		IFile[] fileHandles = null;

		// Get the Form
		jobForm = job.getForm();
		// Get the file name. We only need the last part of it. FIXME! This
		// assumes the form is coming from a JobLauncher
		fileURI = ((ResourceComponent) jobForm.getComponent(2)).getResources()
				.get(0).getPath();
		fileHandles = project.getWorkspace().getRoot()
				.findFilesForLocationURI(fileURI);
		outputName = fileHandles[0].getName();

		logger.info("MultiLauncher Message: Last filename = " + outputName);

		return outputName;

	}

	/**
	 * <p>
	 * This operation configures the Forms of the JobLaunchers based on the Form
	 * of the MultiLauncher. It submits the Forms to the launchers after it
	 * commits them.
	 * </p>
	 * 
	 * @return <p>
	 *         The status of the configuration attempt. It will be
	 *         FormStatus.InfoError if something goes wrong or
	 *         FormStatus.ReadyToProcess if everything is OK.
	 *         </p>
	 */
	private FormStatus configureSubforms() {

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		Item job = null;
		Form jobForm = null;
		DataComponent launchParametersComp = null, inputLaunchParamsComp = null;

		// Loop over all of the jobs and configure them
		for (int i = 0; i < jobMasterDetailsComp.numberOfMasters(); i++) {
			// Find the appropriate launcher. A linear search should be fast
			// enough since the number of masters is low.
			for (Item j : launchers) {
				if (j.getName()
						.equals(jobMasterDetailsComp.getMasterAtIndex(i))) {
					// Create a new launcher and give it the same id as this
					// item and a unique name
					job = (Item) j.clone();
					job.setId(getId() + i);
					job.setName(job.getName() + "_" + i);
					break;
				}
			}
			// Re-configure the Form in the Item if possible
			if (job != null) {
				// Get the Form
				jobForm = job.getForm();
				// Get the DataComponent from the JobLauncher that contains
				// the configuration parameters - FIXME! This assumes a
				// JobLauncher!
				launchParametersComp = (DataComponent) jobForm.getComponent(1);
				// Get the DataComponent from the input form from the client
				inputLaunchParamsComp = jobMasterDetailsComp
						.getDetailsAtIndex(i);
				// We can only launch if the parameters exist
				if (launchParametersComp != null
						&& (!(launchParametersComp.retrieveAllEntries()
								.isEmpty()))) {
					// Loop over all of the Entries and transfer the values
					for (IEntry entry : inputLaunchParamsComp
							.retrieveAllEntries()) {
						// Search for an Entry by the same name in the
						// component
						IEntry launchEntry = launchParametersComp
								.retrieveEntry(entry.getName());
						// And set its value if it exists. Note that this
						// will ignore Entries that do not exist!
						if (launchEntry != null) {
							launchEntry.setValue(entry.getValue());
						}
					}
				}
				// Submit the Form
				retStatus = job.submitForm(jobForm);
				// Only process if all the launchers are ready
				if (!retStatus.equals(FormStatus.ReadyToProcess)) {
					break;
				}
				// Add the launcher to the list of running launchers
				runningLaunchers.add(job);
			} else {
				// Return if the job does not exist in the list because
				// something is wrong.
				break;
			}
		}

		return retStatus;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Runnable#run()
	 */
	@Override
	public void run() {

		// Local Declarations
		Item job = null;
		FormStatus launchStatus = FormStatus.InfoError;
		String lastFilename = null;
		boolean checkStatus = true;
		ArrayList<FormStatus> runningLauncherStatuses = new ArrayList<FormStatus>();

		/*----- Read the documentation on the class before editing this! -----*/

		// Sequential launch block. Make sure we are in the correct mode.
		if (isSequential.get()) {
			// Loop over all of the launchers
			for (int i = 0; i < runningLaunchers.size(); i++) {
				// Get and launch the job
				job = runningLaunchers.get(i);
				logger.info("MultiLauncher Message: "
						+ "Launching sequential job #" + (i + 1) + ", "
						+ job.getName());
				// Set the input file to the output file of the last code it is
				// necessary.
				if (i > 0) {
					setupChainedInput(lastFilename, job);
				}
				job.process("Launch the Job");
				// Launch the job
				launchStatus = job.getStatus();
				// Start the status checking loop
				multiLaunchStatus.set(launchStatus);
				while (launchStatus.equals(FormStatus.Processing)
						|| launchStatus.equals(FormStatus.NeedsInfo)) {
					// The thread shouldn't poll the job continuously. Let it
					// sleep some.
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.error(getClass().getName() + " Exception!",e);
					}
					// Update the status
					launchStatus = job.getStatus();
					multiLaunchStatus.set(launchStatus);
				}
				// Break if there is some kind of error
				if (multiLaunchStatus.get().equals(FormStatus.InfoError)) {
					break;
				}
				// Get the name of the last output file
				lastFilename = getOutputFilename(job);
			}
		}

		// Setup the list of launcher statuses before going into the update
		// block.
		for (Item launcher : runningLaunchers) {
			runningLauncherStatuses.add(launcher.getStatus());
		}

		// Status update block.
		while (checkStatus) {
			// Loop over all of the launchers and get their statuses.
			for (int i = 0; i < runningLaunchers.size(); i++) {
				Item launcher = runningLaunchers.get(i);
				runningLauncherStatuses.set(i, launcher.getStatus());
			}
			// Check the status. This switch works by checking if an instance of
			// any of the launchers is in a particular state and setting the
			// status of the MultiLauncher to that state. First, quit if there
			// is an error.
			if (runningLauncherStatuses.contains(FormStatus.InfoError)) {
				launchStatus = FormStatus.InfoError;
				checkStatus = false;
			} else if (runningLauncherStatuses.contains(FormStatus.NeedsInfo)) {
				// Else look for a launcher that needs more information
				launchStatus = FormStatus.NeedsInfo;
			} else if (runningLauncherStatuses.contains(FormStatus.Processing)) {
				// Else look for a launcher that is still processing
				launchStatus = FormStatus.Processing;
			} else {
				// We're processed! Since all of the other statuses except
				// Processed are covered in the process() operation, break out
				// of the thread.
				launchStatus = FormStatus.Processed;
				checkStatus = false;
			}
			// Update the status on the thread.
			multiLaunchStatus.set(launchStatus);
			// The thread shouldn't poll the jobs continuously. Let it
			// sleep some.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error(getClass().getName() + " Exception!",e);
			}
		}
		// Add the output if the status does not indicate an error
		if (launchStatus.equals(FormStatus.Processed)) {
			// Get the ResourceComponent for the MultiLauncher and clear its
			// output
			ResourceComponent resourceComp = (ResourceComponent) form
					.getComponent(2);
			resourceComp.clearResources();
			// Loop over the launchers and get the output
			for (int i = 0; i < runningLaunchers.size(); i++) {
				// Get the component
				ResourceComponent launcherResourceComp = (ResourceComponent) runningLaunchers
						.get(i).getForm().getComponent(2);
				// Add its resources to the MultiLaunchers ResourceComponent
				for (ICEResource j : launcherResourceComp.getResources()) {
					resourceComp.addResource(j);
				}
			}
			// Clear the launchers if needed
			runningLaunchers.clear();
		}

	}

	/**
	 * This operation overrides the MasterDetailsComponent visitor from the Item
	 * base class to search for the MasterDetailsComponent in the
	 * MultiLauncher's Form.
	 * 
	 * @see IComponentVisitor#visit(MasterDetailsComponent component)
	 */
	@Override
	public void visit(MasterDetailsComponent component) {

		jobMasterDetailsComp = component;

		// Get the header if it is available
		if (jobMasterDetailsComp != null) {
			executionModeComp = jobMasterDetailsComp.getGlobalsComponent();
		}
	}

}