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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.io.serializable.IWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * IPSWriter is a class responsible for converting an ICE Form and writing it as
 * an IPS Configuration file.
 * </p>
 * 
 * @author Andrew Bennett
 * 
 */
public class IPSWriter implements IWriter {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(IPSWriter.class);

	/**
	 * Nullary constructor
	 */
	public IPSWriter() {
		super();
	}

	/**
	 * Writes out an INI file from the given Form to the given IFile.
	 * 
	 * @param form
	 *            The form containing the data to write.
	 * @param ifile
	 *            The file to write to.
	 */
	@Override
	public void write(Form form, IFile ifile) {
		// Make sure the input isn't null
		if (form == null || ifile == null) {
			return;
		}

		// Get the components from the form and make sure we have a
		// valid place that we can write the file out to
		ArrayList<Component> components = form.getComponents();

		// Make sure that the form had data that looks correct, and the output
		// file exists
		if (components != null && components.size() > 3) {
			try {
				PipedInputStream in = new PipedInputStream(8196);
				PipedOutputStream out = new PipedOutputStream(in);
				if (!ifile.exists()) {
					byte[] blank = "".getBytes();
					InputStream s = new ByteArrayInputStream(blank);
					ifile.create(s, true, new NullProgressMonitor());
				}
				// Write out the header, global configuration, and ports table
				writeICEHeader(out);
				writeGlobalConfig((TableComponent) components.get(1), out);
				writePortsTable((TableComponent) components.get(2), out);

				// Get the master details
				MasterDetailsComponent masterDetails = (MasterDetailsComponent) components
						.get(3);

				// Write out each of the ports from the master individually
				for (int i = 0; i < masterDetails.numberOfMasters(); i++) {
					writeComponent(
							masterDetails.getDetailsAtIndex(i),
							out);
				}

				// Write out the time loop data then close the stream
				writeTimeLoopData((DataComponent) components.get(0), out);
				out.close();
				ifile.setContents(in, true, false, new NullProgressMonitor());
			} catch (FileNotFoundException e) {
				logger.info("IPSWriter Message: Could not find "
						+ ifile.getName() + " for writing.");
			} catch (IOException e) {
				logger.info("IPSWriter Message: Could not write to "
						+ ifile.getName() + " du to an IO error");
			} catch (CoreException e) {
				logger.info("IPSWriter Message: Could not write to "
						+ ifile.getName() + " due to an ICE Core error.");
			}
		}

	}

	/**
	 * <p>
	 * Searches a given IFile for content that matches a given regular
	 * expression, and replaces that match with another given value.
	 * </p>
	 * 
	 * @param ifile
	 *            The file to search in
	 * @param regex
	 *            A string representing a regular expression containing the
	 *            specification of what to search for
	 * @param value
	 *            The replacement value for matches to the regex
	 */
	@Override
	public void replace(IFile ifile, String regex, String value) {

		// Make sure we aren't given any null values
		if (ifile == null || regex == null || value == null) {
			return;
		}

		// First, get an ArrayList with each line of the file so
		// we can search through them
		// Read in the ini file and create the iterator
		StringBuffer buffer = null;
		try {
			// Read the FileInputStream and append to a StringBuffer
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ifile.getContents()));
			buffer = new StringBuffer();
			int fileByte;
			while ((fileByte = reader.read()) != -1) {
				buffer.append((char) fileByte);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			logger.info("IPSWriter Message:  Could not find " + ifile.getName()
					+ " for reading.");
			return;
		} catch (IOException e) {
			logger.info("IPSWriter Message:  Could not read in "
					+ ifile.getName() + " for replacement writing.");
			return;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			logger.error(getClass().getName() + " Exception!",e);
		}

		// Break up the StringBuffer at each newline character
		String[] bufferSplit = (buffer.toString()).split("\n");
		ArrayList<String> fileLines = new ArrayList<String>(
				Arrays.asList(bufferSplit));

		/*
		 * NOTE: Need to do this differently if we are using Eclipse IFiles
		 * exclusively -- Andrew // Make a backup of the original file in case
		 * something goes wrong during // the search and replace operations File
		 * tempFile = new File(ifile.getFullPath().toOSString().split("[.]")[0]
		 * + "_bak.conf"); tempFile.deleteOnExit(); try {
		 * Files.copy(file.toPath(), tempFile.toPath(),
		 * StandardCopyOption.REPLACE_EXISTING); } catch (IOException e1) {
		 * System
		 * .out.println("IPSWriter Message:  Error creating copy of original " +
		 * "file for backup."); return; }
		 */

		// Write out the file again, replacing any occurrences
		// of the regex with the given replacement value

		StringBuilder builder = new StringBuilder();
		for (String line : fileLines) {
			if (line.matches(regex)) {
				line = line.replaceAll(regex, value);
			} else if (line.contains(regex)) {
				line = line.replaceAll(regex, value);
			}
			line = line + "\n";
			builder.append(line);
		}

		InputStream replaceStream = new ByteArrayInputStream(builder.toString()
				.getBytes());
		try {
			ifile.setContents(replaceStream, true, true,
					new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			logger.error(getClass().getName() + " Exception!",e);
		}
	}

