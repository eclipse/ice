package org.eclipse.ice.viz.service.paraview;

import java.util.Random;

import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

public class ParaViewPlotRender extends PlotRender {

	public ParaViewPlotRender(Composite parent, ParaViewPlot plot) {
		super(parent, plot);

	}

	@Override
	protected Composite createPlotComposite(Composite parent, int style)
			throws Exception {
		// TODO Hook this up to the ParaView widgets.
		return new Composite(parent, style);
	}

	@Override
	protected void updatePlotComposite(Composite plotComposite)
			throws Exception {
		// TODO Hook this up to the ParaView widgets.
		int seed = (getPlotCategory() + getPlotType()).hashCode();
		Random r = new Random(seed);
		plotComposite.setBackground(new Color(plotComposite.getDisplay(), r
				.nextInt(255), r.nextInt(255), r.nextInt(255)));
	}

	@Override
	protected void disposePlotComposite(Composite plotComposite) {
		// Nothing to do yet.
	}

}
