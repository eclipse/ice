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

import org.eclipse.ice.client.widgets.geometry.GeometryCompositeFactory;
import org.eclipse.ice.client.widgets.geometry.ShapeTreeView;
import org.eclipse.ice.client.widgets.geometry.TransformationView;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.viz.service.geometry.GeometryComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * <p>
 * This class is ICEFormPage that displays the GeometryEditor powered by JME3.
 * It automatically opens the ShapeTreeView and TransformationView to allow the
 * user to add and edit geometry.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEGeometryPage extends ICEFormPage implements IUpdateableListener {
	/**
	 * <p>
	 * The property that determines whether there is a need to Save.
	 * </p>
	 * 
	 */
	private boolean dirty;

	/**
	 * <p>
	 * The DataStructure that gives this page its data.
	 * </p>
	 * 
	 */
	private GeometryComponent geometryComp;

	/**
	 * <p>
	 * This sets the FormEditor to be opened on, as well as the id and title
	 * Strings.
	 * </p>
	 * 
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ICEGeometryPage(FormEditor editor, String id, String title) {

		super(editor, id, title);

	}

	/**
	 * <p>
	 * Returns the dirty status of the Page.
	 * </p>
	 * 
	 * @return True if the page is dirty (needs to be saved), false otherwise.
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * 
	 * @param allowed
	 */
	public void isSaveAsAllowed(boolean allowed) {
	}

	/**
	 * <p>
	 * Returns the GeometryComponent.
	 * </p>
	 * 
	 * @return The GeometryComponent represented by the page.
	 */
	public GeometryComponent getGeometry() {
		return geometryComp;
	}

	/**
	 * <p>
	 * Sets the geometryComponent; giving the geometryEditor data.
	 * </p>
	 * 
	 * @param geometryComponent
	 */
	public void setGeometry(GeometryComponent geometryComponent) {
		geometryComp = geometryComponent;
	}

	/**
	 * <p>
	 * Connects the ShapeTreeView with the geometryEditor.
	 * </p>
	 * 
	 */
	public void getFocus() {

		ShapeTreeView shapeTreeView = (ShapeTreeView) getSite()
				.getWorkbenchWindow().getActivePage()
				.findView(ShapeTreeView.ID);
		shapeTreeView.setGeometry(geometryComp);

		return;
	}

	/**
	 * <p>
	 * Provides the page with the geometryApplication's information to display
	 * geometry.
	 * </p>
	 * 
	 * @param managedForm
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
					.showView(ShapeTreeView.ID);
			getSite().getWorkbenchWindow().getActivePage()
					.showView(TransformationView.ID);

		} catch (PartInitException e) {
			logger.error(getClass().getName() + " Exception!",e);
		}

		// Create the geometry composite - get the parent
		org.eclipse.ui.forms.widgets.Form pageForm = managedForm.getForm()
				.getForm();
		Composite parent = pageForm.getBody();
		// Use the GeometryCompositeFactory
		GeometryCompositeFactory geomFactory = new GeometryCompositeFactory();
		geomFactory.renderGeometryComposite(parent, geometryComp);

		getFocus();

		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org
	 * .eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {
		dirty = true;
	}

	@Override
	public String getPartName() {
		// This is the name of the page displayed on tab for selection of the
		// geometryEditor
		return super.getPartName();
	}

}