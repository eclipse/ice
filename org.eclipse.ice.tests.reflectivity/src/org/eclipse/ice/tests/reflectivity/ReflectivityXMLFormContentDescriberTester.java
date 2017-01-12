package org.eclipse.ice.tests.reflectivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.ice.persistence.xml.XMLFormContentDescriber;
import org.eclipse.ice.reflectivity.ui.ReflectivityXMLFormContentDescriber;
import org.eclipse.ice.tests.persistence.xml.XMLFormContentDescriberTester;

/**
 * This class is responsible for testing
 * {@link org.eclipse.ice.client.widgets.moose.MooseXMLFormContentDescriber}.
 *
 * @author Alex McCaskey
 *
 */
public class ReflectivityXMLFormContentDescriberTester extends XMLFormContentDescriberTester {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.persistence.xml.test.XMLFormContentDescriberTester#getForm()
	 */
	@Override
	public String getForm() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" "
				+ "standalone=\"yes\"?><ReflectivityModel itemType=\"Basic\" "
				+ "status=\"2\" builderName=\"Reflectivity Model\" "
				+ "description=\"This item builds models for Reflectivity.\" id=\"1\" "
				+ "name=\"Reflectivity Model\"><Form itemID=\"1\" "
				+ "description=\"This item builds models for Reflectivity.\" id=\"1\" " + "name=\"Reflectivity Model\">"
				+ "<componentList></componentList></Form><AllowedActions>Launch the Job"
				+ "</AllowedActions><itemBuilderName>"
				+ "Reflectivity Model</itemBuilderName></ReflectivityModel>";
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
	@Override
	protected int runTest(String text) throws IOException {

		// Create the describer
		XMLFormContentDescriber describer = new ReflectivityXMLFormContentDescriber();
		// Convert the text to an input stream
		ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes());
		// Check the text
		return describer.describe(stream, null);
	}
}
