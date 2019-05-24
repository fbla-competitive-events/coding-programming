DROP TABLE IF EXISTS versions;
DROP TABLE IF EXISTS releases;
DROP TABLE IF EXISTS installgroups;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS stages;
DROP TABLE IF EXISTS labs;
DROP TABLE IF EXISTS clouds;


CREATE TABLE clouds (
  cloud              TEXT    NOT NULL PRIMARY KEY,
  provider_constant  TEXT    NOT NULL,
  display_order      INTEGER NOT NULL,
  is_supported       INTEGER NOT NULL,
  required           TEXT    NOT NULL
);
INSERT INTO clouds VALUES ('AWS',       'EC2',       1, 1, 'key, secret, region');
INSERT INTO clouds VALUES ('Azure',     'AZURE_ARM', 2, 1, 'key, secret');
INSERT INTO clouds VALUES ('VMware',    'VCLOUD',    4, 0, 'key, secret');


CREATE TABLE labs (
  lab                TEXT    NOT NULL PRIMARY KEY,
  disp_name          TEXT    NOT NULL,
  credit_to          TEXT    NOT NULL,
  short_desc         TEXT    NOT NULL,
  auth_type          TEXT,   /* (null || 'login') */
  enabled_image      TEXT,
  disabled_image     TEXT
);
INSERT INTO labs VALUES ('multi-host-mgr', 'Multi Server Manager', 'by Mahesh & Denis',
  'Manage multiple PG Connections & SSH hosts from the pgDevOps Server Manager.  Presently works best using a <a href="https://bigsql.org/docs/sudo/" target="_blank">password-less sudo account</a> for remote SSH sessions to work smoothly.',
  null, 'devops-host-mgr-enabled.png', 'devops-host-mgr-disabled.png');
INSERT INTO labs VALUES ('pgdg-repos', 'PGDG Linux Repository', 'by Kiran & Denis', 
  'Support for using the PostgreSQL Global Development Group (PGDG) Linux Repositories.  Presently works best using a <a href="https://bigsql.org/docs/sudo/" target="_blank">password-less sudo account</a>.  Tested with the PGDG Yum repositories on CentOS 6 & 7.  Also tested on the PGDG APT repositories for Ubuntu LTS 12.04, 14.04 & 16.04.',
  null, 'devops-pgdg-enabled.png', 'devops-pgdg-disabled.png');
INSERT INTO labs VALUES ('aws', 'AWS Integration', 'by Scott & Mahesh', 
  'Integrate with <a href="https://bigsql.org/docs/aws/" target="_blank">Amazon Web Services</a> featuring EC2 & PostgresRDS.',
  null, 'devops-aws-enabled.png', 'devops-aws-disabled.png');
INSERT INTO labs VALUES ('azure', 'Azure Integration', 'by George & Denis', 
  'Integrate with <a href="https://bigsql.org/docs/azure/" target="_blank">Microsoft Azure</a> featuring Linux Virtual Machines & AzurePostgres.',
  null, 'devops-azure-enabled.png', 'devops-azure-disabled.png');
INSERT INTO labs VALUES ('vmware', 'VMware Integration', 'by Naveen & Kiran', 
  'Integrate with VMware virtual machines via vCenter API',
  null, 'devops-vmware-enabled.png', 'devops-vmware-disabled.png');
INSERT INTO labs VALUES ('dumprest', 'Postgres Backup & Restore', 'by Naveen & Ismail', 
  'The solid beginnings of an enterprise solution for backup-n-restore to a remote SSH server.',
  null, 'devops-dumprest-enabled.png', 'devops-dumprest-disabled.png');


CREATE TABLE stages (
  stage       TEXT    NOT NULL PRIMARY KEY
);
INSERT INTO stages VALUES ('dev');
INSERT INTO stages VALUES ('test');
INSERT INTO stages VALUES ('prod');


CREATE TABLE categories (
  category    INTEGER NOT NULL PRIMARY KEY,
  description TEXT    NOT NULL
);
INSERT INTO categories VALUES (0, 'Hidden');
INSERT INTO categories VALUES (1, 'PostgreSQL');
INSERT INTO categories VALUES (2, 'Extensions');
INSERT INTO categories VALUES (3, 'Servers');
INSERT INTO categories VALUES (4, 'Applications');
INSERT INTO categories VALUES (5, 'Connectors');
INSERT INTO categories VALUES (6, 'Frameworks');


CREATE TABLE projects (
  project   	 TEXT    NOT NULL PRIMARY KEY,
  category  	 INTEGER NOT NULL,
  port      	 INTEGER NOT NULL,
  depends   	 TEXT    NOT NULL,
  start_order    INTEGER NOT NULL,
  homepage_url   TEXT    NOT NULL,
  short_desc     TEXT    NOT NULL,
  FOREIGN KEY (category) REFERENCES categories(category)
);
INSERT INTO projects VALUES ('hub', 0, 0, 'hub', 0, 'http://bigsql.org', 'Pretty Good CLI');

INSERT INTO projects VALUES ('pg', 1, 5432, 'hub', 1, 'http://postgresql.org', 'Advanced RDBMS');

INSERT INTO projects VALUES ('plprofiler', 2, 0, 'perl', 0, 'https://bitbucket.org/openscg/plprofiler', 'PLpg/SQL Profiler');
INSERT INTO projects VALUES ('postgis', 2, 0, 'hub', 0, 'http://postgis.net', 'Spatial Database Extender for PG');
INSERT INTO projects VALUES ('slony', 2, 0, 'hub', 0, 'http://slony.info', 'Enterprise Level Replication System');
INSERT INTO projects VALUES ('cassandra_fdw', 2, 0, 'hub', 0, 'http://cassandrafdw.org', 'Cassandra Foreign Data Wrapper');
INSERT INTO projects VALUES ('hadoop_fdw', 2, 0, 'hub', 0, 'http://hadoopfdw.org', 'Hadoop Foreign Data Wrapper');
INSERT INTO projects VALUES ('oracle_fdw', 2, 0, 'hub', 0, 'https://github.com/laurenz/oracle_fdw', 'Oracle Foreign Data Wrapper');
INSERT INTO projects VALUES ('mysql_fdw', 2, 0, 'hub', 0, 'https://github.com/EnterpriseDB/mysql_fdw', 'MySQL Foreign Data Wrapper');
INSERT INTO projects VALUES ('tds_fdw', 2, 0, 'hub', 0, 'https://github.com/tds-fdw/tds_fdw', 'TDS (Sybase and MS SQL) Foreign Data Wrapper');
INSERT INTO projects VALUES ('orafce', 2, 0, 'hub', 0, 'https://github.com/orafce/orafce', 'Oracle Compatible Functions');
INSERT INTO projects VALUES ('plv8', 2, 0, 'hub', 0, 'https://github.com/plv8/plv8', 'Javascript Procedural Language');
INSERT INTO projects VALUES ('pljava', 2, 0, 'hub', 0, 'https://github.com/tada/pljava', 'Java Procedural Language');
INSERT INTO projects VALUES ('pgtsql', 2, 0, 'hub', 0, 'https://bitbucket.org/openscg/pgtsql', 'Sybase and MS SQL Compatibiliy');
INSERT INTO projects VALUES ('hintplan', 2, 0, 'hub', 0, 'https://osdn.net/projects/pghintplan', 'Guiding the planner with hints');
INSERT INTO projects VALUES ('bulkload', 2, 0, 'hub', 0, 'http://ossc-db.github.io/pg_bulkload/pg_bulkload.html', 'High Speed Data Loader');
INSERT INTO projects VALUES ('repack', 2, 0, 'hub', 0, 'http://github.com/reorg/pg_repack', 'Online Table Compaction');
INSERT INTO projects VALUES ('logical', 2, 0, 'hub', 0, 'https://github.com/2ndQuadrant/pglogical', 'Logical Replication');
INSERT INTO projects VALUES ('partman', 2, 0, 'hub', 0, 'https://github.com/keithf4/pg_partman', 'Table Partition Manager');
INSERT INTO projects VALUES ('pgaudit', 2, 0, 'hub', 0, 'http://pgaudit.org',                  'Auditing Extension');
INSERT INTO projects VALUES ('setuser', 2, 0, 'hub', 0, 'https://github.com/pgaudit/set_user', 'Secure Privilege Escalation');
INSERT INTO projects VALUES ('pldebugger', 2, 0, 'hub', 0, 'https://bigsql.org/docs/debugger/', 'Procedural Language Debugger');
INSERT INTO projects VALUES ('cstore_fdw', 2, 0, 'hub', 0, 'http://github.com/citusdata/cstore_fdw', 'Optimized Row Columnar (ORC) Store');
INSERT INTO projects VALUES ('background', 2, 0, 'hub', 0, 'https://github.com/vibhorkum/pg_background', 'Autonomous SQL Txns');

