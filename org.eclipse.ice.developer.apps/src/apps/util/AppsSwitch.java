/**
 */
package apps.util;

import apps.*;

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
 * @see apps.AppsPackage
 * @generated
 */
public class AppsSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AppsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AppsSwitch() {
		if (modelPackage == null) {
			modelPackage = AppsPackage.eINSTANCE;
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
			case AppsPackage.IENVIRONMENT: {
				IEnvironment iEnvironment = (IEnvironment)theEObject;
				T result = caseIEnvironment(iEnvironment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.IENVIRONMENT_BUILDER: {
				IEnvironmentBuilder iEnvironmentBuilder = (IEnvironmentBuilder)theEObject;
				T result = caseIEnvironmentBuilder(iEnvironmentBuilder);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.ENVIRONMENT_MANAGER: {
				EnvironmentManager environmentManager = (EnvironmentManager)theEObject;
				T result = caseEnvironmentManager(environmentManager);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.SPACK_PACKAGE: {
				SpackPackage spackPackage = (SpackPackage)theEObject;
				T result = caseSpackPackage(spackPackage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.ENVIRONMENT: {
				Environment environment = (Environment)theEObject;
				T result = caseEnvironment(environment);
				if (result == null) result = caseIEnvironment(environment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.PROJECT_LAUNCHER: {
				ProjectLauncher projectLauncher = (ProjectLauncher)theEObject;
				T result = caseProjectLauncher(projectLauncher);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.PTP_SYNC_PROJECT_LAUNCHER: {
				PTPSyncProjectLauncher ptpSyncProjectLauncher = (PTPSyncProjectLauncher)theEObject;
				T result = casePTPSyncProjectLauncher(ptpSyncProjectLauncher);
				if (result == null) result = caseProjectLauncher(ptpSyncProjectLauncher);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.LOCAL_CDT_PROJECT_LAUNCHER: {
				LocalCDTProjectLauncher localCDTProjectLauncher = (LocalCDTProjectLauncher)theEObject;
				T result = caseLocalCDTProjectLauncher(localCDTProjectLauncher);
				if (result == null) result = caseProjectLauncher(localCDTProjectLauncher);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.SCIENCE_APP: {
				ScienceApp scienceApp = (ScienceApp)theEObject;
				T result = caseScienceApp(scienceApp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IEnvironment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEnvironment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIEnvironment(IEnvironment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IEnvironment Builder</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEnvironment Builder</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIEnvironmentBuilder(IEnvironmentBuilder object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Environment Manager</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Environment Manager</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnvironmentManager(EnvironmentManager object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Environment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Environment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnvironment(Environment object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>PTP Sync Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>PTP Sync Project Launcher</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePTPSyncProjectLauncher(PTPSyncProjectLauncher object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Local CDT Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Local CDT Project Launcher</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocalCDTProjectLauncher(LocalCDTProjectLauncher object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Science App</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Science App</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScienceApp(ScienceApp object) {
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

} //AppsSwitch
