
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
 
#Attempt to locate the Boost 1.4 required thread library.
find_package(Boost 1.40 COMPONENTS thread filesystem REQUIRED)

#include directories from the source
include_directories(${Boost_BINARY_DIRS})
include_directories(${Boost_INCLUDE_DIRS})
include_directories(../src)

#link the directories
#link_directories(${Boost_LIBRARY_DIRS})

#file(GLOB tests *Tester.cpp)
file(GLOB tests *Tester.cpp)

#If boost was found, create tests
if(Boost_FOUND)

	#loop over each of the tester files and add them as tests
	foreach(test ${tests})
		
		#Get the file name component without the end tag
		get_filename_component(testName ${test} NAME_WE)

		#add executable
		add_executable(${testName} ${test} TesterUtils.cpp)
		
		#Link the libraries
		target_link_libraries(${testName} ${Boost_LIBRARIES} updater-static curl)
		
		#add the test
		add_test(${testName} ${testName})		
	
	endforeach()
	
endif()


#Set cpack variables
set(CPACK_PACKAGE_DESCRIPTION_SUMMARY ${PACKAGE_NAME})
set(CPACK_PACKAGE_NAME ${PACKAGE_DESCRIPTION})
set(CPACK_PACKAGE_VERSION "2.0.0")
SET(CPACK_PACKAGE_VERSION_MAJOR "2")
SET(CPACK_PACKAGE_VERSION_MINOR "0")
SET(CPACK_PACKAGE_VERSION_PATCH "0")
set(CPACK_PACKAGE_VENDOR "Oak Ridge National Laboratory")
set(CPACK_PACKAGE_CONTACT "billingsjj@ornl.gov")
#set(CPACK_RESOURCE_FILE_LICENSE "${CMAKE_SOURCE_DIR}/license")

#Set package generators
set(CPACK_GENERATOR "DEB;RPM;TGZ;ZIP")

#These include statements are required for CPack
include(InstallRequiredSystemLibraries)
include(CPack)
