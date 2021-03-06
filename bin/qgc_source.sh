#!/bin/bash

# This is a utility script for running the compiled source in a CLI. Requires that you run mvn install first.

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
JAR_DIR=$SCRIPT_DIR/../target/qr-game-core-1.2.0-SNAPSHOT-jar-with-dependencies.jar

unameOut="$(uname -s)"
case "${unameOut}" in
    CYGWIN*) JAR_DIR=$(cygpath -w $JAR_DIR)
esac

java -jar $JAR_DIR $@