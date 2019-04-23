#!/bin/bash
# 功能：
#1、指定用户运行——未实现
#2、默认logs目录——可否另外指定？
#3、JMX监控——待实现
#4、设置JVM参数（指定程序运行名称、时区、内存大小、GC回收器设置、gc日志文件）
#5、指定配置文件的环境变量
#6、start 启动时检查程序是否已经启动
#7、stop 停止程序、stop之前会先dump，kill失败检查，待完善：然后用kill -9,有可能存在多个server
#8、status 查看程序是否运行
#9、restart 重启
#10、dump dump系统状态和jvm信息到文件中
#11、配置文件使用哪种方式更好？
#12、默认的jvm配置
#13、JMX支持——待实现
#14、增加远程调试接口
# 待补充端口占用检查

#使用：
#1、上传.sh脚本到服务器中,授权脚本执行权限sudo chmod 755 ./app.sh
#2、启动示例：./app.sh start 
#3、重启示例：./app.sh restart 
#4、停止示例：./app.sh stop 
#5、其他示例：./app.sh restart pro debug

echo "参数：p0=$0,p1=$1,p2=$2,p3=$3"
## p0=./app.sh,p1=restart,p2=pro,p3=debug

# 变量 
RUNNING_USER=reven # 指定运行用户
SERVER_NAME=springboot-demo # jar的名字
# 项目中日志地址
LOG_PATH=logs/info.log
# 部署启动指定的配置文件
# DEPLOY_CONFIG=deploy_config/deploy.yml
# 远程调试端口
XDEBUG_ADDRESS=8000

ENV=$2 #指定启动的环境profile，暂时未使用
typeset -l Xdebug  #转换为小写
Xdebug=$3 #参数为debug表示启用远程调试监听

ADATE=`date +%Y%m%d%H%M%S`
APP_HOME=`pwd`

dirname $0|grep "^/" >/dev/null
if [ $? -eq 0 ];then
   APP_HOME=`dirname $0`
else
    dirname $0|grep "^\." >/dev/null
    retval=$?
    if [ $retval -eq 0 ];then
        APP_HOME=`dirname $0|sed "s#^.#$APP_HOME#"`
    else
        APP_HOME=`dirname $0|sed "s#^#$APP_HOME/#"`
    fi
fi

if [ ! -d "$APP_HOME/logs/gclog" ];then
  mkdir -p $APP_HOME/logs/gclog
fi

if [ ! -d "$APP_HOME/data/tmp" ];then
  mkdir -p $APP_HOME/data/tmp
fi

if [ -z "$LOG_PATH"  ];then
    LOG_PATH=$APP_HOME/logs/$SERVER_NAME.out
else
    LOG_PATH="$APP_HOME/$LOG_PATH"
fi
GC_LOG_PATH=$APP_HOME/logs/gclog/gc-$SERVER_NAME-$ADATE.log
#JMX监控需用到
JMX="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1091 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
#JVM参数
#-Djeesuite.configcenter.profile=$ENV
JVM_OPTS="-Dname=$SERVER_NAME -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Duser.timezone=Asia/Shanghai -Djava.io.tmpdir=$APP_HOME/data/tmp "
JVM_OPTS=" $JVM_OPTS -Xms1024M -Xmx1024M  -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -Xloggc:$GC_LOG_PATH -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
JAR_FILE=$SERVER_NAME.jar
DEBUG_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$XDEBUG_ADDRESS,suspend=n"
pid=0


start(){
  checkpid
  if [ ! -n "$pid" ]; then
  
    #JAVA_CMD=" java -server -jar $JVM_OPTS $JAR_FILE > $LOG_PATH 2>&1 &"
    # JAVA_CMD="-server -jar $JVM_OPTS $JAR_FILE --spring.config.location=$APP_HOME/$DEPLOY_CONFIG"
    JAVA_CMD="-server -jar $JVM_OPTS $JAR_FILE "
    
    if [ "$Xdebug" = "debug"  ];then
        JAVA_CMD="$DEBUG_OPTS $JAVA_CMD"
    fi
    echo "JAVA_CMD=$JAVA_CMD"
    #su - $RUNNING_USER -c "$JAVA_CMD"
    nohup java $JAVA_CMD >/dev/null 2>&1 &
    echo "---------------------------------"
    echo "启动完成，按CTRL+C退出日志界面即可>>>>>---"
    echo "---------------------------------"
    sleep 5s
    tail -f $LOG_PATH
  else
      echo "$SERVER_NAME is runing PID: $pid"   
  fi

}


