#!/usr/bin/perl -w
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

# This script looks for the specified tag in the source code, e.g., the @author
# tag that is typically placed in class documentation multiline comments.
# The script then finds all lines between the tag and either the next tag or 
# the end of the multiline comment with a specified regex (e.g., a specific 
# author) and prints it out like so:
# line#:line

# For example, if the tag is '\@foo' and the regex is 'bar', then for the input
# /**
#  * @foo one, two, bar, four
#  *      next line, no match
#  *      
#  *      bar
#  */
# You would get output like:
# 2: * @foo one, two, bar, four
# 3: *      next line, bar
# 5: *      bar

# TODO Make the regex a command line argument.
my $regex='\w{3}';
my $tag='\@authors?';

my $index=0;  # The line number in STDIN.
my $inTag=""; # True if the current line is part of the specified tag.
while(<>) {
    $index++;
    
    # If the line contains the tag(s), we need to parse it. Otherwise, stop 
    # parsing the line if it is the end of the comments or if it contains the 
    # start of another tag.
    if (/^.*$tag.*$/i) {
        $inTag="true";
    } elsif (/^\s*\*+\/.*$/i) {
        $inTag="";
    } elsif (/^.*\@.*$/i) {
        # Technically, there could be more stuff before the next tag starts.
        $inTag="";
    }
    
    # Parse the line to see if it contains the desired regex in the list of 
    # authors.
    if ($inTag && /^\s*\*+\s*($tag\s+|.*\,)?\s*$regex\s*(\,.*)?$/i) {
        # Print the line # and the line content.
        print "$index\:$_";
    }
}
