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

import org.eclipse.ice.client.widgets.mesh.properties.MeshSelection;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.IMeshPart;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.Vertex;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This class extends ViewPart to create a tree of elements (polygons, edges,
 * vertices) in the MeshComponent.
 * 
 * @author Taylor Patterson
 */
public class MeshElementTreeView extends ViewPart implements
		IUpdateableListener, IPartListener2, ISelectionListener,
		ITabbedPropertySheetPageContributor {

	/**
	 * Eclipse view ID
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.MeshElementTreeView";

	/**
	 * The MeshComponent managed by this view.
	 */
	private MeshComponent meshComponent;

	/**
	 * The TreeViewer for mesh elements.
	 */
	private TreeViewer elementTreeViewer;

	/**
	 * The ID of the most recently active item.
	 */
	private int lastFormItemID;

	/**
	 * The constructor.
	 */
	public MeshElementTreeView() {

		// Call the super constructor
		super();

		return;
	}

	/**
	 * This operation sets the MeshComponent that should be used by the
	 * MeshElementTreeView. It also registers the MeshElementTreeView with the
	 * MeshComponent so that it can be notified of state changes through the
	 * IUpdateableListener interface.
	 * 
	 * @param component
	 *            The MeshComponent
	 */
	public void setMeshComponent(MeshComponent component) {
		// begin-user-code

		// Make sure the MeshComponent exists
		if (component != null) {
			// Set the component reference
			meshComponent = component;

			// Register this view with the Component to receive updates
			component.register(this);

			// Update the view
			update(component);
		}

		return;

		// end-user-code
	}

	/**
	 * This operation retrieves the MeshComponent that has been rendered by this
	 * view or null if the component does not exist.
	 * 
	 * @return The MeshComponent or null if the component was not previously
	 *         set.
	 */
	public MeshComponent getMeshComponent() {
		// begin-user-code

		return meshComponent;

		// end-user-code
	}

	/**
	 * This operation overrides the ViewPart.createPartControl method to create
	 * and draw the TreeViewer before registering it as a selection provider and
	 * part listener.
	 * 
	 * @param parent
	 *            The Composite containing this view
	 * 
	 * @see WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Initialize the TreeViewer
		elementTreeViewer = new TreeViewer(parent);

		// Create content and label providers
		initializeTreeViewer(elementTreeViewer);

		// Register this view's TreeViewer as a SelectionProvider
		getSite().setSelectionProvider(elementTreeViewer);

		// Register this view as a part listener
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);

		// Register this view as a SelectionListener to the ICEMeshPage
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(ICEMeshPage.ID, this);

		return;
	}

	/**
	 * This operation creates content and label providers for a TreeViewer.
	 * 
	 * @param inputTreeViewer
	 *            The TreeViewer to have the providers added to.
	 */
	private void initializeTreeViewer(TreeViewer inputTreeViewer) {

		// Set the tree's content provider
		inputTreeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			/**
			 * This function retrieves the elements of the tree.
			 * 
			 * @param inputElement
			 *            The structure containing all of the elements
			 * 
			 * @see ITreeContentProvider#getElements(Object)
			 */
			@Override
			public Object[] getElements(Object inputElement) {

				// Local Declaration
				ArrayList<Polygon> allElements = (ArrayList<Polygon>) inputElement;
				ArrayList<MeshSelection> contents = new ArrayList<MeshSelection>();

				// Wrap the Polygons into PropertySources and add them to
				// the array
				for (Polygon i : allElements) {
					contents.add(new MeshSelection(meshComponent, i));
				}

				return contents.toArray();
			}

			/**
			 * This function retrieves the children of a given tree element.
			 * 
			 * @param parentElement
			 *            The tree element for which children are being
			 *            requested
			 * 
			 * @see ITreeContentProvider#getChildren(Object)
			 */
			@Override
			public Object[] getChildren(Object parentElement) {

				// If the element is a PropertySource
				if (parentElement instanceof MeshSelection) {

					MeshSelection selection = (MeshSelection) parentElement;

					// Load edges and vertices as children of polygons
					ArrayList<MeshSelection> children = new ArrayList<MeshSelection>();

					if (selection.selectedMeshPart instanceof Polygon) {
						Polygon polygon = (Polygon) selection.selectedMeshPart;
						// Add new MeshSelections for the edges.
						for (Edge e : polygon.getEdges()) {
							children.add(new MeshSelection(meshComponent, e));
						}
						// Add new MeshSelections for the vertices.
						for (Vertex v : polygon.getVertices()) {
							children.add(new MeshSelection(meshComponent, v));
						}
					}

					return children.toArray();
				}

				return null;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			/**
			 * This function checks whether or not the tree element has any
			 * children. This should only return true for Polygons.
			 * 
			 * @param element
			 *            The tree element to investigate
			 * 
			 * @return True if this is of type Polygon. False otherwise.
			 * 
			 * @see ITreeContentProvider#hasChildren(Object)
			 */
			@Override
			public boolean hasChildren(Object element) {

				// Only selected Polygons will have children.
				return (element instanceof MeshSelection && ((MeshSelection) element).selectedMeshPart instanceof Polygon);
			}
		});

		// Add a label provider to properly label the mesh elements
		inputTreeViewer.setLabelProvider(new LabelProvider() {

			/**
			 * The String to contain the text of the label.
			 */
			String label = "";

			/**
			 * A function to create text labels for elements in the
			 * MeshElementTreeView.
			 * 
			 * @param element
			 *            The tree element to be labeled
			 * 
			 * @return The String to serve as the tree element label
			 * 
			 * @see LabelProvider#getText(Object)
			 */
			@Override
			public String getText(Object element) {

				// Only set a label if it is a PropertySource. Otherwise we
				// shouldn't need it.... I think.
				if (element instanceof MeshSelection) {

					// Get the wrapped IMeshPart.
					IMeshPart meshPart = ((MeshSelection) element).selectedMeshPart;

					// Cast the IMeshPart to an ICEObject and set the label text
					// from its name and ID.
					ICEObject object = (ICEObject) meshPart;
					label = object.getName() + " " + object.getId();

					return label;
				}
				return element.toString();
			}
		});

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		return;
	}

	/**
	 * Update elementTreeViewer when new elements are added to the mesh.
	 * 
	 * @param component
	 * 
	 * @see IUpdateableListener#update(IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		System.out.println("ICEMeshPage Message: " + "Mesh changed!");

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() { // Just do a blanket update - no need to check
								// the component
				if (elementTreeViewer != null) {
					System.out.println("MeshElementTreeView Message: "
							+ "Updating element view.");

					// Set the tree content
					if (meshComponent != null) {
						elementTreeViewer.setInput(meshComponent.getPolygons());
					}

					// Refresh the view
					elementTreeViewer.refresh();
					elementTreeViewer.getTree().redraw();
				}
			}
		});

		return;
	}

	/**
	 * This function is called whenever a Workbench part is closed. Here, we are
	 * only interested if the part is an ICEFormEditor. Get the Form from the
	 * editor. Clear the elements from the tree view.
	 * 
	 * @param partRef
	 *            The workbench part calling this function.
	 * 
	 * @see IPartListener2#partClosed(IWorkbenchPartReference)
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {

		System.out.println("MeshElementTreeView Message: Called partClosed("
				+ partRef.getId() + ")");

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			// Get the closed editor
			ICEFormEditor activeEditor = (ICEFormEditor) partRef.getPart(false);
			// Get the form from the editor
			Form activeForm = ((ICEFormInput) activeEditor.getEditorInput())
					.getForm();
			// If this is the most recently active form, clear the TreeViewer.
			if (activeForm.getItemID() == lastFormItemID) {
				elementTreeViewer.setInput(null);
				elementTreeViewer.refresh();
				elementTreeViewer.getTree().redraw();
			}
		}

		return;
	}

	/**
	 * This function is called whenever a Workbench part gains focus. Here, we
	 * are only interested if the part is an ICEFormEditor. A call to this
	 * function will occur prior to a part being closed, so just keep track of
	 * that form's id.
	 * 
	 * @param partRef
	 *            The workbench part calling this function.
	 * 
	 * @see IPartListener2#partActivated(IWorkbenchPartReference)
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

		System.out.println("MeshElementTreeView Message: Called partActivated("
				+ partRef.getId() + ")");

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			// Get the activated editor
			ICEFormEditor activeEditor = (ICEFormEditor) partRef.getPart(false);
			// Pull the form from the editor
			Form activeForm = ((ICEFormInput) activeEditor.getEditorInput())
					.getForm();
			// Record the ID of this form
			lastFormItemID = activeForm.getItemID();
		}

		return;
	}

	/**
	 * This function is called whenever a Workbench part gains focus. Here, we
	 * are only interested if the part is an ICEFormEditor. A call to this
	 * function will occur prior to a part being closed, so just keep track of
	 * that form's id.
	 * 
	 * @param partRef
	 *            The workbench part calling this function.
	 * 
	 * @see IPartListener2#partHidden(IWorkbenchPartReference)
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {

		System.out.println("MeshElementTreeView Message: Called partHidden("
				+ partRef.getId() + ")");

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			// Get the hidden editor
			ICEFormEditor activeEditor = (ICEFormEditor) partRef.getPart(false);
			// Get the form from the editor
			Form activeForm = ((ICEFormInput) activeEditor.getEditorInput())
					.getForm();
			// Record the ID of this form
			lastFormItemID = activeForm.getItemID();
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPartListener2#partBroughtToTop(IWorkbenchPartReference)
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Do nothing
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPartListener2#partDeactivated(IWorkbenchPartReference)
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Do nothing
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPartListener2#partOpened(IWorkbenchPartReference)
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// Do nothing
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPartListener2#partVisible(IWorkbenchPartReference)
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Do nothing
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPartListener2#partInputChanged(IWorkbenchPartReference)
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Do nothing
		return;
	}

	/**
	 * This operation overrides the default/abstract implementation of
	 * ISelectionListener.selectionChanged to capture selections made in the
	 * ICEMeshPage and highlight the corresponding element in the
	 * MeshElementTreeView.
	 * 
	 * @param part
	 *            The IWorkbenchPart that called this function.
	 * @param selection
	 *            The ISelection chosen in the part parameter.
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		//Get the selection made in the ICEMeshPage.

		return;
	}

	public void revealSelectedEdge(int id) {
		elementTreeViewer.expandAll();

	}

	public void revealSelectedVertex(int id) {
		elementTreeViewer.expandAll();
	}

	// ---- These methods are required for tabbed properties. ---- //
	public String getContributorId() {
		return getSite().getId();
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class) {
			return new TabbedPropertySheetPage(this);
		}
		return super.getAdapter(adapter);
	}
	// ----------------------------------------------------------- //

}