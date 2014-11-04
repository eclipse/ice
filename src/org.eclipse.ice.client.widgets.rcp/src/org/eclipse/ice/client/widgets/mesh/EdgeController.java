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
package org.eclipse.ice.client.widgets.mesh;

import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class provides a controller for Edge instances, and it creates an
 * EdgeView for an Edge. It also listens for changes to its Edge, at which point
 * it updates its EdgeView. It can also send updates to the Edge model based on
 * user input.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EdgeController extends AbstractMeshController {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The EdgeView managed by this controller.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EdgeView view;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Edge managed by this controller.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Edge model;

	// ---- Property IDs. ---- //
	/**
	 * The location of the edge's end points.
	 */
	private static final String locationId = "location";

	// ----------------------- //

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edge
	 *            <p>
	 *            The Edge managed by this controller.
	 *            </p>
	 * @param queue
	 *            <p>
	 *            The queue used for updating views handled by this and other
	 *            controllers.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public EdgeController(Edge edge,
			ConcurrentLinkedQueue<AbstractMeshController> queue,
			Material material) {
		// begin-user-code
		super(edge, queue);

		// Store a reference to the model.
		model = edge;

		// Create a view.
		view = new EdgeView(Integer.toString(model.getId()), material);

		// ---- Set up the property handlers to sync the view. ---- //
		properties.put(stateId, new PropertyHandler() {
			public void syncView() {
				view.setColor(getState().getColor());
			}
		});
		properties.put(parentNodeId, new PropertyHandler() {
			public void syncView() {
				view.setParentNode(getParentNode());
			}
		});
		properties.put(sizeId, new PropertyHandler() {
			public void syncView() {
				view.setSize(getSize());
			}
		});
		// The scale and location properties both affect the location of the
		// vertex view.
		PropertyHandler locationHandler = new PropertyHandler() {
			public void syncView() {
				float[] start = model.getStartLocation();
				float[] end = model.getEndLocation();
				Vector3f startVector = new Vector3f(start[0], start[1],
						start[2]);
				Vector3f endVector = new Vector3f(end[0], end[1], end[2]);
				float scale = getInverseScale();
				view.setLine(startVector.multLocal(scale),
						endVector.multLocal(scale));
			}
		};
		properties.put(scaleId, locationHandler);
		properties.put(locationId, locationHandler);
		// -------------------------------------------------------- //

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this Edge and another
	 * Edge. It returns true if the objects are equal and false if they are not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the objects are equal, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the Edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashcode of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		// TODO Auto-generated method stub
		return 0;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a Edge into the current object
	 * using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param controller
	 *            <p>
	 *            The object from which the values should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(AbstractMeshController controller) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the Edge using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Initialize a new object.
		EdgeController object = new EdgeController((Edge) model.clone(),
				updateQueue, view.geometry.getMaterial());

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see AbstractMeshController#syncView()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void syncView() {
		// begin-user-code
		super.syncView();

		// If necessary, dispose of the view.
		if (disposed.get()) {
			view.dispose();
		}

		return;
		// end-user-code
	}

	/**
	 * Overrides the default behavior to make sure the appropriate properties
	 * that derive directly from the model get marked as dirty.
	 */
	@Override
	public void update(IUpdateable component) {
		// Update the location.
		setDirtyProperty(locationId);
	}
}