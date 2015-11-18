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
package org.eclipse.ice.viz.service.javafx.internal.model;

import org.eclipse.ice.viz.service.geometry.scene.base.CameraAttachment;
import org.eclipse.ice.viz.service.geometry.scene.model.IAttachment;
import org.eclipse.ice.viz.service.geometry.scene.model.INode;
import org.eclipse.ice.viz.service.javafx.internal.Util;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;

/**
 * <p>
 * Implementation of CameraAttachment for JavaFX.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXCameraAttachment extends CameraAttachment {

    /** The JavaFX camera used with this attachment. */
    private Camera camera;

    /**
     * <p>
     * Creates a JavaFX camera attachment.
     * </p>
     * 
     * @param cam
     */
    public FXCameraAttachment(Camera cam) {
        this.camera = cam;
    }

    /**
     * @see IAttachment#attach(INode)
     */
    @Override
    public void attach(INode owner) {
        super.attach(owner);

        if (camera == null) {
            camera = new PerspectiveCamera();
        }

        Group fxGroup = Util.getFxGroup(owner);

        fxGroup.getChildren().add(fxGroup);
    }

    /**
     * @see IAttachment#detach(INode)
     */
    public void detach(INode owner) {
        super.attach(owner);

        Group fxGroup = Util.getFxGroup(owner);

        fxGroup.getChildren().remove(camera);
    }

    /**
     * <p>
     * Returns the JavaFX camera for this attachment.
     * </p>
     * 
     * @return the camera for this attachment
     */
    public Camera getFxCamera() {
        return camera;
    }

    /**
     * @see IAttachment#getType()
     */
    public Class<?> getType() {
        return CameraAttachment.class;
    }

}
