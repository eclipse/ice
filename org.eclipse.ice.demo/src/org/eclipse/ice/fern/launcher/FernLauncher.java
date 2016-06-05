package org.eclipse.ice.fern.launcher;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;

import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.january.form.AllowedValueType;
import org.eclipse.january.form.FormStatus;
import org.eclipse.january.form.IEntry;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IReader;

@XmlRootElement(name = "FernLauncher")
public class FernLauncher extends JobLauncher {

	// TODO: 
	//   These need to be filled in before using this item
	//	 They can be set in the setupItemInfo() method
	private String execCommand;
	// End required variables
	
	private IIOService ioService;
	private IReader reader;

	/**
	 * Nullary constructory
	 */
	public FernLauncher() {
		this(null);
	}

	/**
	 * Main constructor
	 */
	public FernLauncher(IProject project) {
		super(project);
	}

	/**
	 * Sets the name and description for the item.  This will
	 * have to be updated so that the execCommand is set correctly 
	 */
	@Override
	protected void setupItemInfo() {
		setName("Fern Launcher");
		setDescription("Provide information to launch Fern");
		// TODO: These must be customized before using this item
		execCommand = "${installDir}fern-exec";
		// End required variables
	}

	/**
	 * Sets a default host, architecture, and path.  Can be edited here
	 * or changed on the fly when a new Fern item is created
	 * via the ICE perspective.  Other forms of custom logic for the 
	 * launcher should be implemented here, although they are compeletely 
	 * optional.
	 */
	@Override
	public void setupForm() {
		super.setupForm();
		setAppendInputFlag(false);
		addHost("localhost", "linux x86_64", "/usr/bin");
		
		// TODO: (Optional) Add User Code Here
		addInputType("Network File", "networkFile", "Network File Description", ".inp");
		addInputType("Rate File", "rateFile", "Rate File Description", ".data");

	}

	/**
	 * Complete the launch command
	 * 
	 * @param actionName
	 * 		The action to take when processing
	 * @return 
	 * 		Whether the form was valid and successful in processing
	 */
	@Override
	public FormStatus process(String actionName) {
		setExecutable(getName(), getDescription(), execCommand);
		return super.process(actionName);
	}
}