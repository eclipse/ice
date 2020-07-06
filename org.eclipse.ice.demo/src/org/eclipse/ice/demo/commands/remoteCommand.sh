#/*******************************************************************************
# * Copyright (c) 2019- UT-Battelle, LLC.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
# *
# * Contributors:
# *   Initial API and implementation and/or initial documentation - 
# *   Jay Jay Billings, Joe Osborn
# *******************************************************************************/

#!/bin/bash

script="test_code_execution.sh"
inputFile="someInputFile.txt"
userhostname=$1
hostCdirectory=$2
keypath=$3

# Make a directory to work in on the server
ssh -i $keypath $userhostname "mkdir $2"

# scp the files to that directory
scp -i $keypath $inputFile $userhostname:$2
scp -i $keypath $script $userhostname:$2


# Run the script which executes the job
ssh -i $keypath $userhostname "cd $2; chmod 700 $script; ./$script $inputFile;"

# Delete the directory when you are finished running it
ssh -i $keypath $userhostname "rm -rf $2"
