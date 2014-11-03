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
import org.eclipse.ice.datastructures.updateableComposite.Component;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * An implementation of IComponentVisitor used for copying entries. In order to
 * use this class correctly, the Component must be set with the constructor.
 * Then the visit operation is called on another component. This Class ASSUMES
 * that the entries will be equal in value between the components (based on
 * their tags) and that the components visited are of the same CLASS. This will
 * error out to catch if the entry sizes are not the same (See DataComponent
 * visit for example) or if they are not of the same class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisitorEntryCopy implements IComponentVisitor {
	/**
	 * This value represents the item to compare to. If it is not of this
	 * instance, then it will not visit.
	 */
	private Component copyEntriesFrom;

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
	 * @param The
	 *            component used to copy entries from.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisitorEntryCopy(Component copyEntriesFrom) {
		// begin-user-code

		// Set the default visitation state
		visited = false;
		this.copyEntriesFrom = copyEntriesFrom;

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

		this.visited = false;

		// Set the visitation state to true if the component is not null
		if (component != null && copyEntriesFrom != null
				&& copyEntriesFrom instanceof DataComponent) {
			component.copy((DataComponent) copyEntriesFrom);
			this.visited = true;
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
		this.visited = false;

		// Set the visitation state to true if the component is not null and the
		// template size is two
		if (component != null && this.copyEntriesFrom != null
				&& this.copyEntriesFrom instanceof TableComponent) {
			visited = true;
			component.copy((TableComponent) copyEntriesFrom);

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

		if (component != null
				&& copyEntriesFrom instanceof MasterDetailsComponent) {

			MasterDetailsComponent copiedFromMasters = (MasterDetailsComponent) copyEntriesFrom;

			// Reset the MasterDetailsComponent
			while (component.getMasterAtIndex(0) != null) {
				component.deleteMasterAtIndex(0);
			}

			// Add the masters and copy the details.
			for (int i = 0; i < copiedFromMasters.numberOfMasters(); i++) {
				int masterId = component.addMaster();
				component.setMasterInstanceValue(masterId,
						copiedFromMasters.getMasterAtIndex(i));
				component.getDetails(masterId).copy(
						copiedFromMasters.getDetailsAtIndex(i));
			}
			visited = true;
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

		// end-user-code
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