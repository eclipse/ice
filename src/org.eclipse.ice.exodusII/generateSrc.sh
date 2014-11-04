#!/bin/bash

# Run SWIG
./exodusII_SWIG_gen.sh ~/Programs/exodus-6.06_gcc-4.8.3/include/ ~/Programs/netcdf-4.3.2_gcc-4.8.3_exodusII/include/

# Build the library
./build.sh

cp libexodusII_wrap.so os/linux/x86_64/libexodusII_wrap.so
mv src/* src/org/eclipse/ice/exodusII/
