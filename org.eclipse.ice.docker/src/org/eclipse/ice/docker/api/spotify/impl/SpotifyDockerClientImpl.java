/**
 */
package org.eclipse.ice.docker.api.spotify.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.ice.docker.api.ContainerConfiguration;
import org.eclipse.ice.docker.api.DockerMessageConsole;
import org.eclipse.ice.docker.api.DockerapiFactory;
import org.eclipse.ice.docker.api.spotify.SpotifyDockerClient;
import org.eclipse.ice.docker.api.spotify.SpotifyPackage;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.PortBinding;
import com.spotify.docker.client.messages.ProgressMessage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Docker
 * Client</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.docker.api.spotify.impl.SpotifyDockerClientImpl#getConsole <em>Console</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpotifyDockerClientImpl extends MinimalEObjectImpl.Container implements SpotifyDockerClient {

	/**
	 * The cached value of the '{@link #getConsole() <em>Console</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConsole()
	 * @generated
	 * @ordered
	 */
	protected DockerMessageConsole console;

	private DockerClient dockerClient;

	private boolean imageBuildSuccess = true;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @throws DockerCertificateException
	 */
	protected SpotifyDockerClientImpl() {
		super();
		DefaultDockerClient.Builder builder = null;
		try {
			builder = DefaultDockerClient.fromEnv();
		} catch (DockerCertificateException e) {
			e.printStackTrace();
		}
		dockerClient = builder.build();
		this.console = DockerapiFactory.eINSTANCE.createStdOutConsole();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotifyPackage.Literals.SPOTIFY_DOCKER_CLIENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerMessageConsole getConsole() {
		if (console != null && console.eIsProxy()) {
			InternalEObject oldConsole = (InternalEObject)console;
			console = (DockerMessageConsole)eResolveProxy(oldConsole);
			if (console != oldConsole) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotifyPackage.SPOTIFY_DOCKER_CLIENT__CONSOLE, oldConsole, console));
			}
		}
		return console;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerMessageConsole basicGetConsole() {
		return console;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConsole(DockerMessageConsole newConsole) {
		DockerMessageConsole oldConsole = console;
		console = newConsole;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotifyPackage.SPOTIFY_DOCKER_CLIENT__CONSOLE, oldConsole, console));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean buildImage(String buildFile, String imagename) {
		try {
			dockerClient.build(Paths.get(buildFile), imagename, new ProgressHandler() {
				@Override
				public void progress(ProgressMessage message) throws DockerException {
					if (message.stream() != null && console != null) {
						SpotifyDockerClientImpl.this.console.print("[DockerBuild] " + message.stream());
					}
					if (message.error() != null && console != null) {
						SpotifyDockerClientImpl.this.console.print("Error in building image: " + message.error());
						SpotifyDockerClientImpl.this.imageBuildSuccess = false;
					}

				}
			});
		} catch (DockerException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		// If it wasn't successful, then
		// reset the flag and return false.
		if (!imageBuildSuccess) {
			imageBuildSuccess = true;
			return false;
		} else {
			return true;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public boolean createContainer(String imageName, ContainerConfiguration config) {
		// Local Declarations
		String port = "";
		List<String> envs = new ArrayList<String>();
		final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();
		List<PortBinding> randomPort = new ArrayList<>();
		randomPort.add(PortBinding.randomPort("0.0.0.0"));
		portBindings.put("22", randomPort);

		if (dockerClient != null) {

			HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

			// Create container with exposed ports
			ContainerConfig containerConfig = ContainerConfig.builder().image(imageName).hostConfig(hostConfig)
					.tty(true).attachStdout(true).attachStderr(true).exposedPorts(new String[] { "22" })
					.domainname("HELLO").build();

			ContainerInfo info = null;
			try {
				ContainerCreation creation;
				creation = dockerClient.createContainer(containerConfig, config.getName());
				String containerId = creation.id();
				config.setId(containerId);
				dockerClient.startContainer(containerId);

				// Query the info on the new container.
				info = dockerClient.inspectContainer(containerId);

			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}

			// Get the dynamically assigned port
			port = info.networkSettings().ports().get("22/tcp").get(0).hostPort();
//			containerRemotePort = Integer.valueOf(port);
			config.setRemoteSSHPort(Integer.valueOf(port));
			console.print("Container launched with name " + config.getName() + " with remote SSH port " + port);
			return true;
		}

		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public boolean connectToExistingContainer(String id) {
		try {
			dockerClient.startContainer(id);
			// Query the info on the new container.
//			ContainerInfo info = dockerClient.inspectContainer(id);
			// Get the dynamically assigned port
//			String port = info.networkSettings().ports().get("22/tcp").get(0).hostPort();
//			containerRemotePort = Integer.valueOf(port);
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public boolean deleteContainer(String id) {
		if (dockerClient != null) {
			try {
				dockerClient.removeContainer(id);
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public boolean deleteImage(String imageName) {
		if (dockerClient != null) {
			try {
				dockerClient.removeImage(imageName);
			} catch (DockerException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public boolean stopContainer(String id) {
		if (dockerClient != null) {
			try {
				dockerClient.stopContainer(id, 2);
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public String createContainerExecCommand(String imageName, String[] command) {
		if (dockerClient != null) {
			ContainerConfiguration config = DockerapiFactory.eINSTANCE.createContainerConfiguration();
			createContainer(imageName, config);
			String id = config.getId();
			LogStream output = null;
			try {
				final ExecCreation execCreation = dockerClient.execCreate(id, command,
						DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
				output = dockerClient.execStart(execCreation.id());
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return "";
			}

			String out = output.readFully();
			stopContainer(id);
			deleteContainer(id);

			return out;
		}

		return "";
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public void pull(String imageName) {
		if (dockerClient != null) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				dockerClient.pull(imageName, new ProgressHandler() {
					private boolean printedExtracting = false;
					private boolean printedDownloading = false;

					@Override
					public void progress(ProgressMessage message) throws DockerException {
						if (message.status() != null && console != null) {

							if ((message.status().contains("Downloading") && !printedDownloading)
									|| (message.status().contains("Extracting") && !printedExtracting)) {
								SpotifyDockerClientImpl.this.console.print("[Docker Pull] " + message.status() + " " + imageName);
							}

							if (!message.status().contains("Downloading") && !message.status().contains("Extracting")) {
								SpotifyDockerClientImpl.this.console.print("[Docker Pull] " + message.status());
							}

							if (message.status().contains("Extracting")) {
								printedExtracting = true;
							} else if (message.status().contains("Downloading")) {
								printedDownloading = true;
							}
						}
						if (message.error() != null && console != null) {
							SpotifyDockerClientImpl.this.console.print("Error in pulling image: " + message.error());
						}
					}
				});
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public EList<String> listAvailableImages() {
		if (dockerClient != null) {
			EList<String> imageNames = new BasicEList<String>();
			try {
				List<Image> images = dockerClient.listImages();
				for (Image i : images) {
					if (i.repoTags() != null) {
						imageNames.add(i.repoTags().get(0));
					}
				}
				return imageNames;
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public boolean isContainerRunning(String id) {
		if (dockerClient != null) {
			try {
				List<com.spotify.docker.client.messages.Container> containers = dockerClient.listContainers();
				for (com.spotify.docker.client.messages.Container c : containers) {
					if (id.equals(c.id())) {
						return true;
					}
				}
			} catch (DockerException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT__CONSOLE:
				if (resolve) return getConsole();
				return basicGetConsole();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT__CONSOLE:
				setConsole((DockerMessageConsole)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT__CONSOLE:
				setConsole((DockerMessageConsole)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT__CONSOLE:
				return console != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___BUILD_IMAGE__STRING_STRING:
				return buildImage((String)arguments.get(0), (String)arguments.get(1));
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION:
				return createContainer((String)arguments.get(0), (ContainerConfiguration)arguments.get(1));
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___CONNECT_TO_EXISTING_CONTAINER__STRING:
				return connectToExistingContainer((String)arguments.get(0));
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___DELETE_CONTAINER__STRING:
				return deleteContainer((String)arguments.get(0));
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___DELETE_IMAGE__STRING:
				return deleteImage((String)arguments.get(0));
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___STOP_CONTAINER__STRING:
				return stopContainer((String)arguments.get(0));
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___CREATE_CONTAINER_EXEC_COMMAND__STRING_STRING:
				return createContainerExecCommand((String)arguments.get(0), (String[])arguments.get(1));
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___PULL__STRING:
				pull((String)arguments.get(0));
				return null;
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___LIST_AVAILABLE_IMAGES:
				return listAvailableImages();
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT___IS_CONTAINER_RUNNING__STRING:
				return isContainerRunning((String)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} // SpotifyDockerClientImpl
