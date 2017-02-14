/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentFactory
 * @model kind="package"
 * @generated
 */
public interface ScenvironmentPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "scenvironment";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://org/eclipse/ice/developer/scenvironment";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.ice.developer.scenvironment";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScenvironmentPackage eINSTANCE = org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl <em>SC Environment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getSCEnvironment()
	 * @generated
	 */
	int SC_ENVIRONMENT = 0;

	/**
	 * The feature id for the '<em><b>Image Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__IMAGE_NAME = 0;

	/**
	 * The feature id for the '<em><b>Available OSs</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__AVAILABLE_OSS = 1;

	/**
	 * The feature id for the '<em><b>Available Packages</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__AVAILABLE_PACKAGES = 2;

	/**
	 * The feature id for the '<em><b>Selected Packages</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__SELECTED_PACKAGES = 3;

	/**
	 * The feature id for the '<em><b>Added Files</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__ADDED_FILES = 4;

	/**
	 * The feature id for the '<em><b>Installer Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__INSTALLER_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Configuration Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__CONFIGURATION_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Spackpackage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT__SPACKPACKAGE = 7;

	/**
	 * The number of structural features of the '<em>SC Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT_FEATURE_COUNT = 8;

	/**
	 * The operation id for the '<em>Search Packages</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT___SEARCH_PACKAGES = 0;

	/**
	 * The number of operations of the '<em>SC Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SpackPackageImpl <em>Spack Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SpackPackageImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getSpackPackage()
	 * @generated
	 */
	int SPACK_PACKAGE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VERSION = 1;

	/**
	 * The number of structural features of the '<em>Spack Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Spack Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.InstallerTypeConfigurationImpl <em>Installer Type Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.InstallerTypeConfigurationImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getInstallerTypeConfiguration()
	 * @generated
	 */
	int INSTALLER_TYPE_CONFIGURATION = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTALLER_TYPE_CONFIGURATION__NAME = 0;

	/**
	 * The number of structural features of the '<em>Installer Type Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTALLER_TYPE_CONFIGURATION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Installer Type Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTALLER_TYPE_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ContainerConfigurationImpl <em>Container Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ContainerConfigurationImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getContainerConfiguration()
	 * @generated
	 */
	int CONTAINER_CONFIGURATION = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__NAME = INSTALLER_TYPE_CONFIGURATION__NAME;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__PORTS = INSTALLER_TYPE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Volumes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__VOLUMES = INSTALLER_TYPE_CONFIGURATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Ephemeral</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__IS_EPHEMERAL = INSTALLER_TYPE_CONFIGURATION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_FEATURE_COUNT = INSTALLER_TYPE_CONFIGURATION_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_OPERATION_COUNT = INSTALLER_TYPE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer <em>Installer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getInstaller()
	 * @generated
	 */
	int INSTALLER = 3;

	/**
	 * The number of structural features of the '<em>Installer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTALLER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Create Installer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTALLER___CREATE_INSTALLER = 0;

	/**
	 * The number of operations of the '<em>Installer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTALLER_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.DockerInstallerImpl <em>Docker Installer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.DockerInstallerImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getDockerInstaller()
	 * @generated
	 */
	int DOCKER_INSTALLER = 4;

	/**
	 * The number of structural features of the '<em>Docker Installer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_INSTALLER_FEATURE_COUNT = INSTALLER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Create Installer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_INSTALLER___CREATE_INSTALLER = INSTALLER___CREATE_INSTALLER;

	/**
	 * The operation id for the '<em>Create Docker Installer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_INSTALLER___CREATE_DOCKER_INSTALLER = INSTALLER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Docker Installer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_INSTALLER_OPERATION_COUNT = INSTALLER_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemInstallerImpl <em>File System Installer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemInstallerImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getFileSystemInstaller()
	 * @generated
	 */
	int FILE_SYSTEM_INSTALLER = 5;

	/**
	 * The number of structural features of the '<em>File System Installer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_INSTALLER_FEATURE_COUNT = INSTALLER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Create Installer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_INSTALLER___CREATE_INSTALLER = INSTALLER___CREATE_INSTALLER;

	/**
	 * The operation id for the '<em>Create FS Installer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_INSTALLER___CREATE_FS_INSTALLER = INSTALLER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>File System Installer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_INSTALLER_OPERATION_COUNT = INSTALLER_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentDataManagerImpl <em>SC Environment Data Manager</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentDataManagerImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getSCEnvironmentDataManager()
	 * @generated
	 */
	int SC_ENVIRONMENT_DATA_MANAGER = 6;

	/**
	 * The feature id for the '<em><b>Scenvironments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS = 0;

	/**
	 * The number of structural features of the '<em>SC Environment Data Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT_DATA_MANAGER_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>List Existing SC Eenv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT_DATA_MANAGER___LIST_EXISTING_SC_EENV = 0;

	/**
	 * The operation id for the '<em>Create SC Environment</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT_DATA_MANAGER___CREATE_SC_ENVIRONMENT = 1;

	/**
	 * The number of operations of the '<em>SC Environment Data Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SC_ENVIRONMENT_DATA_MANAGER_OPERATION_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemConfigurationImpl <em>File System Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemConfigurationImpl
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getFileSystemConfiguration()
	 * @generated
	 */
	int FILE_SYSTEM_CONFIGURATION = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_CONFIGURATION__NAME = INSTALLER_TYPE_CONFIGURATION__NAME;

	/**
	 * The feature id for the '<em><b>Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_CONFIGURATION__DIRECTORY = INSTALLER_TYPE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>File System Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_CONFIGURATION_FEATURE_COUNT = INSTALLER_TYPE_CONFIGURATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>File System Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_SYSTEM_CONFIGURATION_OPERATION_COUNT = INSTALLER_TYPE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum <em>Installer Enum</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getInstallerEnum()
	 * @generated
	 */
	int INSTALLER_ENUM = 9;

	/**
	 * The meta object id for the '<em>Packages</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see SpackPackage
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getPackages()
	 * @generated
	 */
	int PACKAGES = 10;

	/**
	 * The meta object id for the '<em>Ports</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getPorts()
	 * @generated
	 */
	int PORTS = 11;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment <em>SC Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>SC Environment</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment
	 * @generated
	 */
	EClass getSCEnvironment();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getImageName <em>Image Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Image Name</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getImageName()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EAttribute getSCEnvironment_ImageName();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAvailableOSs <em>Available OSs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Available OSs</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAvailableOSs()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EAttribute getSCEnvironment_AvailableOSs();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAvailablePackages <em>Available Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Available Packages</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAvailablePackages()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EAttribute getSCEnvironment_AvailablePackages();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getSelectedPackages <em>Selected Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Selected Packages</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getSelectedPackages()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EAttribute getSCEnvironment_SelectedPackages();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAddedFiles <em>Added Files</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Added Files</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAddedFiles()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EAttribute getSCEnvironment_AddedFiles();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getInstallerType <em>Installer Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Installer Type</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getInstallerType()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EAttribute getSCEnvironment_InstallerType();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getConfigurationType <em>Configuration Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Configuration Type</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getConfigurationType()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EReference getSCEnvironment_ConfigurationType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getSpackpackage <em>Spackpackage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Spackpackage</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getSpackpackage()
	 * @see #getSCEnvironment()
	 * @generated
	 */
	EReference getSCEnvironment_Spackpackage();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#searchPackages() <em>Search Packages</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Search Packages</em>' operation.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#searchPackages()
	 * @generated
	 */
	EOperation getSCEnvironment__SearchPackages();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage <em>Spack Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spack Package</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage
	 * @generated
	 */
	EClass getSpackPackage();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage#getName()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage#getVersion()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Version();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration <em>Container Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container Configuration</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration
	 * @generated
	 */
	EClass getContainerConfiguration();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Ports</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#getPorts()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Ports();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#getVolumes <em>Volumes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Volumes</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#getVolumes()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Volumes();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#isIsEphemeral <em>Is Ephemeral</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Ephemeral</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#isIsEphemeral()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_IsEphemeral();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer <em>Installer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Installer</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer
	 * @generated
	 */
	EClass getInstaller();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer#createInstaller() <em>Create Installer</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Installer</em>' operation.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer#createInstaller()
	 * @generated
	 */
	EOperation getInstaller__CreateInstaller();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller <em>Docker Installer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker Installer</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller
	 * @generated
	 */
	EClass getDockerInstaller();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller#createDockerInstaller() <em>Create Docker Installer</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Docker Installer</em>' operation.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller#createDockerInstaller()
	 * @generated
	 */
	EOperation getDockerInstaller__CreateDockerInstaller();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller <em>File System Installer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>File System Installer</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller
	 * @generated
	 */
	EClass getFileSystemInstaller();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller#createFSInstaller() <em>Create FS Installer</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create FS Installer</em>' operation.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller#createFSInstaller()
	 * @generated
	 */
	EOperation getFileSystemInstaller__CreateFSInstaller();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager <em>SC Environment Data Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>SC Environment Data Manager</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager
	 * @generated
	 */
	EClass getSCEnvironmentDataManager();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager#getScenvironments <em>Scenvironments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Scenvironments</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager#getScenvironments()
	 * @see #getSCEnvironmentDataManager()
	 * @generated
	 */
	EReference getSCEnvironmentDataManager_Scenvironments();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager#listExistingSCEenv() <em>List Existing SC Eenv</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>List Existing SC Eenv</em>' operation.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager#listExistingSCEenv()
	 * @generated
	 */
	EOperation getSCEnvironmentDataManager__ListExistingSCEenv();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager#createSCEnvironment() <em>Create SC Environment</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create SC Environment</em>' operation.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager#createSCEnvironment()
	 * @generated
	 */
	EOperation getSCEnvironmentDataManager__CreateSCEnvironment();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration <em>File System Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>File System Configuration</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration
	 * @generated
	 */
	EClass getFileSystemConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration#getDirectory <em>Directory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Directory</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration#getDirectory()
	 * @see #getFileSystemConfiguration()
	 * @generated
	 */
	EAttribute getFileSystemConfiguration_Directory();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration <em>Installer Type Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Installer Type Configuration</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration
	 * @generated
	 */
	EClass getInstallerTypeConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration#getName()
	 * @see #getInstallerTypeConfiguration()
	 * @generated
	 */
	EAttribute getInstallerTypeConfiguration_Name();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum <em>Installer Enum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Installer Enum</em>'.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum
	 * @generated
	 */
	EEnum getInstallerEnum();

	/**
	 * Returns the meta object for data type '{@link SpackPackage <em>Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Packages</em>'.
	 * @see SpackPackage
	 * @model instanceClass="java.lang.Object"
	 * @generated
	 */
	EDataType getPackages();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Ports</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 * @generated
	 */
	EDataType getPorts();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ScenvironmentFactory getScenvironmentFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl <em>SC Environment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getSCEnvironment()
		 * @generated
		 */
		EClass SC_ENVIRONMENT = eINSTANCE.getSCEnvironment();

		/**
		 * The meta object literal for the '<em><b>Image Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SC_ENVIRONMENT__IMAGE_NAME = eINSTANCE.getSCEnvironment_ImageName();

		/**
		 * The meta object literal for the '<em><b>Available OSs</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SC_ENVIRONMENT__AVAILABLE_OSS = eINSTANCE.getSCEnvironment_AvailableOSs();

		/**
		 * The meta object literal for the '<em><b>Available Packages</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SC_ENVIRONMENT__AVAILABLE_PACKAGES = eINSTANCE.getSCEnvironment_AvailablePackages();

		/**
		 * The meta object literal for the '<em><b>Selected Packages</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SC_ENVIRONMENT__SELECTED_PACKAGES = eINSTANCE.getSCEnvironment_SelectedPackages();

		/**
		 * The meta object literal for the '<em><b>Added Files</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SC_ENVIRONMENT__ADDED_FILES = eINSTANCE.getSCEnvironment_AddedFiles();

		/**
		 * The meta object literal for the '<em><b>Installer Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SC_ENVIRONMENT__INSTALLER_TYPE = eINSTANCE.getSCEnvironment_InstallerType();

		/**
		 * The meta object literal for the '<em><b>Configuration Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SC_ENVIRONMENT__CONFIGURATION_TYPE = eINSTANCE.getSCEnvironment_ConfigurationType();

		/**
		 * The meta object literal for the '<em><b>Spackpackage</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SC_ENVIRONMENT__SPACKPACKAGE = eINSTANCE.getSCEnvironment_Spackpackage();

		/**
		 * The meta object literal for the '<em><b>Search Packages</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SC_ENVIRONMENT___SEARCH_PACKAGES = eINSTANCE.getSCEnvironment__SearchPackages();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SpackPackageImpl <em>Spack Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SpackPackageImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getSpackPackage()
		 * @generated
		 */
		EClass SPACK_PACKAGE = eINSTANCE.getSpackPackage();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__NAME = eINSTANCE.getSpackPackage_Name();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__VERSION = eINSTANCE.getSpackPackage_Version();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ContainerConfigurationImpl <em>Container Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ContainerConfigurationImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getContainerConfiguration()
		 * @generated
		 */
		EClass CONTAINER_CONFIGURATION = eINSTANCE.getContainerConfiguration();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__PORTS = eINSTANCE.getContainerConfiguration_Ports();

		/**
		 * The meta object literal for the '<em><b>Volumes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__VOLUMES = eINSTANCE.getContainerConfiguration_Volumes();

		/**
		 * The meta object literal for the '<em><b>Is Ephemeral</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__IS_EPHEMERAL = eINSTANCE.getContainerConfiguration_IsEphemeral();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer <em>Installer</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getInstaller()
		 * @generated
		 */
		EClass INSTALLER = eINSTANCE.getInstaller();

		/**
		 * The meta object literal for the '<em><b>Create Installer</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation INSTALLER___CREATE_INSTALLER = eINSTANCE.getInstaller__CreateInstaller();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.DockerInstallerImpl <em>Docker Installer</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.DockerInstallerImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getDockerInstaller()
		 * @generated
		 */
		EClass DOCKER_INSTALLER = eINSTANCE.getDockerInstaller();

		/**
		 * The meta object literal for the '<em><b>Create Docker Installer</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_INSTALLER___CREATE_DOCKER_INSTALLER = eINSTANCE.getDockerInstaller__CreateDockerInstaller();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemInstallerImpl <em>File System Installer</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemInstallerImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getFileSystemInstaller()
		 * @generated
		 */
		EClass FILE_SYSTEM_INSTALLER = eINSTANCE.getFileSystemInstaller();

		/**
		 * The meta object literal for the '<em><b>Create FS Installer</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation FILE_SYSTEM_INSTALLER___CREATE_FS_INSTALLER = eINSTANCE.getFileSystemInstaller__CreateFSInstaller();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentDataManagerImpl <em>SC Environment Data Manager</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentDataManagerImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getSCEnvironmentDataManager()
		 * @generated
		 */
		EClass SC_ENVIRONMENT_DATA_MANAGER = eINSTANCE.getSCEnvironmentDataManager();

		/**
		 * The meta object literal for the '<em><b>Scenvironments</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS = eINSTANCE.getSCEnvironmentDataManager_Scenvironments();

		/**
		 * The meta object literal for the '<em><b>List Existing SC Eenv</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SC_ENVIRONMENT_DATA_MANAGER___LIST_EXISTING_SC_EENV = eINSTANCE.getSCEnvironmentDataManager__ListExistingSCEenv();

		/**
		 * The meta object literal for the '<em><b>Create SC Environment</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SC_ENVIRONMENT_DATA_MANAGER___CREATE_SC_ENVIRONMENT = eINSTANCE.getSCEnvironmentDataManager__CreateSCEnvironment();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemConfigurationImpl <em>File System Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.FileSystemConfigurationImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getFileSystemConfiguration()
		 * @generated
		 */
		EClass FILE_SYSTEM_CONFIGURATION = eINSTANCE.getFileSystemConfiguration();

		/**
		 * The meta object literal for the '<em><b>Directory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FILE_SYSTEM_CONFIGURATION__DIRECTORY = eINSTANCE.getFileSystemConfiguration_Directory();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.InstallerTypeConfigurationImpl <em>Installer Type Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.InstallerTypeConfigurationImpl
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getInstallerTypeConfiguration()
		 * @generated
		 */
		EClass INSTALLER_TYPE_CONFIGURATION = eINSTANCE.getInstallerTypeConfiguration();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSTALLER_TYPE_CONFIGURATION__NAME = eINSTANCE.getInstallerTypeConfiguration_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum <em>Installer Enum</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getInstallerEnum()
		 * @generated
		 */
		EEnum INSTALLER_ENUM = eINSTANCE.getInstallerEnum();

		/**
		 * The meta object literal for the '<em>Packages</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see SpackPackage
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getPackages()
		 * @generated
		 */
		EDataType PACKAGES = eINSTANCE.getPackages();

		/**
		 * The meta object literal for the '<em>Ports</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Integer
		 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentPackageImpl#getPorts()
		 * @generated
		 */
		EDataType PORTS = eINSTANCE.getPorts();

	}

} //ScenvironmentPackage
