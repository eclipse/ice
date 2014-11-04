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
package org.eclipse.ice.reactor.plant;

import org.eclipse.ice.io.hdf.HdfIOFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

/**
 * This class provides an implementation of {@link HdfIOFactory} geared toward
 * {@link PlantComponent}s.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantIOFactory extends HdfIOFactory {

	/**
	 * The writer is responsible for writing an individual PlantComponent.
	 */
	private final PlantComponentWriter writer;
	/**
	 * The reader is responsible for creating and reading a PlantComponent from
	 * an HDF5 Group.
	 */
	private final PlantComponentReader reader;

	/**
	 * A simple interface for creating {@link PlantComponent}s.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private interface IComponentCreator {
		/**
		 * Gets a tag associated with the PlantComponent type.
		 */
		public String getTag();

		/**
		 * Creates a new PlantComponent of the desired type.
		 */
		public PlantComponent createComponent();
	}

	/**
	 * A Map of the tag Attributes to {@link IComponentCreator}s.
	 */
	private Map<String, IComponentCreator> tagMap;
	/**
	 * A Map of the supported classes to {@link IComponentCreator}s.
	 */
	private Map<Class<?>, IComponentCreator> classMap;

	/**
	 * The default constructor.
	 */
	public PlantIOFactory() {

		// Create the component writer and reader.
		writer = new PlantComponentWriter(this);
		reader = new PlantComponentReader(this);

		// Create the two maps of classes, tags, and component creators.
		tagMap = new HashMap<String, IComponentCreator>();
		classMap = new HashMap<Class<?>, IComponentCreator>();

		IComponentCreator creator;

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.composite";
			}

			public PlantComponent createComponent() {
				return new PlantComposite();
			}
		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(PlantComposite.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.pipe";
			}

			public PlantComponent createComponent() {
				return new Pipe();
			}
		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Pipe.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.heatExchanger";
			}

			public PlantComponent createComponent() {
				return new HeatExchanger();
			}
		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(HeatExchanger.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.junction";
			}

			public PlantComponent createComponent() {
				return new Junction();
			}
		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Junction.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.reactor";
			}

			public PlantComponent createComponent() {
				return new Reactor();
			}
		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Reactor.class, creator);

		// TODO Add the other classes.

		// Generic classes.
		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.GeometricalComponent";
			}

			public PlantComponent createComponent() {
				return new GeometricalComponent();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(GeometricalComponent.class, creator);
		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.PointKinetics";
			}

			public PlantComponent createComponent() {
				return new PointKinetics();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(PointKinetics.class, creator);

		// Pipe sub-classes
		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.CoreChannel";
			}

			public PlantComponent createComponent() {
				return new CoreChannel();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(CoreChannel.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Subchannel";
			}

			public PlantComponent createComponent() {
				return new Subchannel();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Subchannel.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.PipeWithHeatStructure";
			}

			public PlantComponent createComponent() {
				return new PipeWithHeatStructure();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(PipeWithHeatStructure.class, creator);

		// Junction sub-classes.
		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Branch";
			}

			public PlantComponent createComponent() {
				return new Branch();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Branch.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.SubchannelBranch";
			}

			public PlantComponent createComponent() {
				return new SubchannelBranch();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(SubchannelBranch.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.VolumeBranch";
			}

			public PlantComponent createComponent() {
				return new VolumeBranch();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(VolumeBranch.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.FlowJunction";
			}

			public PlantComponent createComponent() {
				return new FlowJunction();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(FlowJunction.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.WetWell";
			}

			public PlantComponent createComponent() {
				return new WetWell();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(WetWell.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Boundary";
			}

			public PlantComponent createComponent() {
				return new Boundary();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Boundary.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.OneInOneOutJunction";
			}

			public PlantComponent createComponent() {
				return new OneInOneOutJunction();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(OneInOneOutJunction.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Turbine";
			}

			public PlantComponent createComponent() {
				return new Turbine();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Turbine.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.IdealPump";
			}

			public PlantComponent createComponent() {
				return new IdealPump();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(IdealPump.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Pump";
			}

			public PlantComponent createComponent() {
				return new Pump();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Pump.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Valve";
			}

			public PlantComponent createComponent() {
				return new Valve();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Valve.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.PipeToPipeJunction";
			}

			public PlantComponent createComponent() {
				return new PipeToPipeJunction();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(PipeToPipeJunction.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Inlet";
			}

			public PlantComponent createComponent() {
				return new Inlet();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Inlet.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.MassFlowInlet";
			}

			public PlantComponent createComponent() {
				return new MassFlowInlet();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(MassFlowInlet.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.SpecifiedDensityAndVelocityInlet";
			}

			public PlantComponent createComponent() {
				return new SpecifiedDensityAndVelocityInlet();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(SpecifiedDensityAndVelocityInlet.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.Outlet";
			}

			public PlantComponent createComponent() {
				return new Outlet();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(Outlet.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.SolidWall";
			}

			public PlantComponent createComponent() {
				return new SolidWall();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(SolidWall.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.TDM";
			}

			public PlantComponent createComponent() {
				return new TDM();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(TDM.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.TimeDependentJunction";
			}

			public PlantComponent createComponent() {
				return new TimeDependentJunction();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(TimeDependentJunction.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.TimeDependentVolume";
			}

			public PlantComponent createComponent() {
				return new TimeDependentVolume();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(TimeDependentVolume.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.DownComer";
			}

			public PlantComponent createComponent() {
				return new DownComer();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(DownComer.class, creator);

		creator = new IComponentCreator() {
			public String getTag() {
				return "plant.SeparatorDryer";
			}

			public PlantComponent createComponent() {
				return new SeparatorDryer();
			}

		};
		tagMap.put(creator.getTag(), creator);
		classMap.put(SeparatorDryer.class, creator);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.hdf.IHdfIOFactory#getSupportedClasses()
	 */
	@Override
	public List<Class<?>> getSupportedClasses() {
		return new ArrayList<Class<?>>(classMap.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.hdf.IHdfIOFactory#getTag(java.lang.Class)
	 */
	@Override
	public String getTag(Class<?> supportedClass) {
		IComponentCreator creator = classMap.get(supportedClass);
		return (creator != null ? creator.getTag() : null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.hdf.HdfIOFactory#writeObjectData(int,
	 * java.lang.Object)
	 */
	@Override
	public void writeObjectData(int groupId, Object object)
			throws NullPointerException, HDF5Exception, HDF5LibraryException {

		// Use the writer to write any PlantComponents.
		if (object != null && object instanceof PlantComponent) {
			writer.writePlantComponent((PlantComponent) object, groupId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.hdf.IHdfIOFactory#read(int, java.lang.String)
	 */
	@Override
	public Object read(int groupId, String tag) throws NullPointerException,
			HDF5Exception, HDF5LibraryException {

		Object object = null;

		// We need to create a new PlantComponent based on the tag. Then, use
		// the reader to load the component with the data from the file.
		IComponentCreator creator = tagMap.get(tag);
		if (creator != null) {
			PlantComponent component = creator.createComponent();
			reader.readPlantComponent(component, groupId);
			object = component;
		}

		return object;
	}

}
