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

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * <p>
 * This class is a FormPage that creates a page with multiple sections for a set
 * of ICE DataComponents.
 * </p>
 *
 * @author Jay Jay Billings
 */
public class ICESectionPage extends ICEFormPage implements IComponentVisitor {

	/**
	 * The IManagedForm for the SectionPage.
	 */
	private IManagedForm managedFormRef;

	/**
	 * <p>
	 * The list of DataComponents that is formed by visiting the Components
	 * assigned to this page.
	 * </p>
	 *
	 */
	private ArrayList<DataComponent> dataComponents;

	/**
	 * <p>
	 * The list of TableComponents that is formed by visiting the Components
	 * assigned to this page.
	 * </p>
	 *
	 */
	private ArrayList<TableComponent> tableComponents;

	/**
	 * <p>
	 * The list of MatrixComponents that is formed by visiting the Components
	 * assigned to this page.
	 * </p>
	 *
	 */
	private ArrayList<MatrixComponent> matrixComponents;

	/**
	 * <p>
	 * The Constructor
	 * </p>
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
	 */
	public ICESectionPage(FormEditor editor, String id, String title) {

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
		dataComponents = new ArrayList<DataComponent>();
		tableComponents = new ArrayList<TableComponent>();
		matrixComponents = new ArrayList<MatrixComponent>();

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		// Get the parent form.
		final ScrolledForm scrolledForm = managedForm.getForm();

		// Set a GridLayout with a single column. Remove the default margins.
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		scrolledForm.getBody().setLayout(layout);

		// Set the class reference to the managed form
		managedFormRef = managedForm;

		// Create the sections on the Eclipse Form - one for each in the list.
		// This uses the IComponentVisitor interface to determine the types of
		// Components and call the code to create the proper SectionPart.
		createDataComponentSections();
		createTableComponentSections();
		createMatrixComponentSections();

		return;
	}

	/**
	 * <p>
	 * This operation retrieves the Components that are managed by this page.
	 * This operation should not be generally used, but for testing purposes
	 * where there is a setter there must be a getter.
	 * </p>
	 *
	 * @return <p>
	 *         The set of Components managed by this page.
	 *         </p>
	 */
	public ArrayList<Component> getComponents() {

		ArrayList<Component> compList = new ArrayList<Component>();

		compList.addAll(dataComponents);
		compList.addAll(tableComponents);
		compList.addAll(matrixComponents);

		return compList;
	}

	/**
	 * <p>
	 * This operation adds a Component to the set of Components that are managed
	 * by this page.
	 * </p>
	 *
	 * @param component
	 *            <p>
	 *            A new Component for this page to manage
	 *            </p>
	 */
	public void addComponent(Component component) {
		if (component != null) {
			// Visit the components so that they can be sorted
			component.accept(this);
		}
	}

	/**
	 * <p>
	 * This operation creates ICEDataComponentSectionParts for each
	 * DataComponent in the list of DataComponents and figures out exactly how
	 * they should be arranged and span across the page based on their
	 * properties.
	 * </p>
	 *
	 */
	private void createDataComponentSections() {

		// Get the parent form and the ToolKit to create decorated Sections.
		final ScrolledForm scrolledForm = managedFormRef.getForm();
		final FormToolkit formToolkit = managedFormRef.getToolkit();

		// Each DataComponent will get its own Section. These Sections will be
		// spread horizontally and will take all available horizontal (but not
		// vertical) space offered by the Form.

		// We want each DataComponent Section to be at least 100 pixels wide.
		// Compute the total width used by these Sections.
		int numDataComponents = dataComponents.size();
		int minWidth = numDataComponents * 100;

		// Create a Composite with horizontal GridLayout to contain the
		// Sections. Use the custom Composite with the computed min width so the
		// horizontal scroll bar is not used unless the width is less than the
		// computed min width.
		Composite dataGridComposite = new ScrollClientComposite(
				scrolledForm.getBody(), SWT.NONE, minWidth);
		// Since we cannot use the FormToolKit to create the custom Composite,
		// we must "adapt" the custom Composite.
		formToolkit.adapt(dataGridComposite);
		GridLayout layout = new GridLayout(numDataComponents, true);
		// Remove the margins from the layout.
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		dataGridComposite.setLayout(layout);

		// The DataComponent Sections should only grab what vertical space they
		// need, so set the containing Composite's GridData to only grab and
		// fill excess horizontal space.
		dataGridComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		// Create the DataComponent Sections.
		for (int i = 0; i < numDataComponents; i++) {
			// Create a new Section for the current DataComponent.
			DataComponent dataComponent = dataComponents.get(i);
			Section section = formToolkit.createSection(dataGridComposite,
					ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | ExpandableComposite.TWISTIE
							| ExpandableComposite.EXPANDED | ExpandableComposite.COMPACT);
			// Each Section should grab all available space it can get within
			// the containing Composite created above this loop.
			section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			// To populate the Section, use a new ICEDataComponentSectionPart.
			ICEDataComponentSectionPart sectionPart;
			sectionPart = new ICEDataComponentSectionPart(section, editor,
					managedFormRef);
			sectionPart.setDataComponent(dataComponent);
			sectionPart.renderSection();
			// Add the part to the ManagedForm's update lifecycle.
			managedFormRef.addPart(sectionPart);
		}

		return;
	}

