# -*- encoding: utf-8 -*-
"""
    flask_triangle.modifiers.multiple
    ---------------------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from flask_triangle.schema import Schema
from ..modifier import Modifier


class Multiple(Modifier):
    """
    Add a constraint allowing more than one entry to be selected in a `select`
    widget. This will affect the attributes of HTML widget and the validation
    json schema altogether. However, this does not set client validation at
    the moment.
    """

    def __init__(self, min_items=None, max_items=None):
        """
        :arg min: a number. The minimum number of selected elements in the 
        list to succeed validation. If set to None, there is no minimum.
        :arg max: a number. The maximum number of selected elements in the
        list to succeed validation. If set to None, there is no maximum.
        """
        self.attributes = {u'multiple': None}
        self.min_items = min_items
        self.max_items = max_items

    def alter_schema(self, schema, fqn):

        if schema[u'type'] != u'object':

            # some properties must be moved from the array object to the item
            # object :
            items = Schema(schema)
            # clean some uneeded value in items
            for k in items.keys():
                if k in ['asPatternProperty', 'required']:
                    del items[k]

            # delete migrated values
            for k in schema.keys():
                if k in items:
                    del schema[k]

            schema[u'type'] = u'array'
            schema[u'items'] = items
            if self.min_items is not None:
                schema[u'minItems'] = self.min_items
            if self.max_items is not None:
                schema[u'maxItems'] = self.max_items

