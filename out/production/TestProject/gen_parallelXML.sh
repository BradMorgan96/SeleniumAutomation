#!/bin/bash
#
# process to generate the selenium parallel.xml file from a 
# directory listing of test classes
#
#
out='/SeleniumTestNG/xml/parallel.xml'
echo "" > $out
echo '<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">'   >> $out
echo '<suite name="Parallel test runs" parallel="tests" thread-count="4">' >> $out


while read line
do
#
# remove the .java from the end of the directory listing
#
    class=`echo $line | cut -d'.' -f1`
	echo '<test name="'$class'">' >> $out
	echo '<classes>' >> $out
	echo '<class name="testNG.'$class'"></class>' >> $out
	echo '</classes>' >> $out
	echo '</test>' >> $out

done < tests.dat

echo '</suite>'   >> $out
