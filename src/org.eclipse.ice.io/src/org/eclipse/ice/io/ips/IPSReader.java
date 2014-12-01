/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.io.ips;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.updateableComposite.Component;

/**
 * IPSReader class is responsible for reading the contents of an IPS INI (.conf)
 * file and converting it into appropriate data structures for ICE to use.
 * 
 * @author bzq
 *
 */
public class IPSReader {
	
	/**
	 * Keeps track of the current ID for entries.  Since we don't know
	 * how many entries we will need for each section we can just keep
	 * a global count of them.
	 */
	private int currID = 0;
	
	/**
	 * Nullary constructor
	 */
	public IPSReader() {
		super();
		return;
	}

	
	/**
	 * Reads in an INI file, and returns an ArrayList of Components representing
	 * the contents of the file.  Each section in an INI file is returned as one
	 * Component.  If the input file is invalid the method returns a null ArrayList.
	 * 
	 * @param iniFile
	 *           The file to read the data from.
	 * @return An ArrayList of the DataComponents that contain the data from 
	 *           each of the sections of the INI file.
	 * @throws FileNotFoundException
	 *           Thrown when the input file cannot be found
	 * @throws IOException
	 *           Thrown when if the readFileLines(...) method fails to read
	 *           in the file.
	 */
	public ArrayList<Component> loadINIFile(File iniFile)
			throws FileNotFoundException, IOException {
		
		// Make sure the file is valid, otherwise just stop here
		if (iniFile == null || !iniFile.isFile()) {
			System.out.println("COULD NOT LOAD " + iniFile);
			return null;
		}
		
		// Create a space for the data from the INI file
		ArrayList<Component> components = new ArrayList<Component>();
		
		// Read in the ini file and create the iterator
        ArrayList<String> lines = readFileLines(iniFile);
        Iterator<String> iniIterator = lines.iterator();

        // Read in the global configuration and ports data
        DataComponent globalConfiguration = loadGlobalConfiguration(iniIterator);
        DataComponent portsData = loadPortsData(iniIterator);
       
        // Determine the number of components that were specified in
        // the ports table and then read them in
        int numberPorts = portsData.retrieveAllEntries().size();
        ArrayList<DataComponent> ipsComponents = new ArrayList<DataComponent>();
        for (int i=0; i<numberPorts; i++) {
                DataComponent ipsComponent = loadComponent(iniIterator);
                ipsComponents.add(ipsComponent);
        }

        // Read in the time loop specification
        DataComponent timeLoopComponent = loadTimeLoopComponent(iniIterator);

        // Put all of the components together in the order they were read in
        components.add(globalConfiguration);
        components.add(portsData);
        for (DataComponent ipsComponent : ipsComponents) {
            components.add(ipsComponent);
        }
        components.add(timeLoopComponent);
	
		// Return the components
		return components;
	}

	
	/**
	 * Load the Time Loop Data from the INI file and return the data as
	 * a DataComponent.  Each parameter in the section is added to the 
	 * DataComponent as an Entry.
	 * 
	 * @param it
	 *           Iterator over the lines of the INI file as an ArrayList
	 * @return a DataComponent of Entries containing the information from
	 *           the Time Loop Component of the INI file.
	 */
	private DataComponent loadTimeLoopComponent(Iterator<String> it) {
		// Create the port component and a generic entry
		DataComponent timeLoopData = new DataComponent();
		Entry entry;
		String[] splitLine = null;
		String line = "";
		
		// Scan until we get to the next port component
		if ( it.hasNext() ) {
			line = it.next();
		} else {
			// Do some error stuff here
		}
		
		while (!line.contains("[TIME_LOOP]") && it.hasNext()) {
			line = it.next();
		}
		
		// Pull the port name and start parsing through the parameters
		timeLoopData.setName("TIME_LOOP");
		timeLoopData.setDescription("A port in an IPS file.");
		timeLoopData.setId(currID);
		currID++;
		
		// Read parameters until reaching the end of the file
		while(it.hasNext()) {
			// The format in this section is: KEY = VALUE # Comment
			// First check if the line contains a parameter
			if (line.contains("=")) {
				
				// If the line has a comment split on it and disregard it
				if (line.contains("#")) {
					line = line.split("#", 2)[0];
				}

				// Get the content for the entry
				splitLine = line.split("=", 2);
				
				// Set up the entry
				entry = makeIPSEntry();
				entry.setName(splitLine[0]);
				entry.setValue(splitLine[1]);
				entry.setId(currID); 
				currID++;
				timeLoopData.addEntry(entry);
			}
			
			// Read in another line
			line = it.next();
		}
		
		// Return the parameters
		return timeLoopData;
	}


