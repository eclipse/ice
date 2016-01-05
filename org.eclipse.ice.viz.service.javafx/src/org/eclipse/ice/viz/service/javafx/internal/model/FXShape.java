package org.eclipse.ice.viz.service.javafx.internal.model;

import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.javafx.canvas.TransformGizmo;
import org.eclipse.ice.viz.service.javafx.internal.Util;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

/**
 * <p>
 * JavaFX node for managing Geometry shapes in a scene.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXShape extends Group {

    /** */
    private Shape3D shape;

    /** */
    private TransformGizmo gizmo;

    /** */
    private PhongMaterial defaultMaterial;

    /** */
    private Material selectedMaterial = Util.DEFAULT_HIGHLIGHTED_MATERIAL;

    /** */
    private boolean selected;

    /**
     * 
     * @param type
     */
    public FXShape(ShapeType type) {

        switch (type) {
        case Cone:
            break;
        case Cube:
            Box box = new Box(50, 50, 50);
            defaultMaterial = new PhongMaterial(Color.rgb(50, 50, 255));
            defaultMaterial.setSpecularColor(Color.WHITE);
            shape = box;
            break;
        case Cylinder:
            Cylinder cyl = new Cylinder(20, 250);

            defaultMaterial = new PhongMaterial(Color.rgb(0, 181, 255));
            defaultMaterial.setSpecularColor(Color.WHITE);
            cyl.setMaterial(defaultMaterial);

            shape = cyl;
            break;
        case None:
            break;
        case Sphere:
            Sphere sphere = new Sphere(50, 50);

            defaultMaterial = new PhongMaterial(Color.rgb(131, 0, 157));
            defaultMaterial.setSpecularColor(Color.WHITE);
            sphere.setMaterial(defaultMaterial);

            shape = sphere;
            break;
        case Tube:
            Cylinder tube = new Cylinder(25, 250);

            defaultMaterial = new PhongMaterial(Color.rgb(0, 131, 157));
            defaultMaterial.setSpecularColor(Color.WHITE);
            shape = tube;
            break;
        default:
            break;
        }

        gizmo = new TransformGizmo(100);
        gizmo.showHandles(false);
        gizmo.setVisible(false);

        getChildren().addAll(shape, gizmo);
    }

    /**
     * 
     * @return
     */
    public Shape3D getShape() {
        return shape;
    }

    /**
     * 
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * 
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;

        if (selected) {
            shape.setMaterial(selectedMaterial);
            gizmo.setVisible(true);
        } else {
            shape.setMaterial(defaultMaterial);
            gizmo.setVisible(false);
        }
    }

}
