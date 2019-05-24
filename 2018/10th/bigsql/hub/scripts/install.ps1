$PGC_VER = "3.3.4"
$PGC_REPO = "https://s3.amazonaws.com/pgcentral"


function pgcDownloadFile ([string]$pURL, [string]$pFile) {
  $wc = New-Object System.Net.WebClient
  try {

    $wc.DownloadFile($pURL, $pFile)
  } catch [System.Net.WebException] {
    if ($_.Exception.InnerException.Message) {
      write-host($($_.Exception.InnerException.Message))
    } else {
      write-host($($_.Exception.Message))
    }
    exit 1
  }

}


function pgcUnzipFile ([string]$pFile) {
  $shell = New-Object -ComObject shell.application
  try {
    $zip = $shell.NameSpace($pFile)
    foreach ($item in $zip.items()) {
      ## overlay each file with prompting
      $shell.NameSpace($cwd).CopyHere($item, 0x14)
    }
  } catch {
    write-host($($_.Exception.Message))
    write-host("FATAL ERROR: unzipping")
    exit 1
  }
}


######################################################################
######                        MAINLINE                          ######
######################################################################

$cwd = (Get-Item -Path ".\" -Verbose).FullName
$bigsql_dir = ($($cwd) + "\bigsql")
$is_bigsql_exists = (Test-Path ($bigsql_dir))
if ($is_bigsql_exists){
  write-host("ERROR: Cannot install over an existing 'bigsql' directory.")
  exit 1
}

$envVER = $env:PGC_VER
if ($envVER) {
  write-host("Using PGC_VER Environment variable: " + $($envVER))
  $PGC_VER = $envVER
}

$envREPO = $env:PGC_REPO
if ($envREPO) {
  write-host("Using PGC_REPO Environment variable: " + $($envREPO))
  $PGC_REPO = $envREPO
}


$pgcFile = ("bigsql-pgc-" + $($PGC_VER) + ".zip")

$url = ($($PGC_REPO) + "/" + $($pgcFile))


write-host $("`r`n" + "Downloading BigSQL PGC " + $($PGC_VER) + " ...")
pgcDownloadFile $url $pgcFile

write-host $("`r`n" + "Unpacking ...")
$qualifiedFile = $($cwd) + "\" + $pgcFile
pgcUnzipFile $qualifiedFile
Remove-Item $qualifiedFile


$pgc_cmd = "bigsql\pgc"

write-host("`r`n" + "Setting REPO to " + $($PGC_REPO))
$repoCmd = $($pgc_cmd) + " set GLOBAL REPO " + $($PGC_REPO)
Invoke-Expression -Command:$repoCmd

write-host("`r`n" + "Updating Metadata")
$updateCmd = $($pgc_cmd) + " update --silent"
Invoke-Expression -Command:$updateCmd

$rc = $LastExitCode
if (-not $rc) {
  write-host $("`r`n" + "BigSQL PGC installed.")
  write-host $("  Try '" + $($pgc_cmd) + " help' to get started." + "`r`n")
}

exit $rc
