/**
 * 
 */
package org.eclipse.ice.tests.commands;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.EmailUpdateHandler;
import org.eclipse.ice.commands.ICommandUpdateHandler;
import org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler;
import org.junit.Test;

/**
 * @author 4jo
 *
 */
public class CommandFactoryUpdaterTest {

	/**
	 * This function tests with real files to test an actual job processing with an
	 * email updater attached to the command, so that when the job finishes an email
	 * is sent
	 */
	@Test
	public void testCommandWithEmailUpdater() {

		String hostname = CommandFactoryTest.getLocalHostname();
		// Set some things specific to the local command
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setNumProcs("1");
		commandConfig.setInstallDirectory("");
		commandConfig
				.setWorkingDirectory(System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/");
		commandConfig.setOS(System.getProperty("os.name"));
		commandConfig.setExecutable("./test_code_execution.sh");
		// If it is windows, configure the test to run on windows
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			commandConfig.setExecutable(".\\test_code_execution.ps1");
			commandConfig.setInterpreter("powershell.exe");
		}
		commandConfig.addInputFile("someInputFile", "someInputFile.txt");
		commandConfig.setAppendInput(true);
		commandConfig.setCommandId(1);
		commandConfig.setErrFileName("someLocalErrFile.txt");
		commandConfig.setOutFileName("someLocalOutFile.txt");

		// Make a default boring connection authorization
		ConnectionAuthorizationHandler handler = new TxtFileConnectionAuthorizationHandler();
		handler.setHostname(hostname);
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		connectionConfig.setAuthorization(handler);

		// Get the command
		CommandFactory factory = new CommandFactory();
		Command localCommand = null;
		try {
			localCommand = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ICommandUpdateHandler updater = setupEmailUpdateHandler();
		localCommand.setUpdateHandler(updater);

		// Run it
		CommandStatus status = localCommand.execute();

		assertEquals(CommandStatus.SUCCESS, status);

		// If test finishes without an error, email was successfully sent
		System.out.println("Finished functional local command");

	}

	/**
	 * Sets up the dummy email address via a text file credential for CI
	 * 
	 * @return
	 */
	private EmailUpdateHandler setupEmailUpdateHandler() {
		// Get a text file with credentials
		String credFile = "/tmp/email-creds.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\email-creds.txt";

		String email = "";
		String password = "";
		String host = "";

		File file = new File(credFile);
		try (Scanner scanner = new Scanner(file)) {
			email = scanner.next();
			password = scanner.next();
			host = scanner.next();
		} catch (FileNotFoundException e) {
			System.out.println("Email credential file not found, can't continue with test...");
			e.printStackTrace();
		}
		EmailUpdateHandler handler = new EmailUpdateHandler();
		handler.setEmailAddress(email);
		handler.setPassword(password);
		handler.setSmtpHost(host);

		return handler;
	}
}
