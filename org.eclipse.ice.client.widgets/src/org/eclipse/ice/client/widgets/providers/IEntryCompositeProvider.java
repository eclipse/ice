/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ExtensionHelper;
import org.eclipse.ice.client.widgets.IEntryComposite;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The IEntryCompositeProvider interface provides the contract for 
 * clients to publish IEntryComposite instance that are tailored for 
 * viewing specific realizations of the IEntry interface. 
 * 
 * @author Alex McCaskey
 *
 */
public interface IEntryCompositeProvider {

	/**
	 * ID for the associated extension point.
	 */
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.entryCompositeProvider";

	/**
	 * Return the name of this IEntryCompositeProvider. This is used in deciding
	 * which IEntryCompositeProvider should be used for a given IEntry. Setting
	 * a corresponding IEntry context String will dictate which realization of
	 * this interface to use.
	 * 
	 * @return name The name of this IEntryCompositeProvider.
	 */
	public String getName();

	/**
	 * This operation returns a newly constructed realization of
	 * IEntryComposite. This IEntryComposite should be constructed with the
	 * provided parent Composite, IEntry instance, and SWT style. A FormToolkit
	 * instance is also provided for further UI customization. Realizations
	 * implementing this method can construct an IEntryComposite that is
	 * specific for the given IEntry type.
	 * 
	 * @param parent
	 *            The parent Composite to embed the IEntryComposite on.
	 * @param entry
	 *            The IEntry instance to visualize.
	 * @param style
	 *            The SWT style for the new IEntryComposite.
	 * @param toolKit
	 *            The FormToolkit instance to be used for further
	 *            customizations.
	 * @return entryComposite An IEntryComposite instance that knows how to
	 *         display the given IEntry.
	 */
	public IEntryComposite getEntryComposite(Composite parent, IEntry entry, int style, FormToolkit toolKit);

	/**
	 * This is a static interface method that returns all of the currently
	 * registers IEntryCompositeProviders.
	 * 
	 * @return The available providers
	 */
	public static ArrayList<IEntryCompositeProvider> getProviders() throws CoreException {
		ExtensionHelper<IEntryCompositeProvider> extensionHelper = new ExtensionHelper<IEntryCompositeProvider>();
		return extensionHelper.getExtensions(EXTENSION_POINT_ID);
	}
}
