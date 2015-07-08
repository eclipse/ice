/**
 * 
 */
package org.eclipse.ice.reflectivity.ui;

import org.eclipse.ice.client.widgets.ICESectionPage;
import org.eclipse.ice.client.widgets.ListComponentNattable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Kasper Gammeltoft
 *
 */
public class TestReflectivitySectionPage extends ICESectionPage {
	
	
	Composite sectionClient;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public TestReflectivitySectionPage(FormEditor editor, String id,
			String title) {
		super(editor, id, title);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui
	 * .forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		// Get the parent form and the toolkit
		final ScrolledForm scrolledForm = managedForm.getForm();
		final FormToolkit formToolkit = managedForm.getToolkit();

		// Set a GridLayout with a single column. Remove the default margins.
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		scrolledForm.getBody().setLayout(layout);

		// Get the parent
		Composite parent = managedForm.getForm().getBody();
		
		// Create the section and set its layout info
		Section listSection = formToolkit.createSection(parent,
				Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
						| Section.EXPANDED | Section.COMPACT);
		listSection.setLayout(new GridLayout(1, false));
		listSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		
		// Create the section client, which is the client area of the
		// section that will actually render data.
		sectionClient = new Composite(listSection, SWT.FLAT);
		sectionClient.setLayout(new GridLayout(2, false));
		sectionClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		// Fixes section header bug where label color is spammed
		sectionClient.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));
		// Fixes background color bug for NatTable
		sectionClient.setBackgroundMode(SWT.INHERIT_FORCE);

		// Set the section client.
		listSection.setClient(sectionClient);

		return;
	}
	

}
