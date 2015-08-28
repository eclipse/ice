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
package org.eclipse.ice.client.widgets.reactoreditor.plant;

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
 * This visitor is commonly used for Plant MVC. Currently, only {@link Pipe}s,
 * {@link Junction}s, {@link Reactor}s, and {@link HeatExchanger}s have views
 * and controllers, so this class leaves the four base class visit operations as
 * abstract and re-directs the visits of the sub-classes to the four base class
 * visits. Un-used visit operations (like GeometricalComponent) are implemented
 * as unused.
 * 
 * @author Jordan H. Deyton
 * 
 */
public abstract class PlantControllerVisitor implements IPlantComponentVisitor {

	@Override
	public abstract void visit(Junction plantComp);

	@Override
	public abstract void visit(Reactor plantComp);

	@Override
	public abstract void visit(HeatExchanger plantComp);

	@Override
	public abstract void visit(Pipe plantComp);

	// Pipe sub-classes redirect to the Pipe visit.
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

	// Junction sub-classes redirect to the Junction visit.
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

	// Un-used visit operations.
	@Override
	public void visit(PlantComposite plantComp) {
		// Do nothing.
	}
	@Override
	public void visit(GeometricalComponent plantComp) {
		// Do nothing.
	}
	@Override
	public void visit(PointKinetics plantComp) {
		// Do nothing.
	}
	
}
