 
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
project(org.eclipse.ice.updater)

#Enable QT testing.
enable_testing()

#Add the src subdirectory.
add_subdirectory(src)

#Add the tests subdirectory.
add_subdirectory(tests)

#Add a UpdaterTestsConfig.h header file to store the TESTS_BUILD_PATH variable for use in the tests.
configure_file ("${CMAKE_CURRENT_SOURCE_DIR}/tests/TesterUtilsConfig.h.in" "${CMAKE_CURRENT_SOURCE_DIR}/tests/TesterUtilsConfig.h")

#The following CONFIGURE_FILE function calls move files to the tests
#build directory so that the the test executables can access them.
configure_file(updater.properties ${TESTS_BUILD_PATH}/updater.properties COPYONLY)
configure_file(updatertests.properties ${TESTS_BUILD_PATH}/updatertests.properties COPYONLY)
configure_file(tests/SampleText.txt ${TESTS_BUILD_PATH}/SampleText.txt COPYONLY)

# The Doxygen script is not available in the git repo. --jdeyton 20150130
##Create the documentation directory and set up the doc target.
#set(DOC_DIR "${BUILD_PATH}/documentation")
#if(NOT EXISTS ${DOC_DIR})
#    execute_process(COMMAND ${CMAKE_COMMAND} -E make_directory "${DOC_DIR}")
#endif(NOT EXISTS ${DOC_DIR})
#set(DOXYFILE_OUTPUT_DIR "${DOC_DIR}")
#set(DOXYFILE_LATEX "ON")
#set(CMAKE_MODULE_PATH "${PROJECT_SOURCE_DIR}")
#include(UseDoxygen OPTIONAL)