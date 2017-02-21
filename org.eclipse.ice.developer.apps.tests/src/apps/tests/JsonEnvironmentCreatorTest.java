/*******************************************************************************
 * Copyright (c) 2017 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alex McCaskey - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package apps.tests;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.BeforeClass;
import org.junit.Test;

import apps.EnvironmentCommandLineParser;
import apps.EnvironmentType;
import apps.IEnvironment;
import apps.JsonEnvironmentCreator;
import apps.SpackPackage;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerEnvironment;

/**
 * This class is meant to test the functionality of 
 * the JsonEnvironmentCreator.
 * 
 * @author Alex McCaskey
 *
 */
public class JsonEnvironmentCreatorTest {

	private static String jsonStr = "{\n" + 
			"   \"General\": {\n" + 
			"       \"name\": \"mccaskey/test_env\",\n" + 
			"       \"type\": \"Docker\"\n" + 
			"    },\n" + 
			"    \"Application\": {\n" + 
			"       \"name\": \"xacc\",\n" + 
			"       \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n" + 
			"       \"compiler\": \"gcc@6.1.0\"\n" + 
			"     },\n" + 
			"     \"Dependencies\": [\n" + 
			"         {\n" + 
			"           \"name\": \"cmake\",\n" + 
			"           \"compiler\": \"gcc@6.1.0\"\n" + 
			"         },\n" + 
			"         {\n" + 
			"           \"name\": \"llvm\",\n" + 
			"           \"compiler\": \"gcc@6.1.0\"\n" + 
			"         }\n" + 
			"      ],\n" + 
			"      \"ContainerConfig\": {\n" + 
			"         \"name\": \"xaccdev\",\n" + 
			"         \"ephemeral\": true\n" + 
			"      }\n" + 
			"}";
	
	
	@Test
	public void checkCreateNewFromJson() {

		IEnvironment env = JsonEnvironmentCreator.create(jsonStr);
		
		assertEquals(env.getName(), "mccaskey/test_env");
		assertEquals(env.getType(), EnvironmentType.DOCKER);
		assertEquals(env.getOs(), "fedora");
		assertTrue(env.isDevelopmentEnvironment());
		assertFalse(env.isGenerateProject());
		
		SpackPackage app = env.getPrimaryApp();
		assertEquals(app.getName(), "xacc");
		assertEquals(app.getBranch(), "master");
		assertEquals(app.getCompiler(), "gcc@6.1.0");
		assertEquals(app.getRepoURL(), "https://github.com/ORNL-QCI/xacc");
		assertEquals(app.getVersion(), "latest");
		
		SpackPackage cmake = env.getDependentPackages().get(0);
		assertEquals(cmake.getName(), "cmake");
		assertEquals(cmake.getBranch(), "master");
		assertEquals(cmake.getCompiler(), "gcc@6.1.0");
		assertEquals(cmake.getRepoURL(), null);
		assertEquals(cmake.getVersion(), "latest");
		
		SpackPackage llvm = env.getDependentPackages().get(1);
		assertEquals(llvm.getName(), "llvm");
		assertEquals(llvm.getBranch(), "master");
		assertEquals(llvm.getCompiler(), "gcc@6.1.0");
		assertEquals(llvm.getRepoURL(), null);
		assertEquals(llvm.getVersion(), "latest");
		
		DockerEnvironment dockerEnv = (DockerEnvironment) env;
		ContainerConfiguration config = dockerEnv.getContainerConfiguration();
		assertEquals(config.getName(), "xaccdev");
		assertTrue(config.isEphemeral());
	}
}
