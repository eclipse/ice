#! /bin/bash

func_bg_task() {
  rm -f bg-test.txt
  for i in {1..6}; do
   echo $i >> bg-test.txt
   sleep 10
  done
}

{ func_bg_task & } &