	/**
	 * Load the data for a component that was listed in the ports table 
	 * and return the information as a DataComponent.  Each parameter in 
	 * the component is added to the DataComponent as an entry.
	 * 
	 * @param it
	 *           Iterator over the lines of the INI file as an ArrayList
	 * @return A DataComponent containing information for one of the
	 *           port entries.
	 */
	private DataComponent loadComponent(Iterator<String> it) {
		// Create the port component and a generic entry
		DataComponent portComponent = new DataComponent();
		Entry entry;
		String[] splitLine = null;

		// Scan until we get to the next port component
		String line = it.next();
		while (!line.contains("[") && !line.contains("]") && it.hasNext()) {
			line = it.next();
		}
		
		// Pull the port name and start parsing through the parameters
		String portName = line.replaceAll("[^a-zA-Z0-9_]","");
		portComponent.setName(portName);
		portComponent.setDescription("A port in an IPS file.");
		portComponent.setId(currID);
		currID++;
		
		// Read parameters until reaching a whitespace line that separates ports
		while(line.trim().length() > 0) {
			// The format in this section is: KEY = VALUE # Comment
			// First check if the line contains a parameter
			if (line.contains("=")) {
				
				// If the line has a comment split on it and disregard it
				if (line.contains("#")) {
					line = line.split("#", 2)[0];
				}

				// Get the content for the entry
				splitLine = line.split("=", 2);
				
				// Set up the entry
				entry = makeIPSEntry();
				entry.setName(splitLine[0]);
				entry.setValue(splitLine[1]);
				entry.setId(currID); 
				currID++;
				portComponent.addEntry(entry);
			}
			
			// Read in another line
			line = it.next();
		} 
		
		// Return the parameters
		return portComponent;
	}


