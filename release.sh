#!/bin/bash

PWD=$(pwd)

TARGET_DIR=${HOME}/tmp/testsuit
TOP_DIR=${PWD}/..
testsuit_aar=${TOP_DIR}/libtestsuit/build/outputs/aar/libtestsuit-release.aar

check_env()
{
    rm -rf $TARGET_DIR
    mkdir -p $TARGET_DIR
}

build()
{
  cd ..
  rm -rf $(find . -name ./libtestsuit/build/outputs/aar/libtestsuit*.aar)

  ./gradlew :libtestsuit:assembleRelease
  if [ $? -ne 0 ]; then
    echo "build failed"
    exit 1
  fi

  if [ ! -f ${testsuit_aar} ]; then
    echo " build failed, cannot find aar file"
    exit 1
  fi
  cd -
}

collect()
{
  cp ${testsuit_aar} ${TARGET_DIR}
  ZigbeeTest=$(find ${TOP_DIR} -name ZigbeeTest.java)
  cp ${ZigbeeTest} ${TARGET_DIR}
}

check_env
build
collect