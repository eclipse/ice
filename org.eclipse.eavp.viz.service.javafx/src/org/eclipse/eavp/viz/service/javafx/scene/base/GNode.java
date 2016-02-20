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
package org.eclipse.eavp.viz.service.javafx.scene.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;

/**
 * <p>
 * INode model implementation. This class is not meant to be extended, either
 * implement new functionality via the {@link INode} interface or with
 * attachments via {@link IAttachment}.
 * </p>
 *
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public final class GNode implements INode {

    /** */
    private List<INode> children;

    /** */
    private Map<Class<?>, List<IAttachment>> attachments;

    /** */
    private Map<String, Object> properties;

    /** */
    private boolean visible;

    /** */
    private INode parent;

    /** */
    private String name;

    /**
     *
     */
    public void addChild(INode node) {
        checkNode(node);

        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(node);

        node.setParent(this);
    }

    /**
     *
     */
    public void removeChild(INode node) {
        if (children == null) {
            return;
        }

        if (!children.contains(node)) {
            return;
        }

        checkNode(node);

        children.remove(node);

        node.setParent(null);
    }

    public void removeAllChildren() {
        if (children == null) {
            return;
        }

        children.clear();
    }

    public List<INode> getChildren(boolean copy) {
        if (children == null) {
            return Collections.emptyList();
        }

        if (copy) {
            return new ArrayList<INode>(children);
        } else {
            return children;
        }
    }

    public void attach(IAttachment attachment) {
        if (attachments == null) {
            attachments = new HashMap<>();
        }

        if (attachment == null) {
            throw new IllegalArgumentException("Null attachment.");
        }

        Class<? extends IAttachment> attachmentClazz = attachment.getClass();

        List<IAttachment> currentAttachmentList = attachments.get(attachment.getClass());

        if (currentAttachmentList == null) {
            currentAttachmentList = new ArrayList<>();
            currentAttachmentList.add(attachment);
            attachments.put(attachmentClazz, currentAttachmentList);
            return;
        }

        if (attachment.isSingleton() && hasAttachment(attachment.getClass())) {
            IAttachment existingSingleton = currentAttachmentList.get(0);
            existingSingleton.detach(this);
            currentAttachmentList.clear();
        }

        currentAttachmentList.add(attachment);

        attachment.attach(this);
    }

    public void detach(IAttachment attachment) {
        if (attachments == null) {
            return;
        }

        attachment.detach(this);
    }

    public boolean hasAttachment(Class<?> attachmentClazz) {
        if (attachmentClazz == null) {
            return false;
        }

        if (attachments.containsKey(attachmentClazz)) {
            List<IAttachment> list = attachments.get(attachmentClazz);

            if (list == null || list.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    public boolean supports(IAttachment attachment) {
        return true;
    }

    /**
     *
     */
    public Map<Class<?>, List<IAttachment>> getAttachments() {
        return attachments;
    }

    /**
     *
     */
    public List<IAttachment> getAttachments(Class<?> clazz) {
        return attachments.get(clazz);
    }

    /**
     * 
     */
    public Map<String, Object> getProperties() {
        if (properties == null) {
            return Collections.emptyMap();
        }

        return properties;
    }

    /**
     * 
     */
    public Object getProperty(String key) {
        if (properties == null) {
            return null;
        }

        return properties.get(key);
    }

    /**
     * 
     */
    public void setProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }

        properties.put(key, value);
    }

    /**
     * 
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    /**
     *
     * @param node
     */
    protected void checkNode(INode node) {
    }

    /**
     *
     */
    @Override
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     *
     * @return
     */
    public INode getParent() {
        return parent;
    }

    /**
     *
     * @return
     */
    public void setParent(INode parent) {
        this.parent = parent;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     */
    public String getName() {
        return name;
    }

}
