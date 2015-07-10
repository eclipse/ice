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
package org.eclipse.ice.viz.service.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This abstract class provides a base implementation for the core functionality
 * inherent in all viz connections. Instead of directly implementing the
 * {@link IVizConnection} interface, classes should inherit from this class.
 * <p>
 * This class provides additional functionality intended for management of viz
 * connections. Instances of this class should not be passed around outside a
 * {@link VizConnectionManager} unless you intend to expose the connect and
 * disconnect operations as well as connection properties.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the underlying connection widget.
 */
public abstract class VizConnection<T> implements IVizConnection<T> {

	/**
	 * The current connection widget. This is only set when {@link #connect()}
	 * or {@link #disconnect()} is called, and should be the return value from
	 * the sub-class implementation of {@link #connectToWidget()}.
	 */
	private T widget;
	/**
	 * The current connection state. This is only ever set when the
	 * {@link #connect()} or {@link #disconnect()} operations are called.
	 */
	private ConnectionState state;
	/**
	 * An informative status message to go along with the current connection
	 * {@link #state}.
	 */
	private String statusMessage;

	/**
	 * The map of properties, keyed on their [user-friendly] property names.
	 */
	private final Map<String, String> properties;
	/**
	 * A map of handlers for validating and setting properties.
	 */
	protected final Map<String, IPropertyHandler> propertyHandlers;

	/**
	 * The set of connection listeners. These need to be notified--using
	 * {@link #notifyListeners(ConnectionState, String)}--whenever the
	 * connection state changes.
	 */
	private final Set<IVizConnectionListener<T>> listeners;

	/**
	 * The minimum allowed port (inclusive).
	 */
	private static final int MIN_PORT = 0;
	/**
	 * The maximum allowed port (inclusive).
	 */
	private static final int MAX_PORT = 65535;

	/**
	 * A lock for controlling access to the {@link #state} and
	 * {@link #executorService} when multiple connect calls happen at roughly
	 * the same time.
	 */
	private final Lock connectionLock;
	/**
	 * A single thread executor service specifically for connect and disconnect
	 * operations.
	 */
	private ExecutorService executorService;

	/**
	 * A single thread executor service specifically for notifying listeners in
	 * an orderly fashion.
	 */
	private final ExecutorService notificationExecutorService;
	/**
	 * A thread pool for worker threads in the notifications. This is used so
	 * notifications are processed by listeners in parallel.
	 */
	private final ExecutorService notificationWorkerPool;

