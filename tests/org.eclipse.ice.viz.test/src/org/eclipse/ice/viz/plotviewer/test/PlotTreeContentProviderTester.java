package org.eclipse.ice.viz.plotviewer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.viz.plotviewer.CSVDataProvider;
import org.eclipse.ice.viz.plotviewer.PlotProvider;
import org.eclipse.ice.viz.plotviewer.PlotTimeIdentifierMapping;
import org.eclipse.ice.viz.plotviewer.PlotTreeContentProvider;
import org.eclipse.ice.viz.plotviewer.SeriesProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is responsible for testing the PlotTreeContentProvider
 * 
 * @author xxi
 * 
 */
public class PlotTreeContentProviderTester {
	/**
	 * CSV Data provider for testing
	 */
	private CSVDataProvider dataProviderForSeries;
	
	/**
	 * testing PlotProvider, PLotTimeIdntifierMapping, and SeriesProvider
	 */
	private PlotProvider parentElementPlot; 
	private PlotTimeIdentifierMapping parentElementMapping;
	private SeriesProvider parentElementSeries;

	/**
	 * ArrayList of PlotProviders for testing
	 */
	private ArrayList<Object> plotProviderList;

	/**
	 * PlotTreeContentProvider for testing
	 */
	private static PlotTreeContentProvider plotTreeContentProvider;

	/**
	 * Double arrayList for times for providers
	 */
	private ArrayList<Double> times;
	
	/**
	 * Generic arrayList for testing getElements()
	 */
	private ArrayList<?> getElementsArray;

	@Before
	public void beforeClass() {

		/**
		 * Initialize the data structure
		 */
		parentElementPlot = new PlotProvider();
		parentElementSeries = new SeriesProvider();
		dataProviderForSeries = new CSVDataProvider();
		plotProviderList = new ArrayList<Object>();
		plotTreeContentProvider = new PlotTreeContentProvider();
		times = new ArrayList<Double>();

		/**
		 * Set values for Series Provider
		 */
		parentElementSeries.setDataProvider(dataProviderForSeries);
		parentElementSeries.setSeriesTitle("Series Title");
		parentElementSeries.setXDataFeature("X Feature");
		parentElementSeries.setYDataFeature("Y Feature");
		parentElementSeries.setSeriesType("Line");

		/**
		 * Set values for Plot provider
		 */
		parentElementPlot.setPlotTitle("Test Title");
		parentElementPlot.setXAxisTitle("X-Axis");
		parentElementPlot.setYAxisTitle("Y-Axis");
		parentElementPlot.setTimeUnits("Fake Time Units");
		
		/**
		 * Add the PlotProvider
		 */
		plotProviderList.add(parentElementPlot);

	}

	@Test
	public void testHasChildren() {
		/**
		 * Check that hasChildren() returns null
		 */
		assertFalse(plotTreeContentProvider.hasChildren(parentElementPlot));
		assertFalse(plotTreeContentProvider.hasChildren(parentElementMapping));
		assertFalse(plotTreeContentProvider.hasChildren(parentElementSeries));

	}

	@Test
	public void testGetParent() {

		/**
		 * check that getParent() returns null
		 */
		assertNull(plotTreeContentProvider.getParent(parentElementPlot));
		assertNull(plotTreeContentProvider.getParent(parentElementMapping));
		assertNull(plotTreeContentProvider.getParent(parentElementSeries));

	}

	@Test
	public void testGetElements() {
		/**
		 * Test return null case
		 */
		getElementsArray = null;
		assertNull(plotTreeContentProvider.getElements(getElementsArray));
		// Test not null return
		assertArrayEquals(plotProviderList.toArray(),
				plotTreeContentProvider.getElements(plotProviderList));
		// Test case not arrayList input
		assertNull(plotTreeContentProvider.getElements(parentElementMapping));

	}

	@Test
	public void testGetChildren() {
		/**
		 * add times to ArrayList
		 */
		times.add(1.0);
		times.add(2.0);
		times.add(3.0);
		/**
		 * Add series provider and time
		 */
		parentElementPlot.addSeries(times.get(0), parentElementSeries);
		parentElementPlot.addSeries(times.get(1), parentElementSeries);
		parentElementPlot.addSeries(times.get(2), parentElementSeries);
		
		/**
		 * Set array of strings for parentElement
		 */
		String[] testFeatures = {
				parentElementSeries.getXDataFeature() + " (Independent Variable)",
				parentElementSeries.getYDataFeature() };
		
		/**
		 * Initialize Array of PlotTimeIdentifierMapping
		 */
		PlotTimeIdentifierMapping[] plotTimesIdentifierArray = new PlotTimeIdentifierMapping[times
				.size()];
		for (int i = 0; i < times.size(); i++) {
			//set value of array
			plotTimesIdentifierArray[i] = new PlotTimeIdentifierMapping(
					parentElementPlot.getPlotTitle(), times.get(i));

		}

		for (int j = 0; j < plotTimesIdentifierArray.length; j++) {
			assertEquals(plotTimesIdentifierArray[j].getTime(),
					((PlotTimeIdentifierMapping) (plotTreeContentProvider.getChildren(parentElementPlot))[j]).getTime());
			assertEquals(plotTimesIdentifierArray[j].getPlotTitle(),
					((PlotTimeIdentifierMapping) (plotTreeContentProvider.getChildren(parentElementPlot))[j]).getPlotTitle());
			assertArrayEquals(parentElementPlot.getSeriesAtTime(plotTimesIdentifierArray[j].getTime()).toArray(),plotTreeContentProvider.getChildren(plotTimesIdentifierArray[j]));
			assertNotNull(plotTreeContentProvider.getChildren(plotTimesIdentifierArray[j]));
		}
		
		assertArrayEquals(testFeatures,plotTreeContentProvider.getChildren(parentElementSeries));
		
		//test null case
		assertNull(plotTreeContentProvider.getChildren(null));
	
		//test different input case
		assertNull(plotTreeContentProvider.getChildren(times));
		
	}
}
