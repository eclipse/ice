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
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit.widgets;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

/**
 * This class provides a widget with three separate means of selecting times
 * from a list of allowed times:
 * <ul>
 * <li>dragging a {@link Scale} widget (coarse-grained control)</li>
 * <li>pressing arrow keys (fine-grained control)</li>
 * <li>typing a value near an existing timestep (very fine-grained control)</li>
 * </ul>
 * As input, the widget takes an ordered list of doubles. If the input list is
 * <i>not</i> in ascending order, then the widget will order them on its own and
 * provide timesteps based on the ordered list.
 * <p>
 * To receive notifications of changes to the selected timestep, a
 * {@link SelectionListener} must be registered as with a typical SWT widget.
 * Notifications will be posted on the UI thread.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class TimeSliderComposite extends Composite {

	/**
	 * The widget used for coarse-grained timestep control.
	 */
	private final Scale scale;
	/**
	 * The text widget used for exact timestep control.
	 */
	private final Text text;
	/**
	 * Sets the timestep to the next available timestep. This is a fine-grained
	 * control.
	 */
	private final Button nextButton;
	/**
	 * Sets the timestep to the previous available timestep. This is a
	 * fine-grained control.
	 */
	private final Button prevButton;
	/**
	 * Starts or pauses playback of the timesteps.
	 */
	private final Button playButton;

	/**
	 * The playback interval or framerate is 1 second.
	 */
	private static final int playbackInterval = 1;
	/**
	 * Whether or not the playback operation is currently running.
	 */
	private boolean isPlaying = false;
	/**
	 * The runnable operation used for "playback". It increments the timestep
	 * and schedules itself to execute later based on the value of
	 * {@link #playbackInterval}.
	 */
	private Runnable playbackRunnable;

	/**
	 * The current timestep, or -1 if there are no times available.
	 */
	private int timestep;
	/**
	 * A tree that contains the times associated with the timesteps.
	 */
	private BinarySearchTree times;

	/**
	 * The image used for the "next" button.
	 */
	private static Image nextImage;
	/**
	 * The image used for the "pause" button.
	 */
	private static Image pauseImage;
	/**
	 * The image used for the "play" button.
	 */
	private static Image playImage;
	/**
	 * The image used for the "previous" button.
	 */
	private static Image prevImage;

	/**
	 * The string to use in the text box when there are no times configured.
	 */
	private static final String NO_TIMES = "N/A";

	/**
	 * A list of listeners that will be notified if the timestep changes to a
	 * new value.
	 */
	private final List<SelectionListener> listeners;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public TimeSliderComposite(Composite parent, int style) {
		super(parent, style);

		// Iniitalize the list of selection listeners.
		listeners = new ArrayList<SelectionListener>();

		// Create the widgets.
		prevButton = createPrevButton(this);
		playButton = createPlayButton(this);
		nextButton = createNextButton(this);
		text = createText(this);
		scale = createScale(this);

		// Layout the widgets. The scale should take up all horizontal space on
		// the right. The text widget should grab whatever space remains, while
		// the normal buttons take up only the space they require.
		setLayout(new GridLayout(5, false));
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
		prevButton.setLayoutData(gridData);
		playButton.setLayoutData(GridDataFactory.copyData(gridData));
		nextButton.setLayoutData(GridDataFactory.copyData(gridData));
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		// The default focus should be on the play button.
		playButton.setFocus();

		// Refresh the widgets. This will enable/disable them as necessary.
		setTimes(new ArrayList<Double>());

		return;
	}

	/**
	 * Creates the "next" button that increments the timestep.
	 * 
	 * @param parent
	 *            The parent Composite for this widget. Assumed not to be
	 *            {@code null}.
	 * @return The new widget.
	 */
	private Button createNextButton(Composite parent) {
		Button nextButton = new Button(parent, SWT.PUSH);

		// When the button is clicked, playback should be stopped and the
		// timestep should be incremented.
		nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Disable playback.
				setPlayback(false, e);

				// Increment the timestep.
				if (incrementTimestep()) {
					notifyListeners(e);
				}
			}
		});

		// Load the button image as necessary.
		if (nextImage == null) {
			nextImage = loadImage("/nav_forward.gif");
		}

		// Set the initial tool tip and image.
		nextButton.setToolTipText("Next");
		nextButton.setImage(nextImage);

		return nextButton;
	}

	/**
	 * Creates the "play" button that toggles the playback operation.
	 * 
	 * @param parent
	 *            The parent Composite for this widget. Assumed not to be
	 *            {@code null}.
	 * @return The new widget.
	 */
	private Button createPlayButton(Composite parent) {
		Button playButton = new Button(parent, SWT.PUSH);

		// When the button is clicked, playback should be toggled.
		playButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Toggle playback.
				setPlayback(!isPlaying, e);
			}
		});

		// Load the play and pause button images as necessary.
		if (playImage == null) {
			playImage = loadImage("/nav_go.gif");
		}
		if (pauseImage == null) {
			pauseImage = loadImage("/suspend_co.gif");
		}

		// Set the initial tool tip and image.
		playButton.setToolTipText("Play");
		playButton.setImage(playImage);

		return playButton;
	}

	/**
	 * Creates the "previous" button that deccrements the timestep.
	 * 
	 * @param parent
	 *            The parent Composite. Assumed not to be {@code null}.
	 * @return The new button.
	 */
	private Button createPrevButton(Composite parent) {
		Button prevButton = new Button(parent, SWT.PUSH);

		// When the button is clicked, playback should be stopped and the
		// timestep should be decremented.
		prevButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Disable playback.
				setPlayback(false, e);

				// Decrement the timestep.
				if (decrementTimestep()) {
					notifyListeners(e);
				}
			}
		});

		// Load the button image as necessary.
		if (prevImage == null) {
			prevImage = loadImage("/nav_backward.gif");
		}

		// Set the initial tool tip and image.
		prevButton.setToolTipText("Previous");
		prevButton.setImage(prevImage);

		return prevButton;
	}

	/**
	 * Creates the scale or slider widget that can be used to quickly traverse
	 * the timesteps.
	 * 
	 * @param parent
	 *            The parent Composite for this widget. Assumed not to be
	 *            {@code null}.
	 * @return The new widget.
	 */
	private Scale createScale(Composite parent) {

		final Scale scale = new Scale(this, SWT.HORIZONTAL);
		scale.setMinimum(0);
		scale.setIncrement(1);
		scale.setMaximum(0);
		scale.setToolTipText("Traverses the timesteps");

		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Disable playback.
				setPlayback(false, e);

				// Get the timestep from the scale widget.
				if (setTimestep(scale.getSelection())) {
					notifyListeners(e);
				}
			}
		});

		return scale;
	}

	/**
	 * Creates the text widget for setting this time widget to a specific time.
	 * 
	 * @param parent
	 *            The parent Composite for this widget. Assumed not to be
	 *            {@code null}.
	 * @return The new widget.
	 */
	private Text createText(Composite parent) {

		final Text text = new Text(this, SWT.SINGLE | SWT.BORDER);

		text.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Disable playback.
				setPlayback(false, e);

				// Try to find the value from the text widget's new text.
				try {
					double value = Double.parseDouble(text.getText());
					// Set the value to the nearest allowed time.
					if (setTimestep(times.findNearestIndex(value))) {
						notifyListeners(e);
					} else {
						// Update the text to the set time regardless of whether
						// it changed. This lets the user know their input was
						// accepted.
						text.setText(Double.toString(getTime()));
					}
				} catch (NumberFormatException exception) {
					// If the number was invalid, revert to the previous text.
					text.setText(Double.toString(getTime()));
				}

				return;
			}
		});

		// Tweak the default appearance.
		text.setFont(parent.getFont());

		// Set the initial tool tip.
		text.setToolTipText("The current time");

		return text;
	}

	/**
	 * A convenient operation for loading a custom image resource from this
	 * widget's path.
	 * 
	 * @param name
	 *            The name of (path to) the image to load, like "/image1.gif".
	 * @return The loaded image, or {@code null} if the image could not be
	 *         loaded.
	 */
	private Image loadImage(String name) {
		InputStream in = TimeSliderComposite.class.getResourceAsStream(name);
		return new Image(getDisplay(), in);
	}

	/*
	 * Overrides a method from Control.
	 */
	@Override
	public void setBackground(Color color) {
		super.setBackground(color);

		// Update the background colors for all child widgets.
		scale.setBackground(color);
		playButton.setBackground(color);
		nextButton.setBackground(color);
		prevButton.setBackground(color);

		return;
	}

	/**
	 * Updates the timestep and all widgets based on the new timestep value.
	 * 
	 * @param newValue
	 *            The new value. Assumed to be valid.
	 * @return True if the timestep changed, false otherwise.
	 */
	private boolean setTimestep(int newValue) {
		boolean changed = false;

		if (newValue != timestep) {
			changed = true;
			// Update the timestep.
			timestep = newValue;

			// Update the selections on the widgets.
			scale.setSelection(timestep);
			text.setText(Double.toString(times.get(timestep)));
		}

		return changed;
	}

	/**
	 * Increments the timestep. Updates all widgets as necessary. This will loop
	 * back around to the first timestep if necessary.
	 * 
	 * @return True if the timestep changed, false otherwise.
	 */
	private boolean incrementTimestep() {
		return setTimestep((timestep + 1) % times.size());
	}

	/**
	 * Decrements the timestep. Updates all widgets as necessary. This will loop
	 * back around to the last timestep if necessary.
	 * 
	 * @return True if the timestep changed, false otherwise.
	 */
	private boolean decrementTimestep() {
		int size = times.size();
		return setTimestep((timestep - 1 + size) % size);
	}

	/**
	 * Sets the list of timesteps used by this widget.
	 * 
	 * @param times
	 *            The list of timesteps. Null values and duplicates will be
	 *            ignored. The list will be ordered.
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the list of times is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTimes(List<Double> times) {
		// Check that this widget can be accessed. Also check the list is not
		// null.
		checkWidget();
		if (times == null) {
			throw new SWTException(SWT.ERROR_NULL_ARGUMENT);
		}

		// Try to recreate the tree of times based on the new list of times.
		try {
			this.times = new BinarySearchTree(times);
			timestep = 0;
		}
		// If the list has no non-null values, set the timestep to -1.
		catch (IllegalArgumentException e) {
			this.times = null;
			timestep = -1;
		}

		// Refresh the widgets.
		final int size = this.times != null ? this.times.size() : 0;
		boolean widgetsEnabled = size > 1;

		// Enable/disable the widgets as necessary.
		scale.setEnabled(widgetsEnabled);
		text.setEnabled(widgetsEnabled);
		nextButton.setEnabled(widgetsEnabled);
		prevButton.setEnabled(widgetsEnabled);
		playButton.setEnabled(widgetsEnabled);

		// Refresh the scale widget's max value.
		scale.setMaximum(widgetsEnabled ? size - 1 : 0);

		// Reset the selection of the widgets.
		scale.setSelection(0);
		text.setText(size > 0 ? Double.toString(getTime()) : NO_TIMES);

		return;
	}

	/**
	 * Gets the currently selected timestep (the index of the selected time).
	 * 
	 * @return The selected timestep.
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getTimestep() {
		// Check that this widget can be accessed.
		checkWidget();
		return timestep;
	}

	/**
	 * Gets the currently selected time.
	 * 
	 * @return The selected time.
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public double getTime() {
		// Check that this widget can be accessed.
		checkWidget();
		return timestep >= 0 ? times.get(timestep) : 0.0;
	}

	/**
	 * Starts or stops the playback operation.
	 * 
	 * @param play
	 *            If true, playback will be started. If false, playback will be
	 *            stopped.
	 * @param e
	 *            The selection event that triggered the playback operation.
	 *            This is only used if play is true. Otherwise, you may use
	 *            {@code null}.
	 */
	private void setPlayback(boolean play, final SelectionEvent e) {
		if (play != this.isPlaying) {
			this.isPlaying = play;

			// Determine the text and image for the play/pause button as well as
			// whether the playback event should be scheduled or cancelled.
			final String text;
			final Image image;
			final int time;
			if (play) {
				// Set the text for the pause button.
				text = "Pause";
				image = pauseImage;

				// The playback runnable should be created.
				time = playbackInterval * 1000;
				playbackRunnable = new Runnable() {
					@Override
					public void run() {
						getDisplay().timerExec(time, this);
						if (incrementTimestep()) {
							notifyListeners(e);
						}
					}
				};
			} else {
				// Set the text for the play button.
				text = "Play";
				image = playImage;

				// The playback runnable should be cancelled.
				time = -1;
			}

			// Update the tool tip and image for the play button.
			playButton.setToolTipText(text);
			playButton.setImage(image);

			// Schedule or cancel the playback task.
			getDisplay().timerExec(time, playbackRunnable);
		}
		return;
	}

	/**
	 * Adds a new listener to be notified when the selected time changes.
	 * 
	 * @param listener
	 *            The new listener to add.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void addSelectionListener(SelectionListener listener) {
		// Check that this widget can be accessed. Also check the listener is
		// not null.
		checkWidget();
		if (listener == null) {
			throw new SWTException(SWT.ERROR_NULL_ARGUMENT);
		}

		listeners.add(listener);
	}

	/**
	 * Removes the specified listener from this widget. It will no longer be
	 * notified of time changes from this widget unless it has been registered
	 * multiple times.
	 * 
	 * @param listener
	 *            The listener to remove.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeSelectionListener(SelectionListener listener) {
		// Check that this widget can be accessed. Also check the listener is
		// not null.
		checkWidget();
		if (listener == null) {
			throw new SWTException(SWT.ERROR_NULL_ARGUMENT);
		}
		listeners.remove(listener);
	}

	/**
	 * Notifies listeners that the selected time has changed. This is performed
	 * on the UI thread as with all SWT selection events.
	 * 
	 * @param e
	 *            The event triggering the updated time.
	 */
	private void notifyListeners(SelectionEvent e) {
		e.data = getTime();
		for (SelectionListener listener : listeners) {
			listener.widgetSelected(e);
		}
	}

}
