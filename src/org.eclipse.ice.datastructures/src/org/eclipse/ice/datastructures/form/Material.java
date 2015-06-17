/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.datastructures.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents physical Materials.
 * 
 * All materials have a name and a size. Other properties whose values are
 * normally double precision floating point numbers are stored as properties.
 * The list of properties available for a material can be retrieved by calling
 * getProperties() and the list of properties can be modified by calling
 * get/setProperty().
 * 
 * Materials can also be composed of other materials called "Components." Each
 * Material that comprises the the composite Materials should be set using the
 * addComponent() operation and the whole list of components can get retrieved
 * with the getComponents() operation. ICE makes no attempt to correct
 * mismatched sizes, etc. between components and composites.
 * 
 * @author Jay Jay Billings
 * 
 */
@XmlRootElement(name = "Material")
@XmlAccessorType(XmlAccessType.FIELD)
public class Material implements Cloneable, Comparable {

	/**
	 * The name of the material.
	 */
	private String name;

	/**
	 * The size of the material.
	 */
	private int size;

	/**
	 * The key-value pair map of properties for this material.
	 */
	private HashMap<String, Double> properties;

	/**
	 * The list of components that comprise this material.
	 */
	@XmlElement(name = "Material")
	private List<Material> components;

	/**
	 * The constructor.
	 */
	public Material() {
		name = "";
		size = 0;
		properties = new HashMap<String, Double>();
		components = new ArrayList<Material>();
	}

	/**
	 * Get the name of the material.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the material
	 * 
	 * @param matName
	 *            The name of the material. There are no restrictions on what it
	 *            may be.
	 */
	public void setName(String matName) {
		name = matName;
	}

	/**
	 * Get the size of the Material
	 * 
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * This operation sets the size of the material.
	 * 
	 * @param matSize
	 *            The size of the material. There are no restrictions on what it
	 *            may be. Technically a negative value here would be really
	 *            stupid, put it is possible for now.
	 */
	public void setSize(int matSize) {
		size = matSize;
	}

	/**
	 * This operation returns the value of the material property with the given
	 * name.
	 * 
	 * @param key
	 *            The key/name of the property whose value should be returned
	 * @return the value or 0.0 if this value is not in the map, but never null.
	 */
	public double getProperty(String key) {
		double value = 0.0;
		if (properties.containsKey(key)) {
			value = properties.get(key);
		}
		return value;
	}

	/**
	 * This operation sets value of the material property with the given name.
	 * 
	 * @param key
	 *            The key/name of the property whose value should be set
	 * @param value
	 *            the property
	 */
	public void setProperty(String key, double value) {
		properties.put(key, value);
	}

	/**
	 * This operation removes a property from the material's properties list.
	 * 
	 * @param key
	 *            The name of the property that should be removed.
	 */
	public void removeProperty(String key) {
		if (properties.containsKey(key)) {
			properties.remove(key);
		}
	}

	/**
	 * This operation returns the full set of properties for this material.
	 * 
	 * @return A map of the properties. Changing this map will not change the
	 *         internal properties of the material.
	 */
	public Map<String, Double> getProperties() {
		return new HashMap<String, Double>(properties);
	}

	/**
	 * This operation returns the list of materials that compose this material.
	 * 
	 * @return The list of Materials that make up this one. Changing this list
	 *         will not affect list stored by the Material.
	 */
	public List<Material> getComponents() {
		return new ArrayList<Material>(components);
	}

	/**
	 * This operation adds a component to this material, effectively marking
	 * this Materials as being a composite of others.
	 * 
	 * @param component
	 *            The material that is a component of this material.
	 */
	public void addComponent(Material component) {
		components.add(component);
	}

	/**
	 * This operation overrides Object.equals() to tailor its behavior for
	 * materials.
	 * 
	 * @param other
	 *            The other Object to compare against this one
	 * @return true if they are equal, false otherwise
	 */
	public boolean equals(Object other) {
		// Local Declarations
		boolean retVal = false;

		// Don't inspect the input if it is not a Material or if it is null
		if (other != null && other instanceof Material) {
			// Check the reference
			if (this == other) {
				retVal = true;
			} else {
				Material otherMaterial = (Material) other;
				// Check each member
				retVal = this.name.equals(otherMaterial.name)
						&& this.size == otherMaterial.size
						&& this.components.equals(otherMaterial.components)
						&& this.properties.equals(otherMaterial.properties);
			}
		}

		return retVal;
	}

