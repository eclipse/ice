/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *    
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;

/**
 * This class provides a custom {@code Composite} that is meant to be a
 * descendant of a {@link SharedScrolledComposite}.
 * 
 * <p>
 * Typically, such a scrolled {@code Composite} will render both vertical and
 * horizontal scroll bars when the <i>preferred</i> sizes of the descendant
 * {@code Composite}s is greater than its client area (excluding the scroll
 * bars).
 * </p>
 * <p>
 * In most cases, this behavior is acceptable. However, a child
 * {@code Composite} may have a large default "preferred" width, which causes
 * the horizontal scroll bars to appear. In such cases, it may be desirable to
 * shrink the child {@code Composite} instead of creating a horizontal scroll
 * bar.
 * </p>
 * <p>
 * This class handles this. The horizontal scroll bar will only be triggered
 * when the client area of the scrolled ancestor {@code Composite} is less than
 * {@link #minWidth}.
 * </p>
 * <p>
 * <b>Note:</b> An instance of this class is expected to occupy the entire
 * horizontal width of the scrolled ancestor {@code Composite}, e.g. in a
 * {@link FillLayout} or a {@link GridLayout} with only a single column.
 * Unexpected behavior may occur if this is not the case.
 * </p>
 *
 * 
 * @author Jordan Deyton
 *
 */
public class ScrollClientComposite extends Composite {

	/**
	 * The width at which this {@code Composite} will stop getting smaller, thus
	 * forcing its ancestor {@code ScrolledComposite} to use a horizontal scroll
	 * bar.
	 */
	private int minWidth = 100;

	/**
	 * The ancestor {@code ScrolledComposite}. Note that we actually use a
	 * {@link SharedScrolledComposite}, primarily due to its
	 * {@link SharedScrolledComposite#reflow(boolean) reflow(boolean)} method.
	 */
	private final SharedScrolledComposite scrolledAncestor;

	/**
	 * The total distance between the left edge of the {@link #scrolledAncestor}
	 * 's client area and the left edge of this {@code Composite}.
	 */
	private int totalPadLeft = 0;

	/**
	 * The total distance between the right edge of the
	 * {@link #scrolledAncestor}'s client area and the right edge of this
	 * {@code Composite}.
	 */
	private int totalPadRight = 0;

	/**
	 * The default constructor. Creates a {@link ScrollClientComposite}
	 * constructor. The parent and style are passed to the {@link Composite}
	 * constructor. The {@link #minWidth} is set to its default value. The
	 * closest scrolled ancestor is determined automatically, but an
	 * {@code IllegalArgumentException} is thrown if one cannot be found.
	 * 
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null).
	 * @param style
	 *            The style of widget to construct.
	 */
	public ScrollClientComposite(Composite parent, int style) {
		super(parent, style);

		// Find the ancestor ScrolledComposite.
		while (parent != null && !(parent instanceof SharedScrolledComposite)) {
			parent = parent.getParent();
		}

		// If a ScrolledComposite was found, then set the class reference to it.
		// Otherwise throw an exception.
		if (parent != null) {
			scrolledAncestor = (SharedScrolledComposite) parent;
		} else {
			scrolledAncestor = null;
			throw new IllegalArgumentException(
					"HorizontalScrollManager error: "
							+ "Client Control does not have a ScrolledComposite ancestor.");
		}

		// We need to add a resize listener to reflow the ancestor
		// ScrolledComposite when it's client area is too small.
		createResizeListener();

		// We must ensure the padding is correct for resizing.
		recomputePads();

		return;
	}

	/**
	 * Creates a {@link ScrollClientComposite} constructor. The parent and style
	 * are passed to the {@link Composite} constructor. The {@link #minWidth} is
	 * set. The closest scrolled ancestor is determined automatically, but an
	 * {@code IllegalArgumentException} is thrown if one cannot be found.
	 * 
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null).
	 * @param style
	 *            The style of widget to construct.
	 * @param minWidth
	 *            The width at which this {@code Composite} will stop getting
	 *            smaller, thus forcing its ancestor {@code ScrolledComposite}
	 *            to use a horizontal scroll bar. If less than 0, the value 0
	 *            will be used instead.
	 */
	public ScrollClientComposite(Composite parent, int style, int minWidth) {
		this(parent, style);
		setMinWidth(minWidth);
	}

	/**
	 * Creates a {@link ScrollClientComposite}. The parent and style are passed
	 * to the {@link Composite} constructor. The ancestor is verified, and the
	 * {@link #minWidth} is set to its default value.
	 * 
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null).
	 * @param style
	 *            The style of widget to construct.
	 * @param scrolledAncestor
	 *            The ancestor scrolled {@code Composite}. Must be a non-null
	 *            ancestor (parent, grandparent, etc.), else an
	 *            {@code IllegalArgumentException} will be thrown.
	 */
	public ScrollClientComposite(Composite parent, int style,
			SharedScrolledComposite scrolledAncestor) {
		super(parent, style);

		// Check the scrolledAncestor parameter for null.
		if (scrolledAncestor == null) {
			this.scrolledAncestor = null;
			throw new IllegalArgumentException(
					"HorizontalScrollManager error: "
							+ "Null ancestor ScrolledComposite passed to constructor!");
		}

		// Find the scrolledAncestor among this Composite's ancestors.
		while (parent != null && parent != scrolledAncestor) {
			parent = parent.getParent();
		}

		// If the correct ancestor was found, then set the class reference to
		// it. Otherwise, throw an exception.
		if (parent != null) {
			this.scrolledAncestor = scrolledAncestor;
		} else {
			this.scrolledAncestor = null;
			throw new IllegalArgumentException(
					"HorizontalScrollManager error: "
							+ "Specified ScrolledComposite is not an ancestor of client Control.");
		}

		// We need to add a resize listener to reflow the ancestor
		// ScrolledComposite when it's client area is too small.
		createResizeListener();

		// We must ensure the padding is correct for resizing.
		recomputePads();

		return;
	}

