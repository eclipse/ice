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

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;

/**
 * <p>
 * Interface describing a type of solid in the geometry editor
 * </p>
 * <p>
 * Each IShape is a node in the CSG tree and can affect the appearance of the
 * final surface or volume mesh when processed with the MeshKit.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface IShape extends IVizUpdateable {
    /**
     * <p>
     * Returns a copy of the transformation matrix associated with this shape
     * node
     * </p>
     * 
     * @return
     *         <p>
     *         The transformation matrix applied to this node in the CSG tree
     *         </p>
     */
    public Transformation getTransformation();

    /**
     * <p>
     * Replaces the transformation matrix with a copy of the given Matrix4x4
     * </p>
     * <p>
     * Returns whether the setting was successful
     * </p>
     * 
     * @param transformation
     *            <p>
     *            The transformation matrix to be applied to the shape
     *            </p>
     * @return
     *         <p>
     *         True if setting the transformation was successful, false
     *         otherwise
     *         </p>
     */
    public boolean setTransformation(Transformation transformation);

    /**
     * <p>
     * Returns the value associated with the property key
     * </p>
     * <p>
     * If the key does not exist, this operation returns null.
     * </p>
     * 
     * @param key
     *            <p>
     *            The key corresponding to the desired property value
     *            </p>
     * @return
     *         <p>
     *         The desired value, or null if the key does not exist
     *         </p>
     */
    public String getProperty(String key);

    /**
     * <p>
     * Sets the property value associated with the key string
     * </p>
     * <p>
     * If the key does not yet exist, append the key/value pair to the end of
     * the property list. If it exists, find and replace the property value with
     * the new one.
     * </p>
     * 
     * @param key
     *            <p>
     *            The new or pre-existing key
     *            </p>
     * @param value
     *            <p>
     *            The new value
     *            </p>
     * @return
     *         <p>
     *         True if the property setting is valid, false otherwise
     *         </p>
     */
    public boolean setProperty(String key, String value);

    /**
     * <p>
     * Removes the value associated with the key in the properties list
     * </p>
     * <p>
     * This operation returns whether the key was found and removed.
     * </p>
     * 
     * @param key
     *            <p>
     *            The key associated with the value to remove
     *            </p>
     * @return
     *         <p>
     *         True if the value was found and removed, false otherwise
     *         </p>
     */
    public boolean removeProperty(String key);

    /**
     * <p>
     * Calls back onto the visitor's visit() operation, revealing the concrete
     * type of the IShape
     * </p>
     * <p>
     * The name of this operation is changed from the typical naming conventions
     * of the visitor pattern to avoid conflicts with the Component::accept()
     * operation.
     * </p>
     * 
     * @param visitor
     *            <p>
     *            The IShapeVisitor to call back in order to reveal the type of
     *            this IShape
     *            </p>
     */
    public void acceptShapeVisitor(IShapeVisitor visitor);

    /**
     * <p>
     * Returns the parent associated with this shape, or null if the shape does
     * not have a parent
     * </p>
     * 
     * @return
     *         <p>
     *         The IShape's parent
     *         </p>
     */
    public IShape getParent();
}