status(){
   checkpid
   if [ ! -n "$pid" ]; then
     echo "status() $SERVER_NAME not runing"
   else
     echo "status() $SERVER_NAME runing PID: $pid"
     sleep 3s
     tail -f $LOG_PATH
   fi 
}

checkpid(){
    pid=`ps -ef |grep $JAR_FILE |grep -v grep |awk '{print $2}'`
}

stop(){
    checkpid
    if [ ! -n "$pid" ]; then
     echo "$SERVER_NAME not runing"
    else
      dump
      echo "$SERVER_NAME stop..."
      kill $pid
    fi 
}

restart(){
    stop 
    sleep 1s
    start
}
dump(){ 
    LOGS_DIR=$APP_HOME/logs/
    DUMP_DIR=$LOGS_DIR/dump
    if [ ! -d $DUMP_DIR ]; then
        mkdir $DUMP_DIR
    fi
    DUMP_DATE=`date +%Y%m%d%H%M%S`
    DATE_DIR=$DUMP_DIR/$DUMP_DATE
    if [ ! -d $DATE_DIR ]; then
        mkdir $DATE_DIR
    fi
    
    echo  "Dumping the $SERVER_NAME ..."
    
    PIDS=`ps -ef | grep java | grep $JAR_FILE |awk '{print $2}'`
    for PID in $PIDS ; do
        jstack $PID > $DATE_DIR/jstack-$PID.dump 2>&1
        echo -e  "PID=$PID .\c"
        jinfo $PID > $DATE_DIR/jinfo-$PID.dump 2>&1
        echo -e  ".\c"
        jstat -gcutil $PID > $DATE_DIR/jstat-gcutil-$PID.dump 2>&1
        echo -e  ".\c"
        jstat -gccapacity $PID > $DATE_DIR/jstat-gccapacity-$PID.dump 2>&1
        echo -e  ".\c"
        jmap $PID > $DATE_DIR/jmap-$PID.dump 2>&1
        echo -e  ".\c"
        jmap -heap $PID > $DATE_DIR/jmap-heap-$PID.dump 2>&1
        echo -e  ".\c"
        jmap -histo $PID > $DATE_DIR/jmap-histo-$PID.dump 2>&1
        echo -e  ".\c"
        if [ -r /usr/sbin/lsof ]; then
        /usr/sbin/lsof -p $PID > $DATE_DIR/lsof-$PID.dump
        echo -e  ".\c"
        fi
    done
    
    if [ -r /bin/netstat ]; then
    /bin/netstat -an > $DATE_DIR/netstat.dump 2>&1
    echo -e  "netstat.dump ..."
    fi
    if [ -r /usr/bin/iostat ]; then
    /usr/bin/iostat > $DATE_DIR/iostat.dump 2>&1
    echo -e  "iostat.dump ..."
    fi
    if [ -r /usr/bin/mpstat ]; then
    /usr/bin/mpstat > $DATE_DIR/mpstat.dump 2>&1
    echo -e  "mpstat.dump ..."
    fi
    if [ -r /usr/bin/vmstat ]; then
    /usr/bin/vmstat > $DATE_DIR/vmstat.dump 2>&1
    echo -e  "vmstat.dump ..."
    fi
    if [ -r /usr/bin/free ]; then
    /usr/bin/free -t > $DATE_DIR/free.dump 2>&1
    echo -e  "free.dump ..."
    fi
    if [ -r /usr/bin/sar ]; then
    /usr/bin/sar > $DATE_DIR/sar.dump 2>&1
    echo -e  ".\c"
    fi
    if [ -r /usr/bin/uptime ]; then
    /usr/bin/uptime > $DATE_DIR/uptime.dump 2>&1
    echo -e  ".\c"
    fi
    
    echo "OK!"
    echo "DUMP: $DATE_DIR"
}

case $1 in  
          start) start;;  
          stop)  stop;; 
          restart)  restart;;  
          status)  status;;   
          dump)  dump;;   
              *)  echo "require start|stop|restart|status|dump"  ;;  
esac 