package org.eclipse.ice.client.widgets.moose.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class MooseNature implements IProjectNature {

	public static final String NATURE_ID = "org.eclipse.ice.client.widgets.moose.nature.moosenature";
	
	private IProject project;
	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	@Override
	public void setProject(IProject proj) {
		// TODO Auto-generated method stub
		project = proj;
	}

}
