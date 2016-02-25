/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class contains static methods frequently used by ParaView tests.
 * 
 * @author Jordan Deyton
 *
 */
public class TestUtils {

	/**
	 * Creates a simple URI for the provided extension. The generated URI will
	 * be local.
	 * 
	 * @param extension
	 *            The extension for the test URI file. This file probably will
	 *            not actually exist. If {@code null}, then the file will have
	 *            no extension.
	 * @return A correctly formed URI with the provided extension.
	 */
	public static URI createURI(String extension) {
		return createURI(extension, null);
	}

	/**
	 * Creates a simple URI for the provided extension.
	 * 
	 * @param extension
	 *            The extension for the test URI file. This file probably will
	 *            not actually exist. If {@code null}, then the file will have
	 *            no extension.
	 * @param host
	 *            The host for the file. If null, a local file URI will be
	 *            created, otherwise the host name will be used.
	 * @return A correctly formed URI with the provided extension.
	 */
	public static URI createURI(String extension, String host) {
		String filename = (extension != null ? "kung_fury." + extension
				: "future_cop");

		URI uri = null;
		if (host == null) {
			uri = new File(filename).toURI();
		} else {
			try {
				uri = new URL("file", host, 10, "/" + filename).toURI();
			} catch (MalformedURLException e) {
				// This will never happen, because "file" is a valid protocol.
			} catch (URISyntaxException e) {
				// This will never happen unless the URL implementation is
				// changed for the worse.
			}
		}
		return uri;
	}

}
