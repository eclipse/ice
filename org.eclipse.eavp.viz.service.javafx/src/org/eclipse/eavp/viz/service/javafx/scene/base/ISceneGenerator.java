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

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;

import javafx.scene.Node;

/**
 * <p>
 * Generates an input
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public interface ISceneGenerator {

    /**
     *
     * @param newRoot
     * @param internalRoot
     * @param scene
     * @return
     */
    Node generateScene(INode newRoot);

    /**
     *
     * @param child
     * @return
     */
    Node generateNode(INode child);

    /**
     *
     * @param child
     */
    void processAttachmentGroups(INode child);

    /**
     *
     * @param node
     * @param currentAttachment
     */
    void processAttachment(INode node, IAttachment currentAttachment);

}