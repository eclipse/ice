/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * This class is an SWT Composite that is specialized to render ICE
 * DataComponents. It can take a message manager for posting messages and it can
 * be configured to post save events.
 * 
 * @author Jay Jay Billings, Jordan H. Deyton, Anna Wojtowicz
 */
public class DataComponentComposite extends Composite implements
		IUpdateableListener {

	/**
	 * This attribute is a reference to an ICE DataComponent that stores the
	 * data that should be displayed by this composite. The DataComponent will
	 * also update the composite when its state changes (i.e. - becomes "stale"
	 * in the Eclipse parlance).
	 */
	private DataComponent dataComp;

	/**
	 * A map of Entries to the Composites that define them. Keyed on their index
	 * (the order they were added to the DataComponent).
	 */
	private SortedMap<Integer, EntryComposite> entryMap = Collections
			.synchronizedSortedMap(new TreeMap<Integer, EntryComposite>());

	// FIXME - Entries in a DataComponent are not guaranteed to have unique IDs,
	// nor are they guaranteed to have unique names. Because there is an
	// inherent order in which they were added to the DataComponent (which
	// previous code attempted to accomplish), we should try to maintain the
	// same order in the DataComponentComposite.

	/**
	 * The message manager that should be used by EntryComposites created by
	 * this composite.
	 */
	private IMessageManager messageManager = null;

	/**
	 * A Label used to inform the user that no data (Entries) are available for
	 * the DataComponent.
	 */
	private Label emptyLabel = null;

	/**
	 * Set the default initial layout. This layout can be changed via
	 * {@link #setLayout(Layout)}. This reference is required for the following
	 * reason: when there are no Entries, a label is displayed. A different
	 * layout is necessary to display the label by itself.
	 */
	private Layout layout;

	/**
	 * The {@code FormToolkit} used by the parent {@code Form}. This should be
	 * set by the customized {@code Form} or {@code Page} that created this
	 * {@code DataComponentComposite} in order to properly decorate the rendered
	 * {@code EntryComposite}s. Otherwise, some default decorations are applied.
	 */
	public FormToolkit formToolkit = null;

	/**
	 * The constructor.
	 * 
	 * @param parentComposite
	 *            The parent of this composite.
	 * @param style
	 *            The style in which the composite should be drawn.
	 */
	public DataComponentComposite(DataComponent comp,
			Composite parentComposite, int style) {

		// Construct the base composite
		super(parentComposite, style);
		// If ICE is in debug mode, draw a red border around this composite,
		// otherwise set it to white.
		if (System.getProperty("DebugICE") == null) {
			setBackground(parentComposite.getBackground());
		} else {
			setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}

		// Save the data component.
		dataComp = comp;

		// Register with the DataComponent if it is not null.
		if (dataComp != null) {
			dataComp.register(this);
		}
		
		// Add a dispose listener which unregisters from the DataComponent
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dataComp.unregister(DataComponentComposite.this);
			}
		});

		// Set the initial layout for the parent class. This should default to
		// a GridLayout with 3 equally sized columns.
		setLayout(new GridLayout(3, true));

		// Render the Entries.
		renderEntries();

		// Add a listener for the "DefaultSelection" key (return/enter). It
		// needs to be registered with the EntryComposite and the Text widget,
		// in case it is being used by JFace, which doesn't always post standard
		// SWT events.
		Listener enterListener = new Listener() {
			public void handleEvent(Event e) {
				// Notify the EntryComposites that the selection was made
				for (EntryComposite entryComp : entryMap.values()) {
					entryComp.notifyListeners(SWT.DefaultSelection, e);
				}
			}
		};
		addListener(SWT.DefaultSelection, enterListener);

		return;
	}

	/**
	 * This operation handles a re-draw of EntryComposites that have been 
	 * recently changed. This method attempts to avoid a complete redraw of all
	 * EntryComposites.
	 */
	public void refresh() {
		
		// Local Declarations
		List<Entry> entries = dataComp.retrieveAllEntries();
		
		// If there's an empty label set and it's not longer necessary, dispose
		if (!entries.isEmpty() && emptyLabel != null) {
			emptyLabel.dispose();
			emptyLabel = null;
		}
		
		// If a selection change triggered this refresh, make sure to update 
		// any Entries on the dataComp that need their value(s) updating
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = dataComp.retrieveAllEntries().get(i);
			EntryComposite entryComp = entryMap.get(i);
			if (entryComp != null && 
					!entry.getValue().equals(entryComp.entry.getValue())) {
				entry.setValue(entryComp.entry.getValue());
			}
		}
				
		// Begin comparing the list of Entries to the EntryComposites in 
		// entryMap to determine what needs to be done
		int maxIterations = entries.size() > entryMap.size() ? 
				entries.size() : entryMap.size();
				
		for (int i = 0; i < maxIterations; i++) {
			Entry entry = (i < entries.size() ? entries.get(i) : null);
			EntryComposite entryComp = (i < entryMap.size() ? entryMap.get(i) : null);
			String value = (entryComp != null ? 
					entryComp.entry.getValue() : 
						(entry != null ? entry.getValue() : ""));
			
			// First, if the Entry isn't supposed to be displayed, dispose it
			// and move on (she ain't worth it, man...)
			if (entry == null || !entry.isReady()) {
				disposeEntry(i);
				continue;
				
			} else if (entryComp == null && entry.isReady()) {
				
	 			// If the EntryComposite hasn't been rendered yet, render it,
				// and add it to the entryMap
				renderEntry(entry, i);
				entryComp = entryMap.get(i);
				entryComp.setEntryValue(value);
				entryComp.refresh();
				
			} else {
			
				// Iterate through the Entry.AllowedValues and compare to the 
				// values of the corresponding EntryComposite in the map
				for (int j = 0; j < entry.getAllowedValues().size(); j++) {
					String allowedValue = entry.getAllowedValues().get(j);
					
					// Re-render Entries only if they've had a new AllowedValue 
					// added
					if (!entryComp.entry.getAllowedValues().contains(allowedValue)) {
						disposeEntry(i);
						renderEntry(entry, i);
						entryComp = entryMap.get(i);
						entryComp.setEntryValue(value);
						entryComp.refresh();
					}			
				}
			}
		}
		
		// Layout the DataComponentComposite. This can redraw stale widgets.
		layout();

		return;

	}

	/**
	 * This operation sets the DataComponent that should be rendered, updated
	 * and monitored by the composite.
	 * 
	 * @param component
	 *            The DataComponent
	 */
	public void setDataComponent(DataComponent component) {

		// If the Component is not null, store it and register as a listener
		if (component != null && component != dataComp) {

			// Unregister from the previous DataComponent if possible.
			if (dataComp != null) {
				dataComp.unregister(this);
			}
			// Set the reference to the component and register with it.
			dataComp = component;
			dataComp.register(this);

			// FIXME Currently, the only reference to this method is inside a
			// method that is run from the UI thread. However, it could
			// reasonably be called from a non-UI thread. Normally, we would use
			// asyncExec, but there seems to be some rendering issues (e.g.,
			// widgets do not draw or are not laid out) with asyncExec. syncExec
			// seems to resolve this issue, but it does block the current
			// thread.

			// Since the DataComponent is new, we can re-render the Entries. We
			// must do this on the Eclipse UI thread.
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				public void run() {
					// Dispose the old EntryComposites.
					disposeEntries();
					// Render the new EntryComposites.
					renderEntries();
				}
			});
		}

		return;
	}

	/**
	 * This operation retrieves the DataComponent that is currently rendered,
	 * updated and monitored by the composite.
	 * 
	 * @return The DataComponent
	 */
	public DataComponent getDataComponent() {
		return dataComp;
	}

	/**
	 * This operation renders the Entries in the DataComponent in the Section
	 * managed by the composite.
	 */
	private void renderEntries() {

		// Try to get the list of ready Entries from the DataComponent.
		List<Entry> entries = (dataComp != null ? 
				dataComp.retrieveAllEntries() : null);

		// If the list is not null and not empty, try to render the Entries.
		if (entries != null && !entries.isEmpty()) {
			// If the empty label has been rendered dispose of it.
			if (emptyLabel != null) {
				emptyLabel.dispose();
				emptyLabel = null;
				// Restore the default layout. Use the super method because the
				// overridden method is not necessary.
				super.setLayout(layout);
			}
			// Create EntryComposites for all ready Entries.
			for (int i = 0; i < entries.size(); i++) {
				Entry entry = entries.get(i);
				if (entry.isReady()) {
					renderEntry(entries.get(i), i);
				}
			}
		}

		// If no EntryComposites were created and the empty label is disposed,
		// create the empty label.
		if (entryMap.isEmpty() && emptyLabel == null) {
			// Set the layout to a single column. Call the super method so the
			// reference to the current layout is not changed.
			super.setLayout(new GridLayout());
			// Create the "no parameters" label.
			emptyLabel = new Label(this, SWT.NONE);
			emptyLabel.setText("No parameters available.");
			emptyLabel.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, true));

			// If not debugging, set the default background to white.
			if (System.getProperty("DebugICE") == null) {
				emptyLabel.setBackground(getBackground());
			} else {
				// If debugging, set the background to red and add some debug
				// information to the label's text.
				emptyLabel.setText(emptyLabel.getText() + "\nDataComponent: "
						+ (dataComp != null ? dataComp.getName() : "null"));
				emptyLabel.setBackground(Display.getCurrent().getSystemColor(
						SWT.COLOR_RED));
			}
		}

		// Layout the DataComponentComposite. This can redraw stale widgets.
		layout();

		return;
	}

	/**
	 * This operation disposes the existing EntryComposites and clears the
	 * current map of Entries.
	 */
	private void disposeEntries() {

		// Dispose the composites from GUI
		for (EntryComposite comp : entryMap.values()) {
			comp.dispose();
		}

		// Clear the list
		entryMap.clear();

		return;
	}

	/**
	 * This operation creates an EntryComposite for an Entry and adds that Entry
	 * to the EntryMap.
	 * 
	 * @param entry
	 *            The Entry for which the Control should be created.
	 * @param index
	 * 			  The index in the entryMap of where the rendered EntryComposite
	 * 			  will be put.
	 */
	private void renderEntry(Entry entry, int index) {

		// Local Declarations
		EntryComposite entryComposite = null;

		// Create a listener for the Entry composite that will pass events to
		// any other listeners.
		Listener entryListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// Let them know
				notifyListeners(SWT.Selection, new Event());
			}
		};

		// Create the EntryComposite.
		entryComposite = new EntryComposite(this, SWT.FLAT, entry);

		// Decorate the EntryComposite. Use the FormToolKit if possible.
		if (formToolkit != null) {
			formToolkit.adapt(entryComposite);
		} else {
			entryComposite.setBackground(getBackground());
		}

		// Set the LayoutData. The DataComponentComposite has a GridLayout. The
		// EntryComposite should grab all available horizontal space.
		entryComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
				true, false));
		// Set the Listener.
		entryComposite.addListener(SWT.Selection, entryListener);
		
		// Add the EntryComposite to the Map
		entryMap.put(index, entryComposite);
		
		// Lastly, reorder the EntryComposites on this DataComponentComposite 
		// to be in the correct order (according to their index in the entryMap)
		
		// Begin by getting a list of the EntryComposites as they appear in the 
		// UI, and make a map indexing them based on their order.
		List<Control> uiEntryComps = Arrays.asList(getChildren());
		HashMap<Integer, EntryComposite> uiMap = 
				new HashMap<Integer, EntryComposite>();
		for (int i = 0; i < uiEntryComps.size(); i++) {
			EntryComposite e = (EntryComposite) uiEntryComps.get(i);
			if (e != null ) {
				uiMap.put(i, e);
			}
		}
		
		// Go through the entryMap and compare the order to the uiMap order
		for (int i = 0; i < entryMap.size(); i++) {
			
			// Get the Entries from the entryMap and UI and make sure they're
			// valid
			Entry mapEntry = null, uiEntry = null;
			if (entryMap.get(i) != null)
				mapEntry = entryMap.get(i).entry;
			if (uiMap.get(i) != null)
				uiEntry = uiMap.get(i).entry;
			if (mapEntry == null || uiEntry == null) {
				continue;
			}
			
			// If the Entries don't match, find where in the UI the 
			// corresponding EntryComposite really is
			int uiPosition = 0;
			if (!mapEntry.getName().equals(uiEntry.getName())) {
				for (int j = 0; j < uiEntryComps.size(); j++) {
					Entry e = uiMap.get(j).entry;
					if (e.getName().equals(mapEntry.getName())) {
						uiPosition = j;
						break;
					}
				}
				
				// Now determine where to move the UI EntryComposite
				if (i-1 >= 0) {
					// Try getting the EntryComposite above the proper location
					EntryComposite entryAbove = uiMap.get(i-1);
					// Move the UI EntryComposite into its correct position
					if (entryAbove != null) {
						uiMap.get(uiPosition).moveBelow(entryAbove);
					}
				} else if (i+1 <= entryMap.size()) {
					// Try getting the EntryComposite below the proper location
					EntryComposite entryBelow = uiMap.get(i+1);
					// Move the UI EntryComposite into its correct position
					if (entryBelow != null) {
						uiMap.get(uiPosition).moveAbove(entryBelow);
					}
				}
			}
		}

		return;
	}

	/**
	 * This operation removes an Entry and its associated SWT Control from the
	 * composite.
	 * 
	 * @param index
	 *            The index of the EntryComposite in the entryMap that will be
	 *            removed.
	 */
	private void disposeEntry(int index) {

		// Remove the Control
		EntryComposite composite = entryMap.remove(index);
		if (composite != null) {
			composite.dispose();
		}

		return;
	}

	/**
	 * Overrides the default behavior to store a reference to the layout. This
	 * is required for the following reason: when there are no Entries, a label
	 * is displayed. A different layout is necessary to display the label by
	 * itself.
	 * 
	 * @param layout
	 *            The new layout.
	 */
	@Override
	public void setLayout(Layout layout) {
		super.setLayout(layout);

		// If the layout is not null, store a reference to it so we can revert
		// to it if new EntryComposites are added when the empty label is
		// displayed.
		if (layout != null) {
			this.layout = layout;
		}

		return;
	}

	/**
	 * This operation sets the message manager that should be used by this
	 * composite and its children to post messages.
	 * 
	 * @param manager
	 *            The message manager.
	 */
	public void setMessageManager(IMessageManager manager) {

		// Set the class reference
		messageManager = manager;

		// Set the message manager if it is available
		if (messageManager != null) {
			for (EntryComposite composite : entryMap.values()) {
				composite.setMessageManager(messageManager);
			}
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateableListener#update(Component component)
	 */
	public void update(IUpdateable component) {

		// When the DataComponent has updated, refresh on the Eclipse UI thread.
		if (component == dataComp) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (!DataComponentComposite.this.isDisposed()) {
						refresh();
					} else {
						dataComp.unregister(DataComponentComposite.this);
					}
				}
			});
		}

		return;
	}
}
