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

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
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

/**
 * <p>
 * This class serves as a base for classes that must link {@link Entry} values
 * from {@link DataComponent}s with properties of {@link PlantComponent}s.
 * </p>
 * <p>
 * It is recommended that sub-classes use {@link BaseVisitor} and override visit
 * operations as necessary to determine which Entries to link with the
 * PlantComponent.
 * </p>
 * 
 * @author Jordan
 * 
 */
public abstract class PlantComponentLinker {

	/**
	 * The PlantBlockManager that must link Entries with PlantComponent
	 * properties. In some cases, this manager must be used to look up existing
	 * PlantComponents based on their name.
	 */
	protected final PlantBlockManager plantManager;

	/**
	 * The default constructor.
	 * 
	 * @param plantManager
	 *            The PlantBlockManager that must link Entries with
	 *            PlantComponent properties.
	 */
	public PlantComponentLinker(PlantBlockManager plantManager) {
		// Set the PlantBlockManager.
		this.plantManager = (plantManager != null ? plantManager
				: new PlantBlockManager());
	}

	/**
	 * Links a PlantComponent with supported {@link Entry} instances contained
	 * in a DataComponent.
	 * 
	 * @param plantComp
	 *            The PlantComponent whose properties are contained in Entries.
	 * @param dataComp
	 *            The DataComponent that contains the Entries.
	 * @return A List of all {@link EntryListener}s created to link the Entries
	 *         and PlantComponents.
	 */
	public abstract List<EntryListener> linkComponents(
			PlantComponent plantComp, DataComponent dataComp);

	/**
	 * This class provides a basic {@link IPlantComponentVisitor} implementation
	 * to save code in sub-classes of {@link PlantComponentLinker}. In its
	 * default state, this visitor re-routes sub-classes of Junction, Pipe,
	 * HeatExchanger, and Reactor to their base visit operations. The base visit
	 * operations do nothing and should be overridden. For example, a
	 * JunctionLinker might instantiate a BaseVisitor in its
	 * {@link PlantComponentLinker#linkComponents(PlantComponent, DataComponent)
	 * linkComponents()} method. This BaseVisitor will have its
	 * {@link #visit(Junction)} method overridden.
	 * 
	 * @author Jordan
	 * 
	 */
	protected class BaseVisitor implements IPlantComponentVisitor {

		// Base sub-classes do nothing.
		public void visit(Junction plantComp) {
			// Do nothing by default.
		}

		public void visit(Reactor plantComp) {
			// Do nothing by default.
		}

		public void visit(HeatExchanger plantComp) {
			// Do nothing by default.
		}

		public void visit(Pipe plantComp) {
			// Do nothing by default.
		}

		// Pipe sub-classes redirect to the Pipe visit.
		public void visit(CoreChannel plantComp) {
			visit((Pipe) plantComp);
		}

		public void visit(Subchannel plantComp) {
			visit((Pipe) plantComp);
		}

		public void visit(PipeWithHeatStructure plantComp) {
			visit((Pipe) plantComp);
		}

		// Junction sub-classes redirect to the Junction visit.
		public void visit(Branch plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(SubchannelBranch plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(VolumeBranch plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(FlowJunction plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(WetWell plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(Boundary plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(OneInOneOutJunction plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(Turbine plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(IdealPump plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(Pump plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(Valve plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(PipeToPipeJunction plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(Inlet plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(MassFlowInlet plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(Outlet plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(SolidWall plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(TDM plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(TimeDependentJunction plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(TimeDependentVolume plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(DownComer plantComp) {
			visit((Junction) plantComp);
		}

		public void visit(SeparatorDryer plantComp) {
			visit((Junction) plantComp);
		}

		// Un-used visit operations.
		public void visit(PlantComposite plantComp) {
			// Do nothing.
		}

		public void visit(GeometricalComponent plantComp) {
			// Do nothing.
		}

		public void visit(PointKinetics plantComp) {
			// Do nothing.
		}
	}
}
