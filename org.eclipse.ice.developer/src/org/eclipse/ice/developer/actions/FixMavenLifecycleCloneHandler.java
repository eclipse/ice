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
 * This Handler subclasses GitCloneHandler to add a Post Clone Task that 
 * fixes the Maven lifecycle mapping bug where all projects fail to compile. 
 * 
 * @author Alex McCaskey
 *
 */
@SuppressWarnings("restriction")
public class FixMavenLifecycleCloneHandler extends GitCloneHandler {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.developer.actions.GitCloneHandler#addPostCloneTasks()
	 */
	@Override
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
			}

		});
		
		// Import all projects
		super.addPostCloneTasks();
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