	/**
	 * The default constructor. Initializes the connection to the default
	 * values.
	 */
	public VizConnection() {

		// All connections start off disconnected.
		widget = null;
		state = ConnectionState.Disconnected;
		statusMessage = "The connection has not been configured.";

		// Initialize the property map.
		properties = new HashMap<String, String>();

		// Initialize the map of property handlers/setters.
		propertyHandlers = new HashMap<String, IPropertyHandler>();

		// Add setters for the default properties. These should just redirect to
		// the implemented setters in this class.

		// The name property should only accept non-empty strings. Values should
		// be trimmed.
		propertyHandlers.put("Name", new IPropertyHandler() {
			@Override
			public String validateValue(String value) {
				String newValue = null;
				if (value != null) {
					newValue = value.trim();
				}
				return newValue != null && !newValue.isEmpty() ? newValue : null;
			}
		});
		// The description property should only accept non-null strings. Values
		// should be trimmed.
		propertyHandlers.put("Description", new IPropertyHandler() {
			@Override
			public String validateValue(String value) {
				return value != null ? value.trim() : null;
			}
		});
		// The name property should only accept non-empty strings. Values should
		// be trimmed.
		propertyHandlers.put("Host", new IPropertyHandler() {
			@Override
			public String validateValue(String value) {
				// Only accept non-empty strings. Also, trim the input value.
				String newValue = null;
				if (value != null) {
					newValue = value.trim();
				}
				return newValue != null && !newValue.isEmpty() ? newValue : null;
			}
		});
		// The port property should only accept integers lying in the valid port
		// range defined by the global min and max ports.
		propertyHandlers.put("Port", new IPropertyHandler() {
			@Override
			public String validateValue(String value) {
				String newValue = null;
				try {
					int port = Integer.parseInt(value);
					if (port >= MIN_PORT && port <= MAX_PORT) {
						newValue = Integer.toString(port);
					}
				} catch (NumberFormatException e) {
					// The new value is invalid.
				}

				return newValue;
			}
		});
		// The description property should only accept non-null strings. Values
		// should be trimmed.
		propertyHandlers.put("Path", new IPropertyHandler() {
			@Override
			public String validateValue(String value) {
				// Accept non-null strings. Also, trim the input value.
				return value != null ? value.trim() : null;
			}
		});

		// Load the default properties.
		setName("Connection1");
		setDescription("");
		setHost("localhost");
		setPort(50000);
		setPath("");

		// Initialize the set of connection listeners.
		listeners = new HashSet<IVizConnectionListener<T>>();
		notificationExecutorService = Executors.newSingleThreadExecutor();
		notificationWorkerPool = Executors.newCachedThreadPool();

		// Create the lock for accessing certain connection thread variables.
		connectionLock = new ReentrantLock(true);

		return;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public ConnectionState getState() {
		return state;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getStatusMessage() {
		return statusMessage;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public T getWidget() {
		return widget;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getName() {
		return properties.get("Name");
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getDescription() {
		return properties.get("Description");
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getHost() {
		return properties.get("Host");
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public int getPort() {
		return Integer.parseInt(properties.get("Port"));
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getPath() {
		return properties.get("Path");
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>(properties);
	}

	/**
	 * Implements a method from IVizConnection.
	 */
	public String getProperty(String value) {
		return properties.get(value);
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public boolean addListener(IVizConnectionListener<T> listener) {
		boolean added = false;
		if (listener != null) {
			added = listeners.add(listener);
		}
		return added;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public boolean removeListener(IVizConnectionListener<T> listener) {
		boolean removed = false;
		if (listener != null) {
			removed = listeners.remove(listener);
		}
		return removed;
	}

	/**
	 * Attempts to establish the connection on a separate worker thread.
	 * 
	 * @return A future wrapping the connection's state when the connection
	 *         operation completes. To wait for the connection to connect (or
	 *         fail), use the future's {@code get()} method.
	 */
	public Future<ConnectionState> connect() {

		Future<ConnectionState> retVal;

		// Based on the current state of the connection, we need to either start
		// connecting, wait on the current connection attempt, or just
		// immediately return (because it's already connected).
		connectionLock.lock();
		try {
			// If already connected, we don't need the executor service.
			if (state == ConnectionState.Connected) {
				retVal = createInstantFuture(state);
			}
			// If already connecting, we should submit a task to the existing
			// executor service.
			else if (state == ConnectionState.Connecting) {
				retVal = hookIntoConnectThread();
			}
			// If not connected, we should submit a task to a new executor
			// service (the old one should have been closed and unset).
			else {
				retVal = startConnectThread();
			}
		} finally {
			connectionLock.unlock();
		}

		return retVal;
	}

	/**
	 * Returns a Future whose state is as specified and can be evaluated
	 * <i>immediately</i>.
	 * 
	 * @param state
	 *            The state that will be returned from the future's get methods.
	 * @return The "instant" future.
	 */
	private Future<ConnectionState> createInstantFuture(ConnectionState state) {
		final ConnectionState stateRef = state;
		return new Future<ConnectionState>() {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				return false;
			}

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public boolean isDone() {
				return true;
			}

			@Override
			public ConnectionState get() throws InterruptedException, ExecutionException {
				return stateRef;
			}

			@Override
			public ConnectionState get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return stateRef;
			}
		};
	}

	/**
	 * Queues a connection task in the {@link #executorService}. This task will
	 * attempt to connect to the client. After completion, the service will be
	 * shut down.
	 * 
	 * @return The future task after which the connection will either be
	 *         connected or failed.
	 */
	private Future<ConnectionState> startConnectThread() {
		executorService = Executors.newSingleThreadExecutor();
		return executorService.submit(new Callable<ConnectionState>() {
			@Override
			public ConnectionState call() throws Exception {

				// Update the state.
				ConnectionState threadState = ConnectionState.Connecting;
				statusMessage = "The connection is being established.";

				// Updating the state variable must be done with
				// the lock.
				connectionLock.lock();
				try {
					state = threadState;
				} finally {
					connectionLock.unlock();
				}

				// Notify the listeners.
				notifyListeners(threadState, statusMessage);

				// Try to open the connection.
				widget = connectToWidget();

				// Whether successful or not, update the state.
				if (widget != null) {
					threadState = ConnectionState.Connected;
					statusMessage = "The connection is established.";
				} else {
					threadState = ConnectionState.Failed;
					statusMessage = "The connection failed to connect.";
				}

				// Updating the state variable must be done with
				// the lock.
				connectionLock.lock();
				try {
					state = threadState;
					// Close the executor service.
					executorService.shutdown();
					executorService = null;
				} finally {
					connectionLock.unlock();
				}

				// Notify the listeners.
				notifyListeners(threadState, statusMessage);

				return state;
			}
		});
	}

	/**
	 * Hooks into the existing connection task started by calling
	 * {@link #startConnectThread()} by submitting a new task to the
	 * {@link #executorService}. The submitted task simply returns the current
	 * connection state.
	 * 
	 * @return The future task after which the connection will either be
	 *         connected or failed.
	 */
	private Future<ConnectionState> hookIntoConnectThread() {
		return executorService.submit(new Callable<ConnectionState>() {
			@Override
			public ConnectionState call() throws Exception {
				return state;
			}
		});
	}

	/**
	 * Attempts to establish the connection to the connection widget. This
	 * method will be called from a separate worker thread.
	 * 
	 * @return The new connection widget, or {@code null} if the connection
	 *         widget could not be created and connected.
	 */
	protected abstract T connectToWidget();

	/**
	 * Attempts to disconnect from the connection widget on a separate worker
	 * thread.
	 * 
	 * @return A future wrapping the connection's state when the disconnect
	 *         operation completes. To wait for the connection to disconnect (or
	 *         fail), use the future's {@code get()} method.
	 */
	public Future<ConnectionState> disconnect() {

		Future<ConnectionState> retVal;

		// Based on the current state of the connection, we need to either start
		// disconnecting or just immediately return (because it's already
		// disconnected).
		connectionLock.lock();
		try {
			// If already disconnected, we don't need the executor service.
			if (state == ConnectionState.Disconnected) {
				retVal = createInstantFuture(state);
			}
			// If the connection has failed previously and has no widget, then
			// we can return immediately, too.
			else if (state == ConnectionState.Failed && widget == null) {
				retVal = createInstantFuture(state);
			}
			// Otherwise, if we haven't already, we need to start disconnecting.
			else {
				retVal = startDisconnectThread();
			}
		} finally {
			connectionLock.unlock();
		}

		return retVal;
	}

	/**
	 * Queues a disconnect task in the {@link #executorService}. This task will
	 * attempt to disconnect from the client. After completion, the service will
	 * be shut down.
	 * 
	 * @return The future task after which the connection will either be
	 *         connected or failed.
	 */
	private Future<ConnectionState> startDisconnectThread() {
		// If necessary, create the executor service.
		if (executorService == null) {
			executorService = Executors.newSingleThreadExecutor();
		}
		return executorService.submit(new Callable<ConnectionState>() {
			@Override
			public ConnectionState call() throws Exception {
				ConnectionState threadState;
				T connectionWidget;

				// Get the current connection state and connection widget.
				connectionLock.lock();
				try {
					threadState = state;
					connectionWidget = widget;
				} finally {
					connectionLock.unlock();
				}

				// If a previous task hasn't successfully disconnected, then try
				// to disconnect.
				if (threadState != ConnectionState.Disconnected && connectionWidget != null) {

					// Try to disconnect.
					boolean success = disconnectFromWidget(connectionWidget);

					// Update the state and perhaps the connection widget
					// depending on the success of the disconnect operation.
					connectionLock.lock();
					try {
						if (success) {
							state = ConnectionState.Disconnected;
							statusMessage = "The connection is closed.";

							// Unset the widget.
							widget = null;

							// Close the executor service.
							executorService.shutdown();
							executorService = null;
						} else {
							state = ConnectionState.Failed;
							statusMessage = "The connection failed while disconnecting.";
						}
						threadState = state;
					} finally {
						connectionLock.unlock();
					}

					// Notify listeners if the connection disconnected or failed
					// to disconnect.
					notifyListeners(threadState, statusMessage);
				}

				// Return the current state of the connection.
				return state;
			}
		});
	}

	/**
	 * Attempts to disconnect from the connection widget. This method will be
	 * called from a separate worker thread.
	 * 
	 * @param widget
	 *            The widget used for the connection.
	 * @return True if the widget is disconnected at the end of the operation,
	 *         false otherwise.
	 */
	protected abstract boolean disconnectFromWidget(T widget);

	/**
	 * Notifies registered {@link IVizConnectionListener}s using the specified
	 * connection state and status message.
	 * 
	 * @param state
	 *            The connection state to send to the listeners.
	 * @param message
	 *            The message to send to the listeners.
	 */
	private void notifyListeners(ConnectionState state, String message) {
		/*
		 * Notifications should be sent out in the order in which they occur.
		 * This is achieved by using a single thread to delegate notifications
		 * to worker threads (so they can work in parallel) and wait for them to
		 * complete before the next set of notifications can be sent out.
		 */

		// Get final references to the arguments required to notify a given
		// connection listener.
		final IVizConnection<T> connRef = this;
		final ConnectionState stateRef = state;
		final String msgRef = message;

		// Submit a new task to notify all listeners.
		notificationExecutorService.submit(new Runnable() {
			@Override
			public void run() {
				List<Future<?>> notifications;
				notifications = new ArrayList<Future<?>>(listeners.size());

				// Delegate each notification to the worker thread pool.
				for (final IVizConnectionListener<T> listener : listeners) {
					notifications.add(notificationWorkerPool.submit(new Runnable() {
						@Override
						public void run() {
							listener.connectionStateChanged(connRef, stateRef, msgRef);
						}
					}));
				}

				// Wait for all notification requests to be completed.
				for (Future<?> notification : notifications) {
					try {
						notification.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}

				return;
			}
		});

		return;
	}

	/**
	 * Sets the specified property to the new value, provided the value is both
	 * allowed and new.
	 * 
	 * @param name
	 *            The name of the property to update.
	 * @param value
	 *            The new value of the property. If {@code null}, the property
	 *            will be removed.
	 * @return True if the property specified by the name was updated to a new
	 *         value, false otherwise.
	 */
	public boolean setProperty(String name, String value) {
		boolean changed = false;

		// If a handler is not available, then assume the new value is invalid.
		boolean canChange = false;
		String newValue = value;

		// Validate the value through any available handler.
		IPropertyHandler handler = propertyHandlers.get(name);
		if (handler != null) {
			newValue = handler.validateValue(value);
			canChange = (newValue != null);
		}

		if (canChange) {
			// If the value is not null, update the property in the map.
			if (value != null) {
				String oldValue = properties.put(name, value);
				changed = (oldValue == null || !oldValue.equals(value));
			}
			// Otherwise, if the value is null, remove it from the map.
			else {
				changed = (properties.remove(name) != null);
			}
		}

		return changed;
	}

	/**
	 * Sets the name of the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param name
	 *            The new name of the property.
	 * @return True if the name was changed, false otherwise.
	 */
	public boolean setName(String name) {
		return setProperty("Name", name);
	}

	/**
	 * Sets the description of the connection. This is a convenient method that
	 * can be used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param description
	 *            The new description of the property.
	 * @return True if the description was changed, false otherwise.
	 */
	public boolean setDescription(String description) {
		return setProperty("Description", description);
	}

	/**
	 * Sets the host for the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param host
	 *            The host name of the property.
	 * @return True if the host was changed, false otherwise.
	 */
	public boolean setHost(String host) {
		return setProperty("Host", host);
	}

	/**
	 * Sets the port of the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param port
	 *            The new port of the property.
	 * @return True if the port was changed, false otherwise.
	 */
	public boolean setPort(int port) {
		return setProperty("Port", Integer.toString(port));
	}

	/**
	 * Sets the path of the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param path
	 *            The new path of the property.
	 * @return True if the path was changed, false otherwise.
	 */
	public boolean setPath(String path) {
		return setProperty("Path", path);
	}

	/**
	 * A simple interface that handles setting a single property.
	 * 
	 * @author Jordan
	 *
	 */
	protected interface IPropertyHandler {

		/**
		 * Validates the specified value for the associated property.
		 * 
		 * @param value
		 *            The value to check.
		 * @return True if the value is allowed, false otherwise.
		 */
		public String validateValue(String value);
	}
}
