#!/bin/bash
name=$1
identify $name | awk '{print $3}'
