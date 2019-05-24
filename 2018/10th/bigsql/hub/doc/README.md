PGCLI is a PrettyGoodCommandLIne interface for component control and management.  

## Usage ##
```
#!
pgc command [component] [options]
```

## Informational Commands ##
```
#!
  help      - Display help file
  info      - Display OS or component information
  status    - Display status of installed server components
  list      - Display available/installed components 
```

## Service Control Commands ##
```
#!
  start     - Start server components
  stop      - Stop server components
  reload    - Reload server configuration files (without a restart)
  restart   - Stop & then start server components
  enable    - Enable a component
  disable   - Disable a server server component from starting automatically
  config    - Configure a component
  init      - Initialize a component
```

## Software Install & Update Commands ##
```
#!
  update    - Retrieve new lists of components
  upgrade   - Perform an upgrade of a component
  install   - Install (or re-install) a component  
  remove    - Un-install component   
  download  - Download a component archive file (but don't install it)
  clean     - Delete downloaded component files
```

## Advanced Internal Commands ##
```
#!
  top        - Cross platform version of the "top" command 
  get        - Retrieve a setting
  set        - Populate a setting
  unset      - Remove a setting 
  register   - Create a HOST, GROUP or REPO meta data entry
  unregister - Delete a HOST, GROUP or REPO meta data entry
  discover   - Gather info on installed PGDG instances
  repolist   - List PGDG repositories
  repo-pkgs  - Work with packages in a PGDG repository
  default    - Get, set or list default settings by group & user
  lablist    - List labs
  dblist     - List databases
  dbdump     - Backup a database
  dbrestore  - Restore a database
  dbstat     - Realtime stats from a running database
  dbtune     - Tune a database
  dbconfig   - Update database configuration settings
  create     - Create a database or virtual host
  cloud      - Hybrid Cloud controller for AWS, AZURE, VMware ...
```
