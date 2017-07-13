#!/bin/bash
id=`ps -A | grep nautilus | awk '{print $1}'`
sudo kill $id
