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
package org.eclipse.ice.client.widgets.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class provides all available {@link MeshApplicationMode}s for a
 * {@link MeshApplication}. If a new mode of operation for the MeshApplication
 * is necessary, it should be added to {@link #modes}.
 * 
 * @author djg
 * 
 */
public class MeshApplicationModeFactory {

	/**
	 * An enumeration for all available MeshApplicationModes.
	 * 
	 * @author djg
	 * 
	 */
	public enum Mode {
		/**
		 * Add mode. Allows the user to add new elements.
		 */
		Add,
		/**
		 * Edit mode. Allows the user to select and move elements.
		 */
		Edit;
	}

	/**
	 * A Map of all available MeshApplicationModes keyed on their types.
	 */
	private final Map<Mode, MeshApplicationMode> modes;

	/**
	 * The default constructor. This is where the Map of available modes is
	 * constructed, and thus where any new modes should be added.
	 */
	public MeshApplicationModeFactory() {
		// Initialize the tree.
		modes = new TreeMap<Mode, MeshApplicationMode>();

		// Add all the nodes currently available. Use mode.getName() as the key.
		modes.put(Mode.Add, new AddMode());
		modes.put(Mode.Edit, new EditMode());

		return;
	}

	/**
	 * Gets all available modes provided by the factory.
	 * 
	 * @return A List of Strings corresponding to MeshApplicationMode names.
	 */
	public List<Mode> getAvailableModes() {
		return new ArrayList<Mode>(modes.keySet());
	}

	/**
	 * Gets a MeshApplicationMode corresponding to a String name.
	 * 
	 * @param type
	 *            The type of mode to fetch.
	 * @return A MeshApplicationMode corresponding to the specified {@link Mode}
	 *         type, or null if that type is invalid not available.
	 */
	public MeshApplicationMode getMode(Mode type) {
		return modes.get(type);
	}

}
