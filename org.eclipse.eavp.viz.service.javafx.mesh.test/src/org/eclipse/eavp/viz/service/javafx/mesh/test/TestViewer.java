package org.eclipse.eavp.viz.service.javafx.mesh.test;

import org.eclipse.eavp.viz.service.javafx.mesh.AxisGridGizmo;
import org.eclipse.eavp.viz.service.javafx.mesh.FXMeshViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * An extension of FXMeshViewer that allows the viewer's axis gizmo to be
 * retrieved.
 * 
 * @author Robert Smith
 *
 */
public class TestViewer extends FXMeshViewer {

	/**
	 * The default constructor
	 * 
	 * @param parent
	 */
	public TestViewer(Composite parent) {
		super(parent);
		gizmo = new TestGizmo(SCALE);
	}

	/**
	 * Get the viewer's axis gizmo
	 */
	public AxisGridGizmo getGizmo() {
		return gizmo;
	}
}