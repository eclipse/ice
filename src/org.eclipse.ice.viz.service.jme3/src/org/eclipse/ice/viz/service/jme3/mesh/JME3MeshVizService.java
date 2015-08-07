package org.eclipse.ice.viz.service.jme3.mesh;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.datastructures.VizObject;
import org.eclipse.ice.viz.service.jme3.widgets.MasterApplication;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;

public class JME3MeshVizService extends AbstractVizService {


	public JME3MeshVizService() {
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getName() {
		return "JME3 Mesh Service";
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		// There are no connection properties, but still an empty map is
		// required.
		return new HashMap<String, String>();
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
		// Nothing to do
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public boolean connect() {
		// No connection to be made, so just return true
		return true;
	}


	@Override
	public IPlot createPlot(URI file) throws Exception {
		return null;
	}


	@Override
	public boolean hasConnectionProperties() {
		return false;
	}


	@Override
	public boolean disconnect() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizService#createCanvas(org.eclipse.ice.viz.service.datastructures.VizObject)
	 */
	@Override
	public IVizCanvas createCanvas(VizObject object) throws Exception {
		JME3MeshCanvas canvas = null;
		if (object instanceof VizMeshComponent){
			canvas = new JME3MeshCanvas((VizMeshComponent) object);
		}
		return canvas;
	}

	@Override
	protected Set<String> findSupportedExtensions() {
		return new HashSet<String>();
	}

}
