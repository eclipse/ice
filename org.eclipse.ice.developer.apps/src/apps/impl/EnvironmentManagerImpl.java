/**
 */
package apps.impl;

import apps.AppsFactory;
import apps.AppsPackage;
import apps.EnvironmentBuilder;
import apps.EnvironmentConsole;
import apps.EnvironmentCreator;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.EnvironmentStorage;
import apps.IEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ice.docker.api.DockerAPI;
import org.eclipse.ice.docker.api.spotify.SpotifyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Manager</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.impl.EnvironmentManagerImpl#getEnvironmentCreator <em>Environment Creator</em>}</li>
 *   <li>{@link apps.impl.EnvironmentManagerImpl#getEnvironmentStorage <em>Environment Storage</em>}</li>
 *   <li>{@link apps.impl.EnvironmentManagerImpl#getConsole <em>Console</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnvironmentManagerImpl extends MinimalEObjectImpl.Container implements EnvironmentManager {
	/**
	 * The cached value of the '{@link #getEnvironmentCreator() <em>Environment Creator</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getEnvironmentCreator()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentCreator environmentCreator;

	/**
	 * The cached value of the '{@link #getEnvironmentStorage() <em>Environment Storage</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnvironmentStorage()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentStorage environmentStorage;

	/**
	 * The cached value of the '{@link #getConsole() <em>Console</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConsole()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentConsole console;

	/**
	 * The mapping of existing available environment names to the 
	 * actual IEnvironment
	 */
	protected HashMap<String, IEnvironment> environments;

	/**
	 * Reference to the Logger.
	 */
	protected final Logger logger;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	protected EnvironmentManagerImpl() {
		super();
		logger = LoggerFactory.getLogger(getClass());

		// FIXME, make this extensible to others.
		environmentCreator = AppsFactory.eINSTANCE.createJsonEnvironmentCreator();

		// Initialize the mapping of environments
		environments = new HashMap<String, IEnvironment>();
		
		console = AppsFactory.eINSTANCE.createEnvironmentConsole();
		
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.ENVIRONMENT_MANAGER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentCreator getEnvironmentCreator() {
		if (environmentCreator != null && environmentCreator.eIsProxy()) {
			InternalEObject oldEnvironmentCreator = (InternalEObject)environmentCreator;
			environmentCreator = (EnvironmentCreator)eResolveProxy(oldEnvironmentCreator);
			if (environmentCreator != oldEnvironmentCreator) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR, oldEnvironmentCreator, environmentCreator));
			}
		}
		return environmentCreator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentCreator basicGetEnvironmentCreator() {
		return environmentCreator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnvironmentCreator(EnvironmentCreator newEnvironmentCreator) {
		EnvironmentCreator oldEnvironmentCreator = environmentCreator;
		environmentCreator = newEnvironmentCreator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR, oldEnvironmentCreator, environmentCreator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentStorage getEnvironmentStorage() {
		if (environmentStorage != null && environmentStorage.eIsProxy()) {
			InternalEObject oldEnvironmentStorage = (InternalEObject)environmentStorage;
			environmentStorage = (EnvironmentStorage)eResolveProxy(oldEnvironmentStorage);
			if (environmentStorage != oldEnvironmentStorage) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_STORAGE, oldEnvironmentStorage, environmentStorage));
			}
		}
		return environmentStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentStorage basicGetEnvironmentStorage() {
		return environmentStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnvironmentStorage(EnvironmentStorage newEnvironmentStorage) {
		EnvironmentStorage oldEnvironmentStorage = environmentStorage;
		environmentStorage = newEnvironmentStorage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_STORAGE, oldEnvironmentStorage, environmentStorage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentConsole getConsole() {
		return console;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConsole(EnvironmentConsole newConsole, NotificationChain msgs) {
		EnvironmentConsole oldConsole = console;
		console = newConsole;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT_MANAGER__CONSOLE, oldConsole, newConsole);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConsole(EnvironmentConsole newConsole) {
		if (newConsole != console) {
			NotificationChain msgs = null;
			if (console != null)
				msgs = ((InternalEObject)console).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AppsPackage.ENVIRONMENT_MANAGER__CONSOLE, null, msgs);
			if (newConsole != null)
				msgs = ((InternalEObject)newConsole).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AppsPackage.ENVIRONMENT_MANAGER__CONSOLE, null, msgs);
			msgs = basicSetConsole(newConsole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT_MANAGER__CONSOLE, newConsole, newConsole));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment create(String dataString) {
		IEnvironment env = environmentCreator.create(dataString);
		environments.put(env.getName(), env);
		env.setState(EnvironmentState.CREATED);
		console.print("Created " + env.getName() + " Environment");
		env.setConsole(console);
		EContentAdapter adapter = new EContentAdapter() {
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				Object featureObject = notification.getFeature();
				if (featureObject instanceof EAttribute) {
					EAttribute attr = (EAttribute) featureObject;
					if (attr.getName().equals("name")) {
						// Update the IEnvironments map
						environments.remove((String) notification.getOldValue());
						environments.put(env.getName(), env);
					}
				}
			}
		};
		env.eAdapters().add(adapter);
		return env;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public EList<String> list() {
		EList<String> list = new BasicEList<String>();
		for (String s : environments.keySet()) {
			list.add(s);
		}
		return list;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment get(String environmentName) {
		if (environments.containsKey(environmentName)) {
			return environments.get(environmentName);
		} else {
			throw new IllegalArgumentException("No environment named " + environmentName);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment loadFromFile(String fileName) {
		AppsPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("env", new XMIResourceFactoryImpl());
		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();
		// Get the resource
		Resource resource = resSet.getResource(URI.createFileURI(fileName), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		IEnvironment environment = (IEnvironment) resource.getContents().get(0);
		environments.put(environment.getName(), environment);
		return environment;
	}

	public String persistToString(IEnvironment environment) {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("env", new XMIResourceFactoryImpl());

        // Obtain a new resource set
        ResourceSet resSet = new ResourceSetImpl();

        // create a resource
        Resource resource = resSet.createResource(URI
                        .createURI("dummy.env"));
        
        // Get the first model element and cast it to the right type, in my
        // example everything is hierarchical included in this first node
        resource.getContents().add(environment);

        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b );
            }

            public String toString(){
                return this.string.toString();
            }
        };
        // now save the content.
        try {
                resource.save(output, Collections.EMPTY_MAP);
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        return output.toString();
	}
	
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public String persistToString(String environmentName) {
		
		if (!environments.containsKey(environmentName)) {
			throw new IllegalArgumentException("Invalid environment name to persist.");
		}
		
		IEnvironment environment = environments.get(environmentName);
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("env", new XMIResourceFactoryImpl());

        // Obtain a new resource set
        ResourceSet resSet = new ResourceSetImpl();

        // create a resource
        Resource resource = resSet.createResource(URI
                        .createURI("dummy.env"));
        
        // Get the first model element and cast it to the right type, in my
        // example everything is hierarchical included in this first node
        resource.getContents().add(environment);

        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b );
            }

            public String toString(){
                return this.string.toString();
            }
        };
        // now save the content.
        try {
                resource.save(output, Collections.EMPTY_MAP);
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        return output.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public void persistToFile(String environmentName, String fileName) {
		if (!environments.containsKey(environmentName)) {
			throw new IllegalArgumentException("Invalid environment name to persist.");
		}
		
		IEnvironment environment = environments.get(environmentName);
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("env", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// create a resource
		Resource resource = resSet.createResource(URI.createURI("fileName"));

		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		resource.getContents().add(environment);

		// now save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public EList<String> listAvailableSpackPackages() {
		// FIXME For now we only check with Docker
		EList<String> packages = new BasicEList<String>();
		DockerAPI api = SpotifyFactory.eINSTANCE.createSpotifyDockerClient();
		String stdOut = api.createContainerExecCommand("eclipseice/base-fedora", new String[] { "/bin/bash", "-c", "source /root/.bashrc && spack list" });
		for (String s : stdOut.split("\n")) {
			if (!s.contains("==> Added") && !s.contains("gcc@6.3.1") && !s.trim().isEmpty()) {
				packages.add(s);
			}
		}
		return packages;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public void persistEnvironments() {
		if (environmentStorage != null) {
			if (console != null) console.print("Persisting existing environments");
			EList<IEnvironment> envs = new BasicEList<IEnvironment>();
			for (IEnvironment env : environments.values()) {
				envs.add(env);
			}
			environmentStorage.store(envs);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment createEmpty(String type) {
		
		IEnvironment env = null;
		EnvironmentBuilder builders[] = null;
		try {
			builders = EnvironmentBuilder.getEnvironmentBuilders();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		for (EnvironmentBuilder b : builders) {
			if (type.equals(b.name())) {
				env = b.build();
				break;
			}
		}
		
		if (env != null) {
			env.setName("unknown");
			env.setState(EnvironmentState.NOT_CREATED);
			env.setConsole(console);
			environments.put(env.getName(), env);
			EContentAdapter adapter = new EContentAdapter() {
				public void notifyChanged(Notification notification) {
					super.notifyChanged(notification);
					Object featureObject = notification.getFeature();
					if (featureObject instanceof EAttribute) {
						EAttribute attr = (EAttribute) featureObject;
						if (attr.getName().equals("name")) {
							// Update the IEnvironments map
							String oldName = (String) notification.getOldValue();
							String newName = (String) notification.getNewValue();
							IEnvironment environment = environments.remove(oldName);
							environments.put(newName, environment);
						}
					}
				}
			};
			env.eAdapters().add(adapter);
		}
		
		return env;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment loadFromXMI(String xmiStr) {
		
		String tempFileLocation = System.getProperty("user.home") + System.getProperty("file.separator") + "temp.env";
		File tempFile = new File(tempFileLocation);
		try {
			FileUtils.writeStringToFile(tempFile, xmiStr);
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.error("", e1);
		}
		IEnvironment env = loadFromFile(tempFileLocation);
		tempFile.delete();
		return env;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void loadEnvironments() {
		if (environmentStorage != null) {
			EList<IEnvironment> envs = environmentStorage.load();
			for (IEnvironment e : envs) {
				environments.put(e.getName(), e);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void startAllStoppedEnvironments() {
		for (IEnvironment e : environments.values()) {
			System.out.println("looking for stopped envs - " + e.getName() + ", " + e.getState());
			if (e.getState() == EnvironmentState.STOPPED) {
				e.connect();
			}
		}
		
		persistEnvironments();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void stopRunningEnvironments() {
		for (IEnvironment e : environments.values()) {
			if (e.getState() == EnvironmentState.RUNNING) {
				e.stop();
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void deleteEnvironment(String name) {
		IEnvironment env = environments.remove(name);
		if (env == null) {
			return;
		}
		persistEnvironments();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__CONSOLE:
				return basicSetConsole(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
				if (resolve) return getEnvironmentCreator();
				return basicGetEnvironmentCreator();
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_STORAGE:
				if (resolve) return getEnvironmentStorage();
				return basicGetEnvironmentStorage();
			case AppsPackage.ENVIRONMENT_MANAGER__CONSOLE:
				return getConsole();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
				setEnvironmentCreator((EnvironmentCreator)newValue);
				return;
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_STORAGE:
				setEnvironmentStorage((EnvironmentStorage)newValue);
				return;
			case AppsPackage.ENVIRONMENT_MANAGER__CONSOLE:
				setConsole((EnvironmentConsole)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
				setEnvironmentCreator((EnvironmentCreator)null);
				return;
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_STORAGE:
				setEnvironmentStorage((EnvironmentStorage)null);
				return;
			case AppsPackage.ENVIRONMENT_MANAGER__CONSOLE:
				setConsole((EnvironmentConsole)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
				return environmentCreator != null;
			case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_STORAGE:
				return environmentStorage != null;
			case AppsPackage.ENVIRONMENT_MANAGER__CONSOLE:
				return console != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case AppsPackage.ENVIRONMENT_MANAGER___CREATE__STRING:
				return create((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___LIST:
				return list();
			case AppsPackage.ENVIRONMENT_MANAGER___GET__STRING:
				return get((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___LOAD_FROM_FILE__STRING:
				return loadFromFile((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_TO_STRING__STRING:
				return persistToString((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_TO_FILE__STRING_STRING:
				persistToFile((String)arguments.get(0), (String)arguments.get(1));
				return null;
			case AppsPackage.ENVIRONMENT_MANAGER___LIST_AVAILABLE_SPACK_PACKAGES:
				return listAvailableSpackPackages();
			case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_ENVIRONMENTS:
				persistEnvironments();
				return null;
			case AppsPackage.ENVIRONMENT_MANAGER___CREATE_EMPTY__STRING:
				return createEmpty((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___LOAD_FROM_XMI__STRING:
				return loadFromXMI((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___LOAD_ENVIRONMENTS:
				loadEnvironments();
				return null;
			case AppsPackage.ENVIRONMENT_MANAGER___START_ALL_STOPPED_ENVIRONMENTS:
				startAllStoppedEnvironments();
				return null;
			case AppsPackage.ENVIRONMENT_MANAGER___STOP_RUNNING_ENVIRONMENTS:
				stopRunningEnvironments();
				return null;
			case AppsPackage.ENVIRONMENT_MANAGER___DELETE_ENVIRONMENT__STRING:
				deleteEnvironment((String)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} // EnvironmentManagerImpl
