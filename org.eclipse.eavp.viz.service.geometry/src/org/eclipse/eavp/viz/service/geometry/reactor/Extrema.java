/*******************************************************************************
 * Copyright (c) 2015-2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.geometry.reactor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class defining the extrema of some shape or region in three dimensional
 * space. It includes the minimum and maximum x, y, and z coordinates found
 * within the region.
 * 
 * @author Robert Smith
 *
 */
public class Extrema {

	/**
	 * The extrema's error logger.
	 */
	private final Logger logger;

	/**
	 * The region's maximum x coordinate.
	 */
	private double maxX;

	/**
	 * The region's maximum y coordinate.
	 */
	private double maxY;

	/**
	 * The region's maximum z coordinate.
	 */
	private double maxZ;

	/**
	 * The region's minimum x coordinate.
	 */
	private double minX;

	/**
	 * The region's minimum y coordinate.
	 */
	private double minY;

	/**
	 * The region's minimum z coordinate.
	 */
	private double minZ;

	/**
	 * A constructor which initializes the extrema to the given values
	 * 
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 * @param minZ
	 * @param maxZ
	 */
	public Extrema(double minX, double maxX, double minY, double maxY,
			double minZ, double maxZ) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;

		// Allocate the logger
		logger = LoggerFactory.getLogger(getClass());

		if (minX > maxX || minY > maxY || minZ > maxZ) {
			logger.error(
					"Illegal Extrama bounds: Minimum values may not exceed maximum values");
		}
	}

	/**
	 * A constructor which creates an extrema from a list of the extrema of
	 * other sub-regions. The new extrema will be the extrema of the smallest
	 * region possible which contains all given sub-regions. If subRegions is
	 * null, the extrema will have 0 for all minimum and maximum values.
	 * 
	 * @param subRegions
	 *            The list of regions which this extrema will contain.
	 */
	public Extrema(List<Extrema> subRegions) {

		// Allocate the logger
		logger = LoggerFactory.getLogger(getClass());

		// If the list is empty, set everything to 0
		if (subRegions.isEmpty()) {
			minX = 0;
			maxX = 0;
			minY = 0;
			maxY = 0;
			minZ = 0;
			maxZ = 0;
		}

		for (Extrema region : subRegions) {

			// For subsequent regions, compare each value
			if (region != subRegions.get(0)) {

				// If the other minimum is lower, set own minimum to that
				// region's minimum
				if (minX > region.minX) {
					minX = region.minX;
				}

				// If the other maximum is higher, set own maximum to that
				// region's maximum
				if (maxX < region.maxX) {
					maxX = region.maxX;
				}

				// If the other minimum is lower, set own minimum to that
				// region's minimum
				if (minY > region.minY) {
					minY = region.minY;
				}

				// If the other maximum is higher, set own maximum to that
				// region's maximum
				if (maxY < region.maxY) {
					maxY = region.maxY;
				}

				// If the other minimum is lower, set own minimum to that
				// region's minimum
				if (minZ > region.minZ) {
					minZ = region.minZ;
				}

				// If the other maximum is higher, set own maximum to that
				// region's maximum
				if (maxZ < region.maxZ) {
					maxZ = region.maxZ;
				}

				// Initialize the the extrema to the first region's boundaries
			} else {
				this.minX = region.minX;
				this.maxX = region.maxX;
				this.minY = region.minY;
				this.maxY = region.maxY;
				this.minZ = region.minZ;
				this.maxZ = region.maxZ;
			}

		}
	}

	/**
	 * Get the region's minimum X coordinate.
	 * 
	 * @return The minimum X coordinate
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * Get the region's minimum Y coordinate.
	 * 
	 * @return The minimum Y coordinate
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * Get the region's minimum Z coordinate.
	 * 
	 * @return The minimum Z coordinate
	 */
	public double getMinZ() {
		return minZ;
	}

	/**
	 * Get the region's maximum X coordinate.
	 * 
	 * @return The maximum X coordinate
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * Get the region's maximum Y coordinate.
	 * 
	 * @return The maximum Y coordinate
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * Get the region's maximum Z coordinate.
	 * 
	 * @return The maximum Z coordinate
	 */
	public double getMaxZ() {
		return maxZ;
	}

	/**
	 * Set the minimum X value.
	 * 
	 * @param coordinate
	 *            the new minimum X coordinate
	 */
	public void setMinX(double coordinate) {
		minX = coordinate;
	}

	/**
	 * Set the minimum Y value.
	 * 
	 * @param coordinate
	 *            the new minimum Y coordinate
	 */
	public void setMinY(double coordinate) {
		minY = coordinate;
	}

	/**
	 * Set the minimum Z value.
	 * 
	 * @param coordinate
	 *            the new minimum Z coordinate
	 */
	public void setMinZ(double coordinate) {
		minZ = coordinate;
	}

	/**
	 * Set the maximum X value.
	 * 
	 * @param coordinate
	 *            the new maximum X coordinate
	 */
	public void setMaxX(double coordinate) {
		maxX = coordinate;
	}

	/**
	 * Set the maximum Y value.
	 * 
	 * @param coordinate
	 *            the new maximum Y coordinate
	 */
	public void setMaxY(double coordinate) {
		maxY = coordinate;
	}

	/**
	 * Set the maximum Z value.
	 * 
	 * @param coordinate
	 *            the new maximum Z coordinate
	 */
	public void setMaxZ(double coordinate) {
		maxZ = coordinate;
	}

}