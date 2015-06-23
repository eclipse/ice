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
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IStateBrokerHandler;
import org.eclipse.ice.client.widgets.reactoreditor.StateBroker;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.plant.Boundary;
import org.eclipse.ice.reactor.plant.Branch;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.DownComer;
import org.eclipse.ice.reactor.plant.FlowJunction;
import org.eclipse.ice.reactor.plant.GeometricalComponent;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.IPlantComponentVisitor;
import org.eclipse.ice.reactor.plant.IdealPump;
import org.eclipse.ice.reactor.plant.Inlet;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.MassFlowInlet;
import org.eclipse.ice.reactor.plant.OneInOneOutJunction;
import org.eclipse.ice.reactor.plant.Outlet;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PipeToPipeJunction;
import org.eclipse.ice.reactor.plant.PipeWithHeatStructure;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.PointKinetics;
import org.eclipse.ice.reactor.plant.Pump;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SeparatorDryer;
import org.eclipse.ice.reactor.plant.SolidWall;
import org.eclipse.ice.reactor.plant.SpecifiedDensityAndVelocityInlet;
import org.eclipse.ice.reactor.plant.Subchannel;
import org.eclipse.ice.reactor.plant.SubchannelBranch;
import org.eclipse.ice.reactor.plant.TDM;
import org.eclipse.ice.reactor.plant.TimeDependentJunction;
import org.eclipse.ice.reactor.plant.TimeDependentVolume;
import org.eclipse.ice.reactor.plant.Turbine;
import org.eclipse.ice.reactor.plant.Valve;
import org.eclipse.ice.reactor.plant.VolumeBranch;
import org.eclipse.ice.reactor.plant.WetWell;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PWRAssembly;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;

