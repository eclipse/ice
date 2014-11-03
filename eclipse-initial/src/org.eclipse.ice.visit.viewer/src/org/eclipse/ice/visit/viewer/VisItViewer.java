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
package org.eclipse.ice.visit.viewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IAnalysisTool;
import org.eclipse.ice.analysistool.IAnalysisDocument;

import java.util.concurrent.atomic.AtomicReference;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The VisItViewer class is the primary class in ICE for analyzing data produced
 * as part of a VisIt simulation.
 * </p>
 * <p>
 * Documentation about the Form for this class is described under setupForm().
 * </p>
 * <p>
 * This class searches for files in the default project space to create the list
 * of Data Sources, including Silo (.silo) and Exodus (.e) files.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "VisItViewer")
public class VisItViewer extends Item implements Runnable {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The data source currently being analyzed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String dataSource;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The set of IAnalysisTools available to the VisItViewer. If this list does
	 * not contain any tools, the VisItViewer can not perform any analysis
	 * operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IAnalysisTool> analysisTools;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A reference to the IAnalysisDocuments used by the VisItViewer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IAnalysisDocument> analysisDocuments;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An AtomicReference to the status to keep the thread from messing things
	 * up.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicReference<FormStatus> concurrentStatus;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An AtomicReference to the Form used for processing on the thread.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicReference<Form> concurrentForm;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The set of file extensions supported by the VisItViewer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> extensions;

	/**
	 * The component id for the LWRComponent component that holds analysis
	 * artifacts
	 */
	private final int visitCompId = 2;

