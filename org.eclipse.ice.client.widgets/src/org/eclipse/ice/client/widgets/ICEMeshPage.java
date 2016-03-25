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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.eavp.viz.service.geometry.widgets.TransformationView;
import org.eclipse.eavp.viz.service.geometry.widgets.TransformationView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This class extends ICEFormPage to provide an area for the jME3 canvas and a
 * toolbar for creating and modifying the mesh.
 * 
 * @author Taylor Patterson, Jordan H. Deyton
 */
public class ICEMeshPage extends ICEFormPage implements ISelectionListener,
		ISelectionProvider, ITabbedPropertySheetPageContributor {

	/**
	 * Eclipse view ID
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.ICEMeshPage";

	/**
	 * The data structure that provides data for this view
	 */
	private MeshComponent meshComp;

	/**
	 * The tree view that displays mesh elements in this page.
	 */
	private MeshElementTreeView meshElementTreeView;

	/**
	 * The List of custom JFace Actions. This List and the ActionTrees therein
	 * are used to populate the primary ToolBar of the parent
	 * AnalysisToolComposite and the actionMenuManager, which can be used to
	 * display any type of Menu (including a context Menu or right-click Menu),
	 */
	private List<ActionTree> actions;

	/**
	 * A MenuManager built from the List of ActionTrees (composed of JFace
	 * Actions).
	 */
	private MenuManager actionMenuManager;

	/**
	 * A ToolBarManager built from the List of ActionTrees (composed of JFace
	 * Actions).
	 */
	private ToolBarManager actionToolBarManager;

	/**
	 * The SWT tool bar to contain the actions.
	 */
	private ToolBar toolBar;

	/**
	 * The collection of listeners to notify of selection changes.
	 */
	private ListenerList listeners;

	/**
	 * The collection of parts selected in the MeshApplication.
	 */
	private ArrayList<ICEObject> selectedMeshParts;

	/**
	 * The canvas on which the mesh is visualized.
	 */
	private IMeshVizCanvas canvas;

	/**
	 * The constructor
	 * 
	 * @param editor
	 *            The FormEditor for which the page should be constructed.
	 * @param id
	 *            The id of the page.
	 * @param title
	 *            The title of the page.
	 */
	public ICEMeshPage(FormEditor editor, String id, String title) {

		// Just call ICEFormPage's constructor
		super(editor, id, title);

		// Create the list of actions (these go in the ToolBar and the
		// rightClickMenu).
		actions = new ArrayList<ActionTree>();

		// Initialize the Menu- and ToolBarManagers.
		actionToolBarManager = new ToolBarManager();
		actionMenuManager = new MenuManager();

		// Initialize the collection of selection listeners
		listeners = new ListenerList();

		return;
	}

	/**
	 * This operation retrieves the MeshComponent displayed by this page or null
	 * if the component does not exist.
	 * 
	 * @return The MeshComponent displayed by this page.
	 */
	public MeshComponent getMeshComponent() {

		return meshComp;

	}

	/**
	 * This operation sets the MeshComponent that should be used by the
	 * ICEMeshPage.
	 * 
	 * @param component
	 *            The MeshComponent for the page
	 */
	public void setMeshComponent(MeshComponent component) {

		// Make sure the ResourceComponent exists
		if (component != null) {
			// Set the component reference
			meshComp = component;
		}

		return;
	}

	/**
	 * This operation overrides the default/abstract implementation of
	 * FormPage.createFormContents to create the contents of the ICEMeshPage and
	 * set the page layout.
	 * 
	 * @param managedForm
	 *            The Form widget on which the ICEMeshPage exists.
	 */
	@Override
	public void createFormContent(IManagedForm managedForm) {

		// Local Declarations
		final ScrolledForm form = managedForm.getForm();
		GridLayout layout = new GridLayout();

		// Setup the layout and layout data
		layout.numColumns = 1;
		form.getBody().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		form.getBody().setLayout(new FillLayout());

		// Opening the views in order to interact with the geometryEditor
		try {

			getSite().getWorkbenchWindow().getActivePage()
					.showView(MeshElementTreeView.ID);
			getSite().getWorkbenchWindow().getActivePage()
					.showView(TransformationView.ID);

		} catch (PartInitException e) {
			logger.error(getClass().getName() + " Exception!", e);
		}

		// Create the geometry composite - get the parent
		org.eclipse.ui.forms.widgets.Form pageForm = managedForm.getForm()
				.getForm();
		Composite parent = pageForm.getBody();

		// Set the layout
		parent.setLayout(new GridLayout(1, false));

		// Create the tool bar and buttons for the view
		toolBar = new ToolBar(parent, SWT.NONE);
		toolBar.setLayoutData(
				new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		actionToolBarManager = new ToolBarManager(toolBar);

		// Get JME3 Geometry service from factory
		IVizServiceFactory factory = editor.getVizServiceFactory();
		IVizService service = factory.get("ICE JavaFX Mesh Editor");

		// Composite editorComposite = new Composite(parent, SWT.NONE);

		// Create and draw geometry canvas
		try {
			canvas = (IMeshVizCanvas) service.createCanvas(meshComp.getMesh());
			Composite composite = canvas.draw(parent);
			composite.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true));

		} catch (Exception e) {
			logger.error("Error creating Mesh Canvas with Mesh Service.", e);
		}

		// The MeshPage should also listen for changes to the MeshApplication's
		// current selection.
		// FIXME This currently isn't doing anything as it just invokes the stub
		// function selectionChanged() when an update triggers
		// canvas.registerListener(this);

		// Now that we have a MeshApplication, we need to create the Actions
		// that fill the ToolBar with configuration settings for the
		// application.
		createActions();
		updateActionManagers();

		// Set the MeshComponent for the element tree view
		meshElementTreeView = (MeshElementTreeView) getSite()
				.getWorkbenchWindow().getActivePage()
				.findView(MeshElementTreeView.ID);
		meshElementTreeView.setMeshComponent(meshComp);

		// Register this page as a SelectionListener to the MeshElementTreeView
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(MeshElementTreeView.ID, this);

		return;

	}

	/**
	 * Create and add Actions to the tool bar and right-click menu for this
	 * page.
	 */
	private void createActions() {

		Action action;
		ActionTree modesActionTree;
		ActionTree toggleHUDActionTree;
		ActionTree toggleAxesActionTree;
		ActionTree deleteActionTree;

		// Create the drop down for switching between add and modify modes
		modesActionTree = new ActionTree("Mode");

		// Create the option to set edit mode
		action = new Action() {
			@Override
			public void run() {
				canvas.setEditMode(true);
			}
		};

		// Set the Action's text
		action.setText("Edit Elements");
		modesActionTree.add(new ActionTree(action));

		// Create the option to set add mode
		action = new Action() {
			@Override
			public void run() {
				canvas.setEditMode(false);
			}
		};

		// Set the Action's text
		action.setText("Add Elements");
		modesActionTree.add(new ActionTree(action));

		actions.add(modesActionTree);

		// Create the drop down to reset the camera placement or zoom
		// TODO create the camera reset action

		// TODO Add actions for toggling the hud based on JME3/JavaFX specific
		// implementation
		// Create the toggle switch to show or hide the heads-up display
		action = new Action() {

			@Override
			public void run() {
				canvas.setVisibleHUD(!canvas.HUDIsVisible());
			}
		};

		action.setText("Toggle HUD");
		toggleHUDActionTree = new ActionTree(action);

		actions.add(toggleHUDActionTree);

		// TODO Add the action for toggling the axes based on JME3/JavaFX
		// specific implementation
		// Create the toggle switch to show or hide the axes.
		action = new Action() {

			@Override
			public void run() {
				canvas.setVisibleAxis(!canvas.AxisAreVisible());
			}
		};

		action.setText("Toggle Axis");
		toggleAxesActionTree = new ActionTree(action);
		actions.add(toggleAxesActionTree);

		// Create the button to delete mesh elements.
		action = new Action() {
			@Override
			public void run() {
				canvas.deleteSelection();
			}
		};
		action.setText("Delete");
		action.setToolTipText("Remove the selected element from the mesh");
		deleteActionTree = new ActionTree(action);
		actions.add(deleteActionTree);

		return;
	}

	/**
	 * Updates the ToolBarManager and MenuManager to pull JFace Actions from
	 * {@link #actions}. This method should be used if actions need to be added
	 * to or removed from the List.
	 */
	private void updateActionManagers() {
		logger.info("ICEMeshPage message:"
				+ " Updating action ToolBar-/MenuManagers.");

		// Clear the managers.
		actionToolBarManager.removeAll();
		actionMenuManager.removeAll();

		// Add all of the Actions from the ActionTree list.
		for (ActionTree action : actions) {
			actionToolBarManager.add(action.getContributionItem());
			actionMenuManager.add(action.getContributionItem());
		}

		// Force an update for them.
		actionToolBarManager.update(true);
		actionMenuManager.update(true);

		return;
	}

	/**
	 * This operation overrides the default/abstract implementation of
	 * ISelectionListener.selectionChanged to capture selections made in the
	 * MeshElementTreeView and highlight the corresponding element in the jME3
	 * canvas.
	 * 
	 * @param part
	 *            The IWorkbenchPart that called this function.
	 * @param selection
	 *            The ISelection chosen in the part parameter.
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// Get the selection made in the MeshElementTreeView.
		if (part.getSite().getId().equals(MeshElementTreeView.ID) && canvas != 
				null) {

			// Get the array of all selections in the Mesh Elements view
			Object[] treeSelections = ((ITreeSelection) selection).toArray();

			// Set the canvas's selection to match the selection from the tree
			canvas.setSelection(treeSelections);
		}

		return;
	}

	// ---- Implements ISelectionProvider ---- //
	/**
	 * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
	 */
	@Override
	public void addSelectionChangedListener(
			ISelectionChangedListener listener) {
		listeners.add(listener);
		return;
	}

	/**
	 * @see ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return new StructuredSelection(selectedMeshParts.toArray());
	}

	/**
	 * @see ISelectionProvider#removeSelectionChangedListener(ISelectionChangedListener)
	 */
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		listeners.remove(listener);
		return;
	}

	/**
	 * @see ISelectionProvider#setSelection(ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		Object[] list = listeners.getListeners();
		for (int i = 0; i < list.length; i++) {
			((ISelectionChangedListener) list[i]).selectionChanged(
					new SelectionChangedEvent(this, getSelection()));
		}
	}

	// --------------------------------------- //

	// ---- These methods are required for tabbed properties. ---- //
	@Override
	public String getContributorId() {
		return getSite().getId();
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class) {
			return new TabbedPropertySheetPage(this);
		}
		return super.getAdapter(adapter);
	}
	// ----------------------------------------------------------- //

}
