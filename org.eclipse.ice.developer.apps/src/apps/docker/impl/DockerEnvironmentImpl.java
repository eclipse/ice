/**
 */
package apps.docker.impl;

import apps.SpackPackage;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerAPI;
import apps.docker.DockerEnvironment;
import apps.docker.DockerPackage;

import apps.impl.EnvironmentImpl;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Environment</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getDocker <em>Docker</em>}</li>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getContainerConfiguration <em>Container Configuration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DockerEnvironmentImpl extends EnvironmentImpl implements DockerEnvironment {
	/**
	 * The cached value of the '{@link #getDocker() <em>Docker</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDocker()
	 * @generated
	 * @ordered
	 */
	protected DockerAPI docker;

	private String dockerFileContents;
	
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerEnvironmentImpl() {
		super();
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPI getDocker() {
		if (docker != null && docker.eIsProxy()) {
			InternalEObject oldDocker = (InternalEObject)docker;
			docker = (DockerAPI)eResolveProxy(oldDocker);
			if (docker != oldDocker) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DockerPackage.DOCKER_ENVIRONMENT__DOCKER, oldDocker, docker));
			}
		}
		return docker;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPI basicGetDocker() {
		return docker;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocker(DockerAPI newDocker) {
		DockerAPI oldDocker = docker;
		docker = newDocker;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__DOCKER, oldDocker, docker));
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
	 */
	public String getDockerFileContents() {
		return dockerFileContents;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
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
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				if (resolve) return getDocker();
				return basicGetDocker();
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return getContainerConfiguration();
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
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				setDocker((DockerAPI)newValue);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				setContainerConfiguration((ContainerConfiguration)newValue);
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
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				setDocker((DockerAPI)null);
				return;
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				setContainerConfiguration((ContainerConfiguration)null);
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
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				return docker != null;
			case DockerPackage.DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION:
				return containerConfiguration != null;
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
			case DockerPackage.DOCKER_ENVIRONMENT___GET_DOCKER_FILE_CONTENTS:
				return getDockerFileContents();
		}
		return super.eInvoke(operationID, arguments);
	}

	@Override
	public boolean launchDerived() {

		// Create the build Dockerfile from the
		// given Environment data.
		dockerFileContents = "from eclipseice/base-fedora\n";
		String runSpackCommand = "run /bin/bash -c \"spack compiler find && ";
		String runPrimaryApp = "run git clone --recursive ";

		// Loop over the dependent spack packages 
		// and create run commands for the Dockerfile
		for (SpackPackage pkg : dependentPackages) {
			String spackCommand = "spack install " + pkg.getName() + " ";
			if (!pkg.getVersion().equals("latest")) {
				spackCommand += "@" + pkg.getVersion() + " ";
			}
			spackCommand += "%" + pkg.getCompiler();
			runSpackCommand += spackCommand + " && ";
		}
		runSpackCommand = runSpackCommand.substring(0, runSpackCommand.length() - 3) + "\"\n";

		// Add a git clone command for the primary app if 
		// this is to be a Development Environment
		if (developmentEnvironment) {
			runPrimaryApp += "-b " + primaryApp.getBranch() + " " + primaryApp.getRepoURL() + " " + primaryApp.getName()
					+ "\n";
		}

		// Add to the Dockerfile contents
		dockerFileContents += runSpackCommand + runPrimaryApp;

		System.out.println("DockerFile:\n" + dockerFileContents);

		// Create the Dockerfile
		File buildFile = new File(
				System.getProperty("user.dir") + System.getProperty("file.separator") + ".tmpDockerbuild/Dockerfile");
		try {
			FileUtils.writeStringToFile(buildFile, dockerFileContents);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Build the Image
		docker.buildImage(buildFile.getParent(), getName());

		// Remove the file
		buildFile.delete();

		// Launch the container.
		docker.launchContainer(getName(), containerConfiguration);

		return true;
	}

} // DockerEnvironmentImpl
