/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor;

import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;

/**
 * <p>
 * This interface defines the "visitation" routines that LWRComponents and
 * subclasses may use to reveal their types to visitors.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public interface ILWRComponentVisitor {
	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a PWReactor.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The PWReactor that must accept this visitor
	 *            </p>
	 */
	public void visit(PressurizedWaterReactor lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a BWReactor.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The BWReactor that must accept this visitor
	 *            </p>
	 */
	public void visit(BWReactor lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a FuelAssembly.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The FuelAssembly that must accept this visitor
	 *            </p>
	 */
	public void visit(FuelAssembly lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a RodClusterAssembly.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The RCA that must accept this visitor
	 *            </p>
	 */
	public void visit(RodClusterAssembly lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a LWRRod.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The LWRRod that must accept this visitor
	 *            </p>
	 */
	public void visit(LWRRod lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a ControlBank.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The ControlBank that must accept this visitor
	 *            </p>
	 */
	public void visit(ControlBank lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as an IncoreInstrument.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The IncoreInstrument that must accept this visitor
	 *            </p>
	 */
	public void visit(IncoreInstrument lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a Ring.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The Ring that must accept this visitor
	 *            </p>
	 */
	public void visit(Tube lwrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a Tube.
	 * </p>
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The Tube that must accept this visitor
	 *            </p>
	 */
	public void visit(Ring lwrComp);
}