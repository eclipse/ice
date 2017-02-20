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
 * @see apps.EnvironmentFactory
 * @model kind="package"
 * @generated
 */
public interface EnvironmentPackage extends EPackage {
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
	EnvironmentPackage eINSTANCE = apps.impl.EnvironmentPackageImpl.init();

	/**
	 * The meta object id for the '{@link apps.IEnvironment <em>IEnvironment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment
	 * @see apps.impl.EnvironmentPackageImpl#getIEnvironment()
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
	 * The feature id for the '<em><b>Spackpackage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__SPACKPACKAGE = 3;

	/**
	 * The feature id for the '<em><b>Scienceapp</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__SCIENCEAPP = 4;

	/**
	 * The number of structural features of the '<em>IEnvironment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_FEATURE_COUNT = 5;

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
	 * The meta object id for the '{@link apps.IEnvironmentBuilder <em>IEnvironment Builder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironmentBuilder
	 * @see apps.impl.EnvironmentPackageImpl#getIEnvironmentBuilder()
	 * @generated
	 */
	int IENVIRONMENT_BUILDER = 1;

	/**
	 * The number of structural features of the '<em>IEnvironment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_BUILDER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Build</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_BUILDER___BUILD__STRING = 0;

	/**
	 * The number of operations of the '<em>IEnvironment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_BUILDER_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link apps.impl.EnvironmentManagerImpl <em>Manager</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.EnvironmentManagerImpl
	 * @see apps.impl.EnvironmentPackageImpl#getEnvironmentManager()
	 * @generated
	 */
	int ENVIRONMENT_MANAGER = 2;

	/**
	 * The feature id for the '<em><b>Builder</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER__BUILDER = 0;

	/**
	 * The number of structural features of the '<em>Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Create Environment</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING = 0;

	/**
	 * The operation id for the '<em>List Existing</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LIST_EXISTING = 1;

	/**
	 * The operation id for the '<em>Load Existing</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LOAD_EXISTING__STRING = 2;

	/**
	 * The number of operations of the '<em>Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER_OPERATION_COUNT = 3;

	/**
	 * The meta object id for the '{@link apps.impl.SpackPackageImpl <em>Spack Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.SpackPackageImpl
	 * @see apps.impl.EnvironmentPackageImpl#getSpackPackage()
	 * @generated
	 */
	int SPACK_PACKAGE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Desired Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__DESIRED_VERSION = 1;

	/**
	 * The feature id for the '<em><b>Compiler</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__COMPILER = 2;

	/**
	 * The feature id for the '<em><b>Versions</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VERSIONS = 3;

	/**
	 * The feature id for the '<em><b>Cpp Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__CPP_FLAGS = 4;

	/**
	 * The feature id for the '<em><b>Virtual Dependency</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VIRTUAL_DEPENDENCY = 5;

	/**
	 * The feature id for the '<em><b>Virtual Dependency Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER = 6;

	/**
	 * The number of structural features of the '<em>Spack Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE_FEATURE_COUNT = 7;

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
	 * @see apps.impl.EnvironmentPackageImpl#getEnvironment()
	 * @generated
	 */
	int ENVIRONMENT = 4;

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
	 * The feature id for the '<em><b>Spackpackage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__SPACKPACKAGE = IENVIRONMENT__SPACKPACKAGE;

	/**
	 * The feature id for the '<em><b>Scienceapp</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT__SCIENCEAPP = IENVIRONMENT__SCIENCEAPP;

	/**
	 * The feature id for the '<em><b>Projectlauncher</b></em>' reference.
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
	int ENVIRONMENT___LAUNCH = IENVIRONMENT___LAUNCH;

	/**
	 * The number of operations of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_OPERATION_COUNT = IENVIRONMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.ProjectLauncher <em>Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.ProjectLauncher
	 * @see apps.impl.EnvironmentPackageImpl#getProjectLauncher()
	 * @generated
	 */
	int PROJECT_LAUNCHER = 5;

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
	int PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP = 0;

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
	 * @see apps.impl.EnvironmentPackageImpl#getPTPSyncProjectLauncher()
	 * @generated
	 */
	int PTP_SYNC_PROJECT_LAUNCHER = 6;

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
	int PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP = PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP;

