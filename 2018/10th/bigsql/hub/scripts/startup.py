from __future__ import print_function, division

##################################################################
########    Copyright (c) 2015-2017 OpenSCG        ###############
##################################################################

import os, tempfile
import util


def user_exists(p_user):
  user_data = util.getoutput ("cat /etc/passwd | egrep '^%s:' ; true" % p_user)

  if user_data:
    u = user_data.split(":")
    return dict(name=u[0],uid=u[2],gid=u[3],home=u[5],shell=u[6])
  else:
    return None

  return(0)


## Create a system user for running specific system services
def useradd_linux(p_user):
  print("Creating the user "+ p_user)
  if not util.get_platform() == "Linux":
    print("ERROR: USERADD is a Linux only command.")
    return(1)

  if not util.is_admin():
    print("ERROR: Must be ROOT to run USERADD.")
    return(1)

  ## make sure the user exists....
  util.run_sudo("useradd -m " + p_user)

  return(0)


def config_linux(p_comp, p_systemsvc, p_S, p_K, p_svc_user, p_start, p_start_log,
                   p_stop, p_reload, p_status="", is_pg=True, p_env=None):

  pgc_home = os.getenv("PGC_HOME")

  if util.is_systemd():
    sys_svc_file = os.path.join(util.get_systemd_dir(), p_systemsvc + ".service")
  else:
    sys_svc_file = os.path.join("/etc/init.d", p_systemsvc)

  print(p_comp + " config autostart " + sys_svc_file)

  if not util.is_systemd():
    svcfile = os.path.join(pgc_home, p_comp, p_systemsvc)
    write_sysv_svcfile(svcfile, p_systemsvc, p_S, p_K, p_svc_user,
                         p_start, p_start_log, p_stop, p_reload, p_status)
    rc = util.run_sudo("ln -sf " + svcfile + " " + sys_svc_file)

    link_cmd = "ln -sf ../init.d/" + p_systemsvc + "  "
    util.run_sudo(link_cmd + "/etc/rc0.d/K" + p_K + p_systemsvc)
    util.run_sudo(link_cmd + "/etc/rc1.d/K" + p_K + p_systemsvc)
    util.run_sudo(link_cmd + "/etc/rc2.d/S" + p_S + p_systemsvc)
    util.run_sudo(link_cmd + "/etc/rc3.d/S" + p_S + p_systemsvc)
    util.run_sudo(link_cmd + "/etc/rc4.d/S" + p_S + p_systemsvc)
    util.run_sudo(link_cmd + "/etc/rc5.d/S" + p_S + p_systemsvc)
    rc = util.run_sudo(link_cmd + "/etc/rc6.d/K" + p_K + p_systemsvc)
    return(rc)

  ## systemD ################################
  unit_file = tempfile.mktemp(".service")
  fh = open(unit_file, "w")
  fh.write("[Unit]\n")
  fh.write("Description=PostgreSQL (" + p_comp + ")\n")
  if is_pg:
    fh.write("After=syslog.target\n")
  fh.write("After=network.target\n")
  fh.write("\n")
  fh.write("[Service]\n")
  if p_env:
    fh.write("Environment=" + p_env + "\n")
  if is_pg:
    fh.write("Type=forking\n")
  fh.write("User=" + p_svc_user + "\n")
  if is_pg:
    fh.write("\n")
    fh.write("OOMScoreAdjust=-1000\n")
  fh.write("ExecStart="  + p_start  + "\n")
  if p_stop!="":
    fh.write("ExecStop="   + p_stop   + "\n")
  fh.write("ExecReload=" + p_reload + "\n")
  if is_pg:
    fh.write("TimeoutSec=300\n")
  fh.write("\n")
  fh.write("[Install]\n")
  fh.write("WantedBy=multi-user.target\n")
  fh.close()

  util.run_sudo("mv " + unit_file + " " + sys_svc_file)

  return(util.run_sudo("systemctl enable " + p_systemsvc))


def start_linux(p_systemsvc):
  if util.is_systemd():
    return (util.run_sudo("systemctl start " + p_systemsvc))
  else:
    return (util.run_sudo("service " + p_systemsvc + " start"))


def stop_linux(p_systemsvc):
  if util.is_systemd():
    return (util.run_sudo("systemctl stop  " + p_systemsvc))
  else:
    return (util.run_sudo("service " + p_systemsvc + " stop"))


