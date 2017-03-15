/**
 */
package apps.docker.impl;

import apps.EnvironmentConsole;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerAPI;
import apps.docker.DockerFactory;
import apps.docker.DockerPackage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.spotify.docker.client.AnsiProgressHandler;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.EventStream;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.Event;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.PortBinding;
import com.spotify.docker.client.messages.ProgressMessage;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>API</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.impl.DockerAPIImpl#getContainerRemotePort <em>Container Remote Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DockerAPIImpl extends MinimalEObjectImpl.Container implements DockerAPI {

	/**
	 * The default value of the '{@link #getContainerRemotePort() <em>Container Remote Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerRemotePort()
	 * @generated
	 * @ordered
	 */
	protected static final int CONTAINER_REMOTE_PORT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getContainerRemotePort() <em>Container Remote Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerRemotePort()
	 * @generated
	 * @ordered
	 */
	protected int containerRemotePort = CONTAINER_REMOTE_PORT_EDEFAULT;

	private DockerClient dockerClient;

	private boolean imageBuildSuccess = true;

	private EnvironmentConsole console;
	
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	protected DockerAPIImpl() throws DockerCertificateException {
		super();
		DefaultDockerClient.Builder builder = null;
		builder = DefaultDockerClient.fromEnv();
		dockerClient = builder.build();
	}

	@Override
	public void setEnvironmentConsole(EnvironmentConsole c) {
		console = c;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerPackage.Literals.DOCKER_API;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getContainerRemotePort() {
		return containerRemotePort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainerRemotePort(int newContainerRemotePort) {
		int oldContainerRemotePort = containerRemotePort;
		containerRemotePort = newContainerRemotePort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_API__CONTAINER_REMOTE_PORT, oldContainerRemotePort, containerRemotePort));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean buildImage(String buildDir, String imagename) {

		try {
			dockerClient.build(Paths.get(buildDir), imagename, new ProgressHandler() {
				@Override
				public void progress(ProgressMessage message) throws DockerException {
					if (message.stream() != null && console != null) {
						DockerAPIImpl.this.console.print("[DockerBuild] " + message.stream());
					}
					if (message.error() != null && console != null) {
						DockerAPIImpl.this.console.print("Error in building image: " + message.error());
						DockerAPIImpl.this.imageBuildSuccess = false;
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
			containerRemotePort = Integer.valueOf(port);
			config.setRemoteSSHPort(Integer.valueOf(port));
			console.print("Container launched with name " + config.getName() + " with remote SSH port " + port);
			return true;
		}

		return false;

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean connectToExistingContainer(String id) {
		try {
			dockerClient.startContainer(id);
			// Query the info on the new container.
			ContainerInfo info = dockerClient.inspectContainer(id);
			// Get the dynamically assigned port
			String port = info.networkSettings().ports().get("22/tcp").get(0).hostPort();
			containerRemotePort = Integer.valueOf(port);
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String createContainerExecCommand(String imageName, String[] command) {
		if (dockerClient != null) {
			ContainerConfiguration config = DockerFactory.eINSTANCE.createContainerConfiguration();
			createContainer(imageName, config);
			for (String s: command) {
				System.out.println(s);
			}
			String id = config.getId();
			LogStream output = null;
			try {
				final ExecCreation execCreation = dockerClient.execCreate(id, command,
						DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
				System.out.println("Exec:\n" + execCreation.toString());
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
							
							if ((message.status().contains("Downloading") && !printedDownloading) || (message.status().contains("Extracting") && !printedExtracting)) {
								DockerAPIImpl.this.console.print("[Docker Pull] " + message.status() + " " + imageName);
							}
							
							if (!message.status().contains("Downloading") && !message.status().contains("Extracting")) {
								DockerAPIImpl.this.console.print("[Docker Pull] " + message.status());
							}

							if (message.status().contains("Extracting")) {
								printedExtracting = true;
							} else if (message.status().contains("Downloading")) {
								printedDownloading = true;
							}
						}
						if (message.error() != null && console != null) {
							DockerAPIImpl.this.console.print("Error in pulling image: " + message.error());
						}
					}
				});
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EList<String> listAvailableImages() {
		if (dockerClient != null) {
			EList<String> imageNames = new BasicEList<String>();
			try {
				List<Image> images = dockerClient.listImages();
				for (Image i : images) {
					imageNames.add(i.repoTags().get(0));
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DockerPackage.DOCKER_API__CONTAINER_REMOTE_PORT:
				return getContainerRemotePort();
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
			case DockerPackage.DOCKER_API__CONTAINER_REMOTE_PORT:
				setContainerRemotePort((Integer)newValue);
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
			case DockerPackage.DOCKER_API__CONTAINER_REMOTE_PORT:
				setContainerRemotePort(CONTAINER_REMOTE_PORT_EDEFAULT);
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
			case DockerPackage.DOCKER_API__CONTAINER_REMOTE_PORT:
				return containerRemotePort != CONTAINER_REMOTE_PORT_EDEFAULT;
		}
		return super.eIsSet(featureID);
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
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case DockerPackage.DOCKER_API___BUILD_IMAGE__STRING_STRING:
				return buildImage((String)arguments.get(0), (String)arguments.get(1));
			case DockerPackage.DOCKER_API___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION:
				return createContainer((String)arguments.get(0), (ContainerConfiguration)arguments.get(1));
			case DockerPackage.DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING:
				return connectToExistingContainer((String)arguments.get(0));
			case DockerPackage.DOCKER_API___DELETE_CONTAINER__STRING:
				return deleteContainer((String)arguments.get(0));
			case DockerPackage.DOCKER_API___DELETE_IMAGE__STRING:
				return deleteImage((String)arguments.get(0));
			case DockerPackage.DOCKER_API___STOP_CONTAINER__STRING:
				return stopContainer((String)arguments.get(0));
			case DockerPackage.DOCKER_API___CREATE_CONTAINER_EXEC_COMMAND__STRING_STRING:
				return createContainerExecCommand((String)arguments.get(0), (String[])arguments.get(1));
			case DockerPackage.DOCKER_API___PULL__STRING:
				pull((String)arguments.get(0));
				return null;
			case DockerPackage.DOCKER_API___LIST_AVAILABLE_IMAGES:
				return listAvailableImages();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (containerRemotePort: ");
		result.append(containerRemotePort);
		result.append(')');
		return result.toString();
	}

} // DockerAPIImpl
