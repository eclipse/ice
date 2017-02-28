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

import apps.AppsFactory;
import apps.IEnvironment;

import static org.junit.Assert.*;

import apps.JsonEnvironmentCreator;
import apps.OSPackage;
import apps.PackageType;
import apps.SourcePackage;
import apps.SpackPackage;
import apps.docker.DockerEnvironment;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * This class is meant to test the functionality of 
 * the JsonEnvironmentCreator.
 * 
 * @author Alex McCaskey
 *
 */
public class JsonEnvironmentCreatorTest extends TestCase {

	private static String jsonStr = "{\n" + 
			"   \"General\": {\n" + 
			"       \"name\": \"mccaskey/test_env\",\n" + 
			"       \"type\": \"Docker\"\n" + 
			"    },\n" + 
			"    \"Application\": {\n" + 
			"       \"type\": \"Source\",\n" + 
			"       \"name\": \"xacc\",\n" + 
			"       \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n" + 
			"       \"buildCommand\": \"cd xacc && mkdir build && cd build && cmake .. && make\"\n" + 
			"     },\n" + 
			"     \"Dependencies\": [\n" + 
			"         {\n" + 
			"           \"type\": \"Spack\",\n" + 
			"           \"name\": \"cmake\",\n" + 
			"           \"compiler\": \"gcc@6.3.1\"\n" + 
			"         },\n" + 
			"         {\n" + 
			"           \"type\": \"Spack\",\n" + 
			"           \"name\": \"llvm\",\n" + 
			"           \"compiler\": \"gcc@6.3.1\"\n" + 
			"         },\n" + 
			"         {\n" + 
			"           \"type\": \"OS\",\n" + 
			"           \"name\": \"gcc-gfortran\"\n" + 
			"         }\n" + 
			"      ],\n" + 
			"      \"ContainerConfig\": {\n" + 
			"         \"name\": \"xaccdev\",\n" + 
			"         \"ephemeral\": true\n" + 
			"      }\n" + 
			"}";
	
	/**
	 * The fixture for this Json Environment Creator test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JsonEnvironmentCreator fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(JsonEnvironmentCreatorTest.class);
	}


	/**
	 * Constructs a new Json Environment Creator test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JsonEnvironmentCreatorTest(String name) {
		super(name);
	}


	/**
	 * Sets the fixture for this Json Environment Creator test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(JsonEnvironmentCreator fixture) {
		this.fixture = fixture;
	}


	/**
	 * Returns the fixture for this Json Environment Creator test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JsonEnvironmentCreator getFixture() {
		return fixture;
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(AppsFactory.eINSTANCE.createJsonEnvironmentCreator());
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

	/**
	 * Tests the '{@link apps.EnvironmentCreator#create(java.lang.String) <em>Create</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentCreator#create(java.lang.String)
	 */
	public void testCreate__String() {
		
		IEnvironment env = AppsFactory.eINSTANCE.createJsonEnvironmentCreator().create(jsonStr);		
		assertEquals(env.getName(), "mccaskey/test_env");
		assertTrue(env instanceof DockerEnvironment);
		assertEquals(env.getPrimaryApp().getName(), "xacc");
		assertEquals(env.getPrimaryApp().getType(), PackageType.SOURCE);
		assertTrue(env.getPrimaryApp() instanceof SourcePackage);
		assertEquals(((SourcePackage)env.getPrimaryApp()).getRepoURL(), "https://github.com/ORNL-QCI/xacc");
		assertEquals(((SourcePackage)env.getPrimaryApp()).getBranch(), "master");
		assertEquals(((SourcePackage)env.getPrimaryApp()).getBuildCommand(), "cd xacc && mkdir build && cd build && cmake .. && make");
		
		assertEquals(env.getDependentPackages().size(), 3);
		assertTrue(env.getDependentPackages().get(0) instanceof SpackPackage);
		assertEquals(env.getDependentPackages().get(0).getName(), "cmake");
		assertEquals(((SpackPackage)env.getDependentPackages().get(0)).getCompiler(), "gcc@6.3.1");

		assertTrue(env.getDependentPackages().get(1) instanceof SpackPackage);
		assertEquals(env.getDependentPackages().get(1).getName(), "llvm");
		assertEquals(((SpackPackage)env.getDependentPackages().get(1)).getCompiler(), "gcc@6.3.1");

		assertTrue(env.getDependentPackages().get(2) instanceof OSPackage);
		assertEquals(env.getDependentPackages().get(2).getName(), "gcc-gfortran");

		DockerEnvironment denv = (DockerEnvironment) env;
		
		assertEquals(denv.getContainerConfiguration().getName(), "xaccdev");
		assertTrue(denv.getContainerConfiguration().isEphemeral());
	}
}
