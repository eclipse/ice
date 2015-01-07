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
package org.eclipse.ice.client.widgets.moose;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.ice.client.common.AddNodeTreeAction;
import org.eclipse.ice.client.common.DeleteNodeTreeAction;
import org.eclipse.ice.client.common.RenameNodeTreeAction;
import org.eclipse.ice.client.common.TreeCompositeContentProvider;
import org.eclipse.ice.client.common.TreeCompositeLabelProvider;
import org.eclipse.ice.client.common.TreeCompositeViewer;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormInput;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class MOOSETreeCompositeView extends TreeCompositeViewer implements
		IPartListener2, ISelectionChangedListener {

	/**
	 * The ID of this view. This should be the same as its ID in the
	 * <code>plugin.xml</code>.
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.moose.MOOSETreeCompositeView";

	/**
	 * The <code>ICEFormEditor</code> using this view.
	 */
	private ICEFormEditor editor;

	/**
	 * The <code>Form</code> contained by the {@link #formInput}.
	 */
	private Form form;

	/**
	 * A reference to the currently active {@link MOOSEFormEditor}.
	 */
	private IWorkbenchPartReference activeEditorRef;

	/**
	 * This wraps the SWT <code>Combo</code> for selecting the MOOSE tool
	 * responsible for the tree content.
	 */
	private ComboViewer appCombo;

	/**
	 * This maps the values available in the {@link #appCombo} to their actual
	 * values as interpreted by the MOOSE Model Builder. This allows us to
	 * customize the text that displayed in the app selection widget.
	 */
	private final Map<String, String> allowedValues;

	/**
	 * The {@link Entry} corresponding to the available apps in the MOOSE Model
	 * Builder.
	 */
	private Entry appsEntry;

	/**
	 * The default constructor.
	 */
	public MOOSETreeCompositeView() {
		// Create the map of allowed app values for the app selection widget.
		// Use a LinkedHashMap so that the order they are added is reflected in
		// the map.
		allowedValues = new LinkedHashMap<String, String>();
	}

	/**
	 * This method is used to compute the preferred width of the app selection widget.
	 * @return The preferred width of the {@link #appCombo}.
	 */
	private int getPreferredComboWidth() {
		Combo combo = appCombo.getCombo();
		
		// TODO If we listen to the ComboViewer properly, we can
		// optimize this so that the longest string (and extent width) is only
		// determined when the viewer has changed.

		// Determine the longest string in the Combo list.
		String longestString = "MOOSE app...";
		
		// This code introduces a separate issue where, when the contents of the
		// Combo are changed, the width of the Combo cannot be updated properly.
		// The problem is that the CTabFolder's client area width is not
		// reported accurately (presumably, there is a missing pack() or
		// refresh() somewhere, but it's a needle in a haystack). This causes
		// The max available width reported in the refreshToolBar() method to be
		// less than it really is, so the Combo is too small. However, the next
		// resize event fixes this, so the Combo "jumps" in size.
		
		// FIXME
//		int maxStringLength = 0;
//		for (String string : combo.getItems()) {
//			int length = string.length();
//			if (length > maxStringLength) {
//				maxStringLength = length;
//				longestString = string;
//			}
//		}

		// Compute the width of the default MOOSE App Combo string based on
		// the Control's font. Note: Add some extra pixels for extraneous
		// widget features like the dropdown arrow.
		GC gc = new GC(combo);
		int extentWidth = gc.textExtent(longestString).x + 40;
		gc.dispose();

		// This computes the default size of the widget, including the
		// length of the above string + the edges of the widget and its
		// arrow.
		return combo.computeSize(extentWidth, SWT.DEFAULT, true).x;
	}

	/**
	 * Refreshes the view's <code>ToolBar</code>. This is used to force items in
	 * the <code>ToolBar</code> to wrap when there is not enough space.
	 */
	private void refreshToolBar() {
		// Get the ToolBar and its container.
		ToolBarManager manager = (ToolBarManager) getViewSite().getActionBars()
				.getToolBarManager();
		ToolBar toolBar = manager.getControl();
		Composite parent = toolBar.getParent();

		// Get the ToolBar's RowData (we may need to create it). The
		// RowData affects where the ancestor CTabFolder draws the
		// ToolBar's container and the view's client Composite.
		RowData rowData = (RowData) toolBar.getLayoutData();
		if (rowData == null) {
			rowData = new RowData();
			toolBar.setLayoutData(rowData);
		}

		// Note: The 5 lines of code below produce some flicker when the
		// ToolBar starts to wrap. This is likely because of the call to
		// parent.setSize(defaultSize), but I have not found a better
		// way to determine maxWidth.

		// I had trouble determining the maximum width available to
		// render the ToolBar (The relationship between the size of the
		// ToolBar and its parent Composite is strange, and they are not
		// at all independent.). I resorted to the following:
		//
		// 1) At each resize event, restore the parent Composite to the
		// default, fully unwrapped size of the ToolBar (first 4 lines).
		// 2) Then determine the available width of the parent Composite
		// via getClientArea().
		// 3) If the available width is not enough, restrict the
		// ToolBar's size by its RowData.width (causes the wrap, affects
		// the computed size of the parent) and update the size of the
		// parent (this forces a re-layout).

		// Determine the maximum horizontal space we actually have
		// available for the ToolBar.
		Point defaultSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		rowData.width = SWT.DEFAULT;
		parent.setSize(defaultSize);
		int maxWidth = parent.getClientArea().width;

		// TODO We could perhaps optimize this so that the loop can be
		// eliminated. But we may also need to create a custom
		// ControlContribution, because the default implementations
		// creates the ToolItem and forgets it.

		// Determine the app widget's corresponding ToolItem.
		Combo combo = appCombo.getCombo();
		for (ToolItem toolItem : toolBar.getItems()) {
			if (combo == toolItem.getControl()) {
				// Update the width of the ToolItem.
				int width = getPreferredComboWidth();
				if (maxWidth < width) {
					width = maxWidth;
				}
				toolItem.setWidth(width);
				break;
			}
		}

		// If the available space is not enough, we need to shrink the
		// ToolBar. We use the RowData for this, and we have to force
		// the container to resize itself appropriately.
		if (maxWidth < defaultSize.x) {
			// FIXME Neither of these calls do anything with gtk, but
			// they work on Mac and Windows.
			rowData.width = maxWidth;
			parent.setSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		}

		return;
	}

	/**
	 * Overrides the parent class's behavior to add the {@link #appCombo} to the
	 * view's <code>ToolBar</code>.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// ---- Add the app selection widget to the view ToolBar. ---- //
		// Get the TreeCompositeViewer's ToolBarManager.
		ToolBarManager toolBarManager = (ToolBarManager) getViewSite()
				.getActionBars().getToolBarManager();
		// Add the MOOSE app selection combo box to the toolbar before the other
		// actions.
		toolBarManager.add(new ControlContribution(
				"org.eclipse.ice.client.widgets.moose.appComboContribution") {

			@Override
			protected Control createControl(Composite parent) {

				// Create the MOOSE app selection widget. The style bits make
				// the Combo widget read-only (can't enter in text like a Text
				// widget) and gives a vertical and horizontal scroll bar to it
				// if necessary.
				appCombo = new ComboViewer(parent, SWT.READ_ONLY | SWT.V_SCROLL
						| SWT.H_SCROLL);

				// Set the content provider so that we can pass in a collection
				// as the viewer's input.
				appCombo.setContentProvider(new ArrayContentProvider());

				// Listen for changes to the app selection widget's selection.
				appCombo.addSelectionChangedListener(MOOSETreeCompositeView.this);

				return appCombo.getControl();
			}

			@Override
			public int computeWidth(Control control) {
				return getPreferredComboWidth();
			}
		});
		// ----------------------------------------------------------- //

		// ---- ControlListener for wrapping the ToolBar contents. ---- //
		// The code below hacks around RCP to gain access to the ToolBar and its
		// container (a CTabFolder). When the container is resized, it needs to
		// adjust the size of the ToolBar so that its items wrap.

		// We have to add a listener for resize events to the ToolBar's ancestor
		// CTabFolder since it receives the resize events. The 3 lines below
		// determine the CTabFolder.
		Control control = toolBarManager.getControl().getParent();
		for (; !(control instanceof CTabFolder); control = control.getParent()) {
			// Do nothing... the loop determines the parent!
		}
		control.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				refreshToolBar();
			}
		});
		// ------------------------------------------------------------ //

		// Proceed with the default behavior.
		super.createPartControl(parent);

		// Register with the PartService to tell which MOOSE Model Builder is
		// currently active.
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);

		return;
	}

	/**
	 * Overrides the default <code>ToolBar</code> <code>Action</code>s. Note
	 * that the add action is simpler than the default because adding to the
	 * root node is not allowed.
	 */
	@Override
	protected void createActions() {

		// Create the action for adding child exemplars as new nodes in the
		// tree. The default action is acceptable.
		addAction = new AddNodeTreeAction();
		addAction.setPartId(ID);

		// Create the action for deleting child nodes from the tree. We should
		// only be allowed to delete nodes below the first level of blocks, so
		// we should check that the node has a grandparent.
		deleteAction = new DeleteNodeTreeAction() {
			@Override
			protected boolean canRun() {
				// Note that the default behavior only checks that the node has
				// a parent.
				return super.canRun()
						&& getSelectedNode().getParent().getParent() != null;
			}
		};
		deleteAction.setPartId(ID);

		// Create the action for renaming tree nodes. We should only be allowed
		// to delete nodes below the first level of blocks, so check that it has
		// a grandparent.
		renameAction = new RenameNodeTreeAction() {
			@Override
			protected boolean canRun() {
				TreeComposite selectedNode = getSelectedNode();
				return super.canRun() && selectedNode.getParent() != null
						&& selectedNode.getParent().getParent() != null;
			}
		};
		renameAction.setPartId(ID);

		return;
	}

	/**
	 * Overrides the default viewer to add an additional feature: When a parent
	 * node is unchecked, all of its child nodes are unchecked.
	 */
	@Override
	protected TreeViewer createViewer(Composite parent) {
		TreeViewer treeViewer = null;

		if (parent != null) {
			// Initialize the TreeViewer. Note: We create a CheckboxTreeViewer!
			final CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(
					parent, SWT.VIRTUAL | SWT.MULTI | SWT.H_SCROLL
							| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
			treeViewer = checkboxTreeViewer;

			// Set and configure the content and label providers
			treeViewer.setContentProvider(new TreeCompositeContentProvider(
					this, parentMap));
			treeViewer.setLabelProvider(new TreeCompositeLabelProvider());

			// Add a provider to tell the viewer when elements should be
			// checked. This is NOT default behavior.
			MOOSETreeCheckStateManager checkManager;
			checkManager = new MOOSETreeCheckStateManager();
			checkboxTreeViewer.setCheckStateProvider(checkManager);
			checkboxTreeViewer.addCheckStateListener(checkManager);
		}

		return treeViewer;
	}

	// ---- Implements IPartListener2 ---- //
	// This class implements this interface so it can listen to the currently
	// active MOOSE Model Builder. When the Model Builder changes, the app
	// selection widget and the tree's content need to be updated.

	/**
	 * This is called when an editor has been activated. If that part is a
	 * {@link MOOSEFormEditor}, mark it as the active editor and refresh the
	 * content of this view.
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			// Set this editor as the active one.
			activeEditorRef = partRef;
			
			// Update the content of the MOOSE app selection widget.
			updateAppSelectionWidget();

			// Update the TreeViewer with the current TreeComposite.
			setInput((TreeComposite) form
					.getComponent(MOOSEModel.mooseTreeCompositeId));
		}

		return;
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	/**
	 * This is called when an editor has been closed. This method clears the
	 * {@link #activeEditorRef} and empties the contents of this view if the
	 * currently active editor was closed.
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {

		if (partRef == activeEditorRef) {

			// Clear the selection widget.
			if (!appCombo.getControl().isDisposed()) {
				allowedValues.clear();
				appCombo.setInput(allowedValues.keySet());

				// Clear the input to the TreeViewer.
				super.setInput(new TreeComposite());
			}

			// Unset the reference to the currently active reference since it
			// was just closed.
			activeEditorRef = null;
		}

		return;
	}

	/**
	 * This is called when an editor has been "deactivated". This method clears
	 * the {@link #activeEditorRef} if the currently active editor was disabled.
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	// ----------------------------------- //

	// ---- Implements ISelectionChangedListener ---- //
	/**
	 * This view listens for selection changes from the {@link #appCombo}. When
	 * the selection has changed, it needs to tell the {@link #form} to update
	 * based on the selected MOOSE app.
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		String selection = null;

		// Get the string from the SelectionChangedEvent.
		if (event != null) {
			ISelection iSelection = event.getSelection();
			if (iSelection != null
					&& iSelection instanceof IStructuredSelection) {
				Object element = ((IStructuredSelection) iSelection)
						.getFirstElement();
				if (element != null) {
					selection = element.toString();
				}
			}
		}

		if (selection != null) {
			// Set the Entry selection to match the selection made in
			// the Combo.
			String appStr = allowedValues.get(selection);
			appsEntry.setValue(appStr);

			MOOSEFormEditor mooseEditor = (MOOSEFormEditor) editor;
			
			// If the selection is for RELAP-7, create a Plant View page
			// if one doesn't already exist. If the selection is NOT for
			// RELAP-7, delete any existing Plant View page.
			if ("relap".equals(appStr)) {
				mooseEditor.addPlantPage();
			} else {
				mooseEditor.removePlantPage();
			}

			// TODO There may be more apps that use the mesh page!
			if ("bison".equals(appStr)) {
				mooseEditor.addMeshPage();
			} else {
				mooseEditor.removeMeshPage();
			}

			// Refresh the TreeViewer's selection for the add and
			// delete actions.
			addAction.selectionChanged(getSite().getPart(), new ISelection() {
				@Override
				public boolean isEmpty() {
					return true;
				}
			});

			// Notify the editor's listeners that it's underlying Form
			// has changed and update the tree.
			editor.notifyUpdateListeners();
		}

		return;
	}

	// ---------------------------------------------- //

	/**
	 * Updates the contents of the MOOSE app selection widget.
	 */
	private void updateAppSelectionWidget() {

		// We cannot proceed if the selection widget is either uninitialized or
		// disposed.
		if (appCombo != null && !appCombo.getControl().isDisposed()
				&& activeEditorRef != null) {

			// The text used by the Entry to say that no app is selected yet.
			String unselectedText = "MOOSE app...";

			// ---- Get the list of MOOSE apps from the Model Builder. ---- //
			// Clear the previous list of allowed values.
			allowedValues.clear();

			// Get the active MOOSE Model Builder.
			editor = (ICEFormEditor) activeEditorRef.getPart(false);
			ICEFormInput formInput = (ICEFormInput) editor.getEditorInput();
			form = formInput.getForm();

			// Get its Entry that contains the available apps.
			DataComponent dataComp = (DataComponent) form
					.getComponent(MOOSEModel.fileDataComponentId);
			appsEntry = dataComp.retrieveEntry("MOOSE-Based Application");

			// Customize the text used when no app is selected.
			allowedValues.put("MOOSE app...", unselectedText);

			// Add all of the available MOOSE apps. Prepend the current editor
			// name and ID to the combo text.
			String prefix = form.getName() + " " + form.getId() + " - ";
			for (String appName : appsEntry.getAllowedValues()) {
				if (!unselectedText.equals(appName)) {
					String comboContentStr = prefix
							+ ("relap".equals(appName) ? appName.toUpperCase()
									+ "-7" : appName.toUpperCase());
					allowedValues.put(comboContentStr, appName);
				}
			}
			// ------------------------------------------------------------ //

			// Update the allowed values in the app selection widget.
			appCombo.setInput(allowedValues.keySet());

			// ---- Set the current value in the app selection widget. ---- //
			// Determine the string in the allowed values that should be
			// displayed.
			String currentApp = appsEntry.getValue();
			if (currentApp.equals(unselectedText)) {
				currentApp = "MOOSE app...";
			} else {
				currentApp = prefix
						+ ("relap".equals(currentApp) ? currentApp
								.toUpperCase() + "-7" : currentApp
								.toUpperCase());
			}

			// Set the selection for the app selection widget.
			appCombo.setSelection(new StructuredSelection(currentApp));
			// ------------------------------------------------------------ //

			MOOSEFormEditor mooseEditor = (MOOSEFormEditor) editor;
			
			// If the selection is for RELAP-7, create a Plant View page
			// if one doesn't already exist. If the selection is NOT for
			// RELAP-7, delete any existing Plant View page.
			String appStr = appsEntry.getValue();
			if ("relap".equals(appStr)) {
				mooseEditor.addPlantPage();
			} else {
				mooseEditor.removePlantPage();
			}
			// TODO There may be more apps that use the mesh page!
			if ("bison".equals(appStr)) {
				mooseEditor.addMeshPage();
			} else {
				mooseEditor.removeMeshPage();
			}

			// Refresh the ToolBar with the updated app selection widget.
			appCombo.refresh();
			refreshToolBar();
		}

		return;
	}

}
