/**
 */
package apps.eclipse.util;

import apps.EnvironmentStorage;
import apps.LanguageProjectProvider;
import apps.ProjectLauncher;

import apps.docker.DockerProjectLauncher;
import apps.eclipse.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

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
 * @see apps.eclipse.EclipsePackage
 * @generated
 */
public class EclipseSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EclipsePackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseSwitch() {
		if (modelPackage == null) {
			modelPackage = EclipsePackage.eINSTANCE;
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
			case EclipsePackage.ECLIPSE_ENVIRONMENT_STORAGE: {
				EclipseEnvironmentStorage eclipseEnvironmentStorage = (EclipseEnvironmentStorage)theEObject;
				T result = caseEclipseEnvironmentStorage(eclipseEnvironmentStorage);
				if (result == null) result = caseEnvironmentStorage(eclipseEnvironmentStorage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EclipsePackage.DOCKER_PTP_SYNC_PROJECT_LAUNCHER: {
				DockerPTPSyncProjectLauncher dockerPTPSyncProjectLauncher = (DockerPTPSyncProjectLauncher)theEObject;
				T result = caseDockerPTPSyncProjectLauncher(dockerPTPSyncProjectLauncher);
				if (result == null) result = caseDockerProjectLauncher(dockerPTPSyncProjectLauncher);
				if (result == null) result = caseProjectLauncher(dockerPTPSyncProjectLauncher);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EclipsePackage.ECLIPSE_CPP_PROJECT_PROVIDER: {
				EclipseCppProjectProvider eclipseCppProjectProvider = (EclipseCppProjectProvider)theEObject;
				T result = caseEclipseCppProjectProvider(eclipseCppProjectProvider);
				if (result == null) result = caseLanguageProjectProvider(eclipseCppProjectProvider);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Environment Storage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Environment Storage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEclipseEnvironmentStorage(EclipseEnvironmentStorage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Docker PTP Sync Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Docker PTP Sync Project Launcher</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDockerPTPSyncProjectLauncher(DockerPTPSyncProjectLauncher object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cpp Project Provider</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cpp Project Provider</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEclipseCppProjectProvider(EclipseCppProjectProvider object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Environment Storage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Environment Storage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnvironmentStorage(EnvironmentStorage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Project Launcher</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProjectLauncher(ProjectLauncher object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Project Launcher</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDockerProjectLauncher(DockerProjectLauncher object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Language Project Provider</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Language Project Provider</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLanguageProjectProvider(LanguageProjectProvider object) {
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

} //EclipseSwitch
