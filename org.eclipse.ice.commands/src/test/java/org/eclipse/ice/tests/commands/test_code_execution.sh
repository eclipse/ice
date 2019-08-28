#!/bin/bash
#*******************************************************************************
# * Copyright (c) 2019- UT-Battelle, LLC.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
# *
# * Contributors:
# * Initial API and implementation and initial documentation -
# *  Joe Osborn
# *****************************************************************************



# This is a test executable shell script for running a job locally.
# It is for testing the LocalCommand chain of execution.
# The script takes some input from a dummy text file and prints it
# to the screen. It is in no way intended to be robust, just to show
# basic usage of an executable command functioning.


# Print a boring string out to the console screen
STRING="This is a hello world executable"
echo $STRING

# list the contents of this directory
ls -lrt

# Read in a dummy file
echo "I will read from file "$1

# Set a variable to the argument value
input=$1

# Read in the data
while IFS= read -r line
do
    echo "$line"
done < "$input"
