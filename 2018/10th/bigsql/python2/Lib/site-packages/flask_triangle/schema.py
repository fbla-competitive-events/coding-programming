# -*- encoding: utf-8 -*-
"""
    flaskey_triangle.schema
    ---------------------

    Implements Schema.

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

import collections
import copy


class Schema(dict):
    """
    This object is a custom dict with advanced methods to easily create a
    genuine JSON-schema. Flask-Triangle introduces specific properties which
    are compiled to standard JSON-schema through the call to the compile
    method of the Schema.
    """

    def __init__(self, init=None):
        self.dirty = True
        super(Schema, self).__init__(dict() if init is None else init)

    @staticmethod
    def __compile(schema, fqn):
        """
        This function is responsible of modifying the specific grammar used in
        Flask-Triangle to their expected definition in the standard JSON-schema
        grammar.

        Introduced keywords :
            - asPatternProperty : convert a standard property to a pattern
                                  property using its value as the regexp.
        """

        # anticipated return when dealing with pattern properties
        if fqn.endswith('*'):
            return

        pattern_properties = schema.get('patternProperties', dict())

        for key, val in schema.get('properties', dict()).items():
            pattern = val.get('asPatternProperty', None)
            if pattern is not None:
                pattern_properties[pattern] = val
                del val['asPatternProperty']
                del schema['properties'][key]
                if key in schema.get('required', list()):
                    schema['required'].remove(key)

        if len(pattern_properties):
            schema['patternProperties'] = pattern_properties

        if 'properties' in schema and not len(schema['properties']):
            del schema['properties']

        if 'required' in schema and not len(schema['required']):
            del schema['required']

    @staticmethod
    def __is_strict(schema, fqn):
        if schema.get('type') == 'object':
            schema['additionalProperties'] = False

    @staticmethod
    def __merge(d, u):
        """
        The effective implementation of the merge with a recursive method.
        """
        for key, val in u.iteritems():
            if isinstance(val, collections.Mapping) and \
                isinstance(d.get(key, None), collections.Mapping):
                d[key] = Schema.__merge(d.get(key, {}), val)
            elif type(val) is list and type(d.get(key, None)) is list:
                d[key] = list(set(d.get(key, []) + val))
            else:
                d[key] = copy.deepcopy(u[key])
        return d

    def compile(self, strict=False):
        """
        Compile the specific grammar subset of Flask-triangle to generate a
        genuine JSON-schema.
        """
        if self.dirty:
            if strict:
                self.apply_func(self.__is_strict)
            self.apply_func(self.__compile)
            self.dirty = False
        return self

    def merge(self, other):
        """
        A recursive merge of another dictionnary in this one.

        Unlike the update method, if two entries are of the same collection
        type, their values will be merged.
        """
        if other is not None:
            self.dirty = True
            Schema.__merge(self, other)


    def apply_func(self, func, fqn=''):
        """
        Call a custom function on each object nested in the schema. This
        function is allowed to transform the schema.

        :arg func: A custom function with the following signature :
                   ``my_func(schema, fqn)``. The first parameter will be the
                   processed object and the second its fully qualified name.
                   The FQN of the root object is an empty string.

        Apply_func will stop when an object is a leaf of the root schema or
        if the applied function return a boolean ``True`` value.
        """
        self.dirty = True

        if func(self, fqn):
            return self

        for key, val in self.get('properties', dict()).items():
            val.apply_func(func, '{}{}'.format(fqn, '.{}'.format(key)
                                                     if fqn else key))

        for key, val in self.get('patternProperties', dict()).items():
            val.apply_func(func, '{}{}'.format(fqn, '.*' if fqn else '*'))

        return self
