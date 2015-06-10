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
package org.eclipse.ice.item.nuclear;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.osgi.service.prefs.BackingStoreException;

/**
 * An MOOSE Item for creating MOOSE input files. This Item expects to find the
 * YAML and action syntax files necessary in the ${workspace}/MOOSE directory.
 * These files can be generated automatically using the ICE YAML/action syntax
 * generator, or manually at the command line with the command(s):
 * 
 * ./{moose-app}-opt --yaml > {moose-app}.yaml <br>
 * ./{moose-app}-opt --syntax > {moose-app}.syntax
 * 
 * These lines must be executed for each MOOSE-based code to be used by ICE.
 * This class' Item builder defaults the MOOSE-based application to null,
 * forcing the user to select the app. Once an app is selected, reviewEntries()
 * is triggered and loads the YAML spec. If the Item was imported from an input
 * file, any data from the input file is consolidated with the YAML file in the
 * reviewEntries() method as well.
 * 
 * The MOOSEModel Form has components with set IDs as such:
 * 
 * ID = 1: The output file DataComponent ID = 2: The TreeComposite containing
 * MOOSE data ID = 3: The ResourceComponent (for displaying mesh files as
 * ICEResources)
 *
 * It is not, in general, necessary to subclass this Item and all
 * reconfiguration can be done by the builder by setting the executable name
 * since the only thing that changes from application to application is the YAML
 * input file.
 * 
 * @author Jay Jay Billings, Anna Wojtowicz, Alex McCaskey
 */

@XmlRootElement(name = "MOOSEModel")
public class MOOSEModel extends Item {

	/**
	 * The ID of the DataComponent containing the output file name.
	 */
	@XmlTransient
	public static final int fileDataComponentId = 1;

	/**
	 * The ID of the TreeComposite containing the MOOSE input tree. tree.
	 */
	@XmlTransient
	public static final int mooseTreeCompositeId = 2;

	/**
	 * The ID of the ResourceComponent that holds the mesh ICEResource.
	 */
	@XmlTransient
	public static final int resourceComponentId = 3;

	/**
	 * The process tag for writing the MOOSE output file.
	 */
	@XmlTransient
	protected static final String mooseProcessActionString = "Write MOOSE File";

	/**
	 * The list of MOOSE applications available for this Model.
	 */
	@XmlTransient
	protected ArrayList<String> mooseApps;

	/**
	 * The currently loaded MOOSE-based application.
	 */
	@XmlTransient
	protected String loadedApp;

	/**
	 * The name of the file currently stored on the Form's ResourceComponent.
	 */
	@XmlTransient
	protected String meshFileName;

	/**
	 * The "Mesh" block stored on the TreeComposite (if there is one). Used to
	 * convert a mesh file into a VizResource by {@link #createMeshResource()}.
	 */
	private TreeComposite meshBlock;

	/**
	 * An ArrayList of TreeComposites, constructed from the top-level children
	 * of the YAML spec (ie. children directly beneath the root). Used for easy
	 * cross-reference across multiple methods.
	 */
	@XmlTransient
	private ArrayList<TreeComposite> topLevelYamlTrees = null;

	/**
	 * An ArrayList of TreeComposites, constructed from the top-level children
	 * of the input data (ie. children directly beneath the root). Used for easy
	 * cross-reference across multiple methods.
	 */
	@XmlTransient
	private ArrayList<TreeComposite> topLevelInputTrees = null;

	/**
	 * Nullary constructor.
	 */
	public MOOSEModel() {
		this(null);
		return;
	}

	/**
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * 
	 * @param projectSpace
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 */
	public MOOSEModel(IProject projectSpace) {
		// Call super
		super(projectSpace);
		return;
	}

	/**
	 * This operation creates the MOOSE input file.
	 * 
	 * @param actionName
	 *            The name of action that should be performed using the
	 *            processed Form data.
	 * @return The status of the Item after processing the Form and executing
	 *         the action. It returns FormStatus.InfoError if it is unable to
	 *         run for any reason, including being asked to run actions that are
	 *         not in the list of available actions.
	 */
	public FormStatus process(String actionName) {

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		Entry outputFileEntry = ((DataComponent) form
				.getComponent(fileDataComponentId))
				.retrieveEntry("Output File Name");
		String outputFilename = outputFileEntry.getValue();
		IWriter writer = getWriter();

		// Check that the process is something that we will do and that the Item
		// is enabled
		if (mooseProcessActionString.equals(actionName) && isEnabled()
				&& writer != null) {
			// Get the file location
			IFile outputFile = project.getFile(outputFilename);

			// Write the Moose tree to file
			writer.write(form, outputFile);

			// Refresh the Project space
			refreshProjectSpace();

			// Update the status
			retStatus = FormStatus.Processed;
		} else {
			// Otherwise, punt it up to the parent to see if it can handle it.
			retStatus = super.process(actionName);
		}

		// Reset the status and return. It should only be updated if the Item is
		// enabled.
		if (isEnabled()) {
			status = retStatus;
			return retStatus;
		} else {
			return FormStatus.Unacceptable;
		}
	}

	/**
	 * This operation sets up the Form for the MOOSEModel. The form is designed
	 * to contain 3 Components.
	 * 
	 * The Form component with ID=1 is a DataComponent containing Entries
	 * related to which MOOSE-based codes are available, as well as the output
	 * file created from the MOOSE Model. These Entries in the DataComponent are
	 * named "MOOSE-Based Application" with ID=1 and "Output File Name" with
	 * ID=2.
	 * 
	 * The Form component with ID=2 is a TreeComposite containing the structure
	 * of the MOOSE input tree. By default, this Tree is empty until blocks are
	 * added to it by the user.
	 * 
	 * The Form component with ID=3 is the ResourceComponent which can store
	 * ICEResources on it. This is intended to be used for problems with mesh
	 * files which can be rendered through a VizResource publisher.
	 */
	protected void setupForm() {

		// Local Declarations
		Entry mooseAppEntry;

		// Create the Form
		form = new Form();

		// Create the DataComponent and add it
		DataComponent fileDataComponent = new DataComponent();
		fileDataComponent.setId(fileDataComponentId);
		fileDataComponent.setName("Output File Parameters");
		fileDataComponent.setDescription("Global parameters for the output "
				+ "file that ICE will create.");
		form.addComponent(fileDataComponent);

		// Create the ResourceComponent and add it
		ResourceComponent resourceComponent = new ResourceComponent();
		resourceComponent.setName("Mesh");
		resourceComponent.setId(3);
		resourceComponent.setDescription("File resources associated "
				+ "to this MOOSE Model.");
		form.addComponent(resourceComponent);

		// Add the default dummy text to the list of available apps
		mooseApps = new ArrayList<String>();

		// Get the Application preferences
		IEclipsePreferences prefs = InstanceScope.INSTANCE
				.getNode("org.eclipse.ice.item.moose");
		try {
			for (String key : prefs.keys()) {
				String app = prefs.get(key, "");
				if (!app.isEmpty()) {
					mooseApps.add(app);
				}
			}
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}

		// Only load up the Entry if some MOOSE apps were discovered.
		if (!mooseApps.isEmpty()) {
			mooseApps.add(0, "Select Application");
			// Set the default to "none", forcing the user to make a selection.
			loadedApp = mooseApps.get(0);
			// Create the MOOSE application Entry. Add all of the files if any
			// were found.
			mooseAppEntry = new Entry() {
				protected void setup() {
					allowedValues = mooseApps;
					allowedValueType = AllowedValueType.File;
					defaultValue = loadedApp;
				}
			};
		} else {
			mooseApps.add("Import Application");
			loadedApp = mooseApps.get(0);
			mooseAppEntry = new Entry() {
				protected void setup() {
					defaultValue = loadedApp;
					allowedValues = mooseApps;
					allowedValueType = AllowedValueType.File;
				}
			};
		}
		mooseAppEntry.setId(1);
		mooseAppEntry.setName("MOOSE-Based Application");
		mooseAppEntry.setDescription("The name of the MOOSE-based application "
				+ "for which you would like to create an input file.");
		// Add it to the DataComponent
		fileDataComponent.addEntry(mooseAppEntry);

		// Create the output file Entry on the form
		Entry outputFileEntry = new Entry() {
			protected void setup() {
				defaultValue = "mooseModel.i";
			}
		};
		outputFileEntry.setId(2);
		outputFileEntry.setName("Output File Name");
		outputFileEntry.setDescription("The file name of the output file, "
				+ "including extension.");
		// Add it to the DataComponent
		fileDataComponent.addEntry(outputFileEntry);

		// Create the TreeComposite on the form
		TreeComposite mooseDataTree = new TreeComposite();
		mooseDataTree.setId(mooseTreeCompositeId);
		mooseDataTree
				.setDescription("The tree of input data for this problem.");
		mooseDataTree.setName("Input Data");
		form.addComponent(mooseDataTree);

//		if (project != null) {
//			Thread thread = new Thread(new Runnable() {
//				public void run() {
//					if (!mooseApps.isEmpty()) {
//						try {
//							loadTreeContents(loadedApp);
//						} catch (IOException | CoreException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//			thread.start();
//		}
		return;
	}

