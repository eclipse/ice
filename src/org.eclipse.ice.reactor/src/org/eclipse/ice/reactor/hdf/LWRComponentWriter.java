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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.MaterialType;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.TubeType;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PWRAssembly;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;

public class LWRComponentWriter {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LWRComponentWriter.class);

	private final HdfIOFactory factory;

	private interface IComponentWriter {
		public void writeComponent(int groupId, LWRComponent component)
				throws NullPointerException, HDF5Exception;
	}

	private final Map<HDF5LWRTagType, IComponentWriter> writerMap;

	public LWRComponentWriter(HdfIOFactory factory) {
		this.factory = factory;

		// Initialize the map of component writers.
		writerMap = new HashMap<HDF5LWRTagType, IComponentWriter>();
		addWriters();
	}

	public void writeComponent(int groupId, LWRComponent component) {

		if (component != null) {
			// Find the appropriate writer based on the tag.
			IComponentWriter writer = writerMap.get(component.getHDF5LWRTag());

			// Try to write the component using the writer.
			try {
				writer.writeComponent(groupId, component);
			} catch (NullPointerException | HDF5Exception e) {
				logger.error(
						getClass().getName() + " error: " + "Error writing "
								+ component.getClass().getName()
								+ " with name \"" + component.getName() + "\".",
						e);
			}
		}

		return;
	}

	private void write(int groupId, LWRComponent component)
			throws NullPointerException, HDF5Exception {
		// Write properties inherited from Identifiable...
		factory.writeIdentifiableAttributes(component, groupId);

		// Write properties specific to this type...
		String tag = component.getHDF5LWRTag().toString();
		factory.writeStringAttribute(groupId, "HDF5LWRTag", tag);

		return;
	}

	private void write(int groupId, LWRComposite composite)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) composite);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, LWReactor reactor)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComposite)...
		write(groupId, (LWRComponent) reactor);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, BWReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWReactor)...
		write(groupId, (LWReactor) reactor);

		// Write properties specific to this type...
		// Nothing to do.

		// Read the assemblies...
		// Nothing to do. This is not supported in BWReactor yet.

		return;
	}

	private void write(int groupId, PressurizedWaterReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWReactor)...
		write(groupId, (LWReactor) reactor);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, PWRAssembly assembly)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComposite)...
		write(groupId, (LWRComponent) assembly);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, ControlBank controlBank)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) controlBank);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, FuelAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (PWRAssembly)...
		write(groupId, (PWRAssembly) assembly);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, IncoreInstrument incoreInstrument)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) incoreInstrument);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, RodClusterAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (PWRAssembly)...
		write(groupId, (PWRAssembly) assembly);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, LWRRod rod)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) rod);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, Ring ring)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) ring);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, Tube tube)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (Ring)...
		write(groupId, (Ring) tube);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, Material material)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) material);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, MaterialBlock block)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) block);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, GridLabelProvider provider)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) provider);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, LWRGridManager gridManager)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) gridManager);

		// Write properties specific to this type...
		// TODO

		return;
	}

	/**
	 * Populates the {@link #writerMap} with {@link IComponentWriter}s that
	 * simply re-direct to the appropriate write operation based on the tag.
	 */
	private void addWriters() {
		// Add writers for base types.
		writerMap.put(HDF5LWRTagType.LWRCOMPONENT, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRComponent) component);
			}
		});
		writerMap.put(HDF5LWRTagType.LWRCOMPOSITE, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRComposite) component);
			}
		});

		// Add writers for LWR types.
		writerMap.put(HDF5LWRTagType.LWREACTOR, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWReactor) component);
			}
		});
		writerMap.put(HDF5LWRTagType.BWREACTOR, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (BWReactor) component);
			}
		});
		writerMap.put(HDF5LWRTagType.PWREACTOR, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (PressurizedWaterReactor) component);
			}
		});

		// Add writers for the assembly types.
		writerMap.put(HDF5LWRTagType.PWRASSEMBLY, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (PWRAssembly) component);
			}
		});
		writerMap.put(HDF5LWRTagType.CONTROL_BANK, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (ControlBank) component);
			}
		});
		writerMap.put(HDF5LWRTagType.FUEL_ASSEMBLY, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (FuelAssembly) component);
			}
		});
		writerMap.put(HDF5LWRTagType.INCORE_INSTRUMENT, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (IncoreInstrument) component);
			}
		});
		writerMap.put(HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY,
				new IComponentWriter() {
					@Override
					public void writeComponent(int groupId,
							LWRComponent component)
									throws NullPointerException, HDF5Exception {
						write(groupId, (RodClusterAssembly) component);
					}
				});

		// Add writers for the rod/pin types.
		writerMap.put(HDF5LWRTagType.LWRROD, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRRod) component);
			}
		});

		// Add writers for the ring types.
		writerMap.put(HDF5LWRTagType.RING, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (Ring) component);
			}
		});
		writerMap.put(HDF5LWRTagType.TUBE, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (Tube) component);
			}
		});

		// Add writers for Materials.
		writerMap.put(HDF5LWRTagType.MATERIAL, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (Material) component);
			}
		});
		writerMap.put(HDF5LWRTagType.MATERIALBLOCK, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (MaterialBlock) component);
			}
		});

		// Add writers for other LWRComponent types.
		writerMap.put(HDF5LWRTagType.GRID_LABEL_PROVIDER,
				new IComponentWriter() {
					@Override
					public void writeComponent(int groupId,
							LWRComponent component)
									throws NullPointerException, HDF5Exception {
						write(groupId, (GridLabelProvider) component);
					}
				});
		writerMap.put(HDF5LWRTagType.LWRGRIDMANAGER, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRGridManager) component);
			}
		});

		return;
	}
}