	/**
	 * Loads the Ports table from the INI file and returns the information
	 * as a DataComponent of Entry.  Each of the ports specified the table 
	 * are set as an entry.  The number of ports in the table specify the 
	 * number of Components that need to be loaded by the loadComponent() 
	 * method.
	 * 
	 * @param it 
	 *           Iterator over the lines of the INI file as an ArrayList
	 * @return A DataComponent of Entries representing the contents in the 
	 *         ports table of the INI file.
	 */
	private DataComponent loadPortsData(Iterator<String> it) {
		
		// Create the ports component
		DataComponent portsTable = new DataComponent();
		portsTable.setName("PORTS");
		portsTable.setDescription("Entries in the Ports table of "
				+ "an IPS framework INI input file");
		portsTable.setId(currID);
		currID++;
		Entry entry;
		String[] splitLine = null;
		
		// Make sure that the file keeps going.  After the completion of the 
		// loadGlobalConfiguration() method the current line that the iterator
		// is at should be [PORTS]
		if (!it.hasNext()) {
			System.err.println("Reached unexpected end of file while trying to "
					+ "locate the Ports Table.  Please check your input file and "
					+ "try again.");
			System.exit(1);
		}
		
		// Iterate until we get to the entries in the Ports Table.  The first
		// entry is an enumeration of the ports that will follow.  We need to 
		// record those before going on so that we can verify that all of the 
		// ports are declared correctly.
		String line = it.next();
		while (it.hasNext() && !line.contains("NAMES = ")){
			line = it.next();
		}
		if (!it.hasNext()) {
			System.err.println("Reached unexpected end of file while trying to "
					+ "read the Ports Table.  Please check your input file and try again.");
			System.exit(1);
		}
		
		
		// Get the names specified in the NAMES entry by splitting on the =
		// sign and then keeping everything after, which we then split on each
		// space, and turn that into an ArrayList for easier searching later
		ArrayList<String> portNames = new ArrayList<String>(
				Arrays.asList(line.split(" = ")[1].split(" ")));
		
		// Go through the rest of the ports table and add the entries as we 
		// find them, while making sure that we find all of them.
		while (portNames.size() > 0 && it.hasNext()){
			
			// Check if this line contains an entry
			if (line.contains("[[") && line.contains("]]")){
				
				// Get the port name of the entry & make sure that it is in 
				// the list of portNames.
				String portName = line.replaceAll("[^a-zA-Z0-9]","");
				entry = makeIPSEntry();
				if (portNames.contains(portName)) {
					
					// Set the details for the entry
					entry.setName(portName);		
					entry.setId(currID);
					currID++;
				
					// The next line should give details of the implementation
					line = it.next();
					if (line.contains("IMPLEMENTATION = ")) {
						String implementation = line.split(" = ", 2)[1];
						entry.setValue(implementation);
					} else {
						System.err.println("Unexpected token after ports entry, " 
								+ "check your input file and try again.");
						System.exit(1);
					}
					
					// Found a complete port entry, now remove the port from the 
					// list of remaining ports to be found.
					portNames.remove(portName);
					portsTable.addEntry(entry);
				}
			}
			// read another line
			line = it.next();

		}
		
		return portsTable;
	}

	
	/**
	 * Loads the top section of the IPS framework INI file and returns the contents
	 * as a DataComponent of Entries.  Each line is set as an Entry.
	 * 
	 * @param it
	 *           Iterator over the lines of the INI file as an ArrayList
	 * @return A DataComponent of Entries representing the contents at the
	 *         top of the INI file.
	 */
	private DataComponent loadGlobalConfiguration(Iterator<String> it) {
		// Create the global configuration component
		DataComponent globalConfiguration = new DataComponent();
		globalConfiguration.setName("Global Configuration");
		globalConfiguration.setDescription("Entries at the top of an "
				+ "IPS framework INI input file.");
		globalConfiguration.setId(currID);
		currID++;
		Entry entry;
		String[] splitLine = null;
		
		// Read in new parameters until we reach the PORTS entry or the end
		// while only taking in lines that have variable assignments

		String line = it.next();
		while (!line.contains("[PORTS]") && it.hasNext()) {
			
			// The format in this section is: KEY = VALUE # Comment
			// First check if the line contains a parameter
			if (line.contains("=")) {
				
				// If the line has a comment split on it and disregard it
				if (line.contains("#")) {
					line = line.split("#", 2)[0];
				}

				// Get the content for the entry
				splitLine = line.split("=", 2);
				
				// Set up the entry
				entry = makeIPSEntry();
				entry.setName(splitLine[0]);
				entry.setValue(splitLine[1]);
				entry.setId(currID); 
				currID++;
				globalConfiguration.addEntry(entry);
			}
			
			// Read in another line
			line = it.next();
		}
		
		// Return the parameters
		return globalConfiguration;
	}


	/**
	 * Read the INI file lines into entries of an ArrayList so that it is 
	 * easy to parse the sections into ICE.
	 * 
	 * @param iniFile
	 *           The INI file to be read in.
	 * @return An ArrayList of strings of the lines of the INI file.
	 * @throws FileNotFoundException
	 *           Thrown when the INI file cannot be found.
	 * @throws IOException
	 *           Thrown when the INI file cannot be read or closed.
	 */
	private ArrayList<String> readFileLines(File iniFile) 
			throws FileNotFoundException, IOException {
		// Convert to FileInputStream
		FileInputStream fileStream = null;
		fileStream = new FileInputStream(iniFile);
		
		// Read the FileInputStream and append to a StringBuffer
		StringBuffer buffer = new StringBuffer();
		int fileByte;
		while ((fileByte = fileStream.read()) != -1)  {
			buffer.append((char) fileByte);
		}
		
		// Close the stream
		fileStream.close();
		
		// Break up the StringBuffer at each newline character
		String[] bufferSplit = (buffer.toString()).split("\n");
		ArrayList<String> fileLines = new ArrayList<String>(
				Arrays.asList(bufferSplit));
		
		// Return the ArrayList
		return fileLines;
	}
	
	
	/**
	 * Initialize a default entry for a Caebat model
	 * @return the default Caebat entry
	 */
	private Entry makeIPSEntry() {
		Entry entry = new Entry() {
			protected void setup() {
				this.setName("IPS Default Entry");
				this.tag = "";
				this.ready = true;
				this.setDescription("");
				this.allowedValues = new ArrayList<String>();
				this.defaultValue = "";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		
		return entry;
	}
}
