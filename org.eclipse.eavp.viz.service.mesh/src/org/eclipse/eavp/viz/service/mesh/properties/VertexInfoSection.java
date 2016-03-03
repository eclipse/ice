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
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ISection;

/**
 * This class provides an {@link ISection} for displaying the information of a
 * {@link VertexController} in a modifiable manner.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class VertexInfoSection extends GeneralInfoSection {

	/**
	 * The index of the vertex in its edge's array of vertex IDs or in its
	 * polygon's list of vertices.
	 */
	private final int index;

	/**
	 * The default constructor. This should be used when the selection is
	 * expected to be a Vertex. You can also use a basic
	 * {@link GeneralInfoSection} instead.
	 */
	public VertexInfoSection() {
		this(0);
	}

	/**
	 * This constructor should be used when the selection is expected to be an
	 * Edge or a Polygon.
	 * 
	 * @param vertexIndex
	 *            The index of the vertex with respect to the edge or polygon.
	 */
	public VertexInfoSection(int vertexIndex) {
		// Set the index of the vertex.
		index = (vertexIndex >= 0 ? vertexIndex : 0);
	}

	/**
	 * Overrides the setInput method so that a VertexInfoSection can be properly
	 * constructed from a selected Edge or Polygon.
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

			// Get the selected IMeshPart and mesh from the selection.
			IController meshPart = meshSelection.selectedMeshPart;
			final IController mesh = meshSelection.mesh;

			// Get the vertex at the given index
			List<IController> vertices = meshPart
					.getEntitiesFromCategory(MeshCategory.VERTICES);
			if (index < vertices.size()) {
				object = vertices.get(index);
			}

		}

		return;
	}
}
