# -*- encoding: utf-8 -*-
"""
    flask_triangle.jinja
    --------------------

    A set of features for Jinja2 to facilitate AngularJS integration in your
    templates.

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

from jinja2 import evalcontextfilter, Undefined, is_undefined
from .widget import Widget


class TriangleUndefined(Undefined):
    """
    Implements a specific `Undefined` class.

    If a variable in your Jinja2 template does not exists, trying to access a
    property will raise an `AttributeError`. This class implements a mechanism
    to prevent this behavior and return a `TriangleUndefined` object when you
    try to access a property of a `TriangleUndefined` object.

    The full name of a property is stored to keep a track of it :

        >>> test = TriangleUndefined(name=test) # this is how it's initialized
        >>> test.demo.for.the.documentation
        Undefined
        >>> test.demo.for.the.documentation._undefined_name
        u'test.demo.for.the.documentation'

    This "magic" class is used by the `angular_filter` to let the user easily
    define its client side template.
    """

    def __getattr__(self, name):
        if name[1] == '_':
            raise AttributeError(name)
        return TriangleUndefined(name='{}.{}'.format(self._undefined_name,
                                                     name))


def angular_filter(value):
    """
    A filter to tell Jinja2 that a variable is for the AngularJS template
    engine. 

    If the variable is undefined, its name will be used in the AngularJS
    template, otherwise, its content will be used.
    """

    if is_undefined(value):
        return '{{{{{}}}}}'.format(value._undefined_name)
    if type(value) is bool:
        value = unicode(value).lower()
    return '{{{{{}}}}}'.format(value)


def widget_test(obj, instance=Widget.__name__):
    """
    Test if a variable is a `Widget` instance.
    """

    if not isinstance(obj, Widget):
        return False

    cls = [obj.__class__]
    while Widget not in cls:
        cls += list(cls[-1].__bases__)

    return instance in [i.__name__ for i in cls]
