#-*- encoding: utf-8 -*-
"""
    flask_triangle.triangle
    -----------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

from flask import request, abort
from jsonschema import validate, SchemaError, ValidationError

from .jinja import angular_filter, widget_test, TriangleUndefined


def json_validate(schema):
    """
    A decorator to automatically handle JSON validation sent as payload.

    This decorator must be used on function triggered by a registered route
    in Flask. If the HTTP request is not valid it will raise an HTTP 415
    error. If the validation schema is not valid it will raise an HTTP 500
    error and finally, if the sent data is not valid an HTTP 400 error is
    raised.

    :arg schema: A json-schema dict to validate data against it.
    """
    def decorator(func):
        def wrapfunc(*args, **kwargs):
            if request.json is None:
                abort(415, u'Unsuported Media Type.'
                           u'Content-Type must by application/json')

            try:
                validate(request.json, schema)
            except SchemaError:
                abort(500)
            except ValidationError:
                abort(400, u'Bad Request. '
                           u'Sent JSON data is not valid.')
            return func(*args, **kwargs)
        return wrapfunc

    return decorator


class Triangle(object):
    """
    Central controller class that can be used to configure how Flask-Triangle
    behaves. Each application that wants to use Flask-Triangle has to create,
    or run :meth:`init_app` on, an instance of this class after the
    configuration was initialized.
    """

    def __init__(self, app=None):
        if app is not None:
            self.init_app(app)

        self.validate = json_validate

    def init_app(self, app):
        """
        Set up this instance for use with *app*, if no app was passed to the
        constructor.
        """

        # set the Jinja2 environment
        app.jinja_env.undefined = TriangleUndefined
        app.jinja_env.filters['angular'] = angular_filter
        app.jinja_env.tests['widget'] = widget_test
