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
package org.eclipse.ice.viz.service.javafx.internal.scene.camera;

import javafx.embed.swt.FXCanvas;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * <p>
 * ArcBallController provides 3D editor like features to a camera, by rotating
 * around a point and letting the user zoom in and out.
 * </p>
 */
public class ArcBallController extends CameraController {

    /** */
    Transform xform;

    /** */
    Affine transform;

    /** */
    private Camera camera;

    /** */
    private Scene scene;

    /** */
    double anchorX;

    /** */
    double anchorY;

    /** */
    double anchorAngle;

    /** */
    private double sphereRadius;

    /** */
    private double height;

    /** */
    private double width;

    /** */
    protected Point3D currentRot;

    /** */
    private Point3D startRot;

    /** */
    protected boolean activeRotation;

    /** */
    private FXCanvas canvas;

    /**
     * <p>
     * </p>
     */
    public ArcBallController(Camera camera, Scene scene, FXCanvas canvas) {
        this.camera = camera;
        this.scene = scene;
        this.canvas = canvas;

        scene.setOnScroll(new EventHandler<ScrollEvent>() {

            public void handle(ScrollEvent event) {
                double translateX = -camera.getTranslateX();
                double translateY = -camera.getTranslateY();
                double translateZ = -camera.getTranslateZ();

                double deltaY = event.getDeltaY();

                // The direction the camera is facing
                Point3D zVec = new Point3D(translateX, translateY, translateZ);

                // Normalized version that can be scaled
                Point3D normalize = zVec.normalize();

                double zoomSpeed = 50.0;

                // Final zoom scaling coefficient
                Point3D scaledMovementCof = normalize.multiply(zoomSpeed);

                double currentX = camera.getTranslateX();
                double currentY = camera.getTranslateY();
                double currentZ = camera.getTranslateZ();

                double zoomX = scaledMovementCof.getX();
                double zoomY = scaledMovementCof.getY();
                double zoomZ = scaledMovementCof.getZ();

                if (deltaY < 0) {
                    camera.setTranslateX(currentX + zoomX);
                    camera.setTranslateY(currentY + zoomY);
                    camera.setTranslateZ(currentZ + zoomZ);
                } else {
                    camera.setTranslateX(currentX - zoomX);
                    camera.setTranslateY(currentY - zoomY);
                    camera.setTranslateZ(currentZ - zoomZ);
                }
            }
        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent arg0) {

                width = scene.getWidth();
                height = scene.getHeight();

                sphereRadius = Math.min(width / 2.0d, height / 2.0d);

                double startX = arg0.getSceneX() - (scene.getWidth() / 2.0d);
                double startY = (scene.getHeight() / 2.0d) - arg0.getSceneY();

                startRot = CamUtil.pointToSphere(-startX, startY, sphereRadius).normalize();
                currentRot = startRot;

                activeRotation = true;
            }

        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent arg0) {
                activeRotation = false;
            }

        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                double dragX = arg0.getSceneX() - (scene.getWidth() / 2.0d);
                double dragY = (scene.getHeight() / 2.0d) - arg0.getSceneY();

                currentRot = CamUtil.pointToSphere(-dragX, dragY, sphereRadius).normalize();

                Point3D rotationAxis = currentRot.crossProduct(startRot).normalize();

                double dotProduct = currentRot.dotProduct(startRot);

                if (dotProduct > 1 - 1E-10) {
                    dotProduct = 1.0;
                }

                double angle = Math.acos(dotProduct) * 180.0f / Math.PI;

                Point3D invRotAxis = new Point3D(-rotationAxis.getX(), -rotationAxis.getY(), -rotationAxis.getZ());

                Point3D pivot = new Point3D(0, 0, 0);
                Rotate rotation = new Rotate(angle * 0.1d, pivot.getX(), pivot.getY(), pivot.getZ(), invRotAxis);

                if (camera.getTransforms().size() > 0) {
                    Transform totalRot = camera.getTransforms().get(0).createConcatenation(rotation);
                    camera.getTransforms().setAll(totalRot);
                } else {
                    camera.getTransforms().add(rotation);
                }

                double translateX = camera.getTranslateX();
                double translateY = camera.getTranslateY();
                double translateZ = camera.getTranslateZ();

                Affine lookAt = CamUtil.lookAt(new Point3D(0, 0, 0), new Point3D(translateX, translateY, translateZ),
                        new Point3D(0, 1, 0));

                camera.getTransforms().add(lookAt);
            }
        });

    }

}
