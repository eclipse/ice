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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Layout;

/**
 * This class is used to compute the left, right, top, and bottom padding (in
 * pixels) for layouts. Currently, the only supported layouts are
 * {@link FillLayout} and {@link GridLayout}.
 * 
 * @author Jordan Deyton
 *
 */
public class PadFinder {

	/**
	 * A static map of {@code PadFinder}s for supported layouts. If null, it
	 * will need to be {@link #initializeStaticMap() initialized}.
	 */
	private static Map<Class<? extends Layout>, PadFinder> padFinders;

	/**
	 * The current layout. This value may be null.
	 */
	private Layout currentLayout;
	/**
	 * The current {@code PadFinder} from the {@link #padFinders map}. This
	 * value may be null.
	 */
	private PadFinder currentFinder;

	/**
	 * The default constructor.
	 */
	public PadFinder() {
		// If necessary, initialize the static map.
		if (padFinders == null) {
			initializeStaticMap();
		}
	}

	/**
	 * Computes the padding on the left side of the specified layout.
	 * 
	 * @param layout
	 *            The layout whose left padding should be computed.
	 * @return The total padding on the left side of the layout, or 0 if the
	 *         padding is empty or the layout is not supported.
	 */
	public int computeLeftPad(Layout layout) {
		int leftPad = 0;
		if (refreshLayout(layout) != null) {
			leftPad = currentFinder.computeLeftPad(layout);
		}
		return leftPad;
	}

	/**
	 * Computes the padding on the right side of the specified layout.
	 * 
	 * @param layout
	 *            The layout whose right padding should be computed.
	 * @return The total padding on the right side of the layout, or 0 if the
	 *         padding is empty or the layout is not supported.
	 */
	public int computeRightPad(Layout layout) {
		int rightPad = 0;
		if (refreshLayout(layout) != null) {
			rightPad = currentFinder.computeRightPad(layout);
		}
		return rightPad;
	}

	/**
	 * Computes the padding on the top side of the specified layout.
	 * 
	 * @param layout
	 *            The layout whose top padding should be computed.
	 * @return The total padding on the top side of the layout, or 0 if the
	 *         padding is empty or the layout is not supported.
	 */
	public int computeTopPad(Layout layout) {
		int topPad = 0;
		if (refreshLayout(layout) != null) {
			topPad = currentFinder.computeTopPad(layout);
		}
		return topPad;
	}

	/**
	 * Computes the padding on the bottom side of the specified layout.
	 * 
	 * @param layout
	 *            The layout whose bottom padding should be computed.
	 * @return The total padding on the bottom side of the layout, or 0 if the
	 *         padding is empty or the layout is not supported.
	 */
	public int computeBottomPad(Layout layout) {
		int bottomPad = 0;
		if (refreshLayout(layout) != null) {
			bottomPad = currentFinder.computeBottomPad(layout);
		}
		return bottomPad;
	}

	/**
	 * Updates the {@link #currentLayout} and {@link #currentFinder} if
	 * necessary.
	 * 
	 * @param layout
	 *            The new layout.
	 * @return The {@link #currentFinder}. This value may be null!
	 */
	private PadFinder refreshLayout(Layout layout) {
		if (layout != currentLayout) {
			currentLayout = layout;
			currentFinder = padFinders.get(currentLayout);
		}
		return currentFinder;
	}

	/**
	 * Initializes the {@link #padFinders} map with the supported layouts.
	 */
	private void initializeStaticMap() {
		padFinders = new HashMap<Class<? extends Layout>, PadFinder>();

		// Add a PadFinder for FillLayouts.
		padFinders.put(FillLayout.class, new PadFinder() {
			@Override
			public int computeLeftPad(Layout layout) {
				FillLayout fillLayout = (FillLayout) layout;
				return fillLayout.marginWidth;
			}

			@Override
			public int computeRightPad(Layout layout) {
				FillLayout fillLayout = (FillLayout) layout;
				return fillLayout.marginWidth;
			}

			@Override
			public int computeTopPad(Layout layout) {
				FillLayout fillLayout = (FillLayout) layout;
				return fillLayout.marginHeight;
			}

			@Override
			public int computeBottomPad(Layout layout) {
				FillLayout fillLayout = (FillLayout) layout;
				return fillLayout.marginHeight;
			}
		});

		// Add a PadFinder for GridLayouts.
		padFinders.put(GridLayout.class, new PadFinder() {
			@Override
			public int computeLeftPad(Layout layout) {
				GridLayout gridLayout = (GridLayout) layout;
				return gridLayout.marginWidth + gridLayout.marginLeft;
			}

			@Override
			public int computeRightPad(Layout layout) {
				GridLayout gridLayout = (GridLayout) layout;
				return gridLayout.marginWidth + gridLayout.marginRight;
			}

			@Override
			public int computeTopPad(Layout layout) {
				GridLayout fillLayout = (GridLayout) layout;
				return fillLayout.marginHeight + fillLayout.marginTop;
			}

			@Override
			public int computeBottomPad(Layout layout) {
				GridLayout fillLayout = (GridLayout) layout;
				return fillLayout.marginHeight + fillLayout.marginBottom;
			}
		});

		return;
	}
}
