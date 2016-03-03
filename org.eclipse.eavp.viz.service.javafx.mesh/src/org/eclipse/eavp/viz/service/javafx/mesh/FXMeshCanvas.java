/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.mesh;

import org.eclipse.eavp.viz.service.javafx.canvas.BasicAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.BasicViewer;
import org.eclipse.eavp.viz.service.javafx.canvas.FXVizCanvas;
import org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas;
import org.eclipse.eavp.viz.service.mesh.properties.MeshSelection;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.swt.widgets.Composite;

/**
 * <p>
 * GeometryCanvas provides the ability to visualize and manipulate 3D geometry
 * data.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 * 
 */
public class FXMeshCanvas extends FXVizCanvas implements IMeshVizCanvas {

	/**
	 * <p>
	 * Creates a canvas for the supplied geometry.
	 * </p>
	 * 
	 * @param geometry
	 *            ICE Geometry instance to visualizer in the canvas.
	 */
	public FXMeshCanvas(IController mesh) {
		super(mesh);

	}

	/**
	 * <p>
	 * Fix for Eclipse/PDE's wonky fragment support. Creates a GeometryViewer
	 * supplied by an implementation fragment.
	 * </p>
	 * 
	 * @param viewerParent
	 *            the parent widget
	 * 
	 * @return a concrete implementation of GeometryViewer
	 * 
	 * @throws Exception
	 *             throws an exception if a concrete implementation cannot be
	 *             found
	 */
	@Override
	protected BasicViewer materializeViewer(Composite viewerParent)
			throws Exception {
		try {
			return new FXMeshViewer(viewerParent);

		} catch (Exception e) {
			throw new Exception("", e); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.FXVizCanvas#
	 * createAttachment( )
	 */
	@Override
	protected void createAttachment() {
		rootAtachment = (BasicAttachment) viewer.getRenderer()
				.createAttachment(FXMeshAttachment.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setEditMode(boolean)
	 */
	@Override
	public void setEditMode(boolean edit) {
		((FXMeshViewer) viewer).setEditSelectionHandeling(edit);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setVisibleHUD(boolean)
	 */
	@Override
	public void setVisibleHUD(boolean on) {
		((FXMeshViewer) viewer).setHUDVisible(on);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setVisibleAxis(boolean)
	 */
	@Override
	public void setVisibleAxis(boolean on) {
		((FXMeshViewer) viewer).setAxisVisible(on);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * HUDIsVisible()
	 */
	@Override
	public boolean HUDIsVisible() {
		return ((FXMeshViewer) viewer).isHUDVisible();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * AxisAreVisible()
	 */
	@Override
	public boolean AxisAreVisible() {
		return ((FXMeshViewer) viewer).getAxisVisible();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * deleteSelection()
	 */
	@Override
	public void deleteSelection() {

		// Check each polygon in the mesh to see if it should be deleted
		for (IController polygon : root.getEntities()) {

			// Whether or not the polygon is completely selected
			boolean selected = true;

			// Check each of the polygon's vertices
			for (IController vertex : polygon
					.getEntitiesFromCategory(MeshCategory.VERTICES)) {

				// If any vertex is not selected, stop and move on to the next
				// polygon
				if (!"True".equals(vertex.getProperty(MeshProperty.SELECTED))) {
					selected = false;
					break;
				}
			}

			// If all the vertices were selected, remove the polygon from the
			// mesh
			if (selected) {
				root.removeEntity(polygon);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setSelection(java.lang.Object[])
	 */
	@Override
	public void setSelection(Object[] selection) {

		// Set the viewer's internal data structures
		((FXMeshViewer) viewer).setInternalSelection(selection);

		// Start by deselecting everything
		for (IController polygon : root.getEntities()) {
			polygon.setProperty(MeshProperty.SELECTED, "False");
		}

		for (IController polygon : root.getEntities()) {

			// If a polygon is in the selection, set it as selected. (This will
			// also select all its children).
			for (Object meshSelection : selection) {
				if (((MeshSelection) meshSelection).selectedMeshPart == polygon) {
					polygon.setProperty(MeshProperty.SELECTED, "True");
				}

				// If the polygon wasn't selected, check each of its children to
				// see if they were.
				else {
					for (IController part : polygon.getEntities()) {
						if (((MeshSelection) meshSelection).selectedMeshPart == part) {
							part.setProperty(MeshProperty.SELECTED, "True");
						}
					}
				}
			}
		}

	}

}
