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

import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;

import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ui.forms.widgets.Section;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is a FormPage that creates a page with multiple sections for a set
 * of ICE DataComponents.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICESectionPage extends ICEFormPage implements IComponentVisitor {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of Components managed by this page. It contains components of
	 * multiple types and is not sorted.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<Component> components;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The IManagedForm for the SectionPage.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IManagedForm managedFormRef;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of DataComponents that is formed by visiting the Components
	 * assigned to this page.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<DataComponent> dataComponents;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of TableComponents that is formed by visiting the Components
	 * assigned to this page.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<TableComponent> tableComponents;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The number of columns in the page's layout.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int numColumns;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of MatrixComponents that is formed by visiting the Components
	 * assigned to this page.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<MatrixComponent> matrixComponents;

	private int colspan;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param editor
	 *            <p>
	 *            The FormEditor for which the Page should be constructed.
	 *            </p>
	 * @param id
	 *            <p>
	 *            The id of the page.
	 *            </p>
	 * @param title
	 *            <p>
	 *            The title of the page.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICESectionPage(FormEditor editor, String id, String title) {
		// begin-user-code

		// Call the super constructor
		super(editor, id, title);

		// Set the FormEditor if it is not null and throw an exception
		// otherwise.
		if (editor != null && editor instanceof ICEFormEditor) {
			this.editor = (ICEFormEditor) editor;
		} else {
			throw new RuntimeException("Editor in ICEFormSectionPage "
					+ " constructor cannot be null.");
		}
		// Setup the list for Components
		components = new ArrayList<Component>();
		dataComponents = new ArrayList<DataComponent>();
		tableComponents = new ArrayList<TableComponent>();
		matrixComponents = new ArrayList<MatrixComponent>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param managedForm
	 *            <p>
	 *            The Form widget on which the ICESectionPage exists.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void createFormContent(IManagedForm managedForm) {
		// begin-user-code

		// Local Declarations
		final ScrolledForm form = managedForm.getForm();
		GridLayout layout = new GridLayout(1, false);
		form.getBody().setLayout(layout);

		// Set the class reference to the managed form
		managedFormRef = managedForm;

		// Create the sections on the Eclipse Form - one for each in the list.
		// This uses the IComponentVisitor interface to determine the types of
		// Components and call the code to create the proper SectionPart.
		createDataComponentSections();
		createTableComponentSections();
		createMatrixComponentSections();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the Components that are managed by this page.
	 * This operation should not be generally used, but for testing purposes
	 * where there is a setter there must be a getter.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The set of Components managed by this page.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Component> getComponents() {
		// begin-user-code

		ArrayList<Component> compList = new ArrayList<Component>();

		compList.addAll(dataComponents);
		compList.addAll(tableComponents);
		compList.addAll(matrixComponents);

		return components;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation adds a Component to the set of Components that are managed
	 * by this page.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            A new Component for this page to manage
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addComponent(Component component) {
		// begin-user-code

		if (component != null) {
			// Visit the components so that they can be sorted
			component.accept(this);

		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation determines the proper layout and number of columns for the
	 * ICESectionPage based on the number of DataComponents and TableComponents
	 * and their properties.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void setProperLayout() {
		// begin-user-code

		// Determine contents of the list
		// Set layout properties according to the list contents
		if (tableComponents.isEmpty()) {
			numColumns = 4;
		} else {
			numColumns = 2;
		}

		if (dataComponents.size() == 1) {
			colspan = 1;
		} else {
			colspan = 2;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates ICEDataComponentSectionParts for each
	 * DataComponent in the list of DataComponents and figures out exactly how
	 * they should be arranged and span across the page based on their
	 * properties.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void createDataComponentSections() {
		// begin-user-code

		// Local Declarations
		final ScrolledForm scrolledForm = managedFormRef.getForm();
		FormToolkit formToolkit = managedFormRef.getToolkit();
		Composite editorComposite = formToolkit.createComposite(
				scrolledForm.getBody(), SWT.NONE);
		ICEDataComponentSectionPart tmpSectionPart = null;

		// Create the DataComponents
		for (int i = 0; i < dataComponents.size(); i++) {
			DataComponent dataComponent = dataComponents.get(i);
			// Create a Section
			Section section = formToolkit.createSection(editorComposite,
					Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
							| Section.EXPANDED | Section.COMPACT);
			// Create a SectionPart
			tmpSectionPart = new ICEDataComponentSectionPart(section, editor,
					managedFormRef);
			tmpSectionPart.setDataComponent(dataComponent);
			tmpSectionPart.renderSection();
			// Add the part to the ManagedForm's update lifecycle
			managedFormRef.addPart(tmpSectionPart);
			// Set the color gradient on the Form's menu bar. The Form in this
			// case is the instance of the Form owned by the ScrolledForm.
			formToolkit.decorateFormHeading(scrolledForm.getForm());
		}

		// Layout the composite and sash form
		editorComposite.setLayout(new FillLayout());
		editorComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates ICETableComponentSectionParts for each
	 * TableComponent in the list of TableComponents and figures out exactly how
	 * they should be arranged and span across the page based on their
	 * properties.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void createTableComponentSections() {
		// begin-user-code

		// Local Declarations
		final ScrolledForm scrolledForm = managedFormRef.getForm();
		FormToolkit formToolkit = managedFormRef.getToolkit();
		ICETableComponentSectionPart tmpSectionPart = null;

		// Create the TableComponents
		for (int i = 0; i < tableComponents.size(); i++) {
			TableComponent tableComponent = tableComponents.get(i);
			// Create a Section
			Section section = formToolkit.createSection(scrolledForm.getBody(),
					Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
							| Section.EXPANDED);
			section.setLayoutData(new GridData(GridData.FILL_BOTH));
			// Create a SectionPart
			tmpSectionPart = new ICETableComponentSectionPart(section, editor,
					managedFormRef);
			tmpSectionPart.setTableComponent(tableComponent);
			tmpSectionPart.renderSection();
			// Add the part to the ManagedForm's update lifecycle
			managedFormRef.addPart(tmpSectionPart);
			// Set the color gradient on the Form's menu bar. The Form in this
			// case is the instance of the Form owned by the ScrolledForm.
			formToolkit.decorateFormHeading(scrolledForm.getForm());
		}
		// Layout the composite and sash form

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates ICEMatrixComponentSectionParts for each
	 * MatrixComponent in the list of MatrixComponents and figures out exactly
	 * how they should be arranged and span across the page based on their
	 * properties.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void createMatrixComponentSections() {
		// begin-user-code

		// Local Declarations
		final ScrolledForm scrolledForm = managedFormRef.getForm();
		FormToolkit formToolkit = managedFormRef.getToolkit();
		Composite editorComposite = formToolkit.createComposite(
				scrolledForm.getBody(), SWT.NONE);
		ICEMatrixComponentSectionPart tmpSectionPart = null;

		for (int i = 0; i < matrixComponents.size(); i++) {
			MatrixComponent matrixComponent = matrixComponents.get(i);
			// Create a Section
			Section section = formToolkit.createSection(editorComposite,
					Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
							| Section.EXPANDED);
			// Create a SectionPart
			tmpSectionPart = new ICEMatrixComponentSectionPart(section, editor,
					managedFormRef);
			tmpSectionPart.setMatrixComponent(matrixComponent);
			tmpSectionPart.renderSection();
			// Add the part to the ManagedForm's update lifecycle
			managedFormRef.addPart(tmpSectionPart);
			// Set the color gradient on the Form's menu bar. The Form in this
			// case is the instance of the Form owned by the ScrolledForm.
			formToolkit.decorateFormHeading(scrolledForm.getForm());

		}
		// Layout the composite and sash form
		editorComposite.setLayout(new FillLayout());
		editorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

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

		if (component != null) {
			dataComponents.add(component);
		}

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
		// TODO Auto-generated method stub

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

		if (component != null) {
			tableComponents.add(component);
		}

		return;
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

		if (component != null) {
			matrixComponents.add(component);
		}

		return;
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

		// end-user-code
	}

	@Override
	public void visit(TimeDataComponent component) {

		// begin-user-code

		// Treat as a DataComponent
		this.visit((DataComponent) component);

		// end-user-code

	}

	@Override
	public void visit(MeshComponent component) {
		// TODO Auto-generated method stub

	}


	@Override
	public void visit(AdaptiveTreeComposite component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EMFComponent component) {
		// TODO Auto-generated method stub
		
	}

}