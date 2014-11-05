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
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.TimeDependentJunction;

/**
 * This class provides an {@link PlantComponentLinker} that links
 * {@link Junction} properties (inputs and outputs) with the {@link Entry}
 * instances in a {@link DataComponent}.
 * 
 * @author Jordan
 * 
 */
public class JunctionLinker extends PlantComponentLinker {

	/**
	 * This is a visitor that is used to determine PlantComponents (pipes or
	 * heat exchangers) from the input/output Entries for a Junction.
	 */
	private final PipeSelector pipeSelector;

	/**
	 * The default constructor.
	 * 
	 * @param plantManager
	 *            The PlantBlockManager that must link Entries with
	 *            PlantComponent properties.
	 */
	public JunctionLinker(PlantBlockManager plantBlockManager) {
		super(plantBlockManager);

		// Initialize the visitor that determines pipe/heat exchangers from
		// entry values.
		pipeSelector = new PipeSelector();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.moose.components.AbstractPlantComponentLinker
	 * #linkComponents(org.eclipse.ice.reactor.plant.PlantComponent,
	 * org.eclipse.ice.datastructures.form.DataComponent)
	 */
	public List<EntryListener> linkComponents(PlantComponent plantComp,
			final DataComponent dataComp) {

		// Initialize the list of EntryListeners to return.
		final List<EntryListener> listeners = new ArrayList<EntryListener>();

		if (plantComp != null && dataComp != null) {
			BaseVisitor visitor = new BaseVisitor() {
				/**
				 * Link the "input" or "inputs" Entry with the junction's inputs
				 * and the "output" or "outputs" Entry with the junction's
				 * outputs.
				 */
				@Override
				public void visit(final Junction junction) {
					Entry entry;

					// Get the "input" or "inputs" Entry.
					entry = dataComp.retrieveEntry("inputs");
					if (entry == null) {
						entry = dataComp.retrieveEntry("input");
					}
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							public void updateEntry() {
								// Get the pipes from the entry and set them as
								// input to the junction.
								ArrayList<PlantComponent> pipes = parsePipes(entry
										.getValue());
								junction.setInputs(pipes);
							}
						});
					}

					// Get the "output" or "outputs" Entry.
					entry = dataComp.retrieveEntry("outputs");
					if (entry == null) {
						entry = dataComp.retrieveEntry("output");
					}
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							public void updateEntry() {
								// Get the pipes from the entry and set them as
								// output to the junction.
								ArrayList<PlantComponent> pipes = parsePipes(entry
										.getValue());
								junction.setOutputs(pipes);
							}
						});
					}

					return;
				} // end of visit(Junction)

				/**
				 * TimeDependentJunctions consider their "input" to go to the
				 * output of a pipe in practice.
				 */
				@Override
				public void visit(final TimeDependentJunction junction) {
					// Get the "input" entry.
					Entry entry = dataComp.retrieveEntry("input");
					// If such an entry exists, create a new EntryListener that
					// links the entry's value with the plant component.
					if (entry != null) {
						listeners.add(new EntryListener(entry) {
							public void updateEntry() {
								// Get the pipes from the entry and set them as
								// input to the junction.
								ArrayList<PlantComponent> pipes = parsePipes(entry
										.getValue());
								junction.setOutputs(pipes);
							}
						});
					}

					return;
				} // end of visit(TimeDependentJunction)
			};
			// Visit the plant component (usually a Junction, but if not,
			// nothing happens).
			plantComp.accept(visitor);
		}

		return listeners;
	}

	/**
	 * Parses a string (typically from an {@link Entry}) and returns a list of
	 * PlantComponents matching the names listed in the string. It also takes
	 * into account any primary/secondary and in/out flags set in the string.
	 * See {@link PipeSelector} for more details on the supported format.
	 * 
	 * @param value
	 *            The value to parse.
	 * @return An ArrayList containing PlantComponents if some could be found
	 *         with names matching values in the string. If none could be found,
	 *         the list will be empty.
	 */
	private ArrayList<PlantComponent> parsePipes(String value) {
		ArrayList<PlantComponent> pipes = new ArrayList<PlantComponent>();

		if (value != null) {
			// Remove single quotes, trim leading and trailing whitespace, and
			// split the string based on one or more spaces.
			String[] split = value.replace("'", "").trim().split("\\s+");

			// Each name must match the following format (ignore spaces):
			// name( (primary|secondary) _ (in|out) )
			Pattern p;
			p = Pattern.compile("(.*)\\((((primary|secondary)_)?(in|out))\\)");

			// Loop over the split string and get the component names.
			for (int i = 0; i < split.length; i++) {
				// Compare the current string with the pattern.
				Matcher m = p.matcher(split[i]);
				if (m.matches()) {
					// Get the name, primary|secondary, and in|out strings.
					String name = m.group(1);
					String primary = m.group(4);
					String input = m.group(5);

					// Add any component derived from the name to the list.
					PlantComponent pipe = pipeSelector.getPipe(name, primary,
							input);
					if (pipe != null) {
						pipes.add(pipe);
					}
				}
			}
		}

		return pipes;
	}

	/**
	 * This class does looks for a PlantComponent (specifically, Pipes or
	 * HeatExchangers) matching a string name. Given additional strings, it can
	 * determine whether to return a Pipe or one of the HeatExchanger's primary
	 * or secondary pipes. The idea is that, in a Junction's inputs/outputs
	 * Entry, the following will work:
	 * 
	 * <ul>
	 * <li><b>input:</b> <i>Pipe(out)</i> <br>
	 * Connects the junction to the pipe's outlet.</li>
	 * <li><b>input:</b> <i>HeatExchanger(primary_out)</i><br>
	 * Connects the junction to the heat exchanger's primary outlet.</li>
	 * <li><b>input:</b> <i>Pipe(in)</i> <br>
	 * Connects the junction to the pipe's primary inlet.</li>
	 * <li><b>input:</b> <i>HeatExchanger(secondary_in)</i><br>
	 * Connects the junction to the heat exchanger's secondary inlet.</li>
	 * </ul>
	 * 
	 * @author Jordan
	 * 
	 */
	private class PipeSelector extends BaseVisitor {

		/**
		 * Whether or not the pipe is a primary pipe (heat exchangers have
		 * secondary pipes).
		 */
		private boolean primary;

		/**
		 * The pipe that should be returned in
		 * {@link #getPipe(PlantBlockManager, String, String, String) getPipe()}
		 * .
		 */
		private PlantComponent pipe;

		/**
		 * A lock used to synchronize access to the class variables.
		 */
		private final ReentrantLock lock = new ReentrantLock();

		/**
		 * Gets a PlantComponent corresponding to a name and two additional
		 * parameter strings.
		 * 
		 * @param name
		 *            The name of the PlantComponent to find.
		 * @param primary
		 *            A string (typically "primary" or "secondary") representing
		 *            the type of pipe.
		 * @param input
		 *            A string (typically "in" or "out") representing the end of
		 *            the pipe.
		 * @return A PlantComponent corresponding to the method's parameters or
		 *         null if one could not be found.
		 */
		public PlantComponent getPipe(String name, String primary, String input) {
			// TODO The in/out string is ignored for now because our model
			// assumes pipes have a direction (one end is for inputs, the other
			// for outputs).

			// Determine the related pipe/heat exchanger from the name.
			PlantComponent component = plantManager.getPlantComponent(name);

			if (component != null) {

				// Set primary to true only if the string is null or "primary".
				boolean isPrimary = false;
				if (primary == null || "primary".equals(primary)) {
					isPrimary = true;
				}
				// Update the primary boolean and perform the visit operation.
				// Make sure the component is set to the pipe determined by the
				// visit operation.
				// We use a lock here to make this operation thread-safe. Note
				// that it surrounds access to the class variables.
				lock.lock();
				try {
					this.primary = isPrimary;
					component.accept(this);
					component = pipe;
				} finally {
					lock.unlock();
				}

			}

			return component;
		}

		/**
		 * Sets {@link #pipe} to the pipe PlantComponent regardless of the
		 * primary flag.
		 */
		@Override
		public void visit(Pipe plantComp) {
			pipe = plantComp;
		}

		/**
		 * Sets {@link #pipe} to the heat exchanger's primary pipe if primary is
		 * true and to the heat exchanger if primary is false..
		 */
		@Override
		public void visit(HeatExchanger plantComp) {
			pipe = (primary ? plantComp.getPrimaryPipe() : plantComp);
		}
	}
}
