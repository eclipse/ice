#!/bin/bash

# ******************************************************************************
# Copyright (c) 2015- UT-Battelle, LLC.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   Initial API and implementation and/or initial documentation -
#   Jordan H. Deyton
# ******************************************************************************

# This is the desired execution environment. I'm lazy, so it's not a command
# line argument.
ee="JavaSE-1.7"

# This variable helps save some space in the grep and regex below.
key="Bundle-RequiredExecutionEnvironment:"

# Print a helpful warning message first.
msg="\nThis script sets the flag \"$key\" to\n
     \"JavaSE-1.7\" for all bundle manifest files if the flag exists in said\n
     MANIFEST.MF.\n\n" 
echo -e $msg

# Prompt the user to continue.
read -p "Are you sure you wish to continue? " -n 1 -r
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
    echo
    exit 1
fi
echo

# Go up one directory. This should be the ice working directory.
cd ../

# Use find to find all MANIFEST.MF files, then use grep to find all files that
# do *not* have the execution environment set to JavaSE-1.7. Case is ignored
# for the file names and grep pattern matching.
matches=$(find . -iname 'MANIFEST.MF' -exec grep -iL "$key JavaSE-1.7" {} \;)

# For each match, do an in-place replacement of the previous execution
# environment.

# Here is the regex we use. Basically, it is:
# 1 - Bundle-RequiredExecutionEnvironment: (saved as group 1)
# 2 - Maybe some whitespace
# 3 - Any string of non-whitespace characters (saved as group 2)
# 4 - We don't care what's after that...
regex="($key)\s*(\S+)"

for match in $matches; do
    # These first two commands print the file that might be updated and what
    # the previous execution environment was.
    echo "Updating the file \"$match\"."
    perl -n -s -e '/$regex/i && print "Replacing \"$2\" with \"$ee\".\n";' -- -regex=$regex -ee=$ee $match
    # This last command actually replaces the execution environment in the
    # file, if the match is valid.
    perl -p -i -s -e 's/$regex/$1 $ee/i;' -- -regex=$regex -ee=$ee $match
done

# Let the user know the script is done.
echo "Done!"
