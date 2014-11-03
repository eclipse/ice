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
package org.eclipse.ice.item.geometry;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.core.resources.IProject;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This is a subclass of Item that provides 3D geometry editing services to ICE.
 * It overrides the setupForm() operation and provides a Form that contains a
 * GeometryComponent. More information about the exact contents of the Form is
 * available on the setupForm() operation description below.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "GeometryEditor")
public class GeometryEditor extends Item {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An alternative nullary constructor used primarily for serialization. The
	 * setProject() operation must be called if this constructor is used!
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GeometryEditor() {
		// begin-user-code

		// Punt to the other Constructor
		this(null);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param project
	 *            <p>
	 *            The Eclipse project used by the GeometryEditor.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GeometryEditor(IProject project) {
		// begin-user-code

		// Call the super constructor
		super(project);

		// Remove the action from the list that allows for writing key-value
		// pairs to a file. The GeometryComponent in the Form can't be written
		// like that.
		allowedActions.remove(taggedExportActionString);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides setupForm() to provide a Form that contains a
	 * single GeometryComponent. This component has id=1 and is named
	 * "Geometry Data."
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// Set the name, description and type
		setName("Geometry Editor");
		itemType = ItemType.Geometry;
		setDescription("This tool allows you to create and edit a 3D geometry.");

		// Instantiate the Form. It's just a regular Form for this Item.
		form = new Form();

		// Create a GeometryComponent to hold the Geometry
		GeometryComponent geometryComp = new GeometryComponent();
		geometryComp.setName("Geometry Data");
		geometryComp.setId(1);
		geometryComp.setDescription(getDescription());

		// Add the component to the Form
		form.addComponent(geometryComp);

		return;

		// end-user-code
	}
}