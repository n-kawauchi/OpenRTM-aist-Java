#!/bin/sh

#if test "x$RTM_JAVA_ROOT" = "x" ; then
#    echo "Environment variable RTM_JAVA_ROOT is not set."
#    echo "Please specify the OpenRTM-aist installation directory."
#    echo "Abort."
#    exit 1
#fi

export CLASSPATH=.:../jar/OpenRTM-aist-0.4.1.jar:../jar/commons-cli-1.1.jar
java RTMExamples.SinCosOut.SinCosOutComp -f RTMExamples/SinCosOut/rtc.conf
