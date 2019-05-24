##################################################################
########    Copyright (c) 2015-2017 OpenSCG        ###############
##################################################################

import sys, os
PGC_VER="3.3.4"
PGC_REPO=os.getenv("PGC_REPO", "https://s3.amazonaws.com/pgcentral")

env_ver_pgc = os.getenv("PGC_VER", None)
if env_ver_pgc:
  PGC_VER = env_ver_pgc
  print("Using PGC_VER Environment Variable: " + PGC_VER)
  
if sys.version_info < (2, 6):
  print("ERROR: BigSQL requires Python 2.6 or greater")
  sys.exit(1)

try:
  # For Python 3.0 and later
  from urllib import request as urllib2
except ImportError:
  # Fall back to Python 2's urllib2
  import urllib2

import tarfile

IS_64BITS = sys.maxsize > 2**32
if not IS_64BITS:
  print("ERROR: This is a 32bit machine and BigSQL packages are 64bit.")
  sys.exit(1)

if os.path.exists("bigsql"):
  print("ERROR: Cannot install over an existing 'bigsql' directory.")
  sys.exit(1)

pgc_file="bigsql-pgc-" + PGC_VER + ".tar.bz2"
f = PGC_REPO + "/" + pgc_file

if not os.path.exists(pgc_file):
  print("\nDownloading BigSQL PGC " + PGC_VER + " ...")
  try:
    fu = urllib2.urlopen(f)
    local_file = open(pgc_file, "wb")
    local_file.write(fu.read())
    local_file.close()
  except Exception as e:
    print("ERROR: Unable to download " + f + "\n" + str(e))
    sys.exit(1)

print("\nUnpacking ...")
try:
  tar = tarfile.open(pgc_file)
  tar.extractall(path=".")
  print("\nCleaning up")
  tar.close()
  os.remove(pgc_file)
except Exception as e:
  print("ERROR: Unable to unpack \n" + str(e))
  sys.exit(1)

print("\nSetting REPO to " + PGC_REPO)
pgc_cmd = "bigsql" + os.sep + "pgc"
os.system(pgc_cmd + " set GLOBAL REPO " + PGC_REPO)

if not env_ver_pgc:
  print("\nUpdating Metadata")
  os.system(pgc_cmd + " update --silent")

print("\nBigSQL PGC installed.  Try '" + pgc_cmd + " help' to get started.\n")

sys.exit(0)

