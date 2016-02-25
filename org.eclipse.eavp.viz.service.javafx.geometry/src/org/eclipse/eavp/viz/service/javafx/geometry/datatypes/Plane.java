package org.eclipse.eavp.viz.service.javafx.geometry.datatypes;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

/**
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class Plane extends MeshView {

    /**
     * 
     * @param width
     * @param height
     * @param depth
     */
    public Plane(double width, double height, double depth, int segments) {
        TriangleMesh mesh = new TriangleMesh();
        mesh.setVertexFormat(VertexFormat.POINT_TEXCOORD);

        generatePlane(mesh, width, height, depth, segments);

    }

    /**
     * 
     * @param mesh
     * @param width
     * @param height
     * @param depth
     * @param segments
     * @return
     */
    private float[] generatePlane(TriangleMesh mesh, double width, double height, double depth, int segments) {
        return null;
    }

}
