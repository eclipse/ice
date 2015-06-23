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
package org.eclipse.ice.reactor.sfr.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;

/**
 * <p>
 * The MaterialBlock class is a generalized class containing a set of concentric
 * and/or radial collection of Rings that constitute the circular structure(s)
 * of SFRPins (in the case of a fuel or control assemblies), and SFRRods (in the
 * case of reflector assemblies).
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class MaterialBlock extends SFRComponent implements
		Comparable<MaterialBlock> {

	/**
	 * <p>
	 * The vertical position of the MaterialBlock from the bottom of the SFRPin
	 * (z-displacement where z=0 at bottom).
	 * </p>
	 * 
	 */
	private double vertPosition;
	/**
	 * <!-- begin-UML-doc --> The collection of rings contained within each
	 * MaterialBlock. A TreeSet structure is used so that rings are sorted in
	 * ascending order of radii. Rings cannot overlap (physically impossible).
	 * 
	 */
	private TreeSet<Ring> rings;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public MaterialBlock() {

		// Initialize a default name, description, and ID
		setName("MaterialBlock 1");
		setDescription("MaterialBlock 1 Description");
		setId(1);

		// Initialize the z-displacement as zero
		vertPosition = 0.0;

		// Create an empty TreeSet of rings
		rings = new TreeSet<Ring>();

	}

	/**
	 * <p>
	 * Set the vertical position (z-displacement) of the material block, where
	 * z=0 at the bottom end of the structure.
	 * </p>
	 * 
	 * @param vertPosition
	 *            The vertical position (z-displacement) of the material block.
	 *            Must be non-negative.
	 */
	public void setVertPosition(double vertPosition) {

		// Vertical position (z-displacement) must be non-negative to set
		if (vertPosition >= 0.0) {
			this.vertPosition = vertPosition;
		}

	}

	/**
	 * <p>
	 * Returns the vertical position (z-displacement) of the material block as a
	 * double, where z=0 at the bottom end of the structure.
	 * </p>
	 * 
	 * @return The vertical position (z-displacement) of the material block.
	 */
	public double getVertPosition() {

		// Return the vertical position (z-displacement)
		return vertPosition;
	}

	/**
	 * <p>
	 * Adds a ring object to the current collection of rings; returns true if
	 * the operation was successful.
	 * </p>
	 * 
	 * @param ring
	 *            The ring to be added to the material block.
	 * @return Returns true if the addition was successful, otherwise false.
	 */
	public boolean addRing(Ring ring) {

		// Check if the ring is null first
		if (ring == null) {
			return false;
		}
		// Check if ring to be added overlaps with any rings in current TreeSet.
		// To do so, iterate through the TreeSet, comparing rings.

		// Initialize Iterator of rings TreeSet
		Iterator<Ring> i = rings.iterator();

		// Create a cursor ring to traverse the TreeSet
		Ring currRing;

		// Iterate the TreeSet as long as there's another element after it
		while (i.hasNext()) {

			// Get the next ring
			currRing = i.next();

			// Check if the rings overlap at all
			if (currRing.compareTo(ring) == 0) {

				// If they overlap, don't add ring to the TreeSet
				return false;
			}
		}

		// Otherwise we can add it to the TreeSet and return true
		rings.add(ring);
		return true;

	}

	/**
	 * <p>
	 * Removes the specified ring from the collection of rings; returns true if
	 * the operation was successful.
	 * </p>
	 * 
	 * @param name
	 *            The name of the ring to be removed from the material block.
	 * @return Returns true if the removal was successful, otherwise false.
	 */
	public boolean removeRing(String name) {

		// Check that the ring name is valid; if not, return null
		if (name == null) {
			return false;
		}
		// Initialize an iterator to traverse through the rings TreeSet
		Iterator<Ring> i = rings.iterator();

		// Create a cursor ring to traverse the TreeSet
		Ring currRing;

		// Iterate the TreeSet as long as there's another element after it
		while (i.hasNext()) {

			// Get the next ring
			currRing = i.next();

			// Check if the ring names are the same
			if (currRing.getName().equals(name)) {

				// If names match, remove the ring and return true
				rings.remove(currRing);
				return true;
			}
		}

		// Otherwise no ring with the specified name was found; couldn't remove
		// anything so return false
		return false;

	}

	/**
	 * <p>
	 * Returns the ring that contains the specified radius in between the
	 * rings's inner and outer radii, otherwise returns null.
	 * </p>
	 * 
	 * @param radius
	 *            The radius which will be searched for in the collection of
	 *            rings.
	 * @return Returns the ring that contains the specified radius. Returns null
	 *         if no ring was found.
	 */
	public Ring getRing(double radius) {

		// Check that the ring radius is valid; if not, return null
		if (radius < 0) {
			return null;
		}
		// Initialize an iterator to traverse through the rings TreeSet
		Iterator<Ring> i = rings.iterator();

		// Create a cursor ring to traverse the TreeSet
		Ring currRing;

		// Iterate the TreeSet as long as there's another element after it
		while (i.hasNext()) {

			// Get the next ring
			currRing = i.next();

			// Check if the specified radius falls within the inner and outer
			// radii of any rings contained in the TreeSet
			if (radius >= currRing.getInnerRadius()
					&& radius <= currRing.getOuterRadius()) {

				// If the radius is between the radii of the current ring
				return currRing;
			}
		}

		// Otherwise no ring with the specified radius was found, return a null
		return null;

	}

	/**
	 * <p>
	 * Returns the ring of the specified name if it exists, otherwise returns
	 * null.
	 * </p>
	 * 
	 * @param name
	 *            The name of the ring being searched for.
	 * @return Returns the ring with the specified name. Returns null if no ring
	 *         was found.
	 */
	public Ring getRing(String name) {

		// Check that the ring name is valid; if not, return null
		if (name == null) {
			return null;
		}
		// Initialize an iterator to traverse through the rings TreeSet
		Iterator<Ring> i = rings.iterator();

		// Create a cursor ring to traverse the TreeSet
		Ring currRing;

		// Iterate the TreeSet as long as there's another element after it
		while (i.hasNext()) {

			// Get the next ring
			currRing = i.next();

			// Check if the ring names are the same
			if (currRing.getName().equals(name)) {

				// If names match, return the ring
				return currRing;
			}
		}

		// Otherwise no ring with the specified name was found; return null
		return null;

	}

	/**
	 * <p>
	 * Returns an ArrayList of Rings contained in the material block, ordered by
	 * ascending radii.
	 * </p>
	 * 
	 * @return Returns an ArrayList of rings contained in the material block.
	 */
	public ArrayList<Ring> getRings() {

		// Initialize an ArrayList of Rings
		ArrayList<Ring> list = new ArrayList<Ring>();

		// Add all the contents of rings to the list
		list.addAll(rings);

		// Return the ArrayList
		return list;

	}

	/**
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * 
	 * @return The hashcode of the object.
	 */
	@Override
	public int hashCode() {

		// Hash based upon superclass hash
		int hash = super.hashCode();

		// Add the hashes for all of the variables.
		hash += 31 * vertPosition;
		hash += 31 * rings.hashCode();

		// Return hash
		return hash;
	}

	/**
	 * <p>
	 * Compares the contents of objects and returns true if that are identical.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object being compared against.
	 * @return Returns true if the both objects are equal, otherwise false.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if otherObject is invalid
		if (otherObject != null && otherObject instanceof MaterialBlock) {

			// Cast to a MaterialBlock so we can access its variables/methods
			MaterialBlock otherBlock = (MaterialBlock) otherObject;

			// Method will check for equality on two levels: shallow (within the
			// scope of the MaterialBlock class), and deep (all inherited
			// variables from superclass). Will only return true if both cases
			// are true.

			// Create flags for checking shallow and deep equality, default to
			// false
			boolean shallowEqual = false;
			boolean deepEqual = false;

			// Check if vertical position is equal (shallow scope)
			if (vertPosition == otherBlock.vertPosition) {
				shallowEqual = true;
			}
			// Check if rings are equal (shallow scope). TreeSet.equals() fails
			// to
			// evaluate individual elements at this level, so we must traverse
			// and
			// compare the TreeSet manually

			// First check the TreeSets are the same size. If they are not, we
			// save
			// a bunch of time here
			if (rings.size() != otherBlock.rings.size()) {
				return false;
			}
			// If they're the same size, initialize rings to traverse through
			// the rings TreeSets
			Iterator<Ring> thisItr = rings.iterator();
			Iterator<Ring> thatItr = otherBlock.rings.iterator();

			// Iterate the TreeSet as long as there's another element after it
			// and everything has matched so far.
			while (shallowEqual && thisItr.hasNext()) {

				// Check the rings against eachother
				shallowEqual = thisItr.next().equals(thatItr.next());
			}

			// Check if all inherited variables are equal (deep scope)
			if (super.equals(otherBlock) && shallowEqual) {
				deepEqual = true;
			}

			// Return final result
			return deepEqual;
		}

		// If otherObject was invalid, return false
		else {
			return false;
		}
	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 */
	public void copy(MaterialBlock otherObject) {

		// Check if the material block is invalid
		if (otherObject == null) {
			return;
		}
		// Call the superclass copy operation
		super.copy(otherObject);

		// Copy the vertical position and rings TreeSet
		vertPosition = otherObject.vertPosition;
		rings = otherObject.rings;

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated object.
	 */
	@Override
	public Object clone() {

		// Create a new material block
		MaterialBlock block = new MaterialBlock();

		// Copy the contents from this material block into the new one
		block.copy(this);

		// Return the new material block
		return block;

	}

	/**
	 * <p>
	 * Compares the vertical position of two MaterialBlocks.
	 * </p>
	 * 
	 * @return Returns -1 if this block is below that block, +1 if this block is
	 *         above that block, and 0 otherwise (overlapping).
	 */
	@Override
	public int compareTo(MaterialBlock block) {

		// If this vertical position below that vertical position
		if (vertPosition < block.vertPosition) {
			return -1;
		}
		// If this vertical position above that vertical position
		else if (vertPosition > block.vertPosition) {
			return 1;
		}
		// Otherwise they must overlap (not possible)
		else {
			return 0;
		}
	}

	/**
	 * Overrides the default behavior (ignore) from SFRComponent and implements
	 * the accept operation for this SFRComponent's type.
	 */
	@Override
	public void accept(ISFRComponentVisitor visitor) {

		if (visitor != null) {
			visitor.visit(this);
		}

		return;
	}
}