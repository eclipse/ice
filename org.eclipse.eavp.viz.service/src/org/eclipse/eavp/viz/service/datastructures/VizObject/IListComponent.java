/*******************************************************************************
 * Copyright (c) 2011, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay 
 *   Billings
 *******************************************************************************/
package org.eclipse.eavp.viz.service.datastructures.VizObject;

import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * This is a base class for Components that are based on lists. Its primary
 * purpose is to provide all of the standard list operations expected from Java
 * Collections in a class that realizes ICE's additional requirements for
 * persistence, unique identification, and notifications.
 * <p>
 * It implements the Component to match up with the ICE requirements and extends
 * the TransformedList from GlazedLists to provide the generic, observable list
 * capabilities.
 * </p>
 * <p>
 * It can be configured to provide a handle to an IElementSource that will
 * provide valid, new elements upon request. This configuration is meant to
 * facilitate the use of the ListComponent as a, for example, proxy for
 * selections made from databases or similar tools. If getElementSource()
 * returns null, clients should feel free to put whatever values they want into
 * the List.
 * </p>
 * <p>
 * This class also realizes the WritableTableFormat interface from GlazedLists
 * so that it can simply be "dropped in" to GlazedList tables in client UIs.
 * This is handled using delegation instead of inheritance so that the format
 * can be easily changed without re-initializing the list. Alternatively, the
 * WritableTableFormat can just be retrieved with a getter too. If the table
 * format is not provided, the behavior is unspecified, but this Component
 * should probably not be put in a Form in that case.
 * </p>
 * <b>Implementation note</b>
 * <p>
 * Unfortunately, there are some tricky implementation details here related to
 * extending TransformedList. The point of a TransformedList is to sit on top of
 * a source list and manipulate it so it is a wrapper around another list,
 * facilitating transformations to that list. This means that in the
 * implementation of this class any work to register listeners or manipulate the
 * list should be done to the source list, not "this" list. TransformedList is
 * the suggested base class for extensions to GlazedLists instead of
 * AbstractEventList, so we will have to live with this for now.
 * </p>
 * 
 * I also had to override the ListEventListener registration operations to make
 * sure that the source list was registered. The TransformedList by defaults
 * registers listeners against itself, not the source, but it processes all of
 * the list additions, etc., through the source.
 * 
 * @author Jay Jay Billings
 * 
 */
public interface IListComponent<T> extends IVizUpdateable, 
WritableTableFormat<T> {

	/**
	 * This operation sets the element source that should be used by the list
	 * 
	 * @param source
	 *            the element source that provides a list of values that
	 *            *should* be used to seed new entries.
	 */
	void setElementSource(IElementSource<T> source);

	/**
	 * This operation returns the element source which should be used to create
	 * new elements to add to the list.
	 * 
	 * @return The element source or null if no IElementSource is provided. If
	 *         null, then the client code should add whatever valid element they
	 *         want.
	 */
	IElementSource<T> getElementSource();

	/**
	 * This operation sets the GlazedList WritableTableFormat that should be
	 * used to describe this List when used by client UI code or other clients
	 * that need to know how to read the List.
	 * 
	 * @param format
	 *            The table format. Calls to this class' implementation of the
	 *            WritableTableFormat interface will be delegated to this
	 *            format.
	 */
	void setTableFormat(WritableTableFormat<T> format);

	/**
	 * This operation returns the GlazedList WritableTableFormat that is used to
	 * describe this list or null if it was not set.
	 * 
	 * @return The table format or null.
	 */
	WritableTableFormat<T> getTableFormat();

}