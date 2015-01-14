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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.ice.client.common.PropertySource;
import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.swt.custom.StackLayout;

/**
 * This class is a FormPage that creates a page with table and metadata viewing
 * area for an ICE ResourceComponent.
 * 
 * @authors Jay Jay Billings, Taylor Patterson
 */
public class ICEResourcePage extends ICEFormPage implements ISelectionListener {
	/**
	 * The ResourceComponent drawn by this page.
	 */
	private ResourceComponent resourceComponent;

	/**
	 * A browser to display the files/images
	 */
	private Browser browser;

	/**
	 * The current resource being managed by this page.
	 */
	private ICEResource currentResource = null;

	/**
	 * The ICEResourceView that holds resources for this page to display.
	 */
	private ICEResourceView resourceView;

	/**
	 * The primary composite for rendering the page.
	 */
	private Composite parent;

	/**
	 * The service factory for visualization tools.
	 */
	private IVizServiceFactory vizFactory;

	/**
	 * The composite responsible for holding the plot
	 */
	private Composite plotComposite;

	/**
	 * The layout that stacks the plot and browser composites.
	 */
	private StackLayout layout;

	/**
	 * The map that holds any plots that are created
	 */
	Map<String,IPlot> plotMap;
	
	/**
	 * The Constructor
	 * 
	 * @param editor
	 *            The FormEditor for which the Page should be constructed.
	 * @param id
	 *            The id of the page.
	 * @param title
	 *            The title of the page.
	 */
	public ICEResourcePage(FormEditor editor, String id, String title) {

		// Call the super constructor
		super(editor, id, title);

		// Set the ICEFormEditor
		if (editor instanceof ICEFormEditor) {
		} else {
			System.out.println("ICEResourcePage Message: Invalid FormEditor.");
		}
		
		// Setup the plot map
		plotMap = new Hashtable<String, IPlot>();

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
		// Set the view's data
		resourceView = (ICEResourceView) getSite().getWorkbenchWindow()
				.getActivePage().findView(ICEResourceView.ID);
		resourceView.setResourceComponent(resourceComponent);

		// Setup the Form layout
		form.getBody().setLayout(new GridLayout());
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Get the parent and set its layout
		parent = new Composite(form.getBody(), SWT.None);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		layout = new StackLayout();
		parent.setLayout(layout);

		plotComposite = new Composite(parent, SWT.NONE);
		managedForm.getToolkit().adapt(plotComposite);
		managedForm.getToolkit().paintBordersFor(plotComposite);
		plotComposite.setLayout(new GridLayout(1, true));
		plotComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

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
	 * This operation draws a browser on the page that displays the selected
	 * ICEResource.
	 * 
	 * @param formToolkit
	 *            The FormToolkit that is used to create SWT widgets for this
	 *            page.
	 * @param form
	 *            The IManagedForm on which the table should be drawn.
	 */
	protected void drawBrowser(FormToolkit formToolkit, IManagedForm form) {
		// begin-user-code

		// Setup the initial browser configuration.
		try {

			// Initialize the browser and apply the layout
			browser = new Browser(parent, SWT.NONE);
			browser.setLayout(new FillLayout());

			// Set the browser to the first resource if available.
			// Otherwise, display a default message.
			currentResource = resourceView.setDefaultResourceSelection();
			if (currentResource != null && !(currentResource instanceof VizResource)) {
				browser.setUrl(currentResource.getPath().toString());
			} else {
				browser.setText("<html><body><center>No resources available.</center></body></html>");
			}
			
			// Set the stack layout appropriately
			layout.topControl = browser;
			parent.layout();
		} catch (SWTError e) {
			System.out.println("Client Message: "
					+ "Could not instantiate Browser: " + e.getMessage());
		}

		return;
		// end-user-code
	}

	/**
	 * This operation overrides the default/abstract implementation of
	 * ISelectionListener.selectionChanged to display the resource selected in
	 * the ICEResourceView.
	 * 
	 * @param part
	 *            The IWorkbenchPart that called this function.
	 * @param selection
	 *            The ISelection chosen in the part parameter.
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
			// If the resource is not null, set the current resource value and
			// update the browser or plot as needed
			if (selectedResource != null
					&& !(selectedResource instanceof VizResource)) {
				// Sometimes the browser breaks, so make sure it is working
				// first.
				if (browser != null && !browser.isDisposed()) {
					currentResource = selectedResource;
					// Update the browser
					browser.setUrl(currentResource.getPath().toString());
					// Update the stack to show the browser
					layout.topControl = browser;
					parent.layout();
				}
			} else if (selectedResource != null) {
				// Switch to the plot composite
				layout.topControl = plotComposite;
				try {
					// Get the plot from the hash table since the user clicked on it
					IPlot plot = plotMap.get(selectedResource.getPath().toString());
					// Get the plot types and pick a plot type
					Map<String,String[]> plotTypes = plot.getPlotTypes();
					ArrayList<String> keys = new ArrayList<String>(plotTypes.keySet());
					String category = keys.get(0);
					String type = plotTypes.get(category)[0];
					// Clear the plotComposite's children
					for (Control control : plotComposite.getChildren()) {
						control.dispose();
					}
					// Draw the plot
					plot.draw(category, type, plotComposite);	
					plotComposite.layout();
				} catch (Exception e) {
					// Complain
					e.printStackTrace();
				}
				// Layout the parent
				parent.layout();
			}
		}

		return;
	}

	/**
	 * This operation sets the IVizServiceFactory that should be used to create
	 * plots.
	 * 
	 * @param service
	 *            The service factory that should be used
	 */
	public void setVizService(IVizServiceFactory factory) {
		vizFactory = factory;
	}

	/**
	 * This operation sets the ResourceComponent that should be used by the
	 * ICEResourcePage. It also registers the ICEResourcePage with the
	 * ResourceComponent so that it can be notified of state changes through the
	 * IUpdateableListener interface.
	 * 
	 * @param component
	 *            <p>
	 *            The ResourceComponent
	 *            </p>
	 */
	public void setResourceComponent(ResourceComponent component) {
		// begin-user-code

		// Make sure the ResourceComponent exists
		if (component != null) {
			// Set the component reference
			resourceComponent = component;
			// Run through the component and create plots for any VizResources it may have
			for (ICEResource resource : resourceComponent.getResources()) {
				if (resource instanceof VizResource) {
					try {
						IPlot plot = vizFactory.get().createPlot(resource.getPath());
						// Cram the plot in the hashtable until the user clicks on it
						plotMap.put(plot.getDataSource().toString(), plot);
					} catch (Exception e) {
						// Complain
						e.printStackTrace();
					}
				}
			}
		}

		return;
		// end-user-code
	}

	/**
	 * This operation retrieves the ResourceComponent that has been rendered by
	 * the ICEResourcePage or null if the component does not exist.
	 * 
	 * @return The ResourceComponent or null if the component was not previously
	 *         set.
	 */
	public ResourceComponent getResourceComponent() {
		// begin-user-code
		return resourceComponent;
		// end-user-code
	}

	/**
	 * This operation sets the ISimpleResource provider that should be used by
	 * the output page to load ICEResources.
	 * 
	 * @param provider
	 *            The ISimpleResourceProvider
	 */
	public void setResourceProvider(ISimpleResourceProvider provider) {
		// begin-user-code

		// Set the provider if it is not null
		if (provider != null) {
		}
		return;
		// end-user-code
	}
}