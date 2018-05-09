#!/bin/bash
proc=`echo $0 | cut -d'/' -f2` 
echo $proc
#
mlist='/SeleniumTestNG/tmp/mail.dat'
#
mysql --silent -u results  -D test_results<<<"call get_mail_addr()" > $mlist

#
cat $mlist
rm -f $mlist
#
#################
echo "call type 2"
export MYSQL_PWD=results
mysql --silent -uresults  <<EOF > $mlist
use test_results;
call get_mail_addr();
EOF
echo "done..."
cat $mlist
