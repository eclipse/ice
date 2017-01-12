package org.eclipse.ice.tests.client.widgets.moose;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.ice.persistence.xml.XMLFormContentDescriber;
import org.eclipse.ice.tests.persistence.xml.XMLFormContentDescriberTester;
import org.eclipse.ice.client.widgets.moose.MooseXMLFormContentDescriber;

/**
 * This class is responsible for testing
 * {@link org.eclipse.ice.client.widgets.moose.MooseXMLFormContentDescriber}.
 *
 * @author Alex McCaskey
 *
 */
public class MooseXMLFormContentDescriberTester extends XMLFormContentDescriberTester {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.persistence.xml.test.XMLFormContentDescriberTester#getForm()
	 */
	@Override
	public String getForm() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" "
				+ "standalone=\"yes\"?><MOOSE itemType=\"Basic\" "
				+ "status=\"2\" builderName=\"MOOSE Workflow\" "
				+ "description=\"The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed "
				+ "by Idaho National Laboratory.\" id=\"1\" "
				+ "name=\"MOOSE Workflow\"><Form itemID=\"1\" "
				+ "description=\"The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed "
				+ "by Idaho National Laboratory.\" id=\"1\" " + "name=\"MOOSE Workflow\">"
				+ "<componentList></componentList></Form><AllowedActions>Launch the Job"
				+ "</AllowedActions><itemBuilderName>"
				+ "MOOSE Workflow</itemBuilderName></MOOSE>";
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
		XMLFormContentDescriber describer = new MooseXMLFormContentDescriber();
		// Convert the text to an input stream
		ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes());
		// Check the text
		return describer.describe(stream, null);
	}
}
