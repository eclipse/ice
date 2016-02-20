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
package org.eclipse.ice.client.widgets.test.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * This class provides a base for UI-based tests that use an {@link SWTBot} (or
 * a sub-class thereof) and provides a reference to the main UI thread's
 * {@link Display} that is created at startup time.
 * <p>
 * This class also provides hooks for test class implementations, to facilitate
 * easy initialization and disposal of shared test resources and resources that
 * can be re-allocated on a per-test basis.
 * </p>
 * <p>
 * To use an {@code SWTBot} to simulate user interaction, use the bot provided
 * via {@link #getBot()}.
 * </p>
 * <p>
 * <b>Note:</b> Changes to the UI must be coordinated with the main UI thread
 * using {@link Display#syncExec(Runnable)} or
 * {@link Display#asyncExec(Runnable)}. To get the shared display, you may call
 * {@link #getDisplay()}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of {@code SWTBot}, e.g., {@link SWTBot} or
 *            {@link SWTWorkbenchBot}.
 * 
 * @see AbstractICESWTTester
 * @see AbstractICEWorkbenchTester
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public abstract class AbstractICEUITester<T extends SWTBot> extends
		SWTBotTestCase {

	/**
	 * The main UI thread's {@code Display}. This class does not create the
	 * display or the UI thread.
	 */
	private static Display display;

	/**
	 * The first instance of the tester that hits {@link #beforeEachTest()}. We
	 * use this to trigger {@link #beforeAllTests()} and
	 * {@link #afterAllTests()} for sub-classes so they need not wrap their
	 * heads around the {@code BeforeClass} tag and inheritance/hiding/shadowing
	 * issues.
	 */
	private static final AtomicReference<AbstractICEUITester<?>> firstTester = new AtomicReference<AbstractICEUITester<?>>();

	/**
	 * A map of temporary files, keyed on their filenames. This is created only
	 * when {@link #createTemporaryFile(String, String...)} is called. All
	 * temporary files created for a test method are deleted after the test
	 * method completes.
	 */
	private Map<String, File> tmpFiles;

	/**
	 * Gets the main UI thread's {@code Display}. This should <b>always</b> be
	 * used to perform updates to the UI from test code using the display's sync
	 * methods.
	 * 
	 * @return The main UI thread's display.
	 * 
	 * @see Display#syncExec(Runnable)
	 * @see Display#asyncExec(Runnable)
	 */
	protected static Display getDisplay() {
		return display;
	}

	/**
	 * Gets the {@code SWTBot} for this test class instance. This bot is to aid
	 * in performing UI tests that simulate user interaction with SWT-based
	 * applications.
	 * 
	 * @return The {@code SWTBot} for testing.
	 */
	protected abstract T getBot();

	/**
	 * Sets the shared reference to the UI thread's {@link #display}.
	 */
	@BeforeClass
	public static void beforeClass() {
		// Set the reference to the main UI thread's Display.
		if (display == null) {
			display = Display.getDefault();
		}
	}

	/**
	 * This method is called <i>before</i> <b>all</b> tests are run. It should
	 * be used to set up any <b>static</b> resources shared among all test
	 * cases.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.beforeAllTests()} at the
	 * <i>***BEGINNING OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	public void beforeAllTests() {
		// Nothing to do yet.
	}

	/**
	 * This method is called <i>before</i> <b>each</b> test is run. It should be
	 * used to set up any <b>non-static</b> resources required for <i>all</i>
	 * tests but that can be allocated once per test.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.beforeEachTest()} at the
	 * <i>***BEGINNING OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	@Before
	public void beforeEachTest() {
		// If this is the first test to be run, make sure all shared resources
		// have been allocated.
		if (firstTester.compareAndSet(null, this)) {
			beforeAllTests();
		}
	}

	/**
	 * This method is called <i>after</i> <b>each</b> test is run. It should be
	 * used to clean up any <b>non-static</b> resources required for <i>all</i>
	 * tests but that can be allocated once per test.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.afterEachTest()} at the
	 * <i>***END OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	@After
	public void afterEachTest() {

		// Delete all temporary files.
		if (tmpFiles != null) {
			Iterator<Entry<String, File>> iter = tmpFiles.entrySet().iterator();
			while (iter.hasNext()) {
				File file = iter.next().getValue();
				if (!file.delete()) {
					fail("AbstractICEUITester error: "
							+ "Could not delete the file \"" + file.getPath()
							+ "\".");
				}
				iter.remove();
			}
			tmpFiles = null;
		}

		return;
	}

	/**
	 * This method is called <i>after</i> <b>all</b> tests are run. It should be
	 * used to clean up any <b>static</b> resources shared among all test cases.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.afterAllTests()} at the
	 * <i>***END OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	public void afterAllTests() {
		display = null;
	}

	/**
	 * This is run by JUnit at the end of all tests. It merely redirects to the
	 * non-static {@link #afterAllTests()} for the {@link #firstTester} to
	 * trigger the cleanup of shared test resources.
	 */
	@AfterClass
	public static void afterClass() {
		AbstractICEUITester<?> instance = firstTester.getAndSet(null);
		if (instance != null) {
			instance.afterAllTests();
		}
	}

	/**
	 * Gets a {@link Matcher} (used by {@code SWTBot} to find widgets) that
	 * matches the exact object. This is helpful if you have a reference to the
	 * widget itself but want an {@code SWTBot} wrapper for it.
	 * 
	 * @param object
	 *            The object or widget to match.
	 * @return The matcher that can be passed to the {@code SWTBot}.
	 */
	protected <E> Matcher<E> getExactMatcher(final E object) {
		return new BaseMatcher<E>() {
			@Override
			public boolean matches(Object item) {
				return item == object;
			}

			@Override
			public void describeTo(Description description) {
				// Nothing to do.
			}
		};
	}

	/**
	 * Creates a temporary file for a single test method. This file will be
	 * stored in the directory equivalent to "/user.home/ICETests/" and with the
	 * specified file name.
	 * <p>
	 * Files created with this method are <b>not</b> stored for use across test
	 * methods. However, this file does not need to be deleted by the caller as
	 * it will be deleted at the end of the test case.
	 * </p>
	 * 
	 * @param filename
	 *            The name of the file. If {@code null}, the name "tmp" will be
	 *            used.
	 * @param lines
	 *            The contents of the file. Each non-null element will be placed
	 *            on a new line.
	 * @return The created file, or {@code null} if a file could not be created.
	 * @throws IOException
	 *             An IOException occurs if the file could not be written.
	 */
	protected File createTemporaryFile(String filename, String... lines)
			throws IOException {

		// Change the filename to "tmp" if none was provided.
		if (filename == null) {
			filename = "tmp";
		}

		// Set up the File based on the provided name.
		String separator = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		File file = new File(home + separator + "ICETests" + separator
				+ filename);

		// Set up the writer and write out the lines.
		FileWriter writer = new FileWriter(file);
		BufferedWriter buffer = new BufferedWriter(writer);
		for (String line : lines) {
			if (line != null) {
				buffer.write(line);
				buffer.newLine();
			}
		}
		// Be sure to close the writers!
		buffer.close();
		writer.close();

		// Store the file in the collection of temporary files.
		if (tmpFiles == null) {
			tmpFiles = new HashMap<String, File>();
		}
		tmpFiles.put(filename, file);

		return file;
	}
}