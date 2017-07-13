#!/bin/sh

[ $# -ne 2 ] && { echo "Usage: pull-db PACKAGE-NAME DATABASE_NAME"; exit 1; }

PACKAGE=$1
DB=$2 

# exec-out can be run only on API21 and up
adb exec-out run-as $PACKAGE cat databases/$DB > $DB