	/**
	 * The full {@link ScrollClientComposite} constructor. The parent and style
	 * are passed to the {@link Composite} constructor. The ancestor is
	 * verified, and the {@link #minWidth} is set.
	 * 
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null).
	 * @param style
	 *            The style of widget to construct.
	 * @param scrolledAncestor
	 *            The ancestor scrolled {@code Composite}. Must be a non-null
	 *            ancestor (parent, grandparent, etc.), else an
	 *            {@code IllegalArgumentException} will be thrown.
	 * @param minWidth
	 *            The width at which this {@code Composite} will stop getting
	 *            smaller, thus forcing its ancestor {@code ScrolledComposite}
	 *            to use a horizontal scroll bar. If less than 0, the value 0
	 *            will be used instead.
	 */
	public ScrollClientComposite(Composite parent, int style,
			SharedScrolledComposite scrolledAncestor, int minWidth) {
		this(parent, style, scrolledAncestor);
		setMinWidth(minWidth);
	}

	/**
	 * Override the default {@code computeSize(int,int)} to redirect it to the
	 * other method regardless of the {@code Composite} implementation.
	 */
	@Override
	public Point computeSize(int wHint, int hHint) {
		return computeSize(wHint, hHint, true);
	}

	/**
	 * Ignore the provided width hint and use either the {@link #minWidth} or
	 * the available width computed by {@link #getAvailableWidth()}. This
	 * prevents the container from getting big enough to require horizontal
	 * scroll bars.
	 */
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		int width = getAvailableWidth();
		wHint = (width > minWidth ? width : minWidth);
		return super.computeSize(wHint, hHint, changed);
	}

	/**
	 * Reflows the {@link #scrolledAncestor} based on the current size of this
	 * {@code Composite}.
	 * 
	 * @param layoutsChanged
	 *            Whether or not any of the layouts have been modified. If true,
	 *            then the pads from the layouts are recomputed.
	 */
	public void refresh(boolean layoutsChanged) {
		if (layoutsChanged) {
			recomputePads();
		}
		scrolledAncestor.reflow(getSize().x > getAvailableWidth());
	}

	/**
	 * Gets the current width at which this {@code Composite} will stop getting
	 * smaller, thus forcing its ancestor {@code ScrolledComposite} to use a
	 * horizontal scroll bar.
	 * 
	 * @return The minimum width of this {@code Composite}.
	 */
	public int getMinWidth() {
		return minWidth;
	}

	/**
	 * Sets the width at which this {@code Composite} will stop getting smaller,
	 * thus forcing its ancestor {@code ScrolledComposite} to use a horizontal
	 * scroll bar.
	 * 
	 * @param minWidth
	 *            The new minimum width of this {@code Composite}. If less than
	 *            0, the value will be set to 0.
	 */
	public void setMinWidth(int minWidth) {
		this.minWidth = Math.max(0, minWidth);
	}

	/**
	 * Creates a {@code ControlListener} to listen for resize events from the
	 * {@link #scrolledAncestor}. When triggered, it causes the horizontal
	 * scroll bar to be re-adjusted.
	 */
	private void createResizeListener() {

		scrolledAncestor.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				// Recompute the child layouts. We should flush the layout cache
				// if the container is too big to properly fit inside. Without
				// the occasional call reflow(true), the default "preferred"
				// size of the container would take over and the computeSize()
				// method above will not be called.
				scrolledAncestor.reflow(getSize().x >= getAvailableWidth());
				// Note: There is a bug where reflowing does not get rid of the
				// horizontal scrollbar when this ScrollClientComposite is
				// created. If you shrink the scrolled ancestor a little and
				// then grow it, the horizontal scroll will disappear,
				// presumably due to caching sizes.
			}
		});

		return;
	}

	/**
	 * Computes the available width for this {@code Composite} based on layout
	 * padding and the client area of the {@link #scrolledAncestor}.
	 * 
	 * @return
	 */
	private int getAvailableWidth() {
		return scrolledAncestor.getClientArea().width - totalPadLeft
				- totalPadRight;
	}

	/**
	 * Recomputes the pads based on the layouts between this {@code Composite}
	 * and the {@link #scrolledAncestor}.
	 */
	private void recomputePads() {
		totalPadLeft = 0;
		totalPadRight = 0;

		PadFinder padFinder = new PadFinder();

		Composite parent = getParent();
		while (parent != null && parent != scrolledAncestor) {

			Layout layout = parent.getLayout();
			totalPadLeft += padFinder.computeLeftPad(layout);
			totalPadRight += padFinder.computeLeftPad(layout);

			parent = parent.getParent();
		}

		return;
	}
}
