# -*- encoding: utf-8 -*-
"""
flask_triangle.
"""


from __future__ import absolute_import
from ..modifier import Modifier


class Regexp(Modifier):
    """
    Adds a regular expresion constraint to an input.

    ..note: This validator does not support string format.
    """

    def __init__(self, regexp, client=True):
        """
        :arg regexp: A ``regular expression``. The result must match the
        regular expression to be valid.

        See `Angular's input API` for more detail.

        .. _`Angular's input API`:
            http://docs.angularjs.org/api/ng.directive:input
        """
        self.client = client
        self.regexp = regexp

    @property
    def attributes(self):
        if not self.client:
            return dict()
        res = self.regexp.replace(u'{', u'{{').replace(u'}', u'}}')
        return {u'data-ng-pattern': u'/{}/'.format(res)}

    def alter_schema(self, schema, fqn):
        if schema[u'type'] != u'object':
            schema[u'pattern'] = self.regexp


class PatternProperty(Modifier):
    """
    Adds an intermediate asPatternProperty field on object in a JSON schema
    based on their FQN and an argument list.

    The list give a value for each level of the FQN of a variable. If it
    differs from the level name set in the JSON schema, then the
    'asPatternProperty' value is set with the argument.

    For example, if a FQN is my.nested.value and the PatternProperty validator
    is called with the following : ``PatternProperty('my', r'^[A-Z]*')`` then
    the object named 'nested' in the JSON schema will have a asPatterProperty
    set to r'^[A-Z]*' which ultimately will result in removing the nested
    object from the properties of the parent object and move it to the 
    patternProperties with the given regex to validate the name for it.
    """

    def __init__(self, *args):
        self.args = args
        self.attributes = {}

    def alter_schema(self, schema, fqn):

        # the root object cannot be a pattern property
        if not fqn:
            return

        reference = fqn.split(u'.')

        if len(self.args) < len(reference):
            return

        current_property = self.args[len(reference)-1]
        if reference[-1] != u'*' and current_property != reference[-1]:
            schema[u'asPatternProperty'] = current_property
