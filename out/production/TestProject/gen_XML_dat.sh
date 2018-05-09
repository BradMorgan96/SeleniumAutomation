#!/bin/bash
####################################################
#
# this process uses test.dat as input, which can be created
# by running the script gen_test_dat.sh
#
# process to generate the selenium parallel_xxxxx.xml file from a
# directory listing of test classes
# each test found in the format controller_user_test.java
# is checked against an entry in test.dat which holds the
# output XML file name
#
####################################################
stub='/SeleniumTestNG'
dirout=$stub'/xml'
dirsrc=$stub'/src'
ftype='xx'
fsave='ZZ'
module='ZZ'
out='/xx.xml'
fdat=$stub/dat/test.dat
xdat=$stub/tmp/test.dat
sdat=$stub/tmp/test.srt
dt=`date +%F.%R.%S`
dt=`echo $dt | tr : .`
log="$stub/log/gen_XML_"$dt".log"
echo "log is : "$log
#
rm -f $sdat $xdat
#
# first pass - check all tests in src have a ref in .dat
#
echo "checking tests in src directory against test.dat entries"
#
for file in `ls $dirsrc/*_*_*.java`
do
#
## get bits from file name
#
#   echo "## file is : "$file
    file=`basename $file`
    module=`echo $file | cut -d'.' -f1`
#   echo "module is :" $module
    if grep -q "$module" "$fdat" 
    then
        grep "$module" "$fdat" >> $xdat
    else
        echo "WARNING - not found : "$module | tee $log
    fi
done
#
echo "sorting tests found to :" $sdat  | tee $log
#
# now sort data by output XML file to be generated
#
sort -t, -k+2 -k+1 $xdat > $sdat
#
####################################################
echo "process sorted file" | tee $log
#
while read line
do
#
## get bits from file name
#
#   echo "## test is :"$line
    ftype=`echo $line | cut -d',' -f2`
    module=`echo $line | cut -d',' -f1`

#   echo "## ftype is:"$ftype
#   echo "## module is :"$module

#
## decide output file name
#
    if [ "$fsave" != "$ftype" ]
    then
#
## end old file if required
#
        if [ "$fsave" != "ZZ" ]
        then
            echo "Tests found :" $num | tee $log
            num=0
            echo '</suite>'   >> $out
        fi
        echo "New file type found "$ftype | tee $log
#
# new output file
#
        out=$dirout/"parallel_"$ftype".xml"
        echo "New file :" $out | tee $log
        rm -f $out
        echo '<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">'   >> $out
        echo "<suite name='Parallel test "$ftype"' parallel='tests' thread-count='100'>" >> $out

        fsave=$ftype
    fi
#
## output test info
#
  echo '<test name="'$module'">'    >> $out
  echo '<classes>' >> $out
  echo '<class name="testNG.'$module'"></class>' >>$out
  echo '</classes>' >>$out
  echo '</test>' >> $out
  let "num++"
done < $sdat
#
echo "</suite>" >> $out
echo "Tests found :" $num | tee $log
echo "process ends"
#
#
