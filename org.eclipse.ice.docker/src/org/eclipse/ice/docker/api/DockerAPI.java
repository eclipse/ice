/**
 */
package org.eclipse.ice.docker.api;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Docker API</b></em>'.
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
 *   <li>{@link org.eclipse.ice.docker.api.DockerAPI#getConsole <em>Console</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.docker.api.DockerapiPackage#getDockerAPI()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DockerAPI extends EObject {
	/**
	 * Returns the value of the '<em><b>Console</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Console</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Console</em>' reference.
	 * @see #setConsole(DockerMessageConsole)
	 * @see org.eclipse.ice.docker.api.DockerapiPackage#getDockerAPI_Console()
	 * @model required="true"
	 * @generated
	 */
	DockerMessageConsole getConsole();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.docker.api.DockerAPI#getConsole <em>Console</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Console</em>' reference.
	 * @see #getConsole()
	 * @generated
	 */
	void setConsole(DockerMessageConsole value);

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
	 * @model commandDataType="org.eclipse.ice.docker.api.CommandList"
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

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isContainerRunning(String id);

} // DockerAPI