	/**
	 * The number of operations of the '<em>PTP Sync Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PTP_SYNC_PROJECT_LAUNCHER_OPERATION_COUNT = PROJECT_LAUNCHER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.impl.LocalCDTProjectLauncherImpl <em>Local CDT Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.LocalCDTProjectLauncherImpl
	 * @see apps.impl.EnvironmentPackageImpl#getLocalCDTProjectLauncher()
	 * @generated
	 */
	int LOCAL_CDT_PROJECT_LAUNCHER = 7;

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
	int LOCAL_CDT_PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP = PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP;

	/**
	 * The number of operations of the '<em>Local CDT Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_CDT_PROJECT_LAUNCHER_OPERATION_COUNT = PROJECT_LAUNCHER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.impl.ScienceAppImpl <em>Science App</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.ScienceAppImpl
	 * @see apps.impl.EnvironmentPackageImpl#getScienceApp()
	 * @generated
	 */
	int SCIENCE_APP = 8;

	/**
	 * The feature id for the '<em><b>Repo URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__REPO_URL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__NAME = 1;

	/**
	 * The feature id for the '<em><b>Branch</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__BRANCH = 2;

	/**
	 * The feature id for the '<em><b>Local Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__LOCAL_PROJECT_NAME = 3;

	/**
	 * The feature id for the '<em><b>Recursive Clone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__RECURSIVE_CLONE = 4;

	/**
	 * The feature id for the '<em><b>Absolute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__ABSOLUTE_PATH = 5;

	/**
	 * The feature id for the '<em><b>Remote Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__REMOTE_PORT = 6;

	/**
	 * The feature id for the '<em><b>Remote Host</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP__REMOTE_HOST = 7;

	/**
	 * The number of structural features of the '<em>Science App</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Science App</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCIENCE_APP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link apps.EnvironmentType <em>Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentType
	 * @see apps.impl.EnvironmentPackageImpl#getEnvironmentType()
	 * @generated
	 */
	int ENVIRONMENT_TYPE = 9;


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
	 * Returns the meta object for the containment reference list '{@link apps.IEnvironment#getSpackpackage <em>Spackpackage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Spackpackage</em>'.
	 * @see apps.IEnvironment#getSpackpackage()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EReference getIEnvironment_Spackpackage();

	/**
	 * Returns the meta object for the reference '{@link apps.IEnvironment#getScienceapp <em>Scienceapp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Scienceapp</em>'.
	 * @see apps.IEnvironment#getScienceapp()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EReference getIEnvironment_Scienceapp();

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
	 * Returns the meta object for class '{@link apps.IEnvironmentBuilder <em>IEnvironment Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEnvironment Builder</em>'.
	 * @see apps.IEnvironmentBuilder
	 * @generated
	 */
	EClass getIEnvironmentBuilder();

	/**
	 * Returns the meta object for the '{@link apps.IEnvironmentBuilder#build(java.lang.String) <em>Build</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Build</em>' operation.
	 * @see apps.IEnvironmentBuilder#build(java.lang.String)
	 * @generated
	 */
	EOperation getIEnvironmentBuilder__Build__String();

	/**
	 * Returns the meta object for class '{@link apps.EnvironmentManager <em>Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Manager</em>'.
	 * @see apps.EnvironmentManager
	 * @generated
	 */
	EClass getEnvironmentManager();

	/**
	 * Returns the meta object for the reference '{@link apps.EnvironmentManager#getBuilder <em>Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Builder</em>'.
	 * @see apps.EnvironmentManager#getBuilder()
	 * @see #getEnvironmentManager()
	 * @generated
	 */
	EReference getEnvironmentManager_Builder();

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
	 * Returns the meta object for the '{@link apps.EnvironmentManager#listExisting() <em>List Existing</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>List Existing</em>' operation.
	 * @see apps.EnvironmentManager#listExisting()
	 * @generated
	 */
	EOperation getEnvironmentManager__ListExisting();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#loadExisting(java.lang.String) <em>Load Existing</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Load Existing</em>' operation.
	 * @see apps.EnvironmentManager#loadExisting(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__LoadExisting__String();

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
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getDesiredVersion <em>Desired Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Desired Version</em>'.
	 * @see apps.SpackPackage#getDesiredVersion()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_DesiredVersion();

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
	 * Returns the meta object for the attribute list '{@link apps.SpackPackage#getVersions <em>Versions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Versions</em>'.
	 * @see apps.SpackPackage#getVersions()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Versions();

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
	 * Returns the meta object for class '{@link apps.Environment <em>Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment</em>'.
	 * @see apps.Environment
	 * @generated
	 */
	EClass getEnvironment();

