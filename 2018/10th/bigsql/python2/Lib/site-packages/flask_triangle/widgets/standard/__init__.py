# -*- encoding: utf-8 -*-
"""
    flask_triangle.widgets.standard
    -------------------------------

    HTML5 standard widgets supported by AngularJS

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

from .text import Input, TextInput, NumberInput, Textarea
from .select import Select

__all__ = ['Input', 'TextInput', 'NumberInput', 'Textarea', 'Select']
