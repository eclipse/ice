/**
 */
package eclipseapps.impl;

import apps.impl.EnvironmentConsoleImpl;

import eclipseapps.EclipseEnvironmentConsole;
import eclipseapps.EclipseappsPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Environment Console</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class EclipseEnvironmentConsoleImpl extends EnvironmentConsoleImpl implements EclipseEnvironmentConsole {
	
	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(EclipseEnvironmentConsoleImpl.class);

	/**
	 * The console view in Eclipse.
	 */
	private static IConsoleView consoleView = null;

	/**
	 * The console that will display text from this widget.
	 */
	private static MessageConsole console = null;

	/**
	 * The message stream for the message console to which text should be
	 * streamed.
	 */
	private static MessageConsoleStream msgStream = null;

	protected static void initializeConsoleOutput() {
		// Open the Console for Action output text
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Get the currently active page
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					// Load the console view
					consoleView = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
					// Create the console instance that will be used to display
					// text from this widget.
					if (console == null) {
						console = new MessageConsole("App Store Console", null);
						// Add the console to the console manager
						ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
						// Show the console in the view
						consoleView.display(console);
						console.activate();
						// Get an output stream for the console
						msgStream = console.newMessageStream();
						msgStream.setActivateOnWrite(true);
						msgStream.println("Streaming output console activated.");
					}
				} catch (PartInitException e) {
					// Complain
					logger.error("Action Message: " + "Unable to stream text!");
					logger.error(getClass().getName() + " Exception!", e);
				}

			}
		});

	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EclipseEnvironmentConsoleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EclipseappsPackage.Literals.ECLIPSE_ENVIRONMENT_CONSOLE;
	}
	
	@Override
	public void print(String message) {
		if (msgStream == null) {
			initializeConsoleOutput();
		}

		// Must sync with the display thread
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Push the text, if possible
				if (msgStream != null) {
					msgStream.println(message);
				}
			}
		});
	}

} //EclipseEnvironmentConsoleImpl
