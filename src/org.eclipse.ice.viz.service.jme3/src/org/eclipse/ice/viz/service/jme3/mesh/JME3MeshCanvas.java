package org.eclipse.ice.viz.service.jme3.mesh;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.jme3.widgets.EmbeddedView;
import org.eclipse.ice.viz.service.jme3.widgets.MasterApplication;
import org.eclipse.ice.viz.service.jme3.widgets.ViewAppState;
import org.eclipse.ice.viz.service.jme3.widgets.internal.MasterApplicationHolder;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JME3MeshCanvas implements IVizCanvas {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(JME3MeshCanvas.class);
	
	private MasterApplication masterApp;

	private VizMeshComponent mesh;
	
	/**
	 * The Constructor
	 *
	 * @param source
	 *            The URI of the CSV file.
	 */
	public JME3MeshCanvas(VizMeshComponent newMesh) {
		mesh = newMesh;

		
	}


	@Override
	public int getNumberOfAxes() {
		// The Mesh is always 2D
		return 2;
	}

	/**
	 * @see org.eclipse.ice.viz.service.IPlot#draw(java.lang.String,
	 *      java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		masterApp = MasterApplicationHolder.getApplication();
		MeshAppState appState = new MeshAppState();
		appState.start(masterApp);
		EmbeddedView view = masterApp.getEmbeddedView();
		appState.updateMesh(mesh);
		Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND) ;
		Frame frame = SWT_AWT.new_Frame(composite);
		view.addToEmbeddedFrame(frame);
		return composite;

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#redraw()
	 */
	@Override
	public void redraw() {
		masterApp.simpleUpdate(0f);
	}


	@Override
	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public URI getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getSourceHost() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isSourceRemote() {
		// TODO Auto-generated method stub
		return false;
	}

}
