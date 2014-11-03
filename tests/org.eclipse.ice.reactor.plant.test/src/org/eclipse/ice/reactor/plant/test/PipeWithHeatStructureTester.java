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
package org.eclipse.ice.reactor.plant.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.eclipse.ice.datastructures.updateableComposite.Component;
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
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PipeWithHeatStructureTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Boolean flag to mark if the PlantComponent was successfully visited.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean wasVisited = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the getting and setting of the component's attributes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkProperties() {
		// begin-user-code

		// Super's tester takes care of this.

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the hashCode and equality methods of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkEquality() {
		// begin-user-code

		// Super's tester takes care of this.

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the construction of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		/* ---- Check nullary constructor ---- */

		// Create a new component.
		PipeWithHeatStructure component = new PipeWithHeatStructure();

		// Check the default values.
		assertEquals("Pipe With Heat Structure 1", component.getName());
		assertEquals("Pipe with heat structure component for reactors",
				component.getDescription());
		assertEquals(1, component.getId());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the copy and clone methods of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Construct a base component to copy from.
		PipeWithHeatStructure component = new PipeWithHeatStructure();

		/* ---- Check copying ---- */

		// Super's tester takes care of this.

		/* ---- Check cloning ---- */

		// Get a clone of the original component.
		Object objectClone = component.clone();

		// Make sure it's not null!
		assertNotNull(objectClone);

		// Make sure the reference is different but the contents are equal.
		assertFalse(component == objectClone);
		assertTrue(component.equals(objectClone));

		// Make sure the object is an instance of PipeWithHeatStructuer.
		assertTrue(objectClone instanceof PipeWithHeatStructure);

		// Cast the component.
		PipeWithHeatStructure componentClone = (PipeWithHeatStructure) component
				.clone();

		// Check the components one last time for good measure.
		assertFalse(component == componentClone);
		assertTrue(component.equals(componentClone));

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks for persistence in the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkPersistence() {
		// begin-user-code
		// Do nothing.

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the visitation routine of the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkVisitation() {
		// begin-user-code

		// Create a new component to visit.
		PipeWithHeatStructure component = new PipeWithHeatStructure();

		// Create an invalid visitor, and try to visit the component.
		FakeComponentVisitor visitor = null;
		component.accept(visitor);

		// Check that the component wasn't visited yet.
		assertFalse(wasVisited);

		// Create a valid visitor, and try to visit the component.
		visitor = new FakeComponentVisitor();
		component.accept(visitor);

		// Check that the component was visited.
		assertTrue(wasVisited);

		// Grab the visitor's visited component.
		Component visitorComponent = visitor.component;

		// Check that the visitor's component is the same component we initially
		// created.
		assertTrue(component == visitorComponent);
		assertTrue(component.equals(visitorComponent));

		return;
		// end-user-code
	}

	private class FakeComponentVisitor implements IPlantComponentVisitor {

		// The fake visitor's visited component.
		private PipeWithHeatStructure component = null;

		public void visit(PipeWithHeatStructure plantComp) {

			// Set the IPlantComponentVisitor component (if valid), and flag the
			// component as having been visited.
			if (plantComp != null) {
				this.component = plantComp;
				wasVisited = true;
			}
			return;
		}

		public void visit(PlantComposite plantComp) {
			// Do nothing.
		}

		public void visit(Junction plantComp) {
			// Do nothing.

		}

		public void visit(Reactor plantComp) {
			// Do nothing.

		}

		public void visit(PointKinetics plantComp) {
			// Do nothing.

		}

		public void visit(HeatExchanger plantComp) {
			// Do nothing.

		}

		public void visit(Pipe plantComp) {
			// Do nothing.

		}

		public void visit(Subchannel plantComp) {
			// Do nothing.

		}

		public void visit(CoreChannel plantComp) {
			// Do nothing.

		}

		public void visit(Branch plantComp) {
			// Do nothing.

		}

		public void visit(SubchannelBranch plantComp) {
			// Do nothing.

		}

		public void visit(VolumeBranch plantComp) {
			// Do nothing.

		}

		public void visit(FlowJunction plantComp) {
			// Do nothing.

		}

		public void visit(WetWell plantComp) {
			// Do nothing.

		}

		public void visit(Boundary plantComp) {
			// Do nothing.

		}

		public void visit(OneInOneOutJunction plantComp) {
			// Do nothing.

		}

		public void visit(Turbine plantComp) {
			// Do nothing.

		}

		public void visit(IdealPump plantComp) {
			// Do nothing.

		}

		public void visit(Pump plantComp) {
			// Do nothing.

		}

		public void visit(Valve plantComp) {
			// Do nothing.

		}

		public void visit(PipeToPipeJunction plantComp) {
			// Do nothing.

		}

		public void visit(Inlet plantComp) {
			// Do nothing.

		}

		public void visit(MassFlowInlet plantComp) {
			// Do nothing.

		}

		public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
			// Do nothing.

		}

		public void visit(Outlet plantComp) {
			// Do nothing.

		}

		public void visit(SolidWall plantComp) {
			// Do nothing.

		}

		public void visit(TDM plantComp) {
			// Do nothing.

		}

		public void visit(TimeDependentJunction plantComp) {
			// Do nothing.

		}

		public void visit(TimeDependentVolume plantComp) {
			// Do nothing.

		}

		public void visit(DownComer plantComp) {
			// Do nothing.

		}

		public void visit(SeparatorDryer plantComp) {
			// Do nothing.

		}

		public void visit(GeometricalComponent plantComp) {
			// Do nothing.

		}

	};
}