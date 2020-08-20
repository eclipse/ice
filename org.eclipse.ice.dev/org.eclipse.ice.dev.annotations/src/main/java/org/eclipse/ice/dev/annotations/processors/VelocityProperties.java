/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import java.util.Properties;

/**
 * Properties used to initialize Velocity.
 */
enum VelocityProperties {

	// Set up Velocity using the Singleton approach; ClasspathResourceLoader allows
	// us to load templates from src/main/resources
	RESOURCE_LOADER("resource.loader", "class"), CLASS_RESOURCE_LOADER("class.resource.loader.class",
			"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

	/**
	 * Property key.
	 */
	private String key;

	/**
	 * Property value.
	 */
	private String value;

	VelocityProperties(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Get key from enum.
	 * 
	 * @return key
	 */
	String key() {
		return this.key;
	}

	/**
	 * Get value from enum.
	 * 
	 * @return value
	 */
	String value() {
		return this.value;
	}

	/**
	 * Generate and return Properties from enum.
	 * 
	 * @return Properties
	 */
	public static Properties get() {
		Properties p = new Properties();
		for (VelocityProperties vp : VelocityProperties.values()) {
			p.setProperty(vp.key(), vp.value());
		}
		return p;
	}
}