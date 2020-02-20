#!/bin/bash

script="test_code_execution.sh"
inputFile="someInputFile.txt"
userhostname=$1
hostCdirectory=$2
keypath=$3

# Make a directory to work in on the server
ssh -i $keypath $userhostname "mkdir $2"

# scp the files to that directory
scp -i $keypath $inputFile $userhostname:$2
scp -i $keypath $script $userhostname:$2


# run the script which executes the job
ssh -i $keypath $userhostname "cd $2; chmod 700 $script; ./$script $inputFile;"