	/**
	 * Returns the meta object for the reference '{@link apps.Environment#getProjectlauncher <em>Projectlauncher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Projectlauncher</em>'.
	 * @see apps.Environment#getProjectlauncher()
	 * @see #getEnvironment()
	 * @generated
	 */
	EReference getEnvironment_Projectlauncher();

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
	 * Returns the meta object for the '{@link apps.ProjectLauncher#launchProject(apps.ScienceApp) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see apps.ProjectLauncher#launchProject(apps.ScienceApp)
	 * @generated
	 */
	EOperation getProjectLauncher__LaunchProject__ScienceApp();

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
	 * Returns the meta object for class '{@link apps.LocalCDTProjectLauncher <em>Local CDT Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Local CDT Project Launcher</em>'.
	 * @see apps.LocalCDTProjectLauncher
	 * @generated
	 */
	EClass getLocalCDTProjectLauncher();

	/**
	 * Returns the meta object for class '{@link apps.ScienceApp <em>Science App</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Science App</em>'.
	 * @see apps.ScienceApp
	 * @generated
	 */
	EClass getScienceApp();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#getRepoURL <em>Repo URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repo URL</em>'.
	 * @see apps.ScienceApp#getRepoURL()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_RepoURL();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see apps.ScienceApp#getName()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_Name();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#getBranch <em>Branch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Branch</em>'.
	 * @see apps.ScienceApp#getBranch()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_Branch();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#getLocalProjectName <em>Local Project Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Local Project Name</em>'.
	 * @see apps.ScienceApp#getLocalProjectName()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_LocalProjectName();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#isRecursiveClone <em>Recursive Clone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Recursive Clone</em>'.
	 * @see apps.ScienceApp#isRecursiveClone()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_RecursiveClone();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#getAbsolutePath <em>Absolute Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Absolute Path</em>'.
	 * @see apps.ScienceApp#getAbsolutePath()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_AbsolutePath();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#getRemotePort <em>Remote Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote Port</em>'.
	 * @see apps.ScienceApp#getRemotePort()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_RemotePort();

	/**
	 * Returns the meta object for the attribute '{@link apps.ScienceApp#getRemoteHost <em>Remote Host</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote Host</em>'.
	 * @see apps.ScienceApp#getRemoteHost()
	 * @see #getScienceApp()
	 * @generated
	 */
	EAttribute getScienceApp_RemoteHost();

	/**
	 * Returns the meta object for enum '{@link apps.EnvironmentType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Type</em>'.
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
	EnvironmentFactory getEnvironmentFactory();

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
		 * @see apps.impl.EnvironmentPackageImpl#getIEnvironment()
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
		 * The meta object literal for the '<em><b>Spackpackage</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENVIRONMENT__SPACKPACKAGE = eINSTANCE.getIEnvironment_Spackpackage();

		/**
		 * The meta object literal for the '<em><b>Scienceapp</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENVIRONMENT__SCIENCEAPP = eINSTANCE.getIEnvironment_Scienceapp();

		/**
		 * The meta object literal for the '<em><b>Launch</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IENVIRONMENT___LAUNCH = eINSTANCE.getIEnvironment__Launch();

		/**
		 * The meta object literal for the '{@link apps.IEnvironmentBuilder <em>IEnvironment Builder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.IEnvironmentBuilder
		 * @see apps.impl.EnvironmentPackageImpl#getIEnvironmentBuilder()
		 * @generated
		 */
		EClass IENVIRONMENT_BUILDER = eINSTANCE.getIEnvironmentBuilder();

		/**
		 * The meta object literal for the '<em><b>Build</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IENVIRONMENT_BUILDER___BUILD__STRING = eINSTANCE.getIEnvironmentBuilder__Build__String();

		/**
		 * The meta object literal for the '{@link apps.impl.EnvironmentManagerImpl <em>Manager</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.EnvironmentManagerImpl
		 * @see apps.impl.EnvironmentPackageImpl#getEnvironmentManager()
		 * @generated
		 */
		EClass ENVIRONMENT_MANAGER = eINSTANCE.getEnvironmentManager();

