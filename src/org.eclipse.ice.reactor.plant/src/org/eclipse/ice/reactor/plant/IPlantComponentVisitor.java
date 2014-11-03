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
 * <!-- begin-UML-doc -->
 * <p>Defines the visitation routine for PlantComponents so that the type of a PlantComponent can be revealed.</p>
 * <!-- end-UML-doc -->
 * @author w5q
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IPlantComponentVisitor {
	
	public void visit(PlantComposite plantComp);
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type GeometricalComponent.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The GeometricalComponent accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(GeometricalComponent plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Junction.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Junction accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Junction plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Reactor.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Reactor accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Reactor plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type PointKinetics.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The PointKinetics accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PointKinetics plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type HeatExchanger.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The HeatExchanger accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(HeatExchanger plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Pipe.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Pipe accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Pipe plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type CoreChannel.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The CoreChannel accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(CoreChannel plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type GeometricalComponent.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Subchannel accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Subchannel plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type PipeWithHeatStructure.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The PipeWithHeatStructure accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PipeWithHeatStructure plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Branch.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Branch accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Branch plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type SubchannelBranch.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The SubchannelBranch accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SubchannelBranch plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type VolumeBranch.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The VolumeBranch accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(VolumeBranch plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type FlowJunction.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The FlowJunction accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(FlowJunction plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type WetWell.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The WetWell accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(WetWell plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Boundary.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Boundary accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Boundary plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type OneInOneOutJunction.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The OneInOneOutJunction accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(OneInOneOutJunction plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Turbine.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Turbine accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Turbine plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type IdealPump.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The IdealPump accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IdealPump plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Pump.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Pump accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Pump plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Valve.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Valve accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Valve plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type PipeToPipeJunction.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The PipeToPipeJunction accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PipeToPipeJunction plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Inlet.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Inlet accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Inlet plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type MassFlowInlet.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The MassFlowInlet accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MassFlowInlet plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type SpecifiedDensityAndVelocityInlet.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp <p>The SpecifiedDensityAndVelocityInlet accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SpecifiedDensityAndVelocityInlet plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type Outlet.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The Outlet accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Outlet plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type SolidWall.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The SolidWall accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SolidWall plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type TDM.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The TDM accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TDM plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type TimeDependentJunction.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The TimeDependentJunction accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TimeDependentJunction plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type TimeDependentVolume.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The TimeDependentVolume accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TimeDependentVolume plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type DownComer.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The DownComer accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(DownComer plantComp);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Visits a PlantComponent of type SeparatorDryer.</p>
	 * <!-- end-UML-doc -->
	 * @param plantComp 	<p>The SeparatorDryer accepting visitors.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SeparatorDryer plantComp);
}