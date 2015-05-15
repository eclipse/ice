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
package org.eclipse.ice.reactorAnalyzer;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IAnalysisTool;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * <p>
 * The ReactorAnalyzer class is the primary class in ICE for analyzing data
 * produced as part of a reactor simulation.
 * </p>
 * <p>
 * The Form for this Item contains a<br>
 * <br>
 * Data Component ("Data Sources" with id=1), a<br>
 * ResourceComponent ("Analysis Artifacts" with id = 2), and a<br>
 * MasterDetailsComponent ("Analysis Configuration" with id = 3).<br>
 * <br>
 * The MasterDetailsComponent is used to select the types of analyses that
 * should be performed and set their properties. (It is a little more like
 * picking the types of artifacts that should be created, but close enough.)
 * </p>
 * <p >
 * The "Data Sources" component contains two Entries named "Input Data" with
 * id=1 and "Reference Data" with id = 2.
 * </p>
 * <p >
 * The ResourceComponent is initially empty, but directing the ReactorAnalyzer
 * to generate artifacts populates the ResourceComponent with those artifacts.
 * The ResourceComponent is monitored for changes and the artifacts are updated
 * on the fly. The ResourceComponent is named "Analysis Artifacts" and has id=2.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "ReactorAnalyzer")
public class ReactorAnalyzer extends Item implements Runnable {

	/**
	 * <p>
	 * The set of IAnalysisTools available to the ReactorAnalyzer. If this list
	 * does not contain any tools, the ReactorAnalyzer can not perform any
	 * analysis operations.
	 * </p>
	 * 
	 */
	private ArrayList<IAnalysisTool> analysisTools;
	/**
	 * <p>
	 * A reference to the IAnalysisDocuments used by the ReactorAnalyzer.
	 * </p>
	 * 
	 */
	private ArrayList<IAnalysisDocument> analysisDocuments;
	/**
	 * <p>
	 * An AtomicReference to the status to keep the thread from messing things
	 * up.
	 * </p>
	 * 
	 */
	private AtomicReference<FormStatus> concurrentStatus;
	/**
	 * <p>
	 * An AtomicReference to the Form used for processing on the thread.
	 * </p>
	 * 
	 */
	private AtomicReference<Form> concurrentForm;

	/**
	 * <p>
	 * The set of file extensions supported by the ReactorAnalyzer.
	 * </p>
	 * 
	 */
	private ArrayList<String> extensions;

	/**
	 * The component id for the DataComponent of input and reference sources.
	 */
	private static final int dataSourcesId = 1;

	/**
	 * The component id for the IReactorComponent component that holds analysis
	 * artifacts
	 */
	public static final int inputReactorComponentId = 1;

	/**
	 * The component id for the LWRComponent component that holds analysis
	 * artifacts
	 */
	public static final int referenceReactorComponentId = 2;

	/**
	 * The component ID for the ReactorComposite that will contain the input and
	 * reference reactor components. The composite will store the input and
	 * reference reactor components and associated them with
	 * {@link #inputReactorComponentId} and {@link #resourceCompId},
	 * respectively.
	 */
	public static final int reactorCompositeId = 4;

	/**
	 * The component id for the resource component that holds analysis artifacts
	 */
	private static final int resourceCompId = 2;

	/**
	 * The component id for the master details component that holds the analysis
	 * configuration
	 */
	private static final int analysisMDCompId = 3;

	/**
	 * A reference to the current input IReactorComponent. This may be out of
	 * sync with that in the ReactorComposite. If so, the KDD analysis tool will
	 * need to be updated. This is done when the form updates in
	 * {@link #reviewEntries(Form)}, which calls
	 * {@link #checkDataSource(String)}.
	 */
	private IReactorComponent inputReactorComponent = null;

	/**
	 * A reference to the current reference IReactorComponent. This may be out
	 * of sync with that in the ReactorComposite. If so, the KDD analysis tool
	 * will need to be updated. This is done when the form updates in
	 * {@link #reviewEntries(Form)}, which calls
	 * {@link #checkReferenceSource(String)}.
	 */
	private IReactorComponent referenceReactorComponent = null;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project used by the ReactorAnalyzer.
	 *            </p>
	 */
	public ReactorAnalyzer(IProject projectSpace) {

		// Call super's constructor
		super(projectSpace);

		// Initialize the status atomic
		concurrentStatus = new AtomicReference<FormStatus>();
		FormStatus formStatus = FormStatus.InfoError;
		concurrentStatus.set(formStatus);
		// Initialize the form atomic
		concurrentForm = new AtomicReference<Form>();

		return;

	}

