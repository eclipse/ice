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

import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jme3.scene.Node;

// TODO Synchronize this class with the model!

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class provides a basic controller for a component of a mesh.
 * </p>
 * <p>
 * The controller acts as a mediator between the mesh model and its view, which
 * is a wrapper around a jME3 scene component. User input modifies the
 * controller, which passes on the update to the model. When updated, the model
 * notifies the controller, which can then push updates to the view. In order to
 * ensure updates are handled in the jME3 SimpleApplication's simpleUpdate()
 * loop, an AbstractMeshController puts itself into a queue in the
 * SimpleApplication.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class AbstractMeshController implements IUpdateableListener {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The part of the mesh managed by this controller. It needs to implement
	 * IUpdateable (usually by extending ICEObject).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IUpdateable model;

	// ---- Property IDs. ---- //
	/**
	 * The property ID corresponding to {@link #state}.
	 */
	protected static final String stateId = "state";
	/**
	 * The property ID corresponding to {@link #parentNode}.
	 */
	protected static final String parentNodeId = "parentNode";
	/**
	 * The property ID corresponding to {@link #size}.
	 */
	protected static final String sizeId = "size";
	/**
	 * The property ID corresponding to {@link #scale}.
	 */
	protected static final String scaleId = "scale";
	// ----------------------- //

	// ---- Properties that should be reflected in the view. ---- //
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current state of the view supported by the controller.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private StateType state;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The parent jME3 Node for the view associated with this controller.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Node parentNode;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The size or scale of the AbstractMeshView corresponding to this
	 * AbstractMeshController.
	 * <p>
	 * <!-- end-UML-doc -->
	 */
	private float size;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The scale of the AbstractMeshView that is used to determine its location.
	 * <p>
	 * <!-- end-UML-doc -->
	 */
	private float scale;

	private float inverseScale;

	// ---------------------------------------------------------- //

	// ---- Property handlers. ---- //
	/**
	 * This class is for synchronizing specific view properties with the
	 * controller. The idea is that only some changes need to be made to the
	 * view at a given time. Previously, all properties were synced at once
	 * regardless of whether or not they had changed.
	 * 
	 * @author djg
	 * 
	 */
	protected class PropertyHandler {
		/**
		 * Synchronizes the view with a particular property known to the
		 * controller.
		 */
		public void syncView() {
			// Do nothing by default.
		}
	}

	/**
	 * A HashMap of {@link PropertyHandler}s keyed on property ID strings.
	 */
	protected final Map<String, PropertyHandler> properties;
	/**
	 * A thread-safe queue that contains properties that need to be synchronized
	 * with the view.
	 */
	private final ConcurrentLinkedQueue<PropertyHandler> dirtyProperties;
	// ---------------------------- //

	// ---- Structures used for updating. ---- //
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * When the controller needs to update its view, it should append itself to
	 * the queue. The containing SimpleApplication will call the controller's
	 * syncView() method when it can perform the next update.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	// TODO Update the model. This was set to final!
	protected final ConcurrentLinkedQueue<AbstractMeshController> updateQueue;
	/**
	 * Whether or not the view has been synced with this controller. This should
	 * be set to true at the beginning of {@link #syncView()}. It is set to
	 * false only in {@link #setDirtyProperty(String)}.
	 */
	private final AtomicBoolean synced = new AtomicBoolean(true);
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Because all updates to the view *must* be processed in the thread that
	 * executes simpleUpdate() in the SimpleApplication, we need this disposal
	 * flag to signify that the controller is being destroyed. The view will
	 * need to be disposed in the syncView() method.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	protected final AtomicBoolean disposed;

	// --------------------------------------- //

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor. Requires a the model that the controller listens
	 * to and a concurrent queue that the controller will publish to when it
	 * needs to update its view.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param model
	 *            <p>
	 *            The part of the mesh listened to and managed by this
	 *            controller.
	 *            </p>
	 * @param queue
	 *            <p>
	 *            The queue used for updating views handled by this and other
	 *            controllers.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AbstractMeshController(IUpdateable model,
			ConcurrentLinkedQueue<AbstractMeshController> queue) {
		// begin-user-code

		// We can only process the parameters if they are valid.
		if (model != null && queue != null) {
			// Store the model and update queue.
			this.model = model;
			this.updateQueue = queue;

			properties = new HashMap<String, PropertyHandler>();
			// We may want to have a single concurrent queue managed in the
			// mesh application.
			dirtyProperties = new ConcurrentLinkedQueue<PropertyHandler>();

			// Initialize the state and parentNode.
			state = StateType.None;
			parentNode = null;

			// Initialize the remaining class variables.
			size = 1f;
			scale = 1f;
			inverseScale = 1f;
			disposed = new AtomicBoolean(false);

			// ---- Set up the default property handlers. ---- //
			// None of these have any pre-defined action.
			properties.put(stateId, new PropertyHandler());
			properties.put(parentNodeId, new PropertyHandler());
			properties.put(sizeId, new PropertyHandler());
			properties.put(scaleId, new PropertyHandler());
			// ----------------------------------------------- //

			// Register with the model.
			model.register(this);
		} else {
			throw new IllegalArgumentException(
					"AbstractMeshController error: Input arguments cannot be null!");
		}

		return;
		// end-user-code
	}

	/**
	 * Marks the property with the specified ID as dirty. To do this, it fetches
	 * the configured {@link PropertyHandler} for the ID and adds it to
	 * {@link #dirtyProperties}. If the controller and its view are already
	 * synced, then the controller becomes out of sync and is added to the
	 * {@link #updateQueue}.
	 * 
	 * @param id
	 *            The ID of the property that has changed.
	 */
	protected final void setDirtyProperty(String id) {
		PropertyHandler handler = properties.get(id);
		if (handler != null) {
			dirtyProperties.add(handler);
			if (synced.getAndSet(false)) {
				updateQueue.add(this);
			}
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Synchronizes the view managed by the AbstractMeshController with its
	 * associated model. This method should be implemented by subclasses to
	 * properly dispose of resources using the application's simpleUpdate()
	 * thread and to sync the view with any properties not marked as dirty with
	 * {@link #setDirtyProperty(String)}. However, this method should still be
	 * called by subclasses so that dirty properties are updated properly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void syncView() {
		// Marked the controller as synced. Any updates that occur during this
		// call may require an additional update.
		synced.set(true);

		// If the controller is not disposed, update the view according to all
		// changed properties.
		if (!disposed.get()) {
			PropertyHandler handler;
			while ((handler = dirtyProperties.poll()) != null) {
				handler.syncView();
			}
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the current state of the Mesh object managed by the
	 * AbstractMeshController.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The current state of the view supported by the controller.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public StateType getState() {
		// begin-user-code
		return state;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the current state of the Mesh object managed by the
	 * AbstractMeshController.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param state
	 *            <p>
	 *            The new state of the view supported by the controller.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setState(StateType state) {
		// begin-user-code

		// Check the parameter. Do not accept null states, and do not do
		// anything if the state hasn't changed.
		if (state != null && !state.equals(this.state)) {
			this.state = state;

			// We will need to update the view accordingly.
			setDirtyProperty(stateId);
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the current parent jME3 Node for the view managed by this
	 * AbstractMeshController.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The current parent jME3 Node for the view associated with this
	 *         controller.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Node getParentNode() {
		// begin-user-code
		return parentNode;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the parent jME3 Node for the view managed by this
	 * AbstractMeshController.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param node
	 *            <p>
	 *            The new parent jME3 Node for the view associated with this
	 *            controller.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void setParentNode(Node node) {
		// begin-user-code

		// Check the parameter. Do not accept null states, and do not do
		// anything if the parent node hasn't changed.
		if (node != null && node != parentNode) {
			this.parentNode = node;

			// We will need to update the view accordingly.
			setDirtyProperty(parentNodeId);
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Disposes of the AbstractMeshController and its associated view.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void dispose() {
		// begin-user-code

		if (disposed.compareAndSet(false, true)) {
			// Unregister from the model.
			model.unregister(this);

			// We will need to update the view appropriately (delete it). This
			// must be done in the syncView() method.
			updateQueue.add(this);
		}

		return;
		// end-user-code
	}

	// TODO Add to model.
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the current size of the mesh controller's view.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The size of the mesh controller's view.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public float getSize() {
		return size;
	}

	// TODO Add to model.
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the current size of the mesh controller's view.
	 * <p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            The new size of the mesh controller's view.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSize(float size) {
		if (size > 0f) {
			this.size = size;

			// We will need to update the view accordingly.
			setDirtyProperty(sizeId);
		}
		return;
	}

	// TODO Add to model.
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the current scale of the mesh controller's view.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The scale of the mesh controller's view.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public float getScale() {
		return scale;
	}

	public float getInverseScale() {
		return inverseScale;
	}

	// TODO Add to model.
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the current scale of the mesh controller's view.
	 * <p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            The new scale of the mesh controller's view.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setScale(float scale) {
		if (scale > 0f) {
			this.scale = scale;
			this.inverseScale = (1f / scale);

			// We will need to update the view accordingly.
			setDirtyProperty(scaleId);
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this
	 * AbstractMeshController and another AbstractMeshController. It returns
	 * true if the objects are equal and false if they are not.
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

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null
				&& otherObject instanceof AbstractMeshController) {

			// We can now cast the other object.
			AbstractMeshController controller = (AbstractMeshController) otherObject;

			// Compare the values between the two objects.
			equals = (model.equals(controller.model)
					&& state.equals(controller.state)
					// Just check the reference on the jME3 node.
					&& parentNode == controller.parentNode
					&& Float.floatToIntBits(size) == Float
							.floatToIntBits(controller.size) && Float
					.floatToIntBits(scale) == Float
					.floatToIntBits(controller.scale));
			// The queue is not really managed by this controller.
		}

		return equals;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the AbstractMeshController.
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

		// Static hash.
		int hash = 11;

		// Add local hashes.
		hash = 31 * hash + model.hashCode();
		hash = 31 * hash + state.hashCode();
		hash = 31 * hash + (parentNode != null ? parentNode.hashCode() : 0);
		hash = 31 * hash + Float.floatToIntBits(size);
		hash = 31 * hash + Float.floatToIntBits(scale);
		// The queue is not really managed by this controller.

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a AbstractMeshController into the
	 * current object using a deep copy.
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

		// Check the parameter.
		if (controller != null) {

			// The updateQueue should already have been passed in via the
			// constructor.

			// We should not clone the model, just update our reference to it.
			model = controller.model;

			// We should not clone the parent node, just update our reference.
			parentNode = controller.parentNode;

			// Copy everything else.
			state = controller.state;
			size = controller.size;
			scale = controller.scale;

			// We will need to update the view accordingly.
			updateQueue.add(this);
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the AbstractMeshController using a deep
	 * copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public abstract Object clone();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Implements the method from the IUpdateableListener interface. If this
	 * method is called, the IUpdateable the controller has registered with has
	 * been changed. This method adds the controller to the update queue to have
	 * its view synced by the MeshApplication.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(IUpdateable component) {
		// begin-user-code

		// If the component matches the model managed by this controller, add
		// the controller to the MeshApplication's updateQueue. This will
		// eventually result in the syncView method's being called.
		if (component == model) {
			updateQueue.add(this);
		}
		return;
		// end-user-code
	}
}