INSERT INTO projects VALUES ('bam', 3, 8050, 'hub', 0, '', 'BigSQL Manager');
INSERT INTO projects VALUES ('pgdevops', 3, 8051, 'hub', 0, '', 'pgDevOps Web Console');
INSERT INTO projects VALUES ('webtty', 3, 3000, 'hub', 0, '', 'Terminal over HTTPS from Browser');
INSERT INTO projects VALUES ('pgbouncer', 3, 6543, 'hub', 2, 'http://pgbouncer.github.io', 'Connection Pooler');
INSERT INTO projects VALUES ('influxdb', 3, 8086, 'hub', 1, 'https://github.com/influxdata/influxdb', 'Time Series DB');
INSERT INTO projects VALUES ('kapacitor', 3, 9092, 'hub', 1, 'https://github.com/influxdata/kapacitor', 'Alerting Mechanism');
INSERT INTO projects VALUES ('chronograf', 3, 8888, 'hub', 1, 'https://github.com/influxdata/chronograf', 'Alerting Web UI');
INSERT INTO projects VALUES ('telegraf', 3, 1, 'hub', 1, 'https://github.com/influxdata/telegraf', 'Metrics Collection');
INSERT INTO projects VALUES ('collectd', 3, 1, 'hub', 1, 'http://collectd.org', 'Metrics Collection');
INSERT INTO projects VALUES ('grafana', 3, 3000, 'hub', 1, 'https://github.com/grafana/grafana', 'Monitoring & Metrics Dashboard');
INSERT INTO projects VALUES ('elasticsearch', 3, 9200, 'hub', 1, 'https://github.com/elastic/elasticsearch', 'REST Search Engine');
INSERT INTO projects VALUES ('logstash', 3, 5044, 'hub', 1, 'https://github.com/elastic/logstash', 'Transport and process logs');
INSERT INTO projects VALUES ('filebeat', 3, 1, 'hub', 1, 'https://github.com/elastic/beats', 'Lightweight log shipper for Elasticsearch');
INSERT INTO projects VALUES ('kibana', 3, 1, 'hub', 1, 'https://github.com/elastic/kibana', 'Analytics and search dashboard for Elasticsearch ');
INSERT INTO projects VALUES ('rundeck', 3, 4440, 'java', 1, 'http://rundeck.org', 'Job Scheduler');
INSERT INTO projects VALUES ('consul', 3, 8500, 'hub', 1, 'http://consul.io', 'Service Discovery');
INSERT INTO projects VALUES ('pgstudio', 3, 8765, 'java', 3, 'http://postgresqlstudio.org', 'Web-based Development');
INSERT INTO projects VALUES ('gpdb', 3, 5432, 'hub', 5, 'http://mygpdb.io', 'PostgreSQL Data Lake');
INSERT INTO projects VALUES ('hadoop', 3, 50010, 'java', 7, 'http://hadoop.apache.org', 'Distributed Framework');
INSERT INTO projects VALUES ('zookeeper', 3, 2181, 'java', 6, 'http://zookeeper.apache.org', 'Cluster Coordinator');
INSERT INTO projects VALUES ('cassandra', 3, 7000, 'java', 4, 'http://cassandra.apache.org', 'Multi-master/Multi-datacenter');
INSERT INTO projects VALUES ('spark', 3, 7077, 'java', 5, 'http://spark.apache.org', 'Speedy Distributed Data Access');
INSERT INTO projects VALUES ('hive', 3, 10000, 'java', 8, 'http://hive.apache.org', 'Hadoop SQL Queries');
INSERT INTO projects VALUES ('pgha', 3, 1, 'hub', 3, 'http://bigsql.org/pgha', 'High Availability');
INSERT INTO projects VALUES ('tomcat', 3, 8080, 'java', 3, 'http://tomcat.apache.org', 'Java Servlets & JSPs');

INSERT INTO projects VALUES ('pgagent', 4, 1, 'hub', 0, 'https://www.pgadmin.org/docs/1.22/pgagent.html', 'Background Scheduler');
INSERT INTO projects VALUES ('pgadmin', 4, 1, 'hub', 0, 'http://pgadmin.org', 'Desktop Development Environment');
INSERT INTO projects VALUES ('pgbadger', 4, 0, 'perl', 0, 'https://github.com/dalibo/pgbadger', 'Fast Log Analyzer');
INSERT INTO projects VALUES ('backrest', 4, 0, 'perl', 0, 'http://www.pgbackrest.org', 'Reliable Backup & Restore');
INSERT INTO projects VALUES ('ora2pg', 4, 0, 'perl', 0, 'http://ora2pg.darold.net/', 'Oracle Migration Toolkit');
INSERT INTO projects VALUES ('vagrant', 4, 0, 'hub', 0, 'http://vagrantup.com/', 'Build & Manage Virtual Machines');
INSERT INTO projects VALUES ('virtualbox', 4, 1, 'hub', 0, 'http://virtualbox.org', 'Virtual Machine Host');

INSERT INTO projects VALUES ('python', 6, 0, 'hub', 0, 'http://python.org', 'Python Programming Language');
INSERT INTO projects VALUES ('perl', 6, 0, 'hub', 0, 'http://perl.org', 'Perl Programming Language');
INSERT INTO projects VALUES ('tcl', 6, 0, 'hub', 0, 'http://tcl.tk', 'Tcl Programming Language');
INSERT INTO projects VALUES ('java', 6, 0, 'hub', 0, 'http://openjdk.java.net', 'Java Runtime Environment');


CREATE TABLE installgroups (
  project_group  TEXT    NOT NULL,
  depend_order   INTEGER NOT NULL,
  project        TEXT    NOT NULL,
  FOREIGN KEY (project) REFERENCES projects(project),
  PRIMARY KEY (project_group, depend_order, project)
);

INSERT INTO installgroups VALUES ('Enhanced Monitoring', 1, 'pgdevops');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 2, 'influxdb');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 3, 'grafana');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 4, 'collectd');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 5, 'kapacitor');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 6, 'telegraf');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 7, 'chronograf');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 8, 'elasticsearch');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 9, 'logstash');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 10, 'filebeat');
INSERT INTO installgroups VALUES ('Enhanced Monitoring', 11, 'kibana');


CREATE TABLE releases (
  component  TEXT    NOT NULL PRIMARY KEY,
  project    TEXT    NOT NULL,
  disp_name  TEXT    NOT NULL,
  short_desc TEXT    NOT NULL,
  sup_plat   TEXT    NOT NULL,
  doc_url    TEXT    NOT NULL,
  stage      TEXT    NOT NULL,
  FOREIGN KEY (project) REFERENCES projects(project),
  FOREIGN KEY (stage)   REFERENCES stages(stage)
);
INSERT INTO releases VALUES ('hub',        'hub',      'Hidden', 'Hidden', '',        '', 'prod');
INSERT INTO releases VALUES ('pg92',       'pg',       'PostgreSQL 9.2', 'PG Server (bigsql)', '', 'http://www.postgresql.org/docs/9.2/', 'prod');
INSERT INTO releases VALUES ('pg93',       'pg',       'PostgreSQL 9.3', 'PG Server (bigsql)', '', 'http://www.postgresql.org/docs/9.3/', 'prod');
INSERT INTO releases VALUES ('pg94',       'pg',       'PostgreSQL 9.4', 'PG Server (bigsql)', '', 'http://www.postgresql.org/docs/9.4/', 'prod');
INSERT INTO releases VALUES ('pg95',       'pg',       'PostgreSQL 9.5', 'PG Server (bigsql)', '', 'http://www.postgresql.org/docs/9.5/', 'prod');
INSERT INTO releases VALUES ('pg96',       'pg',       'PostgreSQL 9.6', 'PG Server (bigsql)', '', 'http://www.postgresql.org/docs/9.6/', 'prod');
INSERT INTO releases VALUES ('pg10',       'pg',       'Postgres 10',    'PG Server (bigsql)', '', 'http://www.postgresql.org/docs/10/', 'prod');

INSERT INTO releases VALUES ('pgdg92',     'pg',       'PostgreSQL 9.2', 'PG Server (pgdg)',   '', 'http://www.postgresql.org/docs/9.2/', 'prod');
INSERT INTO releases VALUES ('pgdg93',     'pg',       'PostgreSQL 9.3', 'PG Server (pgdg)',   '', 'http://www.postgresql.org/docs/9.3/', 'prod');
INSERT INTO releases VALUES ('pgdg94',     'pg',       'PostgreSQL 9.4', 'PG Server (pgdg)',   '', 'http://www.postgresql.org/docs/9.4/', 'prod');
INSERT INTO releases VALUES ('pgdg95',     'pg',       'PostgreSQL 9.5', 'PG Server (pgdg)',   '', 'http://www.postgresql.org/docs/9.5/', 'prod');
INSERT INTO releases VALUES ('pgdg96',     'pg',       'PostgreSQL 9.6', 'PG Server (pgdg)',   '', 'http://www.postgresql.org/docs/9.6/', 'prod');
INSERT INTO releases VALUES ('pgha3',      'pgha',     'pgHA',           'High Availability',        'linux64', '', 'test');
INSERT INTO releases VALUES ('pgagent',    'pgagent',  'pgAgent',        'Background Scheduler', '', 'https://www.pgadmin.org/docs/1.22/pgagent.html', 'prod');
INSERT INTO releases VALUES ('pgadmin3',   'pgadmin',  'pgAdmin3',       'Fat Client Dev Tool', '', 'http://pgadmin3.org', 'prod');
INSERT INTO releases VALUES ('pgadmin4',   'pgadmin',  'pgAdmin4',       'Python port of pgAdmin3', '',   'https://www.pgadmin.org/docs4/dev/index.html', 'prod');
INSERT INTO releases VALUES ('cassandra30','cassandra','Cassandra 3.0',  'Cassandra Server',   '', 'http://docs.datastax.com/en/cassandra/3.0/cassandra/cassandraAbout.html', 'test');
INSERT INTO releases VALUES ('tomcat8',    'tomcat',   'Tomcat 8',       'Tomcat 8',           '', 'http://tomcat.apache.org/tomcat-8.0-doc/index.html', 'test');
INSERT INTO releases VALUES ('spark16',    'spark',    'Spark 1.6',      'Cluster Computing Server', 'linux64', 'http://spark.apache.org/releases/spark-release-1-6-0.html', 'test');
INSERT INTO releases VALUES ('spark20',    'spark',    'Spark 2.0',      'Cluster Computing Server', 'linux64', 'http://spark.apache.org/releases/spark-release-2-0-0.html', 'test');
INSERT INTO releases VALUES ('gpdb5',      'gpdb',     'GPDB 5.0',       'GPDB Data Lake', 'linux64', '', 'prod');
INSERT INTO releases VALUES ('hadoop26',   'hadoop',   'Hadoop 2.6',     'Distributed Computing and Data Storage', 'linux64', '', 'test');
INSERT INTO releases VALUES ('hive2',      'hive',     'Hive 2.0',       'Apache Hive Server', 'linux64', '', 'test');
INSERT INTO releases VALUES ('hive21',     'hive',     'Hive 2.1',       'Apache Hive Server', 'linux64', '', 'test');
INSERT INTO releases VALUES ('zookeeper34','zookeeper','ZooKeeper 3.4',  
  'Apache ZooKeeper', 
  'linux64', '', 'test');
