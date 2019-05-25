# -*- encoding: utf-8 -*-
"""
    tests.html
    ----------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from nose.tools import assert_true, assert_equal

from flask_triangle.helpers import HTMLString


class TestHtml(object):

    def setup(self):

        self.html = HTMLString(u'hello')

    def test_0(self):
        """An HTMLString object is an unicode subtype"""
        assert_true(isinstance(self.html, unicode))

    def test_1(self):
        """An HTMLString object is __html__."""
        assert_true(hasattr(self.html, '__html__'))

    def test_2(self):
        """The __html__ method return the unicode string."""
        assert_equal(self.html.__html__(), self.html)
