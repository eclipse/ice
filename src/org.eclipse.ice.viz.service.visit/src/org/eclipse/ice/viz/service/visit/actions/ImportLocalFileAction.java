/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit.actions;

import java.io.File;

import org.eclipse.ice.viz.service.visit.VisItPlot;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class ImportLocalFileAction extends Action {

	private final VisItPlot plot;

	public ImportLocalFileAction(VisItPlot plot) {
		setText("Browse...");
		setToolTipText("Select a local file to plot with VisIt.");

		this.plot = plot;
	}

	@Override
	public void run() {
		FileDialog dialog = new FileDialog(Display.getDefault()
				.getActiveShell(), SWT.OPEN);

		String filePath = dialog.open();
		if (filePath != null) {
			File file = new File(filePath);
			plot.setDataSource(file.toURI());
		}

		return;
	}
}
