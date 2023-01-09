#!/bin/bash

echo ${WORKSPACE}
echo ${ProjectID}
echo ${Diskname}
sed 's/,/\n/g' ${Diskname}