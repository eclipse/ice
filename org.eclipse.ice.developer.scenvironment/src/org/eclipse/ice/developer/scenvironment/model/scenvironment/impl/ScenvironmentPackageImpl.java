/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentFactory;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenvironmentPackageImpl extends EPackageImpl implements ScenvironmentPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scEnvironmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spackPackageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass containerConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass installerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dockerInstallerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fileSystemInstallerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scEnvironmentDataManagerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fileSystemConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass installerTypeConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum installerEnumEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType packagesEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType portsEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ScenvironmentPackageImpl() {
		super(eNS_URI, ScenvironmentFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ScenvironmentPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ScenvironmentPackage init() {
		if (isInited) return (ScenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ScenvironmentPackage.eNS_URI);

		// Obtain or create and register package
		ScenvironmentPackageImpl theScenvironmentPackage = (ScenvironmentPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ScenvironmentPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ScenvironmentPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theScenvironmentPackage.createPackageContents();

		// Initialize created meta-data
		theScenvironmentPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theScenvironmentPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ScenvironmentPackage.eNS_URI, theScenvironmentPackage);
		return theScenvironmentPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSCEnvironment() {
		return scEnvironmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSCEnvironment_ImageName() {
		return (EAttribute)scEnvironmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSCEnvironment_AvailableOSs() {
		return (EAttribute)scEnvironmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSCEnvironment_AvailablePackages() {
		return (EAttribute)scEnvironmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSCEnvironment_SelectedPackages() {
		return (EAttribute)scEnvironmentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSCEnvironment_AddedFiles() {
		return (EAttribute)scEnvironmentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSCEnvironment_InstallerType() {
		return (EAttribute)scEnvironmentEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSCEnvironment_ConfigurationType() {
		return (EReference)scEnvironmentEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSCEnvironment_Spackpackage() {
		return (EReference)scEnvironmentEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSCEnvironment__SearchPackages() {
		return scEnvironmentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpackPackage() {
		return spackPackageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_Name() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_Version() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContainerConfiguration() {
		return containerConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_Ports() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_Volumes() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_IsEphemeral() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInstaller() {
		return installerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getInstaller__CreateInstaller() {
		return installerEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDockerInstaller() {
		return dockerInstallerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerInstaller__CreateDockerInstaller() {
		return dockerInstallerEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFileSystemInstaller() {
		return fileSystemInstallerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getFileSystemInstaller__CreateFSInstaller() {
		return fileSystemInstallerEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSCEnvironmentDataManager() {
		return scEnvironmentDataManagerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSCEnvironmentDataManager_Scenvironments() {
		return (EReference)scEnvironmentDataManagerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSCEnvironmentDataManager__ListExistingSCEenv() {
		return scEnvironmentDataManagerEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSCEnvironmentDataManager__CreateSCEnvironment() {
		return scEnvironmentDataManagerEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFileSystemConfiguration() {
		return fileSystemConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFileSystemConfiguration_Directory() {
		return (EAttribute)fileSystemConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInstallerTypeConfiguration() {
		return installerTypeConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInstallerTypeConfiguration_Name() {
		return (EAttribute)installerTypeConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getInstallerEnum() {
		return installerEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPackages() {
		return packagesEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPorts() {
		return portsEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenvironmentFactory getScenvironmentFactory() {
		return (ScenvironmentFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		scEnvironmentEClass = createEClass(SC_ENVIRONMENT);
		createEAttribute(scEnvironmentEClass, SC_ENVIRONMENT__IMAGE_NAME);
		createEAttribute(scEnvironmentEClass, SC_ENVIRONMENT__AVAILABLE_OSS);
		createEAttribute(scEnvironmentEClass, SC_ENVIRONMENT__AVAILABLE_PACKAGES);
		createEAttribute(scEnvironmentEClass, SC_ENVIRONMENT__SELECTED_PACKAGES);
		createEAttribute(scEnvironmentEClass, SC_ENVIRONMENT__ADDED_FILES);
		createEAttribute(scEnvironmentEClass, SC_ENVIRONMENT__INSTALLER_TYPE);
		createEReference(scEnvironmentEClass, SC_ENVIRONMENT__CONFIGURATION_TYPE);
		createEReference(scEnvironmentEClass, SC_ENVIRONMENT__SPACKPACKAGE);
		createEOperation(scEnvironmentEClass, SC_ENVIRONMENT___SEARCH_PACKAGES);

		spackPackageEClass = createEClass(SPACK_PACKAGE);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__NAME);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__VERSION);

		containerConfigurationEClass = createEClass(CONTAINER_CONFIGURATION);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__PORTS);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__VOLUMES);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__IS_EPHEMERAL);

		installerEClass = createEClass(INSTALLER);
		createEOperation(installerEClass, INSTALLER___CREATE_INSTALLER);

		dockerInstallerEClass = createEClass(DOCKER_INSTALLER);
		createEOperation(dockerInstallerEClass, DOCKER_INSTALLER___CREATE_DOCKER_INSTALLER);

		fileSystemInstallerEClass = createEClass(FILE_SYSTEM_INSTALLER);
		createEOperation(fileSystemInstallerEClass, FILE_SYSTEM_INSTALLER___CREATE_FS_INSTALLER);

		scEnvironmentDataManagerEClass = createEClass(SC_ENVIRONMENT_DATA_MANAGER);
		createEReference(scEnvironmentDataManagerEClass, SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS);
		createEOperation(scEnvironmentDataManagerEClass, SC_ENVIRONMENT_DATA_MANAGER___LIST_EXISTING_SC_EENV);
		createEOperation(scEnvironmentDataManagerEClass, SC_ENVIRONMENT_DATA_MANAGER___CREATE_SC_ENVIRONMENT);

		fileSystemConfigurationEClass = createEClass(FILE_SYSTEM_CONFIGURATION);
		createEAttribute(fileSystemConfigurationEClass, FILE_SYSTEM_CONFIGURATION__DIRECTORY);

		installerTypeConfigurationEClass = createEClass(INSTALLER_TYPE_CONFIGURATION);
		createEAttribute(installerTypeConfigurationEClass, INSTALLER_TYPE_CONFIGURATION__NAME);

		// Create enums
		installerEnumEEnum = createEEnum(INSTALLER_ENUM);

		// Create data types
		packagesEDataType = createEDataType(PACKAGES);
		portsEDataType = createEDataType(PORTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		containerConfigurationEClass.getESuperTypes().add(this.getInstallerTypeConfiguration());
		dockerInstallerEClass.getESuperTypes().add(this.getInstaller());
		fileSystemInstallerEClass.getESuperTypes().add(this.getInstaller());
		fileSystemConfigurationEClass.getESuperTypes().add(this.getInstallerTypeConfiguration());

		// Initialize classes, features, and operations; add parameters
		initEClass(scEnvironmentEClass, SCEnvironment.class, "SCEnvironment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSCEnvironment_ImageName(), ecorePackage.getEString(), "imageName", null, 1, 1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSCEnvironment_AvailableOSs(), ecorePackage.getEString(), "availableOSs", null, 1, -1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSCEnvironment_AvailablePackages(), this.getPackages(), "availablePackages", null, 0, -1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSCEnvironment_SelectedPackages(), this.getPackages(), "selectedPackages", null, 0, -1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSCEnvironment_AddedFiles(), ecorePackage.getEString(), "addedFiles", null, 0, -1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSCEnvironment_InstallerType(), this.getInstallerEnum(), "installerType", null, 1, 1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSCEnvironment_ConfigurationType(), this.getInstallerTypeConfiguration(), null, "configurationType", null, 1, 1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSCEnvironment_Spackpackage(), this.getSpackPackage(), null, "spackpackage", null, 0, -1, SCEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSCEnvironment__SearchPackages(), this.getPackages(), "searchPackages", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(spackPackageEClass, SpackPackage.class, "SpackPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpackPackage_Name(), ecorePackage.getEString(), "name", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_Version(), ecorePackage.getEString(), "version", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(containerConfigurationEClass, ContainerConfiguration.class, "ContainerConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getContainerConfiguration_Ports(), ecorePackage.getEInt(), "ports", null, 1, -1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_Volumes(), ecorePackage.getEString(), "volumes", null, 0, -1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_IsEphemeral(), ecorePackage.getEBoolean(), "isEphemeral", "true", 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(installerEClass, Installer.class, "Installer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEOperation(getInstaller__CreateInstaller(), null, "createInstaller", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(dockerInstallerEClass, DockerInstaller.class, "DockerInstaller", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEOperation(getDockerInstaller__CreateDockerInstaller(), null, "createDockerInstaller", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fileSystemInstallerEClass, FileSystemInstaller.class, "FileSystemInstaller", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEOperation(getFileSystemInstaller__CreateFSInstaller(), null, "createFSInstaller", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(scEnvironmentDataManagerEClass, SCEnvironmentDataManager.class, "SCEnvironmentDataManager", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSCEnvironmentDataManager_Scenvironments(), this.getSCEnvironment(), null, "scenvironments", null, 0, -1, SCEnvironmentDataManager.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSCEnvironmentDataManager__ListExistingSCEenv(), this.getSCEnvironment(), "listExistingSCEenv", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSCEnvironmentDataManager__CreateSCEnvironment(), this.getSCEnvironment(), "createSCEnvironment", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fileSystemConfigurationEClass, FileSystemConfiguration.class, "FileSystemConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFileSystemConfiguration_Directory(), ecorePackage.getEString(), "directory", null, 1, 1, FileSystemConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(installerTypeConfigurationEClass, InstallerTypeConfiguration.class, "InstallerTypeConfiguration", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInstallerTypeConfiguration_Name(), ecorePackage.getEString(), "name", null, 1, 1, InstallerTypeConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(installerEnumEEnum, InstallerEnum.class, "InstallerEnum");
		addEEnumLiteral(installerEnumEEnum, InstallerEnum.FILE_SYSTEM);
		addEEnumLiteral(installerEnumEEnum, InstallerEnum.DOCKER);

		// Initialize data types
		initEDataType(packagesEDataType, SpackPackage.class, "Packages", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(portsEDataType, Integer.class, "Ports", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ScenvironmentPackageImpl
