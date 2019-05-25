# -*- encoding: utf-8 -*-
"""
    tests.jinja.cond
    ----------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals


import flask
from flask_triangle import Triangle, Widget

from nose.tools import assert_equal


# an helper
t = flask.render_template_string


class DummyWidget(Widget):
    """
    An dummy widget for test purpose
    """
    pass


class TestJinjaTest(object):

    def setup(self):
        self.app = flask.Flask(__name__)
        triangle = Triangle(self.app)

    def test_is_a_widget(self):
        """The widget test returns true if the tested value is a widget."""
        with self.app.test_request_context():
            assert_equal(t('{{test is widget}}',
                            test=DummyWidget('test', name='test')), 'True')

    def test_is_a_specific_widget(self):
        """
        The widget test returns true if the tested value is of the same type of
        a widget.
        """
        with self.app.test_request_context():
            assert_equal(t('{{test is widget(\'DummyWidget\')}}',
                            test=DummyWidget('test', name='test')), 'True')

    def test_is_a_not_a_specific_widget(self):
        """
        The widget test returns false if the tested value is not of the same
        widget type.
        """
        with self.app.test_request_context():
            assert_equal(t('{{test is widget(\'OtherWidget\')}}',
                           test=DummyWidget('test', name='test')), 'False')

    def test_is_an_inherited_widget(self):
        """
        The widget test returns true if the tested value is a subclass widget.
        """
        with self.app.test_request_context():
            assert_equal(t('{{test is widget(\'Widget\')}}',
                           test=DummyWidget('test', name='test')), 'True')

    def test_is_not_widget(self):
        """The widget test returns false if the tested value isn't a widget."""
        with self.app.test_request_context():
            assert_equal(t('{{test is widget}}', test=1), 'False')
