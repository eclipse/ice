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
package org.eclipse.ice.persistence.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer;
import org.eclipse.ice.core.iCore.IPersistenceProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.Material;

/**
 * This class implements the IPersistenceProvider interface using the native XML
 * marshalling routines available on the Item. It stores all of the Items in an
 * Eclipse project and uses the ResourcePlugin to manage that space.
 * 
 * It stores items in the workspace in a project called "itemDB" and uses
 * <itemName>_<itemId>.xml for the file names. White space in the item name is
 * replaced with underscores.
 * 
 * All of the operations performed by this class except for those that load
 * Items are handled on a separate, non-blocking thread. Loading operations are
 * blocking.
 * 
 * Items that are loaded by the provider are not constructed with a project.
 * 
 * This provider should always be started AFTER all of the Items are registered
 * with it because registering Items while it is running would require stopping
 * the thread and recreating the JAXB context. That is easiest enough to do, but
 * it won't be implemented here until we get a feature request for it.
 * 
 * The provider also implements the IReader and IWriter interfaces and is
 * registered with the framework as an XML IO Service. It does not support the
 * find and replace operations from the IReader and IWriter interfaces and it
 * will throw an exception if either is called. In the case of find() it returns
 * null.
 * 
 * @author Jay Jay Billings
 * 
 */
