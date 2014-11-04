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
package org.eclipse.ice.reactor.perspective;

import org.eclipse.ice.client.widgets.reactoreditor.DataSource;

import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.reactor.perspective.internal.ReactorEditorRegistry;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;

/**
 * This class extends the ViewPart class to display opened reactor files and
 * their components.
 * 
 * @author tnp, Jordan H. Deyton
 */
public class ReactorViewer extends ViewPart implements
		ISelectionChangedListener, IUpdateableListener {

	/**
	 * The id for this view
	 */
	public static final String ID = "org.eclipse.ice.reactors.perspective.ReactorViewer";

	/**
	 * The active ResourceComponent.
	 */
	private ResourceComponent resourceComponent;

	/**
	 * A TreeViewer that shows opened reactor files. This is created in
	 * {@link #createPartControl(Composite)}.
	 */
	private TreeViewer reactorTreeViewer;

	/**
	 * This action provides the user a means to select a reactor file to open.
	 */
	private OpenReactorFileAction openReactorFileAction;

	/**
	 * This action lets the user save any changes made to an opened reactor
	 * file.
	 */
	private SaveReactorFileAction saveReactorFileAction;

	/**
	 * This action opens a dialog for selecting reactor components.
	 */
	private AddReactorPartAction addReactorPartAction;

	/**
	 * This action calls {@link #removeSelection()} to remove all selected
	 * resources from this viewer.
	 */
	private DeleteReactorPartAction deleteReactorPartAction;

	/**
	 * This action provides a drop down menu to select the Reactor Editor to
	 * open the current selection in.
	 */
	private AnalyzeInReactorEditorAction showInReactorEditorAction;

	/**
	 * This action provides a drop down menu to select the Reactor Editor whose
	 * contents should be compared with the selection from the TreeViewer.
	 */
	private CompareAction compareAction;

	/**
	 * The default constructor.
	 */
	public ReactorViewer() {

		// Set a default ResourceComponent.
		setResourceComponent(new ResourceComponent());

		return;
	}

	/**
	 * Creates the widgets and controls for the ReactorViewer.
	 * 
	 * @param parent
	 *            The parent Composite that will contain this ReactorViewer.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create the tool bar buttons for this view.
		createActions();

		// Initialize the TreeViewer. Disable multi-selection by not including
		// SWT.MULTI in the constructor parameters.
		reactorTreeViewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		reactorTreeViewer.addSelectionChangedListener(this);

		// Set the tree's content provider.
		reactorTreeViewer.setContentProvider(new ReactorTreeContentProvider());

		// Set the tree's label provider.
		reactorTreeViewer.setLabelProvider(new ReactorTreeLabelProvider());

		// Create a MenuManager that will enable a context menu in the
		// TreeViewer. The context menu will provide access to the same actions
		// that are on the viewer's ToolBar.
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Control control = reactorTreeViewer.getControl();
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);

		return;
	}

	/**
	 * Not used at this time.
	 * 
	 * @see WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		return;
	}

	/**
	 * This listens for selection change events in the reactorTreeViewer and
	 * updates the actions depending on the selection type.
	 * 
	 * @param event
	 *            The SelectionChangedEvent that fired this method.
	 * 
	 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		// Enable the actions if the selection is not empty, disable otherwise.
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			boolean nonEmpty = !selection.isEmpty();
			addReactorPartAction.setEnabled(nonEmpty);
			deleteReactorPartAction.setEnabled(nonEmpty);
			showInReactorEditorAction.setEnabled(nonEmpty);
			compareAction.setEnabled(nonEmpty);
		}

		return;
	}

	/**
	 * Updates the ReactorViewer (specifically, the {@link #reactorTreeViewer})
	 * when the ResourceComponent is updated.
	 * 
	 * @param component
	 *            The ResourceComponent that was just updated.
	 * 
	 * @see IUpdateableListener#update(IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		System.out.println("ReactorViewer Message: Incoming resource update.");

		// Sync with the display.
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// If possible, reset the reactorTreeViewer's input.
				if (reactorTreeViewer != null) {

					// Reset the input for the reactorTreeViewer. It should be
					// built on the ResourceComponent's ArrayList of
					// ICEResources.
					reactorTreeViewer.setInput(resourceComponent.getResources());

					System.out.println("ReactorViewer Message: "
							+ "Updating resource table.");
					reactorTreeViewer.refresh();
					reactorTreeViewer.getTree().redraw();

					// Set the selection to the newly added file
					int lastIndex = resourceComponent.getResources().size() - 1;
					if (lastIndex > -1) {
						ICEResource resource = resourceComponent.getResources()
								.get(lastIndex);
						reactorTreeViewer.setSelection(new StructuredSelection(
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
	private void setResourceComponent(ResourceComponent component) {

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
		return resourceComponent;
	}

	/**
	 * Creates the JFace Actions associated with this ReactorViewer.
	 */
	private void createActions() {

		// Get the IToolBarManager.
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();

		// Create an open button and add it to the tool bar.
		openReactorFileAction = new OpenReactorFileAction(this);
		toolBarManager.add(openReactorFileAction);

		// TODO Re-incorporate the save button when we can edit reactors!
		// // Create a save button and add it to the tool bar.
		// saveReactorFileAction = new SaveReactorFileAction(this);
		// toolBarManager.add(saveReactorFileAction);
		// saveReactorFileAction.setEnabled(false);

		// Create an add button and add it to the tool bar.
		addReactorPartAction = new AddReactorPartAction(this);
		toolBarManager.add(addReactorPartAction);
		addReactorPartAction.setEnabled(false);

		// Create a delete button and add it to the tool bar.
		deleteReactorPartAction = new DeleteReactorPartAction(this);
		toolBarManager.add(deleteReactorPartAction);
		deleteReactorPartAction.setEnabled(false);

		// Create an open in Reactor Editor button and add it to the tool bar.
		showInReactorEditorAction = new AnalyzeInReactorEditorAction(this);
		toolBarManager.add(showInReactorEditorAction);
		showInReactorEditorAction.setEnabled(false);

		// Create the compare with Reactor Editor button and add it to the tool
		// bar.
		compareAction = new CompareAction(this);
		toolBarManager.add(compareAction);
		compareAction.setEnabled(false);

		return;
	}

	/**
	 * Removes all ICEResources selected in {@link #reactorTreeViewer} from the
	 * {@link #resourceComponent}.
	 */
	public void removeSelection() {

		// Make sure the viewer's controls have been created.
		if (reactorTreeViewer != null) {
			// Get the selection from the reactorTreeViewer. It should at least
			// be
			// an IStructuredSelection (a parent interface of TreeSelections).
			ISelection selection = reactorTreeViewer.getSelection();
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
					System.out.println("ReactorViewer message: "
							+ "Removing the resource \"" + resource.getName()
							+ "\".");
					resources.remove(resource);
				}
			}

			// Update the reactorTreeViewer now that the ResourceComponent has
			// changed.
			update(resourceComponent);
		}

		return;
	}

	/**
	 * Add an ICEResource to the ReactorViewer.
	 * 
	 * @param resource
	 *            an ICEResource for the reactor (.h5) file to add to the
	 *            ReactorViewer.
	 */
	public void addReactorFile(ICEResource resource) {

		if (resource != null) {
			System.out.println("ReactorViewer message: Getting ICEResource "
					+ "for file at \"" + resource.getPath().getPath() + "\".");

			// Only add the resource to the ResourceComponent if a resource for
			// the same file does not already exist in the ResourceComponent.
			ArrayList<ICEResource> resources = resourceComponent.getResources();
			if (!resources.contains(resource)) {
				resourceComponent.addResource(resource);
			}
		}
		return;
	}

	/**
	 * Shows the current selection in {@link #reactorTreeViewer} in a Reactor
	 * Analyzer.
	 * 
	 * @param reactorEditorId
	 *            The ID of the target Reactor Analyzer.
	 */
	public void showSelection(int reactorEditorId) {

		System.out.println("ReactorViewer message: Showing selection in"
				+ " Reactor Editor " + reactorEditorId);

		// Get the current selection from the TreeViewer.
		ITreeSelection treeSelection = (ITreeSelection) reactorTreeViewer
				.getSelection();

		// If the selection is not empty, try to set it for the specified
		// reactor editor.
		if (!treeSelection.isEmpty()) {
			ReactorEditorRegistry.setInput(reactorEditorId, treeSelection,
					DataSource.Input);
		}
		return;
	}

	/**
	 * Compares the current selection in {@link #reactorTreeViewer} with an
	 * existing Reactor Analyzer.
	 * 
	 * @param reactorEditorId
	 *            The ID of the target Reactor Analyzer.
	 */
	public void compareSelection(int reactorEditorId) {
		// TODO Change it to do some actual comparison rather than just setting
		// the reference!

		System.out.println("ReactorViewer message: Showing selection in"
				+ " Reactor Editor " + reactorEditorId);

		// Get the current selection from the TreeViewer.
		ITreeSelection treeSelection = (ITreeSelection) reactorTreeViewer
				.getSelection();

		// If the selection is not empty, try to set it for the specified
		// reactor editor.
		if (!treeSelection.isEmpty()) {
			ReactorEditorRegistry.setInput(reactorEditorId, treeSelection,
					DataSource.Reference);
		}
		return;
	}

	/**
	 * Retrieve the current selection in the {@link #reactorTreeViewer} for
	 * access by external classes.
	 * 
	 * @return The current selection in the {@link #reactorTreeViewer}
	 */
	public Object getSelectedElement() {
		return ((ITreeSelection) reactorTreeViewer.getSelection())
				.getFirstElement();
	}

	/**
	 * Fills the context menu for the {@link #reactorTreeViewer}. This method is
	 * called when the context menu is about to appear. It fills out the
	 * MenuManager with the appropriate, available actions from the ToolBar.
	 * 
	 * @param menuManager
	 *            The IMenuManager responsible for the context menu.
	 */
	protected void fillContextMenu(IMenuManager menuManager) {

		// Force the menu to update. This needs to be called for the context
		// menu to update when the set of Reactor Analyzers has changed.
		menuManager.update(true);

		ISelection iSelection = reactorTreeViewer.getSelection();
		if (!iSelection.isEmpty() && iSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) iSelection;

			Object object = selection.getFirstElement();
			// If an ICEResource was clicked, expose the save, add, and delete
			// actions.
			if (object instanceof ICEResource) {
				menuManager.add(saveReactorFileAction);
				menuManager.add(new Separator());
				menuManager.add(addReactorPartAction);
				menuManager.add(deleteReactorPartAction);
			}
			// If an IReactorComponent was clicked, expose the add, delete, and
			// show actions.
			else {
				menuManager.add(addReactorPartAction);
				menuManager.add(deleteReactorPartAction);
				menuManager.add(new Separator());
				menuManager.add(showInReactorEditorAction);
				menuManager.add(compareAction);
			}
		}
		// If the selection was empty, expose the open action.
		else {
			menuManager.add(openReactorFileAction);
		}

		return;
	}

}
