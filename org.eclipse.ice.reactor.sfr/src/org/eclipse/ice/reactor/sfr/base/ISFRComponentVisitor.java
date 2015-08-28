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
package org.eclipse.ice.reactor.sfr.base;

import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;

/**
 * <p>
 * This interface defines the "visitation" routines that SFRComponents and
 * subclasses may use to reveal their types to visitors.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public interface ISFRComponentVisitor {
	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFReactor.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The SFReactor accepting the visitor.
	 */
	public void visit(SFReactor sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFRAssembly.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The SFRAssembly being visited.
	 */
	public void visit(SFRAssembly sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a PinAssembly.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The PinAssembly accepting the visitor.
	 */
	public void visit(PinAssembly sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a ReflectorAssembly.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The ReflectorAssembly accepting the visitor.
	 */
	public void visit(ReflectorAssembly sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFRPin.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The SFRPin accepting the visitor.
	 */
	public void visit(SFRPin sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFRRod.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The SFRRod accepting the visitor.
	 */
	public void visit(SFRRod sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a MaterialBlock.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The MaterialBlock accepting the visitor.
	 */
	public void visit(MaterialBlock sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a Material.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The Material accepting the visitor.
	 */
	public void visit(Material sfrComp);

	/**
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a Ring.
	 * </p>
	 * 
	 * @param sfrComp
	 *            The Ring accepting the visitor.
	 */
	public void visit(Ring sfrComp);
}