	/**
	 * This operation is used to setup the name and description of the model.
	 */
	protected void setupItemInfo() {

		// Local Declarations
		String desc = "This item builds models for "
				+ "MOOSE-based applications for "
				+ "nuclear energy modeling and simulation.";

		// Describe the Item
		setName("MOOSE Model Builder");
		setDescription(desc);
		itemType = ItemType.Model;

		// Setup the action list. Remove key-value pair support.
		allowedActions.remove(taggedExportActionString);
		// Add MOOSE GetPot export action
		allowedActions.add(0, mooseProcessActionString);

		return;
	}

	/**
	 * This operation loads the contents of the TreeComposite from the MOOSE
	 * data. It is called when a MOOSE-based application has been accepted and
	 * the entries are reviewed. It will only load the Form if the project space
	 * exists - so that it can find the files - and if the Form has been
	 * previously configured. It compares the executable name passed to this
	 * function with the files in the project directory and looks for a match
	 * with the name "executuableName.yaml" that it can load. It throws an
	 * IOException if it cannot find a match.
	 * 
	 * @param mooseExecutableName
	 *            The name of the MOOSE executable whose YAML input
	 *            specification should be loaded into the Form's TreeComposite.
	 * @throws IOException
	 * @throws CoreException
	 */
	protected void loadTreeContents(String mooseExecutableName)
			throws IOException, CoreException {

		// Local Declarations
		TreeComposite mooseParentTree = (TreeComposite) form
				.getComponent(mooseTreeCompositeId), tmpParentTree;

		// Load the file from the project space if possible
		if (project != null && project.isAccessible()) {

			// Get the MOOSE folder
			IFolder mooseFolder = project.getFolder("MOOSE");

			// If the MOOSE folder doesn't exist, create it and complain
			if (!mooseFolder.exists()) {
				mooseFolder.create(true, true, null);
			}

			// Create the URI from the user's application path
			URI uri = URI.create(mooseExecutableName);

			// Create a File so we can easily get its file name
			File execFile = new File(uri);

			// Get the YAML and Syntax files file.
			IFile yamlFile = mooseFolder.getFile(execFile.getName()
					.toLowerCase() + ".yaml");
			IFile syntaxFile = mooseFolder.getFile(execFile.getName()
					.toLowerCase() + ".syntax");

			// Create the yaml and syntax exec strings
			String[] yamlCmd = {
					"/bin/sh",
					"-c",
					execFile.getAbsolutePath() + " --yaml > "
							+ yamlFile.getLocation().toOSString() };
			String[] syntaxCmd = {
					"/bin/sh",
					"-c",
					execFile.getAbsolutePath() + " --syntax > "
							+ syntaxFile.getLocation().toOSString() };

			// Create the YAML and Syntax files
			Process p1 = Runtime.getRuntime().exec(yamlCmd);
			Process p2 = Runtime.getRuntime().exec(syntaxCmd);
			try {
				int code1 = p1.waitFor();
				int code2 = p2.waitFor();

				if (code1 != 0 || code2 != 0) {
					System.out.println("ERROR CREATING YAML SYNTAX");
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			// Clean up the comments in the files
			createCleanMOOSEFile(yamlFile.getLocation().toOSString());
			createCleanMOOSEFile(syntaxFile.getLocation().toOSString());

			// Refresh the space
			refreshProjectSpace();

			// Get the IReader instance
			IReader reader = getReader();

			// Load the tree if the file exists
			if (yamlFile.exists() && syntaxFile.exists() && reader != null) {

				// Read the file and get the returned Form
				Form readerForm = reader.read(yamlFile);

				// Get the TreeComposite from the read-in Form
				tmpParentTree = (TreeComposite) readerForm
						.getComponent(mooseTreeCompositeId);

				// Copy the temporary into the parent. This is the cleanest way
				// to clear out the parent completely.
				mooseParentTree.copy(tmpParentTree);

			} else {
				// Complain
				throw new IOException("MOOSEModel Exception: Executable file, "
						+ yamlFile.getName() + " or " + syntaxFile.getName()
						+ ", not available!");
			}
		} else {
			// Complain
			throw new IOException(
					"MOOSEModel Exception: Project space not available!");
		}

		return;
	}

	/**
	 * 
	 * This operation reviews the Entries in the MOOSEModel to determine if the
	 * currently selected MOOSE app has changed. If it has, it loads up the new
	 * app's YAML tree and merges it with any existing tree data (if any).
	 * 
	 * @param preparedForm
	 *            The form prepared for review.
	 * @return The Form's status if the review was successful or not.
	 */
	protected FormStatus reviewEntries(Form preparedForm) {

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		DataComponent mooseFileComponent;

		// Get the MOOSE file information, if available
		mooseFileComponent = (DataComponent) preparedForm.getComponent(1);
		if (mooseFileComponent != null) {

			// Get the entry that stores the currently-selected MOOSE app name
			Entry mooseSpecFileEntry = mooseFileComponent
					.retrieveEntry("MOOSE-Based Application");

			// Load the MOOSE-based application if it is different than the one
			// currently loaded.
			if (mooseSpecFileEntry != null) {
				System.out.println("LOADED APP: " + loadedApp + " " + mooseSpecFileEntry.getValue());

				// Get the current value of the MOOSE app Entry and determine
				String mooseSpecValue = mooseSpecFileEntry.getValue();
				if (loadedApp == null
						|| (!mooseSpecValue.equalsIgnoreCase("none") && !loadedApp
								.equals(mooseSpecValue))) {

					// Get the app name
					loadedApp = mooseSpecFileEntry.getValue();

					// Grab a clone of the old form's TreeComposite with data
					// imported into it
					TreeComposite inputTree = (TreeComposite) preparedForm
							.getComponent(mooseTreeCompositeId).clone();

					try {
						loadTreeContents(loadedApp);
					} catch (IOException | CoreException e1) {
						e1.printStackTrace();
					}

					// Get the empty YAML TreeComposite
					TreeComposite yamlTree = (TreeComposite) form
							.getComponent(mooseTreeCompositeId);

					// Merge the input tree into the YAML spec
					
					// FIXME POSSIBLE PROBLEM IN MERGING INPUT TREE INTO YAML TREE
					// WHEN 
					
					mergeTrees(inputTree, yamlTree);

					// Save this App as a Preference
					IEclipsePreferences prefs = InstanceScope.INSTANCE
							.getNode("org.eclipse.ice.item.moose");
					try {
						prefs.put(new File(new URI(loadedApp)).getName(),
								loadedApp);
						prefs.flush();
					} catch (BackingStoreException | URISyntaxException e1) {
						e1.printStackTrace();
					}
					
				}

				// Try to find a mesh file and append it as an ICEResource
				// on the ResourceComponent
				try {
					updateMeshResource();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Update the status
				retStatus = FormStatus.ReadyToProcess;
			}
		} 

		return retStatus;
	}

	private HashMap<String, TreeComposite> previousAppTrees;
	
	/**
	 * This operation is responsible for loading a MOOSE input file into the
	 * Form's TreeComposite (id=2). It expects the input to be in the MOOSE's
	 * GetPot format. This method is used when an input file is imported as a
	 * MOOSE Model Builder Item.
	 * 
	 * @param input
	 *            The name of the input input file, including the file extension
	 *            (ex. "bison.i", "raven.i", etc.)
	 */
	@Override
	public void loadInput(String input) {

		// Local Declarations
		IFile inputFile = project.getFile(input);
		Form readForm = null;

		// Get the IReader reference
		IReader reader = getReader();

		// Make sure we have a valid IReader
		if (reader != null) {
			readForm = reader.read(inputFile);
		}

		// Make sure we have a valid Form
		if (readForm != null) {
			// Set the new MOOSE TreeComposite as the Form's
			form.removeComponent(2);
			form.addComponent(readForm.getComponent(mooseTreeCompositeId));
		}

		return;
	}

	/**
	 * This method is responsible for merging the TreeComposite of imported
	 * MOOSE data from an input file, with the corresponding YAML spec for that
	 * MOOSE application.
	 * 
	 * We will construct three HashMaps: one for the YAML tree, one for the
	 * input tree, and one for the exemplar children of the YAML tree. All Maps
	 * will be keyed on a tree's pathname relative to the root. (For the sake
	 * semantics here, any reference to "top-level" trees is referring to the
	 * trees directly beneath the root.)
	 * 
	 * Then, we will traverse the input map (this includes all children,
	 * subchildren, etc.) and copy over any applicable exemplar children from
	 * the exemplar map. Once all exemplar children are set in the input tree,
	 * then we will copy over the top-level trees (trees right below the root)
	 * from the input map into the YAML map.
	 * 
	 * @param inputTree
	 *            The TreeComposite of imported MOOSE file data.
	 * @param yamlTree
	 *            The TreeComposite loaded from the MOOSE YAML spec.
	 */
	protected void mergeTrees(TreeComposite inputTree, TreeComposite yamlTree) {

		// Local declarations
		TreeComposite child = null;

		// Store the input tree's top-level children in an ArrayList
		// for reference
		topLevelInputTrees = new ArrayList<TreeComposite>();
		inputTree.resetChildIterator();
		while ((child = inputTree.getNextChild()) != null) {
			topLevelInputTrees.add(child);
		}

		// Store the YAML tree's top-level children in an ArrayList
		// for reference
		topLevelYamlTrees = new ArrayList<TreeComposite>();
		yamlTree.resetChildIterator();
		while ((child = yamlTree.getNextChild()) != null) {
			topLevelYamlTrees.add(child);
		}

		// First, we'll begin by creating a HashMap of the YAML tree
		HashMap<String, TreeComposite> yamlMap = (HashMap<String, TreeComposite>) buildYamlMap(yamlTree);

		// Then, create a HashMap of all the input trees
		HashMap<String, TreeComposite> inputMap = (HashMap<String, TreeComposite>) buildInputMap(inputTree);

		// Lastly, now create a HashMap of the exemplar children defined in the
		// YAML spec tree
		HashMap<String, TreeComposite> exemplarMap = (HashMap<String, TreeComposite>) buildExemplarMap(yamlTree);

		// Now we loop over the input map, and look for matches in the exemplar
		// map, and copy any pertinent data over. This namely copies over
		// exemplar child lists; it also checks which parameters are
		// supposed to have a AllowedValueType.Discrete according to the
		// exemplar map and fixes the appropriately matching parameters in the
		// input map.
		setExemplarData(inputMap, exemplarMap);

		// Now walk through the input tree again, this time
		// copying over the nodes from the input tree into the YAML
		// tree
		mergeInputIntoYaml(inputMap, yamlMap, yamlTree);

		// Set all the active data nodes on the tree
		setActiveDataNodes(yamlTree);

		return;
	}

	/**
	 * This utility method is responsible for taking in a TreeComposite loaded
	 * from a MOOSE YAML spec, and constructs a Map of all its nodes, keyed on a
	 * String pathname (relative to the root).
	 * 
	 * Used exclusively by {@link #reviewEntries(Form)
	 * MOOSEModel.reviewEntries(...)}.
	 * 
	 * @param yamlTree
	 *            The TreeComposite loaded from a MOOSE YAML spec.
	 * @return A Map containing all the YAML tree's nodes, keyed on pathname.
	 */
	private Map<String, TreeComposite> buildYamlMap(TreeComposite yamlTree) {

		// Local declarations
		HashMap<String, TreeComposite> yamlMap = new HashMap<String, TreeComposite>();

		// Create an empty stack for TreeComposites
		Stack<TreeComposite> treeStack = new Stack<TreeComposite>();
		treeStack.push(null);

		// Push the top level TreeComposites from the YAML spec
		// first on top
		for (TreeComposite topLevelTree : topLevelYamlTrees) {
			treeStack.push(topLevelTree);
		}

		// Pop one of the top-level trees off to start.
		TreeComposite tree = treeStack.pop();

		while (tree != null) {
			// Put the tree in the Map, keyed on path name
			yamlMap.put(tree.getName(), tree);
			// Pop the next tree off the stack
			tree = treeStack.pop();
		}

		return yamlMap;
	}

	/**
	 * This utility method is responsible for taking a TreeComposite loaded from
	 * a MOOSE input file, and constructs a Map of all its nodes keyed on a
	 * String pathname (relative to the root).
	 * 
	 * Used exclusively by {@link #reviewEntries(Form)
	 * MOOSEModel.reviewEntries(...)}.
	 * 
	 * @param inputTree
	 *            The TreeComposite loaded from an input MOOSE file.
	 * @return A Map containing all the nodes of the input tree, keyed on
	 *         pathname.
	 */
	private Map<String, TreeComposite> buildInputMap(TreeComposite inputTree) {

		// Local declarations
		Map<String, TreeComposite> inputMap = new HashMap<String, TreeComposite>();
		ArrayList<TreeComposite> children = new ArrayList<TreeComposite>();
		TreeComposite tree = null;
		String treeName = "";
		int prevNameIndex = -1;

		// Create an empty stack for TreeComposites
		Stack<TreeComposite> treeStack = new Stack<TreeComposite>();
		treeStack.push(null);

		// Push the top level TreeComposites from the input file
		// first on top
		for (TreeComposite topLevelTree : topLevelInputTrees) {
			treeStack.push(topLevelTree);
		}

		// Pop one of the top-level trees off to start.
		tree = treeStack.pop();

		while (tree != null) {

			// Append to the tree name
			treeName += "/" + tree.getName();
			// Put the tree in the Map, keyed on path name
			inputMap.put(
					(treeName.startsWith("/") ? treeName.substring(1,
							treeName.length()) : treeName), tree);

			// Clear the children list in case there are any
			// from a previous tree
			children.clear();

			// Check if this tree has children
			if (tree.getNumberOfChildren() > 0) {

				// Construct the list of this node's children
				for (int i = 0; i < tree.getNumberOfChildren(); i++) {
					children.add(tree.getChildAtIndex(i));
				}

				// Push children to the top of the tree stack
				for (int i = (children.size() - 1); i >= 0; i--) {
					treeStack.push(children.get(i));
				}
			}

			// If the next tree in the stack is a top-level tree,
			// clear the path name
			if (topLevelInputTrees.contains(treeStack.peek())) {
				treeName = "";
			}

			// Otherwise, if the current tree didn't have children
			// to push onto the stack, remove the last "part" of the
			// path name, as we'll be going back up one level
			else if (children.isEmpty()) {
				prevNameIndex = treeName.lastIndexOf("/" + tree.getName());
				treeName = treeName.substring(0, prevNameIndex);
			}

			// Pop the next tree off the stack
			tree = treeStack.pop();
		}

		return inputMap;
	}

	/**
	 * This utility method is responsible for taking in a TreeComposite loaded
	 * from a MOOSE YAML spec, and constructs a Map of all child exemplars
	 * contained in the tree keyed on a String pathname (relative to the root).
	 * 
	 * Used exclusively by {@link #reviewEntries(Form)
	 * MOOSEModel.reviewEntries(...)}.
	 * 
	 * @param yamlTree
	 *            The TreeComposite loaded from a YAML spec.
	 * @return A Map containing all the child exemplars of the YAML tree, keyed
	 *         on pathname.
	 */
	private Map<String, TreeComposite> buildExemplarMap(TreeComposite yamlTree) {

		// Local declarations
		ArrayList<TreeComposite> childExemplars;
		HashMap<String, TreeComposite> exemplarMap = new HashMap<String, TreeComposite>();
		TreeComposite tree = null, oneUpTree = null;
		String treeName = "";
		int prevNameIndex = -1;

		// Create an empty stack for TreeComposites
		Stack<TreeComposite> treeStack = new Stack<TreeComposite>();
		treeStack.push(null);

		// Push the top level TreeComposites from the YAML ArrayList
		// first on top
		for (TreeComposite topLevelYamlTree : topLevelYamlTrees) {
			treeStack.push(topLevelYamlTree);
		}

		// Pop one of the top-level trees off to start.
		tree = treeStack.pop();
		while (tree != null) {

			// Append to the tree name
			treeName += "/" + tree.getName();
			// Put the tree in the Map, keyed on path name
			exemplarMap.put(
					(treeName.startsWith("/") ? treeName.substring(1,
							treeName.length()) : treeName), tree);

			// Push child exemplars to the top of the tree stack
			childExemplars = tree.getChildExemplars();
			for (int i = (childExemplars.size() - 1); i >= 0; i--) {
				treeStack.push(childExemplars.get(i));
			}

			// While we're here, append a blank exemplar to the tree (so the
			// user can create custom blocks)
			addBlankChildExemplar(tree);

			// If the next tree in the stack is a top-level tree,
			// clear the path name
			if (topLevelYamlTrees.contains(treeStack.peek())) {
				treeName = "";
			}

			// Otherwise, if the current tree didn't have child
			// exemplars to push onto the stack, remove the last
			// "part" of the path name, as we'll be going back up
			// one level
			else if (childExemplars.isEmpty()) {

				// Get the name of the tree one level up
				prevNameIndex = treeName.lastIndexOf("/" + tree.getName());
				treeName = treeName.substring(0, prevNameIndex);

				// Go up another level if the next tree in the stack isn't
				// a child exemplar of the current tree referenced by treeName
				oneUpTree = exemplarMap.get(treeName.substring(1));
				if (oneUpTree != null
						&& !oneUpTree.getChildExemplars().contains(
								treeStack.peek())) {
					prevNameIndex = treeName.lastIndexOf("/");
					treeName = ((prevNameIndex == 0 || prevNameIndex == -1) ? treeName
							: treeName.substring(0, prevNameIndex));
				}

			}

			// Pop the next tree off the stack
			tree = treeStack.pop();
		}

		return exemplarMap;
	}

	/**
	 * This method is responsible for append a "blank" TreeComposite to the
	 * input tree's list of child exemplars. This enables a MOOSE user to add
	 * their own, customized blocks.
	 * 
	 * @param tree
	 *            The tree to which a blank child exemplar will be appended.
	 */
	private void addBlankChildExemplar(TreeComposite tree) {

		// Create a blank child exemplar
		TreeComposite blankTree = new TreeComposite();
		blankTree.setName("BlankBlock");
		DataComponent dataNode = new DataComponent();
		blankTree.visit(dataNode);

		// Add it to the tree
		tree.addChildExemplar(blankTree);

		return;
	}

	/**
	 * This method is responsible for iterating through a HashMap of MOOSE input
	 * data, comparing it to a HashMap of child exemplars. While it does this,
	 * it copies over any exemplar children, plus converts parameters that are
	 * supposed to have a discrete set of options.
	 * 
	 * This method is "smart" enough to correctly match "wildchar" blocks (ie.
	 * Block/Subblock/*) as well as chains of wildchar blocks when an exact
	 * match to a block name cannot be found. This occurs when a user has
	 * renamed a block.
	 * 
	 * Used exclusively by {@link #reviewEntries(Form)
	 * MOOSEModel.reviewEntries(...)}.
	 * 
	 * @param inputMap
	 *            The HashMap of TreeComposites constructed from an imported
	 *            MOOSE input file, and keyed on pathname.
	 * @param exemplarMap
	 *            The HashMap of TreeComposites constructed from the child
	 *            exemplars of a loaded MOOSE YAML spec, and keyed on pathname.
	 */
	private void setExemplarData(HashMap<String, TreeComposite> inputMap,
			HashMap<String, TreeComposite> exemplarMap) {

		// Local declarations
		TreeComposite inputCur = null, exemplarCur = null;
		DataComponent inputNode = null;
		int prevNameIndex = -1;
		String parentKey = "", typeName = "";
		int generationsUp = 0;
		boolean foundExemplarMatch = false;
		TreeType treeType;

		// Iterate through the input map keys
		Set<String> inputKeys = inputMap.keySet();
		for (String key : inputKeys) {

			// Get the tree from the input map
			inputCur = inputMap.get(key);

			// Reset our counter, flag, etc. if they've been used
			generationsUp = 0;
			foundExemplarMatch = false;
			typeName = "";

			// Figure out the parent tree's key
			prevNameIndex = key.indexOf("/" + inputCur.getName());

			if (prevNameIndex == -1) {
				// If this is a top-level block, skip it
				continue;
			} else {
				// Get the name of the parent tree
				parentKey = key.substring(0, prevNameIndex);
			}

			/*
			 * Starting here, there are 4 circumstances in which we'll look for
			 * a matching block in the exemplar map. Say our current inputCur
			 * points to the node located at /Block/Subblock/foo/bar. Priority
			 * for matching, in descending order:
			 * 
			 * 1. Exact match An exact match to /Block/Subblock/foo/bar is found
			 * in the exemplar HashMap
			 * 
			 * 2. Renamed (with type) bar might have been previously named
			 * something else, but was renamed. If bar has a parameter named
			 * "type" on it, get the parameter value and check the exemplar
			 * HashMap for /Block/Subblock/foo/parameter_value
			 * 
			 * 3. Renamed (without type) bar might have been previously named
			 * something else, but was renamed. If /foo/ has an exemplar child
			 * named "*", check the exemplar HashMap for /Block/Subblock/foo/*
			 * 
			 * 4. Multi-level Renamed If no match to /Block/Subblock/foo/* is
			 * found in exemplar HashMap, begin traversing upwards replacing
			 * more wildchars in the pathname until a match is found, or the
			 * top-level of the tree is reached.
			 * 
			 * First search: //Block//Subblock//
			 *//*
				 * Second search: //Block//
				 *//*
					 * //* Third search: Reached top-level, stop, no match found
					 */

			// First, using the exemplar map, see if this tree is an exact
			// match to an exemplar child (search method #1)
			if (exemplarMap.containsKey(key)) {

				// Get a handle on the matching exemplar block
				exemplarCur = exemplarMap.get(key);

				// Simply copy the exemplar children
				inputCur.setChildExemplars(exemplarCur.getChildExemplars());

				// Check if this is an AdaptiveTreeComposite and if it is, set
				// the type
				treeType = TreeType.fromString(inputCur.getClass().toString());

				if (treeType != null
						&& treeType == TreeType.AdaptiveTreeComposite) {
					setAdaptiveType((AdaptiveTreeComposite) inputCur);
				}

				// While we're here, a bit of administrative work...
				// Fix any parameters in inputCur that are Discrete type in
				// the exemplarCur
				setDiscreteParams(exemplarCur, inputCur);

			}

			// Otherwise, there are a couple other places to search...
			// (search methods #2-4)
			else {

				// Get the data node which contains the parameters
				inputNode = (DataComponent) inputCur.getDataNodes().get(0);

				// Get the "type" parameter from the Node (if it exists)
				Entry typeEntry = inputNode.retrieveEntry("type");

				// If there's a "type" parameter, make note of its value
				if (typeEntry != null) {
					typeName = typeEntry.getValue();
				}

				// Check if this is a block that has been renamed, but has a
				// "type" parameter which indicates what the original name
				// might have been. (search method #3)
				//
				// For example, the subblocks of AuxKernel have a "type"
				// parameter which indicates what kind of subblock it is;
				// this is handy when the blocks have been renamed. Note that
				// this "type" parameter is not related to the "type" of
				// AdaptiveTreeComposites.
				if (!typeName.isEmpty()
						&& exemplarMap.containsKey(parentKey + "/" + typeName)) {

					// Get a handle on the matching exemplar block
					exemplarCur = exemplarMap.get(parentKey + "/" + typeName);

					// Simply copy the exemplar children
					inputCur.setChildExemplars(exemplarCur.getChildExemplars());

					// Check if this is an AdaptiveTreeComposite and if it is,
					// set the type
					treeType = TreeType.fromString(inputCur.getClass()
							.toString());

					if (treeType != null
							&& treeType == TreeType.AdaptiveTreeComposite) {
						setAdaptiveType((AdaptiveTreeComposite) inputCur);
					}

					// While we're here, a bit of administrative work...
					// Fix any parameters in inputCur that are Discrete type in
					// the exemplarCur
					setDiscreteParams(exemplarCur, inputCur);

				}

				// Check if the parent has an exemplar just named "*"
				else if (exemplarMap.containsKey(parentKey + "/*")) {

					// Get a handle on the matching exemplar block
					exemplarCur = exemplarMap.get(parentKey + "/*");

					// Copy the wildchar's exemplar children
					inputCur.setChildExemplars(exemplarCur.getChildExemplars());

					// While we're here, a bit of administrative work...
					// Fix any parameters in inputCur that are Discrete type in
					// the exemplarCur
					setDiscreteParams(exemplarCur, inputCur);

				}
				// Otherwise, things are going to start getting funky now.
				// This means that the parent of this tree might also be a
				// wildchar block, ie. Block/SubBlock/*/* (search method #4)
				else {

					// Begin traversing up the tree, looking at the parent's
					// parents until you find an exact match in the exemplar
					// tree
					while (!foundExemplarMatch) {

						// Get the parent's parent's name
						prevNameIndex = parentKey.lastIndexOf("/");

						// We can't go any higher than the root node, so exit
						// if we've gone up to the top-level blocks
						if (prevNameIndex == -1) {
							break;
						}

						parentKey = key.substring(0, prevNameIndex);

						// Increase our counter to indicate we've moved one
						// generation up
						generationsUp++;

						// Look for it in the exemplar map
						if (exemplarMap.containsKey(parentKey)) {
							foundExemplarMatch = true;
						}
					}

					// If a match was found, we can begin transferring data
					// from the exemplar HashMap into the input HashMap
					if (foundExemplarMatch) {

						// Construct the correct path to this tree's parent
						// depending on how many generations up it went
						String wildcharPath = parentKey;
						for (int i = 0; i < generationsUp; i++) {
							wildcharPath += "/*";
						}

						// Make sure it exists in the exemplar map
						if (exemplarMap.containsKey(wildcharPath)) {

							// Get a handle on the matching exemplar block
							exemplarCur = exemplarMap.get(wildcharPath);

							// Get the parent's exemplar list and set it as
							// this tree's exemplar list
							inputCur.setChildExemplars(exemplarCur
									.getChildExemplars());

							// While we're here, a bit of administrative work...
							// Fix any parameters in inputCur that are Discrete
							// type in the exemplarCur
							setDiscreteParams(exemplarCur, inputCur);

						}
					}
				}
			}
		}
	}

	/**
	 * This method is used by {@link #setExemplarData(HashMap,HashMap)
	 * MOOSEModel.setExemplarData(...)}. It takes in two TreeComposites, and
	 * compares their parameters. If the exemplarCur (assumed to originate from
	 * the YAML spec) has any parameters that are intended to have discrete sets
	 * of values (AllowedValueType.Discrete), the inputCur is fixed to reflect
	 * the same value type and list of AllowedValues.
	 * 
	 * @param exemplarCur
	 *            The TreeComposite from the YAML file to compare against
	 * @param inputCur
	 *            The input TreeComposite which should be changed if necessary
	 */
	private void setDiscreteParams(TreeComposite exemplarCur,
			TreeComposite inputCur) {

		// Local declarations
		DataComponent exemplarNode = null, inputNode = null;
		Entry exemplarParam = null;

		// Get the DataComponent on the exemplarCur
		exemplarNode = (DataComponent) exemplarCur.getDataNodes().get(0);

		// Check if any of its entries are discrete type
		for (int i = 0; i < exemplarNode.retrieveAllEntries().size(); i++) {

			exemplarParam = exemplarNode.retrieveAllEntries().get(i);

			if ((AllowedValueType.Discrete)
					.equals(exemplarParam.getValueType())) {

				// Get the data node on the input tree
				inputNode = (DataComponent) inputCur.getDataNodes().get(0);

				// Check if inputCur has the same parameter on it
				for (int j = 0; j < inputNode.retrieveAllEntries().size(); j++) {

					// Get the next inputParameter
					Entry inputParameter = inputNode.retrieveAllEntries()
							.get(j);

					if (inputParameter.getName()
							.equals(exemplarParam.getName())) {

						// Clone the YAML parameter
						Entry paramClone = (Entry) exemplarParam.clone();

						// Merge Data from the input parameter into it
						paramClone.setDescription(inputParameter
								.getDescription());
						paramClone.setId(inputParameter.getId());
						paramClone.setTag(inputParameter.getTag());
						paramClone.setRequired(inputParameter.isRequired());
						paramClone.setReady(inputParameter.isReady());

						// Set the value
						String oldValue = inputParameter.getValue();
						paramClone.setValue(paramClone.getAllowedValues()
								.contains(oldValue) ? oldValue : paramClone
								.getAllowedValues().get(0));

						// Set the new parameter on the data node
						inputNode.deleteEntry(inputParameter.getName());
						inputNode.addEntry(paramClone);

					}
				}
			}
		}
	}

	/**
	 * This utility method is responsible for merging the contents of a HashMap
	 * of an imported MOOSE input data TreeComposite, into a HashMap of a YAML
	 * TreeComposite, where both keys match. If a key match is not found, the
	 * block in particular is discarded.
	 * 
	 * Used exclusively by {@link #reviewEntries(Form)
	 * MOOSEModel.reviewEntries(...)}.
	 * 
	 * @param inputMap
	 *            The HashMap of the imported MOOSE data TreeComposite.
	 * @param yamlMap
	 */
	private void mergeInputIntoYaml(HashMap<String, TreeComposite> inputMap,
			HashMap<String, TreeComposite> yamlMap, TreeComposite yamlTree) {

		// Local declarations
		TreeComposite inputCur = null, yamlCur = null;
		TreeType treeType;
		String key;

		// Iterate through the the top-level trees of the input map
		for (TreeComposite tree : topLevelInputTrees) {

			// Get the key and corresponding tree
			key = tree.getName();
			inputCur = inputMap.get(key);

			// Try to find the key in the new YAML map (if not found, will just
			// be chucked out the window)
			if (yamlMap.containsKey(key)) {

				// Get the matching YAML version
				yamlCur = yamlMap.get(key);
				// Set the exemplar children of the input tree
				inputCur.setChildExemplars(yamlCur.getChildExemplars());

				// Now copy the input tree into the YAML tree (set the "copy in
				// place" flag to true so parent and sibling references are
				// retained)
				yamlCur.copy(inputCur, true);

				// Now, check if this is an AdaptiveTreeComposite
				// and if it is, set the type
				treeType = TreeType.fromString(yamlCur.getClass().toString());
				if (treeType != null
						&& treeType == TreeType.AdaptiveTreeComposite) {
					setAdaptiveType((AdaptiveTreeComposite) yamlCur);
				}

				// Lastly, check if any children, subchildren, etc. of this
				// tree also need to be converted to AdaptiveTreeComposites

				// TODO This is a quick fix for now and only checks children 1
				// level deep for now. We will have to implement a more robust
				// routine that recursively checks if any of the subchildren
				// need to be converted to AdaptiveTreeComposites
				ArrayList<TreeComposite> exemplars = yamlCur
						.getChildExemplars();
				for (TreeComposite exemplar : exemplars) {

					treeType = TreeType.fromString(exemplar.getClass()
							.toString());
					if (treeType != null
							&& treeType == TreeType.AdaptiveTreeComposite) {
						String childName = exemplar.getName();
						yamlCur.resetChildIterator();
						TreeComposite childCur = null;

						// Look for a child matching the name of the exemplar
						// that is an AdaptiveTreeComposite
						while ((childCur = yamlCur.getNextChild()) != null) {
							if (childCur.getName().equals(childName)) {

								// Clone the exemplar with all the "types" data
								// already entered
								AdaptiveTreeComposite adapChild = (AdaptiveTreeComposite) exemplar
										.clone();
								// Set the new AdaptiveTreeComposite in the
								// yamlCur's list of children
								yamlCur.removeChild(childCur);
								yamlCur.setNextChild(adapChild);
								// Copy the actual child's data in
								adapChild.copy(childCur);
								// Set the adaptive type
								setAdaptiveType(adapChild);
								// Git off mah lawn!
								break;
							}
						}
					}
				}

				// Now check if this is the Mesh block, if it is, try to convert
				// the "file" Entry to a FileEntry
				if (tree.getName().equals("Mesh")) {

					// Get the mesh file entry on the Mesh block
					DataComponent dataComp = (DataComponent) yamlCur
							.getDataNodes().get(0);
					Entry meshEntry = dataComp.retrieveEntry("file");

					// Convert the Entry to a "File" type Entry
					if (meshEntry != null) {
						convertToFileEntry(meshEntry);
					}
				}
			}
		}

		return;
	}

	/**
	 * This method attempts to add a mesh ICEResource to the Form's
	 * ResourceComponent. If it cannot get a handle on a valid ICEResource, this
	 * method does nothing.
	 * 
	 * This method assumes the MOOSE Model Builder only deals with one
	 * ICEResource at a time (the mesh), and thus, the Form's ResourceComponent
	 * will contain no more than one ICEResource at a time.
	 * 
	 * @throws IOException
	 */
	private void updateMeshResource() throws IOException {

		// Get the ResourceComponent on the Form
		ResourceComponent resourceComponent = (ResourceComponent) form
				.getComponent(resourceComponentId);
		// Try to find the mesh file Entry on the Mesh TreeComposite and
		// convert it to an ICEResource
		ICEResource mesh = createMeshResource();

		// If a valid mesh file was found and it isn't already on the
		// ResourceComponent, add it
		if (mesh != null) {
			if (!resourceComponent.contains(mesh)) {

				// We only deal with one mesh at a time. To make list change
				// events easier to interpret, either INSERT or UPDATE the mesh
				// resource.
				if (resourceComponent.isEmpty()) {
					resourceComponent.add(mesh);
				} else {
					resourceComponent.set(0, mesh);
				}

				// Update the name on the Form
				meshFileName = mesh.getName();
				System.out.println("MOOSEModel Message: Adding new mesh file "
						+ mesh.getName() + " to Resources list");
			}
		}
		// If a valid mesh file was not found, then the ResourceComponent should
		// be cleared if it has a mesh file resource.
		else if (!resourceComponent.isEmpty()) {
			resourceComponent.clear();
		}

		return;
	}

	/**
	 * This method scans the Form's TreeComposite with ID=2 for the Mesh block.
	 * If the Mesh block was found, it will return the corresponding
	 * TreeComposite. Otherwise, it will return null.
	 * 
	 * @return The Mesh block's TreeComposite, or null if it could not be found.
	 */
	private TreeComposite findMeshBlock() {

		// Set the default return value (null for not found).
		TreeComposite meshTree = null;

		// Try to find the Mesh block on the TreeComposite
		TreeComposite tree = (TreeComposite) form
				.getComponent(mooseTreeCompositeId);
		TreeComposite treeCur = null;

		// Check if the tree has children
		if (tree != null && tree.getNumberOfChildren() > 0) {

			// Iterate through the tree's children
			tree.resetChildIterator();
			while ((treeCur = tree.getNextChild()) != null) {

				// Try to find the Mesh block by name
				if ("Mesh".equals(treeCur.getName())) {
					meshTree = treeCur;
					break;
				}
			}
		}

		return meshTree;
	}

	/**
	 * This method scans the Form's TreeComposite with ID=2 for the Mesh block.
	 * If the Mesh block exists, it will attempt to find the name of a mesh file
	 * on it. If a mesh filename is found, it will create and return an
	 * ICEResource representing the mesh file.
	 * 
	 * @return An ICEResource representing the MOOSE tree's mesh file, or null
	 *         if no mesh file was found.
	 * @throws IOException
	 */
	private ICEResource createMeshResource() throws IOException {

		// Create an ICEResource
		ICEResource mesh = null;

		// Try to find the Mesh block on the TreeComposite
		if (meshBlock == null) {
			meshBlock = findMeshBlock();
		}

		if (meshBlock != null) {

			// Try to find the Entry with the mesh filename
			DataComponent meshDataComp = (DataComponent) meshBlock
					.getDataNodes().get(0);
			Entry meshEntry = meshDataComp.retrieveEntry("file");
			if (meshEntry != null) {

				// Convert the Mesh entry to a File Entry
				convertToFileEntry(meshEntry);

				// Create an ICEResource from the entry
				if (!meshEntry.getValue().isEmpty()) {
					mesh = getResource(meshEntry);
				}
			}
		}

		return mesh;
	}

	/**
	 * This method convert the mesh "file" Entry into a Entry with
	 * AllowedValueType.File and registers the Form as a listener.
	 * 
	 * @param meshEntry
	 *            The "file" Entry on the Mesh TreeComposite
	 */
	protected void convertToFileEntry(Entry meshEntry) {

		// If the "file" Entry isn't a File Entry, convert it, otherwise do
		// nothing
		if (meshEntry != null
				&& !meshEntry.getValueType().equals(AllowedValueType.File)) {

			final ArrayList<String> meshAllowedValues = new ArrayList<String>(
					Arrays.asList(meshEntry.getValue()));

			// Create an Entry with the mesh filename
			Entry fileEntry = new Entry() {
				// Setup the filenames
				public void setup() {
					this.allowedValues = meshAllowedValues;
					this.allowedValueType = AllowedValueType.File;

					return;
				}

			};

			// Copy the meshEntry information in
			fileEntry.setName(meshEntry.getName());
			fileEntry.setId(meshEntry.getId());
			fileEntry.setDescription(meshEntry.getDescription());
			fileEntry.setValue(meshAllowedValues.get(0));
			fileEntry.setReady(meshEntry.isReady());
			fileEntry.setRequired(meshEntry.isRequired());
			fileEntry.setTag(meshEntry.getTag());

			// Now copy it all back into the original Entry on the Mesh block
			meshEntry.copy(fileEntry);

			// Register the Model to listen to changes in the mesh Entry
			meshEntry.register(this);
		}

		return;
	}

	/**
	 * This method will take in a TreeComposite, traverse through all levels of
	 * child, subchild, etc. TreeComposites, and set the active data nodes on
	 * all that have activeDataNode=null. This method requires that all parent,
	 * sibling and child references be set correctly on all TreeComposites to be
	 * successful.
	 * 
	 * Used exclusively by {@link #reviewEntries(Form)
	 * MOOSEModel.reviewEntries(...)}
	 * 
	 * @param tree
	 *            The tree that will have all active data nodes set.
	 */
	private void setActiveDataNodes(TreeComposite tree) {

		// Perform a pre-order traversal of the tree. For each TreeComposite, we
		// should set an active data node if none is already set.

		// Create an empty stack. Put in a null value so we do not hit an
		// EmptyStackException and so we can use a null check in the while loop.
		Stack<TreeComposite> treeStack = new Stack<TreeComposite>();
		treeStack.push(null);
		while (tree != null) {

			// Operate on the next TreeComposite. This sets its active data node
			// if a data node exists and is not already set.
			ArrayList<Component> dataNodes = tree.getDataNodes();
			if (tree.getActiveDataNode() == null && !dataNodes.isEmpty()) {
				tree.setActiveDataNode(dataNodes.get(0));
			}

			// Add all of the current tree's children to the stack in reverse.
			for (int i = tree.getNumberOfChildren() - 1; i >= 0; i--) {
				treeStack.push(tree.getChildAtIndex(i));
			}

			// Get the next TreeComposite in the Stack.
			tree = treeStack.pop();
		}

		return;
	}

	/**
	 * This utility method is responsible for taking in an
	 * AdaptiveTreeComposite, checks the "type" Entry (found in the tree's
	 * DataNode), and setting the AdaptiveTreeComposite.type to be that (if such
	 * a type exists in its typesMap).
	 * 
	 * @param tree
	 *            The AdaptiveTreeComposite to set the type of.
	 */
	private void setAdaptiveType(AdaptiveTreeComposite tree) {

		// Local declarations
		String typeName = "";

		// Get the DataComponent on the tree
		DataComponent dataNode = (DataComponent) tree.getDataNodes().get(0);

		// Get the "type" and "file" parameters
		Entry typeEntry = dataNode.retrieveEntry("type");
		Entry fileEntry = dataNode.retrieveEntry("file");

		// Check if we're given a valid type name
		if (typeEntry != null && typeEntry.getValue() != null
				&& !typeEntry.getValue().isEmpty()) {
			typeName = typeEntry.getValue();
		}

		// Try setting the type
		if (typeName != null && !typeName.isEmpty() && tree.setType(typeName)) {

		} else if (tree.getName().equals("Mesh") && fileEntry != null
				&& fileEntry.getValue() != null
				&& !fileEntry.getValue().isEmpty()) {
			// Otherwise try setting the Mesh type "FileMesh", if appropriate
			tree.setType("FileMesh");
		}

		// Lastly, if this is the Mesh block, set a listener on its type
		if (tree.getName().equals("Mesh")) {
			tree.register(this);
		}

		return;
	}

	/**
	 * This method updates the ResourceComponet with a new VizResource if the
	 * Mesh block's "file" Entry has changed. It is also used to correctly
	 * display the "file" Entry as commented/uncommented depending on the Mesh
	 * block's currently set type. Lastly, it will listen to updates from the
	 * Mesh's active data node in case a new "file" Entry is ever manually added
	 * (in which case it will register the new Entry with the form).
	 * 
	 * @param component
	 *            The component that triggered an update
	 */
	@Override
	public void update(IUpdateable component) {

		// If the mesh file name is different, update the ResourceComponent and
		// the Mesh block type
		if (component instanceof Entry) {

			Entry fileEntry = (Entry) component;
			if (meshFileName == null || meshFileName.isEmpty()
					|| !fileEntry.getValue().equals(meshFileName)) {
				try {
					// Update the mesh resource
					updateMeshResource();

					// Also change the file type on the Mesh block to
					// "FileEntry"
					if (meshBlock == null) {
						meshBlock = (AdaptiveTreeComposite) findMeshBlock();
					}
					DataComponent meshDataComp = (DataComponent) meshBlock
							.getActiveDataNode();
					if (meshDataComp != null
							&& meshDataComp.retrieveEntry("file") != null) {
						String meshFileName = meshDataComp
								.retrieveEntry("file").getValue();
						if (!meshFileName.isEmpty()
								&& ((AdaptiveTreeComposite) meshBlock)
										.getType() == null) {
							((AdaptiveTreeComposite) meshBlock)
									.setType("FileMesh");
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// If the Mesh type has changed, evaluate if the "file" parameter
			// should
			// be commented/uncommented
		} else if (component instanceof AdaptiveTreeComposite
				&& component.getName().equals("Mesh")) {

			AdaptiveTreeComposite meshBlock = (AdaptiveTreeComposite) component;
			if (meshBlock.getActiveDataNode() != null) {

				// Get the "file" Entry/parameter
				DataComponent dataComp = (DataComponent) meshBlock
						.getActiveDataNode();
				Entry fileParam = dataComp.retrieveEntry("file");

				if (fileParam != null) {

					// Get the type and tag
					String type = meshBlock.getType();
					String fileTag = fileParam.getTag();

					// Set the tag and required flag correctly
					if (!type.equals("FileMesh")
							&& (fileTag.equalsIgnoreCase("true"))
							|| fileTag.equalsIgnoreCase("new_parameter")) {
						// Comment out the "file" parameter
						fileParam.setTag("false");
						fileParam.setRequired(false);
					} else if (type.equals("FileMesh")
							&& fileTag.equalsIgnoreCase("false")) {
						// Enable the "file" parameter
						fileParam.setTag("true");
						fileParam.setRequired(true);
					} else if (!type.equals("FileMesh")) {
						fileParam.setRequired(false);
					} else {
						fileParam.setRequired(true);
					}
				}
			}

			// If a "file" Entry is added, make sure everything is hooked up
			// correctly to the mesh
		} else if (component instanceof DataComponent) {

			DataComponent dataComp = (DataComponent) component;
			if (dataComp.retrieveEntry("file") != null) {

				Entry fileEntry = dataComp.retrieveEntry("file");
				if (!fileEntry.getValue().isEmpty()) {

					// Re-register the fileEntry in case this "file" parameter
					// parameter was created after the tree was set up
					fileEntry.unregister(this);
					fileEntry.register(this);

					// Try updating the Mesh block so the file parameter will
					// be set correctly depending on the current block type
					if (meshBlock == null) {
						meshBlock = findMeshBlock();
					}
					if (meshBlock != null) {
						update(meshBlock);
					}
				}
			}
		}

		return;
	}

	/**
	 * This method is intended to take a filePath corresponding to a MOOSE YAML
	 * or action syntax file, and remove any extraneous header or footer lines
	 * that aren't valid syntax. If any lines from the file were removed, it
	 * re-writes the file. If no changes were made (no header/footer to remove),
	 * it does nothing.
	 * 
	 * @param filePath
	 *            The filepath to the YAML or action syntax file.
	 * @throws IOException
	 * @throws CoreException
	 */
	private void createCleanMOOSEFile(String filePath) throws IOException,
			CoreException {

		// Local declarations
		String fileExt, fileType = null;
		boolean hasHeader = false, hasFooter = false;
		int headerLine = 0, footerLine = 0;
		String separator = System.getProperty("file.separator");
		ArrayList<String> fileLines;

		// Check if the MOOSE folder exists; create it if it doesn't
		IFolder mooseFolder = project.getFolder("MOOSE");

		// If the MOOSE folder doesn't exist, create it
		if (!mooseFolder.exists()) {
			mooseFolder.create(true, true, null);
		}

		// Define where the "clean" MOOSE file will be written
		fileExt = filePath.substring(filePath.lastIndexOf("."));

		if (".yaml".equals(fileExt)) {
			fileType = "YAML";
		} else if (".syntax".equals(fileExt)) {
			fileType = "SYNTAX";
		} else {
			System.out.println("MOOSEFileHandler message: File does not have "
					+ "vaid file extension. Must be .yaml or .syntax but is "
					+ fileExt);
		}

		// Read in the MOOSE file into an ArrayList of Strings
		java.nio.file.Path readPath = Paths.get(filePath);
		fileLines = (ArrayList<String>) Files.readAllLines(readPath,
				Charset.defaultCharset());

		// Define what the header/footer lines look like
		String header = "**START " + fileType + " DATA**";
		String footer = "**END " + fileType + " DATA**";

		// Determine if there is a header and/or footer
		hasHeader = fileLines.contains(header);
		hasFooter = fileLines.contains(footer);

		// Cut off the footer, if there is one
		if (hasFooter) {
			
			// Record the line number of the footer
			footerLine = fileLines.indexOf(footer);
			deleteLines(filePath, footerLine, fileLines.size() - footerLine + 1);
		}
		
		// Cut off the header, if there is one
		if (hasHeader) {

			// Record the line number
			headerLine = fileLines.indexOf(header);
			deleteLines(filePath, 1, headerLine+1);
			
		}

		return;
	}

	/**
	 * A private utility used for deleting a range of lines in a text file.
	 * 
	 * @param filename
	 * @param startline
	 * @param numlines
	 */
	private void deleteLines(String filename, int startline, int numlines) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			// String buffer to store contents of the file
			StringBuffer sb = new StringBuffer("");

			// Keep track of the line number
			int linenumber = 1;
			String line;

			while ((line = br.readLine()) != null) {
				// Store each valid line in the string buffer
				if (linenumber < startline
						|| linenumber >= startline + numlines) {
					sb.append(line + "\n");
				}
				linenumber++;
			}
			if (startline + numlines > linenumber) {
				System.out.println("End of file reached.");
			}
			br.close();

			FileWriter fw = new FileWriter(new File(filename));
			// Write entire string buffer into the file
			fw.write(sb.toString());
			fw.close();
		} catch (Exception e) {
			System.out.println("Something went horribly wrong: "
					+ e.getMessage());
		}
	}

	/**
	 * Return the IO Type string. This method is to be overriden by subclasses
	 * to indicate which IReader and IWriter the Item subclass needs to use.
	 * 
	 * @return The type of IO Reader/Writer to use (moose).
	 */
	protected String getIOType() {
		return "moose";
	}

	/**
	 * Enumeration to discern if a tree object is an AdaptiveTreeComposite, or
	 * just a TreeComposite. This is an alternative to using instanceof.
	 * 
	 * @author Anna Wojtowicz
	 */
	public enum TreeType {

		// The Strings returned by getClass().toString()
		AdaptiveTreeComposite(
				"class org.eclipse.ice.datastructures.form.AdaptiveTreeComposite"), TreeComposite(
				"class org.eclipse.ice.datastructures.form.TreeComposite");

		private final String type;

		/**
		 * Constructor.
		 */
		private TreeType(String toClassString) {
			this.type = toClassString;
			return;
		}

		/**
		 * This method performs a lookup to return a tree type based on an input
		 * string, assumed to be equal to getClass().toString().
		 * 
		 * @param className
		 *            The String returned by getClass().toString()
		 * @return The type associated to the className parameter, or null if
		 *         none matches.
		 */
		public static TreeType fromString(String className) {

			// Default to null
			TreeType type = null;

			// Try to find a match to the className string
			if (AdaptiveTreeComposite.type.equals(className)) {
				type = AdaptiveTreeComposite;
			} else if (TreeComposite.type.equals(className)) {
				type = TreeComposite;
			}

			return type;
		}
	};
}
