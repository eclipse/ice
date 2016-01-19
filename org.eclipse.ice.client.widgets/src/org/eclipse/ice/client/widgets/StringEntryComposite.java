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

import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * The StringEntry is an extension of the AbstractEntryComposite that renders
 * StringEntry realizations of IEntry. It draws a SWT Text widget to get user
 * input for the StringEntry.
 * 
 * @author Alex McCaskey
 *
 */
public class StringEntryComposite extends AbstractEntryComposite {

	/**
	 * The Constructor.
	 * 
	 * @param parent
	 * @param refEntry
	 * @param style
	 */
	public StringEntryComposite(Composite parent, IEntry refEntry, int style) {
		super(parent, refEntry, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.AbstractEntryComposite#render()
	 */
	@Override
	public void render() {
		// Set the default layout to a vertical FillLayout.
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 3;
		fillLayout.spacing = 5;
		Layout layout = fillLayout;

		createLabel();

		// Create a textfield
		if (!entry.isSecret()) {
			logger.info("Creating " + entry.getName() + " StringEntryComposite.");
			widget = new Text(this, SWT.LEFT | SWT.BORDER);
		} else {
			widget = new Text(this, SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
		}

		widget.setToolTipText(entry.getDescription());
		((Text) widget).setText(entry.getValue());
		widget.setBackground(getBackground());

		// Add the Focus Listeners
		widget.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// Notify any listeners that the selection has changed
				notifyListeners(SWT.Selection, new Event());
				// Set the value of the Entry
				setEntryValue(((Text) widget).getText());
			};
		});

		// Add a listener for the "DefaultSelection" key (return/enter). It
		// needs to be registered with the EntryComposite and the Text widget,
		// in case it is being used by JFace, which doesn't always post standard
		// SWT events.
		Listener enterListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// Notify any listeners that the selection has changed
				notifyListeners(SWT.Selection, new Event());
				logger.info("HELLO WORLD SETTING " + ((Text) widget).getText());
				// Set the value of the Entry
				setEntryValue(((Text) widget).getText());
			}
		};
		this.addListener(SWT.DefaultSelection, enterListener);
		widget.addListener(SWT.DefaultSelection, enterListener);

		((Text) widget).addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					e.doit = true;
					notifyListeners(SWT.TAB, new Event());
					setEntryValue(((Text) widget).getText());
				}
			}
		});
		
		setLayout(layout);
	}
}
