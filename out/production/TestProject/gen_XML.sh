#!/bin/bash
#
# process to generate the selenium parallel.xml file from a
# directory listing of test classes
#
stub='/SeleniumTestNG'
dirout=$stub'/xml'
dirsrc=$stub'/src'
ftype='xx'
fsave='zz'
module='ZZ'
out='/xx.xml'

for file in `ls $dirsrc/*£*.java`
do
#
## get bits from file name
#
#   echo "## file is :"$file
    file=`basename $file`
    ftype=`echo $file | cut -d'£' -f1`
    module=`echo $file | cut -d'.' -f1`

#   echo "## ftype is:"$ftype
#   echo "## module is :"$module

#
## decide output file name
#
    if [ "$fsave" != "$ftype" ]
    then
        echo "new file type found "$ftype
        fsave=$ftype
        dohead="YES"
#
## end old file if required
#
        if [ "$ftype" != "ZZ" ]
        then
            echo '</suite>'   >> $out
#
# new output file
#
            out=$dirout/"parallel_"$ftype".xml"
            echo "new output file:"$out
            echo "" > $out
            echo '<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">'   >> $out
            echo "<suite name='Parallel test "$ftype"' parallel='tests' thread-count='100'>" >> $out

        fi
    fi
#
## output test info
#
  echo '<test name="'$module'">'    >> $out
  echo '<classes>' >> $out
  echo '<class name="testNG.'$module'"></class>' >>$out
  echo '</classes>' >>$out
  echo '</test>' >> $out
done
echo "</suite>" >> $out
