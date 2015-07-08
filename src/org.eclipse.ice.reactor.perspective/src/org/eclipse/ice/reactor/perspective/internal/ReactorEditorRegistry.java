/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.reactor.perspective.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetRegistry;
import org.eclipse.ice.client.widgets.reactoreditor.IReactorEditorRegistry;
import org.eclipse.ice.client.widgets.reactoreditor.ReactorFormEditor;
import org.eclipse.ice.client.widgets.reactoreditor.ReactorFormInput;
import org.eclipse.ice.client.widgets.reactoreditor.ReactorFormInputFactory;
import org.eclipse.ice.reactor.perspective.ReactorViewer;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements {@link IReactorEditorRegistry} so that classes in the
 * bundle can access the currently-available Reactor Editors.<br>
 * <br>
 * This class provides the IReactorEditorRegistry interface and is referenced by
 * the main Reactor Editor bundle via OSGi. The {@link ReactorFormEditor}s are
 * registered with this registry when constructed.<br>
 * <br>
 * The IDs of available editors and the ability to set their input are exposed
 * in this implementation.
 * 
 * @author Jordan
 * 
 */
public class ReactorEditorRegistry implements IReactorEditorRegistry {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ReactorEditorRegistry.class);

	/**
	 * An ordered Map of all current Reactor Editors, keyed on their associated
	 * Item/Form ID.
	 */
	private static final TreeMap<Integer, ReactorFormEditor> editors = new TreeMap<Integer, ReactorFormEditor>();

	/**
	 * The IAnalysisWidgetRegistry that is used to get IAnalysisWidgetFactories
	 * for different component types.
	 */
	private static IAnalysisWidgetRegistry widgetRegistry;

	// ---- Implements IReactorEditorRegistry ---- //
	/**
	 * Updates {@link #editors} with the new Reactor Editor.
	 */
	@Override
	public void addReactorEditor(ReactorFormEditor editor, int id) {
		editors.put(id, editor);

		logger.info("ICE Reactor Perspective Message: "
				+ "Added new reactor analyzer with ID " + id);
	}

	/**
	 * Removes the {@link ReactorFormEditor} associated with the ID from
	 * {@link #editors}.
	 */
	@Override
	public void removeReactorEditor(int id) {
		editors.remove(id);

		logger.info("ICE Reactor Perspective Message: "
				+ "Removed reactor analyzer with ID " + id);
	}

	// ------------------------------------------- //

	/**
	 * Gets a List of IDs for all currently available Reactor Editors.
	 * 
	 * @return A List of integer IDs.
	 */
	public static List<Integer> getEditorIds() {
		return new ArrayList<Integer>(editors.keySet());
	}

	/**
	 * Sets the input for a particular Reactor Editor based on an
	 * {@link ITreeSelection} from the {@link ReactorViewer}.
	 * 
	 * @param id
	 *            The ID of the editor.
	 * @param selection
	 *            The ITreeSelection from the ReactorViewer's TreeView.
	 * @param dataSource
	 *            the DataSource that the selection is being sent to, e.g.,
	 *            Input or Reference.
	 */
	public static void setInput(int id, ITreeSelection selection,
			DataSource dataSource) {

		// Get the editor corresponding to the ID.
		ReactorFormEditor editor = editors.get(id);

		// If the editor and selection are not null, try to set the input for
		// the editor.
		if (editor != null && selection != null) {

			// We need to use a ReactorFormInputFactory to create the proper
			// input based on the tree selection. We also need to get the
			// editor's previous input for values that will not change.
			ReactorFormInputFactory factory = new ReactorFormInputFactory();
			ReactorFormInput oldInput = (ReactorFormInput) ((IEditorPart) editor)
					.getEditorInput();
			ReactorFormInput input = factory.createInput(oldInput, selection,
					dataSource);

			// Set the input for the editor.
			editor.setInput((IEditorInput) input);
		}

		return;
	}

	/**
	 * Gets the current {@link IAnalysisWidgetRegistry} that is used to get
	 * IAnalysisWidgetFactories for different component types
	 * 
	 * @return An IAnalysisWidgetRegistry, or null if it is not set.
	 */
	public static IAnalysisWidgetRegistry getAnalysisWidgetRegistry() {
		return widgetRegistry;
	}

	/**
	 * Sets {@link #widgetRegistry} if the provided value is not null.
	 * 
	 * @param registry
	 *            The new IAnalysisWidgetRegistry.
	 */
	public static void setAnalysisWidgetRegistry(
			IAnalysisWidgetRegistry registry) {
		if (registry != null) {
			widgetRegistry = registry;
			logger.info("ICE Reactor Perspective Message: "
					+ "Analysis widget registry set successfully!");
		} else {
			logger.info("ICE Reactor Perspective Message:"
					+ "Framework attempted to set analysis widget registry, "
					+ "but the reference was null.");
		}
		return;
	}

	/**
	 * Unsets {@link #widgetRegistry}. This notifies the bundle that the
	 * registry has been shut down.
	 * 
	 * @param registry
	 *            The new IAnalysisWidgetRegistry.
	 */
	public static void unsetAnalysisWidgetRegistry(
			IAnalysisWidgetRegistry registry) {
		if (registry == widgetRegistry) {
			widgetRegistry = null;
		}
	}

}
