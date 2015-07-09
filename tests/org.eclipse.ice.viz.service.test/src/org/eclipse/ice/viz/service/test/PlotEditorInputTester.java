package org.eclipse.ice.viz.service.test;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Map;

import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.PlotEditorInput;
import org.eclipse.swt.widgets.Composite;
import org.junit.Test;

public class PlotEditorInputTester {

	/**
	 * Dummy IPlot implementation for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	class FakePlot implements IPlot {
		private URI source;

		public FakePlot(URI newSource) {
			source = newSource;
		}

		@Override
		public Map<String, String[]> getPlotTypes() throws Exception {
			return null;
		}

		@Override
		public Composite draw(String category, String plotType, Composite parent)
				throws Exception {
			return null;
		}

		@Override
		public int getNumberOfAxes() {
			return 0;
		}

		@Override
		public Map<String, String> getProperties() {
			return null;
		}

		@Override
		public void setProperties(Map<String, String> props) throws Exception {
		}

		@Override
		public URI getDataSource() {
			return null;
		}

		@Override
		public String getSourceHost() {
			return null;
		}

		@Override
		public boolean isSourceRemote() {
			return false;
		}

		@Test
		public void checkInput() {
			// Create plot
			URI testURI = null;
			try {
				testURI = new URI("test.silo");
			} catch (Exception e) {
			}
			IPlot testPlot = new FakePlot(testURI);

			// Check that the input has the correct URI
			PlotEditorInput testInput = new PlotEditorInput(testPlot);
			assertEquals(testInput.getName(), "test.silo");

		}

		@Override
		public void redraw() {
		}
	}
}
