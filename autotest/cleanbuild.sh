#!/bin/bash
#find . -name "*.jar" -type f | sudo xargs rm -rf
cd .. && find . -name "*build*" -type d |sudo  xargs rm -rf
