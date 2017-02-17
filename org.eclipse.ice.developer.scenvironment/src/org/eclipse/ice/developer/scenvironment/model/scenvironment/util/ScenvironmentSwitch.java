/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage
 * @generated
 */
public class ScenvironmentSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ScenvironmentPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenvironmentSwitch() {
		if (modelPackage == null) {
			modelPackage = ScenvironmentPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ScenvironmentPackage.SC_ENVIRONMENT: {
				SCEnvironment scEnvironment = (SCEnvironment)theEObject;
				T result = caseSCEnvironment(scEnvironment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.SPACK_PACKAGE: {
				SpackPackage spackPackage = (SpackPackage)theEObject;
				T result = caseSpackPackage(spackPackage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.CONTAINER_CONFIGURATION: {
				ContainerConfiguration containerConfiguration = (ContainerConfiguration)theEObject;
				T result = caseContainerConfiguration(containerConfiguration);
				if (result == null) result = caseInstallerTypeConfiguration(containerConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.INSTALLER: {
				Installer installer = (Installer)theEObject;
				T result = caseInstaller(installer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.DOCKER_INSTALLER: {
				DockerInstaller dockerInstaller = (DockerInstaller)theEObject;
				T result = caseDockerInstaller(dockerInstaller);
				if (result == null) result = caseInstaller(dockerInstaller);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.FILE_SYSTEM_INSTALLER: {
				FileSystemInstaller fileSystemInstaller = (FileSystemInstaller)theEObject;
				T result = caseFileSystemInstaller(fileSystemInstaller);
				if (result == null) result = caseInstaller(fileSystemInstaller);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER: {
				SCEnvironmentDataManager scEnvironmentDataManager = (SCEnvironmentDataManager)theEObject;
				T result = caseSCEnvironmentDataManager(scEnvironmentDataManager);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.FILE_SYSTEM_CONFIGURATION: {
				FileSystemConfiguration fileSystemConfiguration = (FileSystemConfiguration)theEObject;
				T result = caseFileSystemConfiguration(fileSystemConfiguration);
				if (result == null) result = caseInstallerTypeConfiguration(fileSystemConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ScenvironmentPackage.INSTALLER_TYPE_CONFIGURATION: {
				InstallerTypeConfiguration installerTypeConfiguration = (InstallerTypeConfiguration)theEObject;
				T result = caseInstallerTypeConfiguration(installerTypeConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>SC Environment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>SC Environment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSCEnvironment(SCEnvironment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spack Package</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spack Package</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSpackPackage(SpackPackage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Container Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Container Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContainerConfiguration(ContainerConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Installer</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Installer</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInstaller(Installer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Docker Installer</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Docker Installer</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDockerInstaller(DockerInstaller object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>File System Installer</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>File System Installer</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFileSystemInstaller(FileSystemInstaller object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>SC Environment Data Manager</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>SC Environment Data Manager</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSCEnvironmentDataManager(SCEnvironmentDataManager object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>File System Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>File System Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFileSystemConfiguration(FileSystemConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Installer Type Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Installer Type Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInstallerTypeConfiguration(InstallerTypeConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //ScenvironmentSwitch
