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
package org.eclipse.ice.viz.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a basic, limited implementation of certain features of
 * {@link IPlot}. This class does not manage the data source, categories, or
 * dependent series. It also does not handle drawing the plot.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractPlot implements IPlot {

	/**
	 * The current independent series for the plot.
	 */
	private ISeries independentSeries = null;

	/**
	 * A lock for synchronizing access to both the {@link #plotListeners} and
	 * {@link #notificationThread} scheduling.
	 */
	private final Lock notificationLock = new ReentrantLock();

	/**
	 * A service for scheduling threads that notify registered plot listeners.
	 */
	private ExecutorService notificationThread;

	/**
	 * Plot listeners that can be updated based on some key/value pair. The
	 * default implementation does not trigger any such events, but sub-classes
	 * may use it in case of data reloads or synchronization with remote
	 * servers.
	 */
	private final List<IPlotListener> plotListeners = new LinkedList<IPlotListener>();

	/**
	 * The title for the plot.
	 */
	private String title = null;

	/**
	 * The current data source.
	 */
	private URI uri;

	/**
	 * Adds the specified listener to the plot to receive updates. The same
	 * listener cannot be added twice.
	 * 
	 * @param listener
	 *            The listener to add. {@code null} cannot be added.
	 * @return True if the listener was added, false otherwise.
	 */
	public boolean addPlotListener(IPlotListener listener) {
		boolean added = false;
		// Use the notification lock to synchronize access to the list.
		notificationLock.lock();
		try {
			if (listener != null && !plotListeners.contains(listener)) {
				plotListeners.add(listener);
				added = true;
			}
		} finally {
			notificationLock.unlock();
		}
		return added;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		throw new UnsupportedOperationException(
				"IPlot error: " + "This plot cannot be drawn.");
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public int getNumberOfAxes() {
		return 0;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>();
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Nothing to do.
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public URI getDataSource() {
		return uri;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public String getSourceHost() {
		String host = null;
		URI source = getDataSource();
		if (source != null) {
			host = source.getHost();
			if (host == null) {
				host = "localhost";
			}
		}
		return host;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public boolean isSourceRemote() {
		String host = getSourceHost();
		return host != null ? !"localhost".equals(host) : false;
	}

	/**
	 * Notifies plot listeners that a change has occurred. Work is performed on
	 * a separate worker thread, but notifications are also sent out in the same
	 * order in which they were scheduled.
	 * 
	 * @param key
	 *            A key for the type of update event.
	 * @param value
	 *            The value for the update event.
	 */
	protected void notifyPlotListeners(String key, String value) {

		/*
		 * In order to ensure that notifications are sent out in order on a
		 * separate thread, we assign a new task for each listener to a shared,
		 * single worker thread using an ExecutorService. If work has completed
		 * and there are no new tasks, the worker thread is closed.
		 */

		// Get references needed for notifying listeners.
		final IPlot plotRef = this;
		final String keyRef = key;
		final String valueRef = value;

		// Use the notification lock to synchronize access to the list and to
		// the notification thread (to prevent tasks from being submitted after
		// shutdown).
		notificationLock.lock();
		try {
			if (!plotListeners.isEmpty()) {
				// Create the notification thread as necessary.
				if (notificationThread == null) {
					notificationThread = Executors.newSingleThreadExecutor();
				}

				/*
				 * Submit a tasks to notify each worker. If a particular task
				 * throws an exception, it should not prevent other listeners
				 * from being notified.
				 */

				for (final IPlotListener listenerRef : plotListeners) {
					notificationThread.submit(new Runnable() {
						@Override
						public void run() {
							listenerRef.plotUpdated(plotRef, keyRef, valueRef);
						}
					});
				}

				/*
				 * Submit a task to shut down the worker thread if it is
				 * available. If additional notifications have been submitted
				 * before this task is processed, that's perfectly OK, and they
				 * will still be completed.
				 */
				notificationThread.submit(new Callable<Boolean>() {
					public Boolean call() throws Exception {

						notificationLock.lock();
						try {
							if (notificationThread != null) {
								notificationThread.shutdown();
								notificationThread = null;
							}
						} finally {
							notificationLock.unlock();
						}
						return true;
					};
				});
			}
		} finally {
			notificationLock.unlock();
		}

		return;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public void redraw() {
		// Nothing to do.
	}

	/**
	 * Removes the specified listener from receiving plot update events.
	 * 
	 * @param listener
	 *            The listener to remove.
	 * @return True if the listener was removed, false otherwise (including if
	 *         it was not a listener).
	 */
	public boolean removePlotListener(IPlotListener listener) {
		boolean removed = false;
		// Use the notification lock to synchronize access to the list.
		notificationLock.lock();
		try {
			removed = plotListeners.remove(listener);
		} finally {
			notificationLock.unlock();
		}
		return removed;
	}

	/**
	 * Sets the URI for this plot.
	 * 
	 * @param uri
	 *            The new data source URI.
	 * @return True if the data source changed.
	 * @throws Exception
	 *             If there was an error setting the data source.
	 */
	public boolean setDataSource(URI uri) throws Exception {
		boolean changed = false;
		if (uri != this.uri) {
			changed = true;
			// Unregister from the old data source URI.

			// Register with the new data source URI.
			this.uri = uri;
		}
		return changed;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public void setPlotTitle(String title) {
		this.title = title;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public String getPlotTitle() {
		return title;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public void setIndependentSeries(ISeries series) {
		independentSeries = series;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public ISeries getIndependentSeries() {
		return independentSeries;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public List<ISeries> getAllDependentSeries(String category) {
		return null;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public List<String> getCategories() {
		return new ArrayList<String>();
	}

}
