package org.eclipse.ice.demo.visualization.model;

import java.io.IOException;

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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.eavp.viz.modeling.ShapeController;
import org.eclipse.eavp.viz.modeling.Shape;
import org.eclipse.eavp.viz.modeling.base.BasicView;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.VizResource;

@XmlRootElement(name = "VisualizationModel")
public class VisualizationModel extends Model {

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
	public VisualizationModel() {
		this(null);
	}

	/**
	 * The Constructor, takes an IProject reference. 
	 * 
	 * @param project The project space this Item will be in.
	 */
	public VisualizationModel(IProject project) {
		super(project);
	}

	/**
	 * Sets the name, description, and custom action name 
	 * for the item.
	 */
	@Override
	protected void setupItemInfo() {
		setName("Visualization Model");
		setDescription("Specify information about Visualization");
		writerName = "VisualizationDefaultWriterName";
		readerName = "VisualizationDefaultReaderName";     	
		outputName = "VisualizationDefaultOutputName";   
		exportString = "Export to Visualization input format";
		allowedActions.add(0, exportString);
	}

	/**
	 * Adds relevant information that specify the ui provided
	 * to the user when they create the Visualization Model Item
	 * in ICE.  
	 */
	@Override
	public void setupForm() {
		form = new Form();
		
		// Get reference to the IOService
		// This will let us get IReader/IWriters for 
		// our specific Model
		ioService = getIOService();

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
