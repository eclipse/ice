 # Copyright (c) 2012, 2014 UT-Battelle, LLC.
 # All rights reserved. This program and the accompanying materials
 # are made available under the terms of the Eclipse Public License v1.0
 # which accompanies this distribution, and is available at
 # http://www.eclipse.org/legal/epl-v10.html
 #
 # Contributors:
 #   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 #   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 #   Claire Saunders, Matthew Wang, Anna Wojtowicz

#Set the minimum required version of cmake for this project.
cmake_minimum_required(VERSION 2.8)

#Set a name for this project.
project(native)

# ICE requires C++11 to use shared pointers
list( APPEND CMAKE_CXX_FLAGS "-std=c++0x ${CMAKE_CXX_FLAGS}")

#Find boost
FIND_PACKAGE(Boost 1.35 REQUIRED)

#Find HDF5
FIND_PACKAGE(HDF5 COMPONENTS CXX REQUIRED)

#Enable testing.
enable_testing()

#Add the I/O, data structures and reactor directories
add_subdirectory(src/native/org.eclipse.ice.io.native)
add_subdirectory(src/native/org.eclipse.ice.datastructures.native)
add_subdirectory(src/native/org.eclipse.ice.reactor.native)
add_subdirectory(src/native/org.eclipse.ice.reactor.sfr)

#Add the test directories
add_subdirectory(tests/native/org.eclipse.ice.io.native.test)
add_subdirectory(tests/native/org.eclipse.ice.reactor.native.test)
add_subdirectory(tests/native/org.eclipse.ice.reactor.sfr.test)
add_subdirectory(examples)

#Create the documentation directory and set up the doc target.
set(DOC_DIR "${CMAKE_BINARY_DIR}/documentation")
if(NOT EXISTS ${DOC_DIR})
    execute_process(COMMAND ${CMAKE_COMMAND} -E make_directory "${DOC_DIR}")
endif(NOT EXISTS ${DOC_DIR})
set(DOXYFILE_OUTPUT_DIR "${DOC_DIR}")
set(DOXYFILE_LATEX "ON")
set(CMAKE_MODULE_PATH "${PROJECT_SOURCE_DIR}")
include(UseDoxygen REQUIRED)
