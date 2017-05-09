/**
 */
package org.eclipse.ice.developer.apps.docker.environment;

import apps.IEnvironment;
import org.eclipse.ice.docker.api.ContainerConfiguration;
import org.eclipse.ice.docker.api.DockerAPI;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Docker Environment</b></em>'.
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
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getContainerConfiguration <em>Container Configuration</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerfile <em>Dockerfile</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerAPI <em>Docker API</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#getDockerEnvironment()
 * @model
 * @generated
 */
public interface DockerEnvironment extends IEnvironment {
	/**
	 * Returns the value of the '<em><b>Container Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to the container configuration data needed to launch a new docker container. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Container Configuration</em>' containment reference.
	 * @see #setContainerConfiguration(ContainerConfiguration)
	 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#getDockerEnvironment_ContainerConfiguration()
	 * @model containment="true"
	 * @generated
	 */
	ContainerConfiguration getContainerConfiguration();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getContainerConfiguration <em>Container Configuration</em>}' containment reference.
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
	 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#getDockerEnvironment_Dockerfile()
	 * @model
	 * @generated
	 */
	String getDockerfile();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerfile <em>Dockerfile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dockerfile</em>' attribute.
	 * @see #getDockerfile()
	 * @generated
	 */
	void setDockerfile(String value);

	/**
	 * Returns the value of the '<em><b>Docker API</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Docker API</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Docker API</em>' reference.
	 * @see #setDockerAPI(DockerAPI)
	 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#getDockerEnvironment_DockerAPI()
	 * @model required="true"
	 * @generated
	 */
	DockerAPI getDockerAPI();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerAPI <em>Docker API</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Docker API</em>' reference.
	 * @see #getDockerAPI()
	 * @generated
	 */
	void setDockerAPI(DockerAPI value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model commandDataType="org.eclipse.ice.docker.api.CommandList"
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
