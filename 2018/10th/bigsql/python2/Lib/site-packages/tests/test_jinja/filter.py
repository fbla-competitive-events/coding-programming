# -*- encoding: utf-8 -*-
"""
    tests.jinja.filter
    ------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

import flask
from flask_triangle import Triangle
from nose.tools import assert_equal


# an helper
t = flask.render_template_string


class TestJinjaFilter(object):

    def setup(self):
        self.app = flask.Flask(__name__)
        triangle = Triangle(self.app)

    def test_defined_variable(self):
        """
        If the variable is defined in the template context, the angular filter
        will return a double curly bracket notation of the variable content for
        use by angularjs.
        """
        with self.app.test_request_context():
            assert_equal(t('{{test|angular}}', test='ok'), '{{ok}}')

    def test_undefined_variable(self):
        """
        If the variable is undefined in the template context, the angular
        filter will return a double curly bracket notation of the variable
        name.
        """
        with self.app.test_request_context():
            assert_equal(t('{{test|angular}}'), '{{test}}')

    def test_natural_type_0(self):
        """
        The filter works on natural types.
        """
        with self.app.test_request_context():
            assert_equal(t('{{9|angular}}'), '{{9}}')
            assert_equal(t('{{\'test\'|angular}}'), '{{test}}')

    def test_bool_exception(self):
        """
        The filter works on natural typed except for bool which are
        automatically lowercased for javascript compatibility.
        """
        with self.app.test_request_context():
            assert_equal(t('{{True|angular}}'), '{{true}}')
            assert_equal(t('{{False|angular}}'), '{{false}}')

    def test_undefined_nested_attributes(self):
        """
        If the filter is applied on undefined attributes returns the fully
        qualified name.
        """
        with self.app.test_request_context():
            assert_equal(t('{{test.demo|angular}}'), '{{test.demo}}')
