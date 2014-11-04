#!/bin/bash

# Compile the library
gcc -I/home/jaybilly/Programs/netcdf-4.3.2_gcc-4.8.3_exodusII/include/ -I/usr/lib/jvm/java-1.7.0/include/linux -I/usr/lib/jvm/java-1.7.0/include -I/home/jaybilly/Programs/exodus-6.06_gcc-4.8.3/include -fpic -c exodusII_wrap.c
# Link the library
gcc -Wl,--gc-sections -fpic -shared exodusII_wrap.o -o libexodusII_wrap.so -L /home/jaybilly/Programs/exodus-6.06_gcc-4.8.3/lib -lexodus
