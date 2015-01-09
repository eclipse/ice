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

import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Frame;

import static org.eclipse.ice.client.widgets.geometry.ShapeTreeView.*;
import static org.eclipse.ice.client.widgets.geometry.TransformationView.*;

import org.eclipse.ice.client.widgets.geometry.GeometryApplication;
import org.eclipse.ice.client.widgets.geometry.GeometryCompositeFactory;
import org.eclipse.ice.client.widgets.geometry.ShapeTreeView;
import org.eclipse.ice.client.widgets.geometry.TransformationView;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.PartInitException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is ICEFormPage that displays the GeometryEditor powered by JME3.
 * It automatically opens the ShapeTreeView and TransformationView to allow the
 * user to add and edit geometry.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEGeometryPage extends ICEFormPage implements
		IUpdateableListener {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The property that determines whether there is a need to Save.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean dirty;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The DataStructure that gives this page its data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private GeometryComponent geometryComp;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This sets the FormEditor to be opened on, as well as the id and title
	 * Strings.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param editor
	 * @param id
	 * @param title
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEGeometryPage(FormEditor editor, String id, String title) {
		// begin-user-code

		super(editor, id, title);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the dirty status of the Page.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isDirty() {
		// begin-user-code
		return dirty;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param allowed
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void isSaveAsAllowed(boolean allowed) {
		// begin-user-code
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the geometryComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GeometryComponent getGeometry() {
		// begin-user-code
		return geometryComp;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the geometryComponent; giving the geometryEditor data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param geometryComponent
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setGeometry(GeometryComponent geometryComponent) {
		// begin-user-code
		geometryComp = geometryComponent;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Connects the ShapeTreeView with the geometryEditor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void getFocus() {
		// begin-user-code

		ShapeTreeView shapeTreeView = (ShapeTreeView) getSite()
				.getWorkbenchWindow().getActivePage()
				.findView(ShapeTreeView.ID);
		shapeTreeView.setGeometry(geometryComp);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Provides the page with the geometryApplication's information to display
	 * geometry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param managedForm
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void createFormContent(IManagedForm managedForm) {
		// begin-user-code

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
			e.printStackTrace();
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

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateableListener#update(Component component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(IUpdateable component) {
		// begin-user-code
		dirty = true;
		// end-user-code
	}

	@Override
	public String getPartName() {
		// This is the name of the page displayed on tab for selection of the
		// geometryEditor
		return super.getPartName();
	}

}