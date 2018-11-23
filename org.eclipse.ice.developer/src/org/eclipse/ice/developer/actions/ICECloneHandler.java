/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alex McCaskey - Initial API and implementation and/or initial documentation
 *
 *******************************************************************************/
package org.eclipse.ice.developer.actions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.egit.core.op.CloneOperation.PostCloneTask;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.m2e.core.MavenPlugin;

/**
 * The ICECloneHandler clones the ICE repository and adds PostCloneTasks that
 * clone the VisIt Java Client and the ICE Dependencies plugin. It also imports
 * all resulting Eclipse projects.
 * 
 * @author Alex McCaskey
 *
 */
@SuppressWarnings("restriction")
public class ICECloneHandler extends GitCloneHandler {

	/**
	 * This implementation of addPostCloneTask adds an action for importing all
	 * cloned projects.
	 */
	@Override
	protected void addPostCloneTasks() {

		// Import all ICE projects
		cloneOperation.addPostCloneTask(new PostCloneTask() {
			@Override
			public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
				fixMavenLifecycleFile();
			}
		});
		
		// Import all projects
		super.addPostCloneTasks();
	}

	/**
	 * 
	 */
	protected void fixMavenLifecycleFile() {
		// This is a fix for the errors that occur with the new ICE Build for 
		// certain maven goals.
		String file = MavenPlugin.getMavenConfiguration().getWorkspaceLifecycleMappingMetadataFile();
		try {
			Path path = Paths.get(file);
			if (Files.exists(path)) {
				Files.write(Paths.get(file), lifecycleXML.getBytes());
			} else {
				Files.write(Paths.get(file), lifecycleXML.getBytes(), StandardOpenOption.CREATE_NEW);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reference to the XML file contents we need for life cycle management M2e file. 
	 */
	protected String lifecycleXML = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<lifecycleMappingMetadata>\n"
			+ "	  <pluginExecutions>\n"
			+ "	    <pluginExecution>\n"
			+ "	      <pluginExecutionFilter>\n"
			+ "	        <groupId>org.eclipse.tycho</groupId>\n"
			+ "	        <artifactId>tycho-compiler-plugin</artifactId>\n"
			+ "	        <versionRange>0.23.0</versionRange>\n"
			+ "	        <goals>\n"
			+ "	          <goal>compile</goal>\n"
			+ "	        </goals>\n"
			+ "	      </pluginExecutionFilter>\n"
			+ "	      <action>\n"
			+ "	        <ignore />\n"
			+ "	      </action>\n"
			+ "	    </pluginExecution>\n"
			+ "	    <pluginExecution>\n"
			+ "	      <pluginExecutionFilter>\n"
			+ "	        <groupId>org.eclipse.tycho</groupId>\n"
			+ "	        <artifactId>tycho-packaging-plugin</artifactId>\n"
			+ "	        <versionRange>0.23.0</versionRange>\n"
			+ "	        <goals>\n"
			+ "	          <goal>build-qualifier-aggregator</goal>\n"
			+ "	          <goal>validate-version</goal>\n"
			+ "	          <goal>validate-id</goal>\n"
			+ "	          <goal>build-qualifier</goal>\n"
			+ "	        </goals>\n"
			+ "	      </pluginExecutionFilter>\n"
			+ "	      <action>\n"
			+ "	        <ignore />\n"
			+ "	      </action>\n"
			+ "	    </pluginExecution>\n"
			+ "	    <pluginExecution>\n"
			+ "	      <pluginExecutionFilter>\n"
			+ "	        <groupId>org.apache.maven.plugins</groupId>\n"
			+ "	        <artifactId>maven-clean-plugin</artifactId>\n"
			+ "	        <versionRange>2.5</versionRange>\n"
			+ "	        <goals>\n"
			+ "	          <goal>clean</goal>\n"
			+ "	        </goals>\n"
			+ "	      </pluginExecutionFilter>\n"
			+ "	      <action>\n"
			+ "	        <ignore />\n"
			+ "	      </action>\n"
			+ "	    </pluginExecution>\n"
			+ "	  </pluginExecutions>\n"
			+ "	</lifecycleMappingMetadata>";
}
