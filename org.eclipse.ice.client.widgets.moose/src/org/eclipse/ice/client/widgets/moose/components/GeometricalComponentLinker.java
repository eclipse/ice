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
package org.eclipse.ice.client.widgets.moose.components;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.reactor.plant.GeometricalComponent;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;

/**
 * This class provides an {@link PlantComponentLinker} that links
 * {@link GeometricalComponent} properties (position, orientation, etc.) with
 * the {@link Entry} instances in a {@link DataComponent}.
 * 
 * @author Jordan
 * 
 */
public class GeometricalComponentLinker extends PlantComponentLinker {

	/**
	 * The default constructor.
	 * 
	 * @param plantManager
	 *            The PlantBlockManager that must link Entries with
	 *            PlantComponent properties.
	 */
	public GeometricalComponentLinker(PlantBlockManager plantManager) {
		super(plantManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.moose.components.PlantComponentLinker#
	 * linkComponents(org.eclipse.ice.reactor.plant.PlantComponent,
	 * org.eclipse.ice.datastructures.form.DataComponent)
	 */
	@Override
	public List<EntryListener> linkComponents(PlantComponent plantComp,
			final DataComponent dataComp) {

		// Initialize the list of EntryListeners to return.
		final List<EntryListener> listeners = new ArrayList<EntryListener>();

		if (plantComp != null && dataComp != null) {
			BaseVisitor visitor = new BaseVisitor() {
				/**
				 * Links the position, orientation, rotation, and n_elems
				 * entries with the GeometricalComponent.
				 */
				@Override
				public void visit(final GeometricalComponent comp) {
					IEntry entry;

					// Get the "position" entry.
					entry = dataComp.retrieveEntry("position");
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							@Override
							public void updateEntry() {
								// Parse the double array value and set the
								// position of the component.
								double[] position = parseDoubleArray(3);
								if (position != null) {
									comp.setPosition(position);
								}
							}
						});
					}

					// Get the "orientation" entry.
					entry = dataComp.retrieveEntry("orientation");
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							@Override
							public void updateEntry() {
								// Parse the double array value and set the
								// orientation of the component.
								double[] orientation = parseDoubleArray(3);
								if (orientation != null) {
									comp.setOrientation(orientation);
								}
							}
						});
					}

					// Get the "rotation" entry.
					entry = dataComp.retrieveEntry("rotation");
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							@Override
							public void updateEntry() {
								// Parse the double value and set the rotation
								// of the component.
								Double rotation = parseDouble();
								if (rotation != null) {
									comp.setRotation(rotation);
								}
							}
						});
					}

					// Get the "n_elems" entry.
					entry = dataComp.retrieveEntry("n_elems");
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							@Override
							public void updateEntry() {
								// Parse the integer value and set the number of
								// elements in the component.
								Integer numElements = parseInteger();
								if (numElements != null) {
									comp.setNumElements(numElements);
								}
							}
						});
					}

					return;
				} // end of visit(GeometricalComponent)

				/**
				 * Redirects to the GeometricalComponent visit operation.
				 */
				@Override
				public void visit(HeatExchanger plantComp) {
					visit((GeometricalComponent) plantComp);
				}

				/**
				 * Redirects to the GeometricalComponent visit operation.
				 */
				@Override
				public void visit(Pipe plantComp) {
					visit((GeometricalComponent) plantComp);
				}
			};
			// Visit the plant component.
			plantComp.accept(visitor);
		}

		return listeners;
	}

}
