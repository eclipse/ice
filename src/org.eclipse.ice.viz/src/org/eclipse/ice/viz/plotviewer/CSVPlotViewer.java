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
package org.eclipse.ice.viz.plotviewer;

import org.eclipse.ice.client.widgets.NextAction;
import org.eclipse.ice.client.widgets.PlayAction;
import org.eclipse.ice.client.widgets.PlayableViewPart;
import org.eclipse.ice.client.widgets.PreviousAction;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.viz.DeletePlotAction;
import org.eclipse.ice.viz.VizFileViewer;
import org.eclipse.ice.viz.VizResource;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;

/**
 * This class extends the ViewPart class and provides a view in the
 * Visualization Perspective to display CSV plots that are currently available.
 * 
 * @author Jay Jay Billings, tnp, Jordan H. Deyton, w8o
 */
public class CSVPlotViewer extends PlayableViewPart implements
		IUpdateableListener, ISelectionChangedListener {

	/**
	 * The ID for this view
	 */
	public static final String ID = "org.eclipse.ice.viz.plotviewer.CSVPlotViewer";

	/**
	 * The currently selected CSV file stored as a VizResource.
	 */
	private VizResource resource;

	/**
	 * A List of all PlotProviders displayed in this view.
	 */
	private final ArrayList<PlotProvider> plottedProviders;

	/**
	 * The active ResourceComponent
	 */
	private ResourceComponent resourceComponent;

	/**
	 * The TreeViewer contained in this ViewPart used for managing resources in
	 * the view.
	 */
	private TreeViewer plotTreeViewer;

	/**
	 * The Action for selecting the next element in the list.
	 */
	private NextAction nextAction;

	/**
	 * The Action for playing through the list of plots.
	 */
	private PlayAction playAction;

	/**
	 * The Action for selecting the previous element in the list.
	 */
	private PreviousAction prevAction;

	/**
	 * Creates a dialog that lets the user select from the available plots for
	 * the currently selected CSV file from the {@link VizFileViewer}.
	 */
	private AddCSVPlotAction addPlotAction;

	/**
	 * Removes the selected plot(s) from the PlotViewer.
	 */
	private DeletePlotAction deletePlotAction;

	/**
	 * The default constructor.
	 */
	public CSVPlotViewer() {

		// Initialize the variables tied to the current CSV file stored as a
		// VizResource.
		resource = null;

		// Initialize the lists for the selected plots.
		plottedProviders = new ArrayList<PlotProvider>();

		// This will always be a playable view, so set it that way.
		playable = true;

		return;
	}

	/**
	 * Creates the widgets and controls for the PlotViewer. This includes
	 * {@link #plotTreeViewer}.
	 * 
	 * @param parent
	 *            The parent Composite that will contain this PlotViewer.
	 * 
	 * @see ViewPart#createPartControl(Composite)
	 */
	public void createPartControl(Composite parent) {

		// Create the tool bar buttons for the view
		createActions();

		// Initialize the TreeViewer.
		plotTreeViewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		plotTreeViewer.addSelectionChangedListener(this);

		// Create content and label providers
		initializeTreeViewer(plotTreeViewer);

		// Register this view's ListViewer as a SelectionProvider
		getSite().setSelectionProvider(plotTreeViewer);

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
	 * This function is called by the NextAction and PlayAction to set the
	 * selection to the next element in the list.
	 * 
	 * @see PlayableViewPart#setToNextResource()
	 */
	@Override
	public void setToNextResource() {

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Get the currently selected resource in the view. (Or the
				// first selected resource if multiple resources are
				// selected even though this has no effect.)
				TreeItem[] currSelection = plotTreeViewer.getTree()
						.getSelection();
				int currIndex = plotTreeViewer.getTree().indexOf(
						currSelection[0]);

				// Set the selection to the next resource in the tree or the
				// first resource if the last resource is currently selected.
				if (!plottedProviders.isEmpty()) {
					int nextIndex = (currIndex + 1) % plottedProviders.size();
					plotTreeViewer.setSelection(new StructuredSelection(
							plottedProviders.get(nextIndex)), true);
				}
			}
		});

		return;
	}

	/**
	 * This function is called by the PreviousAction to set the selection to the
	 * previous action in the list.
	 * 
	 * @see PlayableViewPart#setToPreviousResource()
	 */
	@Override
	public void setToPreviousResource() {

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Get the currently selected resource in the view. (Or the
				// first selected resource if multiple resources are
				// selected even though this has no effect.)
				TreeItem[] currSelection = plotTreeViewer.getTree()
						.getSelection();
				int currIndex = plotTreeViewer.getTree().indexOf(
						currSelection[0]);

				// Set the selection to the previous resource in the tree or the
				// last resource if the first resource is currently selected.
				if (!plottedProviders.isEmpty()) {
					int prevIndex = (currIndex - 1) % plottedProviders.size();
					if (prevIndex < 0) {
						prevIndex = plottedProviders.size() - 1;
					}
					plotTreeViewer.setSelection(new StructuredSelection(
							plottedProviders.get(prevIndex)), true);
				}
			}
		});

		return;
	}

	/**
	 * Refreshes the content in the {@link #plotTreeViewer}.
	 */
	private void refreshPlotViewer() {
		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// If possible, reset the plotTreeViewer's input.
				if (plotTreeViewer != null) {

					System.out.println("CSVPlotViewer message: "
							+ "Refreshing TreeViewer.");

					// Reset the input for the plotTreeViewer. The viewer just
					// takes an array of objects.
					plotTreeViewer.setInput(plottedProviders);

					plotTreeViewer.refresh();
					plotTreeViewer.getTree().redraw();
				}
			}
		});

		return;
	}

	/**
	 * Updates the PlotViewer (specifically, the {@link #plotTreeViewer}) when
	 * the associated Component is updated.
	 * 
	 * @param component
	 *            The Component that was just updated.
	 */
	@Override
	public void update(IUpdateable component) {

		System.out.println("CSVPlotViewer Message: Incoming resource update.");
		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// If possible, reset the plotTreeViewer's input.
				if (plotTreeViewer != null) {

					// TODO Reset the input for the plotTreeViewer.
					// plotTreeViewer.setInput(null);

					System.out.println("CSVPlotViewer Message: "
							+ "Updating resource table.");
					plotTreeViewer.refresh();
					plotTreeViewer.getTree().redraw();
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
	 * Creates the JFace Actions associated with this PlotViewer.
	 */
	private void createActions() {

		// Get the IToolBarManager
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();

		// Create a previous button and add it to the tool bar
		prevAction = new PreviousAction(this);
		toolBarManager.add(prevAction);
		prevAction.setEnabled(!plottedProviders.isEmpty());

		// Create a play button and add it to the tool bar
		playAction = new PlayAction(this);
		toolBarManager.add(playAction);
		playAction.setEnabled(!plottedProviders.isEmpty());

		// Create a next button and add it to the tool bar
		nextAction = new NextAction(this);
		toolBarManager.add(nextAction);
		nextAction.setEnabled(!plottedProviders.isEmpty());

		// Create a delete button and add it to the tool bar
		deletePlotAction = new DeletePlotAction(this);
		toolBarManager.add(deletePlotAction);
		deletePlotAction.setEnabled(!plottedProviders.isEmpty());

		// Create an add button and add it to the tool bar
		addPlotAction = new AddCSVPlotAction(this);
		toolBarManager.add(addPlotAction);
		addPlotAction.setEnabled(resource != null);

		return;
	}

	/**
	 * Initializes the provided TreeViewer based on the current VizResource for
	 * this PlotViewer.
	 * 
	 * @param inputTreeViewer
	 *            The TreeViewer that should be configured to display the
	 *            currently selected plots for a CSV file.
	 */
	private void initializeTreeViewer(TreeViewer inputTreeViewer) {

		// Set the content provider, which determines how the input should
		// produce elements in the TreeViewer.

		inputTreeViewer.setContentProvider(new PlotTreeContentProvider());

		// Set up the label provider, which determines what string is displayed
		// for each element in the tree. Currently, this only needs to produce
		// a string for each PlotProvider.
		inputTreeViewer.setLabelProvider(new PlotTreeLabelProvider());

		return;
	}

	/**
	 * Gets the current VizResource wrapping the CSV file that is being used by
	 * this PlotViewer.
	 * 
	 * @return A VizResource.
	 */
	public VizResource getResource() {
		return resource;
	}

	/**
	 * Adds a plot to the current set of CSV plots.
	 * 
	 * @param newPlotProvider
	 *            The PlotProvider for the plot that is to be added.
	 */
	public void addPlot(PlotProvider newPlotProvider) {
		// Make sure the PlotProvider and current resource are not null
		if (newPlotProvider != null && resource != null) {

			// Add this entry to our bookkeeping.
			plottedProviders.add(newPlotProvider);

			System.out.println("CSVPlotViewer message: adding plot \""
					+ newPlotProvider.getPlotTitle() + "\".");

			// Update the plotViewer.
			refreshPlotViewer();
		}

		return;
	}

	/**
	 * Removes a plot from the current set of CSV plots.
	 * 
	 * @param newPlotProvider
	 *            The PlotProvider for the plot that is to be removed.
	 */
	public void removePlot(PlotProvider newPlotProvider) {
		// Make sure the entry is not null and that it is marked as plotted.
		if (newPlotProvider != null) {
			// Get the index of the entry in the list of plotted entries.
			int index = -1;
			for (int i = 0; i < plottedProviders.size(); i++) {
				if (newPlotProvider == plottedProviders.get(i)) {
					index = i;
					break;
				}
			}

			if (index > -1) {
				// Remove the resource and entry from our bookkeeping.
				plottedProviders.remove(index);

				System.out.println("CSVPlotViewer message: Removing plot \""
						+ newPlotProvider.getPlotTitle() + "\".");

				// Update the plotViewer.
				refreshPlotViewer();
			}
		}
		return;
	}

	/**
	 * This method draws the plot for the specified entry if it is one of the
	 * selected plots.
	 * 
	 * @param newPlotProvider
	 *            The PlotProvider to draw.
	 */
	public void drawPlot(PlotProvider newPlotProvider) {
		// Make sure the entry is not null and that it is marked as plotted.
		if (newPlotProvider != null) {
			// Get the index of the entry in the list of plotted entries.
			int index = -1;
			for (int i = 0; i < plottedProviders.size(); i++) {
				if (newPlotProvider == plottedProviders.get(i)) {
					index = i;
					break;
				}
			}

			if (index > -1) {

				System.out.println("CSVPlotViewer message: Drawing plot \""
						+ newPlotProvider.getPlotTitle());

				// Get the CSVPlotEditor.
				IEditorPart editorPart = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				CSVPlotEditor editor = (CSVPlotEditor) editorPart;

				// Display the plot in the editor.
				editor.showPlotProvider(newPlotProvider);
			}
		}
		return;
	}

	/**
	 * Removes all plots selected in {@link #plotTreeViewer}.
	 */
	public void removeSelection() {
		// Make sure the viewer's controls have been created.
		if (plotTreeViewer != null) {
			// Get the selection from the plotTreeViewer. It should at least be
			// an IStructuredSelection (a parent interface of TreeSelections).
			ISelection selection = plotTreeViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;

				// Create a List of PlotProviders to be unplotted.
				List<PlotProvider> providers = new ArrayList<PlotProvider>();

				// Loop over the selected elements and add any PlotProviders to
				// the List of PlotProviders to be unplotted.
				for (Iterator<?> iter = structuredSelection.iterator(); iter
						.hasNext();) {
					Object object = iter.next();
					if (object instanceof PlotProvider) {
						providers.add((PlotProvider) object);
					}
				}

				// Remove all of the entries that were selected.
				for (PlotProvider provider : providers) {
					removePlot(provider);
				}
			}
		}
		return;
	}

	/**
	 * Draws all plots selected in {@link #plotTreeViewer}.
	 */
	public void drawSelection() {
		// Get the selection from the plotTreeViewer. It should at least be
		// an IStructuredSelection (a parent interface of TreeSelections).
		ISelection selection = plotTreeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			// Create a List of PlotProviders to be unplotted.
			List<PlotProvider> providers = new ArrayList<PlotProvider>();

			// Loop over the selected elements and add any PlotProviders to
			// the List of PlotProviders to be unplotted.
			for (Iterator<?> iter = structuredSelection.iterator(); iter
					.hasNext();) {
				Object object = iter.next();
				if (object instanceof PlotProvider) {
					providers.add((PlotProvider) object);
				}
			}

			// Remove all of the entries that were selected.
			for (PlotProvider provider : providers) {
				drawPlot(provider);
			}
		}

		return;
	}

	/**
	 * This method is called when the selection in the VizFileViewer has
	 * changed. It is used to listen for changes to the currently selected
	 * VizResource in the {@link VizFileViewer}.
	 * 
	 * @param inResource
	 *            The VizResource in the VizFileViewer to set this object's
	 *            {@link resource} to.
	 */
	public void setResource(VizResource inResource) {
		// Reset the VizResource
		resource = inResource;
		String fileName = "";

		// Get the file set title or file name
		if (resource.getFileSet() != null && resource.getFileSetTitle() != null) {
			fileName = resource.getFileSetTitle();
		} else {
			fileName = resource.getContents().getAbsolutePath();
		}
		System.out.println("CSVPlotViewer message: The "
				+ "selected file from the VizFileViewer is \"" + fileName
				+ "\".");

		// Enable the AddPlotAction.
		addPlotAction.setEnabled(true);

		return;
	}

	/**
	 * A Map that keeps track of IEditorInputs for existing, opened PlotEditors
	 * based on some key shared with the available plots in this PlotViewer.
	 */
	// FIXME - Change this from an IdentityHashMap on the PlotProviders! We
	// should use a regular HashMap and some key value shared with items in the
	// PlotViewer's list.
	private final Map<PlotProvider, IEditorInput> editorInputs = new IdentityHashMap<PlotProvider, IEditorInput>();

	// ---- Implements ISelectionChangedListener ---- //
	/**
	 * This method is called when the selection in the PlotViewer has changed.
	 * It should either open a new plot or re-open an existing one.
	 * 
	 * @param event
	 *            The SelectionChangedEvent that fired this method.
	 */
	public void selectionChanged(SelectionChangedEvent event) {

		// Get the selection from the TreeViewer and see if it's empty.
		ISelection selection = plotTreeViewer.getSelection();
		boolean notEmptySelection = !selection.isEmpty();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			// If a plot was selected from the list, we can try to open a
			// PlotEditor.
			if (notEmptySelection && firstElement instanceof PlotProvider) {
				PlotProvider provider = (PlotProvider) firstElement;

				// See if an IEditorInput has already been created for the
				// selected plot.
				IEditorInput editorInput = editorInputs.get(provider);
				boolean newEditor = false;
				if (editorInput == null) {
					editorInput = new IEditorInput() {
						public Object getAdapter(Class adapter) {
							// TODO Auto-generated method stub
							return null;
						}

						public String getToolTipText() {
							// TODO Auto-generated method stub
							return null;
						}

						public IPersistableElement getPersistable() {
							// TODO Auto-generated method stub
							return null;
						}

						public String getName() {
							// TODO Auto-generated method stub
							return null;
						}

						public ImageDescriptor getImageDescriptor() {
							// TODO Auto-generated method stub
							return null;
						}

						public boolean exists() {
							// TODO Auto-generated method stub
							return false;
						}
					};
					newEditor = true;
					// Store the IEditorInput for reference later in case the
					// same item is selected again.
					editorInputs.put(provider, editorInput);
				}

				// Get the workbench page
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();

				// Open an editor with the specified IEditorInput. If it's a new
				// IEditorInput or the associated editor has been closed, the
				// workbench will automatically create a new editor. Otherwise,
				// an existing one is opened.
				try {
					page.openEditor(editorInput, CSVPlotEditor.ID);
				} catch (PartInitException e) {
					// Complain
					e.printStackTrace();
					Shell shell = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();

					// Throw up an error dialog
					MessageBox errBox = new MessageBox(shell, SWT.ICON_ERROR
							| SWT.OK);
					errBox.setText("CSV Plot Editor Error!");
					errBox.setMessage("Unable to open the CSV Plot Editor!");
					errBox.open();
				}

				// Draw the selection
				if (newEditor) {
					drawSelection();
				}
			}
		}

		// Enable the other actions in this view if possible.
		deletePlotAction.setEnabled(notEmptySelection);
		prevAction.setEnabled(notEmptySelection);
		playAction.setEnabled(notEmptySelection);
		nextAction.setEnabled(notEmptySelection);

		return;
	}

	// ---------------------------------------------- //

	/**
	 * Public method for retrieving the selection in this view.
	 * 
	 * @return The PlotProvider selected in this view.
	 */
	public PlotProvider getSelection() {
		// Get the selection from the plotTreeViewer. It should at least be
		// an IStructuredSelection (a parent interface of TreeSelections).
		ISelection selection = plotTreeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			// Loop over the selected elements and add any PlotProviders to
			// the List of PlotProviders to be unplotted.
			for (Iterator<?> iter = structuredSelection.iterator(); iter
					.hasNext();) {
				Object object = iter.next();
				return ((PlotProvider) object);
			}
		}
		return null;
	}
}
