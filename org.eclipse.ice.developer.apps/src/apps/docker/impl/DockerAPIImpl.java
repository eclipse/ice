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

import com.google.common.collect.ImmutableList;
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
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>API</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.impl.DockerAPIImpl#getContainerRemotePort <em>Container Remote Port</em>}</li>
 *   <li>{@link apps.docker.impl.DockerAPIImpl#getSshContainerId <em>Ssh Container Id</em>}</li>
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

	/**
	 * The default value of the '{@link #getSshContainerId() <em>Ssh Container Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSshContainerId()
	 * @generated
	 * @ordered
	 */
	protected static final String SSH_CONTAINER_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSshContainerId() <em>Ssh Container Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSshContainerId()
	 * @generated
	 * @ordered
	 */
	protected String sshContainerId = SSH_CONTAINER_ID_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSshContainerId() {
		return sshContainerId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSshContainerId(String newSshContainerId) {
		String oldSshContainerId = sshContainerId;
		sshContainerId = newSshContainerId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_API__SSH_CONTAINER_ID, oldSshContainerId, sshContainerId));
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

		if (dockerClient != null) {


			// Create container with exposed ports
			ContainerConfig containerConfig = ContainerConfig.builder().image(imageName)
					.tty(true).attachStdout(true).attachStderr(true).cmd("bash")
					.domainname("HELLO").build();

			try {
				ContainerCreation creation;
				creation = dockerClient.createContainer(containerConfig, config.getName());
				String containerId = creation.id();
				config.setId(containerId);
				dockerClient.startContainer(containerId);
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}

			// Get the dynamically assigned port
			containerRemotePort = configureSSH(config.getName());
			config.setRemoteSSHPort(containerRemotePort);
			console.print("Container launched with name " + config.getName() + " with remote SSH port " + containerRemotePort);
			return true;
		}

		return false;

	}

	
	private int configureSSH(String containerNameToAddSSH) {
		if (dockerClient != null) {
			// Local Declarations
			String port = "";
			final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();
			List<PortBinding> randomPort = new ArrayList<>();
			randomPort.add(PortBinding.randomPort("0.0.0.0"));
			portBindings.put("22", randomPort);
			List<String> binds = ImmutableList.of("/var/run/docker.sock:/var/run/docker.sock");
			HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).binds(binds).build();
			List<String> envs = new ArrayList<String>();
			envs.add("CONTAINER="+containerNameToAddSSH);
			envs.add("AUTH_MECHANISM=noAuth");
			
			String imageName = "jeroenpeeters/docker-ssh:latest"; 

			// Create container with exposed ports
			ContainerConfig containerConfig = ContainerConfig.builder().image(imageName).hostConfig(hostConfig)
					.tty(true).attachStdout(true).attachStderr(true).exposedPorts(new String[] { "22" }).env(envs)
					.domainname("HELLO").build();

			ContainerInfo info = null;
			try {
				ContainerCreation creation;
				creation = dockerClient.createContainer(containerConfig, "ssh-"+containerNameToAddSSH);
				sshContainerId = creation.id();
				dockerClient.startContainer(sshContainerId);

				// Query the info on the new container.
				info = dockerClient.inspectContainer(sshContainerId);

			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return -1;
			}

			// Get the dynamically assigned port
			port = info.networkSettings().ports().get("22/tcp").get(0).hostPort();
			containerRemotePort = Integer.valueOf(port);
			return containerRemotePort;
		}
		
		return -1;
	}
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean connectToExistingContainer(String id) {
		try {
			dockerClient.startContainer(id);
			dockerClient.startContainer(sshContainerId);
			
			// Query the info on the new container.
			ContainerInfo info = dockerClient.inspectContainer(sshContainerId);
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
				dockerClient.stopContainer(sshContainerId, 2);
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
			case DockerPackage.DOCKER_API__SSH_CONTAINER_ID:
				return getSshContainerId();
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
			case DockerPackage.DOCKER_API__SSH_CONTAINER_ID:
				setSshContainerId((String)newValue);
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
			case DockerPackage.DOCKER_API__SSH_CONTAINER_ID:
				setSshContainerId(SSH_CONTAINER_ID_EDEFAULT);
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
			case DockerPackage.DOCKER_API__SSH_CONTAINER_ID:
				return SSH_CONTAINER_ID_EDEFAULT == null ? sshContainerId != null : !SSH_CONTAINER_ID_EDEFAULT.equals(sshContainerId);
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
				dockerClient.removeContainer(sshContainerId);
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
		result.append(", sshContainerId: ");
		result.append(sshContainerId);
		result.append(')');
		return result.toString();
	}

} // DockerAPIImpl
