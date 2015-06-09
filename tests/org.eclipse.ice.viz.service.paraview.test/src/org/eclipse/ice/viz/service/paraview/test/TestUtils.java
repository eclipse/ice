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
package org.eclipse.ice.viz.service.paraview.test;

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
	 * Used by {@link #createTestURI(String)} to vary between remote and local
	 * URIs.
	 */
	private static int count = 0;

	/**
	 * Creates a simple URI for the provided extension. The generated URI may be
	 * either local or remote.
	 * 
	 * @param extension
	 *            The extension for the test URI file. This file probably will
	 *            not actually exist. If {@code null}, then the file will have
	 *            no extension.
	 * @return A correctly formed URI with the provided extension.
	 */
	protected static URI createTestURI(String extension) {
		return createTestURI(extension, count++ % 2 == 1);
	}

	/**
	 * Creates a simple URI for the provided extension.
	 * 
	 * @param extension
	 *            The extension for the test URI file. This file probably will
	 *            not actually exist. If {@code null}, then the file will have
	 *            no extension.
	 * @param remote
	 *            If true, then the generated URI will point to a remote file.
	 *            Otherwise, it will point to a local file.
	 * @return A correctly formed URI with the provided extension.
	 */
	protected static URI createTestURI(String extension, boolean remote) {
		String filename = (extension != null ? "kung_fury." + extension
				: "future_cop");

		URI uri = null;
		if (remote) {
			try {
				uri = new URL("file", "foo.bar.com", 10, "/" + filename
						+ extension).toURI();
			} catch (MalformedURLException e) {
				// This will never happen, because "file" is a valid protocol.
			} catch (URISyntaxException e) {
				// This will never happen unless the URL implementation is
				// changed for the worse.
			}
		} else {
			uri = new File(filename).toURI();
		}
		return uri;
	}

}
