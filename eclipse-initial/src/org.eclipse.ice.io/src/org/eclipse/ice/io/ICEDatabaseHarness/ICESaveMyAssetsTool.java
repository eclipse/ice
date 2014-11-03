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
package org.eclipse.ice.io.ICEDatabaseHarness;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.eclipse.ice.item.Item;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A class that is designed to convert previously modified ICEItems
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICESaveMyAssetsTool {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The reference to the Item's EntityManagerFactory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EntityManagerFactory itemEMFactory;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The reference to the Form's EntityManagerFactory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EntityManagerFactory formEMFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICESaveMyAssetsTool() {
		// begin-user-code
		// TODO Auto-generated constructor stub
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This piece is called by the OSGI to set up the EntityManager factories.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param factory
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setEntityManagerFactory(EntityManagerFactory factory) {
		// begin-user-code

		// Get the properties
		String property = factory.getProperties().toString();

		// Only check out the factory if it isn't null
		if (factory != null) {
			// Check for the datastructures entity manager factory
			if (property
					.contains("jdbc:derby:database/datastructuresDatabase;create=true")) {
				System.out
						.println("ICESaveMyAssetsTool Message: Data Structures EntityManagerFactory set!");
				this.formEMFactory = factory;
			}
			// Check for the item entity manager factory
			else if (property
					.contains("jdbc:derby:database/itemDatabase;create=true")) {
				System.out
						.println("ICESaveMyAssetsTool Message: Item EntityManagerFactory set!");
				this.itemEMFactory = factory;
			} else {
				// Return if the entity manager factory is for something else.
				return;
			}
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadOldItemsAndForms() {
		// begin-user-code

		EntityManager itemEManager = this.itemEMFactory.createEntityManager();
		EntityManager formEManager = this.formEMFactory.createEntityManager();
		Item item1 = new Item();
		Item item2 = new Item();
		Query q, q2;

		// Keyed on TableName, and the associated list of items in each section
		// Yes, this is a HashMap that contains an embedded ArrayList of
		// ArrayList on value
		// HashMap: Represents the List of types of tables
		// First ArrayList: Maintains the list of entities in the table
		// ArrayList of ArrayList: Maintains the values of the attributes from
		// the database
		HashMap<String, ArrayList<ArrayList<Object>>> tableList = new HashMap<String, ArrayList<ArrayList<Object>>>();

		// ArrayList of Old Attributes on the current database
		ArrayList<String> oldAttributes = new ArrayList<String>();

		// Setup known arrayList of tableNames
		tableList.put("ITEM", new ArrayList<ArrayList<Object>>());
		tableList.put("JOBLAUNCHER", new ArrayList<ArrayList<Object>>());
		tableList.put("JOBPROFILE", new ArrayList<ArrayList<Object>>());
		Iterator<String> iter;

		// Try to get the driver from the database
		try {

			// Register the Driver
			Class.forName((String) this.itemEMFactory.getProperties().get(
					"javax.persistence.jdbc.driver"));
			// setup the Drive Manager
			Connection conn = DriverManager
					.getConnection((String) this.itemEMFactory.getProperties()
							.get("javax.persistence.jdbc.url"));

			// Get table names
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet tableSet = dbmd.getTables(null, null, null, null);
			System.err.println("Get the table names");
			while (tableSet.next()) {
				String strTableName = tableSet.getString("TABLE_NAME");
				System.err.println("TABLE_NAME is " + strTableName);
			}

			System.err.println();

			// Get attribute names
			System.err.println("Printing out column names");
			String query = "select * from Item";
			PreparedStatement pstmt;
			ResultSetMetaData rsmd;
			int numColumns;

			// Get the column names that represent the attribute names
			pstmt = conn.prepareStatement(query);
			rsmd = pstmt.getMetaData();
			numColumns = rsmd.getColumnCount();
			for (int i = 1; i <= numColumns; i++) {
				String str = rsmd.getColumnName(i);
				oldAttributes.add(str);
				System.err.println(str);
			}

			// Print a new line to break between output
			System.err.println();

			// Iterate over each table
			iter = tableList.keySet().iterator();
			while (iter.hasNext()) {

				// Get the tableName from the keys
				String tableName = iter.next();
				ArrayList<ArrayList<Object>> item;

				// Get the ArrayList of items from the collection of tables
				item = tableList.get(tableName);

				// Get values of attributes off each item and store it in memory
				ResultSet rs = conn.createStatement().executeQuery(
						"Select * From " + tableName);
				ResultSet currentSet;

				// Iterate over each Item
				while (rs.next()) {

					// Setup the attributeList
					ArrayList<Object> attributeList = new ArrayList<Object>();
					// Iterate over the columns on item
					for (int i = 1; i <= numColumns; i++) {
						attributeList.add(rs.getObject(i));
					}

					// Add the ArrayList to the collection
					item.add(attributeList);
				}
			}

			// Print out list
			iter = tableList.keySet().iterator();
			while (iter.hasNext()) {
				String tableName = iter.next();
				ArrayList<ArrayList<Object>> ptr;

				ptr = tableList.get(tableName);

				// Iterate over the items stored at that point
				for (int i = 0; i < ptr.size(); i++) {
					// Iterate over the values at each item
					System.err.println("Item: " + i + " Size of attributes: "
							+ ptr.get(i).size());
					for (int j = 0; j < ptr.get(i).size(); j++) {
						System.err.println("Attribute: " + oldAttributes.get(j)
								+ "  Value:" + ptr.get(i).get(j)
								+ " ClassType:" + ptr.get(i).get(j).getClass());
					}
					System.err.println();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// end-user-code
	}

	// Not in model
	public void flywayIntergration() {

		EntityManager itemEManager = this.itemEMFactory.createEntityManager();
		EntityManager formEManager = this.formEMFactory.createEntityManager();

		try {
			// Register the Driver
			Class.forName((String) this.itemEMFactory.getProperties().get(
					"javax.persistence.jdbc.driver"));
			// setup the Drive Manager
			Connection conn = DriverManager
					.getConnection((String) this.itemEMFactory.getProperties()
							.get("javax.persistence.jdbc.url"));
			javax.naming.Context context = new javax.naming.InitialContext();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Setups the database.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void start() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Cleans up the database by closing connections and resources.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void cleanUp() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}