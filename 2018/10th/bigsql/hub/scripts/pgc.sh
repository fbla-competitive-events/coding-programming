#!/bin/bash

##################################################
####     Copyright (c) 2015-2017 OpenSCG      ####
##################################################

start_dir="$PWD"

# resolve links - $0 may be a softlink
this="${BASH_SOURCE-$0}"
common_bin=$(cd -P -- "$(dirname -- "$this")" && pwd -P)
script="$(basename -- "$this")"
this="$common_bin/$script"
# convert relative path to absolute path
config_bin=`dirname "$this"`
script=`basename "$this"`
pgc_home=`cd "$config_bin"; pwd`

export PGC_HOME="$pgc_home"
export PGC_LOGS="$pgc_home/logs/pgcli_log.out"

cd "$PGC_HOME"

hub_new="$PGC_HOME/hub_new"
if [ -d "$hub_new" ];then
  `mv $PGC_HOME/hub_new $PGC_HOME/hub_upgrade`
  log_time=`date +"%Y-%m-%d %H:%M:%S"`
  echo "$log_time [INFO] : completing hub upgrade" >> $PGC_LOGS
  `mv $PGC_HOME/hub $PGC_HOME/hub_old`
  `cp -rf $PGC_HOME/hub_upgrade/* $PGC_HOME/`
  `rm -rf $PGC_HOME/hub_upgrade`
  `rm -rf $PGC_HOME/hub_old`
  log_time=`date +"%Y-%m-%d %H:%M:%S"`
  echo "$log_time [INFO] : hub upgrade completed" >> $PGC_LOGS
fi

declare -a array
array[0]="$PGC_HOME/hub/scripts"
array[1]="$PGC_HOME/hub/scripts/lib"

export PYTHONPATH=$(printf "%s:${PYTHONPATH}" ${array[@]})
export PYTHON=python

pydir="$PGC_HOME/python2"
if [ -d "$pydir" ]; then
  export PYTHON="$pydir/bin/python"		
  export PATH="$pydir/bin:$PATH"
  if [ `uname` == "Darwin" ]; then
    export DYLD_LIBRARY_PATH="$pydir/lib/python2.7:$DYLD_LIBRARY_PATH"
  else
    export LD_LIBRARY_PATH="$pydir/lib/python2.7:$LD_LIBRARY_PATH"
  fi
else
 pyver=`$PYTHON --version  > /dev/null 2>&1`
 rc=$?
 if [ ! $rc == 0 ];then
   export PYTHON=python3
 fi
fi

$PYTHON -u "$PGC_HOME/hub/scripts/pgc.py" "$@"
