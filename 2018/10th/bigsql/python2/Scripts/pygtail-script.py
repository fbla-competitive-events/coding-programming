#!c:\python27\python.exe
# EASY-INSTALL-ENTRY-SCRIPT: 'pygtail==0.7.0','console_scripts','pygtail'
__requires__ = 'pygtail==0.7.0'
import sys
from pkg_resources import load_entry_point

if __name__ == '__main__':
    sys.exit(
        load_entry_point('pygtail==0.7.0', 'console_scripts', 'pygtail')()
    )
