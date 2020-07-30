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

# This is a hello world test python script for
# testing the commands functionality
# with python rather than a shell script
import os, sys

filename1 = sys.argv[1]
filename2 = sys.argv[2]

print("This is a hello world python script")

f = open(filename1,'r')
print (f.read())
f.close()

f2 = open(filename2,'r');
print(f2.read())
f2.close()
