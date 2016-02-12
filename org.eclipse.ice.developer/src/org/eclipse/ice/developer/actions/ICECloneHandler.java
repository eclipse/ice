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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.internal.util.ProjectUtil;
import org.eclipse.egit.core.op.CloneOperation.PostCloneTask;
import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.internal.UIText;
import org.eclipse.egit.ui.internal.clone.ProjectRecord;
import org.eclipse.egit.ui.internal.clone.ProjectUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.ui.IWorkingSet;

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
	protected void addPostCloneTasks() {

		// Import all ICE projects
		cloneOperation.addPostCloneTask(new PostCloneTask() {
			@Override
			public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
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
				importProjects(repository, new IWorkingSet[0]);
			}

		});
	}

	/**
	 * This private method is used to import existing projects into the project
	 * explorer.
	 * 
	 * @param repository
	 * @param sets
	 */
	private void importProjects(final Repository repository, final IWorkingSet[] sets) {
		String repoName = Activator.getDefault().getRepositoryUtil().getRepositoryName(repository);
		Job importJob = new WorkspaceJob(MessageFormat.format(UIText.GitCloneWizard_jobImportProjects, repoName)) {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				List<File> files = new ArrayList<File>();
				ProjectUtil.findProjectFiles(files, repository.getWorkTree(), true, monitor);
				if (files.isEmpty()) {
					return Status.OK_STATUS;
				}

				Set<ProjectRecord> projectRecords = new LinkedHashSet<ProjectRecord>();
				for (File file : files) {
					projectRecords.add(new ProjectRecord(file));
				}
				try {
					ProjectUtils.createProjects(projectRecords, sets, monitor);
				} catch (InvocationTargetException | InterruptedException e) {
					Activator.logError(e.getLocalizedMessage(), e);
				}

				return Status.OK_STATUS;
			}
		};
		importJob.schedule();
	}
	
	/**
	 * Reference to the XML file contents we need for life cycle management M2e file. 
	 */
	private String lifecycleXML = 
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
