package org.eclipse.ice.viz.plotviewer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.eclipse.ice.viz.csv.viewer.PlotTreeLabelProvider;
import org.eclipse.ice.viz.service.csv.PlotProvider;
import org.eclipse.ice.viz.service.csv.PlotTimeIdentifierMapping;
import org.eclipse.ice.viz.service.csv.SeriesProvider;
import org.junit.Before;
import org.junit.Test;


public class PlotTreeLabelProviderTester {
	

	/**
	 * testing PlotProvider, PLotTimeIdntifierMapping, and SeriesProvider
	 */
	private PlotProvider parentElementPlot; 
	private PlotTimeIdentifierMapping parentElementMapping;
	private SeriesProvider parentElementSeries;


	/**
	 * PlotTreeContentProvider for testing
	 */
	private static PlotTreeLabelProvider plotTreeLabelProvider;

	/**
	 * Double arrayList for times for providers
	 */
	private ArrayList<Double> times;
	
	String plotTitle;
	

	
	@Before
	public void beforeClass() {
		/**
		 * Initialize the data structures
		 */
		parentElementPlot = new PlotProvider();
		parentElementSeries = new SeriesProvider();
		plotTreeLabelProvider = new PlotTreeLabelProvider();
		times = new ArrayList<Double>();
		plotTitle = "Test Title";

		/**
		 * Set title for Series Provider
		 */
		parentElementSeries.setSeriesTitle("Series Title");


		/**
		 * Set title for Plot provider
		 */
		parentElementPlot.setPlotTitle(plotTitle);

		/**
		 * Set the title and time for the Plot Time Identifier Mapping
		 */
		parentElementMapping = new PlotTimeIdentifierMapping("Mapping Title", 1.618);

	}
	
	

	@Test
	public void testIsLabelProperty() {
		String time = "time";
		/**
		 * check that isLabelProperty() returns false
		 */
		assertFalse(plotTreeLabelProvider.isLabelProperty(times, time));
	}

	
	@Test
	public void testGetImage() {
		/**
		 * Check null return
		 */
		assertNull(plotTreeLabelProvider.getImage(times));
	}

	@Test
	public void testGetText() {
		/**
		 * check the getText method for PlotProvider, PlotTimeIdentifierMapping, SeriesProvider, and string
		 */
		assertEquals(plotTreeLabelProvider.getText(parentElementPlot), "Test Title");
		assertEquals(plotTreeLabelProvider.getText(parentElementMapping), 1.618 + "");
		assertEquals(plotTreeLabelProvider.getText(parentElementSeries), "Series Title");
		assertEquals(plotTreeLabelProvider.getText(plotTitle),plotTitle);
	
		
		//check return null case
		assertNull(plotTreeLabelProvider.getText(times));
	}

}
	


