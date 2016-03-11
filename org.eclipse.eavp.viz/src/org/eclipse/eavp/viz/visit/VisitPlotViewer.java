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
package org.eclipse.eavp.viz.visit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.DeletePlotAction;
import org.eclipse.eavp.viz.IDeletePlotActionViewPart;
import org.eclipse.eavp.viz.VizFileViewer;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.resource.IResource;
import org.eclipse.eavp.viz.service.datastructures.resource.IVizResource;
import org.eclipse.eavp.viz.service.datastructures.resource.VizResourceComponent;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.lbnl.visit.swt.VisItSwtWidget;
import gov.lbnl.visit.swt.widgets.TimeSliderWidget;

/**
 * This class extends the ViewPart class and provides a view in the
 * Visualization Perspective to look at the plots that are currently available.
 * 
 * @author Jay Jay Billings, Taylor Patterson, Jordan H. Deyton
 */
public class VisitPlotViewer extends ViewPart
		implements IDeletePlotActionViewPart, IVizUpdateableListener,
		ISelectionChangedListener, IDoubleClickListener {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(VisitPlotViewer.class);

	/**
	 * The ID for this view
	 */
	public static final String ID = "org.eclipse.eavp.viz.visit.VisitPlotViewer";

	/**
	 * The currently selected VisIt-compatible ICEResource.
	 */
	private IVizResource resource;

	/**
	 * A Map of the currently selected resource's plot Entries keyed on their
	 * integer IDs.
	 */
	private final Map<Integer, VizEntry> entryMap;

	/**
	 * A List of all plot-able Entries.
	 */
	private final List<VizEntry> plotEntries;

	/**
	 * The current Entry that is plotted, or null if none is plotted.
	 */
	private VizEntry plottedEntry;

	/**
	 * A List containing the ICEResource for each of the currently plotted
	 * Entries.
	 */
	private final List<IVizResource> entryResources;

	/**
	 * The active ResourceComponent
	 */
	private VizResourceComponent resourceComponent;

	/**
	 * A list of all the resources ever used by the viewer.
	 */
	private Map<String, IVizResource> resourceMap;

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
		entryMap = new HashMap<Integer, VizEntry>();

		// Initialize the lists for the selected plots.
		plotEntries = new ArrayList<VizEntry>();
		entryResources = new ArrayList<IVizResource>();
		resourceMap = new HashMap<String, IVizResource>();

		// Initialize the Map of variable types to plot types
		varTypePlotTypeMap = new HashMap<String, String[]>();
		varTypePlotTypeMap.put("Materials",
				new String[] { "Boundary", "FilledBoundary" });
		varTypePlotTypeMap.put("Meshes", new String[] { "Mesh" });
		varTypePlotTypeMap.put("Scalars",
				new String[] { "Pseudocolor", "Contour", "Volume" });
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
	@Override
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
		plotTypeCombo
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// Add the selection listener
		plotTypeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedPlotType = plotTypeCombo.getText();
				drawSelection(new StructuredSelection(plottedEntry));
			}
		});

		// Add the TimeSliderWidget.
		timeSlider = new TimeSliderWidget(partComposite, SWT.BORDER);
		// The time slider should grab excess horizontal space, but not the
		// vertical space.
		timeSlider
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Initialize the TreeViewer.
		plotTreeViewer = new TreeViewer(partComposite,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);
		// The TreeViewer should grab all horizontal AND vertical space.
		plotTreeViewer.getControl()
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
	@Override
	public void setFocus() {
		return;
	}

	/**
	 * Refreshes the content in the {@link #plotTreeViewer}.
	 */
	private void refreshPlotTreeViewer() {
		// Sync with the display
		if (plotTreeViewer != null) {
			final TreeViewer plotTreeViewer = this.plotTreeViewer;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (plotTreeViewer != null) {
						// Reset the input for the plotTreeViewer. The viewer
						// just takes an array of Entry objects.
						plotTreeViewer.setInput(plotEntries.toArray());

						plotTreeViewer.refresh();

						// If possible, force a redraw of the TreeViewer's Tree.
						Tree plotTree = plotTreeViewer.getTree();
						if (plotTree != null && !plotTree.isDisposed()) {
							plotTree.redraw();
						}
					}
					return;
				}
			});
		}

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
	public void update(IVizUpdateable component) {

		logger.info("VisitPlotViewer Message: " + "Incoming resource update.");
		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// If possible, reset the plotTreeViewer's input.
				if (plotTreeViewer != null) {

					// TODO Reset the input for the plotTreeViewer.
					// plotTreeViewer.setInput(null);

					logger.info("VisitPlotViewer Message: "
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
	 * Creates the JFace Actions associated with this PlotViewer.
	 */
	private void createActions() {

		// Get the IToolBarManager
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();

		// Create a delete button and add it to the tool bar
		deletePlotAction = new DeletePlotAction(this);
		toolBarManager.add(deletePlotAction);
		deletePlotAction.setEnabled(!plotEntries.isEmpty());

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

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				return;
			}

			@Override
			public void dispose() {
				// No image descriptors or non-textual resources to dispose.
				return;
			}

			@Override
			public boolean hasChildren(Object element) {
				// Currently, we do not have nested elements in the tree.
				return false;
			}

			@Override
			public Object getParent(Object element) {
				// Currently, we do not have nested elements in the tree.
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				Object[] elements;
				if (inputElement instanceof Object[]) {
					elements = (Object[]) inputElement;
				} else {
					elements = new Object[] {};
				}

				return elements;
			}

			@Override
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
				if (element instanceof VizEntry) {
					VizEntry entry = (VizEntry) element;
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
	public IResource getResource() {
		return resource;
	}

	/**
	 * Adds a plot to the current set of VisIt plots. The entry should be a
	 * property of the current file's ICEResource.
	 * 
	 * @param entry
	 *            The Entry for the plot that is to be added.
	 */
	public void addPlot(VizEntry entry) {
		// Make sure the entry and current resource are not null, that the entry
		// is not already plotted, and that the current resource has the exact
		// entry in its properties.
		if (entry != null && resource != null
				&& "false".equals(entry.getValue())
				&& entry == entryMap.get(entry.getId())) {

			// Add this entry to our bookkeeping.
			plotEntries.add(entry);
			entryResources.add(resource);

			// Mark the entry as being plotted.
			entry.setValue("true");

			logger.info("VisitPlotViewer message: Adding plot \""
					+ entry.getName() + "\".");

			// Update the plotViewer.
			refreshPlotTreeViewer();
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
	public void removePlot(VizEntry entry) {
		// Make sure the entry is not null and that it is marked as plotted.
		if (entry != null && "true".equals(entry.getValue())) {
			// Get the index of the entry in the list of plotted entries.
			int index = -1;
			for (int i = 0; i < plotEntries.size(); i++) {
				if (entry == plotEntries.get(i)) {
					index = i;
					break;
				}
			}

			if (index > -1) {
				// Remove the resource and entry from our bookkeeping.
				entryResources.remove(index);
				plotEntries.remove(index);

				logger.info("VisitPlotViewer message: Removing plot \""
						+ entry.getName() + "\".");

				// Mark the plot as not plotted.
				entry.setValue("false");

				// Update the plotViewer.
				refreshPlotTreeViewer();

				// If the deleted plot was the plotted one, we need to clear the
				// plot view.
				if (entry == plottedEntry) {
					// Unset the plotted Entry.
					plottedEntry = null;

					// Activate the VisIt widget so we can clear the plot.
					VisItSwtWidget widget = getPlotWidget();
					widget.activate();
					widget.getViewerMethods().deleteActivePlots();
				}
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
	public void drawPlot(VizEntry entry) {
		// Make sure the entry is not null and that it is marked as plotted.
		if (entry != null && "true".equals(entry.getValue())) {
			// Get the index of the entry in the list of plotted entries.
			int index = -1;
			for (int i = 0; i < plotEntries.size(); i++) {
				if (entry == plotEntries.get(i)) {
					index = i;
					break;
				}
			}

			// Set the viewer to the correct resource
			IVizResource parent = resourceMap.get(entry.getDescription());
			if (parent != null) {
				setResource(parent);
			}

			if (index > -1) {
				// Get the ICEResource associated with this entry.

				logger.info("VisitPlotViewer message: Drawing plot \""
						+ entry.getName() + "\"." + entry.getParent());

				// Store a reference to the plotted Entry.
				plottedEntry = entry;

				// Activate the VisIt widget so we can clear the plot.
				VisItSwtWidget widget = getPlotWidget();
				widget.activate();
				widget.getViewerMethods().deleteActivePlots();

				// TODO - Add some sort of check to use the correct path here.
				String dbPath = "";
				// Use this for local
				if (!resource.isRemote()) {
					dbPath = resource.getPath().getPath();
				}
				// The remote file system only needs the name.
				else {
					dbPath = resource.getName();
				}

				// If this is a Windows system, reformat the path to Windows
				// style by changing the file separators.
				if (System.getProperty("os.name").toLowerCase()
						.contains("windows")) {
					if (dbPath.startsWith("/")) {
						dbPath = dbPath.substring(1);
						dbPath = dbPath.replace("/",
								System.getProperty("file.separator"));
					}
				}

				widget.getViewerMethods().openDatabase(dbPath);

				// Add the plot to the widget.
				String plotType = (plotTypeCombo.getText() != "")
						? plotTypeCombo.getText()
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
	@Override
	public void removeSelection() {
		// Make sure the viewer's controls have been created.
		if (plotTreeViewer != null) {
			// Get the selection from the plotTreeViewer. It should at least be
			// an IStructuredSelection (a parent interface of TreeSelections).
			ISelection selection = plotTreeViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;

				// Create a List of entries to be unplotted.
				List<VizEntry> entries = new ArrayList<VizEntry>();

				// Loop over the selected elements and add any Entry to the List
				// of entries to be unplotted.
				for (Iterator<?> iter = structuredSelection.iterator(); iter
						.hasNext();) {
					Object object = iter.next();
					if (object instanceof VizEntry) {
						entries.add((VizEntry) object);
					}
				}

				// Remove all of the entries that were selected.
				for (VizEntry entry : entries) {
					removePlot(entry);
				}
			}
		}
		return;
	}

	/**
	 * Draws all plots selected in {@link #plotTreeViewer}.
	 */
	public void drawSelection(IStructuredSelection selection) {
		if (selection != null && !selection.isEmpty()) {

			// Create a List of entries to be plotted.
			List<VizEntry> entries = new ArrayList<VizEntry>();

			// Loop over the selected elements and add any Entry to the List
			// of entries to be plotted.
			for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
				Object object = iter.next();
				if (object instanceof VizEntry) {
					entries.add((VizEntry) object);
				}
			}

			// Draw all of the entries that were selected.
			for (VizEntry entry : entries) {
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
		for (VizEntry entry : resource.getProperties()) {
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
	 *            object's {@link #resource} to.
	 */
	public void setResource(IVizResource inResource) {
		// Reset the VizResource
		resource = inResource;
		logger.info("VisitPlotViewer message: The selected file from "
				+ "the VizFileViewer is \"" + resource.getName() + "\".");

		// Add the resource to the map if it is not null and isn't already
		// present
		if (inResource != null) {
			String file = inResource.getDescription();
			if (!resourceMap.containsKey(inResource.getDescription())) {
				resourceMap.put(file, inResource);
			}
		}

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
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		// Update the plot type selection Combo.
		updatePlotTypeCombo();

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
	@Override
	public void doubleClick(DoubleClickEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			drawSelection((IStructuredSelection) selection);
		}
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
			if (element instanceof VizEntry) {
				VizEntry entry = (VizEntry) element;

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

	/**
	 * Gets the widget used to render the plot.
	 * 
	 * @return The VisItSwtWidget in the Plot View/Editor.
	 */
	private VisItSwtWidget getPlotWidget() {
		IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		VisitEditor editor = (VisitEditor) editorPart;
		return editor.getVizWidget();
	}
}
