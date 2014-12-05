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
package org.eclipse.ice.client.widgets.moose;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.client.widgets.properties.IButtonCellContentProvider;
import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.ice.client.widgets.properties.TreeProperty;
import org.eclipse.ice.client.widgets.properties.TreePropertyCellContentProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class provides an {@link ICellContentProvider} for cells that display a
 * checkbox <code>Button</code>.
 * <p>
 * <b>Note:</b> There is a limitation with embedding SWT widgets in a
 * <code>TableViewer</code>. In particular, performance decreases due to the
 * number of widgets that may be involved. This class uses a workaround by
 * creating images for each state of a checkbox <code>Button</code>. The states
 * are determined by combining enabled/disabled and selected/unselected (or
 * checked/unchecked). The four {@link Image}s are registered with the JFace
 * {@link ImageRegistry} and placed in each cell that is not currently being
 * edited.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CheckboxCellContentProvider extends
		TreePropertyCellContentProvider implements IButtonCellContentProvider {

	/**
	 * Whether or not the platform-specific checkbox images have been generated
	 * and registered with the JFace <code>ImageRegistry</code>.
	 */
	private static final AtomicBoolean IMAGES_REGISTERED = new AtomicBoolean(
			false);
	/**
	 * The JFace <code>ImageRegistry</code> key for the enabled and checked
	 * checkbox <code>Image</code>.
	 */
	private static final String ENABLED_CHECKED = "ENABLED_CHECKED";
	/**
	 * The JFace <code>ImageRegistry</code> key for the enabled and unchecked
	 * checkbox <code>Image</code>.
	 */
	private static final String ENABLED_UNCHECKED = "ENABLED_UNCHECKED";
	/**
	 * The JFace <code>ImageRegistry</code> key for the disabled and checked
	 * checkbox <code>Image</code>.
	 */
	private static final String DISABLED_CHECKED = "DISABLED_CHECKED";
	/**
	 * The JFace <code>ImageRegistry</code> key for the disabled and unchecked
	 * checkbox <code>Image</code>.
	 */
	private static final String DISABLED_UNCHECKED = "DISABLED_UNCHECKED";

	/**
	 * The default constructor.
	 * 
	 * @param viewer
	 *            The viewer whose contents are managed by this provider.
	 */
	public CheckboxCellContentProvider(ColumnViewer viewer) {

		// Make sure the checkbox Images have been registered before calls to
		// getImage(Object).
		registerImages(viewer.getControl().getDisplay(), viewer.getControl()
				.getBackground());

		return;
	}

	/**
	 * The cell is enabled if the {@link TreeProperty}'s {@link Entry} is
	 * required, otherwise it is disabled.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return isValid(element)
				&& !((TreeProperty) element).getEntry().isRequired();
	}

	/**
	 * The cell is selected if its {@link TreeProperty}'s {@link Entry}'s tag is
	 * set to "true", false otherwise.
	 */
	@Override
	public boolean isSelected(Object element) {
		return (Boolean) getValue(element);
	}

	/**
	 * Override the default tool tip to provide information about being enabled
	 * or disabled.
	 */
	@Override
	public String getToolTipText(Object element) {
		String tooltip;

		if (isSelected(element)) {
			tooltip = "This parameter is enabled.\n"
					+ "It will be included in the input file.";
		} else {
			tooltip = "This parameter is disabled.\n"
					+ "It will be commented out in the input file.";
		}

		return tooltip;
	}

	/**
	 * Provides a checkbox <code>Image</code> depending on whether the cell is
	 * enabled/disabled and selected/unselected.
	 */
	@Override
	public Image getImage(Object element) {
		Image image = null;

		// Depending on whether or not the element is checked and enabled, set
		// its image.
		if (isEnabled(element)) {
			if (isSelected(element)) {
				image = JFaceResources.getImage(ENABLED_CHECKED);
			} else {
				image = JFaceResources.getImage(ENABLED_UNCHECKED);
			}
		} else if (isSelected(element)) {
			image = JFaceResources.getImage(DISABLED_CHECKED);
		} else {
			image = JFaceResources.getImage(DISABLED_UNCHECKED);
		}

		return image;
	}

	/**
	 * Returns true if the element is valid and the {@link TreeProperty}'s
	 * {@link Entry}'s tag is not "false" (case ignored), false otherwise.
	 */
	@Override
	public Object getValue(Object element) {

		// First, check that the element is valid.
		boolean isSelected = isValid(element);

		// If the element is valid, we should mark the flag as false only if the
		// tag is some variation of "false" (case ignored).
		if (isSelected) {
			String tag = ((TreeProperty) element).getEntry().getTag();
			isSelected = tag == null
					|| !"false".equals(tag.trim().toLowerCase());
		}

		return isSelected;
	}

	/**
	 * Sets the {@link TreeProperty}'s {@link Entry}'s tag to "true" if the
	 * value is true and "false" otherwise.
	 */
	@Override
	public boolean setValue(Object element, Object value) {

		boolean changed = false;

		if (isValid(element) && value != null) {
			String newValue = ((Boolean) value).toString();
			Entry entry = ((TreeProperty) element).getEntry();
			// If the value has changed, mark the changed flag and set the new
			// value.
			if (changed = !newValue.equals(entry.getTag())) {
				entry.setTag(newValue);
			}
		}

		return changed;
	}

	/**
	 * If the enabled/disabled checked/unchecked checkbox <code>Image</code>s
	 * have not already been created and added to the JFace
	 * <code>ImageRegistry</code>, this method generates and registers them.
	 * This produces platform-specific images at run-time.
	 * 
	 * @param display
	 *            The <code>Display</code> shared with the underlying
	 *            <code>ColumnViewer</code>.
	 * @param background
	 *            The background of the <code>ColumnViewer</code>'s cells.
	 */
	private static void registerImages(final Display display, Color background) {

		if (display != null && IMAGES_REGISTERED.compareAndSet(false, true)) {

			// Get the ImageRegistry for JFace images. We will load up images
			// for each combination of checkbox enabled/disabled and
			// checked/unchecked.
			ImageRegistry jfaceImages = JFaceResources.getImageRegistry();

			// Create a temporary shell and checkbox Button to generate
			// platform-specific images of checkboxes.
			Shell shell = new Shell(display, SWT.NO_TRIM);
			Button checkbox = new Button(shell, SWT.CHECK);

			// Set the widget's background to the viewer's cell background.
			checkbox.setBackground(background);

			// Reduce the size of the shell to a square (checkboxes are supposed
			// to be square!!!).
			Point size = checkbox.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			// int width = Math.min(size.x, size.y);
			checkbox.setSize(size);
			// checkbox.setLocation(width - size.x, width - size.y);
			// shell.setSize(width, width);
			shell.setSize(size);

			// Open the shell to enable widget drawing.
			shell.open();

			// Create the enabled/unchecked image.
			jfaceImages.put(ENABLED_UNCHECKED, createImage(checkbox));
			// Create the enabled/checked image.
			checkbox.setSelection(true);
			jfaceImages.put(ENABLED_CHECKED, createImage(checkbox));
			// Create the disabled/checked image.
			checkbox.setEnabled(false);
			jfaceImages.put(DISABLED_CHECKED, createImage(checkbox));
			// Create the disabled/unchecked image.
			checkbox.setSelection(false);
			jfaceImages.put(DISABLED_UNCHECKED, createImage(checkbox));

			// Release any remaining resources.
			// gc.dispose();
			shell.close();
		}

		return;
	}

	/**
	 * Creates an <code>Image</code> from the specified <code>Control</code>
	 * (widget).
	 * 
	 * @param control
	 *            The <code>Control</code> to convert to an <code>Image</code>.
	 * @return An <code>Image</code> of the specified <code>Control</code>.
	 */
	private static Image createImage(Control control) {

		// We have to create a separate Image per combination of
		// checked/unchecked (of course). We also have to create a single GC per
		// Image (not so obvious, but re-using them does not appear to work well
		// and does not sync with the UI thread properly, so you get the same
		// image multiple times [but not all the time]).

		Image image = new Image(control.getDisplay(), control.getBounds());
		GC gc = new GC(image);
		control.print(gc);
		gc.dispose();

		return image;
	}

}
