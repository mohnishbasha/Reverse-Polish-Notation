#!/bin/sh

APP_NAME="rpn"
APP_DIR="$APP_NAME-1.0-SNAPSHOT"
APP_PKG="$APP_DIR.zip"

startapp(){
./$APP_DIR/bin/$APP_NAME \
-J-XX:+CMSClassUnloadingEnabled \
-J-XX:+UseConcMarkSweepGC \
-J-XX:+HeapDumpOnOutOfMemoryError \
-J-XX:HeapDumpPath=. \
-J-server \
-Dconfig.file=application.conf \
-Dplay.evolutions.db.default.autoApply=true \
-Dhttp.port=9000 &
}

case "$1" in
   start)
    if [ -d $APP_DIR ]; then
     startapp
     sleep 10
     echo "Application started with process id $(cat $APP_DIR/RUNNING_PID)"
   else
    echo "Application directory not found. Use unzip." 
   fi

   ;;
   stop)
    if [ -f $APP_DIR/RUNNING_PID ]; then
      kill -9 $(cat $APP_DIR/RUNNING_PID)
      rm -f $APP_DIR/RUNNING_PID
      echo "$APP_NAME process stopped."
    else
      echo "$APP_NAME process not running."
    fi

   ;;
   clean)
    rm -rf $APP_DIR
    
   ;;
   cleanall)
    rm -rf $APP_DIR
    rm -rf $APP_PKG
    rm -rf logs/*
    
   ;;
   unzip)
    unzip $APP_PKG
    
   ;;
   reinstall)
     kill -9 $(cat $APP_DIR/RUNNING_PID)
     rm -rf $APP_DIR
     unzip $APP_PKG
     startapp
     
   ;;
   status)
    PSID=`ps -ef | grep $APP_NAME | grep -v grep | tr -s ' ' | cut -d ' ' -f2`
    RPID=$(cat $APP_DIR/RUNNING_PID)
    if [ "$PSID" = "$RPID" ]; then
        echo "Application $APP_NAME is running with PID $RPID."
    else
        echo "Application $APP_NAME is not running."
    fi

   ;;
   help)
    echo "start : start the application."
    echo "stop :  stop/kill the application."
    echo "status : checks the application process/pid is running."
    echo "clean : removes only the application directory."
    echo "cleanall : removes the application directory, logs, application package."
    echo "unzip : unzip the application package."
    echo "reinstall : stop/kill the application if running, clean the application directory, unzip the application package, starts the application."

    ;;
   *)
    echo "Usage: sh start.sh {start|stop|status|clean|cleanall|unzip|reinstall}"
    exit 1
esac
exit 0
