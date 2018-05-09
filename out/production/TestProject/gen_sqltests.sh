#!/bin/bash
#######################
# create a file to insert test details into database
# from all tests in the src directory
#######################
src=/SeleniumTestNG/src
out=/SeleniumTestNG/scripts/load_tests.sql

echo "" > $out
#
mysql --silent -uresults  <<EOF
use test_results;
truncate table test_details
EOF

#
for xx in `ls $src/*£*`; 
do
  test=`echo $xx| cut -d'.' -f1`;
  name=`basename $test`
  echo $name;
  cont=`echo $name | cut -d'£' -f1`
  user=`echo $name | cut -d'£' -f2`
  code=`echo $name | cut -d'£' -f3`
  echo "Controller: " $cont " user: " $user " code: " $code
  if [ $cont == 'Users' ]
  then
    xml=2
  else
    xml=1
  fi
#
  echo "insert into test_details ( fullname,controller,user,test ) values ('"$name"','"$cont"','"$user"','"$code"');" >> $out
#
mysql --silent -uresults  <<EOF 
use test_results;
insert into test_details ( fullname,controller,user,test,px_id ) values ('$name','$cont','$user','$code',$xml) 
EOF

#
done

