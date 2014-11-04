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

import org.eclipse.ice.datastructures.form.mesh.Vertex;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class provides a controller for Vertex instances, and it creates a
 * VertexView for a Vertex. It also listens for changes to its Vertex, at which
 * point it updates its VertexView. It can also send updates to the Vertex model
 * based on user input.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VertexController extends AbstractMeshController {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The VertexView managed by this controller.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private VertexView view;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Vertex managed by this controller.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Vertex model;

	// ---- Property IDs. ---- //
	/**
	 * The location of the vertex.
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
	 * @param vertex
	 *            <p>
	 *            The Vertex managed by this controller.
	 *            </p>
	 * @param queue
	 *            <p>
	 *            The queue used for updating views handled by this and other
	 *            controllers.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The jME3 Material that should be used for the vertex graphics.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	// TODO Update documentation in model. It has the jME3 material now!
	public VertexController(Vertex vertex,
			ConcurrentLinkedQueue<AbstractMeshController> queue,
			Material material) {
		// begin-user-code
		super(vertex, queue);

		// Store a reference to the model.
		model = vertex;

		// Create a new view.
		view = new VertexView(Integer.toString(model.getId()), material);

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
				view.setLocation(getLocation().multLocal(getInverseScale()));
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
	 * Gets the current location from the model.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The current location of the Vertex.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Vector3f getLocation() {
		// begin-user-code
		float[] loc = model.getLocation();
		return new Vector3f(loc[0], loc[1], loc[2]);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the current location in the model.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            <p>
	 *            The new location of the Vertex.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLocation(Vector3f location) {
		// begin-user-code
		model.setLocation(location.x, location.y, location.z);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this VertexController
	 * and another VertexController. It returns true if the objects are equal
	 * and false if they are not.
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
	@Override
	public boolean equals(Object otherObject) {
		// begin-user-code

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof VertexController) {

			// We can now cast the other object.
			VertexController controller = (VertexController) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && view.equals(controller.view));

			// The model is already handled by AbstractMeshController.
		}

		return equals;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the VertexController.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashcode of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public int hashCode() {
		// begin-user-code

		// Static hash.
		int hash = super.hashCode();

		// Add local hashes.
		hash = 31 * hash + view.hashCode();
		// The model is already handled by AbstractMeshController.

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a VertexController into the current
	 * object using a deep copy.
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
	public void copy(VertexController controller) {
		// begin-user-code

		// Check the parameter.
		if (controller != null) {

			// Perform the super copy operation.
			super.copy(controller);

			// The super copy method handles the queue and its own private
			// variables.

			// We need to copy the reference to the Vertex model. The super copy
			// only copies the reference to an IUpdateable.
			model = controller.model;

			// Dispose of the current view.
			view.dispose();

			// Clone the view.
			view = (VertexView) controller.view.clone();

			// We will need to update the view accordingly.
			updateQueue.add(this);
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the VertexController using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public Object clone() {
		// begin-user-code

		// Initialize a new object.
		VertexController object = new VertexController((Vertex) model.clone(),
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