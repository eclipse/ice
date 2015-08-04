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
package org.eclipse.ice.client.widgets.mesh.properties;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.BezierEdge;
import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.Hex;
import org.eclipse.ice.datastructures.form.mesh.IMeshPart;
import org.eclipse.ice.datastructures.form.mesh.IMeshPartVisitor;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.PolynomialEdge;
import org.eclipse.ice.datastructures.form.mesh.Quad;
import org.eclipse.ice.datastructures.form.mesh.Vertex;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ISection;

/**
 * This class provides an {@link ISection} for displaying the information of a
 * {@link Vertex} in a modifiable manner.
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
			IMeshPart meshPart = meshSelection.selectedMeshPart;
			final MeshComponent mesh = meshSelection.mesh;

			// Create a visitor that can determine the appropriate Vertex
			// instance whose properties are being exposed based on the type of
			// IMeshPart passed in through the selection.
			IMeshPartVisitor visitor = new IMeshPartVisitor() {
				@Override
				public void visit(MeshComponent mesh) {
					// Do nothing.
				}

				@Override
				public void visit(Polygon polygon) {
					// Get the vertex from the polygon.
					ArrayList<Vertex> vertices = polygon.getVertices();
					if (index < vertices.size()) {
						object = vertices.get(index);
					}
				}

				@Override
				public void visit(Quad quad) {
					// Re-direct to the standard polygon operation for now.
					visit((Polygon) quad);
				}

				@Override
				public void visit(Hex hex) {
					// Re-direct to the standard polygon operation for now.
					visit((Polygon) hex);
				}

				@Override
				public void visit(Edge edge) {
					// Get the vertex ID from the edge, then use the ID to get
					// the vertex from the mesh.
					int[] vertices = edge.getVertexIds();
					if (index < vertices.length && mesh != null) {
						object = mesh.getVertex(vertices[index]);
					}
				}

				@Override
				public void visit(BezierEdge edge) {
					// Re-direct to the standard edge operation for now.
					visit((Edge) edge);
				}

				@Override
				public void visit(PolynomialEdge edge) {
					// Re-direct to the standard edge operation for now.
					visit((Edge) edge);
				}

				@Override
				public void visit(Vertex vertex) {
					object = vertex;
				}

				@Override
				public void visit(Object object) {
					// Do nothing.
				}
			};

			// Reset the vertex based on the visited IMeshPart.
			meshPart.acceptMeshVisitor(visitor);
		}

		return;
	}
}
