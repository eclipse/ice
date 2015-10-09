/*******************************************************************************
 * Copyright (c) 2012, 2014- UT-Battelle, LLC.
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
import java.util.Arrays;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.ice.viz.service.widgets.PlotGridComposite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.FileStoreEditorInput;

/**
 * This class is a FormPage that creates a page with table and metadata viewing
 * area for an ICE ResourceComponent.
 *
 * @author Jay Jay Billings
 * @author Taylor Patterson
 * @author Anna Wojtowicz
 * @author Jordan Deyton
 * @author Alex McCaskey
 *
 */
public class ICEResourcePage extends ICEFormPage
		implements ISelectionListener, IUpdateableListener, IResourceChangeListener {

	/**
	 * The ResourceComponent drawn by this page.
	 */
	private ResourceComponent resourceComponent;

	/**
	 * The workbench page used by this ICEResourcePage.
	 */
	private IWorkbenchPage workbenchPage;

	/**
	 * The primary composite for rendering the page.
	 */
	private Composite pageComposite;

	/**
	 * The layout that stacks the plot and browser composites. We use a
	 * StackLayout because we only show either the {@link #browser} or the
	 * {@link #plotComposite}.
	 */
	private final StackLayout stackLayout = new StackLayout();

	/**
	 * A browser to display the files/images from resources.
	 */
	private Browser browser;

	/**
	 * The {@code Composite} that contains the grid of drawn plots.
	 */
	private PlotGridComposite plotGridComposite;

	/**
	 * A list of file extensions that the ICEResourcePage should be treat as
	 * text files and opened via the default Eclipse text editor.
	 */
	private ArrayList<String> textFileExtensions;

	/**
	 * The default constructor.
	 *
	 * @param editor
	 *            The FormEditor for which the Page should be constructed. This
	 *            should be an {@link ICEFormEditor}.
	 * @param id
	 *            The id of the page.
	 * @param title
	 *            The title of the page.
	 */
	public ICEResourcePage(FormEditor editor, String id, String title) {

		// Call the super constructor
		super(editor, id, title);

		// Set the ICEFormEditor
		if (!(editor instanceof ICEFormEditor)) {
			logger.info("ICEResourcePage Message: Invalid FormEditor.");
		}

		// Create the list of text file extensions
		String[] extensions = { "txt", "sh", "i", "csv" };
		textFileExtensions = new ArrayList<String>(Arrays.asList(extensions));

		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
		return;
	}

	/**
	 * This operation overrides the default/abstract implementation of
	 * FormPage.createFormContents to create the contents of the
	 * ICEResourcePage.
	 *
	 * @param managedForm
	 *            The Form widget on which the ICEResourcePage exists.
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		// Local Declarations
		final FormToolkit toolkit = managedForm.getToolkit();

		// Try to show the Resource View.
		try {
			getSite().getWorkbenchWindow().getActivePage().showView(ICEResourceView.ID);
		} catch (PartInitException e) {
			logger.error(getClass().getName() + " Exception!", e);
		}

		// Get the parent Composite for the Resource Page widgets and set its
		// layout to the StackLayout.
		pageComposite = managedForm.getForm().getBody();
		pageComposite.setLayout(stackLayout);

		// Register the page with the SelectionService as a listener. Note that
		// this call can be updated to only listen for selections from a
		// particular part.
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		// If the page is disposed, then this should be removed as a selection
		// listener.
		pageComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent event) {
				getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(ICEResourcePage.this);
			}
		});

		// Create the browser.
		browser = createBrowser(pageComposite, toolkit);
		stackLayout.topControl = browser;
		pageComposite.layout();

		// Create the grid of plots.
		plotGridComposite = new PlotGridComposite(pageComposite, SWT.NONE);
		toolkit.adapt(plotGridComposite);

		// Set the workbench page reference
		workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		return;
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
	protected Browser createBrowser(Composite parent, FormToolkit toolkit) {

		Browser browser = null;

		// It is possible that constructing the Browser throws an SWTError.
		// Thus, to create the browser, we must use a try-catch block.
		try {
			// Initialize the browser and apply the layout. It should use a
			// FillLayout so its contents take up all available space.
			browser = new Browser(parent, SWT.NONE);
			toolkit.adapt(browser);

			// A Grid Layout must be used, instead of the more natural
			// FillLayout, in order to avoid a bug in which the browser is
			// capable of forcing its parent section to resize.
			browser.setLayout(new GridLayout());
			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			browser.layout(true);

			// Display the default-selected Resource from the Resource View in
			// the browser, or a message.
			if (!resourceComponent.isEmpty()) {
				browser.setText("<html><body>" + "<p style=\"font-family:Tahoma;font-size:x-small\" "
						+ "align=\"center\">Select a resource to view</p>" + "</body></html>");
			} else {
				browser.setText("<html><body>" + "<p style=\"font-family:Tahoma;font-size:x-small\" "
						+ "align=\"center\">No resources available</p>" + "</body></html>");
			}
		} catch (SWTError e) {
			logger.error(getClass().getName() + " Exception! ", e);
		}

		return browser;
	}

	/**
	 * Updates the Resource Page's widgets to render the specified Resource.
	 * <p>
	 * <b>Note:</b> This method assumes it is called from the UI thread.
	 * </p>
	 *
	 * @param resource
	 *            The resource to render. Assumed not to be {@code null}.
	 * @throws PartInitException
	 */
	public void showResource(ICEResource resource) throws PartInitException {

		// TODO Do this off the UI thread.

		// TODO This method has several return statements, making it a little
		// hard to read. It should be updated and simplified.

		// If no resource is selected, then clear the current contents of
		// the ResourcePage and set the top Control to be the browser with
		// an informative text.
		if (resource == null) {
			Control topControl = stackLayout.topControl;
			if (topControl != browser) {
				// Update the browser.
				browser.setText("<html><body>" + "<p style=\"font-family:Tahoma;font-size:x-small\" "
						+ "align=\"center\">Select a resource to view</p>" + "</body></html>");
				stackLayout.topControl = browser;
				pageComposite.layout();

				// Dispose of the previous Control occupying the
				// ResourcePage.
				if (topControl != null && !topControl.isDisposed()) {
					topControl.dispose();
				}
			}

			return;
		}

		// VizResources should not use the browser. However, if it cannot be
		// rendered with available VizResources, we should try using the
		// browser.
		boolean useBrowser = !(resource instanceof VizResource);
		if (!useBrowser) {
			// Try to draw the plot on the grid.
			try {
				if (plotGridComposite.addPlot(resource.getPath()) != -1) {
					stackLayout.topControl = plotGridComposite;
					pageComposite.layout();
					// Reactivate the Item editor tab if it's not in the front
					activateEditor();

					return;
				}
			} catch (Exception e) {
				// Instead of printing the stack trace, print the error message.
				logger.error(getClass().getName() + " Exception! ", e);
			}
		}

		String path = resource.getPath().toString();

		// Determine if the resource is a text file
		int extIndex = path.lastIndexOf(".");
		String fileExtension = path.substring(extIndex + 1);
		boolean useEditor = textFileExtensions.contains(fileExtension);

		// If the resource is a text file, open it via the Eclipse default
		// text editor
		if (useEditor) {
			// Get the content of the file
			IFileStore fileOnLocalDisk = EFS.getLocalFileSystem().getStore(resource.getPath());
			FileStoreEditorInput editorInput = new FileStoreEditorInput(fileOnLocalDisk);

			// Open the contents in the text editor
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			page.openEditor(editorInput, "org.eclipse.ui.DefaultTextEditor");
		}

		// If the Resource is a regular Resource or cannot be rendered via
		// the VizServices or a text editor, try to open it in the browser
		// as a last resort.
		if (useBrowser && !useEditor && browser != null && !browser.isDisposed()) {
			// Update the browser.
			browser.setUrl(path);
			stackLayout.topControl = browser;
			pageComposite.layout();

			// Reactivate the Item editor tab if it's not in the front
			activateEditor();
		}

		return;
	}

	/**
	 * Gets the resource's key for use in the plot maps.
	 *
	 * @param resource
	 *            The resource whose key should be determined. Assumed not to be
	 *            {@code null}.
	 * @return The resource's key in the plot maps.
	 */
	private String getPlotKey(ICEResource resource) {
		return resource.getPath().toString();
	}

	/**
	 * Reactivates the Item's editor and brings it to the front if any other
	 * editors have been opened on top of it.
	 */
	private void activateEditor() {

		// Check that the workbench page has been set correctly first
		if (workbenchPage != null) {

			// Reactivate the editor tab if it's not in the front
			if (getEditor() != null && workbenchPage.getActiveEditor() != getEditor()) {
				workbenchPage.activate(getEditor());
			}
		} else {

			// Set the workbench page and try activating the editor again
			workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activateEditor();
		}

		return;
	}

	/**
	 * This operation sets the ResourceComponent that should be used by the
	 * ICEResourcePage. It also registers the ICEResourcePage with the
	 * ResourceComponent so that it can be notified of state changes through the
	 * IUpdateableListener interface.
	 * <p>
	 * <b>Note:</b> This method should only be called when the page is created.
	 * </p>
	 *
	 * @param component
	 *            The ResourceComponent
	 */
	public void setResourceComponent(ResourceComponent component) {

		if (component != resourceComponent) {

			// Get the Display for updating the UI from the page's Composite.
			final Display display;
			if (pageComposite != null && !pageComposite.isDisposed()) {
				display = pageComposite.getDisplay();
			} else {
				display = null;
			}

			// If necessary, unregister from the old ResourceComponent.
			if (resourceComponent != null) {
				resourceComponent.unregister(this);
			}

			// ---- Clear the Resource Page widgets. ---- //
			// Update the UI and dispose of any stale UI pieces.
			if (display != null) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						// Clear the browser and make it the top widget.
						browser.setText("<html><body>" + "<p style=\"font-family:Tahoma;font-size:x-small\" "
								+ "align=\"center\">No resources available</p>" + "</body></html>");
						stackLayout.topControl = browser;
						pageComposite.layout();

						// Dispose any plot Composites.
						plotGridComposite.removeAllPlots();

						return;
					}
				});
			}
			// ------------------------------------------ //

			// Set the reference to the new ResourceComponent.
			this.resourceComponent = component;

			// If not null, register for updates from it and trigger an update
			// to sync the ResourceComponent.
			if (component != null) {
				resourceComponent.register(this);
				for (ICEResource resource : resourceComponent.getResources()) {
					if (resource instanceof VizResource) {
						resource.register(this);
					}
				}
				update(resourceComponent);
			}
		}

		return;
	}

	/**
	 * This operation retrieves the ResourceComponent that has been rendered by
	 * the ICEResourcePage or null if the component does not exist.
	 *
	 * @return The ResourceComponent or null if the component was not previously
	 *         set.
	 */
	public ResourceComponent getResourceComponent() {
		return resourceComponent;
	}

	/**
	 * This operation sets the ISimpleResource provider that should be used by
	 * the output page to load ICEResources.
	 *
	 * @param provider
	 *            The ISimpleResourceProvider
	 */
	public void setResourceProvider(ISimpleResourceProvider provider) {

		// Set the provider if it is not null
		if (provider != null) {
			// TODO
		}
		return;
	}

	/**
	 * This method is called whenever the current {@link #resourceComponent} is
	 * updated. It adds any new plots to the map of {@link #plots} if possible.
	 */
	@Override
	public void update(IUpdateable component) {

		if (component != null && component instanceof VizResource) {
			// Cast to a VizResource
			final VizResource resource = (VizResource) component;

			if (plotGridComposite != null && !plotGridComposite.isDisposed()) {
				// Refresh all plots in the grid associated with the resource.
				plotGridComposite.refreshPlots(resource.getPath());

				// Layout the composite on the UI thread.
				if (pageComposite != null && !pageComposite.isDisposed()) {
					pageComposite.getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							pageComposite.layout();
							activateEditor();
						}
					});
				}
			}

		}

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// This method should be used if we need to respond to the current
		// selection. Note that the current selection can change based on the
		// currently active view/part.
		return;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			try {
				event.getDelta().accept(new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta) throws CoreException {
						// System.out.println("Name: " +
						// delta.getResource().getName());
						for (ICEResource r : ICEResourcePage.this.resourceComponent.getResources()) {
							if (delta.getResource().getName().equals(r.getName())) {
								ICEResourcePage.this.update(r);
							}
						}
						return true;
					}
				});
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Return true if the provided ICEResource has already been displayed.
	 * 
	 * @param r
	 * @return
	 */
	public boolean isResourceDisplayed(ICEResource r) {
		if (plotGridComposite != null) {
			return plotGridComposite.contains(r.getPath());
		} else {
			return false;
		}
	}
}