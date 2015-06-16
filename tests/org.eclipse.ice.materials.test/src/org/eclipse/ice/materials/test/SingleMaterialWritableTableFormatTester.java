package org.eclipse.ice.materials.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.materials.MaterialWritableTableFormat;
import org.eclipse.ice.materials.SingleMaterialWritableTableFormat;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the {@link org.eclipse.ice.material.SingleMaterialWritableTableFormat} class.
 * 
 * 
 * 
 * @author Kasper Gammeltoft
 *
 */
public class SingleMaterialWritableTableFormatTester {

	
	/**
	 * Test materials
	 */
	static Material material;
	static Material material1;
	
	/**
	 * The table format to be tested
	 */
	static SingleMaterialWritableTableFormat tableFormat;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Create the Material for the test
		material = new Material();
		material.setName("SomeMaterial");
		material.setProperty("A", 1.0);
		material.setProperty("B", 2.0);
		material.setProperty("C", 3.0);
		
		material1 = new Material();
		material1.setName("SomeOtherMaterial");
		material1.setProperty("A", 5.0);
		material1.setProperty("D", 2.0);

		// Create the table format
		tableFormat = new SingleMaterialWritableTableFormat(material);

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.SingleMaterialWritableTableFormat#testSetMaterial()}
	 * .
	 */
	@Test
	public void testSetMaterial() {
		tableFormat.setMaterial(material1);
		assertEquals(material1, tableFormat.getMaterial());
		tableFormat.setMaterial(material);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.SingleMaterialWritableTableFormat#getColumnCount()}
	 * .
	 */
	@Test
	public void testGetColumnCount() {
		assertTrue(tableFormat.getColumnCount() == 2);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.SingleMaterialWritableTableFormat#getColumnValue()}
	 * .
	 */
	@Test
	public void testGetColumnValue() {
		assertEquals(tableFormat.getColumnValue("A", 1), material.getProperty("A"));
		assertEquals(tableFormat.getColumnValue("C", 0), "C");
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.SingleMaterialWritableTableFormat#setColumnValue()}
	 * .
	 */
	@Test
	public void testSetColumnValue() {
		double newval = 15.0;
		tableFormat.setColumnValue("B", newval, 1);
		assertTrue(material.getProperty("B") == newval);
		
		tableFormat.setColumnValue("C", "23", 1);
		assertTrue(material.getProperty("C") == 23);
	}

}
