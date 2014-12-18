package org.eclipse.ice.datastructures.form;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.datastructures.updateableComposite.Composite;

/**
 * ListComposite allows putting multiple components into an ICEObject
 * that wraps an ArrayList of Components.
 * 
 * @author Andrew Bennett
 *
 */
public class ListComposite extends ICEObject implements Composite {
	
	/* A list of the components read in */
	private ArrayList<Component> componentList;
	
	/**
	 * Default Constructor just creates the componentList
	 */
	public ListComposite() {
		componentList = new ArrayList<Component>();
	}

	/**
	 * A constructor that can be passed an existing list
	 * 
	 * @param list: the ArrayList of Components read in
	 */
	public ListComposite(ArrayList<Component> list) {
		componentList = list;
	}
	
	/**
	 * Add a new component to the list
	 * 
	 * @param child: The Component to add
	 */
	@Override
	public void addComponent(Component child) {
		componentList.add(child);
	}

	/**
	 * Removes a Component from the list given it's index exists
	 * 
	 * @param childId: The index of the Component to delete
	 */
	@Override
	public void removeComponent(int childId) {
		if (childId < componentList.size()) {
			componentList.remove(childId);
		}
	}

	/**
	 * Get a Component from the list given it's index exists
	 * 
	 * @param childId: The index of the Component to get
	 */
	@Override
	public Component getComponent(int childId) {
		Component comp = null;
		if (childId < componentList.size()) {
			comp = componentList.get(childId);
		}
		return comp;
	}

	/**
	 * Returns the number of Components stored in the list
	 * 
	 * @return the size of the list
	 */
	@Override
	public int getNumberOfComponents() {
		return componentList.size();
	}

	/**
	 * Returns an ArrayList of all of the Components that
	 * have been put into the IPSComposite.
	 * 
	 * @return the ArrayList of Components
	 */
	@Override
	public ArrayList<Component> getComponents() {
		return componentList;
	}
	
	/**
	 * TODO: Currently does nothing.
	 */
	@Override
	public void accept(IComponentVisitor visitor) {
		// TODO Auto-generated method stub
	}
}