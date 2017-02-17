/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage
 * @generated
 */
public class ScenvironmentAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ScenvironmentPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenvironmentAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ScenvironmentPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenvironmentSwitch<Adapter> modelSwitch =
		new ScenvironmentSwitch<Adapter>() {
			@Override
			public Adapter caseSCEnvironment(SCEnvironment object) {
				return createSCEnvironmentAdapter();
			}
			@Override
			public Adapter caseSpackPackage(SpackPackage object) {
				return createSpackPackageAdapter();
			}
			@Override
			public Adapter caseContainerConfiguration(ContainerConfiguration object) {
				return createContainerConfigurationAdapter();
			}
			@Override
			public Adapter caseInstaller(Installer object) {
				return createInstallerAdapter();
			}
			@Override
			public Adapter caseDockerInstaller(DockerInstaller object) {
				return createDockerInstallerAdapter();
			}
			@Override
			public Adapter caseFileSystemInstaller(FileSystemInstaller object) {
				return createFileSystemInstallerAdapter();
			}
			@Override
			public Adapter caseSCEnvironmentDataManager(SCEnvironmentDataManager object) {
				return createSCEnvironmentDataManagerAdapter();
			}
			@Override
			public Adapter caseFileSystemConfiguration(FileSystemConfiguration object) {
				return createFileSystemConfigurationAdapter();
			}
			@Override
			public Adapter caseInstallerTypeConfiguration(InstallerTypeConfiguration object) {
				return createInstallerTypeConfigurationAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment <em>SC Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment
	 * @generated
	 */
	public Adapter createSCEnvironmentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage <em>Spack Package</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage
	 * @generated
	 */
	public Adapter createSpackPackageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration <em>Container Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration
	 * @generated
	 */
	public Adapter createContainerConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer <em>Installer</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.Installer
	 * @generated
	 */
	public Adapter createInstallerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller <em>Docker Installer</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller
	 * @generated
	 */
	public Adapter createDockerInstallerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller <em>File System Installer</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller
	 * @generated
	 */
	public Adapter createFileSystemInstallerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager <em>SC Environment Data Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager
	 * @generated
	 */
	public Adapter createSCEnvironmentDataManagerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration <em>File System Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration
	 * @generated
	 */
	public Adapter createFileSystemConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration <em>Installer Type Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration
	 * @generated
	 */
	public Adapter createInstallerTypeConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ScenvironmentAdapterFactory
