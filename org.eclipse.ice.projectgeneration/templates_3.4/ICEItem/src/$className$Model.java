package $packageName$.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.*;
import org.eclipse.ice.datastructures.entry.*;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.model.Model;


@XmlRootElement(name = "$className$Model")
public class $className$Model extends Model {

	private String ioFormat;
	private String outputName;
	private String defaultFileName;
	
    private String exportString;
	private IIOService ioService;
    private IReader reader;
    private IWriter writer;
    
    /**
     * The Constructor
     */
	public $className$Model() {
		this(null);
	}

	/**
	 * The Constructor, takes an IProject reference. 
	 * 
	 * @param project The project space this Item will be in.
	 */
	public $className$Model(IProject project) {
		super(project);
	}

	/**
	 * Sets the name, description, and custom action name 
	 * for the item.
	 */
	@Override
	protected void setupItemInfo() {
		setName("$className$ Model");
		setDescription("Specify information about $className$");
		outputName = "$className$DefaultOutputName";   
		exportString = "Export to $className$ input format";
		allowedActions.add(0, exportString);
		ioFormat = "$ioFormat$";
		defaultFileName = "$defaultFileName$";
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
		if (ioService == null) {
			setIOService(new IOService());
			ioService = getIOService();
		}
		if (ioFormat != "") {
			reader = ioService.getReader(ioFormat);
			writer = ioService.getWriter(ioFormat);
		}
		
		// Populate the Form with Components for your 
		// application Model. The default behavior is 
		// only invoked if there are valid IO services
		if (project != null && reader != null && writer != null) {
			loadInput(null);
		}
		
		/* Alternate Example:
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
		IFile inputFile = null;
		File temp = null;
		if (fileName == null) {
			try {
				// Create a filepath for the default file
				fileName = Paths.get(defaultFileName).getFileName().toString();
				if (fileName.isEmpty()) {
					return;
				}
				String defaultFilePath = project.getLocation().toOSString()
							+ System.getProperty("file.separator")
							+ fileName;			
				// Create a temporary location to load the default file
				temp = new File(defaultFilePath);
				if (!temp.exists()) {
					temp.createNewFile();
				}
				
				// Pull the default file from inside the plugin
				URI uri = new URI(
						"platform:/plugin/$packageName$/data/" + fileName);
				InputStream reader = uri.toURL().openStream();
				FileOutputStream outStream = new FileOutputStream(temp);

				// Write out the default file from the plugin to the temp location
				int fileByte;
				while ((fileByte = reader.read()) != -1) {
					outStream.write(fileByte);
				}
				outStream.close();
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
				inputFile = project.getFile(fileName);

			} catch (URISyntaxException e) {
				logger.error(getClass().getName() + " Exception!",e);
			} catch (MalformedURLException e) {
				logger.error(getClass().getName() + " Exception!",e);
			} catch (IOException e) {
				logger.error(getClass().getName() + " Exception!",e);
			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!",e);
			}
		} else {
			// Get the file
			inputFile = project.getFile(fileName);
		}
	
		form = reader.read(inputFile);
		form.setName(getName());
		form.setDescription(getDescription());
		form.setId(getId());
		form.setItemID(getId());
	}
}
