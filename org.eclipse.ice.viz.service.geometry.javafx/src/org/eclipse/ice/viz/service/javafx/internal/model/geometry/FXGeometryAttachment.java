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
package org.eclipse.ice.viz.service.javafx.internal.model.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.geometry.scene.base.GeometryAttachment;
import org.eclipse.ice.viz.service.geometry.scene.base.IGeometry;
import org.eclipse.ice.viz.service.geometry.scene.model.IAttachment;
import org.eclipse.ice.viz.service.geometry.scene.model.INode;
import org.eclipse.ice.viz.service.geometry.shapes.ComplexShape;
import org.eclipse.ice.viz.service.geometry.shapes.Geometry;
import org.eclipse.ice.viz.service.geometry.shapes.IShape;
import org.eclipse.ice.viz.service.geometry.shapes.IShapeVisitor;
import org.eclipse.ice.viz.service.geometry.shapes.OperatorType;
import org.eclipse.ice.viz.service.geometry.shapes.PrimitiveShape;
import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.javafx.internal.Util;
import org.eclipse.ice.viz.service.javafx.internal.model.FXShape;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Transform;

/**
 * <p>
 * JavaFX implementation of GeometryAttachment.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXGeometryAttachment extends GeometryAttachment {

    /**
     * Node used to attach geometry to (instead of the root, to keep things
     * easier to manipulate).
     */
    private Group fxAttachmentNode;

    /** The manager that owns this attachment. */
    private final FXGeometryAttachmentManager manager;

    /** Maps ICE Geometry shapes to JavaFX shapes. */
    private Map<IShape, Node> fxShapeMap;

    /** */
    private List<Geometry> knownGeometry;

    /**
     * <p>
     * Creates an FXGeometryAttachment instance.
     * </p>
     * 
     * @param manager
     *            the manager that created this instance.
     */
    public FXGeometryAttachment(FXGeometryAttachmentManager manager) {
        this.manager = manager;
    }

    /**
     * @see GeometryAttachment#attach(INode)
     */
    @Override
    public void attach(INode owner) {
        super.attach(owner);

        if (fxAttachmentNode == null) {
            fxAttachmentNode = new Group();
        }

        Group fxNode = Util.getFxGroup(owner);
        fxNode.getChildren().add(fxAttachmentNode);
    }

    /**
     * @see IAttachment#detach(INode)
     */
    @Override
    public void detach(INode owner) {
        Group fxNode = Util.getFxGroup(owner);

        if (fxAttachmentNode != null) {
            fxNode.getChildren().remove(fxAttachmentNode);
        }

        super.detach(owner);
    }

    /**
     * @see IAttachment#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return false;
    }

    /**
     * @see IGeometry#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        fxAttachmentNode.setVisible(visible);
    }

    @Override
    public void addGeometry(Geometry geom) {
        super.addGeometry(geom);

        if (fxAttachmentNode == null) {
            fxAttachmentNode = new Group();
        }

        if (knownGeometry == null) {
            knownGeometry = new ArrayList<>();
        }

        if (!knownGeometry.contains(geom)) {
            geom.register(new IVizUpdateableListener() {

                public void update(IVizUpdateable component) {
                    SyncShapes sync = new SyncShapes(fxAttachmentNode);

                    for (IShape shape : geom.getShapes()) {
                        shape.acceptShapeVisitor(sync);
                    }

                    javafx.application.Platform.runLater(new Runnable() {
                        public void run() {
                            sync.commit();
                        }
                    });
                }
            });

            knownGeometry.add(geom);
        }
    }

    /**
     * <p>
     * Generates JavaFX shapes from the input IShape.
     * </p>
     * 
     * @param shape
     *            an ICE Geometry IShape
     */
    @Override
    public void processShape(IShape shape) {

        if (fxShapeMap == null) {
            fxShapeMap = new HashMap<>();
        }

        shape.acceptShapeVisitor(new IShapeVisitor() {
            public void visit(PrimitiveShape primitiveShape) {
                processShape(primitiveShape);
            }

            public void visit(ComplexShape complexShape) {
                processShape(complexShape);
            }
        });
    }

    /**
     * <p>
     * Generates JavaFX shapes from the input primitive type.
     * </p>
     * 
     * @param ICE
     *            primitive shape instance
     */
    private void processShape(PrimitiveShape shape) {
        ShapeType type = shape.getType();

        Transform xform = Util.convertTransformation(shape.getTransformation());

        FXShape fxShape = new FXShape(type);

        fxShape.getProperties().put(IShape.class, shape);
        fxShape.getProperties().put(Geometry.class, currentGeom);

        fxShape.getTransforms().setAll(xform);

        fxAttachmentNode.getChildren().add(fxShape);

        fxShapeMap.put(shape, fxShape);
    }

    /**
     * <p>
     * Generates geometry from a complex CSG shape.
     * </p>
     * 
     * @param shape
     */
    private void processShape(ComplexShape shape) {
        OperatorType type = shape.getType();

        switch (type) {
        case Complement:
            break;
        case Intersection:
            break;
        case None:
            break;
        case Union:
            break;
        default:
            break;
        }

        for (IShape subShape : shape.getShapes()) {
            subShape.acceptShapeVisitor(new IShapeVisitor() {

                public void visit(PrimitiveShape primitiveShape) {
                    processShape(primitiveShape);
                }

                public void visit(ComplexShape complexShape) {
                    processShape(complexShape);
                }
            });
        }
    }

    /**
     * 
     */
    @Override
    protected void disposeShape(IShape shape) {
        Node node = fxShapeMap.get(shape);

        if (node == null) {
            return;
        }

        fxAttachmentNode.getChildren().remove(node);
    }

    /**
     *
     * @param copy
     * @return
     */
    @Override
    public List<IShape> getShapes(boolean copy) {
        return super.getShapes(copy);

    }

    /**
     *
     */
    @Override
    public void clearShapes() {
        super.clearShapes();
    }

    /**
     * 
     * @return
     */
    public FXGeometryAttachmentManager getManager() {
        return manager;
    }

    /**
     *
     * @return
     */
    public javafx.scene.Node getFxParent() {
        return fxAttachmentNode;
    }

    /**
     *
     * @return
     */
    public Group getFxNode() {
        return fxAttachmentNode;
    }

    /**
     * 
     */
    public Class<?> getType() {
        return GeometryAttachment.class;
    }

    /**
     * 
     */
    public String getName() {
        if (fxAttachmentNode == null) {
            return "UNNAMED";
        }

        return fxAttachmentNode.getId();
    }

}
