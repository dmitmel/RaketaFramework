#!/usr/bin/env bash

# Getting script directory (code supports symlinks)
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
    DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"
    SOURCE="$(readlink "$SOURCE")"
    # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
    [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
done
DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"
cd $DIR

# VisTEG-plugin will generate tasks graph when you're launching task. It doesn't matter how task will finish
gradle build

cd build/reports
dot -Tpng tasks-graph.dot -o tasks-graph.dot.png   # Generating PNG image from .dot graph
