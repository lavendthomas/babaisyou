#!/bin/bash

# Ce script compile BabaIsYou si nécéssaire puis le lance.
# Java 8 et Apache Ant sont nécéssaires.

# Check if ant is installed 
if ! [ -x "$(command -v ant)" ]; then
    echo 'Error: ant is not installed.' >&2
    exit 1
fi

# Check if java is installed 
if ! [ -x "$(command -v java)" ]; then
    echo 'Error: java is not installed.' >&2
    exit 1
fi


# Compile si nésséssaire, c'est à dire si le dossier bin n'est
# pas présent

if [ ! -d bin ]; then
    echo "Compiling..."
    ant build
fi

# Lance le programme

echo "Launching"
ant run
