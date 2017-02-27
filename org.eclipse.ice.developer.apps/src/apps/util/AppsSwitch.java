/**
 */
package apps.util;

import apps.AppsPackage;
import apps.EnvironmentManager;
import apps.IEnvironment;
import apps.OSPackage;
import apps.ProjectLauncher;
import apps.SourcePackage;
import apps.SpackDependency;
import apps.SpackPackage;
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
			case AppsPackage.ENVIRONMENT_MANAGER: {
				EnvironmentManager environmentManager = (EnvironmentManager)theEObject;
				T result = caseEnvironmentManager(environmentManager);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.SPACK_PACKAGE: {
				SpackPackage spackPackage = (SpackPackage)theEObject;
				T result = caseSpackPackage(spackPackage);
				if (result == null) result = casePackage(spackPackage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.PROJECT_LAUNCHER: {
				ProjectLauncher projectLauncher = (ProjectLauncher)theEObject;
				T result = caseProjectLauncher(projectLauncher);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.PACKAGE: {
				apps.Package package_ = (apps.Package)theEObject;
				T result = casePackage(package_);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.SOURCE_PACKAGE: {
				SourcePackage sourcePackage = (SourcePackage)theEObject;
				T result = caseSourcePackage(sourcePackage);
				if (result == null) result = casePackage(sourcePackage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.OS_PACKAGE: {
				OSPackage osPackage = (OSPackage)theEObject;
				T result = caseOSPackage(osPackage);
				if (result == null) result = casePackage(osPackage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AppsPackage.SPACK_DEPENDENCY: {
				SpackDependency spackDependency = (SpackDependency)theEObject;
				T result = caseSpackDependency(spackDependency);
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
	 * Returns the result of interpreting the object as an instance of '<em>Package</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Package</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePackage(apps.Package object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Source Package</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Source Package</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSourcePackage(SourcePackage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>OS Package</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>OS Package</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOSPackage(OSPackage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spack Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spack Dependency</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSpackDependency(SpackDependency object) {
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