	/**
	 * <p>
	 * Return a string saying this is an IPS Writer
	 * </p>
	 * 
	 * @return a string containing "IPSWriter"
	 */
	@Override
	public String getWriterType() {
		return "IPSWriter";
	}

	/**
	 * <p>
	 * Writes an ICE header at the top of the INI file, providing the date, time
	 * and hostname where the file was generated.
	 * </p>
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws UnknownHostException
	 *             Thrown when the host cannot be resolved
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeICEHeader(OutputStream stream)
			throws UnknownHostException, IOException {

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String date = dateFormat.format(new Date());
		String user = System.getProperty("user.name");
		String hostname = "";
		hostname = InetAddress.getLocalHost().getHostName();

		// A header to tell where the file came from
		String iceHeader = String
				.format("# *** IPS INI file generated by ICE ***\n"
						+ "#  (Eclipse Integrated Computational Environment)\n"
						+ "#\n" + "#  Created:   %-30s\n"
						+ "#  User:      %-30s\n" + "#  Hostname:  %-30s\n\n\n",
						date, user, hostname);

		// Write to the output stream
		byte[] byteArray = iceHeader.getBytes();
		stream.write(byteArray);

		return;

	}

	/**
	 * <p>
	 * Takes the Global Configuration DataComponent and extracts each of the
	 * parameters and writes the contents to the specified OutputStream.
	 * </p>
	 * 
	 * @param component
	 *            The DataComponent containing the global configuration
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeGlobalConfig(TableComponent component, OutputStream stream)
			throws IOException {

		// Local Declarations
		String configString = "";
		ArrayList<IEntry> row;
		byte[] byteArray;

		// Build the output by going through each row
		for (int i = 0; i < component.numberOfRows(); i++) {
			row = component.getRow(i);
			configString += row.get(0).getValue().trim() + "="
					+ row.get(1).getValue().trim() + "\n";
		}
		configString += "\n";

		// Write it out
		byteArray = configString.getBytes();
		stream.write(byteArray);
	}

	/**
	 * <p>
	 * Takes the Ports Table DataComponent and extracts each of the ports
	 * implementation details and writes the contents to the specified
	 * OutputStream.
	 * </p>
	 *
	 * @param component
	 *            The DataComponent containing the ports table
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writePortsTable(TableComponent component, OutputStream stream)
			throws IOException {

		// Local Declarations
		String configString = "[PORTS]\n\tNAMES = ";
		ArrayList<IEntry> row;
		byte[] byteArray;

		// Build the output by going through each row
		for (int i = 0; i < component.numberOfRows(); i++) {
			row = component.getRow(i);
			configString += row.get(0).getValue().trim() + " ";
		}
		configString += "\n\n";

		for (int i = 0; i < component.numberOfRows(); i++) {
			row = component.getRow(i);
			configString += "\t[[" + row.get(0).getValue().trim() + "]]\n\t\t"
					+ "IMPLEMENTATION = " + row.get(1).getValue().trim() + "\n";
		}
		configString += "\n";

		// Write it out
		byteArray = configString.getBytes();
		stream.write(byteArray);
	}

	/**
	 * <p>
	 * Takes a Port Entry DataComponent and extracts each of the port's
	 * implementation details and writes the contents to the specified
	 * OutputStream.
	 * </p>
	 * 
	 * @param component
	 *            The DataComponent containing a port entry
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeComponent(DataComponent component, OutputStream stream)
			throws IOException {
		// Get the port name and the entries
		String currLine = "[" + component.getName() + "]\n";
		ArrayList<IEntry> portParams = component.retrieveAllEntries();

		// Write the port header
		byte[] byteArray = currLine.getBytes();
		stream.write(byteArray);

		// Write each of the port parameters
		for (IEntry param : portParams) {
			// Write the port header
			currLine = "\t" + param.getName().trim() + " = ";
			byteArray = currLine.getBytes();
			stream.write(byteArray);

			// Write the port implementation
			currLine = param.getValue().trim() + "\n";
			byteArray = currLine.getBytes();
			stream.write(byteArray);
		}

		// Write a blank line for a spacer
		currLine = "\n";
		byteArray = currLine.getBytes();
		stream.write(byteArray);
	}

	/**
	 * <p>
	 * Takes the Time Loop DataComponent and extracts each of the time loop's
	 * implementation details and writes the contents to the specified
	 * OutputStream.
	 * </p>
	 * 
	 * @param component
	 *            The DataComponent containing time loop data
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeTimeLoopData(DataComponent component, OutputStream stream)
			throws IOException {
		// Get the port name and the entries
		String currLine = "[TIME_LOOP]\n";
		ArrayList<IEntry> timeLoopParams = component.retrieveAllEntries();

		// Write the port header
		byte[] byteArray = currLine.getBytes();
		stream.write(byteArray);

		// Write each of the port parameters
		for (IEntry param : timeLoopParams) {
			// Write the port header
			currLine = "\t" + param.getName().trim() + " = ";
			byteArray = currLine.getBytes();
			stream.write(byteArray);

			// Write the port implementation
			currLine = param.getValue().trim() + "\n";
			byteArray = currLine.getBytes();
			stream.write(byteArray);
		}

		// Write out a final blank line
		currLine = "\n";
		byteArray = currLine.getBytes();
		stream.write(byteArray);

	}

}
