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

import org.eclipse.ice.client.widgets.DiscreteEntryComposite;
import org.eclipse.ice.client.widgets.ExecutableEntryComposite;
import org.eclipse.ice.client.widgets.FileEntryComposite;
import org.eclipse.ice.client.widgets.IEntryComposite;
import org.eclipse.ice.client.widgets.StringEntryComposite;
import org.eclipse.ice.datastructures.entry.ContinuousEntry;
import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.ExecutableEntry;
import org.eclipse.ice.datastructures.entry.FileEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.IEntryVisitor;
import org.eclipse.ice.datastructures.entry.MultiValueEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DefaultEntryCompositeProvider implements IEntryCompositeProvider to create 
 * default IEntryComposites for the internal ICE IEntry realizations. Furthermore, 
 * it implements IEntryVisitor to discover the type of each provided IEntry instance 
 * and discover and create the appropriate IEntryComposite realization. 
 *   
 * @author Alex McCaskey
 *
 */
public class DefaultEntryCompositeProvider implements IEntryCompositeProvider, IEntryVisitor {

	/**
	 * Reference to the logger service.
	 */
	protected Logger logger = LoggerFactory.getLogger(DefaultEntryCompositeProvider.class);

	/**
	 * Default provider name
	 */
	public static final String PROVIDER_NAME = "default";

	/**
	 * Reference to the IEntryComposite to create for the 
	 * provided IEntry.
	 */
	protected IEntryComposite entryComposite;

	/**
	 * The SWT style for the Composite. 
	 */
	protected int style;

	/**
	 * The parent Composite to use for the new IEntryComposite. 
	 */
	protected Composite parent;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider#getName()
	 */
	@Override
	public String getName() {
		return PROVIDER_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider#getEntryComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ice.datastructures.entry.IEntry, int, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public IEntryComposite getEntryComposite(Composite parentComposite, IEntry entry, int swtStyle,
			FormToolkit toolKit) {
		// Set the parent Composite and the SWT style
		parent = parentComposite;
		style = swtStyle;
		
		// Create the correct IEntryComposite using the Visitor pattern
		entry.accept(this);

		// Decorate the IEntryComposite. Use the FormToolKit if possible.
		if (toolKit != null) {
			toolKit.adapt(entryComposite.getComposite());
		} else {
			entryComposite.getComposite().setBackground(parent.getBackground());
		}

		// Set the LayoutData. The DataComponentComposite has a GridLayout. The
		// IEntryComposite should grab all available horizontal space.
		entryComposite.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		// Set the Listener.
		// Create a listener for the Entry composite that will pass events to
		// any other listeners.
		Listener entryListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// Let them know
				parent.notifyListeners(SWT.Selection, new Event());
			}
		};

		entryComposite.getComposite().addListener(SWT.Selection, entryListener);

		return entryComposite;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.entry.IEntryVisitor#visit(org.eclipse.ice.datastructures.entry.StringEntry)
	 */
	@Override
	public void visit(StringEntry entry) {
		entryComposite = new StringEntryComposite(parent, entry, style);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.entry.IEntryVisitor#visit(org.eclipse.ice.datastructures.entry.FileEntry)
	 */
	@Override
	public void visit(FileEntry entry) {
		entryComposite = new FileEntryComposite(parent, entry, style);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.entry.IEntryVisitor#visit(org.eclipse.ice.datastructures.entry.DiscreteEntry)
	 */
	@Override
	public void visit(DiscreteEntry entry) {
		entryComposite = new DiscreteEntryComposite(parent, entry, style);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.entry.IEntryVisitor#visit(org.eclipse.ice.datastructures.entry.ExecutableEntry)
	 */
	@Override
	public void visit(ExecutableEntry entry) {
		entryComposite = new ExecutableEntryComposite(parent, entry, style);
	}

	@Override
	public void visit(ContinuousEntry entry) {
		entryComposite = new StringEntryComposite(parent, entry, style);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.entry.IEntryVisitor#visit(org.eclipse.ice.datastructures.entry.MultiValueEntry)
	 */
	@Override
	public void visit(MultiValueEntry entry) {
		// TODO Auto-generated method stub

	}

}