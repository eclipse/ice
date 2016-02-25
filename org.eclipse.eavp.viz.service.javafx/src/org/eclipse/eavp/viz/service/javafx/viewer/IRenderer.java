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
 * Interface defines logic for executing and managing a scene via a viewer.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public interface IRenderer {

    /**
     * <p>
     * Creates an attachment of the specified type.
     * </p>
     * 
     * @param clazz
     *            the type of IAttachment to allocate.
     * 
     * @return an IAttachment instance of the specified type or null if the
     *         attachment cannot be allocated.
     */
    public IAttachment createAttachment(Class<? extends IAttachment> clazz);

    /**
     * <p>
     * Returns true if the renderer supports the supplied attachment type, false
     * otherwise.
     * </p>
     * 
     * @param clazz
     *            the type of attachment to check for
     * 
     * @return true if the renderer supports the supplied attachment type, false
     *         otherwise.
     */
    public boolean supportsAttachment(Class<? extends IAttachment> clazz);

    /**
     * <p>
     * Associates the supplied IAttachmentManager with the renderer, so
     * attachments can be allocated with the associated type.
     * </p>
     * 
     * @param type
     *            the type of attachments supplied by the manager
     * 
     * @param mgr
     *            the manager instance to handle allocating attachments
     */
    void register(Class<? extends IAttachment> type, IAttachmentManager mgr);

    /**
     * <p>
     * Removes any manager associated with the supplied type from the renderer.
     * </p>
     * 
     * @param type
     *            the type of attachment manager to unregister
     */
    void unregister(Class<? extends IAttachment> type);

}
