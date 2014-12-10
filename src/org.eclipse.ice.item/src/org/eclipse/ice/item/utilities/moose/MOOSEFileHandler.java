/*******************************************************************************

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
package org.eclipse.ice.item.utilities.moose;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class reads and writes MOOSE Blocks and Parameters to and from the
 * different MOOSE file types, including parsing from YAML and writing to
 * GetPot.
 * </p>
 * <p>
 * There are two primary types of files associated with MOOSE: the YAML file
 * used to specify the possible configuration of an input file and the input
 * file itself, which is a GetPot/Perl configuration file.
 * </p>
 * <p>
 * This class realizes the IComponentVisitor interface to find DataComponents in
 * the TreeComposites that can be converted into a parameter set for a MOOSE
 * input block. If there are other Components in a TreeComposite. They are
 * completely ignored.
 * </p>
 * <p>
 * Blocks and YAMLBlocks are used because each block needs to be converted to or
 * from a TreeComposite. This is complicated in the case of loading the YAML
 * input specification because it is a *specification* (or schema) and not the
 * input itself. The nodes of this tree are what could be configured, not what
 * is, so they must be setup as child exemplars on a TreeComposite.
 * </p>
 * <!-- end-UML-doc -->
 *  * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MOOSEFileHandler {

	/**
	 * A flag to denote whether or not debugging is enabled
	 */
	private static boolean debugFlag = false;

	// Set the debug flag
	static {
		if (System.getProperty("DebugICE") != null) {
			debugFlag = true;
		}
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation writes a set of MOOSE blocks to the specified file path.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param filePath
	 *            <p>
	 *            The file path to which the MOOSE blocks should be dumped. If
	 *            the path is null or empty, the operation returns without doing
	 *            any work.
	 *            </p>
	 * @param blockSet
	 *            <p>
	 *            The collection of TreeComposites that represent MOOSE blocks
	 *            to be dumped to the file. The TreeComposites should only
	 *            contain a single DataComponent, id = 1, and other
	 *            TreeComposites. Any other components in the TreeComposite will
	 *            be ignored.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void dumpInputFile(String filePath, ArrayList<TreeComposite> blockSet) {
		// begin-user-code

		// Local Declarations
		File inputFile = null;
		String outputString = "";
		FileWriter fileWriter = null;
		BufferedWriter fileOutputWriter = null;
		ArrayList<Block> blocks = null;

		// Only do this if the file path and data are valid
		if (filePath != null && blockSet != null) {
			// Create the blocks from the incoming tree composites
			blocks = new ArrayList<Block>();
			for (TreeComposite blockTree : blockSet) {
				Block block = new Block();
				block.fromTreeComposite(blockTree);
				blocks.add(block);
			}
			// Reorganize the blocks so that blocks that must come first, such
			// as "Functions" and "Variables" do.
			for (int i = 0; i < blocks.size(); i++) {
				// Grab the block and its name
				Block block = blocks.get(i);
				String name = block.getName();
				// Check for the names of the blocks that have to be written
				// first
				if ("Functions".equals(name) || "Variables".equals(name)) {
					// Remove the block from the set
					blocks.remove(i);
					// Write the block to the output string
					outputString += block.toGetPot(null);
					// Decrement the counter so we go back and get any remaining
					// blocks.
					--i;
				}
			}
			// Dump the blocks to the output String
			for (int i = 0; i < blocks.size(); i++) {
				outputString += blocks.get(i).toGetPot(null);
			}
			// Try to write the file
			try {
				// Open the file and writers
				inputFile = new File(filePath);
				fileWriter = new FileWriter(inputFile);
				fileOutputWriter = new BufferedWriter(fileWriter);
				// Dump the string to the file
				fileOutputWriter.write(outputString);
				// Flush everything to the file and close it
				fileOutputWriter.flush();
				fileOutputWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("MOOSEFileHandler Exception: "
						+ "Unable to write output file.");
				e.printStackTrace();
			}
		}

		return;
		// end-user-code
	}

	/**
	 * This operations loads a MOOSE GetPot file at the specified path and
	 * returns a fully-configured set of ICE TreeComposites.
	 * 
	 * @param filePath
	 *            The file path from which the MOOSE blocks written in GetPot
	 *            should be read. If the path is null or empty, the operation
	 *            returns without doing any work.
	 * @return The MOOSE input file specification as read from the GetPot input
	 *         and stored in TreeComposites. Each TreeComposite contains both
	 *         parameters and exemplar children. Any parameters in a
	 *         TreeComposite are contained in a DataComponent. The id of the
	 *         data component is 1.
	 */
	public ArrayList<TreeComposite> loadFromGetPot(String filePath) {

		// Local Declarations
		ArrayList<TreeComposite> trees = new ArrayList<TreeComposite>();
		byte[] fileByteArray = null;
		String mooseFileString = null, potLine = null;

		// Quit if the path is boned
		if (filePath == null || filePath.isEmpty()) {
			return null;
		}

		// Post some debug info
		if (debugFlag) {
			System.out.println("MOOSEFileHandler Message: "
					+ "Attempting to loading GetPot file " + filePath);
		}

		// Load the GetPot file
		try {
			RandomAccessFile mooseFile = new RandomAccessFile(filePath, "r");
			// Put it into a byte array
			fileByteArray = new byte[(int) mooseFile.length()];
			mooseFile.read(fileByteArray);
			// And then a string
			mooseFileString = new String(fileByteArray);
			// Close the file
			mooseFile.close();
			// Post some more debug info
			if (debugFlag) {
				System.out.println("MOOSEFileHandler Message: File loaded.");
			}
		} catch (IOException e) {
			// Complain if the file is not found
			System.err.println("MOOSEFileHandler Message: "
					+ "Unable to load GetPot file!");
			e.printStackTrace();
		}

		// Check the string before proceeding
		if (mooseFileString != null && !mooseFileString.isEmpty()) {
			// Create an array list from the string
			ArrayList<String> potLines = new ArrayList<String>(
					Arrays.asList(mooseFileString.split("\n")));

			// Remove (non-parameter) commented lines and white space
			String trimmedPotLine = "";
			for (int i = 0; i < potLines.size(); i++) {
				
				trimmedPotLine = potLines.get(i).trim();
				
				if (trimmedPotLine.startsWith("#") 
						&& !trimmedPotLine.contains("=")
						&& !trimmedPotLine.contains("[")
						&& !trimmedPotLine.contains("]")) {
					// Lines that start with "#" but have no "=" are comments
					// that aren't parameters and should be removed
					potLines.remove(i);
					// Update "i" so that we read correctly
					--i;
				} else 
					if (potLines.get(i).isEmpty()) {
					// Remove empty lines
					potLines.remove(i);
					// Update "i" so that we read correctly
					--i;
				}
				else {
					// All other lines should be trimmed
					potLines.set(i, potLines.get(i).trim());
				}
			}
			
			// Read all of the lines again, create blocks and load them.
			int counter = 0, endCounter = 1;
			while (counter < potLines.size()) {
				// Get the line and shift the counters
				potLine = potLines.get(counter);
				++counter;
				
				// The start of a full block is a line with the name in brackets
				// and without the "./" sequence.
				if (potLine.contains("[") && potLine.contains("]")) {
					// Go to the next line
					potLine = potLines.get(endCounter);			
					
					// Loop over the block and find the end
					while (!potLine.contains("[]")) {
						// Update the line and the counter
						potLine = potLines.get(endCounter);
						++endCounter;
					}
					// Create a new block
					Block block = new Block();
					ArrayList<String> blockLines = new ArrayList<String>(
							potLines.subList(counter - 1, endCounter));
					StringBuilder stringBuilder = new StringBuilder(
							blockLines.get(0));
					blockLines.set(0, stringBuilder.toString());
					block.fromGetPot(blockLines);
					// Add the block to the list
					trees.add(block.toTreeComposite());
					// Update the counter to point to the last read line
					counter = endCounter;
					// Print some debug information
					if (debugFlag) {
						System.out.println("\nMOOSEFileHandler Message: "
								+ "Block output read from GetPot file "
								+ filePath + " follows.");
						// Dump each line of the newly created block
						for (String line : blockLines) {
							System.out.println(line);
						}
					}
				}
			}
		} else if (debugFlag) {
			System.err.println("MOOSEFileHandler Message: "
					+ "String loaded from " + filePath + " is null or empty.");
		}

		return trees;
	}
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations loads a MOOSE YAML file at the specified path and returns
	 * a fully-configured set of ICE TreeComposites.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param filePath
	 *            <p>
	 *            The file path from which the MOOSE blocks written in YAML
	 *            should be read. If the path is null or empty, the operation
	 *            returns without doing any work.
	 *            </p>
	 * @return <p>
	 *         The MOOSE input file specification as read from the YAML input
	 *         and stored in TreeComposites. Each TreeComposite contains both
	 *         parameters and exemplar children. Any parameters in a
	 *         TreeComposite are contained in a DataComponent. The id of the
	 *         data component is 1.
	 *         </p>
	 * @throws IOException 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<TreeComposite> loadYAML(String filePath) throws IOException {
		// begin-user-code

		// Local Declarations
		InputStream input = null;
		String syntaxFilePath, treeName;
		ArrayList<String> hardPathsList = null;
		ArrayList<TreeComposite> trees = new ArrayList<TreeComposite>();
		Map<String, TreeComposite> treeMap = null;
		TreeComposite oneUpTree = null;

		// Quit if the path is boned
		if (filePath == null || filePath.isEmpty()) {
			return null;
		}
		
		// Get a handle on the YAML file
		File yamlFile = new File(filePath);
		input = new FileInputStream(yamlFile);
				
		// Load the YAML tree
		if (debugFlag) {
			System.out.println("MOOSEFileHandler Message: Loading YAML file "
					+ filePath.toString());
		}
		Yaml yaml = new Yaml();
		ArrayList list = (ArrayList) yaml.load(input);
		if (debugFlag) {
			System.out.println("MOOSEFileHandler Message: File loaded.");
		}

		// Load the block list. Use YAMLBlocks so that they can be converted to
		// TreeComposites appropriately.
		for (int i = 0; i < list.size(); i++) {
			Block block = new YAMLBlock();
			block.loadFromMap((Map<String, Object>) list.get(i));
			block.active = true;
			trees.add(block.toTreeComposite());
		}

		// Close the files
		try {
			input.close();
		} catch (IOException e) {
			// Complain
			e.printStackTrace();
		}

		// Put all the names of top-level nodes into a list (we use this later)
		ArrayList<String> topLevelNodes = new ArrayList<String>();
		for (TreeComposite node : trees) {
			topLevelNodes.add(node.getName());
		}
		// Instantiate a HashMap that all TreeComposites and their exemplar
		// children trees can be added to, keyed by absolute path name
		treeMap = new HashMap<String, TreeComposite>();

		// Create empty stack for TreeComposites
		Stack<TreeComposite> treeStack = new Stack<TreeComposite>();
		treeStack.push(null);

		// Push the top level TreeComposites from the ArrayList first on first
		for (TreeComposite tree : trees) {
			treeStack.push(tree);
		}
		// Pop one of the top-level trees off to start.
		TreeComposite tree = treeStack.pop();
		ArrayList<TreeComposite> childExemplars;
		treeName = "";
		int prevNameIndex;
		while (tree != null) {

			// Append to the tree name
			treeName += "/" + tree.getName();			
			
			// Put the tree in the Map, keyed on path name
			treeMap.put(
					(treeName.startsWith("/") ? 
							treeName.substring(1) : treeName), tree);

			// Push child exemplars to the top of the tree stack
			childExemplars = tree.getChildExemplars();
			for (int i = (childExemplars.size() - 1); i >= 0; i--) {
				treeStack.push(childExemplars.get(i));
			}

			// If the next tree in the stack is a top-level tree, clear the
			// path name
			if (trees.contains(treeStack.peek())) {
				treeName = "";
			}

			// Otherwise, if the current tree didn't have child exemplars to
			// push onto the stack, remove the last "part" of the path name, as
			// we'll be going back up one level
			else if (childExemplars.isEmpty()) {
				
				// Get the name of the tree one level up
				prevNameIndex = treeName.lastIndexOf("/" + tree.getName());
				treeName = treeName.substring(0, prevNameIndex);				
				
				// Go up another level if the next tree in the stack isn't
				// a child exemplar of the current tree referenced by treeName
				oneUpTree = treeMap.get(treeName.substring(1));
				if (oneUpTree != null && !oneUpTree.getChildExemplars().contains(treeStack.peek())) {
					prevNameIndex = treeName.lastIndexOf("/");
					treeName = ((prevNameIndex == 0 || prevNameIndex == -1) ? 
							treeName : treeName.substring(0, prevNameIndex));
				}
				
			}

			// Pop the next tree off the stack
			tree = treeStack.pop();
		}

		// Define the file path of the action syntax file
		int yamlIndex = filePath.indexOf(".yaml");
		syntaxFilePath = filePath.substring(0, yamlIndex) + ".syntax";

		// Load the list of all "hard" paths from the action syntax file
		try {
			hardPathsList = loadActionSyntax(syntaxFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Begin looking through the TreeComposites for matches to the list of
		// "hard" paths from the action syntax file
		TreeComposite currTree;
		boolean hasType = false;
		int typeIndex = -1;
		String cleanPath;
		ArrayList<TreeComposite> types, currChildExemplars;
		DataComponent typeParameters = null, treeParameters = null;

		for (String path : hardPathsList) {

			// Clean the path of the excess return carriage at the end
			cleanPath = (path.endsWith("\r") ? path.substring(0,
					path.length() - 1) : path);

			// Get the tree with the corresponding hard path
			currTree = treeMap.get(cleanPath);

			// Check if there is a corresponding tree at all (depends on the
			// user's YAML file), and if so, if it has child exemplars
			if (currTree != null && !(currTree.getChildExemplars().isEmpty())) {

				// Iterate through the child exemplars, look for one named
				// "<type>"
				currChildExemplars = currTree.getChildExemplars();
				for (int i = 0; i < currChildExemplars.size(); i++) {
					hasType = "<type>".equals(currChildExemplars.get(i)
							.getName());
					if (hasType) {
						typeIndex = i;
						break;
					}
				}

				if (hasType) {
					// Get the exemplars of <type>, these will become
					// the list of types to chose from
					types = (ArrayList<TreeComposite>) currTree
							.getChildExemplars().get(typeIndex)
							.getChildExemplars();

					// Remove the <type> child exemplar from currTree
					currChildExemplars.remove(typeIndex);
					currTree.setChildExemplars(currChildExemplars);

					// Copy all the parameters from currTree into all to the
					// childExemplars before we instantiate an
					// AdaptiveTreeComposite
					Map<String, Entry> parameterMap = new HashMap<String, Entry>();
					for (TreeComposite currType : types) {

						// Get the current type's data node
						typeParameters = (DataComponent) currType
								.getDataNodes().get(0);
						// Get the current tree's data node
						treeParameters = (DataComponent) currTree
								.getDataNodes().get(0);

						// Put all the typeParameters in a HashMap, keyed on
						// name
						parameterMap.clear();
						for (Entry parameter : typeParameters
								.retrieveAllEntries()) {
							parameterMap.put(parameter.getName(), parameter);
						}
						// Loop through the current tree's parameters, appending
						// them all onto the type's parameters list
						for (Entry currEntry : treeParameters
								.retrieveAllEntries()) {

							// Check that the HashMap doesn't already have an
							// entry with the same name
							if (!parameterMap.containsKey(currEntry.getName())) {
								// Append the parameter from one list onto the
								// other
								typeParameters.addEntry(currEntry);
							}
						}
					}

					// Create a new AdaptiveTreeComposite with the list of types
					AdaptiveTreeComposite adapTree = new AdaptiveTreeComposite(
							types);

					// Copy all the TreeComposite data (data node, exemplars,
					// etc.) from currTree
					adapTree.copy(currTree);
					currTree = adapTree;

					// Overwrite the tree in the map
					treeMap.put(cleanPath, adapTree);

					// Since we allocated the AdaptiveTreeComposite with new,
					// the reference to the original object is lost, so we must
					// wire it back in

					// Check if this tree is a child exemplar of something (ie.
					// isn't a top-level node)
					if (!topLevelNodes.contains(currTree.getName())) {

						// Get the name of the tree that this
						// AdaptiveTreeComposite is a child exemplar of
						prevNameIndex = cleanPath.indexOf("/"
								+ currTree.getName());
						treeName = cleanPath.substring(0, prevNameIndex);

						// Re-set the AdaptiveTreeComposite as a child exemplar
						// of whatever tree it belongs to
						TreeComposite exemplarParent = treeMap.get(treeName);
						if (exemplarParent != null) {
							exemplarParent.addChildExemplar(currTree);
						}
					}
				}
			}

			// Reset flags, indices
			hasType = false;
			typeIndex = -1;
		}

		// Reconstruct the top level nodes into an ArrayList
		ArrayList<TreeComposite> newTrees = new ArrayList<TreeComposite>();
		for (String nodeName : topLevelNodes) {
			newTrees.add(treeMap.get(nodeName));
		}
		return newTrees;
		// end-user-code
	}

	/**
	 * This method is responsible for loading the action syntax file associated
	 * with a MOOSE app. It reads through the list of paths and returns the
	 * names of any that are "hard" paths (ie. have no asterisks). It also only
	 * returns unique paths, as there is no significance to duplicates (to us)
	 * in the action syntax file (it's a MOOSE "bug").
	 * 
	 * @param filePath
	 *            The file path pointing to the action syntax file
	 * @return A String ArrayList of unique action syntax "hard" paths (ie. does
	 *         not end in an asterisk)
	 * @throws IOException
	 */
	public ArrayList<String> loadActionSyntax(String filePath)
			throws IOException {

		// Local declarations
		ArrayList<String> actionSyntax = null;
		String currLine, previousLine = "";

		// Check if the filepath is valid
		if (filePath == null || filePath.isEmpty()) {
			if (debugFlag) {
				System.out.println("MOOSEFileHandler Error: Could not open "
						+ "action syntax file: " + filePath);
			}
			return actionSyntax;
		}

		// Open the action syntax file
		if (debugFlag) {
			System.out.println("MOOSEFileHandler Message: Loading action "
					+ "syntax file: " + filePath);
		}
		
		actionSyntax = (ArrayList<String>) 
				Files.readAllLines(Paths.get(filePath), Charset.defaultCharset());

		// Iterate through the list and eliminate non-hard-paths and
		// duplicate entries
		int i = 0;
		while (i < actionSyntax.size()) {

			// Get the current line
			currLine = actionSyntax.get(i);

			// Check for an asterisk at the end of the line
			if (currLine.endsWith("*\r") || currLine.endsWith("*")) {
				actionSyntax.remove(currLine);
			}
			
			// Remove from the ArrayList if it's the same as the last line
			else if (previousLine.equals(currLine)) {
				actionSyntax.remove(currLine);
			}
			
			// Otherwise it's a unique hard-path, and move to the next line
			else {
				previousLine = currLine;
				i++;
			}
		}

		return actionSyntax;
	}
	
}