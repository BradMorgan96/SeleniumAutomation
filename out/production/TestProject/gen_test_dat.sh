#!/bin/bash
######################################################################
#
# process to update the test.dat file with new tests found 
# in the src directory. Each test found is checked to see
# if it is referenced in test.dat If not, an entry is added
# using the controller from the test name as the default
#
# contents of test.dat are read, and if no source java code found
# line is removed from the test.dat file
# test.dat can then be edited manually
#
# then run gen_XML_dat.sh to generate parallel_xxxxxx.xml files
# where xxxxxx represents the controller
#
######################################################################
stub='/SeleniumTestNG'
dirsrc=$stub'/src'
ftype='xx'
fsave='ZZ'
module='ZZ'
out='/xx.xml'
fdat=$stub/dat/test.dat
dt=`date +%F.%R.%S`
dt=`echo $dt | tr : .`
log="$stub/log/gen_test_dat_"$dt".log"
numAdd=0
numOk=0
echo "log is : "$log
#
# check all tests in src have a ref in test.dat
#
echo "checking tests in src directory against test.dat entries" | tee $log
#
for file in `ls $dirsrc/*_*_*.java`
do
#
## get bits from file name
#
#   echo "## file is : "$file
    file=`basename $file`
    module=`echo $file | cut -d'.' -f1`
    cont=`echo $file | cut -d'_' -f1`
#   echo "module is :" $module
    if grep -q "$module" "$fdat" 
    then
        let "numOk++"
    else
        echo "WARNING - not found : "$module | tee $log
        let "numAdd++"
        echo $module","$cont >> $fdat        
    fi
done
echo "Tests unchanged :"$numOk  | tee $log
echo "New tests added :"$numAdd | tee $log
#
# check tests in test.dat, and if code doesnt exist, delete from test.dat
#
echo "checking tests in test.dat against files in " $dirsrc | tee $log
tmp=$stub/tmp/test.tmp
rm -f $tmp
numDel=0
while read line
do
    module=`echo $line | cut -d',' -f1`
    module=$module".java"
    if  [ -f $dirsrc/$module ]
    then
        echo $line >> $tmp
    else
        echo "remove:"$line
        let "numDel++"
    fi
done < $fdat
sort -t, -k+2 -k+1 $tmp > $fdat
echo "Tests removed   :"$numDel | tee $log
####################################################
