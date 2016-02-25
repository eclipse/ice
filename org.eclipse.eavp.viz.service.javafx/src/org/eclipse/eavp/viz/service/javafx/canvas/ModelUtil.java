/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.canvas;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;

/**
 * A class for utility functions related to testing an object's validity.
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class ModelUtil {

	/**
	 * Tests whether the given object is a node.
	 * 
	 * @param obj
	 *            The object to be tested.
	 * @return True if the object is a non-null instance of INode. False
	 *         otherwise.
	 */
	public static boolean isNode(Object obj) {
		return obj != null && obj instanceof INode;
	}

	/**
	 * Tests whether the given object is an Attachment.
	 * 
	 * @param obj
	 *            The object to be tested.
	 * @return True if the object is a non-null instance of IAttachment. False
	 *         otherwise.
	 */
	public static boolean isAttachment(Object obj) {
		return obj != null && obj instanceof IAttachment;
	}

}
