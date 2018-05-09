#!/bin/bash
#
###################################################
#
# process to monitor running selenium tests using ChromeDriver
# if process has been running over a defined length of time
# process is killed to free up thread
# 2015-11-03 Initial version
# 2015-11-13 PIDs recycle after about 32K, so tail the 
# pall file to age out pids
#
##################################################
dt=`date +%F.%R.%S`
dt=`echo $dt | tr : .`
stub=/SeleniumTestNG
log=$stub/scripts/monitor_$dt".log"
pout=$stub/scripts/ps.dat
pall=$stub/scripts/ps.all
pchk=$stub/scripts/ps.chk
##
rm -f $pout $pall $pids
touch $pout
touch $pall
####
echo $dt" Process Starts"  >> $log
while [ 1 -eq 1 ]
do
    dt=`date +%F.%R.%S`
    dt=`echo $dt | tr : .`
    ps -f --forest -C chrome | grep -v ^UID  > $pout
    if [ -s $pout ]
    then
        found='NO'
        tail -25 $pall >$pchk
        cat $pchk > $pall
##
        while read line
        do
            line=`echo $line | tr -s " "`
            pid=`echo $line | cut -d' ' -f2`
#
# check to see if process is in file....
#

            if grep $pid $pall
            then
                found='YES'
            else
#               echo $dt " adding "$pid  | tee >> $log
                echo $pid >> $pall
            fi

        done < $pout
#
# kill only last
#
        if [ "$found" == "YES" ]
        then
            echo $dt "found, killing " $pid   >> $log
            kill -9 $pid
        fi
    else
        echo $dt" files empty"   >> $log
    fi
    sleep 60
done

