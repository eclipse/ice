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
package org.eclipse.eavp.viz.service.javafx.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.eavp.viz.service.javafx.internal.messages"; //$NON-NLS-1$
    public static String FXContentProvider_InvalidInputNode;
    public static String FXContentProvider_InvalidInputType;
    public static String FXContentProvider_InvalidViewerType;
    public static String FXGeometryViewer_InvalidCamera;
    public static String FXGeometryViewer_NullCamera;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
