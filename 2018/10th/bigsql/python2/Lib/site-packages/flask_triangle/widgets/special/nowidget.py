# -*- encoding: utf-8 -*-
"""
    flask_triangle.widgets.special.nowidget
    ---------------------------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

import flask_triangle
from flask_triangle.widget import Widget


class Nowidget(Widget):
    """
    A special component to add a property in the schema with no corresponding
    item to insert in the HTML form.
    """

    schema = {'type': 'string'}
    html_template = ''

    def __customize__(self, required=False):

        if required is not False:
            self.modifiers.append(flask_triangle.modifiers.Required(required))
