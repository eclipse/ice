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
 *   Jordan Deyton - bug 471166
 *   Jordan Deyton - bug 471248
 *   Jordan Deyton - bug 471749
 *   Jordan Deyton - bug 471750
 *   Jordan Deyton - bug 472304
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.eavp.viz.service.test.utils.AbstractSWTTester;
import org.eclipse.eavp.viz.service.widgets.TimeSliderComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotScale;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Ignore;
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

	// TODO Figure out how to test the context Menu that appears when the
	// options button is clicked.

	/**
	 * The time widget that will be tested. This gets initialized before and
	 * disposed after each test.
	 */
	private TimeSliderComposite timeComposite;

	/**
	 * A list of test times. This includes the values -1.0, 0.0, null, -2.0,
	 * 42.0, null, 0.0 (again) and 1337.1337. It should be able to be passed to
	 * the {@link #timeComposite}.
	 */
	private List<Double> testTimes;

	/**
	 * A list of the complete test times, ordered, and without nulls or
	 * duplicates. This means it should include the values (in this exact
	 * order): -2.0, -1.0, 0.0, 42.0, 1337.1337.
	 */
	private SortedSet<Double> orderedTimes;

	/**
	 * The expected size of {@link #testTimes}.
	 */
	private int testTimesSize;

	/**
	 * The string used in the time text widget when no times are available.
	 */
	private static final String NO_TIMES = "N/A";

	/**
	 * A fake listener to listen to selection events. Its notified flag is reset
	 * before each test.
	 */
	private FakeListener fakeListener1;
	/**
	 * A second fake listener to listen to selection events. Its notified flag
	 * is reset before each test.
	 */
	private FakeListener fakeListener2;

	/**
	 * The error of margin for double comparisons.
	 */
	private final static double epsilon = 1e-5;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester#beforeEachTest()
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();

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
				// Refresh the shell's layout so screenshots will show changes.
				getShell().layout();
			}
		});

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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester#afterEachTest()
	 */
	@Override
	public void afterEachTest() {

		// Dispose the time widget. The UI thread can do this whenever it gets a
		// chance.
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!timeComposite.isDisposed()) {
					timeComposite.dispose();
				}
				timeComposite = null;
				// Refresh the shell's layout so screenshots will show changes.
				getShell().layout();
			}
		});

		// Empty out the list of times.
		testTimes.clear();
		testTimes = null;

		super.afterEachTest();
	}

	/**
	 * Checks the default values for the time widget.
	 */
	@Test
	public void checkDefaults() {

		final AtomicInteger timestep = new AtomicInteger();
		final AtomicReference<Double> time = new AtomicReference<Double>();
		final AtomicReference<Double> fps = new AtomicReference<Double>();
		final AtomicBoolean isPlaying = new AtomicBoolean();

		// TODO Check that the first/last timestep buttons are disabled.

		// Create a temporary, blank test Composite, pull off its default
		// values, and dispose it.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				TimeSliderComposite c = new TimeSliderComposite(getShell(), SWT.NONE);
				timestep.set(c.getTimestep());
				time.set(c.getTime());
				fps.set(c.getFPS());
				isPlaying.set(c.isPlaying());
				c.dispose();
			}
		});

		// The default timestep is -1 (no times set).
		assertEquals(-1, timestep.get());
		// The default time is 0.0 (no times set).
		assertEquals(0.0, time.get(), epsilon);
		// The default framerate is 1 FPS.
		assertEquals(1.0, fps.get(), epsilon);
		// The widget should not be playing.
		assertFalse(isPlaying.get());

		return;
	}

	// ---- Public getter/setter tests ---- //
	/**
	 * Checks that all but a few certain setters will cause the widget to pause
	 * if it is currently playing.
	 */
	@Test
	public void checkPauseWhenSettersCalled() {

		// Check that the public setters also pause playback.
		final Queue<Boolean> playing = new LinkedList<Boolean>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Set the FPS to 1 frame every 10 seconds.
				timeComposite.setFPS(0.1);

				// We should be able to pause by sending the pause request.
				timeComposite.play();
				playing.add(timeComposite.isPlaying());
				timeComposite.pause();
				playing.add(timeComposite.isPlaying());

				// We can pause by calling setTime(double).
				timeComposite.play();
				playing.add(timeComposite.isPlaying());
				timeComposite.setTime(1000.0);
				playing.add(timeComposite.isPlaying());

				// We can pause by calling setTimes(List<Double>).
				List<Double> newTimes = new ArrayList<Double>(orderedTimes);
				newTimes.remove(0);
				timeComposite.play();
				playing.add(timeComposite.isPlaying());
				timeComposite.setTimes(newTimes);
				playing.add(timeComposite.isPlaying());

				// We can pause by calling setTimestep(int).
				timeComposite.play();
				playing.add(timeComposite.isPlaying());
				timeComposite.setTimestep(timeComposite.getTimestep() + 1);
				playing.add(timeComposite.isPlaying());

				// Setting the FPS shouldn't pause.
				timeComposite.play();
				playing.add(timeComposite.isPlaying());
				timeComposite.setFPS(0.05); // 1 frame every 20 seconds.
				playing.add(timeComposite.isPlaying());

				// Sending another play command shouldn't pause.
				timeComposite.play();
				playing.add(timeComposite.isPlaying());

				// Stop the widget.
				timeComposite.pause();

				return;
			}
		});

		// Check each attempt to pause the widget.

		// used pause()
		assertTrue(playing.poll());
		assertFalse(playing.poll());
		// used setTime(double)
		assertTrue(playing.poll());
		assertFalse(playing.poll());
		// used setTimes(List<Double>)
		assertTrue(playing.poll());
		assertFalse(playing.poll());
		// used setTimestep(int)
		assertTrue(playing.poll());
		assertFalse(playing.poll());
		// used setFPS(double) -- this shouldn't pause
		assertTrue(playing.poll());
		assertTrue(playing.poll());
		// used play() -- this shouldn't pause.
		assertTrue(playing.poll());

		return;
	}

	/**
	 * Checks the return values for the play, pause, and isPlaying methods.
	 * <p>
	 * It also checks that this method can trigger notifications and that the
	 * widgets are updated accordingly.
	 * </p>
	 */
	@Test
	public void checkPlayPauseMethods() {

		// Use queues to store return values and timesteps. This makes it a
		// little easier so we don't have to clear any collections.
		final Queue<Boolean> returnValues = new LinkedList<Boolean>();
		final Queue<Integer> timesteps = new LinkedList<Integer>();

		// Get the initial timestep. It should be 0. Try playing twice. Then try
		// pausing twice. Then get the resulting timestep.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timesteps.add(timeComposite.getTimestep());
				returnValues.add(timeComposite.play());
				returnValues.add(timeComposite.isPlaying());
				returnValues.add(timeComposite.play());
				returnValues.add(timeComposite.isPlaying());
				returnValues.add(timeComposite.pause());
				returnValues.add(timeComposite.isPlaying());
				returnValues.add(timeComposite.pause());
				returnValues.add(timeComposite.isPlaying());
				timesteps.add(timeComposite.getTimestep());
			}
		});

		// The timestep shouldn't have changed because we paused before
		// the first frame change.
		assertEquals(0, (int) timesteps.poll());
		assertEquals(0, (int) timesteps.poll());

		// The first call to play should return true, and the widget is playing.
		assertTrue(returnValues.poll());
		assertTrue(returnValues.poll());
		// The subsequent call to play should return false, but the widget is
		// still playing.
		assertFalse(returnValues.poll());
		assertTrue(returnValues.poll());

		// The first call to pause should return true, and the widget is not
		// playing.
		assertTrue(returnValues.poll());
		assertFalse(returnValues.poll());
		// The subsequent call to play should return false, but the widget is
		// still not playing.
		assertFalse(returnValues.poll());
		assertFalse(returnValues.poll());

		// The listener should not have been notified yet.
		assertFalse(fakeListener1.wasNotified());

		// Now trigger the play method and make sure the listener is notified.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setFPS(10);
				timeComposite.play();
			}
		});

		// The listener should be notified at least 10 times.
		assertTrue(fakeListener1.wasNotified(10));

		final AtomicInteger timestep = new AtomicInteger();
		final AtomicReference<Double> time = new AtomicReference<Double>();

		// Stop the widget.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.pause();

				// Get the current timestep and time.
				timestep.set(timeComposite.getTimestep());
				time.set(timeComposite.getTime());
			}
		});

		// Check the contents of the widgets.
		assertEquals(timestep.get(), getTimeScale().getValue());
		assertEquals(Double.toString(time.get()), getTimeText().getText());

		return;
	}

	/**
	 * Check the return values for setting the FPS as well as that the play rate
	 * changes accordingly.
	 */
	@Test
	public void checkSetFPS() {

		// Use queues for the FPS values.
		final Queue<Double> fpsValues = new LinkedList<Double>();
		final Queue<Boolean> returnValues = new LinkedList<Boolean>();

		// Try setting invalid FPS values.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				fpsValues.add(timeComposite.getFPS());
				returnValues.add(timeComposite.setFPS(0.0));
				fpsValues.add(timeComposite.getFPS());
				returnValues.add(timeComposite.setFPS(0.01));
				fpsValues.add(timeComposite.getFPS());
			}
		});

		// The value never should have changed from the default value of 1.
		assertEquals(1.0, fpsValues.poll(), epsilon);
		assertEquals(1.0, fpsValues.poll(), epsilon);
		assertEquals(1.0, fpsValues.poll(), epsilon);
		// Both set requests should have failed.
		assertFalse(returnValues.poll());
		assertFalse(returnValues.poll());

		// Try setting valid FPS values. Calls using the same value should
		// return false.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Use the default value first. We expect false and no change.
				returnValues.add(timeComposite.setFPS(1.0));
				fpsValues.add(timeComposite.getFPS());
				// Use a new value. We expect true and a change.
				returnValues.add(timeComposite.setFPS(10.0));
				fpsValues.add(timeComposite.getFPS());
				// Use the same value. We expect false and no change.
				returnValues.add(timeComposite.setFPS(10.0));
				fpsValues.add(timeComposite.getFPS());
				// Use a new value. We expect true and a change.
				returnValues.add(timeComposite.setFPS(20.0));
				fpsValues.add(timeComposite.getFPS());
				// Use the same value. We expect false and no change.
				returnValues.add(timeComposite.setFPS(20.0));
				fpsValues.add(timeComposite.getFPS());
			}
		});

		// The first call used the current FPS.
		assertFalse(returnValues.poll());
		assertEquals(1.0, fpsValues.poll(), epsilon);
		// We then tried to set it to a new value twice (second attempt fails).
		assertTrue(returnValues.poll());
		assertEquals(10.0, fpsValues.poll(), epsilon);
		assertFalse(returnValues.poll());
		assertEquals(10.0, fpsValues.poll(), epsilon);
		// We then set it to another new value twice (second attempt fails).
		assertTrue(returnValues.poll());
		assertEquals(20.0, fpsValues.poll(), epsilon);
		assertFalse(returnValues.poll());
		assertEquals(20.0, fpsValues.poll(), epsilon);

		// To make sure the FPS change takes effect, play for half a second,
		// then stop. We should get at least 10 notifications.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Play.
				timeComposite.play();
			}
		});
		// Check that the listener was notified more than once within a second.
		// This validates that the FPS actually changed because the default
		// number of notifications should be approximately 1 per second (1 FPS).
		assertTrue(fakeListener1.wasNotified(2, 1000));

		// Pause the widget.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.pause();
			}
		});

		return;
	}

	/**
	 * Checks the return values for setting and getting the looped playback
	 * property.
	 * <p>
	 * This test checks that looped playback can be disabled. Checking that
	 * enabled looped playback works is handled by
	 * {@link #checkPlayWidgetNotifiesSelectionListeners()}.
	 * </p>
	 */
	@Test
	public void checkSetLoop() {

		// Looped playback is already tested when

		// Try toggling the loop. Check the return value if the new value is the
		// same.
		final Queue<Boolean> queue = new LinkedList<Boolean>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				queue.add(timeComposite.getLoopPlayback());
				queue.add(timeComposite.setLoopPlayback(true));
				queue.add(timeComposite.getLoopPlayback());
				queue.add(timeComposite.setLoopPlayback(false));
				queue.add(timeComposite.getLoopPlayback());
			}
		});

		// Initially, playback is looped.
		assertTrue(queue.poll());
		// Since the value is not new, the return value when setting
		// loop-playback to true is false.
		assertFalse(queue.poll());
		// Playback is still looped.
		assertTrue(queue.poll());
		// The setting was changed, so the return value is true.
		assertTrue(queue.poll());
		// The playback should now be looped.
		assertFalse(queue.poll());

		// Set the framerate to something fast, then play.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setFPS(100);
				timeComposite.play();
			}
		});

		// The entire set of timesteps should have been traversed, and play
		// should have stopped.
		assertTrue(fakeListener1.wasNotified(orderedTimes.size() - 1));
		assertFalse(isPlaying());

		// Check that the current timestep is in fact the last one.
		final AtomicInteger timestep = new AtomicInteger();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timestep.set(timeComposite.getTimestep());
			}
		});
		assertEquals(orderedTimes.size() - 1, timestep.get());

		return;
	}

	/**
	 * Checks the return values for setting the time and that the current time
	 * for the widget changes accordingly.
	 * <p>
	 * It also checks that this method does not trigger notifications and that
	 * the widgets are updated accordingly.
	 * </p>
	 */
	@Test
	public void checkSetTime() {

		// Use queues for the time values.
		final Queue<Double> timeValues = new LinkedList<Double>();
		final Queue<Boolean> returnValues = new LinkedList<Boolean>();

		Iterator<Double> iter = orderedTimes.iterator();
		iter.next();
		final double secondTime = iter.next();

		// Try setting the time to the same (first) time, then a new time
		// (last), then the same time again. Then try two more attempts with the
		// second time.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Get the initial time.
				timeValues.add(timeComposite.getTime());
				// Set the same time value.
				returnValues.add(timeComposite.setTime(orderedTimes.first()));
				timeValues.add(timeComposite.getTime());
				// Set a new time value.
				returnValues.add(timeComposite.setTime(orderedTimes.last()));
				timeValues.add(timeComposite.getTime());
				// Set the same time value.
				returnValues.add(timeComposite.setTime(orderedTimes.last()));
				timeValues.add(timeComposite.getTime());
				// Set a new time value.
				returnValues.add(timeComposite.setTime(secondTime));
				timeValues.add(timeComposite.getTime());
				// Set the same time value.
				returnValues.add(timeComposite.setTime(secondTime));
				timeValues.add(timeComposite.getTime());
			}
		});

		// Check the initial value.
		assertEquals(orderedTimes.first(), timeValues.poll(), epsilon);
		// The first attempt failed because the value was not new.
		assertFalse(returnValues.poll());
		assertEquals(orderedTimes.first(), timeValues.poll(), epsilon);
		// The second attempt succeeded because the value was new.
		assertTrue(returnValues.poll());
		assertEquals(orderedTimes.last(), timeValues.poll(), epsilon);
		// The third attempt failed because the value was not new.
		assertFalse(returnValues.poll());
		assertEquals(orderedTimes.last(), timeValues.poll(), epsilon);
		// The fourth attempt succeeded because the value was new.
		assertTrue(returnValues.poll());
		assertEquals(secondTime, timeValues.poll(), epsilon);
		// The fifth attempt failed because the value was not new.
		assertFalse(returnValues.poll());
		assertEquals(secondTime, timeValues.poll(), epsilon);

		// The listener should not have been notified.
		assertFalse(fakeListener1.wasNotified());

		// Check the contents of the widgets.
		assertEquals(1, getTimeScale().getValue());
		assertEquals(Double.toString(secondTime), getTimeText().getText());

		return;
	}

	/**
	 * Checks that the set of allowed times set and fetched.
	 */
	@Test
	public void checkSetTimes() {

		final AtomicInteger timestep = new AtomicInteger();
		final AtomicReference<Double> time = new AtomicReference<Double>();

		final List<Double> emptyList = new ArrayList<Double>();
		final List<Double> nullList = null;

		// Get a copy of the ordered times.
		SortedSet<Double> orderedTimes = new TreeSet<Double>(this.orderedTimes);

		// Get the time scale widget.
		SWTBotScale widget = getTimeScale();
		SWTBotScale scale = getTimeScale();

		// Check that the list of times sent to the widget was not modified.
		assertEquals(
				"TimeSliderComposite failure: The collection passed into " + "setTimes(...) should not be modified!",
				testTimesSize, testTimes.size());

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
				+ "not thrown for setTimes(List<Double>) when passed a " + "null list.", e);
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
	 * Checks the return values for setting the timestep and that the current
	 * time for the widget changes accordingly.
	 * <p>
	 * It also checks that this method does not trigger notifications and that
	 * the widgets are updated accordingly.
	 * </p>
	 */
	@Test
	public void checkSetTimestep() {

		// Use queues for the time values.
		final Queue<Integer> timesteps = new LinkedList<Integer>();
		final Queue<Boolean> returnValues = new LinkedList<Boolean>();

		Iterator<Double> iter = orderedTimes.iterator();
		iter.next();
		final double secondTime = iter.next();
		final int size = orderedTimes.size();

		// Try setting the time to the same (first) time, then a new time
		// (last), then the same time again. Then try two more attempts with the
		// second time.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Get the initial time.
				timesteps.add(timeComposite.getTimestep());
				// Set the same time value.
				returnValues.add(timeComposite.setTimestep(0));
				timesteps.add(timeComposite.getTimestep());
				// Set a new time value.
				returnValues.add(timeComposite.setTimestep(size - 1));
				timesteps.add(timeComposite.getTimestep());
				// Set the same time value.
				returnValues.add(timeComposite.setTimestep(size - 1));
				timesteps.add(timeComposite.getTimestep());
				// Set a new time value.
				returnValues.add(timeComposite.setTimestep(1));
				timesteps.add(timeComposite.getTimestep());
				// Set the same time value.
				returnValues.add(timeComposite.setTimestep(1));
				timesteps.add(timeComposite.getTimestep());
			}
		});

		// Check the initial value.
		assertEquals(0, (int) timesteps.poll());
		// The first attempt failed because the value was not new.
		assertFalse(returnValues.poll());
		assertEquals(0, (int) timesteps.poll());
		// The second attempt succeeded because the value was new.
		assertTrue(returnValues.poll());
		assertEquals(size - 1, (int) timesteps.poll());
		// The third attempt failed because the value was not new.
		assertFalse(returnValues.poll());
		assertEquals(size - 1, (int) timesteps.poll());
		// The fourth attempt succeeded because the value was new.
		assertTrue(returnValues.poll());
		assertEquals(1, (int) timesteps.poll());
		// The fifth attempt failed because the value was not new.
		assertFalse(returnValues.poll());
		assertEquals(1, (int) timesteps.poll());

		// The listener should not have been notified.
		assertFalse(fakeListener1.wasNotified());

		// Check the contents of the widgets.
		assertEquals(1, getTimeScale().getValue());
		assertEquals(Double.toString(secondTime), getTimeText().getText());

		return;
	}

	/**
	 * Checks that attempting to set the timestep to an invalid index throws an
	 * IndexOutOfBoundsException.
	 */
	@Test
	public void checkSetTimestepIndexException() {

		// Use queues for the time values.
		final Queue<Integer> timesteps = new LinkedList<Integer>();
		final Queue<IndexOutOfBoundsException> exceptions = new LinkedList<IndexOutOfBoundsException>();

		// Get the max timestep index + 1.
		final int size = orderedTimes.size();

		// Try a timestep that is too low and one that is too high. Also try
		// setting the timestep when there are no times.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {

				// Try a timestep that is too low (negative).
				try {
					timeComposite.setTimestep(-1);
					exceptions.add(null);
				} catch (IndexOutOfBoundsException e) {
					exceptions.add(e);
				}

				// Try a timestep that is too high.
				try {
					timeComposite.setTimestep(size);
					exceptions.add(null);
				} catch (IndexOutOfBoundsException e) {
					exceptions.add(e);
				}

				// Get the current timestep.
				timesteps.add(timeComposite.getTimestep());

				// Try setting a timestep if there are no times set.
				timeComposite.setTimes(new ArrayList<Double>(0));
				try {
					timeComposite.setTimestep(0);
					exceptions.add(null);
				} catch (IndexOutOfBoundsException e) {
					exceptions.add(e);
				}

				// Get the current timestep.
				timesteps.add(timeComposite.getTimestep());

				return;
			}
		});

		// A timestep that is too low should throw an exception.
		assertNotNull(exceptions.poll());
		// A timestep that is too high should throw an exception.
		assertNotNull(exceptions.poll());
		// The timestep should still be 0.
		assertEquals(0, (int) timesteps.poll());

		// If there are no times, any index is invalid and should throw an index
		// exception.
		assertNotNull(exceptions.poll());
		// The timestep should, in this case, be -1.
		assertEquals(-1, (int) timesteps.poll());

		return;
	}
	// ------------------------------------ //

	// ---- Widget tests ---- //
	/**
	 * Checks that attempting to set or get fields on the widget from a non-UI
	 * thread throws an invalid thread access exception.
	 */
	@Test
	public void checkInvalidThreadAccessExceptions() {
		// Set up the helpful test failure message used if the exception is not
		// thrown.
		String missedExceptionFormat = "TimeSliderComposite failure: "
				+ "Invalid thread exception not thrown for %s when accessed " + "from non-UI thread.";
		final int errorCode = SWT.ERROR_THREAD_INVALID_ACCESS;

		// Check getFPS().
		try {
			timeComposite.getFPS();
			fail(String.format(missedExceptionFormat, "getFPS()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check getLoopPlayback().
		try {
			timeComposite.getLoopPlayback();
			fail(String.format(missedExceptionFormat, "getLoopPlayback()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check getTime().
		try {
			timeComposite.getTime();
			fail(String.format(missedExceptionFormat, "getTime()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check getTimeStep().
		try {
			timeComposite.getTimestep();
			fail(String.format(missedExceptionFormat, "getTimestep()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check isPlaying().
		try {
			timeComposite.isPlaying();
			fail(String.format(missedExceptionFormat, "isPlaying()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check pause().
		try {
			timeComposite.pause();
			fail(String.format(missedExceptionFormat, "pause()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check play().
		try {
			timeComposite.play();
			fail(String.format(missedExceptionFormat, "play()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setBackground(Color).
		try {
			timeComposite.setBackground(null);
			fail(String.format(missedExceptionFormat, "setBackground(Color)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setFPS(double).
		try {
			timeComposite.setFPS(1.0);
			fail(String.format(missedExceptionFormat, "setFPS(double)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setLoopPlayback(boolean).
		try {
			timeComposite.setLoopPlayback(false);
			fail(String.format(missedExceptionFormat, "setLoopPlayback(boolean)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setTime(double).
		try {
			timeComposite.setTime(1.0);
			fail(String.format(missedExceptionFormat, "setTime(double)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setTimes(List<Double>).
		try {
			timeComposite.setTimes(testTimes);
			fail(String.format(missedExceptionFormat, "setTimes(List<Double>)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setTimestep(int).
		try {
			timeComposite.setTimestep(0);
			fail(String.format(missedExceptionFormat, "setTimestep(int)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check addSelectionListener(SelectionListener).
		try {
			timeComposite.addSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat, "addSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check removeSelectionListener(SelectionListener).
		try {
			timeComposite.removeSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat, "removeSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		return;
	}

	/**
	 * Checks that clicking the first and last timestep buttons in the options
	 * menu update the timestep properly and notifies selection listeners.
	 */
	@Ignore
	@Test
	public void checkFirstAndLastStepButtonsNotifySelectionListeners() {
		fail("Not implemented. Figure out how to handle the context Menu.");

		// Check that the initial timestep is 0.
		assertEquals(0, getCurrentTimestep());

		// TODO Click the last timestep button in the context Menu.

		// Check that the listener was notified and the last timestep is the
		// current one.
		assertFalse(fakeListener1.wasNotified());
		assertEquals(orderedTimes.size() - 1, getCurrentTimestep());

		// TODO Click the last timestep button again.

		// Check that the listener was not notified and the timestep hasn't
		// changed.
		assertFalse(fakeListener1.wasNotified());
		assertEquals(orderedTimes.size() - 1, getCurrentTimestep());

		// TODO Click the first timestep button in the context Menu.

		// Check that the listener was notified and the first timestep is the
		// current one.
		assertTrue(fakeListener1.wasNotified());
		assertEquals(0, getCurrentTimestep());

		// TODO Click the first timestep button in the context Menu.

		// Check that the listener was not notified and the timestep hasn't
		// changed.
		assertFalse(fakeListener1.wasNotified());
		assertEquals(0, getCurrentTimestep());

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
				+ "Null argument exception not thrown for %s when passed a " + "null selection listener.";

		final SelectionListener nullListener = null;

		SWTException e;

		// Check addSelectionListener(SelectionListener).
		e = catchSWTException(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(nullListener);
			}
		});
		assertNotNull(String.format(missedExceptionFormat, "addSelectionListener(SelectionListener)"), e);
		assertEquals(SWT.ERROR_NULL_ARGUMENT, e.code);

		// Check removeSelectionListener(SelectionListener).
		e = catchSWTException(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(nullListener);
			}
		});
		assertNotNull(String.format(missedExceptionFormat, "removeSelectionListener(SelectionListener)"), e);
		assertEquals(SWT.ERROR_NULL_ARGUMENT, e.code);

		return;
	}

	/**
	 * Checks that, when any of the time widgets are updated, the active play
	 * action is cancelled.
	 */
	@Test
	public void checkPauseOnNewSelectionByWidget() {

		SWTBotButton playButton = getPlayPauseButton();

		SWTBotText text = getTimeText();
		String firstTime = text.getText();

		// Set the framerate to 1 frame every 10 seconds.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setFPS(0.1);
			}
		});

		// Pause by clicking the play button.
		playButton.click();
		assertTrue(isPlaying());
		playButton.click();
		assertFalse(isPlaying());

		// Pause by clicking the time scale.
		playButton.click();
		assertTrue(isPlaying());
		getTimeScale().setValue(1);
		assertFalse(isPlaying());

		// Pause by typing text.
		playButton.click();
		assertTrue(isPlaying());
		text.selectAll();
		text.typeText(firstTime + SWT.CR + SWT.LF);
		assertFalse(isPlaying());

		// Pause by clicking the next button.
		playButton.click();
		assertTrue(isPlaying());
		getNextButton().click();
		assertFalse(isPlaying());

		// Pause by clicking the previous button.
		playButton.click();
		assertTrue(isPlaying());
		getPrevButton().click();
		assertFalse(isPlaying());

		return;
	}

	/**
	 * Checks that listeners are periodically updated when the play button is
	 * clicked, and that notifications stop when the same (pause) button is
	 * clicked.
	 * <p>
	 * The times should also loop from last to first when the play button is
	 * clicked.
	 * </p>
	 */
	@Test
	public void checkPlayWidgetNotifiesSelectionListeners() {

		// Get the specific widget that will be used to set the time.
		SWTBotButton widget = getPlayPauseButton();

		// Set the time to the last timestep. This will ensure the play action
		// hits the last timestep, then loops back around to the first.
		getTimeScale().setValue(orderedTimes.size() - 2);

		// Set the FPS to 20.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.setFPS(20);
			}
		});

		// Play.
		widget.click();

		// The listener should have been notified at least three times, which
		// means the play button looped back around.
		assertTrue(fakeListener1.wasNotified(3));

		// Pause to stop notifications.
		widget.click();

		// Check the notification count and the times sent with each
		// notification.
		Queue<Double> times = fakeListener1.notificationTimes;
		assertTrue(times.size() >= 3);
		// Ignore the first value, as it is the second to last value which was
		// sent out when setting the time with the scale widget.
		times.poll();
		assertEquals(orderedTimes.last(), times.poll(), epsilon);
		assertEquals(orderedTimes.first(), times.poll(), epsilon);

		return;
	}

	/**
	 * Checks that listeners are updated when the scale widget changes.
	 */
	@Test
	public void checkScaleNotifiesSelectionListeners() {

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
	 * Checks that the FPS can be set via the options menu.
	 */
	@Ignore
	@Test
	public void checkSetFPSByOptionsMenu() {
		fail("Not implemented. Figure out how to handle context Menus first.");

		final AtomicReference<Double> fps = new AtomicReference<Double>();

		// Try setting the option for 12 fps.
		// TODO
		// Get the current framerate.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				fps.set(timeComposite.getFPS());
			}
		});
		// Check the current framerate.
		assertEquals(12.0, fps.get(), epsilon);

		// Try setting the option for 24 fps.
		// TODO
		// Get the current framerate.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				fps.set(timeComposite.getFPS());
			}
		});
		// Check the current framerate.
		assertEquals(24.0, fps.get(), epsilon);

		// Try setting the option for 30 fps.
		// TODO
		// Get the current framerate.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				fps.set(timeComposite.getFPS());
			}
		});
		// Check the current framerate.
		assertEquals(24.0, fps.get(), epsilon);

		// Try setting a custom framerate.
		final double customFPS = 100.0;
		// TODO
		// Get the current framerate.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				fps.set(timeComposite.getFPS());
			}
		});
		// Check the current framerate.
		assertEquals(customFPS, fps.get(), epsilon);

		return;
	}

	/**
	 * Checks that playback looping can be toggled via the options menu.
	 */
	@Ignore
	@Test
	public void checkSetLoopByOptionsMenu() {
		fail("Not implemented. Figure out how to handle the context Menu.");

		final AtomicBoolean isLooped = new AtomicBoolean();

		// TODO Click the loop button in the options context Menu.
		// Check that looped playback has been disabled.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				isLooped.set(timeComposite.getLoopPlayback());
			}
		});
		assertFalse(isLooped.get());

		// TODO Click the loop button in the options context Menu.
		// Check that looped playback has been enabled.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				isLooped.set(timeComposite.getLoopPlayback());
			}
		});
		assertTrue(isLooped.get());

		return;
	}

	/**
	 * Checks that listeners are updated when the spinner widget changes.
	 * <p>
	 * The times should also loop between last and first when the next/previous
	 * button is clicked.
	 * </p>
	 */
	@Test
	public void checkSpinnerNotifiesSelectionListeners() {

		// Get the specific widget that will be used to set the time.
		SWTBotButton nextButton = getNextButton();
		SWTBotButton prevButton = getPrevButton();

		// Register the second fake listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.addSelectionListener(fakeListener2);
			}
		});

		// They should both be notified when the widget is used to change the
		// values.
		prevButton.click();
		assertTrue(fakeListener1.wasNotified());
		assertTrue(fakeListener2.wasNotified());

		// The previous button should have looped around to the last time.
		assertEquals(orderedTimes.last(), fakeListener1.notificationTimes.poll(), epsilon);

		// Unregister the second fake listener.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.removeSelectionListener(fakeListener2);
			}
		});

		// It should not be notified when the widget changes, but the other
		// should still be notified.
		nextButton.click();
		assertFalse(fakeListener2.wasNotified()); // Test this one first!
		assertTrue(fakeListener1.wasNotified());

		// The next button should have looped around to the first time.
		assertEquals(orderedTimes.first(), fakeListener1.notificationTimes.poll(), epsilon);

		return;
	}

	/**
	 * Checks that listeners are updated when the spinner's associated text
	 * widget changes.
	 */
	@Test
	public void checkTextNotifiesSelectionListeners() {

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
	public void checkTextRejectsInvalidInput() {

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
			widget.selectAll();
			widget.typeText(invalidTime + SWT.CR + SWT.LF);
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
	 * Checks that attempting to set or get fields on the widget throws a widget
	 * disposed exception if it is disposed.
	 */
	@Test
	public void checkWidgetDisposedExceptions() {
		// Set up the helpful test failure message used if the exception is not
		// thrown.
		String missedExceptionFormat = "TimeSliderComposite failure: "
				+ "Widget disposed exception not thrown for %s when accessed " + "after widget is disposed.";
		final int errorCode = SWT.ERROR_WIDGET_DISPOSED;

		// Dispose the test time widget.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.dispose();
			}
		});

		// Check getFPS().
		try {
			timeComposite.getFPS();
			fail(String.format(missedExceptionFormat, "getFPS()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check getLoopPlayback().
		try {
			timeComposite.getLoopPlayback();
			fail(String.format(missedExceptionFormat, "getLoopPlayback()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check getTime().
		try {
			timeComposite.getTime();
			fail(String.format(missedExceptionFormat, "getTime()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check getTimeStep().
		try {
			timeComposite.getTimestep();
			fail(String.format(missedExceptionFormat, "getTimestep()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check isPlaying().
		try {
			timeComposite.isPlaying();
			fail(String.format(missedExceptionFormat, "isPlaying()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check pause().
		try {
			timeComposite.pause();
			fail(String.format(missedExceptionFormat, "pause()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check play().
		try {
			timeComposite.play();
			fail(String.format(missedExceptionFormat, "play()"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setBackground(Color).
		try {
			timeComposite.setBackground(null);
			fail(String.format(missedExceptionFormat, "setBackground(Color)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setFPS(double).
		try {
			timeComposite.setFPS(1.0);
			fail(String.format(missedExceptionFormat, "setFPS(double)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setLoopPlayback(boolean).
		try {
			timeComposite.setLoopPlayback(false);
			fail(String.format(missedExceptionFormat, "setLoopPlayback(boolean)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setTime(double).
		try {
			timeComposite.setTime(1.0);
			fail(String.format(missedExceptionFormat, "setTime(double)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setTimes(List<Double>).
		try {
			timeComposite.setTimes(testTimes);
			fail(String.format(missedExceptionFormat, "setTimes(List<Double>)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check setTimestep(int).
		try {
			timeComposite.setTimestep(0);
			fail(String.format(missedExceptionFormat, "setTimestep(int)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check addSelectionListener(SelectionListener).
		try {
			timeComposite.addSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat, "addSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		// Check removeSelectionListener(SelectionListener).
		try {
			timeComposite.removeSelectionListener(fakeListener1);
			fail(String.format(missedExceptionFormat, "removeSelectionListener(SelectionListener)"));
		} catch (SWTException e) {
			assertEquals(errorCode, e.code);
		}

		return;
	}

	/**
	 * Checks that the widgets are enabled when they should be (when there is
	 * more than 1 time to pick from) and disabled otherwise.
	 */
	@Test
	public void checkWidgetsEnabled() {

		SWTBotScale scale = getTimeScale();
		SWTBotButton nextButton = getNextButton();
		SWTBotButton prevButton = getPrevButton();
		SWTBotButton playButton = getPlayPauseButton();
		SWTBotButton optionsButton = getOptionsButton();
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
		assertNotEnabled(testBot.button(0));
		assertNotEnabled(testBot.button(1));
		assertNotEnabled(testBot.button(2));
		assertEnabled(testBot.button(3)); // The options are always enabled.
		assertNotEnabled(testBot.text());
		// TODO Check that the first/last timestep buttons are disabled.
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
		assertEnabled(prevButton);
		assertEnabled(playButton);
		assertEnabled(optionsButton); // The options are always enabled.
		assertEnabled(text);
		// TODO Check that the first/last timestep buttons are enabled.

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
		assertEnabled(optionsButton); // The options are always enabled.
		assertNotEnabled(text);
		// TODO Check that the first/last timestep buttons are disabled.
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
		assertEnabled(prevButton);
		assertEnabled(playButton);
		assertEnabled(optionsButton); // The options are always enabled.
		assertEnabled(text);
		// TODO Check that the first/last timestep buttons are enabled.

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
		assertEnabled(optionsButton); // The options are always enabled.
		assertNotEnabled(text);
		// TODO Check that the first/last timestep buttons are disabled.
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
	 * Checks that helpful tool tips are set on all of the child widgets.
	 */
	@Test
	public void checkWidgetAppearance() {

		// Get the widgets.
		SWTBotScale scale = getTimeScale();
		final SWTBotButton nextButton = getNextButton();
		final SWTBotButton prevButton = getPrevButton();
		final SWTBotButton playButton = getPlayPauseButton();
		SWTBotText text = getTimeText();

		// The buttons should not have text.
		assertTrue(prevButton.getText().isEmpty());
		assertTrue(playButton.getText().isEmpty());
		assertTrue(nextButton.getText().isEmpty());

		// Check that the buttons use images.
		final List<Image> images = new ArrayList<Image>(3);
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				images.add(prevButton.widget.getImage());
				images.add(playButton.widget.getImage());
				images.add(nextButton.widget.getImage());
			}
		});
		assertNotNull("\"prev\" button is missing an image.", images.get(0));
		assertNotNull("\"play\" button is missing an image.", images.get(1));
		assertNotNull("\"next\" button is missing an image.", images.get(2));

		// Check the tool tips.
		assertEquals("Previous", prevButton.getToolTipText());
		assertEquals("Play", playButton.getToolTipText());
		assertEquals("Next", nextButton.getToolTipText());
		assertEquals("Traverses the timesteps", scale.getToolTipText());
		assertEquals("The current time", text.getToolTipText());

		// Check the tool tip before and after the play button is toggled.
		playButton.click();
		assertEquals("Pause", playButton.getToolTipText());
		playButton.click();
		assertEquals("Play", playButton.getToolTipText());

		return;
	}
	// ---------------------- //

	// ---- Convenient private getters. ---- //
	/**
	 * Gets the SWTBot-wrapped button widget that jumps to the next timestep.
	 * 
	 * @return The wrapped next button.
	 */
	private SWTBotButton getNextButton() {
		return getBot().button(2);
	}

	/**
	 * Gets the SWTBot-wrapped button widget that opens widget options.
	 * 
	 * @return The wrapped options button.
	 */
	private SWTBotButton getOptionsButton() {
		return getBot().button(3);
	}

	/**
	 * Gets the SWTBot-wrapped scale widget for the play/pause button.
	 * 
	 * @return The wrapped play button.
	 */
	private SWTBotButton getPlayPauseButton() {
		return getBot().button(1);
	}

	/**
	 * Gets the SWTBot-wrapped button widget that jumps to the previous
	 * timestep.
	 * 
	 * @return The wrapped previous button.
	 */
	private SWTBotButton getPrevButton() {
		return getBot().button(0);
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
	 * Gets the SWTBot-wrapped text widget for the time steps.
	 * 
	 * @return The wrapped text widget.
	 */
	private SWTBotText getTimeText() {
		return getBot().text();
	}

	/**
	 * A convenient method for checking, on the UI thread, the result of
	 * {@link TimeSliderComposite#isPlaying()}.
	 * 
	 * @return True if the time widget is playing, false otherwise.
	 */
	private boolean isPlaying() {
		final AtomicBoolean isPlaying = new AtomicBoolean();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				isPlaying.set(timeComposite.isPlaying());
			}
		});
		return isPlaying.get();
	}

	/**
	 * A convenient method for getting the {@link #timeComposite}'s current
	 * timestep index.
	 * 
	 * @return The timestep.
	 */
	private int getCurrentTimestep() {
		final AtomicInteger timestep = new AtomicInteger();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timestep.set(timeComposite.getTimestep());
			}
		});
		return timestep.get();
	}
	// ------------------------------------- //

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
		 * (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
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
			for (long sleepTime = 0; sleepTime < THRESHOLD && notificationCount.get() < count; sleepTime += interval) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			return notificationCount.get() >= count;
		}

		/**
		 * Waits up to {@link #THRESHOLD} milliseconds before returning whether
		 * or not the notification count is greater than or equal to the desired
		 * count. This will return sooner if the notification count is satisfied
		 * sooner!
		 * 
		 * @param count
		 *            The number of notifications to wait on.
		 * @param totalThreshold
		 *            The total amount of time this request will wait.
		 * @return True if the notification count is at least the specified
		 *         count, false otherwise.
		 */
		private boolean wasNotified(int count, long totalThreshold) {
			long interval = 50;
			for (long sleepTime = 0; sleepTime < totalThreshold
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
