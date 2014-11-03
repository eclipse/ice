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
 * <!-- begin-UML-doc -->
 * <p>
 * This interface defines the "visitation" routines that SFRComponents and
 * subclasses may use to reveal their types to visitors.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface ISFRComponentVisitor {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFReactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The SFReactor accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SFReactor sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFRAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The SFRAssembly being visited.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SFRAssembly sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a PinAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The PinAssembly accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PinAssembly sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a ReflectorAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The ReflectorAssembly accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(ReflectorAssembly sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFRPin.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The SFRPin accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SFRPin sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a SFRRod.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The SFRRod accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(SFRRod sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a MaterialBlock.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The MaterialBlock accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MaterialBlock sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a Material.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The Material accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Material sfrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * SFRComponent as a Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sfrComp
	 *            The Ring accepting the visitor.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Ring sfrComp);
}