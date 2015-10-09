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
package org.eclipse.ice.viz.service.geometry.shapes;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Represents an ordered collection of shape components which are applied to a
 * parent set operator
 * </p>
 * <p>
 * Through the geometry composite pattern, an instance of ComplexShape is always
 * a parent of PrimitiveShapes, other ComplexShapes, or a collection of both
 * types.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "ComplexShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class ComplexShape extends AbstractShape {
    /**
     * <p>
     * Represents the type of boolean operator applied to the ComplexShape
     * </p>
     * <p>
     * Type of the geometry set operator applied to this set of shapes
     * </p>
     * 
     */
    @XmlAttribute
    private OperatorType operatorType;
    /**
     * <p>
     * Ordered list of shapes which are contained
     * </p>
     * <p>
     * List of shapes in the set
     * </p>
     * 
     */
    @XmlAnyElement()
    @XmlElementRefs(value = { @XmlElementRef(name = "ComplexShape", type = ComplexShape.class),
            @XmlElementRef(name = "PrimitiveShape", type = PrimitiveShape.class) })
    private ArrayList<IShape> shapes;

    /**
     * <p>
     * Calls AbstractShape's constructor and sets the operator type to None
     * </p>
     * 
     */
    public ComplexShape() {

        // Call AbstractShape's constructor
        super();

        // Initialize operatorType to None
        this.operatorType = OperatorType.None;

        // Initialize shapes list
        shapes = new ArrayList<IShape>();

    }

    /**
     * <p>
     * Calls AbstractShape's constructor and sets the operator type to the given
     * value
     * </p>
     * 
     * @param operatorType
     *            <p>
     *            The OperatorType to set for the new ComplexShape
     *            </p>
     */
    public ComplexShape(OperatorType operatorType) {

        // Call nullery constructor first
        this();

        // Set the given operatorType
        if (operatorType == null) {
            operatorType = OperatorType.None;
        }
        this.operatorType = operatorType;

    }

    /**
     * <p>
     * Accepts all values of the OperatorType enumerator
     * </p>
     * <p>
     * Once the OperatorType has been set to a value which is not None, the
     * OperatorType cannot be changed. Any additional calls to this operation
     * will be ignored.
     * </p>
     * 
     * @param operatorType
     *            <p>
     *            The type of the geometry set operator to be applied to this
     *            set of shapes
     *            </p>
     */
    public void setType(OperatorType operatorType) {

        // Fail silently if operatorType is null
        if (operatorType == null) {
            return;
        }
        // Set the type only if the current OperatorType is None
        if (this.operatorType == OperatorType.None) {
            this.operatorType = operatorType;

            // We've updated a value, so notify listeners
            notifyListeners();
        }

    }

    /**
     * <p>
     * Returns the OperatorType of the ComplexShape
     * </p>
     * 
     * @return
     *         <p>
     *         Type of the geometry set operator applied to this set of shapes
     *         </p>
     */
    public OperatorType getType() {
        return operatorType;
    }

    /**
     * <p>
     * Appends a single shape to the end of the shape list
     * </p>
     * 
     * @param shape
     *            <p>
     *            The shape to be added
     *            </p>
     */
    public void addShape(IShape shape) {

        // Check that the shape is not null or a reference to itself
        if (shape == null || shape == this) {
            return;
        }
        shapes.add(shape);

        // Notify listeners
        notifyListeners();

        ((AbstractShape) shape).setParent(this);

    }

    /**
     * <p>
     * Removes a single shape from the shape list
     * </p>
     * 
     * @param shape
     *            <p>
     *            The shape reference to find and remove from the ComplexShape
     *            </p>
     */
    public void removeShape(IShape shape) {

        // Remove the shape from the shapes list
        boolean success = shapes.remove(shape);

        if (success) {
            // If the shape was actually removed, notify listeners
            notifyListeners();

            ((AbstractShape) shape).setParent(null);
        }

    }

    /**
     * <p>
     * Returns a reference to the list of shapes containing in this ComplexShape
     * </p>
     * 
     * @return
     *         <p>
     *         List of shapes contained in the ComplexShape
     *         </p>
     */
    public ArrayList<IShape> getShapes() {

        // Return a reference to the shapes array
        return shapes;

    }

    /**
     * <p>
     * Replaces the list of shapes with the given ArrayList
     * </p>
     * 
     * @param shapes
     *            <p>
     *            The list of shapes which will replace the old list
     *            </p>
     */
    public void setShapes(ArrayList<IShape> shapes) {

        // Check that the new shapes list is not null
        if (shapes == null) {
            return;
        }
        // Remove the parents from all existing children
        for (IShape shape : shapes) {
            ((AbstractShape) shape).setParent(null);
        }

        // Set the shapes list to the same reference as shapes
        this.shapes = shapes;

        // Set the parent for each of the new children
        for (IShape shape : shapes) {
            ((AbstractShape) shape).setParent(this);
        }

        // Notify listeners
        notifyListeners();

    }

    /**
     * <p>
     * This operation returns the hashcode value of the ComplexShape.
     * </p>
     * 
     * @return
     *         <p>
     *         The hashcode of the ICEObject.
     *         </p>
     */
    @Override
    public int hashCode() {

        // Get the initial hashcode
        int hash = super.hashCode();

        // Hash the OperatorType
        hash = 31 * hash + operatorType.hashCode();

        // Hash each shape in the shapes list
        for (IShape shape : shapes) {
            hash = 31 * hash + shape.hashCode();
        }

        return hash;

    }

    /**
     * <p>
     * This operation is used to check equality between this ComplexShape and
     * another ComplexShape. It returns true if the ComplexShapes are equal and
     * false if they are not.
     * </p>
     * 
     * @param otherObject
     *            <p>
     *            The other ICEObject that should be compared with this one.
     *            </p>
     * @return
     *         <p>
     *         True if the ICEObjects are equal, false otherwise.
     *         </p>
     */
    @Override
    public boolean equals(Object otherObject) {

        // Check if a similar reference
        if (this == otherObject) {
            return true;
        }
        // Check that the other object is not null and an instance of the
        // ComplexShape
        if (otherObject == null || !(otherObject instanceof ComplexShape)) {
            return false;
        }
        // Check that these objects have the same ICEObject data
        if (!super.equals(otherObject)) {
            return false;
        }
        // At this point, other object must be a PrimitiveShape, so cast it
        ComplexShape otherComplexShape = (ComplexShape) otherObject;

        // Check for equal OperatorTypes
        if (this.operatorType != otherComplexShape.operatorType) {
            return false;
        }
        // Check for equal number of shapes in shapes list
        if (this.shapes.size() != otherComplexShape.shapes.size()) {
            return false;
        }
        // Check for equal elements in shapes list
        // The list must be ordered similarly for this test to pass.
        // This is desired for an intersection, for example, where the first
        // element has significance.
        int numShapes = shapes.size();
        for (int index = 0; index < numShapes; index++) {

            // Check for equal shapes in the current index
            if (!shapes.get(index).equals(otherComplexShape.shapes.get(index))) {
                return false;
            }
        }

        // The two shapes might as well be equal.
        return true;

    }

    /**
     * <p>
     * This operation copies the contents of a ComplexShape into the current
     * object using a deep copy.
     * </p>
     * 
     * @param iceObject
     *            <p>
     *            The ICEObject from which the values should be copied.
     *            </p>
     */
    public void copy(ComplexShape iceObject) {

        // Return if object is null
        if (iceObject == null) {
            return;
        }
        // Copy the ICEObject data
        super.copy(iceObject);

        // Copy operatorType
        this.operatorType = iceObject.operatorType;

        // Copy shapes list
        this.shapes.clear();

        for (IShape shape : iceObject.shapes) {

            // Assume that all IShapes are AbstractShapes
            // (We must do this because IShape is an interface. It cannot
            // extend ICEObject to be cloned.)

            AbstractShape clonedShape = (AbstractShape) ((AbstractShape) shape).clone();
            clonedShape.setParent(this);

            this.shapes.add(clonedShape);
        }

    }

    /**
     * <p>
     * This operation returns a clone of the ComplexShape using a deep copy.
     * </p>
     * 
     * @return
     *         <p>
     *         The new clone.
     *         </p>
     */
    @Override
    public Object clone() {

        // Create a new ComplexShape
        ComplexShape complexShape = new ComplexShape();

        // Copy `this` into complexShape
        complexShape.copy(this);

        return complexShape;

    }

    /**
     * (non-Javadoc)
     * 
     * @see IUpdateable#update(String updatedKey, String newValue)
     */
    @Override
    public void update(String updatedKey, String newValue) {
        // Not implemented
    }

    /**
     * (non-Javadoc)
     * 
     * @see IShape#acceptShapeVisitor(IShapeVisitor visitor)
     */
    @Override
    public void acceptShapeVisitor(IShapeVisitor visitor) {

        // Only visit if it is not null
        if (visitor != null) {
            visitor.visit(this);
        }
    }
}