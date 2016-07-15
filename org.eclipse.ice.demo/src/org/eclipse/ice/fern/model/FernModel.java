package org.eclipse.ice.fern.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.*;
import org.eclipse.ice.datastructures.entry.*;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.model.Model;

@XmlRootElement(name = "FernModel")
public class FernModel extends Model {

	// TODO: 
	//   These need to be filled in before using this item
	//   They can be set in the setupItemInfo() method
	private String writerName;
	private String readerName;
	private String outputName;
	// End required variables
	
    private String exportString;
	private IIOService ioService;
    private IReader reader;
    private IWriter writer;
    
    /**
     * The Constructor
     */
	public FernModel() {
		this(null);
	}

	/**
	 * The Constructor, takes an IProject reference. 
	 * 
	 * @param project The project space this Item will be in.
	 */
	public FernModel(IProject project) {
		super(project);
	}

	/**
	 * Sets the name, description, and custom action name 
	 * for the item.
	 */
	@Override
	protected void setupItemInfo() {
		setName("Fern Model");
		setDescription("This Item constructs input files for the FERN reaction network solver");
		writerName = "INIWriter";
		readerName = "INIReader";
		outputName = "fern_config.ini";
		exportString = "Export to INI";
		allowedActions.add(0, exportString);
	}

	/**
	 * Adds relevant information that specify the ui provided
	 * to the user when they create the Fern Model Item
	 * in ICE.  
	 */
	@Override
	public void setupForm() {
		form = new Form();
		
		// Get reference to the IOService
		// This will let us get IReader/IWriters for 
		// our specific Model
		ioService = getIOService();

		// Create the network section
		DataComponent networkComp = new DataComponent();
		networkComp.setName("network");
		networkComp.setDescription("The parameters needed " + "to describe the nuclear " + "reaction network");
		networkComp.setId(1);
		
		// Create the IEntries we need for this DataComponent
		StringEntry numSpecies = new StringEntry();
		numSpecies.setName("numSpecies");
		numSpecies.setDescription("The number of species to consider");
		numSpecies.setDefaultValue("16");
		
		StringEntry numReactions = new StringEntry();
		numReactions.setName("numReactions");
		numReactions.setDescription("The number of reactions to consider");
		numReactions.setDefaultValue("48");
		
		StringEntry numReactionGrps = new StringEntry();
		numReactionGrps.setName("numReactionsGroups");
		numReactionGrps.setDescription("The number of reaction " + "groups to consider");
		numReactionGrps.setDefaultValue("19");
		
		StringEntry massTol = new StringEntry();
		massTol.setName("massTol");
		massTol.setDescription("The mass tolerance to consider");
		massTol.setDefaultValue("1.0e-7");

		StringEntry fluxFrac = new StringEntry();
		fluxFrac.setName("fluxFrac");
		fluxFrac.setDescription("The flux fraction to consider");
		fluxFrac.setDefaultValue(".01");
		
		FileEntry networkFile = new FileEntry(".inp");
		networkFile.setProject(project);
		networkFile.setName("networkFile");
		networkFile.setDescription("The network file for this problem");
		
		FileEntry rateFile = new FileEntry(".data");
		rateFile.setProject(project);
		rateFile.setName("rateFile");
		rateFile.setDescription("The rate file for this problem");
		
		// Add the entries to the DataComponent!
		networkComp.addEntry(numSpecies);
		networkComp.addEntry(numReactions);
		networkComp.addEntry(numReactionGrps);
		networkComp.addEntry(massTol);
		networkComp.addEntry(fluxFrac);
		networkComp.addEntry(networkFile);
		networkComp.addEntry(rateFile);
		
		// Create the initial conditions section
		DataComponent initConditionsComp = new DataComponent();
		initConditionsComp.setName("initialConditions");
		initConditionsComp.setId(2);
		initConditionsComp
				.setDescription("The parameters " + "needed to describe the initial " + "conditions for the problem");
		
		StringEntry t9 = new StringEntry();
		t9.setName("T9");
		t9.setDescription("The temperature in Kelvin x 10^9");
		t9.setDefaultValue("7.0");
		
		StringEntry startTime = new StringEntry();
		startTime.setName("startTime");
		startTime.setDescription("The start time for the simulation.");
		startTime.setDefaultValue("1.0e-20");
		
		StringEntry endTime = new StringEntry();
		endTime.setName("endTime");
		endTime.setDescription("The end time for the simulation");
		endTime.setDefaultValue("1.0e-8");

		StringEntry initialTimeStep = new StringEntry();
		initialTimeStep.setName("initialTimeStep");
		initialTimeStep.setDescription("The initial time step " + "for the simulation.");
		initialTimeStep.setDefaultValue("1.2345e-22");
		
		StringEntry density = new StringEntry();
		density.setName("density");
		density.setDescription("The initial density.");
		density.setDefaultValue("1.0e8");
		
		// Add the entries to the DataComponent!
		initConditionsComp.addEntry(t9);
		initConditionsComp.addEntry(startTime);
		initConditionsComp.addEntry(endTime);
		initConditionsComp.addEntry(initialTimeStep);
		initConditionsComp.addEntry(density);

		DataComponent outputComp = new DataComponent();
		outputComp.setName("output");
		outputComp.setDescription("The parameters needed to output data.");
		outputComp.setId(3);

		StringEntry popFile = new StringEntry();
		popFile.setName("popFile");
		popFile.setDescription("The name of the output populations file");
		popFile.setDefaultValue("popFile.csv");
		    
		outputComp.addEntry(popFile);
		
		// Add the components to the Form
		form.addComponent(networkComp);
		form.addComponent(initConditionsComp);
	    form.addComponent(outputComp);

		// Set the Form ID info
		form.setName(getName());
		form.setDescription(getDescription());
		form.setId(getId());
		form.setItemID(getId());

	}
	
