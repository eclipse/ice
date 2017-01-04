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

import java.util.List;

import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.january.form.DataComponent;
import org.eclipse.january.form.IEntry;

/**
 * This class provides an {@link PlantComponentLinker} that links {@link Pipe}
 * properties (length, radius, etc. and its base geometric properties) with the
 * {@link Entry} instances in a {@link DataComponent}.
 * 
 * @author Jordan
 * 
 */
public class PipeLinker extends GeometricalComponentLinker {

	/**
	 * The default constructor.
	 * 
	 * @param plantManager
	 *            The PlantBlockManager that must link Entries with
	 *            PlantComponent properties.
	 */
	public PipeLinker(PlantBlockManager plantManager) {
		super(plantManager);
	}

	/**
	 * Overrides the default GeometricalComponentLinker behavior to add
	 * Pipe-specific properties (length, radius, etc.).
	 */
	@Override
	public List<EntryListener> linkComponents(PlantComponent plantComp,
			final DataComponent dataComp) {

		// Link the default GeometricalComponent entries.
		final List<EntryListener> listeners = super.linkComponents(plantComp,
				dataComp);

		if (plantComp != null && dataComp != null) {
			BaseVisitor visitor = new BaseVisitor() {
				/**
				 * Links the position, orientation, rotation, and n_elems
				 * entries with the GeometricalComponent.
				 */
				@Override
				public void visit(final Pipe comp) {
					IEntry entry;

					// Get the "length" entry.
					entry = dataComp.retrieveEntry("length");
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							@Override
							public void updateEntry() {
								// Parse the double value and set the length
								// of the pipe.
								Double length = parseDouble();
								if (length != null) {
									comp.setLength(length);
								}
							}
						});
					}

					// Get the "radius" entry.
					entry = dataComp.retrieveEntry("radius");
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							@Override
							public void updateEntry() {
								// Parse the double value and set the radius
								// of the pipe.
								Double radius = parseDouble();
								if (radius != null) {
									comp.setRadius(radius);
								}
							}
						});
					}

					return;
				} // end of visit(Pipe)
			};
			// Visit the plant component.
			plantComp.accept(visitor);
		}

		return listeners;
	}

}
