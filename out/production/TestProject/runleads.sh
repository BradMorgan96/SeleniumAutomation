#!/bin/bash
log="/SeleniumTestNG/log/run_tests_"$(date +%F:%R)".log"
echo "===============================================" | tee $log
echo $0 | cut -d'/' -f2 | tee $log
echo "#### Output to:"$log
log=`echo $log | tr -d '\b\r'`
echo "#### Running test suite" | tee $log
echo "Date start:"`date +%F:%R` | tee $log
echo "Current PATH is :"  | tee $log
echo $PATH | tr ':' '\n'  | tee $log
xml=/SeleniumTestNG/xml
#
# remove previous re-run files
#
rm -f /SeleniumTestNG/scripts/failures.xml
rm -f /SeleniumTestNG/scripts/skips.xml
rm -f /SeleniumTestNG/tmp/*.dat
#
## get CRM version from screen using selenium
#
echo "#### getting CRM version...." | tee $log

java -cp /SeleniumTestNG/jar_lib/*:/SeleniumTestNG/bin/ org.testng.TestNG $xml/getversion.xml | tee $log

cat /SeleniumTestNG/tmp/version.dat | tee $log

#
#
echo "#### Running Selenium suite - pass 1" | tee $log
java -cp /SeleniumTestNG/jar_lib/*:/SeleniumTestNG/bin/ org.testng.TestNG $xml/leads.xml | tee $log
#
#
echo "===============================================" | tee $log
echo "#### Running getFail.sh to generate failures run" | tee $log

/SeleniumTestNG/scripts/getfail.sh | tee $log
echo "===============================================" | tee $log

if [ -s /SeleniumTestNG/tmp/failures.dat ] 
then
    echo "#### Running failed processes - pass 2" | tee $log
    java -cp /SeleniumTestNG/jar_lib/*:/SeleniumTestNG/bin/ org.testng.TestNG $xml/testfail.xml | tee $log
else
    echo "#### no failures found" | tee $log
fi
echo "Date end:"`date +%F:%R` | tee $log
echo "#### Process Ends " | tee $log
echo "===============================================" | tee $log
exit 0
