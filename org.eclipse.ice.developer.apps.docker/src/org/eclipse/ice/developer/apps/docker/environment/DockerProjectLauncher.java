/**
 */
package org.eclipse.ice.developer.apps.docker.environment;

import org.eclipse.ice.docker.api.ContainerConfiguration;

import apps.ProjectLauncher;
import apps.SourcePackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Docker Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The DockerPTPProjectLauncher provides a launchProject method that uses the ContainerConfiguration of the current DockerEnvironment to create a new PTP Synchronized Project for the IEnvironment's primary application running in the created docker container. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getContainerconfiguration <em>Containerconfiguration</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getProjectName <em>Project Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#getDockerProjectLauncher()
 * @model annotation="http://www.obeo.fr/dsl/dnc/archetype archetype='Thing'"
 * @generated
 */
public interface DockerProjectLauncher extends ProjectLauncher {
	/**
	 * Returns the value of the '<em><b>Containerconfiguration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Containerconfiguration</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Containerconfiguration</em>' reference.
	 * @see #setContainerconfiguration(ContainerConfiguration)
	 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#getDockerProjectLauncher_Containerconfiguration()
	 * @model
	 * @generated
	 */
	ContainerConfiguration getContainerconfiguration();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getContainerconfiguration <em>Containerconfiguration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Containerconfiguration</em>' reference.
	 * @see #getContainerconfiguration()
	 * @generated
	 */
	void setContainerconfiguration(ContainerConfiguration value);

	/**
	 * Returns the value of the '<em><b>Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Project Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Project Name</em>' attribute.
	 * @see #setProjectName(String)
	 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#getDockerProjectLauncher_ProjectName()
	 * @model
	 * @generated
	 */
	String getProjectName();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getProjectName <em>Project Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Project Name</em>' attribute.
	 * @see #getProjectName()
	 * @generated
	 */
	void setProjectName(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Launch the new Eclipse project for the given application. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean launchProject(SourcePackage project);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void updateConnection(int port);

} // DockerProjectLauncher