def reload_linux(p_systemsvc):
  if util.is_systemd():
    return (util.run_sudo("systemctl reload " + p_systemsvc))
  else:
    return (util.run_sudo("service " + p_systemsvc + " reload"))


def remove_linux(p_systemsvc, p_S, p_K):
  if util.is_systemd():
    util.run_sudo("systemctl disable " + p_systemsvc)
    util.run_sudo("rm -f " + os.path.join(util.get_systemd_dir(), p_systemsvc + ".service"))
    return(0)

  ## sysV #####################################
  cmd = "rm -f "
  util.run_sudo(cmd + "/etc/rc0.d/K" + p_K + p_systemsvc)
  util.run_sudo(cmd + "/etc/rc1.d/K" + p_K + p_systemsvc)
  util.run_sudo(cmd + "/etc/rc2.d/S" + p_S + p_systemsvc)
  util.run_sudo(cmd + "/etc/rc3.d/S" + p_S + p_systemsvc)
  util.run_sudo(cmd + "/etc/rc4.d/S" + p_S + p_systemsvc)
  util.run_sudo(cmd + "/etc/rc5.d/S" + p_S + p_systemsvc)
  util.run_sudo(cmd + "/etc/rc6.d/K" + p_K + p_systemsvc)

  rc = util.run_sudo("rm -f /etc/init.d/" + p_systemsvc)
  return(rc)


def write_sysv_svcfile(svcfile, p_systemsvc, p_S, p_K, p_svc_user, 
                       p_start, p_start_log, p_stop, p_reload, p_status=""):

  fh = open(svcfile, "w")
  fh.write("#!/bin/bash\n")
  fh.write("#\n")
  fh.write("# chkconfig: 2345 " + p_S + " " + p_K + "\n")
  fh.write("# description: Control " + p_systemsvc + " server process\n")
  fh.write("#\n")
  fh.write("### BEGIN INIT INFO\n")
  fh.write("# Provides:          " + p_systemsvc + "\n")
  fh.write("# Required-Start: $remote_fs\n")
  fh.write("# Required-Stop: $remote_fs\n")
  fh.write("# Should-Start:\n")
  fh.write("# Should-Stop:\n")
  fh.write("# Default-Start:     2 3 4 5\n")
  fh.write("# Default-Stop:      0 1 6\n")
  fh.write("# Short-Description: " + p_systemsvc + "\n")
  fh.write("# Description: " + p_systemsvc + " Server\n")
  fh.write("### END INIT INFO\n")
  fh.write("\n")
  fh.write("SERVICEUSER=" + p_svc_user + "\n")
  fh.write("\n")
  fh.write("start()\n")
  fh.write("{\n")
  start_cmd = p_start + " " + p_start_log
  fh.write('  su - $SERVICEUSER  -c "' + start_cmd + '"\n')
  fh.write("}\n")
  fh.write("\n")
  fh.write("stop()\n")
  fh.write("{\n")
  fh.write('  su - $SERVICEUSER -c "' + p_stop + '"\n')
  fh.write("}\n")
  fh.write("\n")
  fh.write("reload()\n")
  fh.write("{\n")
  fh.write('  su - $SERVICEUSER -c "' + p_reload + '"\n')
  fh.write("}\n")
  fh.write("\n")
  fh.write("restart() \n")
  fh.write("{\n")
  fh.write("  stop\n")
  fh.write("  sleep 3\n")
  fh.write("  start\n")
  fh.write("}\n")
  fh.write("\n")
  fh.write("# Determine arguments passed to script\n")
  fh.write('case "$1" in\n')
  fh.write("  start)\n")
  fh.write("        start\n")
  fh.write("        ;;\n")
  fh.write("  stop)\n")
  fh.write("        stop\n")
  fh.write("        ;;\n")
  fh.write("  reload)\n")
  fh.write("        reload\n")
  fh.write("        ;;\n")
  fh.write("  restart)\n")
  fh.write("        restart\n")
  fh.write("        ;;\n")
  if p_status != "":
    fh.write("  status)\n")
    fh.write('        su - $SERVICEUSER -c "' + p_status + '"\n')
    fh.write("        ;;\n")
  fh.write("  *)\n")
  usage = "Usage: $0 {start|stop|restart|reload"
  if p_status == "":
    usage = usage + "}"
  else:
    usage = usage + "|status}"
  fh.write('        echo "' + usage + '"\n')
  fh.write("        exit 1\n")
  fh.write("esac\n")
  fh.close()
  os.system("chmod 775 " + svcfile)
  return


