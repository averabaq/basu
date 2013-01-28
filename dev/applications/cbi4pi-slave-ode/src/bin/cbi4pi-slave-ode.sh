#!/bin/sh
#
#    Licensed to the Apache Software Foundation (ASF) under one or more
#    contributor license agreements.  See the NOTICE file distributed with
#    this work for additional information regarding copyright ownership.
#    The ASF licenses this file to You under the Apache License, Version 2.0
#    (the "License"); you may not use this file except in compliance with
#    the License.  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.

cygwin=false;
darwin=false;
case "`uname`" in
    Darwin*) 
	darwin=true
	if [ -z "$JAVA_HOME" ] ; then
	    JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Home
	fi
	;;
    CYGWIN*) 
	cygwin=true 
	;;
esac

## Try to find our home directory 
command="$0"
progname=`basename "$0"`
# need this for relative symlinks
while [ -h "$command" ] ; do
    ls=`ls -ld "$command"`
    link=`expr "$command" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
	command="$link"
    else
	command=`dirname "$command"`"/$link"
    fi
done

CBI4PI_ODE_BIN=`dirname "$command"`
CBI4PI_ODE_BIN=`cd "$CBI4PI_ODE_BIN" && pwd`

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
    [ -n "$CBI4PI_ODE_BIN" ] && CBI4PI_ODE_BIN=`cygpath --unix "$CBI4PI_ODE_BIN"`
    [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi

CBI4PI_ODE_HOME=`cd "$CBI4PI_ODE_BIN/.." && pwd`
LIB="$CBI4PI_ODE_HOME/lib"
ETC="$CBI4PI_ODE_HOME/etc"

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java 2> /dev/null `
    if [ -z "$JAVACMD" ] ; then
        JAVACMD=java
    fi
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ ! -d "$JAVA_HOME" ] ; then
 echo "Error: JAVA_HOME is not defined correctly (no such directory)."
 exit 1
fi

# Add user-specified classpath. 
LOCALCLASSPATH="$CBI4PI_ODE_CLASSPATH"

# Add CBI4PI_ODE libraries
for f in $LIB/*.jar
do
  LOCALCLASSPATH=$LOCALCLASSPATH:$f
done

# Add CBI4PI_ODE bin libraries
for f in $CBI4PI_ODE_BIN/*.jar
do
  LOCALCLASSPATH=$LOCALCLASSPATH:$f
done

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    CBI4PI_ODE_HOME=`cygpath --windows "$CBI4PI_ODE_HOME"`
    JAVA_HOME=`cygpath --windows "$JAVA_HOME"`
    LOCALCLASSPATH=`cygpath --path --windows "$LOCALCLASSPATH"`
    CYGHOME=`cygpath --windows "$HOME"`
    LIB=`cygpath --windows "$LIB"`
    CBI4PI_ODE_BIN=`cygpath --windows "$CBI4PI_ODE_BIN"`
    ETC=`cygpath --windows "$ETC"`
fi

exec "$JAVACMD" $CBI4PI_ODE_JAVAOPTS -cp "$LOCALCLASSPATH" es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.EventPublisherService "$@"
#exec "$JAVACMD" $CBI4PI_ODE_JAVAOPTS -cp "$LOCALCLASSPATH" es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.EventPublisherService "$@" &
echo $! > .cbi4pi.pid