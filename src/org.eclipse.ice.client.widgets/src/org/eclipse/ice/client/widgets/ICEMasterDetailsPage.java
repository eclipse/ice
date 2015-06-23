/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * <p>
 * This class is a FormPage that creates a page with as master-details block for
 * manipulating an ICEMasterDetailsComponent. It presents information to users
 * in a way that is consistent with the master-details pattern.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEMasterDetailsPage extends ICEFormPage {

	/**
	 * <p>
	 * The MasterDetailsComponent that is being rendered and manipulated.
	 * </p>
	 * 
	 */
	private MasterDetailsComponent masterDetailsComponent;

	private ICEFormEditor ICEFormEditor;

	/**
	 * A private reference to the ICEScrolledPropertiesBlock. Used for
	 * refreshing later.
	 */
	private ICEScrolledPropertiesBlock block;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param editor
	 *            <p>
	 *            The FormEditor for which the Page should be constructed.
	 *            </p>
	 * @param id
	 *            <p>
	 *            The id of the page.
	 *            </p>
	 * @param title
	 *            <p>
	 *            The title of the page.
	 *            </p>
	 */
	public ICEMasterDetailsPage(FormEditor editor, String id, String title) {
		super(editor, id, title);

		ICEFormEditor = (ICEFormEditor) editor;
	}

	/**
	 * <p>
	 * This operation overrides the default/abstract implementation of
	 * FormPage.createFormContents to create the contents of the
	 * ICEMasterDetailsPage.
	 * </p>
	 * 
	 * @param managedForm
	 *            <p>
	 *            The Form widget on which the ICEMasterDetailsPage exists.
	 *            </p>
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		// Set the Form
		final ScrolledForm scrolledForm = managedForm.getForm();

		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(scrolledForm.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(gd);

		// Set the MasterDetailsComponent's "header component" that contains
		// global data if it exists.
		ICEDataComponentSectionPart headerSectionPart = new ICEDataComponentSectionPart(
				section, ICEFormEditor, managedForm);
		DataComponent header = masterDetailsComponent.getGlobalsComponent();
		if (header != null) {
			headerSectionPart.setDataComponent(header);
			headerSectionPart.renderSection();
		}

		// Get the Provider
		ICEDetailsPageProvider pageProvider = new ICEDetailsPageProvider(
				masterDetailsComponent, ICEFormEditor);

		// Create a scrolledPropertiesBlock with given providers.
		block = new ICEScrolledPropertiesBlock(masterDetailsComponent,
				ICEFormEditor, pageProvider);
		block.createContent(managedForm);

	}

	/**
	 * <p>
	 * This operation sets the MasterDetailsComponent that should be managed by
	 * the ICEMasterDetailsPage.
	 * </p>
	 * 
	 * @param component
	 *            <p>
	 *            The MasterDetailsComponent.
	 *            </p>
	 */
	public void setMasterDetailsComponent(MasterDetailsComponent component) {

		masterDetailsComponent = component;

	}

	/**
	 * <p>
	 * This operation retrieves the MasterDetailsComponent that is currently
	 * managed by the ICEMasterDetailsPage.
	 * </p>
	 * 
	 * @return <p>
	 *         The MasterDetailsComponent or null if the component has not yet
	 *         been set in the page.
	 *         </p>
	 */
	public MasterDetailsComponent getMasterDetailsComponent() {

		return masterDetailsComponent;

	}

	/**
	 * Refreshes the tableViewer on the ICEScrollBlock
	 */

	public void refreshMaster() {

		// Verify the block has been setup.
		if (this.block != null) {

			// Refresh the tableViewer
			this.block.refreshTableViewer();
		}
	}
}