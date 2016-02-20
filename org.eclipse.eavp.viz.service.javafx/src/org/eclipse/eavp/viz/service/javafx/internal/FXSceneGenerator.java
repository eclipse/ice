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
package org.eclipse.eavp.viz.service.javafx.internal;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.eavp.viz.service.javafx.scene.base.ISceneGenerator;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;

import java.util.Queue;
import java.util.Set;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * <p>
 * Generates JavaFX scenes from ICE geometry model inputs.
 * </p>
 *
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXSceneGenerator implements ISceneGenerator {

    /**
     * <p>
     * Iteratively generates a JavaFX scene from the input {@link INode} model.
     * TODO: parallelize scene generation.
     * </p>
     *
     * @param iceRoot
     *            the root model node of the scene
     * @param fxRoot
     *            the fxRoot
     */
    @Override
    public Node generateScene(INode iceRoot) {

        // Queue for managing search
        Queue<INode> processQueue = new ArrayDeque<>();
        Set<INode> nodeMap = new HashSet<INode>();

        // Queue up the root node
        processQueue.add(iceRoot);
        nodeMap.add(iceRoot);

        // Iterate through the tree, generating the FX scene
        while (!processQueue.isEmpty()) {
            INode currentNode = (INode) processQueue.poll();

            generateNode(currentNode);

            List<INode> children = currentNode.getChildren(false);
            int size = children.size();

            for (int i = 0; i < size; i++) {
                INode child = children.get(i);

                if (!nodeMap.contains(child)) {
                    processQueue.add(child);
                    nodeMap.add(child);
                }
            }
        }

        Node root = Util.getFxGroup(iceRoot);

        return root;
    }

    /**
     * <p>
     * Generates a JavaFX node from the scene model node.
     * </p>
     * 
     * @param node
     *            the scene model node to use to generate a JavaFX node.
     * 
     * @return JavaFX node representative of the input model node.
     */
    @Override
    public Node generateNode(INode node) {
        Group fxNode = new Group();
        node.setProperty(INode.RENDERER_NODE_PROP, fxNode);

        processAttachmentGroups(node);

        INode parent = (INode) node.getParent();

        // Add the child to scene, if it's not the root
        if (parent != null) {
            Group fxGroup = Util.getFxGroup(parent);
            fxGroup.getChildren().add(fxNode);
        }

        // Add a reference to ICE model on the javafx side
        fxNode.getProperties().put(INode.class, node);

        return fxNode;
    }

    /**
     * <p>
     * Processes all attachments for the given model node, for all types of
     * attachments.
     * </p>
     * 
     * @param child
     *            the input model node
     */
    @Override
    public void processAttachmentGroups(INode child) {
        Map<Class<?>, List<IAttachment>> attachmentGroups = child.getAttachments();

        if (attachmentGroups == null) {
            return;
        }

        // Iterate through each attachment type and each attachment
        for (Entry<Class<?>, List<IAttachment>> attachType : attachmentGroups.entrySet()) {
            List<IAttachment> attachmentItems = attachType.getValue();

            int size = attachmentItems.size();

            for (int i = 0; i < size; i++) {
                IAttachment currentAttachment = attachmentItems.get(i);
                processAttachment(child, currentAttachment);
            }
        }
    }

    /**
     * <p>
     * Processes the node attachment, generating the JavaFX specific
     * functionality related to the attachment.
     * </p>
     * 
     * @param child
     *            the node who owns the attachments to process
     * @param currentAttachment
     *            the attachment to process.
     */
    @Override
    public void processAttachment(INode node, IAttachment currentAttachment) {
        currentAttachment.attach(node);
    }

}
