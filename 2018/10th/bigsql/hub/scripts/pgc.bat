@echo off
setlocal

@REM ###################################################
@REM ######   Copyright (c) 2015-2017 OpenSCG   ########
@REM ###################################################

set PGC_HOME=%~sdp0

if "%PGC_HOME%"=="%PGC_HOME: =%" goto pgc_run
(
    echo The DOS Short Path is not enabled on this drive/directory.
    echo Your local System Administrator needs to fix before using BigSQL PGC from here.
    goto pgc_exit
)

:pgc_run

set PGC_HOME=%PGC_HOME:~0,-1%
set PGC_LOGS=%PGC_HOME%\logs\pgcli_log.out

cd /d %PGC_HOME%
set PATH=%SYSTEMROOT%\System32;%SYSTEMROOT%\System32\wbem;"%PATH%"

if NOT EXIST "%PGC_HOME%\hub_new" goto pgc_cmd
(
   rename hub_new hub_upgrade
   @REM log the hub upgrade status to pgc log
   setlocal EnableDelayedExpansion
   FOR /F %%A IN ('WMIC OS GET LocalDateTime ^| FINDSTR \.') DO @SET B=%%A
   set logdate=!B:~0,4!-!B:~4,2!-!B:~6,2!
   set logtime=%time:~-11,2%:%time:~-8,2%:%time:~-5,2%
   set logmsg=!logdate! !logtime! [INFO] : completing hub upgrade
   echo !logmsg! >> "%PGC_LOGS%"
   rename hub hub_old
   xcopy "%PGC_HOME%"\hub_upgrade\* "%PGC_HOME%"\ /E /Y /Q > nul
   rmdir hub_old /s /q
   rmdir hub_upgrade /s /q
   setlocal EnableDelayedExpansion
   @REM log the hub upgrade status as completed to pgc log
   FOR /F %%A IN ('WMIC OS GET LocalDateTime ^| FINDSTR \.') DO @SET B=%%A
   set logdate=!B:~0,4!-!B:~4,2!-!B:~6,2!
   set logtime=%time:~-11,2%:%time:~-8,2%:%time:~-5,2%
   set logmsg=!logdate! !logtime! [INFO] : hub upgrade completed.
   echo !logmsg! >> "%PGC_LOGS%"
)

:pgc_cmd

set PYTHONPATH=%PGC_HOME%\hub\scripts;%PGC_HOME%\hub\scripts\lib

if NOT EXIST "%PGC_HOME%\python2" goto pgc_run
(
   set PATH=%PGC_HOME%\python2;%PATH%
   set PYTHONHOME=%PGC_HOME%\python2
)

:pgc_run

python -u hub\scripts\pgc.py %*

:pgc_exit
