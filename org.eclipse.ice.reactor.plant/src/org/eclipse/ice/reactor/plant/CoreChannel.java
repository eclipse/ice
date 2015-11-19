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
package org.eclipse.ice.reactor.plant;


/** 
 * <p>Simulates the fluid flow associated with a solid heat structure part.</p>
 * @author Anna Wojtowicz
 */
public class CoreChannel extends Pipe {

	/** 
	 * <p>Nullary constructor.</p>
	 */
	public CoreChannel() {

		// Call super constructor.
		super();
		
		// Set the name, description and ID.
		setName("Core Channel 1");
		setDescription("Core channel component for reactors");
		setId(1);	
		
		return;
	}

	/** 
	 * <p>Performs an equality check between two Objects.</p>
	 * @param otherObject <p>The other Object to compare against.</p>
	 * @return <p>Returns true if the two objects are equal, otherwise false.</p>
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Super's equality check takes care of this.
		return super.equals(otherObject);
		
	}

	/** 
	 * <p>Performs a deep copy and returns a newly instantiated Object.</p>
	 * @return <p>The newly instantiated Object.</p>
	 */
	@Override
	public Object clone() {
		
		// Initialize a new object.
		CoreChannel object = new CoreChannel();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/** 
	 * <p>Deep copies the contents of otherObject.</p>
	 * @param otherObject <p>The other object to copy the contents from.</p>
	 */
	public void copy(CoreChannel otherObject) {
		
		// Super's copy takes care of this.

	}

	/** 
	 * <p>Returns the hashCode of the object.</p>
	 * @return <p>The hashCode of the Object.</p>
	 */
	@Override
	public int hashCode() {

		return super.hashCode();
		
	}

	/** 
	 * <p>Accepts PlantComponentVisitors to reveal the type of a PlantComponent.</p>
	 * @param visitor <p>The PlantComponent's visitor.</p>
	 */
	@Override
	public void accept(IPlantComponentVisitor visitor) {
		
		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}
}