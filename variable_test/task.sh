#!/bin/bash

echo ${WORKSPACE}
echo ${ProjectID}
echo ${Diskname}
was=`echo ${Diskname} | awk -F, '{print $1}'`
web=`echo ${Diskname} | awk -F, '{print $2}'`
echo ${was}
echo ${web}







