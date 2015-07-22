package org.eclipse.ice.viz.service.geometry;

import java.util.ArrayList;

import org.eclipse.ice.viz.service.geometry.IVizUpdateableListener;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "VizObject")
public class VizObject implements IVizUpdateable{
	/**
	 * Logger for handling event messages and other information.
	 */
	@XmlTransient
	protected final Logger logger;	
	
	/**
	 * <p>
	 * The unique identification number of the ICEObject.
	 * </p>
	 * 
	 */
	protected int uniqueId;
	/**
	 * <p>
	 * The name of the ICEObject.
	 * </p>
	 * 
	 */
	protected String objectName;
	/**
	 * <p>
	 * The description of the ICEObject. This description should be different
	 * than the name of the ICEObject and should contain information that would
	 * be useful to a human user.
	 * </p>
	 * 
	 */
	protected String objectDescription;

	/**
	 * <p>
	 * The set of IUpdateableListeners observing the ICEObject.
	 * </p>
	 * 
	 */
	@XmlTransient
	protected ArrayList<IVizUpdateableListener> listeners;

	/**
	 * <p>
	 * The Constructor
	 * </p>
	 * 
	 */
	public VizObject() {

		// Create the logger
		logger = LoggerFactory.getLogger(getClass());
		
		// Set it all up
		uniqueId = 1;
		objectName = "ICE Object";
		objectDescription = "ICE Object";
		listeners = new ArrayList<IVizUpdateableListener>();

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setId(int id)
	 */
	@Override
	public void setId(int id) {

		if (id >= 0) {
			uniqueId = id;
			// Notify the listeners that the object has changed.
			notifyListeners();
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getId()
	 */
	@Override
	@XmlAttribute()
	public int getId() {
		return uniqueId;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setName(String name)
	 */
	@Override
	public void setName(String name) {

		if (name != null) {
			objectName = name;
			// Notify the listeners that the object has changed.
			notifyListeners();
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getName()
	 */
	@Override
	@XmlAttribute()
	public String getName() {
		return objectName;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setDescription(String description)
	 */
	@Override
	public void setDescription(String description) {

		if (description != null) {
			objectDescription = description;
			// Notify the listeners that the object has changed.
			notifyListeners();
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getDescription()
	 */
	@Override
	@XmlAttribute()
	public String getDescription() {
		return objectDescription;
	}

	/**
	 * This operation copies the contents of an ICEObject into the current
	 * object using a deep copy.
	 * 
	 * @param entity The Identifiable entity from which the values should be
	 *            copied.
	 */
	public void copy(VizObject entity) {

		// Return if null
		if (entity == null) {
			return;
		}
		// Copy contents of entity to this ICEObject.
		this.objectDescription = entity.objectDescription;
		this.objectName = entity.objectName;
		this.uniqueId = entity.uniqueId;

	}

	/**
	 * <p>
	 * This protected operation notifies the listeners of the ICEObject that its
	 * state has changed.
	 * </p>
	 * 
	 */
	protected void notifyListeners() {

		// Only process the update if there are listeners
		if (listeners != null && !listeners.isEmpty()) {
			// Create a thread on which to notify the listeners.
			Thread notifierThread = new Thread() {
				@Override
				public void run() {
					// Loop over all listeners and update them
					for (int i = 0; i < listeners.size(); i++) {
						listeners.get(i).update(VizObject.this);
					}
					return;
				}
			};

			// Launch the thread and do the notifications
			notifierThread.start();
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the ICEObject using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Create a new instance of the current object
		VizObject newObject = new VizObject();

		// Copy contents from current object to new object
		newObject.copy(this);

		// return object
		return newObject;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#equals(Object otherObject)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		boolean retVal = false;
		VizObject castedOtherObject = null;

		// Check null and base type first. Note that the instanceof operator
		// must be used because subclasses of ICEObject can be anonymous.
		if (otherObject != null && (otherObject instanceof VizObject)) {
			// See if they are the same reference on the heap
			if (this == otherObject) {
				retVal = true;
			} else {
				castedOtherObject = (VizObject) otherObject;
				// Check each member attribute
				retVal = (this.uniqueId == castedOtherObject.uniqueId)
						&& (this.objectName
								.equals(castedOtherObject.objectName))
						&& (this.objectDescription
								.equals(castedOtherObject.objectDescription));
			}
		}

		return retVal;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#hashCode()
	 */
	@Override
	public int hashCode() {

		// Local Declaration
		int hash = 11;

		// Compute the hashcode from this ICEObject's data
		hash = 31 * hash + uniqueId;
		// If objectName is null, add 0, otherwise add String.hashcode()
		hash = 31 * hash + (null == objectName ? 0 : objectName.hashCode());
		hash = 31
				* hash
				+ (null == objectDescription ? 0 : objectDescription.hashCode());
		// Return the computed hash code
		return hash;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 */
	@Override
	public void update(String updatedKey, String newValue) {

		// Nothing TODO. Subclasses must override this operation for tailored
		// behavior.

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#register(IUpdateableListener listener)
	 */
	@Override
	public void register(IVizUpdateableListener listener) {

		// Register the listener if it is not null
		if (listener != null) {
			listeners.add(listener);
		}
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#unregister(IUpdateableListener listener)
	 */
	@Override
	public void unregister(IVizUpdateableListener listener) {

		// Unregister the listener if it is not null and in the list
		if (listener != null && listeners.contains(listener)) {
			listeners.remove(listener);
		}

		return;
	}
}


