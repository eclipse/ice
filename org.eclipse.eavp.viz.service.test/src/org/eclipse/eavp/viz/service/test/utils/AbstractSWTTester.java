/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation 
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.test.utils;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.Ignore;

/**
 * This abstract class provides a basic framework for performing SWTBot tests
 * for an SWT application inside a JUnit test framework. As such, test classes
 * sub-classing this abstract class can be run as JUnit tests, JUnit plug-in
 * tests (on or off the UI thread), or SWTBot tests.
 * <p>
 * A {@link Shell} is opened (outside the default Eclipse workbench if used as a
 * plug-in test). This shell provides the "sandbox" in which UI tests are
 * performed. For instance, to test a specific {@code Composite}, you may create
 * an instance of said {@code Composite} in the shell and perform its UI tests.
 * The shell is shared among sub-class tests, and can be acquired via
 * {@link #getShell()}.
 * </p>
 * <p>
 * An {@link SWTBot} is opened for each test class instance and should be used
 * for the actual UI tests. The bot can be acquired via {@link #getBot()}.
 * </p>
 * <p>
 * <b>Note:</b> Changes to the UI must be coordinated with the main UI thread
 * using {@link Display#syncExec(Runnable)} or
 * {@link Display#asyncExec(Runnable)}. To get the shell's display, you may call
 * {@link #getDisplay()}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
@Ignore
public class AbstractSWTTester extends AbstractICEUITester<SWTBot> {

	/**
	 * The {@code SWTBot} for this test class instance. This bot is to aid in
	 * performing UI tests that simulate user interaction with workbench-based
	 * plug-ins.
	 */
	private static SWTBot bot;

	/**
	 * The shell used when creating an {@code SWTBot} for unit tests.
	 */
	private static Shell shell;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractICEUITester#getBot()
	 */
	@Override
	protected SWTBot getBot() {
		return bot;
	}

	/**
	 * Gets the main shell for testing SWT widgets "outside" (although they may
	 * be embedded in) the Eclipse plug-in infrastructure. This should contain
	 * top-level SWT {@code Composite}s that will be tested.
	 * 
	 * @return The main shell for testing.
	 */
	protected static Shell getShell() {
		return shell;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractUITester#beforeAllTests()
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();

		final Display display = getDisplay();
		// Creating the shell must be done on the UI thread.
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				// Create and open the shell.
				shell = new Shell(display);
				shell.setLayout(new FillLayout());
				shell.open();
			}
		});

		// Initialize static or otherwise shared resources here.

		// Set up the SWTBot for the shell.
		bot = new SWTBot(shell);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractUITester#beforeEachTest()
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();

		// Initialize per-test resources here.
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterEachTest()
	 */
	@Override
	public void afterEachTest() {
		// Dispose per-test resources here.
		// Nothing to do yet.

		// Proceed with the default post-test cleanup.
		super.afterEachTest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterAllTests()
	 */
	@Override
	public void afterAllTests() {
		// Dispose static or otherwise shared resources here.

		// Close and unset the shell. This must be done on the UI thread.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				shell.close();
			}
		});
		shell = null;

		// Proceed with the default post-tests cleanup.
		super.afterAllTests();
	}

	/**
	 * This can be used to verify an exception that is thrown on the UI thread.
	 * 
	 * @param runnable
	 *            The runnable that would normally be sent to
	 *            {@link Display#syncExec(Runnable)}.
	 * @return The first {@link SWTException} that was thrown, or {@code null}
	 *         if one was not thrown.
	 */
	protected SWTException catchSWTException(final Runnable runnable) {
		// Use an AtomicReference to catch the exception.
		final AtomicReference<SWTException> exceptionRef;
		exceptionRef = new AtomicReference<SWTException>();
		// Run the runnable synchronously, but try to catch the SWTException.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (SWTException e) {
					exceptionRef.set(e);
				}
			}
		});
		// Return any SWTException that was thrown from the specified runnable.
		return exceptionRef.get();
	}
}