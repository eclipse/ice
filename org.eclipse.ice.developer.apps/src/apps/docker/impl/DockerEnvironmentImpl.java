/**
 */
package apps.docker.impl;

import apps.EnvironmentConsole;
import apps.EnvironmentState;
import apps.OSPackage;
import apps.ProjectLauncher;
import apps.SourcePackage;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerAPI;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;
import apps.docker.DockerPackage;
import apps.docker.DockerProjectLauncher;

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

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Environment</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getOs <em>Os</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getDependentPackages <em>Dependent Packages</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getPrimaryApp <em>Primary App</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getProjectlauncher <em>Projectlauncher</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getState <em>State</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getConsole <em>Console</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getDocker <em>Docker</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getContainerConfiguration <em>Container Configuration</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getDockerfile <em>Dockerfile</em>}</li>
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
	 * The cached value of the '{@link #getDocker() <em>Docker</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDocker()
	 * @generated
	 * @ordered
	 */
	protected DockerAPI docker;

	/**
	 * The cached value of the '{@link #getContainerConfiguration()
	 * <em>Container Configuration</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	protected DockerEnvironmentImpl() {
		super();
		docker = DockerFactory.eINSTANCE.createDockerAPI();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerPackage.Literals.DOCKER_ENVIRONMENT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__OS, oldOs, os));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<apps.Package> getDependentPackages() {
		if (dependentPackages == null) {
			dependentPackages = new EObjectContainmentEList<apps.Package>(apps.Package.class, this, DockerPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES);
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, oldPrimaryApp, newPrimaryApp);
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
				msgs = ((InternalEObject)primaryApp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, null, msgs);
			if (newPrimaryApp != null)
				msgs = ((InternalEObject)newPrimaryApp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, null, msgs);
			msgs = basicSetPrimaryApp(newPrimaryApp, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP, newPrimaryApp, newPrimaryApp));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, oldProjectlauncher, newProjectlauncher);
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
				msgs = ((InternalEObject)projectlauncher).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			if (newProjectlauncher != null)
				msgs = ((InternalEObject)newProjectlauncher).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			msgs = basicSetProjectlauncher(newProjectlauncher, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER, newProjectlauncher, newProjectlauncher));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__STATE, oldState, state));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__CONSOLE, oldConsole, newConsole);
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
				msgs = ((InternalEObject)console).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__CONSOLE, null, msgs);
			if (newConsole != null)
				msgs = ((InternalEObject)newConsole).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__CONSOLE, null, msgs);
			msgs = basicSetConsole(newConsole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__CONSOLE, newConsole, newConsole));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPI getDocker() {
		return docker;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDocker(DockerAPI newDocker, NotificationChain msgs) {
		DockerAPI oldDocker = docker;
		docker = newDocker;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__DOCKER, oldDocker, newDocker);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocker(DockerAPI newDocker) {
		if (newDocker != docker) {
			NotificationChain msgs = null;
			if (docker != null)
				msgs = ((InternalEObject)docker).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__DOCKER, null, msgs);
			if (newDocker != null)
				msgs = ((InternalEObject)newDocker).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__DOCKER, null, msgs);
			msgs = basicSetDocker(newDocker, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__DOCKER, newDocker, newDocker));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ContainerConfiguration getContainerConfiguration() {
		return containerConfiguration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContainerConfiguration(ContainerConfiguration newContainerConfiguration,
			NotificationChain msgs) {
		ContainerConfiguration oldContainerConfiguration = containerConfiguration;
		containerConfiguration = newContainerConfiguration;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, oldContainerConfiguration, newContainerConfiguration);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainerConfiguration(ContainerConfiguration newContainerConfiguration) {
		if (newContainerConfiguration != containerConfiguration) {
			NotificationChain msgs = null;
			if (containerConfiguration != null)
				msgs = ((InternalEObject)containerConfiguration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, null, msgs);
			if (newContainerConfiguration != null)
				msgs = ((InternalEObject)newContainerConfiguration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, null, msgs);
			msgs = basicSetContainerConfiguration(newContainerConfiguration, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION, newContainerConfiguration, newContainerConfiguration));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__DOCKERFILE, oldDockerfile, dockerfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean build() {
		docker.setEnvironmentConsole(console);
		// Create the build Dockerfile from the
		// given Environment data.
		dockerfile = "from mccaskey/base-fedora-gcc6\n";
		String runSpackCommand = "";//run /bin/bash -c \"source /root/.bashrc && spack compiler find && ";
		String runPrimaryApp = "run git clone --recursive ";
		String runOSPkgInstaller = "run " + (os == "fedora" ? "dnf install -y " : "apt-get install -y ");
		
		// Loop over the dependent spack packages
		// and create run commands for the Dockerfile
		for (apps.Package pkg : dependentPackages) {
			/*if (pkg instanceof SpackPackage) {
				SpackPackage spkg = (SpackPackage) pkg;

				String spackCommand = "spack install --fake " + pkg.getName() + " ";
				if (!spkg.getVersion().equals("latest")) {
					spackCommand += "@" + spkg.getVersion() + " ";
				}
				spackCommand += "%" + spkg.getCompiler();
				runSpackCommand += spackCommand + " && ";
			} else */if (pkg instanceof OSPackage) {
				runOSPkgInstaller += pkg.getName() + " ";
			}
		}
