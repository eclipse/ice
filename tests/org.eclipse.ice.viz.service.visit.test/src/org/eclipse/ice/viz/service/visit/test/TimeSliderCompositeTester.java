/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton (UT-Battelle, LLC.) - Initial API and implementation and/or
 *     initial documentation
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;
import org.eclipse.ice.viz.service.visit.widgets.TimeSliderComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotArrowButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotScale;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Test;

/**
 * This tester applies SWTBot tests to the {@link TimeSliderComposite}. It tests
 * the default values, selection listeners, and setting the range of time values
 * for the underlying scale, spinner, and text widgets.
 * 
 * @author Jordan
 *
 */
public class TimeSliderCompositeTester extends AbstractSWTTester {

	/**
	 * The time widget that will be tested. This gets initialized before and
	 * disposed after each test.
	 */
	private static TimeSliderComposite timeComposite;

	/**
	 * A list of test times. This includes the values -1.0, 0.0, null, -2.0,
	 * 42.0, null, 0.0 (again) and 1337.1337. It should be able to be passed to
	 * the {@link #timeComposite}.
	 */
	private static List<Double> testTimes;

	/**
	 * A list of the complete test times, ordered, and without nulls or
	 * duplicates.
	 */
	private static SortedSet<Double> orderedTimes;

	/**
	 * The expected size of {@link #testTimes}.
	 */
	private static int testTimesSize;

	/**
	 * The string used in the time text widget when no times are available.
	 */
	private static final String NO_TIMES = "N/A";

	/**
	 * A fake listener to listen to selection events. Its notified flag is reset
	 * before each test.
	 */
	private static FakeListener fakeListener1;
	/**
	 * A second fake listener to listen to selection events. Its notified flag
	 * is reset before each test.
	 */
	private static FakeListener fakeListener2;

	/**
	 * The error of margin for double comparisons.
	 */
	private final static double epsilon = 1e-5;

