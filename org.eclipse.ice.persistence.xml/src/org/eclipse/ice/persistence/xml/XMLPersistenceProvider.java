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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.jaxbclassprovider.IJAXBClassProvider;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.persistence.IPersistenceProvider;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the IPersistenceProvider interface using the native XML
 * marshalling routines available on the Item. It stores all of the Items in an
 * Eclipse project and uses the ResourcePlugin to manage that space. Note: The
 * default project must be set by a client! Clients should always make sure that
 * getDefaultProject() does not return null when using this class.
 *
 * It stores items in the workspace in the default project provided by clients.
 * It uses <itemName>_<itemId>.xml for the file names. White space in the item
 * name is replaced with underscores. Items may be stored and loaded from other
 * projects by using the appropriate call.
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
public class XMLPersistenceProvider implements IPersistenceProvider, Runnable, IReader, IWriter {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(XMLPersistenceProvider.class);

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
	ArrayBlockingQueue<QueuedTask> taskQueue = new ArrayBlockingQueue<QueuedTask>(1024);

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
	 * The list of IJAXBClassProviders to be used in the construction of the
	 * JAXBContext.
	 */
	private List<IJAXBClassProvider> classProviders;

	/**
	 * The JAXBContext that is used to create (un)marshalling tools for the XML
	 * files.
	 */
	JAXBContext context;

	/**
	 * Default constructor.
	 */
	public XMLPersistenceProvider() {
		classProviders = new ArrayList<IJAXBClassProvider>();
	}

	/**
	 * An alternative constructor that allows the project space to be set for
	 * testing.
	 *
	 * @param projectSpace
	 *            The project space that should be used instead of the default.
	 */
	public XMLPersistenceProvider(IProject projectSpace) {
		classProviders = new ArrayList<IJAXBClassProvider>();
		project = projectSpace;
	}

