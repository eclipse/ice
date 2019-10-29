#!/bin/bash

# Build command that builds the SWT authorization package with the dependency on the Commands package. Note that the commands package must be built first, which is why the Commands build script is run first

# Build the Commands package
cd ../org.eclipse.ice.commands/
./build.sh

# Go back to the swt directory
cd ../org.eclipse.ice.commands.swt/

# Now build the SWT package with the Commands dependency
mvn clean install:install-file -DpomFile=../org.eclipse.ice.commands/pom.xml -Dfile=../org.eclipse.ice.commands/target/org.eclipse.ice.commands-0.0.1-SNAPSHOT.jar
