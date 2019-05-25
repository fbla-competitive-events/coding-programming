# -*- encoding: utf-8 -*-
"""
    flask_triangle.validators.type
    ------------------------------

    Validators to modify and verify the return type of a widget.

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals
from ..modifier import Modifier


class AsBoolean(Modifier):
    """
    The widget value could either be `true` or `false`.
    """

    def alter_schema(self, schema, fqn):
        if schema['type'] != 'object':
            schema['type'] = 'boolean'


class AsInteger(Modifier):
    """
    The widget value is an integer.
    """

    def alter_schema(self, schema, fqn):
        if schema['type'] != 'object':
            schema['type'] = 'integer'
