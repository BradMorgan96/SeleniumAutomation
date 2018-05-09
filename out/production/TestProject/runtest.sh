#!/bin/bash
#####################################################
#
# script to run selenium tests
# 2015-10      Initial version
# 2015-11-03   copy each test-results.xml file to log dir
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
nglog=/SeleniumTestNG/scripts/test-output/test-results.xml
#
# remove previous re-run files
#
rm -f $script/failures.xml
rm -f $tmp/*.dat
#
# logs for testng run and fialures re-run
#
xlog="/SeleniumTestNG/log/test-results-run_"$dt".xml"
flog="/SeleniumTestNG/log/test-results-fai_"$dt".xml"

dtfile=$tmp/rundate.dat
rm -f $dtfile
log="/SeleniumTestNG/log/run_tests_"$dt".log"
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
## get CRM version from screen using selenium
#
echo "#### getting CRM version...."  | tee >> $log
echo "run getversion.xml"            | tee >> $log
java -cp /SeleniumTestNG/jar_lib/*:/SeleniumTestNG/bin/ org.testng.TestNG $xml/getversion.xml  | tee >> $log
#
#
echo "#### Running Selenium suite - pass 1"  | tee >> $log
echo "run testrun1.xml"                      | tee >> $log
java -cp /SeleniumTestNG/jar_lib/*:/SeleniumTestNG/bin/ org.testng.TestNG $xml/testing.xml | tee >> $log
echo "copy test-results.xml to "$xlog        | tee >> $log
cp $nlog $xlog
#
#
echo "==============================================="   | tee >> $log
echo "#### Running getFail.sh to generate failures run"  | tee >> $log

$script/getfail.sh  | tee >> $log
echo "==============================================="   | tee >> $log

if [ -s $tmp/failures.dat ] 
then
    echo "#### Running failed processes - pass 2"        | tee >> $log
    echo "run testfail.xml"                              | tee >> $log
    java -cp /SeleniumTestNG/jar_lib/*:/SeleniumTestNG/bin/ org.testng.TestNG $xml/testfail.xml  | tee >> $log
    echo "copy test-results.xml to "$flog        | tee >> $log
    cp $nlog $flog
    
else
    echo "#### no failures found"  | tee >> $log
fi
echo "Date end:"`date +%F:%R.%S`                         | tee >> $log
echo "#### Process Ends "                                | tee >> $log
echo "==============================================="   | tee >> $log
echo "emailing log"                                      | tee >> $log
#
# get email info
#
mdat=$tmp/mail.dat
file=$tmp/mailstuff.dat
jpg=$tmp/jpgstuff.dat
#
rm -f $file
##
echo "Selenium Test Run"          >> $file
echo "log is :" $log              >> $file
echo "Date start:" $dt            >> $file
#
# get all jpg files created since the start of the run
#
find /SeleniumTestNG/log -newer $dtfile | grep jpg >$jpg
num=`cat $jpg | wc -l`
echo "number of jpg files :"$num  >> $file
######################################################
# get email addresses from database
# user/password set up in .my.cnf
######################################################
mysql --silent -u results  -D test_results<<<"call get_mail_addr()" > $mdat
######################################################
# send email to each recipient set up on database
######################################################
while read email
do
    echo $email
    mutt -s "Selenium test results log" -a $log  -- $email < $file
done < $mdat
exit 0
