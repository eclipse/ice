package org.eclipse.ice.viz.service.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
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
		private String title;
		private Map<String, List<ISeries>> depSeries;
		private ISeries indepSeries;

		public FakePlot(URI newSource) {
			source = newSource;
			depSeries = new HashMap<String, List<ISeries>>();
			indepSeries = null;
		}

		@Override
		public Composite draw(Composite parent) throws Exception {
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
			// Nothing TODO
			return;
		}

		@Override
		public void setPlotTitle(String title) {
			this.title = title;
		}

		@Override
		public String getPlotTitle() {
			return title;
		}

		@Override
		public void setIndependentSeries(ISeries series) {
			this.indepSeries = series;

		}

		@Override
		public ISeries getIndependentSeries() {
			return indepSeries;
		}

		public void removeDependantSeries(ISeries series) {
			depSeries.remove(series);
			// If this series is in the list
			if (depSeries.containsValue(series)) {
				// Iterate to find the right key
				for (String key : depSeries.keySet()) {
					// Remove the series for the first category it is in in the
					// map
					if (depSeries.get(key).contains(series)) {
						depSeries.get(key).remove(series);
						break;
					}
				}
			}
		}

		public void addDependentSeries(String category, ISeries seriesToAdd) {
			if (depSeries.get(category) == null) {
				depSeries.put(category, new ArrayList<ISeries>());
			}
			depSeries.get(category).add(seriesToAdd);
		}

		@Override
		public List<ISeries> getAllDependentSeries(String category) {
			return depSeries.get(category);
		}

		@Override
		public String[] getCategories() {
			return depSeries.keySet()
					.toArray(new String[depSeries.keySet().size()]);
		}
	}
}
