#!/bin/bash

# This is a utility script for running the the jar with dependencies if the jar is in the same folder as the script

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
SCRIPT_DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"

JAR_DIR=$SCRIPT_DIR/qr-game-core-jar-with-dependencies.jar

unameOut="$(uname -s)"
case "${unameOut}" in
    CYGWIN*) JAR_DIR=$(cygpath -w $JAR_DIR)
esac

java -jar $JAR_DIR $@