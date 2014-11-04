#!/bin/bash
projectDir=$PWD
outDir=$PWD/src
echo $1 $2
cd $1
swig -I$2 -cpperraswarn -outdir $outDir -java -package org.eclipse.ice.exodusII $projectDir/exodusII.i
