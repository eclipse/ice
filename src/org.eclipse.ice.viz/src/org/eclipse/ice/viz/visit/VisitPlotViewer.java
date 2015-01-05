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
package org.eclipse.ice.viz.visit;

import gov.lbnl.visit.swt.VisItSwtWidget;
import gov.lbnl.visit.swt.widgets.TimeSliderWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.viz.DeletePlotAction;
import org.eclipse.ice.viz.IDeletePlotActionViewPart;
import org.eclipse.ice.viz.VizFileViewer;
import org.eclipse.ice.viz.VizResource;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;

/**
 * This class extends the ViewPart class and provides a view in the
 * Visualization Perspective to look at the plots that are currently available.
 * 
 * @author Jay Jay Billings, Taylor Patterson, Jordan H. Deyton
 */
public class VisitPlotViewer extends ViewPart implements
		IDeletePlotActionViewPart, IUpdateableListener,
		ISelectionChangedListener, IDoubleClickListener {

	/**
	 * The ID for this view
	 */
	public static final String ID = "org.eclipse.ice.viz.visit.VisitPlotViewer";

	/**
	 * The currently selected VisIt-compatible ICEResource.
	 */
	private VizResource resource;

	/**
	 * A Map of the currently selected resource's plot Entries keyed on their
	 * integer IDs.
	 */
	private final Map<Integer, Entry> entryMap;

	/**
	 * A List of all plotted Entries.
	 */
	private final List<Entry> plottedEntries;

	/**
	 * A List containing the ICEResource for each of the currently plotted
	 * Entries.
	 */
	private final List<VizResource> entryResources;

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
	 * Creates a dialog that lets the user select from the available plots for
	 * the currently selected VisIt file from the {@link VizFileViewer}.
	 */
	private AddVisitPlotAction addPlotAction;

	/**
	 * Removes the selected plot(s) from the PlotViewer.
	 */
	private DeletePlotAction deletePlotAction;

	/**
	 * This action calls a new dialog to pop open a Python command line console.
	 */
	private LaunchPythonScriptDialogAction pythonCLIAction;

	/**
	 * The TimeSliderWidget that provides controls to allow the user to step
	 * through time-dependent data contained in a single plot.
	 */
	private TimeSliderWidget timeSlider;

	/**
	 * The Combo that allows the user to select the plot type for the selection
	 * in the {@link #plotTreeViewer}.
	 */
	private Combo plotTypeCombo;

	/**
	 * A Map of variable types (Materials, Meshes, Scalars, Vectors) to
	 * available plot types (Contour, Mesh, Pseudocolor, etc.).
	 */
	private Map<String, String[]> varTypePlotTypeMap;

	/**
	 * The currently selected plot type.
	 */
	private String selectedPlotType = null;

	/**
	 * The default constructor.
	 */
	public VisitPlotViewer() {

		// Initialize the variables tied to the current VisIt-compatible
		// ICEResource.
		resource = null;
		entryMap = new HashMap<Integer, Entry>();

		// Initialize the lists for the selected plots.
		plottedEntries = new ArrayList<Entry>();
		entryResources = new ArrayList<VizResource>();

		// Initialize the Map of variable types to plot types
		varTypePlotTypeMap = new HashMap<String, String[]>();
		varTypePlotTypeMap.put("Materials", new String[] { "Boundary",
				"FilledBoundary" });
		varTypePlotTypeMap.put("Meshes", new String[] { "Mesh" });
		varTypePlotTypeMap.put("Scalars", new String[] { "Contour",
				"Pseudocolor", "Volume" });
		varTypePlotTypeMap.put("Vectors", new String[] { "Vector" });

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

		// The parent Composite has a FillLayout. We want to display the
		// plotTypeCombo and TimeSliderWidget (a Composite) above the
		// plotTreeViewer, so we must create an intermediate Composite with a
		// GridLayout (1 column) to contain them.
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		Composite partComposite = toolkit.createComposite(parent);
		partComposite.setLayout(new GridLayout(1, true));
		// Dispose the toolkit since we no longer need it.
		toolkit.dispose();

		// Add the Combo for selecting from the available plot types
		plotTypeCombo = new Combo(partComposite, SWT.READ_ONLY);
		plotTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		// Add the selection listener
		plotTypeCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedPlotType = plotTypeCombo.getText();
				drawSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selectedPlotType = plotTypeCombo.getText();
				drawSelection();
			}
		});

		// Add the TimeSliderWidget.
		timeSlider = new TimeSliderWidget(partComposite, SWT.BORDER);
		// The time slider should grab excess horizontal space, but not the
		// vertical space.
		timeSlider
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Initialize the TreeViewer.
		plotTreeViewer = new TreeViewer(partComposite, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		// The TreeViewer should grab all horizontal AND vertical space.
		plotTreeViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		plotTreeViewer.addSelectionChangedListener(this);
		plotTreeViewer.addDoubleClickListener(this);

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
	 * Refreshes the content in the {@link #plotTreeViewer}.
	 */
	private void refreshPlotViewer() {
		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// If possible, reset the plotTreeViewer's input.
				if (plotTreeViewer != null) {

					System.out.println("VisitPlotViewer message: "
							+ "Refreshing TreeViewer.");

					// Reset the input for the plotTreeViewer. The viewer just
					// takes an array of Entry objects.
					plotTreeViewer.setInput(plottedEntries.toArray());

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

		System.out
				.println("VisitPlotViewer Message: Incoming resource update.");
		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// If possible, reset the plotTreeViewer's input.
				if (plotTreeViewer != null) {

					// TODO Reset the input for the plotTreeViewer.
					// plotTreeViewer.setInput(null);

					System.out.println("VisitPlotViewer Message: "
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

		// Create a delete button and add it to the tool bar
		deletePlotAction = new DeletePlotAction(this);
		toolBarManager.add(deletePlotAction);
		deletePlotAction.setEnabled(!plottedEntries.isEmpty());

		// Create an add button and add it to the tool bar
		addPlotAction = new AddVisitPlotAction(this);
		toolBarManager.add(addPlotAction);
		addPlotAction.setEnabled(resource != null);

		// Create the button for selecting and executing a Python script
		pythonCLIAction = new LaunchPythonScriptDialogAction(this);
		toolBarManager.add(pythonCLIAction);

		return;
	}

	/**
	 * Initializes the provided TreeViewer based on the current ICEResource for
	 * this PlotViewer.
	 * 
	 * @param inputTreeViewer
	 *            The TreeViewer that should be configured to display the
	 *            currently selected plots for a VisIt-compatible ICEResource.
	 */
	private void initializeTreeViewer(TreeViewer inputTreeViewer) {

		// Set up the content provider and label provider for the TreeViewer.
		// The input should be of the type Entry[]. Elements should be the
		// entries themselves.

		// Set the content provider, which determines how the input (an Entry[])
		// should produce elements in the TreeViewer.
		inputTreeViewer.setContentProvider(new ITreeContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				return;
			}

			public void dispose() {
				// No image descriptors or non-textual resources to dispose.
				return;
			}

			public boolean hasChildren(Object element) {
				// Currently, we do not have nested elements in the tree.
				return false;
			}

			public Object getParent(Object element) {
				// Currently, we do not have nested elements in the tree.
				return null;
			}

			public Object[] getElements(Object inputElement) {
				Object[] elements;
				if (inputElement instanceof Object[]) {
					elements = (Object[]) inputElement;
				} else {
					elements = new Object[] {};
				}

				return elements;
			}

			public Object[] getChildren(Object parentElement) {
				// Currently, we do not have nested elements in the tree.
				return null;
			}
		});

		// Set up the label provider, which determines what string is displayed
		// for each element in the tree. Currently, this only needs to produce
		// a string for each Entry.
		inputTreeViewer.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Object element = cell.getElement();

				// Get a String from the Entry if possible.
				StyledString styledStr = new StyledString();
				if (element instanceof Entry) {
					Entry entry = (Entry) element;
					// Get the name from the resource
					styledStr.append(entry.getName());
					// Append the path stored as the Entry description
					styledStr.append(" [" + entry.getParent(),
							StyledString.QUALIFIER_STYLER);
					// Append the data type stored as the Entry parent
					styledStr.append("-" + entry.getDescription() + "]",
							StyledString.QUALIFIER_STYLER);
				}
				// If the element isn't an Entry, convert it to a String.
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
	 * Gets the current VisIt-compatible ICEResource that is being used by this
	 * PlotViewer.
	 * 
	 * @return an ICEResource with plots as the Entry properties.
	 */
	public ICEResource getResource() {
		return resource;
	}

	/**
	 * Adds a plot to the current set of VisIt plots. The entry should be a
	 * property of the current file's ICEResource.
	 * 
	 * @param entry
	 *            The Entry for the plot that is to be added.
	 */
	public void addPlot(Entry entry) {
		// Make sure the entry and current resource are not null, that the entry
		// is not already plotted, and that the current resource has the exact
		// entry in its properties.
		if (entry != null && resource != null
				&& "false".equals(entry.getValue())
				&& entry == entryMap.get(entry.getId())) {

			// Add this entry to our bookkeeping.
			plottedEntries.add(entry);
			entryResources.add(resource);

			// Mark the entry as being plotted.
			entry.setValue("true");

			System.out.println("VisitPlotViewer message: Adding plot \""
					+ entry.getName() + "\".");

			// Update the plotViewer.
			refreshPlotViewer();
		}

		return;
	}

	/**
	 * Removes a plot from the current set of VisIt plots. The entry should be a
	 * property of the current file's ICEResource.
	 * 
	 * @param entry
	 *            The Entry for the plot that is to be removed.
	 */
	public void removePlot(Entry entry) {
		// Make sure the entry is not null and that it is marked as plotted.
		if (entry != null && "true".equals(entry.getValue())) {
			// Get the index of the entry in the list of plotted entries.
			int index = -1;
			for (int i = 0; i < plottedEntries.size(); i++) {
				if (entry == plottedEntries.get(i)) {
					index = i;
					break;
				}
			}

			if (index > -1) {
				// Remove the resource and entry from our bookkeeping.
				entryResources.remove(index);
				plottedEntries.remove(index);

				System.out.println("VisitPlotViewer message: Removing plot \""
						+ entry.getName() + "\".");

				// Mark the plot as not plotted.
				entry.setValue("false");

				// FIXME - This definitely needs a better way to access the
				// VisIt widget.
				IEditorPart editorPart = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				VisitEditor editor = (VisitEditor) editorPart;
				VisItSwtWidget widget = editor.getVizWidget();
				widget.activate();

				// Delete an existing plot
				widget.getViewerMethods().deleteActivePlots();

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
	 * @param entry
	 *            The entry to draw with the VisIt widget.
	 */
	public void drawPlot(Entry entry) {
		// Make sure the entry is not null and that it is marked as plotted.
		if (entry != null && "true".equals(entry.getValue())) {
			// Get the index of the entry in the list of plotted entries.
			int index = -1;
			for (int i = 0; i < plottedEntries.size(); i++) {
				if (entry == plottedEntries.get(i)) {
					index = i;
					break;
				}
			}

			if (index > -1) {
				// Get the ICEResource associated with this entry.

				System.out.println("VisitPlotViewer message: Drawing plot \""
						+ entry.getName() + "\"." + entry.getParent());

				// FIXME - This definitely needs a better way to access the
				// VisIt widget.
				IEditorPart editorPart = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				VisitEditor editor = (VisitEditor) editorPart;
				VisItSwtWidget widget = editor.getVizWidget();
				widget.activate();

				// Delete an existing plot
				widget.getViewerMethods().deleteActivePlots();

				// Add the plot to the widget.
				String plotType = (plotTypeCombo.getText() != "") ? plotTypeCombo
						.getText()
						: varTypePlotTypeMap.get(entry.getParent())[0];
				widget.getViewerMethods().addPlot(plotType, entry.getName());

				// Draw the plot using the widget.
				widget.getViewerMethods().drawPlots();

				// Set the TimeSliderWidget's VisIt connection
				timeSlider
						.setVisItSwtConnection(widget.getVisItSwtConnection());
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

				// Create a List of entries to be unplotted.
				List<Entry> entries = new ArrayList<Entry>();

				// Loop over the selected elements and add any Entry to the List
				// of entries to be unplotted.
				for (Iterator<?> iter = structuredSelection.iterator(); iter
						.hasNext();) {
					Object object = iter.next();
					if (object instanceof Entry) {
						entries.add((Entry) object);
					}
				}

				// Remove all of the entries that were selected.
				for (Entry entry : entries) {
					removePlot(entry);
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

			// Create a List of entries to be plotted.
			List<Entry> entries = new ArrayList<Entry>();

			// Loop over the selected elements and add any Entry to the List
			// of entries to be plotted.
			for (Iterator<?> iter = structuredSelection.iterator(); iter
					.hasNext();) {
				Object object = iter.next();
				if (object instanceof Entry) {
					entries.add((Entry) object);
				}
			}

			// Draw all of the entries that were selected.
			for (Entry entry : entries) {
				// Draw the plot
				drawPlot(entry);
			}
		}

		return;
	}

	/**
	 * This method fills in the entryMap based on the current resource.
	 */
	public void setEntryMap() {

		// Build a map of the entries for the current resource
		// keyed on their IDs.
		entryMap.clear();
		for (Entry entry : resource.getProperties()) {
			entryMap.put(entry.getId(), entry);
		}

		return;
	}

	/**
	 * This method is called when the selection in the VizFileViewer has
	 * changed. It is used to listen for changes to the currently selected
	 * VizResource in the {@link VizFileViewer}.
	 * 
	 * @param inResource
	 *            The VizResource in the {@link VizFileViewer} to set this
	 *            object's {@link resource} to.
	 */
	public void setResource(VizResource inResource) {
		// Reset the VizResource
		resource = inResource;
		System.out.println("VisitPlotViewer message: The selected file from "
				+ "the VizFileViewer is \"" + resource.getName() + "\".");

		// Enable the AddPlotAction.
		addPlotAction.setEnabled(true);

		return;
	}

	// ---- Implements ISelectionChangedListener ---- //
	/**
	 * This method is used to draw plots whenever a TreeItem in the
	 * plotTreeViewer is selected. If multi-select is enabled, it draws all of
	 * the selected plots.
	 * 
	 * @param event
	 *            The SelectionChangedEvent that fired this method.
	 */
	public void selectionChanged(SelectionChangedEvent event) {

		// Update the plot type selection Combo.
		updatePlotTypeCombo();

		// Draw the selection on single clicks
		drawSelection();

		// Enable the DeletePlotAction if possible.
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			deletePlotAction.setEnabled(!selection.isEmpty());
		}

		return;
	}

	// ---------------------------------------------- //

	// ---- Implements IDoubleClickListener ---- //
	/**
	 * This method is used to draw plots whenever a TreeItem in the
	 * plotTreeViewer is double-clicked. If multi-select is enabled, it draws
	 * all of the selected plots.
	 * 
	 * @param event
	 *            The DoubleClickEvent that fired this method.
	 */
	public void doubleClick(DoubleClickEvent event) {

		// FIXME Consider using double-clicks to draw in a new window. For now,
		// just draw in the same window.
		// Update the plot type selection Combo.
		updatePlotTypeCombo();

		drawSelection();
	}

	// ----------------------------------------- //

	/**
	 * Update the contents of the plot type selection <code>Combo<\code>.
	 */
	private void updatePlotTypeCombo() {
		// Get the selection from the plotTreeViewer. It should at least be
		// an IStructuredSelection (a parent interface of TreeSelections).
		ISelection selection = plotTreeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			// Just get the first selection from the {@link #plotTreeViewer}.
			Object element = structuredSelection.getFirstElement();
			// Make sure this is an Entry
			if (element instanceof Entry) {
				Entry entry = (Entry) element;

				// Update the contents of the plot type selection Combo based on
				// the selection in the plotTreeViewer.
				String[] plotTypes = varTypePlotTypeMap.get(entry.getParent());
				plotTypeCombo.setItems(plotTypes);

				// Set the default plot type. If the variable type has not
				// changed, the plot type can remain the same. Otherwise, just
				// default to the first available plot type.
				for (int i = 0; i < plotTypes.length; i++) {
					if (plotTypes[i] == selectedPlotType) {
						plotTypeCombo.setText(selectedPlotType);
						return;
					}
				}
				// If we get here, the variable type has changed.
				selectedPlotType = plotTypes[0];
				plotTypeCombo.setText(selectedPlotType);
			}
		}
	}

}
