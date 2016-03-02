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
		
		ioService = getIOService();
		// TODO: Add User Code Here
		
		/**
		 * The following two lines of code can be changed
		 * if there is supposed to be some default information
		 * populated in the form when creating this item from
		 * the new item button in the ICE perspective.
		 * 
		 * Additionally, you will have to add code to the 
		 * loadInput() method so that it will correctly handle 
		 * a null argument.
		 */
		if (project != null) { 
			loadInput(null);
		}
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
		
		// TODO: Add User Code Here
		
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
		
		// Check to make sure that the item code has been filled in properly
		// Before going further make sure that the top three variables are 
		// customized to the appropriate values for your new item.
		if (readerName == "$className$DefaultReaderName") {
			return;
		}

		if (fileName == null) {
			// TODO: Add User Code Here
			return;
		} else {
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
	
	/**
	 * Creates an appropriate entry type with some initial setup.
	 * 
	 * @param name 
	 * 		The name to display
	 * @param value 
	 * 		The default value
	 * @param entryType 
	 * 		The type of entry to use
	 * @return the constructed entry
	 */
	public IEntry createNumEntry(String name, String value, AllowedValueType entryType) {
	    IEntry entry = null;
	    if (entryType == AllowedValueType.Continuous) {
	        entry = new ContinuousEntry();
	    } else if (entryType = AllowedValueType.Undefined) {
	        entry = new StringEntry();
	    } else {
	        entry = new StringEntry();
	    }
	    entry.setName(name);
	    entry.setValue(value);
	    return entry;
	}
}
