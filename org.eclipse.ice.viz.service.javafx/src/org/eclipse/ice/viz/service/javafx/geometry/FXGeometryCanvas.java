/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.geometry;

import org.eclipse.ice.viz.service.geometry.widgets.TransformationView;
import org.eclipse.ice.viz.service.javafx.canvas.AbstractAttachment;
import org.eclipse.ice.viz.service.javafx.canvas.AbstractViewer;
import org.eclipse.ice.viz.service.javafx.canvas.FXSelection;
import org.eclipse.ice.viz.service.javafx.canvas.FXVizCanvas;
import org.eclipse.ice.viz.service.modeling.ShapeController;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * An extension fo FXVizCanvas that implements functionality for the Geometry
 * Editor.
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXGeometryCanvas extends FXVizCanvas {

	/**
	 * The default constructor
	 * 
	 * @param source
	 *            The shape under which all parts in the model will be contained
	 *            as children
	 */
	public FXGeometryCanvas(ShapeController source) {
		super(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.javafx.internal.FXVizCanvas#createAttachment(
	 * )
	 */
	@Override
	protected void createAttachment() {
		rootAtachment = (AbstractAttachment) viewer.getRenderer()
				.createAttachment(FXGeometryAttachment.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.javafx.internal.FXVizCanvas#
	 * handleSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	protected void handleSelectionChanged(SelectionChangedEvent event) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(TransformationView.ID);

		if (view == null || !(view instanceof TransformationView)) {
			return;
		}

		TransformationView transformView = (TransformationView) view;

		FXSelection selection = (FXSelection) event.getSelection();

		transformView.setShape((ShapeController) selection.getShape());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.javafx.internal.FXVizCanvas#materializeViewer
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected AbstractViewer materializeViewer(Composite viewerParent)
			throws Exception {

		// Create a geometry viewer, throwing an exception if the operation
		// fails
		try {
			return new FXGeometryViewer(viewerParent);

		} catch (Exception e) {
			throw new Exception("", e); //$NON-NLS-1$
		}
	}
}
