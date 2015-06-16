/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.test.pwr;

import static org.junit.Assert.*;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.ice.reactor.*;
import org.eclipse.ice.reactor.pwr.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Eric J. Lingerfelt
 */
public class PWReactorHDF5IntegrationTester {

	/**
	 * 
	 */
	@BeforeClass
	public static void beforeClass() {

	}

	/**
	 * 
	 */
	@Test
	public void checkHDF5Integration() {

		PressurizedWaterReactor reactor = createPopulatedPWReactor();

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorData";
		File dataFile = new File(userDir + separator + "integration_test.h5");
		URI uri = dataFile.toURI();

		LWRComponentWriter writer = new LWRComponentWriter();
		writer.write(reactor, uri);
		LWRComponentReader reader = new LWRComponentReader();
		PressurizedWaterReactor readReactor = (PressurizedWaterReactor) reader
				.read(uri);

		assertTrue(reactor.equals(readReactor));

	}

	/**
	 * 
	 * @return
	 */
	private PressurizedWaterReactor createPopulatedPWReactor() {

		PressurizedWaterReactor reactor = new PressurizedWaterReactor(1);
		reactor.setName("Test PWR");
		reactor.setFuelAssemblyPitch(21.5);

		FuelAssembly assembly = new FuelAssembly(17);
		assembly.setName("Test Assembly");

		reactor.addAssembly(AssemblyType.Fuel, assembly);
		reactor.setAssemblyLocation(AssemblyType.Fuel, assembly.getName(), 0, 0);

		ArrayList<Material> materialList = new ArrayList<Material>();
		materialList.add(new Material("uo2", MaterialType.SOLID));
		materialList.add(new Material("instrument", MaterialType.SOLID));
		materialList.add(new Material("poison", MaterialType.SOLID));
		materialList.add(new Material("guide", MaterialType.SOLID));

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorData";
		File pinDataFile = new File(userDir + separator + "pin_data.txt");
		File powerDataFile = new File(userDir + separator + "power_data.txt");

		try {

			String pinDataFileContents = new String(this.readFile(pinDataFile));
			String powerDataFileContents = new String(
					this.readFile(powerDataFile));

			String[] pinDataLines = pinDataFileContents.split("\n");
			String[] powerDataLines = powerDataFileContents.split("\n");
			ArrayList<LWRRod> rodList = new ArrayList<LWRRod>();

			int column = 0;
			int row = 0;

			for (String line : pinDataLines) {

				String[] elements = line.split(",");
				int pinNumber = Integer.valueOf(elements[0]);
				int materialNumber = Integer.valueOf(elements[1]);
				double radius = Double.valueOf(elements[2]);

				Ring clad = new Ring(
						"Clad " + pinNumber,
						(Material) materialList.get(materialNumber - 1).clone(),
						1.0, radius);
				LWRRod rod = new LWRRod("Pin " + pinNumber);
				rod.setClad(clad);
				rodList.add(rod);

				assembly.addLWRRod(rod);

				if (column == 9) {
					column = 0;
					row++;
				}

				assembly.setLWRRodLocation(rod.getName(), row, column);

				column++;
			}

			for (String line : powerDataLines) {

				String[] elements = line.split(",");
				int pinNumber = Integer.valueOf(elements[0]);
				double pinPower = Double.valueOf(elements[1]);
				double uncertainty = Double.valueOf(elements[2]);

				LWRRod rod = rodList.get(pinNumber - 1);
				LWRData data = new LWRData("Pin Powers");
				data.setValue(pinPower);
				data.setUncertainty(uncertainty);
				rod.addData(data, 0.0);

			}

			return reactor;

		} catch (IOException e) {

			e.printStackTrace();
			return null;

		}

	}

	/**
	 * Read file.
	 * 
	 * @param file
	 *            the file
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte[] readFile(File file) throws IOException {
		int i = (int) file.length();
		byte[] buffer = new byte[i];
		FileInputStream fis = new FileInputStream(file);
		fis.read(buffer);
		fis.close();
		return buffer;
	}

	/**
	 * 
	 */
	@AfterClass
	public static void afterClass() {

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorData";
		File dataFile = new File(userDir + separator + "integration_test.h5");

		if (dataFile.exists()) {
			dataFile.delete();
		}

	}
}