	/*
	 * Overrides a method from AbstractSWTTester.
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();

		// Initialize the list of times.
		testTimes = new ArrayList<Double>();
		testTimes.add(-1.0);
		testTimes.add(0.0);
		testTimes.add(null);
		testTimes.add(-2.0);
		testTimes.add(42.0);
		testTimes.add(null);
		testTimes.add(0.0);
		testTimes.add(1337.1337);
		testTimesSize = testTimes.size();

		// Create an ordered set of the times, and remove nulls. We expect the
		// time widget to effectively traverse across this set.
		Set<Double> hashedTimes = new HashSet<Double>(testTimes);
		hashedTimes.remove(null);
		orderedTimes = new TreeSet<Double>(hashedTimes);

		// Create the two fake listeners.
		fakeListener1 = new FakeListener();
		fakeListener2 = new FakeListener();

		// Initialize the time Composite, set its timesteps, and register the
		// first fake listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite = new TimeSliderComposite(getShell(), SWT.NONE);
				timeComposite.setTimes(testTimes);
				timeComposite.addSelectionListener(fakeListener1);
			}
		});

		return;
	}

	/*
	 * Overrides a method from AbstractSWTTester.
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();

		// Reset the time.
		SWTBotScale scale = getTimeScale();
		scale.setValue(scale.getMinimum());

		// Reset the listeners.
		fakeListener1.wasNotified.set(false);
		fakeListener2.wasNotified.set(false);

		// Unregister the second fake listener in case one of the listener tests
		// failed.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(fakeListener2);
			}
		});

		return;
	}

	/*
	 * Overrides a method from AbstractSWTTester.
	 */
	@Override
	public void afterAllTests() {

		// Dispose the time widget. The UI thread can do this whenever it gets a
		// chance.
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.dispose();
				timeComposite = null;
			}
		});

		// Empty out the list of times.
		testTimes.clear();
		testTimes = null;

		super.afterAllTests();
	}

	/**
	 * Checks the default values for the time widget.
	 */
	@Test
	public void checkDefaults() {

		final AtomicInteger timestep = new AtomicInteger();
		final AtomicReference<Double> time = new AtomicReference<Double>();

		// Create a temporary, blank test Composite, pull off its default
		// values, and dispose it.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				TimeSliderComposite c = new TimeSliderComposite(getShell(),
						SWT.NONE);
				timestep.set(c.getTimestep());
				time.set(c.getTime());
				c.dispose();
			}
		});

		// Check the two getters.
		assertEquals(-1, timestep.get());
		assertEquals(0.0, time.get(), epsilon);

		return;
	}

	/**
	 * Checks that the time steps can be set and fetched.
	 */
	@Test
	public void checkSetTimeSteps() {

		final AtomicInteger timestep = new AtomicInteger();
		final AtomicReference<Double> time = new AtomicReference<Double>();

		final List<Double> emptyList = new ArrayList<Double>();
		final List<Double> nullList = null;

		// Get the time scale widget.
		SWTBotScale widget = getTimeScale();
		SWTBotScale scale = getTimeScale();

		// Check that the list of times sent to the widget was not modified.
		assertEquals("TimeSliderComposite failure: The collection passed into "
				+ "setTimes(...) should not be modified!", testTimesSize,
				testTimes.size());

		// Get the timestep and time from the time widget, and check its values
		// match.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timestep.set(timeComposite.getTimestep());
				time.set(timeComposite.getTime());
			}
		});
		// The timestep should be the first timestep (index 0), while the time
		// should be the lowest value.
		assertEquals(0, timestep.get());
		assertEquals(orderedTimes.first(), time.get(), epsilon);

		// Check the scale's min, max, and increment. Each timestep should have
		// a tick on the scale.
		assertEquals(0, scale.getMinimum());
		assertEquals(orderedTimes.size() - 1, scale.getMaximum());
		assertEquals(1, scale.getIncrement());

		// Set the time widget to the last time.
		widget.setValue(orderedTimes.size() - 1);

		// Get the timestep and time from the time widget, and check its values
		// match.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timestep.set(timeComposite.getTimestep());
				time.set(timeComposite.getTime());
			}
		});
		// The timestep should be the last timestep, while the time should be
		// the highest value.
		assertEquals(orderedTimes.size() - 1, timestep.get());
		assertEquals(orderedTimes.last(), time.get(), epsilon);

		// Set new times on the widget and get each one from the widget.
		orderedTimes.remove(42.0);
		orderedTimes.remove(0.0);
		final List<Double> newTimes = new ArrayList<Double>(orderedTimes);

		// Update the times for the widget and get the default values.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Update the times and get the default values.
				timeComposite.setTimes(newTimes);
				timestep.set(timeComposite.getTimestep());
				time.set(timeComposite.getTime());
			}
		});
		assertEquals(0, timestep.get());
		assertEquals(orderedTimes.first(), time.get(), epsilon);

		// Check all of the times.
		for (int i = 0; i < newTimes.size(); i++) {
			// Increment the widget.
			widget.setValue(i);
			// Get the time widget's time and timestep, and compare them.
			getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					timestep.set(timeComposite.getTimestep());
					time.set(timeComposite.getTime());
				}
			});
			assertEquals(i, timestep.get());
			assertEquals(newTimes.get(i), time.get(), epsilon);
		}

		// We should be able to set to an empty list, in which case the timestep
		// switches to -1 and the time to 0.0 (the defaults).
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Update the times and get the default values.
				timeComposite.setTimes(emptyList);
				timestep.set(timeComposite.getTimestep());
				time.set(timeComposite.getTime());
			}
		});
		assertEquals(-1, timestep.get());
		assertEquals(0.0, time.get(), epsilon);

		// Make sure an exception is thrown when the argument is null.
		SWTException e = catchSWTException(new Runnable() {
			@Override
			public void run() {
				timeComposite.setTimes(nullList);
			}
		});
		assertNotNull("TimeSliderComposite failure: Null argument exception "
				+ "not thrown for setTimes(List<Double>) when passed a "
				+ "null list.", e);
		assertEquals(SWT.ERROR_NULL_ARGUMENT, e.code);

		// Restore the original times.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setTimes(testTimes);
			}
		});

		return;
	}

	/**
	 * Checks that the widgets are enabled when they should be (when there is
	 * more than 1 time to pick from) and disabled otherwise.
	 */
	@Test
	public void checkWidgetsEnabled() {

		SWTBotScale scale = getTimeScale();
		SWTBotArrowButton nextButton = getTimeSpinnerNext();
		SWTBotArrowButton prevButton = getTimeSpinnerPrev();
		SWTBotButton playButton = getPlayPauseButton();
		SWTBotText text = getTimeText();

		final List<Double> goodTimes = new ArrayList<Double>();
		goodTimes.add(1.0);
		goodTimes.add(2.0);
		final List<Double> badTimes = new ArrayList<Double>();
		badTimes.add(1.0);

		// For a new time widget, all sub-widgets should be disabled (no times
		// have been set).
		final AtomicReference<TimeSliderComposite> testWidget;
		testWidget = new AtomicReference<TimeSliderComposite>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				testWidget.set(new TimeSliderComposite(getShell(), SWT.NONE));
			}
		});

		// None of them should be enabled. Also, the text widget should say N/A.
		SWTBot testBot = new SWTBot(testWidget.get());
		assertNotEnabled(testBot.scale());
		assertNotEnabled(testBot.arrowButton(0));
		assertNotEnabled(testBot.arrowButton(1));
		assertNotEnabled(testBot.button());
		assertNotEnabled(testBot.text());
		assertEquals(NO_TIMES, testBot.text().getText());

		// Dispose the temporary test widget.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				testWidget.get().dispose();
			}
		});

		// Initially, the test time widget's sub-widgets are enabled because the
		// times have been set.
		assertEnabled(scale);
		assertEnabled(nextButton);
		assertNotEnabled(prevButton); // First timestep... prev is disabled.
		assertEnabled(playButton);
		assertEnabled(text);

		// Setting the times to something with 1 value should disable them.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setTimes(badTimes);
			}
		});
		assertNotEnabled(scale);
		assertNotEnabled(nextButton);
		assertNotEnabled(prevButton);
		assertNotEnabled(playButton);
		assertNotEnabled(text);
		// The text widget's text should be set to the current value.
		assertEquals(badTimes.get(0).toString(), text.getText());

		// Setting the times to something with 2 values should enable them.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setTimes(goodTimes);
			}
		});
		assertEnabled(scale);
		assertEnabled(nextButton);
		assertNotEnabled(prevButton); // First timestep... prev is disabled.
		assertEnabled(playButton);
		assertEnabled(text);

		// Setting it to the last timestep should disable the "next" button.
		nextButton.click();
		assertNotEnabled(nextButton);
		assertEnabled(prevButton);
		// Setting it to the first timestep should disable the "prev" button.
		prevButton.click();
		assertEnabled(nextButton);
		assertNotEnabled(prevButton);

		// Setting the times to something with 0 values should also disable
		// them.
		badTimes.clear();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setTimes(badTimes);
			}
		});
		assertNotEnabled(scale);
		assertNotEnabled(nextButton);
		assertNotEnabled(prevButton);
		assertNotEnabled(playButton);
		assertNotEnabled(text);
		// The text widget's text should be set to N/A.
		assertEquals(NO_TIMES, text.getText());

		// Restore the times.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setTimes(testTimes);
			}
		});

		return;
	}

	/**
	 * Checks that you cannot add null selection listeners to the time widget.
	 */
	@Test
	public void checkNullSelectionListenerExceptions() {
		// Set up the helpful test failure message used if the exception is not
		// thrown.
		final String missedExceptionFormat = "TimeSliderComposite failure: "
				+ "Null argument exception not thrown for %s when passed a "
				+ "null selection listener.";

		final SelectionListener nullListener = null;

		SWTException e;

		// Check addSelectionListener(SelectionListener).
		e = catchSWTException(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(nullListener);
			}
		});
		assertNotNull(String.format(missedExceptionFormat,
				"addSelectionListener(SelectionListener)"), e);
		assertEquals(SWT.ERROR_NULL_ARGUMENT, e.code);

		// Check removeSelectionListener(SelectionListener).
		e = catchSWTException(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(nullListener);
			}
		});
		assertNotNull(String.format(missedExceptionFormat,
				"removeSelectionListener(SelectionListener)"), e);
		assertEquals(SWT.ERROR_NULL_ARGUMENT, e.code);

		return;
	}

	/**
	 * Checks that listeners are updated when the scale widget changes.
	 */
	@Test
	public void checkSelectionListenersByScale() {

		// Get the specific widget that will be used to set the time.
		SWTBotScale widget = getTimeScale();

		// Register the second fake listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(fakeListener2);
			}
		});

		// They should both be notified when the widget is used to change the
		// values.
		widget.setValue(widget.getValue() + widget.getIncrement());
		assertTrue(fakeListener1.wasNotified());
		assertTrue(fakeListener2.wasNotified());

		// Unregister this listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(fakeListener2);
			}
		});

		// It should not be notified when the widget changes, but the other
		// should still be notified.
		widget.setValue(widget.getValue() - widget.getIncrement());
		assertFalse(fakeListener2.wasNotified()); // Test this one first!
		assertTrue(fakeListener1.wasNotified());

		return;
	}

	/**
	 * Checks that listeners are updated when the spinner widget changes.
	 */
	@Test
	public void checkSelectionListenersBySpinner() {

		// Get the specific widget that will be used to set the time.
		SWTBotArrowButton nextButton = getTimeSpinnerNext();
		SWTBotArrowButton prevButton = getTimeSpinnerPrev();

		// Register the second fake listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(fakeListener2);
			}
		});

		// They should both be notified when the widget is used to change the
		// values.
		nextButton.click();
		assertTrue(fakeListener1.wasNotified());
		assertTrue(fakeListener2.wasNotified());

		// Unregister this listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(fakeListener2);
			}
		});

		// It should not be notified when the widget changes, but the other
		// should still be notified.
		prevButton.click();
		assertFalse(fakeListener2.wasNotified()); // Test this one first!
		assertTrue(fakeListener1.wasNotified());

		return;
	}

	/**
	 * Checks that listeners are periodically updated when the play button is
	 * clicked, and that notifications stop when the same (pause) button is
	 * clicked.
	 */
	@Test
	public void checkSelectionListenersByPlayButton() {

		// Get the specific widget that will be used to set the time.
		SWTBotButton widget = getPlayPauseButton();

		// Set the time to the last timestep. This will ensure the play action
		// hits the last timestep, then loops back around to the first.
		getTimeScale().setValue(orderedTimes.size() - 2);

		// Add a new test listener.
		final FakeListener listener = new FakeListener();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(listener);
			}
		});

		// Play for two seconds, then pause. Use a buffer of a half second in
		// case the SWTBot click beats the display thread scheduling.
		widget.click();
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			fail("TimeSliderCompositeTester error: "
					+ "Thread interrupted while testing play button.");
		}
		widget.click();

		// The listener should have been notified twice: two timesteps were
		// traversed while playing.
		assertTrue(listener.wasNotified(2));

		// Check the notification count and the times sent with each
		// notification.
		Queue<Double> times = listener.notificationTimes;
		assertEquals(2, times.size());
		assertEquals(orderedTimes.last(), times.poll(), epsilon);
		assertEquals(orderedTimes.first(), times.poll(), epsilon);

		// Remove the test listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(listener);
			}
		});

		return;
	}

	/**
	 * Checks that, when any of the time widgets are updated, the active play
	 * action is cancelled.
	 */
	@Test
	public void checkPauseOnNewSelection() {

		SWTBotButton playButton = getPlayPauseButton();

		SWTBotText text = getTimeText();
		String firstTime = text.getText();

		// Add a new test listener.
		final FakeListener listener = new FakeListener();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(listener);
			}
		});

		// Pause by clicking the play button. No notification sent.
		playButton.click();
		playButton.click();
		// Pause by clicking the time scale. 1 notification.
		playButton.click();
		getTimeScale().setValue(1);
		// Pause by typing text. 1 notification.
		playButton.click();
		text.selectAll();
		text.typeText(firstTime + SWT.CR + SWT.LF);
		// Pause by clicking the next button. 1 notification.
		playButton.click();
		getTimeSpinnerNext().click();
		// Pause by clicking the previous button. 1 notification.
		playButton.click();
		getTimeSpinnerPrev().click();

		// In the above test, the listener should have only been notified 4
		// times (when the other widgets, not including the play button, were
		// clicked).
		assertTrue(listener.wasNotified(4));
		// The times sent should also alternate between the second and first
		// timesteps, because each subsequent call undoes the previous one.
		Iterator<Double> iter = orderedTimes.iterator();
		double first = iter.next();
		double second = iter.next();
		assertEquals(second, listener.notificationTimes.poll());
		assertEquals(first, listener.notificationTimes.poll());
		assertEquals(second, listener.notificationTimes.poll());
		assertEquals(first, listener.notificationTimes.poll());

		// The listener should not be notified again.
		assertFalse(listener.wasNotified(5));

		// Remove the test listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(listener);
			}
		});

		return;
	}

	/**
	 * Checks that listeners are updated when the spinner's associated text
	 * widget changes.
	 */
	@Test
	public void checkSelectionListenersByText() {

		/*
		 * IMPORTANT NOTE ABOUT THIS TEST!
		 * 
		 * Simulating input into a Text widget with SWTBot is tricky.
		 * widget.setText(String) does not notify listeners, so you must use
		 * widget.selectAll() [highlights all the text] and
		 * widget.typeText(String) [replaces the text].
		 * 
		 * However, this can be finicky and fail if you are touching the
		 * keyboard or the mouse, so try running this test again when you are
		 * not actively using your computer's input. :)
		 */

		// Get the first and last time.
		Double firstTime = testTimes.get(0);
		Double lastTime = testTimes.get(testTimes.size() - 1);

		// Get the specific widget that will be used to set the time.
		SWTBotText widget = getTimeText();

		// Register the second fake listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(fakeListener2);
			}
		});

		// They should both be notified when the widget is used to change the
		// values.
		widget.selectAll();
		widget.typeText(lastTime.toString() + SWT.CR + SWT.LF);
		assertTrue(fakeListener1.wasNotified());
		assertTrue(fakeListener2.wasNotified());

		// Unregister this listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(fakeListener2);
			}
		});

		// It should not be notified when the widget changes, but the other
		// should still be notified.
		widget.selectAll();
		widget.typeText(firstTime.toString() + SWT.CR + SWT.LF);
		assertFalse(fakeListener2.wasNotified()); // Test this one first!
		assertTrue(fakeListener1.wasNotified());

		return;
	}

	/**
	 * Checks that the widget denies changes when the spinner's associated text
	 * widget is set to an invalid time.
	 */
	@Test
	public void checkInvalidInputIntoText() {

		// Get the text widget. We will try feeding it invalid times.
		SWTBotText widget = getTimeText();

		// Create a list of invalid times.
		List<String> invalidTimes = new ArrayList<String>();
		invalidTimes.add("");
		invalidTimes.add("infinite improbability");
		invalidTimes.add("    ");

		// Get the initial time.
		final AtomicReference<Double> time = new AtomicReference<Double>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				time.set(timeComposite.getTime());
			}
		});
		final double initialTime = time.get();

		for (String invalidTime : invalidTimes) {
			// Try setting an invalid time string.
			widget.setText(invalidTime);
			getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					time.set(timeComposite.getTime());
				}
			});
			// Ensure that the time did not change.
			assertEquals(initialTime, time.get(), epsilon);
		}

		return;
	}

	/**
	 * Checks that attempting to set or get fields on the widget from a non-UI
	 * thread throws an invalid thread access exception.
	 */
	@Test
	public void checkInvalidThreadAccessExceptions() {
		// Set up the helpful test failure message used if the exception is not
		// thrown.
		String missedExceptionFormat = "TimeSliderComposite failure: "
				+ "Invalid thread exception not thrown for %s when accessed "
				+ "from non-UI thread.";

		// Check getTimeStep().
		try {
			timeComposite.getTimestep();
			fail(String.format(missedExceptionFormat, "getTimestep()"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_THREAD_INVALID_ACCESS, e.code);
		}

		// Check getTime().
		try {
			timeComposite.getTime();
			fail(String.format(missedExceptionFormat, "getTime()"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_THREAD_INVALID_ACCESS, e.code);
		}

		// Check setTimes(List<Double>).
		try {
			timeComposite.setTimes(testTimes);
			fail(String.format(missedExceptionFormat, "setTimes(List<Double>)"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_THREAD_INVALID_ACCESS, e.code);
		}

		// Check addSelectionListener(SelectionListener).
		try {
			timeComposite.addSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat,
					"addSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_THREAD_INVALID_ACCESS, e.code);
		}

		// Check removeSelectionListener(SelectionListener).
		try {
			timeComposite.removeSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat,
					"removeSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_THREAD_INVALID_ACCESS, e.code);
		}

		return;
	}

	/**
	 * Checks that attempting to set or get fields on the widget throws a widget
	 * disposed exception if it is disposed.
	 */
	@Test
	public void checkWidgetDisposedExceptions() {
		// Set up the helpful test failure message used if the exception is not
		// thrown.
		String missedExceptionFormat = "TimeSliderComposite failure: "
				+ "Widget disposed exception not thrown for %s when accessed "
				+ "after widget is disposed.";

		// Create a disposed time widget.
		final AtomicReference<TimeSliderComposite> testCompositeRef;
		testCompositeRef = new AtomicReference<TimeSliderComposite>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				TimeSliderComposite composite;
				composite = new TimeSliderComposite(getShell(), SWT.NONE);
				composite.dispose();
				testCompositeRef.set(composite);
			}
		});

		// Check getTimeStep().
		try {
			testCompositeRef.get().getTimestep();
			fail(String.format(missedExceptionFormat, "getTimestep()"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_WIDGET_DISPOSED, e.code);
		}

		// Check getTime().
		try {
			testCompositeRef.get().getTime();
			fail(String.format(missedExceptionFormat, "getTime()"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_WIDGET_DISPOSED, e.code);
		}

		// Check setTimes(List<Double>).
		try {
			testCompositeRef.get().setTimes(testTimes);
			fail(String.format(missedExceptionFormat, "setTimes(List<Double>)"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_WIDGET_DISPOSED, e.code);
		}

		// Check addSelectionListener(SelectionListener).
		try {
			testCompositeRef.get().addSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat,
					"addSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_WIDGET_DISPOSED, e.code);
		}

		// Check removeSelectionListener(SelectionListener).
		try {
			testCompositeRef.get().removeSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat,
					"removeSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(SWT.ERROR_WIDGET_DISPOSED, e.code);
		}

		return;
	}

	/**
	 * Gets the SWTBot-wrapped scale widget for the play/pause button.
	 * 
	 * @return The wrapped play button.
	 */
	private SWTBotButton getPlayPauseButton() {
		return getBot().button();
	}

	/**
	 * Gets the SWTBot-wrapped scale widget for the time steps.
	 * 
	 * @return The wrapped scale widget.
	 */
	private SWTBotScale getTimeScale() {
		return getBot().scale();
	}

	/**
	 * Gets the SWTBot-wrapped button widget that jumps to the next timestep.
	 * 
	 * @return The wrapped next button.
	 */
	private SWTBotArrowButton getTimeSpinnerNext() {
		return getBot().arrowButton(0);
	}

	/**
	 * Gets the SWTBot-wrapped button widget that jumps to the previous
	 * timestep.
	 * 
	 * @return The wrapped previous button.
	 */
	private SWTBotArrowButton getTimeSpinnerPrev() {
		return getBot().arrowButton(1);
	}

	/**
	 * Gets the SWTBot-wrapped text widget for the time steps.
	 * 
	 * @return The wrapped text widget.
	 */
	private SWTBotText getTimeText() {
		return getBot().text();
	}

	/**
	 * A fake {@link SelectionListener} that sets its {@link #wasNotified} flag
	 * to true if notified of a selection event. Instead of checking the flag
	 * itself, use the method {@link #wasNotified()}, which waits to make sure
	 * the listener has time to update.
	 * 
	 * @author Jordan
	 *
	 */
	private class FakeListener extends SelectionAdapter {

		/**
		 * This can be used to count how many notifications have been sent to
		 * this listener.
		 */
		private final AtomicInteger notificationCount = new AtomicInteger();

		/**
		 * This can be used to determine what time was sent at each
		 * notification.
		 */
		private final Queue<Double> notificationTimes = new ConcurrentLinkedQueue<Double>();

		/**
		 * This flag is set by {@link #widgetSelected(SelectionEvent)}.
		 */
		private final AtomicBoolean wasNotified = new AtomicBoolean();

		/**
		 * The threshold of time (in milliseconds) to wait before returning.
		 */
		private static final long THRESHOLD = 3000;

		/*
		 * Overrides a method from SelectionAdapter.
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			notificationTimes.add((Double) e.data);
			notificationCount.incrementAndGet();
			wasNotified.set(true);
		}

		/**
		 * Determines whether the listener was notified of a selection event.
		 * This is safe to call from off the UI thread, and will wait up to
		 * {@link #THRESHOLD} milliseconds before returning the notified state.
		 * It will return sooner if the listener is notified sooner!
		 * 
		 * @return True if the listener was notified within the threshold, false
		 *         otherwise.
		 */
		private boolean wasNotified() {

			// See if the listener was notified.
			boolean notified = wasNotified.getAndSet(false);

			// If not, keep trying for a few seconds or until it is notified.
			long interval = 50;
			for (long sleepTime = 0; sleepTime < THRESHOLD && !notified; sleepTime += interval) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				notified = wasNotified.getAndSet(false);
			}
			return notified;
		}

		/**
		 * Waits up to {@link #THRESHOLD} milliseconds before returning whether
		 * or not the notification count is greater than or equal to the desired
		 * count. This will return sooner if the notification count is satisfied
		 * sooner!
		 * 
		 * @param count
		 *            The number of notifications to wait on.
		 * @return True if the notification count is at least the specified
		 *         count, false otherwise.
		 */
		private boolean wasNotified(int count) {
			long interval = 50;
			for (long sleepTime = 0; sleepTime < THRESHOLD
					&& notificationCount.get() < count; sleepTime += interval) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			return notificationCount.get() >= count;
		}
	}
}
