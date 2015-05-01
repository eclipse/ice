#!/bin/bash

# ******************************************************************************
# Copyright (c) 2015- UT-Battelle, LLC.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   Jordan H. Deyton - Initial API and implementation and/or initial documentation
# 
# ******************************************************************************

# What this file does:
# It looks for all Java, C, C++, and Python source files that do not include 
# ICE's default license in it. For each file, it prompts the script user for
# the license years, initial author(s), and contributors. It then prints the
# license out for review and, upon user input, will insert the license in the
# source file.

# What this file does NOT:
# 1 - Advanced license search: It just looks for the text 
#     "Copyright (c) ... UT-Battelle" somewhere in the source files.
# 2 - Look up years, initial author, contributors, etc. from the git repo.
# 3 - Handle other source files besides Java, C, C++, or Python.
# 4 - Bestow upon the user three wishes before vanishing into storage.


# Example find commands here: #################################################

# This finds all Java source files without a license in the source.
#matches=$(find . -type f -iname "*.java" -exec grep -iL "Copyright (c).*UT\-Battelle" {} \;)
# Java/C/C++ multiline comments look like:
# /*
#  comments here
#  */

# This finds all C and C++ source files without a license in the source.
#matches=$(find . -type f \( -iname "*.c" -o -iname "*.cpp" -o -iname "*.h" \) -exec grep -iL "Copyright (c).*UT\-Battelle" {} \;)
# C/C++ multiline comments are just like Java's.

# This finds all Python source files without a license in the source.
#matches=$(find . -type f -iname "*.py" -exec grep -iL "Copyright (c).*UT\-Battelle" {} \;)
# Python multiline comments look like:
# '''
# comments here
# '''
###############################################################################

# Go up one directory to the main "ice" folder.
cd ..
# This finds all Java, C, C++, and Python source files that lack a license.
matches=$(find . -type f \( -iname "*.java" -o -iname "*.c" -o -iname "*.cpp" -o -iname "*.h" -o -iname "*.py" \) -exec grep -iL "Copyright (c).*UT\-Battelle" {} \;)

# Set the default values for the year, author, and contributors.
years="$(date +"%Y")-" # Gets the current year.
author="Jay Jay Billings"
contributors=""

# Set up the headers for each supported type of source file.
cHeader="/*******************************************************************************"
cFooter=" *******************************************************************************/"
pyHeader="'''\n *******************************************************************************"
pyFooter=" *******************************************************************************\n'''"

tmpFile="/tmp/LicenseFixer.tmp"

# Process each matched file that is missing a license.
for match in $matches; do
    echo "File missing license: $match"
    
    # Prompt the user for the license years.
    read -p "Please enter the year(s) to use in the license (press enter for default=$years): " -r
    echo
    # This if says "if the reply is set and is not the empty string".
    if [ -n "$REPLY" ]; then
        years="$REPLY"
    fi
    
    # Prompt the user for the author.
    read -p "Please name the initial author of the file (press enter for default=$author): " -r
    echo
    if [ -n "$REPLY" ]; then
        author="$REPLY"
    fi
    
    # Prompt the user to see if we should use the same contributors.
    while read -p "Are the contributors the same as the previous file? (Y/N)" -r -n 1 -s answer; do
        echo
        if [[ $answer = [YyNn] ]]; then
            break
        fi
    done
    
    # If the contributors are not the same, prompt the user for the new contributors.
    if [[ $answer = [Nn] ]]; then
        read -p "Please enter any contributors (separated by commas; if none, press enter): " -r contributors
        echo
    fi
    
    # Prepare the license...
    
    # Determine the file extension.
    filename=$(basename "$match")
    extension="${filename##*.}"
    # Determine which style of comments to use.
    shopt -s nocasematch # Ignore case here.
    if [[ "$extension" =~ ^(py)$ ]]; then
        # Python-style multiline comments.
        header="$pyHeader"
        footer="$pyFooter"
    else
        # Assume Java/C-style multiline comments. We could also use: if [[ "$extension" =~ ^(java|c|cpp|h)$ ]]; then
        header="$cHeader"
        footer="$cFooter"
    fi
    shopt -u nocasematch # Turn off ignore case.

    # Build the license text.
    license="${header}\n * Copyright (c) ${years} UT-Battelle, LLC.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n *\n * Contributors:\n *    ${author} - initial API and implementation and/or initial documentation\n *   ${contributors}\n${footer}"
    
    # Prompt the user to make sure the license is OK.
    echo "The license for this file will be:"
    echo -e "$license"
    echo
    
    # Write the license to the file if it is accepted.
    while read -p "Apply this license to the file? (Y/N)" -r -n 1 -s answer; do
        echo
        if [[ $answer = [YyNn] ]]; then
            break
        fi
    done
    if [[ $answer = [Yy] ]]; then
        echo -e "$license" > "$tmpFile"
        /bin/cat "$match" >> "$tmpFile"
        mv "$tmpFile" "$match"
        echo "Updated the file: $match"
    else
        echo "Did not update the file: $match"
    fi
    echo
done

# Let the user know the script is done.
echo "Done!"
