/**
 */
package apps.docker;

import apps.IEnvironment;

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
 *   <li>{@link apps.docker.DockerEnvironment#getContainerConfiguration <em>Container Configuration</em>}</li>
 *   <li>{@link apps.docker.DockerEnvironment#getDockerfile <em>Dockerfile</em>}</li>
 * </ul>
 *
 * @see apps.docker.DockerPackage#getDockerEnvironment()
 * @model
 * @generated
 */
public interface DockerEnvironment extends IEnvironment {
	/**
	 * Returns the value of the '<em><b>Docker</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Docker</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to the DockerAPI that enables the building of docker images, creation of docker containers from those images, re-connection to existing 'exited' containers, and the deletion of containers and images. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Docker</em>' containment reference.
	 * @see #setDocker(DockerAPI)
	 * @see apps.docker.DockerPackage#getDockerEnvironment_Docker()
	 * @model containment="true"
	 * @generated
	 */
	DockerAPI getDocker();

	/**
	 * Sets the value of the '{@link apps.docker.DockerEnvironment#getDocker <em>Docker</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Docker</em>' containment reference.
	 * @see #getDocker()
	 * @generated
	 */
	void setDocker(DockerAPI value);

	/**
	 * Returns the value of the '<em><b>Container Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Container Configuration</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to the container configuration data needed to launch a new docker container. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Container Configuration</em>' containment reference.
	 * @see #setContainerConfiguration(ContainerConfiguration)
	 * @see apps.docker.DockerPackage#getDockerEnvironment_ContainerConfiguration()
	 * @model containment="true"
	 * @generated
	 */
	ContainerConfiguration getContainerConfiguration();

	/**
	 * Sets the value of the '{@link apps.docker.DockerEnvironment#getContainerConfiguration <em>Container Configuration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Container Configuration</em>' containment reference.
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	void setContainerConfiguration(ContainerConfiguration value);

	/**
	 * Returns the value of the '<em><b>Dockerfile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to the String contents of the Dockerfile created to model this DockerEnvironment.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dockerfile</em>' attribute.
	 * @see #setDockerfile(String)
	 * @see apps.docker.DockerPackage#getDockerEnvironment_Dockerfile()
	 * @model
	 * @generated
	 */
	String getDockerfile();

	/**
	 * Sets the value of the '{@link apps.docker.DockerEnvironment#getDockerfile <em>Dockerfile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dockerfile</em>' attribute.
	 * @see #getDockerfile()
	 * @generated
	 */
	void setDockerfile(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model commandDataType="apps.docker.CommandList"
	 * @generated
	 */
	String execute(String imageName, String[] command);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean hasDocker();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void pullImage(String imageName);

} // DockerEnvironment
