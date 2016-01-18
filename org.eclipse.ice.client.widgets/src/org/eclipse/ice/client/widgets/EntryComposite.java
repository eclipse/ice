package org.eclipse.ice.client.widgets;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EntryComposite extends Composite implements IUpdateableListener {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(EntryComposite.class);

	/**
	 * A label that describes the Entry.
	 */
	protected Label label;

	/**
	 * The Entry that is displayed by the EntryComposite.
	 */
	protected IEntry entry;

	/**
	 * 
	 */
	protected Control widget;

	/**
	 * The currently set value of the Entry.
	 */
	protected String currentSelection;

	/**
	 * Entry map of binary/boolean-type allowed values
	 */
	protected final List<String> allowedBinaryValues = new ArrayList<String>();

	/**
	 * List of allowed values for the entry in lowercase text
	 */
	protected final List<String> lowercaseAllowedValues = new ArrayList<String>();

	/**
	 * A set of buttons for the Entry.
	 */
	protected final List<Button> buttons;

	/**
	 * The message manager to which message about the success or failure of
	 * manipulating the Entry should be posted.
	 */
	protected IMessageManager messageManager;

	/**
	 * The name of the message posted to the message manager.
	 */
	protected String messageName;

	/**
	 * This listens to the {@code EntryComposite}'s resize events and adjusts
	 * the size of the dropdown if necessary. This is currently only used for
	 * file entries.
	 */
	protected ControlListener resizeListener = null;

	/**
	 * A ControlDecoration that can be added to the EntryComposite if desired.
	 */
	protected ControlDecoration decoration = null;

	protected String entryCompositeName;

	/**
	 * The Constructor
	 *
	 * @param parent
	 *            The parent Composite.
	 * @param style
	 *            The style of the EntryComposite.
	 * @param refEntry
	 *            An Entry that should be used to create the widget, to update
	 *            when changed by the user and to be updated from when changed
	 *            internally by ICE.
	 */
	public EntryComposite(Composite parent, IEntry refEntry) {

		// Call the super constructor
		super(parent, SWT.FLAT);

		// Set the Entry reference
		if (refEntry != null) {
			entry = refEntry;
		} else {
			throw new RuntimeException("Entry passed to EntryComposite " + "constructor cannot be null!");
		}

		// Setup the allowedBinaryValues for check boxes
		// Setup the list of values that are equivalent to "ready"
		allowedBinaryValues.add("ready");
		allowedBinaryValues.add("yes");
		allowedBinaryValues.add("y");
		allowedBinaryValues.add("true");
		allowedBinaryValues.add("enabled");
		allowedBinaryValues.add("on");
		// Setup the list of values that are equivalent to "not ready"
		allowedBinaryValues.add("not ready");
		allowedBinaryValues.add("no");
		allowedBinaryValues.add("n");
		allowedBinaryValues.add("false");
		allowedBinaryValues.add("disabled");
		allowedBinaryValues.add("off");

		// Create the Buttons array
		buttons = new ArrayList<Button>();

		// Create the MessageName String
		messageName = new String();
		messageName = entry.getName() + " " + entry.getId();

		// Register for updates from the Entry
		entry.register(this);

		// Add a listener to the Entry that unregisters this composite as a
		// listener upon disposal.
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				entry.unregister(EntryComposite.this);
			}
		});

		// Get a reference to the current Entry value
		currentSelection = entry.getValue();

		// Render the entry
		render();

		return;
	}

	/**
	 * This operation renders the SWT widgets for the Entry.
	 */
	protected abstract void render();

	protected abstract String getContextId();

	/**
	 * This operation directs the EntryComposite to refresh its view of the
	 * Entry. This should be called in the event that the Entry has changed on
	 * the file system and the view needs to be updated.
	 */
	public void refresh() {

		// Print an error if this Entry has been prematurely disposed.
		if (isDisposed()) {
			logger.info("EntryComposite Message: " + "This composite has been prematurely disposed!");
			return;
		}

		if (widget != null) {
			System.out.println("DISPOSING WIDGET : " + entry.getName());
			widget.dispose();
			widget = null;
		}
		
		for (Button button : buttons) {
			if (!button.isDisposed()) {
				button.dispose();
			}
		}

		// Remove all of the previous buttons.
		buttons.clear();

		// Remove the resize listener.
		if (resizeListener != null) {
			removeControlListener(resizeListener);
			resizeListener = null;
		}

		// Re-render the Composite
		render();

		// Re-draw the Composite
		layout();

		return;
	}

	/**
	 * This operation sets the Message Manager that should be used by the
	 * EntryComposite to post messages about the Entry. If the Message Manager
	 * is not set, the EntryComposite will not attempt to post messages.
	 *
	 * @param manager
	 *            The Message Manager that the EntryComposite should use.
	 */
	public void setMessageManager(IMessageManager manager) {

		// Set the messageManager
		messageManager = manager;

		return;
	}

	/**
	 * Returns the entry stored on this Composite
	 *
	 * @return The Entry rendered by this Composite.
	 */
	public IEntry getEntry() {
		return entry;
	}

	/**
	 * Creates a label for the EntryComposite.
	 */
	protected void createLabel() {

		// Create the Label
		label = new Label(this, SWT.WRAP);
		label.setText(entry.getName() + ":");
		label.setToolTipText(entry.getDescription());
		label.setBackground(getBackground());

		return;
	}

	/**
	 * This method is responsible for toggling a ControlDecoration on and off on
	 * the EntryComposite. The decoration will toggle on if the editor is dirty
	 * and the selection was recently changed (monitored by
	 * {@link EntryComposite#currentSelection}). Otherwise, it will toggle off.
	 */
	public void toggleSaveDecoration() {

		if (decoration == null) {
			// Create a new decoration and message
			decoration = new ControlDecoration(this, SWT.TOP | SWT.LEFT);
			final String saveMessage = "The form contains unsaved changes";

			// Set a description and image
			decoration.setDescriptionText(saveMessage);
			Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING)
					.getImage();
			decoration.setImage(image);

			// Set a listener to hide/show the decoration according to the
			// editor's state and the current entry value
			final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
			editor.addPropertyListener(new IPropertyListener() {
				@Override
				public void propertyChanged(Object source, int propId) {
					// Toggle the decoration on if the form is dirty and the
					// value has changed
					if (editor != null) {
						if (editor.isDirty() && !EntryComposite.this.entry.getValue().equals(currentSelection)) {
							// Show the decoration
							EntryComposite.this.decoration.show();
						} else if (!editor.isDirty()) {
							// Hide the decoration
							EntryComposite.this.decoration.hide();
						}
					}

					return;
				}
			});
		}

		// If the decoration already exists, check the Entry's state and set the
		// decoration as needed.
		else {
			final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
			if (editor != null) {
				if (editor.isDirty() && !EntryComposite.this.entry.getValue().equals(currentSelection)) {
					// Show the decoration
					EntryComposite.this.decoration.show();
				} else if (!editor.isDirty()) {
					// Hide the decoration
					EntryComposite.this.decoration.hide();
				}
			}
		}

		return;
	}

	@Override
	public void update(IUpdateable component) {
		// TODO Auto-generated method stub
		// When the Entry has updated, refresh on the Eclipse UI thread.
		if (component == entry) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!EntryComposite.this.isDisposed()) {

						// Refresh the EntryComposite
						refresh();

						// Toggle the "unsaved changes" decoration if the entry
						// value is different
						if (!EntryComposite.this.entry.getValue().equals(currentSelection)) {
							toggleSaveDecoration();
						}

						// Update the reference to the Entry's value
						currentSelection = EntryComposite.this.entry.getValue();

					} else {
						entry.unregister(EntryComposite.this);
					}
				}
			});
		}
	}

	/**
	 * 
	 * @param parent
	 * @param entry
	 * @return
	 */
	public static EntryComposite getEntryComposite(Composite parent, IEntry entry) {

		EntryComposite entryComposite = null;
		String id = "org.eclipse.ice.client.widgets.entryComposite";
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (IConfigurationElement element : elements) {
				String attribute = element.getAttribute("class");
				String contributorName = element.getDeclaringExtension().getContributor().getName();
				System.out.println("HELLO: " + attribute + ", " + contributorName);
				try {
					Class<?> javaClass = Platform.getBundle(contributorName).loadClass(attribute);
					Constructor<?> ctor = javaClass.getDeclaredConstructor(Composite.class, IEntry.class);
					EntryComposite tempEntryComposite = (EntryComposite) ctor.newInstance(parent, entry);
					System.out.println("CONTEXT: " + entry.getContextId() + ", " + tempEntryComposite.getContextId());
					if (tempEntryComposite.getContextId().equals(entry.getContextId())) {
						entryComposite = tempEntryComposite;
						break;
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return entryComposite;

	}
}
