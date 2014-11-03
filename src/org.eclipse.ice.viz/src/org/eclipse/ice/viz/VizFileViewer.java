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
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz;

import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This class extends the ViewPart class and provides a view in the
 * Visualization Perspective to look at the files that are currently available
 * to use for creating plots.
 * 
 * @authors bkj, tnp, djg
 */
public class VizFileViewer extends ViewPart implements IUpdateableListener,
		ISelectionChangedListener {

	/**
	 * The ID for this view.
	 */
	public static final String ID = "org.eclipse.ice.viz.VizFileViewer";

	/**
	 * The active ResourceComponent.
	 */
	private ResourceComponent resourceComponent;

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
		setResourceComponent(new ResourceComponent());
	}

	/**
	 * Creates the widgets and controls for the VizFileViewer. This includes
	 * {@link #fileTreeViewer}.
	 * 
	 * @param parent
	 *            The parent Composite that will contain this VizFileViewer.
	 */
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
	 * 
	 * @see WorkbenchPart#setFocus()
	 */
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
	public void update(IUpdateable component) {

		System.out.println("VizFileViewer Message: Incoming resource update.");

		// Sync with the display.
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// If possible, reset the fileTreeViewer's input.
				if (fileTreeViewer != null) {

					// Reset the input for the fileTreeViewer. It should be
					// built on the ResourceComponent's ArrayList of
					// ICEResources.
					fileTreeViewer.setInput(resourceComponent.getResources());

					System.out.println("VizFileViewer Message: "
							+ "Updating resource table.");
					fileTreeViewer.refresh();
					fileTreeViewer.getTree().redraw();

					// Set the selection to the newly added file
					int lastIndex = resourceComponent.getResources().size() - 1;
					if (lastIndex > -1) {
						ICEResource resource = resourceComponent.getResources()
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
	public void setResourceComponent(ResourceComponent component) {

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
	public ResourceComponent getResourceComponent() {
		// begin-user-code
		return resourceComponent;
		// end-user-code
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
				return null;
			}
		});

		inputTreeViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {

				// Get a String from the ICEResource if possible.
				String text;
				if (element instanceof ICEResource) {
					text = ((ICEResource) element).getName();
				}
				// If the element isn't an ICEResource, convert it to a String.
				else {
					text = element.toString();
				}

				return text;
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

				// Get the resources from the ResourceComponent.
				ArrayList<ICEResource> resources = resourceComponent
						.getResources();

				// Remove each ICEResource in the selection from the
				// ResourceComponent.
				for (Iterator<?> iter = structuredSelection.iterator(); iter
						.hasNext();) {
					ICEResource resource = (ICEResource) iter.next();
					System.out.println("VizFileViewer message: "
							+ "Removing the resource for file \""
							+ resource.getPath().getPath() + "\".");
					resources.remove(resource);
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
	public void addFile(ICEResource resource) {

		if (resource != null) {
			// If it's a VizResource, print out the name
			// Only add the resource to the ResourceComponent if a resource for
			// the same file does not already exist in the ResourceComponent.
			ArrayList<ICEResource> resources = resourceComponent.getResources();
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
	 * selected.
	 * 
	 * 
	 * @param event
	 *            The SelectionChangedEvent that fired this method.
	 */
	public void selectionChanged(SelectionChangedEvent event) {

		// Enable the DeleteFileAction if possible.
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			deleteFileAction.setEnabled(!selection.isEmpty());
		}

		return;
	}
	// ---------------------------------------------- //

}
