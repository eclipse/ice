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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.reactor.plant.Branch;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.DownComer;
import org.eclipse.ice.reactor.plant.FlowJunction;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.IdealPump;
import org.eclipse.ice.reactor.plant.Inlet;
import org.eclipse.ice.reactor.plant.Outlet;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PipeWithHeatStructure;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PointKinetics;
import org.eclipse.ice.reactor.plant.Pump;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SeparatorDryer;
import org.eclipse.ice.reactor.plant.SolidWall;
import org.eclipse.ice.reactor.plant.Subchannel;
import org.eclipse.ice.reactor.plant.SubchannelBranch;
import org.eclipse.ice.reactor.plant.TDM;
import org.eclipse.ice.reactor.plant.TimeDependentJunction;
import org.eclipse.ice.reactor.plant.TimeDependentVolume;
import org.eclipse.ice.reactor.plant.Turbine;
import org.eclipse.ice.reactor.plant.Valve;
import org.eclipse.ice.reactor.plant.VolumeBranch;
import org.eclipse.ice.reactor.plant.WetWell;

/**
 * This class is used to create a generic PlantComponent based on a string (or a
 * string that is pulled from an {@link Entry} in a {@link DataComponent}).
 * 
 * @author Jordan
 * 
 */
public class PlantComponentFactory {

	/**
	 * A HashMap containing {@link IComponentCreator}s keyed on type strings.
	 * Currently, these strings are hard-coded as they appear in RELAP files.
	 */
	private final Map<String, IComponentCreator> creatorMap;

	/**
	 * The default constructor.
	 */
	public PlantComponentFactory() {
		// Initialize the map of IComponentcreatorMap and fill it.
		creatorMap = new HashMap<String, IComponentCreator>();

		creatorMap.put("Pipe", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Pipe();
			}
		});
		creatorMap.put("HeatExchanger", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new HeatExchanger();
			}
		});
		creatorMap.put("Reactor", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Reactor();
			}
		});
		creatorMap.put("Branch", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Branch();
			}
		});
		creatorMap.put("CoreChannel", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new CoreChannel();
			}
		});
		creatorMap.put("Subchannel", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Subchannel();
			}
		});
		creatorMap.put("PipeWithHeatStructure", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new PipeWithHeatStructure();
			}
		});
		creatorMap.put("Valve", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Valve();
			}
		});
		creatorMap.put("Pump", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Pump();
			}
		});
		creatorMap.put("FlowJunction", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new FlowJunction();
			}
		});
		creatorMap.put("SubchannelBranch", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new SubchannelBranch();
			}
		});
		creatorMap.put("IdealPump", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new IdealPump();
			}
		});
		creatorMap.put("VolumeBranch", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new VolumeBranch();
			}
		});
		creatorMap.put("DownComer", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new DownComer();
			}
		});
		creatorMap.put("WetWell", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new WetWell();
			}
		});
		creatorMap.put("Turbine", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Turbine();
			}
		});
		creatorMap.put("PointKinetics", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new PointKinetics();
			}
		});
		creatorMap.put("Outlet", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Outlet();
			}
		});
		creatorMap.put("Inlet", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new Inlet();
			}
		});
		creatorMap.put("SeparatorDryer", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new SeparatorDryer();
			}
		});
		creatorMap.put("TDM", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new TDM();
			}
		});
		creatorMap.put("SolidWall", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new SolidWall();
			}
		});
		creatorMap.put("TimeDependentJunction", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new TimeDependentJunction();
			}
		});
		creatorMap.put("TimeDependentVolume", new IComponentCreator() {
			@Override
			public PlantComponent createComponent() {
				return new TimeDependentVolume();
			}
		});

		return;
	}

	/**
	 * Creates a PlantComponent based on a type string.
	 * 
	 * @param type
	 *            The string corresponding to the type of PlantComponent to
	 *            create.
	 * @return A new PlantComponent if the type string is valid, null otherwise.
	 */
	public PlantComponent createComponent(String type) {
		// Use the available IComponentCreator based on the string or null if
		// no creator matches the string.
		IComponentCreator creator = creatorMap.get(type);
		return (creator != null ? creator.createComponent() : null);
	}

	/**
	 * Creates a PlantComponent based on the "type" {@link Entry} in a
	 * DataComponent.
	 * 
	 * @param dataComp
	 *            The DataComponent that should have a "type" Entry.
	 * @return A new PlantComponent if the DataComponent has a valid "type"
	 *         Entry, null otherwise.
	 */
	public PlantComponent createComponent(DataComponent dataComp) {
		// Set the default return value.
		PlantComponent plantComp = null;

		// Get the "type" Entry from the DataComponent and re-direct to the
		// createComponent(String) method with the Entry's value.
		if (dataComp != null) {
			IEntry entry = dataComp.retrieveEntry("type");
			if (entry != null) {
				plantComp = createComponent(entry.getValue());
			}
		}
		return plantComp;
	}

	/**
	 * This interface is for an object that creates PlantComponents.
	 * 
	 * @author Jordan
	 * 
	 */
	private interface IComponentCreator {
		/**
		 * Creates a specific type of PlantComponent.
		 * 
		 * @return A PlantComponent. This should never be null.
		 */
		public PlantComponent createComponent();
	}

}
