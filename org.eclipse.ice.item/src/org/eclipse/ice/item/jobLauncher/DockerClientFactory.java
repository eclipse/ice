package org.eclipse.ice.item.jobLauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.net.HostAndPort;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DefaultDockerClient.Builder;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;

/**
 * This class provides a factory method for creating the DefaultDockerClient in
 * a way that is OS independent.
 * 
 * @author Alex McCAskey
 *
 */
public class DockerClientFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(DockerClientFactory.class);

	/**
	 * This method returns the DockerClient dependent on the current OS.
	 * 
	 * @return
	 * @throws DockerCertificateException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public DockerClient getDockerClient() throws DockerCertificateException, IOException, InterruptedException  {
		DockerClient client = null;

		// If this is not Linux, then we have to find DOCKER_HOST
		if (!Platform.getOS().equals(Platform.OS_LINUX)) {

			// See if we can get the DOCKER_HOST environment variaable
			String dockerHost = System.getenv("DOCKER_HOST");
			if (dockerHost == null) {

				// If not, run a script to see if we can get it
				File script = getDockerConnectionScript();
				String[] scriptExec = null;
				if (Platform.getOS().equals(Platform.OS_MACOSX)) {
					scriptExec = new String[] { script.getAbsolutePath() };
				} else if (Platform.getOS().equals(Platform.OS_WIN32)) {
					scriptExec = new String[] { "cmd.exe", "/C", script.getAbsolutePath() };
				}

				// Execute the script to get the DOCKER vars.
				Process process = new ProcessBuilder(scriptExec).start();
				process.waitFor();
				int exitValue = process.exitValue();
				if (exitValue == 0) {

					// Read them into a Properties object
					InputStream processInputStream = process.getInputStream();
					Properties dockerSettings = new Properties();
					// Properties.load screws up windows path separators
					// so if windows, just get the string from the stream
					if (Platform.getOS().equals(Platform.OS_WIN32)) {
						String result = streamToString(processInputStream).trim();
						String[] dockerEnvs = result.split(System.lineSeparator());
						for (String s : dockerEnvs) {
							String[] env = s.split("=");
							dockerSettings.put(env[0], env[1]);
						}
					} else {
						dockerSettings.load(processInputStream);
					}
					
					// Create the Builder object that wil build the DockerClient
					Builder builder = new Builder();

					// Get the DOCKER_HOST and CERT_PATH vars
					String endpoint = dockerSettings.getProperty("DOCKER_HOST");
					Path dockerCertPath = Paths.get(dockerSettings.getProperty("DOCKER_CERT_PATH"));

					System.out.println("DOCKERHOST: " + endpoint);
					System.out.println("DOCKER CERT PATH: " + dockerSettings.getProperty("DOCKER_CERT_PATH"));
					// Set up the certificates
					Optional<DockerCertificates> certs = DockerCertificates.builder().dockerCertPath(dockerCertPath).build();

					// Set the data need for the builder.
					String stripped = endpoint.replaceAll(".*://", "");
					HostAndPort hostAndPort = HostAndPort.fromString(stripped);
					String hostText = hostAndPort.getHostText();
					String scheme = certs != null ? "https" : "http";
					int port = hostAndPort.getPortOrDefault(2375);
					String address = hostText;
					builder.uri(scheme + "://" + address + ":" + port);
					if (certs != null) {
						builder.dockerCertificates(certs.get());
					}

					// Build the Dockerclient!
					client = builder.build();

				} else {
					// log what happened if the process did not end as expected
					// an exit value of 1 should indicate no connection found
					InputStream processErrorStream = process.getErrorStream();
					String errorMessage = streamToString(processErrorStream);
					logger.error("Error in getting DOCKER variables: " + errorMessage);
				}
			} else {
				client = DefaultDockerClient.fromEnv().build();
			}
		} else {
			// It was equal to Linux, so just use the default stuff.
			client = DefaultDockerClient.fromEnv().build();
		}

		return client;
	}

	/**
	 * This method gets the script file for finding the Docker environment
	 * variables.
	 * 
	 * @return
	 */
	private File getDockerConnectionScript() {
		String scriptName = "";
		if (Platform.getOS().equals(Platform.OS_MACOSX)) {
			scriptName = "script-macosx.sh";
		} else if (Platform.getOS().equals(Platform.OS_WIN32)) {
			scriptName = "script.bat";
		}

		Bundle bundle = Platform.getBundle("org.eclipse.ice.item");
		final File script = bundle.getDataFile(scriptName);

		// if the script file does not exist or is outdated.
		if (script != null && (!script.exists() || script.lastModified() < bundle.getLastModified())) {
			try (final FileOutputStream output = new FileOutputStream(script);
					final InputStream is = DockerClientFactory.class.getResourceAsStream("/resources/" + scriptName)) {
				byte[] buff = new byte[1024];
				int n;
				while ((n = is.read(buff)) > 0) {
					output.write(buff, 0, n);
				}
				script.setExecutable(true);
			} catch (IOException e) {
				logger.error("Error in getting input file.", e);
			}
		}
		return script;
	}

	/**
	 * Private utility for getting string from InputStream.
	 * 
	 * @param stream
	 * @return
	 */
	private String streamToString(InputStream stream) {
		BufferedReader buff = new BufferedReader(new InputStreamReader(stream));
		StringBuffer res = new StringBuffer();
		String line = ""; //$NON-NLS-1$
		try {
			while ((line = buff.readLine()) != null) {
				res.append(System.getProperty("line.separator"));
				res.append(line);
			}
			buff.close();
		} catch (IOException e) {
		}
		return res.length() > 0 ? res.substring(1) : "";
	}

}
