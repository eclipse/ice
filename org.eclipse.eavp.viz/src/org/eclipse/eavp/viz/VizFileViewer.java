/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.eavp.viz;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.eavp.viz.service.PlotEditor;
import org.eclipse.eavp.viz.service.PlotEditorInput;
import org.eclipse.eavp.viz.service.csv.CSVPlot;
import org.eclipse.eavp.viz.service.csv.CSVProxyPlot;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.resource.IResource;
import org.eclipse.eavp.viz.service.datastructures.resource.IVizResource;
import org.eclipse.eavp.viz.service.datastructures.resource.VisualizationResource;
import org.eclipse.eavp.viz.service.datastructures.resource.VizResourceComponent;
import org.eclipse.eavp.viz.visit.VisitPlotViewer;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends the ViewPart class and provides a view in the
 * Visualization Perspective to look at the files that are currently available
 * to use for creating plots.
 * 
 * @author Jay Jay Billings
 * @author Taylor Patterson
 * @author Jordan H. Deyton
 * @author Kasper Gammeltoft- Changed functionality to use the
 *         {@link PlotEditor}
 */
public class VizFileViewer extends ViewPart implements IVizUpdateableListener,
		ISelectionChangedListener {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(VizFileViewer.class);

	/**
	 * The ID for this view.
	 */
	public static final String ID = "org.eclipse.eavp.viz.VizFileViewer";

	/**
	 * The active ResourceComponent.
	 */
	private VizResourceComponent resourceComponent;

	/**
	 * A TreeViewer that shows imported files. This is created in
	 * {@link #createPartControl(Composite)}.
	 */
	private TreeViewer fileTreeViewer;

	/**
	 * This action opens a dialog for selecting files to add to this viewer.
	 */
	private AddFileAction addFileAction;

	/**
	 * This action calls {@link #removeSelection()} to remove all selected
	 * resources from this viewer.
	 */
	private DeleteFileAction deleteFileAction;

	/**
	 * The default constructor.
	 */
	public VizFileViewer() {
		// Set a default ResourceComponent.
		setResourceComponent(new VizResourceComponent());
	}

	/**
	 * Creates the widgets and controls for the VizFileViewer. This includes
	 * {@link #fileTreeViewer}.
	 * 
	 * @param parent
	 *            The parent Composite that will contain this VizFileViewer.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create the tool bar buttons for the view.
		createActions();

		// Initialize the ListViewer. Disable multi-selection by specifying the
		// default style bits except for SWT.MULTI.
		fileTreeViewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		fileTreeViewer.addSelectionChangedListener(this);

		// Create content and label providers.
		initializeTreeViewer(fileTreeViewer);

		// Register this view's ListViewer as a SelectionProvider.
		getSite().setSelectionProvider(fileTreeViewer);

		return;
	}

	/**
	 * Does nothing yet.
	 */
	@Override
	public void setFocus() {
		return;
	}

	/**
	 * Updates the VizFileViewer (specifically, the fileTreeViewer) when the
	 * ResourceComponent is updated.
	 * 
	 * @param component
	 *            The ResourceComponent that was just updated.
	 */
	@Override
	public void update(IVizUpdateable component) {

		logger.info("VizFileViewer Message: Incoming resource update.");

		// Sync with the display.
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// If possible, reset the fileTreeViewer's input.
				if (fileTreeViewer != null) {

					// Reset the input for the fileTreeViewer. It should be
					// built on the ResourceComponent's ArrayList of
					// ICEResources.
					fileTreeViewer.setInput(resourceComponent.getResources());

					logger.info("VizFileViewer Message: "
							+ "Updating resource table.");
					fileTreeViewer.refresh();
					fileTreeViewer.getTree().redraw();

					// Set the selection to the newly added file
					int lastIndex = resourceComponent.getResources().size() - 1;
					if (lastIndex > -1) {
						IResource resource = resourceComponent.getResources()
								.get(lastIndex);
						fileTreeViewer.setSelection(new StructuredSelection(
								resource), true);
					}

				}
			}
		});

		return;
	}

	/**
	 * This operation sets the ResourceComponent that should be used by this
	 * view. It also registers this view with the ResourceComponent so that it
	 * can be notified of state changes through the IUpdateableListener
	 * interface.
	 * 
	 * @param component
	 *            The ResourceComponent
	 */
	public void setResourceComponent(VizResourceComponent component) {

		// Make sure the ResourceComponent exists.
		if (component != null) {
			// If there was an associated ResourceComponent, unregister from it.
			if (resourceComponent != null) {
				resourceComponent.unregister(this);
			}

			// Set the component reference.
			resourceComponent = component;

			// Register this view with the Component to receive updates.
			component.register(this);

			// Update the view.
			update(component);
		}

		return;
	}

	/**
	 * This operation retrieves the active ResourceComponent of this view or
	 * null if the component does not exist.
	 * 
	 * @return The ResourceComponent or null if the component was not previously
	 *         set.
	 */
	public VizResourceComponent getResourceComponent() {
		return resourceComponent;
	}

	/**
	 * Creates the JFace Actions associated with this VizFileViewer.
	 */
	private void createActions() {

		// Get the IToolBarManager.
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();

		// Create a delete button and add it to the tool bar.
		deleteFileAction = new DeleteFileAction(this);
		toolBarManager.add(deleteFileAction);
		deleteFileAction.setEnabled(false);

		// Create an add button and add it to the tool bar.
		addFileAction = new AddFileAction(this);
		toolBarManager.add(addFileAction);

		return;
	}

	/**
	 * Initializes the provided TreeViewer based on the ResourceComponent for
	 * this VizFileViewer.
	 * 
	 * @param inputTreeViewer
	 *            The TreeViewer that should be configured to display the
	 *            ICEResources in a ResourceComponent.
	 */
	private void initializeTreeViewer(TreeViewer inputTreeViewer) {

		inputTreeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				return;
			}

			@Override
			public void dispose() {
				return;
			}

			@Override
			public boolean hasChildren(Object element) {

				// Make sure we have a VizResource
				if (element instanceof IVizResource) {
					// Cast it to make life easier
					IVizResource resource = (IVizResource) element;

					// If this resource has a valid fileset...
					if (resource.getFileSet() != null) {
						// Then return true if it is not empty
						return resource.getFileSet().length != 0;

						// If the fileset was null, but we have children
						// resources...
					} else if (resource.getChildrenResources() != null) {
						// then return true if the children list is not empty
						return !resource.getChildrenResources().isEmpty();
					}
				}

				// All else, we have no children
				return false;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {

				// Local Declaration
				Object[] elements;

				// Convert the element to an ArrayList (it should be an
				// ArrayList<ICEResource>) and then get an Object array.
				if (inputElement instanceof ArrayList<?>) {
					elements = ((ArrayList<?>) inputElement).toArray();
				} else {
					elements = new Object[] {};
				}

				return elements;
			}

			@Override
			public Object[] getChildren(Object parentElement) {

				// Make sure this is a VizResource
				if (parentElement instanceof IVizResource) {
					// Cast to make life easier
					IVizResource resource = (IVizResource) parentElement;

					// If we have children...
					if (resource.getChildrenResources() != null
							&& !resource.getChildrenResources().isEmpty()) {
						// Return all Child resources
						return resource.getChildrenResources().toArray();

					} else if (resource.getFileSet() != null
							&& resource.getFileSet().length != 0) {
						// If we didn't have VizResourc children, then check
						// that we
						// have file names to return
						ArrayList<IVizResource> children = new ArrayList<IVizResource>();
						for (String filePath : resource.getFileSet()) {
							File file = new File(filePath);

							// Try to construct an ICEResource from the File,
							// then add it to
							// the viewer.
							try {
								IVizResource childResource = new VisualizationResource(
										file);
								childResource.setHost("localhost");
								children.add(childResource);
							} catch (IOException e) {
								System.err
										.println("AddLocalFileAction error: Failed to "
												+ "create an ICEResource for the file at \""
												+ filePath + "\".");
								logger.error(getClass().getName()
										+ " Exception!", e);
							}
						}
						return children.toArray();
					}
				}

				// Otherwise return null
				return null;

			}
		});

		inputTreeViewer.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Object element = cell.getElement();

				// Get a String from the VizResource if possible.
				StyledString styledStr = new StyledString();
				if (element instanceof IVizResource) {
					IVizResource resource = (IVizResource) element;
					// Get the name from the resource
					styledStr.append(resource.getName());

					String host = resource.getHost();
					URI pathFile = resource.getPath();
					String path = null;
					if (pathFile != null) {
						pathFile.getPath();
					}

					// Get the host from the resource and append it to the tree
					// entry String grayed-out and enclosed in square brackets.
					if (host != null && !host.isEmpty()) {
						styledStr.append(" [" + host,
								StyledString.QUALIFIER_STYLER);
						// Include the path with the host if available
						if (path != null && !path.isEmpty()
								&& !resource.isRemote()) {
							styledStr.append(":" + path,
									StyledString.QUALIFIER_STYLER);
						}
						styledStr.append("]", StyledString.QUALIFIER_STYLER);
					}
				}

				// If the element isn't an VizResource, convert it to a String.
				else {
					styledStr.append(element.toString());
				}

				// Set the text for the cell and call
				// StyledCellLabelProvider#update()
				cell.setText(styledStr.toString());
				cell.setStyleRanges(styledStr.getStyleRanges());
				super.update(cell);
			}
		});

		return;
	}

	/**
	 * Removes all ICEResources selected in {@link #fileTreeViewer} from the
	 * ResourceComponent.
	 */
	public void removeSelection() {

		// Make sure the viewer's controls have been created.
		if (fileTreeViewer != null) {
			// Get the selection from the fileTreeViewer. It should at least be
			// an IStructuredSelection (a parent interface of TreeSelections).
			ISelection selection = fileTreeViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;

				// Remove each ICEResource in the selection from the
				// ResourceComponent.
				for (Iterator<?> iter = structuredSelection.iterator(); iter
						.hasNext();) {
					IResource resource = (IResource) iter.next();
					logger.info("VizFileViewer message: "
							+ "Removing the resource for file \""
							+ resource.getPath().getPath() + "\".");
					resourceComponent.remove(resource);
				}
			}

			// Update the fileTreeViewer now that the ResourceComponent has
			// changed.
			update(resourceComponent);
		}

		return;
	}

	/**
	 * Add an ICEResource to the VizFileViewer.
	 * 
	 * @param resource
	 *            an ICEResource for the file to add to the VizFileViewer.
	 */
	public void addFile(IResource resource) {

		if (resource != null) {
			// If it's a VizResource, print out the name
			// Only add the resource to the ResourceComponent if a resource for
			// the same file does not already exist in the ResourceComponent.
			ArrayList<IResource> resources = resourceComponent.getResources();
			if (!resources.contains(resource)) {
				resourceComponent.addResource(resource);
			}
		}
		return;
	}

	// ---- Implements ISelectionChangedListener ---- //
	/**
	 * This listens for selection change events in the fileTreeViewer and
	 * updates {@link #deleteFileAction} depending on whether or not a file is
	 * selected. Also, this method shows an appropriate plot viewer based on the
	 * file type of the selection.
	 * 
	 * @param event
	 *            The SelectionChangedEvent that fired this method.
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		// Get the selection
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			// Enable the DeleteFileAction if possible.
			deleteFileAction.setEnabled(!selection.isEmpty());

			// Get the VizResource of this selection.
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (!structuredSelection.isEmpty()) {
				Object object = structuredSelection.getFirstElement();
				if (object instanceof IVizResource) {
					IVizResource vizResource = (IVizResource) object;

					// Extract the file name from the VizResource
					String fileName = "";

					// Get the file set title or file name
					if (vizResource.getFileSet() != null
							&& vizResource.getFileSetTitle() != null) {
						fileName = vizResource.getFileSetTitle();
					} else {
						fileName = vizResource.getContents().getAbsolutePath();
					}

					// Get the Current page.
					IWorkbenchPage page = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();

					// Get the file from the resource
					File file = vizResource.getContents();
					if (file != null) {
						fileName = file.getAbsolutePath();
						// If it is a CSV file, then open it here
						if (fileName.matches(".*\\.csv$")) {
							try {
								// FIXME This should be using the IVizService
								// from
								// OSGi instead of instantiating a CSVPlot.
								CSVPlot dataPlot = new CSVPlot();
								dataPlot.setDataSource(file.toURI());
								CSVProxyPlot plot = new CSVProxyPlot();
								plot.setSource(dataPlot);
								page.openEditor(new PlotEditorInput(plot),
										PlotEditor.ID);
							} catch (Exception e) {
								logger.error(
										"Could not open editor for CSV file: "
												+ file.getAbsolutePath(), e);
							}
						}
						// If the file is something else...
						else {
							// Show the VisIt Plot Viewer
							try {
								getSite().getWorkbenchWindow().getActivePage()
										.showView(VisitPlotViewer.ID);
								// Get the views of the this page
								IViewReference[] refs = getSite()
										.getWorkbenchWindow().getActivePage()
										.getViewReferences();
								// Get the VisIt Plot Viewer and set its
								// resource
								for (IViewReference ref : refs) {
									if ("VisIt Plot Viewer".equals(ref
											.getPartName())) {
										VisitPlotViewer view = (VisitPlotViewer) ref
												.getView(false);
										view.setResource(vizResource);
									}
								}
							} catch (PartInitException e) {
								logger.error(getClass().getName()
										+ " Exception!", e);
							}
						}
					}
				}
			}
		}

		return;
	}
	// ---------------------------------------------- //

}
