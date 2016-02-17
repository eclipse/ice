package $packageName$.launcher;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;

import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.serializable.IIOService;

@XmlRootElement(name = "$className$Launcher")
public class $className$Launcher extends JobLauncher {

	private String fullExecCMD;

	private IIOService ioService;

	public $className$Launcher() {
		this(null);
	}

	public $className$Launcher(IProject project) {

		// Call the JobLauncher constructor
		super(project);

		// TODO: Add User Code Here
	}

	@Override
	protected void setupItemInfo() {
		// TODO: Add User Code Here
	}

	@Override
	public void setupForm() {
		// TODO: Add User Code Here
	}

	@Override
	public FormStatus process(String actionName) {
		// TODO: Add User Code Here
		return null;
	}
}