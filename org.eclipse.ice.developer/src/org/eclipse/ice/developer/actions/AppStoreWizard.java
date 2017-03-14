package org.eclipse.ice.developer.actions;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;

public class AppStoreWizard extends Wizard {

	private AppStoreWizardPage page; 
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 */
	public AppStoreWizard() {
		page = new AppStoreWizardPage("App Store");
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
//		workbenchWindow = window;
		page = new AppStoreWizardPage("Fork the Stork!");

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