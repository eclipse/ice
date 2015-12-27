/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.application;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

/**
 * This class represents a {@link SimpleAppState} that can contain multiple
 * child <code>SimpleAppState</code>s. The lifecycle of a child depends on the
 * parent <code>CompositeAppState</code>:
 * 
 * <ul>
 * <li>Multiple <code>SimpleAppState</code>s can be attached as children before
 * or after initialization or cleanup of the <code>CompositeAppState</code>.</li>
 * <li>After the <code>CompositeAppState</code> is started, the attached
 * <code>SimpleAppState</code>s are started.</li>
 * <li>When the <code>CompositeAppState</code> is stopped, children are stopped.
 * (The order in which they are stopped is handled by the underlying jME
 * <code>Application</code>'s {@link AppStateManager} and <i>cannot be
 * guaranteed</i>.)</li>
 * <li>Children can be started or stopped manually.</li>
 * <li>When added/removed, children are started/stopped.</li>
 * <li>When the <code>CompositeAppState</code> (or its controls) is
 * enabled/disabled, so are the children (or their controls).</li>
 * </ul>
 * 
 * @author Jordan
 * 
 */
public abstract class CompositeAppState extends SimpleAppState {

	/**
	 * The child <code>SimpleAppState</code>s managed by this
	 * <code>CompositeAppState</code>.
	 */
	private final List<SimpleAppState> appStates = new ArrayList<SimpleAppState>();

	/**
	 * Flags for whether or not each <code>SimpleAppState</code> in
	 * {@link #appStates} should be enabled when this
	 * <code>CompositeAppState</code> is started or re-enabled.
	 */
	private final List<Boolean> enabledStates = new ArrayList<Boolean>();

	/**
	 * In addition to the default behavior, this method initializes any child
	 * <code>SimpleAppState</code>s.
	 */
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		// If the parent app state is initialized, try to initialize the child
		// SimpleAppStates.
		if (isInitialized()) {
			for (SimpleAppState appState : appStates) {
				appState.start(getApplication());
			}
		}

		return;
	}

	/**
	 * Before the <code>CompositeAppState</code> is stopped, this method stops
	 * the child <code>SimpleAppState</code>s.
	 * 
	 * <p>
	 * Note that the underlying jME <code>Application</code>'s
	 * {@link AppStateManager} processes the stop commands, so this class cannot
	 * guarantee that the cleanup methods of the child
	 * <code>SimpleAppState</code>s will be processed before the
	 * <code>CompositeAppState</code>.
	 * </p>
	 */
	@Override
	public void stop() {

		for (SimpleAppState appState : appStates) {
			appState.stop();
		}

		super.stop();
	}

	/**
	 * Adds a child <code>SimpleAppState</code> to the
	 * <code>CompositeAppState</code>.
	 * 
	 * @param appState
	 *            The new child <code>SimpleAppState</code>. If the
	 *            <code>CompositeAppState</code> is already started, this
	 *            <code>SimpleAppState</code> will be started.
	 */
	protected void addAppState(SimpleAppState appState) {
		
		if (appState != null && !appStates.contains(appState)) {
			appStates.add(appState);
			// Add a flag for whether or not the appState is enabled.
			enabledStates.add(appState.isEnabled());
			// We also should not let the child be enabled while the parent is
			// not (although this can be overridden by keeping a reference to
			// the child).
			if (!isEnabled()) {
				appState.setEnabled(false);
			}

			if (isInitialized()) {
				appState.start(getApplication());
			}
		}
		
		return;
	}

	/**
	 * Removes a child <code>SimpleAppState</code> from the
	 * <code>CompositeAppState</code>.
	 * 
	 * @param appState
	 *            The child <code>SimpleAppState</code> to remove. The removed
	 *            <code>SimpleAppState</code> will be stopped.
	 */
	protected void removeAppState(SimpleAppState appState) {

		// Remove the AppState and its associated enabled flag (in case the
		// parent AppState is disabled).
		if (appState != null) {
			int index = appStates.indexOf(appState);
			if (index != -1) {
				appStates.remove(index);
				enabledStates.remove(index);
				appState.stop();
			}
		}
		
		return;
	}

	// ---- Extends SimpleAppState ---- //
	/**
	 * In addition to enabling the <code>CompositeAppState</code>, this enables
	 * all child <code>SimpleAppState</code>s.
	 */
	@Override
	public void enableAppState() {
		super.enableAppState();

		// Only enable the AppState's whose associated enable flags have been
		// set.
		int size = appStates.size();
		for (int i = 0; i < size; i++) {
			if (enabledStates.get(i)) {
				appStates.get(i).enableAppState();
			}
		}

		return;
	}

	/**
	 * In addition to enabling the <code>CompositeAppState</code>'s controls,
	 * this enables all child <code>SimpleAppState</code> controls.
	 */
	@Override
	public void enableControls() {
		super.enableControls();

		for (SimpleAppState appState : appStates) {
			appState.enableControls();
		}
	}

	/**
	 * In addition to disabling the <code>CompositeAppState</code>, this
	 * disables all child <code>SimpleAppState</code>s.
	 */
	@Override
	public void disableAppState() {
		super.disableAppState();

		// We need to keep track of which child AppStates are currently enabled
		// so that we do not re-enable disabled ones in enableAppState().
		int size = appStates.size();
		for (int i = 0; i < size; i++) {
			SimpleAppState appState = appStates.get(i);
			enabledStates.set(i, appState.isEnabled());
			appState.disableAppState();
		}
		
		return;
	}

	/**
	 * In addition to disabling the <code>CompositeAppState</code>'s controls,
	 * this disables all child <code>SimpleAppState</code> controls.
	 */
	@Override
	public void disableControls() {
		super.disableControls();

		for (SimpleAppState appState : appStates) {
			appState.disableControls();
		}
	}
	// -------------------------------- //

}
