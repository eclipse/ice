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
 * <!-- begin-UML-doc -->
 * <p>
 * This interface defines the "visitation" routines that LWRComponents and
 * subclasses may use to reveal their types to visitors.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface ILWRComponentVisitor {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a PWReactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The PWReactor that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PressurizedWaterReactor lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a BWReactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The BWReactor that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(BWReactor lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a FuelAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The FuelAssembly that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(FuelAssembly lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a RodClusterAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The RCA that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(RodClusterAssembly lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a LWRRod.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The LWRRod that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(LWRRod lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a ControlBank.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The ControlBank that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(ControlBank lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as an IncoreInstrument.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The IncoreInstrument that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IncoreInstrument lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The Ring that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Tube lwrComp);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs a visitor to perform its actions on the
	 * LWRComponent as a Tube.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lwrComp
	 *            <p>
	 *            The Tube that must accept this visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Ring lwrComp);
}