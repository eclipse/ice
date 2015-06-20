package org.eclipse.ice.viz.service.visit.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.visit.widgets.TimeSliderComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestLauncher {
	public static void main(String[] args) {

		Display display = Display.getDefault();
		Shell shell = new Shell(display);

		shell.setLayout(new FillLayout());
		shell.setSize(500, 100);

		List<Double> times = new ArrayList<Double>();
		times.add(-1.0);
		times.add(0.0);
		for (int i = 1; i < 256; i *= 2) {
			times.add((double) i);
		}

		TimeSliderComposite timeComposite = new TimeSliderComposite(shell,
				SWT.NONE);
		timeComposite.setTimes(times);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}
