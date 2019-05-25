# -*- encoding: utf-8 -*-
"""
    flask_triangle.modifiers.limits
    -------------------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

from flask_triangle.modifier import Modifier


class Minimum(Modifier):
    """
    Add a minimum value validation.
    """

    def __init__(self, value):
        """
        """
        self.value = value

    @property
    def attributes(self):
        return {'min': self.value}

    def alter_schema(self, schema, fqn):
        if schema['type'] != 'object':
            schema['minimum'] = self.value


class Maximum(Modifier):
    """
    Add a maximum value validation.
    """

    def __init__(self, value):
        """
        """
        self.value = value

    @property
    def attributes(self):
        return {'max': self.value}

    def alter_schema(self, schema, fqn):
        if schema['type'] != 'object':
            schema['maximum'] = self.value


class MinimumLength(Modifier):
    """
    Add a minimum value validation.
    """

    def __init__(self, value):
        """
        """
        self.value = value

    @property
    def attributes(self):
        return {'data-ng-minlength': self.value}

    def alter_schema(self, schema, fqn):
        if schema['type'] != 'object':
            schema['minLength'] = self.value


class MaximumLength(Modifier):
    """
    Add a maximum value validation.
    """

    def __init__(self, value):
        """
        """
        self.value = value

    @property
    def attributes(self):
        return {'data-ng-maxlength': self.value}

    def alter_schema(self, schema, fqn):
        if schema['type'] != 'object':
            schema['maxLength'] = self.value
