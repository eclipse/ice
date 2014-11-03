 
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

#Set the lib name
SET(LIBNAME ICEIO)

#Set the package name
SET(PACKAGE_NAME "org.eclipse.ice.io.native")

#Set the description
SET(PACKAGE_DESCRIPTION "Native implementations of the ICE I/O Packages for HDF5")

#Setup HDF5 libraries
include_directories(${HDF5_INCLUDE_DIRS})
include_directories(${Boost_INCLUDE_DIRS})

#Collect all cpp filenames in this project 
#and set it to SRC
file(GLOB SRC *.cpp)
#Compile only what is necessary for now

#Collect all header filenames in this project 
#and set it to HEADERS
file(GLOB HEADERS *.h)
#Compile only what is necessary for now

#Add a shared library to this project using the specified source files.
link_directories(${HDF5_LIBRARY_DIRS})
add_library(${LIBNAME} SHARED ${SRC})
target_link_libraries(${LIBNAME} ${HDF5_LIBRARIES})

#Set this property so that we can create shared and shared libraries with the same name
set_target_properties(${LIBNAME} PROPERTIES CLEAN_DIRECT_OUTPUT 1)

#Set the name of the shared library to ICEIO
set_target_properties(${LIBNAME} PROPERTIES OUTPUT_NAME ICEIO)

#Install the ICEIO shared library
install(TARGETS ${LIBNAME} LIBRARY DESTINATION lib)

#Install the ICEIO header files
install(FILES ${HEADERS} DESTINATION include)

#Set cpack variables
set(CPACK_PACKAGE_DESCRIPTION_SUMMARY ${PACKAGE_NAME})
set(CPACK_PACKAGE_NAME ${PACKAGE_DESCRIPTION})
set(CPACK_PACKAGE_VERSION "2.0.0")
SET(CPACK_PACKAGE_VERSION_MAJOR "2")
SET(CPACK_PACKAGE_VERSION_MINOR "0")
SET(CPACK_PACKAGE_VERSION_PATCH "0")
set(CPACK_PACKAGE_VENDOR "Oak Ridge National Laboratory")
set(CPACK_PACKAGE_CONTACT "billingsjj@ornl.gov")
set(CPACK_RESOURCE_FILE_LICENSE "${CMAKE_SOURCE_DIR}/license")

#Set package generators
set(CPACK_GENERATOR "DEB;RPM;TGZ;ZIP")

#These include statements are required for CPack
include(InstallRequiredSystemLibraries)
include(CPack)
