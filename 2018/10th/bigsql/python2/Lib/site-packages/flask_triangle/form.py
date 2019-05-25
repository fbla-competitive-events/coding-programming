# -*- encoding: utf-8 -*-
"""
    flask_triangle.form
    -------------------

    Implements the Form class.

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import

import copy
import flask

from .widget import Widget
from .schema import Schema
from .triangle import json_validate
from .helpers import HTMLString


class FormBase(type):
    """Metaclass for a Form object"""

    def __new__(mcs, name, bases, attrs):

        super_new = super(FormBase, mcs).__new__

        if name == 'NewBase' and attrs == {}:
            return super_new(mcs, name, bases, attrs)
        parents = [b for b in bases if isinstance(b, FormBase) and
                   not (b.__name__ == 'NewBase' and b.__mro__ == (b, object))]
        if not parents:
            return super_new(mcs, name, bases, attrs)

        module = attrs.pop('__module__')
        new_class = super_new(mcs, name, bases, {'__module__': module})

        new_class._form_widget_list = copy.deepcopy(new_class._form_widget_list)

        new_widgets = list()
        for obj_name, obj in attrs.items():
            setattr(new_class, obj_name, obj)
            if isinstance(obj, Widget):

                # use the attribute name as the widget's name
                if obj.name is None:
                    obj.name = obj_name

                # for each widgets found, add the new ones
                if obj_name not in new_class._form_widget_list:
                    new_widgets.append(obj_name)

            elif obj_name in new_class._form_widget_list:
                # if the attribute is not a widget but there was a former
                # attribute which was a widget, removes it from the list.
                new_class._form_widget_list.remove(obj_name)

        new_widgets.sort(key=lambda k: getattr(new_class, k).instance_counter)
        new_class._form_widget_list += new_widgets

        return new_class


class Form(object):
    """
    The Form acts as a container for multiple Widgets.
    """

    __metaclass__ = FormBase
    _form_widget_list = list()

    def __init__(self, name, schema=None, strict=True, root=None):
        """
        :arg schema: A ``dict``. A custom schema to describe how-to validate
        resulting JSON from this form.

        :arg root: A ``string``. The name of the properties to use as
        root of the JSON schema.
        """

        if schema is not None:
            self.schema = Schema(schema)
        else:
            self.schema = Schema()

            for widget in self:
                #print widget, widget.schema
                self.schema.merge(widget.schema)

        self.schema.compile(strict)

        if root is not None:
            self.schema = self.schema.get('properties').get(root, self.schema)

        self.name = name

    @property
    def validate(self):
        """
        Return a function decorator to validate JSON in the current request.
        """
        return json_validate(self.schema)

    def __iter__(self):
        return (getattr(self, obj_name) for obj_name in self._form_widget_list)
