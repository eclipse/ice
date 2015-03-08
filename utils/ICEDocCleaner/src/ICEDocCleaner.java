/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * This application is designed to clean files of any superfluous documentation
 * created by UML to Java transformations. To use it:
 * 
 * 1.) Import the project into Eclipse > General > Existing project
 * 2.) Right click ICEDocCleaner.java > Run as Java Application
 * 3.) Interaction is at the console; input the full filepath for a .java file 
 *     (or type "exit" to terminate)
 * 
 * @author Anna Wojtowicz
 */
public class ICEDocCleaner {
		
    public static void main(String[] args) throws IOException {

    	// Get a handle on the file
    	System.out.println("Enter full filepath: ");
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String path = br.readLine();
    	if (path.contains("exit")) {
    		return;
    	}
    	File javaFile = new File(path);
    	
    	if (javaFile != null && javaFile.exists() && javaFile.isFile()) {
    		
    			System.out.println("File found, beginning cleaning...");
	    		FileInputStream inputStream = null;
	    		Scanner scanner = null;
	    		StringBuilder sb = null;
	    		
	    		try {
	    			
        		// Set up a scanner (should be more efficient than Files.nio for very big files)
    		    inputStream = new FileInputStream(javaFile);
    		    scanner = new Scanner(inputStream, "UTF-8");
    		    String line = "";
    		    sb = new StringBuilder();
    		    
    		    while (scanner.hasNextLine() && (line = scanner.nextLine()) != null) {
    		    	    		    	
    		    	if (line.matches("\\s*\\*\\s*<!-- (begin|end)-UML-doc -->\\s*")
    		    			|| line.matches("\\s*\\*\\s*<!-- (begin|end)-UML-doc -->\\s*<!-- (begin|end)-UML-doc -->")
    		    			|| line.matches("\\s*// (begin|end)-user-code\\s*")
    		    			|| line.contains("@generated")
    		    			|| line.contains("UML to Java")) {

    		    		// Skip over the line
    		    		continue;
    		    		
    		    	} else {

	    		    	// Append the line to the StringBuilder
	        			sb.append(line + "\n");
    		    	}
    		    }
    		    
    		    System.out.println("Done cleaning.");
    		    
    		    // Note that Scanner suppresses exceptions, throw manually
    		    if (scanner.ioException() != null) {
    		        throw scanner.ioException();
    		    }
    		    
    		} finally {
    			
    			// Close this MF dooown
    		    if (inputStream != null) {
    		        inputStream.close();
    		    }
    		    if (scanner != null) {
    		        scanner.close();
    		    }
    		}
	        
	    	// Create a backup file in case something goes wrong
        	int fileExtIndex = javaFile.getPath().lastIndexOf(".");
        	String bakFilePath = javaFile.getPath().substring(0, fileExtIndex) + "_bak" + javaFile.getPath().substring(fileExtIndex);
        	
    		File bakFile = new File(bakFilePath);
    		if (bakFile.exists()) {
    			bakFile.delete();
    		}
    		Files.copy(javaFile.toPath(), bakFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    		System.out.println("Creating back-up file... (will delete on successful exit)\n" + bakFile.getPath());
    		
        	// Overwrite the original file
        	BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile));
        	writer.write(sb.toString());
        	System.out.println("Overwriting original file...");
        	if (writer != null) {
        		writer.close();
        	}
        	
        	System.out.println("Process complete!");
        	bakFile.deleteOnExit();
	    		
    	} else {
    		System.out.println("Could not find file.");
    		main(args);
    	}
    	
    	// k bai.
    	return;
    }

}