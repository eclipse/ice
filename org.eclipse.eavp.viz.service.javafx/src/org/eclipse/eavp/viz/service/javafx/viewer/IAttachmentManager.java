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
package org.eclipse.eavp.viz.service.javafx.viewer;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;

/**
 * <p>
 * Interface for managing attachment allocations in a geometry viewer
 * implementation.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public interface IAttachmentManager {

    /**
     * <p>
     * Create a new instance of IAttachment.
     * </p>
     * 
     * @return an IAttachment instance of the manager's type
     */
    IAttachment allocate();

    /**
     * <p>
     * Frees an instance of IAttachment.
     * </p>
     * 
     * @param attach
     *            an IAttachment to free
     */
    void destroy(IAttachment attach);

    /**
     * <p>
     * Called when the attachment should update its logic.
     * </p>
     */
    void update();

}
