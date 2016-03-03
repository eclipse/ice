package $packageName$.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.model.Model;

@XmlRootElement(name = "$className$Model")
public class $className$Model extends Model {

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
    
	public $className$Model() {
		this(null);
	}

	public $className$Model(IProject project) {
		super(project);
	}

	/**
	 * Adds relevant information that specify the ui provided
	 * to the user when they create the $className$ Model Item
	 * in ICE.  
	 */
	@Override
	public void setupForm() {
		form = new Form();
		
		// Get reference to the IOService
		// This will let us get IReader/IWriters for 
		// our specific Model
		ioService = getIOService();

		// Populate the Form with Components for your 
		// application Model.
		/* Example:
		 * 
		 * DataComponent data = new DataComponent();
		 * data.setName("Example Input Data");
		 * data.setDescription("DataComponents let you expose Entries for user input.");
		 * data.setId(1);
		 * 
		 * IEntry inputVal1 = new StringEntry();
		 * inputVal1.setName("Input 1");
		 * inputVal1.setDescription("A description for this user input.");
		 * inputVal1.setId(1);
		 * 
		 * IEntry inputVal2 = new DiscreteEntry("allowedVal1", "allowedVal2");
		 * inputVal2.setName("Input 2");
		 * inputVal2.setDescription("A description for this user input - 
		 * 							it shows a drop down of discrete values.");
		 * inputVal2.setId(1);
		 * 
		 * data.addEntry(inputVal1);
		 * data.addEntry(inputVal2);
		 * 
		 * form.addComponent(data);
		 */
		
	}
	
	/**
	 * Sets the name, description, and custom action name 
	 * for the item.
	 */
	@Override
	protected void setupItemInfo() {
		setName("$className$ Model");
		setDescription("Specify information about $className$");
		writerName = "$className$DefaultWriterName";
		readerName = "$className$DefaultReaderName";     	
		outputName = "$className$DefaultOutputName";   
		exportString = "Export to $className$ input format";
		allowedActions.add(0, exportString);
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
		
		// Check to make sure that the item code has been filled in properly
		// Before going further make sure that the top three variables are 
		// customized to the appropriate values for your new item.
		if (writerName == "$className$DefaultWriterName" || 
				outputName == "$className$DefaultOutputName") {
			return FormStatus.InfoError;
		}
		
		// This action occurs only when the default processing option is chosen
		// The default processing option is defined in the last line of the 
		// setupItemInfo() method defined above.
		if (actionName == exportString) {
			IFile outputFile = project.getFile(outputName);
			writer = ioService.getWriter(writerName); 
			try {
				retStatus = FormStatus.Processing;
				writer.write(form, outputFile);
				project.refreshLocal(1, null);
				retStatus = FormStatus.Processed;
			} catch (CoreException e) {
				logger.error(getClass().getName() + " CoreException!", e);
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
