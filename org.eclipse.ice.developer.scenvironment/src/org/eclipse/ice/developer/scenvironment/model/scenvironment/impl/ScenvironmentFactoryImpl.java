/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenvironmentFactoryImpl extends EFactoryImpl implements ScenvironmentFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ScenvironmentFactory init() {
		try {
			ScenvironmentFactory theScenvironmentFactory = (ScenvironmentFactory)EPackage.Registry.INSTANCE.getEFactory(ScenvironmentPackage.eNS_URI);
			if (theScenvironmentFactory != null) {
				return theScenvironmentFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ScenvironmentFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenvironmentFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ScenvironmentPackage.SC_ENVIRONMENT: return createSCEnvironment();
			case ScenvironmentPackage.SPACK_PACKAGE: return createSpackPackage();
			case ScenvironmentPackage.CONTAINER_CONFIGURATION: return createContainerConfiguration();
			case ScenvironmentPackage.DOCKER_INSTALLER: return createDockerInstaller();
			case ScenvironmentPackage.FILE_SYSTEM_INSTALLER: return createFileSystemInstaller();
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER: return createSCEnvironmentDataManager();
			case ScenvironmentPackage.FILE_SYSTEM_CONFIGURATION: return createFileSystemConfiguration();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ScenvironmentPackage.INSTALLER_ENUM:
				return createInstallerEnumFromString(eDataType, initialValue);
			case ScenvironmentPackage.PACKAGES:
				return createPackagesFromString(eDataType, initialValue);
			case ScenvironmentPackage.PORTS:
				return createPortsFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ScenvironmentPackage.INSTALLER_ENUM:
				return convertInstallerEnumToString(eDataType, instanceValue);
			case ScenvironmentPackage.PACKAGES:
				return convertPackagesToString(eDataType, instanceValue);
			case ScenvironmentPackage.PORTS:
				return convertPortsToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SCEnvironment createSCEnvironment() {
		SCEnvironmentImpl scEnvironment = new SCEnvironmentImpl();
		return scEnvironment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpackPackage createSpackPackage() {
		SpackPackageImpl spackPackage = new SpackPackageImpl();
		return spackPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContainerConfiguration createContainerConfiguration() {
		ContainerConfigurationImpl containerConfiguration = new ContainerConfigurationImpl();
		return containerConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerInstaller createDockerInstaller() {
		DockerInstallerImpl dockerInstaller = new DockerInstallerImpl();
		return dockerInstaller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FileSystemInstaller createFileSystemInstaller() {
		FileSystemInstallerImpl fileSystemInstaller = new FileSystemInstallerImpl();
		return fileSystemInstaller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SCEnvironmentDataManager createSCEnvironmentDataManager() {
		SCEnvironmentDataManagerImpl scEnvironmentDataManager = new SCEnvironmentDataManagerImpl();
		return scEnvironmentDataManager;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FileSystemConfiguration createFileSystemConfiguration() {
		FileSystemConfigurationImpl fileSystemConfiguration = new FileSystemConfigurationImpl();
		return fileSystemConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InstallerEnum createInstallerEnumFromString(EDataType eDataType, String initialValue) {
		InstallerEnum result = InstallerEnum.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInstallerEnumToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createPackagesFromString(EDataType eDataType, String initialValue) {
		return (Object)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPackagesToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createPortsFromString(EDataType eDataType, String initialValue) {
		return (Integer)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPortsToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenvironmentPackage getScenvironmentPackage() {
		return (ScenvironmentPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ScenvironmentPackage getPackage() {
		return ScenvironmentPackage.eINSTANCE;
	}

} //ScenvironmentFactoryImpl
