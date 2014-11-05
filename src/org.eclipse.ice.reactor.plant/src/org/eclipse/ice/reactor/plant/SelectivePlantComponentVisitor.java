package org.eclipse.ice.reactor.plant;


/**
 * This adapter class provides default implementations for the methods described
 * by the {@link IPlantComponentVisitor} interface. The default behavior for
 * each method is to do nothing.
 * <p>
 * Classes that wish to deal with visit operations for specific
 * <code>PlantComponent</code>s should extend or instantiate this class and
 * override only the methods which they are interested in.
 * </p>
 * <p>
 * For example, if you have a <code>PlantComponent comp</code> and would like to
 * perform a special action if it is a <code>Pipe</code>, you would do the
 * following:
 * 
 * <pre>
 * <code>IPlantComponentVisitor visitor = new SelectivePlantComponentVisitor() {
 *     {@literal @}Override
 *     public void visit(Pipe pipe) {
 *         // Do something unique for Pipes...
 *     }
 * };
 * visitor.visit(comp);
 * </code>
 * </pre>
 * 
 * </p>
 * 
 * @see IPlantComponentVisitor
 */
public abstract class SelectivePlantComponentVisitor implements
		IPlantComponentVisitor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.PlantComposite)
	 */
	@Override
	public void visit(PlantComposite plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.GeometricalComponent)
	 */
	@Override
	public void visit(GeometricalComponent plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Junction)
	 */
	@Override
	public void visit(Junction plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Reactor)
	 */
	@Override
	public void visit(Reactor plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.PointKinetics)
	 */
	@Override
	public void visit(PointKinetics plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.HeatExchanger)
	 */
	@Override
	public void visit(HeatExchanger plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Pipe)
	 */
	@Override
	public void visit(Pipe plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.CoreChannel)
	 */
	@Override
	public void visit(CoreChannel plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Subchannel)
	 */
	@Override
	public void visit(Subchannel plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.PipeWithHeatStructure)
	 */
	@Override
	public void visit(PipeWithHeatStructure plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Branch)
	 */
	@Override
	public void visit(Branch plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.SubchannelBranch)
	 */
	@Override
	public void visit(SubchannelBranch plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.VolumeBranch)
	 */
	@Override
	public void visit(VolumeBranch plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.FlowJunction)
	 */
	@Override
	public void visit(FlowJunction plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.WetWell)
	 */
	@Override
	public void visit(WetWell plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Boundary)
	 */
	@Override
	public void visit(Boundary plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.OneInOneOutJunction)
	 */
	@Override
	public void visit(OneInOneOutJunction plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Turbine)
	 */
	@Override
	public void visit(Turbine plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.IdealPump)
	 */
	@Override
	public void visit(IdealPump plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Pump)
	 */
	@Override
	public void visit(Pump plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Valve)
	 */
	@Override
	public void visit(Valve plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.PipeToPipeJunction)
	 */
	@Override
	public void visit(PipeToPipeJunction plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Inlet)
	 */
	@Override
	public void visit(Inlet plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.MassFlowInlet)
	 */
	@Override
	public void visit(MassFlowInlet plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.SpecifiedDensityAndVelocityInlet)
	 */
	@Override
	public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.Outlet)
	 */
	@Override
	public void visit(Outlet plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.SolidWall)
	 */
	@Override
	public void visit(SolidWall plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.TDM)
	 */
	@Override
	public void visit(TDM plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.TimeDependentJunction)
	 */
	@Override
	public void visit(TimeDependentJunction plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.TimeDependentVolume)
	 */
	@Override
	public void visit(TimeDependentVolume plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.DownComer)
	 */
	@Override
	public void visit(DownComer plantComp) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantComponentVisitor#visit(org.eclipse
	 * .ice.reactor.plant.SeparatorDryer)
	 */
	@Override
	public void visit(SeparatorDryer plantComp) {
		// Do nothing.
	}

}
