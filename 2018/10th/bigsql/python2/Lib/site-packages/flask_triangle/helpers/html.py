# -*- encoding: utf-8 -*-
"""
    flask_triangle.helpers.html
    ---------------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import


class HTMLString(unicode):
    """
    A `unicode` string considered as sage HTML.
    """
    def __html__(self):
        return self
