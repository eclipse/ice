#!/bin/bash

LIB_NAME=exodusII
# Important files include:
# exodusII.i - The input for SWIG
# exodusII_wrap.c - SWIG-generated wrappers
# libexodusII_wrap.so - The linked library generated from exodusII_wrap.c


PROJECT_DIR=$PWD
SRC_DIR=$PROJECT_DIR/src
LIB_DIR=$PROJECT_DIR/os/linux/x86_64

PACKAGE_NAME=org.eclipse.ice.$LIB_NAME
PACKAGE_DIR=$SRC_DIR/org/eclipse/ice/$LIB_NAME

LIBS_DIR=/home/djg/libs/os/linux/x86_64

# Run SWIG. This generates exodusII_wrap.c (${LIB_NAME}_wrap.c)
swig -I$LIBS_DIR/include -I$ROOT_EXODUS/include -cpperraswarn -outdir $SRC_DIR -java -package $PACKAGE_NAME $PROJECT_DIR/$LIB_NAME.i

# Compile the libraries.
gcc -I$LIBS_DIR/include -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -I$ROOT_EXODUS/include -fpic -c ${LIB_NAME}_wrap.c
gcc -Wl,--gc-sections -fpic -shared ${LIB_NAME}_wrap.o -o lib${LIB_NAME}_wrap.so -L $ROOT_EXODUS/lib -lexodus

# Move the libraries to the proper directories.
mv lib${LIB_NAME}_wrap.so $LIB_DIR
mv $SRC_DIR/*.java $PACKAGE_DIR

# Clean up.
rm exodusII_wrap.o