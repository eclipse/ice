/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.ice.reactor.hdf;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PWRAssembly;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;

/**
 * This class provides a factory that can instantiate {@link LWRComponent}s of
 * any type based on the provided {@link HDF5LWRTagType}. Note that the size is
 * required as some components require the size at construction time.
 * 
 * @author Jordan Deyton
 *
 */
public class LWRComponentFactory {

	/**
	 * A simple interface for creating components. This is used to redirect the
	 * create operations to one for the specific type. We use this because the
	 * LWR visitor does not include visit operations for all types with an
	 * {@link HDF5LWRTagType}
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private interface IComponentCreator {
		/**
		 * Creates the specific type of component with the specified size.
		 * 
		 * @param size
		 *            The size, if available, of the component. This is only
		 *            used for some component types.
		 * @return The new component of a specific type.
		 */
		public LWRComponent createComponent(int size);
	}

	/**
	 * The map of creators. For the content of the map, see
	 * {@link #addCreators()}.
	 */
	private final Map<HDF5LWRTagType, IComponentCreator> creatorMap;

	/**
	 * The default constructor.
	 */
	public LWRComponentFactory() {
		// Initialize the map of component creators.
		creatorMap = new HashMap<HDF5LWRTagType, IComponentCreator>();
		addCreators();

		return;
	}

	/**
	 * Populates the {@link #creatorMap} with {@link IComponentCreator}s that
	 * simply initialize the appropriate component based on the tag.
	 */
	private void addCreators() {
		// Add creators for base types.
		creatorMap.put(HDF5LWRTagType.LWRCOMPONENT, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new LWRComponent();
			}
		});
		creatorMap.put(HDF5LWRTagType.LWRCOMPOSITE, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new LWRComposite();
			}
		});

		// Add creators for LWR types.
		creatorMap.put(HDF5LWRTagType.LWREACTOR, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new LWReactor(size);
			}
		});
		creatorMap.put(HDF5LWRTagType.BWREACTOR, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new BWReactor(size);
			}
		});
		creatorMap.put(HDF5LWRTagType.PWREACTOR, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new PressurizedWaterReactor(size);
			}
		});

		// Add creators for the assembly types.
		creatorMap.put(HDF5LWRTagType.PWRASSEMBLY, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new PWRAssembly(size);
			}
		});
		creatorMap.put(HDF5LWRTagType.CONTROL_BANK, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new ControlBank();
			}
		});
		creatorMap.put(HDF5LWRTagType.FUEL_ASSEMBLY, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new FuelAssembly(size);
			}
		});
		creatorMap.put(HDF5LWRTagType.INCORE_INSTRUMENT,
				new IComponentCreator() {
					@Override
					public LWRComponent createComponent(int size) {
						return new IncoreInstrument();
					}
				});
		creatorMap.put(HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY,
				new IComponentCreator() {
					@Override
					public LWRComponent createComponent(int size) {
						return new RodClusterAssembly(size);
					}
				});

		// Add creators for the rod/pin types.
		creatorMap.put(HDF5LWRTagType.LWRROD, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new LWRRod();
			}
		});

		// Add creators for the ring types.
		creatorMap.put(HDF5LWRTagType.RING, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new Ring();
			}
		});
		creatorMap.put(HDF5LWRTagType.TUBE, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new Tube();
			}
		});

		// Add creators for Materials.
		creatorMap.put(HDF5LWRTagType.MATERIAL, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new Material();
			}
		});
		creatorMap.put(HDF5LWRTagType.MATERIALBLOCK, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new MaterialBlock();
			}
		});

		// Add creators for other LWRComponent types.
		creatorMap.put(HDF5LWRTagType.GRID_LABEL_PROVIDER,
				new IComponentCreator() {
					@Override
					public LWRComponent createComponent(int size) {
						return new GridLabelProvider(size);
					}
				});
		creatorMap.put(HDF5LWRTagType.LWRGRIDMANAGER, new IComponentCreator() {
			@Override
			public LWRComponent createComponent(int size) {
				return new LWRGridManager(size);
			}
		});

		return;
	}

	/**
	 * Creates a component with the specified type and size.
	 * 
	 * @param type
	 *            The type of component, usually read from a file as a tag. Use
	 *            {@link HDF5LWRTagType#toType(String)} to get an enum value
	 *            from a string.
	 * @param size
	 *            The size of the component. May be any number if the component
	 *            does not need a size.
	 * @return A new component of a specific type, depending on the specified
	 *         tag, or {@code null} if the type is not supported (or
	 *         {@code null}).
	 */
	public LWRComponent createComponent(HDF5LWRTagType type, int size) {
		LWRComponent component = null;

		IComponentCreator creator = creatorMap.get(type);
		if (creator != null) {
			component = creator.createComponent(size);
		}

		return component;
	}
}
