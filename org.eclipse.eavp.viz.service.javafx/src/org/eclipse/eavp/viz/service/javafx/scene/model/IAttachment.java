/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.scene.model;

/**
 * <p>
 * Interface for attachments, which provide new capabilities for INode
 * instances. These capabilities can be things like rendering geometry,
 * providing advanced control over the node and more.
 * </p>
 * 
 * <p>
 * Because inheritance is a very poor, brittle way to extend node capabilities
 * in a scene hierarchy, this approach allows for design via composition.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public interface IAttachment {

    /**
     * <p>
     * Returns the node this attachment is attached to or null if there is no
     * attached node.
     * </p>
     * 
     * @return the INode currently attached or null if a node has not been
     *         attached.
     */
    public INode getOwner();

    /**
     * <p>
     * Adds this instance to the supplied node. The attachment's functionality
     * will be applied to the new owner node.
     * </p>
     * 
     * <p>
     * If the attachment is a singleton, this will replace any existing
     * attachment of the same type.
     * </p>
     * 
     * @param owner
     *            the node to attach to
     */
    public void attach(INode owner);

    /**
     * <p>
     * Removes this instance from the supplied node. Any functionality provided
     * by this attachment will be removed from the owner node.
     * </p>
     * 
     * <p>
     * If the supplied node is not attached, this is a no-op.
     * </p>
     * 
     * @param owner
     *            the node to detach from
     */
    public void detach(INode owner);

    /**
     * <p>
     * If the attachment is a singleton, only a single instance of the
     * attachment can be attached to any given node.
     * </p>
     * 
     * @return true if the attachment type is a singleton, false otherwise.
     */
    public boolean isSingleton();

    /**
     * <p>
     * Returns the class type of the attachment. Note that this may not be the
     * same as the concrete type.
     * </p>
     * 
     * @return the class type of the attachment
     */
    public Class<?> getType();

}
