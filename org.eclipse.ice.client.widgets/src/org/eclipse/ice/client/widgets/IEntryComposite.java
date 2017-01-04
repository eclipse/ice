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
package org.eclipse.ice.client.widgets;

import org.eclipse.january.form.IEntry;
import org.eclipse.january.form.IUpdateableListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IMessageManager;

/**
 * The IEntryComposite interface defines methods that are common to 
 * all Composites that wrap and visualize IEntry instances. It is 
 * expected that realizations of this interface also extend the SWT 
 * Composite class.  
 * 
 * @author Alex McCaskey
 *
 */
public interface IEntryComposite extends IUpdateableListener {

	/**
	 * This operation renders the SWT widgets for the Entry.
	 */
	public void render();
	
	/**
	 * Returns the entry stored on this Composite
	 *
	 * @return The Entry rendered by this Composite.
	 */
	public IEntry getEntry();
	
	/**
	 * This operation lets clients set a new IEntry instance. 
	 * 
	 * @param entry The new entry to be viewed. 
	 */
	public void setEntry(IEntry entry);
	
	/**
	 * This operation lets clients invoke SWT Composite methods on 
	 * this IEntryComposite. 
	 * 
	 * @return this This IEntryComposite as a SWT Composite. 
	 */
	public Composite getComposite();
	
	/**
	 * This operation sets the Message Manager that should be used by the
	 * EntryComposite to post messages about the Entry. If the Message Manager
	 * is not set, the EntryComposite will not attempt to post messages.
	 *
	 * @param manager
	 *            The Message Manager that the EntryComposite should use.
	 */
	public void setMessageManager(IMessageManager manager);
	
	/**
	 * This operation directs the IEntryComposite to refresh its view of the
	 * Entry. This should be called in the event that the Entry has changed on
	 * the file system and the view needs to be updated.
	 */
	public void refresh();
}
