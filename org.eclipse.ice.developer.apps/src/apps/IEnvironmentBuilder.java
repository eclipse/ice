/**
 */
package apps;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IEnvironment Builder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The IEnvironmentBuilder provides a common interface for building new IEnvironment instances. 
 * <!-- end-model-doc -->
 *
 *
 * @see apps.EnvironmentPackage#getIEnvironmentBuilder()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IEnvironmentBuilder extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return a new IEnvironment instance. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment build(String properties);

} // IEnvironmentBuilder
