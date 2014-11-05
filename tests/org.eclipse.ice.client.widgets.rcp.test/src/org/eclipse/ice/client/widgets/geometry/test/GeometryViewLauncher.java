package org.eclipse.ice.client.widgets.geometry.test;

import org.eclipse.ice.client.widgets.jme.MasterApplication;
import org.eclipse.ice.client.widgets.jme.ViewAppState;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class can be used to test an embedded, custom {@link ViewAppState}. This
 * test in particular tests the <code>GeometryEditor</code>'s view.
 * 
 * @author Jordan Deyton
 * 
 */
public class GeometryViewLauncher {

	public static void main(String[] args) {
		// Create the display and shell.
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();

		// Create the application and widgets that can create and dispose of jME
		// views.
		final MasterApplication app = MasterApplication.createApplication();

		// Set the shell's Layout and create a SashForm with a left and right
		// Composite.
		shell.setLayout(new FillLayout());

		// Wait until the Application is initialized.
		app.blockUntilInitialized(0);

		// Create a geometry view, start it, and embed it in the shell.
		// TODO Uncomment this when class is implemented.
		// GeometryAppState view = new GeometryAppState(app);
		// view.start();
		// view.createComposite(shell);

		// Lay out the shell.
		shell.layout();

		// ---- Add geometries to the view. ---- //
		// TODO
		// ------------------------------------- //

		// Close the display and shell.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

		// Stop the ViewAppState and the Application.
//		view.stop();
		app.stop();

		return;
	}
}
