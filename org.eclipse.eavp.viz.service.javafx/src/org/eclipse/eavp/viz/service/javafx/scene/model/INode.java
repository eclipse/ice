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

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Interface representation of a node in a scene. A node is an element in a
 * scene that can contain other nodes, such: as child nodes, attachments which
 * provide new capabilities (rendering, control, etc.) and properties which can
 * be used to define node specific attributes.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public interface INode {

    /** Property key for accessing the associated renderer node. */
    public static final String RENDERER_NODE_PROP = "RENDERER_NODE";

    /**
     *
     * <p>
     * Returns the parent of the node, or null if the node is not associated
     * with a parent.
     * </p>
     * 
     * @return the parent of the node
     */
    public INode getParent();

    /**
     * <p>
     * Sets the parent of the node.
     * </p>
     * 
     * @param parent
     *            the parent of the node to set
     */
    public void setParent(INode parent);

    /**
     * <p>
     * Returns the human readable name of the node.
     * </p>
     * 
     * @return a String containing the human readable name of the node.
     */
    public String getName();

    /**
     * <p>
     * Adds a node to this node as a child node. Once the child has been added,
     * the child node will be linked to the parent for transforms and other
     * operations.
     * </p>
     * 
     * @param node
     *            node to add as child
     */
    public void addChild(INode node);

    /**
     * <p>
     * Removes the supplied node as a child from this node. If node is not
     * currently a child, this is a no-op.
     * </p>
     * 
     * @param node
     *            node to remove as child
     */
    public void removeChild(INode node);

    /**
     * <p>
     * Removes all children from this node.
     * </p>
     * 
     * @param node
     *            removes all children from this node.
     */
    public void removeAllChildren();

    /**
     * <p>
     * Returns a List of this node's children, optionally returning a copy of
     * the List.
     * </p>
     * 
     * @return copy if true, the list will be a copy of the node's internal
     *         List. if false, the list will be a reference to the internal
     *         List.
     */
    public List<INode> getChildren(boolean copy);

    /**
     * <p>
     * Sets the visibility of the node. If the node is not visible, it will not
     * show up in a viewport.
     * </p>
     * 
     * @param visible
     *            if true, the node will be rendered in visualizations. if
     *            false, the node will not be rendered.
     */
    public void setVisible(boolean visible);

    /**
     * <p>
     * Returns if the node is visible.
     * </p>
     * 
     * @return true if the node is visible, false otherwise.
     */
    public boolean isVisible();

    /**
     * <p>
     * Adds the supplied attachment to this node. The attachment's functionality
     * will be applied to this node.
     * </p>
     * 
     * @param attachment
     *            the attachment to attach
     */
    public void attach(IAttachment attachment);

    /**
     * <p>
     * Removes the supplied attachment to this node. The attachment's
     * functionality with be removed from this node.
     * </p>
     * 
     * <p>
     * If the attachment is not currently attached, this is a no-op.
     * </p>
     * 
     * @param attachment
     *            the attachment to remove.
     */
    public void detach(IAttachment attachment);

    /**
     * <p>
     * Returns true if this node has an attachment of the supplied class type.
     * </p>
     *
     * @param attachmentClass
     *            the attachment class to check for.
     * 
     * @return true if the attachment type exists in this node, false otherwise.
     */
    public boolean hasAttachment(Class<?> attachmentClass);

    /**
     * <p>
     * Returns true if the node supports the supplied attachment. Some
     * attachments may not be used together.
     * </p>
     * 
     * @param attachment
     *            the attachment to check for
     * 
     * @return true if the node supports the attachment, false otherwise
     */
    public boolean supports(IAttachment attachment);

    /**
     * <p>
     * Returns a Map of all the node's attachments.
     * </p>
     * 
     * @return a Map of the node's current attachments
     */
    public Map<Class<?>, List<IAttachment>> getAttachments();

    /**
     * <p>
     * Returns a List of the node's attachments that are of the supplied type.
     * </p>
     * 
     * @param clazz
     *            the attachment class type to retrieve
     * 
     * @return a List of the node's attachments of the specified type.
     */
    public List<IAttachment> getAttachments(Class<?> clazz);

    /**
     * <p>
     * Returns a Map of the node's current properties.
     * </p>
     *
     * @return a Map of the node's current properties.
     */
    public Map<String, Object> getProperties();

    /**
     * <p>
     * Returns the node property of the specified type or null if the property
     * doesn't exist.
     * </p>
     * 
     * @return the node property of the specified type or null if the property
     *         doesn't exist
     */
    public Object getProperty(String key);

    /**
     * <p>
     * Sets the property identified by the key String to the supplied value.
     * </p>
     * 
     * @param key
     *            the String identifier of the property
     * @param value
     *            the value of the property
     */
    public void setProperty(String key, Object value);

    /**
     * <p>
     * Returns true if the property has been set on this object, false
     * otherwise.
     * </p>
     * 
     * @param key
     *            the property key to check for
     * 
     * @return true if the property exists, false otherwise
     */
    public boolean hasProperty(String key);

}