	/**
	 * <p>
	 * An alternative nullary constructor used primarily for serialization. The
	 * setProject() operation must be called if this constructor is used!
	 * </p>
	 * 
	 */
	public ReactorAnalyzer() {

		// Punt to the other constructor.
		this(null);

	}

	/**
	 * <p>
	 * This operation sets the IAnalysisTools that are available to the
	 * ReactorAnalyzer.
	 * </p>
	 * 
	 * @param tools
	 *            <p>
	 *            The set of IAnalysisTools.
	 *            </p>
	 */
	public void setAnalysisTools(ArrayList<IAnalysisTool> tools) {

		// Only overwrite the tools if
		if (tools != null) {
			analysisTools = tools;
		}

	}

	/**
	 * <p>
	 * This operation overrides the default setupForm() operation on Item and
	 * creates a Form for the ReactorAnalyzer according to the specification on
	 * the class.
	 * </p>
	 * 
	 */
	@Override
	protected void setupForm() {

		// Set the name
		setName("Reactor Analyzer");
		// Set the Item type
		itemType = ItemType.AnalysisSession;
		// Set description
		setDescription("Review and analyze data from simluations.");

		// No special Form for this, just a regular one
		form = new Form();

		// Setup the lists for analysis pieces
		analysisTools = new ArrayList<IAnalysisTool>();
		analysisDocuments = new ArrayList<IAnalysisDocument>();

		// Setup the action list
		allowedActions.clear();
		allowedActions.add("Generate Analysis Artifacts");

		// Create the DataComponent that holds Data Sources
		DataComponent dataSourceComp = new DataComponent();
		dataSourceComp.setName("Data Sources");
		dataSourceComp.setId(dataSourcesId);
		dataSourceComp.setDescription("Select one of the available data "
				+ "sources from the list so that it can be analyzed.");

		// Create the Entry of input data sources
		final ArrayList<String> inputSources = getDataSources();
		Entry dataSourcesEntry = new Entry() {
			// Override setup to contain the allowed files from the project
			// space
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Input Data";
				this.uniqueId = 1;
				this.objectDescription = "The available input files.";
				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues = inputSources;
			}
		};
		// Create the Entry of reference input sources
		Entry referenceSourcesEntry = new Entry() {
			// Override setup to contain the allowed files from the project
			// space
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Reference Data";
				this.uniqueId = 2;
				this.objectDescription = "The available reference files to "
						+ "which the selected input is compared.";
				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("None");
				allowedValues.addAll(inputSources);
			}
		};
		// Add the Entries
		dataSourceComp.addEntry(dataSourcesEntry);
		dataSourceComp.addEntry(referenceSourcesEntry);
		// Add the component to the Form
		form.addComponent(dataSourceComp);

		// Create the ResourceComponent that holds the analysis assets
		ResourceComponent assetsComp = new ResourceComponent();
		assetsComp.setName("Analysis Artifacts");
		assetsComp.setId(resourceCompId);
		assetsComp.setDescription("This page shows all of the analysis "
				+ "artifacts that were generated from the output file.");
		// Add the assets components to the Form
		form.addComponent(assetsComp);

		// Add a ReactorComposite to the form. This Composite will contain the
		// input and reference reactor components and will key them on final IDs
		// rather than setting the component's IDs directly.
		ReactorComposite reactorComposite = new ReactorComposite();
		reactorComposite.setName("Reactor Components");
		reactorComposite.setId(reactorCompositeId);
		reactorComposite.setDescription("A group of reactor components "
				+ "containing the input and reference reactor.");
		form.addComponent(reactorComposite);

		// Configure the LWRComponent that shows a view of the Reactor if an ICE
		// Reactor file is loaded
		PressurizedWaterReactor inputReactor = new PressurizedWaterReactor(15);
		inputReactor.setName("Input");
		inputReactor.setId(inputReactorComponentId);
		inputReactor.setDescription("The reactor being analyzed.");
		// Add the reactor to the Form
		reactorComposite.setComponent(inputReactorComponentId, inputReactor);

		// Configure the LWRComponent that shows a view of the Reactor if an ICE
		// Reactor file is loaded
		PressurizedWaterReactor refReactor = new PressurizedWaterReactor(15);
		refReactor.setName("Reference");
		refReactor.setId(referenceReactorComponentId);
		refReactor
				.setDescription("A reference for the reactor being analyzed.");
		// Add the reactor to the Form
		reactorComposite.setComponent(referenceReactorComponentId, refReactor);

		// Configure the master details block
		configureMasterDetailsBlock();

		return;

	}

	/**
	 * <p>
	 * This operation overrides Item.reviewEntries() to customize the review
	 * process for the ReactorAnalyzer. It is primarily concerned with changes
	 * to the data source that should be analyzed and changes to the properties
	 * of the analysis artifacts. It delegates most of its work to the check*()
	 * set of operations.
	 * </p>
	 * 
	 * @param preparedForm
	 *            <p>
	 *            The Form input from a client.
	 *            </p>
	 * @return <p>
	 *         The status.
	 *         </p>
	 */
	protected FormStatus reviewEntries(Form preparedForm) {

		// Local Declarations
		FormStatus reviewStatus = FormStatus.InfoError;
		boolean checkStatus = false;

		try {

			// Check the input and reference data.
			checkStatus = checkDataSource(null);
			checkStatus &= checkReferenceSource(null);

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
	}

	/**
	 * <p>
	 * This operation checks the data source on the Form to determine if it has
	 * been set or changed from the previous value. If the value has changed
	 * (either case), then the MasterDetailsComponent is reconfigured to contain
	 * the proper list of available analysis types for the new data source.
	 * </p>
	 * 
	 * @param source
	 *            <p>
	 *            The name of the data source to compare to the last one.
	 *            </p>
	 * @return <p>
	 *         True if the operation was successful, false if not.
	 *         </p>
	 * @throws RuntimeException
	 */
	protected boolean checkDataSource(String source) throws RuntimeException {

		// Get the input reactor from the ReactorComposite.
		ReactorComposite composite = (ReactorComposite) form
				.getComponent(reactorCompositeId);
		IReactorComponent inputReactor = composite
				.getReactorComponent(inputReactorComponentId);

		// If it's valid and if it has changed, we need to update the KDD with
		// the new IDataProvider.
		if (inputReactor != null && inputReactor != inputReactorComponent) {
			inputReactorComponent = inputReactor;

			// FIXME - We should probably change the search to look for
			// an ID instead of a String.
			// Get documents for the new data source
			for (IAnalysisTool tool : analysisTools) {
				if ("Knowledge Discovery and Data Mining ICE Analysis Tool"
						.equals(tool.getName())
						&& inputReactor instanceof IDataProvider) {
					analysisDocuments
							.add(tool
									.createDocument(getKDDIDataProvider((IDataProvider) inputReactor)));
				}
			}

			// Reconfigure the master details component
			configureMasterDetailsBlock();
		}

		return true;
	}

	/**
	 * <p>
	 * This operation checks the properties of analysis artifacts, which are
	 * ICEResources, in the ResourceComponent. If they have changed from the
	 * properties that are currently stored for the IAnalysisAsset, then the
	 * properties are updated on the asset and the asset itself is updated.
	 * </p>
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
	 */
	protected boolean checkArtifactProperties(ArrayList<ICEResource> resources)
			throws RuntimeException {

		return true;

	}

	/**
	 * <p>
	 * This operation updates global parameters for the assets.
	 * </p>
	 * 
	 */
	protected void updateGlobalParametersMap() {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * This operation configures the MasterDetailsComponent that is used by the
	 * ReactorAnalyzer to expose analysis asset types and the properties
	 * available for each type. It is updated each time the data source is
	 * changed.
	 * </p>
	 * 
	 * @throws RuntimeException
	 */
	protected void configureMasterDetailsBlock() throws RuntimeException {

		// Local Declarations
		int pairId = 0;
		ArrayList<MasterDetailsPair> templatePairs = null;
		MasterDetailsComponent oldComp = null;

		// Setup a list to store master-details pairs
		templatePairs = new ArrayList<MasterDetailsPair>();

		// Get ahold of the master details component from the Form if it already
		// exists
		if (form.getComponent(analysisMDCompId) != null) {
			oldComp = (MasterDetailsComponent) form
					.getComponent(analysisMDCompId);
		}

		// Create the MasterDetailsComponent that is used to hold the available
		// analysis types
		MasterDetailsComponent analysisConfigComp = new MasterDetailsComponent();
		analysisConfigComp.setName("Analysis Configuration");
		analysisConfigComp.setId(analysisMDCompId);
		analysisConfigComp
				.setDescription("Select analysis artifacts to be created "
						+ "from the data and examine properties for each analysis type.");

		// If there are analysis documents, configure the master values and
		// details blocks
		if (!(analysisDocuments.isEmpty())) {
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
					for (String assetName : doc.getAvailableAssets()) {
						// Create a master-details pair
						MasterDetailsPair pair = new MasterDetailsPair();
						// Add the generic details block
						pair.setDetails(detailsBlock);
						// Add the asset type as the master value
						pair.setMaster(assetName);
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

	}

	/**
	 * <p>
	 * This operation overrides the operation on the Item base class to generate
	 * analysis artifacts. The only valid action name is
	 * "Generate Analysis Artifacts."
	 * </p>
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of the action. In the case of the Reactor Analyzer,
	 *            the only valid value is currently
	 *            "Generate Analysis Artifacts."
	 *            </p>
	 * @return <p>
	 *         The status of the processing attempt.
	 *         </p>
	 */
	public FormStatus process(String actionName) {

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
		} else {
			return FormStatus.InfoError;
		}

		return status;
	}

	/**
	 * <p>
	 * This operation checks the status of the ReactorAnalyzer and returns true
	 * if it can be processed safely.
	 * </p>
	 * 
	 * @return <p>
	 *         True if the ReactorAnalyzer is in either the ReadyToProcess or
	 *         Processed states, false otherwise.
	 *         </p>
	 */
	private boolean checkProcessingStatus() {
		return false;
	}

	/**
	 * <p>
	 * This operation creates a list of data sources based on the contents of
	 * the workspace. At the moment, it only finds .silo files.
	 * </p>
	 * 
	 * @return <p>
	 *         The list of file names of the available data sources.
	 *         </p>
	 */
	private ArrayList<String> getDataSources() {

		// Local Declarations
		ArrayList<String> dataSources = new ArrayList<String>();

		if (extensions == null) {
			// Setup the allowed file extensions
			extensions = new ArrayList<String>();
			extensions.add("silo");
			extensions.add("e");
			extensions.add("h5");
		}

		// Only load the list if nothing goes nuts
		if (project != null) {
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
		}

		return dataSources;
	}

	/**
	 * <p>
	 * This operation functions as Item.getStatus(), but is overridden to handle
	 * concurrency within the ReactorAnalyzer.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public FormStatus getStatus() {

		// Pass to super.getStatus() if the thread isn't involved
		if (status != FormStatus.Processing) {
			return super.getStatus();
		} else {
			status = concurrentStatus.get();
			return status;
		}

	}

	/**
	 * <p>
	 * Given a general IDataProvider, attempt to construct an IDataProvider
	 * required by the KDD Analysis Tool. If unsuccessful, return null.
	 * </p>
	 * 
	 * @param generalProvider
	 * @return
	 */
	private IDataProvider getKDDIDataProvider(IDataProvider generalProvider) {

		// FIXME - Temporary solution to keep Java from complaining when we have
		// an SFReactor.
		if (!(generalProvider instanceof PressurizedWaterReactor)) {
			return new LWRDataProvider();
		}
		// I can expect this to be a PressurizedWaterReactor...
		PressurizedWaterReactor reactor = (PressurizedWaterReactor) generalProvider;

		// Local Declarations
		int totalNAssemblies = 0, nRows = 0, nCols = 0, nAxial = 49;
		int nAssemblies = 0;
		FuelAssembly assembly = null;
		ArrayList<IData> dataElements = new ArrayList<IData>();
		LWRDataProvider provider = new LWRDataProvider();
		LWRData temp = null;

		// Get the total number of possible assemblies,
		// each could be null, but this is the size of the
		// fuel assembly grid
		totalNAssemblies = reactor.getSize();

		// Loop over all the possible fuel assembly locations
		for (int i = 0; i < totalNAssemblies; i++) {
			for (int j = 0; j < totalNAssemblies; j++) {
				// Get the assembly at location i,j
				assembly = (FuelAssembly) reactor.getAssemblyByLocation(
						AssemblyType.Fuel, i, j);
				// It could be null, so make sure its not
				if (assembly != null) {
					// We've found a valid assembly,
					// so increment the counter.
					nAssemblies++;

					// This assembly's get size should
					// be the number of rows and columns
					nRows = assembly.getSize();
					nCols = assembly.getSize();

					// Loop over the possible pin rows and columns
					for (int k = 0; k < nRows; k++) {
						for (int l = 0; l < nCols; l++) {

							// Get the LWRRod IDataProvider at the grid
							// position k, l. Could be null if this is a
							// control rod or something else
							IDataProvider prov = assembly
									.getLWRRodDataProviderAtLocation(k, l);
							if (prov != null
									&& prov.getFeatureList().contains(
											"Axial Pin Power")) {
								// Since there was a rod here, take the Axial
								// Pin Power
								// data and add it to the list of elements
								dataElements
										.addAll(prov
												.getDataAtCurrentTime("Axial Pin Power"));
								// Get the number of axial levels,
								// I am assuming its the same throughout
								nAxial = prov.getDataAtCurrentTime(
										"Axial Pin Power").size();
							} else {
								// If we had no fuel rod at k,l then
								// we should add an empty IData element
								temp = new LWRData("Data");
								temp.setValue(0.0);
								temp.setUncertainty(0.0);
								// Add nAxial of them to simulate an empty
								// fuel rod spot
								for (int ii = 0; ii < nAxial; ii++) {
									dataElements.add(temp);
								}

							}
						}
					}
				} else {
				}

			}
		}

		// Add all the IData elements to the
		// IDataProvider we are returning
		for (IData d : dataElements) {
			temp = new LWRData("Data");
			temp.setValue(d.getValue());
			temp.setUncertainty(d.getUncertainty());
			provider.addData(temp, 0.0);
		}

		// KDD needs to know the number of rows
		temp = new LWRData("Number of Rows");
		temp.setValue(nRows);
		provider.addData(temp, 0.0);

		// KDD needs to know the number of columns
		temp = new LWRData("Number of Columns");
		temp.setValue(nRows);
		provider.addData(temp, 0.0);

		// KDD needs to know the number of assemblies
		temp = new LWRData("Number of Assemblies");
		temp.setValue(nAssemblies);
		provider.addData(temp, 0.0);

		// Return the provider

		return provider;
	}

	/**
	 * <p>
	 * This operation checks the reference data source on the Form to determine
	 * if it has been set or changed from the previous value. If the value has
	 * changed (either case), then the MasterDetailsComponent is reconfigured to
	 * contain the proper list of available analysis types for the new reference
	 * data source.
	 * </p>
	 * 
	 * @param source
	 * @return
	 */
	protected boolean checkReferenceSource(String source) {

		// Get the reference reactor from the ReactorComposite.
		ReactorComposite composite = (ReactorComposite) form
				.getComponent(reactorCompositeId);
		IReactorComponent referenceReactor = composite
				.getReactorComponent(referenceReactorComponentId);

		// If it's valid and if it has changed, we need to update the KDD with
		// the new IDataProvider.
		if (referenceReactor != null
				&& referenceReactor != referenceReactorComponent) {
			referenceReactorComponent = referenceReactor;

			// FIXME - We should probably change the search to look for
			// an ID instead of a String.
			// Get documents for the new data source
			for (IAnalysisTool tool : analysisTools) {
				if ("Knowledge Discovery and Data Mining ICE Analysis Tool"
						.equals(tool.getName())
						&& referenceReactor instanceof IDataProvider
						&& !(analysisDocuments.isEmpty())
						&& analysisDocuments.get(0) != null) {
					analysisDocuments
							.get(0)
							.loadReferenceData(
									getKDDIDataProvider((IDataProvider) referenceReactor));
				}
			}

			// Reconfigure the master details component
			configureMasterDetailsBlock();
		}

		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Runnable#run()
	 */
	public void run() {

		// Get the MasterDetailsComponent component from the concurrentForm
		MasterDetailsComponent analysisConfigComp = (MasterDetailsComponent) concurrentForm
				.get().getComponent(3);

		// Create an ArrayList to hold the selected assets
		ArrayList<String> selectedAssets = new ArrayList<String>();

		// Loop over all the MasterDetailsPairs
		for (int i = 0; i < analysisConfigComp.numberOfMasters(); i++) {

			// Add the value to the selected assets list
			selectedAssets.add(analysisConfigComp.getMasterAtIndex(i));

		}

		// Get the first document in the analysisDocuments list
		// since we will only have a KDDAnalysisDocument to access initially
		IAnalysisDocument document = analysisDocuments.get(0);

		// Set the selected assets on the document
		document.setSelectedAssets(selectedAssets);

		// Create the assets
		document.createSelectedAssets();

		// Get all of the selected assets
		ArrayList<IAnalysisAsset> assets = document.getAllAssets();

		// Get the ResourceComponent from the concurrentForm
		ResourceComponent resourceComponent = (ResourceComponent) concurrentForm
				.get().getComponent(resourceCompId);

		// Check for any resources currently stored in the resourceComponent
		if (!(resourceComponent.getResources().isEmpty())) {

			// Clear those resources
			resourceComponent.clearResources();

		}

		// Initialize a resource id counter
		int resourceId = 0;

		// Loop over all assets in the asset array list
		if (!assets.isEmpty()) {
			for (IAnalysisAsset asset : assets) {

				// This is processed for every item in the ICE resource view.

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

				// Add the resource to the resourceComponent
				resourceComponent.addResource(resource);

				// Increment the resourceId
				resourceId++;

			}
		}
		// Set the status
		concurrentStatus.set(FormStatus.Processed);

		// Assign the concurrentForm reference to form.
		form = concurrentForm.get();

		return;

	}

}