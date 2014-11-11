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

import org.eclipse.ice.client.common.PropertySource;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;

import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.IManagedForm;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is a FormPage that creates a page with table and metadata viewing
 * area for an ICE ResourceComponent.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @authors Jay Jay Billings, Taylor Patterson
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEResourcePage extends ICEFormPage implements ISelectionListener {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ResourceComponent drawn by this page.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ResourceComponent resourceComponent;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ISimpleResourceProvider that should be used to load ICEResources.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ISimpleResourceProvider resourceProvider;

	/**
	 * A browser to display the files/images
	 */
	private Browser browser;

	/**
	 * The ICEFormEditor used by this page
	 */
	private ICEFormEditor formEditor;

	/**
	 * The IManagedForm used by the page and its parts
	 */
	private IManagedForm managedForm;

	/**
	 * The current resource being managed by this page.
	 */
	private ICEResource currentResource = null;

	/**
	 * The data component of resource properties available for a given resource
	 * is managed with this SectionPart.
	 */
	private ICEDataComponentSectionPart propertiesPart;

	/**
	 * The section that actually renders the properties.
	 */
	private Section propertiesSection;

	/**
	 * The composite that holds the properties section part.
	 */
	private Composite propertiesComposite;

	/**
	 * The ICEResourceView that holds resources for this page to display.
	 */
	private ICEResourceView resourceView;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEResourcePage(FormEditor editor, String id, String title) {
		// begin-user-code

		// Call the super constructor
		super(editor, id, title);

		// Set the ICEFormEditor
		if (editor instanceof ICEFormEditor) {
			formEditor = (ICEFormEditor) editor;
		} else {
			System.out.println("ICEResourcePage Message: Invalid FormEditor.");
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the ResourceComponent that should be used by the
	 * ICEResourcePage. It also registers the ICEResourcePage with the
	 * ResourceComponent so that it can be notified of state changes through the
	 * IUpdateableListener interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            The ResourceComponent
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setResourceComponent(ResourceComponent component) {
		// begin-user-code

		// Make sure the ResourceComponent exists
		if (component != null) {
			// Set the component reference
			resourceComponent = component;
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the ResourceComponent that has been rendered by
	 * the ICEResourcePage or null if the component does not exist.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The ResourceComponent or null if the component was not previously
	 *         set.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ResourceComponent getResourceComponent() {
		// begin-user-code
		return resourceComponent;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the ISimpleResource provider that should be used by
	 * the output page to load ICEResources.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param provider
	 *            <p>
	 *            The ISimpleResourceProvider
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setResourceProvider(ISimpleResourceProvider provider) {
		// begin-user-code

		// Set the provider if it is not null
		if (provider != null) {
			resourceProvider = provider;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation draws a browser on the page that displays the selected
	 * ICEResource.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param formToolkit
	 *            <p>
	 *            The FormToolkit that is used to create SWT widgets for this
	 *            page.
	 *            </p>
	 * @param form
	 *            <p>
	 *            The IManagedForm on which the table should be drawn.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void drawBrowser(FormToolkit formToolkit, IManagedForm form) {
		// begin-user-code

		// Local Declarations
		final ScrolledForm scrolledForm = form.getForm();

		// Setup the initial browser configuration.
		try {
			// Setup the browser's layout
			GridData gridData = new GridData();
			gridData.grabExcessVerticalSpace = true;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.FILL;
			gridData.horizontalAlignment = GridData.FILL;

			// Initialize the browser and apply the layout
			browser = new Browser(scrolledForm.getBody(), SWT.NONE);
			browser.setLayoutData(gridData);
			browser.setLayout(new FillLayout());

			// Set the browser to the first resource if available.
			// Otherwise, display a default message.
			currentResource = resourceView.setDefaultResourceSelection();
			if (currentResource != null) {
				browser.setUrl(currentResource.getPath().toString());
			} else {
				browser.setText("<html><body><center>No resources available.</center></body></html>");
			}
		} catch (SWTError e) {
			System.out.println("Client Message: "
					+ "Could not instantiate Browser: " + e.getMessage());
		}

		// Initialize the composite that will hold the properties
		propertiesComposite = formToolkit.createComposite(scrolledForm
				.getBody());
		propertiesComposite.setLayout(new FillLayout());

		return;
		// end-user-code
	}

	/**
	 * <p>
	 * This operation updates the properties section to reflect the data of the
	 * selected resource.
	 * </p>
	 */
	protected void updatePropertiesSection() {

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {

				// Redraw the properties section if needed
				if (!(currentResource.getProperties().isEmpty())) {
					// Dispose the old pieces
					for (Control childControl : propertiesComposite
							.getChildren()) {
						childControl.dispose();
					}
					// Create a SectionPart for the Resource properties
					propertiesSection = managedForm.getToolkit().createSection(
							propertiesComposite,
							Section.TITLE_BAR | Section.DESCRIPTION
									| Section.TWISTIE | Section.EXPANDED);
					propertiesSection.setText("Selected Resource Properties");
					propertiesSection.setDescription("The following"
							+ " editable properties are available for"
							+ " this resource. Changing the properties"
							+ " will change the resource after the"
							+ " server is updated.");
					// Setup the properties DataComponentSectionPart
					propertiesPart = new ICEDataComponentSectionPart(
							propertiesSection, formEditor, managedForm);
					// Create the properties component, just completely redraw
					// it and the section
					DataComponent propsComponents = new DataComponent();
					propsComponents.setName("Selected Resource Properties");
					propertiesSection.setDescription("The following"
							+ " editable properties are available for"
							+ " this resource. Changing the properties"
							+ " will change the resource after the"
							+ " server is updated.");
					// Get all of the Entries from the resource
					for (Entry i : currentResource.getProperties()) {
						propsComponents.addEntry(i);
					}
					// Set the component and re-render
					propertiesPart.initialize(managedForm);
					propertiesPart.setDataComponent(propsComponents);
					propertiesPart.renderSection();
					// Layout the section
					propertiesComposite.layout(true);
					managedForm.reflow(true);
				}
			}
		});

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the default/abstract implementation of
	 * FormPage.createFormContents to create the contents of the
	 * ICEResourcePage.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param managedForm
	 *            <p>
	 *            The Form widget on which the ICEResourcePage exists.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void createFormContent(IManagedForm managedForm) {
		// begin-user-code

		// Local Declarations
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		// Show the view
		try {
			getSite().getWorkbenchWindow().getActivePage()
					.showView(ICEResourceView.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		resourceView = (ICEResourceView) getSite().getWorkbenchWindow()
				.getActivePage().findView(ICEResourceView.ID);
		resourceView.setResourceComponent(resourceComponent);

		// Setup the layout
		GridLayout gridLayout = new GridLayout();
		form.getBody().setLayout(gridLayout);

		// Set the managed form reference
		this.managedForm = managedForm;

		// Draw the browser
		drawBrowser(toolkit, managedForm);

		// Register the page as a selection listener. The page returned by
		// getPage() is not the same as this page! There are some subtle UI
		// differences under the hood.
		getSite().getPage().addSelectionListener(this);

		// Add the ICEResourceView as a listener to this form.
		getSite().getWorkbenchWindow().getPartService()
				.addPartListener(resourceView);

		return;
		// end-user-code
	}

	/**
	 * <p>
	 * This operation overrides the default/abstract implementation of
	 * ISelectionListener.selectionChanged to display the resource selected in
	 * the ICEResourceView.
	 * </p>
	 * 
	 * @param part
	 *            <p>
	 *            The IWorkbenchPart that called this function.
	 *            </p>
	 * @param selection
	 *            <p>
	 *            The ISelection chosen in the part parameter.
	 *            </p>
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// Get the selection in the ICEResourceView and update the browser.
		if (part.getSite().getId().equals(ICEResourceView.ID)) {
			Object selectedElement = ((ITreeSelection) selection)
					.getFirstElement();
			ICEResource selectedResource = null;
			if (selectedElement instanceof String) {
				selectedResource = resourceView.resourceChildMap
						.get(selectedElement);
			} else if (selectedElement instanceof PropertySource) {
				PropertySource source = (PropertySource) selectedElement;
				selectedResource = (ICEResource) source.getWrappedData();
			}
			// If the resource is not null, set the current
			// resource value and update the browser and
			// properties
			if (selectedResource != null && browser != null
					&& !browser.isDisposed()) {
				currentResource = selectedResource;
				// Update the browser
				browser.setUrl(currentResource.getPath().toString());
				// Update the properties section
				updatePropertiesSection();
			}
		}

		return;
	}
}