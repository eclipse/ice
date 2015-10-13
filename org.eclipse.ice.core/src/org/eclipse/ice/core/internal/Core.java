/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.core.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.core.iCore.ICore;
import org.eclipse.ice.core.iCore.IPersistenceProvider;
import org.eclipse.ice.core.internal.itemmanager.ItemManager;
import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.SerializedItemBuilder;
import org.eclipse.ice.item.messaging.Message;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * The Core class is responsible for implementing and/or managing all of the
 * data and workflow management capabilities of ICE. It implements the ICore
 * interface and provides additional administration operations not found on
 * ICore. Most of the operations performed for managing Items are forwarded
 * directly to the ItemManager class.
 *
 * @author Jay Jay Billings
 */
@ApplicationPath("/ice")
public class Core extends Application implements ICore, BundleActivator {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Core.class);

	/**
	 * Reference to the ItemManager responsible for creating, querying, and
	 * updating available Items.
	 */
	private ItemManager itemManager;

	/**
	 * The component context for the ICE Core OSGi component.
	 *
	 */
	private BundleContext bundleContext;

	/**
	 * The master table of IProject Eclipse projects, keyed by username.
	 */
	private Hashtable<String, IProject> projectTable;

	/**
	 * This is the default database where Items are stored. It is passed to both
	 * the ItemManager and the IPersistenceProvider.
	 */
	private IProject itemDBProject;

	/**
	 * The ServiceReference used to track the HTTP service.
	 */
	private ServiceReference<HttpService> httpServiceRef;

	/**
	 * The OSGi HTTP Service used by the Core to publish itself.
	 */
	private HttpService httpService;

	/**
	 * The string identifying the Item Builder extension point.
	 */
	public static final String builderID = "org.eclipse.ice.item.itemBuilder";

	/**
	 * The string identifying the persistence provider extension point.
	 */
	public static final String providerID = "org.eclipse.ice.core.persistenceProvider";

	/**
	 * The persistence provided by the osgi. This piece is set by the
	 * setPersistenceProvider method. This piece is passed to the ItemManager to
	 * be used for persisting Items.
	 *
	 */
	private IPersistenceProvider provider;

	/**
	 * An AtomicBoolean to lock and unlock the update operation with hardware.
	 * True if locked, false if not.
	 */
	private AtomicBoolean updateLock;

	/**
	 * This is the service registration used to register the Core as a service
	 * of the OSGi framework.
	 */
	private ServiceRegistration<ICore> registration;

	/**
	 * An alternative constructor that allows the Core to be constructed with a
	 * particular ItemManager. This is used for testing.
	 *
	 * @param manager
	 *            The alternative ItemManager.
	 */
	public Core(ItemManager manager) {

		// Setup the ItemManager and the project table
		itemManager = manager;
		projectTable = new Hashtable<String, IProject>();

		// Set the project location
		if (!setupProjectLocation()) {
			throw new RuntimeException(
					"ICore Message: Unable to load workspace!");
		}

		// Set the update lock
		updateLock = new AtomicBoolean(false);

		return;
	}

	/**
	 * The Constructor
	 *
	 */
	public Core() {

		// Setup the ItemManager and the project table
		itemManager = new ItemManager();
		projectTable = new Hashtable<String, IProject>();

		// Set the project location
		if (!setupProjectLocation()) {
			throw new RuntimeException(
					"ICore Message: Unable to load workspace!");
		}

		// Set the update lock
		updateLock = new AtomicBoolean(false);

		return;
	}

	/**
	 * This operation is responsible for creating the project space used by the
	 * IPersistenceProvider.
	 */
	private void createItemDBProjectSpace() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String projectName = "itemDB";
		System.getProperty("file.separator");

		try {
			// Get the project handle
			itemDBProject = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!itemDBProject.exists()) {
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(projectName);
				// Create the project
				itemDBProject.create(desc, null);
			}
			// Open the project if it is not already open
			if (itemDBProject.exists() && !itemDBProject.isOpen()) {
				itemDBProject.open(null);
				// Refresh the project in case users manipulated files.
				itemDBProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		} catch (CoreException e) {
			// Catch exception for creating the project
			logger.error(getClass().getName() + " Exception!", e);
		}
	}

	/**
	 * This operation starts the Core, sets the component context and starts the
	 * web client if the HTTP service is available.
	 *
	 * @param context
	 *            The bundle context for this OSGi bundle.
	 */
	@Override
	public void start(BundleContext context) throws CoreException {

		// Store the component's context
		bundleContext = context;

		logger.info("ICore Message: Component context set!");

		// Get the persistence provider from the extension registry.
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(providerID);
		// Retrieve the provider from the registry and set it if one has not
		// already been set.
		if (point != null && provider == null) {
			// We only need one persistence provider, so just pull the
			// configuration element for the first one available.
			IConfigurationElement element = point.getConfigurationElements()[0];
			setPersistenceProvider((IPersistenceProvider) element
					.createExecutableExtension("class"));
		} else {
			logger.error("Extension Point " + providerID + "does not exist");
		}

		// Setup the persistence provider for the ItemManager so that it can
		// load items.
		itemManager.setPersistenceProvider(provider);

		// Load up the
		ItemBuilder builder = null;
		point = Platform.getExtensionRegistry().getExtensionPoint(builderID);

		// If the point is available, create all the builders and load them into
		// the Item Manager.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				builder = (ItemBuilder) elements[i]
						.createExecutableExtension("class");
				// Register the builder
				registerItem(builder);
			}
		} else {
			logger.error("Extension Point " + builderID + "does not exist");
		}

		// Set the default project on the Persistence Provider
		createItemDBProjectSpace();
		provider.setDefaultProject(itemDBProject);

		// Tell the ItemManager to suit up. It's time to rock and roll.
		itemManager.loadItems(itemDBProject);

		// Start the webservice!
		startHttpService();

		// Check the currently registered extensions
		//debugCheckExtensions();

		// Register this class as a service with the framework.
		registration = context.registerService(ICore.class, this, null);

		return;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// Update everything in the ItemManager that requires it
		itemManager.persistItems();

		// Unregister with the HTTP Service
		bundleContext.ungetService(httpServiceRef);

		// Unregister this service from the framework
		registration.unregister();
	}

	private void debugCheckExtensions() {
		Set<String> extensionPoints = new HashSet<String>();
		extensionPoints.add("org.eclipse.ice.item.itemBuilder");
		extensionPoints.add("org.eclipse.ice.item.compositeItemBuilder");
		extensionPoints.add("org.eclipse.ice.io.writer");
		extensionPoints.add("org.eclipse.ice.io.reader");
		extensionPoints.add("org.eclipse.ice.core.persistenceProvider");
		extensionPoints.add("org.eclipse.ice.datastructures.jaxbClassProvider");
		for (String extensionPointName : extensionPoints) {
			IExtensionPoint point = Platform.getExtensionRegistry()
					.getExtensionPoint(extensionPointName);
			logger.debug(
					"##### Extensions for: " + extensionPointName + " #####");
			if (point != null) {
				IExtension[] extensions = point.getExtensions();
				for (IExtension extension : extensions) {
					logger.debug("--" + extension.getSimpleIdentifier());
				}
			} else {
				logger.debug("Point does not exist");
			}

			logger.debug("##### end of " + extensionPointName + " #####");
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ICore#registerItem(ItemBuilder itemBuilder)
	 */
	@Override
	public void registerItem(ItemBuilder itemBuilder) {

		// Register the builder with the ItemManager so long as it is not null
		if (itemBuilder != null) {
			logger.info("ICore Message: Item " + itemBuilder.getItemName()
					+ " registered with Core.");
			itemManager.registerBuilder(itemBuilder);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ICore#registerCompositeItem(ICompositeItemBuilder builder)
	 */
	@Override
	public void registerCompositeItem(ICompositeItemBuilder builder) {

		// Register the builder with the ItemManager so long as it is not null
		if (builder != null) {
			logger.info("ICore Message: Composite Item " + builder.getItemName()
					+ " registered with Core.");
			itemManager.registerCompositeBuilder(builder);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ICore#unregisterItem(ItemBuilder itemBuilder)
	 */
	@Override
	public void unregisterItem(ItemBuilder itemBuilder) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ICore#createItem(String itemType)
	 */
	@Override
	public String createItem(String itemType) {
		// This operation retrieves the default "itemDB
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject("itemDB");
		return createItem(itemType, project);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.core.iCore.ICore#createItem(java.lang.String,
	 * org.eclipse.core.resources.IProject)
	 */
	@Override
	public String createItem(String itemType, IProject project) {

		// Local Declarations
		int newItemId = -1;

		// Create the Item if the ItemType is not null and the project space is
		// available
		if (itemType != null && project != null) {
			newItemId = itemManager.createItem(itemType, project);
		} else {
			logger.error("Unable to create Item in the core! Type = " + itemType
					+ " , project = " + project);
		}

		return String.valueOf(newItemId);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#deleteItem(String itemId)
	 */
	@Override
	public void deleteItem(String itemId) {

		// Forward the call to the ItemManager if the String is OK
		if (itemId != null) {
			itemManager.deleteItem(Integer.parseInt(itemId));
		}

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#getItemStatus(Integer id)
	 */
	@Override
	public FormStatus getItemStatus(Integer id) {
		return itemManager.getItemStatus(id);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#getItem(int itemId)
	 */
	@Override
	public Form getItem(int itemId) {
		return itemManager.retrieveItem(itemId);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#getAvailableItemTypes()
	 */
	@Override
	public ICEList<String> getAvailableItemTypes() {

		// Local Declarations
		ArrayList<String> types = itemManager.getAvailableBuilders();

		// FIXME - this is posting a warning in Eclipse about unchecked types.
		// It passes unit tests, so I am inclined to leave it.
		ICEList<String> retList = new ICEList<String>();

		// Fix the list
		retList.setList(types);

		return retList;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#updateItem(Form form, int uniqueClientId)
	 */
	@Override
	public FormStatus updateItem(Form form, int uniqueClientId) {

		// Local Declarations
		FormStatus status = FormStatus.InfoError;

		// FIXME - Check unique client id! Left out for now. c.f. - Sequence
		// diagram in model

		// Process the update request
		status = itemManager.updateItem(form);

		return status;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#processItem(int itemId, String actionName, int uniqueClientId)
	 */
	@Override
	public FormStatus processItem(int itemId, String actionName,
			int uniqueClientId) {

		// Local Declarations
		FormStatus status = FormStatus.InfoError;

		// Check the Item id and name
		if (itemId > 0 && actionName != null) {
			// Process the Item
			status = itemManager.processItem(itemId, actionName);
		}

		return status;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#getItemList()
	 */
	@Override
	public ArrayList<Identifiable> getItemList() {
		return itemManager.retrieveItemList();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#getItemOutputFile(int id)
	 */
	@Override
	public File getItemOutputFile(int id) {
		return itemManager.getOutputFile(id);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#cancelItemProcess(int itemId, String actionName)
	 */
	@Override
	public FormStatus cancelItemProcess(int itemId, String actionName) {
		return itemManager.cancelItemProcess(itemId, actionName);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#importFile(URI file)
	 */
	@Override
	public void importFile(URI file) {
		// Local Declarations
		IProject project = projectTable.get("defaultUser");

		// Only do this if the file is good
		if (file != null) {
			// Get the file handle
			IPath path = (new Path(file.toString()));
			IFile fileInProject = project.getFile(path.lastSegment());
			// Get the paths and convert them to strings
			IPath fullPathInProject = fileInProject.getLocation();
			String path1 = path.toString(),
					path2 = fullPathInProject.toString();
			// Remove devices ids and other such things from the path strings
			path1 = path1.substring(path1.lastIndexOf(":") + 1);
			path2 = path2.substring(path2.lastIndexOf(":") + 1);
			// Only manipulate the file if it is not already in the workspace.
			// It is completely reasonable to stick the file in the workspace
			// and then "import" it, so a simple check here relieves some
			// heartburn I would no doubt otherwise endure.
			if (!path1.equals(path2)) {
				// If the project space contains a file by the same name, but
				// with a different absolute path, delete that file.
				if (fileInProject.exists()) {
					try {
						fileInProject.delete(true, null);
					} catch (CoreException e) {
						// Complain and don't do anything else.
						logger.info(
								"Core Message: " + "Unable to import file.");
						logger.error(getClass().getName() + " Exception!", e);
						return;
					}
				}
				try {
					// Open a stream of the file
					FileInputStream fileStream = new FileInputStream(
							new File(file));
					// Import the file
					fileInProject.create(fileStream, true, null);
				} catch (FileNotFoundException e) {
					// Complain and don't do anything else.
					logger.info("Core Message: " + "Unable to import file.");
					logger.error(getClass().getName() + " Exception!", e);
					return;
				} catch (CoreException e) {
					// Complain and don't do anything else.
					logger.info("Core Message: " + "Unable to import file.");
					logger.error(getClass().getName() + " Exception!", e);
					return;
				}
			}
			// Refresh all of the Items
			itemManager.reloadItemData();

			// Drop some debug info.
			if (System.getProperty("DebugICE") != null) {
				logger.info(
						"Core Message: " + "Imported file " + file.toString());
			}
		}

		return;
	}

	/**
	 * This operation publishes a message to a publicly available log that may
	 * be consumed by clients.
	 *
	 * @param message
	 *            The message that should be published.
	 */
	private void publishMessage(String message) {
	}

	/**
	 * This operation scans the default project area for SerializedItems and
	 * loads them into the Core. It returns false if it encounters and error and
	 * true if it is successful.
	 *
	 * @return True if the SerializedItems stored in the default project area
	 *         were loaded successfully, false otherwise.
	 */
	private boolean loadDefaultAreaItems() {

		// Local Declarations
		boolean status = false;
		ArrayList<String> serializedItemNames = new ArrayList<String>();
		SerializedItemBuilder builder = null;
		IProject project;
		IResource[] resources = null;
		IResource currentResource = null;
		IFile file = null;
		String filename = null;

		// Get the default project and grab its contents
		project = projectTable.get("defaultUser");
		if (project == null) {
			return status;
		}
		try {
			// Update the status
			status = true;
			// If the "jobProfiles" folder exists, loop over the resources and
			// get the serialized items
			if (project.getFolder("jobProfiles").exists()) {
				// Get the list of resources in the project
				resources = project.getFolder("jobProfiles").members();
				for (int i = 0; i < resources.length; i++) {
					currentResource = resources[i];
					filename = currentResource.getFullPath().toOSString();
					// Grab PSF or XML file
					if (filename.endsWith(".psf") | filename.endsWith(".xml")) {
						// Get the file
						file = project.getFile(
								currentResource.getProjectRelativePath());
						try {
							// Load the SerializedItemBuilder
							builder = new SerializedItemBuilder(
									file.getContents());
							// Register the builder
							itemManager.registerBuilder(builder);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error(getClass().getName() + " Exception!",
									e);
							status = false;
						}
					}
				}
			} else {
				// Otherwise create the folder and return since there is nothing
				// to load
				project.getFolder("jobProfiles").create(true, true, null);
			}
		} catch (CoreException e) {
			logger.error(getClass().getName() + " Exception!", e);
		}

		return status;
	}

	/**
	 * This operation sets the HTTP service that should be used by the Core to
	 * publish itself.
	 *
	 * @param service
	 *            The HTTP service.
	 */
	public void setHttpService(HttpService service) {
		// Set the webservice reference
		httpService = service;
		logger.info("ICore Message: Web service set!");
	}

	/**
	 * Private method used by Core.start() method to start the HttpService.
	 */
	private void startHttpService() {

		// Grab the service reference and the service
		httpServiceRef = bundleContext.getServiceReference(HttpService.class);

		// If it is good to go, start up the webserver
		if (httpServiceRef != null) {

			// Local Declaration
			Dictionary<String, String> servletParams = new Hashtable<String, String>();

			/// Get the service
			httpService = bundleContext.getService(httpServiceRef);

			// Set the parameters
			servletParams.put("javax.ws.rs.Application", Core.class.getName());

			// Register the service
			try {
				// Get the bundle
				Bundle bundle = null;
				if (bundleContext != null) {
					bundle = bundleContext.getBundle();
				} else {
					logger.info("ICore Message: "
							+ "ICE Core ComponentContext was null! No web service started.");
					return;
				}

				// Make sure we got a valid bundle
				if (bundle == null) {
					logger.info("ICore Message: "
							+ "ICE Core Bundle was null! No web service started.");
					return;
				}

				// Find the root location and the jaas_config file
				URL resourceURL = bundle.getEntry("");
				URL configFileURL = bundle.getEntry("jaas_config.txt");
				// Resolve the URLs to be absolute
				resourceURL = FileLocator.resolve(resourceURL);
				configFileURL = FileLocator.resolve(configFileURL);
				HttpContext httpContext = new BasicAuthSecuredContext(
						resourceURL, configFileURL,
						"ICE Core Server Configuration");
				httpService.registerServlet("/ice", new ServletContainer(this),
						servletParams, httpContext);
			} catch (ServletException | NamespaceException | IOException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
			logger.info("ICore Message: ICE Core Server loaded and web "
					+ "service started!");
		} else {
			logger.info("ICore Message: ICE Core Server loaded, but "
					+ "without webservice.");
		}

		return;
	}

	/**
	 * This operation returns the current instance of the ICE core to the HTTP
	 * service so that it can be published. It overrides
	 * Application.getSingletons().
	 *
	 * @return The set of "singletons" - in this case just the running instance
	 *         of the Core.
	 */
	@Override
	public Set<Object> getSingletons() {
		// Create a set that just points to this class as the servlet
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		return result;
	}

	/**
	 * This private operation configures the project area for the Core. It uses
	 * the Eclipse Resources Plugin and behaves differently based on the value
	 * of the osgi.instance.area system property.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @return True if the setup operation was successful and false otherwise.
	 */
	private boolean setupProjectLocation() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = null;
		String separator = System.getProperty("file.separator");
		boolean status = true;

		// Print some diagnostic information
		if (Platform.getInstanceLocation() != null) {
			logger.info("ICore Message: Default workspace location is "
					+ Platform.getInstanceLocation().getURL().toString());
		}
		// Create the project space for the *default* user. This will have to
		// change to something more efficient and better managed when multi-user
		// support is added.
		try {
			// Get the project handle
			project = workspaceRoot.getProject("default");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("default");
				// Create the project
				project.create(desc, null);
			}

			// Open the project if it is not already open. Note that this is not
			// an "else if" because it always needs to be checked.
			if (project.exists() && !project.isOpen()) {
				project.open(null);
				// Always refresh the project too in case users manipulated the
				// files.
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
			// Add the project to the master table
			projectTable.put("defaultUser", project);
		} catch (CoreException e) {
			// Catch for creating the project
			logger.error(getClass().getName() + " Exception!", e);
			status = false;
		}

		// Load any SerializedItems that are stored in the default directory
		if (status) {
			status = loadDefaultAreaItems();
		}
		return status;
	}

	/**
	 * This operation sets the persistence provider from the
	 * IPersistenceProvider interface. This is picked up via OSGI.
	 *
	 * @param provider
	 *            The persistence provider.
	 */
	@Inject
	public void setPersistenceProvider(IPersistenceProvider provider) {

		// If the provider is not null, store the reference and log a message.
		if (provider != null) {
			logger.info("ICore Message: PersistenceProvider set!");
			this.provider = provider;
		}
	}

	/**
	 * This operation configures the IOService that should be used by Items
	 *
	 * @param service
	 *            The IOService that provides Input/Output capabilities to Items
	 *            managed by the Core.
	 */
	public void setIOService(IOService service) {
		itemManager.setIOService(service);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#connect()
	 */
	@Override
	public String connect() {
		return "1";
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#disconnect(int uniqueClientId)
	 */
	@Override
	public void disconnect(int uniqueClientId) {
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#importFileAsItem(URI file, String itemType)
	 */
	@Override
	public String importFileAsItem(URI file, String itemType) {

		// Local Declarations
		int newItemId = -1;

		// Create the Item if the ItemType is not null and the project space is
		// available
		if (projectTable.get("defaultUser") != null) {
			// Import the file
			importFile(file);
			// Get the name of the file. This is only created to get the short
			// name of the file.
			File tmpFile = new File(file);
			// Create the Item
			newItemId = itemManager.createItem(tmpFile.getName(), itemType,
					projectTable.get("defaultUser"));
		}

		return String.valueOf(newItemId);
	}

	/**
	 * This private operation creates an instance of the Message class from a
	 * string using a JSON parser.
	 *
	 * This operation is synchronized so that the core can't be overloaded.
	 *
	 * @param messageString
	 *            The original message, as a string
	 * @return list list of built messages.
	 */
	private ArrayList<Message> buildMessagesFromString(String messageString) {

		// Create the ArrayList of messages
		ArrayList<Message> messages = new ArrayList<Message>();

		// Create the parser and gson utility
		JsonParser parser = new JsonParser();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		// Catch any exceptions and return the empty list
		try {

			// Make the string a json string
			JsonElement messageJson = parser.parse(messageString);
			JsonObject messageJsonObject = messageJson.getAsJsonObject();

			// Get the Item id from the json
			JsonPrimitive itemIdJson = messageJsonObject
					.getAsJsonPrimitive("item_id");
			int itemId = itemIdJson.getAsInt();

			// Get the array of posts from the message
			JsonArray jsonMessagesList = messageJsonObject
					.getAsJsonArray("posts");

			// Load the list
			for (int i = 0; i < jsonMessagesList.size(); i++) {
				// Get the message as a json element
				JsonElement jsonMessage = jsonMessagesList.get(i);
				// Marshal it into a message
				Message tmpMessage = gson.fromJson(jsonMessage, Message.class);
				// Set the item id
				tmpMessage.setItemId(itemId);
				// Put it in the list
				messages.add(tmpMessage);
			}
		} catch (JsonParseException e) {
			// Log the message
			String err = "Core Message: " + "JSON parsing failed for message "
					+ messageString;
			logger.error(getClass().getName() + " Exception!", e);
			logger.error(err);
		}

		return messages;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see ICore#postUpdateMessage(String message)
	 */
	@Override
	public String postUpdateMessage(String message) {

		// Lock the operation
		updateLock.set(true);

		// Local Declarations
		String retVal = null;

		// Print the message if debugging is enabled
		// if (debuggingEnabled) {
		logger.info(
				"Core Message: " + "Update received with message: " + message);
				// }

		// Only process the message if it exists and is not empty
		if (message != null && !message.isEmpty() && message.contains("=")) {
			// Split the message on "=" since it is
			// application/x-www-form-encoded
			String[] messageParts = message.split("=");
			if (messageParts.length > 1) {
				// Get the message object.
				ArrayList<Message> msgList = buildMessagesFromString(
						messageParts[1]);
				// Post the messages if there are any. Fail otherwise.
				if (!msgList.isEmpty()) {
					for (int i = 0; i < msgList.size(); i++) {
						Message msg = msgList.get(i);
						itemManager.postUpdateMessage(msg);
					}
					// Set the return value
					retVal = "OK";
				}
			}
		}

		// Unlock the operation and return safely
		return (updateLock.getAndSet(false)) ? retVal : null;
	}

}
