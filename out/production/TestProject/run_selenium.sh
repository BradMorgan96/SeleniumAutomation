#!/bin/bash
######################################################
dt=`date +%F.%R.%S`
dt=`echo $dt | tr : .`
#
# set up directories
#
script=/SeleniumTestNG/scripts
bin=/usr/bin
#
# remove previous re-run files
#
rm -f $script/failures.xml
rm -f $tmp/*.dat
#

dtfile=$tmp/rundate.dat
rm -f $dtfile
log="/SeleniumTestNG/log/run_selenium_"$dt".log"
hub="/SeleniumTestNG/log/selenium_hub_"$dt".log"
nod="/SeleniumTestNG/log/selenium_node_"$dt".log"
touch $dtfile

echo "==============================================="  | tee >> $log
echo "#### Output to:" $log               | tee >> $log
echo "log is :" $log
echo "#### Running selenium hub and node" | >> $log
echo "Date start:"`date +%F:%R.%S`        | tee >> $log

echo "starting hub...." | tee $log
java -jar $bin/selenium-server-standalone-2.47.1.jar -role hub -log $hub &
echo "hub pid is :"$! | tee $log
sleep 5

#
# done on separate box
#
#echo "starting node...." | tee $log
#
#java -jar $bin/selenium-server-standalone-2.47.1.jar -role node -hub "http://192.168.201.231:4444/grid/register/" -log $nod &
#
#echo "node pid is :"$! | tee $log
#
#
#
exit 0
