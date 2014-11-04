/*******************************************************************************
* Copyright (c) 2012, 2014 UT-Battelle, LLC.
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

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This is a subclass of MasterDetailsBlock that works with a structured content
 * provider that is provided by ICE. The only way to provide the content
 * provider and details page provider handles for this class is through the
 * constructor.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEScrolledPropertiesBlock extends MasterDetailsBlock {

	/**
	 * A content provider that provides the proper names for Masters added to
	 * the Masters block.
	 * 
	 * @author Jay Jay Billings
	 * 
	 */
	class MasterContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {

			// Local Declarations
			MasterDetailsComponent comp = (MasterDetailsComponent) inputElement;
			ArrayList<String> contents = new ArrayList<String>();
			int numberOfMasters = masterDetailsComponent.numberOfMasters();

			// Load the contents into the array
			for (int i = 0; i < numberOfMasters; i++) {
				contents.add(comp.getUniqueMasterValueAtIndex(i));
			}
			// Create the contents
			return contents.toArray();
		}
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The MasterDetailsBlock used to populate the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MasterDetailsComponent masterDetailsComponent;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The page provider that is used to retrieve IDetailsPages for the Details
	 * block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IDetailsPageProvider detailsPageProvider;

	/**
	 * The FormEditor that should be notified when it is time to save.
	 */
	private ICEFormEditor formEditor;

	private TableViewer viewer;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. If either argument is null, this class will be unable to
	 * render anything to the screen.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            The MasterDetailsComponent used to populate the
	 *            MasterDetailsBlock/ScrolledPropertiesBlock.
	 *            </p>
	 * @param editor
	 *            <p>
	 *            The ICEFormEditor in which the pages will exist. This editor
	 *            is marked as dirty when the page changes.
	 *            </p>
	 * @param pageProvider
	 *            <p>
	 *            The IDetailsPageProvider that will provide IDetailsPages for
	 *            the Details block.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEScrolledPropertiesBlock(MasterDetailsComponent component,
			ICEFormEditor editor, IDetailsPageProvider pageProvider) {
		// begin-user-code

		// Set the references
		masterDetailsComponent = component;
		detailsPageProvider = pageProvider;
		formEditor = editor;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the content provider or null if it was not set in
	 * the constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The MasterDetailsComponent that is used to populate the
	 *         MasterDetailsBlock/ScrolledPropertiesBlock or null if it was not
	 *         set.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public MasterDetailsComponent getComponent() {
		// begin-user-code
		return masterDetailsComponent;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the page provider or null if it was not set in the
	 * constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The IDetailsPageProvider or null if it was not set.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IDetailsPageProvider getDetailsPageProvider() {
		// begin-user-code
		return detailsPageProvider;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a master part in the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param managedForm
	 *            <p>
	 *            The parent Form
	 *            </p>
	 * @param parent
	 *            <p>
	 *            The parent Composite.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		// begin-user-code

		FormToolkit toolkit = managedForm.getToolkit();

		// Define key features for the ScrolledPropertiesBlock
		Section section = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		section.setText(masterDetailsComponent.getName());
		section.setDescription(masterDetailsComponent.getDescription());
		section.marginWidth = 10;
		section.marginHeight = 5;

		// Define layouts
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		Table table = toolkit.createTable(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		gd.verticalSpan = 2;
		table.setLayoutData(gd);
		toolkit.paintBordersFor(client);

		// Setup the section's client and add it to the part lifecycle with a
		// section part.
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);

		// Use a standard tableviewer from JFace to hold the list of masters.
		final TableViewer viewer = new TableViewer(table);
		this.viewer = viewer;
		final IManagedForm finalManagedForm = managedForm;
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				finalManagedForm.fireSelectionChanged(spart,
						event.getSelection());
			}
		});

		// Create the content provider, set its content and set the input that
		// will be read in the provider. In this case, this is just an instance
		// of the content provider declared at the top of this class and the
		// input is just the MasterDetailsComponent.
		final MasterContentProvider masterContentProvider = new MasterContentProvider();
		viewer.setContentProvider(masterContentProvider);
		viewer.setInput(masterDetailsComponent);

		// Define the button to add MasterComponents to the table
		Button button = toolkit.createButton(client, "  Add  ", SWT.PUSH);
		button.addSelectionListener(new SelectionListener() {

			// What to do when the add button is clicked
			public void widgetSelected(SelectionEvent event) {
				// Local Declarations
				ArrayList<String> ids = masterDetailsComponent
						.getAllowedMasterValues();

				// Local Declarations
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow workbenchWindow = workbench
						.getActiveWorkbenchWindow();

				// Create an error box
				EclipseErrorBoxWidget errorBox = new EclipseErrorBoxWidget();

				// Present a selection dialog if Items are available
				if (masterDetailsComponent != null) {

					// Setup the list dialog
					ListDialog masterCreatorDialog = new ListDialog(
							workbenchWindow.getShell());
					masterCreatorDialog.setAddCancelButton(true);
					masterCreatorDialog
							.setContentProvider(new ArrayContentProvider());
					masterCreatorDialog.setLabelProvider(new LabelProvider());
					masterCreatorDialog.setInput(masterDetailsComponent
							.getAllowedMasterValues().toArray());
					masterCreatorDialog.setInitialElementSelections(ids);
					masterCreatorDialog.setTitle("MasterDetailPairs");
					masterCreatorDialog.open();

					// Direct the client to create a new Item if a selection was
					// made
					if (masterCreatorDialog.getResult() != null) {
						int id = masterDetailsComponent.addMaster();
						masterDetailsComponent
								.setMasterInstanceValue(id,
										(masterCreatorDialog.getResult()[0])
												.toString());
						// Mark the editor as dirty since information has
						// changed
						formEditor.setDirty(true);
						// Refresh the view
						viewer.refresh();
					} else {
						// Close the list dialog otherwise
						masterCreatorDialog.close();
					}

				} else {
					// Throw an error if no masters are available
					errorBox.setErrorString("There are no items to add to the list."
							+ " If you believe this is a serious error, please "
							+ "contact your system's administrator.");
					errorBox.display();
				}

			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		// Set the button layout
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);

		// Create delete button
		Button button2 = toolkit.createButton(client, "Delete", SWT.PUSH);
		button2.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {

				int id = viewer.getTable().getSelectionIndex();
				masterDetailsComponent.deleteMasterAtIndex(id);

				// Mark the editor as dirty since information has changed
				formEditor.setDirty(true);

				viewer.refresh();

			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button2.setLayoutData(gd);
		System.err.println("ICEScrolledPropertiesBlock Message: "
				+ "Printing out status: "
				+ this.masterDetailsComponent.canAddRemoveButton());
		// Disable buttons if set to false
		if (!this.masterDetailsComponent.canAddRemoveButton()) {
			System.err.println("ICEScrolledPropertiesBlock Message: Buttons disabled!");
			// disable buttons
			button.setEnabled(false);
			button2.setEnabled(false);
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation registers the IDetailsPageProvider that provides pages to
	 * the DetailsPart.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param detailsPart
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void registerPages(DetailsPart detailsPart) {
		// begin-user-code

		detailsPart.setPageProvider(detailsPageProvider);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates actions in the toolbar for the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param managedForm
	 *            <p>
	 *            The parent Form
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void createToolBarActions(IManagedForm managedForm) {
		// begin-user-code
		final ScrolledForm form = managedForm.getForm();
		Action haction = new Action("Horizontal Orientation",
				Action.AS_RADIO_BUTTON) {
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(true);
		haction.setToolTipText("Set Details to the Right of Masters");
		Action vaction = new Action("Vertical Orientation",
				Action.AS_RADIO_BUTTON) {
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText("Set Details Below Masters");
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
		// end-user-code
	}

	/**
	 * Refreshes the TableViewer.
	 */
	protected void refreshTableViewer() {

		// If the viewer is set, refresh
		if (this.viewer != null) {
			this.viewer.refresh();
		}
	}
}