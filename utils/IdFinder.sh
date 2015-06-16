#!/bin/bash
# ******************************************************************************
# Copyright (c) 2015 UT-Battelle, LLC.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   Jordan H. Deyton - Initial API and implementation and/or initial documentation
#
# ******************************************************************************

# Create a message to describe the script.
msg="This script searches all Java source files for certain authors.\nSee IdFixer.pl for more info."
echo -e $msg

# This is a convenient function to ask a yes/no question and get a response.
# The first argument (if available) is the prompt question, which will
# automatically have a (Y/N) added after it. The function will not return until
# a Y, y, N, or n is entered. The result variable is set to true if yes, false 
# if no. This function also returns 0 for true and 1 for false.
result=false
function promptYesNo {
    # Get the prompt question. If none is specified, use a default prompt.
    local prompt="Are you sure?"
    if [[ $1 ]]; then
        prompt=$1
    fi
    # Prompt until a valid response is received (can be interrupted).
    result=false
    while read -p "$prompt (Y/N)" -r -n 1 -s answer; do
        echo
        if [[ $answer = [Yy] ]]; then
            result=true
            return 0
        elif [[ $answer = [Nn] ]]; then
            return 1
        fi
    done
}

# Prompt the user to continue.
if ! promptYesNo "Are you sure you wish to continue?"; then
    echo "Quitting..."
    exit 0
fi

# When creating tmp or log files, append a timestamp so we don't accidentally
# overwrite anything.
timestamp=$(date +%Y%m%d-%H%M)
# Create file names for a temporary file and a log file.
tmpOutFile="tmpOut-$timestamp.txt"
logFile="log-$timestamp.txt"

# Get all of the Java source files. We need the extra parentheses to make the 
# results into a bash array.
matches=($(find ../ -iname "*.java"))
nMatches=${#matches[@]}

# Process each matched source file.
for match in ${matches[@]}; do
    # Find the matching lines in the file.
    /usr/bin/perl IdFinder.pl < $match > $tmpOutFile
    # If matching lines were found, we need to print output.
    if [ -s $tmpOutFile ]; then
        # Print the matched file and all matching lines.
        echo "$match"
        cat $tmpOutFile
        # Print the same info to the log file.
        echo $match >> $logFile
        cat $tmpOutFile >> $logFile
    fi
    # Remove the temporary output file populated from the perl script.
    rm $tmpOutFile
done

echo "Done."
