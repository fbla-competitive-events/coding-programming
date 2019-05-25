# -*- encoding: utf-8 -*-
"""
    flask_triangle.widgets.core
    ---------------------------

    Implement the base widgets of HTML5 supported by AngularJS.

    * input

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

from flask_triangle.widget import Widget
import flask_triangle.modifiers


class Input(Widget):
    """
    HTML input element control with angular data-binding. Input control follows
    HTML5 input types and polyfills the HTML5 validation behavior for older
    browsers.
    """

    schema = {'type': 'string'}

    html_template = '<input {{widget.html_attributes}}></input>'

    def __customize__(self, required=False, min_length=None, max_length=None,
                      pattern=None, change=None):

        if required is not False:
            self.modifiers.append(flask_triangle.modifiers.Required(required))
        if min_length is not None:
            self.modifiers.append(flask_triangle.modifiers.MinimumLength(min_length))
        if max_length is not None:
            self.modifiers.append(flask_triangle.modifiers.MaximumLength(max_length))
        if pattern is not None:
            self.modifiers.append(flask_triangle.modifiers.Regexp(pattern))
        if change is not None:
            self.html_attributes['data-ng-change'] = change


class TextInput(Input):
    """
    Standard HTML text input with angular data binding.
    """

    def __customize__(self, trim=True):
        self.html_attributes['type'] = 'text'

        if trim is not True and trim is not None:
            self.html_attributes['data-ng-trim'] = trim


class PasswordInput(Input):
    """
    Standard HTML password input with angular data binding.
    """

    def __customize__(self):
        self.html_attributes['type'] = 'password'


class EmailInput(Input):
    """
    Text input with email validation.
    """

    def __customize__(self, pattern=None):
        self.html_attributes['type'] = 'email'

        # the default server-side pattern is set only if no pattern is set by
        # the user. The default Input __customize__ deals with the pattern
        # property. 
        if pattern is None:
            val = Regexp(r'^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-.])*$',
                         client=False)
            self.modifiers.append(val)


class NumberInput(Input):
    """
    Text input with number validation and transformation.
    """

    schema = {'type': 'number'}

    def __customize__(self, min=None, max=None):
        self.html_attributes['type'] = 'number'
        if min is not None:
            self.modifiers.append(flask_triangle.modifiers.Minimum(min))
        if max is not None:
            self.modifiers.append(flask_triangle.modifiers.Maximum(max))


class Textarea(Input):
    """
    HTML textarea element control with angular data-binding.
    """
    html_template = '<textarea {{widget.html_attributes}}></textarea>'
