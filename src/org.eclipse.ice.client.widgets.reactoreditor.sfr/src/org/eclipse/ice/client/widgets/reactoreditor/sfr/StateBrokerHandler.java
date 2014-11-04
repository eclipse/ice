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
package org.eclipse.ice.client.widgets.reactoreditor.sfr;

import java.util.ArrayList;

import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IStateBrokerHandler;
import org.eclipse.ice.client.widgets.reactoreditor.StateBroker;

/**
 * This class provides keys for use in a {@link StateBroker}. It is tailored
 * specifically for {@link SFRComponent}s and the SFR analysis views.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class StateBrokerHandler implements IStateBrokerHandler,
		ISFRComponentVisitor {

	/**
	 * The StateBroker that holds selections of SFRComponents.
	 */
	private StateBroker broker;
	/**
	 * The IDataProvider for a selection SFRComponent. May be null.
	 */
	private IDataProvider data;
	/**
	 * The location of the SFRComponent in its parent SFRComponent. If -1, then
	 * the component does not occupy a location in its parent.
	 */
	private int row, column;
	/**
	 * The parent object to the SFRComponent that is visited by this class.
	 */
	private Object parent;
	/**
	 * Whether or not the SFRComponent was added to the broker.
	 */
	private boolean added;
	/**
	 * The source of the data (Input or Reference). This determines the final
	 * key used to add an object to the broker.
	 */
	private DataSource dataSource = DataSource.Input;

	/**
	 * This class provides a default implementation of an
	 * {@link ISFRComponentVisitor}. It can be used to retrieve some value from
	 * a specified type of {@link SFRComponent} by using its
	 * {@link BaseVisitor#getValue(Object)} method.<br>
	 * <br>
	 * <b>It is primarily intended to be used for inner/untyped classes and
	 * eliminate unused visit methods elsewhere in this class.</b>
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class BaseVisitor implements ISFRComponentVisitor {

		/**
		 * The value that can be set by subclasses.
		 */
		protected Object value;

		/**
		 * Gets some value depending on the type of object.
		 * 
		 * @param object
		 *            The object whose type should determine some sort of value.
		 * @return Some value, usually determined in the method that
		 *         instantiates a BaseVisitor. Null if the object's type is not
		 *         supported.
		 */
		public final Object getValue(Object object) {
			value = null;
			if (object != null && object instanceof SFRComponent) {
				((SFRComponent) object).accept(this);
			}
			return value;
		}

		public void visit(SFReactor sfrComp) {
		}

		public void visit(SFRAssembly sfrComp) {
		}

		public void visit(PinAssembly sfrComp) {
		}

		public void visit(ReflectorAssembly sfrComp) {
		}

		public void visit(SFRPin sfrComp) {
		}

		public void visit(SFRRod sfrComp) {
		}

		public void visit(MaterialBlock sfrComp) {
		}

		public void visit(Material sfrComp) {
		}

		public void visit(Ring sfrComp) {
		}
	}

	/**
	 * Sets the {@link StateBroker} used by this handler to store
	 * {@link SFRComponent}s.
	 * 
	 * @param broker
	 *            The new StateBroker.
	 */
	public void setStateBroker(StateBroker broker) {
		this.broker = broker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.reactoreditor.IStateBrokerHandler
	 * #setDataSource (org.eclipse.ice.client.widgets.reactoreditor.DataSource)
	 */
	public void setDataSource(DataSource dataSource) {
		if (dataSource != null) {
			this.dataSource = dataSource;
		}
	}

	/**
	 * This method returns keys only for SFRComponents, specifically reactors,
	 * assemblies, and pins/rods.
	 */
	public String getKey(Object object) {
		String key = null;

		// TODO Figure out a better way to handle keys shared between reactor
		// types.

		// Create a visitor to generate a key for supported SFRComponents.
		BaseVisitor keyVisitor = new BaseVisitor() {
			public void visit(SFReactor sfrComp) {
				value = "SFReactor";
			}

			public void visit(SFRAssembly sfrComp) {
				value = "sfr" + sfrComp.getAssemblyType().toString();
			}

			public void visit(PinAssembly sfrComp) {
				visit((SFRAssembly) sfrComp);
			}

			public void visit(ReflectorAssembly sfrComp) {
				visit((SFRAssembly) sfrComp);
			}

			public void visit(SFRPin sfrComp) {
				value = "pin";
			}

			public void visit(SFRRod sfrComp) {
				value = "pin";
			}
		};
		key = (String) keyVisitor.getValue(object);

		// FIXME We need to quit using input/reference in the state broker.
		return (key != null ? dataSource + "-" + key : null);
	}

	/**
	 * This method only adds SFRComponents, specifically reactors, assemblies,
	 * and rods/pins (or any component implemented in this class' visit
	 * operations).
	 */
	public boolean addValue(Object value, Object parent, StateBroker broker) {

		added = false;
		String key = getKey(value);

		if (key != null && broker != null) {
			// Set the StateBroker used by the visit operations.
			setStateBroker(broker);

			// Store the reference to the parent object.
			this.parent = parent;

			// Clear the other properties for this component.
			row = column = -1;
			data = null;

			// Visit the component to update the value that will be put into the
			// broker.
			if (value instanceof SFRComponent) {
				((SFRComponent) value).accept(this);
			}
		}

		return added;
	}

	// ---- Basic supported add operations. ---- //
	private void addReactor(SFReactor reactor) {
		String key = getKey(reactor);

		if (key != null) {
			broker.putValue(key, reactor);
			added = true;
		}

		return;
	}

	private void addAssembly(SFRAssembly assembly, SFReactor reactor) {
		if (assembly != null) {
			// Set the variables used in the loops below.
			int row = 0, column = 0;
			boolean found = false;
			int size = (reactor != null ? reactor.getSize() : 0);

			// Get the type of assembly.
			AssemblyType type = assembly.getAssemblyType();
			if (type != null) {
				// Loop over each location in the assembly and get the data
				// provider if the rod matches the assembly component at that
				// location.
				for (row = 0; !found && row < size; row++) {
					for (column = 0; column < size; column++) {
						if (assembly == reactor.getAssemblyByLocation(type,
								row, column)) {
							// Break out of the loops. Make sure row is correct.
							found = true;
							row--;
							break;
						}
					}
				}
			}

			// Add the assembly. Adjust row and column if necessary.
			if (!found) {
				row = column = -1;
			}
			addAssembly(assembly, row, column);
		}

		return;
	}

	private void addAssembly(SFRAssembly assembly, int row, int column) {
		String key = getKey(assembly);

		if (key != null) {
			SFRComponentInfo info = new SFRComponentInfo(row, column, assembly);
			broker.putValue(key, info);
			added = true;
		}

		return;
	}

	private void addPin(SFRPin pin, PinAssembly assembly) {
		if (pin != null) {
			// Set the variables used in the loops below.
			IDataProvider data = null;
			int row = -1, column = -1;
			int size = (assembly != null ? assembly.getSize() : 0);

			// Get the first location in the assembly that is occupied by this
			// pin and grab the data there.
			String name = pin.getName();
			ArrayList<Integer> locations = assembly.getPinLocations(name);
			if (!locations.isEmpty()) {
				int index = locations.get(0);
				row = index / size;
				column = index % size;
				// Get the data from that location.
				data = assembly.getDataProviderByLocation(row, column);
			}

			// Add the pin.
			addPin(pin, data, row, column);
		}

		return;
	}

	private void addPin(SFRPin pin, IDataProvider data, int row, int column) {
		String key = getKey(pin);

		if (key != null) {
			SFRComponentInfo info = new SFRComponentInfo(row, column, pin, data);
			broker.putValue(key, info);
			added = true;
		}

		return;
	}

	// ----------------------------------------- //

	// ---- Implements ISFRComponentVisitor ---- //
	// These visit operations need to call one of the basic operations that adds
	// supported Objects to the StateBroker.
	public void visit(SFReactor sfrComp) {
		// TODO Update this when plant components can contain SFReactors.
		addReactor(sfrComp);
	}

	public void visit(SFRAssembly sfrComp) {
		// If the row and column are set, we can just add a new
		// SFRComponentInfo.
		if (row >= 0 && column >= 0) {
			addAssembly(sfrComp, row, column);
		}
		// Otherwise, we need to find the location of the assembly in the parent
		// reactor.
		else {
			// If the parent reactor does not exist, we can still put the value
			// into the broker.
			SFReactor reactor = null;

			// If the parent object exists and is an SFRComponent, try to
			// convert it to a valid parent of a PinAssembly (currently, this
			// means it must be an SFReactor).
			BaseVisitor visitor = new BaseVisitor() {
				@Override
				public void visit(SFReactor sfrComp) {
					value = sfrComp;
				}
			};
			// Get the reactor from the visitor.
			reactor = (SFReactor) visitor.getValue(parent);

			// Try to add the assembly. This method finds the location of the
			// assembly within the reactor (if it is in the reactor).
			addAssembly(sfrComp, reactor);
		}

		return;
	}

	public void visit(PinAssembly sfrComp) {
		visit((SFRAssembly) sfrComp);
	}

	public void visit(ReflectorAssembly sfrComp) {
		visit((SFRAssembly) sfrComp);
	}

	public void visit(SFRPin sfrComp) {
		// If the row and column are set, we can just add a new
		// SFRComponentInfo.
		if (row >= 0 && column >= 0) {
			addPin(sfrComp, data, row, column);
		}
		// Otherwise, we need to find the location of the pin in the parent
		// assembly.
		else {
			// If the parent assembly does not exist, we can still put the value
			// into the broker.
			PinAssembly assembly = null;

			// If the parent object exists and is an SFRComponent, try to
			// convert it to a valid parent of a pin (currently, this means it
			// must be a PinAssembly).
			BaseVisitor visitor = new BaseVisitor() {
				@Override
				public void visit(PinAssembly sfrComp) {
					value = sfrComp;
				}
			};
			// Get the assembly from the visitor.
			assembly = (PinAssembly) visitor.getValue(parent);

			// Try to add the pin. This method finds the location of the pin in
			// its parent assembly.
			addPin(sfrComp, assembly);
		}

		return;
	}

	public void visit(SFRRod sfrComp) {
		// TODO Currently, rods can only be found in ReflectorAssemblies. We do
		// not actually display ReflectorAssemblies.
	}

	public void visit(MaterialBlock sfrComp) {
		// Nothing to do.
	}

	public void visit(Material sfrComp) {
		// Nothing to do.
	}

	public void visit(Ring sfrComp) {
		// Nothing to do.
	}
	// ----------------------------------------- //

}
