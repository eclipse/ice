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

/** 
 * <p>Defines the visitation routine for PlantComponents so that the type of a PlantComponent can be revealed.</p>
 * @author w5q
 */
public interface IPlantComponentVisitor {
	
	public void visit(PlantComposite plantComp);
	
	/** 
	 * <p>Visits a PlantComponent of type GeometricalComponent.</p>
	 * @param plantComp 	<p>The GeometricalComponent accepting visitors.</p>
	 */
	public void visit(GeometricalComponent plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Junction.</p>
	 * @param plantComp 	<p>The Junction accepting visitors.</p>
	 */
	public void visit(Junction plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Reactor.</p>
	 * @param plantComp 	<p>The Reactor accepting visitors.</p>
	 */
	public void visit(Reactor plantComp);

	/** 
	 * <p>Visits a PlantComponent of type PointKinetics.</p>
	 * @param plantComp 	<p>The PointKinetics accepting visitors.</p>
	 */
	public void visit(PointKinetics plantComp);

	/** 
	 * <p>Visits a PlantComponent of type HeatExchanger.</p>
	 * @param plantComp 	<p>The HeatExchanger accepting visitors.</p>
	 */
	public void visit(HeatExchanger plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Pipe.</p>
	 * @param plantComp 	<p>The Pipe accepting visitors.</p>
	 */
	public void visit(Pipe plantComp);

	/** 
	 * <p>Visits a PlantComponent of type CoreChannel.</p>
	 * @param plantComp 	<p>The CoreChannel accepting visitors.</p>
	 */
	public void visit(CoreChannel plantComp);

	/** 
	 * <p>Visits a PlantComponent of type GeometricalComponent.</p>
	 * @param plantComp 	<p>The Subchannel accepting visitors.</p>
	 */
	public void visit(Subchannel plantComp);

	/** 
	 * <p>Visits a PlantComponent of type PipeWithHeatStructure.</p>
	 * @param plantComp 	<p>The PipeWithHeatStructure accepting visitors.</p>
	 */
	public void visit(PipeWithHeatStructure plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Branch.</p>
	 * @param plantComp 	<p>The Branch accepting visitors.</p>
	 */
	public void visit(Branch plantComp);

	/** 
	 * <p>Visits a PlantComponent of type SubchannelBranch.</p>
	 * @param plantComp 	<p>The SubchannelBranch accepting visitors.</p>
	 */
	public void visit(SubchannelBranch plantComp);

	/** 
	 * <p>Visits a PlantComponent of type VolumeBranch.</p>
	 * @param plantComp 	<p>The VolumeBranch accepting visitors.</p>
	 */
	public void visit(VolumeBranch plantComp);

	/** 
	 * <p>Visits a PlantComponent of type FlowJunction.</p>
	 * @param plantComp 	<p>The FlowJunction accepting visitors.</p>
	 */
	public void visit(FlowJunction plantComp);

	/** 
	 * <p>Visits a PlantComponent of type WetWell.</p>
	 * @param plantComp 	<p>The WetWell accepting visitors.</p>
	 */
	public void visit(WetWell plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Boundary.</p>
	 * @param plantComp 	<p>The Boundary accepting visitors.</p>
	 */
	public void visit(Boundary plantComp);

	/** 
	 * <p>Visits a PlantComponent of type OneInOneOutJunction.</p>
	 * @param plantComp 	<p>The OneInOneOutJunction accepting visitors.</p>
	 */
	public void visit(OneInOneOutJunction plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Turbine.</p>
	 * @param plantComp 	<p>The Turbine accepting visitors.</p>
	 */
	public void visit(Turbine plantComp);

	/** 
	 * <p>Visits a PlantComponent of type IdealPump.</p>
	 * @param plantComp 	<p>The IdealPump accepting visitors.</p>
	 */
	public void visit(IdealPump plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Pump.</p>
	 * @param plantComp 	<p>The Pump accepting visitors.</p>
	 */
	public void visit(Pump plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Valve.</p>
	 * @param plantComp 	<p>The Valve accepting visitors.</p>
	 */
	public void visit(Valve plantComp);

	/** 
	 * <p>Visits a PlantComponent of type PipeToPipeJunction.</p>
	 * @param plantComp 	<p>The PipeToPipeJunction accepting visitors.</p>
	 */
	public void visit(PipeToPipeJunction plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Inlet.</p>
	 * @param plantComp 	<p>The Inlet accepting visitors.</p>
	 */
	public void visit(Inlet plantComp);

	/** 
	 * <p>Visits a PlantComponent of type MassFlowInlet.</p>
	 * @param plantComp 	<p>The MassFlowInlet accepting visitors.</p>
	 */
	public void visit(MassFlowInlet plantComp);

	/** 
	 * <p>Visits a PlantComponent of type SpecifiedDensityAndVelocityInlet.</p>
	 * @param plantComp <p>The SpecifiedDensityAndVelocityInlet accepting visitors.</p>
	 */
	public void visit(SpecifiedDensityAndVelocityInlet plantComp);

	/** 
	 * <p>Visits a PlantComponent of type Outlet.</p>
	 * @param plantComp 	<p>The Outlet accepting visitors.</p>
	 */
	public void visit(Outlet plantComp);

	/** 
	 * <p>Visits a PlantComponent of type SolidWall.</p>
	 * @param plantComp 	<p>The SolidWall accepting visitors.</p>
	 */
	public void visit(SolidWall plantComp);

	/** 
	 * <p>Visits a PlantComponent of type TDM.</p>
	 * @param plantComp 	<p>The TDM accepting visitors.</p>
	 */
	public void visit(TDM plantComp);

	/** 
	 * <p>Visits a PlantComponent of type TimeDependentJunction.</p>
	 * @param plantComp 	<p>The TimeDependentJunction accepting visitors.</p>
	 */
	public void visit(TimeDependentJunction plantComp);

	/** 
	 * <p>Visits a PlantComponent of type TimeDependentVolume.</p>
	 * @param plantComp 	<p>The TimeDependentVolume accepting visitors.</p>
	 */
	public void visit(TimeDependentVolume plantComp);

	/** 
	 * <p>Visits a PlantComponent of type DownComer.</p>
	 * @param plantComp 	<p>The DownComer accepting visitors.</p>
	 */
	public void visit(DownComer plantComp);

	/** 
	 * <p>Visits a PlantComponent of type SeparatorDryer.</p>
	 * @param plantComp 	<p>The SeparatorDryer accepting visitors.</p>
	 */
	public void visit(SeparatorDryer plantComp);
}