	/**
	 * This operation overrides Object.hashCode to return the proper hash for
	 * Materials.
	 * 
	 * @return the hash
	 */
	@Override
	public int hashCode() {
		// Local Declarations
		int hash = 8;

		// Compute the hash code
		hash = 31 * hash + name.hashCode();
		hash = 31 * hash + size;
		hash = 31 * hash + properties.hashCode();
		hash = 31 * hash + components.hashCode();

		return hash;
	}

	/**
	 * This operation copies the content of the incoming material into this
	 * material.
	 * 
	 * @param material
	 *            the material to copy
	 */
	public void copy(Material material) {
		// Don't copy the input if it is not a Material or if it is null
		if (material != null && material != this) {
			this.name = material.name;
			this.size = material.size;
			this.properties = new HashMap<String, Double>(material.properties);
			this.components = new ArrayList<Material>(material.components);
		}
	}

	/**
	 * Gets the number before the element denoting which isotope or form of an
	 * element or compound this material represents.
	 * 
	 * @return The number of this isotope, as an int. Will return 0 if this is a
	 *         pure element (no number preceding its name)
	 */
	public int getIsotopicNumber() {
		// Get an empty string to build off of
		String numStr = "";
		// Iterate over the characters in the name to pull out the isotope
		// number.
		// it is assumed that the name will follow the format xxxYy, where x is
		// a digit and y is a letter.
		for (int i = 0; i < name.length(); i++) {
			if (Character.isDigit(name.charAt(i))) {
				numStr += name.charAt(i);
			} else {
				break;
			}
		}
		// Get the isotope number in int form. If no x values in name, return 0.
		int retVal;
		if (numStr.equals("")) {
			retVal = 0;
		} else {
			retVal = Integer.parseInt(numStr);
		}
		return retVal;
	}

	/**
	 * Gets the elemental or compound name for this material. Note- this will
	 * return the same string for two isotopes of the same element.
	 * 
	 * @return A String containing the name of the element or compound that this
	 *         material represents.
	 */
	public String getElementalName() {
		// A string to build on
		String nameStr = "";
		// Iterate over the name of the material, it is assumed that the
		// name follow the form xxxYy, where x is a digit and y is a letter.
		for (int i = name.length() - 1; i >= 0; i--) {
			if (Character.isLetter(name.charAt(i))) {
				nameStr = name.charAt(i) + nameStr;
			} else {
				break;
			}
		}
		return nameStr;
	}

	/**
	 * This operation clones the material and creates a completely new material
	 * with the same information.
	 * 
	 * @return The clone
	 */
	public Object clone() {
		// Create a new Material, copy everything into it and return it
		Material clone = new Material();
		clone.copy(this);
		return clone;
	}

	/**
	 * This operation compares materials so that they may be sorted when in
	 * lists. Implements the Comparable interface. Uses only the material's
	 * names, as these should be the best unique identifiers for sorting.
	 * 
	 * @param otherMaterial
	 *            The other material to be compared. Will return 0 if this is
	 *            not a material object or a subclass!
	 * @return Returns a value less than one if it is to be closer to index 0
	 *         than the other material. Returns a value of exactly 0 if it is
	 *         equal to the other material. Finally, returns a value of greater
	 *         than one if it is to be further from index 0 than the other
	 *         material.
	 */
	@Override
	public int compareTo(Object otherMaterial) {

		int returnVal = 0;

		// The name of the element or compound for the two materials
		String thisElement = getElementalName();
		String otherElement = ((Material) otherMaterial).getElementalName();

		// The isotopic numbers for the two materials
		int thisNum = getIsotopicNumber();
		int otherNum = ((Material) otherMaterial).getIsotopicNumber();

		// Dealing with the same element, sort by isotope number
		if (thisElement.toLowerCase().equals(otherElement.toLowerCase())) {

			// Sort from lower isotopic number to greater
			if (thisNum < otherNum) {
				returnVal = -1;
			} else if (thisNum > otherNum) {
				returnVal = 1;
			} else {
				returnVal = 0;
			}

			// Dealing with different elements, sort by name.
		} else {
			returnVal = thisElement.toLowerCase().compareTo(
					otherElement.toLowerCase());
		}

		// Return the sorting value for these two Materials
		return returnVal;

	}

}
