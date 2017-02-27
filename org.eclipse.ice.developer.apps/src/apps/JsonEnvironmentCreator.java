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
		String type = generalData.get("type").getAsString();
		if (type.equals("Docker")) {
			env = DockerFactory.eINSTANCE.createDockerEnvironment();
		} else {
			throw new UnsupportedOperationException("Cannot create anything other than Docker Environments for now...");
		}
		
		env.setName(generalData.get("name").getAsString());
		JsonElement os = generalData.get("os");
		if (os != null) {
			env.setOs(os.getAsString());
		}
		
		// Set the Primary App data if available
		SourcePackage p = AppsFactory.eINSTANCE.createSourcePackage();
		p.setName(appData.get("name").getAsString());
		p.setRepoURL(appData.get("repoURL").getAsString());
		JsonElement compiler = appData.get("compiler");
		JsonElement version = appData.get("version");
		if (version != null) {
			p.setVersion(version.getAsString());
		}
		JsonElement branch = appData.get("branch");
		if (branch != null) {
			p.setBranch(branch.getAsString());
		}
		
		// Set it as the primary application
		env.setPrimaryApp(p);

		// Add dependent spack packages
		if (deps != null) {
			for (JsonElement s : deps) {
				SpackPackage pack = AppsFactory.eINSTANCE.createSpackPackage();
				pack.setName(s.getAsJsonObject().get("name").getAsString());
				JsonElement spackcompiler = s.getAsJsonObject().get("compiler");
				if (spackcompiler != null) {
					pack.setCompiler(spackcompiler.getAsString());
				}
				JsonElement spackversion = s.getAsJsonObject().get("version");
				if (spackversion != null) {
					pack.setVersion(spackversion.getAsString());
				}
				env.getDependentPackages().add(pack);
			}
		} 
		
		if (type.equals("Docker")) {
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
