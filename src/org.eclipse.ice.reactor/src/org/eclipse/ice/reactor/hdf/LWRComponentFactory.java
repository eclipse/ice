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

public class LWRComponentFactory {

	private final Map<HDF5LWRTagType, IComponentCreator> creatorMap;

	private interface IComponentCreator {
		public LWRComponent createComponent(int size);
	}

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

	public LWRComponent createComponent(HDF5LWRTagType type, int size) {
		LWRComponent component = null;

		IComponentCreator creator = creatorMap.get(type);
		if (creator != null) {
			component = creator.createComponent(size);
		}

		return component;
	}
}
