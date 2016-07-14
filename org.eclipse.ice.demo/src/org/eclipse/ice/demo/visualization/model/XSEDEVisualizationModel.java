package org.eclipse.ice.demo.visualization.model;

import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.eavp.viz.modeling.Shape;
import org.eclipse.eavp.viz.modeling.ShapeController;
import org.eclipse.eavp.viz.modeling.base.BasicView;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.model.Model;

@XmlRootElement(name = "VisualizationCompleteModel")
public class XSEDEVisualizationModel extends Model {

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
	public XSEDEVisualizationModel() {
		this(null);
	}

	/**
	 * The Constructor, takes an IProject reference. 
	 * 
	 * @param project The project space this Item will be in.
	 */
	public XSEDEVisualizationModel(IProject project) {
		super(project);
	}

	/**
	 * Sets the name, description, and custom action name 
	 * for the item.
	 */
	@Override
	protected void setupItemInfo() {
		setName("VisualizationComplete Model");
		setDescription("Specify information about VisualizationComplete");
		writerName = "VisualizationCompleteDefaultWriterName";
		readerName = "VisualizationCompleteDefaultReaderName";     	
		outputName = "VisualizationCompleteDefaultOutputName";   
		exportString = "Export to VisualizationComplete input format";
		allowedActions.add(0, exportString);
	}

	/**
	 * Adds relevant information that specify the ui provided
	 * to the user when they create the VisualizationComplete Model Item
	 * in ICE.  
	 */
	@Override
	public void setupForm() {
		form = new Form();
		
		// Get reference to the IOService
		// This will let us get IReader/IWriters for 
		// our specific Model
		ioService = getIOService();

		/**
		 * Begin copying here.
		 */
		
		//Create the resource component
		ResourceComponent resourceComponent = new ResourceComponent();

		//Set the component's data members
		resourceComponent.setName("Resource Component");
		resourceComponent.setDescription("Results");
		resourceComponent.setId(1);

		//Declare the files and resources
		VizResource visItResource = null;
		IFile visItFile = null;


		//If the file was found, create the resource and add it to the component
		try{
			
			//Open the files
			visItFile = ResourcesPlugin.getWorkspace().getRoot().getProject("itemDB").getFile("tire.silo");
				        
			//If the file was found, create the VisIt resource and add it to 
			//the component
			if(visItFile.exists()){
				visItResource = new 
		                    VizResource(visItFile.getLocation()
		                    .toFile());
				resourceComponent.addResource(visItResource);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		//Create the geometry component
		ShapeController geometryRoot = new ShapeController(new
		    Shape(), new BasicView());
		GeometryComponent geometryComponent = new 
		    GeometryComponent();
		geometryComponent.setGeometry(geometryRoot);
		geometryComponent.setName("Geometry Editor");

		//Create mesh component
		MeshComponent meshComponent = new MeshComponent();
		meshComponent.setName("Mesh Editor");

		//Add the components to the form
		form.addComponent(resourceComponent);
		form.addComponent(geometryComponent);
		form.addComponent(meshComponent);	
		
		// Set the context on the Form
		form.setContext("visualization");
		
		/**
		 * End copying here.
		 */
		
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
			retStatus = FormStatus.Processing;
			writer.write(form, outputFile);
			refreshProjectSpace();
			retStatus = FormStatus.Processed;
		} else {
			retStatus = super.process(actionName);
		}
		
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