//		runSpackCommand = runSpackCommand.substring(0, runSpackCommand.length() - 3) + "\"\n";
//		runOSPkgInstaller = runOSPkgInstaller.substring(0, runOSPkgInstaller.length() - 3) + "\n";

		// Add a git clone command for the primary app if
		// this is to be a Development Environment
		runPrimaryApp += "-b " + ((SourcePackage) primaryApp).getBranch() + " "
				+ ((SourcePackage) primaryApp).getRepoURL() + " " + primaryApp.getName() + "\n";

		// Add to the Dockerfile contents
		dockerfile += runSpackCommand + runOSPkgInstaller + "\n" + runPrimaryApp;

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
		boolean success = docker.buildImage(buildFile.getParent(), getName());

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
			if (!docker.createContainer(getName(), containerConfiguration)) {
				return false;
			}
			
			state = EnvironmentState.RUNNING;
			
			if (projectlauncher != null && projectlauncher instanceof DockerProjectLauncher) {
				((DockerProjectLauncher) projectlauncher).setContainerconfiguration(containerConfiguration);
				return projectlauncher.launchProject((SourcePackage) getPrimaryApp());
			}
			
			
		} else if (getState().equals(EnvironmentState.STOPPED)) {
			DockerProjectLauncher launcher = (DockerProjectLauncher) projectlauncher;
			
			String containerid = containerConfiguration.getId();
			docker.connectToExistingContainer(containerid);
			launcher.updateConnection(docker.getContainerRemotePort());
			state = EnvironmentState.RUNNING;
		
		} else {
			return false;
		}
		
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean delete() {
		return docker.deleteContainer(containerConfiguration.getId());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean stop() {
		state = EnvironmentState.STOPPED;
		return docker.stopContainer(containerConfiguration.getId());
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DockerPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				return ((InternalEList<?>)getDependentPackages()).basicRemove(otherEnd, msgs);
			case DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				return basicSetPrimaryApp(null, msgs);
			case DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				return basicSetProjectlauncher(null, msgs);
			case DockerPackage.DOCKER_ENVIRONMENT__CONSOLE:
				return basicSetConsole(null, msgs);
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				return basicSetDocker(null, msgs);
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return basicSetContainerConfiguration(null, msgs);
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
			case DockerPackage.DOCKER_ENVIRONMENT__NAME:
				return getName();
			case DockerPackage.DOCKER_ENVIRONMENT__OS:
				return getOs();
			case DockerPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				return getDependentPackages();
			case DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				return getPrimaryApp();
			case DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				return getProjectlauncher();
			case DockerPackage.DOCKER_ENVIRONMENT__STATE:
				return getState();
			case DockerPackage.DOCKER_ENVIRONMENT__CONSOLE:
				return getConsole();
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				return getDocker();
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return getContainerConfiguration();
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				return getDockerfile();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DockerPackage.DOCKER_ENVIRONMENT__NAME:
				setName((String)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__OS:
				setOs((String)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				getDependentPackages().addAll((Collection<? extends apps.Package>)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((apps.Package)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__STATE:
				setState((EnvironmentState)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__CONSOLE:
				setConsole((EnvironmentConsole)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				setDocker((DockerAPI)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				setContainerConfiguration((ContainerConfiguration)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				setDockerfile((String)newValue);
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
			case DockerPackage.DOCKER_ENVIRONMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__OS:
				setOs(OS_EDEFAULT);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((apps.Package)null);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)null);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__STATE:
				setState(STATE_EDEFAULT);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__CONSOLE:
				setConsole((EnvironmentConsole)null);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				setDocker((DockerAPI)null);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				setContainerConfiguration((ContainerConfiguration)null);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				setDockerfile(DOCKERFILE_EDEFAULT);
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
			case DockerPackage.DOCKER_ENVIRONMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DockerPackage.DOCKER_ENVIRONMENT__OS:
				return OS_EDEFAULT == null ? os != null : !OS_EDEFAULT.equals(os);
			case DockerPackage.DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES:
				return dependentPackages != null && !dependentPackages.isEmpty();
			case DockerPackage.DOCKER_ENVIRONMENT__PRIMARY_APP:
				return primaryApp != null;
			case DockerPackage.DOCKER_ENVIRONMENT__PROJECTLAUNCHER:
				return projectlauncher != null;
			case DockerPackage.DOCKER_ENVIRONMENT__STATE:
				return state != STATE_EDEFAULT;
			case DockerPackage.DOCKER_ENVIRONMENT__CONSOLE:
				return console != null;
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				return docker != null;
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return containerConfiguration != null;
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKERFILE:
				return DOCKERFILE_EDEFAULT == null ? dockerfile != null : !DOCKERFILE_EDEFAULT.equals(dockerfile);
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
			case DockerPackage.DOCKER_ENVIRONMENT___BUILD:
				return build();
			case DockerPackage.DOCKER_ENVIRONMENT___CONNECT:
				return connect();
			case DockerPackage.DOCKER_ENVIRONMENT___DELETE:
				return delete();
			case DockerPackage.DOCKER_ENVIRONMENT___STOP:
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

} // DockerEnvironmentImpl
