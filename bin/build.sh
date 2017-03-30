#!/usr/bin/env bash

######################################################
##Usage: The first parameter for user of BDWS  #######
######################################################

bin=$(dirname $0)
cd $bin
cd ..

USER_NAME=$1
SUDO_USER="sudo -u "$USER_NAME
DIST_PATH=`pwd`"/demo-dist/target/itm-config-dist-0.1.0-SNAPSHOT-bin"

mvn clean install -Dmaven.test.skip=true


