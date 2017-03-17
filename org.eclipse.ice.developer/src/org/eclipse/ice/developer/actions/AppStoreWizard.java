package org.eclipse.ice.developer.actions;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;

public class AppStoreWizard extends Wizard {

	private CreateAppWizardPage page; 
	
	@Override
	public boolean performFinish() {
		return true;
	}
	/**
	 */
	public AppStoreWizard() {
		page = new CreateAppWizardPage("App Store");
		setWindowTitle("ICE Scientific Application Store");
		this.setForcePreviousAndNextButtons(false);

	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers.
	 * 
	 * @param window
	 *            The workbench window.
	 */
	public AppStoreWizard(IWorkbenchWindow window) {
		// Initialize the default information.
		this();
		// Store a reference to the workbench window.
		page = new CreateAppWizardPage("Create App Environment!");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(page);
	}

}