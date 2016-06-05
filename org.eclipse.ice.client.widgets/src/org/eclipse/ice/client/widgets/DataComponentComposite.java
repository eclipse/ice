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

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider;
import org.eclipse.ice.client.widgets.providers.Default.DefaultEntryCompositeProvider;
import org.eclipse.january.form.DataComponent;
import org.eclipse.january.form.IEntry;
import org.eclipse.january.form.IUpdateable;
import org.eclipse.january.form.IUpdateableListener;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an SWT Composite that is specialized to render ICE
 * DataComponents. It can take a message manager for posting messages and it can
 * be configured to post save events.
 *
 * @author Jay Jay Billings, Jordan H. Deyton, Anna Wojtowicz, Alex McCaskey
 */
public class DataComponentComposite extends Composite
		implements IUpdateableListener {

	/**
	 * Reference to the logger service.
	 */
	protected Logger logger = LoggerFactory
			.getLogger(DataComponentComposite.class);

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
	private SortedMap<Integer, IEntryComposite> entryMap = Collections
			.synchronizedSortedMap(new TreeMap<Integer, IEntryComposite>());

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
	 * {@code AbstractEntryComposite}s. Otherwise, some default decorations are
	 * applied.
	 */
	public FormToolkit formToolkit = null;

	/**
	 * The eclipse context
	 */
	private IEclipseContext context;

	/**
	 * The constructor.
	 *
	 * @param comp
	 *            The DataComponent shown in the composite.
	 * @param parentComposite
	 *            The parent of this composite.
	 * @param style
	 *            The style in which the composite should be drawn.
	 */
	public DataComponentComposite(DataComponent comp, Composite parentComposite,
			int style) {

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

		// Get the E4 Context. For now I need to grab it from the singleton.
		context = PlatformUI.getWorkbench().getService(IEclipseContext.class);

		// Instruct the framework to perform dependency injection for
		// this Form using the ContextInjectionFactory.
		ContextInjectionFactory.inject(this, context);

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
		// needs to be registered with the AbstractEntryComposite and the Text
		// widget,
		// in case it is being used by JFace, which doesn't always post standard
		// SWT events.
		Listener enterListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// Notify the EntryComposites that the selection was made
				for (IEntryComposite entryComp : entryMap.values()) {
					entryComp.getComposite()
							.notifyListeners(SWT.DefaultSelection, e);
				}
			}
		};
		addListener(SWT.DefaultSelection, enterListener);

		return;
	}

	/**
	 * This operation handles a re-draw of EntryComposites that have been
	 * recently changed. This method attempts to avoid a complete redraw of all
	 * EntryComposites and only re-draws those that have been recently changed.
	 */
	public void refresh() {

		// Local Declarations
		List<IEntry> entries = dataComp.retrieveAllEntries();

		// If there's an empty label set and it's not longer necessary, dispose
		if (!entries.isEmpty() && emptyLabel != null) {
			emptyLabel.dispose();
			emptyLabel = null;
		}

		// If a selection change triggered this refresh, make sure to update
		// any Entries on the dataComp that need their value(s) updating
		for (int i = 0; i < entries.size(); i++) {
			IEntry entry = dataComp.retrieveAllEntries().get(i);
			IEntryComposite entryComp = entryMap.get(i);
			if (entryComp != null && !entry.getValue()
					.equals(entryComp.getEntry().getValue())) {
				// Reset the reference to the Entry because depending on the way
				// its value was updated it could be an entirely new Entry
				// (reset vs. cloned/destructive copy).
				entryComp.setEntry(entry);
				entryComp.refresh();
			}
		}

		// Begin comparing the list of Entries to the EntryComposites in
		// entryMap to determine what needs to be done
		int maxIterations = entries.size() > entryMap.size() ? entries.size()
				: entryMap.size();

		for (int i = 0; i < maxIterations; i++) {
			IEntry entry = (i < entries.size() ? entries.get(i) : null);
			IEntryComposite entryComp = (i < entryMap.size() ? entryMap.get(i)
					: null);
			String value = (entryComp != null ? entryComp.getEntry().getValue()
					: (entry != null ? entry.getValue() : ""));

			// First, if the Entry isn't supposed to be displayed, dispose it
			// and move on (she ain't worth it, man...)
			if (entry == null || !entry.isReady()) {
				disposeEntry(i);
				continue;

			} else if (entryComp == null && entry.isReady()) {

				// If the AbstractEntryComposite hasn't been rendered yet,
				// render
				// it,
				// and add it to the entryMap
				renderEntry(entry, i);
				entryComp = entryMap.get(i);
				entryComp.getEntry().setValue(value);
				entryComp.refresh();

			} else {

				// Iterate through the Entry.AllowedValues and compare to the
				// values of the corresponding AbstractEntryComposite in the map
				int nAllowed = 0;

				// This IEntry may not be a DiscreteEntry, so check
				// that we can get allowed values here, continue if not.
				try {
					nAllowed = entry.getAllowedValues().size();
				} catch (UnsupportedOperationException e) {
					continue;
				}

				for (int j = 0; j < nAllowed; j++) {
					String allowedValue = entry.getAllowedValues().get(j);

					// Re-render Entries only if they've had a new AllowedValue
					// added
					if (!entryComp.getEntry().getAllowedValues()
							.contains(allowedValue)) {
						disposeEntry(i);
						renderEntry(entry, i);
						entryComp = entryMap.get(i);
						entryComp.getEntry().setValue(value);
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
				@Override
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
		List<IEntry> entries = (dataComp != null ? dataComp.retrieveAllEntries()
				: null);

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
				IEntry entry = entries.get(i);
				if (entry.isReady()) {
					renderEntry(entry, i);
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
			emptyLabel.setLayoutData(
					new GridData(SWT.FILL, SWT.BEGINNING, true, true));

			// If not debugging, set the default background to white.
			if (System.getProperty("DebugICE") == null) {
				emptyLabel.setBackground(getBackground());
			} else {
				// If debugging, set the background to red and add some debug
				// information to the label's text.
				emptyLabel.setText(emptyLabel.getText() + "\nDataComponent: "
						+ (dataComp != null ? dataComp.getName() : "null"));
				emptyLabel.setBackground(
						Display.getCurrent().getSystemColor(SWT.COLOR_RED));
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
		for (IEntryComposite comp : entryMap.values()) {
			comp.getComposite().dispose();
		}

		// Clear the list
		entryMap.clear();

		return;
	}

	/**
	 * This operation creates an AbstractEntryComposite for an Entry and adds
	 * that Entry to the EntryMap.
	 *
	 * @param entry
	 *            The Entry for which the Control should be created.
	 * @param index
	 *            The index in the entryMap of where the rendered
	 *            AbstractEntryComposite will be put.
	 */
	private void renderEntry(IEntry entry, int index) {

		// Check the entry map to see if there is already a composite for this
		// entry. If there is, fail silently.
		for (IEntryComposite entryTemp : entryMap.values()) {
			if (entryTemp.getEntry().equals(entry)) {
				return;
			}
		}

		// Local Declarations
		IEntryComposite entryComposite = null;

		// Create the Provider for Entry Composites
		IEntryCompositeProvider provider = null;

		String contextKey = entry.getContext();
		// Get the appropriate entry provider if indicated and available in the
		// e4 context
		if (context.containsKey(contextKey)) {
			provider = (IEntryCompositeProvider) context.get(contextKey);
		} else {
			// Otherwise fall back to the default
			provider = new DefaultEntryCompositeProvider();
		}

		// Create the IEntryComposite.
		entryComposite = provider.getEntryComposite(this, entry, SWT.FLAT,
				formToolkit);

		if (messageManager != null) {
			entryComposite.setMessageManager(messageManager);
		}

		// Add theIEntryComposite to the Map
		entryMap.put(index, entryComposite);

		// Lastly, reorder the EntryComposites on this DataComponentComposite
		// to be in the correct order (according to their index in the entryMap)

		// Begin by getting a list of the EntryComposites as they appear in the
		// UI, and make a map indexing them based on their order.
		List<Control> uiEntryComps = Arrays.asList(getChildren());
		HashMap<Integer, IEntryComposite> uiMap = new HashMap<Integer, IEntryComposite>();
		for (int i = 0; i < uiEntryComps.size(); i++) {
			IEntryComposite e = (IEntryComposite) uiEntryComps.get(i);
			if (e != null) {
				uiMap.put(i, e);
			}
		}

		// Go through the entryMap and compare the order to the uiMap order
		for (int i = 0; i < entryMap.size(); i++) {

			// Get the Entries from the entryMap and UI and make sure they're
			// valid
			IEntry mapEntry = null, uiEntry = null;
			if (entryMap.get(i) != null)
				mapEntry = entryMap.get(i).getEntry();
			if (uiMap.get(i) != null)
				uiEntry = uiMap.get(i).getEntry();
			if (mapEntry == null || uiEntry == null) {
				continue;
			}

			// If the Entries don't match, find where in the UI the
			// corresponding AbstractEntryComposite really is
			int uiPosition = 0;
			if (!mapEntry.getName().equals(uiEntry.getName())) {
				for (int j = 0; j < uiEntryComps.size(); j++) {
					IEntry e = uiMap.get(j).getEntry();
					if (e.getName().equals(mapEntry.getName())) {
						uiPosition = j;
						break;
					}
				}

				// Now determine where to move the UI AbstractEntryComposite
				if (i - 1 >= 0) {
					// Try getting the AbstractEntryComposite above the proper
					// location
					IEntryComposite entryAbove = uiMap.get(i - 1);
					// Move the UI AbstractEntryComposite into its correct
					// position
					if (entryAbove != null) {
						uiMap.get(uiPosition).getComposite()
								.moveBelow(entryAbove.getComposite());
					}
				} else if (i + 1 <= entryMap.size()) {
					// Try getting the AbstractEntryComposite below the proper
					// location
					IEntryComposite entryBelow = uiMap.get(i + 1);
					// Move the UI AbstractEntryComposite into its correct
					// position
					if (entryBelow != null) {
						uiMap.get(uiPosition).getComposite()
								.moveAbove(entryBelow.getComposite());
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
	 *            The index of the AbstractEntryComposite in the entryMap that
	 *            will be removed.
	 */
	private void disposeEntry(int index) {

		// Remove the Control
		IEntryComposite composite = entryMap.remove(index);
		if (composite != null) {
			composite.getComposite().dispose();
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		// Clean up the dependency injection
		ContextInjectionFactory.uninject(this, context);
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
			for (IEntryComposite composite : entryMap.values()) {
				composite.setMessageManager(messageManager);
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org
	 * .eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		// When the DataComponent has updated, refresh on the Eclipse UI thread.
		if (component == dataComp) {
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				@Override
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
