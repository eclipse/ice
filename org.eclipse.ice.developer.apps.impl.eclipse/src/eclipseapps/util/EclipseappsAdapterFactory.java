/**
 */
package eclipseapps.util;

import apps.EnvironmentStorage;
import apps.LanguageProjectProvider;
import apps.ProjectLauncher;

import apps.docker.DockerProjectLauncher;

import eclipseapps.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see eclipseapps.EclipseappsPackage
 * @generated
 */
public class EclipseappsAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EclipseappsPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseappsAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = EclipseappsPackage.eINSTANCE;
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
	protected EclipseappsSwitch<Adapter> modelSwitch =
		new EclipseappsSwitch<Adapter>() {
			@Override
			public Adapter caseEclipseEnvironmentStorage(EclipseEnvironmentStorage object) {
				return createEclipseEnvironmentStorageAdapter();
			}
			@Override
			public Adapter caseDockerPTPSyncProjectLauncher(DockerPTPSyncProjectLauncher object) {
				return createDockerPTPSyncProjectLauncherAdapter();
			}
			@Override
			public Adapter caseEclipseCppProjectProvider(EclipseCppProjectProvider object) {
				return createEclipseCppProjectProviderAdapter();
			}
			@Override
			public Adapter caseEnvironmentStorage(EnvironmentStorage object) {
				return createEnvironmentStorageAdapter();
			}
			@Override
			public Adapter caseProjectLauncher(ProjectLauncher object) {
				return createProjectLauncherAdapter();
			}
			@Override
			public Adapter caseDockerProjectLauncher(DockerProjectLauncher object) {
				return createDockerProjectLauncherAdapter();
			}
			@Override
			public Adapter caseLanguageProjectProvider(LanguageProjectProvider object) {
				return createLanguageProjectProviderAdapter();
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
	 * Creates a new adapter for an object of class '{@link eclipseapps.EclipseEnvironmentStorage <em>Eclipse Environment Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eclipseapps.EclipseEnvironmentStorage
	 * @generated
	 */
	public Adapter createEclipseEnvironmentStorageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eclipseapps.DockerPTPSyncProjectLauncher <em>Docker PTP Sync Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eclipseapps.DockerPTPSyncProjectLauncher
	 * @generated
	 */
	public Adapter createDockerPTPSyncProjectLauncherAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eclipseapps.EclipseCppProjectProvider <em>Eclipse Cpp Project Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eclipseapps.EclipseCppProjectProvider
	 * @generated
	 */
	public Adapter createEclipseCppProjectProviderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.EnvironmentStorage <em>Environment Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.EnvironmentStorage
	 * @generated
	 */
	public Adapter createEnvironmentStorageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.ProjectLauncher <em>Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.ProjectLauncher
	 * @generated
	 */
	public Adapter createProjectLauncherAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.docker.DockerProjectLauncher <em>Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.docker.DockerProjectLauncher
	 * @generated
	 */
	public Adapter createDockerProjectLauncherAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.LanguageProjectProvider <em>Language Project Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.LanguageProjectProvider
	 * @generated
	 */
	public Adapter createLanguageProjectProviderAdapter() {
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

} //EclipseappsAdapterFactory
