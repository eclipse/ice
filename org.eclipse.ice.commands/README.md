# Commands Package

This README serves as an overview of the commands package, which is a standalone maven package that can be used within or outside of ICE. The package provides the necessary API to set up and run jobs on either one's local computer or a remote host. Additionally, the API includes file transfer and file system browsing capabilities, with the option to move or copy files on the local host or remote host. It is suggested that users encode their file processing logic into a bash/python/powershell script to be run locally/remotely. For example, a remote job on a remote host B could be run from commands on local host A which executes a remote job on remote host C, assuming the script contains the necessary logic to connect remote host B to remote host C.

Examples can be found directly underneath this directory in`src/test/java/org/eclipse/ice/tests/commands` or in the standalone demo package within ICE `org.eclipse.ice.demo/src/org/eclipse/ice/demo/commands`. Other tests, that can serve as examples of remote command usage, can be found in `org.eclipse.ice.tests.integration/org.eclipse.ice.tests.commands`.


## Build Instructions
Building the package can be performed like any other maven package, where the end result is a jar file that can be included as a dependency

```
$ mvn clean install
```

This installs the jar file to the local repository in `~/.m2`. It is also possible to build the package without installing by running

```
$ mvn clean verify
```

In both cases one can skip the tests by including `-DskipTests` in your build. 

### Dependencies
All dependencies are noted in the `pom` file, and all but one are within maven central. The only non-centralized dependency is the ICE package `org.eclipse.ice.tests.util.data`. To install it, perform the following commands (after cloning the ICE repositiory) so that the Commands package can build successfully:

```shell
$ cd org.eclipse.ice.data
$ mvn clean install
```

### Test packages
The tests are split into two separate directories to enable simplified compilation. Tests pertaining to local commands are available within this package, while the tests pertaining to remote commands are available in `org.eclipse.ice.tests.integration/org.eclipse.ice.tests.commands`. The remote tests require additional testing setup, which is why they are available in the integration testing suite. Please see [this link](https://github.com/eclipse/ice/tree/next/org.eclipse.ice.tests.integration/org.eclipse.ice.tests.commands) for further details.


## Commands API
### General Commands Use
The Commands API works best when the user of the API writes a script containing their job logic, and then hands this script to Commands to run. The script can be in a number of languages (e.g. bash, python, etc.), depending on what kind of interpreter you like. A basic example of a CommandConfiguration is shown below, for example with a python script:

```java
CommandConfiguration configuration = new CommandConfiguration; 
configuration.setExecutable("test_python_script.py");
configuration.setInterpreter("python");
configuration.setNumProcs("1");
configuration.setInstallDirectory("");
configuration.setWorkingDirectory(pwd);
configuration.setOS(System.getProperty("os.name"));
configuration.setCommandId(9);
configuration.setErrFileName("somePythErrFile.txt");
configuration.setOutFileName("somePythOutFile.txt");
configuration.setAppendInput(true);
configuration.addInputFile("inputfile", "someInputFile.txt");
configuration.addInputFile("inputfile2", "someOtherInputFile.txt");
configuration.setWorkingDirectory(scriptDir);
configuration.setRemoteWorkingDirectory("/tmp/pythonTest");
``` 

The API checks that the executable and input files exist in the local working directory, set by `setWorkingDirectory(directory)`. If the command is intended to be run locally, the job will execute in that working directory. If the command is intended to be executed remotely, the API will copy the necessary files to the directory given by `setRemoteWorkingDirectory(directory)` and run the job on the remote host in that directory. Any files created by the logic in the script will be left in that remote directory (or wherever the script logic might have left them). 

The command can then be obtained and executed simply via a Factory method:

```java
Command command = factory.getCommand(commandConfig, connectionConfig);
CommandStatus status = remoteCommand.execute();
```
### Commands FileHandler	
The `FileHandler` API also allows users to take advantage of the file copying and browsing capabilities that Commands uses, should these be desired. The `FileHandler` has file browsing, copying, and moving capabilities, both remotely and locally. A short description of each function is listed below.

#### FileBrowser

The FileBrowser allows the user to search for files or directories given a top directory. Suppose we have a directory structure that looks like

```
topDirectory
     |
     |-- subDir1
     |-- subDir2
 		     |--- otherFile.txt
 		     |--- file.txt
 	 |-- file2.txt    
```

If the `FileBrowser` is called on `topDirectory`, an array of strings with all of the files or directories is returned that exist below `topDirectory`. The caller can then search through that array for a desired path in the file system. The FileBrowser can easily by called by the following:

```java
ConnectionConfiguration someConfig = new ConnectionConfiguration();
FileHandler handler = fileHandlerFactory.getFileHandler(someConfig);
FileBrowser browser = handler.getFileBrowser("/path/to/top/directory");
ArrayList directories<String> = browser.listDirectories();

```
In the case of our example with `topDirectory`, the list `directories` would contain the full paths to `subDir1` and `subDir2`.


#### File Transferring

Files can be transferred both locally and remotely as well using the `FileHandler`. Using the same example as above, files can be moved or copied via the following:

```java
ConnectionConfiguration someConfig = new ConnectionConfiguration();
FileHandler handler = fileHandlerFactory.getFileHandler(someConfig);
CommandStatus status = handler.move("/some/sourcefile.txt","/some/newPath/");
```

Locally, move and copy act as their names suggest. When transferring a file remotely, move and copy are the same if the transfer is occurring from a local-to-remote host or vice versa and occur via an `sftp` transfer. Transfers can occur remotely-to-remotely, for which then move and copy retain their original meaning when used on a single file system. It transferring files remotely, it is advised that you specify what kind of transfer you want to perform. This can be done via:

```java
handler.setHandleType(HandleType.REMOTELOCAL);
```
`HandleType` is an enum which can be set, with three options (`REMOTELOCAL`, `LOCALREMOTE`, or `REMOTEREMOTE`).


The transferring has four steps:

	1. Check existence - the existence of the source file is confirmed
	2. Check existence - the existence of the destination path is confirmed
	3. If the destination path does not exist, FileHandler attempts to make it
	4. The transfer is executed.

Similarly to Commands, any return status that is not `CommandStatus.SUCCESS` is considered a failure and indicates that the transfer was not successful.


### Connection Manager

All remote connections are managed by the `ConnectionManager` class. A static `ConnectionManager` can be obtained from the `ConnectionManagerFactory`, such that only one `ConnectionManager` exists and thus takes care of all connections that are opened. The user of Commands is responsible for explicitly calling the `ConnectionManager` to end a session via:

```java
ConnectionManagerFactory.getConnectionManager().closeConnection("someName");
```
This functionality allows multiple commands to be run over a single connection without having to re-establish or re-check the connection credentials several times.

For `Commands` and `FileHandler`, the classes will check if a given connection with the passed `ConnectionConfiguration` and associated name already exist. If the connection exists, the command/transfer will be run with that connection; otherwise, a new connection with the relevant information will be established.
