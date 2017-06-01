/**
 */
package org.eclipse.ice.developer.apps.docker.environment.impl;

import apps.EnvironmentConsole;
import apps.EnvironmentState;
import apps.OSPackage;
import apps.ProjectLauncher;
import apps.SourcePackage;
import apps.SpackPackage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment;
import org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher;
import org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage;
import org.eclipse.ice.docker.api.ContainerConfiguration;
import org.eclipse.ice.docker.api.DockerAPI;
import org.eclipse.ice.docker.api.DockerapiFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Docker Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getOs <em>Os</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getDependentPackages <em>Dependent Packages</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getPrimaryApp <em>Primary App</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getProjectlauncher <em>Projectlauncher</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getState <em>State</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getConsole <em>Console</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getContainerConfiguration <em>Container Configuration</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getDockerfile <em>Dockerfile</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl#getDockerAPI <em>Docker API</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DockerEnvironmentImpl extends MinimalEObjectImpl.Container implements DockerEnvironment {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOs() <em>Os</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOs()
	 * @generated
	 * @ordered
	 */
	protected static final String OS_EDEFAULT = "fedora";

	/**
	 * The cached value of the '{@link #getOs() <em>Os</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOs()
	 * @generated
	 * @ordered
	 */
	protected String os = OS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDependentPackages() <em>Dependent Packages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependentPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<apps.Package> dependentPackages;

	/**
	 * The cached value of the '{@link #getPrimaryApp() <em>Primary App</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryApp()
	 * @generated
	 * @ordered
	 */
	protected apps.Package primaryApp;

	/**
	 * The cached value of the '{@link #getProjectlauncher() <em>Projectlauncher</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjectlauncher()
	 * @generated
	 * @ordered
	 */
	protected ProjectLauncher projectlauncher;

	/**
	 * The default value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected static final EnvironmentState STATE_EDEFAULT = EnvironmentState.NOT_CREATED;

	/**
	 * The cached value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentState state = STATE_EDEFAULT;

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
	 * The cached value of the '{@link #getContainerConfiguration() <em>Container Configuration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerConfiguration()
	 * @generated
	 * @ordered
	 */
	protected ContainerConfiguration containerConfiguration;

	/**
	 * The default value of the '{@link #getDockerfile() <em>Dockerfile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDockerfile()
	 * @generated
	 * @ordered
	 */
	protected static final String DOCKERFILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDockerfile() <em>Dockerfile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDockerfile()
	 * @generated
	 * @ordered
	 */
	protected String dockerfile = DOCKERFILE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDockerAPI() <em>Docker API</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDockerAPI()
	 * @generated
	 * @ordered
	 */
	protected DockerAPI dockerAPI;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerEnvironmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EnvironmentPackage.Literals.DOCKER_ENVIRONMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOs() {
		return os;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOs(String newOs) {
		String oldOs = os;
		os = newOs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__OS, oldOs, os));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<apps.Package> getDependentPackages() {
		if (dependentPackages == null) {
			dependentPackages = new EObjectContainmentEList<apps.Package>(apps.Package.class, this, EnvironmentPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES);
		}
		return dependentPackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public apps.Package getPrimaryApp() {
		return primaryApp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPrimaryApp(apps.Package newPrimaryApp, NotificationChain msgs) {
		apps.Package oldPrimaryApp = primaryApp;
		primaryApp = newPrimaryApp;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, oldPrimaryApp, newPrimaryApp);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrimaryApp(apps.Package newPrimaryApp) {
		if (newPrimaryApp != primaryApp) {
			NotificationChain msgs = null;
			if (primaryApp != null)
				msgs = ((InternalEObject)primaryApp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, null, msgs);
			if (newPrimaryApp != null)
				msgs = ((InternalEObject)newPrimaryApp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, null, msgs);
			msgs = basicSetPrimaryApp(newPrimaryApp, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, newPrimaryApp, newPrimaryApp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectLauncher getProjectlauncher() {
		return projectlauncher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProjectlauncher(ProjectLauncher newProjectlauncher, NotificationChain msgs) {
		ProjectLauncher oldProjectlauncher = projectlauncher;
		projectlauncher = newProjectlauncher;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, oldProjectlauncher, newProjectlauncher);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProjectlauncher(ProjectLauncher newProjectlauncher) {
		if (newProjectlauncher != projectlauncher) {
			NotificationChain msgs = null;
			if (projectlauncher != null)
				msgs = ((InternalEObject)projectlauncher).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			if (newProjectlauncher != null)
				msgs = ((InternalEObject)newProjectlauncher).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			msgs = basicSetProjectlauncher(newProjectlauncher, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, newProjectlauncher, newProjectlauncher));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentState getState() {
		return state;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setState(EnvironmentState newState) {
		EnvironmentState oldState = state;
		state = newState == null ? STATE_EDEFAULT : newState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__STATE, oldState, state));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE, oldConsole, newConsole);
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
				msgs = ((InternalEObject)console).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE, null, msgs);
			if (newConsole != null)
				msgs = ((InternalEObject)newConsole).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE, null, msgs);
			msgs = basicSetConsole(newConsole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE, newConsole, newConsole));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContainerConfiguration getContainerConfiguration() {
		return containerConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContainerConfiguration(ContainerConfiguration newContainerConfiguration, NotificationChain msgs) {
		ContainerConfiguration oldContainerConfiguration = containerConfiguration;
		containerConfiguration = newContainerConfiguration;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, oldContainerConfiguration, newContainerConfiguration);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainerConfiguration(ContainerConfiguration newContainerConfiguration) {
		if (newContainerConfiguration != containerConfiguration) {
			NotificationChain msgs = null;
			if (containerConfiguration != null)
				msgs = ((InternalEObject)containerConfiguration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, null, msgs);
			if (newContainerConfiguration != null)
				msgs = ((InternalEObject)newContainerConfiguration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, null, msgs);
			msgs = basicSetContainerConfiguration(newContainerConfiguration, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, newContainerConfiguration, newContainerConfiguration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDockerfile() {
		return dockerfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDockerfile(String newDockerfile) {
		String oldDockerfile = dockerfile;
		dockerfile = newDockerfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKERFILE, oldDockerfile, dockerfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPI getDockerAPI() {
		if (dockerAPI != null && dockerAPI.eIsProxy()) {
			InternalEObject oldDockerAPI = (InternalEObject)dockerAPI;
			dockerAPI = (DockerAPI)eResolveProxy(oldDockerAPI);
			if (dockerAPI != oldDockerAPI) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKER_API, oldDockerAPI, dockerAPI));
			}
		}
		return dockerAPI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPI basicGetDockerAPI() {
		return dockerAPI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDockerAPI(DockerAPI newDockerAPI) {
		DockerAPI oldDockerAPI = dockerAPI;
		dockerAPI = newDockerAPI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKER_API, oldDockerAPI, dockerAPI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String execute(String imageName, String[] command) {
		return dockerAPI.createContainerExecCommand(imageName, command);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean hasDocker() {
		return dockerAPI != null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void pullImage(String imageName) {
		dockerAPI.pull(imageName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean build() {
//		dockerAPI.setEnvironmentConsole(console);
		dockerAPI.setConsole(DockerapiFactory.eINSTANCE.createStdOutConsole());
		// Create the build Dockerfile from the
		// given Environment data.
		dockerfile = "from eclipseice/base-fedora\n";
		String runSpackCommand = "run /bin/bash -c \"source /root/.bashrc && spack compiler find && ";
		String initialSpackCmd = runSpackCommand;
		String runPrimaryApp = primaryApp != null ? "run git clone --recursive " : "";
		String runOSPkgInstaller = "run " + (os == "fedora" ? "dnf install -y " : "apt-get install -y ");
		
		// Loop over the dependent spack packages
		// and create run commands for the Dockerfile
		for (apps.Package pkg : dependentPackages) {
			if (pkg instanceof SpackPackage) {
				SpackPackage spkg = (SpackPackage) pkg;

				String spackCommand = "spack install --fake " + pkg.getName() + " ";
				if (!spkg.getVersion().equals("latest")) {
					spackCommand += "@" + spkg.getVersion() + " ";
				}
				spackCommand += "%" + spkg.getCompiler();
				runSpackCommand += spackCommand + " && ";
			} else if (pkg instanceof OSPackage) {
				runOSPkgInstaller += pkg.getName() + " ";
			}
		}
		
		boolean noSpackPackages = false;
		if (runSpackCommand.equals(initialSpackCmd)) {
			noSpackPackages = true;
		}
		
		if (!noSpackPackages) {
			runSpackCommand = runSpackCommand.substring(0, runSpackCommand.length() - 3) + "\"\n";
		}
		// Add a git clone command for the primary app if
		// this is to be a Development Environment
		if (primaryApp != null) {
			runPrimaryApp += "-b " + ((SourcePackage) primaryApp).getBranch() + " "
					+ ((SourcePackage) primaryApp).getRepoURL() + " /projects/" + primaryApp.getName() + "\n";
		}
		
		// Add to the Dockerfile contents
		dockerfile += (!noSpackPackages ? runSpackCommand : "") + runOSPkgInstaller + "\n" + runPrimaryApp;

		console.print("Current DockerFile Contents:\n" + dockerfile);

		// Create the Dockerfile
		File buildFile = new File(
				System.getProperty("user.dir") + System.getProperty("file.separator") + ".tmpDockerbuild/Dockerfile");
		try {
			FileUtils.writeStringToFile(buildFile, dockerfile);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		// Build the Image
		boolean success = dockerAPI.buildImage(buildFile.getParent(), getName());

		// Remove the file
		buildFile.delete();

		this.state = EnvironmentState.CREATED;
		return success;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean connect() {

		if (getState().equals(EnvironmentState.CREATED)) {
			if (!dockerAPI.createContainer(getName(), containerConfiguration)) {
				return false;
			}
			
			state = EnvironmentState.RUNNING;
			
			if (projectlauncher != null && projectlauncher instanceof DockerProjectLauncher) {
				DockerProjectLauncher launcher = (DockerProjectLauncher) projectlauncher;
				launcher.setContainerconfiguration(containerConfiguration);
				return launcher.launchProject((SourcePackage) getPrimaryApp());
			}
			
			return true;
			
		} else if (getState().equals(EnvironmentState.STOPPED)) {
			String containerid = containerConfiguration.getId();
			if (!dockerAPI.connectToExistingContainer(containerid)) {
				return false;
			}
			
			if (projectlauncher != null && projectlauncher instanceof DockerProjectLauncher) {
				DockerProjectLauncher launcher = (DockerProjectLauncher) projectlauncher;
				launcher.updateConnection(containerConfiguration.getRemoteSSHPort());
				state = EnvironmentState.RUNNING;
			}
			return true;
		} 

		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean delete() {
		if (state == EnvironmentState.RUNNING) {
			if (!stop()) {
				return false;
			}
		}

		return dockerAPI.deleteContainer(containerConfiguration.getId());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean stop() {
		state = EnvironmentState.STOPPED;
		return dockerAPI.stopContainer(containerConfiguration.getId());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				return ((InternalEList<?>)getDependentPackages()).basicRemove(otherEnd, msgs);
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				return basicSetPrimaryApp(null, msgs);
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				return basicSetProjectlauncher(null, msgs);
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE:
				return basicSetConsole(null, msgs);
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return basicSetContainerConfiguration(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EnvironmentPackage.DOCKER_ENVIRONMENT__NAME:
				return getName();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__OS:
				return getOs();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				return getDependentPackages();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				return getPrimaryApp();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				return getProjectlauncher();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__STATE:
				return getState();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE:
				return getConsole();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return getContainerConfiguration();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				return getDockerfile();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKER_API:
				if (resolve) return getDockerAPI();
				return basicGetDockerAPI();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EnvironmentPackage.DOCKER_ENVIRONMENT__NAME:
				setName((String)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__OS:
				setOs((String)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				getDependentPackages().addAll((Collection<? extends apps.Package>)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((apps.Package)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__STATE:
				setState((EnvironmentState)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE:
				setConsole((EnvironmentConsole)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				setContainerConfiguration((ContainerConfiguration)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				setDockerfile((String)newValue);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKER_API:
				setDockerAPI((DockerAPI)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EnvironmentPackage.DOCKER_ENVIRONMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__OS:
				setOs(OS_EDEFAULT);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((apps.Package)null);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)null);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__STATE:
				setState(STATE_EDEFAULT);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE:
				setConsole((EnvironmentConsole)null);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				setContainerConfiguration((ContainerConfiguration)null);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				setDockerfile(DOCKERFILE_EDEFAULT);
				return;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKER_API:
				setDockerAPI((DockerAPI)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EnvironmentPackage.DOCKER_ENVIRONMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EnvironmentPackage.DOCKER_ENVIRONMENT__OS:
				return OS_EDEFAULT == null ? os != null : !OS_EDEFAULT.equals(os);
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				return dependentPackages != null && !dependentPackages.isEmpty();
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				return primaryApp != null;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				return projectlauncher != null;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__STATE:
				return state != STATE_EDEFAULT;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONSOLE:
				return console != null;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return containerConfiguration != null;
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				return DOCKERFILE_EDEFAULT == null ? dockerfile != null : !DOCKERFILE_EDEFAULT.equals(dockerfile);
			case EnvironmentPackage.DOCKER_ENVIRONMENT__DOCKER_API:
				return dockerAPI != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EnvironmentPackage.DOCKER_ENVIRONMENT___EXECUTE__STRING_STRING:
				return execute((String)arguments.get(0), (String[])arguments.get(1));
			case EnvironmentPackage.DOCKER_ENVIRONMENT___HAS_DOCKER:
				return hasDocker();
			case EnvironmentPackage.DOCKER_ENVIRONMENT___PULL_IMAGE__STRING:
				pullImage((String)arguments.get(0));
				return null;
			case EnvironmentPackage.DOCKER_ENVIRONMENT___BUILD:
				return build();
			case EnvironmentPackage.DOCKER_ENVIRONMENT___CONNECT:
				return connect();
			case EnvironmentPackage.DOCKER_ENVIRONMENT___DELETE:
				return delete();
			case EnvironmentPackage.DOCKER_ENVIRONMENT___STOP:
				return stop();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", os: ");
		result.append(os);
		result.append(", state: ");
		result.append(state);
		result.append(", Dockerfile: ");
		result.append(dockerfile);
		result.append(')');
		return result.toString();
	}

} //DockerEnvironmentImpl
