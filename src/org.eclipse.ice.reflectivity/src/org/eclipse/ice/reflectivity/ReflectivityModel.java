/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reflectivity;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings, aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "ReflectivityModel")
public class ReflectivityModel extends Item {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>`
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ReflectivityModel() {
		// begin-user-code
		this(null);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ReflectivityModel(IProject projectSpace) {
		// begin-user-code

		// Call super
		super(projectSpace);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * 
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of action that should be performed using the
	 *            processed Form data.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after processing the Form and executing
	 *         the action. It returns FormStatus.InfoError if it is unable to
	 *         run for any reason, including being asked to run actions that are
	 *         not in the list of available actions.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus process(String actionName) {
		// begin-user-code
		return super.process(actionName);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * 
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		ArrayList<Entry> template = new ArrayList<Entry>();
		Entry id = new Entry();
		Entry mat = new Entry();
		Entry thickness = new Entry();
		Entry roughness = new Entry();
		Entry sld = new Entry();
		Entry mu_abs = new Entry();
		Entry mu_inc = new Entry();

		// Create the Form
		form = new Form();
		TableComponent table = new TableComponent();
		table.setId(1);
		table.setName("Reflectivity Input Data");
		table.setDescription("");

		int idNum = 1;

		// Configure the entry information
		id.setName("ID");
		id.setDescription("Unique ID for this layer.");
		id.setId(idNum);
		mat.setName("Material");
		// Need stoichometry and mass density to define a compound.
		mat.setDescription("Chemical compound for this layer.");
		mat.setId(++idNum);
		thickness.setName("Thickness");
		thickness.setDescription("The thickness of this material as an "
				+ "initial guess or the actual calculated value if the "
				+ "fit has been run. (Angstroms)");
		thickness.setId(++idNum);
		roughness.setName("Roughness");
		roughness.setDescription("The width of the region of intermixing "
				+ "between layer n-1 and layer n. It goes up and not "
				+ "down. (Angstroms)");
		roughness.setId(++idNum);
		sld.setName("Scattering Length Density");
		sld.setDescription("The product of the mass density and its "
				+ "stoichiometry. It is a proxy for the refractive index. "
				+ "(Angstroms^-2)");
		sld.setId(++idNum);
		mu_abs.setName("Mu_abs");
		mu_abs.setDescription("The absorption coefficient divided by the wavelength. "
				+ "(Angstroms^-2)");
		mu_abs.setId(++idNum);
		mu_inc.setName("Mu_inc");
		mu_inc.setDescription("The effective incoherent absorption "
				+ "coefficient. (Angstroms^-1)");
		mu_inc.setId(++idNum);

		// Add everything to the row template.
		template.add(id);
		template.add(mat);
		template.add(sld);
		template.add(mu_abs);
		template.add(mu_inc);
		template.add(thickness);
		template.add(roughness);
		// Set the template
		table.setRowTemplate(template);

		// Add this to the form
		form.addComponent(table);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to setup the name and description of the model.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Local Declarations
		String desc = "This item builds models for " + "Reflectivity.";

		// Describe the Item
		setName("Reflectivity Model Builder");
		setDescription(desc);
		itemType = ItemType.Model;

		// Setup the action list. Remove key-value pair support.
		// allowedActions.remove(taggedExportActionString);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param preparedForm
	 *            The form prepared for review.
	 * @return The Form's status if the review was successful or not.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected FormStatus reviewEntries(Form preparedForm) {
		// begin-user-code
		return super.reviewEntries(preparedForm);
		// end-user-code
	}
}