		/**
		 * The meta object literal for the '<em><b>Builder</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENVIRONMENT_MANAGER__BUILDER = eINSTANCE.getEnvironmentManager_Builder();

		/**
		 * The meta object literal for the '<em><b>Create Environment</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING = eINSTANCE.getEnvironmentManager__CreateEnvironment__String();

		/**
		 * The meta object literal for the '<em><b>List Existing</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LIST_EXISTING = eINSTANCE.getEnvironmentManager__ListExisting();

		/**
		 * The meta object literal for the '<em><b>Load Existing</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LOAD_EXISTING__STRING = eINSTANCE.getEnvironmentManager__LoadExisting__String();

		/**
		 * The meta object literal for the '{@link apps.impl.SpackPackageImpl <em>Spack Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.SpackPackageImpl
		 * @see apps.impl.EnvironmentPackageImpl#getSpackPackage()
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
		 * The meta object literal for the '<em><b>Desired Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__DESIRED_VERSION = eINSTANCE.getSpackPackage_DesiredVersion();

		/**
		 * The meta object literal for the '<em><b>Compiler</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__COMPILER = eINSTANCE.getSpackPackage_Compiler();

		/**
		 * The meta object literal for the '<em><b>Versions</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__VERSIONS = eINSTANCE.getSpackPackage_Versions();

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
		 * The meta object literal for the '{@link apps.impl.EnvironmentImpl <em>Environment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.EnvironmentImpl
		 * @see apps.impl.EnvironmentPackageImpl#getEnvironment()
		 * @generated
		 */
		EClass ENVIRONMENT = eINSTANCE.getEnvironment();

		/**
		 * The meta object literal for the '<em><b>Projectlauncher</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENVIRONMENT__PROJECTLAUNCHER = eINSTANCE.getEnvironment_Projectlauncher();

		/**
		 * The meta object literal for the '{@link apps.ProjectLauncher <em>Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.ProjectLauncher
		 * @see apps.impl.EnvironmentPackageImpl#getProjectLauncher()
		 * @generated
		 */
		EClass PROJECT_LAUNCHER = eINSTANCE.getProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP = eINSTANCE.getProjectLauncher__LaunchProject__ScienceApp();

		/**
		 * The meta object literal for the '{@link apps.impl.PTPSyncProjectLauncherImpl <em>PTP Sync Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.PTPSyncProjectLauncherImpl
		 * @see apps.impl.EnvironmentPackageImpl#getPTPSyncProjectLauncher()
		 * @generated
		 */
		EClass PTP_SYNC_PROJECT_LAUNCHER = eINSTANCE.getPTPSyncProjectLauncher();

		/**
		 * The meta object literal for the '{@link apps.impl.LocalCDTProjectLauncherImpl <em>Local CDT Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.LocalCDTProjectLauncherImpl
		 * @see apps.impl.EnvironmentPackageImpl#getLocalCDTProjectLauncher()
		 * @generated
		 */
		EClass LOCAL_CDT_PROJECT_LAUNCHER = eINSTANCE.getLocalCDTProjectLauncher();

		/**
		 * The meta object literal for the '{@link apps.impl.ScienceAppImpl <em>Science App</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.ScienceAppImpl
		 * @see apps.impl.EnvironmentPackageImpl#getScienceApp()
		 * @generated
		 */
		EClass SCIENCE_APP = eINSTANCE.getScienceApp();

		/**
		 * The meta object literal for the '<em><b>Repo URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__REPO_URL = eINSTANCE.getScienceApp_RepoURL();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__NAME = eINSTANCE.getScienceApp_Name();

		/**
		 * The meta object literal for the '<em><b>Branch</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__BRANCH = eINSTANCE.getScienceApp_Branch();

		/**
		 * The meta object literal for the '<em><b>Local Project Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__LOCAL_PROJECT_NAME = eINSTANCE.getScienceApp_LocalProjectName();

		/**
		 * The meta object literal for the '<em><b>Recursive Clone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__RECURSIVE_CLONE = eINSTANCE.getScienceApp_RecursiveClone();

		/**
		 * The meta object literal for the '<em><b>Absolute Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__ABSOLUTE_PATH = eINSTANCE.getScienceApp_AbsolutePath();

		/**
		 * The meta object literal for the '<em><b>Remote Port</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__REMOTE_PORT = eINSTANCE.getScienceApp_RemotePort();

		/**
		 * The meta object literal for the '<em><b>Remote Host</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCIENCE_APP__REMOTE_HOST = eINSTANCE.getScienceApp_RemoteHost();

		/**
		 * The meta object literal for the '{@link apps.EnvironmentType <em>Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.EnvironmentType
		 * @see apps.impl.EnvironmentPackageImpl#getEnvironmentType()
		 * @generated
		 */
		EEnum ENVIRONMENT_TYPE = eINSTANCE.getEnvironmentType();

	}

} //EnvironmentPackage