INSERT INTO releases VALUES ('pgstudio2',  'pgstudio', 'Postgresql Studio', 
  'Web-based PostgreSQL Development', 
  '', '', 'prod');
INSERT INTO releases VALUES ('pgbouncer17','pgbouncer','pgBouncer 1.7', 
  'Lightweight Connection Pooler', 
  '', 'https://pgbouncer.github.io/usage.html', 'prod');
INSERT INTO releases VALUES ('influxdb',     'influxdb',   'InfluxDB', 
  'Time-series DB', 
  '', 'https://github.com/influxdata/influxdb', 'test');
INSERT INTO releases VALUES ('kapacitor',     'kapacitor',   'Kapacitor',
  'Alerting Component',
  '', 'https://github.com/influxdata/kapacitor', 'test');
INSERT INTO releases VALUES ('chronograf',     'chronograf',   'Chronograf',
  'Alerting Web IO',
  '', 'https://github.com/influxdata/chronograf', 'test');
INSERT INTO releases VALUES ('telegraf',     'telegraf',   'Telegraf',
  'Metrics Collection',
  '', 'https://github.com/influxdata/telegraf', 'test');
INSERT INTO releases VALUES ('grafana', 'grafana', 'Grafana',
  'Monitoring & Metrics Dashboard', 
  '', 'https://github.com/grafana/grafana', 'test');
INSERT INTO releases VALUES ('elasticsearch',     'elasticsearch',   'ElasticSearch',
  'REST Search Engine',
  '', 'https://github.com/elastic/elasticsearch', 'test');
INSERT INTO releases VALUES ('logstash',     'logstash',   'LogStash',
  'Transport and process your logs',
  '', 'https://github.com/elastic/logstash', 'test');
INSERT INTO releases VALUES ('filebeat',     'filebeat',   'FileBeat',
  'Lightweight log shipper for elasticsearch',
  '', 'https://github.com/elastic/beats', 'test');
INSERT INTO releases VALUES ('kibana',     'kibana',   'Kibana',
  'Analytics and search dashboard for Elasticsearch',
  '', 'https://github.com/elastic/kibana', 'test');
INSERT INTO releases VALUES ('rundeck', 'rundeck', 'RunDeck',
  'Job Scheduler', 
  '', 'http://rundeck.org', 'test');
INSERT INTO releases VALUES ('collectd5', 'collectd', 'collectd',
  'Metrics Collection', 
  '', 'http://collectd.org', 'test');
INSERT INTO releases VALUES ('consul',     'consul',   'Consul', 
  'HA & Distributed Service Discovery', 
  '', 'http://consul.io/docs', 'test');
INSERT INTO releases VALUES ('pgbadger',   'pgbadger', 'pgBadger',
  'Fully detailed SQL query performance reports from your PostgreSQL log file', 
  '', 'https://github.com/dalibo/pgbadger/blob/master/doc/pgBadger.pod', 'prod');
INSERT INTO releases VALUES ('backrest',   'backrest', 'pgBackRest', 
  'Reliable backup & restore that seamlessly scales to the largest databases and workloads', 
  '', 'http://www.pgbackrest.org', 'prod');
INSERT INTO releases VALUES ('ora2pg',     'ora2pg',   'Ora2Pg',
  'Easily migrate an Oracle database to a PostgreSQL schema', 
  '', 'http://ora2pg.darold.net/documentation.html', 'prod');
INSERT INTO releases VALUES ('vagrant',     'vagrant',   'Vagrant',
  'Build & Manage Virtual Machines', 
  '', 'http://vagrantup.com', 'test');
INSERT INTO releases VALUES ('virtualbox',     'virtualbox',   'VirtualBox',
  'Virtual Machine Host', 
  '', 'http://virtualbox.org', 'test');
INSERT INTO releases VALUES ('python2',    'python',   'Python 2.7', 
  'Python Programming Language', 
  '', '', 'prod');
INSERT INTO releases VALUES ('perl5',      'perl',     'Perl 5', 
  'Perl Programming Language', '', '', 'prod');
INSERT INTO releases VALUES ('tcl86',      'tcl',      'Tcl', 
  'Tcl Programming Language', '', '', 'prod');
INSERT INTO releases VALUES ('java8',      'java',     'Java 8',
  'Java 8 (JRE) Programming Laanguage', '', '', 'prod');
INSERT INTO releases VALUES ('bam2',       'bam',      'BAM 2', 
  'BigSQL Manager 2', '', '', 'prod');
INSERT INTO releases VALUES ('pgdevops', 'pgdevops', 'pgDevOps',
  'pgDevOps by BigSQL', 
  '', '', 'prod');
INSERT INTO releases VALUES ('webtty', 'webtty', 'webtty', 
  'Terminal in browser over http', 
  '', 'http://bitbucket.org/openscg/webtty', 'test');

INSERT INTO releases VALUES ('bulkload3-pg96', 'bulkload', 'pgBulkload', 'Quick Data Loading', '', '', 'prod');
INSERT INTO releases VALUES ('bulkload3-pg95', 'bulkload', 'pgBulkload', 'Quick Data Loading', '', '', 'prod');
INSERT INTO releases VALUES ('bulkload3-pg94', 'bulkload', 'pgBulkload', 'Quick Data Loading', '', '', 'prod');
INSERT INTO releases VALUES ('bulkload3-pg93', 'bulkload', 'pgBulkload', 'Quick Data Loading', '', '', 'prod');
INSERT INTO releases VALUES ('bulkload3-pg92', 'bulkload', 'pgBulkload', 'Quick Data Loading', '', '', 'prod');

INSERT INTO releases VALUES ('logical2-pg96', 'logical', 'pgLogical', 'Logical Replication', '', '', 'prod');
INSERT INTO releases VALUES ('logical2-pg95', 'logical', 'pgLogical', 'Logical Replication', '', '', 'prod');

INSERT INTO releases VALUES ('pgpartman3-pg10', 'partman', 'pgPartMan', 'Manage Partitioned Tables by Time or ID', '', '', 'prod');
INSERT INTO releases VALUES ('pgpartman3-pg96', 'partman', 'pgPartMan', 'Manage Partitioned Tables by Time or ID', '', '', 'prod');
INSERT INTO releases VALUES ('pgpartman3-pg95', 'partman', 'pgPartMan', 'Manage Partitioned Tables by Time or ID', '', '', 'prod');
INSERT INTO releases VALUES ('pgpartman3-pg94', 'partman', 'pgPartMan', 'Manage Partitioned Tables by Time or ID', '', '', 'prod');

INSERT INTO releases VALUES ('pgpartman2-pg96', 'partman', 'pgPartMan', 'Manage Partitioned Tables by Time or ID', '', '', 'prod');
INSERT INTO releases VALUES ('pgpartman2-pg95', 'partman', 'pgPartMan', 'Manage Partitioned Tables by Time or ID', '', '', 'prod');
INSERT INTO releases VALUES ('pgpartman2-pg94', 'partman', 'pgPartMan', 'Manage Partitioned Tables by Time or ID', '', '', 'prod');