	/**
	 * The reviewEntries method is used to ensure that the form is 
	 * in an acceptable state before processing the information it
	 * contains.  If the form is not ready to process it is advisable
	 * to have this method return FormStatus.InfoError.
	 * 
	 * @param preparedForm
	 *		the form to validate 
	 * @return whether the form was correctly set up
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {
		FormStatus retStatus = FormStatus.ReadyToProcess;
		
		// Here you can add code that checks the Entries in the Form 
		// after the user clicks Save. If there are any errors in the 
		// Entry values, return FormStatus.InfoError. Otherwise 
		// return FormStatus.ReadyToProcess.
		
		return retStatus;
	}

	/**
	 * Use this method to process the data that has been 
	 * specified in the form. 
	 * 
	 * @param actionName
	 * 		a string representation of the action to perform
	 * @return whether the form was processed successfully
	 */
	@Override
	public FormStatus process(String actionName) {
		FormStatus retStatus = FormStatus.ReadyToProcess;
		
		// This action occurs only when the default processing option is chosen
		// The default processing option is defined in the last line of the 
		// setupItemInfo() method defined above.
		if (actionName == exportString) {
			IFile outputFile = project.getFile(outputName);
			writer = ioService.getWriter(writerName);
			if (writer != null) {
				retStatus = FormStatus.Processing;
				writer.write(form, outputFile);
				refreshProjectSpace();
				retStatus = FormStatus.Processed;
			} else {
				logger.error("Could not get reference to the IWriter " + writerName);
				retStatus = FormStatus.InfoError;
			}
		} else {
			retStatus = super.process(actionName);
		}
		
		// TODO: Add User Code Here
		
		return retStatus;
	}

	/**
	 * This method is called when loading a new item either via the item 
	 * creation button or through importing a file associated with this
	 * item.  It is responsible for setting up the form for user interaction.
	 *  
	 * @param fileName
	 * 		the file to load
	 */
	@Override
	public void loadInput(String fileName) {

		// Read in the file and set up the form
		IFile inputFile = project.getFile(fileName);
		reader = ioService.getReader(readerName);
		form = reader.read(inputFile);
		form.setName(getName());
		form.setDescription(getDescription());
		form.setId(getId());
		form.setItemID(getId());

	}
}
