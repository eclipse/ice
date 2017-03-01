/**
 */
package apps.docker;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Container Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The ContainerConfiguration wraps container-specific launch configuration data. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.ContainerConfiguration#getName <em>Name</em>}</li>
 *   <li>{@link apps.docker.ContainerConfiguration#isEphemeral <em>Ephemeral</em>}</li>
 *   <li>{@link apps.docker.ContainerConfiguration#getPorts <em>Ports</em>}</li>
 *   <li>{@link apps.docker.ContainerConfiguration#getVolumesConfig <em>Volumes Config</em>}</li>
 *   <li>{@link apps.docker.ContainerConfiguration#getRemoteSSHPort <em>Remote SSH Port</em>}</li>
 *   <li>{@link apps.docker.ContainerConfiguration#getId <em>Id</em>}</li>
 * </ul>
 *
 * @see apps.docker.DockerPackage#getContainerConfiguration()
 * @model
 * @generated
 */
public interface ContainerConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the container. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see apps.docker.DockerPackage#getContainerConfiguration_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link apps.docker.ContainerConfiguration#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Ephemeral</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Indicates whether --rm should be added to the launch. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ephemeral</em>' attribute.
	 * @see #setEphemeral(boolean)
	 * @see apps.docker.DockerPackage#getContainerConfiguration_Ephemeral()
	 * @model
	 * @generated
	 */
	boolean isEphemeral();

	/**
	 * Sets the value of the '{@link apps.docker.ContainerConfiguration#isEphemeral <em>Ephemeral</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ephemeral</em>' attribute.
	 * @see #isEphemeral()
	 * @generated
	 */
	void setEphemeral(boolean value);

	/**
	 * Returns the value of the '<em><b>Ports</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of ports to open on the container. By default, 22 is already opened. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ports</em>' attribute list.
	 * @see apps.docker.DockerPackage#getContainerConfiguration_Ports()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Int"
	 * @generated
	 */
	EList<Integer> getPorts();

	/**
	 * Returns the value of the '<em><b>Volumes Config</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The volume mounting string. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Volumes Config</em>' attribute.
	 * @see #setVolumesConfig(String)
	 * @see apps.docker.DockerPackage#getContainerConfiguration_VolumesConfig()
	 * @model
	 * @generated
	 */
	String getVolumesConfig();

	/**
	 * Sets the value of the '{@link apps.docker.ContainerConfiguration#getVolumesConfig <em>Volumes Config</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volumes Config</em>' attribute.
	 * @see #getVolumesConfig()
	 * @generated
	 */
	void setVolumesConfig(String value);

	/**
	 * Returns the value of the '<em><b>Remote SSH Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Remote SSH Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to the localhost SSH port for the container. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Remote SSH Port</em>' attribute.
	 * @see #setRemoteSSHPort(int)
	 * @see apps.docker.DockerPackage#getContainerConfiguration_RemoteSSHPort()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Int"
	 * @generated
	 */
	int getRemoteSSHPort();

	/**
	 * Sets the value of the '{@link apps.docker.ContainerConfiguration#getRemoteSSHPort <em>Remote SSH Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote SSH Port</em>' attribute.
	 * @see #getRemoteSSHPort()
	 * @generated
	 */
	void setRemoteSSHPort(int value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see apps.docker.DockerPackage#getContainerConfiguration_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link apps.docker.ContainerConfiguration#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // ContainerConfiguration
