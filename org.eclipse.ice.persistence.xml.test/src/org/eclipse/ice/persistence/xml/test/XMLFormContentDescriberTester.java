/**
 *
 */
package org.eclipse.ice.persistence.xml.test;

/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *******************************************************************************/
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.runtime.content.ITextContentDescriber;
import org.eclipse.ice.persistence.xml.XMLFormContentDescriber;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class is responsible for testing
 * {@link org.eclipse.ice.persistence.xml.XMLFormContentDescriber}.
 *
 * @author Jay Jay Billings
 *
 */
public class XMLFormContentDescriberTester {

	// Load some text from a VIBE KV Pair Item.
	String xmlForm = "<?xml version=\"1.0\" encoding=\"UTF-8\" "
			+ "standalone=\"yes\"?><VibeKVPair itemType=\"Model\" "
			+ "status=\"2\" builderName=\"VIBE Key-Value Pair\" "
			+ "description=\"Generate input files for VIBE.\" id=\"1\" "
			+ "name=\"VIBE Key-Value Pair\"><Form itemID=\"1\" "
			+ "description=\"Generate input files for VIBE.\" id=\"1\" "
			+ "name=\"VIBE Key-Value Pair\">"
			+ "<componentList></componentList></Form><AllowedActions>Export "
			+ "to key-value pair output</AllowedActions><itemBuilderName>"
			+ "VIBE Key-Value Pair</itemBuilderName></VibeKVPair>";

	/**
	 * Test method for
	 * {@link org.eclipse.ice.persistence.xml.XMLFormContentDescriber#describe(java.io.InputStream, org.eclipse.core.runtime.content.IContentDescription)}
	 * .
	 *
	 * It is sufficient to only test the first describe() operation since it
	 * delegates the work to the second.
	 *
	 * @throws IOException
	 *             This exception is thrown if the stream cannot be read for
	 *             some reason.
	 */
	@Test
	public void testDescribe() throws IOException {

		// Test with valid text.
		assertEquals(ITextContentDescriber.VALID, runTest(xmlForm));

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.persistence.xml.XMLFormContentDescriber#getSupportedOptions()}
	 * .
	 */
	@Ignore
	@Test
	public void testGetSupportedOptions() {
		fail("Not yet implemented");

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.persistence.xml.XMLFormContentDescriber#describe(java.io.Reader, org.eclipse.core.runtime.content.IContentDescription)}
	 * .
	 */
	@Test
	public void testBadText() {
		// Test with bad text.
		try {
			assertEquals(ITextContentDescriber.INVALID, runTest("foo"));
		} catch (IOException e) {
			// Complain and fail since these cases should not have produced
			// IOExceptions.
			e.printStackTrace();
			fail();
		}

		return;
	}

	/**
	 * This operation runs the actual tests in place of the individual test
	 * operations.
	 *
	 * @param text
	 *            The text that should be used in the test.
	 * @throws IOException
	 *             This exception is thrown if there is an IO error.
	 */
	private int runTest(String text) throws IOException {

		// Create the describer
		XMLFormContentDescriber describer = new XMLFormContentDescriber();
		// Convert the text to an input stream
		ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes());
		// Check the text
		return describer.describe(stream, null);
	}

}
