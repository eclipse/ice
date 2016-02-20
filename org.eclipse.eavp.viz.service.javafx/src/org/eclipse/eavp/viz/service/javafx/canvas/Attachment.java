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
package org.eclipse.eavp.viz.service.javafx.canvas;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;

/**
 * <p>
 * Base for IAttachment implementations.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public abstract class Attachment implements IAttachment {

    /** The node that owns this attachment (is attached). */
    protected INode owner;

    /**
     * @see IAttachment#getOwner()
     */
    public INode getOwner() {
        return owner;
    }

    /**
     * @see IAttachment#attach(INode)
     */
    public void attach(INode owner) {
        this.owner = owner;
    }

    /**
     * @see IAttachment#detach(INode)
     */
    public void detach(INode owner) {
        this.owner = null;
    }

    /**
     * @see IAttachment#isSingleton()
     */
    public boolean isSingleton() {
        return false;
    }

}
