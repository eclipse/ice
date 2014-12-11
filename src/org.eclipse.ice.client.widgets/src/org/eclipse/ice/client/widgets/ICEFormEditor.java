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
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ice.client.common.TreeCompositeViewer;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.iclient.uiwidgets.IObservableWidget;
import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ICEFormEditor is an Eclipse FormEditor subclass that renders and displays
 * a Form. It is also observable, as described by the IObservableWidget
 * interface, and it dispatches updates to IUpdateEventListeners and
 * IProcessEventListeners.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEFormEditor extends SharedHeaderFormEditor implements
		IComponentVisitor, IObservableWidget, IUpdateableListener {

	/**
	 * ID for Eclipse
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.ICEFormEditor";

	/**
	 * Dirty state for Eclipse
	 */
	private boolean dirty = false;

	/**
	 * The Component Map. This map is used to organize the Components in the
	 * Form by type. The type is the key and is a string equal to one of "data,"
	 * "output," "matrix," "masterDetails", "table," "geometry," "shape" or
	 * "tree," or reactor. The value is a list that stores all components of
	 * that type. This is a simulated multimap.
	 */
	protected HashMap<String, ArrayList<Component>> componentMap = new HashMap<String, ArrayList<Component>>();

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICEFormInput ICEFormInput;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The form for this FormEditor retrieved from the ICEFormInput.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Form iceDataForm;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the action that should be performed when the Form is
	 * processed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String processName;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The SWT drop-down menu used in action processing.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Combo processDropDown;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The SWT button used in action processing.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Button goButton;

	/**
	 * <p>
	 * The SWT button used to cancel a process request.
	 * </p>
	 */
	protected Button cancelButton;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of IProcessEventListeners that have subscribed to the parent ICE
	 * UIWidget of this Eclipse Editor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IProcessEventListener> processListeners;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of IUpdateEventListeners that have subscribed to the parent ICE
	 * UIWidget of this Eclipse Editor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IUpdateEventListener> updateListeners;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ISimpleResourceProvider that should be used to load ICEResources for
	 * the user.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ISimpleResourceProvider resourceProvider;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ICEOutputPage.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ICEResourcePage resourceComponentPage;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This class' ICEFormPage that displays the GeometryEditor powered by JME3.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ICEGeometryPage geometryPage;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This class' ICEFormPage that displays the MeshEditor powered by JME3.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	// TODO Add to model if necessary.
	protected ICEMeshPage meshPage;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEFormEditor() {
		// begin-user-code

		// Setup listener lists
		updateListeners = new ArrayList<IUpdateEventListener>();
		processListeners = new ArrayList<IProcessEventListener>();

		// Setup the component map
		componentMap.put("data", new ArrayList<Component>());
		componentMap.put("output", new ArrayList<Component>());
		componentMap.put("table", new ArrayList<Component>());
		componentMap.put("matrix", new ArrayList<Component>());
		componentMap.put("masterDetails", new ArrayList<Component>());
		componentMap.put("shape", new ArrayList<Component>());
		componentMap.put("geometry", new ArrayList<Component>());
		componentMap.put("mesh", new ArrayList<Component>());
		componentMap.put("tree", new ArrayList<Component>());
		componentMap.put("reactor", new ArrayList<Component>());
		componentMap.put("emf", new ArrayList<Component>());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation changes the dirty state of the FormEditor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param value
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	void setDirty(boolean value) {
		// begin-user-code

		// Set the dirty value and fire a property change
		dirty = value;
		firePropertyChange(PROP_DIRTY);

		// end-user-code
	}

	/**
	 * This operation sets the focus to this editor and also updates the
	 * TreeCompositeViewer, if required.
	 */
	@Override
	public void setFocus() {

		// Call the super class' version of this because we definitely want
		// whatever it does.
		super.setFocus();

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation adds a Component to the map with the specified key. It is
	 * called by the visit() operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            The Component to insert into the map of Components.
	 *            </p>
	 * @param tag
	 *            <p>
	 *            The tag that identifies the type of the Component, equal to
	 *            one of "unspecified" or "output."
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void addComponentToMap(Component component, String tag) {
		// begin-user-code

		// Local Declarations
		ArrayList<Component> components = null;

		// Add the DataComponent to the map
		if (component != null) {
			// Grab the list of DataComponents
			components = this.componentMap.get(tag);
			// Add the Component
			components.add(component);
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates the ICEOutputPage that will show any output files
	 * created by ICE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The ICEOutputPage that should be rendered as part of the Form.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICEFormPage createResourceComponentPage() {
		// begin-user-code

		// Local Declarations
		ResourceComponent resourceComponent = null;

		// Get the ResourceComponent and create the ICEOutput page. There
		// should
		// only be one output page.
		if (!(componentMap.get("output").isEmpty())) {
			resourceComponent = (ResourceComponent) (componentMap.get("output")
					.get(0));
			if (resourceComponent != null) {
				// Make the page
				resourceComponentPage = new ICEResourcePage(this,
						resourceComponent.getName(),
						resourceComponent.getName());
				// Set the ResourceComponent
				resourceComponentPage.setResourceComponent(resourceComponent);
			}
		}

		return resourceComponentPage;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates an ICEMasterDetailsPage which will show data in a
	 * form consistent with the master-details pattern. It uses
	 * MasterDetailsComponents from ICE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The Form pages, one for each MasterDetailsComponent.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<ICEFormPage> createMasterDetailsComponentPages() {
		// begin-user-code

		// Local Declarations
		ArrayList<ICEFormPage> masterDetailsPages = new ArrayList<ICEFormPage>();
		MasterDetailsComponent masterDetailsComponent = null;

		// Get the MasterDetailsComponent and create the MasterDetails page.
		if (!(componentMap.get("masterDetails").isEmpty())) {
			masterDetailsComponent = (MasterDetailsComponent) (componentMap
					.get("masterDetails").get(0));
			if (masterDetailsComponent != null) {
				// Get the name
				String name = masterDetailsComponent.getName();
				// Make the page
				ICEMasterDetailsPage iCEMasterDetailsPage = new ICEMasterDetailsPage(
						this, "MDPid", name);

				// Set the MasterDetailsComponent
				iCEMasterDetailsPage
						.setMasterDetailsComponent(masterDetailsComponent);

				masterDetailsPages.add(iCEMasterDetailsPage);
			}

		}

		return masterDetailsPages;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation posts a status message to the ICEFormEditor should be
	 * displayed to the user or system viewing the widget. It is a simple
	 * string.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param statusMessage
	 *            <p>
	 *            The status message.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void updateStatus(String statusMessage) {
		// begin-user-code

		// Local Declarations
		final String message = statusMessage;

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Post the message to the update manager
				if (getHeaderForm() != null) {
					final IMessageManager messageManager = getHeaderForm()
							.getMessageManager();
					messageManager.addMessage("statusUpdate", message, null,
							IMessageProvider.INFORMATION);
				}
			}
		});

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates a GeometryPage to display shapes powered with JME3. Also gives
	 * the geometryComponent to provide data to display.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICEFormPage createGeometryPage() {
		// begin-user-code

		// Local Declarations
		GeometryComponent geometryComponent = new GeometryComponent();

		// Get the GeometryComponent and create the GeometryPage.
		if (!(componentMap.get("geometry").isEmpty())) {
			geometryComponent = (GeometryComponent) (componentMap
					.get("geometry").get(0));

			if (geometryComponent != null) {

				// Make the GeometryPage
				geometryPage = new ICEGeometryPage(this, "GPid",
						geometryComponent.getName());

				// Set the GeometryComponent
				geometryPage.setGeometry(geometryComponent);
			}

		}

		return geometryPage;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates a MeshPage to display 2D meshes powered with JME3. Also gives the
	 * MeshComponent to provide data to display.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	// TODO Add to model if necessary.
	private ICEFormPage createMeshPage() {
		// begin-user-code

		// Local Declarations
		MeshComponent meshComponent = new MeshComponent();

		// Get the GeometryComponent and create the GeometryPage.
		if (!(componentMap.get("mesh").isEmpty())) {
			meshComponent = (MeshComponent) (componentMap.get("mesh").get(0));

			if (meshComponent != null) {

				// Make the MeshPage
				meshPage = new ICEMeshPage(this, "MeshPid",
						meshComponent.getName());

				// Set the MeshComponent
				meshPage.setMeshComponent(meshComponent);
			}

		}

		return meshPage;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the input on the TreeCompositeViewer to the
	 * TreeComposite or set of TreeComposites in ICE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<ICEFormPage> createEMFSectionPages() {
		// Local Declarations
		EMFComponent emfComponent = null;
		EMFSectionPage emfPage = null;
		ArrayList<ICEFormPage> pages = new ArrayList<ICEFormPage>();

		// Get the EMFComponent and create the EMFSectionPage.
		if (componentMap.get("emf").size() > 0) {
			for (Component comp : componentMap.get("emf")) {
				emfComponent = (EMFComponent) comp;

				if (emfComponent != null) {

					// Make the EMFSectionPage
					emfPage = new EMFSectionPage(this, emfComponent.getName(),
							emfComponent.getName());

					// Set the EMFComponent
					emfPage.setEMFComponent(emfComponent);
					pages.add(emfPage);
				}
			}

		}

		return pages;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the input on the TreeCompositeViewer to the
	 * TreeComposite or set of TreeComposites in ICE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void setTreeCompositeViewerInput() {
		// begin-user-code

		// Only go through the trouble if there is a TreeComposite to be had
		List<Component> trees = componentMap.get("tree");
		if (!trees.isEmpty()) {
			// Show the view
			try {
				getSite().getWorkbenchWindow().getActivePage()
						.showView(getTreeCompositeViewerID());
			} catch (PartInitException e) {
				e.printStackTrace();
			}

			// Get the TreeComposite to pass to the tree view.
			int id = getTreeCompositeViewerInputID();
			TreeComposite tree = null;
			if (id != -1) {
				// If the ID is specified, find the matching tree.
				for (Component formTree : trees) {
					if (id == formTree.getId()) {
						tree = (TreeComposite) formTree;
						break;
					}
				}
			}
			// If no tree was found, get the first available one.
			if (tree == null) {
				tree = (TreeComposite) trees.get(0);
			}

			// Get the TreeCompositeViewer
			TreeCompositeViewer treeView = (TreeCompositeViewer) getSite()
					.getWorkbenchWindow().getActivePage()
					.findView(getTreeCompositeViewerID());
			// Set the tree as input to the tree view
			treeView.setInput(tree);

			// Register the FormEditor to receive updates from the TreeComposite
			// and its children because they are being edited externally but
			// should still trigger a save event.
			// FIXME We should not register with the TreeComposite more than
			// once. However, the register and unregister calls are heavyweight
			// (recursive). We should use some other logic to ensure
			// registration doesn't happen more than once for the current
			// TreeComposite.
			tree.unregister(this);
			tree.register(this);
		}

		// end-user-code
	}

	/**
	 * This operation retrieves the ID of the TreeCompositeViewer to be used.
	 * Subclasses should override this method to return their own ID.
	 * 
	 * @return The String ID of the TreeCompositeViewer
	 */
	protected String getTreeCompositeViewerID() {
		return TreeCompositeViewer.ID;
	}

	/**
	 * This operation retrieves the ID of the TreeComposite that is passed as
	 * input to the TreeCompositeViewer.
	 * 
	 * @return The integer ID of the TreeComposite to show in the
	 *         TreeCompositeViewer.
	 */
	protected int getTreeCompositeViewerInputID() {
		return -1;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables the ICEFormEditor. Disabled ICEFormEditors will
	 * not make it possible for clients to process the Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param state
	 *            <p>
	 *            True if the editor is disabled, false if not.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disable(boolean state) {
		// begin-user-code

		// Local Declarations
		final boolean buttonState = state;

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Disable processing. Enabled = !disabled
				goButton.setEnabled(!buttonState);
			}
		});

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the createHeaderContents operations from the
	 * SharedHeaderFormEditor super class to create a common header across the
	 * top of the Form pages.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param headerForm
	 *            <p>
	 *            The IManagedForm that manages the content in the common
	 *            header.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void createHeaderContents(IManagedForm headerForm) {
		// begin-user-code

		// Get the Form that provides the common header and decorate it.
		org.eclipse.ui.forms.widgets.Form form = headerForm.getForm().getForm();
		headerForm.getToolkit().decorateFormHeading(form);

		// Create a composite for the overall head layout.
		Composite headClient = new Composite(form.getHead(), SWT.NONE);

		// Set the layout to a GridLayout. It will contain separate columns for
		// the description and, if applicable, process widgets (a label, a
		// dropdown, and go/cancel buttons).
		GridLayout gridLayout = new GridLayout(1, false);
		headClient.setLayout(gridLayout);

		// Create a label to take up the first space and provide the
		// description of the Form.
		Label descLabel = new Label(headClient, SWT.WRAP);
		descLabel.setText(iceDataForm.getDescription());

		// Create the GridData for the label. It must take up all of the
		// available horizontal space, but capable of shrinking down to the
		// minimum width.
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		// For the minimum width, pick a length based on the average character
		// width with the label's font. Use, say, 35 characters.
		GC gc = new GC(descLabel);
		int widthOf50Chars = gc.getFontMetrics().getAverageCharWidth() * 35;
		gc.dispose();
		// We set the min width so the label won't shrink below that width. We
		// set the width hint to the same value so the widget won't compute its
		// size base on SWT.DEFAULT (if this is the case, it won't wrap).
		gridData.minimumWidth = widthOf50Chars;
		gridData.widthHint = widthOf50Chars;
		descLabel.setLayoutData(gridData);

		// Create the process label, button and dropdown if the action list is
		// available.
		if (iceDataForm.getActionList() != null
				&& !iceDataForm.getActionList().isEmpty()) {

			// Create a label for the process buttons
			Label processLabel = new Label(headClient, SWT.NONE);
			processLabel.setText("Process:");

			// Create the dropdown menu
			processDropDown = new Combo(headClient, SWT.DROP_DOWN | SWT.SINGLE
					| SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
			for (String i : iceDataForm.getActionList()) {
				processDropDown.add(i);
			}
			// Set the default process
			processName = iceDataForm.getActionList().get(0);
			processDropDown.select(0);
			// Add the dropdown listener
			processDropDown.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Set the action value to use when processing
					processName = processDropDown.getItem(processDropDown
							.getSelectionIndex());
				}
			});

			// Create the button to process the Form
			goButton = new Button(headClient, SWT.PUSH);
			goButton.setText("Go!");

			// Set the button's listener and process command
			goButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Process the Form
					notifyProcessListeners(processName);
				}
			});

			// Create the button to cancel the process
			cancelButton = new Button(headClient, SWT.PUSH);
			cancelButton.setText("Cancel");

			// Set the button's listener and process command
			cancelButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Process the Form
					notifyCancelListeners(processName);
				}
			});

			// Since we have more widgets, add more columns to the GridLayout.
			// All of these new widgets should grab what horizontal space they
			// need but be vertically centered.
			gridLayout.numColumns += 4;
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			processLabel.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			processDropDown.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			goButton.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			cancelButton.setLayoutData(gridData);
		}
		// Set the processComposite as the Form's head client
		form.setHeadClient(headClient);

		// Set Form name
		form.setText(iceDataForm.getName() + " " + iceDataForm.getId());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a set of ICESectionPages using DataComponents,
	 * TableComponents, and MatrixComponents. These pages are used in the
	 * addPages() operation and rendered to the screen.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<ICEFormPage> createDataTableAndMatrixComponentPages() {
		// begin-user-code
		// Local Declarations
		ArrayList<ICEFormPage> sectionPages = new ArrayList<ICEFormPage>();
		ArrayList<Component> comps = new ArrayList<Component>();
		ICESectionPage tmpPage = null;
		String pageName = null;
		int numCompsPerPage = 4, i = 0, j = 0;
		int numComponents = 0;

		// Get the TableComponents and DataComponents
		comps.addAll(componentMap.get("data"));
		comps.addAll(componentMap.get("table"));
		comps.addAll(componentMap.get("matrix"));

		numComponents = comps.size();

		// If there are less components than the allowed number of components
		// per page, just add them all to the first page
		if (numComponents < numCompsPerPage) {
			// Set a name for the page that is a combination of the first two
			// components or only the first one if there is but one component
			if (numComponents == 1) {
				pageName = ((ICEObject) (comps.get(0))).getName();
			} else {
				pageName = ((ICEObject) (comps.get(0))).getName() + ", "
						+ ((ICEObject) (comps.get(1))).getName() + ", etc.";
			}
			// Instantiate the page
			tmpPage = new ICESectionPage(this, pageName, pageName);
			// Loop over the list of DataComponents and create pages for them
			for (Component component : comps) {
				tmpPage.addComponent(component);
			}
			sectionPages.add(tmpPage);
		} else {
			// Otherwise, if there are more components than the number of
			// components per page, add them all with numCompsPerPage Components
			// per page. This loop adds all of the full pages.
			for (i = 0; i < (numComponents / numCompsPerPage) * numCompsPerPage; i = i
					+ numCompsPerPage) {
				// Set a name for the page that is a combination of the first
				// two components
				pageName = ((ICEObject) (comps.get(i))).getName() + ", "
						+ ((ICEObject) (comps.get(i + 1))).getName() + ", etc.";
				// Create the page
				tmpPage = new ICESectionPage(this, pageName, pageName);
				// Add the components
				for (j = 0; j < numCompsPerPage; j++) {
					// i - 1 + j is the buffer offset
					tmpPage.addComponent(comps.get(i + j));
				}
				// Add the page to the list
				sectionPages.add(tmpPage);
			}
			System.out.println(i + numCompsPerPage);
			// Clean up the left over components by just adding them to
			// their own page.
			if (i != comps.size()) {
				// Start by adding setting up the name
				if (comps.size() - i == 1) {
					pageName = ((ICEObject) (comps.get(i))).getName();
				} else {
					pageName = ((ICEObject) (comps.get(i))).getName() + ", "
							+ ((ICEObject) (comps.get(i + 1))).getName()
							+ ", etc.";
				}
				// Create the page
				tmpPage = new ICESectionPage(this, pageName, pageName);
				// Add the components
				while (i < comps.size()) {
					tmpPage.addComponent(comps.get(i));
					i++;
				}
				// Add the page to the list
				sectionPages.add(tmpPage);
			}
		}

		return sectionPages;
		// end-user-code
	}

	/**
	 * This operation overrides init so that the ICE Form, passed as an
	 * IEditorInput, can be stored.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) {

		// Check the input and throw an error if the type is wrong
		if (!(input instanceof ICEFormInput)) {
			throw new RuntimeException("Input passed to ICEFormEditor.init()"
					+ " is not of type ICEFormInput.");
		}

		// Grab the Form and set the part name
		ICEFormInput = (ICEFormInput) input;
		iceDataForm = ICEFormInput.getForm();
		setPartName(input.getName());

		// Set the site
		setSite(site);

		// Set the input
		setInput(input);

		return;
	}

	/**
	 * This operation overrides isDirty to report the dirty state of the
	 * FormEditor.
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * This operation overrides the createToolkit operation to create the proper
	 * toolkit for an ICEFormEditor.
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#createToolkit(org.eclipse.swt.widgets.Display)
	 */
	@Override
	protected FormToolkit createToolkit(Display display) {

		// Create and return the toolkit
		return new FormToolkit(display);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSave(IProgressMonitor monitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		// begin-user-code

		// Set the dirty flag
		setDirty(false);

		// Do the update
		notifyUpdateListeners();

		// Check the Form and enable the process button if the Form is ready
		if (iceDataForm.isReady() && goButton != null) {
			goButton.setEnabled(iceDataForm.isReady());
		}

		// Refresh the parts on the selected page
		for (IFormPart part : ((ICEFormPage) this.getSelectedPage())
				.getManagedForm().getParts()) {
			part.refresh();
		}

		// Refresh the TreeCompositeViewer
		if (!(componentMap.get("tree").isEmpty())) {
			setTreeCompositeViewerInput();
		}

		// Refresh the MasterDetailsComponent
		if (!(componentMap.get("masterDetails").isEmpty())) {

			// Get the first MasterDetailsComponent (The current code only
			// allows one master details to be implemented at this time
			MasterDetailsComponent comp = (MasterDetailsComponent) (componentMap
					.get("masterDetails").get(0));

			// Get the name of the component
			String name = comp.getName();

			// Iterate over the pages of this editor to grab the
			// MasterDetailsPage
			for (int i = 0; i < this.getPageCount(); i++) {

				// Get the page
				FormPage formPage = (FormPage) this.pages.get(i);

				// If the formPage has the same name as the masterDetails, then
				// it is the masterDetails page (it is stored this way!)
				if (formPage.getPartName().equals(name)) {

					// Cast it as a MasterDetailsPage
					ICEMasterDetailsPage masterPage = (ICEMasterDetailsPage) formPage;

					// Refresh the master
					masterPage.refreshMaster();

				}

			}

		}

		if (!(componentMap.get("emf")).isEmpty()) {
			for (int i = 0; i < this.getPageCount(); i++) {
				FormPage formPage = (FormPage) this.pages.get(i);
				EMFComponent comp = (EMFComponent) componentMap.get("emf").get(
						0);
				if (formPage.getPartName().equals(comp.getName())) {
					formPage.doSave(null);
				}
			}
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		// begin-user-code

		// Just save
		doSave(null);

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		// begin-user-code
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see FormEditor#addPages()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void addPages() {
		// begin-user-code

		// Local Declaration
		ArrayList<ICEFormPage> formPages = new ArrayList<ICEFormPage>();

		// Loop over the DataComponents and get them into the map
		for (Component i : iceDataForm.getComponents()) {
			System.out.println("ICEFormEditor Message: Adding component "
					+ i.getName() + " " + i.getId());
			i.accept(this);
		}

		// Create pages for the DataComponents and add them to the list
		if (!(componentMap.get("data").isEmpty())
				|| !(componentMap.get("table").isEmpty())
				|| !(componentMap.get("matrix").isEmpty())) {
			formPages.addAll(createDataTableAndMatrixComponentPages());
		}

		// Create pages for the MasterDetailsComponents
		if (!(componentMap.get("masterDetails").isEmpty())) {
			formPages.addAll(createMasterDetailsComponentPages());
		}

		// Create the page for ResourceComponents
		if (!(componentMap.get("output").isEmpty())) {
			formPages.add(createResourceComponentPage());
		}

		// Create the page for GeometryComponents
		if (!(componentMap.get("geometry").isEmpty())) {
			formPages.add(createGeometryPage());
		}

		// Create the page for MeshComponents
		if (!(componentMap.get("mesh").isEmpty())) {
			formPages.add(createMeshPage());
		}

		if (componentMap.get("emf").size() > 0) {
			for (ICEFormPage p : createEMFSectionPages()) {
				formPages.add(p);
			}
		}

		// Set the TreeCompositeViewer Input
		setTreeCompositeViewerInput();

		// Create the page for Reactors
		if (!(componentMap.get("reactor").isEmpty())) {
			System.out.println("ICEFormEditor Message: "
					+ componentMap.get("reactor").size()
					+ " IReactorComponents not rendered.");
		}

		// Add the Pages
		try {
			for (ICEFormPage i : formPages) {
				addPage(i);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(DataComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(DataComponent component) {
		// begin-user-code

		// Add the Component to the map of components
		addComponentToMap(component, "data");

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(ResourceComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(ResourceComponent component) {
		// begin-user-code

		// Add the Component to the map of components
		addComponentToMap(component, "output");

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TableComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TableComponent component) {
		// begin-user-code

		// Add the Component to the map of components
		addComponentToMap(component, "table");

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MatrixComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MatrixComponent component) {
		// begin-user-code

		// Add the matrix component to the map of components
		addComponentToMap(component, "matrix");

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IShape component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IShape component) {
		// begin-user-code
		addComponentToMap(component, "shape");
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(GeometryComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(GeometryComponent component) {
		// begin-user-code

		// Add the GeometryComponent to the map of components
		addComponentToMap(component, "geometry");

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MasterDetailsComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MasterDetailsComponent component) {
		// begin-user-code

		// Add the masterDetails component to the map of components
		addComponentToMap(component, "masterDetails");

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TreeComposite component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TreeComposite component) {
		// begin-user-code

		// Add the tree to the map
		addComponentToMap(component, "tree");

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IReactorComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IReactorComponent component) {
		// begin-user-code

		// Add the reactor to the map
		addComponentToMap(component, "reactor");

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#registerUpdateListener(IUpdateEventListener
	 *      listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerUpdateListener(IUpdateEventListener listener) {
		// begin-user-code

		// Add the listener so long as it is not null
		if (listener != null) {
			updateListeners.add(listener);
		}
		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#registerProcessListener(IProcessEventListener
	 *      listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerProcessListener(IProcessEventListener listener) {
		// begin-user-code

		// Add the listener so long as it is not null
		if (listener != null) {
			processListeners.add(listener);
		}
		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#registerResourceProvider(ISimpleResourceProvider
	 *      provider)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerResourceProvider(ISimpleResourceProvider provider) {
		// begin-user-code

		// Add the provider so long as it is not null
		if (provider != null && resourceComponentPage != null) {
			resourceProvider = provider;
			// Set the ISimpleResourceProvider that should be used for load
			// requests (i.e. - double clicks)
			resourceComponentPage.setResourceProvider(resourceProvider);
		}

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyUpdateListeners()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyUpdateListeners() {
		// begin-user-code

		// Notify the update listeners
		for (IUpdateEventListener eventListener : updateListeners) {
			eventListener.formUpdated(iceDataForm);
		}

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyProcessListeners(String process)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyProcessListeners(String process) {
		// begin-user-code

		// Make sure the process is not null
		if (process != null) {
			// Notify the process listeners
			for (IProcessEventListener listener : processListeners) {
				listener.processSelected(iceDataForm, process);
			}
		}

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyCancelListeners(String process)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyCancelListeners(String process) {
		// begin-user-code

		// Make sure the process is not null
		if (process != null) {
			// Notify the process listeners
			for (IProcessEventListener listener : processListeners) {
				listener.cancelRequested(iceDataForm, process);
			}
		}

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateableListener#update(IUpdateable component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(IUpdateable component) {
		// begin-user-code

		System.out.println("ICEFormEditor Message: State changed to dirty.");

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Just set the dirty bit (wow that reads naughty...) ;)
				setDirty(true);
			}
		});

		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor#visit
	 * (org.eclipse.ice.datastructures.form.TimeDataComponent)
	 */
	@Override
	public void visit(TimeDataComponent component) {

		// begin-user-code

		// Treat as a datacomponent
		this.visit((DataComponent) component);

		// end-user-code

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor#visit
	 * (org.eclipse.ice.datastructures.form.mesh.MeshComponent)
	 */
	public void visit(MeshComponent component) {
		// begin-user-code

		// Add the GeometryComponent to the map of components
		addComponentToMap(component, "mesh");

		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor#visit
	 * (org.eclipse.ice.datastructures.form.AdaptiveTreeComposite)
	 */
	@Override
	public void visit(AdaptiveTreeComposite component) {
		// Proceed with the usual TreeComposite operation.
		visit((TreeComposite) component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor#visit
	 * (org.eclipse.ice.datastructures.form.emf.EMFComponent)
	 */
	@Override
	public void visit(EMFComponent component) {
		System.out.println("Adding EMFComponent: " + component.getName());
		addComponentToMap(component, "emf");
	}

}