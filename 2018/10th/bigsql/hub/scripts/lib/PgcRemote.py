####################################################################
#########      Copyright (c) 2016-2017 BigSQL           ############
####################################################################

import paramiko
from fabric.state import connections, ssh
from fabric.api import env, hide, cd, put, run, settings, sudo
from fabric.contrib import files
from StringIO import StringIO
from fabric.context_managers import shell_env
import os
import sys
from fabric.context_managers import remote_tunnel


class PgcRemoteException(Exception):
    pass


class StreamFilter(object):
    def __init__(self, filter, stream):
        self.stream = stream
        self.filter = filter

    def write(self, data):
        data = data.replace(self.filter, '')
        self.stream.write(data)
        self.stream.flush()

    def flush(self):
        self.stream.flush()


class PgcRemote(object):
    def __init__(self, host, username="", password="", ssh_key="", sudo_pwd=None, ssh_key_path=None):
        self.user = username
        self.host = host
        self.password = password
        self.sftp = None
        self.client = None
        self.ssh_key=None
        self.temp_file=None
        self.sudo_pwd = sudo_pwd
        env.eagerly_disconnect = False
        env.command_timeout=None


        if ssh_key:
            import tempfile
            file_descriptor, file_path = tempfile.mkstemp(suffix='.pem')
            self.temp_file = file_path
            # You can convert the low level file_descriptor to a normal open file using fdopen
            with os.fdopen(file_descriptor, 'w') as open_file:
                open_file.write(ssh_key)
            env.key_filename = self.temp_file
            self.ssh_key=self.temp_file
        if ssh_key_path:
            self.ssh_key = ssh_key_path
        env.host_string = "%s@%s" % (username, host)  # , 22)
        if password:
            env.password = password
        env.remote_interrupt = True
        env.output_prefix = False
        env.connection_attempts = 1
        env.warn_only = True

    def connect(self):
        sshClient = ssh.SSHClient()
        sshClient.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        if self.ssh_key:
            sshClient.connect(self.host,
                              username=self.user,
                              key_filename=self.ssh_key,
                              timeout=10)
        else:
            sshClient.connect(self.host,
                              username=self.user,
                              password=self.password,
                              timeout=10)
        if env.host_string not in connections:
            connections[env.host_string] = sshClient
        self.client = sshClient

    def has_sudo(self):
        with settings(hide('everything'), warn_only=True):
            cmd="uname"
            m = sudo(cmd, shell_escape=True, pty=False)
            has_root_access=False
            if m.return_code == 0:
                has_root_access=True
            return has_root_access


    def get_home_dir(self):
        with settings(hide('everything'), warn_only=True):
            cmd="pwd"
            m = run(cmd, shell_escape=True, pty=False)
            if m.return_code == 0:
                return m.stdout.strip()
            return ""

    def get_exixting_pgc_path(self):
        env.command_timeout=20
        import json
        result={}
        opt_path = "/opt/postgresql"
        need_sudo = False
        bigsql_path = self.get_home_dir() + "/bigsql"
        osx_installer_path = self.get_home_dir() + "/PostgreSQL"
        pgc_info_cmd = "/pgc info --json"
        result['pgc_path'] = bigsql_path
        result['pgc_path_exists'] = False
        result['need_sudo'] = need_sudo
        pgc_not_exists=True
        if self.is_exists(opt_path):
            if self.is_exists(opt_path + "/pgc"):
                sudo_exec = False
                try:
                    if self.sudo_pwd:
                        sudo_exec = True
                except Exception as e:
                    pass
                pgc_info_out = self.execute(opt_path + pgc_info_cmd, is_sudo=sudo_exec)
                try:
                    if pgc_info_out.get("stderr").strip().find("Command failed to finish ")>=0:
                        result['not_sudoer'] = True
                        return result
                    if pgc_info_out.get("stderr").strip() == "1":
                        result['auth_err'] = True
                        return result
                    if pgc_info_out.get("stderr").strip().find("not in the sudoers file") >= 0:
                        result['not_sudoer'] = True
                        return result
                    if pgc_info_out['stdout']:
                        info_out = pgc_info_out['stdout']
                        if info_out.strip().find("You must run as administrator/root.") >= 0:
                            result['root_pgc_path'] = opt_path
                            pgc_info_out = self.execute(opt_path + pgc_info_cmd, is_sudo=True)
                            if pgc_info_out.get("stderr")=="1":
                                result['auth_err'] = True
                            if pgc_info_out.get("stderr").strip().find("not in the sudoers file") >= 0:
                                result['not_sudoer'] = need_sudo
                                return result
                            if pgc_info_out['stdout']:
                                info_out = pgc_info_out['stdout']
                                need_sudo = True
                        if not pgc_info_out.get("stderr") and info_out.strip().find("You must run as administrator/root.") < 0:
                            pgc_info = json.loads(info_out)
                            result['pgc_version'] = pgc_info[0]['version']
                            result['pgc_path_exists'] = True
                            result['pgc_path'] = opt_path
                            result['need_sudo'] = need_sudo
                            result.pop('root_pgc_path')
                            return result
                except Exception as e:
                    return result
                    pass

        if self.is_exists(osx_installer_path):
            if self.is_exists(osx_installer_path + "/pgc"):
                pgc_info_out = self.execute(osx_installer_path + pgc_info_cmd)
                if pgc_info_out['stdout']:
                    pgc_info = json.loads(pgc_info_out['stdout'])
                    result['pgc_version'] = pgc_info[0]['version']
                    result['pgc_path_exists']=True
                    result['pgc_path'] = osx_installer_path
                    return result

        if self.is_exists(bigsql_path):
            if self.is_exists(bigsql_path + "/pgc"):
                pgc_info_out = self.execute(bigsql_path + pgc_info_cmd)
                if pgc_info_out['stdout']:
                    pgc_info = json.loads(pgc_info_out['stdout'])
                    result['pgc_version'] = pgc_info[0]['version']
                    result['pgc_path_exists']=True
                    result['pgc_path'] = bigsql_path
                    return result

        return result


    def is_exists(self, path):
        return files.exists(path)

    def add_file(self, path, txt):
        with settings(hide('everything'), warn_only=True):
            files.append(path, txt)

    def has_execution_access(self, path):
        fn = run if not self.has_sudo else sudo
        with settings(hide('everything'), warn_only=True):
            response = fn('test -x ' + path)
            return response.return_code == 0

    def user_exists(self, user):
        """
        Determine if a user exists with given user.

        This returns the information as a dictionary
        '{"name":<str>,"uid":<str>,"gid":<str>,"home":<str>,"shell":<str>}' or 'None'
        if the user does not exist.
        """
        with settings(hide('warnings','stderr','stdout','running'),warn_only=True):
            user_data = run("cat /etc/passwd | egrep '^%s:' ; true" % user)

        if user_data:
            u = user_data.split(":")
            return dict(name=u[0],uid=u[2],gid=u[3],home=u[5],shell=u[6])
        else:
            return None

    def user_create(self, user, home=None, uid=None, gid=None, password=False):
        """
        Creates the user with the given user, optionally giving a specific home/uid/gid.

        By default users will be created without a password.  To create users with a
        password you must set "password" to True.
        """
        u = self.user_exists(user)
        if not u:
            options = []
            if home: options.append("-d '%s'" % home)
            if uid:  options.append("-u '%s'" % uid)
            if gid:  options.append("-g '%s'" % gid)
            if not password: options.append("--disabled-password")
            sudo("adduser %s '%s'" % (" ".join(options), user))
        else:
            return None

    def group_exists(self, name):
        """
        Determine if a group exists with a given name.

        This returns the information as a dictionary
        '{"name":<str>,"gid":<str>,"members":<list[str]>}' or 'None'
        if the group does not exist.
        """
        with settings(hide('warnings','stderr','stdout','running'),warn_only=True):
            group_data = run("cat /etc/group | egrep '^%s:' ; true" % (name))

        if group_data:
            name,_,gid,members = group_data.split(":",4)
            return dict(name=name,gid=gid,members=tuple(m.strip() for m in members.split(",")))
        else:
            return None
        return None

    def group_create(self, name, gid=None):
        """ Creates a group with the given name, and optionally given gid. """
        options = []
        if gid: options.append("-g '%s'" % gid)
        sudo("addgroup %s '%s'" % (" ".join(options), name))

    def group_user_exists(self, group, user):
        """ Determine if the given user is a member of the given group. """
        g = self.group_exists(group)

        u = self.user_exists(user)

        return user in g["members"]

    def group_user_add(self, group, user):
        """ Adds the given user to the given group. """
        if not self.group_user_exists(group, user):
            sudo('adduser %s %s' % (user, group))

    def grant_sudo_access(self, user, service_name):
        """ Grants sudo access to a user. """
        u = self.user_exists(user)
        if u:
          service_cmd = self.get_systemctl_path()
          if service_cmd is None:
            # Not a systemd platform
            service_cmd = "/sbin/service "
            sudo_cmds = [user + " ALL=(ALL) NOPASSWD:" + service_cmd + service_name + " start",
                         user + " ALL=(ALL) NOPASSWD:" + service_cmd + service_name + " stop",
                         user + " ALL=(ALL) NOPASSWD:" + service_cmd + service_name + " restart",
                         user + " ALL=(ALL) NOPASSWD:" + service_cmd + service_name + " status"]
          else:
            sudo_cmds = [user + " ALL=(ALL) NOPASSWD:" + service_cmd + " start "   + service_name,
                         user + " ALL=(ALL) NOPASSWD:" + service_cmd + " stop "    + service_name,
                         user + " ALL=(ALL) NOPASSWD:" + service_cmd + " restart " + service_name,
                         user + " ALL=(ALL) NOPASSWD:" + service_cmd + " status "  + service_name]

          for cmd in sudo_cmds:
            files.append("/etc/sudoers.d/" + user, cmd, use_sudo=True)

    def get_systemctl_path(self):
        systemctls = ["/usr/bin/systemctl", "/bin/systemctl"]
        for pth in systemctls:
            if self.is_exists(pth) and self.has_execution_access(pth):
                return pth
        return None

    def upload_pgc(self, source_path, target_path, pgc_version, repo, repo_port=8000):
        with settings(hide('everything'), warn_only=True):
            if not self.is_exists(target_path):
                run("mkdir -p " + target_path)

        with cd(target_path), shell_env(PGC_VER=pgc_version, PGC_REPO=repo), hide("everything"):
            pgc_file = "bigsql-pgc-" + pgc_version + ".tar.bz2"
            source_file = os.path.join(source_path, pgc_file)
            put(source_file, ".")
            source_file = os.path.join(source_path, "install.py")
            put(source_file, ".")
            cmd = "python install.py"
            target_pgc_file = target_path + "/" + pgc_file
            target_install_file = target_path + "/install.py"
            try:
                m = run(cmd, shell_escape=True, pty=False)
                if m.return_code == 0:
                    out = m.stdout.strip()
                    err = m.stderr.strip()
                else:
                    out = ""
                    err = m.stdout.strip()
                if self.is_exists(target_pgc_file):
                    run("rm "+ target_pgc_file)
                if self.is_exists(target_install_file):
                    run("rm "+ target_install_file)
                return {"stdout": out, "stderr": err}
            except Exception as e:
                if self.is_exists(target_pgc_file):
                    run("rm "+ target_pgc_file)
                if self.is_exists(target_install_file):
                    run("rm "+ target_install_file)
                return {"stdout": "", "stderr": str(e)}

    def execute(self, cmd, is_sudo=False, isTTY=False):
        with settings(hide('everything'), warn_only=True, sudo_password=self.sudo_pwd):
            executable_func = run
            if is_sudo:
                executable_func = sudo
                sys.stdout = StreamFilter("sudo password:", sys.stdout)
                sys.stdout = StreamFilter("sudo :", sys.stdout)
                if env.password:
                    sys.stdout = StreamFilter(env.password, sys.stdout)
                if self.sudo_pwd:
                    sys.stdout = StreamFilter(self.sudo_pwd, sys.stdout)
                env.abort_on_prompts = True
            try:
                m = executable_func(cmd, shell_escape=True, pty=isTTY)
                rc = m.return_code
                if rc == 0:
                    out = m.stdout.strip()
                    err = m.stderr.strip()
                else:
                    out = ""
                    err = m.stdout.strip()
                return {"stdout": out, "stderr": err, "rc": rc}
            except SystemExit as e:
                return {"stdout": "", "stderr": str(e)}
            except Exception as e:
                return {"stdout": "", "stderr": str(e)}

    def execute_stream(self, cmd, is_sudo=False, isTTY=False):
        with settings(hide('warnings', 'running', 'stderr'), warn_only=True, sudo_password=self.sudo_pwd):
            executable_func = run
            if is_sudo:
                executable_func = sudo
                sys.stdout = StreamFilter("sudo password:", sys.stdout)
                sys.stdout = StreamFilter("sudo :", sys.stdout)
                if env.password:
                    sys.stdout = StreamFilter(env.password, sys.stdout)
                if self.sudo_pwd:
                    sys.stdout = StreamFilter(self.sudo_pwd, sys.stdout)
                env.abort_on_prompts = True

            try:
                m = executable_func(cmd, shell_escape=True, pty=isTTY)
                rc = m.return_code
                if rc == 0:
                    out = m.stdout.strip()
                    err = m.stderr.strip()
                else:
                    out = ""
                    err = m.stdout.strip()
                return {"stdout": out, "stderr": err, "rc": rc}
            except SystemExit as e:
                return {"stdout": "", "stderr": str(e)}
            except Exception as e:
                return {"stdout": "", "stderr": str(e)}

    def disconnect(self):
        try:
            if self.temp_file:
                os.unlink(self.temp_file)
        except Exception as e:
            pass

        try:
            host = self.host
            if host and host in connections:
                connections[host].get_transport().close()
            if hasattr(self.client, 'get_transport'):
                self.client.get_transport().close()
        except Exception as e:
            pass
