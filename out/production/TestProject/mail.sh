#!/bin/bash
sub="'this is the email subject'"
file='/SeleniumTestNG/xml/leads.xml'
mailto="simon.abraham@reassured.co.uk"
mlist='/SeleniumTestNG/tmp/mail.dat'
#
# get list of email recipients
#
mysql --silent -u results -presults -D test_results<<<"call get_mail_addr()" > $mlist
#
#
while read line
do
    echo $line
#    mutt -s `echo $sub` -a $file -- $line < /SeleniumTestNG/scripts/content.txt
done < $mlist
