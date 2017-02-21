/**
 */
package apps;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see apps.AppsFactory
 * @model kind="package"
 * @generated
 */
public interface AppsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "apps";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/ice/apps";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "developerappstore";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AppsPackage eINSTANCE = apps.impl.AppsPackageImpl.init();

	/**
	 * The meta object id for the '{@link apps.IEnvironment <em>IEnvironment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment
	 * @see apps.impl.AppsPackageImpl#getIEnvironment()
	 * @generated
	 */
	int IENVIRONMENT = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Os</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__OS = 2;

	/**
	 * The feature id for the '<em><b>Dependent Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__DEPENDENT_PACKAGES = 3;

	/**
	 * The feature id for the '<em><b>Development Environment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__DEVELOPMENT_ENVIRONMENT = 4;

	/**
	 * The feature id for the '<em><b>Generate Project</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__GENERATE_PROJECT = 5;

	/**
	 * The feature id for the '<em><b>Primary App</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__PRIMARY_APP = 6;

	/**
	 * The number of structural features of the '<em>IEnvironment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_FEATURE_COUNT = 7;

	/**
	 * The operation id for the '<em>Launch</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT___LAUNCH = 0;

	/**
	 * The number of operations of the '<em>IEnvironment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link apps.impl.EnvironmentManagerImpl <em>Environment Manager</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.EnvironmentManagerImpl
	 * @see apps.impl.AppsPackageImpl#getEnvironmentManager()
	 * @generated
	 */
	int ENVIRONMENT_MANAGER = 1;

	/**
	 * The number of structural features of the '<em>Environment Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Create Environment</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING = 0;

	/**
	 * The operation id for the '<em>List Existing Environments</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LIST_EXISTING_ENVIRONMENTS = 1;

	/**
	 * The operation id for the '<em>Load Existing Environment</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LOAD_EXISTING_ENVIRONMENT__STRING = 2;

	/**
	 * The operation id for the '<em>Load Environment From File</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT_FROM_FILE__STRING = 3;

	/**
	 * The operation id for the '<em>Persist To XMI String</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___PERSIST_TO_XMI_STRING__IENVIRONMENT = 4;

	/**
	 * The operation id for the '<em>Persist XMI To File</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___PERSIST_XMI_TO_FILE__IENVIRONMENT_STRING = 5;

	/**
	 * The number of operations of the '<em>Environment Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER_OPERATION_COUNT = 6;

	/**
	 * The meta object id for the '{@link apps.impl.SpackPackageImpl <em>Spack Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.SpackPackageImpl
	 * @see apps.impl.AppsPackageImpl#getSpackPackage()
	 * @generated
	 */
	int SPACK_PACKAGE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Compiler</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__COMPILER = 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VERSION = 2;

	/**
	 * The feature id for the '<em><b>Cpp Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__CPP_FLAGS = 3;

	/**
	 * The feature id for the '<em><b>Virtual Dependency</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VIRTUAL_DEPENDENCY = 4;

	/**
	 * The feature id for the '<em><b>Virtual Dependency Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER = 5;

	/**
	 * The feature id for the '<em><b>Repo URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__REPO_URL = 6;

	/**
	 * The feature id for the '<em><b>Branch</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__BRANCH = 7;

	/**
	 * The number of structural features of the '<em>Spack Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Spack Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link apps.impl.EnvironmentImpl <em>Environment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.EnvironmentImpl
	 * @see apps.impl.AppsPackageImpl#getEnvironment()
	 * @generated
	 */
	int ENVIRONMENT = 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__TYPE = IENVIRONMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__NAME = IENVIRONMENT__NAME;

	/**
	 * The feature id for the '<em><b>Os</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__OS = IENVIRONMENT__OS;

	/**
	 * The feature id for the '<em><b>Dependent Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__DEPENDENT_PACKAGES = IENVIRONMENT__DEPENDENT_PACKAGES;

	/**
	 * The feature id for the '<em><b>Development Environment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__DEVELOPMENT_ENVIRONMENT = IENVIRONMENT__DEVELOPMENT_ENVIRONMENT;

	/**
	 * The feature id for the '<em><b>Generate Project</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__GENERATE_PROJECT = IENVIRONMENT__GENERATE_PROJECT;

	/**
	 * The feature id for the '<em><b>Primary App</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__PRIMARY_APP = IENVIRONMENT__PRIMARY_APP;

	/**
	 * The feature id for the '<em><b>Projectlauncher</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__PROJECTLAUNCHER = IENVIRONMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_FEATURE_COUNT = IENVIRONMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Launch</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT___LAUNCH = IENVIRONMENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Launch Derived</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT___LAUNCH_DERIVED = IENVIRONMENT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_OPERATION_COUNT = IENVIRONMENT_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link apps.ProjectLauncher <em>Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.ProjectLauncher
	 * @see apps.impl.AppsPackageImpl#getProjectLauncher()
	 * @generated
	 */
	int PROJECT_LAUNCHER = 4;

	/**
	 * The number of structural features of the '<em>Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_LAUNCHER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Launch Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE = 0;

	/**
	 * The number of operations of the '<em>Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_LAUNCHER_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link apps.impl.PTPSyncProjectLauncherImpl <em>PTP Sync Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.PTPSyncProjectLauncherImpl
	 * @see apps.impl.AppsPackageImpl#getPTPSyncProjectLauncher()
	 * @generated
	 */
	int PTP_SYNC_PROJECT_LAUNCHER = 5;

	/**
	 * The number of structural features of the '<em>PTP Sync Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PTP_SYNC_PROJECT_LAUNCHER_FEATURE_COUNT = PROJECT_LAUNCHER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Launch Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE = PROJECT_LAUNCHER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>PTP Sync Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PTP_SYNC_PROJECT_LAUNCHER_OPERATION_COUNT = PROJECT_LAUNCHER_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link apps.impl.LocalCDTProjectLauncherImpl <em>Local CDT Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.LocalCDTProjectLauncherImpl
	 * @see apps.impl.AppsPackageImpl#getLocalCDTProjectLauncher()
	 * @generated
	 */
	int LOCAL_CDT_PROJECT_LAUNCHER = 6;

	/**
	 * The number of structural features of the '<em>Local CDT Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_CDT_PROJECT_LAUNCHER_FEATURE_COUNT = PROJECT_LAUNCHER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Launch Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_CDT_PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE = PROJECT_LAUNCHER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Local CDT Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_CDT_PROJECT_LAUNCHER_OPERATION_COUNT = PROJECT_LAUNCHER_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link apps.EnvironmentType <em>Environment Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentType
	 * @see apps.impl.AppsPackageImpl#getEnvironmentType()
	 * @generated
	 */
	int ENVIRONMENT_TYPE = 7;


	/**
	 * Returns the meta object for class '{@link apps.IEnvironment <em>IEnvironment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEnvironment</em>'.
	 * @see apps.IEnvironment
	 * @generated
	 */
	EClass getIEnvironment();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see apps.IEnvironment#getType()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_Type();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see apps.IEnvironment#getName()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_Name();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#getOs <em>Os</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Os</em>'.
	 * @see apps.IEnvironment#getOs()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_Os();

	/**
	 * Returns the meta object for the containment reference list '{@link apps.IEnvironment#getDependentPackages <em>Dependent Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dependent Packages</em>'.
	 * @see apps.IEnvironment#getDependentPackages()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EReference getIEnvironment_DependentPackages();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#isDevelopmentEnvironment <em>Development Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Development Environment</em>'.
	 * @see apps.IEnvironment#isDevelopmentEnvironment()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_DevelopmentEnvironment();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#isGenerateProject <em>Generate Project</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Project</em>'.
	 * @see apps.IEnvironment#isGenerateProject()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_GenerateProject();

	/**
	 * Returns the meta object for the containment reference '{@link apps.IEnvironment#getPrimaryApp <em>Primary App</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Primary App</em>'.
	 * @see apps.IEnvironment#getPrimaryApp()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EReference getIEnvironment_PrimaryApp();

	/**
	 * Returns the meta object for the '{@link apps.IEnvironment#launch() <em>Launch</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch</em>' operation.
	 * @see apps.IEnvironment#launch()
	 * @generated
	 */
	EOperation getIEnvironment__Launch();

	/**
	 * Returns the meta object for class '{@link apps.EnvironmentManager <em>Environment Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment Manager</em>'.
	 * @see apps.EnvironmentManager
	 * @generated
	 */
	EClass getEnvironmentManager();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#createEnvironment(java.lang.String) <em>Create Environment</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Environment</em>' operation.
	 * @see apps.EnvironmentManager#createEnvironment(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__CreateEnvironment__String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#listExistingEnvironments() <em>List Existing Environments</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>List Existing Environments</em>' operation.
	 * @see apps.EnvironmentManager#listExistingEnvironments()
	 * @generated
	 */
	EOperation getEnvironmentManager__ListExistingEnvironments();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#loadExistingEnvironment(java.lang.String) <em>Load Existing Environment</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Load Existing Environment</em>' operation.
	 * @see apps.EnvironmentManager#loadExistingEnvironment(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__LoadExistingEnvironment__String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#loadEnvironmentFromFile(java.lang.String) <em>Load Environment From File</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Load Environment From File</em>' operation.
	 * @see apps.EnvironmentManager#loadEnvironmentFromFile(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__LoadEnvironmentFromFile__String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#persistToXMIString(apps.IEnvironment) <em>Persist To XMI String</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Persist To XMI String</em>' operation.
	 * @see apps.EnvironmentManager#persistToXMIString(apps.IEnvironment)
	 * @generated
	 */
	EOperation getEnvironmentManager__PersistToXMIString__IEnvironment();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#persistXMIToFile(apps.IEnvironment, java.lang.String) <em>Persist XMI To File</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Persist XMI To File</em>' operation.
	 * @see apps.EnvironmentManager#persistXMIToFile(apps.IEnvironment, java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__PersistXMIToFile__IEnvironment_String();

	/**
	 * Returns the meta object for class '{@link apps.SpackPackage <em>Spack Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spack Package</em>'.
	 * @see apps.SpackPackage
	 * @generated
	 */
	EClass getSpackPackage();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see apps.SpackPackage#getName()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Name();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getCompiler <em>Compiler</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Compiler</em>'.
	 * @see apps.SpackPackage#getCompiler()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Compiler();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see apps.SpackPackage#getVersion()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Version();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getCppFlags <em>Cpp Flags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cpp Flags</em>'.
	 * @see apps.SpackPackage#getCppFlags()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_CppFlags();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getVirtualDependency <em>Virtual Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Virtual Dependency</em>'.
	 * @see apps.SpackPackage#getVirtualDependency()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_VirtualDependency();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getVirtualDependencyProvider <em>Virtual Dependency Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Virtual Dependency Provider</em>'.
	 * @see apps.SpackPackage#getVirtualDependencyProvider()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_VirtualDependencyProvider();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getRepoURL <em>Repo URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repo URL</em>'.
	 * @see apps.SpackPackage#getRepoURL()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_RepoURL();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getBranch <em>Branch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Branch</em>'.
	 * @see apps.SpackPackage#getBranch()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Branch();

	/**
	 * Returns the meta object for class '{@link apps.Environment <em>Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment</em>'.
	 * @see apps.Environment
	 * @generated
	 */
	EClass getEnvironment();

	/**
	 * Returns the meta object for the containment reference '{@link apps.Environment#getProjectlauncher <em>Projectlauncher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Projectlauncher</em>'.
	 * @see apps.Environment#getProjectlauncher()
	 * @see #getEnvironment()
	 * @generated
	 */
	EReference getEnvironment_Projectlauncher();

	/**
	 * Returns the meta object for the '{@link apps.Environment#launch() <em>Launch</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch</em>' operation.
	 * @see apps.Environment#launch()
	 * @generated
	 */
	EOperation getEnvironment__Launch();

	/**
	 * Returns the meta object for the '{@link apps.Environment#launchDerived() <em>Launch Derived</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Derived</em>' operation.
	 * @see apps.Environment#launchDerived()
	 * @generated
	 */
	EOperation getEnvironment__LaunchDerived();

	/**
	 * Returns the meta object for class '{@link apps.ProjectLauncher <em>Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project Launcher</em>'.
	 * @see apps.ProjectLauncher
	 * @generated
	 */
	EClass getProjectLauncher();

	/**
	 * Returns the meta object for the '{@link apps.ProjectLauncher#launchProject(apps.SpackPackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see apps.ProjectLauncher#launchProject(apps.SpackPackage)
	 * @generated
	 */
	EOperation getProjectLauncher__LaunchProject__SpackPackage();

	/**
	 * Returns the meta object for class '{@link apps.PTPSyncProjectLauncher <em>PTP Sync Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>PTP Sync Project Launcher</em>'.
	 * @see apps.PTPSyncProjectLauncher
	 * @generated
	 */
	EClass getPTPSyncProjectLauncher();

	/**
	 * Returns the meta object for the '{@link apps.PTPSyncProjectLauncher#launchProject(apps.SpackPackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see apps.PTPSyncProjectLauncher#launchProject(apps.SpackPackage)
	 * @generated
	 */
	EOperation getPTPSyncProjectLauncher__LaunchProject__SpackPackage();

	/**
	 * Returns the meta object for class '{@link apps.LocalCDTProjectLauncher <em>Local CDT Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Local CDT Project Launcher</em>'.
	 * @see apps.LocalCDTProjectLauncher
	 * @generated
	 */
	EClass getLocalCDTProjectLauncher();

	/**
	 * Returns the meta object for the '{@link apps.LocalCDTProjectLauncher#launchProject(apps.SpackPackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see apps.LocalCDTProjectLauncher#launchProject(apps.SpackPackage)
	 * @generated
	 */
	EOperation getLocalCDTProjectLauncher__LaunchProject__SpackPackage();

	/**
	 * Returns the meta object for enum '{@link apps.EnvironmentType <em>Environment Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Environment Type</em>'.
	 * @see apps.EnvironmentType
	 * @generated
	 */
	EEnum getEnvironmentType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AppsFactory getAppsFactory();

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
		 * The meta object literal for the '{@link apps.IEnvironment <em>IEnvironment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.IEnvironment
		 * @see apps.impl.AppsPackageImpl#getIEnvironment()
		 * @generated
		 */
		EClass IENVIRONMENT = eINSTANCE.getIEnvironment();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__TYPE = eINSTANCE.getIEnvironment_Type();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__NAME = eINSTANCE.getIEnvironment_Name();

		/**
		 * The meta object literal for the '<em><b>Os</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__OS = eINSTANCE.getIEnvironment_Os();

		/**
		 * The meta object literal for the '<em><b>Dependent Packages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENVIRONMENT__DEPENDENT_PACKAGES = eINSTANCE.getIEnvironment_DependentPackages();

		/**
		 * The meta object literal for the '<em><b>Development Environment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__DEVELOPMENT_ENVIRONMENT = eINSTANCE.getIEnvironment_DevelopmentEnvironment();

		/**
		 * The meta object literal for the '<em><b>Generate Project</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__GENERATE_PROJECT = eINSTANCE.getIEnvironment_GenerateProject();

		/**
		 * The meta object literal for the '<em><b>Primary App</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENVIRONMENT__PRIMARY_APP = eINSTANCE.getIEnvironment_PrimaryApp();

		/**
		 * The meta object literal for the '<em><b>Launch</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IENVIRONMENT___LAUNCH = eINSTANCE.getIEnvironment__Launch();

		/**
		 * The meta object literal for the '{@link apps.impl.EnvironmentManagerImpl <em>Environment Manager</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.EnvironmentManagerImpl
		 * @see apps.impl.AppsPackageImpl#getEnvironmentManager()
		 * @generated
		 */
		EClass ENVIRONMENT_MANAGER = eINSTANCE.getEnvironmentManager();

		/**
		 * The meta object literal for the '<em><b>Create Environment</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING = eINSTANCE.getEnvironmentManager__CreateEnvironment__String();

		/**
		 * The meta object literal for the '<em><b>List Existing Environments</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LIST_EXISTING_ENVIRONMENTS = eINSTANCE.getEnvironmentManager__ListExistingEnvironments();

		/**
		 * The meta object literal for the '<em><b>Load Existing Environment</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LOAD_EXISTING_ENVIRONMENT__STRING = eINSTANCE.getEnvironmentManager__LoadExistingEnvironment__String();

		/**
		 * The meta object literal for the '<em><b>Load Environment From File</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT_FROM_FILE__STRING = eINSTANCE.getEnvironmentManager__LoadEnvironmentFromFile__String();

		/**
		 * The meta object literal for the '<em><b>Persist To XMI String</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___PERSIST_TO_XMI_STRING__IENVIRONMENT = eINSTANCE.getEnvironmentManager__PersistToXMIString__IEnvironment();

		/**
		 * The meta object literal for the '<em><b>Persist XMI To File</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___PERSIST_XMI_TO_FILE__IENVIRONMENT_STRING = eINSTANCE.getEnvironmentManager__PersistXMIToFile__IEnvironment_String();

		/**
		 * The meta object literal for the '{@link apps.impl.SpackPackageImpl <em>Spack Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.SpackPackageImpl
		 * @see apps.impl.AppsPackageImpl#getSpackPackage()
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
		 * The meta object literal for the '<em><b>Compiler</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__COMPILER = eINSTANCE.getSpackPackage_Compiler();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__VERSION = eINSTANCE.getSpackPackage_Version();

		/**
		 * The meta object literal for the '<em><b>Cpp Flags</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__CPP_FLAGS = eINSTANCE.getSpackPackage_CppFlags();

		/**
		 * The meta object literal for the '<em><b>Virtual Dependency</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__VIRTUAL_DEPENDENCY = eINSTANCE.getSpackPackage_VirtualDependency();

		/**
		 * The meta object literal for the '<em><b>Virtual Dependency Provider</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER = eINSTANCE.getSpackPackage_VirtualDependencyProvider();

		/**
		 * The meta object literal for the '<em><b>Repo URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__REPO_URL = eINSTANCE.getSpackPackage_RepoURL();

		/**
		 * The meta object literal for the '<em><b>Branch</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__BRANCH = eINSTANCE.getSpackPackage_Branch();

		/**
		 * The meta object literal for the '{@link apps.impl.EnvironmentImpl <em>Environment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.EnvironmentImpl
		 * @see apps.impl.AppsPackageImpl#getEnvironment()
		 * @generated
		 */
		EClass ENVIRONMENT = eINSTANCE.getEnvironment();

		/**
		 * The meta object literal for the '<em><b>Projectlauncher</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENVIRONMENT__PROJECTLAUNCHER = eINSTANCE.getEnvironment_Projectlauncher();

		/**
		 * The meta object literal for the '<em><b>Launch</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT___LAUNCH = eINSTANCE.getEnvironment__Launch();

		/**
		 * The meta object literal for the '<em><b>Launch Derived</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT___LAUNCH_DERIVED = eINSTANCE.getEnvironment__LaunchDerived();

		/**
		 * The meta object literal for the '{@link apps.ProjectLauncher <em>Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.ProjectLauncher
		 * @see apps.impl.AppsPackageImpl#getProjectLauncher()
		 * @generated
		 */
		EClass PROJECT_LAUNCHER = eINSTANCE.getProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE = eINSTANCE.getProjectLauncher__LaunchProject__SpackPackage();

		/**
		 * The meta object literal for the '{@link apps.impl.PTPSyncProjectLauncherImpl <em>PTP Sync Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.PTPSyncProjectLauncherImpl
		 * @see apps.impl.AppsPackageImpl#getPTPSyncProjectLauncher()
		 * @generated
		 */
		EClass PTP_SYNC_PROJECT_LAUNCHER = eINSTANCE.getPTPSyncProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE = eINSTANCE.getPTPSyncProjectLauncher__LaunchProject__SpackPackage();

		/**
		 * The meta object literal for the '{@link apps.impl.LocalCDTProjectLauncherImpl <em>Local CDT Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.LocalCDTProjectLauncherImpl
		 * @see apps.impl.AppsPackageImpl#getLocalCDTProjectLauncher()
		 * @generated
		 */
		EClass LOCAL_CDT_PROJECT_LAUNCHER = eINSTANCE.getLocalCDTProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOCAL_CDT_PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE = eINSTANCE.getLocalCDTProjectLauncher__LaunchProject__SpackPackage();

		/**
		 * The meta object literal for the '{@link apps.EnvironmentType <em>Environment Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.EnvironmentType
		 * @see apps.impl.AppsPackageImpl#getEnvironmentType()
		 * @generated
		 */
		EEnum ENVIRONMENT_TYPE = eINSTANCE.getEnvironmentType();

	}

} //AppsPackage
