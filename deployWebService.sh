#!/bin/bash
AXIS_CLASSES_PATH=/home/cmihail/Documents/tomcat7.0/webapps/axis/WEB-INF/classes
cp -r bin/webservice $AXIS_CLASSES_PATH
cp -r bin/model $AXIS_CLASSES_PATH
java -cp $AXISCLASSPATH org.apache.axis.client.AdminClient -lhttp://localhost:8080/axis/services/AdminService deploy.wsdd