public class XMLPersistenceProvider implements IPersistenceProvider, Runnable,
		IReader, IWriter {

	/**
	 * An atomic boolean used to manage the event loop. It is set to true when
	 * start is called and false when stop is called.
	 */
	AtomicBoolean runFlag = new AtomicBoolean();

	/**
	 * This is a private class used to store queue events. The Item or its id
	 * are stored along with one of the words "persist" or "delete" to denote
	 * which task should be performed for the given item. Alternatively, if the
	 * task says "write" the file attribute of the task is used to write the
	 * Form to the specified IFile to satisfy the IWriter interface.
	 * 
	 * @author Jay Jay Billings
	 * 
	 */
	private static class QueuedTask {
		/**
		 * The item that should be processed.
		 */
		public Item item;
		/**
		 * The task that should be performed; one of "persist," "delete," or
		 * "write."
		 */
		public String task;
		/**
		 * The Form that should be persisted as part of the IWriter interface.
		 */
		public Form form;
		/**
		 * The file to which the Form should be written.
		 */
		public IFile file;
	}

	/**
	 * The blocking queue that holds all of the persistence tasks that are left
	 * to be processed.
	 */
	ArrayBlockingQueue<QueuedTask> taskQueue = new ArrayBlockingQueue<QueuedTask>(
			1024);

	/**
	 * A private thread on which the event loop is run. The runnable for this
	 * thread is the current instance of this class. All work is processed on
	 * this thread.
	 */
	private Thread eventLoop;

	/**
	 * The Eclipse project used by the provider.
	 */
	private IProject project;

	/**
	 * A list of Items constructed from the ItemBuilders that were registered
	 * with the persistence provider by calling addBuilder(). These Items are
	 * used to create the list of classes passed to the JAXBContext.
	 */
	private ArrayList<Item> referenceItems = new ArrayList<Item>();

	/**
	 * A map of the ids of the Items that have persisted as its keys and the
	 * file names of those Items as values. This is used when loading Items off
	 * of disk and updated when Items are persisted.
	 */
	private Hashtable<Integer, String> itemIdMap = new Hashtable<Integer, String>();

	/**
	 * The JAXBContext that is used to create (un)marshalling tools for the XML
	 * files.
	 */
	JAXBContext context;

	/**
	 * Empty default constructor. No work to do.
	 */
	public XMLPersistenceProvider() {
	}

	/**
	 * An alternative constructor that allows the project space to be set for
	 * testing.
	 * 
	 * @param projectSpace
	 *            The project space that should be used instead of the default.
	 */
	public XMLPersistenceProvider(IProject projectSpace) {
		project = projectSpace;
	}

	/**
	 * This operation loads the Item id map with the contents of the project
	 * space.
	 */
	private void loadItemIdMap() {

		// Local Declarations
		int id;
		ArrayList<String> names = new ArrayList<String>();
		IResource[] members;

		try {
			// Get the list of files in the project space
			members = project.members();
			for (IResource resource : members) {
				// Only add the resources that are xml files with the format
				// that we expect. This uses a regular expression that checks
				// for <itemName>_<itemId>.xml.
				if (resource.getType() == IResource.FILE
						&& resource.getName().matches(
								"^[a-zA-Z0-9_\\-]*_\\d+\\.xml$")) {
					names.add(resource.getName());
				}
			}

			// Get the ids of all of the items from the file names and map them
			// to the names.
			for (String name : names) {
				System.out.println("XMLPersistenceProvider Message: "
						+ "Found persisted Item at " + name);
				// Remove the file extension
				String[] nameParts = name.split("\\.");
				String nameMinusExt = nameParts[0];
				// Get the id from the end
				String[] nameMinusExtParts = nameMinusExt.split("_");
				String idString = nameMinusExtParts[nameMinusExtParts.length - 1];
				id = Integer.valueOf(idString);
				// Put the info in the map
				itemIdMap.put(id, name);
			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	/**
	 * This operation is responsible for creating the project space used by the
	 * XMLPersistenceProvider.
	 */
	private void createProjectSpace() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String projectName = "itemDB";
		System.getProperty("file.separator");

		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(projectName);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
				// Refresh the project in case users manipulated files.
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
		}
	}

	/**
	 * This operation creates the JAXBContext used by the provider to create XML
	 * (un)marshallers.
	 * 
	 * @throws JAXBException
	 *             An exception indicating that the JAXB Context could not be
	 *             created.
	 */
	private void createJAXBContext() throws JAXBException {
		// Make an array to store the class list of registered Items
		ArrayList<Class> classList = new ArrayList<Class>();
		Class[] classArray = {};
		// Create the list of classes for the JAXBContext
		for (Item refItem : referenceItems) {
			classList.add(refItem.getClass());
		}
		// We need to explicitly add some classes to the list so that they will
		// be handled appropriately. For example, Material does not have a
		// component so it will not get added to the class list when the Form is
		// read from all of the Items above.
		classList.add(Material.class);
		// Create new JAXB class context and unmarshaller
		context = JAXBContext.newInstance(classList.toArray(classArray));
	}

	/**
	 * This operation is called to start the XMLPersistenceProvider by the OSGi
	 * Declarative Services engine. It sets up the project space and starts the
	 * event loop.
	 * 
	 * @throws JAXBException
	 *             An exception indicating that the JAXB Context could not be
	 *             created.
	 */
	public void start() throws JAXBException {

		// Debug information
		System.out.println("XMLPersistenceProvider Message: "
				+ "Starting Provider!");

		// Setup the project if needed. Setup may not be needed if the
		// alternative constructor was used.
		if (project == null) {
			createProjectSpace();
		}

		// Create the JAXB context
		createJAXBContext();

		// Get the names and ids for all of the Items that have been persisted.
		loadItemIdMap();

		// Start the event loop
		runFlag.set(true);
		eventLoop = new Thread(this);
		eventLoop.start();

		// Debug information
		System.out.println("XMLPersistenceProvider Message: "
				+ "Provider started.");

		return;
	}

	/**
	 * This operation is called to stop the XMLPersistenceProvider by the OSGi
	 * Declarative Services engine. It throws the run flag to shut down the
	 * event loop and waits until it has processed all of its request or for one
	 * minute, whichever is sooner.
	 */
	public void stop() {

		long counter = 0, maxPollCount = 60;

		// Debug information
		System.out.println("XMLPersistenceProvider Message: "
				+ "Stopping Provider!");

		// Shut down the thread if it was started
		if (eventLoop != null) {
			// Thrown the flag to shut down the thread
			runFlag.set(false);
			// Watch the thread until it shuts down or for one minute,
			// whichever is sooner.
			while (counter < maxPollCount) {
				if (eventLoop.isAlive()) {
					Thread.currentThread();
					try {
						// Sleep for a second if the event loop is still busy
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// Complain if something interrupts naptime!
						e.printStackTrace();
					}
					// Increment the counter
					counter++;
				} else {
					// Just quit because the event loop has already died.
					break;
				}
			}
		}

		// Debug information
		System.out.println("XMLPersistenceProvider Message: "
				+ "Provider stopped.");

		return;
	}

	/**
	 * This operation registers an ItemBuilder with the persistence provider.
	 * 
	 * Every builder registered with the persistence provider is called at least
	 * once so that class information can be stored about the Items they create.
	 * 
	 * @param builder
	 */
	public void addBuilder(ItemBuilder builder) {

		System.out.println("XMLPersistenceProvider Message: " + "Item "
				+ builder.getItemName() + " registered.");

		// Build an Item from this builder and store it so that we can get its
		// class info later.
		Item item = builder.build(null);
		if (item != null) {
			referenceItems.add(item);
		}

		return;

	}

	/**
	 * This operation returns an output stream containing the XML representation
	 * of an Item stored in a QueuedTask.
	 * 
	 * @param obj
	 *            the object to write to the stream
	 * @return the output stream containing the Item as XML
	 */
	private ByteArrayOutputStream createXMLStream(Object obj) {
		// Get the XML
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// Create the marshaller and write the item
		Marshaller marshaller;
		try {
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(obj, outputStream);
		} catch (JAXBException e) {
			// Complain
			e.printStackTrace();
			System.out.println("XMLPersistenceProvider Message: "
					+ "Failed to execute persistence task for " + obj);
		}
		return outputStream;
	}

	/**
	 * This operation writes the specified object to the file in XML.
	 * 
	 * @param obj
	 *            The object to be written
	 * @param file
	 *            The file to where it should be written
	 */
	private void writeFile(Object obj, IFile file) {
		// Create an output stream containing the XML.
		ByteArrayOutputStream outputStream = createXMLStream(obj);
		// Convert it to an input stream so it can be pushed to file
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());
		try {
			// Update the output file if it already exists
			if (file.exists()) {
				file.setContents(inputStream, IResource.FORCE, null);
			} else {
				// Or create it from scratch
				file.create(inputStream, IResource.FORCE, null);
			}
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
		}
		return;
	}

	/**
	 * A utility operation for processing tasks in the event loop.
	 * 
	 * @param currentTask
	 *            The current task to be processed.
	 */
	private void processTask(QueuedTask currentTask) {

		// Local Declarations
		String name = null;
		IFile file = null;

		try {
			// Handle the task if it is available
			if (currentTask != null) {
				// Get the file name if this is a persist or delete
				if ("persist".equals(currentTask.task)
						|| "delete".equals(currentTask.task)) {
					// Setup the file name
					name = currentTask.item.getName().replaceAll("\\s+", "_")
							+ "_" + currentTask.item.getId() + ".xml";
					// Get the file in the project
					file = project.getFile(name);
				}
				// Process persists
				if ("persist".equals(currentTask.task)
						&& !(currentTask.item instanceof ReactorAnalyzer)) {
					// Send the Item off to be written to the file
					writeFile(currentTask.item, file);
					// Update the item id map
					itemIdMap.put(currentTask.item.getId(), file.getName());
				} else if ("delete".equals(currentTask.task) && file.exists()) {
					// Handle deletes
					file.delete(true, null);
					// Update the item id map
					itemIdMap.remove(currentTask.item.getId());
				} else if ("write".equals(currentTask.task)) {
					// Deal with simple Form write requests from the IWriter
					// interface.
					writeFile(currentTask.form, currentTask.file);
				}
			} else {
				// Otherwise sleep for a bit
				Thread.currentThread();
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// Complain
			e.printStackTrace();
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
		}

		return;
	}

	/**
	 * The event loop. When Items are persisted, loaded, deleted or updated,
	 * they are added to the queue and those operations are processed on this
	 * thread.
	 */
	public void run() {

		// While the provider is set to run, just process tasks from the
		// queue
		while (runFlag.get()) {
			try {
				// Grab the next task
				QueuedTask currentTask = taskQueue.poll(2, TimeUnit.SECONDS);
				// Process it
				processTask(currentTask);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * A private utility operation that submits a persistence task to the queue.
	 * 
	 * @param item
	 *            The Item that is part of the persistence task.
	 * @param id
	 *            The id of the Item to load
	 * @param taskName
	 *            The name of the persistence task to perform. Same as
	 *            QueuedTask.task.
	 * @return True if the task was submitted, false if there was some exception
	 *         or the Item was null.
	 */
	private boolean submitTask(Item item, String taskName, Form form, IFile file) {

		// Local Declarations
		boolean retVal = true;
		QueuedTask task = new QueuedTask();

		// Create and submit the task if the Item is good
		if (item != null) {
			// Setup the task
			task.item = item;
			task.task = taskName;
			// Submit the task
			try {
				taskQueue.add(task);
			} catch (Exception exception) {
				// Complain
				exception.printStackTrace();
				retVal = false;
			}
		} else if (form != null && file != null) {
			// Otherwise submit the task if the Form and IFile are good (for the
			// IWriter interface). Setup the task.
			task.task = taskName;
			task.form = form;
			task.file = file;
			// Submit the task
			try {
				taskQueue.add(task);
			} catch (Exception exception) {
				// Complain
				exception.printStackTrace();
				retVal = false;
			}
		} else {
			// The submission was invalid
			retVal = false;
		}
		return retVal;
	}

	/**
	 * This operation persists the Item. It logs the persist order with the
	 * queue and only returns false if an exception is thrown.
	 * 
	 * @param The
	 *            Item to persist.
	 * @return true if the Item was queued for persistence, false if an
	 *         exception was caught.
	 */
	public boolean persistItem(Item item) {
		// Submit the job
		return submitTask(item, "persist", null, null);
	}

	/**
	 * This operation loads the Item. It logs the load order with the queue and
	 * only returns false if an exception is thrown.
	 * 
	 * @param itemID
	 *            id of the Item to load.
	 * @return The loaded Item or null if it could not be loaded.
	 */
	public Item loadItem(int itemID) {

		// Local Declarations
		Item item = null;
		String fileName;

		try {
			// If the map contains the item, load it.
			fileName = itemIdMap.get(itemID);
			if (fileName != null) {
				// Create the unmarshaller and load the item
				Unmarshaller unmarshaller = context.createUnmarshaller();
				item = (Item) unmarshaller.unmarshal(project.getFile(fileName)
						.getContents());
			}
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
			// Null out the Item so that it can't be returned uninitialized
			item = null;
		} catch (JAXBException e) {
			// Complain
			e.printStackTrace();
			// Null out the Item so that it can't be returned uninitialized
			item = null;
		}

		return item;
	}

	/**
	 * This operation deletes the Item. It logs the delete order with the queue
	 * and only returns false if an exception is thrown.
	 * 
	 * @param item
	 *            Item that should be deleted.
	 * @return true if the Item was queued for deletion, false if an exception
	 *         was caught.
	 */
	public boolean deleteItem(Item item) {
		// Submit the job
		return submitTask(item, "delete", null, null);
	}

	/**
	 * This operation updates the Item. It logs the update order with the queue
	 * and only returns false if an exception is thrown.
	 * 
	 * This operation is identical to calling persistItem() because there is no
	 * way to do an efficient merge of the XML files.
	 * 
	 * @param item
	 *            Item to update.
	 * @return true if the Item was queued for an update, false if an exception
	 *         was caught.
	 */
	public boolean updateItem(Item item) {
		// Submit the job
		return submitTask(item, "persist", null, null);
	}

	/**
	 * This operation loads all of the Items that this provider can find.
	 * 
	 * @return A list of all of the Items that this persistence provider was
	 *         able to load from the project space.
	 */
	public ArrayList<Item> loadItems() {

		// Local Declarations
		ArrayList<Item> items = new ArrayList<Item>();
		Set<Integer> keys = itemIdMap.keySet();
		Item item = null;

		// Load them all
		for (int id : keys) {
			item = loadItem(id);
			items.add(item);
		}

		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IWriter#write(org.eclipse.ice.datastructures
	 * .form.Form, java.net.URI)
	 */
	@Override
	public void write(Form formToWrite, IFile file) {
		// Submit the job
		submitTask(null, "write", formToWrite, file);
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IWriter#replace(java.net.URI,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void replace(IFile file, String regex, String value) {
		try {
			throw new OperationNotSupportedException(
					"XMLPersistenceProvider Error: "
							+ "IWriter.replace() is not supported.");
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IWriter#getWriterType()
	 */
	@Override
	public String getWriterType() {
		return "xml";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IReader#read(java.net.URI)
	 */
	@Override
	public Form read(IFile file) {

		Form form = null;

		try {
			// Create the marshaller
			Unmarshaller unmarshaller = context.createUnmarshaller();
			// Grab the form
			form = (Form) unmarshaller.unmarshal(file.getContents());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IReader#findAll(java.net.URI,
	 * java.lang.String)
	 */
	@Override
	public ArrayList<Entry> findAll(IFile file, String regex) {
		try {
			throw new OperationNotSupportedException(
					"XMLPersistenceProvider Error: "
							+ "IReader.findAll() is not supported.");
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IReader#getReaderType()
	 */
	@Override
	public String getReaderType() {
		return "xml";
	}

}
