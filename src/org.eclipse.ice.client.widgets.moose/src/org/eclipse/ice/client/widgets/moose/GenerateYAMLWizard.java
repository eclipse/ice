package org.eclipse.ice.client.widgets.moose;

import java.util.ArrayList;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;

public class GenerateYAMLWizard extends Wizard {

	/**
	 * Handle to the workbench window.
	 */
	protected IWorkbenchWindow workbenchWindow;

	private GenerateYAMLWizardPage page;
	
	private ArrayList<String> host_name_password;
	
	/**
	 * A nullary constructor. This is used by the platform. <b>If called from an
	 * {@link IHandler}, use {@link #NewItemWizard(IWorkbenchWindow)} </b>.
	 */
	public GenerateYAMLWizard() {
		// Nothing to do.
		page = new GenerateYAMLWizardPage("NAME");

	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers.
	 * 
	 * @param window
	 *            The workbench window.
	 */
	public GenerateYAMLWizard(IWorkbenchWindow window) {
		// Initialize the default information.
		this();
		// Store a reference to the workbench window.
		workbenchWindow = window;
		page = new GenerateYAMLWizardPage("NAME");
		
	}

	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
	
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		//if (page == null) {
			//page = new NewItemWizardPage("Select an Item to create");
	//	}
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		host_name_password = new ArrayList<String>();
		
		host_name_password.add(page.getHostName());
		host_name_password.add(page.getUsername());
		host_name_password.add(page.getPassword());
		
		return true;
	}

	public ArrayList<String> getData() {
		return host_name_password;
	}
}
