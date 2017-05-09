/**
 */
package org.eclipse.ice.docker.api.spotify;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.ice.docker.api.DockerapiPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.docker.api.spotify.SpotifyFactory
 * @model kind="package"
 * @generated
 */
public interface SpotifyPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "spotify";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/ice/docker/spotify";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "dockerspotify";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SpotifyPackage eINSTANCE = org.eclipse.ice.docker.api.spotify.impl.SpotifyPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ice.docker.api.spotify.impl.SpotifyDockerClientImpl <em>Docker Client</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.docker.api.spotify.impl.SpotifyDockerClientImpl
	 * @see org.eclipse.ice.docker.api.spotify.impl.SpotifyPackageImpl#getSpotifyDockerClient()
	 * @generated
	 */
	int SPOTIFY_DOCKER_CLIENT = 0;

	/**
	 * The feature id for the '<em><b>Console</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT__CONSOLE = DockerapiPackage.DOCKER_API__CONSOLE;

	/**
	 * The number of structural features of the '<em>Docker Client</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT_FEATURE_COUNT = DockerapiPackage.DOCKER_API_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Build Image</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___BUILD_IMAGE__STRING_STRING = DockerapiPackage.DOCKER_API___BUILD_IMAGE__STRING_STRING;

	/**
	 * The operation id for the '<em>Create Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION = DockerapiPackage.DOCKER_API___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION;

	/**
	 * The operation id for the '<em>Connect To Existing Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___CONNECT_TO_EXISTING_CONTAINER__STRING = DockerapiPackage.DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING;

	/**
	 * The operation id for the '<em>Delete Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___DELETE_CONTAINER__STRING = DockerapiPackage.DOCKER_API___DELETE_CONTAINER__STRING;

	/**
	 * The operation id for the '<em>Delete Image</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___DELETE_IMAGE__STRING = DockerapiPackage.DOCKER_API___DELETE_IMAGE__STRING;

	/**
	 * The operation id for the '<em>Stop Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___STOP_CONTAINER__STRING = DockerapiPackage.DOCKER_API___STOP_CONTAINER__STRING;

	/**
	 * The operation id for the '<em>Create Container Exec Command</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___CREATE_CONTAINER_EXEC_COMMAND__STRING_STRING = DockerapiPackage.DOCKER_API___CREATE_CONTAINER_EXEC_COMMAND__STRING_STRING;

	/**
	 * The operation id for the '<em>Pull</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___PULL__STRING = DockerapiPackage.DOCKER_API___PULL__STRING;

	/**
	 * The operation id for the '<em>List Available Images</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___LIST_AVAILABLE_IMAGES = DockerapiPackage.DOCKER_API___LIST_AVAILABLE_IMAGES;

	/**
	 * The operation id for the '<em>Is Container Running</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT___IS_CONTAINER_RUNNING__STRING = DockerapiPackage.DOCKER_API___IS_CONTAINER_RUNNING__STRING;

	/**
	 * The number of operations of the '<em>Docker Client</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOTIFY_DOCKER_CLIENT_OPERATION_COUNT = DockerapiPackage.DOCKER_API_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.docker.api.spotify.SpotifyDockerClient <em>Docker Client</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker Client</em>'.
	 * @see org.eclipse.ice.docker.api.spotify.SpotifyDockerClient
	 * @generated
	 */
	EClass getSpotifyDockerClient();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SpotifyFactory getSpotifyFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.ice.docker.api.spotify.impl.SpotifyDockerClientImpl <em>Docker Client</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.docker.api.spotify.impl.SpotifyDockerClientImpl
		 * @see org.eclipse.ice.docker.api.spotify.impl.SpotifyPackageImpl#getSpotifyDockerClient()
		 * @generated
		 */
		EClass SPOTIFY_DOCKER_CLIENT = eINSTANCE.getSpotifyDockerClient();

	}

} //SpotifyPackage
