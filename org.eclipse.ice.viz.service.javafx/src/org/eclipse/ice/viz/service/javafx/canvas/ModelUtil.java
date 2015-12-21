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
package org.eclipse.ice.viz.service.javafx.canvas;

import org.eclipse.ice.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.ice.viz.service.javafx.scene.model.INode;

/**
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class ModelUtil {

    /**
     * <p>
     * </p>
     * 
     * @param obj
     * @return
     */
    public static boolean isNode(Object obj) {
        return obj != null && obj instanceof INode;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isAttachment(Object obj) {
        return obj != null && obj instanceof IAttachment;
    }

}
