package $packageName$.launcher;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;

import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IReader;

@XmlRootElement(name = "$className$Launcher")
public class $className$Launcher extends JobLauncher {

	// TODO: 
	//   These need to be filled in before using this item
	//	 They can be set in the $className$Model(IProject) method
	private String execCommand;
	// End required variables
	
	private IIOService ioService;
	private IReader reader;

	/**
	 * Nullary constructory
	 */
	public $className$Launcher() {
		this(null);
	}

	/**
	 * Main constructor
	 * 
	 * Developer is required to update the class variables that are
	 * 
	 */
	public $className$Launcher(IProject project) {
		super(project);

		// TODO: These must be customized before using this item
		execCommand = "$className$DefaultExecCommand";
		// End required variables
			
		// TODO: Add User Code Here
	}

	/**
	 * Sets the name and description for the item. 
	 */
	@Override
	protected void setupItemInfo() {
		setName("$className$ Launcher");
		setDescription("Provide information to launch $className$");
	}

	/**
	 * Sets a default host, architecture, and path.  Can be edited here
	 * or changed on the fly when a new $className$ item is created
	 * via the ICE perspective.  Other forms of custom logic for the 
	 * launcher should be implemented here, although they are compeletely 
	 * optional.
	 */
	@Override
	public void setupForm() {
		super.setupForm();
		setAppendInputFlag(false);
		addHost("localhost", "linux x86_64", "/home/user/$className$");
		
		// TODO: (Optional) Add User Code Here
	}

	/**
	 *
	 * 
	 * @param actionName
	 * 		The action to take when processing
	 * @return 
	 * 		Whether the form was valid and successful in processing
	 */
	@Override
	public FormStatus process(String actionName) {
		FormStatus retStatus = FormStatus.ReadyToProcess;
	
		// Make sure that the launcher code has been updated
		if (execCommand == "$className$DefaultExecCommand") {
			return FormStatus.InfoError;
		}
		
		setExecutable(getName(), getDescription(), execCommand);
		retStatus = super.process(actionName);
		
		return retStatus;
	}
}