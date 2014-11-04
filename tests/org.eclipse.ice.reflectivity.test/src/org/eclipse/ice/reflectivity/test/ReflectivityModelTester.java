package org.eclipse.ice.reflectivity.test;

import static org.junit.Assert.*;

import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.reflectivity.ReflectivityModel;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReflectivityModelTester {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the workspace. It copies the necessary MOOSE data
	 * files into ${workspace}/MOOSE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ReflectivityModel and makes sure that it can
	 * properly construct its Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Local Declarations
		int tableCompID = 1;
		TableComponent table = null;
		
		// Just create one with the nullary constructor
		// No need to check the Item with a IProject instance
		ReflectivityModel model = new ReflectivityModel();

		// Make sure we have a form and some components
		assertNotNull(model.getForm());
		assertEquals(1, model.getForm().getComponents().size());
		
		// Get the table component
		table = (TableComponent) model.getForm().getComponent(tableCompID);
		
		// Make sure it's not null and the name is correct
		assertNotNull(table);
		assertEquals("Reflectivity Input Data", table.getName());

		assertEquals(7, table.getRowTemplate().size());
		
		// end-user-code
	}

}
