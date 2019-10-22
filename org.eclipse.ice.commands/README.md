## Commands Package

This README serves as an overview of the commands package, which is a standalone maven package that can be used within or outside of ICE. The package provides the necessary API to set up and run jobs on either one's local computer or a remote host. Additionally, the API includes file transfer capabilities, with the option to move or copy files on the local host or remote host. 

Examples can be found in either the `src/test/java/org/eclipse/ice/tests/commands` directory or in the standalone package within ICE `org/eclipse/ice/demo/commands/`. 


### Build Instructions
Building the package can be performed like any other maven package, where the end result is a jar file that can be included as a dependency
```
$ mvn clean install
```

To skip the tests, include `-DskipTests` in your build. 

#### Note about tests
The automated testing is performed with a dummy remote host, which has private credentials. Thus, if the tests are built with the package, a significant portion of the tests will fail due to the fact that the dummy remote host credentials are not distributed publicly. To solve this, one may enter any generic remote host credentials into the file `/tmp/ice-remote-creds.txt` in the following order

```
username 
password
hostname
```
The automated tests will then grab the necessary credentials from this file to run the tests. Any valid ssh connection will work. 

Note that this is also a way through which ssh validation can be performed in the package for running actual remote commands/file transfers.

Windows users need to put their ssh credentials into the file located at `C:\ice-remote-creds.txt` in order for the tests to properly function.


### Note on Dependencies
If you use this package as a dependency in your own Maven project, you may find that an error arises when building if you also include the log4j package in your dependencies. If you are trying to use log4j in your own Maven project, simply add the following snip of code to the Commands pom file inside the `<dependency>` tag:

```
<exclusions>
	<exclusion>
		<groupId>org.slf4j</groupId>
		<artifactId>*</artifactId>
	</exclusion>
</exclusions>
```
This will remove the Commands dependency on log4j. Note that the same structure would apply to any other dependency that Commands and some parent package might both depend on.
