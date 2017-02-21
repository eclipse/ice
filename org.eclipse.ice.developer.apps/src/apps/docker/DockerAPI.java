/**
 */
package apps.docker;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>API</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The DockerAPI provides a high-level abstraction for performing Docker-specific tasks. 
 * <!-- end-model-doc -->
 *
 *
 * @see apps.docker.DockerPackage#getDockerAPI()
 * @model
 * @generated
 */
public interface DockerAPI extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Build the image described by the provided Dockerfile build string. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void buildImage(String buildFile);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Launch a new container from the provided image name and ContainerConfiguration object. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void launchContainer(String name, ContainerConfiguration config);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void connectToExistingContainer(String id);

} // DockerAPI
