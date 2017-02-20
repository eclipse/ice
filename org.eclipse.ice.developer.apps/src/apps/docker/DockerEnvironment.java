/**
 */
package apps.docker;

import apps.Environment;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The DockerEnvironment is a realization of the IEnvironment interface that provides a launch method to create a scientific application environment in a Docker container. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.DockerEnvironment#getDocker <em>Docker</em>}</li>
 * </ul>
 *
 * @see apps.docker.DockerPackage#getDockerEnvironment()
 * @model
 * @generated
 */
public interface DockerEnvironment extends Environment {
	/**
	 * Returns the value of the '<em><b>Docker</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Docker</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Docker</em>' reference.
	 * @see #setDocker(DockerAPI)
	 * @see apps.docker.DockerPackage#getDockerEnvironment_Docker()
	 * @model
	 * @generated
	 */
	DockerAPI getDocker();

	/**
	 * Sets the value of the '{@link apps.docker.DockerEnvironment#getDocker <em>Docker</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Docker</em>' reference.
	 * @see #getDocker()
	 * @generated
	 */
	void setDocker(DockerAPI value);

} // DockerEnvironment
