/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature.ColorByLocation;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature.ColorByMode;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty.PropertyType;
import org.junit.Test;

/**
 * This class tests the default properties set at construction when creating a
 * {@link ProxyFeature}.
 * 
 * @author Jordan Deyton
 *
 */
public class ProxyFeatureTester {

	/**
	 * Checks the properties passed in via the constructors.
	 */
	@Test
	public void checkConstruction() {
		FakeProxyFeature feature;

		PropertyType defaultType = PropertyType.DISCRETE_MULTI;
		ColorByMode defaultMode = ColorByMode.SOLID;
		ColorByLocation defaultLocation = ColorByLocation.POINTS;

		// Check the most basic constructor.
		feature = new FakeProxyFeature("xia", 0);
		// Check its properties.
		assertEquals("xia", feature.name);
		assertEquals(0, feature.index);
		assertEquals(defaultType, feature.getPropertyType());
		assertFalse(feature.getCanColorBy()); // Cannot color by this feature!
		assertEquals(defaultMode, feature.getColorByMode());
		assertEquals(defaultLocation, feature.getColorByLocation());

		// Check the constructor that does not specify the property type.
		feature = new FakeProxyFeature("shang", 1, ColorByMode.ARRAY,
				ColorByLocation.CELLS);
		// Check its properties.
		assertEquals("shang", feature.name);
		assertEquals(1, feature.index);
		assertEquals(defaultType, feature.getPropertyType());
		assertTrue(feature.getCanColorBy());
		assertEquals(ColorByMode.ARRAY, feature.getColorByMode());
		assertEquals(ColorByLocation.CELLS, feature.getColorByLocation());

		// Check the constructor that does not specify the property type. When
		// passed null for the ColorBy settings, check their defaults.
		feature = new FakeProxyFeature("zhou", 3, null, null);
		// Check its properties.
		assertEquals("zhou", feature.name);
		assertEquals(3, feature.index);
		assertEquals(defaultType, feature.getPropertyType());
		assertFalse(feature.getCanColorBy()); // Cannot color by this feature!
		assertEquals(defaultMode, feature.getColorByMode());
		assertEquals(defaultLocation, feature.getColorByLocation());

		// Check the full constructor.
		feature = new FakeProxyFeature("qin", 4, PropertyType.UNDEFINED,
				ColorByMode.SOLID, ColorByLocation.POINTS);
		// Check its properties.
		assertEquals("qin", feature.name);
		assertEquals(4, feature.index);
		assertEquals(PropertyType.UNDEFINED, feature.getPropertyType());
		assertTrue(feature.getCanColorBy());
		assertEquals(ColorByMode.SOLID, feature.getColorByMode());
		assertEquals(ColorByLocation.POINTS, feature.getColorByLocation());

		// Check the full constructor. When passed null for the PropertyType or
		// ColorBy settings, check their defaults.
		feature = new FakeProxyFeature("qin", 4, null, null, null);
		// Check its properties.
		assertEquals("qin", feature.name);
		assertEquals(4, feature.index);
		assertEquals(defaultType, feature.getPropertyType());
		assertFalse(feature.getCanColorBy()); // Cannot color by this feature!
		assertEquals(defaultMode, feature.getColorByMode());
		assertEquals(defaultLocation, feature.getColorByLocation());
	}

	/**
	 * A fake class that exposes the properties of {@link ProxyFeature}. The
	 * constructors for this class merely call the super class' constructors.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeProxyFeature extends ProxyFeature {
		public FakeProxyFeature(String name, int index) {
			super(name, index);
		}

		public FakeProxyFeature(String name, int index, ColorByMode mode,
				ColorByLocation location) {
			super(name, index, mode, location);
		}

		public FakeProxyFeature(String name, int index, PropertyType type,
				ColorByMode mode, ColorByLocation location) {
			super(name, index, type, mode, location);
		}

		public PropertyType getPropertyType() {
			return type;
		}

		public boolean getCanColorBy() {
			return canColorBy;
		}

		public ColorByMode getColorByMode() {
			return colorByMode;
		}

		public ColorByLocation getColorByLocation() {
			return colorByLocation;
		}

		// Provide a default value.
		@Override
		protected int getProxyId() {
			return 0;
		}
	}
}
