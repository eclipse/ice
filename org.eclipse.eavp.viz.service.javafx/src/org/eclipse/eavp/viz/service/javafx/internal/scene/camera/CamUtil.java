package org.eclipse.eavp.viz.service.javafx.internal.scene.camera;

import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;

/**
 * <p>
 * Utility for working with Cameras in the FXGeometryViewer.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class CamUtil {

    /**
     * <p>
     * Maps canvas coordinate to a spherical location.
     * </p>
     * 
     * @param x
     *            canvas x coordinate
     * @param y
     *            canvas y coordinate
     * 
     * @return x/y location mapped onto sphere
     */
    public static Point3D pointToSphere(double x, double y, double sphereRadius) {
        double screenDist = x * x + y * y;
        double sphereSquared = sphereRadius * sphereRadius;

        if (screenDist > sphereSquared) {
            return new Point3D(x, y, 0);
        } else {
            double z = Math.sqrt(sphereSquared - screenDist);
            return new Point3D(x, y, z);
        }
    }

    /**
     * <p>
     * Computes a matrix to orient a node towards a given point, defined by
     * target.
     * </p>
     * 
     * @param target
     *            the target to look at
     * @param pos
     *            the viewer's current location
     * @param yUp
     *            the direction of "up"
     * 
     * @return affine transform that orients towards the target
     */
    public static Affine lookAt(Point3D target, Point3D pos, Point3D yUp) {
        Point3D z = target.subtract(pos).normalize();
        Point3D x = yUp.normalize().crossProduct(z).normalize();
        Point3D y = z.crossProduct(x).normalize();

        return new Affine(x.getX(), y.getX(), z.getX(), pos.getX(), x.getY(), y.getY(), z.getY(), pos.getY(), x.getZ(),
                y.getZ(), z.getZ(), pos.getZ());
    }

}