	/**
	 * This operation registers an IJAXBClassProvider with the persistence
	 * provider.
	 *
	 * @param provider
	 *            The IJAXBClassProvider to be used in creation of the
	 *            JAXBContext.
	 */
	public void registerClassProvider(IJAXBClassProvider provider) {
		if (provider != null) {
			logger.info("Adding Class Provider " + provider.getProviderName());
			classProviders.add(provider);
		}

		return;
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
						&& resource.getName().matches("^[a-zA-Z0-9_\\-]*_\\d+\\.xml$")) {
					names.add(resource.getName());
				}
			}

			// Get the ids of all of the items from the file names and map them
			// to the names.
			for (String name : names) {
				logger.info("XMLPersistenceProvider Message: " + "Found persisted Item at " + name);
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
			logger.error(getClass().getName() + " Exception!", e);
		}

		return;
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
		// classList.add(Material.class);

		// Now add all Classes provided by the registered
		// IJAXBClassProviders if they are available.
		if (classProviders.size() > 0) {
			for (IJAXBClassProvider provider : classProviders) {
				classList.addAll(provider.getClasses());
			}
		}

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
		logger.info("XMLPersistenceProvider Message: " + "Starting Provider!");

		// Create the JAXB context
		createJAXBContext();

		// Start the event loop
		runFlag.set(true);
		eventLoop = new Thread(this);
		eventLoop.start();

		// Debug information
		logger.info("XMLPersistenceProvider Message: " + "Provider started.");

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
		logger.info("XMLPersistenceProvider Message: " + "Stopping Provider!");

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
						logger.error(getClass().getName() + " Exception!", e);
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
		logger.info("XMLPersistenceProvider Message: " + "Provider stopped.");

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

		logger.info("XMLPersistenceProvider Message: " + "Item " + builder.getItemName() + " registered.");

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
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(obj, outputStream);
		} catch (JAXBException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
			logger.info("XMLPersistenceProvider Message: " + "Failed to execute persistence task for " + obj);
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
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
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
			logger.error(getClass().getName() + " Exception!", e);
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
				if ("persist".equals(currentTask.task) || "delete".equals(currentTask.task)) {
					// Setup the file name
					name = currentTask.item.getName().replaceAll("\\s+", "_")
							/* + "_" + currentTask.item.getId() */ + ".xml";
					// Get the file from the project registered with the Item.
					// This may change depending on whether or not this Item was
					// created in the default project.
					file = currentTask.item.getProject().getFile(name);
					System.out.println("Persist: Getting file = " + file.getName());
				}
				// Process persists
				if ("persist".equals(currentTask.task) && !(currentTask.item instanceof ReactorAnalyzer)) {
					// Send the Item off to be written to the file
					writeFile(currentTask.item, file);
					// Update the item id map
					itemIdMap.put(currentTask.item.getId(), file.getName());
				} else if ("delete".equals(currentTask.task)) {
					// Handle deletes
					// Make sure it exists, the platform may have deleted it
					// first
					if (file.exists()) {
						file.delete(true, null);
					}
					// Update the item id map
					itemIdMap.remove(currentTask.item.getId());
				} else if ("write".equals(currentTask.task)) {
					// Deal with simple Form write requests from the IWriter
					// interface.
					writeFile(currentTask.form, currentTask.file);
				} else if ("rename".equals(currentTask.task)) {
					String oldFile = itemIdMap.get(currentTask.item.getId());
					if (oldFile != null) {
						itemIdMap.put(currentTask.item.getId(), currentTask.file.getName());
						
						// Sleep for a bit to let the platform delete if it wants
						Thread.currentThread();
						Thread.sleep(1000);
						
						IProject project = currentTask.item.getProject();
						IFile oldFileHandle = project.getFile(oldFile);
						if (oldFileHandle.exists()) {
							oldFileHandle.move(currentTask.file.getProjectRelativePath(), true, null);
						}
						
					}

				}
			} else {
				// Otherwise sleep for a bit
				Thread.currentThread();
				Thread.sleep(1000);
			}
		} catch (InterruptedException | CoreException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
		} 

		return;
	}

	/**
	 * The event loop. When Items are persisted, loaded, deleted or updated,
	 * they are added to the queue and those operations are processed on this
	 * thread.
	 */
	@Override
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
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.persistence.IPersistenceProvider#renameItem(org.
	 * eclipse.ice.item.Item, java.lang.String)
	 */
	@Override
	public void renameItem(Item item, String newName) {
		IFile newFile = item.getProject().getFile(newName+".xml");
		submitTask(item, "rename", item.getForm(), newFile);
		return;
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

			if (file != null) {
				task.file = file;
			}
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.core.iCore.IPersistenceProvider#persistItem(org.eclipse.
	 * ice.item.Item)
	 */
	@Override
	public boolean persistItem(Item item) {
		// Submit the job
		return submitTask(item, "persist", null, null);
	}

	/**
	 * This operation loads an Item from an IFile resource.
	 *
	 * @param file
	 *            The IFile that should be loaded as an Item from XML.
	 * @return the Item
	 */
	public Item loadItem(IFile file) {

		Item item = null;

		try {
			// Create the unmarshaller and load the item
			Unmarshaller unmarshaller = context.createUnmarshaller();
			item = (Item) unmarshaller.unmarshal(file.getContents());
		} catch (CoreException | JAXBException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
			// Null out the Item so that it can't be returned uninitialized
			item = null;
		}

		return item;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.core.iCore.IPersistenceProvider#loadItem(int)
	 */
	@Override
	public Item loadItem(int itemID) {

		// Get the file name from the map that stores the ids of the items in
		// the default project.
		String fileName = itemIdMap.get(itemID);

		// Delegate the load to the IFile version of this call
		return loadItem(project.getFile(fileName));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.core.iCore.IPersistenceProvider#loadItem(org.eclipse.core
	 * .resources.IResource)
	 */
	@Override
	public Item loadItem(IResource itemResource) {
		// If the IResource is an IFile, load it and otherwise return null.
		return (itemResource instanceof IFile) ? loadItem((IFile) itemResource) : null;
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
	@Override
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
	@Override
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
	@Override
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
	 * @see org.eclipse.ice.io.serializable.IWriter#write(org.eclipse.ice.
	 * datastructures .form.Form, java.net.URI)
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
					"XMLPersistenceProvider Error: " + "IWriter.replace() is not supported.");
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			logger.error(getClass().getName() + " Exception!", e);
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
			logger.error(getClass().getName() + " Exception!", e);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			logger.error(getClass().getName() + " Exception!", e);
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
					"XMLPersistenceProvider Error: " + "IReader.findAll() is not supported.");
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			logger.error(getClass().getName() + " Exception!", e);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.core.iCore.IPersistenceProvider#setDefaultProject(org.
	 * eclipse.core.resources.IProject)
	 */
	@Override
	public void setDefaultProject(IProject project) {
		this.project = project;

		// Get the names and ids for all of the Items that have been persisted.
		loadItemIdMap();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.core.iCore.IPersistenceProvider#getDefaultProject()
	 */
	@Override
	public IProject getDefaultProject() {
		return project;
	}

}