	/**
	 * <p>
	 * This operation creates ICETableComponentSectionParts for each
	 * TableComponent in the list of TableComponents and figures out exactly how
	 * they should be arranged and span across the page based on their
	 * properties.
	 * </p>
	 *
	 */
	private void createTableComponentSections() {

		// Get the parent form and the ToolKit to create decorated Sections.
		final ScrolledForm scrolledForm = managedFormRef.getForm();
		final FormToolkit formToolkit = managedFormRef.getToolkit();

		// Each TableComponent will get is own Section that occupies a single
		// row in the parent GridLayout (that is, the Form's GridLayout). The
		// rows will grab all excess vertical and horizontal space available in
		// the Form.

		// Create the TableComponent Sections.
		Composite container = scrolledForm.getBody();
		for (int i = 0; i < tableComponents.size(); i++) {
			// Create a new Section for the current TableComponent.
			TableComponent tableComponent = tableComponents.get(i);
			Section section = formToolkit.createSection(container,
					ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | ExpandableComposite.TWISTIE
							| ExpandableComposite.EXPANDED);
			// Each Section should fill all available horizontal and vertical
			// space in the parent GridLayout, but it should only grab excess
			// horizontal space.
			section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			// To populate the Section, use an ICETableComponentSectionPart.
			ICETableComponentSectionPart sectionPart;
			sectionPart = new ICETableComponentSectionPart(section, editor,
					managedFormRef);
			sectionPart.setTableComponent(tableComponent);
			sectionPart.renderSection();
			// Add the part to the ManagedForm's update lifecycle.
			managedFormRef.addPart(sectionPart);
		}

		return;
	}

	/**
	 * <p>
	 * This operation creates ICEMatrixComponentSectionParts for each
	 * MatrixComponent in the list of MatrixComponents and figures out exactly
	 * how they should be arranged and span across the page based on their
	 * properties.
	 * </p>
	 *
	 */
	private void createMatrixComponentSections() {

		// Get the parent form and the ToolKit to create decorated Sections.
		final ScrolledForm scrolledForm = managedFormRef.getForm();
		final FormToolkit formToolkit = managedFormRef.getToolkit();

		// Each MatrixComponent will get its own Section. These Sections will be
		// spread horizontally and will take all remaining available horizontal
		// and vertical space offered by the Form.

		// Create a Composite to wrap the MatrixComponent Sections. As specified
		// above, it should have a default horizontal FillLayout.
		Composite matrixGridComposite = formToolkit.createComposite(
				scrolledForm.getBody(), SWT.NONE);
		matrixGridComposite.setLayout(new FillLayout());

		// The MatrixComponentSections should grab all remaining space.
		matrixGridComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));

		// Create the MatrixComponent Sections.
		for (int i = 0; i < matrixComponents.size(); i++) {
			// Create a new Section for the current MatrixComponent.
			MatrixComponent matrixComponent = matrixComponents.get(i);
			Section section = formToolkit.createSection(matrixGridComposite,
					ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | ExpandableComposite.TWISTIE
							| ExpandableComposite.EXPANDED);
			// No layout data needs to be set because the matrixGridComposite
			// uses a FillLayout.

			// To populate the Section, use an ICEMatrixComponentSectionPart.
			ICEMatrixComponentSectionPart sectionPart;
			sectionPart = new ICEMatrixComponentSectionPart(section, editor,
					managedFormRef);
			sectionPart.setMatrixComponent(matrixComponent);
			sectionPart.renderSection();
			// Add the part to the ManagedForm's update lifecycle
			managedFormRef.addPart(sectionPart);
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(DataComponent component)
	 */
	@Override
	public void visit(DataComponent component) {

		if (component != null) {
			dataComponents.add(component);
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(ResourceComponent component)
	 */
	@Override
	public void visit(ResourceComponent component) {
		// Nothing to do yet.
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(TableComponent component)
	 */
	@Override
	public void visit(TableComponent component) {

		if (component != null) {
			tableComponents.add(component);
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(MatrixComponent component)
	 */
	@Override
	public void visit(MatrixComponent component) {

		if (component != null) {
			matrixComponents.add(component);
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(IShape component)
	 */
	@Override
	public void visit(IShape component) {
		// Nothing to do yet.
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(GeometryComponent component)
	 */
	@Override
	public void visit(GeometryComponent component) {
		// Nothing to do yet.
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(MasterDetailsComponent component)
	 */
	@Override
	public void visit(MasterDetailsComponent component) {
		// Nothing to do yet.
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(TreeComposite component)
	 */
	@Override
	public void visit(TreeComposite component) {
		// Nothing to do yet.
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IComponentVisitor#visit(IReactorComponent component)
	 */
	@Override
	public void visit(IReactorComponent component) {
		// Nothing to do yet.
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


		// Treat as a DataComponent
		this.visit((DataComponent) component);


	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor#visit
	 * (org.eclipse.ice.datastructures.form.mesh.MeshComponent)
	 */
	@Override
	public void visit(MeshComponent component) {
		// Nothing to do yet.
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
		// Nothing to do yet.
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
		// Nothing to do yet.
	}

	@Override
	public void visit(ListComponent component) {
		// TODO Auto-generated method stub

	}

}