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

# This script looks for lines to remove from Java source code. This includes:
# 1 - '// begin-user-code' and '// end-user-code'
# 2 - '<!-- begin-UML-doc -->' and '<!-- end-UML-doc -->'
# 3 - '@generated "UML..."' (the part in quotes could be on the next line)

# It prints out all remaining lines to STDOUT.
# It prints out all deleted lines (including line #) to STDERR.

my $generated="";
my $index=0;
while(<>) {
    $index++;
    if ($generated) {
        # If the previous line had an @generated tag, then this catches the
        # overflow on the next line (if there is any).
        $generated="";
        if (/^.*\".*\".*$/i) {
            print STDERR "Deleted line $index: ";
            print STDERR;
        } else {
            print;
        } 
    } elsif (/^\s*\/\/\s*(begin|end)-user-code\s*$/i) {
        # This catches all // begin|end-user-code lines.        
        print STDERR "Deleted line $index: ";
        print STDERR;
    } elsif (/^\s*\*+\s*(<!-- (begin|end)-UML-doc -->\s*)+$/i) {
        # This catches all <!-- begin|end-UML-doc --> lines.
        print STDERR "Deleted line $index: ";
        print STDERR;
    } elsif (/^\s*\*+\s*\@generated\s*(\".*\")?\s*$/i) {
        # This catches all @generated lines. If the "UML..." bit is missing, we
        # need to check the next line, hence the "generated" flag.
        print STDERR "Deleted line $index: ";
        print STDERR;
        unless ($1) {
            $generated="true";
        }
    } else {
        print;
    }
}
