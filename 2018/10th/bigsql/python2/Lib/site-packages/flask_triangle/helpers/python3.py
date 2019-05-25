# -*- encoding: utf-8 -*-
"""
    helpers.python3
    ---------------

    An helper to facilitate the transition to Python 3.

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

import sys


class UnicodeMixin(object):
    # A workaround for an easier 2to3 migration.
    if sys.version_info > (3, 0):
        __str__ = lambda x: x.__unicode__()
    else:
        __str__ = lambda x: unicode(x).encode('utf8')
