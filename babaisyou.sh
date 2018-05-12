#!/bin/bash

# Ce script compile BabaIsYou si nécéssaire puis le lance.
# Java 8 et Apache Ant sont nécéssaires.



# Compile si nésséssaire, c'est à dire si le dossier bin n'est
# pas présent

if [ ! -d bin ]; then
    echo "Compiling..."
    ant build
fi

# Lance le programme

echo "Launching"
ant run
