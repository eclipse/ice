/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.eavp.viz.service.mesh.properties;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ISection;

/**
 * This class provides an {@link ISection} for displaying the information of an
 * {@link EdgeController} in a modifiable manner.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class EdgeInfoSection extends GeneralInfoSection {

	/**
	 * The index of the edge in its polygon's list of edges.
	 */
	private final int index;

	/**
	 * The default constructor. This should be used when the selection is
	 * expected to be an Edge.
	 */
	public EdgeInfoSection() {
		// Call the other constructor. The edge's index will just be ignored.
		this(0);
	}

	/**
	 * This constructor should be used when the selection is expected to be a
	 * Polygon.
	 * 
	 * @param edgeIndex
	 *            The index of the edge with respect to the polygon.
	 */
	public EdgeInfoSection(int edgeIndex) {
		// Set the index of the edge.
		index = (edgeIndex >= 0 ? edgeIndex : 0);

	}

	/**
	 * Overrides the setInput method so that an EdgeInfoSection can be properly
	 * constructed from a selected Polygon.
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		// Call the super method. This works if the selection is an Edge. If the
		// selection is a Polygon, we should be able to pull the appropriate
		// Edge from the Polygon.
		super.setInput(part, selection);

		// First, make sure the selection is valid. It should be some type of
		// IStructuredSelection.
		Assert.isTrue(selection instanceof IStructuredSelection);
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		// TODO Incorporate a selection of multiple mesh parts.

		// For now, we are only dealing with the first available element in the
		// selection.
		if (!structuredSelection.isEmpty()) {
			Object element = structuredSelection.getFirstElement();
			Assert.isTrue(element instanceof MeshSelection);
			MeshSelection meshSelection = (MeshSelection) element;

			// Get the selected IMeshPart.
			AbstractController meshPart = meshSelection.selectedMeshPart;

			// If the selected object is a Polygon, then we need to get an edge
			// from the Polygon.
			if (meshPart instanceof NekPolygonController) {
				NekPolygonController polygon = (NekPolygonController) meshPart;
				List<AbstractController> edges = polygon
						.getEntitiesByCategory("Edges");
				// Set the parent class' ICEObject to the appropriate edge.
				if (index < edges.size()) {
					object = edges.get(index);
				}
			}
		}

		return;
	}

}
