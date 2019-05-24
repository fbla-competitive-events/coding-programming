@echo off

@REM ###################################################
@REM ######   Copyright (c) 2016-2017 BigSQL    ########
@REM ###################################################

set PG_SERVICE_NAME=%SVC_NAME%
set PG_SERVICE_DESCRIPTION=%SVC_DESC%
set PG_SERVICE_DISPLAY_NAME=%SVC_NAME%

set CWD=%~sdp0

REM environment variables used by proc run
set PR_STARTUP=auto
set PR_STARTMODE=EXE
set PR_PG_PID_FILE="postgresql.pid"

set PR_LOG_PATH="%CWD%..\data\logs\%PG_VER%"

set PG_STARTIMAGE="%CWD%..\python2\python.exe"
set PG_STARTPARAM="%CWD%run-pgctl.py"
set PG_STOPPARAM="%CWD%stop-%PG_VER%.py"

set PROCRUN="%CWD%pgservice.exe"

%PROCRUN% //IS//%PG_SERVICE_NAME% --StartImage=%PG_STARTIMAGE% --StopImage=%PG_STARTIMAGE% --Description=%PG_SERVICE_DESCRIPTION% --DisplayName=%PG_SERVICE_DISPLAY_NAME% --StdOutput=auto --StdError=auto ++StartParams=%PG_STARTPARAM% --StopMode=%PR_STARTMODE% ++StopParams=%PG_STOPPARAM% ++Environment="PYTHONHOME=%CWD%..\python2" ++Environment="PGC_HOME=%CWD%.." ++Environment="PATH='%PATH%;%CWD%bin;%CWD%..\python2'"
