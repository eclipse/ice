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
package org.eclipse.ice.caebat.model;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.BatteryComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * An implementation of IComponentVisitor used for creating the iniFormat
 * required by Caebat given by specific components. The way this utility works
 * is that the user must visit the component with this instantiated visitor. It
 * will then set the iniFormat string based upon the Component visited. The user
 * can then call the getINIString call located on this visitor in order to read
 * the information off this object.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisitorINI implements IComponentVisitor {
	/**
	 * This value represents the string set ran by the last visitation
	 * operation. Will be set to null if an operation ran and fails. This holds
	 * a string representation of the ini format.
	 */
	private String iniFormat;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Visitation state. False if the TestVisitor has not been visited, true
	 * otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean visited;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisitorINI() {
		// begin-user-code

		// Set the default visitation state
		visited = false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the TestVisitor was visited, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the TestVisitor was visited by a Component, false
	 *         otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasVisited() {
		// begin-user-code
		return visited;
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

		// Set the visitation state to true if the component is not null
		if (component != null) {
			visited = true;
			this.iniFormat = "";

			// Loop through Entries and add key/value pairs (tag= value\n")
			for (int i = 0; i < component.retrieveAllEntries().size(); i++) {

				// Get the entry
				Entry entry = component.retrieveAllEntries().get(i);

				this.iniFormat += "" + entry.getTag() + " = "
						+ entry.getValue() + "\n";
			}

		}

		// Always set the value to null if there is a failed visit
		else {
			this.iniFormat = null;
			this.visited = false;
		}

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

		// Set the visitation state to true if the component is not null
		if (component != null) {
			visited = true;
		}
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

		// Set the visitation state to true if the component is not null and the
		// template size is two
		if (component != null && component.getRowTemplate() != null
				&& component.getRowTemplate().size() == 2) {
			visited = true;
			this.iniFormat = "";

			for (int i = 0; i < component.getRowIds().size(); i++) {
				int rowId = component.getRowIds().get(i);

				// Add preliminary table information if the iterator is 0
				if (i == 0) {

					// Add preliminary string info
					this.iniFormat += "[" + component.getName() + "]\n"
							+ "    NAMES =";

					// Add implementation hook. Required to get this file to
					// work correctly
					this.iniFormat += " INIT";

					for (int j = 0; j < component.getRowIds().size(); j++) {
						int rowId2 = component.getRowIds().get(j);
						this.iniFormat += " "
								+ component.getRow(rowId2).get(0).getValue();

					}

					// Add two newlines
					this.iniFormat += "\n\n";

					// Add implementation hook. Required to get this file to
					// work correctly
					this.iniFormat += "    [[INIT]]\n"
							+ "        IMPLEMENTATION = \n";

				}

				// For every iteration of rowId, add information about the port
				// name and value
				this.iniFormat += "    [["
						+ component.getRow(rowId).get(0).getValue() + "]]\n"
						+ "        IMPLEMENTATION = "
						+ component.getRow(rowId).get(1).getValue() + "\n";

			}

		}
		// Always set the value to null if there is a failed visit
		else {
			this.iniFormat = null;
			this.visited = false;
		}
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
			visited = true;
		}
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

		// Set the visitation state to true if the component is not null
		if (component != null) {
			visited = true;
		}
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

		if (component != null) {
			visited = true;
		}
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

		// Local Declarations
		ArrayList<String> masters = new ArrayList<String>();
		ArrayList<DataComponent> details = new ArrayList<DataComponent>();

		// Set the visitation state to true if the component is not null
		if (component != null && component.getAllowedMasterValues() != null) {

			// Check the details at each iteration to see the following:
			// 1.) Identify the one to one relationship between templates and
			// used pairs

			for (int i = 0; i < component.numberOfMasters(); i++) {

				// Get the masters and the respective details
				if (!masters.contains(component.getMasterAtIndex(i))) {

					// Add the masters
					masters.add(component.getMasterAtIndex(i));

					// Add the details
					details.add(component.getDetailsAtIndex(i));
				}

			}

			// if somehow the masters is not equal to the details (which will be
			// REALLY BAD)
			if (masters.size() != details.size()) {
				this.iniFormat = null;
				this.visited = false;
				return;
			}

			// Set the format to empty
			iniFormat = "";

			// Iterate over the masters list and then add the K/V pairs to the
			// details
			for (int j = 0; j < masters.size(); j++) {
				// If the value is not empty when trimmed, then create it
				if (!masters.get(j).trim().isEmpty()) {
					iniFormat += "[" + masters.get(j) + "]\n";

					// Iterate over the DataComponents and add entries to list
					for (int k = 0; k < details.get(j).retrieveAllEntries()
							.size(); k++) {
						iniFormat += "    "
								+ details.get(j).retrieveAllEntries().get(k)
										.getTag()
								+ " = "
								+ details.get(j).retrieveAllEntries().get(k)
										.getValue() + "\n";
					}

					// Add a newline
					iniFormat += "\n";
				}
			}

			// Everything was good, set to true!
			visited = true;
		}

		// Always set the value to null if there is a failed visit
		else {
			this.iniFormat = null;
			this.visited = false;
		}

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

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TimeDataComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TimeDataComponent component) {
		// begin-user-code

		// Visit it as a DataComponent first
		this.visit((DataComponent) component);

		// If the string is not null, then you will need to modify it for
		// TimeDataComponent
		if (this.iniFormat != null && this.visited) {

			// Add Tag to first line
			this.iniFormat = "[" + component.getName() + "]\n" + this.iniFormat;

			// Add spaces for every new line
			this.iniFormat = this.iniFormat.replace("\n", "\n    ");

			// Replace Mode correctly: true = REGULAR, false = EXPLICIT
			this.iniFormat = this.iniFormat.replace("MODE = True",
					"MODE = REGULAR");
			this.iniFormat = this.iniFormat.replace("MODE = False",
					"MODE = EXPLICIT");

		}

		// end-user-code
	}

	/**
	 * Returns the ini format or null if the visit operation failed.
	 * 
	 * @return
	 */
	public Object getINIString() {
		return this.iniFormat;
	}

	@Override
	public void visit(MeshComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BatteryComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AdaptiveTreeComposite component) {
		// TODO Auto-generated method stub

	}
}