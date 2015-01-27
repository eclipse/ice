package org.eclipse.ice.viz.service.paraview;

import java.net.URI;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.swt.widgets.Composite;

public class ParaViewPlot implements IPlot {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void draw(String category, String plotType, Composite parent)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#setProperties(java.util
	 * .Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		// TODO Auto-generated method stub
		return false;
	}

}
