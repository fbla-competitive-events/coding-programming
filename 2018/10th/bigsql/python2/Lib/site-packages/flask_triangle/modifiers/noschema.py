# -*- encoding: utf-8 -*-
"""
flask_triangle.
"""


from __future__ import absolute_import
from ..modifier import Modifier


class NoSchema(Modifier):
    """
    """

    def __init__(self, condition=True):
        """
        """
        self.attributes = {}

    def alter_schema(self, schema, fqn):

        for k in schema.keys():
            del schema[k]
        return True