INSERT INTO releases VALUES ('repack14-pg10', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack14-pg96', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack14-pg95', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack14-pg94', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack14-pg93', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');

INSERT INTO releases VALUES ('repack13-pg96', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack13-pg95', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack13-pg94', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack13-pg93', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');
INSERT INTO releases VALUES ('repack13-pg92', 'repack', 'pgRepack', 'Online Table Reorganization', '', '', 'prod');

INSERT INTO releases VALUES ('setuser1-pg10',  'setuser', 'SetUser', 
  'PostgreSQL extension allowing privilege escalation with enhanced logging and control', 
  '', '', 'prod');
INSERT INTO releases VALUES ('setuser1-pg96',  'setuser', 'SetUser', 
  'PostgreSQL extension allowing privilege escalation with enhanced logging and control', 
  '', '', 'prod');
INSERT INTO releases VALUES ('setuser1-pg95',  'setuser', 'SetUser', 
  'PostgreSQL extension allowing privilege escalation with enhanced logging and control', 
  '', '', 'prod');

INSERT INTO releases VALUES ('pgaudit12-pg10', 'pgaudit', 'pgAudit', 'PostgreSQL Auditing Extension', '', 'http://pgaudit.org', 'prod');
INSERT INTO releases VALUES ('pgaudit11-pg96', 'pgaudit', 'pgAudit', 'PostgreSQL Auditing Extension', '', 'http://pgaudit.org', 'prod');
INSERT INTO releases VALUES ('pgaudit10-pg95', 'pgaudit', 'pgAudit', 'PostgreSQL Auditing Extension', '', 'http://pgaudit.org', 'prod');

INSERT INTO releases VALUES ('pldebugger96-pg10', 'pldebugger', 'plDebugger', 'Debug Stored Procedures', '', '', 'prod');
INSERT INTO releases VALUES ('pldebugger96-pg96', 'pldebugger', 'plDebugger', 'Debug Stored Procedures', '', '', 'prod');
INSERT INTO releases VALUES ('pldebugger96-pg95', 'pldebugger', 'plDebugger', 'Debug Stored Procedures', '', '', 'prod');
INSERT INTO releases VALUES ('pldebugger96-pg94', 'pldebugger', 'plDebugger', 'Debug Stored Procedures', '', '', 'prod');
INSERT INTO releases VALUES ('pldebugger96-pg93', 'pldebugger', 'plDebugger', 'Debug Stored Procedures', '', '', 'prod');
INSERT INTO releases VALUES ('pldebugger96-pg92', 'pldebugger', 'plDebugger', 'Debug Stored Procedures', '', '', 'prod');

INSERT INTO releases VALUES ('background1-pg96', 'background', 'pgBackground', 'Autonomous SQL Txns', '', '', 'prod');
INSERT INTO releases VALUES ('background1-pg95', 'background', 'pgBackground', 'Autonomous SQL Txns', '', '', 'prod');

INSERT INTO releases VALUES ('cstore_fdw1-pg96', 'cstore_fdw', 'cStoreFDW', 'Columnar Store for PostgreSQL', '', '', 'prod');
INSERT INTO releases VALUES ('cstore_fdw1-pg95', 'cstore_fdw', 'cStoreFDW', 'Columnar Store for PostgreSQL', '', '', 'prod');
INSERT INTO releases VALUES ('cstore_fdw1-pg94', 'cstore_fdw', 'cStoreFDW', 'Columnar Store for PostgreSQL', '', '', 'prod');
INSERT INTO releases VALUES ('cstore_fdw1-pg93', 'cstore_fdw', 'cStoreFDW', 'Columnar Store for PostgreSQL', '', '', 'prod');

INSERT INTO releases VALUES ('plprofiler3-pg10', 'plprofiler', 'plProfiler', 'Procedural Language Performance Profiler', '', 'https://bitbucket.org/openscg/plprofiler', 'prod');
INSERT INTO releases VALUES ('plprofiler3-pg96', 'plprofiler', 'plProfiler', 'Procedural Language Performance Profiler', '', 'https://bitbucket.org/openscg/plprofiler', 'prod');
INSERT INTO releases VALUES ('plprofiler3-pg95', 'plprofiler', 'plProfiler', 'Procedural Language Performance Profiler', '', 'https://bitbucket.org/openscg/plprofiler', 'prod');
INSERT INTO releases VALUES ('plprofiler3-pg94', 'plprofiler', 'plProfiler', 'Procedural Language Performance Profiler', '', 'https://bitbucket.org/openscg/plprofiler', 'prod');
INSERT INTO releases VALUES ('plprofiler3-pg93', 'plprofiler', 'plProfiler', 'Procedural Language Performance Profiler', '', 'https://bitbucket.org/openscg/plprofiler', 'prod');
INSERT INTO releases VALUES ('plprofiler3-pg92', 'plprofiler', 'plProfiler', 'Procedural Language Performance Profiler', '', 'https://bitbucket.org/openscg/plprofiler', 'prod');

INSERT INTO releases VALUES ('cassandra_fdw3-pg96', 'cassandra_fdw', 'CassandraFDW', 'C* Interoperability', '', '', 'prod');
INSERT INTO releases VALUES ('cassandra_fdw3-pg95', 'cassandra_fdw', 'CassandraFDW', 'C* Interoperability', '', '', 'prod');
INSERT INTO releases VALUES ('cassandra_fdw3-pg94', 'cassandra_fdw', 'CassandraFDW', 'C* Interoperability', '', '', 'prod');

INSERT INTO releases VALUES ('hadoop_fdw2-pg96', 'hadoop_fdw', 'HadoopFDW', 'Hive Queries to Hadoop or Spark', '', '', 'prod');
INSERT INTO releases VALUES ('hadoop_fdw2-pg95', 'hadoop_fdw', 'HadoopFDW', 'Hive Queries to Hadoop or Spark', '', '', 'prod');
INSERT INTO releases VALUES ('hadoop_fdw2-pg94', 'hadoop_fdw', 'HadoopFDW', 'Hive Queries to Hadoop or Spark', '', '', 'prod');

INSERT INTO releases VALUES ('slony22-pg10',   'slony',  'Slony-I', 'Master to Multiple Slaves Replication', '', 'http://slony.info/documentation/2.2/index.html', 'prod');
INSERT INTO releases VALUES ('slony22-pg96',   'slony',  'Slony-I', 'Master to Multiple Slaves Replication', '', 'http://slony.info/documentation/2.2/index.html', 'prod');
INSERT INTO releases VALUES ('slony22-pg95',   'slony',  'Slony-I', 'Master to Multiple Slaves Replication', '', 'http://slony.info/documentation/2.2/index.html', 'prod');
INSERT INTO releases VALUES ('slony22-pg94',   'slony',  'Slony-I', 'Master to Multiple Slaves Replication', '', 'http://slony.info/documentation/2.2/index.html', 'prod');
INSERT INTO releases VALUES ('slony22-pg93',   'slony',  'Slony-I', 'Master to Multiple Slaves Replication', '', 'http://slony.info/documentation/2.2/index.html', 'prod');
INSERT INTO releases VALUES ('slony22-pg92',   'slony',  'Slony-I', 'Master to Multiple Slaves Replication', '', 'http://slony.info/documentation/2.2/index.html', 'prod');

INSERT INTO releases VALUES ('postgis24-pg10', 'postgis', 'PostGIS 2.4', 'Spatial & Geographic Objects', '', 'http://postgis.net/docs/manual-2.4', 'prod');
INSERT INTO releases VALUES ('postgis23-pg96', 'postgis', 'PostGIS 2.3', 'Spatial & Geographic Objects', '', 'http://postgis.net/docs/manual-2.3', 'prod');
INSERT INTO releases VALUES ('postgis22-pg95', 'postgis', 'PostGIS 2.2', 'Spatial & Geographic Objects', '', 'http://postgis.net/docs/manual-2.2', 'prod');
INSERT INTO releases VALUES ('postgis22-pg94', 'postgis', 'PostGIS 2.2', 'Spatial & Geographic Objects', '', 'http://postgis.net/docs/manual-2.2', 'prod');
INSERT INTO releases VALUES ('postgis22-pg93', 'postgis', 'PostGIS 2.2', 'Spatial & Geographic Objects', '', 'http://postgis.net/docs/manual-2.2', 'prod');
INSERT INTO releases VALUES ('postgis22-pg92', 'postgis', 'PostGIS 2.2', 'Spatial & Geographic Objects', '', 'http://postgis.net/docs/manual-2.2', 'prod');

INSERT INTO releases VALUES ('oracle_fdw2-pg10', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');
INSERT INTO releases VALUES ('oracle_fdw2-pg96', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');
INSERT INTO releases VALUES ('oracle_fdw2-pg95', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');

INSERT INTO releases VALUES ('oracle_fdw1-pg96', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');
INSERT INTO releases VALUES ('oracle_fdw1-pg95', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');
INSERT INTO releases VALUES ('oracle_fdw1-pg94', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');
INSERT INTO releases VALUES ('oracle_fdw1-pg93', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');
INSERT INTO releases VALUES ('oracle_fdw1-pg92', 'oracle_fdw', 'OracleFDW', 'Foreign Data Wrapper for Oracle', '', 'https://github.com/laurenz/oracle_fdw', 'prod');

INSERT INTO releases VALUES ('mysql_fdw2-pg10', 'mysql_fdw', 'MySQL-FDW', 'Foreign Data Wrapper for MySQL', '', 'https://github.com/EnterpriseDB/mysql_fdw', 'prod');
INSERT INTO releases VALUES ('mysql_fdw2-pg96', 'mysql_fdw', 'MySQL-FDW', 'Foreign Data Wrapper for MySQL', '', 'https://github.com/EnterpriseDB/mysql_fdw', 'prod');
INSERT INTO releases VALUES ('mysql_fdw2-pg95', 'mysql_fdw', 'MySQL-FDW', 'Foreign Data Wrapper for MySQL', '', 'https://github.com/EnterpriseDB/mysql_fdw', 'prod');
INSERT INTO releases VALUES ('mysql_fdw2-pg94', 'mysql_fdw', 'MySQL-FDW', 'Foreign Data Wrapper for MySQL', '', 'https://github.com/EnterpriseDB/mysql_fdw', 'prod');
INSERT INTO releases VALUES ('mysql_fdw2-pg93', 'mysql_fdw', 'MySQL-FDW', 'Foreign Data Wrapper for MySQL', '', 'https://github.com/EnterpriseDB/mysql_fdw', 'prod');

INSERT INTO releases VALUES ('tds_fdw1-pg96', 'tds_fdw', 'TDS-FDW', 'Foreign Data Wrapper for Sybase & SqlServer', '', 'https://github.com/tds-fdw/tds_fdw', 'prod');
INSERT INTO releases VALUES ('tds_fdw1-pg95', 'tds_fdw', 'TDS-FDW', 'Foreign Data Wrapper for Sybase & SqlServer', '', 'https://github.com/tds-fdw/tds_fdw', 'prod');
INSERT INTO releases VALUES ('tds_fdw1-pg94', 'tds_fdw', 'TDS-FDW', 'Foreign Data Wrapper for Sybase & SqlServer', '', 'https://github.com/tds-fdw/tds_fdw', 'prod');
INSERT INTO releases VALUES ('tds_fdw1-pg93', 'tds_fdw', 'TDS-FDW', 'Foreign Data Wrapper for Sybase & SqlServer', '', 'https://github.com/tds-fdw/tds_fdw', 'prod');
INSERT INTO releases VALUES ('tds_fdw1-pg92', 'tds_fdw', 'TDS-FDW', '', 'Foreign Data Wrapper for Sybase and SqlServer', 'https://github.com/tds-fdw/tds_fdw', 'prod');

INSERT INTO releases VALUES ('orafce3-pg10', 'orafce', 'OraFCE', 'Oracle Compatible Functions', '', 'https://github.com/orafce/orafce', 'prod');
INSERT INTO releases VALUES ('orafce3-pg96', 'orafce', 'OraFCE', 'Oracle Compatible Functions', '', 'https://github.com/orafce/orafce', 'prod');
INSERT INTO releases VALUES ('orafce3-pg95', 'orafce', 'OraFCE', 'Oracle Compatible Functions', '', 'https://github.com/orafce/orafce', 'prod');
INSERT INTO releases VALUES ('orafce3-pg94', 'orafce', 'OraFCE', 'Oracle Compatible Functions', '', 'https://github.com/orafce/orafce', 'prod');
INSERT INTO releases VALUES ('orafce3-pg93', 'orafce', 'OraFCE', 'Oracle Compatible Functions', '', 'https://github.com/orafce/orafce', 'prod');
INSERT INTO releases VALUES ('orafce3-pg92', 'orafce', 'OraFCE', 'Oracle Compatible Functions', '', 'https://github.com/orafce/orafce', 'prod');

INSERT INTO releases VALUES ('plv814-pg96', 'plv8', 'plV8 1.4', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv814-pg95', 'plv8', 'plV8 1.4', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv814-pg94', 'plv8', 'plV8 1.4', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv814-pg93', 'plv8', 'plV8 1.4', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv814-pg92', 'plv8', 'plV8 1.4', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');

INSERT INTO releases VALUES ('plv815-pg96', 'plv8', 'plV8 1.5', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv815-pg95', 'plv8', 'plV8 1.5', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv815-pg94', 'plv8', 'plV8 1.5', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv815-pg93', 'plv8', 'plV8 1.5', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');
INSERT INTO releases VALUES ('plv815-pg92', 'plv8', 'plV8 1.5', 'Javascript Procedural Language', '', 'https://github.com/plv8/plv8', 'prod');

INSERT INTO releases VALUES ('pljava15-pg95', 'pljava', 'plJava', 'Stored Procedures in Java', '', 'https://github.com/tada/pljava', 'prod');
INSERT INTO releases VALUES ('pljava15-pg94', 'pljava', 'plJava', 'Stored Procedures in Java', '', 'https://github.com/tada/pljava', 'prod');
INSERT INTO releases VALUES ('pljava15-pg93', 'pljava', 'plJava', 'Stored Procedures in Java', '', 'https://github.com/tada/pljava', 'prod');
INSERT INTO releases VALUES ('pljava15-pg92', 'pljava', 'plJava', 'Stored Procedures in Java', '', 'https://github.com/tada/pljava', 'prod');

INSERT INTO releases VALUES ('hintplan-pg96', 'hintplan', 'HintPlan', 'Guiding the Planner w/ Hints', '', 'https://osdn.net/projects/pghintplan', 'prod');
INSERT INTO releases VALUES ('hintplan-pg95', 'hintplan', 'HintPlan', 'Guiding the Planner w/ Hints', '', 'https://osdn.net/projects/pghintplan', 'prod');
INSERT INTO releases VALUES ('hintplan-pg94', 'hintplan', 'HintPlan', 'Guiding the Planner w/ Hints', '', 'https://osdn.net/projects/pghintplan', 'prod');
INSERT INTO releases VALUES ('hintplan-pg93', 'hintplan', 'HintPlan', 'Guiding the Planner w/ Hints', '', 'https://osdn.net/projects/pghintplan', 'prod');

INSERT INTO releases VALUES ('pgtsql9-pg95', 'pgtsql', 'pgTSQL', 'TSQL Procedural Language', '', 'https://bitbucket.org/openscg/pgtsql', 'prod');
INSERT INTO releases VALUES ('pgtsql9-pg94', 'pgtsql', 'pgTSQL', 'TSQL Procedural Language', '', 'https://bitbucket.org/openscg/pgtsql', 'prod');


CREATE TABLE versions (
  component     TEXT    NOT NULL,
  version       TEXT    NOT NULL,
  platform      TEXT    NOT NULL,
  is_current    INTEGER NOT NULL,
  release_date  DATE    NOT NULL,
  parent        TEXT    NOT NULL,
  PRIMARY KEY (component, version),
  FOREIGN KEY (component) REFERENCES releases(component)
);

INSERT INTO versions VALUES ('hub', '3.3.4',       '', 1, '20171109', '');
INSERT INTO versions VALUES ('hub', '3.3.3',       '', 0, '20171104', '');
INSERT INTO versions VALUES ('hub', '3.3.2',       '', 0, '20171025', '');
INSERT INTO versions VALUES ('hub', '3.3.1',       '', 0, '20171025', '');
INSERT INTO versions VALUES ('hub', '3.3.0',       '', 0, '20171012', '');
INSERT INTO versions VALUES ('hub', '3.2.9',       '', 0, '20170918', '');
INSERT INTO versions VALUES ('hub', '3.2.8',       '', 0, '20170810', '');
INSERT INTO versions VALUES ('hub', '3.2.7',       '', 0, '20170802', '');

INSERT INTO versions VALUES ('webtty', '1.2', 'linux64', 1, '20170209', '');

INSERT INTO versions VALUES ('pgdevops', '2.0-2', '', 1, '20171109', '');
INSERT INTO versions VALUES ('pgdevops', '2.0-1', '', 0, '20171025', '');
INSERT INTO versions VALUES ('pgdevops', '2.0-0', '', 0, '20171012', '');
INSERT INTO versions VALUES ('pgdevops', '1.6-4', '', 0, '20171005', '');
INSERT INTO versions VALUES ('pgdevops', '1.6-3', '', 0, '20170810', '');

INSERT INTO versions VALUES ('bam2', '1.6.3', '', 0, '20161027', '');

INSERT INTO versions VALUES ('slony22-pg10',   '2.2.6-1', 'linux64',         1, '20170921', 'pg10');

INSERT INTO versions VALUES ('slony22-pg96',   '2.2.6-1', 'linux64',         1, '20170921', 'pg96');
INSERT INTO versions VALUES ('slony22-pg96',   '2.2.5-2', 'linux64',         0, '20160901', 'pg96');

INSERT INTO versions VALUES ('slony22-pg95',   '2.2.6-1', 'linux64',         1, '20170921', 'pg95');
INSERT INTO versions VALUES ('slony22-pg95',   '2.2.5-2', 'linux64',         0, '20160901', 'pg95');
INSERT INTO versions VALUES ('slony22-pg95',   '2.2.5-1', 'linux64',         0, '20160607', 'pg95');

INSERT INTO versions VALUES ('slony22-pg94',   '2.2.6-1', 'linux64',         1, '20170921', 'pg94');
INSERT INTO versions VALUES ('slony22-pg94',   '2.2.5-2', 'linux64',         0, '20160901', 'pg94');
INSERT INTO versions VALUES ('slony22-pg94',   '2.2.5-1', 'linux64',         0, '20160607', 'pg94');

INSERT INTO versions VALUES ('slony22-pg93',   '2.2.6-1', 'linux64',         1, '20170921', 'pg93');
INSERT INTO versions VALUES ('slony22-pg93',   '2.2.5-2', 'linux64',         0, '20160901', 'pg93');
INSERT INTO versions VALUES ('slony22-pg93',   '2.2.5-1', 'linux64',         0, '20160607', 'pg93');

INSERT INTO versions VALUES ('slony22-pg92',   '2.2.6-1', 'linux64',         1, '20170921', 'pg92');
INSERT INTO versions VALUES ('slony22-pg92',   '2.2.5-2', 'linux64',         0, '20160901', 'pg92');
INSERT INTO versions VALUES ('slony22-pg92',   '2.2.5-1', 'linux64',         0, '20160607', 'pg92');

INSERT INTO versions VALUES ('bulkload3-pg96', '3.1.13-1', 'linux64, osx64', 1, '20170209', 'pg96');
INSERT INTO versions VALUES ('bulkload3-pg95', '3.1.13-1', 'linux64, osx64', 1, '20170209', 'pg95');
INSERT INTO versions VALUES ('bulkload3-pg94', '3.1.13-1', 'linux64, osx64', 1, '20170209', 'pg94');
INSERT INTO versions VALUES ('bulkload3-pg93', '3.1.13-1', 'linux64, osx64', 1, '20170209', 'pg93');
INSERT INTO versions VALUES ('bulkload3-pg92', '3.1.13-1', 'linux64, osx64', 1, '20170209', 'pg92');

INSERT INTO versions VALUES ('logical2-pg96', '2.0-1', 'win64, linux64, osx64', 1, '20170518', 'pg96');
INSERT INTO versions VALUES ('logical2-pg95', '2.0-1', 'win64, linux64, osx64', 1, '20170518', 'pg95');

INSERT INTO versions VALUES ('pgpartman3-pg10', '3.0.1-1', 'win64, linux64, osx64', 0, '20170415', 'pg10');

INSERT INTO versions VALUES ('pgpartman3-pg10', '3.1.0-1', 'win64, linux64, osx64', 1, '20171012', 'pg10');
INSERT INTO versions VALUES ('pgpartman3-pg96', '3.1.0-1', 'win64, linux64, osx64', 1, '20171012', 'pg96');
INSERT INTO versions VALUES ('pgpartman3-pg95', '3.1.0-1', 'win64, linux64, osx64', 1, '20171012', 'pg95');
INSERT INTO versions VALUES ('pgpartman3-pg94', '3.1.0-1', 'win64, linux64, osx64', 1, '20171012', 'pg94');

INSERT INTO versions VALUES ('pgpartman3-pg10', '3.0.2-1', 'win64, linux64, osx64', 0, '20170908', 'pg10');
INSERT INTO versions VALUES ('pgpartman3-pg96', '3.0.2-1', 'win64, linux64, osx64', 0, '20170908', 'pg96');
INSERT INTO versions VALUES ('pgpartman3-pg95', '3.0.2-1', 'win64, linux64, osx64', 0, '20170908', 'pg95');
INSERT INTO versions VALUES ('pgpartman3-pg94', '3.0.2-1', 'win64, linux64, osx64', 0, '20170908', 'pg94');

INSERT INTO versions VALUES ('pgpartman2-pg96', '2.6.4-1', 'win64, linux64, osx64', 0, '20170415', 'pg96');
INSERT INTO versions VALUES ('pgpartman2-pg95', '2.6.4-1', 'win64, linux64, osx64', 0, '20170415', 'pg95');
INSERT INTO versions VALUES ('pgpartman2-pg94', '2.6.4-1', 'win64, linux64, osx64', 0, '20170415', 'pg94');

INSERT INTO versions VALUES ('repack14-pg10', '1.4.2-1', 'linux64, osx64', 1, '20171104', 'pg10');
INSERT INTO versions VALUES ('repack14-pg96', '1.4.2-1', 'linux64, osx64', 1, '20171104', 'pg96');
INSERT INTO versions VALUES ('repack14-pg95', '1.4.2-1', 'linux64, osx64', 1, '20171104', 'pg95');
INSERT INTO versions VALUES ('repack14-pg94', '1.4.2-1', 'linux64, osx64', 1, '20171104', 'pg94');
INSERT INTO versions VALUES ('repack14-pg93', '1.4.2-1', 'linux64, osx64', 1, '20171104', 'pg93');

INSERT INTO versions VALUES ('repack14-pg96', '1.4.1-1', 'linux64, osx64', 0, '20171004', 'pg96');
INSERT INTO versions VALUES ('repack14-pg95', '1.4.1-1', 'linux64, osx64', 0, '20171004', 'pg95');
INSERT INTO versions VALUES ('repack14-pg94', '1.4.1-1', 'linux64, osx64', 0, '20171004', 'pg94');
INSERT INTO versions VALUES ('repack14-pg93', '1.4.1-1', 'linux64, osx64', 0, '20171004', 'pg93');

INSERT INTO versions VALUES ('repack13-pg96', '1.3.4-1', 'linux64, osx64', 0, '20161108', 'pg96');
INSERT INTO versions VALUES ('repack13-pg95', '1.3.4-1', 'linux64, osx64', 0, '20161108', 'pg95');
INSERT INTO versions VALUES ('repack13-pg94', '1.3.4-1', 'linux64, osx64', 0, '20161108', 'pg94');
INSERT INTO versions VALUES ('repack13-pg93', '1.3.4-1', 'linux64, osx64', 0, '20161108', 'pg93');
INSERT INTO versions VALUES ('repack13-pg92', '1.3.4-1', 'linux64, osx64', 0, '20161108', 'pg92');

INSERT INTO versions VALUES ('pgaudit12-pg10', '1.2.0-1', 'win64, linux64, osx64', 1, '20171012', 'pg10');
INSERT INTO versions VALUES ('pgaudit11-pg96', '1.1.1-1', 'win64, linux64, osx64', 1, '20170907', 'pg96');
INSERT INTO versions VALUES ('pgaudit11-pg96', '1.1.0-2', 'win64, linux64, osx64', 0, '20170518', 'pg96');
INSERT INTO versions VALUES ('pgaudit11-pg96', '1.1.0-1', 'win64, linux64, osx64', 0, '20161228', 'pg96');
INSERT INTO versions VALUES ('pgaudit10-pg95', '1.0.6-1', 'win64, linux64, osx64', 1, '20170907', 'pg95');
INSERT INTO versions VALUES ('pgaudit10-pg95', '1.0.5-2', 'win64, linux64, osx64', 0, '20170518', 'pg95');
INSERT INTO versions VALUES ('pgaudit10-pg95', '1.0.4-1', 'win64, linux64, osx64', 0, '20161228', 'pg95');

INSERT INTO versions VALUES ('setuser1-pg10',  '1.4.0-1', 'win64, linux64, osx64', 1, '20170831', 'pg10');
INSERT INTO versions VALUES ('setuser1-pg96',  '1.4.0-1', 'win64, linux64, osx64', 1, '20170831', 'pg96');
INSERT INTO versions VALUES ('setuser1-pg95',  '1.4.0-1', 'win64, linux64, osx64', 1, '20170831', 'pg95');

INSERT INTO versions VALUES ('setuser1-pg96',  '1.2.0-1', 'win64, linux64, osx64', 0, '20170223', 'pg96');
INSERT INTO versions VALUES ('setuser1-pg95',  '1.2.0-1', 'win64, linux64, osx64', 0, '20170223', 'pg95');

INSERT INTO versions VALUES ('setuser1-pg96',  '1.1.0-1', 'win64, linux64, osx64', 0, '20161228', 'pg96');
INSERT INTO versions VALUES ('setuser1-pg95',  '1.1.0-1', 'win64, linux64, osx64', 0, '20161228', 'pg95');

INSERT INTO versions VALUES ('pldebugger96-pg10',  '9.6.0-2', 'win64, linux64, osx64', 1, '20170608', 'pg10');

INSERT INTO versions VALUES ('pldebugger96-pg96',  '9.6.0-1', 'win64, linux64, osx64', 1, '20161228', 'pg96');
INSERT INTO versions VALUES ('pldebugger96-pg95',  '9.6.0-1', 'win64, linux64, osx64', 1, '20161228', 'pg95');
INSERT INTO versions VALUES ('pldebugger96-pg94',  '9.6.0-1', 'win64, linux64, osx64', 1, '20161228', 'pg94');
INSERT INTO versions VALUES ('pldebugger96-pg93',  '9.6.0-1', 'linux64, osx64',        1, '20161228', 'pg93');
INSERT INTO versions VALUES ('pldebugger96-pg92',  '9.6.0-1', 'linux64, osx64',        1, '20161228', 'pg92');

INSERT INTO versions VALUES ('background1-pg96', '1.0-1', 'linux64, osx64', 1, '20170425', 'pg96');
INSERT INTO versions VALUES ('background1-pg95', '1.0-1', 'linux64, osx64', 1, '20170425', 'pg95');

INSERT INTO versions VALUES ('cstore_fdw1-pg96', '1.5.0-1', 'linux64', 1, '20161108', 'pg96');
INSERT INTO versions VALUES ('cstore_fdw1-pg95', '1.5.0-1', 'linux64', 1, '20161108', 'pg95');
INSERT INTO versions VALUES ('cstore_fdw1-pg94', '1.5.0-1', 'linux64', 1, '20161108', 'pg94');
INSERT INTO versions VALUES ('cstore_fdw1-pg93', '1.5.0-1', 'linux64', 1, '20161108', 'pg93');

INSERT INTO versions VALUES ('plprofiler3-pg10', '3.2-1', 'linux64, osx64, win64', 1, '20170608', 'pg10');
INSERT INTO versions VALUES ('plprofiler3-pg96', '3.2-1', 'linux64, osx64, win64', 1, '20170415', 'pg96');
INSERT INTO versions VALUES ('plprofiler3-pg95', '3.2-1', 'linux64, osx64, win64', 1, '20170415', 'pg95');
INSERT INTO versions VALUES ('plprofiler3-pg94', '3.2-1', 'linux64, osx64, win64', 1, '20170415', 'pg94');
INSERT INTO versions VALUES ('plprofiler3-pg93', '3.2-1', 'linux64, osx64, win64', 1, '20170415', 'pg93');
INSERT INTO versions VALUES ('plprofiler3-pg92', '3.2-1', 'linux64, osx64, win64', 1, '20170415', 'pg92');

INSERT INTO versions VALUES ('plprofiler3-pg96', '3.1-2', 'linux64, osx64, win64', 0, '20170323', 'pg96');
INSERT INTO versions VALUES ('plprofiler3-pg95', '3.1-2', 'linux64, osx64, win64', 0, '20170323', 'pg95');
INSERT INTO versions VALUES ('plprofiler3-pg94', '3.1-2', 'linux64, osx64, win64', 0, '20170323', 'pg94');
INSERT INTO versions VALUES ('plprofiler3-pg93', '3.1-2', 'linux64, osx64, win64', 0, '20170323', 'pg93');
INSERT INTO versions VALUES ('plprofiler3-pg92', '3.1-2', 'linux64, osx64, win64', 0, '20170323', 'pg92');

INSERT INTO versions VALUES ('cassandra_fdw3-pg96', '3.0.1-1', 'linux64, osx64, win64', 1, '20161108', 'pg96');
INSERT INTO versions VALUES ('cassandra_fdw3-pg95', '3.0.1-1', 'linux64, osx64, win64', 1, '20161108', 'pg95');
INSERT INTO versions VALUES ('cassandra_fdw3-pg94', '3.0.1-1', 'linux64, osx64, win64', 1, '20161108', 'pg94');

INSERT INTO versions VALUES ('cassandra_fdw3-pg95', '3.0.0-1', 'linux64, osx64, win64', 0, '20160603', 'pg95');

INSERT INTO versions VALUES ('hadoop_fdw2-pg96', '2.5.0-1', 'linux64, osx64, win64', 1, '20160901', 'pg96');
INSERT INTO versions VALUES ('hadoop_fdw2-pg95', '2.5.0-1', 'linux64, osx64, win64', 1, '20160701', 'pg95');
INSERT INTO versions VALUES ('hadoop_fdw2-pg94', '2.5.0-1', 'linux64, osx64, win64', 1, '20160701', 'pg94');

INSERT INTO versions VALUES ('hadoop_fdw2-pg95', '2.1.0-1', 'linux64, osx64, win64', 0, '20160603', 'pg95');
INSERT INTO versions VALUES ('hadoop_fdw2-pg94', '2.1.0-1', 'linux64, osx64, win64', 0, '20160603', 'pg94');

INSERT INTO versions VALUES ('oracle_fdw2-pg10', '2.0.0-1', 'linux64, win64', 1, '20160928', 'pg10');
INSERT INTO versions VALUES ('oracle_fdw2-pg96', '2.0.0-1', 'linux64, win64', 1, '20160928', 'pg96');
INSERT INTO versions VALUES ('oracle_fdw2-pg95', '2.0.0-1', 'linux64, win64', 1, '20160928', 'pg95');

INSERT INTO versions VALUES ('oracle_fdw1-pg96', '1.5.0-1', 'linux64, win64', 0, '20160901', 'pg96');
INSERT INTO versions VALUES ('oracle_fdw1-pg95', '1.5.0-1', 'linux64, win64', 0, '20160901', 'pg95');

INSERT INTO versions VALUES ('oracle_fdw1-pg95', '1.4.0-1', 'linux64, win64', 0, '20160701', 'pg95');
INSERT INTO versions VALUES ('oracle_fdw1-pg94', '1.4.0-1', 'linux64, win64', 1, '20160701', 'pg94');
INSERT INTO versions VALUES ('oracle_fdw1-pg93', '1.4.0-1', 'linux64, win64', 1, '20160701', 'pg93');
INSERT INTO versions VALUES ('oracle_fdw1-pg92', '1.4.0-1', 'linux64, win64', 1, '20160701', 'pg92');

INSERT INTO versions VALUES ('mysql_fdw2-pg10', '2.3.0-1', 'linux64',  1, '20170921', 'pg10');
INSERT INTO versions VALUES ('mysql_fdw2-pg96', '2.3.0-1', 'linux64',  1, '20170921', 'pg96');
INSERT INTO versions VALUES ('mysql_fdw2-pg95', '2.3.0-1', 'linux64',  1, '20170921', 'pg95');
INSERT INTO versions VALUES ('mysql_fdw2-pg94', '2.3.0-1', 'linux64',  1, '20170921', 'pg94');
INSERT INTO versions VALUES ('mysql_fdw2-pg93', '2.3.0-1', 'linux64',  1, '20170921', 'pg93');

INSERT INTO versions VALUES ('mysql_fdw2-pg96', '2.2.0-1', 'linux64',  0, '20170209', 'pg96');
INSERT INTO versions VALUES ('mysql_fdw2-pg95', '2.2.0-1', 'linux64',  0, '20170209', 'pg95');
INSERT INTO versions VALUES ('mysql_fdw2-pg94', '2.2.0-1', 'linux64',  0, '20170209', 'pg94');
INSERT INTO versions VALUES ('mysql_fdw2-pg93', '2.2.0-1', 'linux64',  0, '20170209', 'pg93');

INSERT INTO versions VALUES ('mysql_fdw2-pg95', '2.1.2-1', 'linux64',  0, '20160701', 'pg95');
INSERT INTO versions VALUES ('mysql_fdw2-pg94', '2.1.2-1', 'linux64',  0, '20160701', 'pg94');
INSERT INTO versions VALUES ('mysql_fdw2-pg93', '2.1.2-1', 'linux64',  0, '20160701', 'pg93');

INSERT INTO versions VALUES ('tds_fdw1-pg96', '1.0.8-1', 'linux64, win64',  1, '20161123', 'pg96');
INSERT INTO versions VALUES ('tds_fdw1-pg95', '1.0.8-1', 'linux64, win64',  1, '20161123', 'pg95');
INSERT INTO versions VALUES ('tds_fdw1-pg94', '1.0.8-1', 'linux64, win64',  1, '20161123', 'pg94');
INSERT INTO versions VALUES ('tds_fdw1-pg93', '1.0.8-1', 'linux64, win64',  1, '20161123', 'pg93');
INSERT INTO versions VALUES ('tds_fdw1-pg92', '1.0.8-1', 'linux64, win64',  1, '20161123', 'pg92');

INSERT INTO versions VALUES ('orafce3-pg10', '3.6.1-1', 'linux64, osx64, win64',  1, '20171104', 'pg10');
INSERT INTO versions VALUES ('orafce3-pg96', '3.6.1-1', 'linux64, osx64, win64',  1, '20171104', 'pg96');
INSERT INTO versions VALUES ('orafce3-pg95', '3.6.1-1', 'linux64, osx64, win64',  1, '20171104', 'pg95');
INSERT INTO versions VALUES ('orafce3-pg94', '3.6.1-1', 'linux64, osx64, win64',  1, '20171104', 'pg94');
INSERT INTO versions VALUES ('orafce3-pg93', '3.6.1-1', 'linux64, osx64, win64',  1, '20171104', 'pg93');

INSERT INTO versions VALUES ('orafce3-pg96', '3.4.0-1', 'linux64, osx64, win64',  0, '20170713', 'pg96');
INSERT INTO versions VALUES ('orafce3-pg95', '3.4.0-1', 'linux64, osx64, win64',  0, '20170713', 'pg95');
INSERT INTO versions VALUES ('orafce3-pg94', '3.4.0-1', 'linux64, osx64, win64',  0, '20170713', 'pg94');
INSERT INTO versions VALUES ('orafce3-pg93', '3.4.0-1', 'linux64, osx64, win64',  0, '20170713', 'pg93');
INSERT INTO versions VALUES ('orafce3-pg92', '3.4.0-1', 'linux64, osx64, win64',  0, '20170713', 'pg92');

INSERT INTO versions VALUES ('orafce3-pg96', '3.3.1-1', 'linux64, osx64, win64',  0, '20160923', 'pg96');
INSERT INTO versions VALUES ('orafce3-pg95', '3.3.1-1', 'linux64, osx64, win64',  0, '20160923', 'pg95');
INSERT INTO versions VALUES ('orafce3-pg94', '3.3.1-1', 'linux64, osx64, win64',  0, '20160923', 'pg94');
INSERT INTO versions VALUES ('orafce3-pg93', '3.3.1-1', 'linux64, osx64, win64',  0, '20160923', 'pg93');
INSERT INTO versions VALUES ('orafce3-pg92', '3.3.1-1', 'linux64, osx64, win64',  0, '20160923', 'pg92');

INSERT INTO versions VALUES ('orafce3-pg96', '3.3.0-1', 'linux64, osx64, win64',  0, '20160901', 'pg96');
INSERT INTO versions VALUES ('orafce3-pg95', '3.3.0-1', 'linux64, osx64, win64',  0, '20160701', 'pg95');
INSERT INTO versions VALUES ('orafce3-pg94', '3.3.0-1', 'linux64, osx64, win64',  0, '20160701', 'pg94');
INSERT INTO versions VALUES ('orafce3-pg93', '3.3.0-1', 'linux64, osx64, win64',  0, '20160701', 'pg93');
INSERT INTO versions VALUES ('orafce3-pg92', '3.3.0-1', 'linux64, osx64, win64',  0, '20160701', 'pg92');

INSERT INTO versions VALUES ('plv814-pg96', '1.4.8-1', 'linux64',  1, '20160901', 'pg96');
INSERT INTO versions VALUES ('plv814-pg95', '1.4.8-1', 'linux64',  1, '20160701', 'pg95');
INSERT INTO versions VALUES ('plv814-pg94', '1.4.8-1', 'linux64',  1, '20160701', 'pg94');
INSERT INTO versions VALUES ('plv814-pg93', '1.4.8-1', 'linux64',  1, '20160701', 'pg93');
INSERT INTO versions VALUES ('plv814-pg92', '1.4.8-1', 'linux64',  1, '20160701', 'pg92');

INSERT INTO versions VALUES ('plv815-pg96', '1.5.3-1', 'osx64',  1, '20160701', 'pg96');
INSERT INTO versions VALUES ('plv815-pg95', '1.5.3-1', 'osx64',  1, '20160701', 'pg95');
INSERT INTO versions VALUES ('plv815-pg94', '1.5.3-1', 'osx64',  1, '20160701', 'pg94');
INSERT INTO versions VALUES ('plv815-pg93', '1.5.3-1', 'osx64',  1, '20160701', 'pg93');
INSERT INTO versions VALUES ('plv815-pg92', '1.5.3-1', 'osx64',  1, '20160701', 'pg92');

INSERT INTO versions VALUES ('pljava15-pg95', '1.5.0-1', 'linux64, osx64, win64',  1, '20160701', 'pg95');
INSERT INTO versions VALUES ('pljava15-pg94', '1.5.0-1', 'linux64, osx64, win64',  1, '20160701', 'pg94');
INSERT INTO versions VALUES ('pljava15-pg93', '1.5.0-1', 'linux64, osx64, win64',  1, '20160701', 'pg93');
INSERT INTO versions VALUES ('pljava15-pg92', '1.5.0-1', 'linux64, osx64, win64',  1, '20160701', 'pg92');

INSERT INTO versions VALUES ('pgtsql9-pg95', '9.5-1', 'linux64, osx64, win64',  1, '20160701', 'pg95');
INSERT INTO versions VALUES ('pgtsql9-pg94', '9.5-1', 'linux64, osx64, win64',  1, '20160701', 'pg94');

INSERT INTO versions VALUES ('hintplan-pg96', '1.2.2-1', 'linux64', 1, '20170802', 'pg96');
INSERT INTO versions VALUES ('hintplan-pg95', '1.1.5-1', 'linux64', 1, '20170802', 'pg95');
INSERT INTO versions VALUES ('hintplan-pg94', '1.1.5-1', 'linux64', 1, '20170802', 'pg94');
INSERT INTO versions VALUES ('hintplan-pg93', '1.1.5-1', 'linux64', 1, '20170802', 'pg93');

INSERT INTO versions VALUES ('postgis24-pg10', '2.4.1-1',   'linux64, osx64, win64', 1, '20171104', 'pg10');
INSERT INTO versions VALUES ('postgis24-pg10', '2.4.0-1',   'linux64, osx64, win64', 0, '20171004', 'pg10');

INSERT INTO versions VALUES ('postgis23-pg96', '2.3.3-2', 'linux64, osx64, win64', 1, '20170731', 'pg96');
INSERT INTO versions VALUES ('postgis23-pg96', '2.3.3-1', 'linux64, osx64, win64', 0, '20170713', 'pg96');
INSERT INTO versions VALUES ('postgis23-pg96', '2.3.2-5', 'linux64, osx64, win64', 0, '20170616', 'pg96');

INSERT INTO versions VALUES ('postgis22-pg95', '2.2.5-1', 'linux64, osx64, win64', 1, '20170209', 'pg95');
INSERT INTO versions VALUES ('postgis22-pg95', '2.2.4-1', 'linux64, osx64, win64', 0, '20161214', 'pg95');

INSERT INTO versions VALUES ('postgis22-pg94', '2.2.5-1', 'linux64, osx64, win64', 1, '20170209', 'pg94');
INSERT INTO versions VALUES ('postgis22-pg94', '2.2.4-1', 'linux64, osx64, win64', 0, '20161214', 'pg94');

INSERT INTO versions VALUES ('postgis22-pg93', '2.2.5-1', 'linux64, osx64, win64', 1, '20170209', 'pg93');
INSERT INTO versions VALUES ('postgis22-pg93', '2.2.4-1', 'linux64, osx64, win64', 0, '20161214', 'pg93');

INSERT INTO versions VALUES ('postgis22-pg92', '2.2.5-1', 'linux64, osx64, win64', 1, '20170209', 'pg92');
INSERT INTO versions VALUES ('postgis22-pg92', '2.2.4-1', 'linux64, osx64, win64', 0, '20161214', 'pg92');

INSERT INTO versions VALUES ('pgdg96', '9.6-1', 'linux64', 0, '20160929', '');
INSERT INTO versions VALUES ('pgdg95', '9.5-1', 'linux64', 0, '20160107', '');
INSERT INTO versions VALUES ('pgdg94', '9.4-1', 'linux64', 0, '20141218', '');
INSERT INTO versions VALUES ('pgdg93', '9.3-1', 'linux64', 0, '20130909', '');
INSERT INTO versions VALUES ('pgdg92', '9.2-1', 'linux64', 0, '20120910', '');

INSERT INTO versions VALUES ('pg10', '10.1-1',     'linux64, osx64, win64', 1, '20171109', '');
INSERT INTO versions VALUES ('pg10', '10.0-1',     'linux64, osx64, win64', 0, '20171005', '');

INSERT INTO versions VALUES ('pg96', '9.6.6-1',    'linux64, osx64, win64', 1, '20171109', '');
INSERT INTO versions VALUES ('pg96', '9.6.5-1',    'linux64, osx64, win64', 0, '20170831', '');
INSERT INTO versions VALUES ('pg96', '9.6.4-1',    'linux64, osx64, win64', 0, '20170810', '');

INSERT INTO versions VALUES ('pg95', '9.5.10-1',   'linux64, osx64, win64', 1, '20171109', '');
INSERT INTO versions VALUES ('pg95', '9.5.9-1',    'linux64, osx64, win64', 0, '20170831', '');
INSERT INTO versions VALUES ('pg95', '9.5.8-1',    'linux64, osx64, win64', 0, '20170810', '');

INSERT INTO versions VALUES ('pg94', '9.4.15-1',   'linux64, osx64, win64', 1, '20171109', '');
INSERT INTO versions VALUES ('pg94', '9.4.14-1',   'linux64, osx64, win64', 0, '20170831', '');
INSERT INTO versions VALUES ('pg94', '9.4.13-1',   'linux64, osx64, win64', 0, '20170810', '');

INSERT INTO versions VALUES ('pg93', '9.3.20-1',   'linux64, osx64, win64', 1, '20171109', '');
INSERT INTO versions VALUES ('pg93', '9.3.19-1',   'linux64, osx64, win64', 0, '20170831', '');
INSERT INTO versions VALUES ('pg93', '9.3.18-1',   'linux64, osx64, win64', 0, '20170810', '');

INSERT INTO versions VALUES ('pg92', '9.2.24-1',   'linux64, osx64, win64', 0, '20171109', '');
INSERT INTO versions VALUES ('pg92', '9.2.23-1',   'linux64, osx64, win64', 0, '20170831', '');
INSERT INTO versions VALUES ('pg92', '9.2.22-1',   'linux64, osx64, win64', 0, '20170810', '');

INSERT INTO versions VALUES ('python2',    '2.7.11-4',    'win64',  1, '20160118', '');

INSERT INTO versions VALUES ('perl5',      '5.20.3.3',     'win64',  1, '20160314', '');

INSERT INTO versions VALUES ('tcl86',      '8.6.4-1',     'win64',    1, '20160311', '');

INSERT INTO versions VALUES ('java8',      '8u151',        'linux64, osx64, win64', 1, '20171104', '');
INSERT INTO versions VALUES ('java8',      '8u121',        'linux64, osx64, win64', 0, '20170209', '');

INSERT INTO versions VALUES ('pgha3',     '3.1.3.4',    '',                 1, '20171012', '');

INSERT INTO versions VALUES ('vagrant',   '1.9.8',      'osx64',            1, '20171012', '');
INSERT INTO versions VALUES ('virtualbox', '5.1.26',    'osx64',            1, '20171012', '');

INSERT INTO versions VALUES ('pgagent',   '3.4.1-1',    'win64', 1, '20170223', '');

INSERT INTO versions VALUES ('pgadmin3',   '1.23.0b',   'win64, osx64',     1, '20170608', '');
INSERT INTO versions VALUES ('pgadmin3',   '1.23.0a',   'win64, osx64',     0, '20161020', '');
INSERT INTO versions VALUES ('pgadmin3',   '1.22.1',    'win64, osx64',     0, '20160314', '');

INSERT INTO versions VALUES ('pgadmin4',   '1.6',       'win64, osx64',     0, '20170731', '');
INSERT INTO versions VALUES ('pgadmin4',   '1.3',       'win64, osx64',     0, '20170323', '');

INSERT INTO versions VALUES ('pgstudio2',  '2.0.1-2',        '',       0, '20160323', '');

INSERT INTO versions VALUES ('ora2pg',     '18.2',         '',     1, '20170918', '');
INSERT INTO versions VALUES ('ora2pg',     '18.1',         '',     0, '20170323', '');

INSERT INTO versions VALUES ('pgbouncer17',  '1.7.2-1b',   'linux64', 1, '20170209', '');

INSERT INTO versions VALUES ('collectd5',    '5.7.2-1',    'linux64', 1, '20171012', '');

INSERT INTO versions VALUES ('grafana',      '4.5.2',      'linux64', 1, '20171012', '');

INSERT INTO versions VALUES ('influxdb',     '1.3.6',      'linux64', 1, '20171012', '');
INSERT INTO versions VALUES ('kapacitor',    '1.3.1',      'linux64', 0, '20171025', '');
INSERT INTO versions VALUES ('chronograf',   '1.3.8.3',    'linux64', 0, '20171025', '');
INSERT INTO versions VALUES ('telegraf',     '1.4.1',      'linux64', 0, '20171025', '');

INSERT INTO versions VALUES ('elasticsearch','5.6.3',      'linux64', 1, '20171025', '');
INSERT INTO versions VALUES ('logstash',     '5.6.3',      'linux64', 1, '20171025', '');
INSERT INTO versions VALUES ('filebeat',     '5.6.3',      'linux64', 1, '20171025', '');
INSERT INTO versions VALUES ('kibana',       '5.6.3',      'linux64', 1, '20171025', '');

INSERT INTO versions VALUES ('rundeck',      '2.9.3',      '',        1, '20171012', '');

INSERT INTO versions VALUES ('consul',       '0.9.0',      'linux64, osx64, win64', 1, '20170802', '');

INSERT INTO versions VALUES ('gpdb5',        '5.1.0-1',    'linux64', 1, '20171104', '');
INSERT INTO versions VALUES ('gpdb5',        '5.0.0-1',    'linux64', 0, '20171025', '');

INSERT INTO versions VALUES ('pgbadger',   '9.2',          '',             1, '20170731', '');
INSERT INTO versions VALUES ('pgbadger',   '9.1',          '',             0, '20170209', '');

INSERT INTO versions VALUES ('backrest', '1.25', '', 1, '20171104', '');
INSERT INTO versions VALUES ('backrest', '1.24', '', 0, '20171012', '');
INSERT INTO versions VALUES ('backrest', '1.23', '', 0, '20170918', '');

INSERT INTO versions VALUES ('cassandra30',  '3.0.8',      '',             1, '20160901', '');

INSERT INTO versions VALUES ('tomcat8',      '8.5.4',      '',             1, '20160901', '');

INSERT INTO versions VALUES ('spark20',  '2.0.1',          '',             1, '20161108', '');
INSERT INTO versions VALUES ('spark16',  '1.6.1',          '',             0, '20160316', '');

INSERT INTO versions VALUES ('hadoop26',  '2.6.5',         '',             1, '20161108', '');

INSERT INTO versions VALUES ('hive21', '2.1.0',            '',             0, '20161108', '');
INSERT INTO versions VALUES ('hive2',  '2.0.1',            '',             1, '20160616', '');

INSERT INTO versions VALUES ('zookeeper34',  '3.4.8',      '',             1, '20160330', '');
