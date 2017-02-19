#!/bin/bash
identify *.* | awk '{print $3}'
