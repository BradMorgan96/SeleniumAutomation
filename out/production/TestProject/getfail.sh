#!/bin/bash
#
stub=/SeleniumTestNG
fails=$stub/tmp/failures.dat
xml=$stub/xml/failures.xml
cat test-output/testng-results.xml | grep FAIL > $fails
rm -f $xml
touch $xml
echo "temp file is:" $fails
echo '<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">'          >>$xml
echo '<suite name="Parallel test runs" parallel="tests" thread-count="4">' >>$xml
while read line
do
#   echo "####"
#   echo $line
    test=`echo $line | cut -d':' -f3`
    test=`echo $test | cut -d'@' -f1`
    test=`echo $test | cut -d'.' -f2`
    echo "#### test=" $test
    echo '<test name="'$test'">'                 >> $xml
    echo '<classes>'                             >> $xml
    echo '<class name="testNG.'$test'"></class>' >> $xml
    echo '</classes>'                            >> $xml
    echo '</test>'                               >> $xml
done < $fails
echo '</suite>'                                  >>$xml