/**
 * This class provides keys for use in a {@link StateBroker}. It is tailored
 * specifically for {@link LWRComponent}s and the LWR analysis views.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class StateBrokerHandler implements IStateBrokerHandler,
		ILWRComponentVisitor, IPlantComponentVisitor {

	/**
	 * The StateBroker that holds selections of LWRComponents.
	 */
	private StateBroker broker;
	/**
	 * The IDataProvider for a selection LWRComponent. May be null.
	 */
	private IDataProvider data;
	/**
	 * The location of the LWRComponent in its parent LWRComponent. If -1, then
	 * the component does not occupy a location in its parent.
	 */
	private int row, column;
	/**
	 * The parent object to the LWRComponent that is visited by this class.
	 */
	private Object parent;
	/**
	 * Whether or not the LWRComponent was added to the broker.
	 */
	private boolean added;
	/**
	 * The source of the data (Input or Reference). This determines the final
	 * key used to add an object to the broker.
	 */
	private DataSource dataSource = DataSource.Input;

	/**
	 * This class provides a default implementation of an
	 * {@link ILWRComponentVisitor}. It can be used to retrieve some value for a
	 * specific type of {@link LWRComponent} by using its
	 * {@link BaseVisitor#getValue(Object)} method.<br>
	 * <br>
	 * <b>It is primarily intended to be used for inner/untyped classes.</b>
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class BaseVisitor implements ILWRComponentVisitor,
			IPlantComponentVisitor {

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
			if (object != null) {
				if (object instanceof PlantComponent) {
					((PlantComponent) object).accept(this);
				} else {
					((LWRComponent) object).accept(this);
				}
			}
			return value;
		}

		@Override
		public void visit(PressurizedWaterReactor lwrComp) {
		}

		@Override
		public void visit(BWReactor lwrComp) {
		}

		@Override
		public void visit(FuelAssembly lwrComp) {
		}

		@Override
		public void visit(RodClusterAssembly lwrComp) {
		}

		@Override
		public void visit(LWRRod lwrComp) {
		}

		@Override
		public void visit(ControlBank lwrComp) {
		}

		@Override
		public void visit(IncoreInstrument lwrComp) {
		}

		@Override
		public void visit(Tube lwrComp) {
		}

		@Override
		public void visit(Ring lwrComp) {
		}

		@Override
		public void visit(PlantComposite plantComp) {
		}

		@Override
		public void visit(GeometricalComponent plantComp) {
		}

		@Override
		public void visit(Junction plantComp) {
		}

		@Override
		public void visit(Reactor plantComp) {
		}

		@Override
		public void visit(PointKinetics plantComp) {
		}

		@Override
		public void visit(HeatExchanger plantComp) {
		}

		@Override
		public void visit(Pipe plantComp) {
		}

		// ---- Sub-classes are redirected to their base classes. ---- //
		@Override
		public void visit(CoreChannel plantComp) {
			visit((Pipe) plantComp);
		}

		@Override
		public void visit(Subchannel plantComp) {
			visit((Pipe) plantComp);
		}

		@Override
		public void visit(PipeWithHeatStructure plantComp) {
			visit((Pipe) plantComp);
		}

		@Override
		public void visit(Branch plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SubchannelBranch plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(VolumeBranch plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(FlowJunction plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(WetWell plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Boundary plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(OneInOneOutJunction plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Turbine plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(IdealPump plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Pump plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Valve plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(PipeToPipeJunction plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Inlet plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(MassFlowInlet plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(Outlet plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SolidWall plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(TDM plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(TimeDependentJunction plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(TimeDependentVolume plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(DownComer plantComp) {
			visit((Junction) plantComp);
		}

		@Override
		public void visit(SeparatorDryer plantComp) {
			visit((Junction) plantComp);
		}
	}

	/**
	 * Sets the {@link StateBroker} used by this handler to store
	 * {@link LWRComponent}s.
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
	@Override
	public void setDataSource(DataSource dataSource) {
		if (dataSource != null) {
			this.dataSource = dataSource;
		}
	}

	/**
	 * This method returns keys only for LWRComponents, specifically reactors,
	 * assemblies, and rods/tubes.
	 */
	@Override
	public String getKey(Object object) {
		String key = null;

		// TODO Figure out a better way to handle keys shared between reactor
		// types.

		// Create a visitor to generate a key from the object.
		BaseVisitor keyVisitor = new BaseVisitor() {
			@Override
			public void visit(PressurizedWaterReactor lwrComp) {
				value = "PWReactor";
			}

			@Override
			public void visit(BWReactor lwrComp) {
				// Nothing to do.
			}

			@Override
			public void visit(FuelAssembly lwrComp) {
				value = "lwr" + AssemblyType.Fuel.toString();
			}

			@Override
			public void visit(RodClusterAssembly lwrComp) {
				value = "lwr" + AssemblyType.RodCluster.toString();
			}

			@Override
			public void visit(LWRRod lwrComp) {
				value = "rod";
			}

			@Override
			public void visit(ControlBank lwrComp) {
				value = "lwr" + AssemblyType.ControlBank.toString();
			}

			@Override
			public void visit(IncoreInstrument lwrComp) {
				value = "lwr" + AssemblyType.IncoreInstrument.toString();
			}

			@Override
			public void visit(Tube lwrComp) {
				value = "rod";
			}

			@Override
			public void visit(PlantComposite plantComp) {
				value = "plant";
			}

			@Override
			public void visit(GeometricalComponent plantComp) {
				value = "plantComponent";
			}

			@Override
			public void visit(Junction plantComp) {
				value = "plantComponent";
			}

			@Override
			public void visit(Reactor plantComp) {
				value = "plantComponent";
			}

			@Override
			public void visit(PointKinetics plantComp) {
				value = "plantComponent";
			}

			@Override
			public void visit(HeatExchanger plantComp) {
				value = "plantComponent";
			}

			@Override
			public void visit(Pipe plantComp) {
				value = "plantComponent";
			}
		};
		key = (String) keyVisitor.getValue(object);

		// FIXME We need to quit using input/reference in the state broker.
		return (key != null ? dataSource + "-" + key : null);
	}

	/**
	 * This method only adds LWRComponents, specifically reactors, assemblies,
	 * and rods/tubes (or any component implemented in this class' visit
	 * operations).
	 */
	@Override
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
			if (value instanceof PlantComponent) {
				((PlantComponent) value).accept(this);
			} else {
				((LWRComponent) value).accept(this);
			}
		}

		return added;
	}

	// ---- Generic add operations. ---- //
	/**
	 * Adds an {@link PlantComponent} to the current {@link StateBroker}.
	 * 
	 * @param plantComp
	 *            The PlantComponent to add to the StateBroker.
	 */
	public void addComponent(PlantComponent plantComp) {
		if (plantComp != null && broker != null) {
			plantComp.accept(this);
		}
	}

	/**
	 * Adds an {@link LWRComponent} to the current {@link StateBroker}. No data
	 * or location information is provided, but any such information that is
	 * required will be found, if possible.
	 * 
	 * @param lwrComp
	 *            The LWRComponent to add to the StateBroker.
	 */
	public void addComponent(LWRComponent lwrComp) {
		addComponent(lwrComp, null, -1, -1);
	}

	/**
	 * Adds an {@link LWRComponent} to the current {@link StateBroker}. No data
	 * is provided.
	 * 
	 * @param lwrComp
	 *            The LWRComponent to add to the StateBroker.
	 * @param row
	 *            The row of the LWRComponent in its parent LWRComponent.
	 * @param column
	 *            The column of the LWRComponent in its parent LWRComponent.
	 */
	public void addComponent(LWRComponent lwrComp, int row, int column) {
		addComponent(lwrComp, null, row, column);
	}

	/**
	 * Adds an {@link LWRComponent} to the current {@link StateBroker}.
	 * 
	 * @param lwrComp
	 *            The LWRComponent to add to the StateBroker.
	 * @param data
	 *            An {@link IDataProvider} associated either with the
	 *            LWRComponent or its location in its parent LWRComponent.
	 * @param row
	 *            The row of the LWRComponent in its parent LWRComponent.
	 * @param column
	 *            The column of the LWRComponent in its parent LWRComponent.
	 */
	public void addComponent(LWRComponent lwrComp, IDataProvider data, int row,
			int column) {
		// Make sure the component and broker are not null before proceeding.
		if (lwrComp != null && broker != null) {
			this.data = data;
			this.row = row;
			this.column = column;

			// Visit the component after setting the meta information about the
			// component. This should add it to the broker.
			lwrComp.accept(this);
		}

		return;
	}

	// --------------------------------- //

	// ---- Basic supported add operations. ---- //
	private void addPlant(PlantComposite plant) {
		String key = getKey(plant);

		if (key != null) {
			broker.putValue(key, plant);
			added = true;
		}

		return;
	}

	private void addPlantComponent(PlantComponent plantComp) {
		String key = getKey(plantComp);

		if (key != null) {
			broker.putValue(key, plantComp);
			added = true;
		}

		return;
	}

	private void addReactor(LWReactor reactor) {
		String key = getKey(reactor);

		if (key != null) {
			broker.putValue(key, reactor);
			added = true;
		}

		return;
	}

	private void addAssembly(PWRAssembly assembly,
			PressurizedWaterReactor parent) {
		if (assembly != null) {
			// Set the variables used in the loops below.
			int row = 0, column = 0;
			boolean found = false;
			int size = (parent != null ? parent.getSize() : 0);

			// Get the type of assembly.
			AssemblyType type = getAssemblyType(assembly);
			if (type != null) {

				// Loop over each location in the assembly and get the data
				// provider if the rod matches the assembly component at that
				// location.
				for (row = 0; !found && row < size; row++) {
					for (column = 0; column < size; column++) {
						if (assembly == parent.getAssemblyByLocation(type, row,
								column)) {
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

	private void addAssembly(PWRAssembly assembly, int row, int column) {
		String key = getKey(assembly);

		if (key != null) {
			LWRComponentInfo info = new LWRComponentInfo(row, column, assembly);
			broker.putValue(key, info);
			added = true;
		}

		return;
	}

	private void addRod(LWRRod rod, PWRAssembly parent) {
		if (rod != null) {
			// Set the variables used in the loops below.
			IDataProvider data = null;
			int row = 0, column = 0;
			boolean found = false;
			int size = (parent != null ? parent.getSize() : 0);

			// Loop over each location in the assembly and get the data provider
			// if the rod matches the assembly component at that location.
			for (row = 0; !found && row < size; row++) {
				for (column = 0; column < size; column++) {
					if (rod == parent.getLWRRodByLocation(row, column)) {
						data = parent.getLWRRodDataProviderAtLocation(row,
								column);

						// Break out of the loops. Make sure row is correct.
						found = true;
						row--;
						break;
					}
				}
			}

			// Add the rod. Adjust row and column if necessary.
			if (!found) {
				row = column = -1;
			}
			addRod(rod, data, row, column);
		}

		return;
	}

	private void addRod(LWRRod rod, IDataProvider data, int row, int column) {
		String key = getKey(rod);

		if (key != null) {
			LWRComponentInfo info = new LWRComponentInfo(row, column, rod, data);
			broker.putValue(key, info);
			added = true;
		}

		return;
	}

	private void addTube(Tube tube, FuelAssembly parent) {
		if (tube != null) {
			IDataProvider data = null;
			int row = 0, column = 0;

			boolean found = false;
			int size = (parent != null ? parent.getSize() : 0);

			// Loop over each location in the assembly and get the data provider
			// if the tube matches the assembly component at that location.
			for (row = 0; !found && row < size; row++) {
				for (column = 0; column < size; column++) {
					if (tube == parent.getTubeByLocation(row, column)) {
						data = parent
								.getTubeDataProviderAtLocation(row, column);

						// Break out of the loops. Make sure row is correct.
						found = true;
						row--;
						break;
					}
				}
			}

			// Add the tube. Adjust row and column if necessary.
			if (!found) {
				row = column = -1;
			}
			addTube(tube, data, row, column);
		}

		return;
	}

	private void addTube(Tube tube, IDataProvider data, int row, int column) {
		String key = getKey(tube);

		if (key != null) {
			LWRComponentInfo info = new LWRComponentInfo(row, column, tube,
					data);
			broker.putValue(key, info);
			added = true;
		}

		return;
	}

	// ----------------------------------------- //

	// ---- Utility operations ---- //
	private AssemblyType getAssemblyType(PWRAssembly assembly) {
		BaseVisitor visitor = new BaseVisitor() {
			@Override
			public void visit(FuelAssembly lwrComp) {
				value = AssemblyType.Fuel;
			}

			@Override
			public void visit(RodClusterAssembly lwrComp) {
				value = AssemblyType.RodCluster;
			}
		};
		return (AssemblyType) visitor.getValue(assembly);
	}

	// ---------------------------- //

	// ---- Implements ILWRComponentVisitor ---- //
	/**
	 * Adds the reactor directly to the broker as is.
	 */
	@Override
	public void visit(PressurizedWaterReactor lwrComp) {
		// TODO Update this when plant components can contain PWRs.
		addReactor(lwrComp);
	}

	/**
	 * BWReactors are not currently supported.
	 */
	@Override
	public void visit(BWReactor lwrComp) {
		// Nothing to do.
	}

	/**
	 * This method is not actually a part of {@link ILWRComponentVisitor}, but
	 * it can serve as the visit operation for any PWRAssembly type (fuel
	 * assemblies and RCAs).<br>
	 * <br>
	 * Wraps the assembly in an {@link LWRComponentInfo}. No data provider is
	 * necessary.
	 * 
	 * @param lwrComp
	 *            The PWRAssembly that is being visited.
	 */
	public void visit(PWRAssembly lwrComp) {
		// If the row and column are set, we can just add a new
		// LWRComponentInfo.
		if (row >= 0 && column >= 0) {
			addAssembly(lwrComp, row, column);
		}
		// Otherwise, we need to find the location of the assembly in the parent
		// reactor.
		else {
			// If the parent reactor does not exist, we can still put the value
			// into the broker.
			PressurizedWaterReactor reactor = null;

			// If the parent object exists and is an LWRComponent, try to
			// convert it to a valid parent of a FuelAssembly (currently, this
			// means it must be a PressurizedWaterReactor).
			BaseVisitor visitor = new BaseVisitor() {
				@Override
				public void visit(PressurizedWaterReactor lwrComp) {
					value = lwrComp;
				}
			};
			// Get the reactor from the visitor.
			reactor = (PressurizedWaterReactor) visitor.getValue(parent);

			// Try to add the assembly. This method finds the location of the
			// assembly within the reactor (if it is in the reactor).
			addAssembly(lwrComp, reactor);
		}

		return;
	}

	/**
	 * Wraps the assembly in an {@link LWRComponentInfo}. No data provider is
	 * necessary.
	 */
	@Override
	public void visit(FuelAssembly lwrComp) {
		visit((PWRAssembly) lwrComp);
	}

	/**
	 * Wraps the assembly in an {@link LWRComponentInfo}. No data provider is
	 * necessary.
	 */
	@Override
	public void visit(RodClusterAssembly lwrComp) {
		visit((PWRAssembly) lwrComp);
	}

	/**
	 * Wraps the rod in an {@link LWRComponentInfo}. The appropriate data
	 * provider should be added.
	 */
	@Override
	public void visit(LWRRod lwrComp) {
		// If the row and column are set, we can just add a new
		// LWRComponentInfo.
		if (row >= 0 && column >= 0) {
			addRod(lwrComp, data, row, column);
		}
		// Otherwise, we need to find the location of the rod in the parent
		// assembly.
		else {
			// If the parent assembly does not exist, we can still put the value
			// into the broker.
			PWRAssembly assembly = null;

			// If the parent object exists and is an LWRComponent, try to
			// convert it to a valid parent of a rod (currently, this means it
			// must be a PWRAssembly).
			BaseVisitor visitor = new BaseVisitor() {
				@Override
				public void visit(FuelAssembly lwrComp) {
					value = lwrComp;
				}

				@Override
				public void visit(RodClusterAssembly lwrComp) {
					value = lwrComp;
				}
			};
			// Get the assembly from the visitor.
			assembly = (PWRAssembly) visitor.getValue(parent);

			// Try to add the rod. This method finds the location of the rod in
			// its parent assembly.
			addRod(lwrComp, assembly);
		}

		return;
	}

	/**
	 * Wraps the assembly in an {@link LWRComponentInfo}. No data provider is
	 * necessary.
	 */
	@Override
	public void visit(ControlBank lwrComp) {
		// TODO
	}

	/**
	 * Wraps the assembly in an {@link LWRComponentInfo}. No data provider is
	 * necessary.
	 */
	@Override
	public void visit(IncoreInstrument lwrComp) {
		// TODO
	}

	/**
	 * Wraps the tube in an {@link LWRComponentInfo}. The appropriate data
	 * provider should be added.
	 */
	@Override
	public void visit(Tube lwrComp) {
		// If the row and column are set, we can just add a new
		// LWRComponentInfo.
		if (row >= 0 && column >= 0) {
			addTube(lwrComp, data, row, column);
		}
		// Otherwise, we need to find the location of the tube in the parent
		// assembly.
		else {
			// If the parent assembly does not exist, we can still put the value
			// into the broker.

			FuelAssembly assembly = null;
			// If the parent object exists and is an LWRComponent, try to
			// convert it to a valid parent of a FuelAssembly (currently, this
			// means it must be a PressurizedWaterReactor).
			BaseVisitor visitor = new BaseVisitor() {
				@Override
				public void visit(FuelAssembly lwrComp) {
					value = lwrComp;
				}
			};
			// Get the assembly from the visitor.
			LWRComponent parent = (LWRComponent) this.parent;
			assembly = (FuelAssembly) visitor.getValue(parent);

			// Try to add the tube. This method finds the location of the tube
			// in its parent assembly.
			addTube(lwrComp, assembly);
		}

		return;
	}

	/**
	 * Rings are not currently supported. Does nothing.
	 */
	@Override
	public void visit(Ring lwrComp) {
		// Nothing to do.
	}

	// ----------------------------------------- //

	// ---- Implements IPlantComponentVisitor ---- //
	@Override
	public void visit(PlantComposite plantComp) {
		addPlant(plantComp);
	}

	@Override
	public void visit(GeometricalComponent plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Junction plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Reactor plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(PointKinetics plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(HeatExchanger plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Pipe plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(CoreChannel plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Subchannel plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(PipeWithHeatStructure plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Branch plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(SubchannelBranch plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(VolumeBranch plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(FlowJunction plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(WetWell plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Boundary plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(OneInOneOutJunction plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Turbine plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(IdealPump plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Pump plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Valve plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(PipeToPipeJunction plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Inlet plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(MassFlowInlet plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(Outlet plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(SolidWall plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(TDM plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(TimeDependentJunction plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(TimeDependentVolume plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(DownComer plantComp) {
		addPlantComponent(plantComp);
	}

	@Override
	public void visit(SeparatorDryer plantComp) {
		addPlantComponent(plantComp);
	}
	// ------------------------------------------- //
}