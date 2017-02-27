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

import org.junit.Test;

import apps.IEnvironment;
import apps.JsonEnvironmentCreator;
import apps.SourcePackage;
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
		assertTrue(env instanceof DockerEnvironment);
		assertEquals(env.getOs(), "fedora");
		
		apps.SourcePackage app = (SourcePackage) env.getPrimaryApp();
		assertEquals(app.getName(), "xacc");
		assertEquals(app.getBranch(), "master");
		assertEquals(app.getRepoURL(), "https://github.com/ORNL-QCI/xacc");
		assertEquals(app.getVersion(), "latest");
		
		SpackPackage cmake = (SpackPackage) env.getDependentPackages().get(0);
		assertEquals(cmake.getName(), "cmake");
		assertEquals(cmake.getCompiler(), "gcc@6.1.0");
		assertEquals(cmake.getVersion(), "latest");
		
		SpackPackage llvm = (SpackPackage) env.getDependentPackages().get(1);
		assertEquals(llvm.getName(), "llvm");
		assertEquals(llvm.getCompiler(), "gcc@6.1.0");
		assertEquals(llvm.getVersion(), "latest");
		
		DockerEnvironment dockerEnv = (DockerEnvironment) env;
		ContainerConfiguration config = dockerEnv.getContainerConfiguration();
		assertEquals(config.getName(), "xaccdev");
		assertTrue(config.isEphemeral());
	}
}
