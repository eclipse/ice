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
