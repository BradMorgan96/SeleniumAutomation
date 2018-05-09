#!/bin/bash
######################################################
#
# run a test in a loop forever - to test for hanging
# processes
#
######################################################
dt=`date +%F.%R.%S`
dt=`echo $dt | tr : .`
#
# set up directories
#
script=/SeleniumTestNG/scripts
tmp=/SeleniumTestNG/tmp
xml=/SeleniumTestNG/xml

#
# remove previous re-run files
#
rm -f $script/failures.xml
rm -f $tmp/*.dat
#

dtfile=$tmp/rundate.dat
rm -f $dtfile
log="/SeleniumTestNG/log/run_forever_"$dt".log"
touch $dtfile

echo "==============================================="  | tee >> $log
echo "#### Output to:" $log         | tee >> $log
echo "log is :" $log
echo "#### Running test suite"      | tee >> $log
echo "Date start:"`date +%F:%R.%S`  | tee >> $log
#
# get timestamp for email process
#
echo "Current PATH is :"            | tee >> $log
echo $PATH | tr ':' '\n'            | tee >> $log
#
#
#
echo "#### Running Forever suite"            | tee >> $log
echo "run testrun1.xml"                      | tee >> $log
while [ 1 -eq 1 ]
do
    java -cp /SeleniumTestNG/jar_lib/*:/SeleniumTestNG/bin/ org.testng.TestNG $xml/forever.xml | tee >> $log
done
#
#
echo "==============================================="   | tee >> $log
exit 0
