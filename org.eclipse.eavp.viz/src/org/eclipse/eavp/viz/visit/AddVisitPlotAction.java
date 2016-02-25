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
 *   Jordan Deyton - replaced the selection dialog with something simpler
 *******************************************************************************/
package org.eclipse.eavp.viz.visit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.eavp.viz.PlotEntryContentProvider;
import org.eclipse.eavp.viz.VizFileViewer;
import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.resource.IResource;
import org.eclipse.eavp.viz.service.datastructures.resource.IVizResource;
import org.eclipse.eavp.viz.service.widgets.TreeSelectionDialogProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.lbnl.visit.swt.VisItSwtWidget;
import visit.java.client.FileInfo;

/**
 * This Action opens a dialog that allows the user to pick from plots available
 * in the selected file in the {@link VizFileViewer}.
 * 
 * @author Taylor Patterson, Jordan H. Deyton
 */
public class AddVisitPlotAction extends Action {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AddVisitPlotAction.class);

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public AddVisitPlotAction(ViewPart parent) {

		viewer = parent;

		// Set the action's tool tip text.
		setToolTipText("Add a plot to the list");

		// Set the action's image (the green plus button for adding).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path(
				"icons" + System.getProperty("file.separator") + "add.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {

		// Get the Shell of the workbench
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		// Get the editor part
		IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		// If the editor is null, there is no chance VisIt has been initialized.
		if (editorPart == null
				|| !editorPart.getSite().getId().equals(VisitEditor.ID)) {
			// Present an error dialog and return
			MessageDialog.openError(shell, "VisIt Connection Error",
					"Please connect to a running VisIt client or "
							+ "select a previously opened VisIt Editor prior "
							+ "to attempting to open a file.");
			return;
		}

		// Get the VisItSWTWidget from the editor
		VisitEditor editor = (VisitEditor) editorPart;
		VisItSwtWidget widget = editor.getVizWidget();

		// Make sure the widget is initialized
		if (widget == null || !widget.hasInitialized()) {
			// Present an error message and return
			MessageDialog.openError(shell, "VisIt Not Initialized",
					"Please connect to a running VisIt client prior to "
							+ "attempting to open a file.");
			return;
		}

		// Get the viewer as a PlotViewer.
		VisitPlotViewer plotViewer = (VisitPlotViewer) viewer;

		// Get the ICEResource used by the PlotViewer.
		IResource resource = plotViewer.getResource();

		// If we pulled an ICEResource from the selection, we may proceed.
		if (resource != null) {
			logger.info("AddVisitPlotAction message: "
					+ "The currently selected resource is \""
					+ resource.getName() + "\".");

			// Create the plot entries and populate the plotViewer's entryMap
			createPlotEntries(resource);
			plotViewer.setEntryMap();

			// Get the Entries for the ICEResource. The Entries include the
			// four categories of available plots (meshes, scalars, vectors,
			// and materials) and the available plots for them.
			ArrayList<VizEntry> entries = resource.getProperties();

			// Set up the ITreeContentProvider and the ILabelProvider for
			// the TreeViewer that will go inside the dialog.
			final Map<String, List<VizEntry>> groupMap = new TreeMap<String, List<VizEntry>>();
			for (VizEntry entry : entries) {
				// Add the Entry to the proper group list. Create a new list
				// if necessary.
				String parentName = entry.getParent();
				List<VizEntry> groupEntries = groupMap.get(parentName);
				if (groupEntries == null) {
					groupEntries = new ArrayList<VizEntry>();
					groupMap.put(parentName, groupEntries);
				}
				groupEntries.add(entry);
			}

			// Create an ElementTreeSelectionDialog to allow the user to
			// select from the plots available in the currently-selected
			// file from the VizFileViewer.
			TreeSelectionDialogProvider provider = new TreeSelectionDialogProvider() {
				@Override
				public Object[] getChildren(Object parent) {
					final Object[] children;
					// The root input is the map of plottable features keyed on
					// their type.
					if (parent instanceof Map<?, ?>) {
						// Return the available plot types/categories.
						children = ((Map<?, ?>) parent).keySet().toArray();
					}
					// For the plot type/category, return the available plots.
					else if (parent instanceof String) {
						children = groupMap.get(parent.toString()).toArray();
					} else {
						children = new Object[0];
					}
					return children;
				}

				@Override
				public String getText(Object element) {
					// Get a String from the ICEResource if possible.
					final String text;
					if (element instanceof VizEntry) {
						text = ((VizEntry) element).getName();
					}
					// If the element isn't an ICEResource, convert it to a
					// String.
					else {
						text = element.toString();
					}
					return text;
				}

				@Override
				public boolean isSelected(Object element) {
					// Leaf nodes are only selected if they are currently marked
					// as plotted.
					return element instanceof VizEntry
							? "true".equals(((VizEntry) element).getValue())
							: false;
				}
			};

			// Customize the dialog.
			provider.setTitle("Select Plots");
			provider.setMessage("Select the variables to plot. Checked "
					+ "variables are already plotted.");

			// Open the and check its result. If selected plots were changed,
			// update the plot viewer.
			if (provider.openDialog(shell, groupMap, true) == Window.OK) {
				// Add all newly selected plots.
				for (Object element : provider.getSelectedLeafElements()) {
					if (element instanceof VizEntry) {
						plotViewer.addPlot((VizEntry) element);
					}
				}
				// Remove all unselected plots.
				for (Object element : provider.getUnselectedLeafElements()) {
					if (element instanceof VizEntry) {
						plotViewer.removePlot((VizEntry) element);
					}
				}
			} else {
				logger.info("AddPlotAction message: "
						+ "Plot selection unchanged.");
			}
		}

		return;
	}

	/**
	 * Gets the List of Entries for the plots of a VisIt-compatible ICEResource.
	 * If the Entries do not exist, then this method tries to create them by
	 * opening up the file's database with the VisIt widget.
	 * 
	 * @param resource
	 *            The VisIt-compatible ICEResource.
	 * @return A List of Entries corresponding to the available plots in the
	 *         ICEResource.
	 */
	private ArrayList<VizEntry> createPlotEntries(IResource resource) {

		ArrayList<VizEntry> entries;

		if (resource != null) {
			// Get the ICEResource's List of entries. If it's empty, we need to
			// add new entries to it.
			entries = resource.getProperties();
			if (entries.isEmpty()) {
				// We need to create entries for each group of plots. There
				// are four total: meshes, scalars, vectors, and materials.
				// Then we need to add the available plots for each by
				// querying the resource's database and FileInfo.

				// ---- Open the database for the resource. ---- //
				// Get the VisIt widget, open the resource's database, and
				// get the widget's FileInfo so we can get the available
				// plots.

				// FIXME - This definitely needs a better way to access the
				// VisIt widget.
				IEditorPart editorPart = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				VisitEditor editor = (VisitEditor) editorPart;
				VisItSwtWidget widget = editor.getVizWidget();

				// TODO - Add some sort of check to use the correct path here.
				String dbPath = "";
				// Use this for local
				if (!((IVizResource) resource).isRemote()) {
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
				FileInfo fileInfo = widget.getViewerMethods().getDatabaseInfo();
				// --------------------------------------------- //

				// Create the groups for meshes, scalars, vectors, and
				// materials. The method automatically adds them to the entry
				// list.
				String parentFile = dbPath;
				createPlotEntryGroup("Meshes", fileInfo.getMeshes(), entries,
						parentFile);
				createPlotEntryGroup("Scalars", fileInfo.getScalars(), entries,
						parentFile);
				createPlotEntryGroup("Vectors", fileInfo.getVectors(), entries,
						parentFile);
				createPlotEntryGroup("Materials", fileInfo.getMaterials(),
						entries, parentFile);
			}
		} else {
			entries = new ArrayList<VizEntry>();
		}

		return entries;
	}

	/**
	 * This method creates Entries for a group and adds them to a provided List
	 * of Entries.
	 * 
	 * @param groupName
	 *            The name of the group, e.g., "Meshes", "Scalars", etc.
	 * @param childNames
	 *            A List of names for the Entries.
	 * @param entries
	 *            A List of Entries to which the children should be added.
	 * @param parentFile
	 *            The filename from which this plot originated.
	 */
	private void createPlotEntryGroup(String groupName, List<String> childNames,
			List<VizEntry> entries, String parentFile) {
		if (groupName != null && childNames != null && entries != null) {
			VizEntry entry;
			IVizEntryContentProvider entryContent;

			// Create the child entries. We can re-use the same content provider
			// for the children, so create one.
			entryContent = new PlotEntryContentProvider(groupName);
			for (String child : childNames) {
				entry = new VizEntry(entryContent);
				entry.setName(child);
				entry.setId(entries.size() + 1);
				// FIXME using the entry description to keep track of the parent
				// filename.
				entry.setDescription(parentFile);
				entries.add(entry);
			}
		}

		return;
	}

}
