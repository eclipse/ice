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
package org.eclipse.ice.client.widgets.geometry;

import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Provides IShape objects for a TreeViewer, given a parent IShape
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ShapeTreeContentProvider implements ITreeContentProvider,
		IShapeVisitor {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Temporary variable for setting the return value of getChildren when the
	 * visit() operation is called
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Object[] temporaryChildren = null;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the child shapes of the given parent shape, if any
	 * </p>
	 * <p>
	 * If a PrimitiveShape, an empty ComplexShape, or null is passed, this
	 * operation returns an empty array of Objects.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param parentElement
	 *            <p>
	 *            The parent IShape element
	 *            </p>
	 * @return <p>
	 *         The child IShapes
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object[] getChildren(Object parentElement) {
		// begin-user-code

		// If the element is an IShape, call its accept() operation to
		// trigger the visit() call

		if (parentElement instanceof IShape) {
			temporaryChildren = null;

			// Call the parentShape's accept operation to call the appropriate
			// visit member function in this class

			IShape parentShape = (IShape) parentElement;
			parentShape.acceptShapeVisitor(this);

			// Return the result of the visit() operation

			return temporaryChildren;
		} else {
			return null;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the child shape elements of a root GeometryComponent when the
	 * input of the shape TreeViewer is set or reset
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputElement
	 *            <p>
	 *            The input GeometryComponent
	 *            </p>
	 * @return <p>
	 *         The child IShapes
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object[] getElements(Object inputElement) {
		// begin-user-code

		// If the element is a GeometryComponent, return its shapes
		if (inputElement instanceof GeometryComponent) {
			// Return an array of the GeometryComponent's shapes
			GeometryComponent parentGeometry = (GeometryComponent) inputElement;
			return parentGeometry.getShapes().toArray();
		} else {
			return null;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the parent of the element if it exists
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param element
	 *            <p>
	 *            The child IShape
	 *            </p>
	 * @return <p>
	 *         The parent IShape
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object getParent(Object element) {
		// begin-user-code

		// Return null if the element is not an IShape

		if (!(element instanceof IShape)) {
			return null;
		}
		// Return the object's parent

		IShape shape = (IShape) element;
		return shape.getParent();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns whether the given element has children
	 * </p>
	 * <p>
	 * In this implementation, there are no optimizations to quickly retrieve
	 * whether the element has children, so the array of child objects is found
	 * and counted.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param element
	 *            <p>
	 *            The IShape to check for children
	 *            </p>
	 * @return <p>
	 *         Represents whether the element has children
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean hasChildren(Object element) {
		// begin-user-code

		// Get the children from the getChildren operation and return whether
		// there are elements in the array

		// No optimizations can be made to quickly retrieve the number of
		// children from an object, so the best we can do is retrieve the
		// entire array of objects and count the number of elements.

		Object[] elements = getChildren(element);

		if (elements != null) {
			return elements.length > 0;
		} else {
			return false;
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IContentProvider#dispose()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void dispose() {
		// begin-user-code

		// We don't need to dispose of anything.

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IContentProvider#inputChanged(Viewer viewer, Object oldInput, Object
	 *      newInput)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// begin-user-code

		// The state of this class does not depend on the input, so we do not
		// need to change the state when the input of the TreeViewer changes.

		// end-user-code
	}

	/**
	 * The blank state item to display in the shape TreeViewer when a
	 * ComplexShape has no children
	 * 
	 * @author abd
	 * 
	 */
	public class BlankShape {

		/**
		 * The default label of the BlankShape, designed to be as generic as
		 * possible
		 */
		public static final String TEXT = "<add shape>";

		/**
		 * The shape which "contains" this blank shape object
		 */
		private IShape parent;

		/**
		 * Initializes the BlankShape with a parent
		 * 
		 * @param parent
		 *            The parent shape in the TreeViewer hierarchy
		 */
		public BlankShape(IShape parent) {
			this.parent = parent;
		}

		/**
		 * Returns the parent of this BlankShape
		 * 
		 * @return The parent shape
		 */
		public IShape getParent() {
			return parent;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShapeVisitor#visit(ComplexShape complexShape)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(ComplexShape complexShape) {
		// begin-user-code

		// IShape is a ComplexShape, so put its children in the temporary
		// children field

		temporaryChildren = complexShape.getShapes().toArray();

		// Use a blank state if there are no children to display

		if (temporaryChildren.length == 0) {
			temporaryChildren = new Object[] { new BlankShape(complexShape) };
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShapeVisitor#visit(PrimitiveShape primitiveShape)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PrimitiveShape primitiveShape) {
		// begin-user-code

		// IShape is a PrimitiveShape, so it has no children :(

		temporaryChildren = new Object[0];

		// end-user-code
	}
}