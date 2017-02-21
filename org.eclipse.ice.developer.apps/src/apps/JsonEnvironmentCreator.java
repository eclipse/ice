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
package apps;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import apps.docker.ContainerConfiguration;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;

/**
 * This class provides a static method that takes a 
 * json file as input and outputs a new Environment 
 * based on that input. 
 * 
 * @author Alex McCaskey
 *
 */
public class JsonEnvironmentCreator {

	/**
	 * Return the Environment described by the given 
	 * java Reader
	 * @param reader
	 * @return
	 */
	public static IEnvironment create(Reader reader) {
		IEnvironment env = null;
		JsonParser parser = new JsonParser();
		JsonObject root = parser.parse(reader).getAsJsonObject();	
		JsonObject generalData = root.getAsJsonObject("General");
		JsonObject appData = root.getAsJsonObject("Application");
		JsonArray deps = root.getAsJsonArray("Dependencies");
		
		// Create the Environment
		env = AppsFactory.eINSTANCE.createEnvironmentManager()
				.createEnvironment(generalData.get("type").getAsString());
		
		env.setName(generalData.get("name").getAsString());
		JsonElement os = generalData.get("os");
		if (os != null) {
			env.setOs(os.getAsString());
		}
		JsonElement genProject = generalData.get("generateProject");
		if (genProject != null) {
			env.setGenerateProject(genProject.getAsBoolean());
		}
		
		// Set the Primary App data if available
		if (appData == null) {
			env.setDevelopmentEnvironment(false);
		} else {
			SpackPackage p = AppsFactory.eINSTANCE.createSpackPackage();
			p.setName(appData.get("name").getAsString());
			p.setRepoURL(appData.get("repoURL").getAsString());
			JsonElement compiler = appData.get("compiler");
			if (compiler != null) {
				p.setCompiler(compiler.getAsString());
			}
			JsonElement version = appData.get("version");
			if (version != null) {
				p.setVersion(version.getAsString());
			}
			JsonElement branch = appData.get("branch");
			if (branch != null) {
				p.setBranch(branch.getAsString());
			}
			env.setPrimaryApp(p);
		}

		// Add dependent spack packages
		if (deps != null) {
			for (JsonElement s : deps) {
				SpackPackage p = AppsFactory.eINSTANCE.createSpackPackage();
				p.setName(s.getAsJsonObject().get("name").getAsString());
				JsonElement compiler = s.getAsJsonObject().get("compiler");
				if (compiler != null) {
					p.setCompiler(compiler.getAsString());
				}
				JsonElement version = s.getAsJsonObject().get("version");
				if (version != null) {
					p.setVersion(version.getAsString());
				}
				JsonElement branch = s.getAsJsonObject().get("branch");
				if (branch != null) {
					p.setBranch(branch.getAsString());
				}
				JsonElement repoURL = s.getAsJsonObject().get("repoURL");
				if (repoURL != null) {
					p.setRepoURL(repoURL.getAsString());
				}
				env.getDependentPackages().add(p);
			}
		} 
		
		if (generalData.get("type").getAsString().equals("Docker")) {
			ContainerConfiguration config = DockerFactory.eINSTANCE.createContainerConfiguration();
			JsonObject containerConfig = root.getAsJsonObject("ContainerConfig");
			if (containerConfig.get("name") != null) {
				config.setName(containerConfig.get("name").getAsString()); 
			}
			if (containerConfig.get("ephemeral") != null ) {
				config.setEphemeral(containerConfig.get("ephemeral").getAsBoolean());
			}
			// FIXME ADD MORE LATER
			
			((DockerEnvironment)env).setContainerConfiguration(config);
		}
		
		return env;
	}
	
	/**
	 * Return the Environment described by the given 
	 * json input file. 
	 * 
	 * @param jsonFile
	 * @return
	 */
	public static IEnvironment create(String jsonString) {
		BufferedReader br = null;
		InputStream is = new ByteArrayInputStream(jsonString.getBytes());
		br = new BufferedReader(new InputStreamReader(is));
		return create(br);
	}
	
	
}
