# -*- encoding: utf-8 -*-
"""
    flask_triangle
    --------------

    Flask-Triangle is utterly influenced by Flask-WTF. It aims to provide you
    with similar features : form input handling and validation but with
    AngularJS and XHR in mind.

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import

from .triangle import Triangle
from .form import Form
from .widget import Widget

__all__ = ['Triangle', 'Form', 'Widget']
