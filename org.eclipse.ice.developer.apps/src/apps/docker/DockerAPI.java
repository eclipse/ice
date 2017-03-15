/**
 */
package apps.docker;

import org.eclipse.emf.ecore.EObject;

import apps.EnvironmentConsole;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>API</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The DockerAPI provides a high-level abstraction for performing Docker-specific tasks. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.DockerAPI#getContainerRemotePort <em>Container Remote Port</em>}</li>
 *   <li>{@link apps.docker.DockerAPI#getSshContainerId <em>Ssh Container Id</em>}</li>
 * </ul>
 *
 * @see apps.docker.DockerPackage#getDockerAPI()
 * @model
 * @generated
 */
public interface DockerAPI extends EObject {
	/**
	 * Returns the value of the '<em><b>Container Remote Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Container Remote Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Container Remote Port</em>' attribute.
	 * @see #setContainerRemotePort(int)
	 * @see apps.docker.DockerPackage#getDockerAPI_ContainerRemotePort()
	 * @model
	 * @generated
	 */
	int getContainerRemotePort();

	/**
	 * Sets the value of the '{@link apps.docker.DockerAPI#getContainerRemotePort <em>Container Remote Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Container Remote Port</em>' attribute.
	 * @see #getContainerRemotePort()
	 * @generated
	 */
	void setContainerRemotePort(int value);

	/**
	 * Returns the value of the '<em><b>Ssh Container Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ssh Container Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ssh Container Id</em>' attribute.
	 * @see #setSshContainerId(String)
	 * @see apps.docker.DockerPackage#getDockerAPI_SshContainerId()
	 * @model
	 * @generated
	 */
	String getSshContainerId();

	/**
	 * Sets the value of the '{@link apps.docker.DockerAPI#getSshContainerId <em>Ssh Container Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ssh Container Id</em>' attribute.
	 * @see #getSshContainerId()
	 * @generated
	 */
	void setSshContainerId(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Build the image described by the provided Dockerfile build string. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean buildImage(String buildFile, String imagename);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Launch a new container from the provided image name and ContainerConfiguration object. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean createContainer(String imageName, ContainerConfiguration config);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Connect to the container with the given name or id. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean connectToExistingContainer(String id);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Delete the container with the given name. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean deleteContainer(String id);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Delete the image with the given name. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean deleteImage(String imageName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean stopContainer(String id);
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model commandDataType="apps.docker.CommandList"
	 * @generated
	 */
	String createContainerExecCommand(String imageName, String[] command);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void pull(String imageName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EList<String> listAvailableImages();

	void setEnvironmentConsole(EnvironmentConsole c);

} // DockerAPI
