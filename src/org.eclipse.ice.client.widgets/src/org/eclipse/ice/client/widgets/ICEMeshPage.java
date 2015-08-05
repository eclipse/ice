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
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.geometry.TransformationView;
import org.eclipse.ice.client.widgets.jme.ViewFactory;
import org.eclipse.ice.client.widgets.mesh.properties.MeshSelection;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.viz.service.jme3.mesh.IMeshSelectionListener;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppState;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateMode;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateModeFactory;
import org.eclipse.ice.viz.service.jme3.mesh.MeshSelectionManager;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateModeFactory.Mode;
import org.eclipse.ice.viz.service.mesh.datastructures.BezierEdge;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Hex;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshPartVisitor;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.PolynomialEdge;
import org.eclipse.ice.viz.service.mesh.datastructures.Quad;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;
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
import org.eclipse.ui.forms.widgets.Form;
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
		IMeshSelectionListener, ISelectionProvider,
		ITabbedPropertySheetPageContributor {

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
	 * The jME mesh view (a <code>MeshAppState</code>) contained in this page.
	 */
	private MeshAppState meshView;

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

		// Setup the layout
		form.getBody().setLayout(new FillLayout());

		// Show the view related views
		try {
			getSite().getWorkbenchWindow().getActivePage()
					.showView(MeshElementTreeView.ID);
			getSite().getWorkbenchWindow().getActivePage()
					.showView(TransformationView.ID);
		} catch (PartInitException e) {
			logger.error(getClass().getName() + " Exception!",e);
		}

		// Create the geometry composite - get the parent
		Form pageForm = managedForm.getForm().getForm();
		Composite parent = pageForm.getBody();

		// Set the layout
		parent.setLayout(new GridLayout(1, false));

		// Create the tool bar and buttons for the view
		toolBar = new ToolBar(parent, SWT.NONE);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		actionToolBarManager = new ToolBarManager(toolBar);

		// Use a ViewFactory to create a jME mesh view.
		ViewFactory factory = new ViewFactory();
		meshView = factory.createMeshView(meshComp);
		// Render the mesh inside the parent Composite. We have to set its
		// GridData so it will fill all available space!
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		meshView.createComposite(parent).setLayoutData(gridData);

		// The MeshPage should also listen for changes to the MeshApplication's
		// current selection.
		meshView.getSelectionManager().addMeshApplicationListener(this);

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
		// Use a MeshAppStateModeFactory to get the available modes and create
		// ActionTrees for each one to go in the Mode menu.
		MeshAppStateModeFactory factory = meshView.getModeFactory();
		for (Mode type : factory.getAvailableModes()) {
			final MeshAppStateMode mode = factory.getMode(type);
			action = new Action() {
				@Override
				public void run() {
					meshView.setMode(mode);
				}
			};
			// Set the Action's text and tool tip.
			action.setText(mode.getName());
			action.setToolTipText(mode.getDescription());
			modesActionTree.add(new ActionTree(action));
		}
		actions.add(modesActionTree);

		// Create the drop down to reset the camera placement or zoom
		// TODO create the camera reset action

		// Create the toggle switch to show or hide the heads-up display
		action = new org.eclipse.ice.viz.service.jme3.widgets.ToggleHUDAction(meshView);
		toggleHUDActionTree = new ActionTree(action);
		actions.add(toggleHUDActionTree);

		// Create the toggle switch to show or hide the axes.
		action = new org.eclipse.ice.viz.service.jme3.widgets.ToggleAxesAction(meshView);
		toggleAxesActionTree = new ActionTree(action);
		actions.add(toggleAxesActionTree);

		// Create the button to delete mesh elements.
		action = new Action() {
			@Override
			public void run() {
				meshView.getSelectionManager().deleteSelection();
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
		if (part.getSite().getId().equals(MeshElementTreeView.ID)) {

			// Get the mesh selection manager from the app.
			MeshSelectionManager selectionManager = meshView
					.getSelectionManager();

			// Reset any existing selection data in the MeshApplication
			selectionManager.clearSelection();

			// Get the array of all selections in the Mesh Elements view
			Object[] treeSelections = ((ITreeSelection) selection).toArray();

			// Initialize lists of IDs for vertices, edges, and polygons.
			final List<Integer> vertexIds = new ArrayList<Integer>();
			final List<Integer> edgeIds = new ArrayList<Integer>();
			final List<Integer> polygonIds = new ArrayList<Integer>();

			// Create a visitor to populate the above lists of IDs
			IMeshPartVisitor visitor = new IMeshPartVisitor() {

				@Override
				public void visit(Vertex vertex) {
					vertexIds.add(vertex.getId());
				}

				@Override
				public void visit(PolynomialEdge edge) {
					visit((Edge) edge);
				}

				@Override
				public void visit(BezierEdge edge) {
					visit((Edge) edge);
				}

				@Override
				public void visit(Edge edge) {
					edgeIds.add(edge.getId());
				}

				@Override
				public void visit(Hex hex) {
					visit((Polygon) hex);
				}

				@Override
				public void visit(Quad quad) {
					visit((Polygon) quad);
				}

				@Override
				public void visit(Polygon polygon) {
					polygonIds.add(polygon.getId());
				}

				@Override
				public void visit(Object object) {
					// Do nothing.
				}

				@Override
				public void visit(VizMeshComponent mesh) {
					// Do nothing.
				}
			};

			// Get each element from the selection and add the ID for the
			// corresponding vertex/edge/polygon to one of the above lists.
			// These lists will be sent to the selection manager later.
			for (Object element : treeSelections) {

				if (element instanceof MeshSelection) {
					MeshSelection meshSelection = (MeshSelection) element;
					meshSelection.selectedMeshPart.acceptMeshVisitor(visitor);
				}
			}

			// Select all of the vertices, edges, and polygons.
			selectionManager.selectVertices(vertexIds);
			selectionManager.selectEdges(edgeIds);
			selectionManager.selectPolygons(polygonIds);
		}

		return;
	}

	/**
	 * This method should make the appropriate ISelectionProvider calls to send
	 * the recent update to the MeshApplication's selection to the other
	 * ISelectionListeners.
	 */
	@Override
	public void selectionChanged() {

		return;
	}

	// ---- Implements ISelectionProvider ---- //
	/**
	 * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
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
			((ISelectionChangedListener) list[i])
					.selectionChanged(new SelectionChangedEvent(this,
							getSelection()));
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
