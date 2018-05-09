#!/bin/bash
####
log="/SeleniumTestNG/log/compile_"$(date +%F:%R)".log"
echo "Output to:"$log
cp /SeleniumTestNG/src/SetEnvironment.java.231 /SeleniumTestNG/src/SetEnvironment.java
echo "Compile source" | tee $log
#
#
ant -buildfile build.xml | tee $log