	/**
	 * The component id for the resource component that holds analysis artifacts
	 */
	private final int resourceCompId = 3;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project used by the VisItViewer.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisItViewer(IProject projectSpace) {
		// begin-user-code

		// Call super's constructor
		super(projectSpace);

		// Make sure the Data Source is null
		dataSource = null;

		// Initialize the status atomic
		concurrentStatus = new AtomicReference<FormStatus>();
		FormStatus formStatus = FormStatus.InfoError;
		concurrentStatus.set(formStatus);
		// Initialize the form atomic
		concurrentForm = new AtomicReference<Form>();

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An alternative nullary constructor used primarily for serialization. The
	 * setProject() operation must be called if this constructor is used!
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisItViewer() {
		// begin-user-code

		// Punt to the other constructor.
		this(null);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the IAnalysisTools that are available to the
	 * VisItViewer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param tools
	 *            <p>
	 *            The set of IAnalysisTools.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setAnalysisTools(ArrayList<IAnalysisTool> tools) {
		// begin-user-code

		// Only overwrite the tools if
		if (tools != null) {
			analysisTools = tools;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the default setupForm() operation on Item and
	 * creates a Form for the VisItViewer that has a MasterDetailsComponent and
	 * a ResourceComponent.
	 * </p>
	 * <p>
	 * The MasterDetailsComponent is used to select the types of analyses that
	 * should be performed and set their properties. (It is a little more like
	 * picking the types of artifacts that should be created, but close enough.)
	 * The MasterDetailsComponent contains a global data component (which is a
	 * DataComponent) that contains the list of files available as data sources.
	 * The MasterDetailsComponent is named "Analysis Configuration" and has
	 * id=1. The global data component is named "Data Sources" and has id=3. The
	 * global data component can not be retrieved directly from the Form and is
	 * only available by calling getGlobalsComponent() on the
	 * MasterDetailsComponent.
	 * </p>
	 * <p>
	 * The "Data Sources" component contains a single Entry named
	 * "Available Data Sources" with id=1. This Entry contains the list of
	 * allowed data sources that can be analyzed.
	 * </p>
	 * <p>
	 * The ResourceComponent is initially empty, but directing the VisItViewer
	 * to generate artifacts populates the ResourceComponent with those
	 * artifacts. The ResourceComponent is monitored for changes and the
	 * artifacts are updated on the fly. The ResourceComponent is named
	 * "Analysis Artifacts" and has id=2.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	protected void setupForm() {
		// begin-user-code

		// Set the name
		setName("VisIt Viewer");
		// Set the Item type
		itemType = ItemType.AnalysisSession;
		// Set description
		setDescription("Review and analyze data from simluations."
				+ " At the moment, only Silo, Exodus and ICE's HDF5 files "
				+ "are supported.");

		// No special Form for this, just a regular one
		form = new Form();

		// Setup the lists for analysis pieces
		analysisTools = new ArrayList<IAnalysisTool>();
		analysisDocuments = new ArrayList<IAnalysisDocument>();

		// Setup the action list
		allowedActions.clear();
		allowedActions.add("Generate Analysis Artifacts");

		// Configure the master details block
		configureMasterDetailsBlock();

		// Create the ResourceComponent that holds the analysis assets
		ResourceComponent assetsComp = new ResourceComponent();
		assetsComp.setName("Analysis Artifacts");
		assetsComp.setId(resourceCompId);
		assetsComp.setDescription("This page shows all of the analysis "
				+ "artifacts that were generate from the output file.");
		// Add the assets components to the Form
		form.addComponent(assetsComp);

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides Item.reviewEntries() to customize the review
	 * process for the VisItViewer. It is primarily concerned with changes to
	 * the data source that should be analyzed and changes to the properties of
	 * the analysis artifacts. It delegates most of its work to the check*() set
	 * of operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param preparedForm
	 *            <p>
	 *            The Form input from a client.
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
		FormStatus reviewStatus = FormStatus.InfoError;
		boolean checkStatus = false;
		DataComponent dataSourceComp = null;
		ResourceComponent resources = null;
		String currentDataSource = "";

		try {

			// Get the DataComponent that holds the data source selection
			dataSourceComp = (DataComponent) ((MasterDetailsComponent) preparedForm
					.getComponent(1)).getGlobalsComponent();
			// Get the current data source name
			currentDataSource = dataSourceComp.retrieveEntry(
					"Available Data Sources").getValue();

			// Get the ResourceComponent that holds the references to the
			// analysis
			// assets
			resources = (ResourceComponent) form.getComponent(resourceCompId);

			// Check the data source. This will also re-configure the master
			// details component if needed.
			if (!currentDataSource.equals(dataSource)) {
				checkStatus = checkDataSource(currentDataSource);
			} else {
				checkStatus = checkArtifactProperties(resources.getResources());
			}
			// Update the Form and status if the checks succeeded.
			if (checkStatus) {
				// Set Form
				form = preparedForm;
				// Set the return status to ready
				reviewStatus = FormStatus.ReadyToProcess;
			}

		} catch (Exception e) {
			// Catch any of the exceptions that are thrown the above operations.
			e.printStackTrace();
		}

		return reviewStatus;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the data source on the Form to determine if it has
	 * been set or changed from the previous value. If the value has changed
	 * (either case), then the MasterDetailsComponent is reconfigured to contain
	 * the proper list of available analysis types for the new data source.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param source
	 *            <p>
	 *            The name of the data source to compare to the last one.
	 *            </p>
	 * @return <p>
	 *         True if the operation was successful, false if not.
	 *         </p>
	 * @throws RuntimeException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected boolean checkDataSource(String source) throws RuntimeException {
		// begin-user-code

		// Local Declarations
		boolean status = true;
		IPath dataSourcePath = null;
		IAnalysisTool tool;

		// Check the data source
		if (!source.equals(dataSource)) {

			// Reset the source
			dataSource = source;
			// Get the data source path
			dataSourcePath = project.findMember(dataSource).getLocation();
			// Clear out the old documents
			analysisDocuments.clear();
			System.out.println("VisitViewer: Data Source is "
					+ dataSourcePath.toOSString());
			// Get documents for the new data source
			for (int i = 0; i < analysisTools.size(); i++) {
				tool = analysisTools.get(i);
				if (tool != null && tool.getName().contains("VisIt")) {
					analysisDocuments.add(tool.createDocument(dataSourcePath
							.toFile().toURI()));
				}
			}

			// Reconfigure the master details component
			configureMasterDetailsBlock();
			status = true;
		}

		return status;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the properties of analysis artifacts, which are
	 * ICEResources, in the ResourceComponent. If they have changed from the
	 * properties that are currently stored for the IAnalysisAsset, then the
	 * properties are updated on the asset and the asset itself is updated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param resources
	 *            <p>
	 *            The list of ICEResources that represent the analysis assets
	 *            whose properties should be checked.
	 *            </p>
	 * @return <p>
	 *         True if the check succeeded and properties were successfully,
	 *         false otherwise.
	 *         </p>
	 * @throws RuntimeException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected boolean checkArtifactProperties(ArrayList<ICEResource> resources)
			throws RuntimeException {
		// begin-user-code

		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation updates global parameters for the assets.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void updateGlobalParametersMap() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation configures the MasterDetailsComponent that is used by the
	 * VisItViewer to expose analysis asset types and the properties available
	 * for each type. It is updated each time the data source is changed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @throws RuntimeException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void configureMasterDetailsBlock() throws RuntimeException {
		// begin-user-code

		// Local Declarations
		int pairId = 0;
		ArrayList<MasterDetailsPair> templatePairs = null;
		MasterDetailsComponent oldComp = null;

		// Setup a list to store master-details pairs
		templatePairs = new ArrayList<MasterDetailsPair>();

		// Remove the master details component from the Form if it already
		// exists
		if (form.getComponent(1) != null) {
			oldComp = (MasterDetailsComponent) form.getComponent(1);
		}

		// Create the DataComponent that holds Data Sources
		DataComponent dataSourceComp = new DataComponent();
		dataSourceComp.setName("Data Sources");
		dataSourceComp.setId(3);
		dataSourceComp.setDescription("Select one of the available data "
				+ "sources from the list so that it can be analyzed.");

		// Create the Entry of data sources
		final ArrayList<String> dataSources = getDataSources();
		Entry dataSourcesEntry = new Entry() {
			// Override setup to contain the allowed files from the project
			// space
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Available Data Sources";
				this.uniqueId = 1;
				this.objectDescription = "The available data sources for analysis.";
				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues = dataSources;
			}
		};
		// Add the Entry
		dataSourceComp.addEntry(dataSourcesEntry);

		// If a current data source exists and it is an element of dataSources
		if (dataSource != null && dataSources.contains(dataSource)) {
			// Set the entry value to the current data source otherwise the
			// value will default to the default value and will not match the
			// currently selected data source
			dataSourceComp.retrieveEntry("Available Data Sources").setValue(
					dataSource);
		}

		// Create the MasterDetailsComponent that is used to hold the available
		// analysis types
		MasterDetailsComponent analysisConfigComp = new MasterDetailsComponent();
		analysisConfigComp.setName("Analysis Configuration");
		analysisConfigComp.setId(1);
		analysisConfigComp
				.setDescription("Select analysis artifacts to be created "
						+ "from the data and examine properties for each analysis type.");
		analysisConfigComp.setGlobalsComponent(dataSourceComp);

		// If there are analysis documents, configure the master values and
		// details blocks
		if (analysisDocuments.size() > 0) {
			// Create an empty data component as a place holder for now.
			// Eventually each IAnalysisDocument will publish information and
			// options that will be provided in this data component/details
			// block.
			DataComponent detailsBlock = new DataComponent();
			detailsBlock.setId(4);
			detailsBlock.setName("Global Analysis Options");
			detailsBlock
					.setDescription("The following options are available to "
							+ "configure the selected analysis operation at a global level. "
							+ "If no options are shown below, then this analysis has no "
							+ "published options in ICE.");
			// Loop over all of the available analysis documents to gather their
			// allowed asset types and descriptive information
			for (IAnalysisDocument doc : analysisDocuments) {
				if (doc != null) {
					// Loop over all of the allowed asset types for this
					// document
					for (String assetType : doc.getAvailableAssets()) {
						// Create a master-details pair
						MasterDetailsPair pair = new MasterDetailsPair();
						// Add the generic details block
						pair.setDetails(detailsBlock);
						// Add the asset type as the master value
						pair.setMaster(assetType);
						// Set the pair id
						pair.setId(pairId);
						// Increment the id for the next pair
						pairId++;
						// Add the pair to the template list
						templatePairs.add(pair);
					}
				}
			}
		}

		// Set the MasterDetailsComponent template. It may be empty, but the
		// component needs it regardless.
		analysisConfigComp.setTemplates(templatePairs);

		// Add the component to the Form if it doesn't already exist. If it does
		// exist, just copy the data over so that any references are maintained.
		if (oldComp != null) {
			oldComp.copy(analysisConfigComp);
		} else {
			form.addComponent(analysisConfigComp);
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the operation on the Item base class to generate
	 * analysis artifacts. The only valid action name is
	 * "Generate Analysis Artifacts."
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of the action. In the case of the VisIt Viewer, the
	 *            only valid value is currently "Generate Analysis Artifacts."
	 *            </p>
	 * @return <p>
	 *         The status of the processing attempt.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus process(String actionName) {
		// begin-user-code

		// Check the action name and status
		if ((actionName != null && "Generate Analysis Artifacts"
				.equals(actionName))
				&& (status.equals(FormStatus.ReadyToProcess) || status
						.equals(FormStatus.Processed))) {
			// Set the status
			status = FormStatus.Processing;
			// Set the references
			concurrentStatus.set(status);
			concurrentForm.set(form);
			// Start the processing thread
			Thread analysisThread = new Thread(this);
			analysisThread.start();
		} else
			return FormStatus.InfoError;

		return status;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the status of the VisItViewer and returns true if
	 * it can be processed safely.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the VisItViewer is in either the ReadyToProcess or
	 *         Processed states, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean checkProcessingStatus() {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a list of data sources based on the contents of
	 * the workspace. At the moment, it only finds .silo files.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The list of file names of the available data sources.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> getDataSources() {
		// begin-user-code

		// Local Declarations
		ArrayList<String> dataSources = new ArrayList<String>();

		if (extensions == null) {
			// Setup the allowed file extensions
			extensions = new ArrayList<String>();
			extensions.add("silo");
			extensions.add("e");
		}

		// Only load the list if nothing goes nuts
		try {
			// Look at all the files in the project
			for (IResource resource : project.members()) {
				// Get the extension
				String extension = "badExtension";
				extension = resource.getFileExtension();
				// Look for silo files
				if (extension != null && extensions.contains(extension)) {
					// Add the file to the list
					dataSources.add(resource.getProjectRelativePath()
							.toOSString());
				}
			}
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
		}

		return dataSources;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation functions as Item.getStatus(), but is overridden to handle
	 * concurrency within the VisItViewer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public FormStatus getStatus() {
		// begin-user-code

		// Pass to super.getStatus() if the thread isn't involved
		if (status != FormStatus.Processing) {
			return super.getStatus();
		} else {
			status = concurrentStatus.get();
			return status;
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Runnable#run()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void run() {
		// begin-user-code

		// Get the MasterDetailsComponent component from the concurrentForm
		MasterDetailsComponent analysisConfigComp = (MasterDetailsComponent) concurrentForm
				.get().getComponent(1);

		// Create an ArrayList to hold the selected assets
		ArrayList<String> selectedAssets = new ArrayList<String>();

		// Loop over all the MasterDetailsPairs
		for (int i = 0; i < analysisConfigComp.numberOfMasters(); i++) {

			// Add the value to the selected assets list
			selectedAssets.add(analysisConfigComp.getMasterAtIndex(i));

		}

		// Get the first document in the analysisDocuments list
		// since we will only have a VisItAnalysisDocument to access initially
		IAnalysisDocument document = analysisDocuments.get(0);

		// Set the selected assets on the document
		document.setSelectedAssets(selectedAssets);

		// Create the assets
		document.createSelectedAssets();

		// Get all of the selected assets
		ArrayList<IAnalysisAsset> assets = document.getAllAssets();

		// Here we are temporarily injecting modified properties
		// into each asset to provide a rotated view
		for (IAnalysisAsset asset : assets) {
			asset.setProperty("View Normal X", "0.440838");
			asset.setProperty("View Normal Y", "0.423938");
			asset.setProperty("View Normal Z", "0.791163");
			asset.setProperty("View Up X", "-0.243247");
			asset.setProperty("View Up Y", "0.904875");
			asset.setProperty("View Up Z", "-0.349332");
		}

		// Get the ResourceComponent from the concurrentForm
		ResourceComponent resourceComponent = (ResourceComponent) concurrentForm
				.get().getComponent(resourceCompId);

		// Check for any resources currently stored in the resourceComponent
		if (resourceComponent.getResources().size() > 0) {

			// Clear those resources
			resourceComponent.clearResources();

		}

		// Initialize a resource id counter
		int resourceId = 0;

		// Loop over all assets in the asset array list
		for (IAnalysisAsset asset : assets) {

			// Create a new ICEResource and assign values from the asset
			ICEResource resource = new ICEResource();
			resource.setId(resourceId);
			// Set the file contents for the resource
			try {
				resource.setContents(new File(asset.getURI().getPath()));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			resource.setPath(asset.getURI());
			resource.setName(asset.getName());
			resource.setPictureType(true);

			// Add the resource to the resourceComponent
			resourceComponent.addResource(resource);

			// Increment the resourceId
			resourceId++;

		}

		// Set the status
		concurrentStatus.set(FormStatus.Processed);

		// Assign the concurrentForm reference to form.
		form = concurrentForm.get();

		return;

		// end-user-code
	}
}