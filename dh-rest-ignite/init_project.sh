#!/usr/bin/env bash

echo "script usage is sh init_project.sh <base_package_name>"

if [ !n "$1" ]
then
   echo "script usage is sh init_project.sh <base_package_name>"
   exit 1
fi

echo "try to init spring boot project......."

BASE_PACKAGE_NAME=$1
PACKAGE_FOLDER=`echo ${BASE_PACKAGE_NAME} | sed -e 's/\./\//g'`
echo $BASE_PACKAGE_NAME
BASE_DIR=`pwd`
JAVA_SRC_PATH=src/main/java/${PACKAGE_FOLDER}

DEFAULT_FOLDERS="service controller entity repository dto"
for folder in $DEFAULT_FOLDERS
do
    echo "creating "$folder
    echo ${JAVA_SRC_PATH}/$folder
    mkdir -p ${JAVA_SRC_PATH}/$folder
done