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

import org.eclipse.eavp.viz.service.javafx.canvas.Attachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;

/**
 * <p>
 * Abstract base for creating Camera implementations, which control how the
 * scene is viewed in a GeometryViewer.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 */
public abstract class CameraAttachment extends Attachment implements ICamera {

    /** The camera's name. */
    protected String name;

    /**
     * @see IAttachment#isSingleton()
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * @see ICamera#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Sets the name of the camera.
     * </p>
     * 
     * @param name
     *            the new name of this camera.
     */
    public void setName(String name) {
        this.name = name;
    }

}
