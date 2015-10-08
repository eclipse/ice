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
package org.eclipse.ice.viz.service.geometry.viewer;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.ice.viz.service.geometry.viewer.messages"; //$NON-NLS-1$
    public static String GeometryViewer_InvalidInput;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
