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

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.viz.PlotEntryContentProvider;
import org.eclipse.ice.viz.VizFileViewer;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import visit.java.client.FileInfo;

/**
 * This Action opens a dialog that allows the user to pick from plots available
 * in the selected file in the {@link VizFileViewer}.
 * 
 * @author Taylor Patterson, Jordan H. Deyton
 */
public class AddVisitPlotAction extends Action {

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
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "add.png");
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
		ICEResource resource = plotViewer.getResource();

		// If we pulled an ICEResource from the selection, we may proceed.
		if (resource != null) {
			System.out.println("AddVisitPlotAction message: "
					+ "The currently selected resource is \""
					+ resource.getName() + "\".");

			// Create the plot entries and populate the plotViewer's entryMap
			createPlotEntries(resource);
			plotViewer.setEntryMap();

			// Get the Entries for the ICEResource. The Entries include the
			// four categories of available plots (meshes, scalars, vectors,
			// and materials) and the available plots for them.
			ArrayList<Entry> entries = resource.getProperties();

			// Set up the ITreeContentProvider and the ILabelProvider for
			// the TreeViewer that will go inside the dialog.
			final Map<String, List<Entry>> groupMap = new HashMap<String, List<Entry>>(
					4);
			final Map<Integer, Boolean> valueMap = new HashMap<Integer, Boolean>(
					entries.size());
			List<String> groupNames = new ArrayList<String>(4);
			for (Entry entry : entries) {
				// Add the Entry to the proper group list. Create a new list
				// if necessary.
				String parentName = entry.getParent();
				List<Entry> groupEntries = groupMap.get(parentName);
				if (groupEntries == null) {
					groupEntries = new ArrayList<Entry>();
					groupMap.put(parentName, groupEntries);
					groupNames.add(parentName);
				}
				groupEntries.add(entry);

				// Store a value in the map for the entry's current value.
				valueMap.put(entry.getId(), "true".equals(entry.getValue()));
			}

			ITreeContentProvider contentProvider = new ITreeContentProvider() {
				public void dispose() {
					// Do nothing.
				}

				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
					// Do nothing.
				}

				public Object[] getElements(Object inputElement) {
					Object[] elements;

					// The input is expected to be a List of group names.
					if (inputElement instanceof Object[]) {
						elements = (Object[]) inputElement;
					} else {
						elements = new Object[] {};
					}

					return elements;
				}

				public Object[] getChildren(Object parentElement) {
					Object[] children;

					// The parent nodes should be the parent name Strings.
					// Look up the List of entries for that group.
					if (parentElement instanceof String) {
						String parentName = (String) parentElement;
						children = groupMap.get(parentName).toArray();
					} else {
						children = new Object[] {};
					}

					return children;
				}

				public Object getParent(Object element) {
					Object parent = null;

					// Only entries should have parents. Just return the
					// parent's name.
					if (element instanceof Entry) {
						parent = ((Entry) element).getParent();
					}

					return parent;
				}

				public boolean hasChildren(Object element) {
					boolean hasChildren = false;

					// The parent nodes should be the parent name Strings.
					// Return true only if the List of entries for the
					// parent/group name is not empty.
					if (element instanceof String) {
						String parentName = (String) element;
						hasChildren = !(groupMap.get(parentName).isEmpty());
					}
					return hasChildren;
				}
			};
			ILabelProvider labelProvider = new LabelProvider() {
				@Override
				public String getText(Object element) {

					// Get a String from the ICEResource if possible.
					String text;
					if (element instanceof Entry) {
						text = ((Entry) element).getName();
					}
					// If the element isn't an ICEResource, convert it to a
					// String.
					else {
						text = element.toString();
					}

					return text;
				}
			};

			// Create an ElementTreeSelectionDialog to allow the user to
			// select from the plots available in the currently-selected
			// file from the VizFileViewer.
			ExposedCheckTreeDialog dialog = new ExposedCheckTreeDialog(shell,
					labelProvider, contentProvider);

			// Set the input. This is just the list of parent/group names.
			dialog.setInput(groupNames.toArray());

			// Get the initial selection of entries based on the entries'
			// values.
			List<Entry> initialSelection = new ArrayList<Entry>();
			for (Entry entry : resource.getProperties()) {
				if ("true".equals(entry.getValue())) {
					initialSelection.add(entry);
				}
			}
			dialog.setInitialSelections(initialSelection.toArray());

			// Configure the dialog so that the root nodes in the TreeViewer
			// are checked only if all child nodes are checked.
			dialog.setContainerMode(true);
			dialog.setTitle("Select Plots");
			dialog.setMessage("Select the variables to plot. Checked "
					+ "variables are already plotted.");

			// Create an ICheckStateListener to listen to for check events
			// in the dialog. It should populate the map of values with the
			// current selection.
			ICheckStateListener listener = new ICheckStateListener() {
				public void checkStateChanged(CheckStateChangedEvent event) {
					// Get the element whose check state has changed.
					Object element = event.getElement();
					// If the element was an Entry, then one of the plots
					// was either checked or unchecked.
					if (element instanceof Entry) {
						Entry entry = (Entry) element;
						valueMap.put(entry.getId(), event.getChecked());
					}
					// If the element was a String, then an entire group of
					// plots was either checked or unchecked.
					else {
						String groupName = (String) element;
						boolean checked = event.getChecked();
						for (Entry entry : groupMap.get(groupName)) {
							valueMap.put(entry.getId(), checked);
						}
					}
				}
			};
			dialog.setCheckStateListener(listener);

			// When the "Select All" and "Deselect All" buttons are clicked,
			// the ICheckStateListener is not notified. Add listeners to
			// these buttons so that the map of values can be updated
			// accordingly.
			dialog.setSelectAllListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					for (Integer id : valueMap.keySet()) {
						valueMap.put(id, true);
					}
				}
			});
			dialog.setDeselectAllListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					for (Integer id : valueMap.keySet()) {
						valueMap.put(id, false);
					}
				}
			});

			// Configure the dialog to block user input until it is
			// dismissed. Then open it and check its result.
			if (dialog.open() == Window.OK) {

				// Loop over the entries to determine which ones should be
				// added or removed from the PlotViewer.
				for (Entry entry : entries) {
					// See if the entry has been checked by comparing its
					// value to its element's state in the TreeViewer.
					boolean checked = valueMap.get(entry.getId());
					if ("true".equals(entry.getValue()) != checked) {
						if (checked) {
							// The entry was checked.
							plotViewer.addPlot(entry);
						} else {
							// The entry was unchecked.
							plotViewer.removePlot(entry);
						}
					}
				}
			} else {
				System.out.println("AddPlotAction message: "
						+ "No plot selected.");
			}
		}

		return;
	}

	/**
	 * This is a sub-class of {@link CheckedTreeSelectionDialog} that (a)
	 * exposes the dialog's {@link CheckboxTreeViewer} to this class and (b)
	 * allows this class to add an {@link ICheckStateListener} to the viewer.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class ExposedCheckTreeDialog extends CheckedTreeSelectionDialog {

		/**
		 * An ICheckStateListener.
		 */
		private ICheckStateListener listener;

		/**
		 * This listener can be used to do something when the dialog's
		 * "Select All" button is clicked.
		 */
		private SelectionListener selectAllListener;

		/**
		 * This listener can be used to do something when the dialog's
		 * "Deselect All" button is clicked.
		 */
		private SelectionListener deselectAllListener;

		/**
		 * The default constructor.
		 * 
		 * @see CheckedTreeSelectionDialog#CheckedTreeSelectionDialog(Shell,
		 *      ILabelProvider, ITreeContentProvider)
		 */
		public ExposedCheckTreeDialog(Shell parent,
				ILabelProvider labelProvider,
				ITreeContentProvider contentProvider) {
			super(parent, labelProvider, contentProvider);
		}

		/**
		 * Same as the default constructor, but this includes a style bit.
		 * 
		 * @see CheckedTreeSelectionDialog#CheckedTreeSelectionDialog(Shell,
		 *      ILabelProvider, ITreeContentProvider, int)
		 */
		public ExposedCheckTreeDialog(Shell parent,
				ILabelProvider labelProvider,
				ITreeContentProvider contentProvider, int style) {
			super(parent, labelProvider, contentProvider, style);
		}

		/**
		 * Sets an ICheckStateListener to listen for check events in the
		 * dialog's CheckboxTreeViewer.
		 * 
		 * @param listener
		 *            The ICheckStateListener that will listen to the dialog's
		 *            CheckboxTreeViewer.
		 */
		public void setCheckStateListener(ICheckStateListener listener) {
			this.listener = listener;
		}

		/**
		 * Sets a SelectionListener that will fire when the dialog's
		 * "Select All" button is clicked.
		 * 
		 * @param listener
		 *            The new "Select All" SelectionListener.
		 */
		public void setSelectAllListener(SelectionListener listener) {
			selectAllListener = listener;
		}

		/**
		 * Sets a SelectionListener that will fire when the dialog's
		 * "Deselect All" button is clicked.
		 * 
		 * @param listener
		 *            The new "Deselect All" SelectionListener.
		 */
		public void setDeselectAllListener(SelectionListener listener) {
			deselectAllListener = listener;
		}

		/**
		 * Supplements the default behavior by adding a custom
		 * ICheckStateListener if one is set via {@link #}.
		 */
		@Override
		protected CheckboxTreeViewer createTreeViewer(Composite parent) {
			CheckboxTreeViewer viewer = super.createTreeViewer(parent);
			if (listener != null) {
				viewer.addCheckStateListener(listener);
			}
			return viewer;
		}

		/**
		 * Supplements the default behavior by adding listeners to the "Select
		 * All" and "Deselect All" buttons.
		 */
		@Override
		protected Composite createSelectionButtons(Composite composite) {
			Composite buttonComposite = super.createSelectionButtons(composite);

			Button button;

			// Add the listener to the "Select All" button.
			button = getButton(IDialogConstants.SELECT_ALL_ID);
			if (button != null && selectAllListener != null) {
				button.addSelectionListener(selectAllListener);
			}
			// Add the listener to the "Deselect All" button.
			button = getButton(IDialogConstants.DESELECT_ALL_ID);
			if (button != null && deselectAllListener != null) {
				button.addSelectionListener(deselectAllListener);
			}

			return buttonComposite;
		}

		/**
		 * Exposes the super class' operation so the Action can gain access to
		 * the TreeViewer.
		 */
		@Override
		public CheckboxTreeViewer getTreeViewer() {
			return super.getTreeViewer();
		}

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
	private ArrayList<Entry> createPlotEntries(ICEResource resource) {

		ArrayList<Entry> entries;

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
				if (!((VizResource) resource).isRemote()) {
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
			entries = new ArrayList<Entry>();
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
	private void createPlotEntryGroup(String groupName,
			List<String> childNames, List<Entry> entries, String parentFile) {
		if (groupName != null && childNames != null && entries != null) {
			Entry entry;
			IEntryContentProvider entryContent;

			// Create the child entries. We can re-use the same content provider
			// for the children, so create one.
			entryContent = new PlotEntryContentProvider(groupName);
			for (String child : childNames) {
				entry = new Entry(entryContent);
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
