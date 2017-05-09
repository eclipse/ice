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
package apps.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import apps.AppsFactory;
import apps.EnvironmentManager;
import apps.IEnvironment;

/**
 * This class is responsible for taking user command 
 * line arguments and creating a new, or loading an existing, 
 * scientific application environment. 
 * 
 * @author Alex McCaskey
 *
 */
public class EnvironmentCommandLineParser {

	/**
	 * Reference to the command line options 
	 * input by the user. 
	 */
	private CommandLine cmd;
	
	private EnvironmentManager manager;
	
	/**
	 * The constructor, creates a CommandLineParser 
	 * object and gets the users command line input
	 * 
	 * @param args Command line arguments
	 */
	@SuppressWarnings("static-access")
	public EnvironmentCommandLineParser(String[] args) {
		// create Options object
		Options options = new Options();

		Option newJsonFile = OptionBuilder.withArgName("FILE")
				.withDescription("The JSON file describing the environment").hasArg().create("create");
		Option loadXMIFile = OptionBuilder.withArgName("FILE")
				.withDescription("The XMI file describing the environment").hasArg().create("load");

		options.addOption(newJsonFile);
		options.addOption(loadXMIFile);
		options.addOption( "c", "connect", false, "Connect to the created environment." );
		
		CommandLineParser parser = new BasicParser();

		try {
			cmd = parser.parse( options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (!cmd.hasOption("create") && !cmd.hasOption("load")) {
			throw new IllegalArgumentException("You must specify to create or load a file.");
		}
		
		manager = AppsFactory.eINSTANCE.createEnvironmentManager();

	}

	/**
	 * Return the constructed Environment given 
	 * the command line arguments provided at 
	 * EnvironmentCommandLineParser construction.
	 * 
	 * @return environment
	 */
	public void execute() {

		// Initialize the environment we will return 
		// to null
		IEnvironment env = null;
		
		// If the user said load, then 
		// let's load the file they provided
		if (cmd.hasOption("load")) {
			// Get the name of the XMI file
			String fileName = cmd.getOptionValue("load");
			// Return the Environment
			env = manager.loadFromFile(fileName);
		} else {
			// This is a json file if we are here.
			String fileName = cmd.getOptionValue("create");
			String fileString = "";
			try {
				fileString = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// Map the json to an Environment
			env = manager.create(fileString);
		}
		
		System.out.println(manager.persistToString(env.getName()));

		if (!env.build()) {
			System.out.println("Error building the environment");
			throw new IllegalArgumentException("Invalid file");
		}
		
		if (cmd.hasOption("connect")) {
			if (!env.connect()) {
				throw new RuntimeException("Could not connect to environment");
			}
			
		}
		return;
	}
	
}
