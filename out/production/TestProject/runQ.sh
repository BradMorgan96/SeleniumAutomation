#!/bin/bash
# script to tun QuoteStub process
#Quotestub 3 and 5 sort of work - but keys are wrong!
#
DIR1=/root/ca
DIR2=/root/ca/tls-ca/private
PASS=reassured
java -Djavax.net.ssl.keyStoreType=pkcs12 -Djavax.net.ssl.trustStoreType=jks -Djavax.net.ssl.keyStore=$DIR2/tls-ca.key -Djavax.net.ssl.trustStore=$DIR1/tls-ca.crt -Djavax.net.debug=ssl -Djavax.net.ssl.keyStorePassword=$PASS -Djavax.net.ssl.trustStorePassword=$PASS -cp /SeleniumTestNG/bin testNG.QuoteStub3
